package com.myseriousorganization.bigpipe.example.vo;

import com.myseriousorganization.bigpipe.core.marker.ViewObject;

public class RightModuleVO implements ViewObject {

	private String moduleName;

	public RightModuleVO(String moduleName) {
		this.moduleName = moduleName;
	}
	
	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
}
