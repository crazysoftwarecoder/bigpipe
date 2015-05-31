package com.myseriousorganization.bigpipe.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PageletTask {

	/**
	 * Corresponds the task to the pagelet name on the JSP.
	 * This is required.
	 * 
	 * @return
	 */
	String name();
	
}
