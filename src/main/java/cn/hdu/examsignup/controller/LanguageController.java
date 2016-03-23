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

import cn.hdu.examsignup.model.ExLanguage;
import cn.hdu.examsignup.service.LanguageService;

@RemoteProxy(creator = SpringCreator.class)
public class LanguageController {
	
	@Autowired
	private LanguageService languageService;
	
	@RemoteMethod
	public List<ExLanguage> findAll(){
		return this.languageService.findAll();
	}
	
	@RemoteMethod
	public Map paginationShow(String pageNum, String pageSize){
			Map map = new HashMap();
		List<Map> result = languageService.getPageLanguages( pageNum, pageSize);
		map.put("totalProperty", languageService.getLanguageTotalCount());
		map.put("root", result);
		return map;
	}

	@RemoteMethod
	public boolean saveLanguage(JSONObject language){
		if(language.get("id").toString().equals("")||language.get("id").toString() == ""){
			return languageService.saveLanguage(language);
		}else{
			return languageService.updateLanguage(language);
		}
	}

	@RemoteMethod
	public boolean deleteLanguage(List<String> ids){
			return languageService.deleteLanguage(ids);
	}
	
	@RemoteMethod
	public List getLanguagesName(){
		return languageService.getLanguagesName();
	}
	
	@RemoteMethod
	public Map loadlanguageList(){
		Map map = new HashMap();
		List<Map> result = languageService.loadlanguageList();
		map.put("totalProperty", result==null?0:result.size());
		map.put("root", result);
		return map;
	}
	
	@RemoteMethod
	public Map getTheoryLanguageList() {
		Map map = new HashMap();
		List<Map> result = languageService.getTheoryLanguageList();
		map.put("totalProperty", result==null?0:result.size());
		map.put("root", result);
		return map;
	}
	
	@RemoteMethod
	public boolean importLanguage(List<JSONObject> language){
		String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		return languageService.importLanguage(language,schoolnum);
	}
	
	
	
	
}
