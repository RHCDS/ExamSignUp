package cn.hdu.examsignup.controller;

import java.util.List;
import java.util.Map;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.spring.SpringCreator;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.hdu.examsignup.service.UserService;

@RemoteProxy(creator = SpringCreator.class)
public class UserController {

	@Autowired
	private UserService userservice;
	
	@RemoteMethod
	public List<Map> getAllUserByType(String uertype){
		return userservice.getAllUserByType(uertype);
	}
	
	@RemoteMethod
	public String saveUser(JSONObject user, String roletype){
		return userservice.saveUser(user,roletype);
	}
	
	@RemoteMethod
	public boolean deleteUsers(List<String> ids){
		return userservice.deleteUsers(ids);
	}
}
