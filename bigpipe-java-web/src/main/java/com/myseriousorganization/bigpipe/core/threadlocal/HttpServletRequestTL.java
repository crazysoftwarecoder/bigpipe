package com.myseriousorganization.bigpipe.core.threadlocal;

import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestTL {

	public static final ThreadLocal<HttpServletRequest> 
		local = new ThreadLocal<HttpServletRequest>();

}
