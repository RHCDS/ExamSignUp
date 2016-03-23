package cn.hdu.examsignup.controller;

import java.io.File;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.directwebremoting.WebContextFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import cn.hdu.examsignup.utility.FileOperateUtil;

/**
* 
* @author geloin
* @date 2012-5-5 上午11:56:35
*/
@Controller
public class FileOperateController {
	/**
	 * 到上传文件的位置
	 * 
	 * @author geloin
	 * @date 2012-3-29 下午4:01:31
	 * @return
	 */
	@RequestMapping(value = "to_upload")
	public ModelAndView toUpload() {
		return new ModelAndView("background/fileOperate/upload");
	}

	/**
	 * 上传文件
	 * 
	 * @author geloin
	 * @date 2012-3-29 下午4:01:41
	 * @param request
	 * @return
	 * @throws Exception
	 */
	
	@RequestMapping(value = "uploadFile", method = RequestMethod.POST)
	public ModelAndView upload(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		Writer out = response.getWriter();
		List<String> result=new ArrayList<String>();
		HttpSession curSession = request.getSession();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		
		String subDir=multipartRequest.getParameter("subdir");
		String institutionnum = (String) curSession.getAttribute("institution");
		if(institutionnum==null || institutionnum.trim().isEmpty()){
			out.write( "{ success: false, errors:{info: '请重新登陆！'}}");
			return null;
    	}
		if(subDir==null)
			return null;
		if(!subDir.trim().isEmpty()){
			subDir=subDir+"/";
		}
		
		File tmpBaseDir = new File(curSession.getServletContext().getRealPath(
				"/")
				+ curSession.getAttribute("institution") + "/" + subDir);
		if (!(tmpBaseDir.exists() && tmpBaseDir.isDirectory())) {
			tmpBaseDir.mkdirs();
		}
		File[] tmpList = tmpBaseDir.listFiles();
		for(File tmp:tmpList) {
			tmp.delete();
		}
		//like 451/pictures/
		if( (result=FileOperateUtil.upload(request, institutionnum+"/"+subDir))==null){
			out.write( "{ success: false, errors:{info: '上传失败'}}");
			return null;
		}else{
			out.write("{success:true,errors:{info:'共上传"+result.size()+"个文件！<br>");
			for(String element:result){
				out.write(element+"<br>");
			}
			out.write("'}}");
		}
		return null;
	}


	@RequestMapping(value = "downloadOwnerFile",method = RequestMethod.POST)
	public ModelAndView downloadOwnerFile(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		RequestDispatcher requestDispatcher = null;
		ServletContext servletContext = request.getServletContext();
		
		String subDir=request.getParameter("subdir");
		String filename=request.getParameter("filename");
		String institutionnum = (String) request.getSession().getAttribute("institution");
		
		if(institutionnum==null || institutionnum.trim().isEmpty()){
			requestDispatcher = servletContext.getRequestDispatcher("/Info.jsp"); // 定向的页面
			((HttpServletRequest) request).setAttribute("msg", "您没有权限访问此页面，或者该页面不存在！");
			requestDispatcher.forward(request, response);
    	}
		if(subDir==null){
			requestDispatcher = servletContext.getRequestDispatcher("/Info.jsp"); // 定向的页面
			((HttpServletRequest) request).setAttribute("msg", "您访问的资源不存在！");
			requestDispatcher.forward(request, response);
		}
		if(!subDir.trim().isEmpty()){
			subDir=subDir+"/";
		}

		if(FileOperateUtil.download(request, response, institutionnum+"/"+subDir,filename)==false){
			requestDispatcher = servletContext.getRequestDispatcher("/Info.jsp"); // 定向的页面
			((HttpServletRequest) request).setAttribute("msg", "您访问的资源不存在！");
			requestDispatcher.forward(request, response);
		}
		return null;
	}
	
	@RequestMapping(value = "downloadFile",method = RequestMethod.POST)
	public ModelAndView downloadFile(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		RequestDispatcher requestDispatcher = null;
		ServletContext servletContext = request.getServletContext();
		
		String subDir=request.getParameter("subdir");
		String filename=request.getParameter("filename");
		String institutionnum = (String) request.getSession().getAttribute("institution");
		
		if(institutionnum==null || institutionnum.trim().isEmpty()){
			requestDispatcher = servletContext.getRequestDispatcher("/Info.jsp"); // 定向的页面
			((HttpServletRequest) request).setAttribute("msg", "您没有权限访问此页面，或者该页面不存在！");
			requestDispatcher.forward(request, response);
			return null;
    	}
		if(subDir==null){
			requestDispatcher = servletContext.getRequestDispatcher("/Info.jsp"); // 定向的页面
			((HttpServletRequest) request).setAttribute("msg", "您访问的资源不存在！");
			requestDispatcher.forward(request, response);
			return null;
		}
		if(!subDir.trim().isEmpty()){
			subDir=subDir+"/";
		}

		if(FileOperateUtil.download(request, response, subDir,filename)==false){
			requestDispatcher = servletContext.getRequestDispatcher("/Info.jsp"); // 定向的页面
			((HttpServletRequest) request).setAttribute("msg", "您访问的资源不存在！");
			requestDispatcher.forward(request, response);
		}
		return null;
	}
}
