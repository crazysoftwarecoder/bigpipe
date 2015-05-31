package com.myseriousorganization.bigpipe.core.tag;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Base64;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.myseriousorganization.bigpipe.core.executor.PageletTaskOutputHolder;
import com.myseriousorganization.bigpipe.core.threadlocal.PageletTaskOutputHolderTL;

public class PageletBodyTag extends SimpleTagSupport {

	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String viewObject;
	
	public String getViewObject() {
		return viewObject;
	}

	public void setViewObject(String viewObject) {
		this.viewObject = viewObject;
	}
	
	private StringWriter writer = new StringWriter();
	
	private void emitDivPlaceholder(String pageletName) throws IOException {
		String div = "<div id=\"" + pageletName + "\"> </div>";
		getJspContext().getOut().println(div);
	}

	@Override
	public void doTag() throws JspException, IOException {
		PageletTaskOutputHolder viewObjectHolder = PageletTaskOutputHolderTL.local.get();
		if (viewObjectHolder.doesPageletExist(name)) 
			getJspContext().setAttribute(viewObject, viewObjectHolder.getViewObject(name));
		// TODO Warn on the logs - that pagelet was not found.
		
		emitDivPlaceholder(name);
		
		getJspBody().invoke(writer);

		String pageletContent = Base64.getEncoder().encodeToString(writer.toString().getBytes());
		
		StringBuffer javascriptInvocation = new StringBuffer("<script type=\"text/javascript\">");
		javascriptInvocation.append("setHTML('" + name + "','" + pageletContent + "');");
		javascriptInvocation.append("</script>");
		
		getJspContext().getOut().println(javascriptInvocation.toString());
		getJspContext().getOut().flush();
	}
}
