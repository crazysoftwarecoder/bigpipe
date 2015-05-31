package com.myseriousorganization.bigpipe.core.tag;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class BigPipeStartTag extends SimpleTagSupport {

	private final String SETTER_JS_FUNCTION;

	public BigPipeStartTag() {
		// TODO Auto-generated constructor stub
		InputStream is = this.getClass().getResourceAsStream(
				"/setter_function.js");
		SETTER_JS_FUNCTION = convertStreamToString(is);
	}

	private static String convertStreamToString(InputStream is) {
		if (is == null)
			return null;
		StringBuilder sb = new StringBuilder(2048); // Define a size if you have
													// an idea of it.
		char[] read = new char[128]; // Your buffer size.
		try (InputStreamReader ir = new InputStreamReader(is,
				StandardCharsets.UTF_8)) {
			for (int i; -1 != (i = ir.read(read)); sb.append(read, 0, i))
				;
		} catch (Throwable t) {
		}
		return sb.toString();
	}

	@Override
	public void doTag() throws JspException, IOException {
		JspWriter out = getJspContext().getOut();
		out.println(SETTER_JS_FUNCTION);
	}

	public static void main(String[] args) {
		new BigPipeStartTag();
	}

}
