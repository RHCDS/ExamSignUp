package cn.hdu.examsignup.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import cn.hdu.examsignup.model.Function;
import cn.hdu.examsignup.service.ExamNumEditService;
import cn.hdu.examsignup.service.InstitutionService;

/**
 *  主要管理准考证号编排获取
 */
@RemoteProxy(creator = SpringCreator.class)
public class ExamNumEditController {
	@Autowired
	private ExamNumEditService examNumEditService;
	@Autowired
	private InstitutionService institutionService;
	
	/**
	 * @param filterString
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * 
	 * 获取高校本校所有已经缴费的考生信息
	 * 注：如果有存在学校还没有缴费通过，则没有显示信息
	 * 
	 * 1、如果还没有编排准考证，则准考证为空
	 * 2、如果以及编排准考证，则显示准考证信息
	 */
	@RemoteMethod
	public Map loadPageDate(String filterString, String pageNum, String pageSize) {
		Map map = new HashMap();
		String whereString = Function.initSearchHql_whereString_(filterString);
		Map<String, Object> paramsMap = Function.initSearchValues(filterString);
		String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		List<Map> result = examNumEditService.getPageExamNumEdit(whereString, paramsMap,institutionnum, Integer.parseInt(pageNum.trim()), Integer.parseInt(pageSize.trim()));
		map.put("totalProperty", this.examNumEditService.getExamNumEditTotalCount(whereString, paramsMap,institutionnum));
		map.put("root", result);
		return map;
	}
	
	@RemoteMethod
	public Map loadExamNumLanguageInfo(){
		Map map = new HashMap();
		String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		List<Map> result = examNumEditService.loadExamNumLanguageInfo(institutionnum);
		
		map.put("totalProperty", result.size());
		map.put("root", result);
		return map;
	}
	
	@RemoteMethod
	public Map loadExamNumLanguageInfoByInstitutionnum(String institutionnum){
		Map map = new HashMap();
		List<Map> result = examNumEditService.loadExamNumLanguageInfo(institutionnum);
		
		map.put("totalProperty", result.size());
		map.put("root", result);
		return map;
	}
	
	/**
	 * @return
	 * 高校点击编排准考证之前首先验证是否所有考生都已经完成缴费
	 * 
	 * 编排准考证之前需要所有考生都已经完成缴费
	 * 
	 */
	@RemoteMethod
	public String validateBeforeArrange(){
		String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		return this.examNumEditService.validateBeforeArrange(institutionnum);
	}
	
	@RemoteMethod
	public Map getArrangeInfo(){
		Map map=new HashMap();
		Calendar cal=Calendar.getInstance();
		String tempString;
		Integer tempInt;
		if(cal.get(Calendar.MONTH)<=6)
			tempString="1";
		else
			tempString="2";
		tempInt=cal.get(Calendar.YEAR)%100;
		tempString=tempInt.toString()+tempString;
		map.put("examtime", tempString);
		map.put("institutionnum",(String) WebContextFactory.get().getSession().getAttribute("institution"));
		map.put("capacity", 30);
		map.put("appendflag","reset");
		
		return map;
	}
	
	/**
	 * @param map
	 * @return
	 * 
	 * 开始自动编排准考证信息
	 * 
	 */
	@RemoteMethod
	public String startArrange(Map map){
		String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		return this.examNumEditService.startArrange(map);
	}
}
