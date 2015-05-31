package com.myseriousorganization.bigpipe.core.threadlocal;

import javax.servlet.http.HttpServletRequest;

public class PageletContext {
	
	public static HttpServletRequest getRequest() {
		return HttpServletRequestTL.local.get();
	}
}
