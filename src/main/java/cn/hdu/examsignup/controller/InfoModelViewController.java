package cn.hdu.examsignup.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class InfoModelViewController {
	
	@RequestMapping(value = "Logout", method = RequestMethod.GET)
	public ModelAndView logoutController(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		HttpSession curSession = request.getSession();
		curSession.invalidate();
		
		ModelAndView model = new ModelAndView("../Info");
		model.addObject("msg","注销成功！");
		return model;
	}
	
	@RequestMapping(value = "Error", method = RequestMethod.POST)
	public ModelAndView errorController(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String msg = (String) request.getAttribute("msg");

		HttpSession curSession = request.getSession();
		curSession.invalidate();
		
		ModelAndView model = new ModelAndView("../Info");
		model.addObject("msg",msg);
		return model;
	}
}