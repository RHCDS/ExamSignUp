package cn.hdu.examsignup.controller;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;

import cn.hdu.examsignup.service.ExcelUtilService;
import cn.hdu.examsignup.service.StudentService;

@Controller
public class ExcelUtilController {
	
	private Logger logger= LoggerFactory.getLogger(ExcelUtilController.class);
	@Autowired
	private ExcelUtilService excelUtilService;
	
	@Autowired
	private StudentService studentservice;
	
	/**
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 
	 * 高校端教务上传已经完成报名考生信息数据
	 * 
	 */
	@RequestMapping(value = "xlsToJson", method = RequestMethod.POST)
	public ModelAndView upLoadSingleFile(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		Writer out = response.getWriter();
		HttpSession curSession = request.getSession();
		//判断是否上传了文件
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("file");
		if(file.getSize() == 0) {
			out.write("{success: false, errors:{info: '未选择文件！'}}");
			return null;
		}
		
		InputStream sampleInputStream=null;
		InputStream fromInputStream=null;
		InputStream checksampleInputStream=null;
		InputStream checkfromInputStream=null;
		JSONObject result=null;
		List<JSONObject> students = null;
		
		String schoolnum = curSession.getAttribute("institution").toString();
		try{
			logger.debug("上传的文件为："+file.getOriginalFilename());
			logger.debug(request.getSession().getServletContext().getRealPath("/")+"sample/批量导入学生报名信息sample.xls");
			sampleInputStream=new FileInputStream(request.getSession().getServletContext().getRealPath("/")+"sample/批量导入学生报名信息sample.xls");
			fromInputStream  = file.getInputStream();
			checksampleInputStream=new FileInputStream(request.getSession().getServletContext().getRealPath("/")+"sample/批量导入学生报名信息sample.xls");
			checkfromInputStream  = file.getInputStream();
			//检查excel列
			String checkInfo = excelUtilService.checkExcelHeader(checkfromInputStream, checksampleInputStream);
			if(checkInfo.equals("passcheck")){
				logger.debug("上传的文件 "+file.getOriginalFilename() + "now passcheck start inport!");
				result = excelUtilService.convertstudentExcelToJson(fromInputStream, sampleInputStream);
				logger.debug("11111");
				students = (List<JSONObject>)result.get("excelArray");
				logger.debug("22222");
				if(studentservice.checkStudentsData(students)==null){
					logger.debug("33333");
					studentservice.importStudents(students, schoolnum);
					out.write("{ success: true, errors:{info: '上传成功！'}}");
				}else{
					out.write("{ success: false, errors:{info: '" + studentservice.checkStudentsData(students) +"！'}}");
				};
			}else{
				out.write(checkInfo);
			}
			System.out.println(checkInfo);
//			logger.debug(result.toString());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				sampleInputStream.close();
				fromInputStream.close();
				checksampleInputStream.close();
				checkfromInputStream.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return null;
	}
	
	@RequestMapping(value = "xlsUnSignToJson", method = RequestMethod.POST)
	public ModelAndView upLoadUnSingleFile(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		Writer out = response.getWriter();
		HttpSession curSession = request.getSession();
		
		InputStream sampleInputStream=null;
		InputStream fromInputStream=null;
		InputStream checksampleInputStream=null;
		InputStream checkfromInputStream=null;
		JSONObject result=null;
		List<JSONObject> students = null;
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("file");
		String schoolnum = curSession.getAttribute("institution").toString();
		try{
			logger.debug("上传的文件为："+file.getOriginalFilename());
			logger.debug(request.getSession().getServletContext().getRealPath("/")+"sample/批量导入学生报名信息sample.xls");
			sampleInputStream=new FileInputStream(request.getSession().getServletContext().getRealPath("/")+"sample/批量导入学生报名信息sample.xls");
			fromInputStream  = file.getInputStream();
			checksampleInputStream=new FileInputStream(request.getSession().getServletContext().getRealPath("/")+"sample/批量导入学生报名信息sample.xls");
			checkfromInputStream  = file.getInputStream();
			String checkInfo = excelUtilService.checkExcelHeader(checkfromInputStream, checksampleInputStream);
			if(checkInfo.equals("passcheck")){
				result=excelUtilService.convertstudentExcelToJson(fromInputStream, sampleInputStream);
				students = (List<JSONObject>)result.get("excelArray");
				if(studentservice.checkUnSignUpStudentsData(students)==null){
					studentservice.importStudents(students, schoolnum);
					out.write("{ success: true, errors:{info: '上传成功！'}}");
				}else{
					out.write("{ success: false, errors:{info: '" + studentservice.checkUnSignUpStudentsData(students) +"！'}}");
				};
			}else{
				out.write(checkInfo);
			}
			System.out.println(checkInfo);
//			logger.debug(result.toString());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				sampleInputStream.close();
				fromInputStream.close();
				checksampleInputStream.close();
				checkfromInputStream.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return null;
	}
	
	@RequestMapping(value = "JFxlsToJson", method = RequestMethod.POST)
	public ModelAndView upLoadJF(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		Writer out = response.getWriter();
		HttpSession curSession = request.getSession();
		
		InputStream sampleInputStream=null;
		InputStream fromInputStream=null;
		JSONObject result=null;
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("file");
		
		try{
			logger.debug("上传的文件为："+file.getOriginalFilename());
			logger.debug(request.getSession().getServletContext().getRealPath("/")+"sample/批量导入已缴费学生sample.xls");
			sampleInputStream=new FileInputStream(request.getSession().getServletContext().getRealPath("/")+"sample/批量导入已缴费学生sample.xls");
			fromInputStream  = file.getInputStream();
			result=excelUtilService.convertExcelToJson(fromInputStream, sampleInputStream);
			logger.debug(result.toString());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				sampleInputStream.close();
				fromInputStream.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		out.write(result.toString());
		return null;
	}
	@RequestMapping(value = "examroomXlsToJson", method = RequestMethod.POST)
	public ModelAndView upLoadExamroomFile(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		Writer out = response.getWriter();
		HttpSession curSession = request.getSession();
		
		InputStream sampleInputStream=null;
		InputStream fromInputStream=null;
		JSONObject result=null;
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("examroomfile");
		
		try{
			logger.debug("上传的文件为："+file.getOriginalFilename());
			logger.debug(request.getSession().getServletContext().getRealPath("/")+"sample/批量导入考场sample.xls");
			sampleInputStream=new FileInputStream(request.getSession().getServletContext().getRealPath("/")+"sample/批量导入考场sample.xls");
			fromInputStream  = file.getInputStream();
			result=excelUtilService.convertExcelToJson(fromInputStream, sampleInputStream);
			logger.debug(result.toString());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				sampleInputStream.close();
				fromInputStream.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		out.write(result.toString());
		return null;
	}
	
	@RequestMapping(value = "supervisorXlsToJson", method = RequestMethod.POST)
	public ModelAndView upLoadSupervisorFile(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		Writer out = response.getWriter();
		HttpSession curSession = request.getSession();
		
		InputStream sampleInputStream=null;
		InputStream fromInputStream=null;
		JSONObject result=null;
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("supervisorfile");
		
		try{
			logger.debug("上传的文件为："+file.getOriginalFilename());
			logger.debug(request.getSession().getServletContext().getRealPath("/")+"sample/批量导入考场sample.xls");
			sampleInputStream=new FileInputStream(request.getSession().getServletContext().getRealPath("/")+"sample/批量导入监考sample.xls");
			fromInputStream  = file.getInputStream();
			result=excelUtilService.convertExcelToJson(fromInputStream, sampleInputStream);
			logger.debug(result.toString());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				sampleInputStream.close();
				fromInputStream.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		out.write(result.toString());
		return null;
	}
	
	@RequestMapping(value = "acceptStudentCJ", method = RequestMethod.POST)
	public ModelAndView acceptStudentCJ(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		Writer out = response.getWriter();
		HttpSession curSession = request.getSession();
		
		InputStream inputStream=null;
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("studentCJfile");
		try{
			logger.debug("上传的文件为："+file.getOriginalFilename());
			inputStream = file.getInputStream();
			DBFReader reader = new DBFReader(inputStream);
			reader.setCharactersetName("GBK");
			int fieldCount = reader.getFieldCount();
			String[] fieldNames = new String[fieldCount];
			for(int i=0;i<fieldCount;i++) {
				fieldNames[i] = reader.getField(i).getName();
			}
		    Object []rowObjects;
		    if(reader.getRecordCount()==0){
		        out.write("{success: false,errors : {info:'文件没有数据！'}}");
	        	return null;
		    }
		    int studentcount = 1;
		    while( (rowObjects = reader.nextRecord()) != null) {
		    	String result = studentservice.acceptStudentCJ(studentcount++,rowObjects,fieldNames);
		    	if(result.equals("SUCCESS")){
		    		continue;
		    	}else{
		    		out.write("{success: false,errors : {info:'"+result+"'}}");
		    		return null;
		    	}
		    }
		    out.write("{success: true,errors : {info:'接收成功！'}}");
		}catch(Exception e){
			e.printStackTrace();
			System.out.println( e.getMessage());
			out.write("{success: false,errors : {info:'接收异常！'}}");
		}finally{
			try{
				 inputStream.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return null;
	}
	
	@RequestMapping(value = "importStuHistoryInfo", method = RequestMethod.POST)
	public ModelAndView importStuHistoryInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		Writer out = response.getWriter();
		HttpSession curSession = request.getSession();
		
		InputStream sampleInputStream=null;
		InputStream fromInputStream=null;
		JSONObject result=null;
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("studenthistoryfile");
		
		try{
			logger.debug("上传的文件为："+file.getOriginalFilename());
			logger.debug(request.getSession().getServletContext().getRealPath("/")+"sample/学生历史数据导入sample.xls");
			sampleInputStream=new FileInputStream(request.getSession().getServletContext().getRealPath("/")+"sample/学生历史数据导入sample.xls");
			fromInputStream  = file.getInputStream();
			result=excelUtilService.convertExcelToJson(fromInputStream, sampleInputStream);
			logger.debug(result.toString());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				sampleInputStream.close();
				fromInputStream.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		out.write(result.toString());
		return null;
	}
	@RequestMapping(value = "importFraud", method = RequestMethod.POST)
	public ModelAndView imortFraud(HttpServletRequest request,
			HttpServletResponse response) throws Exception { 
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		Writer out = response.getWriter();
		HttpSession curSession = request.getSession();
		
		InputStream sampleInputStream=null;
		InputStream fromInputStream=null;
		JSONObject result=null;
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("zbkfile");
		
		try{
			logger.debug("上传的文件为："+file.getOriginalFilename());
			logger.debug(request.getSession().getServletContext().getRealPath("/")+"sample/ZBKsample.xls");
			sampleInputStream=new FileInputStream(request.getSession().getServletContext().getRealPath("/")+"sample/ZBKsample.xls");
			fromInputStream  = file.getInputStream();
			result=excelUtilService.convertExcelToJson(fromInputStream, sampleInputStream);
			logger.debug(result.toString());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				sampleInputStream.close();
				fromInputStream.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		out.write(result.toString());
		return null;
	}
	@RequestMapping(value = "importAbsence", method = RequestMethod.POST)
	public ModelAndView imortAbsence(HttpServletRequest request,
			HttpServletResponse response) throws Exception { 
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		Writer out = response.getWriter();
		HttpSession curSession = request.getSession();
		
		InputStream sampleInputStream=null;
		InputStream fromInputStream=null;
		JSONObject result=null;
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("qkkfile");
		
		try{
			logger.debug("上传的文件为："+file.getOriginalFilename());
			logger.debug(request.getSession().getServletContext().getRealPath("/")+"sample/QKKsample.xls");
			sampleInputStream=new FileInputStream(request.getSession().getServletContext().getRealPath("/")+"sample/QKKsample.xls");
			fromInputStream  = file.getInputStream();
			result=excelUtilService.convertExcelToJson(fromInputStream, sampleInputStream);
			logger.debug(result.toString());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				sampleInputStream.close();
				fromInputStream.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		out.write(result.toString());
		return null;
	}
	//学校上传ksdb到市地考试院
	@RequestMapping(value = "importStudentToKSY", method = RequestMethod.POST)
	public ModelAndView importStudentToKSY(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		Writer out = response.getWriter();
		HttpSession curSession = request.getSession();
		
		InputStream sampleInputStream=null;
		InputStream fromInputStream=null;
		JSONObject result=null;
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("ksdbfile");
		
		try{
			logger.debug("上传的文件为："+file.getOriginalFilename());
			logger.debug(request.getSession().getServletContext().getRealPath("/")+"sample/KSDBsample.xls");
			sampleInputStream=new FileInputStream(request.getSession().getServletContext().getRealPath("/")+"sample/KSDBsample.xls");
			fromInputStream  = file.getInputStream();
			result=excelUtilService.convertExcelToJson(fromInputStream, sampleInputStream);
			logger.debug(result.toString());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				sampleInputStream.close();
				fromInputStream.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		out.write(result.toString());
		return null;
	}
	
	
	@RequestMapping(value = "languageXlsToJson", method = RequestMethod.POST)
	public ModelAndView upLoadLanguageFile(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		Writer out = response.getWriter();
		HttpSession curSession = request.getSession();
		
		InputStream sampleInputStream=null;
		InputStream fromInputStream=null;
		JSONObject result=null;
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("languagefile");
		try{
			logger.debug("上传的文件为："+file.getOriginalFilename());
			logger.debug(request.getSession().getServletContext().getRealPath("/")+"sample/批量导入语种sample.xls");
			sampleInputStream=new FileInputStream(request.getSession().getServletContext().getRealPath("/")+"sample/批量导入语种sample.xls");
			fromInputStream  = file.getInputStream();
			result=excelUtilService.convertExcelToJson(fromInputStream, sampleInputStream);
			logger.debug(result.toString());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				sampleInputStream.close();
				fromInputStream.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		out.write(result.toString());
		return null;
	}
	
	

}
