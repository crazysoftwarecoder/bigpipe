package com.myseriousorganization.bigpipe.example.vo;

import com.myseriousorganization.bigpipe.core.marker.ViewObject;

public class LeftModuleVO implements ViewObject {

	public LeftModuleVO(String message) {
		this.message = message;
	}
	
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}