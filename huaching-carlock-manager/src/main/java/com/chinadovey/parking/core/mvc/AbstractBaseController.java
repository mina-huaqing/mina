package com.chinadovey.parking.core.mvc;

import org.apache.log4j.Logger;

/**
 * 抽象的Spring控制器类<br>
 * 原则上所有的Controller都必须继承该类
 * @author Bean
 *
 */
public abstract class AbstractBaseController {

	protected Logger logger = Logger.getLogger(getClass());

	protected void debug(String message) {
		if (logger.isDebugEnabled())
			logger.debug(message);
	}

	protected void error(String message) {
		logger.error(message);
	}

	protected void info(String message) {
		if (logger.isInfoEnabled())
			logger.info(message);
	}

//	protected void renderText(HttpServletResponse response, String result)
//			throws IOException {
//		PrintWriter out = response.getWriter();
//		response.setHeader("Cache-Control", "no-store");
//		response.setHeader("Pragma", "no-cache");
//		response.setDateHeader("Expires", 0);
//		out.print(result);
//		out.flush();
//		out.close();
//	}

//	@Autowired
//	private MessageSource messageSource;
//	暂时不启用国际化
//	protected String getMessage(String key) {
//		return messageSource.getMessage(key, null, null);
//	}
}
