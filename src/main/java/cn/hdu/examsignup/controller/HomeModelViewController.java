package cn.hdu.examsignup.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import cn.hdu.examsignup.model.ExInstitution;
import cn.hdu.examsignup.service.InstitutionService;
import cn.hdu.examsignup.service.MainMenuService;
import cn.hdu.examsignup.service.RoleService;
import cn.hdu.examsignup.service.StudentService;
import cn.hdu.examsignup.service.UserService;

@Controller
public class HomeModelViewController {

	@Autowired
	private MainMenuService mainMenuService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private InstitutionService institutionService;

	@RequestMapping(value = "Home", method = RequestMethod.GET)
	public ModelAndView getindexPage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Integer roleNum=9;
		String school=null;
		String IDnum=null;
		String managerName=null;
		String institution=null;
		
		ModelAndView model = new ModelAndView("Home");
		HttpSession curSession = request.getSession();
		
		roleNum=(Integer)curSession.getAttribute("role");
		
		model.addObject("menus", mainMenuService.getAuthorisedMenu(roleNum));
		
		if(roleNum==null)
		{
			model.addObject("username","未定义角色");
		}
		else{
			if(this.roleService.getRoleByRoleName("student").getRolenum()==roleNum){
				school=(String)curSession.getAttribute("institution");
				IDnum=(String)curSession.getAttribute("IDnum");
				
				model.addObject("username",this.studentService.getStudentByIDnum(IDnum, school).getName()+"("+this.studentService.getStudentByIDnum(IDnum, school).getStudentnum()+")");
				model.addObject("institution",this.institutionService.getInstitutionByInstitutionNum(school).getName());
			}else{
				managerName=(String)curSession.getAttribute("managerName");
				institution=(String)curSession.getAttribute("institution");
				
				model.addObject("username",this.userService.getRoleByLoginName(managerName,institution).getName());
				model.addObject("institution",this.institutionService.getInstitutionByInstitutionNum(institution).getName());
			}
		}
		return model;
	}
}
