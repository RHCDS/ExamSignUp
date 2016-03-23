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

import cn.hdu.examsignup.service.ClearHistoryDataService;

/**
 * 主要管理数据清除功能
 *
 */
@RemoteProxy(creator = SpringCreator.class)
public class ClearHistoryDataController {
	@Autowired
	private ClearHistoryDataService clearHistoryDataService;
	
	/**
	 * @return
	 * 高校端用户清空历史数据
	 */
	@RemoteMethod
	public String clearHistoryData() {
		// 获取当前登录高校的机构编号，以便根据编号删除历史数据信息
		String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		return this.clearHistoryDataService.clear(institutionnum);
		
	}
	
	/**
	 * @return
	 * 考试院服务端删除全省历史信息
	 */
	@RemoteMethod
	public String clearProvinceStuInfo(){
		return clearHistoryDataService.clearProvinceStuInfo();
	}
	
	
	/**
	 * @return
	 * 高校端取消考务考场安排信息
	 * 
	 * 主要用于高校清除考务管理数据
	 * 
	 */
	@RemoteMethod
	public String clearExamManageData() {
		String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		return this.clearHistoryDataService.clearExamManageData(institutionnum);
	}
}
