package com.myseriousorganization.bigpipe.core.executor;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.myseriousorganization.bigpipe.core.marker.ViewObject;

public class PageletTaskOutputHolder {
	
	private Logger logger = LoggerFactory.getLogger(PageletTaskOutputHolder.class);

	private Map<String, BlockingQueue<ViewObject>> viewObjects
		= new ConcurrentHashMap<String, BlockingQueue<ViewObject>>();

	public void addPageletTask(String pageletName) {
		BlockingQueue<ViewObject> queue = this.viewObjects.get(pageletName);
		
		Preconditions.checkArgument(queue==null, this.getClass().getName() 
			+ " already has a queue for pageletName:= " + pageletName);
		
		this.viewObjects.put(pageletName, new ArrayBlockingQueue<ViewObject>(1));
	}

	public boolean doesPageletExist(String pageletName) {
		return (viewObjects.containsKey(pageletName));
	}
	
	public ViewObject getViewObject(String pageletName) {
		BlockingQueue<ViewObject> queue = this.viewObjects.get(pageletName);
		if (queue==null) {
			String error = "Queue for " + pageletName + " is null.";
			logger.error(error);
			throw new IllegalArgumentException(error);
		}
		ViewObject object = null;
		try {
			object = queue.take();
		}
		catch (InterruptedException e) {
			String message = "Control is not supposed to come here. Something is very wrong";
			logger.error(message);
			throw new IllegalStateException(message);
		}
		
		return object;
	}
	
	public void putViewObject(String pageletName, ViewObject viewObject) {
		BlockingQueue<ViewObject> queue = this.viewObjects.get(pageletName);
		if (queue==null) {
			throw new IllegalArgumentException("No queue found for " + pageletName);
		}
		try {
			if (queue.size()==1) {
				throw new IllegalArgumentException("ViewObject for pagelet " 
						+ pageletName + " has already been dropped into queue.");
			}
			queue.put(viewObject);
		}
		catch (InterruptedException e) {
			
		}
	}
	
}
