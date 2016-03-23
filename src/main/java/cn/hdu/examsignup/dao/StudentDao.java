package cn.hdu.examsignup.dao;

import cn.hdu.examsignup.model.ExArrangement;
import cn.hdu.examsignup.model.ExStudent;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.simple.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class StudentDao extends AbstractHibernateDao<ExStudent> {
    StudentDao() {
        super(ExStudent.class);
    }

    public boolean validateStudent(String studentID, String IDnum, String school) {
        String hql = "select e from cn.hdu.examsignup.model.ExStudent e where e.studentnum=:studentID and idnum=:IDnum and e.exInstitution.institutionnum=:school and e.exInstitution.category='school'";
        Query query = this.getCurrentSession().createQuery(hql);
        query.setParameter("studentID", studentID);
        query.setParameter("IDnum", IDnum);
        query.setParameter("school", school);
        List<ExStudent> result = query.list();
        if (result.size() == 1)
            return true;
        return false;
    }

    public String validateStudent(String studentID, String IDnum) {
        String hql = "select e from cn.hdu.examsignup.model.ExStudent e where e.studentnum=:studentID and idnum=:IDnum and e.exInstitution.category='school'";
        Query query = this.getCurrentSession().createQuery(hql);
        query.setParameter("studentID", studentID);
        query.setParameter("IDnum", IDnum);
        List<ExStudent> result = query.list();
        if (result.size() == 1)
            return result.get(0).getExInstitution().getInstitutionnum();
        return null;
    }

    public long getStudentsTotalCount(String whereString, Map<String, Object> valuesMap, String schoolnum) {
        String hql = "select student.id from cn.hdu.examsignup.model.ExStudent student ";
        hql += whereString;
        hql += " and student.exInstitution.institutionnum =:schoolnum and student.exLanguage is not null" +
                " and student.paied='1'";
        Query query = this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum);
        String[] perchs = valuesMap.keySet().toArray(new String[0]);
        for (int i = 0; i < perchs.length; i++) {
            query.setParameter(perchs[i], valuesMap.get(perchs[i]));
        }
        return query.list().size();
    }

    public List<Map> getPageStudents(String whereString, Map<String, Object> valuesMap, int pageNum, int pageSize, String schoolnum) {
        String hql = "select new map(student.id as id, student.exInstitution.name as exInstitution, student.exLanguage.name as exLanguage, student.exProfession.name as exProfession," +
                " student.exCollege.name as exCollege," +
                " student.exCampus.name as exCampus, student.name as name, student.password as password," +
                " student.sex as sex, student.studentnum as studentnum, student.idnum as idnum, student.ethno as ethno," +
                " student.examnum as examnum, student.exambatch as exambatch, student.grade as grade, student.classnum as classnum," +
                " student.lengthofyear as lengthofyear, student.studentcategory as studentcategory, student.paied as paied," +
                " student.score as score, student.theoryabsent as theoryabsent, student.operateabsent as operateabsent," +
                " student.theoryfraud as theoryfraud, student.operatefraud as operatefraud) " +
                "from cn.hdu.examsignup.model.ExStudent student";
        hql += whereString;
        hql += " and student.exInstitution.institutionnum =:schoolnum " +
                "and student.paied='1' " +
                "and  student.exLanguage is not null order by student.examnum,student.exLanguage.languagenum,student.exCampus.campusnum,student.studentcategory,student.studentnum ";
        Query query = this.getCurrentSession().createQuery(hql).setFirstResult(pageNum).setMaxResults(pageSize).setParameter("schoolnum", schoolnum);
        String[] perchs = valuesMap.keySet().toArray(new String[0]);
        for (int i = 0; i < perchs.length; i++) {
            query.setParameter(perchs[i], valuesMap.get(perchs[i]));
        }
//		List<Map> results = new ArrayList<Map>();
//		String hqlForNullLanguage = "select new map(student.id as id, student.exInstitution.name as exInstitution,  student.exProfession.name as exProfession," +
//				" student.exCollege.name as exCollege," +
//				" student.exCampus.name as exCampus, student.name as name, student.password as password," +
//				" student.sex as sex, student.studentnum as studentnum, student.idnum as idnum, student.ethno as ethno," +
//				" student.examnum as examnum, student.exambatch as exambatch, student.grade as grade, student.classnum as classnum," +
//				" student.lengthofyear as lengthofyear, student.studentcategory as studentcategory, student.paied as paied," +
//				" student.score as score, student.theoryabsent as theoryabsent, student.operateabsent as operateabsent," +
//				" student.theoryfraud as theoryfraud, student.operatefraud as operatefraud) " +
//				"from cn.hdu.examsignup.model.ExStudent student";
//		hqlForNullLanguage += whereString;
//		hqlForNullLanguage +=" and student.exInstitution.institutionnum =:schoolnum and student.exLanguage is null order by student.examnum, student.exCampus.campusnum,student.studentcategory,student.studentnum ";
//		Query queryForNullLanguage = this.getCurrentSession().createQuery(hqlForNullLanguage).setFirstResult(pageNum).setMaxResults(pageSize).setParameter("schoolnum", schoolnum);
//		String[] perchsForNullLanguage = valuesMap.keySet().toArray(new String[0]);
//		for(int i = 0; i < perchsForNullLanguage.length; i++){
//			queryForNullLanguage.setParameter(perchsForNullLanguage[i], valuesMap.get(perchs[i]));
//		}
//		results = queryForNullLanguage.list();
//		for(Map result : results ){
//			result.put("exLanguage", "");
//		}
//		results.addAll(query.list());
        return query.list();
    }

    public List<Map> getStudentByStudentnumList(List<String> studentnumList, String schoolnum) {
        String hql = "select new map(student.id as id, student.name as name, student.studentnum as studentnum, student.idnum as idnum," +
                " student.examnum as examnum,  student.exCollege.name as exCollege, student.classnum as classname) " +
                //	" student.examnum as examnum, student.classnum as class,) " +
                "from cn.hdu.examsignup.model.ExStudent student where student.studentnum in(:studentnumList) and student.exInstitution.institutionnum=:schoolnum " +
                " order by student.exCollege.name, student.classnum, student.studentnum";
        Query query = this.getCurrentSession().createQuery(hql).setParameterList("studentnumList", studentnumList).setParameter("schoolnum", schoolnum);
        return query.list();
    }

    public List<String> getAllStudentNumList(String schoolnum) {
        String hql = "select  student.studentnum " +
                " from cn.hdu.examsignup.model.ExStudent student  where student.exInstitution.institutionnum=:schoolnum " +
                "order by student.exCollege.name, student.classnum, student.studentnum";
        Query query = this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum);
        return query.list();
    }

    public List<Map> paginationShowForApproval(String whereString, Map<String, Object> valuesMap, int pageNum, int pageSize, String institutionnum) {
        String hql = "select new map(student.examnum as examnum, " +
                " student.exLanguage.name as exLanguage, student.name as name," +
                " student.sex as sex, student.idnum as idnum) " +
                "from cn.hdu.examsignup.model.ExStudent student";
        hql += whereString;
        hql += " and student.exInstitution.institutionnum =:institutionnum and  student.exLanguage is not null order by student.examnum,student.exLanguage.languagenum ";
        Query query = this.getCurrentSession().createQuery(hql).setFirstResult(pageNum).setMaxResults(pageSize).setParameter("institutionnum", institutionnum);
        String[] perchs = valuesMap.keySet().toArray(new String[0]);
        for (int i = 0; i < perchs.length; i++) {
            query.setParameter(perchs[i], valuesMap.get(perchs[i]));
        }
        return query.list();
    }

    public List<Map> ExamCollegeGetStudent(String whereString, Map<String, Object> valuesMap, int pageNum, int pageSize, String institutionnum) {
        String hql = "select new map(student.id as id, student.exInstitution.name as institutionName, " +
                " student.exLanguage.name as languageName, student.name as name," +
                " student.sex as sex, student.idnum as idnum,  student.examnum as examnum) " +
                "from cn.hdu.examsignup.model.ExStudent student";
        hql += whereString;
        hql += " and student.exInstitution.institutionnum =:institutionnum and  student.exLanguage is not null order by student.examnum,student.exLanguage.languagenum ";
        Query query = this.getCurrentSession().createQuery(hql).setFirstResult(pageNum).setMaxResults(pageSize).setParameter("institutionnum", institutionnum);
        String[] perchs = valuesMap.keySet().toArray(new String[0]);
        for (int i = 0; i < perchs.length; i++) {
            query.setParameter(perchs[i], valuesMap.get(perchs[i]));
        }
        return query.list();
    }

    public long getAbsentStudentsTotalCount(String schoolnum) {
        String hql = "select student.id from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum =:schoolnum and ( student.theoryabsent like '1' or student.operateabsent like '1' )";
        return this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).list().size();
    }

    public List<Map> getPageAbsentStudents(int pageNum, int pageSize, String schoolnum) {
        String hql = "select new map(student.examnum as examnum,student.name as name,student.theoryabsent as theoryabsent,student.operateabsent as operateabsent) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum =:schoolnum and ( student.theoryabsent like '1' or student.operateabsent like '1' )";
        Query query = this.getCurrentSession().createQuery(hql).setFirstResult(pageNum).setMaxResults(pageSize).setParameter("schoolnum", schoolnum);
        return (List<Map>) query.list();
    }

    public ExStudent getStudentByIDnum(String IDnum, String school) {

        String hql = "select e from cn.hdu.examsignup.model.ExStudent e where idnum=:IDnum and e.exInstitution.institutionnum=:school and e.exInstitution.category='school'";
        Query query = this.getCurrentSession().createQuery(hql);
        query.setParameter("IDnum", IDnum);
        query.setParameter("school", school);
        List<ExStudent> result = query.list();
        if (result.size() == 1)
            return result.get(0);
        return null;
    }

    public Session getSession() {
        return this.getCurrentSession();
    }

    public Map findAbsentStudentByExamNum(String examnum) {
        String hql = "select new map(t.examnum as examnum,t.name as name,t.theoryabsent as theoryabsent,t.operateabsent as operateabsent) from cn.hdu.examsignup.model.ExStudent t where t.examnum =:examnum";
        return (Map) this.getCurrentSession().createQuery(hql).setParameter("examnum", examnum).list().get(0);
    }

    public List<Map> getAbsentStudentsBySchool(String schoolnum) {
        String hql = "select new map(student.examnum as examnum,student.name as name,student.theoryabsent as theoryabsent,student.operateabsent as operateabsent) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum =:schoolnum and ( student.theoryabsent like '1' or student.operateabsent like '1' )";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).list();
    }
    
    
    public List<Map> getAllSchoolsAbsent() {
        String hql = "select new map(student.examnum as examnum,student.name as name,student.theoryabsent as theoryabsent,student.operateabsent as operateabsent) from cn.hdu.examsignup.model.ExStudent student where student.theoryabsent like '1' or student.operateabsent like '1' ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).list();
    }

    public List<Map> getPageFraudStudents(String schoolnum, int pageNum, int pageSize) {
        String hql = "select new map(student.examnum as examnum,student.name as name,student.theoryfraud as theoryfraud,student.operatefraud as operatefraud) " +
                "from cn.hdu.examsignup.model.ExStudent student " +
                "where student.exInstitution.institutionnum =:schoolnum and ( student.theoryfraud like '1' or student.operatefraud like '1' )";
        Query query = this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setFirstResult(pageNum).setMaxResults(pageSize);
        return (List<Map>) query.list();
    }

    public List<Map> getPageCheckScoreStudents(String schoolnum, int pageNum, int pageSize) {
        String hql = "select new map(student.examnum as examnum,student.name as name," +
                "student.exCollege.name as college,student.score as score ) " +
                "from cn.hdu.examsignup.model.ExStudent student " +
                "where student.exInstitution.institutionnum =:schoolnum and " +
                "student.exStudentstatus is not null and " +
                "student.exStudentstatus.statusnum='7'";
        Query query = this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setFirstResult(pageNum).setMaxResults(pageSize);
        return (List<Map>) query.list();
    }

    public Map findFraudStudentByExamNum(String examnum) {
        String hql = "select new map(t.examnum as examnum,t.name as name,t.theoryfraud as theoryfraud,t.operatefraud as operatefraud) from cn.hdu.examsignup.model.ExStudent t where t.examnum =:examnum";
        return (Map) this.getCurrentSession().createQuery(hql).setParameter("examnum", examnum).list().get(0);
    }

    public Map findCheckScoreStudentByExamNum(String examnum) {
        String hql = "select new map(t.examnum as examnum,t.name as name,t.score as score,t.newscore as newscore)" +
                " from cn.hdu.examsignup.model.ExStudent t where t.examnum =:examnum";
        return (Map) this.getCurrentSession().createQuery(hql).setParameter("examnum", examnum).list().get(0);
    }

    public List<Map> getFraudStudentsBySchool(String institutionnum) {
        String hql = "select new map(student.examnum as examnum,student.name as name,student.theoryfraud as theoryfraud,student.operatefraud as operatefraud) " +
                "from cn.hdu.examsignup.model.ExStudent student " +
                "where  ( student.theoryfraud like '1' or student.operatefraud like '1' ) and" +
                " student.exInstitution.institutionnum=:institutionnum";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum).list();
    }
    
    
    public List<Map> getAllSchoolsFraud() {
        String hql = "select new map(student.examnum as examnum,student.name as name,student.theoryfraud as theoryfraud,student.operatefraud as operatefraud) " +
                "from cn.hdu.examsignup.model.ExStudent student " +
                "where  ( student.theoryfraud like '1' or student.operatefraud like '1' ) ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).list();
    }

    public List<Map> getAllCheckScoreStudents(String institutionnum) {
        String hql = "select new map(student.examnum as examnum,student.name as name,student.theoryfraud as theoryfraud,student.operatefraud as operatefraud, student.score as score) " +
                "from cn.hdu.examsignup.model.ExStudent student " +
                "where student.exInstitution.institutionnum=:institutionnum " +
                "and student.exStudentstatus is not null and" +
                " student.exStudentstatus.statusnum='7'";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum).list();
    }

    public long getFraudStudentsTotalCount(String schoolnum) {
        String hql = "select student.id from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum =:schoolnum and  ( student.theoryfraud like '1' or student.operatefraud like '1' )";
        return this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).list().size();
    }

    public long getCheckScoreStudentsTotalCount(String schoolnum) {
        String hql = "select student.id from cn.hdu.examsignup.model.ExStudent student " +
                "where student.exInstitution.institutionnum =:schoolnum and " +
                "student.exStudentstatus is not null and student.exStudentstatus.statusnum='7'";
        return this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).list().size();
    }

    public List<Map> getAllStudents(String schoolnum) {
        String hql = "select new map(student.examnum as examnum,student.name as name,student.idnum as idnum,student.sex as sex) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum =:schoolnum and student.examnum is not null order by student.examnum,student.studentnum";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).list();
    }

    public List<Map> getAllStudentsCJ(String schoolnum) {
        String hql = "select new map(student.examnum as examnum,student.name as name," +
                "student.idnum as idnum,student.sex as sex,student.score as score," +
                "student.theoryabsent as theoryabsent,student.operateabsent as operateabsent) " +
                "from cn.hdu.examsignup.model.ExStudent student " +
                "where student.exInstitution.institutionnum =:schoolnum " +
                "and student.exInstitution is not null and student.examnum is not null and student.score is not null order by student.examnum,student.studentnum";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).list();
    }

    public List<Map> getAllStudentsUnpaid(String schoolnum) {
        String hql = "select new map(student.studentnum as studentnum,student.name as name,student.idnum as idnum,student.exLanguage.name as exLanguage, student.exCollege.name as exCollege, student.exProfession.name as exProfession,student.paied as paied,student.classnum as class) " +
                "from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum =:schoolnum and student.paied = '0' " +
                "and student.exLanguage is not null order by student.exCampus.name, student.exCollege.name, student.exLanguage.name,student.classnum,student.studentnum";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).list();
    }

    public List<Map> backupStudentsExcel(String schoolnum) {
        String hql = "select new map(student.id as id, student.exInstitution.name as exInstitution, student.exLanguage.name as exLanguage, student.exProfession.name as exProfession," +
                " student.exCollege.name as exCollege," +
                " student.exCampus.name as exCampus, student.name as name, student.password as password," +
                " student.sex as sex, student.studentnum as studentnum, student.idnum as idnum, student.ethno as ethno," +
                " student.examnum as examnum, student.exambatch as exambatch, student.grade as grade, student.classnum as classnum," +
                " student.lengthofyear as lengthofyear, student.studentcategory as studentcategory, student.paied as paied," +
                " student.score as score, student.theoryabsent as theoryabsent, student.operateabsent as operateabsent," +
                " student.theoryfraud as theoryfraud, student.operatefraud as operatefraud) " +
                "from cn.hdu.examsignup.model.ExStudent student where student.examnum is not null and student.exInstitution.institutionnum =:schoolnum order by student.examnum ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).list();
    }

    public String saveArrangedStudent(List<Map> readyToArrange, ExArrangement exArrangement, long operateseat) {
        String studentId;
        ExStudent exStudent;
        String hql = "";
        Query query;
        if (exArrangement == null)
            return "{ success: false, errors:{info: '没有找到对应的场次！'}}";
        for (Map element : readyToArrange) {
            studentId = (String) element.get("id");
            hql = "select a from cn.hdu.examsignup.model.ExStudent a where a.id=:studentId";
            query = this.getCurrentSession().createQuery(hql)
                    .setParameter("studentId", studentId);
            exStudent = (ExStudent) query.uniqueResult();
            if (exStudent == null)
                return "{ success: false, errors:{info: '没有找到对应学生！'}}";
            operateseat++;
            exStudent.setExArrangementByOperatearrangeid(exArrangement);
            exStudent.setOperateseat(Integer.valueOf((int) operateseat));
            this.update(exStudent);
        }
        return "{ success: true, errors:{info: '成功安排" + readyToArrange.size() + "个考生考试!'}}";
    }

    public String saveArrangedStudent(List<Map> readyToArrange, String arrangeid) {
        String studentId;
        ExStudent exStudent;
        String hql = "select a from cn.hdu.examsignup.model.ExArrangement a where a.id=:arrangeid";
        Query query = this.getCurrentSession().createQuery(hql)
                .setParameter("arrangeid", arrangeid);
        ExArrangement exArrangement = (ExArrangement) query.uniqueResult();
        if (exArrangement == null)
            return "{ success: false, errors:{info: '没有找到对应的场次！'}}";
        for (Map element : readyToArrange) {
            studentId = (String) element.get("id");
            hql = "select a from cn.hdu.examsignup.model.ExStudent a where a.id=:studentId";
            query = this.getCurrentSession().createQuery(hql)
                    .setParameter("studentId", studentId);
            exStudent = (ExStudent) query.uniqueResult();
            if (exStudent == null)
                return "{ success: false, errors:{info: '没有找到对应学生！'}}";
            exStudent.setExArrangementByOperatearrangeid(exArrangement);
            this.update(exStudent);
        }
        return "{ success: true, errors:{info: '成功安排" + readyToArrange.size() + "个考生考试!'}}";
    }

    public String deleteArrangedStudent(List<Map> unArranged) {
        String studentId;
        String hql;
        Query query;
        ExStudent exStudent;
        for (Map element : unArranged) {
            studentId = (String) element.get("id");
            hql = "select a from cn.hdu.examsignup.model.ExStudent a where a.id=:studentId";
            query = this.getCurrentSession().createQuery(hql)
                    .setParameter("studentId", studentId);
            exStudent = (ExStudent) query.uniqueResult();
            if (exStudent == null)
                return "{ success: false, errors:{info: '没有找到对应学生！'}}";
            exStudent.setExArrangementByOperatearrangeid(null);
            this.update(exStudent);
        }
        return "{ success: true, errors:{info: '成功去除" + unArranged.size() + "个考生的考试安排!'}}";
    }

    public List<Map> getCollegeSignUpInfo(String schoolnum, String languagenum) {
        String hql = "select new map(student.exCollege.name as collegename,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum =:schoolnum and student.exLanguage.languagenum  =:languagenum group by student.exCollege.name ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("languagenum", languagenum).list();
    }

    public List<Map> getCollegeSignUpInfo(String schoolnum) {
        String hql = "select new map(student.exCollege.name as collegename,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum =:schoolnum group by student.exCollege.name ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).list();
    }


    public List<Map> getSignUpInfoGroupByLanguage(String schoolnum) {
        String hql = "select new map(student.exLanguage.languagenum as languagenum,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum =:schoolnum group by student.exLanguage.languagenum ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).list();
    }

    public long getPassStudentsTotalCount(String whereString, Map<String, Object> valuesMap, String schoolnum) {
        String hql = "select student.id from cn.hdu.examsignup.model.ExStudent student ";
        hql += whereString;
        hql += " and student.exInstitution.institutionnum =:schoolnum and student.score>59";
        Query query = this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum);
        String[] perchs = valuesMap.keySet().toArray(new String[0]);
        for (int i = 0; i < perchs.length; i++) {
            query.setParameter(perchs[i], valuesMap.get(perchs[i]));
        }
        return query.list().size();
    }

    public List<Map> getPassStudents(String whereString, Map<String, Object> valuesMap, int pageNum, int pageSize, String schoolnum) {
        String hql = "select new map(student.id as id, student.exInstitution.name as exInstitution, student.exLanguage.name as exLanguage, student.exProfession.name as exProfession," +
                " student.exCollege.name as exCollege," +
                " student.exCampus.name as exCampus, student.name as name, " +
                " student.sex as sex, student.studentnum as studentnum, student.idnum as idnum, " +
                " student.examnum as examnum, student.exambatch as exambatch, student.score as score) " +
                "from cn.hdu.examsignup.model.ExStudent student ";
        hql += whereString;
        hql += " and student.exInstitution.institutionnum =:schoolnum and student.score>59 order by student.exCollege.name,student.classnum,student.exLanguage.name,student.examnum";
        Query query = this.getCurrentSession().createQuery(hql).setFirstResult(pageNum).setMaxResults(pageSize).setParameter("schoolnum", schoolnum);
        String[] perchs = valuesMap.keySet().toArray(new String[0]);
        for (int i = 0; i < perchs.length; i++) {
            query.setParameter(perchs[i], valuesMap.get(perchs[i]));
        }
        return (List<Map>) query.list();
    }


    //得到一个学校的每个学院及总人数
    public List<Map> getScoreInfo_getCollege(String schoolnum, String languagenum) {
        String hql = "select new map(student.exCollege.name as collegename,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum=:schoolnum and student.exLanguage.languagenum=:languagenum group by student.exCollege.name ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("languagenum", languagenum).list();
    }

    //得到一个学校各个学院某个语种不及格的成绩人数
    public List<Map> getScoreInfo_1(String schoolnum, String languagenum, JSONObject summaryCondition) {
        String passline = summaryCondition.get("passLine").toString();//得到及格线
        BigDecimal passLine = new BigDecimal(passline);
        String hql = "select new map(student.exCollege.name as collegename,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum=:schoolnum and student.exLanguage.languagenum=:languagenum and student.score<:passLine group by student.exCollege.name ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("languagenum", languagenum).setParameter("passLine", passLine).list();
    }

    //得到一个学校各个学院某个语种及格的成绩人数
    public List<Map> getScoreInfo_2(String schoolnum, String languagenum, JSONObject summaryCondition) {
        String passline = summaryCondition.get("passLine").toString();//得到及格线
        BigDecimal passLine = new BigDecimal(passline);
        String hql = "select new map(student.exCollege.name as collegename,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum=:schoolnum and student.exLanguage.languagenum=:languagenum and student.score>=:passLine group by student.exCollege.name ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("languagenum", languagenum).setParameter("passLine", passLine).list();
    }

    //得到一个学校各个学院某个语种优秀的成绩人数
    public List<Map> getScoreInfo_3(String schoolnum, String languagenum, JSONObject summaryCondition) {
        String excellentline = summaryCondition.get("excellentLine").toString();//得到及优秀线
        BigDecimal excellentLine = new BigDecimal(excellentline);
        String hql = "select new map(student.exCollege.name as collegename,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum=:schoolnum and student.exLanguage.languagenum=:languagenum and student.score>=:excellentLine group by student.exCollege.name ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("languagenum", languagenum).setParameter("excellentLine", excellentLine).list();
    }


    //得到语种列表
    public List<Map> getLanguageList(String schoolnum) {

        String hql = "select new map(t.exLanguage.languagenum as languagenum, t.exLanguage.name as languagename) from cn.hdu.examsignup.model.ExStudent t where t.exInstitution.institutionnum=:schoolnum group by t.exLanguage.languagenum";
        List<Map> t = this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).list();

        return this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).list();
    }

    //得到语种列表
    public List<Map> getLanguageList_theory(String schoolnum) {

        String hql = "select new map(t.exLanguage.languagenum as languagenum, t.exLanguage.name as languagename) from cn.hdu.examsignup.model.ExStudent t where t.exInstitution.institutionnum=:schoolnum and t.exLanguage.theoryflag='1' group by t.exLanguage.languagenum";
        List<Map> t = this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).list();

        return this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).list();
    }

    //得到一个学校的每个年级及总人数
    public List<Map> getScoreInfo_getGrade(String schoolnum, String languagenum) {
        String hql = "select new map(student.grade as grade,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum=:schoolnum and student.exLanguage.languagenum=:languagenum group by student.grade ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("languagenum", languagenum).list();
    }

    //得到一个学校各个年级某个语种不及格的成绩人数
    public List<Map> getScoreInfo_getUnPassStudent(String schoolnum, String languagenum, JSONObject summaryCondition) {
        String passline = summaryCondition.get("passLine").toString();//得到及格线
        BigDecimal passLine = new BigDecimal(passline);
        String hql = "select new map(student.grade as grade,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum=:schoolnum and student.exLanguage.languagenum=:languagenum and student.score<:passLine group by student.grade ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("languagenum", languagenum).setParameter("passLine", passLine).list();
    }

    //得到一个学校各个年级某个语种及格的成绩人数
    public List<Map> getScoreInfo_getPassStudent(String schoolnum, String languagenum, JSONObject summaryCondition) {
        String passline = summaryCondition.get("passLine").toString();//得到及格线
        BigDecimal passLine = new BigDecimal(passline);
        String hql = "select new map(student.grade as grade,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum=:schoolnum and student.exLanguage.languagenum=:languagenum and student.score>=:passLine group by student.grade ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("languagenum", languagenum).setParameter("passLine", passLine).list();
    }

    //得到一个学校各个年级某个语种优秀的成绩人数
    public List<Map> getScoreInfo_getExcellentStudent(String schoolnum, String languagenum, JSONObject summaryCondition) {
        String excellentline = summaryCondition.get("excellentLine").toString();//得到及优秀线
        BigDecimal excellentLine = new BigDecimal(excellentline);
        String hql = "select new map(student.grade as grade,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum=:schoolnum and student.exLanguage.languagenum=:languagenum and student.score>=:excellentLine group by student.grade ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("languagenum", languagenum).setParameter("excellentLine", excellentLine).list();
    }

    //得到学院列表,只得到student表中存在的学院
    public List<Map> getCollegeList(String schoolnum) {

        String hql = "select new map(t.exCollege.name as name) from  cn.hdu.examsignup.model.ExStudent t where t.exInstitution.institutionnum=:schoolnum group by t.exCollege.name order by t.exCollege.name";
        List<Map> res = (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).list();
        return res;
    }

    //得到一个学校的某个 学院某个 语种  各个年级及总人数
    public List<Map> getScoreInfo_getGrade(String schoolnum, String collegename, String languagenum) {

        String hql = "select new map(student.grade as grade,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum=:schoolnum and student.exCollege.name=:collegename and student.exLanguage.languagenum=:languagenum group by student.grade ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("collegename", collegename).setParameter("languagenum", languagenum).list();
    }

    //得到一个学校某个 学院各个年级某个语种不及格的成绩人数
    public List<Map> getScoreInfo_getUnPassStudent(String schoolnum, String collegename, String languagenum, JSONObject summaryCondition) {
        String passline = summaryCondition.get("passLine").toString();//得到及格线
        BigDecimal passLine = new BigDecimal(passline);
        String hql = "select new map(student.grade as grade,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum=:schoolnum and student.exCollege.name=:collegename and student.exLanguage.languagenum=:languagenum and student.score<:passLine group by student.grade ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("collegename", collegename).setParameter("languagenum", languagenum).setParameter("passLine", passLine).list();
    }

    //得到一个学校某个 学院各个年级某个语种及格的成绩人数
    public List<Map> getScoreInfo_getPassStudent(String schoolnum, String collegename, String languagenum, JSONObject summaryCondition) {
        String passline = summaryCondition.get("passLine").toString();//得到及格线
        BigDecimal passLine = new BigDecimal(passline);
        String hql = "select new map(student.grade as grade,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum=:schoolnum and student.exCollege.name=:collegename and student.exLanguage.languagenum=:languagenum and student.score>=:passLine group by student.grade ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("collegename", collegename).setParameter("languagenum", languagenum).setParameter("passLine", passLine).list();
    }

    //得到一个学校某个 学院各个年级某个语种优秀的成绩人数
    public List<Map> getScoreInfo_getExcellentStudent(String schoolnum, String collegename, String languagenum, JSONObject summaryCondition) {
        String excellentline = summaryCondition.get("excellentLine").toString();//得到及优秀线
        BigDecimal excellentLine = new BigDecimal(excellentline);
        String hql = "select new map(student.grade as grade,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum=:schoolnum and student.exCollege.name=:collegename and student.exLanguage.languagenum=:languagenum and student.score>=:excellentLine group by student.grade ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("collegename", collegename).setParameter("languagenum", languagenum).setParameter("excellentLine", excellentLine).list();
    }

    //得到年级列表
    public List getGradeList(String schoolnum) {
        String hql = "select t.grade, t.grade from cn.hdu.examsignup.model.ExStudent t where t.exInstitution.institutionnum=:schoolnum group by t.grade";
        return this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).list();
    }

    //得到一个学校的某个 年级某个 语种  各个学院及总人数
    public List<Map> getScoreInfo_getCollege(String schoolnum, String grade, String languagenum) {

        String hql = "select new map(student.exCollege.name as collegename,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum=:schoolnum and student.grade=:grade and student.exLanguage.languagenum=:languagenum group by student.exCollege.name ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("grade", grade).setParameter("languagenum", languagenum).list();
    }

    //得到一个学校的某个 年级某个 语种  各个学院不及格的成绩人数
    public List<Map> getScoreInfoByGrade_College_getUnPassStudent(String schoolnum, String grade, String languagenum, JSONObject summaryCondition) {
        String passline = summaryCondition.get("passLine").toString();//得到及格线
        BigDecimal passLine = new BigDecimal(passline);
        String hql = "select new map(student.exCollege.name as collegename,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum=:schoolnum and student.grade=:grade and student.exLanguage.languagenum=:languagenum and student.score<:passLine group by student.exCollege.name ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("grade", grade).setParameter("languagenum", languagenum).setParameter("passLine", passLine).list();
    }

    //得到一个学校某个 年级某个语种 各个学院及格的成绩人数
    public List<Map> getScoreInfoByGrade_College_getPassStudent(String schoolnum, String grade, String languagenum, JSONObject summaryCondition) {
        String passline = summaryCondition.get("passLine").toString();//得到及格线
        BigDecimal passLine = new BigDecimal(passline);
        String hql = "select new map(student.exCollege.name as collegename,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum=:schoolnum and student.grade=:grade and student.exLanguage.languagenum=:languagenum and student.score>=:passLine group by student.exCollege.name ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("grade", grade).setParameter("languagenum", languagenum).setParameter("passLine", passLine).list();
    }

    //得到一个学校某个 年级某个语种 各个学院优秀的成绩人数
    public List<Map> getScoreInfoByGrade_College_getExcellentStudent(String schoolnum, String grade, String languagenum, JSONObject summaryCondition) {
        String excellentline = summaryCondition.get("excellentLine").toString();//得到及优秀线
        BigDecimal excellentLine = new BigDecimal(excellentline);
        String hql = "select new map(student.exCollege.name as collegename,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum=:schoolnum and student.grade=:grade and student.exLanguage.languagenum=:languagenum and student.score>=:excellentLine group by student.exCollege.name ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("grade", grade).setParameter("languagenum", languagenum).setParameter("excellentLine", excellentLine).list();
    }

    //取得年级报名信息
    public List<Map> getGradeSignUpInfo(String schoolnum, String languagenum) {
        String hql = "select new map(student.grade as grade,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum =:schoolnum and student.exLanguage.languagenum  =:languagenum group by student.grade ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("languagenum", languagenum).list();
    }

    //得到所有年级及各年级报名总人数
    public List<Map> getGradeSignUpInfo(String schoolnum) {
        String hql = "select new map(student.grade as grade,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum =:schoolnum group by student.grade ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).list();
    }

    //得到该学生成绩
    public List<Map> getScoreInfo(String IDnum) {
        String hql = "select new map(student.examnum as examnum, student.name as stuname," +
                "student.exLanguage.name as languagename,student.score as score ) " +
                "from cn.hdu.examsignup.model.ExStudent student where student.idnum =:IDnum and student.theoryabsent like '0' and student.operateabsent like '0' and student.theoryfraud like '0' and student.operatefraud like '0'";
        List<Map> result = (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("IDnum", IDnum).list();
        Map temp = result.get(0);
        ExStudent entity = this.findByProperty("idnum", IDnum).get(0);
        String status;
        if (entity.getExStudentstatus() == null || (entity.getExStudentstatus().getStatusnum() != 7)) {
            status = "无";
        } else if (entity.getNewscore() == null) {
            status = "复查中";
        } else {
            status = "复查成功";
        }
        if (entity.getNewscore() == null) {
            String newscore = "无";
            temp.remove("newscore");
            temp.put("newscore", newscore);
        }
        temp.put("status", status);
        List<Map> resultofcheck = new ArrayList<Map>();
        resultofcheck.add(temp);
        return resultofcheck;
    }

    //得到学校名称
    public List<Map> getSchoolName(String schoolnum) {
        String hql = "select new map(t.name as schoolname) from cn.hdu.examsignup.model.ExInstitution t where t.institutionnum=:schoolnum ";
        return this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).list();
    }

    //得到考场信息,只有理论考试的门口标贴
    public List<Map> getSection(String whereString, Map<String, Object> valuesMap, int pageNum, int pageSize, String schoolnum) {
            /*	String hql = "select new map(arrangeSupervisor.logicExamroomNum as logicExamroomNum, arrangeSupervisor.exArrangement.exPhysicexamroom.roomlocation as roomlocation, arrangeSupervisor.exArrangement.exLanguage.name as languagename, arrangeSupervisor.exArrangement.exSection.starttime as startTime,arrangeSupervisor.exArrangement.exSection.theoryflag as theoryflag,arrangeSupervisor.exArrangement.exLanguage.theorylength as theorylength,arrangeSupervisor.exArrangement.exLanguage.operatelength as operatelength)"+
						"from cn.hdu.examsignup.model.ExArrangeSupervisor arrangeSupervisor ";
				hql += whereString;
				hql +=" and arrangeSupervisor.exArrangement.exPhysicexamroom.theoryflag='1' and arrangeSupervisor.exInstitution.institutionnum =:schoolnum group by arrangeSupervisor.logicExamroomNum  order by arrangeSupervisor.logicExamroomNum ASC ";
				Query query = this.getCurrentSession().createQuery(hql).setFirstResult(pageNum).setMaxResults(pageSize).setParameter("schoolnum", schoolnum);
				String[] perchs = valuesMap.keySet().toArray(new String[0]);
				for(int i = 0; i < perchs.length; i++){
					query.setParameter(perchs[i], valuesMap.get(perchs[i]));
				}
				*/

        List<Map> result = new ArrayList<Map>();
        ///得到所有 考场号
        String hqlstr_examroom = "select new map(substring(a.examnum,11,3) as examroom) from cn.hdu.examsignup.model.ExStudent a ";
        hqlstr_examroom += whereString;
        hqlstr_examroom += "and a.exInstitution.institutionnum =:schoolnum and a.exArrangementByTheoryarrangeid.exPhysicexamroom.theoryflag='1' group by substring(a.examnum,11,3) order by substring(a.examnum,11,3)";
        Query query = this.getCurrentSession().createQuery(hqlstr_examroom).setFirstResult(pageNum).setMaxResults(pageSize).setParameter("schoolnum", schoolnum);
        String[] perchs = valuesMap.keySet().toArray(new String[0]);
        for (int i = 0; i < perchs.length; i++) {
            query.setParameter(perchs[i], valuesMap.get(perchs[i]));
        }
        List<Map> result_examroom = (List<Map>) query.list();
        //得到每个 考场号的详细信息
        for (Map element : result_examroom) {
            String examroom = element.get("examroom").toString();
            String hqlstr_detail = "select new map(substring(s.examnum,11,3) as logicExamroomNum,s.exArrangementByTheoryarrangeid.exPhysicexamroom.roomlocation as roomlocation,s.exLanguage.name as languagename,s.exArrangementByTheoryarrangeid.exSection.starttime as startTime,s.exArrangementByTheoryarrangeid.exSection.theoryflag as theoryflag,s.exArrangementByTheoryarrangeid.exLanguage.theorylength as theorylength) from cn.hdu.examsignup.model.ExStudent s ";
            hqlstr_detail += "where s.exInstitution.institutionnum =:schoolnum and s.exArrangementByTheoryarrangeid.exPhysicexamroom.theoryflag='1' and substring(s.examnum,11,3)=:examroom ";
            List<Map> res = (List<Map>) this.getCurrentSession().createQuery(hqlstr_detail).setParameter("schoolnum", schoolnum).setParameter("examroom", examroom).list();

            for (Map temp : res) {
                result.add(temp);
                break;
            }
        }


        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        SimpleDateFormat dataFormat2 = new SimpleDateFormat("HH:mm");
        //SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        for (Map element : result) {
            Date startTime = (Date) element.get("startTime");

            if (element.get("startTime") != null) {
                element.put("startTime", dataFormat.format(startTime));

            }
            //计算结束时间
            Calendar c = Calendar.getInstance();
            c.setTime(startTime);
            int minuteAmount = 0;
            if (element.get("theoryflag") != null && element.get("theoryflag").toString().equals("1")) {
                if (element.get("theorylength") != null)
                    minuteAmount = Integer.parseInt(element.get("theorylength").toString());
            }

            c.add(Calendar.MINUTE, minuteAmount);

            element.put("endTime", dataFormat2.format(c.getTime()));


            String logicroomNum = element.get("logicExamroomNum").toString();

            String strHql = "select new map(student.examnum as examnum) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum =:schoolnum and substring(examnum,11,3)=:logicroomNum order by student.examnum ASC";
            List<Map> res = (List<Map>) this.getCurrentSession().createQuery(strHql).setParameter("schoolnum", schoolnum).setParameter("logicroomNum", logicroomNum).list();
            Map elementFirst = res.get(0);
            Map elementLast = res.get(res.size() - 1);

            element.put("startExamnum", elementFirst.get("examnum").toString());
            element.put("endExamnum", elementLast.get("examnum").toString());

        }
        return result;
    }

    //得到总考场数,是理论的
    public long getTotalCount(String whereString, Map<String, Object> valuesMap, String schoolnum) {

        String hql = "select substring(student.examnum,11,3) from cn.hdu.examsignup.model.ExStudent student ";
        hql += whereString;
        hql += " and student.exInstitution.institutionnum =:schoolnum and student.exArrangementByTheoryarrangeid.exPhysicexamroom.theoryflag='1' group by substring(student.examnum,11,3) ";
        Query query = this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum);
        String[] perchs = valuesMap.keySet().toArray(new String[0]);
        for (int i = 0; i < perchs.length; i++) {
            query.setParameter(perchs[i], valuesMap.get(perchs[i]));
        }
        return query.list().size();
    }

    public Map getStudentAllInfo(String IDnum) {
        ExStudent student = this.findByProperty("idnum", IDnum).get(0);
        if (student.getExLanguage() != null) {
            String hql = "select new map(student.id as id, student.exInstitution.name as exInstitution, student.exLanguage.name as exLanguage, student.exProfession.name as exProfession," +
                    " student.exCollege.name as exCollege," +
                    " student.exCampus.name as exCampus, student.name as name, " +
                    " student.sex as sex, student.studentnum as studentnum, student.idnum as idnum, " +
                    " student.ethno as ethno, student.grade as grade, student.classnum as classnum, student.lengthofyear as lengthofyear, " +
                    " student.studentcategory as studentcategory, student.paied as paied) " +
                    "from cn.hdu.examsignup.model.ExStudent student where student.idnum=:IDnum ";
            Query query = this.getCurrentSession().createQuery(hql);
            query.setParameter("IDnum", IDnum);
            List<Map> result = query.list();
            System.out.println(result.get(0));
            return result.get(0);
        } else {
            String hql = "select new map(student.id as id, student.exInstitution.name as exInstitution, student.exProfession.name as exProfession," +
                    " student.exCollege.name as exCollege," +
                    " student.exCampus.name as exCampus, student.name as name, " +
                    " student.sex as sex, student.studentnum as studentnum, student.idnum as idnum, " +
                    " student.ethno as ethno, student.grade as grade, student.classnum as classnum, student.lengthofyear as lengthofyear, " +
                    " student.studentcategory as studentcategory, student.paied as paied) " +
                    "from cn.hdu.examsignup.model.ExStudent student where student.idnum=:IDnum ";
            Query query = this.getCurrentSession().createQuery(hql);
            query.setParameter("IDnum", IDnum);
            List<Map> result = query.list();
            result.get(0).put("exLanguage", "");
            return result.get(0);
        }
    }

    //得到专业列表
    public List<Map> getProfessionList(String schoolnum) {
        String hql = "select new map(t.exProfession.name as name) from cn.hdu.examsignup.model.ExStudent t where t.exInstitution.institutionnum=:schoolnum group by  t.exProfession.name order by t.exProfession.name ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).list();
    }

    //得到某语种某专业的所有学生
    public List<Map> getStudentByProfession_language(String whereString, Map<String, Object> valuesMap, int pageNum, int pageSize, String schoolnum) {
        String hql = "select new map(student.studentnum as studentnum,student.name as name,student.idnum as idnum,student.sex as sex,student.grade as grade, student.lengthofyear as lengthofyear, " +
                "student.exCollege.name as collegename,student.classnum as classname,student.exLanguage.languagenum as languagenum,student.exLanguage.name as languagename,student.exProfession.name as professionname)" +
                "from cn.hdu.examsignup.model.ExStudent student ";
        hql += whereString;
        hql += " and student.exInstitution.institutionnum =:schoolnum and student.exLanguage is not null order by student.exCampus,student.exCollege,student.classnum, student.studentnum";
        Query query = this.getCurrentSession().createQuery(hql).setFirstResult(pageNum).setMaxResults(pageSize).setParameter("schoolnum", schoolnum);
        String[] perchs = valuesMap.keySet().toArray(new String[0]);
        for (int i = 0; i < perchs.length; i++) {
            query.setParameter(perchs[i], valuesMap.get(perchs[i]));
        }
        return (List<Map>) query.list();
    }

    //得到某语种某专业的学生总人数
    public long getStudentsTotalCountByProfession_language(String whereString, Map<String, Object> valuesMap, String schoolnum) {
        String hql = "select student.id from cn.hdu.examsignup.model.ExStudent student ";
        hql += whereString;
        hql += "  and student.exInstitution.institutionnum =:schoolnum and student.exLanguage is not null order by student.exCampus,student.exCollege,student.classnum, student.studentnum ";
        Query query = this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum);
        String[] perchs = valuesMap.keySet().toArray(new String[0]);
        for (int i = 0; i < perchs.length; i++) {
            query.setParameter(perchs[i], valuesMap.get(perchs[i]));
        }
        return query.list().size();
    }

    //得到某语种的所有学生的准考证号
    //得到某语种的所有学生的准考证号
    public List<Map> getAdmissionNum(String logicExamRoomNum, String schoolnum) {
        String hql = "select new map(student.name as name,student.examnum as examnum,student.exLanguage.name as languagename)" +
                "from cn.hdu.examsignup.model.ExStudent student where  student.exInstitution.institutionnum =:schoolnum and substring(examnum,11,3)=:logicExamRoomNum  order by student.examnum ASC";
        List<Map> result = (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("logicExamRoomNum", logicExamRoomNum).list();
        return result;

    }


    //得到所有的年级及各个年级总人数
    public List<Map> getAllGrade_SignUpInfo(String schoolnum, String languagenum) {
        String hql = "select new map(student.grade as grade,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum =:schoolnum and student.exLanguage.languagenum  =:languagenum group by student.grade ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("languagenum", languagenum).list();
    }

    //得到所有年级
    public List<Map> getAllGrade_SignUpInfo(String schoolnum) {
        String hql = "select new map(student.grade as grade,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum =:schoolnum group by student.grade ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).list();
    }

    //得到某校区的所有的年级及各个年级总人数
    public List<Map> getAllGrade_SignUpInfo(String campusnum, String schoolnum, String languagenum) {
        if (campusnum.equals(null) || campusnum.equals("")) {
            String hql = "select new map(student.grade as grade,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum =:schoolnum and student.exLanguage.languagenum  =:languagenum group by student.grade ";
            return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("languagenum", languagenum).list();

        } else {
            String hql = "select new map(student.grade as grade,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum =:schoolnum and student.exLanguage.languagenum  =:languagenum and student.exCampus.campusnum=:campusnum group by student.grade ";
            return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("languagenum", languagenum).setParameter("campusnum", campusnum).list();
        }
    }

    //得到某校区的所有的语种及各个语种报名总人数
    public List<Map> getSignUpInfoGroupByLanguage(String campusnum, String schoolnum) {

        if (campusnum.equals(null) || campusnum.equals("")) {
            String hql = "select new map(student.exLanguage.languagenum as languagenum,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum =:schoolnum group by student.exLanguage.languagenum ";
            return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).list();

        } else {
            String hql = "select new map(student.exLanguage.languagenum as languagenum,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum =:schoolnum and student.exCampus.campusnum=:campusnum group by student.exLanguage.languagenum ";
            return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("campusnum", campusnum).list();
        }
    }

    //得到所有年级
    public List<Map> getAllGrade_studentcount(String campusnum, String schoolnum) {
        if (campusnum.equals(null) || campusnum.equals("")) {
            String hql = "select new map(student.grade as grade,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum =:schoolnum group by student.grade ";
            return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).list();

        } else {
            String hql = "select new map(student.grade as grade,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum =:schoolnum and student.exCampus.campusnum=:campusnum group by student.grade ";
            return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("campusnum", campusnum).list();
        }
    }

    //删除逻辑考场安排
    public String deleteArrangedExamroom(List<Map> examroom, String institutionnum, String operateOrTheory) {
        String hql;
        Query query;
        for (Map element : examroom) {
            String examroomnum = element.get("examroomnum").toString();
            hql = "select a from cn.hdu.examsignup.model.ExStudent a where" +
                    " substring(a.examnum,11,3)=:examroomnum and " +
                    "a.exInstitution.institutionnum=:institutionnum";
            query = this.getCurrentSession().createQuery(hql).setParameter("examroomnum", examroomnum)
                    .setParameter("institutionnum", institutionnum);
            List<ExStudent> students = query.list();
            for (ExStudent temp : students) {
                if (operateOrTheory.equals("theory")) {
                    temp.setExArrangementByTheoryarrangeid(null);
                } else {
                    temp.setExArrangementByOperatearrangeid(null);
                }
                this.update(temp);
            }
        }
        return "{success: true,info: '删除考场安排成功！'}";
    }

    //得到莫学校莫语种的人数
    public long getStudentCountByLang(String institutionnum, String languagenum) {
        String hql = "select s.id from cn.hdu.examsignup.model.ExStudent s " +
                "where s.exInstitution.institutionnum=:institutionnum " +
                "and s.exLanguage.languagenum=:languagenum ";
        Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum).setParameter("languagenum", languagenum);
        return query.list().size();
    }

    //得到某学校某语种某校区的人数
    public long getStudentCountByLang(String institutionnum, String languagenum, String campusid) {
        String hql = "select s.id from cn.hdu.examsignup.model.ExStudent s " +
                "where s.exInstitution.institutionnum=:institutionnum " +
                "and s.exLanguage.languagenum=:languagenum " +
                "and s.exCampus.id=:campusid ";
        Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum)
                .setParameter("languagenum", languagenum).setParameter("campusid", campusid);
        return query.list().size();
    }

    //得到莫学校莫语种未安排机试考场的学生数
    public long getUnarrangedStudentCountByLang(String institutionnum, String languagenum, String operateOrTheory) {
        String hql = "select s.id from cn.hdu.examsignup.model.ExStudent s " +
                "where s.exInstitution.institutionnum=:institutionnum " +
                "and s.exLanguage.languagenum=:languagenum ";
        if (operateOrTheory.equals("operate")) {
            hql += "and s.exArrangementByOperatearrangeid=null";
        } else {
            hql += "and s.exArrangementByTheoryarrangeid=null";
        }
        Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum)
                .setParameter("languagenum", languagenum);
        return query.list().size();
    }

    //得到莫学校莫语种某校区未安排机试考场的学生数
    public long getUnarrangedStudentCountByLang(String institutionnum, String languagenum, String campusid, String operateOrTheory) {
        String hql = "select s.id from cn.hdu.examsignup.model.ExStudent s " +
                "where s.exInstitution.institutionnum=:institutionnum " +
                "and s.exLanguage.languagenum=:languagenum " +
                "and s.exCampus.id=:campusid ";
        if (operateOrTheory.equals("operate")) {
            hql += "and s.exArrangementByOperatearrangeid=null";
        } else {
            hql += "and s.exArrangementByTheoryarrangeid=null";
        }
        Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum)
                .setParameter("languagenum", languagenum)
                .setParameter("campusid", campusid);
        return query.list().size();
    }

    //安排逻辑考场
    public String arrangeByExamroom(String institutionnum, String arrangeid, String examroomnum, String operateOrTheory) {
        String studentId;
        ExStudent exStudent;
        String hql = "select a.id as id from cn.hdu.examsignup.model.ExStudent a " +
                "where a.exInstitution.institutionnum=:institutionnum and substring(a.examnum,11,3)=:examroomnum";
        List<String> toArrangeStudent = this.getCurrentSession().createQuery(hql)
                .setParameter("institutionnum", institutionnum).setParameter("examroomnum", examroomnum).list();
        hql = "select a from cn.hdu.examsignup.model.ExArrangement a where a.id=:arrangeid";
        ExArrangement exArrangement = (ExArrangement) this.getCurrentSession().createQuery(hql).setParameter("arrangeid", arrangeid).uniqueResult();
        for (String element : toArrangeStudent) {
            studentId = element;
            hql = "select a from cn.hdu.examsignup.model.ExStudent a where a.id=:studentId";
            exStudent = (ExStudent) this.getCurrentSession().createQuery(hql).setParameter("studentId", studentId).uniqueResult();
            if (operateOrTheory.equals("operate")) {
                exStudent.setExArrangementByOperatearrangeid(exArrangement);
            } else {
                exStudent.setExArrangementByTheoryarrangeid(exArrangement);
            }

            this.update(exStudent);
        }
        return "{success: true,info: '成功安排" + toArrangeStudent.size() + "个考生！'}";
    }

    //安排逻辑理论考场
    public void arrangeByExamroom(String institutionnum, String arrangeid, String examroomnum) {
        String studentId;
        ExStudent exStudent;
        String hql = "select a.id as id from cn.hdu.examsignup.model.ExStudent a " +
                "where a.exInstitution.institutionnum=:institutionnum and substring(a.examnum,11,3)=:examroomnum";
        List<String> toArrangeStudent = this.getCurrentSession().createQuery(hql)
                .setParameter("institutionnum", institutionnum).setParameter("examroomnum", examroomnum).list();
        hql = "select a from cn.hdu.examsignup.model.ExArrangement a where a.id=:arrangeid";
        ExArrangement exArrangement = (ExArrangement) this.getCurrentSession().createQuery(hql).setParameter("arrangeid", arrangeid).uniqueResult();
        for (String element : toArrangeStudent) {
            studentId = element;
            hql = "select a from cn.hdu.examsignup.model.ExStudent a where a.id=:studentId";
            exStudent = (ExStudent) this.getCurrentSession().createQuery(hql).setParameter("studentId", studentId).uniqueResult();
            exStudent.setExArrangementByTheoryarrangeid(exArrangement);
            this.update(exStudent);
        }
    }
			
			
		/*	public List<Map> getStudentAdmissionInfo(String whereString, Map<String,Object> valuesMap,int pageNum, int pageSize,String schoolnum){
				String hql1 = "select new map(student.name as name,student.examnum as examnum,student.exLanguage.name as exLanguage,student.idnum as idnum,student.exCollege.name as exCollege,student.studentnum as studentnum,student.exProfession.name as exProfession)" +
						     "from cn.hdu.examsignup.model.ExStudent student ";
				hql1 += whereString;
				hql1 +=" and student.exInstitution.institutionnum =:schoolnum order by student.examnum ";
				Query query = this.getCurrentSession().createQuery(hql1).setFirstResult(pageNum).setMaxResults(pageSize).setParameter("schoolnum", schoolnum);
				String[] perchs = valuesMap.keySet().toArray(new String[0]);
				for(int i = 0; i < perchs.length; i++){
					query.setParameter(perchs[i], valuesMap.get(perchs[i]));
				}
				//System.out.println(query.list().get(0));
				return (List<Map>)query.list();
				
			}  */

    //得到符合条件的所有学生成绩,不包括缺考和作弊的考生
    public List<Map> getStudentScoreByCondition(String whereString, Map<String, Object> valuesMap, int pageNum, int pageSize, String schoolnum) {
        String hql = "select new map(student.examnum as examnum,student.name as name,student.studentnum as studentnum,student.exCollege.name as collegename,student.classnum as classnum,student.exProfession.name as professionname,student.exLanguage.name as languagename,student.score as score)" +
                "from cn.hdu.examsignup.model.ExStudent student ";
        hql += whereString;
        hql += " and student.theoryabsent='0' and student.operateabsent='0' and student.theoryfraud='0' and student.operatefraud='0' and student.exInstitution.institutionnum =:schoolnum order by languagename,collegename,classnum,student.examnum";
        Query query = this.getCurrentSession().createQuery(hql).setFirstResult(pageNum).setMaxResults(pageSize).setParameter("schoolnum", schoolnum);
        String[] perchs = valuesMap.keySet().toArray(new String[0]);
        for (int i = 0; i < perchs.length; i++) {
            query.setParameter(perchs[i], valuesMap.get(perchs[i]));
        }
        return (List<Map>) query.list();
    }

    //得到某语种的所有理论监考教师
    public List<Map> getExaminerTeacher(String logicExamroomNum, String examroom, String campusname, String languagename, String schoolnum) {
        String hql = "select new map(arrangeSupervisor.exSupervisor.name as supervisor) " +
                "from cn.hdu.examsignup.model.ExArrangeSupervisor arrangeSupervisor ";
        hql += " where arrangeSupervisor.exInstitution.institutionnum =:schoolnum and arrangeSupervisor.logicExamroomNum=:logicExamroomNum and arrangeSupervisor.exArrangement.exPhysicexamroom.roomlocation=:examroom and arrangeSupervisor.exArrangement.exPhysicexamroom.exCampus.name=:campusname and arrangeSupervisor.exArrangement.exLanguage.name=:languagename ";
        hql += " and arrangeSupervisor.exArrangement.exSection.theoryflag='1' order by supervisor ASC ";
        Query query = this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("logicExamroomNum", logicExamroomNum).setParameter("examroom", examroom).setParameter("campusname", campusname).setParameter("languagename", languagename);


        List<Map> result = (List<Map>) query.list();

        return result;

    }

    //得到某语种的理论监考教师总人数
    public long getLogicExamRoomTotalCountBylanguage(String whereString, Map<String, Object> valuesMap, String schoolnum) {
        String hql = "select arrangeSupervisor.logicExamroomNum,arrangeSupervisor.exArrangement.exPhysicexamroom.roomlocation,arrangeSupervisor.exArrangement.exPhysicexamroom.exCampus.name,arrangeSupervisor.exArrangement.exLanguage.name from cn.hdu.examsignup.model.ExArrangeSupervisor arrangeSupervisor ";
        hql += whereString;
        hql += " and arrangeSupervisor.exArrangement.exPhysicexamroom.theoryflag='1' and arrangeSupervisor.exInstitution.institutionnum =:schoolnum and arrangeSupervisor.exSupervisor is not null group by arrangeSupervisor.logicExamroomNum,arrangeSupervisor.exArrangement.exPhysicexamroom.roomlocation,arrangeSupervisor.exArrangement.exPhysicexamroom.exCampus.name,arrangeSupervisor.exArrangement.exLanguage.name";
        Query query = this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum);
        String[] perchs = valuesMap.keySet().toArray(new String[0]);
        for (int i = 0; i < perchs.length; i++) {
            query.setParameter(perchs[i], valuesMap.get(perchs[i]));
        }
        return query.list().size();
    }

    public String deleteArrangedStudent(List<Map> unArranged, String theoryOrOperateflag) {
        String studentId;
        String hql;
        Query query;
        ExStudent exStudent;
        for (Map element : unArranged) {
            studentId = (String) element.get("id");
            hql = "select a from cn.hdu.examsignup.model.ExStudent a where a.id=:studentId";
            query = this.getCurrentSession().createQuery(hql)
                    .setParameter("studentId", studentId);
            exStudent = (ExStudent) query.uniqueResult();
            if (exStudent == null)
                return "{ success: false, errors:{info: '没有找到对应学生！'}}";
            if (theoryOrOperateflag == "theory") {
                exStudent.setExArrangementByTheoryarrangeid(null);
            } else
                exStudent.setExArrangementByOperatearrangeid(null);
            this.update(exStudent);
        }
        return "{ success: true, errors:{info: '成功去除" + unArranged.size() + "个考生的考试安排!'}}";
    }

    //获取某语种莫校区学生总数
    public long getStudentTotalByCampus(String institutionnum, String languagenum, String campusid) {
        String hql = "select a.id from cn.hdu.examsignup.model.ExStudent a where " +
                "a.exInstitution.institutionnum=:institutionnum and " +
                "a.exLanguage.languagenum=:languagenum and " +
                "a.exCampus.id=:campusid and a.examnum is not null";
        Query query = this.getCurrentSession().createQuery(hql)
                .setParameter("institutionnum", institutionnum)
                .setParameter("languagenum", languagenum).setParameter("campusid", campusid);
        return query.list().size();
    }

    //根据学校校区，语种，上机或理论获取一个未安排的考生
    public ExStudent getOneUnarrangedStudent(String institutionnum, String campusid, String languagenum, String operateOrTheory) {
        String hql = "select a from cn.hdu.examsignup.model.ExStudent a where " +
                "a.exInstitution.institutionnum=:institutionnum and " +
                "a.exLanguage.languagenum=:languagenum and " +
                "a.exCampus.id=:campusid and a.examnum is not null and ";
        if (operateOrTheory.equals("operate")) {
            hql += "a.exArrangementByOperatearrangeid is null order by a.examnum";
        } else {
            hql += "a.exArrangementByTheoryarrangeid is null order by a.examnum";
        }
        Query query = this.getCurrentSession().createQuery(hql);
        query.setParameter("institutionnum", institutionnum)
                .setParameter("languagenum", languagenum).setParameter("campusid", campusid);
        List<ExStudent> list = query.list();
        if (list.size() == 0) {
            return null;
        }

        return list.get(0);
    }

    public String cancelArrange(String institutionnum, String languagenum, String operateOrTheory) {
        String hql = "select a from cn.hdu.examsignup.model.ExStudent a where " +
                "a.exInstitution.institutionnum=:institutionnum and " +
                "a.exLanguage.languagenum=:languagenum";
        Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum)
                .setParameter("languagenum", languagenum);
        List<ExStudent> students = query.list();
        for (ExStudent student : students) {
            if (operateOrTheory.equals("operate")) {
                student.setExArrangementByOperatearrangeid(null);
            } else {
                student.setExArrangementByTheoryarrangeid(null);
            }
            this.update(student);
        }
        return "{success: true,info: {errors: '取消安排成功！'}}";
    }

    public void cancelAllArrangement(String institutionnum) {
        String hql = "select a from cn.hdu.examsignup.model.ExStudent a where " +
                "a.exInstitution.institutionnum=:institutionnum";

        Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum);

        List<ExStudent> students = query.list();
        for (ExStudent student : students) {
            student.setExArrangementByOperatearrangeid(null);
            student.setExArrangementByTheoryarrangeid(null);
            this.update(student);
        }
        this.getCurrentSession().flush();
    }

    //得到理论 考场号及考场地点
    public List<Map> getLogicExamRoom(String whereString, Map<String, Object> valuesMap, int pageNum, int pageSize, String schoolnum) {
        List<Map> result = new ArrayList<Map>();
        ///得到所有 考场号
        String hqlstr_examroom = "select new map(substring(student.examnum,11,3) as examroom) from cn.hdu.examsignup.model.ExStudent student ";
        hqlstr_examroom += whereString;
        hqlstr_examroom += "and student.exInstitution.institutionnum =:schoolnum and student.exArrangementByTheoryarrangeid.exPhysicexamroom.theoryflag='1' group by substring(student.examnum,11,3) order by substring(student.examnum,11,3)";
        Query query = this.getCurrentSession().createQuery(hqlstr_examroom).setFirstResult(pageNum).setMaxResults(pageSize).setParameter("schoolnum", schoolnum);
        String[] perchs = valuesMap.keySet().toArray(new String[0]);
        for (int i = 0; i < perchs.length; i++) {
            query.setParameter(perchs[i], valuesMap.get(perchs[i]));
        }
        List<Map> result_examroom = (List<Map>) query.list();
        //得到每个 考场号的详细信息
        for (Map element : result_examroom) {
            String examroom = element.get("examroom").toString();
            String hqlstr_detail = "select new map(substring(student.examnum,11,3) as logicExamroomNum,student.exArrangementByTheoryarrangeid.exPhysicexamroom.roomlocation as roomlocation,student.exLanguage.name as languagename) from cn.hdu.examsignup.model.ExStudent student ";
            hqlstr_detail += whereString;
            hqlstr_detail += "and student.exInstitution.institutionnum =:schoolnum and student.exArrangementByTheoryarrangeid.exPhysicexamroom.theoryflag='1' and substring(student.examnum,11,3)=:examroom ";

            // List<Map> res = (List<Map>)this.getCurrentSession().createQuery(hqlstr_detail).setParameter("schoolnum", schoolnum).setParameter("examroom", examroom).list();


            Query query2 = this.getCurrentSession().createQuery(hqlstr_detail).setParameter("schoolnum", schoolnum).setParameter("examroom", examroom);
            String[] perchs_temp = valuesMap.keySet().toArray(new String[0]);
            for (int i = 0; i < perchs_temp.length; i++) {
                query2.setParameter(perchs_temp[i], valuesMap.get(perchs_temp[i]));
            }
            List<Map> res = (List<Map>) query2.list();
            for (Map temp : res) {
                result.add(temp);
                break;
            }
        }
        return result;
				
				/*
				String hql = "select new map(arrangeSupervisor.logicExamroomNum as logicExamroomNum, arrangeSupervisor.exArrangement.exPhysicexamroom.roomlocation as roomlocation, arrangeSupervisor.exArrangement.exLanguage.name as languagename)"+
						"from cn.hdu.examsignup.model.ExArrangeSupervisor arrangeSupervisor ";
				hql += whereString;
				hql +="and arrangeSupervisor.exArrangement.exPhysicexamroom.theoryflag='1' and arrangeSupervisor.exInstitution.institutionnum =:schoolnum group by arrangeSupervisor.logicExamroomNum order by arrangeSupervisor.logicExamroomNum ASC  ";
				Query query = this.getCurrentSession().createQuery(hql).setFirstResult(pageNum).setMaxResults(pageSize).setParameter("schoolnum", schoolnum);
				String[] perchs = valuesMap.keySet().toArray(new String[0]);
				for(int i = 0; i < perchs.length; i++){
					query.setParameter(perchs[i], valuesMap.get(perchs[i]));
				}
												
				List<Map> result = (List<Map>) query.list();
				return result;*/
    }

    //得到考场里的学生信息
    public List<Map> getStudentInfo(String logicExamRoomNum, String schoolnum) {

        String strHql = "select new map(student.exLanguage.name as languagename,student.examnum as examnum,student.name as name,student.studentnum as studentnum,student.exCollege.name as collegename,student.classnum as classnum,student.grade as grade,student.remark as remark) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum =:schoolnum and substring(examnum,11,3)=:logicExamRoomNum order by student.examnum ASC";
        List<Map> result = (List<Map>) this.getCurrentSession().createQuery(strHql).setParameter("schoolnum", schoolnum).setParameter("logicExamRoomNum", logicExamRoomNum).list();
        return result;
    }

    //得到学生考场信息
    public Map getStuExamInfo(String studentidnum) {
        String Hql;
        ExStudent student = this.findByProperty("idnum", studentidnum).get(0);
        Map result;
        if (student.getExLanguage().getOperateflag().equals("1") && student.getExLanguage().getTheoryflag().equals("1")) {
            Hql = "select new map(student.examnum as examnum,student.name as stuname,student.exLanguage.name as languagename, substring(student.examnum,11,3) as logicexamnum," +
                    "student.exArrangementByTheoryarrangeid.exPhysicexamroom.roomlocation as theoryroomlocation, student.exArrangementByTheoryarrangeid.exSection.starttime as theorystarttime, " +
                    "student.exArrangementByOperatearrangeid.exPhysicexamroom.roomlocation as operateroomlocation, student.exArrangementByOperatearrangeid.exSection.starttime as operatestarttime) " +
                    "from cn.hdu.examsignup.model.ExStudent student where student.idnum =:idnum";
            result = (Map) this.getCurrentSession().createQuery(Hql).setParameter("idnum", studentidnum).list().get(0);
            Date theorydate = (Date) result.get("theorystarttime");
            Date operatedate = (Date) result.get("operatestarttime");
            SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            if (theorydate != null) {
                result.put("theorydate", dataFormat.format(theorydate).toString());
                result.put("theorytime", timeFormat.format(theorydate).toString());
                result.remove("theorystarttime");
            }
            if (operatedate != null) {
                result.put("operatedate", dataFormat.format(operatedate).toString());
                result.put("operatetime", timeFormat.format(operatedate).toString());
                result.remove("operatestarttime");
            }
        } else if (student.getExLanguage().getOperateflag().equals("1")) {
            Hql = "select new map(student.examnum as examnum,student.name as stuname,student.exLanguage.name as languagename, substring(student.examnum,11,3) as logicexamnum," +
//							"student.exArrangementByTheoryarrangeid.exPhysicexamroom.roomlocation as theoryroomlocation, student.exArrangementByTheoryarrangeid.exSection.starttime as theorystarttime, " +
                    "student.exArrangementByOperatearrangeid.exPhysicexamroom.roomlocation as operateroomlocation, student.exArrangementByOperatearrangeid.exSection.starttime as operatestarttime) " +
                    "from cn.hdu.examsignup.model.ExStudent student where student.idnum =:idnum";
            result = (Map) this.getCurrentSession().createQuery(Hql).setParameter("idnum", studentidnum).list().get(0);
            Date operatedate = (Date) result.get("operatestarttime");
            SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            if (operatedate != null) {
                result.put("theoryroomlocation", "无理论考试");
                result.put("theorydate", "--");
                result.put("theorytime", "--");
                result.put("operatedate", dataFormat.format(operatedate).toString());
                result.put("operatetime", timeFormat.format(operatedate).toString());
                result.remove("operatestarttime");
            }
        } else {
            Hql = "select new map(student.examnum as examnum,student.name as stuname,student.exLanguage.name as languagename, substring(student.examnum,11,3) as logicexamnum," +
                    "student.exArrangementByTheoryarrangeid.exPhysicexamroom.roomlocation as theoryroomlocation, student.exArrangementByTheoryarrangeid.exSection.starttime as theorystarttime) " +
//							"student.exArrangementByOperatearrangeid.exPhysicexamroom.roomlocation as operateroomlocation, student.exArrangementByOperatearrangeid.exSection.starttime as operatestarttime) " +
                    "from cn.hdu.examsignup.model.ExStudent student where student.idnum =:idnum";
            result = (Map) this.getCurrentSession().createQuery(Hql).setParameter("idnum", studentidnum).list().get(0);
            Date theorydate = (Date) result.get("theorystarttime");
            SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            if (theorydate != null) {
                result.put("theorydate", dataFormat.format(theorydate).toString());
                result.put("theorytime", timeFormat.format(theorydate).toString());
                result.put("operateroomlocation", "无上机考试");
                result.put("operatedate", "--");
                result.put("operatetime", "--");
                result.remove("theorystarttime");
            }
        }
        return result;
    }

    public void clearData(String institutionnum) {
        String hql;
        Query query;
        hql = "select a.id from cn.hdu.examsignup.model.ExStudent a where " +
                "a.exInstitution.institutionnum=:institutionnum";
        query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum);
        List deleteList = query.list();
        if (deleteList.size() == 0) return;
        hql = "delete from cn.hdu.examsignup.model.ExStudent a " +
                "where a.id in(:list)";
        query = this.getCurrentSession().createQuery(hql).setParameterList("list", deleteList);
        query.executeUpdate();

    }

    public void clearUnPaiedStudent(String institutionnum) {
        String hql;
        Query query;
        hql = "select a.id from cn.hdu.examsignup.model.ExStudent a where " +
                "a.exInstitution.institutionnum=:institutionnum and " +
                "a.paied='0'";
        query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum);
        List deleteList = query.list();
        if (deleteList.size() == 0) return;
        hql = "delete from cn.hdu.examsignup.model.ExStudent a " +
                "where a.id in(:list)";
        query = this.getCurrentSession().createQuery(hql).setParameterList("list", deleteList);
        query.executeUpdate();

    }

    //得到上机考试的场次及考场地点
    public List<Map> getSectionAndLocationRoom(String whereString, Map<String, Object> valuesMap, int pageNum, int pageSize, String schoolnum) {
        String hql = "select new map(arrangement.exSection.sectionnum as sectionnum, arrangement.exPhysicexamroom.roomlocation as roomlocation, arrangement.exLanguage.name as languagename)" +
                "from cn.hdu.examsignup.model.ExArrangement arrangement";
        hql += whereString;
        hql += "and arrangement.exPhysicexamroom.operateflag='1' and arrangement.exInstitution.institutionnum =:schoolnum order by sectionnum ASC ";
        Query query = this.getCurrentSession().createQuery(hql).setFirstResult(pageNum).setMaxResults(pageSize).setParameter("schoolnum", schoolnum);
        String[] perchs = valuesMap.keySet().toArray(new String[0]);
        for (int i = 0; i < perchs.length; i++) {
            query.setParameter(perchs[i], valuesMap.get(perchs[i]));
        }

        List<Map> result = (List<Map>) query.list();
        return result;
    }

    //得到上机考试的总教室数
    public long getOperateSectionTotalCount(String whereString, Map<String, Object> valuesMap, String schoolnum) {
        String hql = "select arrangement.id from cn.hdu.examsignup.model.ExArrangement arrangement ";
        hql += whereString;
        hql += "and arrangement.exPhysicexamroom.operateflag='1' and arrangement.exInstitution.institutionnum =:schoolnum ";
        Query query = this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum);
        String[] perchs = valuesMap.keySet().toArray(new String[0]);
        for (int i = 0; i < perchs.length; i++) {
            query.setParameter(perchs[i], valuesMap.get(perchs[i]));
        }
        return query.list().size();
    }

    //得到理论总考场数
    public long getLogicExamRoomTotalCount(String whereString, Map<String, Object> valuesMap, String schoolnum) {
	/*	String hql = "select arrangeSupervisor.logicExamroomNum from cn.hdu.examsignup.model.ExArrangeSupervisor arrangeSupervisor ";
		hql += whereString;
		hql +="and arrangeSupervisor.exArrangement.exPhysicexamroom.theoryflag='1' and arrangeSupervisor.exInstitution.institutionnum =:schoolnum ";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum);
		String[] perchs = valuesMap.keySet().toArray(new String[0]);
		for(int i = 0; i < perchs.length; i++){
			query.setParameter(perchs[i], valuesMap.get(perchs[i]));
		}
		return query.list().size();*/


        String hql = "select substring(student.examnum,11,3) from cn.hdu.examsignup.model.ExStudent student ";
        hql += whereString;
        hql += " and student.exInstitution.institutionnum =:schoolnum and student.exArrangementByTheoryarrangeid.exPhysicexamroom.theoryflag='1' group by substring(student.examnum,11,3) ";
        Query query = this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum);
        String[] perchs = valuesMap.keySet().toArray(new String[0]);
        for (int i = 0; i < perchs.length; i++) {
            query.setParameter(perchs[i], valuesMap.get(perchs[i]));
        }
        return query.list().size();
    }


    //得到上机考试每个教室的学生信息
    public List<Map> getStudentInfoBySectionAndLocationRoom(String sectionnumber, String roomlocation, String languagename, String schoolnum) {
        int sectionnum = Integer.parseInt(sectionnumber);
        String strHql = "select new map(student.examnum as examnum,student.name as name,student.operateseat as operateseat,student.exCollege.name as collegename,student.classnum as classnum,student.grade as grade,student.remark as remark) from cn.hdu.examsignup.model.ExStudent student where  student.exInstitution.institutionnum =:schoolnum " +
                " and  student.exArrangementByOperatearrangeid.exSection.sectionnum=:sectionnum and  student.exArrangementByOperatearrangeid.exPhysicexamroom.roomlocation=:roomlocation and student.exLanguage.name=:languagename order by student.examnum ASC";
        List<Map> result = (List<Map>) this.getCurrentSession().createQuery(strHql).setParameter("schoolnum", schoolnum).setParameter("sectionnum", sectionnum).setParameter("roomlocation", roomlocation).setParameter("languagename", languagename).list();
        return result;
    }

    /**
     * @param whereString
     * @param valuesMap
     * @param pageNum
     * @param pageSize
     * @param schoolnum
     * @return
     * 
     * 打印准考证信息
     * 
     * 获取准考证设计信息
     */
    public List<Map> getStudentAdmissionInfo(String whereString, Map<String, Object> valuesMap, int pageNum, int pageSize, String schoolnum) {
    	// 首先检查所有考生是否已经编排好准考证
    	
    	if (this.checkAllStudentArrage(schoolnum)) {
            return this.getStudentAdmission(whereString, valuesMap, pageNum, pageSize, schoolnum);
        } else {
            return null;
        }
    }


    /**
     * @param whereString
     * @param valuesMap
     * @param pageNum
     * @param pageSize
     * @param schoolnum
     * @return
     * 
     * 实际获取所有考生的准考证信息
     * 
     */
    public List<Map> getStudentAdmission(String whereString, Map<String, Object> valuesMap, int pageNum, int pageSize, String schoolnum) {

        String hql2 = "select new map(student.name as name,student.idnum as idnum,student.examnum as examnum,student.exLanguage.name as exLanguage,student.exCollege.name as exCollege,student.exCampus.name as exCampus," +
                "student.studentnum as studentnum,student.exProfession.name as exProfession,student.classnum as classnum,student.operateseat as operateseat," +
                "student.exLanguage.theoryflag as theoryflag, student.exLanguage.operateflag as operateflag) " +
                "from  cn.hdu.examsignup.model.ExStudent student ";
        hql2 += whereString;
        hql2 += "and student.exInstitution is not null  and student.exInstitution.institutionnum =:schoolnum   and student.exLanguage is not null and student.exCollege is not null and student.exProfession is not null and student.exCampus is not null order by student.exLanguage.name,student.exCollege.name,student.classnum,student.studentnum,student.idnum";
        Query query2 = this.getCurrentSession().createQuery(hql2).setFirstResult(pageNum).setMaxResults(pageSize).setParameter("schoolnum", schoolnum);
        String[] perchs = valuesMap.keySet().toArray(new String[0]);
        for (int i = 0; i < perchs.length; i++) {
            query2.setParameter(perchs[i], valuesMap.get(perchs[i]));
        }


        List<Map> query_map = query2.list();
        List<Map> result = new ArrayList<Map>();
        Map roomlocationinfo = new HashMap();


        for (Map element2 : query_map) {
            Map admission = new JSONObject();
            String idnum = element2.get("idnum").toString();
            if (element2.get("theoryflag").equals("1") && element2.get("operateflag").equals("1")) {
                String hql3 = "select new map(student.exArrangementByTheoryarrangeid.exPhysicexamroom.roomlocation as theoryroomlocation, student.exArrangementByTheoryarrangeid.exSection.starttime as theorystarttime, student.exLanguage.theorylength as theorylength," +
                        "student.exArrangementByOperatearrangeid.exPhysicexamroom.roomlocation as operateroomlocation, student.exArrangementByOperatearrangeid.exSection.starttime as operatestarttime, student.exLanguage.operatelength as operatelength) " +
                        "from  cn.hdu.examsignup.model.ExStudent student  where student.idnum =:idnum and student.exLanguage is not null and student.exArrangementByTheoryarrangeid is not null and student.exArrangementByTheoryarrangeid is not null and student.exArrangementByTheoryarrangeid.exPhysicexamroom is not null" +
                        " and student.exArrangementByTheoryarrangeid.exPhysicexamroom is not null and student.exArrangementByTheoryarrangeid.exSection is not null and student.exArrangementByOperatearrangeid.exPhysicexamroom is not null and student.exArrangementByOperatearrangeid.exSection is not null order by student.exLanguage.name,student.exCollege.name,student.classnum,student.studentnum,student.idnum";
                Query query = this.getCurrentSession().createQuery(hql3);
                List list = query.setParameter("idnum", idnum).list();
                if (list.size() > 0) {
                    roomlocationinfo = (Map) list.get(0);
                }
            } else if (element2.get("operateflag").equals("1")) {
                String hql3 = "select new map(student.exArrangementByOperatearrangeid.exPhysicexamroom.roomlocation as operateroomlocation, student.exArrangementByOperatearrangeid.exSection.starttime as operatestarttime,student.exLanguage.operatelength as operatelength) " +
                        "from  cn.hdu.examsignup.model.ExStudent student  where " +
                        " student.exArrangementByOperatearrangeid is not null and  student.exArrangementByOperatearrangeid.exPhysicexamroom is not null and student.exArrangementByOperatearrangeid.exSection is not null and student.exLanguage is not null and student.idnum =:idnum order by student.exLanguage.name,student.exCollege.name,student.classnum,student.studentnum,student.idnum";
                Query query = this.getCurrentSession().createQuery(hql3);
                List list = query.setParameter("idnum", idnum).list();
                if (list.size() > 0) {
                    roomlocationinfo = (Map) list.get(0);
                }
            } else {
                String hql3 = "select new map(student.exArrangementByTheoryarrangeid.exPhysicexamroom.roomlocation as theoryroomlocation, student.exArrangementByTheoryarrangeid.exSection.starttime as theorystarttime, student.exLanguage.theorylength as theorylength)" +
                        "from  cn.hdu.examsignup.model.ExStudent student  where " +
                        "student.idnum =:idnum and student.exArrangementByTheoryarrangeid is not null and student.exLanguage is not null and student.exArrangementByTheoryarrangeid.exPhysicexamroom is not null and student.exArrangementByTheoryarrangeid.exSection is not null order by student.exLanguage.name,student.exCollege.name,student.classnum,student.studentnum,student.idnum";
                Query query = this.getCurrentSession().createQuery(hql3);
                List list = query.setParameter("idnum", idnum).list();
                if (list.size() > 0) {
                    roomlocationinfo = (Map) list.get(0);
                }
            }


            SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
            SimpleDateFormat dataFormat2 = new SimpleDateFormat("HH:mm");
            int minuteAmount1 = 0;
            int minuteAmount2 = 0;
            //	if(element2.get("theoryflag") != null && element2.get("theoryflag").equals("1"))
            if (element2.get("theoryflag").equals("1")) {
                Date theorystarttime = (Date) roomlocationinfo.get("theorystarttime");
                Calendar theory = Calendar.getInstance();
                theory.setTime(theorystarttime);
                minuteAmount1 = Integer.parseInt(roomlocationinfo.get("theorylength").toString());
                theory.add(Calendar.MINUTE, minuteAmount1);
                admission.put("theoryendTime", dataFormat2.format(theory.getTime()));
                admission.put("theorystarttime", dataFormat.format(theorystarttime).toString());
                admission.put("theoryroomlocation", roomlocationinfo.get("theoryroomlocation"));
                String exam = (String) element2.get("examnum");
                admission.put("logicExamroomNum", exam.substring(10, 13));
            } else {
                admission.put("theoryendTime", "无");
                admission.put("theorystarttime", "无");
                admission.put("theoryroomlocation", "无");
                admission.put("logicExamroomNum", "无");
            }

            //	if(element2.get("operateflag") != null && element2.get("operateflag").equals("1"))
            if (element2.get("operateflag").equals("1")) {

                Date operatestarttime = (Date) roomlocationinfo.get("operatestarttime");
                Calendar operate = Calendar.getInstance();
                operate.setTime(operatestarttime);
                minuteAmount2 = Integer.parseInt(roomlocationinfo.get("operatelength").toString());
                operate.add(Calendar.MINUTE, minuteAmount2);
                admission.put("operateendTime", dataFormat2.format(operate.getTime()));
                admission.put("operatestarttime", dataFormat.format(operatestarttime).toString());
                admission.put("operateroomlocation", roomlocationinfo.get("operateroomlocation"));
                //	admission.put("operateroomlocation",roomlocationinfo.get("operateroomlocation")+"   第"+element2.get("operateseat")+"座位");
                admission.put("operateseat", element2.get("operateseat"));
            } else {
                admission.put("operateendTime", "无");
                admission.put("operatestarttime", "无");
                admission.put("operateroomlocation", "无");
                admission.put("operateseat", "无");
            }

            admission.put("name", element2.get("name"));
            admission.put("examnum", element2.get("examnum"));
            admission.put("exLanguage", element2.get("exLanguage"));
            admission.put("studentnum", element2.get("studentnum"));
            admission.put("exProfession", element2.get("exProfession"));
            admission.put("idnum", element2.get("idnum"));
            admission.put("exCampus", element2.get("exCampus"));
            admission.put("exCollege", element2.get("exCollege"));
            admission.put("classnum", element2.get("classnum"));
//			admission.put("operateseat",element2.get("operateseat"));
//		    System.out.println(admission);
            result.add(admission);

        }
        return (List<Map>) result;

    }

    //得到某场次某教室某校区某语种的所有上机监考教师
    public List<Map> getOperateExaminerTeacher(String sectionnumTemp, String examroom, String campusname, String languagename, String schoolnum) {
        int sectionnum = Integer.parseInt(sectionnumTemp);
        String hql = "select new map(arrangeSupervisor.exSupervisor.name as supervisor) " +
                "from cn.hdu.examsignup.model.ExArrangeSupervisor arrangeSupervisor ";
        hql += " where arrangeSupervisor.exInstitution.institutionnum =:schoolnum and arrangeSupervisor.exArrangement.exSection.sectionnum=:sectionnum and arrangeSupervisor.exArrangement.exPhysicexamroom.roomlocation=:examroom and arrangeSupervisor.exArrangement.exPhysicexamroom.exCampus.name=:campusname and arrangeSupervisor.exArrangement.exLanguage.name=:languagename ";
        hql += " and arrangeSupervisor.exArrangement.exSection.operateflag='1' order by supervisor ASC ";
        Query query = this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("sectionnum", sectionnum).setParameter("examroom", examroom).setParameter("campusname", campusname).setParameter("languagename", languagename);


        List<Map> result = (List<Map>) query.list();

        return result;

    }

    //得到上机按照场次教室校区语种分组的总数
    public long getOperateExaminerTeacherTotalCount(String whereString, Map<String, Object> valuesMap, String schoolnum) {

        String hql = "select arrangeSupervisor.id from cn.hdu.examsignup.model.ExArrangeSupervisor arrangeSupervisor ";
        hql += whereString;
        hql += " and arrangeSupervisor.exArrangement.exPhysicexamroom.operateflag='1' and arrangeSupervisor.exInstitution.institutionnum =:schoolnum and arrangeSupervisor.exSupervisor is not null group by arrangeSupervisor.exArrangement.exSection.sectionnum,arrangeSupervisor.exArrangement.exPhysicexamroom.roomlocation,arrangeSupervisor.exArrangement.exPhysicexamroom.exCampus.name,arrangeSupervisor.exArrangement.exLanguage.name";
        Query query = this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum);
        String[] perchs = valuesMap.keySet().toArray(new String[0]);
        for (int i = 0; i < perchs.length; i++) {
            query.setParameter(perchs[i], valuesMap.get(perchs[i]));
        }
        return query.list().size();
    }

    ////学校审核数据统计
//		public List<Map> getApprovalData(ExInstitution school){
//			String hql = "select new map(t.exLanguage.name as languagename, count(t) as studentcount) " +
//					"from  cn.hdu.examsignup.model.ExStudent t where t.exInstitution =:school and t.exInstitution.approvalstatus like '审核完毕' group by t.exLanguage order by t.exLanguage.name";
//			Query query = this.getCurrentSession().createQuery(hql).setParameter("school", school);
//			return query.list();
//		}

    public List<Map> statisticAllLanguageBySchoolID(String currentSchoolID, String category) {
        String hql;
        if (category.equals("city")) {
            hql = "select new map(t.exLanguage.languagenum as languagenum, count(t) as studentCount) " +
                    "from  cn.hdu.examsignup.model.ExStudent t where t.exInstitution.higherInstitution.id =:currentSchoolID group by t.exLanguage order by t.exLanguage.languagenum";

        } else {
            hql = "select new map(t.exLanguage.languagenum as languagenum, count(t) as studentCount) " +
                    "from  cn.hdu.examsignup.model.ExStudent t where t.exInstitution.id =:currentSchoolID group by t.exLanguage order by t.exLanguage.languagenum";
        }

        Query query = this.getCurrentSession().createQuery(hql).setParameter("currentSchoolID", currentSchoolID);
        return query.list();
    }

    //收索出所有未报名学生的信息
    public List<Map> unSignUpStudentShow(String whereString, Map<String, Object> valuesMap, int pageNum, int pageSize, String schoolnum) {
        String hql = "select new map(student.id as id, student.exInstitution.name as exInstitution, student.exProfession.name as exProfession," +
                " student.exCollege.name as exCollege,student.ethno as ethno," +
//					"case when student.exLanguage is null then '' else student.exLanguage.name end as exLanguage," +
                " student.exCampus.name as exCampus, student.name as name, student.password as password," +
                " student.sex as sex, student.studentnum as studentnum, student.idnum as idnum," +
                " student.grade as grade,student.classnum as classnum," +
                " student.lengthofyear as lengthofyear, student.studentcategory as studentcategory, student.paied as paied) " +
                "from cn.hdu.examsignup.model.ExStudent student ";
        hql += whereString;
        hql += " and student.exInstitution.institutionnum =:schoolnum  and student.paied='0' order by student.exCampus.campusnum,student.classnum,student.studentnum ";
        Query query = this.getCurrentSession().createQuery(hql).setFirstResult(pageNum).setMaxResults(pageSize).setParameter("schoolnum", schoolnum);
        String[] perchs = valuesMap.keySet().toArray(new String[0]);
        for (int i = 0; i < perchs.length; i++) {
            query.setParameter(perchs[i], valuesMap.get(perchs[i]));
        }

        return query.list();
    }

    //得到未报名考生的总人数
    public long getUnSignUpStudentsTotalCount(String whereString, Map<String, Object> valuesMap, String schoolnum) {
        String hql = "select student.id from cn.hdu.examsignup.model.ExStudent student ";
        hql += whereString;
        hql += " and student.exInstitution.institutionnum =:schoolnum and student.paied='0'";
        Query query = this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum);
        String[] perchs = valuesMap.keySet().toArray(new String[0]);
        for (int i = 0; i < perchs.length; i++) {
            query.setParameter(perchs[i], valuesMap.get(perchs[i]));
        }
        return query.list().size();
    }

    //收索出所有未报名学生的信息
    public List<Map> getAllStudentsUnsignup(String schoolnum) {
        String hql = "select new map(student.studentnum as studentnum, student.name as name," +
                "student.classnum as classnum,student.exLanguage.name as languagename ," +
                "student.idnum as idnum,student.exInstitution.name as exInstitution, " +
                "student.exCollege.name as exCollege, student.exProfession.name as exProfession," +
                "student.exCampus.name as exCampus,student.grade as grade,student.paied as paied)" +
                "from cn.hdu.examsignup.model.ExStudent student " +
                "where student.exInstitution.institutionnum =:schoolnum and student.exLanguage is not null and student.paied = '0' " +
                "order by student.exCampus.name, student.exCollege.name, student.classnum, student.exLanguage.name, student.studentnum";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).list();
    }

    //统计语种人数分校区
    public List<Map> getStuCountByLangGroupByCampus(String languagenum, String institutionnum) {
        String hql = "select new map(count(t.id) as stucount, t.exCampus.campusnum as campusnum, t.exCampus.name as campusname)from cn.hdu.examsignup.model.ExStudent t " +
                "where t.exCampus is not null and t.exLanguage.languagenum =:languagenum and t.exInstitution.institutionnum =:institutionnum " +
                "group by t.exCampus.campusnum";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("languagenum", languagenum).setParameter("institutionnum", institutionnum).list();
    }


    public boolean checkAllStudentArrage(String schoolnum) {
        String hql = "select count(t) from cn.hdu.examsignup.model.ExStudent t where" +
                " t.exLanguage is not null and t.exInstitution is not null  and t.exInstitution.institutionnum =:schoolnum and " +
                "((t.exLanguage.theoryflag like '1' and t.exArrangementByTheoryarrangeid is null) or (t.exLanguage.operateflag like '1' and t.exArrangementByOperatearrangeid is null))";
        long count = (Long) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).list().get(0);
        if (count == 0) {
            return true;
        } else {
            return false;
        }
    }/*
		//获取已编排考生人数
		public long getCountAllStudentArrage(String schoolnum){
			String hql = "select count(t) from cn.hdu.examsignup.model.ExStudent t where" +
					" t.exLanguage is not null and t.exInstitution is not null  and t.exInstitution.institutionnum =:schoolnum and " +
					"((t.exLanguage.theoryflag like '1' and t.exArrangementByTheoryarrangeid is not null) or (t.exLanguage.operateflag like '1' and t.exArrangementByOperatearrangeid is not null))";
			long count = (Long)this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).list().get(0);
			return count;
		}  */

    //统计报名考生校区
    public List<Map> getStuCampus(String institutionnum) {
        String hql = "select new map(t.exCampus.campusnum as campusnum, t.exCampus.name as campusname)from cn.hdu.examsignup.model.ExStudent t " +
                "where t.exCampus is not null and t.exInstitution is not  null and t.exInstitution.institutionnum =:institutionnum " +
                "group by t.exCampus.campusnum";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum).list();
    }

    //统计校区考试袋
    public Map calcStuCountCampus(String institutionnum, String campusnum, String languagenum) {
        String hql = "select new map(count(t.id) as stucount)from cn.hdu.examsignup.model.ExStudent t " +
                "where t.exCampus is not null and t.exLanguage.languagenum =:languagenum and t.exInstitution.institutionnum =:institutionnum and t.exCampus.campusnum =:campusnum";
        return (Map) this.getCurrentSession().createQuery(hql).setParameter("languagenum", languagenum).setParameter("institutionnum", institutionnum).setParameter("campusnum", campusnum).list().get(0);
    }

    public List<Map> getOneStudentAdmission(String whereString, Map<String, Object> valuesMap, int pageNum, int pageSize, String idnum) {
        if (this.checkStudentArrage(idnum)) {
            return this.getOneStudentAdmissionInfo(whereString, valuesMap, pageNum, pageSize, idnum);
        } else {
            return null;
        }
    }


    //得到单个考生的打印信息，用于考生端
    public List<Map> getOneStudentAdmissionInfo(String whereString, Map<String, Object> valuesMap, int pageNum, int pageSize, String idnum) {
        String hql2 = "select new map(student.name as name,student.idnum as idnum,student.examnum as examnum,student.exLanguage.name as exLanguage,student.exCollege.name as exCollege,student.exCampus.name as exCampus," +
                "student.studentnum as studentnum,student.exProfession.name as exProfession,student.operateseat as operateseat,student.classnum as classnum," +
                "student.exLanguage.theoryflag as theoryflag, student.exLanguage.operateflag as operateflag) " +
                "from  cn.hdu.examsignup.model.ExStudent student " +
                "where student.exInstitution is not null  and student.idnum =:idnum   and student.exLanguage is not null and student.exCollege is not null and student.exProfession is not null and student.exCampus is not null ";
        Map query2 = (Map) this.getCurrentSession().createQuery(hql2).setParameter("idnum", idnum).list().get(0);
        Map admission = new JSONObject();
        Map roomlocationinfo = new JSONObject();
        List<Map> result = new ArrayList<Map>();

        System.out.println(idnum);


        //	Map admission = new JSONObject();
        //	String idnum=query2.get("idnum").toString();
        if (query2.get("theoryflag").equals("1") && query2.get("operateflag").equals("1")) {
            String hql3 = "select new map(student.exArrangementByTheoryarrangeid.exPhysicexamroom.roomlocation as theoryroomlocation, student.exArrangementByTheoryarrangeid.exSection.starttime as theorystarttime, student.exLanguage.theorylength as theorylength," +
                    "student.exArrangementByOperatearrangeid.exPhysicexamroom.roomlocation as operateroomlocation, student.exArrangementByOperatearrangeid.exSection.starttime as operatestarttime, student.exLanguage.operatelength as operatelength) " +
                    "from  cn.hdu.examsignup.model.ExStudent student  where student.idnum =:idnum  and  student.exLanguage is not null and student.exArrangementByTheoryarrangeid is not null and student.exArrangementByTheoryarrangeid is not null and student.exArrangementByTheoryarrangeid.exPhysicexamroom is not null" +
                    " and student.exArrangementByTheoryarrangeid.exPhysicexamroom is not null and student.exArrangementByTheoryarrangeid.exSection is not null and student.exArrangementByOperatearrangeid.exPhysicexamroom is not null and student.exArrangementByOperatearrangeid.exSection is not null";
            Query query = this.getCurrentSession().createQuery(hql3);
            List list = query.setParameter("idnum", idnum).list();
            if (list.size() > 0) {
                roomlocationinfo = (Map) list.get(0);
            }
        } else if (query2.get("operateflag").equals("1")) {
            String hql3 = "select new map(student.exArrangementByOperatearrangeid.exPhysicexamroom.roomlocation as operateroomlocation, student.exArrangementByOperatearrangeid.exSection.starttime as operatestarttime,student.exLanguage.operatelength as operatelength) " +
                    "from  cn.hdu.examsignup.model.ExStudent student  where " +
                    " student.exArrangementByOperatearrangeid is not null and  student.exArrangementByOperatearrangeid.exPhysicexamroom is not null and student.exArrangementByOperatearrangeid.exSection is not null and student.exLanguage is not null and student.idnum =:idnum";
            Query query = this.getCurrentSession().createQuery(hql3);
            List list = query.setParameter("idnum", idnum).list();
            if (list.size() > 0) {
                roomlocationinfo = (Map) list.get(0);
            }
        } else {
            String hql3 = "select new map(student.exArrangementByTheoryarrangeid.exPhysicexamroom.roomlocation as theoryroomlocation, student.exArrangementByTheoryarrangeid.exSection.starttime as theorystarttime, student.exLanguage.theorylength as theorylength)" +
                    "from  cn.hdu.examsignup.model.ExStudent student  where " +
                    "student.idnum =:idnum and student.exArrangementByTheoryarrangeid is not null and student.exLanguage is not null and student.exArrangementByTheoryarrangeid.exPhysicexamroom is not null and student.exArrangementByTheoryarrangeid.exSection is not null";
            Query query = this.getCurrentSession().createQuery(hql3);
            List list = query.setParameter("idnum", idnum).list();
            if (list.size() > 0) {
                roomlocationinfo = (Map) list.get(0);
            }
        }


        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        SimpleDateFormat dataFormat2 = new SimpleDateFormat("HH:mm");
        int minuteAmount1 = 0;
        int minuteAmount2 = 0;
        //	if(element2.get("theoryflag") != null && element2.get("theoryflag").equals("1"))
        if (query2.get("theoryflag").equals("1")) {
            Date theorystarttime = (Date) roomlocationinfo.get("theorystarttime");
            Calendar theory = Calendar.getInstance();
            theory.setTime(theorystarttime);
            minuteAmount1 = Integer.parseInt(roomlocationinfo.get("theorylength").toString());
            theory.add(Calendar.MINUTE, minuteAmount1);
            admission.put("theoryendTime", dataFormat2.format(theory.getTime()));
            admission.put("theorystarttime", dataFormat.format(theorystarttime).toString());
            admission.put("theoryroomlocation", roomlocationinfo.get("theoryroomlocation"));
            String exam = (String) query2.get("examnum");
            admission.put("logicExamroomNum", exam.substring(10, 13));
        } else {
            admission.put("theoryendTime", "无");
            admission.put("theorystarttime", "无");
            admission.put("theoryroomlocation", "无");
            admission.put("logicExamroomNum", "无");
        }

        //	if(element2.get("operateflag") != null && element2.get("operateflag").equals("1"))
        if (query2.get("operateflag").equals("1")) {

            Date operatestarttime = (Date) roomlocationinfo.get("operatestarttime");
            Calendar operate = Calendar.getInstance();
            operate.setTime(operatestarttime);
            minuteAmount2 = Integer.parseInt(roomlocationinfo.get("operatelength").toString());
            operate.add(Calendar.MINUTE, minuteAmount2);
            admission.put("operateendTime", dataFormat2.format(operate.getTime()));
            admission.put("operatestarttime", dataFormat.format(operatestarttime).toString());
            admission.put("operateroomlocation", roomlocationinfo.get("operateroomlocation"));
        } else {
            admission.put("operateendTime", "无");
            admission.put("operatestarttime", "无");
            admission.put("operateroomlocation", "无");

        }

        admission.put("name", query2.get("name"));
        admission.put("examnum", query2.get("examnum"));
        admission.put("exLanguage", query2.get("exLanguage"));
        admission.put("studentnum", query2.get("studentnum"));
        admission.put("exProfession", query2.get("exProfession"));
        admission.put("operateseat", query2.get("operateseat"));
        admission.put("idnum", query2.get("idnum"));
        admission.put("classnum", query2.get("classnum"));
        admission.put("exCampus", query2.get("exCampus"));
        admission.put("exCollege", query2.get("exCollege"));
        System.out.println(admission);
        result.add(admission);


        return (List<Map>) result;


    }

    public boolean checkStudentArrage(String idnum) {
        String hql = "select count(t) from cn.hdu.examsignup.model.ExStudent t where" +
                " t.exLanguage is not null and t.exInstitution is not null  and t.idnum =:idnum and " +
                "((t.exLanguage.theoryflag like '1' and t.exArrangementByTheoryarrangeid is null) or (t.exLanguage.operateflag like '1' and t.exArrangementByOperatearrangeid is null))";
        long count = (Long) this.getCurrentSession().createQuery(hql).setParameter("idnum", idnum).list().get(0);
        if (count == 0) {
            return true;
        } else {
            return false;
        }
    }

    //得到班级列表
    public List<Map> getClassNameList(String collegename, String schoolnum) {
        String hql = "select new map(t.classnum as name) from cn.hdu.examsignup.model.ExStudent t where t.exInstitution.institutionnum=:schoolnum and t.exCollege.name=:collegename group by  t.classnum order by t.classnum";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("collegename", collegename).list();
    }


    public long getCountAllStudentArrage(String whereString, Map<String, Object> valuesMap, String schoolnum) {
        String hql = "select student.id from cn.hdu.examsignup.model.ExStudent student ";
        hql += whereString;
        hql += "  and student.exLanguage is not null and student.exInstitution is not null  and student.exInstitution.institutionnum =:schoolnum and ((student.exLanguage.theoryflag like '1' and student.exArrangementByTheoryarrangeid is not null) or (student.exLanguage.operateflag like '1' and student.exArrangementByOperatearrangeid is not null)) ";
        Query query = this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum);
        String[] perchs = valuesMap.keySet().toArray(new String[0]);
        for (int i = 0; i < perchs.length; i++) {
            query.setParameter(perchs[i], valuesMap.get(perchs[i]));
        }
        return query.list().size();
    }

    //按照上机场次教室语种分类
    public List<Map> getSectionAndRoomAndLanguage(String whereString, Map<String, Object> valuesMap, int pageNum, int pageSize, String schoolnum) {
        String hql = "select new map(arrangeSupervisor.exArrangement.exSection.sectionnum as sectionnum,arrangeSupervisor.exArrangement.exPhysicexamroom.roomlocation as examroom,arrangeSupervisor.exArrangement.exPhysicexamroom.exCampus.name as campusname,arrangeSupervisor.exArrangement.exLanguage.name as languagename," +
                " arrangeSupervisor.exArrangement.exSection.operateflag as operateflag,arrangeSupervisor.exArrangement.exSection.starttime as startTime) " +
                "from cn.hdu.examsignup.model.ExArrangeSupervisor arrangeSupervisor ";
        hql += whereString;
        hql += " and arrangeSupervisor.exArrangement.exSection.operateflag='1' and arrangeSupervisor.exInstitution.institutionnum =:schoolnum group by arrangeSupervisor.exArrangement.exSection.sectionnum ,arrangeSupervisor.exArrangement.exPhysicexamroom.roomlocation,arrangeSupervisor.exArrangement.exPhysicexamroom.exCampus.name,arrangeSupervisor.exArrangement.exLanguage.name  order by sectionnum ASC,arrangeSupervisor.exArrangement.exPhysicexamroom.roomlocation ASC,campusname ASC,languagename ASC ";
        Query query = this.getCurrentSession().createQuery(hql).setFirstResult(pageNum).setMaxResults(pageSize).setParameter("schoolnum", schoolnum);
        String[] perchs = valuesMap.keySet().toArray(new String[0]);
        for (int i = 0; i < perchs.length; i++) {
            query.setParameter(perchs[i], valuesMap.get(perchs[i]));
        }


        List<Map> result = (List<Map>) query.list();
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        //SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        for (Map element : result) {
            Date startTime = (Date) element.get("startTime");
            if (element.get("startTime") != null) {
                element.put("startTime", dataFormat.format(startTime));

            }
        }
        return result;

    }


    //按照理论考场教室校区语种分类
    public List<Map> logicroomAndLocalroomAndLanguage(String whereString, Map<String, Object> valuesMap, int pageNum, int pageSize, String schoolnum) {
				/*	String hql = "select new map(arrangeSupervisor.logicExamroomNum as logicexamroomnum,arrangeSupervisor.exArrangement.exPhysicexamroom.roomlocation as examroom,arrangeSupervisor.exArrangement.exPhysicexamroom.exCampus.name as campusname,arrangeSupervisor.exArrangement.exLanguage.name as languagename," +
							" arrangeSupervisor.exArrangement.exPhysicexamroom.theoryflag as theoryflag,arrangeSupervisor.exArrangement.exSection.starttime as startTime,arrangeSupervisor.exSupervisor.name as supervisor) " +
							     "from cn.hdu.examsignup.model.ExArrangeSupervisor arrangeSupervisor ";
					hql += whereString;
					hql +=" and arrangeSupervisor.exArrangement.exPhysicexamroom.theoryflag='1' and arrangeSupervisor.exInstitution.institutionnum =:schoolnum  order by  arrangeSupervisor.logicExamroomNum";
					Query query = this.getCurrentSession().createQuery(hql).setFirstResult(pageNum).setMaxResults(pageSize).setParameter("schoolnum", schoolnum);
					String[] perchs = valuesMap.keySet().toArray(new String[0]);
					for(int i = 0; i < perchs.length; i++){
						query.setParameter(perchs[i], valuesMap.get(perchs[i]));
					}*/


        String hql = "select new map(arrangeSupervisor.logicExamroomNum as logicexamroomnum,arrangeSupervisor.exArrangement.exPhysicexamroom.roomlocation as examroom,arrangeSupervisor.exArrangement.exPhysicexamroom.exCampus.name as campusname,arrangeSupervisor.exArrangement.exLanguage.name as languagename," +
                " arrangeSupervisor.exArrangement.exPhysicexamroom.theoryflag as theoryflag,arrangeSupervisor.exArrangement.exSection.starttime as startTime,arrangeSupervisor.exSupervisor.name as supervisor) " +
                "from cn.hdu.examsignup.model.ExArrangeSupervisor arrangeSupervisor ";
        hql += whereString;
        hql += " and arrangeSupervisor.exArrangement.exPhysicexamroom.theoryflag='1' and arrangeSupervisor.exInstitution.institutionnum =:schoolnum group by arrangeSupervisor.logicExamroomNum ,arrangeSupervisor.exArrangement.exPhysicexamroom.roomlocation,arrangeSupervisor.exArrangement.exPhysicexamroom.exCampus.name,arrangeSupervisor.exArrangement.exLanguage.name  order by arrangeSupervisor.logicExamroomNum ASC,arrangeSupervisor.exArrangement.exPhysicexamroom.roomlocation ASC,campusname ASC,languagename ASC ";
        Query query = this.getCurrentSession().createQuery(hql).setFirstResult(pageNum).setMaxResults(pageSize).setParameter("schoolnum", schoolnum);
        String[] perchs = valuesMap.keySet().toArray(new String[0]);
        for (int i = 0; i < perchs.length; i++) {
            query.setParameter(perchs[i], valuesMap.get(perchs[i]));
        }


        List<Map> result = (List<Map>) query.list();
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        //SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        for (Map element : result) {
            Date startTime = (Date) element.get("startTime");
            if (element.get("startTime") != null) {
                element.put("startTime", dataFormat.format(startTime));

            }
        }
        return result;

    }


    //从数据库中获取打印准考证所需的那句注释
    public List<Map> getRemarkAboutZKZ() {
        String hql = "select  new map(a.paramvalue as remark) from cn.hdu.examsignup.model.ExParameter a where a.name='打印准考证备注'";
        return this.getCurrentSession().createQuery(hql).list();
    }


    //用于打印准考证界面中的，导出考生准考证信息
    public List<Map> getStudentAdmissionExcel(String schoolnum) {

        String hql2 = "select new map(student.name as name,student.idnum as idnum,student.examnum as examnum,student.exLanguage.name as exLanguage,student.exCollege.name as exCollege,student.exCampus.name as exCampus," +
                "student.studentnum as studentnum,student.exProfession.name as exProfession,student.classnum as classnum,student.operateseat as operateseat," +
                "student.exLanguage.theoryflag as theoryflag, student.exLanguage.operateflag as operateflag) " +
                "from  cn.hdu.examsignup.model.ExStudent student " +
                "where student.exInstitution is not null  " +
                "and student.exInstitution.institutionnum =:schoolnum   " +
                "and student.exLanguage is not null " +
                "and student.exCollege is not null " +
                "and student.exProfession is not null " +
                "and student.exCampus is not null order by student.exCollege.name,student.classnum,student.exLanguage.name,student.studentnum,student.idnum";
        Query query2 = this.getCurrentSession().createQuery(hql2).setParameter("schoolnum", schoolnum);

        List<Map> query_map = query2.list();
        List<Map> result = new ArrayList<Map>();
        Map roomlocationinfo = new HashMap();


        for (Map element2 : query_map) {
            Map admission = new JSONObject();
            String idnum = element2.get("idnum").toString();
            if (element2.get("theoryflag").equals("1") && element2.get("operateflag").equals("1")) {
                String hql3 = "select new map(student.exArrangementByTheoryarrangeid.exPhysicexamroom.roomlocation as theoryroomlocation, student.exArrangementByTheoryarrangeid.exSection.starttime as theorystarttime, student.exLanguage.theorylength as theorylength," +
                        "student.exArrangementByOperatearrangeid.exPhysicexamroom.roomlocation as operateroomlocation, student.exArrangementByOperatearrangeid.exSection.starttime as operatestarttime, student.exLanguage.operatelength as operatelength) " +
                        "from  cn.hdu.examsignup.model.ExStudent student  where student.idnum =:idnum and student.exLanguage is not null and student.exArrangementByTheoryarrangeid is not null and student.exArrangementByTheoryarrangeid is not null and student.exArrangementByTheoryarrangeid.exPhysicexamroom is not null" +
                        " and student.exArrangementByTheoryarrangeid.exPhysicexamroom is not null and student.exArrangementByTheoryarrangeid.exSection is not null and student.exArrangementByOperatearrangeid.exPhysicexamroom is not null and student.exArrangementByOperatearrangeid.exSection is not null";
                Query query = this.getCurrentSession().createQuery(hql3);
                List list = query.setParameter("idnum", idnum).list();
                if (list.size() > 0) {
                    roomlocationinfo = (Map) list.get(0);
                }
            } else if (element2.get("operateflag").equals("1")) {
                String hql3 = "select new map(student.exArrangementByOperatearrangeid.exPhysicexamroom.roomlocation as operateroomlocation, student.exArrangementByOperatearrangeid.exSection.starttime as operatestarttime,student.exLanguage.operatelength as operatelength) " +
                        "from  cn.hdu.examsignup.model.ExStudent student " +
                        "where student.idnum =:idnum and student.exArrangementByOperatearrangeid is not null and  student.exArrangementByOperatearrangeid.exPhysicexamroom is not null and student.exArrangementByOperatearrangeid.exSection is not null and student.exLanguage is not null ";
                Query query = this.getCurrentSession().createQuery(hql3);
                List list = query.setParameter("idnum", idnum).list();
                if (list.size() > 0) {
                    roomlocationinfo = (Map) list.get(0);
                }
            } else {
                String hql3 = "select new map(student.exArrangementByTheoryarrangeid.exPhysicexamroom.roomlocation as theoryroomlocation, student.exArrangementByTheoryarrangeid.exSection.starttime as theorystarttime, student.exLanguage.theorylength as theorylength) " +
                        "from cn.hdu.examsignup.model.ExStudent student " +
                        "where student.idnum =:idnum and student.exArrangementByTheoryarrangeid is not null and student.exLanguage is not null and student.exArrangementByTheoryarrangeid.exPhysicexamroom is not null and student.exArrangementByTheoryarrangeid.exSection is not null";
                Query query = this.getCurrentSession().createQuery(hql3);
                List list = query.setParameter("idnum", idnum).list();
                if (list.size() > 0) {
                    roomlocationinfo = (Map) list.get(0);
                }
            }


            SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
            SimpleDateFormat dataFormat2 = new SimpleDateFormat("HH:mm");
            int minuteAmount1 = 0;
            int minuteAmount2 = 0;
            //	if(element2.get("theoryflag") != null && element2.get("theoryflag").equals("1"))
            if (element2.get("theoryflag").equals("1")) {
                Date theorystarttime = (Date) roomlocationinfo.get("theorystarttime");
                Calendar theory = Calendar.getInstance();
                theory.setTime(theorystarttime);
                minuteAmount1 = Integer.parseInt(roomlocationinfo.get("theorylength").toString());
                theory.add(Calendar.MINUTE, minuteAmount1);
                admission.put("theoryendTime", dataFormat2.format(theory.getTime()));
                admission.put("theorystarttime", dataFormat.format(theorystarttime).toString());
                admission.put("theoryroomlocation", roomlocationinfo.get("theoryroomlocation"));
                String exam = (String) element2.get("examnum");
                admission.put("logicExamroomNum", exam.substring(10, 13));
            } else {
                admission.put("theoryendTime", "无");
                admission.put("theorystarttime", "无");
                admission.put("theoryroomlocation", "无");
                admission.put("logicExamroomNum", "无");
            }

            //	if(element2.get("operateflag") != null && element2.get("operateflag").equals("1"))
            if (element2.get("operateflag").equals("1")) {

                Date operatestarttime = (Date) roomlocationinfo.get("operatestarttime");
                Calendar operate = Calendar.getInstance();
                operate.setTime(operatestarttime);
                minuteAmount2 = Integer.parseInt(roomlocationinfo.get("operatelength").toString());
                operate.add(Calendar.MINUTE, minuteAmount2);
                admission.put("operateendTime", dataFormat2.format(operate.getTime()));
                admission.put("operatestarttime", dataFormat.format(operatestarttime).toString());
                admission.put("operateroomlocation", roomlocationinfo.get("operateroomlocation"));
                admission.put("operateseat", element2.get("operateseat"));
            } else {
                admission.put("operateendTime", "无");
                admission.put("operatestarttime", "无");
                admission.put("operateroomlocation", "无");
                admission.put("operateseat", "无");
            }

            admission.put("name", element2.get("name"));
            admission.put("examnum", element2.get("examnum"));
            admission.put("exLanguage", element2.get("exLanguage"));
            admission.put("studentnum", element2.get("studentnum"));
            admission.put("exProfession", element2.get("exProfession"));
            admission.put("idnum", element2.get("idnum"));
            admission.put("exCampus", element2.get("exCampus"));
            admission.put("exCollege", element2.get("exCollege"));
            admission.put("classnum", element2.get("classnum"));

            result.add(admission);
        }

        return (List<Map>) result;

    }


    //得到一个学校各个学院某个语种不及格的成绩人数
    public List<Map> getScoreInfo_1(String schoolnum, String languagenum, HashMap summaryCondition) {
        String passline = summaryCondition.get("passLine").toString();//得到及格线
        BigDecimal passLine = new BigDecimal(passline);
        String hql = "select new map(student.exCollege.name as collegename,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum=:schoolnum and student.exLanguage.languagenum=:languagenum and student.score<:passLine group by student.exCollege.name ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("languagenum", languagenum).setParameter("passLine", passLine).list();
    }

    //得到一个学校各个学院某个语种及格的成绩人数
    public List<Map> getScoreInfo_2(String schoolnum, String languagenum, HashMap summaryCondition) {
        String passline = summaryCondition.get("passLine").toString();//得到及格线
        BigDecimal passLine = new BigDecimal(passline);
        String hql = "select new map(student.exCollege.name as collegename,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum=:schoolnum and student.exLanguage.languagenum=:languagenum and student.score>=:passLine group by student.exCollege.name ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("languagenum", languagenum).setParameter("passLine", passLine).list();
    }

    //得到一个学校各个学院某个语种优秀的成绩人数
    public List<Map> getScoreInfo_3(String schoolnum, String languagenum, HashMap summaryCondition) {
        String excellentline = summaryCondition.get("excellentLine").toString();//得到及优秀线
        BigDecimal excellentLine = new BigDecimal(excellentline);
        String hql = "select new map(student.exCollege.name as collegename,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum=:schoolnum and student.exLanguage.languagenum=:languagenum and student.score>=:excellentLine group by student.exCollege.name ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("languagenum", languagenum).setParameter("excellentLine", excellentLine).list();
    }


    //得到一个学校各个年级某个语种不及格的成绩人数
    public List<Map> getScoreInfo_getUnPassStudent(String schoolnum, String languagenum, HashMap summaryCondition) {
        String passline = summaryCondition.get("passLine").toString();//得到及格线
        BigDecimal passLine = new BigDecimal(passline);
        String hql = "select new map(student.grade as grade,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum=:schoolnum and student.exLanguage.languagenum=:languagenum and student.score<:passLine group by student.grade ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("languagenum", languagenum).setParameter("passLine", passLine).list();
    }

    //得到一个学校各个年级某个语种及格的成绩人数
    public List<Map> getScoreInfo_getPassStudent(String schoolnum, String languagenum, HashMap summaryCondition) {
        String passline = summaryCondition.get("passLine").toString();//得到及格线
        BigDecimal passLine = new BigDecimal(passline);
        String hql = "select new map(student.grade as grade,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum=:schoolnum and student.exLanguage.languagenum=:languagenum and student.score>=:passLine group by student.grade ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("languagenum", languagenum).setParameter("passLine", passLine).list();
    }

    //得到一个学校各个年级某个语种优秀的成绩人数
    public List<Map> getScoreInfo_getExcellentStudent(String schoolnum, String languagenum, HashMap summaryCondition) {
        String excellentline = summaryCondition.get("excellentLine").toString();//得到及优秀线
        BigDecimal excellentLine = new BigDecimal(excellentline);
        String hql = "select new map(student.grade as grade,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum=:schoolnum and student.exLanguage.languagenum=:languagenum and student.score>=:excellentLine group by student.grade ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("languagenum", languagenum).setParameter("excellentLine", excellentLine).list();
    }

    //得到一个学校某个 学院各个年级某个语种不及格的成绩人数
    public List<Map> getScoreInfo_getUnPassStudent(String schoolnum, String collegename, String languagenum, HashMap summaryCondition) {
        String passline = summaryCondition.get("passLine").toString();//得到及格线
        BigDecimal passLine = new BigDecimal(passline);
        String hql = "select new map(student.grade as grade,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum=:schoolnum and student.exCollege.name=:collegename and student.exLanguage.languagenum=:languagenum and student.score<:passLine group by student.grade ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("collegename", collegename).setParameter("languagenum", languagenum).setParameter("passLine", passLine).list();
    }

    //得到一个学校某个 学院各个年级某个语种及格的成绩人数
    public List<Map> getScoreInfo_getPassStudent(String schoolnum, String collegename, String languagenum, HashMap summaryCondition) {
        String passline = summaryCondition.get("passLine").toString();//得到及格线
        BigDecimal passLine = new BigDecimal(passline);
        String hql = "select new map(student.grade as grade,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum=:schoolnum and student.exCollege.name=:collegename and student.exLanguage.languagenum=:languagenum and student.score>=:passLine group by student.grade ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("collegename", collegename).setParameter("languagenum", languagenum).setParameter("passLine", passLine).list();
    }

    //得到一个学校某个 学院各个年级某个语种优秀的成绩人数
    public List<Map> getScoreInfo_getExcellentStudent(String schoolnum, String collegename, String languagenum, HashMap summaryCondition) {
        String excellentline = summaryCondition.get("excellentLine").toString();//得到及优秀线
        BigDecimal excellentLine = new BigDecimal(excellentline);
        String hql = "select new map(student.grade as grade,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum=:schoolnum and student.exCollege.name=:collegename and student.exLanguage.languagenum=:languagenum and student.score>=:excellentLine group by student.grade ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("collegename", collegename).setParameter("languagenum", languagenum).setParameter("excellentLine", excellentLine).list();
    }


    //得到一个学校的某个 年级某个 语种  各个学院不及格的成绩人数
    public List<Map> getScoreInfoByGrade_College_getUnPassStudent(String schoolnum, String grade, String languagenum, HashMap summaryCondition) {
        String passline = summaryCondition.get("passLine").toString();//得到及格线
        BigDecimal passLine = new BigDecimal(passline);
        String hql = "select new map(student.exCollege.name as collegename,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum=:schoolnum and student.grade=:grade and student.exLanguage.languagenum=:languagenum and student.score<:passLine group by student.exCollege.name ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("grade", grade).setParameter("languagenum", languagenum).setParameter("passLine", passLine).list();
    }

    //得到一个学校某个 年级某个语种 各个学院及格的成绩人数
    public List<Map> getScoreInfoByGrade_College_getPassStudent(String schoolnum, String grade, String languagenum, HashMap summaryCondition) {
        String passline = summaryCondition.get("passLine").toString();//得到及格线
        BigDecimal passLine = new BigDecimal(passline);
        String hql = "select new map(student.exCollege.name as collegename,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum=:schoolnum and student.grade=:grade and student.exLanguage.languagenum=:languagenum and student.score>=:passLine group by student.exCollege.name ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("grade", grade).setParameter("languagenum", languagenum).setParameter("passLine", passLine).list();
    }

    //得到一个学校某个 年级某个语种 各个学院优秀的成绩人数
    public List<Map> getScoreInfoByGrade_College_getExcellentStudent(String schoolnum, String grade, String languagenum, HashMap summaryCondition) {
        String excellentline = summaryCondition.get("excellentLine").toString();//得到及优秀线
        BigDecimal excellentLine = new BigDecimal(excellentline);
        String hql = "select new map(student.exCollege.name as collegename,count(student) as studentcount) from cn.hdu.examsignup.model.ExStudent student where student.exInstitution.institutionnum=:schoolnum and student.grade=:grade and student.exLanguage.languagenum=:languagenum and student.score>=:excellentLine group by student.exCollege.name ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum).setParameter("grade", grade).setParameter("languagenum", languagenum).setParameter("excellentLine", excellentLine).list();
    }


    //统计语种人数分校区
    public List<Map> getStuCountByLangGroupByCollege(String languagenum, String collegenum) {
        String hql = "select new map(count(t.id) as stucount, t.exCollege.name as collegename)from cn.hdu.examsignup.model.ExStudent t " +
                "where t.exCollege is not null and t.exLanguage.languagenum =:languagenum and t.exCollege.collegenum =:collegenum ";
        return (List<Map>) this.getCurrentSession().createQuery(hql).setParameter("languagenum", languagenum).setParameter("collegenum", collegenum).list();
    }


    //得到某学院某语种的人数
    public long getStudentCount(String languagenum, String collegenum) {
        String hql = "select t.id from cn.hdu.examsignup.model.ExStudent t " +
                "where t.exCollege is not null and t.exLanguage.languagenum =:languagenum and t.exCollege.collegenum =:collegenum ";
        Query query = this.getCurrentSession().createQuery(hql).setParameter("languagenum", languagenum).setParameter("collegenum", collegenum);
        return query.list().size();
    }


    public Map calcStuCountCollege(String institutionnum, String collegename, String languagenum) {
        String hql = "select new map(count(t.id) as stucount)from cn.hdu.examsignup.model.ExStudent t " +
                "where t.exCollege is not null and t.exLanguage.languagenum =:languagenum and t.exInstitution.institutionnum =:institutionnum and t.exCollege.name =:collegename";
        return (Map) this.getCurrentSession().createQuery(hql).setParameter("languagenum", languagenum).setParameter("institutionnum", institutionnum).setParameter("collegename", collegename).list().get(0);
    }


    public List<Map> getAllGrades(String institutionnum) {
        String hql = "select new map(t.grade as gradename) from cn.hdu.examsignup.model.ExStudent t where t.exInstitution.institutionnum =:institutionnum group by t.grade";
        Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum);
        return (List<Map>) query.list();
    }


    public Map calcStuCountGrade(String institutionnum, String grade, String languagenum) {
        String hql = "select new map(count(t.id) as stucount)from cn.hdu.examsignup.model.ExStudent t " +
                "where t.exLanguage.languagenum =:languagenum and t.exInstitution.institutionnum =:institutionnum and t.grade =:grade";
        return (Map) this.getCurrentSession().createQuery(hql).setParameter("languagenum", languagenum).setParameter("institutionnum", institutionnum).setParameter("grade", grade).list().get(0);
    }

    public long calc_Grade_TotalStu(String institutionnum, String grade) {
        String hql = "select t.id from cn.hdu.examsignup.model.ExStudent t " +
                "where  t.exInstitution.institutionnum =:institutionnum and t.grade =:grade";
        return this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum).setParameter("grade", grade).list().size();
    }


    public long getStudentsOperateAbsenceTotalCount(String whereString, Map<String, Object> valuesMap, String schoolnum) {
        String hql = "select student.id from cn.hdu.examsignup.model.ExStudent student ";
        hql += whereString;
        hql += " and student.exInstitution.institutionnum =:schoolnum and student.exLanguage is not null" +
                " and student.paied='1' and student.operateabsent='1'";
        Query query = this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum);
        String[] perchs = valuesMap.keySet().toArray(new String[0]);
        for (int i = 0; i < perchs.length; i++) {
            query.setParameter(perchs[i], valuesMap.get(perchs[i]));
        }
        return query.list().size();
    }

    public List<Map> getPageStudentsOperateAbsence(String whereString, Map<String, Object> valuesMap, int pageNum, int pageSize, String schoolnum) {
        String hql = "select new map(student.id as id, student.exInstitution.name as exInstitution, student.exLanguage.name as exLanguage, student.exProfession.name as exProfession," +
                " student.exCollege.name as exCollege," +
                " student.exCampus.name as exCampus, student.name as name, student.password as password," +
                " student.sex as sex, student.studentnum as studentnum, student.idnum as idnum, student.ethno as ethno," +
                " student.examnum as examnum, student.exambatch as exambatch, student.grade as grade, student.classnum as classnum," +
                " student.lengthofyear as lengthofyear, student.studentcategory as studentcategory, student.paied as paied," +
                " student.score as score, student.theoryabsent as theoryabsent, student.operateabsent as operateabsent," +
                " student.theoryfraud as theoryfraud, student.operatefraud as operatefraud) " +
                "from cn.hdu.examsignup.model.ExStudent student";
        hql += whereString;
        hql += " and student.exInstitution.institutionnum =:schoolnum " +
                "and student.paied='1' and student.operateabsent='1'" +
                "and  student.exLanguage is not null order by student.examnum,student.exLanguage.languagenum,student.exCampus.campusnum,student.studentcategory,student.studentnum ";
        Query query = this.getCurrentSession().createQuery(hql).setFirstResult(pageNum).setMaxResults(pageSize).setParameter("schoolnum", schoolnum);
        String[] perchs = valuesMap.keySet().toArray(new String[0]);
        for (int i = 0; i < perchs.length; i++) {
            query.setParameter(perchs[i], valuesMap.get(perchs[i]));
        }
        return query.list();
    }


    public long getStudentsNoOperateAbsenceTotalCount(String whereString, Map<String, Object> valuesMap, String schoolnum) {
        String hql = "select student.id from cn.hdu.examsignup.model.ExStudent student ";
        hql += whereString;
        hql += " and student.exInstitution.institutionnum =:schoolnum and student.exLanguage is not null" +
                " and student.paied='1' and student.operateabsent='0'";
        Query query = this.getCurrentSession().createQuery(hql).setParameter("schoolnum", schoolnum);
        String[] perchs = valuesMap.keySet().toArray(new String[0]);
        for (int i = 0; i < perchs.length; i++) {
            query.setParameter(perchs[i], valuesMap.get(perchs[i]));
        }
        return query.list().size();
    }

    public List<Map> getPageStudentsNoOperateAbsence(String whereString, Map<String, Object> valuesMap, int pageNum, int pageSize, String schoolnum) {
        String hql = "select new map(student.id as id, student.exInstitution.name as exInstitution, student.exLanguage.name as exLanguage, student.exProfession.name as exProfession," +
                " student.exCollege.name as exCollege," +
                " student.exCampus.name as exCampus, student.name as name, student.password as password," +
                " student.sex as sex, student.studentnum as studentnum, student.idnum as idnum, student.ethno as ethno," +
                " student.examnum as examnum, student.exambatch as exambatch, student.grade as grade, student.classnum as classnum," +
                " student.lengthofyear as lengthofyear, student.studentcategory as studentcategory, student.paied as paied," +
                " student.score as score, student.theoryabsent as theoryabsent, student.operateabsent as operateabsent," +
                " student.theoryfraud as theoryfraud, student.operatefraud as operatefraud) " +
                "from cn.hdu.examsignup.model.ExStudent student";
        hql += whereString;
        hql += " and student.exInstitution.institutionnum =:schoolnum " +
                "and student.paied='1' and student.operateabsent='0'" +
                "and  student.exLanguage is not null order by student.examnum,student.exLanguage.languagenum,student.exCampus.campusnum,student.studentcategory,student.studentnum ";
        Query query = this.getCurrentSession().createQuery(hql).setFirstResult(pageNum).setMaxResults(pageSize).setParameter("schoolnum", schoolnum);
        String[] perchs = valuesMap.keySet().toArray(new String[0]);
        for (int i = 0; i < perchs.length; i++) {
            query.setParameter(perchs[i], valuesMap.get(perchs[i]));
        }
        return query.list();
    }


    public boolean setOperateAbsence(String examnum) {
        ExStudent exStudent;
        String hql = "select t from cn.hdu.examsignup.model.ExStudent t where t.examnum =:examnum";
        Query query = this.getCurrentSession().createQuery(hql).setParameter("examnum", examnum);
        System.out.println("999999999999999999999999999999999999999999999999999999999999");
        System.out.println(query);
        //	exStudent = (ExStudent)query.uniqueResult();
        exStudent = (ExStudent) query.list().get(0);
        exStudent.setOperateabsent("1");
        System.out.println(exStudent);
        this.update(exStudent);
        //		this.save(exStudent);
        //		query.executeUpdate();
        return true;
    }


    public boolean setOperateNoAbsence(String examnum) {
        ExStudent exStudent;
        String hql = "select t from cn.hdu.examsignup.model.ExStudent t where t.examnum =:examnum";
        Query query = this.getCurrentSession().createQuery(hql).setParameter("examnum", examnum);
        System.out.println("999999999999999999999999999999999999999999999999999999999999");
        System.out.println(query);
        //	exStudent = (ExStudent)query.uniqueResult();
        exStudent = (ExStudent) query.list().get(0);
        exStudent.setOperateabsent("0");
        System.out.println(exStudent);
        this.update(exStudent);
        //		this.save(exStudent);
        //		query.executeUpdate();
        return true;
    }

    public List<Map> getStudentOperateAbsenceExcel() {
        String hql = "select new map(student.id as id, student.name as name," +
                " student.examnum as examnum, student.operateabsent as operateabsent)" +
                "from cn.hdu.examsignup.model.ExStudent student" +
                " where  student.operateabsent='1'" +
                " order by student.examnum";
        Query query = this.getCurrentSession().createQuery(hql);
        List<Map> result = query.list();
        return result;
    }
    
    
  //用于显示在考试院,所有高校的缺考信息
    public List<Map> getPageAbsentStudentsAllSchools(int pageNum, int pageSize) {
        String hql = "select new map(student.examnum as examnum,student.name as name,student.theoryabsent as theoryabsent,student.operateabsent as operateabsent) from cn.hdu.examsignup.model.ExStudent student where  student.theoryabsent like '1' or student.operateabsent like '1'";
        Query query = this.getCurrentSession().createQuery(hql).setFirstResult(pageNum).setMaxResults(pageSize);
        return (List<Map>) query.list();
    }
    
  //用于显示在考试院,所有高校的缺考信息
    public long getAbsentStudentsTotalCountAllSchools() {
        String hql = "select student.id from cn.hdu.examsignup.model.ExStudent student where  student.theoryabsent like '1' or student.operateabsent like '1'";
        return this.getCurrentSession().createQuery(hql).list().size();
    }
    
    
  //用于显示在考试院,所有高校的作弊信息
    public List<Map> getPageFraudStudentsAllSchools(int pageNum, int pageSize) {
        String hql = "select new map(student.examnum as examnum,student.name as name,student.theoryfraud as theoryfraud,student.operatefraud as operatefraud) " +
                "from cn.hdu.examsignup.model.ExStudent student " +
                "where student.theoryfraud like '1' or student.operatefraud like '1'";
        Query query = this.getCurrentSession().createQuery(hql).setFirstResult(pageNum).setMaxResults(pageSize);
        return (List<Map>) query.list();
    }
    
  //用于显示在考试院,所有高校的作弊信息
    public long getFraudStudentsTotalCountAllSchools() {
        String hql = "select student.id from cn.hdu.examsignup.model.ExStudent student where student.theoryfraud like '1' or student.operatefraud like '1'";
        return this.getCurrentSession().createQuery(hql).list().size();
    }

  


}