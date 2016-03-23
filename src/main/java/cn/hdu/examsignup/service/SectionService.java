package cn.hdu.examsignup.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.hdu.examsignup.dao.SectionDao;
import cn.hdu.examsignup.dao.TheoryExamArrangeDao;
import cn.hdu.examsignup.model.ExInstitution;
import cn.hdu.examsignup.model.ExSection;

@Service
public class SectionService {
	@Autowired
	private SectionDao sectionDao;
	
	@Autowired
	TheoryExamArrangeDao arrangementdao;

	private Logger logger = LoggerFactory.getLogger(SectionService.class);

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> getPageSections(String institutionnum, String pageNum,
			String pageSize, String theoryOrOperate) {
		try {
			return sectionDao.getPageSections(institutionnum,
					Integer.parseInt(pageNum), Integer.parseInt(pageSize),theoryOrOperate);
		} catch (Exception e) {
			System.out.println("getPageSections error:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> getAllSections(String institutionnum) {
		try {
			return sectionDao.getAllSections(institutionnum);
		} catch (Exception e) {
			System.out.println("getPageSections error:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Integer getSectionTotalCount(String institutionnum,String theoryOrOperate) {
		try {
			return sectionDao.getSectionTotal(institutionnum,theoryOrOperate).size();
		} catch (Exception e) {
			System.out.println("getSectionTotalCount error:" + e.getMessage());
			e.printStackTrace();
			return 0;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public boolean deleteSections(List<String> ids) {
		try {
			for (String id : ids) {
				ExSection entity = sectionDao.findById(id);
				if(arrangementdao.findByProperty("exSection", entity).size()==0){
					sectionDao.delete(entity);
				}else
					return false;
			}
			return true;
		} catch (Exception e) {
			System.out.println("deleteSections error!" + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> getSectionTotalWithSectionnum(String institutionnum,String theoryOrOperate,Integer sectionnum) {
		return this.sectionDao.getSectionTotalWithSectionnum(institutionnum, theoryOrOperate, sectionnum);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String saveSection(JSONObject section, ExInstitution institution) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date startTime = null;
		try {
			startTime = dateFormat.parse(section.get("dateValue").toString()+" "+section.get("timeValue").toString());
		} catch (ParseException e1) {
			e1.printStackTrace();
			return null;
		}
		try {
			ExSection entity = new ExSection();
			entity.setExArrangements(null);
			entity.setExInstitution(institution);
			entity.setSectionnum(Integer.parseInt(section.get("sectionnum")
					.toString().trim()));
			entity.setStarttime(startTime);

			if (section.get("theoryflag").toString().equals("1")) {
				entity.setTheoryflag("1");
				entity.setOperateflag("0");
			} else {
				entity.setTheoryflag("0");
				entity.setOperateflag("1");
			}
		/*	String valideResult = sectionDao.validateSection(entity,institution);
			if(valideResult!=null)
				return valideResult; */
			sectionDao.save(entity);
			return "{ success: true, errors:{info: '成功插入场次信息。'}}";
		} catch (Exception e) {
			System.out.println("saveSection error!" + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String updateSection(JSONObject section,
			ExInstitution institution) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date startTime = null;
		try {
			startTime = dateFormat.parse(section.get("dateValue").toString()+" "+section.get("timeValue").toString());
		} catch (ParseException e1) {
			e1.printStackTrace();
			return null;
		}
		try {
			ExSection entity = sectionDao.findById(section.get("id")
					.toString());
			if (entity == null)
				return null;
			entity.setExInstitution(institution);
			entity.setSectionnum(Integer.parseInt(section.get("sectionnum")
					.toString().trim()));
			entity.setStarttime(startTime);

			if (section.get("theoryflag").toString().equals("1")) {
				entity.setTheoryflag("1");
				entity.setOperateflag("0");
			} else {
				entity.setTheoryflag("0");
				entity.setOperateflag("1");
			}
	/*		String valideResult = sectionDao.validateSection(entity,institution);
			if(valideResult!=null)
				return valideResult; */
			sectionDao.save(entity);
			return "{ success: true, errors:{info: '成功修改场次信息。'}}";
		} catch (Exception e) {
			System.out.println("updateSection error!" + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public ExSection getSectionBySectionnumAndType(String institutionnum,String sectionnum,
			String theoryOrOperate){
		try {
			return sectionDao.getSectionBySectionnumAndType(institutionnum, sectionnum, theoryOrOperate);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
