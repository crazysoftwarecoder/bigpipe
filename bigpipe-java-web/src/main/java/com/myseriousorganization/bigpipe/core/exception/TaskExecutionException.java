package com.myseriousorganization.bigpipe.core.exception;

/**
 * This exception is thrown if anything happens
 * with the pagelet task execution.
 */
public class TaskExecutionException extends Exception {

	private static final long serialVersionUID = -3481437020535171193L;

	public TaskExecutionException(String message) {
		super(message);
	}
}
