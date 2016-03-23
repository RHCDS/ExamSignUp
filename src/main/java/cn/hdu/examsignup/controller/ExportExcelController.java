package cn.hdu.examsignup.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;


import org.directwebremoting.WebContextFactory;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFWriter;

import cn.hdu.examsignup.service.InstitutionService;
import cn.hdu.examsignup.service.LanguageService;
import cn.hdu.examsignup.service.OperateExamArrangeService;
import cn.hdu.examsignup.service.OperateSupervisorArrangeService;
import cn.hdu.examsignup.service.StudentService;
import cn.hdu.examsignup.service.PhysiceexamroomService;
import cn.hdu.examsignup.service.SupervisorService;
import cn.hdu.examsignup.service.SectionService;
import cn.hdu.examsignup.service.TheoryExamArrangeService;
import cn.hdu.examsignup.service.TheorySupervisorArrangeService;


import org.directwebremoting.json.types.JsonArray;
import org.directwebremoting.json.types.JsonObject;
import org.json.simple.JSONArray;


@Controller
public class ExportExcelController {
	@Autowired
	private OperateSupervisorArrangeService operateSupervisorArrangeService;
	@Autowired
	private TheorySupervisorArrangeService theorySupervisorArrangeService;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private SupervisorService supervisorService;
	@Autowired
	private StudentService studentservice;
	@Autowired
	private OperateExamArrangeService operateExamArrangeService;
	@Autowired
	private InstitutionService institutionService;
	@Autowired
	private TheoryExamArrangeService theoryExamArrangeService;
	@Autowired
	private LanguageService languageService;
	@Autowired
	private PhysiceexamroomService physiceexamroomService;
	
	
	@RequestMapping(value = "exportAllSchoolsAbsentExcel", method = RequestMethod.POST)
	public ModelAndView exportAllSchoolsAbsentExcel(HttpServletRequest request,HttpServletResponse response) throws Exception{
		 response.reset(); 
        OutputStream os = response.getOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(os);
		String filename = "qkkAllSchools";
        try {
                   
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");        //改成输出excel文件
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type","application/force-download");
            response.setHeader("Content-disposition","attachment;filename="+filename+".xls");
            WritableSheet sheet  = wwb.createSheet("缺考库", 0);
            sheet.setColumnView(0, 25);
            sheet.setColumnView(1, 25);
            WritableFont blodFont = new WritableFont(WritableFont.TAHOMA,10,WritableFont.BOLD, false);
            WritableCellFormat blodFormat = new WritableCellFormat (blodFont);
            Label label = null;
        		label = new Label(0,0,"准考证号");
        		sheet.addCell(label);
        		label = new Label(1,0,"姓名");
        		sheet.addCell(label);
        		label = new Label(2,0,"理论缺考");
        		sheet.addCell(label);
        		label = new Label(3,0,"上机缺考");
        		sheet.addCell(label);
                List<Map> absentstudents = studentservice.getAllSchoolsAbsent();
                for(int i = 0; i < absentstudents.size(); i++){
                	label = new Label(0,i+1,absentstudents.get(i).get("examnum").toString());
                	sheet.addCell(label);
                	label = new Label(1,i+1,absentstudents.get(i).get("name").toString());
                	sheet.addCell(label);
                	if(absentstudents.get(i).get("theoryabsent").toString().equals("1")){
                		label = new Label(2,i+1,"是");
                	}else{
                		label = new Label(2,i+1,"否");
                	}
                	sheet.addCell(label);
                	if(absentstudents.get(i).get("operateabsent").toString().equals("1")){
                		label = new Label(3,i+1,"是");
                	}else{
                		label = new Label(3,i+1,"否");
                	}
                	sheet.addCell(label);
                }

        } catch(Exception ex) {
                ex.printStackTrace();
                System.out.println("写入Excel文件发生错误！！！");
        }finally{
            wwb.write();
            wwb.close();
            os.flush();
            os.close();
        }
        
        return null;
	} 
	
	
	@RequestMapping(value = "exportAbsentStudentsBySchoolExcel", method = RequestMethod.POST)
	public ModelAndView exportAbsentStudentsBySchoolExcel(HttpServletRequest request,HttpServletResponse response) throws Exception{
		 response.reset(); 
        OutputStream os = response.getOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(os);
		String filename = "qkk" + request.getSession().getAttribute("institution").toString();
        try {
                   
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");        //改成输出excel文件
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type","application/force-download");
            response.setHeader("Content-disposition","attachment;filename="+filename+".xls");
            WritableSheet sheet  = wwb.createSheet("缺考库", 0);
            sheet.setColumnView(0, 25);
            sheet.setColumnView(1, 25);
            WritableFont blodFont = new WritableFont(WritableFont.TAHOMA,10,WritableFont.BOLD, false);
            WritableCellFormat blodFormat = new WritableCellFormat (blodFont);
            Label label = null;
        		label = new Label(0,0,"准考证号");
        		sheet.addCell(label);
        		label = new Label(1,0,"姓名");
        		sheet.addCell(label);
        		label = new Label(2,0,"理论缺考");
        		sheet.addCell(label);
        		label = new Label(3,0,"上机缺考");
        		sheet.addCell(label);
                List<Map> absentstudents = studentservice.getAbsentStudentsBySchool(request.getSession().getAttribute("institution").toString());
                for(int i = 0; i < absentstudents.size(); i++){
                	label = new Label(0,i+1,absentstudents.get(i).get("examnum").toString());
                	sheet.addCell(label);
                	label = new Label(1,i+1,absentstudents.get(i).get("name").toString());
                	sheet.addCell(label);
                	if(absentstudents.get(i).get("theoryabsent").toString().equals("1")){
                		label = new Label(2,i+1,"是");
                	}else{
                		label = new Label(2,i+1,"否");
                	}
                	sheet.addCell(label);
                	if(absentstudents.get(i).get("operateabsent").toString().equals("1")){
                		label = new Label(3,i+1,"是");
                	}else{
                		label = new Label(3,i+1,"否");
                	}
                	sheet.addCell(label);
                }

        } catch(Exception ex) {
                ex.printStackTrace();
                System.out.println("写入Excel文件发生错误！！！");
        }finally{
            wwb.write();
            wwb.close();
            os.flush();
            os.close();
        }
        
        return null;
	} 
	
	private void finalfunction(WritableWorkbook wwb,OutputStream os) throws IOException, WriteException{

		wwb.write();
		wwb.close();
		os.flush();
		os.close();
	}
	@RequestMapping(value = "exportExaminationExcel",method = RequestMethod.POST)
	public ModelAndView exportExamination(HttpServletRequest request,HttpServletResponse response) throws Exception {
		response.reset();
		OutputStream os = response.getOutputStream();
		WritableWorkbook wwb = Workbook.createWorkbook(os);
		String institutionnum = request.getSession().getAttribute("institution").toString();
		String filename = "ExamninationData";
		try {
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");        //改成输出excel文件
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type","application/force-download");
            response.setHeader("Content-disposition","attachment;filename="+filename+".xls");
			WritableSheet classroomInfo = wwb.createSheet("教室信息",0);
			WritableSheet supervisorInfo = wwb.createSheet("监考人员信息", 1);
			WritableSheet sectionInfo = wwb.createSheet("考试场次", 2);
			WritableSheet theoryExamInfo = wwb.createSheet("理论考场编排", 3);
			WritableSheet operateExamInfo = wwb.createSheet("上机考场编排", 4);
			WritableSheet theorySupervisInfo = wwb.createSheet("理论监考", 5);
			WritableSheet operateSupervisInfo = wwb.createSheet("上机监考", 6);
            //教室信息表
			classroomInfo.setColumnView(0, 13);
			Label label = null;
			label = new Label(0,0,"教室位置");
			classroomInfo.addCell(label);
			label = new Label(1,0,"所在校区");
			classroomInfo.addCell(label);
			label = new Label(2,0,"教室容量（人）");
			classroomInfo.addCell(label);
			label = new Label(3,0,"理论教室");
			classroomInfo.addCell(label);
			label = new Label(4,0,"上机教室");
			classroomInfo.addCell(label);
			List<Map> examrooms = physiceexamroomService.getAllExamrooms(institutionnum);
			if(null==examrooms) {
				finalfunction(wwb,os);
				return null;
			}
			int roomsNum = examrooms.size();
			for(int i=0;i<roomsNum;++i) {
				classroomInfo.addCell(new Label(0,i+1,examrooms.get(i).get("roomlocation").toString()));
				classroomInfo.addCell(new Label(1,i+1,examrooms.get(i).get("exCampus").toString()));
				classroomInfo.addCell(new Label(2,i+1,examrooms.get(i).get("capacity").toString()));
				classroomInfo.addCell(new Label(3,i+1,examrooms.get(i).get("theoryflag").toString()));
				classroomInfo.addCell(new Label(4,i+1,examrooms.get(i).get("operateflag").toString()));
			}
			
			//监考人员信息
			supervisorInfo.addCell(new Label(0,0,"监考工号"));
			supervisorInfo.addCell(new Label(1,0,"姓名"));
			supervisorInfo.addCell(new Label(2,0,"所属院系/机构"));
			supervisorInfo.setColumnView(2, 15);
			supervisorInfo.addCell(new Label(3,0,"主监考"));
			supervisorInfo.addCell(new Label(4,0,"理论监考"));
			supervisorInfo.addCell(new Label(5,0,"上机监考"));
			supervisorInfo.addCell(new Label(6,0,"联系方式"));
			List<Map> allSuperviors = supervisorService.getAllSupervisors(institutionnum);
			if(null==allSuperviors){
				finalfunction(wwb,os);
				return null;
			}
			int supervisorsNum = allSuperviors.size();
			for(int i = 0;i<supervisorsNum;++i) {
				supervisorInfo.addCell(new Label(0,i+1,allSuperviors.get(i).get("supervisornum").toString()));
				supervisorInfo.addCell(new Label(1,i+1,allSuperviors.get(i).get("name").toString()));
				supervisorInfo.addCell(new Label(2,i+1,allSuperviors.get(i).get("exCollege").toString()));
				supervisorInfo.addCell(new Label(3,i+1,allSuperviors.get(i).get("primaryflag").toString()));
				supervisorInfo.addCell(new Label(4,i+1,allSuperviors.get(i).get("theoryflag").toString()));
				supervisorInfo.addCell(new Label(5,i+1,allSuperviors.get(i).get("operateflag").toString()));
				supervisorInfo.addCell(new Label(6,i+1,allSuperviors.get(i).get("contact").toString()));
			}
			//考试场次信息
			sectionInfo.setColumnView(1, 12);
			sectionInfo.setColumnView(3, 12);
			sectionInfo.setColumnView(4, 12);
			sectionInfo.addCell(new Label(0,0,"场次序号"));
			sectionInfo.addCell(new Label(1,0,"开始日期"));
			sectionInfo.addCell(new Label(2,0,"开始时间"));
			sectionInfo.addCell(new Label(3,0,"是否理论考试"));
			sectionInfo.addCell(new Label(4,0,"是否上级考试"));
			List<Map> allSections = sectionService.getAllSections(institutionnum);
			if(null==allSections) {
				finalfunction(wwb,os);
				return null;
			}
			int sectionsNum = allSections.size();
			for(int i = 0;i < sectionsNum;++i) {
				sectionInfo.addCell((new Label(0,i+1,allSections.get(i).get("sectionnum").toString())));
				sectionInfo.addCell((new Label(1,i+1,allSections.get(i).get("dateValue").toString())));
				sectionInfo.addCell((new Label(2,i+1,allSections.get(i).get("timeValue").toString())));
				sectionInfo.addCell((new Label(3,i+1,allSections.get(i).get("theoryflag").toString())));
				sectionInfo.addCell((new Label(4,i+1,allSections.get(i).get("operateflag").toString())));
			}
			//理论考场编排
			theoryExamInfo.setColumnView(0, 17);
			theoryExamInfo.setColumnView(1, 31);
			theoryExamInfo.setColumnView(2, 13);
			theoryExamInfo.addCell(new Label(0,0,"语种"));
			theoryExamInfo.addCell(new Label(1,0,"场次"));
			theoryExamInfo.addCell(new Label(2,0,"教室位置"));
			theoryExamInfo.addCell(new Label(3,0,"包含理论考场"));
			theoryExamInfo.addCell(new Label(4,0,"总量"));
			theoryExamInfo.addCell(new Label(5,0,"余量"));
			theoryExamInfo.addCell(new Label(6,0,"所在校区"));
			List<Map> languages = theoryExamArrangeService.getLoadSignedLanguage(institutionnum);
			int currentRow = 0;
			for(Map language:languages) {
				List<Map> arrangeInfo = theoryExamArrangeService.
						loadArrangeInfo(institutionnum, language.get("languagenum").toString());
				if(null == arrangeInfo) {
					continue;
				}
				int arrangeLength = arrangeInfo.size();
				for(int i =0;i < arrangeLength;i++) {
					theoryExamInfo.addCell(new Label(0,currentRow + 1,language.get("languagename").toString()));
					theoryExamInfo.addCell(new Label(1,currentRow + 1,arrangeInfo.get(i).get("sectioninfo").toString()));
					theoryExamInfo.addCell(new Label(2,currentRow + 1,arrangeInfo.get(i).get("roomlocation").toString()));
					theoryExamInfo.addCell(new Label(3,currentRow + 1,arrangeInfo.get(i).get("examrooms").toString()));
					theoryExamInfo.addCell(new Label(4,currentRow + 1,arrangeInfo.get(i).get("capacity").toString()));
					theoryExamInfo.addCell(new Label(5,currentRow + 1,arrangeInfo.get(i).get("surplus").toString()));
					theoryExamInfo.addCell(new Label(6,currentRow + 1,arrangeInfo.get(i).get("campusname").toString()));
					currentRow ++;
				}
			}
			//上机考场编排信息
			operateExamInfo.setColumnView(0, 17);
			operateExamInfo.setColumnView(1, 31);
			operateExamInfo.addCell(new Label(0,0,"语种"));
			operateExamInfo.addCell(new Label(1,0,"场次"));
			operateExamInfo.addCell(new Label(2,0,"教室位置"));
			operateExamInfo.addCell(new Label(3,0,"总量"));
			operateExamInfo.addCell(new Label(4,0,"余量"));
			operateExamInfo.addCell(new Label(5,0,"所在校区"));
			List<Map> operateLanguages = operateExamArrangeService.getLoadSignedLanguage(institutionnum);
			currentRow = 0;
			for(Map language:operateLanguages) {
				List<Map> arrangeInfo = operateExamArrangeService.
						loadArrangeInfo(institutionnum, language.get("languagenum").toString());
				if(null == arrangeInfo) {
					continue;
				}
				int arrageLength = arrangeInfo.size();
				for(int i = 0;i < arrageLength;i++) {
					operateExamInfo.addCell(new Label(0,currentRow + 1,language.get("languagename").toString()));
					operateExamInfo.addCell(new Label(1,currentRow + 1,arrangeInfo.get(i).get("sectioninfo").toString()));
					operateExamInfo.addCell(new Label(2,currentRow + 1,arrangeInfo.get(i).get("roomlocation").toString()));
					operateExamInfo.addCell(new Label(3,currentRow + 1,arrangeInfo.get(i).get("capacity").toString()));
					operateExamInfo.addCell(new Label(4,currentRow + 1,arrangeInfo.get(i).get("surplus").toString()));
					operateExamInfo.addCell(new Label(5,currentRow + 1,arrangeInfo.get(i).get("campusname").toString()));
					currentRow ++;
				}
			}
			//理论监考老师编排
			theorySupervisInfo.setColumnView(0, 31);
			theorySupervisInfo.setColumnView(2, 17);
			theorySupervisInfo.addCell(new Label(0,0,"场次"));
			theorySupervisInfo.addCell(new Label(1,0,"语种"));
			theorySupervisInfo.addCell(new Label(2,0,"理论考场"));
			theorySupervisInfo.addCell(new Label(3,0,"教室位置"));
			theorySupervisInfo.addCell(new Label(4,0,"监考老师"));
			theorySupervisInfo.addCell(new Label(5,0,"学生人数"));
			theorySupervisInfo.addCell(new Label(6,0,"校区"));
			List<Map> supervisorsArranges = theorySupervisorArrangeService.loadArrangeInfo(institutionnum);
			if(null == supervisorsArranges) {
				finalfunction(wwb,os);
				return null;
			}
			int supervisorsArrangeLength = supervisorsArranges.size();
			for(int i = 0;i < supervisorsArrangeLength;i++) {
			/*	theorySupervisInfo.addCell(new Label(0,i+1,supervisorsArranges.get(i).get("sectioninfo").toString()));
				theorySupervisInfo.addCell(new Label(1,i+1,supervisorsArranges.get(i).get("language").toString()));
				theorySupervisInfo.addCell(new Label(2,i+1,supervisorsArranges.get(i).get("examroomnum").toString()));
				theorySupervisInfo.addCell(new Label(3,i+1,supervisorsArranges.get(i).get("roomlocation").toString()));
				theorySupervisInfo.addCell(new Label(4,i+1,supervisorsArranges.get(i).get("supervisor").toString()));
				theorySupervisInfo.addCell(new Label(5,i+1,supervisorsArranges.get(i).get("capacity").toString()));
				theorySupervisInfo.addCell(new Label(6,i+1,supervisorsArranges.get(i).get("campusname").toString())); */
				
				theorySupervisInfo.addCell(new Label(0,i+1,supervisorsArranges.get(i).get("sectioninfo")!=null?
						supervisorsArranges.get(i).get("sectioninfo").toString():""));
				theorySupervisInfo.addCell(new Label(1,i+1,supervisorsArranges.get(i).get("language")!=null?
						supervisorsArranges.get(i).get("language").toString():""));
				theorySupervisInfo.addCell(new Label(2,i+1,supervisorsArranges.get(i).get("examroomnum")!=null?
						supervisorsArranges.get(i).get("examroomnum").toString():""));
				theorySupervisInfo.addCell(new Label(3,i+1,supervisorsArranges.get(i).get("roomlocation")!=null?
						supervisorsArranges.get(i).get("roomlocation").toString():""));
				theorySupervisInfo.addCell(new Label(4,i+1,supervisorsArranges.get(i).get("supervisor")!=null?
						supervisorsArranges.get(i).get("supervisor").toString():""));
				theorySupervisInfo.addCell(new Label(5,i+1,supervisorsArranges.get(i).get("capacity")!=null?
						supervisorsArranges.get(i).get("capacity").toString():""));
				theorySupervisInfo.addCell(new Label(6,i+1,supervisorsArranges.get(i).get("campusname")!=null?
						supervisorsArranges.get(i).get("campusname").toString():""));
			}
			//上机监考老师编排
			operateSupervisInfo.setColumnView(0,31);
			operateExamInfo.setColumnView(2,17);
			operateSupervisInfo.addCell(new Label(0,0,"场次"));
			operateSupervisInfo.addCell(new Label(1,0,"语种"));
			operateSupervisInfo.addCell(new Label(2,0,"教室位置"));
			operateSupervisInfo.addCell(new Label(3,0,"监考老师"));
			operateSupervisInfo.addCell(new Label(4,0,"学生人数"));
			operateSupervisInfo.addCell(new Label(5,0,"校区"));
			List<Map> oSupervisorsArranges = operateSupervisorArrangeService.loadArrangeInfo(institutionnum);
			if(null == oSupervisorsArranges) {
				finalfunction(wwb,os);
				return null;
			}
			int oSupervisorsArrangeLength = oSupervisorsArranges.size();
			for(int i = 0; i < oSupervisorsArrangeLength;i++) {
				operateSupervisInfo.addCell(new Label(0,i+1,oSupervisorsArranges.get(i).get("sectioninfo").toString()));
				operateSupervisInfo.addCell(new Label(1,i+1,oSupervisorsArranges.get(i).get("language").toString()));
				operateSupervisInfo.addCell(new Label(2,i+1,oSupervisorsArranges.get(i).get("roomlocation").toString()));
				operateSupervisInfo.addCell(new Label(3,i+1,oSupervisorsArranges.get(i).get("supervisor").toString()));
				operateSupervisInfo.addCell(new Label(4,i+1,oSupervisorsArranges.get(i).get("capacity").toString()));
				operateSupervisInfo.addCell(new Label(5,i+1,oSupervisorsArranges.get(i).get("campusname").toString()));
			}
		} catch(Exception ex) {
            ex.printStackTrace();
            System.out.println("写入Excel文件发生错误！！！");
           
		} finally {
			finalfunction(wwb,os);
		}
		return null;
	}
	
	@RequestMapping(value = "exportFraudStudentsBySchoolExcel", method = RequestMethod.POST)
	public ModelAndView exportFraudStudentsBySchoolExcel(HttpServletRequest request,HttpServletResponse response) throws Exception{

        response.reset(); 
        OutputStream os = response.getOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(os);  
		String institutionnum = request.getSession().getAttribute("institution").toString();
		String filename = "zbk" + request.getSession().getAttribute("institution").toString();
        try {       
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");        //改成输出excel文件
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type","application/force-download");
            response.setHeader("Content-disposition","attachment;filename="+filename+".xls");
            WritableSheet sheet  = wwb.createSheet("作弊库", 0);
            sheet.setColumnView(0, 25);
            sheet.setColumnView(1, 25);
            WritableFont blodFont = new WritableFont(WritableFont.TAHOMA,10,WritableFont.BOLD, false);
            WritableCellFormat blodFormat = new WritableCellFormat (blodFont);
            Label label = null;
        		label = new Label(0,0,"准考证号");
        		sheet.addCell(label);
        		label = new Label(1,0,"姓名");
        		sheet.addCell(label);
        		label = new Label(2,0,"理论作弊");
        		sheet.addCell(label);
        		label = new Label(3,0,"上机作弊");
        		sheet.addCell(label);
                List<Map> fraudstudents = studentservice.getFraudStudentsBySchool(institutionnum);
                for(int i = 0; i < fraudstudents.size(); i++){
                	label = new Label(0,i+1,fraudstudents.get(i).get("examnum").toString());
                	sheet.addCell(label);
                	label = new Label(1,i+1,fraudstudents.get(i).get("name").toString());
                	sheet.addCell(label);
                	if(fraudstudents.get(i).get("theoryfraud").toString().equals("1")){
                		label = new Label(2,i+1,"是");
                	}else{
                		label = new Label(2,i+1,"否");
                	}
                	sheet.addCell(label);
                	if(fraudstudents.get(i).get("operatefraud").toString().equals("1")){
                		label = new Label(3,i+1,"是");
                	}else{
                		label = new Label(3,i+1,"否");
                	}
                	sheet.addCell(label);
                }

        } catch(Exception ex) {
                ex.printStackTrace();
                System.out.println("写入Excel文件发生错误！！！");
               
        }finally{
            wwb.write();
            wwb.close();
            os.flush();
            os.close();
        }
        return null;
	} 
	
	
	@RequestMapping(value = "exportAllSchoolsFraudExcel", method = RequestMethod.POST)
	public ModelAndView exportAllSchoolsFraudExcel(HttpServletRequest request,HttpServletResponse response) throws Exception{

        response.reset(); 
        OutputStream os = response.getOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(os);  
	//	String institutionnum = request.getSession().getAttribute("institution").toString();
		String filename = "zbkAllSchool";
        try {       
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");        //改成输出excel文件
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type","application/force-download");
            response.setHeader("Content-disposition","attachment;filename="+filename+".xls");
            WritableSheet sheet  = wwb.createSheet("作弊库", 0);
            sheet.setColumnView(0, 25);
            sheet.setColumnView(1, 25);
            WritableFont blodFont = new WritableFont(WritableFont.TAHOMA,10,WritableFont.BOLD, false);
            WritableCellFormat blodFormat = new WritableCellFormat (blodFont);
            Label label = null;
        		label = new Label(0,0,"准考证号");
        		sheet.addCell(label);
        		label = new Label(1,0,"姓名");
        		sheet.addCell(label);
        		label = new Label(2,0,"理论作弊");
        		sheet.addCell(label);
        		label = new Label(3,0,"上机作弊");
        		sheet.addCell(label);
                List<Map> fraudstudents = studentservice.getAllSchoolsFraud();
                for(int i = 0; i < fraudstudents.size(); i++){
                	label = new Label(0,i+1,fraudstudents.get(i).get("examnum").toString());
                	sheet.addCell(label);
                	label = new Label(1,i+1,fraudstudents.get(i).get("name").toString());
                	sheet.addCell(label);
                	if(fraudstudents.get(i).get("theoryfraud").toString().equals("1")){
                		label = new Label(2,i+1,"是");
                	}else{
                		label = new Label(2,i+1,"否");
                	}
                	sheet.addCell(label);
                	if(fraudstudents.get(i).get("operatefraud").toString().equals("1")){
                		label = new Label(3,i+1,"是");
                	}else{
                		label = new Label(3,i+1,"否");
                	}
                	sheet.addCell(label);
                }

        } catch(Exception ex) {
                ex.printStackTrace();
                System.out.println("写入Excel文件发生错误！！！");
               
        }finally{
            wwb.write();
            wwb.close();
            os.flush();
            os.close();
        }
        return null;
	} 
	
	
	@RequestMapping(value = "exportCheckScoreExcel", method = RequestMethod.POST)
	public ModelAndView exportCheckScoreExcel(HttpServletRequest request,HttpServletResponse response) throws Exception{
		response.reset(); 
        OutputStream os = response.getOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(os); 
		String institutionnum = request.getSession().getAttribute("institution").toString();
		String filename = "checkScroe" + request.getSession().getAttribute("institution").toString();
        try {
                    
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");        //改成输出excel文件
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type","application/force-download");
            response.setHeader("Content-disposition","attachment;filename="+filename+".xls");
            WritableSheet sheet  = wwb.createSheet("查分学生表", 0);
            sheet.setColumnView(0, 25);
            sheet.setColumnView(1, 25);
            WritableFont blodFont = new WritableFont(WritableFont.TAHOMA,10,WritableFont.BOLD, false);
            WritableCellFormat blodFormat = new WritableCellFormat (blodFont);
            Label label = null;
        	label = new Label(0,0,"准考证号");
        	sheet.addCell(label);
        	label = new Label(1,0,"姓名");
        	sheet.addCell(label);
        	label = new Label(2,0,"成绩");
        	sheet.addCell(label);
            List<Map> checkScorestudents = studentservice.getAllCheckScoreStudents(institutionnum);
            for(int i = 0; i < checkScorestudents.size(); i++){
            	label = new Label(0,i+1,checkScorestudents.get(i).get("examnum").toString());
            	sheet.addCell(label);
            	label = new Label(1,i+1,checkScorestudents.get(i).get("name").toString());
            	sheet.addCell(label);
            	label = new Label(2,i+1,checkScorestudents.get(i).get("score").toString());
            	sheet.addCell(label);
            }

        } catch(Exception ex) {
                ex.printStackTrace();
                System.out.println("写入Excel文件发生错误！！！");
               
        }finally{
            wwb.write();
            wwb.close();
            os.flush();
            os.close();
        }
        return null;
	} 
	private void deleteDbfFile(File file) throws Exception {
		if(file.exists()){ 
			if(file.isFile()){ 
				file.delete(); 
			}else if(file.isDirectory()){ 
				File files[] = file.listFiles(); 
				for(int i=0;i<files.length;i++){ 
					this.deleteDbfFile(files[i]); 
				} 
			} 
			file.delete(); 
		}else{ 
			System.out.println("所删除的文件不存在！"+'\n'); 
		} 
	}
	private void saveOneSchooleDbf(String realPath,String schoolnum) throws Exception{
		OutputStream fos = null;
		DateFormat date = new SimpleDateFormat( "yyyy-MM-dd"); 
		String outFilePath = realPath;
		try {
			File outFile = new File(outFilePath);
			if(!(outFile.exists())){
				outFile.mkdirs();
			}
		} catch(Exception e) {
			System.out.println("新建目录操作出错");  
		      e.printStackTrace();  
		}
		//outFilePath += "/" + schoolnum + "(" +  date.format(new Date()).toString() + ").dbf";
		outFilePath += "/Ks" + schoolnum +  ".dbf";
		try {
			//DBFField[] fields = new DBFField[4]; 
			DBFField[] fields = new DBFField[3]; 
			fields[0] = new DBFField();
			fields[0].setName("Zkzh");
			fields[0].setDataType( DBFField.FIELD_TYPE_C);
			fields[0].setFieldLength(20);   
			/*
			fields[3] = new DBFField();
			fields[3].setName("SFZH");
			fields[3].setDataType( DBFField.FIELD_TYPE_C);
			fields[3].setFieldLength(18);  
			*/
			fields[1] = new DBFField();
			fields[1].setName("Xm");
			fields[1].setDataType( DBFField.FIELD_TYPE_C);
			fields[1].setFieldLength(50);   
			
			fields[2] = new DBFField();
			fields[2].setName("Xb");
			fields[2].setDataType( DBFField.FIELD_TYPE_C);
			fields[2].setFieldLength(5);   
			
			DBFWriter  writer = new DBFWriter();
			writer.setCharactersetName("GBK");
			writer.setFields(fields);
			
			// 准考证号，姓名，性别
			List<Map> students = studentservice.getAllStudents(schoolnum);
			for(Map student:students) {
				Object[] rowData = new Object[3];
				rowData[0] = student.get("examnum");
			//	rowData[3] = student.get("idnum");
			//	rowData[2] = student.get("name");
				rowData[1] = student.get("name");
			//	for(int i = 0;i<rowData[2].toString().trim().length();i++) {
				for(int i = 0;i<rowData[1].toString().trim().length();i++) {
			//		String temp = rowData[2].toString();
					String temp = rowData[1].toString();
					temp += " ";
					//rowData[2] = temp;
					rowData[1] = temp;
				}
				String sex = student.get("sex").toString();
				if(sex.equals("F")) {
					sex = "女 ";
				} else {
					sex = "男 ";
				}
				//rowData[3] = sex;
				rowData[2] = sex;
				writer.addRecord(rowData);
			}

			fos = new FileOutputStream(outFilePath);
			writer.write(fos);
			
		} catch (Exception e) {
			  e.printStackTrace();
              System.out.println("写入DBF文件发生错误！！！");
		} finally {
	         fos.flush();
	         fos.close();
		}
	}
	private void saveOneSchooleCJDBF(String realPath,String school) throws Exception{
		OutputStream fos = null;
		DateFormat date = new SimpleDateFormat( "yyyy-MM-dd"); 
		String outFilePath = realPath;
		try {
			File outFile = new File(outFilePath);
			if(!(outFile.exists())){
				outFile.mkdirs();
			}
		} catch(Exception e) {
			System.out.println("新建目录操作出错");  
		      e.printStackTrace();  
		}

		outFilePath += "/" + school + "CJ(" +  date.format(new Date()).toString() + ").dbf";
		try {
			DBFField[] fields = new DBFField[6]; 
			
			fields[0] = new DBFField();
			fields[0].setName("ZKZH");
			fields[0].setDataType( DBFField.FIELD_TYPE_C);
			fields[0].setFieldLength(20);   
			
			fields[1] = new DBFField();
			fields[1].setName("SFZH");
			fields[1].setDataType( DBFField.FIELD_TYPE_C);
			fields[1].setFieldLength(18);  
			
			fields[2] = new DBFField();
			fields[2].setName("XM");
			fields[2].setDataType( DBFField.FIELD_TYPE_C);
			fields[2].setFieldLength(100);  
			
			fields[3] = new DBFField();
			fields[3].setName("XB");
			fields[3].setDataType( DBFField.FIELD_TYPE_C);
			fields[3].setFieldLength(5);   
			
			fields[4] = new DBFField();
			fields[4].setName("FS");
			fields[4].setDataType( DBFField.FIELD_TYPE_F);
			fields[4].setFieldLength(4);   
			
			fields[5] = new DBFField();
			fields[5].setName("DJ");
			fields[5].setDataType( DBFField.FIELD_TYPE_C);
			fields[5].setFieldLength(2);   
			
			DBFWriter  writer = new DBFWriter();
			writer.setCharactersetName("GBK");
			writer.setFields(fields);
			
			List<Map> students = studentservice.getAllStudentsCJ(school);
			for(Map student:students) {
				Object[] rowData = new Object[6];
				rowData[0] = student.get("examnum");
				rowData[1] = student.get("idnum");
				rowData[2] = student.get("name");
				for(int i = 0;i<rowData[2].toString().trim().length();i++) {
					String temp = rowData[2].toString();
					temp += " ";
					rowData[2] = temp;
				}
				String sex = student.get("sex").toString();
				if(sex.equals("F")) {
					sex = "女 ";
				} else {
					sex = "男 ";
				}
				rowData[3] = sex;
				String sut = student.get("score").toString();
				rowData[4] = Double.parseDouble(student.get("score").toString());
				rowData[5] = student.get("DJ");
				writer.addRecord(rowData);
			}

			fos = new FileOutputStream(outFilePath);
			writer.write(fos);
			
		} catch (Exception e) {
			  e.printStackTrace();
              System.out.println("写入DBF文件发生错误！！！");
		} finally {
	         fos.flush();
	         fos.close();
		}
	}
	@RequestMapping(value = "exportAllSchoolCJDBF", method = RequestMethod.POST)
	public ModelAndView exportAllSchoolCJDBF(HttpServletRequest request,HttpServletResponse response) throws Exception{
		response.reset();  
		OutputStream os = response.getOutputStream();
		ZipOutputStream zo = null;
		String realPath = request.getSession().getServletContext().getRealPath( "/") + "cjdbf";
		DateFormat date = new SimpleDateFormat( "yyyy-MM-dd"); 
		String institutionnum = (String) request.getSession().getAttribute("institution");
		List schools = new ArrayList();
		List<Map> schoolsInfo;
		if(institutionnum.equals("1001")) {
			schools = this.institutionService.getSchoolNum();
		} else {
			schoolsInfo = this.institutionService.loadManagedSchoolList(institutionnum);
			for(Map element:schoolsInfo) {
				schools.add(element.get("institutionnum"));
			}
		}
		String filename = "CJ(" + date.format(new Date()).toString() + ")";
		for(Object school:schools) {
			String schoolnum = school.toString();
			this.saveOneSchooleCJDBF(realPath,schoolnum);
		}
		try {
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");        
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-Type","application/force-download");
			response.setHeader("Content-disposition","attachment;filename="+filename+".zip");
			
			BufferedOutputStream bs = new BufferedOutputStream(os);
			zo = new ZipOutputStream(bs);
			this.zip(realPath, zo);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			zo.closeEntry();
			zo.close();
			this.deleteDbfFile(new File(realPath));
		}
		return null;
	}
	
	/**
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 
	 * 考试院端
	 * 导出所有高校考生库
	 */
	@RequestMapping(value = "exportAllSchoolDbf", method = RequestMethod.POST)
	public ModelAndView exportAllSchoolDbf(HttpServletRequest request,HttpServletResponse response) throws Exception{
		response.reset();  
		OutputStream os = response.getOutputStream();
		ZipOutputStream zo = null;
		String realPath = request.getSession().getServletContext().getRealPath( "/") + "ksdbf";
		DateFormat date = new SimpleDateFormat( "yyyy-MM-dd"); 
		String institutionnum = (String) request.getSession().getAttribute("institution");
		List schools = new ArrayList();
		List<Map> schoolsInfo;
		if(institutionnum.equals("1001")) {
			schools = this.institutionService.getSchoolNum();
		} else {
			schoolsInfo = this.institutionService.loadManagedSchoolList(institutionnum);
			for(Map element:schoolsInfo) {
				schools.add(element.get("institutionnum"));
			}
		}
		String filename = "ks(" + date.format(new Date()).toString() + ")";
		for(Object school:schools) {
			String schoolnum = school.toString();
			this.saveOneSchooleDbf(realPath,schoolnum);
		}
		try {
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");        
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-Type","application/force-download");
			response.setHeader("Content-disposition","attachment;filename="+filename+".zip");
			
			BufferedOutputStream bs = new BufferedOutputStream(os);
			zo = new ZipOutputStream(bs);
			this.zip(realPath, zo);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			zo.closeEntry();
			zo.close();
			this.deleteDbfFile(new File(realPath));
		}
		return null;
	}
	private void zip(String realPath,ZipOutputStream zo) throws IOException{
		File infile = new File(realPath);
		File[] dbfs = infile.listFiles();
		byte[] buf = new byte[1024*1024];
        int len;
		try {
			for(int i=0; i<dbfs.length; i++){
				BufferedInputStream bis = new BufferedInputStream(new FileInputStream(dbfs[i]));
				String pathName = "";
				pathName = dbfs[i].getName();
				zo.putNextEntry(new ZipEntry(pathName));
				while((len=bis.read(buf))>0) {
	                zo.write(buf,0,len);
				}
				bis.close();
			}
		} catch(Exception e) {
			  e.printStackTrace();
              System.out.println("写入ZIP文件发生错误！！！");
		} 
	}
	@RequestMapping(value = "exportStudentsDbf", method = RequestMethod.POST)
	public ModelAndView exportStudentsDbf(HttpServletRequest request,HttpServletResponse response) throws Exception{
		response.reset();  
		OutputStream os = response.getOutputStream();
		String instittutionnum = request.getSession().getAttribute("institution").toString();
		DateFormat date = new SimpleDateFormat( "yyyy-MM-dd"); 
		String filename = "ks" + request.getSession().getAttribute("institution").toString() + "(" +  date.format(new Date()).toString() + ")";
		try  {
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");        
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-Type","application/force-download");
			response.setHeader("Content-disposition","attachment;filename="+filename+".dbf");
			
			DBFField[] fields = new DBFField[4]; 
			
			fields[0] = new DBFField();
			fields[0].setName("ZKZH");
			fields[0].setDataType( DBFField.FIELD_TYPE_C);
			fields[0].setFieldLength(20);   
			
			fields[1] = new DBFField();
			fields[1].setName("SFZH");
			fields[1].setDataType( DBFField.FIELD_TYPE_C);
			fields[1].setFieldLength(18);  
			
			fields[2] = new DBFField();
			fields[2].setName("XM");
			fields[2].setDataType( DBFField.FIELD_TYPE_C);
			fields[2].setFieldLength(100);   
			
			fields[3] = new DBFField();
			fields[3].setName("XB");
			fields[3].setDataType( DBFField.FIELD_TYPE_C);
			fields[3].setFieldLength(5);   
			
			DBFWriter  writer = new DBFWriter();
			writer.setCharactersetName("GBK");
			writer.setFields(fields);
			
			List<Map> students = studentservice.getAllStudents(instittutionnum);
			for(Map student:students) {
				Object[] rowData = new Object[4];
				rowData[0] = student.get("examnum");
				rowData[1] = student.get("idnum");
				rowData[2] = student.get("name");
				for(int i = 0;i<rowData[2].toString().trim().length();i++) {
					String temp = rowData[2].toString();
					temp += " ";
					rowData[2] = temp;
				}
				String sex = student.get("sex").toString();
				if(sex.equals("F")) {
					sex = "女 ";
				} else {
					sex = "男 ";
				}
				rowData[3] = sex;
				writer.addRecord(rowData);
			}
			writer.write(os);

			
		} catch (Exception e) {
			  e.printStackTrace();
              System.out.println("写入DBF文件发生错误！！！");
		} finally {
			 os.flush();
	         os.close();
		}
		return null;
	}
	@RequestMapping(value = "exportOneSchoolDbf", method = RequestMethod.POST)
	public ModelAndView exportOneSchoolDbf(@RequestParam("school") String school,HttpServletRequest request,HttpServletResponse response) throws Exception{
		response.reset();  
		OutputStream os = response.getOutputStream();
		DateFormat date = new SimpleDateFormat( "yyyy-MM-dd"); 
		String filename = "ks" + school + "(" +  date.format(new Date()).toString() + ")";
		try  {
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");        
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-Type","application/force-download");
			response.setHeader("Content-disposition","attachment;filename="+filename+".dbf");
			
			DBFField[] fields = new DBFField[4]; 
			
			fields[0] = new DBFField();
			fields[0].setName("ZKZH");
			fields[0].setDataType( DBFField.FIELD_TYPE_C);
			fields[0].setFieldLength(20);   
			
			fields[1] = new DBFField();
			fields[1].setName("SFZH");
			fields[1].setDataType( DBFField.FIELD_TYPE_C);
			fields[1].setFieldLength(18);  
			
			fields[2] = new DBFField();
			fields[2].setName("XM");
			fields[2].setDataType( DBFField.FIELD_TYPE_C);
			fields[2].setFieldLength(100);   
			
			fields[3] = new DBFField();
			fields[3].setName("XB");
			fields[3].setDataType( DBFField.FIELD_TYPE_C);
			fields[3].setFieldLength(5);   
			
			
			DBFWriter  writer = new DBFWriter();
			writer.setCharactersetName("GBK");
			writer.setFields(fields);
			
			List<Map> students = studentservice.getAllStudents(school);
			for(Map student:students) {
				Object[] rowData = new Object[4];
				rowData[0] = student.get("examnum");
				rowData[1] = student.get("idnum");
				rowData[2] = student.get("name");
				for(int i = 0;i<rowData[2].toString().trim().length();i++) {
					String temp = rowData[2].toString();
					temp += " ";
					rowData[2] = temp;
				}
				String sex = student.get("sex").toString();
				if(sex.equals("F")) {
					sex = "女 ";
				} else {
					sex = "男 ";
				}
				rowData[3] = sex;
				writer.addRecord(rowData);
			}
			writer.write(os);
			
		} catch (Exception e) {
			  e.printStackTrace();
              System.out.println("写入DBF文件发生错误！！！");
		} finally {
			 os.flush();
	         os.close();
		}
		return null;
	}
	@RequestMapping(value = "exportOneSchoolCJDbf", method = RequestMethod.POST)
	public ModelAndView exportOneSchoolCJ(HttpServletRequest request,HttpServletResponse response) throws Exception{
		response.reset();  
		OutputStream os = response.getOutputStream();
		DateFormat date = new SimpleDateFormat( "yyyy-MM-dd"); 
		String school = request.getSession().getAttribute("institution").toString();
		String filename =  school + "CJ" + "(" +  date.format(new Date()).toString() + ")";
		try  {
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");        
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-Type","application/force-download");
			response.setHeader("Content-disposition","attachment;filename="+filename+".dbf");
			
			DBFField[] fields = new DBFField[6]; 
			
			fields[0] = new DBFField();
			fields[0].setName("ZKZH");
			fields[0].setDataType( DBFField.FIELD_TYPE_C);
			fields[0].setFieldLength(20);   
			
			fields[1] = new DBFField();
			fields[1].setName("SFZH");
			fields[1].setDataType( DBFField.FIELD_TYPE_C);
			fields[1].setFieldLength(18);  
			
			fields[2] = new DBFField();
			fields[2].setName("XM");
			fields[2].setDataType( DBFField.FIELD_TYPE_C);
			fields[2].setFieldLength(100);  
			
			fields[3] = new DBFField();
			fields[3].setName("XB");
			fields[3].setDataType( DBFField.FIELD_TYPE_C);
			fields[3].setFieldLength(5);   
			
			fields[4] = new DBFField();
			fields[4].setName("FS");
			fields[4].setDataType( DBFField.FIELD_TYPE_F);
			fields[4].setFieldLength(4);   
			
			fields[5] = new DBFField();
			fields[5].setName("DJ");
			fields[5].setDataType( DBFField.FIELD_TYPE_C);
			fields[5].setFieldLength(2);   
			
			DBFWriter  writer = new DBFWriter();
			writer.setCharactersetName("GBK");
			writer.setFields(fields);
			
			List<Map> students = studentservice.getAllStudentsCJ(school);
			for(Map student:students) {
				Object[] rowData = new Object[6];
				rowData[0] = student.get("examnum");
				rowData[1] = student.get("idnum");
				rowData[2] = student.get("name");
				for(int i = 0;i<rowData[2].toString().trim().length();i++) {
					String temp = rowData[2].toString();
					temp += " ";
					rowData[2] = temp;
				}
				String sex = student.get("sex").toString();
				if(sex.equals("F")) {
					sex = "女 ";
				} else {
					sex = "男 ";
				}
				rowData[3] = sex;
				String sut = student.get("score").toString();
				rowData[4] = Double.parseDouble(student.get("score").toString());
				rowData[5] = student.get("DJ");
				writer.addRecord(rowData);
			}
			writer.write(os);
			
		} catch (Exception e) {
			  e.printStackTrace();
              System.out.println("写入DBF文件发生错误！！！");
		} finally {
			 os.flush();
	         os.close();
		}
		return null;
	}
	@RequestMapping(value = "exportStudentsExcel", method = RequestMethod.POST)
	public ModelAndView exportStudentsXls(HttpServletRequest request,HttpServletResponse response) throws Exception{
		response.reset();  
        OutputStream os = response.getOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(os);
		DateFormat   date   =   new  SimpleDateFormat( "yyyy-MM-dd"); 
		String filename = "ks" + request.getSession().getAttribute("institution").toString() + "(" +  date.format(new Date()).toString() + ")";
        try {     
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");        //改成输出excel文件
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type","application/force-download");
            response.setHeader("Content-disposition","attachment;filename="+filename+".xls");
            WritableSheet sheet  = wwb.createSheet("考生库", 0);
            sheet.setColumnView(0, 25);
            sheet.setColumnView(1, 25);
            WritableFont blodFont = new WritableFont(WritableFont.TAHOMA,10,WritableFont.BOLD, false);
            WritableCellFormat blodFormat = new WritableCellFormat (blodFont);
            Label label = null;
        		label = new Label(0,0,"ZKZH");
        		sheet.addCell(label);
        		label = new Label(1,0,"SFZH");
        		sheet.addCell(label);
        		label = new Label(2,0,"XM");
        		sheet.addCell(label);
        		label = new Label(3,0,"XB");
        		sheet.addCell(label);
                List<Map> students = studentservice.getAllStudents(request.getSession().getAttribute("institution").toString());
                for(int i = 0; i < students.size(); i++){
                	label = new Label(0,i+1,students.get(i).get("examnum").toString());
                	sheet.addCell(label);
                	label = new Label(1,i+1,students.get(i).get("idnum").toString());
                	sheet.addCell(label);
                	label = new Label(2,i+1,students.get(i).get("name").toString());
                	sheet.addCell(label);
                	label = new Label(3,i+1,students.get(i).get("sex").toString());
                	sheet.addCell(label);
                }

        } catch(Exception ex) {
                ex.printStackTrace();
                System.out.println("写入Excel文件发生错误！！！");
        }finally{
            wwb.write();
            wwb.close();
            os.flush();
            os.close();
        }
        
        return null;
	} 
	
	@RequestMapping(value = "exportStudentsPaymentExcel", method = RequestMethod.POST)
	public ModelAndView exportStudentsPaymentExcel(HttpServletRequest request,HttpServletResponse response) throws Exception{
        response.reset(); 
        OutputStream os = response.getOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(os);
		DateFormat date = new  SimpleDateFormat( "yyyy-MM-dd");
		String filename = request.getSession().getAttribute("institution").toString() + "studentunpaid" + date.format(new Date()).toString();
        try {
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");        //改成输出excel文件
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type","application/force-download");
            response.setHeader("Content-disposition","attachment;filename="+filename+".xls");
            WritableSheet sheet  = wwb.createSheet("考生缴费确认", 0);
            sheet.setColumnView(0, 25);
            sheet.setColumnView(1, 25);
            sheet.setColumnView(2, 25);
            sheet.setColumnView(3, 25);
            sheet.setColumnView(4, 25);
            sheet.setColumnView(5, 25);
            WritableFont blodFont = new WritableFont(WritableFont.TAHOMA,10,WritableFont.BOLD, false);
            WritableCellFormat blodFormat = new WritableCellFormat (blodFont);
            Label label = null;
        		label = new Label(0,0,"学号");
        		sheet.addCell(label);
        		label = new Label(1,0,"身份证号");
        		sheet.addCell(label);
        		label = new Label(2,0,"姓名");
        		sheet.addCell(label);
        		label = new Label(3,0,"语种");
        		sheet.addCell(label);
        		label = new Label(4,0,"学院");
        		sheet.addCell(label);
        		label = new Label(5,0,"专业");
        		sheet.addCell(label);
        		label = new Label(6,0,"班级");
        		sheet.addCell(label);
        		label = new Label(7,0,"是否缴费");
        		sheet.addCell(label);
                List<Map> students = studentservice.getAllStudentsUnpaid(request.getSession().getAttribute("institution").toString());
                for(int i = 0; i < students.size(); i++){
                	label = new Label(0,i+1,students.get(i).get("studentnum").toString());
                	sheet.addCell(label);
                	label = new Label(1,i+1,students.get(i).get("idnum").toString());
                	sheet.addCell(label);
                	label = new Label(2,i+1,students.get(i).get("name").toString());
                	sheet.addCell(label);
                	label = new Label(3,i+1,students.get(i).get("exLanguage").toString());
                	sheet.addCell(label);
                	label = new Label(4,i+1,students.get(i).get("exCollege").toString());
                	sheet.addCell(label);
                	label = new Label(5,i+1,students.get(i).get("exProfession").toString());
                	sheet.addCell(label);
                	label = new Label(6,i+1,students.get(i).get("class").toString());
                	sheet.addCell(label);
                	label = new Label(7,i+1,students.get(i).get("paied").toString());
                	sheet.addCell(label);
                }

        } catch(Exception ex) {
                ex.printStackTrace();
                System.out.println("写入Excel文件发生错误！！！");
               
        }finally{
            wwb.write();
            wwb.close();
            os.flush();
            os.close();
        }
        return null;
	} 
	
	@RequestMapping(value = "backupStudentsExcel", method = RequestMethod.POST)
	public ModelAndView backupStudentsExcel(HttpServletRequest request,HttpServletResponse response) throws Exception{
		response.reset();
        OutputStream os = response.getOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(os);
		DateFormat   date   =   new  SimpleDateFormat( "yyyy-MM-dd");
		String filename = request.getSession().getAttribute("institution").toString() + "StudentInfoBackUp" + date.format(new Date()).toString();
        try { 

            response.setContentType("application/vnd.ms-excel;charset=UTF-8");        //改成输出excel文件
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type","application/force-download");
            response.setHeader("Content-disposition","attachment;filename="+filename+".xls");
            WritableSheet sheet  = wwb.createSheet("考生信息备份", 0);
            sheet.setColumnView(0, 25);
            sheet.setColumnView(1, 25);
            sheet.setColumnView(2, 25);
            sheet.setColumnView(3, 25);
            sheet.setColumnView(4, 25);
            sheet.setColumnView(5, 25);
            WritableFont blodFont = new WritableFont(WritableFont.TAHOMA,10,WritableFont.BOLD, false);
            WritableCellFormat blodFormat = new WritableCellFormat (blodFont);
            Label label = null;
            	int m = 0;
        		label = new Label(m++,0,"学号");
        		sheet.addCell(label);
        		label = new Label(m++,0,"身份证号");
        		sheet.addCell(label);
        		label = new Label(m++,0,"准考证号");
        		sheet.addCell(label);
        		label = new Label(m++,0,"姓名");
        		sheet.addCell(label);
        		label = new Label(m++,0,"语种名称");
        		sheet.addCell(label);
        		label = new Label(m++,0,"分数");
        		sheet.addCell(label);
        		label = new Label(m++,0,"理论缺考");
        		sheet.addCell(label);
        		label = new Label(m++,0,"理论作弊");
        		sheet.addCell(label);
        		label = new Label(m++,0,"上机缺考");
        		sheet.addCell(label);
        		label = new Label(m++,0,"上机作弊");
        		sheet.addCell(label);
        		label = new Label(m++,0,"校区名称");
        		sheet.addCell(label);
        		label = new Label(m++,0,"学院名称");
        		sheet.addCell(label);
        		label = new Label(m++,0,"专业名称");
        		sheet.addCell(label);
        		label = new Label(m++,0,"年级");
        		sheet.addCell(label);
        		label = new Label(m++,0,"性别");
        		sheet.addCell(label);
        		label = new Label(m++,0,"班级");
        		sheet.addCell(label);
        		label = new Label(m++,0,"学制");
        		sheet.addCell(label);
        		label = new Label(m++,0,"考生类别");
        		sheet.addCell(label);
        		label = new Label(m++,0,"是否缴费");
        		sheet.addCell(label);
        		
                List<Map> students = studentservice.backupStudentsExcel(request.getSession().getAttribute("institution").toString());
                for(int i = 0; i < students.size(); i++){
                	int n = 0;
                	label = new Label(n++,i+1,students.get(i).get("studentnum").toString());
                	sheet.addCell(label);
                	label = new Label(n++,i+1,students.get(i).get("idnum").toString());
                	sheet.addCell(label);
                	label = new Label(n++,i+1,students.get(i).get("examnum").toString());
                	sheet.addCell(label);
                	label = new Label(n++,i+1,students.get(i).get("name").toString());
                	sheet.addCell(label);
                	label = new Label(n++,i+1,students.get(i).get("exLanguage").toString());
                	sheet.addCell(label);
                	label = new Label(n++,i+1,students.get(i).get("score").toString());
                	sheet.addCell(label);
                	label = new Label(n++,i+1,students.get(i).get("theoryabsent").toString());
                	sheet.addCell(label);
                	label = new Label(n++,i+1,students.get(i).get("theoryfraud").toString());
                	sheet.addCell(label);
                	label = new Label(n++,i+1,students.get(i).get("operateabsent").toString());
                	sheet.addCell(label);
                	label = new Label(n++,i+1,students.get(i).get("operatefraud").toString());
                	sheet.addCell(label);
                	label = new Label(n++,i+1,students.get(i).get("exCampus").toString());
                	sheet.addCell(label);
                	label = new Label(n++,i+1,students.get(i).get("exCollege").toString());
                	sheet.addCell(label);
                	label = new Label(n++,i+1,students.get(i).get("exProfession").toString());
                	sheet.addCell(label);
                	label = new Label(n++,i+1,students.get(i).get("grade").toString());
                	sheet.addCell(label);
                	label = new Label(n++,i+1,students.get(i).get("sex").toString());
                	sheet.addCell(label);
                	label = new Label(n++,i+1,students.get(i).get("classnum").toString());
                	sheet.addCell(label);
                	label = new Label(n++,i+1,students.get(i).get("lengthofyear").toString());
                	sheet.addCell(label);
                	label = new Label(n++,i+1,students.get(i).get("studentcategory").toString());
                	sheet.addCell(label);
                	label = new Label(n++,i+1,students.get(i).get("paied").toString());
                	sheet.addCell(label);
                }
        } catch(Exception ex) {
                ex.printStackTrace();
                System.out.println(ex.getMessage());
                System.out.println("写入Excel文件发生错误！！！");
        }finally{
            wwb.write();
            wwb.close();
            os.flush();
            os.close();
        }
        return null;
	} 
	
	//导出未报名学生
	@RequestMapping(value = "exportUnSignUpStudentsExcel", method = RequestMethod.POST)
	public ModelAndView exportUnSignUpStudentsExcel(HttpServletRequest request,HttpServletResponse response) throws Exception{
        response.reset(); 
        OutputStream os = response.getOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(os);
		DateFormat   date   =   new  SimpleDateFormat( "yyyy-MM-dd"); 
		String filename = request.getSession().getAttribute("institution").toString() + "unsignupstudent" + date.format(new Date()).toString();
        try {
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");        //改成输出excel文件
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type","application/force-download");
            response.setHeader("Content-disposition","attachment;filename="+filename+".xls");
            WritableSheet sheet  = wwb.createSheet("已报名未缴费考生确认", 0);
            sheet.setColumnView(0, 25);
            sheet.setColumnView(1, 25);
            sheet.setColumnView(2, 25);
            sheet.setColumnView(7, 25);
            sheet.setColumnView(4, 25);
            sheet.setColumnView(6, 25);
            WritableFont blodFont = new WritableFont(WritableFont.TAHOMA,10,WritableFont.BOLD, false);
            WritableCellFormat blodFormat = new WritableCellFormat (blodFont);
            Label label = null;
        		label = new Label(0,0,"学号");
        		sheet.addCell(label);
        		label = new Label(1,0,"姓名");
        		sheet.addCell(label);
        		label = new Label(2,0,"身份证号");
        		sheet.addCell(label);
        		label = new Label(3,0,"校区名称");
        		sheet.addCell(label);
        		label = new Label(4,0,"学院名称");
        		sheet.addCell(label);
        		label = new Label(5,0,"班级");
        		sheet.addCell(label);
        		label = new Label(6,0,"语种");
        		sheet.addCell(label);
        		label = new Label(7,0,"是否缴费");
        		sheet.addCell(label);
                List<Map> students = studentservice.getAllStudentsUnsignup(request.getSession().getAttribute("institution").toString());
                for(int i = 0; i < students.size(); i++){
                	label = new Label(0,i+1,students.get(i).get("studentnum").toString());
                	sheet.addCell(label);
                	label = new Label(1,i+1,students.get(i).get("name").toString());
                	sheet.addCell(label);
                	label = new Label(2,i+1,students.get(i).get("idnum").toString());
                	sheet.addCell(label);
                	label = new Label(3,i+1,students.get(i).get("exCampus").toString());
                	sheet.addCell(label);
                	label = new Label(4,i+1,students.get(i).get("exCollege").toString());
                	sheet.addCell(label);
                	label = new Label(5,i+1,students.get(i).get("classnum").toString());
                	sheet.addCell(label);
                	label = new Label(6,i+1,students.get(i).get("languagename").toString());
                	sheet.addCell(label);           	                	
                	label = new Label(7,i+1,students.get(i).get("paied").toString());
                	sheet.addCell(label);
                }

        } catch(Exception ex) {
                ex.printStackTrace();
                System.out.println("写入Excel文件发生错误！！！");
               
        }finally{
            wwb.write();
            wwb.close();
            os.flush();
            os.close();
        }
        return null;
	} 
	
	//导出全省高校报名统计表
	@RequestMapping(value = "exportAllProvinceSignUpInfoSum", method = RequestMethod.POST)
	public ModelAndView exportAllProvinceSignUpInfoSum(HttpServletRequest request,HttpServletResponse response) throws Exception{
        response.reset(); 
        OutputStream os = response.getOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(os);
		DateFormat   date   =   new  SimpleDateFormat( "yyyy-MM-dd"); 
		String institutionnum = request.getSession().getAttribute("institution").toString();
		String filename =   "SignUpInfoSum" + date.format(new Date()).toString();
        try {
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");        //改成输出excel文件
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type","application/force-download");
            response.setHeader("Content-disposition","attachment;filename="+filename+".xls");
            WritableSheet sheet  = wwb.createSheet("报名考生数据统计表", 0);
            sheet.setColumnView(1, 25);

            WritableFont blodFont = new WritableFont(WritableFont.TAHOMA,10,WritableFont.BOLD, false);
            WritableCellFormat blodFormat = new WritableCellFormat (blodFont);
//            String category =  institutionService.getInstitutionByInstitutionNum(institutionnum).getCategory();
            List<Map> languageList = languageService.loadlanguageList();
            List<Map> students = studentservice.AllProvinceStuInfoSum(institutionnum,"city",true);
            Label label = null;
        		label = new Label(0,0,"学校代码");
        		sheet.addCell(label);
        		label = new Label(1,0,"学校名称");
        		sheet.addCell(label);
        		int m = 2;
        		for(Map language : languageList){
        			label = new Label(m,0,language.get("languagenum").toString()+language.get("languagename").toString());
            		sheet.addCell(label);
            		m++;
        		}
        		label = new Label(m,0,"共计");
        		sheet.addCell(label);	
                for(int i = 0; i < students.size(); i++){
                	label = new Label(0,i+1,students.get(i).get("institutionnum").toString());
                	sheet.addCell(label);
                	label = new Label(1,i+1,students.get(i).get("institutionname").toString());
                	sheet.addCell(label);
                	String lauguagenum ;
                	for(int j = 2 ; j<m ;j++){
                		lauguagenum = sheet.getCell(j, 0).getContents().substring(0,2);
                    	label = new Label(j,i+1,students.get(i).get(lauguagenum).toString());
                    	sheet.addCell(label);
                	}
                	label = new Label(m,i+1,students.get(i).get("count").toString());
                	sheet.addCell(label);
                }

        } catch(Exception ex) {
                ex.printStackTrace();
                System.out.println("写入Excel文件发生错误！！！");
               
        }finally{
            wwb.write();
            wwb.close();
            os.flush();
            os.close();
        }
        return null;
	} 
	
	//导出全省高校报名统计表
	@RequestMapping(value = "exportAllProvinceSignUpInfoFJ", method = RequestMethod.POST)
	public ModelAndView exportAllProvinceSignUpInfoFJ(HttpServletRequest request,HttpServletResponse response) throws Exception{
        response.reset(); 
        OutputStream os = response.getOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(os);
		DateFormat   date   =   new  SimpleDateFormat( "yyyy-MM-dd"); 
		String institutionnum = request.getSession().getAttribute("institution").toString();
		String filename =   "FJSignUpInfo" + date.format(new Date()).toString();
        try {
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");        //改成输出excel文件
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type","application/force-download");
            response.setHeader("Content-disposition","attachment;filename="+filename+".xls");
            WritableSheet sheet  = wwb.createSheet("报名考生数据统计表(分级)", 0);
            sheet.setColumnView(1, 25);

            WritableFont blodFont = new WritableFont(WritableFont.TAHOMA,10,WritableFont.BOLD, false);
            WritableCellFormat blodFormat = new WritableCellFormat (blodFont);
//            String category =  institutionService.getInstitutionByInstitutionNum(institutionnum).getCategory();
            List<Map> languageList = languageService.loadlanguageList();
            List<Map> students = studentservice.AllProvinceStuInfoSum(institutionnum,"city",false);
            Label label = null;
        		label = new Label(0,0,"学校代码");
        		sheet.addCell(label);
        		label = new Label(1,0,"学校名称");
        		sheet.addCell(label);
        		int m = 2;
        		String levelNum = "";
        		for(Map language : languageList){
        			int level = Integer.parseInt(language.get("languagenum").toString().substring(0, 1));
        			String levelname = "";
        			switch(level){
        				case 1 : levelname = "一级";break;
        				case 2 : levelname = "二级";break;
        				case 3 : levelname = "三级";break;
        				case 4 : levelname = "四级";break;
        				default : levelname = "五级";
        			
        			}
					if(!language.get("languagenum").toString().substring(0, 1).equals(levelNum)){
	        			label = new Label(m,0,levelname);
	            		sheet.addCell(label);
	            		m++;
					} 
					levelNum = language.get("languagenum").toString().substring(0, 1);
        		}
        		label = new Label(m,0,"共计");
        		sheet.addCell(label);	
                for(int i = 0; i < students.size(); i++){
                	label = new Label(0,i+1,students.get(i).get("institutionnum").toString());
                	sheet.addCell(label);
                	label = new Label(1,i+1,students.get(i).get("institutionname").toString());
                	sheet.addCell(label);
                	String lauguagenum ;
                	for(int j = 2 ; j<m ;j++){
                		lauguagenum = sheet.getCell(j, 0).getContents();
                		if(lauguagenum.equals("一级")){
                			lauguagenum = "1"; 
                		}else if(lauguagenum.equals("二级")){
                			lauguagenum = "2"; 
                		}else if(lauguagenum.equals("三级")){
                			lauguagenum = "3"; 
                		}else if(lauguagenum.equals("四级")){
                			lauguagenum = "4"; 
                		}else
                			lauguagenum = "5"; 
                    	label = new Label(j,i+1,students.get(i).get(lauguagenum).toString());
                    	sheet.addCell(label);
                	}
                	label = new Label(m,i+1,students.get(i).get("count").toString());
                	sheet.addCell(label);
                }

        } catch(Exception ex) {
                ex.printStackTrace();
                System.out.println("写入Excel文件发生错误！！！");
               
        }finally{
            wwb.write();
            wwb.close();
            os.flush();
            os.close();
        }
        return null;
	}
	
	//导出全省高校报名统计表
		@RequestMapping(value = "exportCitySignUpInfoFJ", method = RequestMethod.POST)
		public ModelAndView exportCitySignUpInfoFJ(HttpServletRequest request,HttpServletResponse response) throws Exception{
	        response.reset(); 
	        OutputStream os = response.getOutputStream();
	        WritableWorkbook wwb = Workbook.createWorkbook(os);
			DateFormat   date   =   new  SimpleDateFormat( "yyyy-MM-dd"); 
			String institutionnum = request.getSession().getAttribute("institution").toString();
			String filename =   "FJSignUpInfo" + date.format(new Date()).toString();
	        try {
	            response.setContentType("application/vnd.ms-excel;charset=UTF-8");        //改成输出excel文件
	            response.setCharacterEncoding("UTF-8");
	            response.setHeader("Content-Type","application/force-download");
	            response.setHeader("Content-disposition","attachment;filename="+filename+".xls");
	            WritableSheet sheet  = wwb.createSheet("报名考生数据统计表(分级)", 0);
	            sheet.setColumnView(1, 25);

	            WritableFont blodFont = new WritableFont(WritableFont.TAHOMA,10,WritableFont.BOLD, false);
	            WritableCellFormat blodFormat = new WritableCellFormat (blodFont);
//	            String category =  institutionService.getInstitutionByInstitutionNum(institutionnum).getCategory();
	            List<Map> languageList = languageService.loadlanguageList();
	            List<Map> students = studentservice.AllProvinceStuInfoSum(institutionnum,"school",true);
	            Label label = null;
	        	label = new Label(0,0,"学校代码");
	        	sheet.addCell(label);
	        	label = new Label(1,0,"学校名称");
	        	sheet.addCell(label);
	        	int m = 2;
	        	for(Map language : languageList){
	        		label = new Label(m,0,language.get("languagenum").toString()+language.get("languagename").toString());
	            	sheet.addCell(label);
	            	m++;
	        	}
	        	label = new Label(m,0,"共计");
	        	sheet.addCell(label);	
	        	for(int i = 0; i < students.size(); i++){
                	label = new Label(0,i+1,students.get(i).get("institutionnum").toString());
                	sheet.addCell(label);
                	label = new Label(1,i+1,students.get(i).get("institutionname").toString());
                	sheet.addCell(label);
                	String lauguagenum ;
                	for(int j = 2 ; j<m ;j++){
                		lauguagenum = sheet.getCell(j, 0).getContents().substring(0,2);
                    	label = new Label(j,i+1,students.get(i).get(lauguagenum).toString());
                    	sheet.addCell(label);
                	}
                	label = new Label(m,i+1,students.get(i).get("count").toString());
                	sheet.addCell(label);
                }
	        } catch(Exception ex) {
	                ex.printStackTrace();
	                System.out.println("写入Excel文件发生错误！！！");
	               
	        }finally{
	            wwb.write();
	            wwb.close();
	            os.flush();
	            os.close();
	        }
	        return null;
		}
	
	//导出全省考试袋数统计表
	@RequestMapping(value = "exportProvincePaperBags", method = RequestMethod.POST)
	public ModelAndView exportProvincePaperBags(HttpServletRequest request,HttpServletResponse response) throws Exception{
        response.reset(); 
        OutputStream os = response.getOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(os);
		DateFormat   date   =   new  SimpleDateFormat( "yyyy-MM-dd"); 
		String institutionnum = request.getSession().getAttribute("institution").toString();
		String filename =   "PaperBags" + date.format(new Date()).toString();
        try {
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");        //改成输出excel文件
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type","application/force-download");
            response.setHeader("Content-disposition","attachment;filename="+filename+".xls");
            WritableSheet sheet  = wwb.createSheet("全省考卷袋数统计表", 0);
            sheet.setColumnView(1, 25);
            
            WritableFont blodFont = new WritableFont(WritableFont.TAHOMA,10,WritableFont.BOLD, false);
            WritableCellFormat blodFormat = new WritableCellFormat (blodFont);
//            String category =  institutionService.getInstitutionByInstitutionNum(institutionnum).getCategory();
            List<Map> languageList = languageService.getTheoryLanguageList();
            List<Map> paperBags = studentservice.calcPaperBagsForExamCollege(institutionnum, "province", true);
            Label label = null;
        		label = new Label(0,0,"单位代码");
        		sheet.addCell(label);
        		label = new Label(1,0,"单位名称");
        		sheet.addCell(label);
        		int m = 2;
        		for(Map language : languageList){
	        			label = new Label(m,0,language.get("languagenum").toString() + language.get("languagename").toString());
	            		sheet.addCell(label);
	            		m++;
	        			label = new Label(m,0,language.get("languagenum").toString()+"备用");
	            		sheet.addCell(label);
	            		m++;
        		}
//        		label = new Label(m,0,"共计");
//        		sheet.addCell(label);	
                for(int i = 0; i < paperBags.size(); i++) {
                	label = new Label(0,i+1,paperBags.get(i).get("institutionnum").toString());
                	sheet.addCell(label);
                	label = new Label(1,i+1,paperBags.get(i).get("institutionname").toString());
                	sheet.addCell(label);
                	String lauguagenum ;
                	for(int j = 2 ; j<m ;j++) {
                		lauguagenum = sheet.getCell(j, 0).getContents().toString();
                		if(lauguagenum.substring(2, 4).equals("备用")){
                			lauguagenum = sheet.getCell(j, 0).getContents().toString().substring(0, 2) + "bybags";
                		}else {
                			lauguagenum = sheet.getCell(j, 0).getContents().toString().substring(0, 2) + "bags";
                		}
                    	label = new Label(j,i+1,paperBags.get(i).get(lauguagenum).toString());
                    	sheet.addCell(label);
                	}
                }

        } catch(Exception ex) {
                ex.printStackTrace();
                System.out.println("写入Excel文件发生错误！！！");
               
        }finally{
            wwb.write();
            wwb.close();
            os.flush();
            os.close();
        }
        return null;
	}
	
	
	//导出市地考试袋数统计表
	@RequestMapping(value = "exportCityPaperBags", method = RequestMethod.POST)
	public ModelAndView exportCityPaperBags(HttpServletRequest request,HttpServletResponse response) throws Exception{
        response.reset(); 
        OutputStream os = response.getOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(os);
		DateFormat   date   =   new  SimpleDateFormat( "yyyy-MM-dd"); 
		String institutionnum = request.getSession().getAttribute("institution").toString();
		String filename =   "PaperBags" + date.format(new Date()).toString();
		List<Map> languageList = languageService.getTheoryLanguageList();
		List<Map> paperBags = studentservice.calcPaperBagsForExamCollege(institutionnum, "city", true);
        try {
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");        //改成输出excel文件
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type","application/force-download");
            response.setHeader("Content-disposition","attachment;filename="+filename+".xls");
            WritableSheet sheet  = wwb.createSheet("全市考卷袋数统计表", 0);
            sheet.setColumnView(1, 25);

            WritableFont blodFont = new WritableFont(WritableFont.TAHOMA,10,WritableFont.BOLD, false);
            WritableCellFormat blodFormat = new WritableCellFormat (blodFont);
            
            
            Label label = null;
            label = new Label(0,0,"单位代码");
            sheet.addCell(label);
            label = new Label(1,0,"单位名称");
            sheet.addCell(label);
            int m = 2;
            for(Map language : languageList){
            	label = new Label(m,0,language.get("languagenum").toString() + language.get("languagename").toString());
            	sheet.addCell(label);
            	m++;
            	label = new Label(m,0,language.get("languagenum").toString()+"备用");
            	sheet.addCell(label);
            	m++;
            }
            for(int i = 0; i < paperBags.size(); i++){
            	label = new Label(0,i+1,paperBags.get(i).get("institutionnum").toString());
            	sheet.addCell(label);
            	label = new Label(1,i+1,paperBags.get(i).get("institutionname").toString());
            	sheet.addCell(label);
            	String lauguagenum ;
            	for(int j = 2 ; j<m ;j++){
            		lauguagenum = sheet.getCell(j, 0).getContents().toString();
            		if(lauguagenum.substring(2, 4).equals("备用")){
            			lauguagenum = sheet.getCell(j, 0).getContents().toString().substring(0, 2) + "bybags";
            		}else {
            			lauguagenum = sheet.getCell(j, 0).getContents().toString().substring(0, 2) + "bags";
            		}
            		label = new Label(j,i+1,paperBags.get(i).get(lauguagenum).toString());
            		sheet.addCell(label);
            	}
            }	
        } catch(Exception ex) {
                ex.printStackTrace();
                System.out.println("写入Excel文件发生错误！！！");
               
        }finally{
            wwb.write();
            wwb.close();
            os.flush();
            os.close();
        }
        return null;
	}
	
	//导出高校考试袋数统计表
	@RequestMapping(value = "exportSchoolPaperBags", method = RequestMethod.POST)
	public ModelAndView exportSchoolPaperBags(HttpServletRequest request,HttpServletResponse response) throws Exception{
        response.reset(); 
        OutputStream os = response.getOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(os);
		DateFormat   date   =   new  SimpleDateFormat( "yyyy-MM-dd"); 
		String institutionnum = request.getSession().getAttribute("institution").toString();
		String filename =   "PaperBags" + date.format(new Date()).toString();
        try {
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");        //改成输出excel文件
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type","application/force-download");
            response.setHeader("Content-disposition","attachment;filename="+filename+".xls");
            WritableSheet sheet  = wwb.createSheet("全校考卷袋数统计表", 0);
            sheet.setColumnView(1, 25);

            WritableFont blodFont = new WritableFont(WritableFont.TAHOMA,10,WritableFont.BOLD, false);
            WritableCellFormat blodFormat = new WritableCellFormat (blodFont);
//            String category =  institutionService.getInstitutionByInstitutionNum(institutionnum).getCategory();
            List<Map> languageList = languageService.getTheoryLanguageList();
            List<Map> paperBags = studentservice.calcPaperBagsForExamCollege(institutionnum, "school", true);
            Label label = null;
        		label = new Label(0,0,"单位代码");
        		sheet.addCell(label);
        		label = new Label(1,0,"单位名称");
        		sheet.addCell(label);
        		int m = 2;
        		for(Map language : languageList){
	        			label = new Label(m,0,language.get("languagenum").toString() + language.get("languagename").toString());
	            		sheet.addCell(label);
	            		m++;
	        			label = new Label(m,0,language.get("languagenum").toString()+"备用");
	            		sheet.addCell(label);
	            		m++;
        		}
//        		label = new Label(m,0,"共计");
//        		sheet.addCell(label);	
                for(int i = 0; i < paperBags.size(); i++){
                	label = new Label(0,i+1,paperBags.get(i).get("institutionnum").toString());
                	sheet.addCell(label);
                	label = new Label(1,i+1,paperBags.get(i).get("institutionname").toString());
                	sheet.addCell(label);
                	String lauguagenum ;
                	for(int j = 2 ; j<m ;j++){
                		lauguagenum = sheet.getCell(j, 0).getContents().toString();
                		if(lauguagenum.substring(2, 4).equals("备用")){
                			lauguagenum = sheet.getCell(j, 0).getContents().toString().substring(0, 2) + "bybags";
                		}else {
                			lauguagenum = sheet.getCell(j, 0).getContents().toString().substring(0, 2) + "bags";
                		}
                    	label = new Label(j,i+1,paperBags.get(i).get(lauguagenum).toString());
                    	sheet.addCell(label);
                	}
                }

        } catch(Exception ex) {
                ex.printStackTrace();
                System.out.println("写入Excel文件发生错误！！！");
               
        }finally{
            wwb.write();
            wwb.close();
            os.flush();
            os.close();
        }
        return null;
	}
	
	
	
	@RequestMapping(value = "exportStudentsAdmissionExcel", method = RequestMethod.POST)
	public ModelAndView exportStudentsAdmissionExcel(HttpServletRequest request,HttpServletResponse response) throws Exception{
        response.reset(); 
        OutputStream os = response.getOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(os);
		DateFormat date = new  SimpleDateFormat( "yyyy-MM-dd");
		String filename = request.getSession().getAttribute("institution").toString() + "studentsadmission" + date.format(new Date()).toString();
        try {
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");        //改成输出excel文件
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type","application/force-download");
            response.setHeader("Content-disposition","attachment;filename="+filename+".xls");
            WritableSheet sheet  = wwb.createSheet("所有考生准考证信息", 0);
            sheet.setColumnView(0, 15);
            sheet.setColumnView(1, 25);
            sheet.setColumnView(2, 25);
            sheet.setColumnView(3, 15);
            sheet.setColumnView(4, 25);
            sheet.setColumnView(5, 25);
            sheet.setColumnView(6, 25);
            sheet.setColumnView(7, 25);
            sheet.setColumnView(8, 25);
            sheet.setColumnView(9, 25);
            sheet.setColumnView(10, 25);
            sheet.setColumnView(11, 25);
            sheet.setColumnView(12, 25);
            sheet.setColumnView(13, 25);
            sheet.setColumnView(14, 25);
            sheet.setColumnView(15, 25);
            sheet.setColumnView(16, 10);
            WritableFont blodFont = new WritableFont(WritableFont.TAHOMA,10,WritableFont.BOLD, false);
            WritableCellFormat blodFormat = new WritableCellFormat (blodFont);
            Label label = null;
        		label = new Label(0,0,"姓名");
        		sheet.addCell(label);
        		label = new Label(1,0,"学院");
        		sheet.addCell(label);
        		label = new Label(2,0,"专业");
        		sheet.addCell(label);
        		label = new Label(3,0,"学号");
        		sheet.addCell(label);
        		label = new Label(4,0,"身份证号");
        		sheet.addCell(label);
        		label = new Label(5,0,"语种");
        		sheet.addCell(label);
        		label = new Label(6,0,"班级");
        		sheet.addCell(label);
        		label = new Label(7,0,"准考证号");
        		sheet.addCell(label);
        		label = new Label(8,0,"校区名称");
        		sheet.addCell(label);
        		label = new Label(9,0,"考场号");
        		sheet.addCell(label);
        		label = new Label(10,0,"理论考试地址");
        		sheet.addCell(label);
        		label = new Label(11,0,"理论考试开始时间");
        		sheet.addCell(label);
        		label = new Label(12,0,"理论考试结束时间");
        		sheet.addCell(label);
        		label = new Label(13,0,"上机考试地址");
        		sheet.addCell(label);
        		label = new Label(14,0,"上机考试开始时间");
        		sheet.addCell(label);
        		label = new Label(15,0,"上机考试结束时间");
        		sheet.addCell(label);
        		label = new Label(16,0,"上机座位号");
        		sheet.addCell(label);

        		List<Map> students = studentservice.getStudentAdmissionExcel(request.getSession().getAttribute("institution").toString());
                for(int i = 0; i < students.size(); i++){
                	label = new Label(0,i+1,students.get(i).get("name").toString());
                	sheet.addCell(label);
                	label = new Label(1,i+1,students.get(i).get("exCollege").toString());
                	sheet.addCell(label);
                	label = new Label(2,i+1,students.get(i).get("exProfession").toString());
                	sheet.addCell(label);
                	label = new Label(3,i+1,students.get(i).get("studentnum").toString());
                	sheet.addCell(label);
                	label = new Label(4,i+1,students.get(i).get("idnum").toString());
                	sheet.addCell(label);
                	label = new Label(5,i+1,students.get(i).get("exLanguage").toString());
                	sheet.addCell(label);
                	label = new Label(6,i+1,students.get(i).get("classnum").toString());
                	sheet.addCell(label);
                	label = new Label(7,i+1,students.get(i).get("examnum").toString());
                	sheet.addCell(label);
                	label = new Label(8,i+1,students.get(i).get("exCampus").toString());
                	sheet.addCell(label);
                	label = new Label(9,i+1,students.get(i).get("logicExamroomNum").toString());
                	sheet.addCell(label);
                	label = new Label(10,i+1,students.get(i).get("theoryroomlocation").toString());
                	sheet.addCell(label);
                	label = new Label(11,i+1,students.get(i).get("theorystarttime").toString());
                	sheet.addCell(label);
                	label = new Label(12,i+1,students.get(i).get("theoryendTime").toString());
                	sheet.addCell(label);
                	label = new Label(13,i+1,students.get(i).get("operateroomlocation").toString());
                	sheet.addCell(label);
                	label = new Label(14,i+1,students.get(i).get("operatestarttime").toString());
                	sheet.addCell(label);
                	label = new Label(15,i+1,students.get(i).get("operateendTime").toString());
                	sheet.addCell(label);
                	label = new Label(16,i+1,students.get(i).get("operateseat").toString());
                	sheet.addCell(label);
              
                }

        } catch(Exception ex) {
                ex.printStackTrace();
                System.out.println("写入Excel文件发生错误！！！");
               
        }finally{
            wwb.write();
            wwb.close();
            os.flush();
            os.close();
        }
        return null;
	} 
	
	
	
	
	@RequestMapping(value = "exportYXBMTJBExcel", method = RequestMethod.POST)
	public ModelAndView exportYXBMTJBExcel(HttpServletRequest request,HttpServletResponse response) throws Exception{
		 response.reset(); 
        OutputStream os = response.getOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(os);
		String filename = "YXBMTJB" + request.getSession().getAttribute("institution").toString();
        try {
                   
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");        //改成输出excel文件
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type","application/force-download");
            response.setHeader("Content-disposition","attachment;filename="+filename+".xls");
            WritableSheet sheet  = wwb.createSheet("院系报名统计表", 0);
            sheet.setColumnView(0, 25);
            sheet.setColumnView(1, 25);
            WritableFont blodFont = new WritableFont(WritableFont.TAHOMA,10,WritableFont.BOLD, false);
            WritableCellFormat blodFormat = new WritableCellFormat (blodFont);
            Label label = null;
        		label = new Label(0,0,"院系名称");
        		sheet.addCell(label);
        		label = new Label(1,0,"11");
        		sheet.addCell(label);
        		label = new Label(2,0,"12");
        		sheet.addCell(label);
        		label = new Label(3,0,"21");
        		sheet.addCell(label);       		
        		label = new Label(4,0,"22");
        		sheet.addCell(label);
        		label = new Label(5,0,"23");
        		sheet.addCell(label);
        		label = new Label(6,0,"24");
        		sheet.addCell(label); 		
        		label = new Label(7,0,"25");
        		sheet.addCell(label);
        		label = new Label(8,0,"26");
        		sheet.addCell(label);
        		label = new Label(9,0,"31");
        		sheet.addCell(label);		
        		label = new Label(10,0,"32");
        		sheet.addCell(label);
        		label = new Label(11,0,"33");
        		sheet.addCell(label);
        		label = new Label(12,0,"34");
        		sheet.addCell(label);
        		label = new Label(13,0,"35");
        		sheet.addCell(label);
        		label = new Label(14,0,"合计");
        		sheet.addCell(label);
        		
        		
                //List<Map> absentstudents = studentservice.getAllAbsentStudents(request.getSession().getAttribute("institution").toString());
              
        		List<Map> signupInfo=studentservice.getCollegeSignUpInfo(request.getSession().getAttribute("institution").toString());
        		       		      		
        		for(int i = 0; i < signupInfo.size(); i++){
                	label = new Label(0,i+1,signupInfo.get(i).get("collegename").toString());
                	sheet.addCell(label);
                	label = new Label(1,i+1,signupInfo.get(i).get("11studentcount").toString());
                	sheet.addCell(label);  	
                	label = new Label(2,i+1,signupInfo.get(i).get("12studentcount").toString());
                	sheet.addCell(label);
                	label = new Label(3,i+1,signupInfo.get(i).get("21studentcount").toString());
                	sheet.addCell(label);    	
                	label = new Label(4,i+1,signupInfo.get(i).get("22studentcount").toString());
                	sheet.addCell(label);  	
                	label = new Label(5,i+1,signupInfo.get(i).get("23studentcount").toString());
                	sheet.addCell(label);
                	label = new Label(6,i+1,signupInfo.get(i).get("24studentcount").toString());
                	sheet.addCell(label);                	               	
                	label = new Label(7,i+1,signupInfo.get(i).get("25studentcount").toString());
                	sheet.addCell(label);    	
                	label = new Label(8,i+1,signupInfo.get(i).get("26studentcount").toString());
                	sheet.addCell(label);  	             	             	
                	label = new Label(9,i+1,signupInfo.get(i).get("31studentcount").toString());
                	sheet.addCell(label);    	
                	label = new Label(10,i+1,signupInfo.get(i).get("32studentcount").toString());
                	sheet.addCell(label);  	
                	label = new Label(11,i+1,signupInfo.get(i).get("33studentcount").toString());
                	sheet.addCell(label);
                	label = new Label(12,i+1,signupInfo.get(i).get("34studentcount").toString());
                	sheet.addCell(label);            	
                	label = new Label(13,i+1,signupInfo.get(i).get("35studentcount").toString());
                	sheet.addCell(label);
                	label = new Label(14,i+1,signupInfo.get(i).get("sumstudentcount").toString());
                	sheet.addCell(label);               	
                	
                }

        } catch(Exception ex) {
                ex.printStackTrace();
                System.out.println("写出Excel文件发生错误！！！");
        }finally{
            wwb.write();
            wwb.close();
            os.flush();
            os.close();
        }
        
        return null;
	} 
	
	
	
	
	@RequestMapping(value = "exportNJBMTJBExcel", method = RequestMethod.POST)
	public ModelAndView exportNJBMTJBExcel(HttpServletRequest request,HttpServletResponse response) throws Exception{
		 response.reset(); 
        OutputStream os = response.getOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(os);
		String filename = "NJBMTJB" + request.getSession().getAttribute("institution").toString();
        try {
                   
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");        //改成输出excel文件
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type","application/force-download");
            response.setHeader("Content-disposition","attachment;filename="+filename+".xls");
            WritableSheet sheet  = wwb.createSheet("年级报名统计表", 0);
            sheet.setColumnView(0, 25);
            sheet.setColumnView(1, 25);
            WritableFont blodFont = new WritableFont(WritableFont.TAHOMA,10,WritableFont.BOLD, false);
            WritableCellFormat blodFormat = new WritableCellFormat (blodFont);
            Label label = null;
        		label = new Label(0,0,"年级");
        		sheet.addCell(label);
        		label = new Label(1,0,"11");
        		sheet.addCell(label);
        		label = new Label(2,0,"12");
        		sheet.addCell(label);
        		label = new Label(3,0,"21");
        		sheet.addCell(label);       		
        		label = new Label(4,0,"22");
        		sheet.addCell(label);
        		label = new Label(5,0,"23");
        		sheet.addCell(label);
        		label = new Label(6,0,"24");
        		sheet.addCell(label); 		
        		label = new Label(7,0,"25");
        		sheet.addCell(label);
        		label = new Label(8,0,"26");
        		sheet.addCell(label);
        		label = new Label(9,0,"31");
        		sheet.addCell(label);		
        		label = new Label(10,0,"32");
        		sheet.addCell(label);
        		label = new Label(11,0,"33");
        		sheet.addCell(label);
        		label = new Label(12,0,"34");
        		sheet.addCell(label);
        		label = new Label(13,0,"35");
        		sheet.addCell(label);
        		label = new Label(14,0,"合计");
        		sheet.addCell(label);
        		
        		
                //List<Map> absentstudents = studentservice.getAllAbsentStudents(request.getSession().getAttribute("institution").toString());
              
        		List<Map> signupInfo=studentservice.getGradeSignUpInfo(request.getSession().getAttribute("institution").toString());
        		       		      		
        		for(int i = 0; i < signupInfo.size(); i++){
                	label = new Label(0,i+1,signupInfo.get(i).get("grade").toString());
                	sheet.addCell(label);
                	label = new Label(1,i+1,signupInfo.get(i).get("11studentcount").toString());
                	sheet.addCell(label);  	
                	label = new Label(2,i+1,signupInfo.get(i).get("12studentcount").toString());
                	sheet.addCell(label);
                	label = new Label(3,i+1,signupInfo.get(i).get("21studentcount").toString());
                	sheet.addCell(label);    	
                	label = new Label(4,i+1,signupInfo.get(i).get("22studentcount").toString());
                	sheet.addCell(label);  	
                	label = new Label(5,i+1,signupInfo.get(i).get("23studentcount").toString());
                	sheet.addCell(label);
                	label = new Label(6,i+1,signupInfo.get(i).get("24studentcount").toString());
                	sheet.addCell(label);                	               	
                	label = new Label(7,i+1,signupInfo.get(i).get("25studentcount").toString());
                	sheet.addCell(label);    	
                	label = new Label(8,i+1,signupInfo.get(i).get("26studentcount").toString());
                	sheet.addCell(label);  	             	             	
                	label = new Label(9,i+1,signupInfo.get(i).get("31studentcount").toString());
                	sheet.addCell(label);    	
                	label = new Label(10,i+1,signupInfo.get(i).get("32studentcount").toString());
                	sheet.addCell(label);  	
                	label = new Label(11,i+1,signupInfo.get(i).get("33studentcount").toString());
                	sheet.addCell(label);
                	label = new Label(12,i+1,signupInfo.get(i).get("34studentcount").toString());
                	sheet.addCell(label);            	
                	label = new Label(13,i+1,signupInfo.get(i).get("35studentcount").toString());
                	sheet.addCell(label);
                	label = new Label(14,i+1,signupInfo.get(i).get("sumstudentcount").toString());
                	sheet.addCell(label);               	
                	
                }

        } catch(Exception ex) {
                ex.printStackTrace();
                System.out.println("写出Excel文件发生错误！！！");
        }finally{
            wwb.write();
            wwb.close();
            os.flush();
            os.close();
        }
        
        return null;
	} 
	
	
	
	
	@RequestMapping(value = "exportYXCJTJBExcel", method = RequestMethod.POST)
	public ModelAndView exportYXCJTJBExcel(HttpServletRequest request,HttpServletResponse response) throws Exception{
		 response.reset(); 
		 		
		
        OutputStream os = response.getOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(os);
		String filename = "YXCJTJB" + request.getSession().getAttribute("institution").toString();
        try {
                   
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");        //改成输出excel文件
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type","application/force-download");
            response.setHeader("Content-disposition","attachment;filename="+filename+".xls");
            WritableSheet sheet  = wwb.createSheet("院系成绩统计表", 0);
            sheet.setColumnView(0, 25);
            sheet.setColumnView(1, 25);
            WritableFont blodFont = new WritableFont(WritableFont.TAHOMA,10,WritableFont.BOLD, false);
            WritableCellFormat blodFormat = new WritableCellFormat (blodFont);
            Label label = null;
        		label = new Label(0,0,"院系名称");
        		sheet.addCell(label);
        		label = new Label(1,0,"不及格人数");
        		sheet.addCell(label);
        		label = new Label(2,0,"及格人数");
        		sheet.addCell(label);
        		label = new Label(3,0,"及格率");
        		sheet.addCell(label);       		
        		label = new Label(4,0,"优秀人数");
        		sheet.addCell(label);
        		label = new Label(5,0,"优秀率");
        		sheet.addCell(label);
        		label = new Label(6,0,"总人数");
        		sheet.addCell(label); 		
        		
        		HashMap sCondition=new HashMap();
        		sCondition.put("language", request.getSession().getAttribute("language").toString());
        		sCondition.put("passLine", request.getSession().getAttribute("passLine").toString());
        		sCondition.put("excellentLine", request.getSession().getAttribute("excellentLine").toString());
       			      		 
                //List<Map> absentstudents = studentservice.getAllAbsentStudents(request.getSession().getAttribute("institution").toString());
        		List<Map> signupInfo=studentservice.summaryScore(sCondition, request.getSession().getAttribute("institution").toString());
        	      		      		
        		for(int i = 0; i < signupInfo.size(); i++){
                	label = new Label(0,i+1,signupInfo.get(i).get("collegename").toString());
                	sheet.addCell(label);
                	label = new Label(1,i+1,signupInfo.get(i).get("UnPassStudentCount").toString());
                	sheet.addCell(label);  	
                	label = new Label(2,i+1,signupInfo.get(i).get("PassStudentCount").toString());
                	sheet.addCell(label);
                	label = new Label(3,i+1,signupInfo.get(i).get("PassStudentPercent").toString());
                	sheet.addCell(label);    	
                	label = new Label(4,i+1,signupInfo.get(i).get("ExcellentStudentCount").toString());
                	sheet.addCell(label);  	
                	label = new Label(5,i+1,signupInfo.get(i).get("ExcellentStudentPercent").toString());
                	sheet.addCell(label);
                	label = new Label(6,i+1,signupInfo.get(i).get("StudentCount").toString());
                	sheet.addCell(label);                	               	             	
                	
                }
        		
        } catch(Exception ex) {
                ex.printStackTrace();              
                System.out.println("写出Excel文件发生错误！！！");
        }finally{
            wwb.write();
            wwb.close();
            os.flush();
            os.close();
        }
        
        return null;
	} 
	
	
	@RequestMapping(value = "exportNJCJTJBExcel", method = RequestMethod.POST)
	public ModelAndView exportNJCJTJBExcel(HttpServletRequest request,HttpServletResponse response) throws Exception{
		 response.reset(); 
		 		
		
        OutputStream os = response.getOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(os);
		String filename = "NJCJTJB" + request.getSession().getAttribute("institution").toString();
        try {
                   
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");        //改成输出excel文件
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type","application/force-download");
            response.setHeader("Content-disposition","attachment;filename="+filename+".xls");
            WritableSheet sheet  = wwb.createSheet("年级成绩统计表", 0);
            sheet.setColumnView(0, 25);
            sheet.setColumnView(1, 25);
            WritableFont blodFont = new WritableFont(WritableFont.TAHOMA,10,WritableFont.BOLD, false);
            WritableCellFormat blodFormat = new WritableCellFormat (blodFont);
            Label label = null;
        		label = new Label(0,0,"年级");
        		sheet.addCell(label);
        		label = new Label(1,0,"不及格人数");
        		sheet.addCell(label);
        		label = new Label(2,0,"及格人数");
        		sheet.addCell(label);
        		label = new Label(3,0,"及格率");
        		sheet.addCell(label);       		
        		label = new Label(4,0,"优秀人数");
        		sheet.addCell(label);
        		label = new Label(5,0,"优秀率");
        		sheet.addCell(label);
        		label = new Label(6,0,"总人数");
        		sheet.addCell(label); 		
        		
        		HashMap sCondition=new HashMap();
        		sCondition.put("language", request.getSession().getAttribute("language").toString());
        		sCondition.put("passLine", request.getSession().getAttribute("passLine").toString());
        		sCondition.put("excellentLine", request.getSession().getAttribute("excellentLine").toString());
       			      		 
                //List<Map> absentstudents = studentservice.getAllAbsentStudents(request.getSession().getAttribute("institution").toString());
        		List<Map> signupInfo=studentservice.summaryScoreByGrade(sCondition, request.getSession().getAttribute("institution").toString());
        	      		      		
        		for(int i = 0; i < signupInfo.size(); i++){
                	label = new Label(0,i+1,signupInfo.get(i).get("grade").toString());
                	sheet.addCell(label);
                	label = new Label(1,i+1,signupInfo.get(i).get("UnPassStudentCount").toString());
                	sheet.addCell(label);  	
                	label = new Label(2,i+1,signupInfo.get(i).get("PassStudentCount").toString());
                	sheet.addCell(label);
                	label = new Label(3,i+1,signupInfo.get(i).get("PassStudentPercent").toString());
                	sheet.addCell(label);    	
                	label = new Label(4,i+1,signupInfo.get(i).get("ExcellentStudentCount").toString());
                	sheet.addCell(label);  	
                	label = new Label(5,i+1,signupInfo.get(i).get("ExcellentStudentPercent").toString());
                	sheet.addCell(label);
                	label = new Label(6,i+1,signupInfo.get(i).get("StudentCount").toString());
                	sheet.addCell(label);                	               	             	
                	
                }
        		
        } catch(Exception ex) {
                ex.printStackTrace();              
                System.out.println("写出Excel文件发生错误！！！");
        }finally{
            wwb.write();
            wwb.close();
            os.flush();
            os.close();
        }
        
        return null;
	} 
	
	
	@RequestMapping(value = "exportYXNJCJTJBExcel", method = RequestMethod.POST)
	public ModelAndView exportYXNJCJTJBExcel(HttpServletRequest request,HttpServletResponse response) throws Exception{
		 response.reset(); 
		 		
		
        OutputStream os = response.getOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(os);
		String filename = "YXNJCJTJB" + request.getSession().getAttribute("institution").toString();
        try {
                   
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");        //改成输出excel文件
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type","application/force-download");
            response.setHeader("Content-disposition","attachment;filename="+filename+".xls");
            WritableSheet sheet  = wwb.createSheet("按院系各年级成绩统计表", 0);
            sheet.setColumnView(0, 25);
            sheet.setColumnView(1, 25);
            WritableFont blodFont = new WritableFont(WritableFont.TAHOMA,10,WritableFont.BOLD, false);
            WritableCellFormat blodFormat = new WritableCellFormat (blodFont);
            Label label = null;
        		label = new Label(0,0,"年级");
        		sheet.addCell(label);
        		label = new Label(1,0,"不及格人数");
        		sheet.addCell(label);
        		label = new Label(2,0,"及格人数");
        		sheet.addCell(label);
        		label = new Label(3,0,"及格率");
        		sheet.addCell(label);       		
        		label = new Label(4,0,"优秀人数");
        		sheet.addCell(label);
        		label = new Label(5,0,"优秀率");
        		sheet.addCell(label);
        		label = new Label(6,0,"总人数");
        		sheet.addCell(label); 		
        		
        		HashMap sCondition=new HashMap();
        		sCondition.put("value", request.getSession().getAttribute("college").toString());
        		sCondition.put("language", request.getSession().getAttribute("language").toString());
        		sCondition.put("passLine", request.getSession().getAttribute("passLine").toString());
        		sCondition.put("excellentLine", request.getSession().getAttribute("excellentLine").toString());
       			      		 
                //List<Map> absentstudents = studentservice.getAllAbsentStudents(request.getSession().getAttribute("institution").toString());
        		List<Map> signupInfo=studentservice.summaryScoreByCollege_Grade(sCondition, request.getSession().getAttribute("institution").toString());
        	      		      		
        		for(int i = 0; i < signupInfo.size(); i++){
                	label = new Label(0,i+1,signupInfo.get(i).get("grade").toString());
                	sheet.addCell(label);
                	label = new Label(1,i+1,signupInfo.get(i).get("UnPassStudentCount").toString());
                	sheet.addCell(label);  	
                	label = new Label(2,i+1,signupInfo.get(i).get("PassStudentCount").toString());
                	sheet.addCell(label);
                	label = new Label(3,i+1,signupInfo.get(i).get("PassStudentPercent").toString());
                	sheet.addCell(label);    	
                	label = new Label(4,i+1,signupInfo.get(i).get("ExcellentStudentCount").toString());
                	sheet.addCell(label);  	
                	label = new Label(5,i+1,signupInfo.get(i).get("ExcellentStudentPercent").toString());
                	sheet.addCell(label);
                	label = new Label(6,i+1,signupInfo.get(i).get("StudentCount").toString());
                	sheet.addCell(label);                	               	             	
                	
                }
        		
        } catch(Exception ex) {
                ex.printStackTrace();              
                System.out.println("写出Excel文件发生错误！！！");
        }finally{
            wwb.write();
            wwb.close();
            os.flush();
            os.close();
        }
        
        return null;
	} 
	
	
	
	
	@RequestMapping(value = "exportNJYXCJTJBExcel", method = RequestMethod.POST)
	public ModelAndView exportNJYXCJTJBExcel(HttpServletRequest request,HttpServletResponse response) throws Exception{
		 response.reset(); 
		 		
		
        OutputStream os = response.getOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(os);
		String filename = "NJYXCJTJB" + request.getSession().getAttribute("institution").toString();
        try {
                   
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");        //改成输出excel文件
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type","application/force-download");
            response.setHeader("Content-disposition","attachment;filename="+filename+".xls");
            WritableSheet sheet  = wwb.createSheet("按年级各院系成绩统计表", 0);
            sheet.setColumnView(0, 25);
            sheet.setColumnView(1, 25);
            WritableFont blodFont = new WritableFont(WritableFont.TAHOMA,10,WritableFont.BOLD, false);
            WritableCellFormat blodFormat = new WritableCellFormat (blodFont);
            Label label = null;
        		label = new Label(0,0,"院系名称");
        		sheet.addCell(label);
        		label = new Label(1,0,"不及格人数");
        		sheet.addCell(label);
        		label = new Label(2,0,"及格人数");
        		sheet.addCell(label);
        		label = new Label(3,0,"及格率");
        		sheet.addCell(label);       		
        		label = new Label(4,0,"优秀人数");
        		sheet.addCell(label);
        		label = new Label(5,0,"优秀率");
        		sheet.addCell(label);
        		label = new Label(6,0,"总人数");
        		sheet.addCell(label); 		
        		
        		HashMap sCondition=new HashMap();
        		sCondition.put("grade", request.getSession().getAttribute("grade").toString());
        		sCondition.put("language", request.getSession().getAttribute("language").toString());
        		sCondition.put("passLine", request.getSession().getAttribute("passLine").toString());
        		sCondition.put("excellentLine", request.getSession().getAttribute("excellentLine").toString());
       			      		 
                //List<Map> absentstudents = studentservice.getAllAbsentStudents(request.getSession().getAttribute("institution").toString());
        		List<Map> signupInfo=studentservice.summaryScoreByGrade_College(sCondition, request.getSession().getAttribute("institution").toString());
        	      		      		
        		for(int i = 0; i < signupInfo.size(); i++){
                	label = new Label(0,i+1,signupInfo.get(i).get("collegename").toString());
                	sheet.addCell(label);
                	label = new Label(1,i+1,signupInfo.get(i).get("UnPassStudentCount").toString());
                	sheet.addCell(label);  	
                	label = new Label(2,i+1,signupInfo.get(i).get("PassStudentCount").toString());
                	sheet.addCell(label);
                	label = new Label(3,i+1,signupInfo.get(i).get("PassStudentPercent").toString());
                	sheet.addCell(label);    	
                	label = new Label(4,i+1,signupInfo.get(i).get("ExcellentStudentCount").toString());
                	sheet.addCell(label);  	
                	label = new Label(5,i+1,signupInfo.get(i).get("ExcellentStudentPercent").toString());
                	sheet.addCell(label);
                	label = new Label(6,i+1,signupInfo.get(i).get("StudentCount").toString());
                	sheet.addCell(label);                	               	             	
                	
                }
        		
        } catch(Exception ex) {
                ex.printStackTrace();              
                System.out.println("写出Excel文件发生错误！！！");
        }finally{
            wwb.write();
            wwb.close();
            os.flush();
            os.close();
        }
        
        return null;
	} 
	
	
	//导出院系报名考生统计表
	@RequestMapping(value = "exportYXBMTJB", method = RequestMethod.POST)
	public ModelAndView exportYXBMTJB(HttpServletRequest request,HttpServletResponse response) throws Exception{
        response.reset(); 
        OutputStream os = response.getOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(os);
		DateFormat   date   =   new  SimpleDateFormat( "yyyy-MM-dd"); 
		String institutionnum = request.getSession().getAttribute("institution").toString();
		String filename =   "YXBM" + date.format(new Date()).toString();
        try {
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");        //改成输出excel文件
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type","application/force-download");
            response.setHeader("Content-disposition","attachment;filename="+filename+".xls");
            WritableSheet sheet  = wwb.createSheet("院系报名统计表", 0);
            sheet.setColumnView(1, 25);

            WritableFont blodFont = new WritableFont(WritableFont.TAHOMA,10,WritableFont.BOLD, false);
            WritableCellFormat blodFormat = new WritableCellFormat (blodFont);
//            String category =  institutionService.getInstitutionByInstitutionNum(institutionnum).getCategory();
            List<Map> languageList = languageService.loadlanguageList();
            List<Map> stuCount = studentservice.calcStuForCollege(institutionnum);
            Label label = null;
        		label = new Label(0,0,"院系名称");
        		sheet.addCell(label);
        		int m = 1;
        		for(Map language : languageList){
	        			label = new Label(m,0,language.get("languagenum").toString());
	            		sheet.addCell(label);
	            		m++;

      		}

                for(int i = 0; i < stuCount.size(); i++){

                	label = new Label(0,i+1,stuCount.get(i).get("collegename").toString());
                	sheet.addCell(label);
                	String lauguagenum ;
                	for(int j = 1 ; j<m ;j++){
                		lauguagenum = sheet.getCell(j, 0).getContents().toString();
                //		if(lauguagenum.substring(2, 4).equals("标准")){
                			lauguagenum = sheet.getCell(j, 0).getContents().toString().substring(0, 2) + "count";
                		//}
                    	label = new Label(j,i+1,stuCount.get(i).get(lauguagenum).toString());
                    	sheet.addCell(label);
                	}
                }

        } catch(Exception ex) {
                ex.printStackTrace();
                System.out.println("写入Excel文件发生错误！！！");
               
        }finally{
            wwb.write();
            wwb.close();
            os.flush();
            os.close();
        }
        return null;
	}
	
	
	
	@RequestMapping(value = "exportNJBMTJB", method = RequestMethod.POST)
	public ModelAndView exportNJBMTJB(HttpServletRequest request,HttpServletResponse response) throws Exception{
        response.reset(); 
        OutputStream os = response.getOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(os);
		DateFormat   date   =   new  SimpleDateFormat( "yyyy-MM-dd"); 
		String institutionnum = request.getSession().getAttribute("institution").toString();
		String filename =   "NJBM" + date.format(new Date()).toString();
        try {
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");        //改成输出excel文件
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type","application/force-download");
            response.setHeader("Content-disposition","attachment;filename="+filename+".xls");
            WritableSheet sheet  = wwb.createSheet("年级报名统计表", 0);
            sheet.setColumnView(1, 25);

            WritableFont blodFont = new WritableFont(WritableFont.TAHOMA,10,WritableFont.BOLD, false);
            WritableCellFormat blodFormat = new WritableCellFormat (blodFont);
            List<Map> languageList = languageService.loadlanguageList();
            List<Map> stuCount = studentservice.calcStuForGrade(institutionnum);
            Label label = null;
        		label = new Label(0,0,"年级");
        		sheet.addCell(label);
        		int m = 1;
        		for(Map language : languageList){
	        			label = new Label(m,0,language.get("languagenum").toString());
	            		sheet.addCell(label);
	            		m++;

      		}
	
                for(int i = 0; i < stuCount.size(); i++){
                	label = new Label(0,i+1,stuCount.get(i).get("gradename").toString());
                	sheet.addCell(label);
                	String lauguagenum ;
                	for(int j = 1 ; j<m ;j++){
                		lauguagenum = sheet.getCell(j, 0).getContents().toString();
                //		if(lauguagenum.substring(2, 4).equals("标准")){
                			lauguagenum = sheet.getCell(j, 0).getContents().toString().substring(0, 2) + "count";
                		//}
                    	label = new Label(j,i+1,stuCount.get(i).get(lauguagenum).toString());
                    	sheet.addCell(label);
                	}
                }

        } catch(Exception ex) {
                ex.printStackTrace();
                System.out.println("写入Excel文件发生错误！！！");
               
        }finally{
            wwb.write();
            wwb.close();
            os.flush();
            os.close();
        }
        return null;
	}
	
}
