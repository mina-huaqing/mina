package com.chinadovey.parking.webapps.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chinadovey.parking.core.mvc.AbstractBaseController;

@Controller
public class DefaultController extends AbstractBaseController {
	/**
	 * 显示系统首页
	 * @deprecated 兼容性入口
	 * @return
	 */
	@Deprecated
	@RequestMapping("/default")
	public String defaultPage(
			HttpServletRequest request,
			HttpServletResponse response,
			HttpSession session,
			Model model
			) {
		return "/default";
	}
}
