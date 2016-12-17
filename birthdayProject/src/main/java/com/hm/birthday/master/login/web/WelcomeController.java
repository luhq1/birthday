package com.hm.birthday.master.login.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hm.birthday.core.controller.AbstractDisplayController;

@Controller
public class WelcomeController extends AbstractDisplayController{
	
	@RequestMapping("admin.do")
	public ModelAndView adminWelcomePage() {
		return setModelView("admin/login");
	}
	
	@RequestMapping("login.do")
	public ModelAndView welcomePage() {
		return setModelView("master/login");
	}
	
}
