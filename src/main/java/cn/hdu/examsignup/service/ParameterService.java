package cn.hdu.examsignup.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sun.xml.internal.stream.Entity;

import cn.hdu.examsignup.dao.ParameterDao;
import cn.hdu.examsignup.model.ExParameter;

@Service
@Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
public class ParameterService {
	
	@Autowired
	private ParameterDao parameterdao;
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public ExParameter saveParameter(ExParameter parameter){
		try{
			ExParameter entity = new ExParameter();
			entity.setName(parameter.getName());
			entity.setParamvalue(parameter.getParamvalue());
			entity.setParamtype(parameter.getParamtype());
			entity.setMemo(parameter.getMemo());
			entity.setEnumvalue(parameter.getEnumvalue());
			parameterdao.save(entity);
			return parameterdao.findById(entity.getId());
		}catch(Exception e){
			System.out.println("saveParameter error" + e.getMessage());
			return null;
		}

	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public ExParameter updateParameter(ExParameter parameter){
		try{
			ExParameter entity = parameterdao.findById(parameter.getId());
			entity.setName(parameter.getName());
			entity.setParamvalue(parameter.getParamvalue());
			entity.setParamtype(parameter.getParamtype());
			entity.setMemo(parameter.getMemo());
			entity.setEnumvalue(parameter.getEnumvalue());
			parameterdao.update(entity);
			return parameterdao.findById(entity.getId());
		}catch(Exception e){
			System.out.println("updateParameter error" + e.getMessage());
			return null;
		}

	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public List getParamGroup(String paramtype){
		try{
			return parameterdao.getParamGroup(paramtype);
		}catch(Exception e){
			System.out.println("getParamGroup error" + e.getMessage());
			return null;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public ExParameter findParameterByType(String paramtype){
		try {
			return parameterdao.findByProperty("paramtype", paramtype).get(0);
		} catch (Exception e) {
			System.out.println("findParameterByType error" + e.getMessage());
			return null;
		}
	}
}
