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

import cn.hdu.examsignup.service.ProfessionService;
import cn.hdu.examsignup.model.ExProfession;

@RemoteProxy(creator = SpringCreator.class)
public class ProfessionController {
	
	@Autowired
	private ProfessionService professionservice;
	
	@RemoteMethod
	public Map paginationShow(String pageNum, String pageSize){
		Map map = new HashMap();
		String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");	
		List<Map> result = professionservice.getPageProfessions(institutionnum, pageNum, pageSize);
		map.put("totalProperty", professionservice.getProfessionTotalCount(institutionnum));
		map.put("root", result);
		return map;
	}

	@RemoteMethod
	public boolean saveProfession(JSONObject profession){
		String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		if(profession.get("id").toString().equals("")||profession.get("id").toString() == ""){
			return professionservice.saveProfession(profession,institutionnum);
		}else{
			return professionservice.updateProfession(profession);
		}
	}

	@RemoteMethod
	public boolean deleteProfession(List<String> ids){
			return professionservice.deleteProfession(ids);
	}

}
