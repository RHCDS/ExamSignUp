package cn.hdu.examsignup.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.spring.SpringCreator;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.hdu.examsignup.model.ExMainmenu;
import cn.hdu.examsignup.service.InstitutionService;
import cn.hdu.examsignup.service.MainMenuService;


@RemoteProxy(creator = SpringCreator.class)
public class MainMenuController {
	@Autowired
	private MainMenuService mainmenuservice;

	@RemoteMethod
	public List<Map> findAllMenuByGrouping(){
		return mainmenuservice.findAllMenuByGrouping();
	}
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RemoteMethod
	public Map loadParentMenu() {
		Map map = new HashMap();
		List<Map> result = mainmenuservice.findParentMenu();
		map.put("totalProperty",result.size());
		map.put("root", result);
		return map;
	}
	@RemoteMethod
	public boolean saveMainMenu(JSONObject mainmenu){
		if(mainmenu.get("id").toString().equals("")||mainmenu.get("id").toString() == ""){
			return mainmenuservice.saveMainMenu(mainmenu);
		}else{
			return mainmenuservice.updateMainMenu(mainmenu);
		}
	}
	
	@RemoteMethod
	public String saveParentMenu(JSONObject mainmenu){
		if(mainmenu.get("id").toString().equals("")||mainmenu.get("id").toString() == ""){
			return mainmenuservice.saveParentMenu(mainmenu);
		}else{
			return mainmenuservice.updateParentMenu(mainmenu);
		}
	}
	@RemoteMethod
	public Map loadParentMenuName() {
		Map map = new HashMap();
		List<Map> result = mainmenuservice.loadParentMenuName();
		map.put("totalProperty",result.size());
		map.put("root", result);
		return map;
	}
	@RemoteMethod
	public List<ExMainmenu> getByShowname(String showname){
		return mainmenuservice.getByShowname(showname);
	}
	
	@RemoteMethod
	public boolean deleteMainMenu(List<String> ids){
		return mainmenuservice.deleteMainMenu(ids);
	}
}
