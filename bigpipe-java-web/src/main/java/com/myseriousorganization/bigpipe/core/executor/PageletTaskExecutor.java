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

public class PageletTaskExecutor {
	
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
	
	public void execute(final Object pageletTask) throws TaskExecutionException {
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
					Object returnObject = method.invoke(pageletTask, new Object[]{});
					outputHolder.putViewObject(pageletTaskName, (ViewObject) returnObject);
				}
				catch (InvocationTargetException | IllegalAccessException e) {
					// TO DO logging.
				}
			}
		});
	}
	
	private boolean isPageletTask(Object obj) {
		return (obj.getClass().getAnnotation(PageletTask.class)==null);
	}

	private String getPageletTaskName(Object obj) {
		PageletTask pageletTask = obj.getClass().getDeclaredAnnotation(PageletTask.class);
		return pageletTask.name();
	}
	
	private Method getMethodFromTask(Object pageletTask) {
		Method[] methods = pageletTask.getClass().getMethods();
		
		Method annotatedMethod = null;
		for (Method method : methods) {
			Annotation pageletMethodAnnotation = method.getAnnotation(PageletTaskMethod.class);
			if (pageletMethodAnnotation!=null) {
				annotatedMethod = method;
				break;
			}
		}
		
		return annotatedMethod;
	}
}
