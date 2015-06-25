package com.myseriousorganization.bigpipe.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate the method which is will serve
 * the <b>ViewObject</b> for the pagelet.
 *
 * This method will be executed in a separate thread for parallelization.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PageletTaskMethod {

}
