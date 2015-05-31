package com.myseriousorganization.bigpipe.core.threadlocal;

import com.myseriousorganization.bigpipe.core.executor.PageletTaskOutputHolder;

public class PageletTaskOutputHolderTL {
	
	public static final ThreadLocal<PageletTaskOutputHolder> 
		local = new ThreadLocal<PageletTaskOutputHolder>();
	
}
