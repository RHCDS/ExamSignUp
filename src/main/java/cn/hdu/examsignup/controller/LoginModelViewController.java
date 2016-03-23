package cn.hdu.examsignup.controller;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import cn.hdu.examsignup.service.RoleService;
import cn.hdu.examsignup.service.StudentService;
import cn.hdu.examsignup.service.UserService;
import cn.hdu.examsignup.service.IdCardCheck;

@Controller
public class LoginModelViewController {

	@Autowired
	private StudentService studentService;
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	
	private Logger logger= LoggerFactory.getLogger(LoginModelViewController.class);

	
	@RequestMapping(value = "hello", method = RequestMethod.GET)
	public ModelAndView helloworld(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView("helloworld");
		model.addObject("msg", "hello world");
		return model;
	}
	
	@RequestMapping(value = "ManagerLogin", method = RequestMethod.POST)
	public ModelAndView managerLoginController(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json;charset=UTF-8");
		
		Writer out = response.getWriter();
		HttpSession curSession = request.getSession();
		String managerName = request.getParameter("managerName");
		String managerPassword = request.getParameter("managerPassword");
		String category=request.getParameter("category");
		String institution=null;

		if (managerName == null || managerPassword == null) {
			out.write("{ success: false, errors:{info: '无法获得用户、密码！'}}");
			return null;
		}
		if(!(category.equals("school")||category.equals("examCollege"))){
			out.write("{ success: false, errors:{info: '类型错误！'}}");
			return null;
		}
		managerName=managerName.trim();
		managerPassword=managerPassword.trim();
		if (managerName.isEmpty() || managerPassword.isEmpty()) {
			out.write("{ success: false, errors:{info: '用户、密码不能为空！'}}");
			return null;
		}
		
		institution=userService.validateManagerWithoutInstitution(managerName, managerPassword,category);
		if(institution==null)
		{
			out.write("{ success: false, errors:{info: '您的身份信息未通过验证！'}}");
			return null;
		}
		
		if(curSession.getAttribute("IDnum")!=null)curSession.removeAttribute("IDnum");
		if(curSession.getAttribute("studentID")!=null)curSession.removeAttribute("studentID");
		if(curSession.getAttribute("institution")!=null)curSession.removeAttribute("institution");
		if(curSession.getAttribute("role")!=null)curSession.removeAttribute("role");
		
		curSession.setAttribute("managerName", managerName);
		curSession.setAttribute("institution", institution);
		curSession.setAttribute("role",this.userService.getRoleNumByLoginName(managerName,institution));
		logger.debug("所登录用户角色为："+(Integer) curSession.getAttribute("role"));
		
		out.write("{ success: true, errors:{info: '验证成功！'}}"); 
		return null;
	}
	
	@RequestMapping(value = "StudentLoginWithoutSchoolNum", method = RequestMethod.POST)
	public ModelAndView StudentLoginWithoutSchoolNum(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		Writer out = response.getWriter();
		HttpSession curSession = request.getSession();
		String studentID = request.getParameter("studentID");
		String IDnum = request.getParameter("IDnum");
		String institution=null;

		if (studentID == null || IDnum == null ) {
			out.write("{ success: false, errors:{info: '无法获得身份证号码、学号！'}}");
			return null;
		}
		studentID=studentID.trim();
		IDnum=IDnum.trim();
		
		if (studentID.isEmpty() || IDnum.isEmpty() ) {
			out.write("{ success: false, errors:{info: '身份证号码或学号不能为空！'}}");
			return null;
		}
		if (!IdCardCheck.isValidate18Idcard(IDnum)) {
			out.write("{ success: false, errors:{info: '身份证号码错误！'}}");
			return null;
		}
		institution=studentService.validateStudent(studentID, IDnum);
		if(institution==null)
		{
			out.write("{ success: false, errors:{info: '您的身份信息不存在！'}}");
			return null;
		}
		if(curSession.getAttribute("managerName")!=null)curSession.removeAttribute("IDnum");
		if(curSession.getAttribute("institution")!=null)curSession.removeAttribute("studentID");
		if(curSession.getAttribute("role")!=null)curSession.removeAttribute("role");
		
		curSession.setAttribute("IDnum", IDnum);
		curSession.setAttribute("studentID", studentID);
		curSession.setAttribute("institution", institution);
		curSession.setAttribute("role",roleService.getRoleByRoleName("student").getRolenum());
		
		out.write("{ success: true, errors:{info: '验证成功！'}}");
		return null;
	}
}
