package cn.hdu.examsignup.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import cn.hdu.examsignup.model.ExArrangeSupervisor;
import cn.hdu.examsignup.model.ExArrangement;
import cn.hdu.examsignup.model.ExInstitution;
import cn.hdu.examsignup.model.ExSupervisor;

public class ArrangeSupervisorDao extends
		AbstractHibernateDao<ExArrangeSupervisor> {
	ArrangeSupervisorDao() {
		super(ExArrangeSupervisor.class);
	}
	public List getSupervisorByArrangeid(String arrangeid,String institutionnum) {
		String hql = "select a.exSupervisor.name from cn.hdu.examsignup.model.ExArrangeSupervisor a " +
				"where a.exArrangement.id=:arrangeid and a.exInstitution.institutionnum=:institutionnum";
		Query query =  this.getCurrentSession().createQuery(hql).setParameter("arrangeid", arrangeid)
				.setParameter("institutionnum", institutionnum);
		return query.list();
	}
	public List getSupervisorByExamroomnum(String examroomnum,String institutionnum) {
		String hql = "select a.exSupervisor.name from cn.hdu.examsignup.model.ExArrangeSupervisor a " +
				"where a.logicExamroomNum=:examroomnum and a.exInstitution.institutionnum=:institutionnum";
		Query query =  this.getCurrentSession().createQuery(hql).setParameter("examroomnum", examroomnum)
				.setParameter("institutionnum", institutionnum);
		return query.list();
	}
	//上机
	public String deleteArrangedSupervisor(List<Map> supervisors,String arrangeid) {
		String hql;
		Query query;
		for(Map supervisor:supervisors) {
			String supervisorid = supervisor.get("id").toString();
			hql = "select a from cn.hdu.examsignup.model.ExArrangeSupervisor a " +
					"where a.exArrangement.id=:arrangeid and a.exSupervisor.id=:supervisorid";
			try {
				query = this.getCurrentSession().createQuery(hql).setParameter("arrangeid", arrangeid)
						.setParameter("supervisorid", supervisorid);
				ExArrangeSupervisor exArrangeSupervisor = (ExArrangeSupervisor) query.list().get(0);
				this.delete(exArrangeSupervisor);
			} catch(Exception e) {
				System.out.println(e.getMessage());
				return "{success: false,errors: {info: '操作失败！'}}";
			}
		}
		return "{success: true,errors: {info: '操作成功！'}}";
	}
	//理论
	public String deleteArrangedSupervisor(List<Map> supervisors,String examroomnum,String institutionnum) {
		String hql;
		Query query;
		for(Map supervisor:supervisors) {
			String supervisorid = supervisor.get("id").toString();
			hql = "select a from cn.hdu.examsignup.model.ExArrangeSupervisor a " +
					"where a.logicExamroomNum=:examroomnum and a.exSupervisor.id=:supervisorid and " +
					"a.exInstitution.institutionnum=:institutionnum";
			try {
				query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum)
						.setParameter("supervisorid", supervisorid)
						.setParameter("examroomnum", examroomnum);
				ExArrangeSupervisor exArrangeSupervisor = (ExArrangeSupervisor) query.list().get(0);
				this.delete(exArrangeSupervisor);
			} catch(Exception e) {
				System.out.println(e.getMessage());
				return "{success: false,errors: {info: '操作失败！'}}";
			}
		}
		return "{success: true,errors: {info: '操作成功！'}}";
	}
	//安排上机监考老师
	public String arrangeSupervisor(String supervisorid,String arrangeid,String institutionnum) {
		String hql;
		Query query;
		ExArrangeSupervisor entity = new ExArrangeSupervisor();
		try {
			hql = "select a from cn.hdu.examsignup.model.ExSupervisor a " +
					"where a.id=:supervisorid";
			query = this.getCurrentSession().createQuery(hql).setParameter("supervisorid", supervisorid);
			ExSupervisor supervisor = (ExSupervisor) query.list().get(0);
			
			hql = "select a from cn.hdu.examsignup.model.ExArrangement a " +
					"where a.id=:arrangeid";
			query = this.getCurrentSession().createQuery(hql).setParameter("arrangeid", arrangeid);
			ExArrangement arrangement = (ExArrangement) query.list().get(0);
			
			hql = "select a from cn.hdu.examsignup.model.ExInstitution a " +
					"where a.institutionnum=:institutionnum";
			query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum);
			ExInstitution institution = (ExInstitution) query.list().get(0);
			
			entity.setExArrangement(arrangement);
			entity.setExInstitution(institution);
			entity.setExSupervisor(supervisor);
			
			this.save(entity);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return "{success: false,errors: {info: '操作失败！'}}";
		}
		return "{success: true,errors: {info: '操作成功！'}}";
	}
	//安排理论监考老师
	public String arrangeSupervisor(String supervisorid,String examroomnum,String arrangeid,String institutionnum) {
		String hql;
		Query query;
		ExArrangeSupervisor entity = new ExArrangeSupervisor();
		try {
			hql = "select a from cn.hdu.examsignup.model.ExSupervisor a " +
					"where a.id=:supervisorid";
			query = this.getCurrentSession().createQuery(hql).setParameter("supervisorid", supervisorid);
			ExSupervisor supervisor = (ExSupervisor) query.list().get(0);
			
			hql = "select a from cn.hdu.examsignup.model.ExArrangement a " +
					"where a.id=:arrangeid";
			query = this.getCurrentSession().createQuery(hql).setParameter("arrangeid", arrangeid);
			ExArrangement arrangement = (ExArrangement) query.list().get(0);
			
			hql = "select a from cn.hdu.examsignup.model.ExInstitution a " +
					"where a.institutionnum=:institutionnum";
			query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum);
			ExInstitution institution = (ExInstitution) query.list().get(0);
			entity.setLogicExamroomNum(examroomnum);
			entity.setExArrangement(arrangement);
			entity.setExInstitution(institution);
			entity.setExSupervisor(supervisor);
			
			this.save(entity);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return "{success: false,errors: {info: '操作失败！'}}";
		}
		return "{success: true,errors: {info: '操作成功！'}}";
	}
	//上机boolean
	public boolean arrangeOneSupervisor(String supervisorid,String arrangeid,String institutionnum) {
		String hql;
		Query query;
		ExArrangeSupervisor entity = new ExArrangeSupervisor();
		try {
			hql = "select a from cn.hdu.examsignup.model.ExSupervisor a " +
					"where a.id=:supervisorid";
			query = this.getCurrentSession().createQuery(hql).setParameter("supervisorid", supervisorid);
			ExSupervisor supervisor = (ExSupervisor) query.list().get(0);
			
			hql = "select a from cn.hdu.examsignup.model.ExArrangement a " +
					"where a.id=:arrangeid";
			query = this.getCurrentSession().createQuery(hql).setParameter("arrangeid", arrangeid);
			ExArrangement arrangement = (ExArrangement) query.list().get(0);
			
			hql = "select a from cn.hdu.examsignup.model.ExInstitution a " +
					"where a.institutionnum=:institutionnum";
			query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum);
			ExInstitution institution = (ExInstitution) query.list().get(0);
			
			entity.setExArrangement(arrangement);
			entity.setExInstitution(institution);
			entity.setExSupervisor(supervisor);
			
			this.save(entity);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}
	//返回类型，理论
	public boolean arrangeOneSupervisor(String supervisorid,String examroomnum,String arrangeid,String institutionnum) {
		String hql;
		Query query;
		ExArrangeSupervisor entity = new ExArrangeSupervisor();
		try {
			hql = "select a from cn.hdu.examsignup.model.ExSupervisor a " +
					"where a.id=:supervisorid";
			query = this.getCurrentSession().createQuery(hql).setParameter("supervisorid", supervisorid);
			ExSupervisor supervisor = (ExSupervisor) query.list().get(0);
			
			hql = "select a from cn.hdu.examsignup.model.ExArrangement a " +
					"where a.id=:arrangeid";
			query = this.getCurrentSession().createQuery(hql).setParameter("arrangeid", arrangeid);
			ExArrangement arrangement = (ExArrangement) query.list().get(0);
			
			hql = "select a from cn.hdu.examsignup.model.ExInstitution a " +
					"where a.institutionnum=:institutionnum";
			query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum);
			ExInstitution institution = (ExInstitution) query.list().get(0);
			entity.setLogicExamroomNum(examroomnum);
			entity.setExArrangement(arrangement);
			entity.setExInstitution(institution);
			entity.setExSupervisor(supervisor);
			
			this.save(entity);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}
	public String cancelArrange(String institutionnum,String operateOrTheory) {
		String hql;
		Query query;
		hql = "select a.id from cn.hdu.examsignup.model.ExArrangeSupervisor a where " +
				"a.exInstitution.institutionnum=:institutionnum and a.exSupervisor.";

		if(operateOrTheory.equals("operate")) {
			hql += "operateflag='1'";
		} else {
			hql += "theoryflag='1'";
		}
		query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum);
		List deleteList = query.list();
		if(deleteList.size() == 0 ) {
			return "{success: true,errors: {info: '操作成功！'}}";
		}
		
		hql = "delete from cn.hdu.examsignup.model.ExArrangeSupervisor a where " +
				"a.id in (:list)";
		query = this.getCurrentSession().createQuery(hql).setParameterList("list", deleteList);
		try {
			query.executeUpdate();
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return "{success: false,errors: {info: '操作失败！'}}";
		}
		return "{success: true,errors: {info: '操作成功！'}}";
	}
	public void clearData(String institutionnum) {
		String hql;
		Query query;
		hql = "select a.id from cn.hdu.examsignup.model.ExArrangeSupervisor a where " +
				"a.exInstitution.institutionnum=:institutionnum";
		query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum);
		List deleteList = query.list();
		if(deleteList.size() == 0) return;
		hql = "delete from cn.hdu.examsignup.model.ExArrangeSupervisor a " +
				"where a.id in(:list)";
		query = this.getCurrentSession().createQuery(hql).setParameterList("list", deleteList);
		query.executeUpdate();
	
	}
	
	//查看上机考试安排情况
	public String checkArrangeStatus(String institutionnum) {
		String hql = "select a.id from cn.hdu.examsignup.model.ExStudent a " +
				"where a.exArrangementByOperatearrangeid is null and " +
				"a.exInstitution.institutionnum=:institutionnum and " +
				"a.exLanguage.operateflag='1' and " +
				"a.examnum is not null";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum);
		int flag = query.list().size();
		if(flag>0) {
			return "false";
		}
		return "true";
	}
}
