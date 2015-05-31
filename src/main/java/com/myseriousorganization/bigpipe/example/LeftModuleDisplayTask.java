package com.myseriousorganization.bigpipe.example;

import com.myseriousorganization.bigpipe.core.annotations.PageletTask;
import com.myseriousorganization.bigpipe.core.annotations.PageletTaskMethod;
import com.myseriousorganization.bigpipe.core.marker.ViewObject;
import com.myseriousorganization.bigpipe.example.vo.LeftModuleVO;

@PageletTask(name="leftModule")
public class LeftModuleDisplayTask {

	@PageletTaskMethod
	public ViewObject doStuff() {
		return new LeftModuleVO("left-module display");
	}
}

