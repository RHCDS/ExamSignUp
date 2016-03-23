package cn.hdu.examsignup.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.directwebremoting.WebContextFactory;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ImportStudentPhotoController {
	
	private Logger logger= LoggerFactory.getLogger(ExcelUtilController.class);
	
	@RequestMapping(value = "importStudentPhoto", method = RequestMethod.POST)
	public ModelAndView upLoadStudentPhoto(HttpServletRequest request,HttpServletResponse response) throws Exception {
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/html;charset=UTF-8");
				
				Writer out = response.getWriter();
				HttpSession curSession = request.getSession();
				
				InputStream photoInputStream=null;
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
				CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("importphoto");
				String filePath = curSession.getServletContext().getRealPath( "/");
				String institutionnum = (String) curSession.getAttribute("institution");
				String studentnum = (String) curSession.getAttribute("studentID");
				filePath += "" + institutionnum + "\\pictures";
				System.out.println(filePath);
				filePath = filePath.replace('\\', '/');
				File newphotofile = new File(filePath);
				if(!(newphotofile.exists())){
					newphotofile.mkdirs();
				}else
					
					System.out.println("newphotofile is exists!");
				try{
					logger.debug("上传的文件为："+file.getOriginalFilename());
					photoInputStream  = file.getInputStream();
					if(photoInputStream.available()>200*1024){
						out.write("{ success: false, errors:{info: '照片容量不能超过200KB！'}}");
						return null;
					}
					String realFileName = file.getOriginalFilename();
					if(realFileName.endsWith(".jpg")){
						realFileName = studentnum + ".jpg";
						File uploadFile = new File(filePath + "/" + realFileName);
						FileCopyUtils.copy(file.getBytes(), uploadFile);
						out.write("{ success: true, errors:{info: '验证成功！'}, fileName: '" + realFileName + "'}");
					}else
						out.write("{ success: false, errors:{info: '照片格式不正确！'}}");
				}catch(Exception e){
					e.printStackTrace();
					out.write("{ success: false, errors:{info: '上传失败！'}}");
				}finally{
					try{
						photoInputStream.close();
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				return null;
	}
	
	@RequestMapping(value = "importStudentsPhoto", method = RequestMethod.POST)
	public ModelAndView upLoadStudentsPhoto(HttpServletRequest request,HttpServletResponse response) throws Exception {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		Writer out = response.getWriter();
		HttpSession curSession = request.getSession();
		
		InputStream photoInputStream=null;
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("Filedata");
		String   filePath   =   curSession.getServletContext().getRealPath( "/");
		String institutionnum = (String) curSession.getAttribute("institution");
		filePath += "" + institutionnum + "\\pictures";
		System.out.println(filePath);
		filePath = filePath.replace('\\', '/');
		File newphotofile = new File(filePath);
		if(!(newphotofile.exists())){
			newphotofile.mkdirs();
		}else
			
			System.out.println("newphotofile is exists!");
		try{
			logger.debug("上传的文件为："+file.getOriginalFilename());
			photoInputStream  = file.getInputStream();
			String realFileName = file.getOriginalFilename();
			if(realFileName.endsWith(".jpg")){
				File uploadFile = new File(filePath + "/" + realFileName);
				FileCopyUtils.copy(file.getBytes(), uploadFile);
				out.write("{ success: true, errors:{info: '验证成功！'}, fileName: '" + realFileName + "'}");
			}else
				out.write("{ success: false, errors:{info: '照片格式不正确！'}}");
		}catch(Exception e){
			e.printStackTrace();
			out.write("{ success: false, errors:{info: '上传失败！'}}");
		}finally{
			try{
				photoInputStream.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return null;
}
	
}
