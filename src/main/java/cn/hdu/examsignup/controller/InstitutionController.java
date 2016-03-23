package cn.hdu.examsignup.controller;

import java.util.ArrayList;
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
import cn.hdu.examsignup.service.InstitutionService;


@RemoteProxy(creator = SpringCreator.class)
public class InstitutionController {

	@Autowired
	private InstitutionService institutionService;

	public InstitutionController() {
	}
	
	@RemoteMethod
	public List<String[]> getSchoolNumName() {
		return this.institutionService.getSchoolNumName();
	}
	
	@RemoteMethod
	public List<String[]> getInstitutionNumName() {
		return this.institutionService.getInstitutionNumName();
	}
	
	@RemoteMethod
	public ExInstitution getInstitutionByInstitutionNum(String institutionnum){
		return institutionService.getInstitutionByInstitutionNum(institutionnum);
	}
	
	@RemoteMethod
	public ExInstitution getInstitutionByInstitutionID(String institutionid){
		return institutionService.getInstitutionByInstitutionID(institutionid);
	}
	
	@RemoteMethod
	public  Map loadManagedSchoolList(){
		Map map = new HashMap();
		String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		List<Map> result = this.institutionService.loadManagedSchoolList(institutionnum);
		map.put("totalProperty", result==null?0:result.size());
		map.put("root", result);
		return map;
	}
	
	@RemoteMethod
	public  Map loadChildInstitution(String institutionnum){
		Map map = new HashMap();
		List<Map> result = this.institutionService.loadChildInstitution(institutionnum);
		map.put("totalProperty", result==null?0:result.size());
		map.put("root", result);
		return map;
	}
	
	@RemoteMethod
	public  String cityManagerSetCheckPass(List<Map> readyToConfirmList){
		Integer roleNum = (Integer) WebContextFactory.get().getSession().getAttribute("role");
		String currentInstitution = (String) WebContextFactory.get().getSession().getAttribute("institution");
		return this.institutionService.setInstitutionstatusByString(readyToConfirmList,roleNum,currentInstitution,4);
	}
	
	@RemoteMethod
	public  String getCityManagerStatus(){
		String currentInstitution = (String) WebContextFactory.get().getSession().getAttribute("institution");
		return "{ success: true, errors:{info: '"+this.institutionService.getCityManagerStatus(currentInstitution)+"'}}";
	}
	
	@RemoteMethod
	public  String cityManagerSelfCheckPass(){
		String currentInstitution = (String) WebContextFactory.get().getSession().getAttribute("institution");
		return this.institutionService.cityManagerSelfCheckPass(currentInstitution,4);
	}
	
	/*@RemoteMethod
	public  String provinceManagerSetCheckPass(){
		Integer roleNum = (Integer) WebContextFactory.get().getSession().getAttribute("role");
		String currentInstitution = (String) WebContextFactory.get().getSession().getAttribute("institution");
		return this.institutionService.setInstitutionstatusByString(roleNum,currentInstitution,5);
	}*/
	
	@RemoteMethod
	public List<Map> getInstitutionByType(String type){
		return institutionService.getInstitutionByType(type);
	}
	
	@RemoteMethod
	public String saveInstitution(JSONObject institution, String category){
		return institutionService.saveInstitution(institution, category);
	}
	
	@RemoteMethod
	public String deleteInstitutions(List<String> ids){
		return institutionService.deleteInstitutions(ids);
	}
	
	@RemoteMethod
	public List<Map> getSchoolAndCityStatusInfo( ){
		String currentInstitution = (String) WebContextFactory.get().getSession().getAttribute("institution");
		return institutionService.getSchoolAndCityStatusInfo(currentInstitution);
	}
	@RemoteMethod
	public String turnOnSignUp() {
		String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		return this.institutionService.turnOnSignUp(schoolnum);
	}
	@RemoteMethod
	public String turnDownSignUp() {
		String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		return this.institutionService.turnDownSignUp(schoolnum);
	}
	@RemoteMethod
	public String checkSignUpStatus() {
		String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		return this.institutionService.checkSignUpStatus(schoolnum);
	}
	@RemoteMethod
	public String turnOnCheckScore() {
		String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		return this.institutionService.turnOnCheckScore(schoolnum);
	}
	@RemoteMethod
	public String turnOffCheckScore() {
		String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		return this.institutionService.turnOffCheckScore(schoolnum);
	}
	@RemoteMethod
	public String checkCheckScoreStatus() {
		String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		return this.institutionService.checkCheckScoreStatus(schoolnum);
	}
}
