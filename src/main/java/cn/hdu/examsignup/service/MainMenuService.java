package cn.hdu.examsignup.service;

import java.util.List;
import java.util.Map;
import java.util.Set;


import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.spring.SpringCreator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import cn.hdu.examsignup.dao.MainMenuDao;
import cn.hdu.examsignup.dao.RoleDao;
import cn.hdu.examsignup.model.ExMainmenu;
import cn.hdu.examsignup.model.ExRole;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MainMenuService {

	@Autowired
	private MainMenuDao mainMenuDao;
	@Autowired
	private RoleDao roledao;
	
	static int TOPMENU_MASK =100;
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public String getAuthorisedMenu(Integer roleNum){
		List<ExMainmenu> authorisedMenu= this.mainMenuDao.getAuthorisedMenu(roleNum);
		JSONArray topArray = new JSONArray();
	    for (ExMainmenu topMenu:authorisedMenu) {
	    	if(topMenu.getExMainmenus().size()==0)
	    		continue;
	        JSONObject topJson = new JSONObject();
	        topJson.put("name",topMenu.getShowname());
	        topJson.put("index",topMenu.getSortindex()/TOPMENU_MASK);
			
			Set<ExMainmenu> childMenu = topMenu.getExMainmenus();
			JSONArray childArray = new JSONArray();
			for(ExMainmenu childNode:childMenu)
			{
				JSONObject childJson=new JSONObject();
				childJson.put("tabId", childNode.getMenuid());
				childJson.put("name", childNode.getShowname());
				childJson.put("index",childNode.getSortindex()%TOPMENU_MASK);
				childJson.put("script", childNode.getOnclickscript());
				childArray.add(childJson);
			}
			topJson.put("child",childArray);
			topArray.add(topJson);
			System.out.println(topArray.toString());
	    }
	    System.out.println(topArray);
		return topArray.toString();
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> findAllMenuByGrouping(){
		try{
			return mainMenuDao.findAllMenuByGrouping();
		}catch (Exception e) {
			System.out.println("findAllMenuByGrouping error" + e.getMessage());
			return null;
		}
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> findParentMenu(){
		try{
			return mainMenuDao.findParentMenu();
		}catch (Exception e) {
			System.out.println("findParentMenu error" + e.getMessage());
			return null;
		}
		
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> loadParentMenuName() {
		try {
			return mainMenuDao.loadParentMenuName();
		} catch(Exception e) {
			System.out.println("loadParentMenuName error" + e.getMessage());
			return null;
		}
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public boolean saveMainMenu(JSONObject mainmenu){
		try {
			ExMainmenu entity = new  ExMainmenu();
			entity.setMenuid(mainmenu.get("menuid").toString());
			entity.setOnclickscript(mainmenu.get("onclickscript").toString());
			entity.setShowname(mainmenu.get("showname").toString());
			entity.setSortindex(Integer.parseInt(mainmenu.get("sortindex").toString()));
			if(!(mainmenu.get("parentmenu").toString().equals(""))){
				entity.setExMainmenu(mainMenuDao.getByShowname(mainmenu.get("parentmenu").toString()).get(0));
			}
			if(!(mainmenu.get("rolename").toString().equals(""))){
				entity.setExRole(roledao.findByProperty("remark", mainmenu.get("rolename").toString()).get(0));
			}
			mainMenuDao.save(entity);
			return true;
		} catch (Exception e) {
			System.out.println("saveMainMenu error!" + e.getMessage());
			return false;
		}	
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public boolean updateMainMenu(JSONObject mainmenu){
		try {
			ExMainmenu entity = mainMenuDao.findById(mainmenu.get("id").toString());
			entity.setMenuid(mainmenu.get("menuid").toString());
			entity.setOnclickscript(mainmenu.get("onclickscript").toString());
			entity.setShowname(mainmenu.get("showname").toString());
			entity.setSortindex(Integer.parseInt(mainmenu.get("sortindex").toString()));
			if(!(mainmenu.get("parentmenu").toString().equals(""))){
				entity.setExMainmenu(mainMenuDao.getByShowname(mainmenu.get("parentmenu").toString()).get(0));
			}
			if(!(mainmenu.get("rolename").toString().equals(""))){
				entity.setExRole(roledao.getByRemark(mainmenu.get("rolename").toString()).get(0));
			}
			mainMenuDao.update(entity);
			return true;
		} catch (Exception e) {
			System.out.println("updateMainMenu error!" + e.getMessage());
			return false;
		}	
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String saveParentMenu(JSONObject mainmenu){
		try {
			ExMainmenu entity = new  ExMainmenu();
			String menuid = mainmenu.get("menuid").toString();
			entity.setMenuid(menuid);
			entity.setShowname(mainmenu.get("showname").toString());
			entity.setSortindex(Integer.parseInt(mainmenu.get("sortindex").toString()));
			if(!(mainmenu.get("rolename").toString().equals(""))){
				entity.setExRole(roledao.getByRemark(mainmenu.get("rolename").toString()).get(0));
			}
			mainMenuDao.save(entity);
			return "{success: true, errors:{info: '新建成功！'}}";
		} catch (Exception e) {
			System.out.println("saveParentMenu error!" + e.getMessage());
			return "{ success: false, errors:{info: '新建失败！' }}";
		}	
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String updateParentMenu(JSONObject mainmenu){
		try {
			ExMainmenu entity = mainMenuDao.findById(mainmenu.get("id").toString());
			entity.setMenuid(mainmenu.get("menuid").toString());
			entity.setShowname(mainmenu.get("showname").toString());
			entity.setSortindex(Integer.parseInt(mainmenu.get("sortindex").toString()));
			if(!(mainmenu.get("rolename").toString().equals(""))){
				entity.setExRole(roledao.getByRemark(mainmenu.get("rolename").toString()).get(0));
			}
			mainMenuDao.update(entity);
			return "{ success: true, errors:{info: '修改成功！' }}";
		} catch (Exception e) {
			System.out.println("updateMainMenu error!" + e.getMessage());
			return "{ success: false, errors:{info: '修改失败！' }}";
		}	
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<ExMainmenu> getByShowname(String showname){
		return mainMenuDao.getByShowname(showname);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public boolean deleteMainMenu(List<String> ids){
		try {
			for(String id : ids){
				ExMainmenu entity = mainMenuDao.findById(id);
				mainMenuDao.delete(entity);
			}
			return true;
		} catch (Exception e) {
			System.out.println("deleteMainMenu error!" + e.getMessage());
			return false;
		}

	}

}
