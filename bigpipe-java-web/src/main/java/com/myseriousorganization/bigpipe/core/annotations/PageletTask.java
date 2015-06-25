package com.myseriousorganization.bigpipe.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a pagelet task (which is a mini controller for the pagelet).
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PageletTask {

	/**
	 * Associates the task to the pagelet name on the JSP.
	 *
	 * @return
	 */
	String name();
	
}
