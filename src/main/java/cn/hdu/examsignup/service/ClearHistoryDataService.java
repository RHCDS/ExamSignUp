package cn.hdu.examsignup.service;
import java.util.List;
import java.util.Map;

import org.directwebremoting.WebContextFactory;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.hdu.examsignup.dao.ArrangeSupervisorDao;
import cn.hdu.examsignup.dao.CampusDao;
import cn.hdu.examsignup.dao.CollegeDao;
import cn.hdu.examsignup.dao.InstitutionDao;
import cn.hdu.examsignup.dao.InstitutionstatusDao;
import cn.hdu.examsignup.dao.OperateExamArrangeDao;
import cn.hdu.examsignup.dao.PhysiceexamroomDao;
import cn.hdu.examsignup.dao.ProfessionDao;
import cn.hdu.examsignup.dao.SectionDao;
import cn.hdu.examsignup.dao.StudentDao;
import cn.hdu.examsignup.dao.SupervisorDao;
import cn.hdu.examsignup.dao.TheoryExamArrangeDao;
import cn.hdu.examsignup.model.ExInstitution;

@Service
public class ClearHistoryDataService {
	@Autowired 
	private ArrangeSupervisorDao arrangeSupervisorDao;
	@Autowired 
	private TheoryExamArrangeDao theoryExamArrangeDao;
	@Autowired 
	private OperateExamArrangeDao operateExamArrangeDao;
	@Autowired 
	private SectionDao sectionDao;
	@Autowired 
	private SupervisorDao supervisorDao;
	@Autowired 
	private StudentDao studentDao;
	@Autowired 
	private PhysiceexamroomDao physiceexamroomDao;
	@Autowired 
	private CampusDao campusDao;
	@Autowired
	private CollegeDao collegeDao;
	@Autowired
	private ProfessionDao professionDao;
	@Autowired
	private InstitutionDao institutiondao;
	@Autowired
	private InstitutionstatusDao institutionstatusDao;
	
	
	/**
	 * @param institutionnum
	 * @return
	 * 
	 * 高校清楚本校历史数据信息
	 * 主要删除：
	 * 		1、高校学生信息
	 * 		2、考场安排以及考场信息
	 * 		3、监考员信息
	 * 		4、校区学院专业等信息
	 * 
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String clear(String institutionnum) {
		try {
			// 取消学生的理论和上机考试的安排,由于设置外键，所以先取消学生的考场安排设置null
			this.studentDao.cancelAllArrangement(institutionnum);
			// 删除本校的学生信息
			this.studentDao.clearData(institutionnum);
			// 删除监考员安排信息
			this.arrangeSupervisorDao.clearData(institutionnum);
			// 删除理论和上机考场安排信息
			this.theoryExamArrangeDao.clearData(institutionnum);
			this.operateExamArrangeDao.clearData(institutionnum);
			// 删除安排考场信息
			this.sectionDao.clearData(institutionnum);
			// 删除考场信息
			this.physiceexamroomDao.clearData(institutionnum);
			// 删除监考员信息
			this.supervisorDao.clearData(institutionnum);
			// 删除校区信息
			this.campusDao.clearData(institutionnum);
			// 删除学院信息
			this.collegeDao.clearData(institutionnum);
			// 删除专业信息
			this.professionDao.clearData(institutionnum);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return "{success:false ,errors: {info: '操作失败！'}}";
		}
		return "{success:true ,errors: {info:'清除考生数据成功！'}}";
	}
	
	
	/**
	 * @param institutionnum
	 * @return
	 * 高校端取消考务考场安排信息
	 * 
	 * 主要用于高校想重新编排考场信息
	 * 
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String clearExamManageData(String institutionnum) {
		try {
			this.studentDao.cancelAllArrangement(institutionnum);
			this.arrangeSupervisorDao.clearData(institutionnum);
			
			this.operateExamArrangeDao.clearData(institutionnum);
		}  catch(Exception e) {
			System.out.println(e.getMessage());
			return "{success:false ,errors: {info: '操作失败！'}}";
		}
		return "{success:true ,errors: {info:'清除考务管理数据成功！'}}";
	}
	
	/**
	 * @return
	 * 
	 * 考试院服务端删除全省历史信息
	 * 主要循环删除：
	 * 		1.各高校上报学生数据
	 * 		2.设置高校状态为未上报数据
	 * 
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String clearProvinceStuInfo() {
		try {
			// 获取全省所有高校机构编号信息
			List<String> institutionnums = (List<String>)institutiondao.getAllInstitutionnum();
			// 按照高校编号清除历史信息
			for(String institutionnum : institutionnums){
				// 首先删除该高校上报学生信息
				this.studentDao.clearData(institutionnum);
				// 获取该高校机构并设置状态为未上报数据状态
				List<ExInstitution> institutionlist = this.institutiondao.findByProperty("institutionnum", institutionnum);
				for(ExInstitution institution : institutionlist){
					institution.setInstitutionstatus(this.institutionstatusDao.findByProperty("statusnum", 1).get(0));
					institutiondao.update(institution);
				}
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return "{success:false ,errors: {info: '操作失败！'}}";
		}
		return "{success:true ,errors: {info:'清除考生数据成功！'}}";
	}
}
