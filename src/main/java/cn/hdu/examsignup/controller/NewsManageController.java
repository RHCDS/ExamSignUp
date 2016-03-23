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

import cn.hdu.examsignup.model.ExNews;
import cn.hdu.examsignup.service.NewsService;

@RemoteProxy(creator = SpringCreator.class)
public class NewsManageController {
	@Autowired
	NewsService newsservice;
	
	@RemoteMethod
	public Map loadNewsByInstitution() {
		String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		List<Map> result = newsservice.loadNewsByInstitution(institutionnum);
		Map map = new HashMap();
		map.put("totalProperty", result.size());
		map.put("root", result);
		return map;
	}
}
