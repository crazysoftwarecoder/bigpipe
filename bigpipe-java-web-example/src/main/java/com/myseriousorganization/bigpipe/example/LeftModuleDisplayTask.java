package com.myseriousorganization.bigpipe.example;

import com.myseriousorganization.bigpipe.core.annotations.PageletTask;
import com.myseriousorganization.bigpipe.core.annotations.PageletTaskMethod;
import com.myseriousorganization.bigpipe.core.marker.ViewObject;
import com.myseriousorganization.bigpipe.core.threadlocal.HttpServletRequestTL;
import com.myseriousorganization.bigpipe.example.vo.LeftModuleVO;

import javax.servlet.http.HttpServletRequest;

@PageletTask(name="leftModule")
public class LeftModuleDisplayTask {

	@PageletTaskMethod
	public ViewObject doStuff(HttpServletRequest request) {
		String leftModuleParam = request.getParameter("left-module-param");
		return new LeftModuleVO("left-module display says:= " + leftModuleParam);
	}
}

