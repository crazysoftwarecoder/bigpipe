package com.myseriousorganization.bigpipe.core.executor;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


import com.google.common.base.Preconditions;
import com.myseriousorganization.bigpipe.core.annotations.PageletTask;
import com.myseriousorganization.bigpipe.core.annotations.PageletTaskMethod;
import com.myseriousorganization.bigpipe.core.exception.TaskExecutionException;
import com.myseriousorganization.bigpipe.core.marker.ViewObject;
import com.myseriousorganization.bigpipe.core.threadlocal.PageletTaskOutputHolderTL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * <b>PageletTaskExecutor</b> orchestrates the execution of
 * @PageletTask's in a new thread and gathers all the <b>ViewObject</b>s
 */
public class PageletTaskExecutor {

	private static final Logger logger = LoggerFactory.getLogger(PageletTaskExecutor.class);

	private ExecutorService executorService = null;
	
	public PageletTaskExecutor(int noOfThreads) {
		this.executorService = Executors.newFixedThreadPool(noOfThreads);
	}
	
	public PageletTaskExecutor(ThreadPoolExecutor customExecutor) {
		this.executorService = customExecutor;
	}
	
	public PageletTaskExecutor() {
		// create as many threads as there are cores.
		this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	}
	
	public void execute(final Object pageletTask, final HttpServletRequest servletRequest) throws TaskExecutionException {
		Preconditions.checkArgument(pageletTask!=null);

		// Validate if this is a pagelet task.
		if (isPageletTask(pageletTask)) {
			throw new TaskExecutionException("passed in object is not a PageletTask. "
					+ "Please annotate your pagelet task class with @" + PageletTask.class.getCanonicalName());
		}
		
		final String pageletTaskName = getPageletTaskName(pageletTask);
		
		final PageletTaskOutputHolder outputHolder = PageletTaskOutputHolderTL.local.get();
		outputHolder.addPageletTask(pageletTaskName);
		
		final Method method = getMethodFromTask(pageletTask);

		executorService.execute(new Runnable() {
			public void run() {
				try {
					// Setting the Servlet request on the new thread.
					Object returnObject = method.invoke(pageletTask, new Object[] {servletRequest});
					// Send the return ViewObject packing off to the JSP pagelet snippets
					// that want it.
					outputHolder.putViewObject(pageletTaskName, (ViewObject) returnObject);
				}
				catch (InvocationTargetException | IllegalAccessException e) {
					logger.error("Error while invoking @PageletTaskMethod ("
					+ method.getDeclaringClass() + "." + method.getName() + "):= " + e.getMessage());
				}
			}
		});
	}
	
	private boolean isPageletTask(Object obj) {
		return (obj.getClass().getAnnotation(PageletTask.class)==null);
	}

	private String getPageletTaskName(Object obj) {
		PageletTask pageletTask = obj.getClass().getAnnotation(PageletTask.class);
		return pageletTask.name();
	}
	
	private Method getMethodFromTask(Object pageletTask) {
		Method[] methods = pageletTask.getClass().getMethods();
		
		Method annotatedMethod = null;
		for (Method method : methods) {
			Annotation pageletMethodAnnotation = method.getAnnotation(PageletTaskMethod.class);
			if (pageletMethodAnnotation!=null) {
				Class<?>[] parameters = method.getParameterTypes();
				if ( (parameters.length!=1) || 
						(!parameters[0].getCanonicalName().equals(HttpServletRequest.class.getCanonicalName())) ) {
					String message = "@PageletTaskMethod " + method.getDeclaringClass() + "." + method.getName()
							+ " does not have HttpServletRequest as the ONLY argument";
					logger.error(message);
					throw new IllegalArgumentException(message);
				}
				annotatedMethod = method;
				break;
			}
		}
		
		return annotatedMethod;
	}
}
