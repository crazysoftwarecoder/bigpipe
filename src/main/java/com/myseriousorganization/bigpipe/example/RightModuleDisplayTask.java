package com.myseriousorganization.bigpipe.example;

import com.myseriousorganization.bigpipe.core.annotations.PageletTask;
import com.myseriousorganization.bigpipe.core.annotations.PageletTaskMethod;
import com.myseriousorganization.bigpipe.core.marker.ViewObject;

@PageletTask(name="Remona")
public class RightModuleDisplayTask {
	
	@PageletTaskMethod
	public ViewObject doingStuff() {
		
		final String threadName = Thread.currentThread().getName(); 

		return new ViewObject() {
			@Override
			public String toString() {
				return threadName;
			}
		};
	}
}
