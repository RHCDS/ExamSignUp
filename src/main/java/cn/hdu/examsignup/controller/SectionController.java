package cn.hdu.examsignup.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.directwebremoting.WebContextFactory;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.spring.SpringCreator;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.hdu.examsignup.model.ExInstitution;
import cn.hdu.examsignup.model.ExPhysicexamroom;
import cn.hdu.examsignup.model.ExSection;
import cn.hdu.examsignup.service.InstitutionService;
import cn.hdu.examsignup.service.SectionService;

@RemoteProxy(creator = SpringCreator.class)
public class SectionController {
	@Autowired
	private SectionService sectionService;
	@Autowired
	private InstitutionService institutionService;
	
	@RemoteMethod
	public Map loadPageDate(String pageNum, String pageSize,String theoryOrOperate) {
		Map map = new HashMap();
		String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		List<Map> result = sectionService.getPageSections(institutionnum, pageNum, pageSize,theoryOrOperate);
		
		map.put("totalProperty", this.sectionService.getSectionTotalCount(institutionnum, theoryOrOperate));
		map.put("root", result);
//		System.out.println("********************************************2222222222222222222222222222");
//		System.out.println(map.get("sectionnum"));
//		System.out.println(result.get(0).get("sectionnum"));
//		System.out.println("********************************************2222222222222222222222222222");
		return map;
	}
	
	@RemoteMethod
	public String saveSection(JSONObject section){
		String institutioNnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		ExInstitution institution = institutionService.getInstitutionByInstitutionNum(institutioNnum);
		if(section.get("id")==null)
			return "{ success: false, errors:{info: '传入参数错误!'}}";
		List<Map> result=this.sectionService.getSectionTotalWithSectionnum(institution.getInstitutionnum(), 
				section.get("theoryflag").toString().equals("1")?"theory":"operate"
				, Integer.parseInt(section.get("sectionnum")
				.toString().trim()));
		if(result.size()==1)
			section.put("id", result.get(0).get("id"));
		if(section.get("id").toString().trim().equals("")){
			return sectionService.saveSection(section,institution);
		}else{
			return sectionService.updateSection(section,institution);
		}
	}
	
	@RemoteMethod
	public boolean deleteSections(List<String> ids){
		return sectionService.deleteSections(ids);
	}
}
