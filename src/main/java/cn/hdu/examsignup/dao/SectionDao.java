package cn.hdu.examsignup.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import cn.hdu.examsignup.model.ExInstitution;
import cn.hdu.examsignup.model.ExSection;

public class SectionDao extends AbstractHibernateDao<ExSection> {

	SectionDao() {
		super(ExSection.class);
	}

	public List<Map> getPageSections(String institutionnum, int pageNum,
			int pageSize, String theoryOrOperate) {
		String hql = "select new map(t.id as id,t.sectionnum as sectionnum,t.theoryflag as theoryflag,t.operateflag as operateflag,t.starttime as starttime) "
				+ "from cn.hdu.examsignup.model.ExSection t "
				+ "where t.exInstitution.institutionnum =:institutionnum and t.theoryflag=:theoryflag and t.operateflag=:operateflag order by t.sectionnum";
		Query query = null;
		if (theoryOrOperate.equals("theory")) {
			query = this.getCurrentSession().createQuery(hql)
					.setParameter("institutionnum", institutionnum)
					.setFirstResult(pageNum).setMaxResults(pageSize)
					.setParameter("theoryflag", "1")
					.setParameter("operateflag", "0");
		} else {
			query = this.getCurrentSession().createQuery(hql)
					.setParameter("institutionnum", institutionnum)
					.setFirstResult(pageNum).setMaxResults(pageSize)
					.setParameter("theoryflag", "0")
					.setParameter("operateflag", "1");
		}
		List<Map> result = (List<Map>) query.list();
		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		for (Map element : result) {
			Date startTime = (Date) element.get("starttime");
			if (element.get("starttime") != null) {
				element.put("dateValue", dataFormat.format(startTime));
				element.put("timeValue", timeFormat.format(startTime));
				element.remove("starttime");
			}
		}
//		System.out.println("********************************************11111111111111111111111111111111111");
//		System.out.println(result);
//		System.out.println("********************************************11111111111111111111111111111111111");
		return result;
	}
	public List<Map> getAllSections(String institutionnum) {
		String hql = "select new map(t.id as id,t.sectionnum as sectionnum,t.theoryflag as theoryflag,t.operateflag as operateflag,t.starttime as starttime) "
				+ "from cn.hdu.examsignup.model.ExSection t "
				+ "where t.exInstitution.institutionnum =:institutionnum  order by t.sectionnum";
		Query query = null;
		
		query = this.getCurrentSession().createQuery(hql)
					.setParameter("institutionnum", institutionnum);

		List<Map> result = (List<Map>) query.list();
		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		for (Map element : result) {
			Date startTime = (Date) element.get("starttime");
			if (element.get("starttime") != null) {
				element.put("dateValue", dataFormat.format(startTime));
				element.put("timeValue", timeFormat.format(startTime));
				element.remove("starttime");
			}
		}
		return result;
	}
	public List<Map> getSectionTotal(String institutionnum,
			String theoryOrOperate) {
		String hql = "select new map(t.id as id,t.sectionnum as sectionnum,t.theoryflag as theoryflag,t.operateflag as operateflag,t.starttime as starttime) "
				+ "from cn.hdu.examsignup.model.ExSection t "
				+ "where t.exInstitution.institutionnum =:institutionnum and t.theoryflag=:theoryflag and t.operateflag=:operateflag order by t.sectionnum";
		Query query = null;
		if (theoryOrOperate.equals("theory")) {
			query = this.getCurrentSession().createQuery(hql)
					.setParameter("institutionnum", institutionnum)
					.setParameter("theoryflag", "1")
					.setParameter("operateflag", "0");
		} else {
			query = this.getCurrentSession().createQuery(hql)
					.setParameter("institutionnum", institutionnum)
					.setParameter("theoryflag", "0")
					.setParameter("operateflag", "1");
		}
		return query.list();
	}

	public List<Map> getSectionTotalWithSectionnum(String institutionnum,
			String theoryOrOperate, Integer sectionnum) {
		String hql = "select new map(t.id as id,t.sectionnum as sectionnum,t.theoryflag as theoryflag,t.operateflag as operateflag,t.starttime as starttime) "
				+ "from cn.hdu.examsignup.model.ExSection t "
				+ "where t.exInstitution.institutionnum =:institutionnum and t.theoryflag=:theoryflag and t.operateflag=:operateflag and t.sectionnum=:sectionnum"
				+ " order by t.sectionnum";
		Query query = null;
		if (theoryOrOperate.equals("theory")) {
			query = this.getCurrentSession().createQuery(hql)
					.setParameter("institutionnum", institutionnum)
					.setParameter("theoryflag", "1")
					.setParameter("operateflag", "0")
					.setParameter("sectionnum", sectionnum);
		} else {
			query = this.getCurrentSession().createQuery(hql)
					.setParameter("institutionnum", institutionnum)
					.setParameter("theoryflag", "0")
					.setParameter("operateflag", "1")
					.setParameter("sectionnum", sectionnum);
		}
		return query.list();
	}

	public String validateSection(ExSection target, ExInstitution institution) {
		String hql = "select new map(t.id as id,t.sectionnum as sectionnum,t.theoryflag as theoryflag,t.operateflag as operateflag,t.starttime as starttime) "
				+ "from cn.hdu.examsignup.model.ExSection t "
				+ "where t.exInstitution.institutionnum =:institutionnum and t.theoryflag=:theoryflag and t.operateflag=:operateflag "
				+ "order by t.sectionnum";
		Query query = this
				.getCurrentSession()
				.createQuery(hql)
				.setParameter("institutionnum", institution.getInstitutionnum())
				.setParameter("theoryflag", target.getTheoryflag())
				.setParameter("operateflag", target.getOperateflag());
		List<Map> result = query.list();
		// 数据空中记录为空
		if (result.size() == 0)
			return null;
		Integer current = 0;
		for (Map element : result) {
			if ((Integer) element.get("sectionnum") >= target.getSectionnum()) {
				break;
			}
			current++;
		}
		// 未查找到考场号大于等于target的记录，
		if (current == result.size()) {
			if (!target.getStarttime().before(
					(Date) result.get(result.size() - 1).get("starttime")))
				return null;
			else
				return "{ success: false, errors:{info: '设置的场次开始时间早于第"
				+ result.get(result.size() - 1).get("sectionnum")
				+ "场开始时间，请重新设置时间！'}}";
		}
		// 相同考场号，不同的id
		if (result.get(current).get("sectionnum").toString().trim()
				.equals(target.getSectionnum().toString())
				&& !(result.get(current).get("id").toString().trim()
						.equals(target.getId()))) {
			return "{ success: false, errors:{info: '已经存在相同场次序号的记录，请填写其他场次号！'}}";
		}
		// 修改原始记录
		if (result.get(current).get("sectionnum").toString().trim()
				.equals(target.getSectionnum().toString())) {
			//第一条且系统中只有一个条目
			if (current == 0 ) {
				if(result.size()==1)
					return null;
				else
				{
					if (!target.getStarttime().before(
							(Date) result.get(current + 1).get("starttime"))) {
						return "{ success: false, errors:{info: '设置的场次开始时间晚于第"
								+ result.get(current + 1).get("sectionnum")
								+ "场开始时间，请重新设置时间！'}}";
					}
					return null;
				}
			}
			//中间条目
			if (current != 0 && current != (result.size() - 1)) {//这里判断应该与上一个if是else的关系，但这样写也无伤大雅
				if (!target.getStarttime().after(
						(Date) result.get(current - 1).get("starttime"))) {
					return "{ success: false, errors:{info: '设置的场次开始时间早于第"
							+ result.get(current - 1).get("sectionnum")
							+ "场开始时间，请重新设置时间！'}}";
				}
				if (!target.getStarttime().before(
						(Date) result.get(current + 1).get("starttime"))) {
					return "{ success: false, errors:{info: '设置的场次开始时间晚于第"
							+ result.get(current + 1).get("sectionnum")
							+ "场开始时间，请重新设置时间！'}}";
				}
				return null;
			}
			//最后一条
			if (current == (result.size() - 1)) {
				if (!target.getStarttime().after(
						(Date) result.get(current - 1).get("starttime"))) {
					return "{ success: false, errors:{info: '设置的场次开始时间早于第"
							+ result.get(current - 1).get("sectionnum")
							+ "场开始时间，请重新设置时间！'}}";
				}
				return null;
			}
			if (!target.getStarttime().before(
					(Date) result.get(current + 1).get("starttime"))) {
				return "{ success: false, errors:{info: '设置的场次开始时间晚于第"
						+ result.get(current + 1).get("sectionnum")
						+ "场开始时间，请重新设置时间！'}}";
			}
			if (!target.getStarttime().after(
					(Date) result.get(current - 1).get("starttime"))) {
				return "{ success: false, errors:{info: '设置的场次开始时间早于第"
						+ result.get(current - 1).get("sectionnum")
						+ "场开始时间，请重新设置时间！'}}";
			}
			return null;
		}
		// 插入新的记录
		if (current == 0) {
			if (!target.getStarttime().before(
					(Date) result.get(0).get("starttime"))) {
				return "{ success: false, errors:{info: '设置的场次开始时间晚于第"
						+ result.get(0).get("sectionnum") + "场开始时间，请重新设置时间！'}}";
			}
			return null;
		}
		if (!target.getStarttime().before(
				(Date) result.get(current).get("starttime"))) {
			return "{ success: false, errors:{info: '设置的场次开始时间晚于第"
					+ result.get(current).get("sectionnum")
					+ "场开始时间，请重新设置时间！'}}";
		}
		if (!target.getStarttime().after(
				(Date) result.get(current - 1).get("starttime"))) {
			return "{ success: false, errors:{info: '设置的场次开始时间早于第"
					+ result.get(current - 1).get("sectionnum")
					+ "场开始时间，请重新设置时间！'}}";
		}
		return null;
	}

	public ExSection getSectionBySectionnumAndType(String institutionnum,String sectionnum,
			String theoryOrOperate) {
		List<ExSection> result;
		String hql = "select e from cn.hdu.examsignup.model.ExSection e " +
				"where e.sectionnum=:sectionnum and e.theoryflag=:theoryflag and e.operateflag=:operateflag and e.exInstitution.institutionnum =:institutionnum";
		Query query = this.getCurrentSession().createQuery(hql);
		query.setParameter("sectionnum", Integer.parseInt(sectionnum.trim()));
		query.setParameter("institutionnum", institutionnum);
		if (theoryOrOperate.equals("theory")) {
			query.setParameter("theoryflag", "1").setParameter("operateflag",
					"0");
		} else {
			query.setParameter("theoryflag", "0").setParameter("operateflag",
					"1");
		}
		result = query.list();
		if (result.size() == 1)
			return result.get(0);
		return null;
	}
	public void clearData(String institutionnum) {
		String hql;
		Query query;
		hql = "select a.id from cn.hdu.examsignup.model.ExSection a where " +
				"a.exInstitution.institutionnum=:institutionnum";
		query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum);
		List deleteList = query.list();
		if(deleteList.size() == 0) return;
		hql = "delete from cn.hdu.examsignup.model.ExSection a " +
				"where a.id in(:list)";
		query = this.getCurrentSession().createQuery(hql).setParameterList("list", deleteList);
		query.executeUpdate();
	
	}
}
