package cn.hdu.examsignup.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.directwebremoting.WebContextFactory;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.spring.SpringCreator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.hdu.examsignup.model.ExInstitution;
import cn.hdu.examsignup.model.ExLanguage;
import cn.hdu.examsignup.model.ExSection;
import cn.hdu.examsignup.service.InstitutionService;
import cn.hdu.examsignup.service.LanguageService;
import cn.hdu.examsignup.service.TheoryExamArrangeService;
import cn.hdu.examsignup.service.SectionService;

@RemoteProxy(creator = SpringCreator.class)
public class TheoryExamArrangeController {
	@Autowired
	private TheoryExamArrangeService theoryExamArrangeService;
	@Autowired
	private InstitutionService institutionService;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private LanguageService languageService;

	@RemoteMethod
	public Map loadSignedLanguage() {
		Map map = new HashMap();
		String institutionnum = (String) WebContextFactory.get().getSession()
				.getAttribute("institution");
		List<Map> result = theoryExamArrangeService
				.getLoadSignedLanguage(institutionnum);

		map.put("totalProperty", result.size());
		map.put("root", result);
		return map;
	}
	@RemoteMethod
	public Map loadArrangeInfo(String languageNum) {
		Map map = new HashMap();
		String institutionnum = (String) WebContextFactory.get().getSession()
				.getAttribute("institution");
		if (languageNum == null || languageNum.trim().isEmpty()
				|| languageNum.trim().equals("0")) {
			List<Map> emptyList = new ArrayList<Map>();
			map.put("totalProperty", 0);
			map.put("root", emptyList);
			return map;
		}

		List<Map> result = theoryExamArrangeService.loadArrangeInfo(
				institutionnum, languageNum);
		map.put("totalProperty", result.size());
		map.put("root", result);
		return map;
	}

	@RemoteMethod
	public String savetheoryExamroom(String selectNum,String languageNum,List<Map> changedItems){
		String institutionnum = (String) WebContextFactory.get().getSession()
				.getAttribute("institution");
		ExInstitution institution =institutionService.getInstitutionByInstitutionNum(institutionnum);
		ExSection section= sectionService.getSectionBySectionnumAndType(institutionnum,selectNum,"theory");
		ExLanguage language= languageService.getLanguageBylanguageNumAndType(languageNum,"theory");
		if(institution == null)
		{
			return "{ success: false, errors:{info: '无法找到学校代码为"+institutionnum+"的学校信息！'}}";
		}
		if(section == null)
		{
			return "{ success: false, errors:{info: '无法找到场次代码为"+section+"的场次信息！'}}";
		}
		if(language ==null)
		{
			return "{ success: false, errors:{info: '无法找到语种代码为"+section+"的语种信息！'}}";
		}
		return this.theoryExamArrangeService.saveTheoryExamroom(section,language,institution,changedItems);
	}
	@RemoteMethod
	public String loadtheoryExamroom(String sectionnumParam,String languagenum){
		int maintenance,sectionnum;
		List<Map> availabletheoryExamroom,selectedtheoryExamroom;
		JSONArray selectedJson = new JSONArray();
		JSONArray unselectedJson = new JSONArray();
		JSONObject result = new JSONObject();

		if(sectionnumParam == null || sectionnumParam.trim().isEmpty())
			return null;
		sectionnum=Integer.parseInt(sectionnumParam.trim());
		String institutionnum = (String) WebContextFactory.get().getSession()
				.getAttribute("institution");
		
		availabletheoryExamroom = this.theoryExamArrangeService.loadAvailableTheoryExamroom(institutionnum,sectionnum,languagenum);
		selectedtheoryExamroom = this.theoryExamArrangeService.loadSelectedTheoryExamroom(institutionnum, sectionnum, languagenum);

		for(Map element:availabletheoryExamroom){
			if(Integer.parseInt(element.get("capacity").toString()) == 0) {
				continue;
			}
			JSONArray temp=new JSONArray();
			temp.add((element.get("id")));
			temp.add((element.get("campusname"))+":"+(element.get("location"))+"(可容纳"+element.get("capacity").toString()+"人)");
			temp.add("new");
			unselectedJson.add(temp);
		}
		result.put("unselected", unselectedJson);

		for(Map element:selectedtheoryExamroom){
			
			JSONArray temp=new JSONArray();
			temp.add(((String)element.get("id")));
			temp.add((element.get("campusname"))+":"+(element.get("location"))+"(可容纳"+element.get("capacity").toString()+"人)");
			temp.add("old");
			selectedJson.add(temp);
		}
		result.put("selected", selectedJson);
		result.put("success", true);

		return result.toString();
	}
	@RemoteMethod
	public Map loadAllSections(){
		Map map = new HashMap();

		String institutionnum = (String) WebContextFactory.get().getSession()
				.getAttribute("institution");
		List<Map> result=this.theoryExamArrangeService.loadAllSections(institutionnum);
		map.put("totalProperty", result.size());
		map.put("root", result);
		return map;
	}

	@RemoteMethod
	public Map loadSpecialArrangedStudent(String arrangeid,String languagenum){
		Map map = new HashMap();

		String institutionnum = (String) WebContextFactory.get().getSession()
				.getAttribute("institution");
		List<Map> result=this.theoryExamArrangeService.loadSpecialArrangedStudent(institutionnum,arrangeid,languagenum);
		map.put("totalProperty", result.size());
		map.put("root", result);
		return map;
	}

	@RemoteMethod
	public Map loadExamroomItem(String languagenum,String arrangeid) {
		Map map = new HashMap();

		String institutionnum = (String) WebContextFactory.get().getSession()
				.getAttribute("institution");
		List<Map> result=this.theoryExamArrangeService.loadExamroomItem(institutionnum, languagenum,arrangeid);
		map.put("totalProperty", (result==null)?0:result.size());
		map.put("root", result);
		return map;
	}

	@RemoteMethod
	public Map loadStudentBySpecialExamroomAndCampus(String examroomNum,String arrangeid,String languagenum) {
		Map map = new HashMap();

		String institutionnum = (String) WebContextFactory.get().getSession()
				.getAttribute("institution");
		List<Map> result=this.theoryExamArrangeService.loadStudentBySpecialExamroomAndCampus(institutionnum,examroomNum,arrangeid, languagenum);
		map.put("totalProperty", (result==null)?0:result.size());
		map.put("root", result);
		return map;
	}

	@RemoteMethod
	public String saveArrangedStudent(List<Map> readyToArrange,String arrangeid ){
		if(arrangeid.trim().isEmpty()){
			return "{ success: false, errors:{info: '没有场次信息！'}}";
		}
		return this.theoryExamArrangeService.saveArrangedStudent(readyToArrange,arrangeid);
	}

	@RemoteMethod
	public String deleteArrangedStudent(List<Map> unArranged){
		return this.theoryExamArrangeService.deleteArrangedStudent(unArranged);
	}
	//得到某语种考试安排统计信息
	@RemoteMethod
	public String  getStatisticsByLang(String languagenum,String sectionnum) {
		String institutinnum = (String)WebContextFactory.get().getSession()
				.getAttribute("institution");
		return this.theoryExamArrangeService.getStatisticsByLang(institutinnum,languagenum,sectionnum);
	}

	//得到某语种按校区分类的考试安排统计信息
	@RemoteMethod
	public Map  getStatisticsByLangAndCampus(String languagenum,String sectionnum) {
		String institutinnum = (String)WebContextFactory.get().getSession()
				.getAttribute("institution");
		Map map = new HashMap();
		List<Map> root=this.theoryExamArrangeService.getStatisticsByLangAndCampus(institutinnum,languagenum,sectionnum);
		map.put("totalProperty", root.size());
		map.put("root",root);
		return map;
	}

	//获取已安排的考场号
	@RemoteMethod
	public Map getArrangedExamroom(String arrangeid) {
		Map map = new HashMap();
		String institutionnum = (String) WebContextFactory.get().getSession()
				.getAttribute("institution");
		List<Map> root = this.theoryExamArrangeService.getArrangedExamroom(arrangeid,institutionnum);
		map.put("totalProperty", root.size());
		map.put("root", root);
		return map;
	}
	//获取未安排逻辑考场号信息
	@RemoteMethod
	public Map getUnarrangedExamroom(String languagenum,String arrangeid) {
		Map map = new HashMap();
		String institutionnum =(String) WebContextFactory.get().getSession()
				.getAttribute("institution");
		List<Map> root = this.theoryExamArrangeService.getUnarrangedExamroom(institutionnum, languagenum, arrangeid);
		map.put("totalProperty", root.size());
		map.put("root", root);
		return map;
	}
	//删除已安排考场
	@RemoteMethod
	public String deleteArrangedExamroom(List<Map> examroom) {
		String institutionnum = (String) WebContextFactory.get().getSession()
				.getAttribute("institution");
		return this.theoryExamArrangeService.deleteArrangedExamroom(examroom,institutionnum);
	}
	//安排理论考场
	@RemoteMethod
	public String arrangeByExamroom(String arrangeid,String examroomnum) {
		String institutionnum = (String)WebContextFactory.get().getSession().getAttribute("institution");
		String result = this.theoryExamArrangeService.arrangeByExamroom(institutionnum,arrangeid,examroomnum);
		return result;
	}
	//自动安排考生
	@RemoteMethod
	public String autoArrange(String languagenum,String sectionnum) {
		String institutionnum = (String) WebContextFactory.get().getSession()
				.getAttribute("institution");
		return this.theoryExamArrangeService.autoArrange(institutionnum,languagenum,sectionnum);
	}
	//取消安排
	@RemoteMethod
	public String cancelArrange(String languagenum){
		String institutionnum = (String) WebContextFactory.get().getSession()
				.getAttribute("institution");
		return this.theoryExamArrangeService.cancelArrange(institutionnum,languagenum);
	}
}
