package com.myseriousorganization.bigpipe.core.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myseriousorganization.bigpipe.core.exception.TaskExecutionException;
import com.myseriousorganization.bigpipe.core.executor.PageletTaskExecutor;
import com.myseriousorganization.bigpipe.core.executor.PageletTaskOutputHolder;
import com.myseriousorganization.bigpipe.core.threadlocal.HttpServletRequestTL;
import com.myseriousorganization.bigpipe.core.threadlocal.PageletTaskOutputHolderTL;

public class BigPipeDispatcherServlet extends HttpServlet {

	private static final long serialVersionUID = -4125890259732263067L;

	private String forwardToJSP = null;
	
	private Class<?>[] pageletTasks = null;
	
	private PageletTaskExecutor pageletTaskExecutor = new PageletTaskExecutor();
	
	private Logger logger = LoggerFactory.getLogger(BigPipeDispatcherServlet.class);
	
	@Override
	public void init(ServletConfig config) {
		this.forwardToJSP = config.getInitParameter("jsp-file");
		try {
			this.pageletTasks = getPageletTaskClasses(config.getInitParameter("pageletTaskClasses"));
		}
		catch (ClassNotFoundException e) {
			String error = "@PageletTask was not found:= " + e.getMessage();
			logger.error(error);
			throw new IllegalArgumentException(error);
		}
	}
	
	private Class<?>[] getPageletTaskClasses(String pageletClassesString) throws ClassNotFoundException {
		String[] pageletClassArray = pageletClassesString.split(",");
		Class<?>[] pageletClasses = new Class<?>[pageletClassArray.length];
		
		int i=0;
		for (String pageleClassString : pageletClassArray) {
			pageletClasses[i++]=Class.forName(pageleClassString);
		}
		return pageletClasses;
	}
	
	private void dispatchToJSP(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RequestDispatcher dispatcher = req.getServletContext().getRequestDispatcher(forwardToJSP);
		dispatcher.forward(req, resp);
	}
	
    public void service(
    	HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
    	
    	// Set the servletRequest in the thread local.
    	HttpServletRequestTL.local.set(req);
    	PageletTaskOutputHolderTL.local.set(new PageletTaskOutputHolder());
    	
    	for (Class<?> pageletTask : pageletTasks) {
    		try {
    			// Creating new pagelet task objects per request
    			// for thread safety.
    			pageletTaskExecutor.execute(pageletTask.newInstance());
    		}
    		catch (IllegalAccessException | TaskExecutionException | InstantiationException e) {
    			throw new ServletException(e.getMessage());
    		}
    	}
    	
    	dispatchToJSP(req, resp);
    }
}