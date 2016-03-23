package cn.hdu.examsignup.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.directwebremoting.WebContextFactory;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.spring.SpringCreator;

@RemoteProxy(creator = SpringCreator.class)
public class SoftwareDownloadController {

	final static String SOFTWARE_BASEDIR="software";
	final static String SOFTWARE_CONFIGURE="file.properies";
	@RemoteMethod
	public String getSoftwareList( ){
			String filedir = (String) WebContextFactory.get().getSession().getServletContext().getRealPath("/")+SOFTWARE_BASEDIR+"/"+SOFTWARE_CONFIGURE;
			File file=new File(filedir);
			if(!(file.exists()&&file.isFile())){
				return "{ success: false, errors:{info: '配置文件不存在!'},basedir:'"+SOFTWARE_BASEDIR+"',data:[]}";
			}
			try{
				InputStream in = new FileInputStream(file);
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				String line = reader.readLine(); // 读取第一行 
				StringBuffer stringBuffer = new StringBuffer("");
				while (line != null) { // 如果 line 为空说明读完了 
					stringBuffer.append(line); // 将读到的内容添加到 buffer 中 
					line = reader.readLine(); // 读取下一行 
				} 
				return "{ success: true, errors:{info: '文件读取成功!'},basedir:'"+SOFTWARE_BASEDIR+"',data:"+stringBuffer+"}";
			}catch(IOException e){
				e.printStackTrace();
				return "{ success: false, errors:{info: '配置文件读取错误!'},basedir:'"+SOFTWARE_BASEDIR+"',data:[]}";
			}
			
	}
}
