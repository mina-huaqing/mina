package com.chinadovey.parking.core.supports.spring;

import java.io.File;
import java.util.Locale;

public class JstlView extends org.springframework.web.servlet.view.JstlView {

	public JstlView() {
	}

	@Override
	public boolean checkResource(Locale locale) throws Exception { 
		File file = new File(getServletContext().getRealPath("/") + getUrl());
		return file.exists();
	}

}
