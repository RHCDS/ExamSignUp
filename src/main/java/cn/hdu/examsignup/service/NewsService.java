package cn.hdu.examsignup.service;

import cn.hdu.examsignup.dao.NewsDao;
import java.util.List;
import java.util.Map;

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
import cn.hdu.examsignup.model.ExNews;
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class NewsService {

	@Autowired
	private NewsDao newsdao;
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> loadNewsByInstitution(String institutionnum) {
		return newsdao.loadNewsByInstitution(institutionnum);
	}
}