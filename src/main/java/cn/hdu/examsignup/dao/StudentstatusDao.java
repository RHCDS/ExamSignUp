package cn.hdu.examsignup.dao;

import cn.hdu.examsignup.model.ExStudentstatus;

public class StudentstatusDao extends AbstractHibernateDao<ExStudentstatus> {

	StudentstatusDao() {
		super(ExStudentstatus.class);
	}

}
