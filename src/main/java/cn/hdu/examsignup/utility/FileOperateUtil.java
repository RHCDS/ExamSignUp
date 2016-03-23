package cn.hdu.examsignup.utility;

/**
*
* @author geloin
* @date 2012-5-5 下午12:05:57
*/

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
* 
* @author geloin
* @date 2012-5-5 下午12:05:57
*/
public class FileOperateUtil {
	
	private static Logger logger= LoggerFactory.getLogger(FileOperateUtil.class);

	public static List<String> upload(HttpServletRequest request, String subDir) throws Exception {//subDir没有开头的"/"但是有结尾的"/"

		CommonsMultipartResolver commonsMultipartResolver = new  CommonsMultipartResolver(request.getSession().getServletContext());  
		commonsMultipartResolver.setDefaultEncoding("UTF-8");
		List<String> result=new ArrayList<String>();
        
        if (commonsMultipartResolver.isMultipart(request)) {
        	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        	List<MultipartFile> fileList=multipartRequest.getFiles("file");
        	if(subDir==null){
        		return null;
        	}
        	String deployDir=request.getSession().getServletContext().getRealPath("/");
        	String finalDir=deployDir+subDir;
        	File dir=new File(finalDir);
        	if(!(dir.exists()&&dir.isDirectory())){
        		if(dir.mkdirs()==false){
        			return null;
        		}
        	}
            try {
            	for (MultipartFile file : fileList) {
                    if (file.isEmpty()) continue;   
                    FileOutputStream fileOS=new FileOutputStream(finalDir+file.getOriginalFilename());   
                    fileOS.write(file.getBytes());   
                    fileOS.flush();
                    fileOS.close();   
                    result.add(file.getOriginalFilename());
                }   
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            System.out.println("the enctype must be multipart/form-data");
        }
		return result;
	}

	public static boolean download(HttpServletRequest request,
			HttpServletResponse response, String subDir,//subDir没有开头的"/"但是有结尾的"/"
			String fileName) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		response.setContentType("multipart/form-data");
		request.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Disposition", "attachment;fileName="+fileName); 
		
        try { 
        	String finalFile=request.getSession().getServletContext().getRealPath("/")+subDir+fileName;
            File file=new File(finalFile); 
            if(!file.exists()){
            	return false;
            }
            System.out.println(file.getAbsolutePath());  
            InputStream inputStream=new FileInputStream(finalFile);  
            OutputStream os=response.getOutputStream();  
            byte[] b=new byte[4096];
            int length;  
            while((length=inputStream.read(b))>0){  
                os.write(b,0,length);  
            }  
            inputStream.close();
            return true;
        } catch (FileNotFoundException e) {  
            e.printStackTrace(); 
            return false;
        } catch (IOException e) {  
            e.printStackTrace(); 
            return false;
        } 
	}
}
