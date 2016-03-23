package cn.hdu.examsignup.service;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.hdu.examsignup.dao.InstitutionDao;
import cn.hdu.examsignup.dao.LanguageDao;
import cn.hdu.examsignup.model.ExCampus;
import cn.hdu.examsignup.model.ExInstitution;
import cn.hdu.examsignup.model.ExLanguage;
import cn.hdu.examsignup.model.ExPhysicexamroom;


@Service
@Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
public class LanguageService {
	
	@Autowired
	private LanguageDao languageDao;
	
	@Autowired 
	InstitutionDao institutiondao;
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<ExLanguage> findAll(){
		return this.languageDao.findAll();
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public long getLanguageTotalCount(){
		try {
			return languageDao.getLanguageTotalCount();
		} catch (Exception e) {
			System.out.println("getLanguageTotalCount error:" + e.getMessage());
			return 0;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> getPageLanguages(String pageNum, String pageSize){
		try {
			return languageDao.getPageLanguages(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
		} catch (Exception e) {
			System.out.println("getPageLanguages error:" + e.getMessage());
			return null;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public boolean saveLanguage(JSONObject language){
		try {
			ExLanguage entity = new  ExLanguage();
			entity.setName(language.get("name").toString());
			entity.setLanguagenum(language.get("languagenum").toString());
			entity.setTheorylength(Integer.parseInt(language.get("theorylength").toString()));
			entity.setOperatelength(Integer.parseInt(language.get("operatelength").toString()));
			if(language.get("theoryflag").toString().equals("是")){
				entity.setTheoryflag("1");
			}else{entity.setTheoryflag("0");}
			if(!(language.get("operateflag").toString().equals("否"))){
				entity.setOperateflag("1");
			}else{entity.setOperateflag("0");}
			languageDao.save(entity);
			return true;
		} catch (Exception e) {
			System.out.println("saveLanguage error!" + e.getMessage());
			return false;
		}	
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public boolean updateLanguage(JSONObject language){
		try {
			ExLanguage entity = languageDao.findById(language.get("id").toString());
			entity.setName(language.get("name").toString());
			entity.setLanguagenum(language.get("languagenum").toString());
			entity.setTheorylength(Integer.parseInt(language.get("theorylength").toString()));
			entity.setOperatelength(Integer.parseInt(language.get("operatelength").toString()));
			if(language.get("theoryflag").toString().equals("是")){
				entity.setTheoryflag("1");
			}else{entity.setTheoryflag("0");}
			if(!(language.get("operateflag").toString().equals("否"))){
				entity.setOperateflag("1");
			}else{entity.setOperateflag("0");}
			languageDao.update(entity);
			return true;
		} catch (Exception e) {
			System.out.println("saveLanguage error!" + e.getMessage());
			return false;
		}	
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public boolean deleteLanguage(List<String> ids){
		try {
			for(String id : ids){
				ExLanguage entity = languageDao.findById(id);
				languageDao.delete(entity);
			}
			return true;
		} catch (Exception e) {
			System.out.println("deleteLanguage error!" + e.getMessage());
			return false;
		}

	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List getLanguagesName(){
		try {
			return languageDao.getLanguagesName();
		} catch (Exception e) {
			System.out.println("getLanguagesName error!" + e.getMessage());
			return null;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public ExLanguage getLanguageBylanguageNumAndType(String languageNum,String theoryOrOperate){
		try {
			return languageDao.getLanguageBylanguageNumAndType(languageNum,theoryOrOperate);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Map> loadlanguageList() {
		try {
			return languageDao.loadlanguageList( );
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Map> getTheoryLanguageList() {
		try {
			return languageDao.getTheoryLanguageList();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public boolean importLanguage(List<JSONObject> languages,String schoolnum){
		try {
			int roomcount = 0;
			ExLanguage entity;
			ExInstitution school = institutiondao.getInstitutionByInstitutionNum(schoolnum);
			for(JSONObject language : languages){
				if(languageDao.findByProperty("languagenum",language.get("languagenum").toString()).size()>0){
					entity = languageDao.findByProperty("languagenum", language.get("languagenum").toString()).get(0);
				}else{
					entity = new ExLanguage();
				}
				
				
			//	entity.setExInstitution(school);
				entity.setLanguagenum(language.get("languagenum").toString());
				entity.setName(language.get("name").toString());
				
			
				
				if(language.get("theoryflag").toString().equals("是")||language.get("theoryflag").toString().equals("1")){
					entity.setTheoryflag("1");
				
				}else{
					entity.setTheoryflag("0");
					}
				if(language.get("operateflag").toString().equals("是")||language.get("operateflag").toString().equals("1")){
					entity.setOperateflag("1");
				}else{
					entity.setOperateflag("0");
					}
				entity.setTheorylength(Integer.parseInt(language.get("theorylength").toString()));
				entity.setOperatelength(Integer.parseInt(language.get("operatelength").toString()));
				languageDao.save(entity);
				if(roomcount%50==0){
				languageDao.getSession().flush();
				languageDao.getSession().clear();
				}
				roomcount++;
			}
		return true;
	} catch (Exception e) {
		System.out.println("importlanguage error:" + e.getMessage());
		languageDao.getSession().getTransaction().rollback();
		return false;
	}
}
	
	



}
