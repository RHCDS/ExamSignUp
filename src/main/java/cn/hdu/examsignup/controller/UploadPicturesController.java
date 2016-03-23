package cn.hdu.examsignup.controller;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.spring.SpringCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.hdu.examsignup.filter.JPGFileFilter;
import cn.hdu.examsignup.filter.ZIPFileFilter;
import cn.hdu.examsignup.service.StudentService;
import cn.hdu.examsignup.utility.FileOperateUtil;

@RemoteProxy(creator = SpringCreator.class)
public class UploadPicturesController {

	@Autowired
	private StudentService studentservice;

	final static int WITHOUT_PICTURE_NUM = 0;
	final static String WITHOUT_PICTURE_STRING = "没有照片的考生";
	final static int WITH_PICTURE_NUM = 1;
	final static String WITH_PICTURE_STRING = "有照片的考生";

	/*
	 * final static int EXCESS_PICTURE_NUM=2; final static String
	 * EXCESS_PICTURE_STRING ="多余的照片";
	 */

	@RemoteMethod
	public Map checkPicturesByModel(String startNum, String pageSize,
			String modelNum) {
		// 0列出对应学生列表里有照片的学生
		String schoolnum = (String) WebContextFactory.get().getSession()
				.getAttribute("institution");
		Map map = new HashMap();
		List<String> studentNumList = null;
		switch (Integer.parseInt(modelNum)) {
		case WITHOUT_PICTURE_NUM:
			studentNumList = studentservice.studentWithoutPicture(schoolnum);
			break;
		case WITH_PICTURE_NUM:
			studentNumList = studentservice.studentWithPicture(schoolnum);
			break;
		/*
		 * case EXCESS_PICTURE_NUM: studentNumList =
		 * studentservice.excessPicture(schoolnum); break;
		 */
		default:
			map.put("totalProperty", 0);
			map.put("root", new ArrayList());
			return map;
		}

		Integer endPoint = 0;
		if (studentNumList==null || studentNumList.size()==0 || Integer.parseInt(startNum) > studentNumList.size() - 1) {
			map.put("totalProperty", 0);
			map.put("root", new ArrayList());
			return map;
		}
		if (((Integer.parseInt(startNum)) + Integer.parseInt(pageSize)) > studentNumList
				.size())
			endPoint = studentNumList.size();
		else
			endPoint = (Integer.parseInt(startNum))
					+ Integer.parseInt(pageSize);

		List<String> subList = studentNumList.subList(
				Integer.parseInt(startNum), endPoint);
		List<Map> result = this.studentservice.getStudentByStudentnumList(
				subList, schoolnum);
		map.put("totalProperty", studentNumList.size());
		map.put("root", result);
		return map;
	}

	@RemoteMethod
	public Map loadSelectedModel() {
		Map map = new HashMap();
		List<Map> modelList = new ArrayList();
		Map tempMap = new HashMap();
		tempMap.put("modelNum", WITHOUT_PICTURE_NUM);
		tempMap.put("modelName", WITHOUT_PICTURE_STRING);
		modelList.add(tempMap);
		tempMap = new HashMap();
		tempMap.put("modelNum", WITH_PICTURE_NUM);
		tempMap.put("modelName", WITH_PICTURE_STRING);
		modelList.add(tempMap);

		map.put("totalProperty", modelList.size());
		map.put("root", modelList);
		return map;
	}
	
	@RemoteMethod
	public String clearAllPictures(){
		HttpSession curSession = WebContextFactory.get().getSession();
		if(curSession.getAttribute("institution")==null || ((String)curSession.getAttribute("institution")).isEmpty()){
			return "{ success: false, errors:{info: '请重新登录!'}}";
		}
		
		clearTmpFile();
		clearPictureFile();
		
		return "{ success: true, errors:{info: '删除成功'}}";
	}
	@RemoteMethod
	public String clearSurplusPictures() {
		HttpSession curSession = WebContextFactory.get().getSession();
		
		if(curSession.getAttribute("institution")==null || ((String)curSession.getAttribute("institution")).isEmpty()){
			return "{ success: false, errors:{info: '请重新登录!'}}";
		}
		String scoolNum = curSession.getAttribute("institution").toString();
		final String PICTURE_EXTERN=".jpg";
		final String baseDir = curSession.getServletContext().getRealPath("/")
				+ curSession.getAttribute("institution")
				+ "/pictures/";
		List<String > toDelPic = studentservice.excessPicture(scoolNum);
		if( null == toDelPic) {
			return "{ success: true, errors:{info: '多余照片删除成功删除成功！'}}";
		}
		for(String filepath:toDelPic) {
			filepath = baseDir + filepath + PICTURE_EXTERN;
			File pic = new File(filepath);
			pic.delete();
		}
		return "{ success: true, errors:{info: '多余照片删除成功删除成功！'}}";
	}
	public static void deleteDir(File dir) {  
        // 检查参数  
        if (dir == null || !dir.exists() || !dir.isDirectory()) {  
            return;  
        }  
        for (File file : dir.listFiles()) {  
            if (file.isFile()) {  
                file.delete(); // 删除目录  
            } else if (file.isDirectory()) {  
                deleteDir(file); // 递规的方式删除目录  
            }  
        }  
        dir.delete(); // 删除目录本身  
    }  
	
	public boolean clearTmpFile(){
		HttpSession curSession = WebContextFactory.get().getSession();
		if(curSession.getAttribute("institution")==null || ((String)curSession.getAttribute("institution")).isEmpty()){
			return false;
		}
		File tmpDir = new File(curSession.getServletContext().getRealPath(
				"/")
				+ curSession.getAttribute("institution") + "/tmp");
		if(tmpDir.exists() && tmpDir.isDirectory()) {
			for(File element:tmpDir.listFiles()){
				if(element.isDirectory()){
					deleteDir(element);
				}
				else
					element.delete();
			}
		}
		return true;
	}
	
	public boolean clearPictureFile(){
		HttpSession curSession = WebContextFactory.get().getSession();
		if(curSession.getAttribute("institution")==null || ((String)curSession.getAttribute("institution")).isEmpty()){
			return false;
		}
		File tmpDir = new File(curSession.getServletContext().getRealPath(
				"/")
				+ curSession.getAttribute("institution") + "/pictures");
		if(tmpDir.exists() && tmpDir.isDirectory()) {
			for(File element:tmpDir.listFiles()){
				if(element.isDirectory()){
					deleteDir(element);
				}
				else
					element.delete();
			}
		}
		return true;
	}

	@RemoteMethod
	public String processTheUploadedPictures(String basedir) {
		int totalCount=0;
		HttpSession curSession = WebContextFactory.get().getSession();
		File tempBaseDir = new File(curSession.getServletContext().getRealPath(
				"/")
				+ curSession.getAttribute("institution") + "/" + basedir);
		File pictureDir = new File(curSession.getServletContext().getRealPath(
				"/")
				+ curSession.getAttribute("institution") + "/pictures");
		
		if (!(tempBaseDir.exists() && tempBaseDir.isDirectory())) {
			if(!tempBaseDir.mkdirs()) {
				return "{ success: false, errors:{info: '缓存目录创建失败!'}}";
			}
		}
		if (!pictureDir.isDirectory()) {
			if (!pictureDir.mkdirs())
				return "{ success: false, errors:{info: '图片路径创建失败!'}}";
		}
		File[] zipFileList = tempBaseDir.listFiles(new ZIPFileFilter());
		try{
			for (File element : zipFileList) {
				totalCount=totalCount+this.upZipJPGFile(element, pictureDir.getAbsolutePath());
			}
		}catch(Exception e){
			e.printStackTrace();
			return "{ success: false, errors:{info: '解压失败!'}}";
		}
		
		List<File> tempPictureFileList = Arrays.asList(tempBaseDir
				.listFiles(new JPGFileFilter()));
		for(File element: tempPictureFileList){
			String finalFile= pictureDir.getAbsolutePath()+"/"+ element.getName().subSequence(0,element.getName().length() - 4) + ".jpg";
			element.renameTo(new File(finalFile));
			totalCount++;
		}
		for(File element:tempBaseDir.listFiles()){
			element.delete();
		}
		
		//图片分辨率转换
		File tempFile=null;
		for(File element:pictureDir.listFiles()){
			String sourcePictureName=element.getAbsolutePath();
			String finalPictureName=element.getAbsolutePath().subSequence(0,sourcePictureName.length() - 4)+"final.jpg";
			if(compressPic(sourcePictureName,finalPictureName,143,192)){
				if(element.isFile())
					element.delete();
				tempFile=new File(finalPictureName);
				tempFile.renameTo(element);
			}else{
				tempFile=new File(finalPictureName);
				if(tempFile.isFile())
					tempFile.delete();
			}
		}
		
		return "{ success: true, errors:{info: '此次载入"+totalCount+"张图片!'}}";
	}

	/**
	 * 解压缩功能. 将ZIP_FILENAME文件解压到ZIP_DIR目录下.
	 * 
	 * @throws Exception
	 */
	public int upZipJPGFile(File file, String pictureDir) throws Exception {
		// TODO Auto-generated method stub
		int count=0;
		ZipFile zf = new ZipFile(file.getAbsolutePath());
		// ZIP文件
		// 使用entries方法，返回一个枚举对象，循环获得文件的ZIP条目对象
		Enumeration entries = zf.getEntries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			System.out.println(entry.getName());
			if (entry.isDirectory()
					|| (!entry.getName().toLowerCase().endsWith(".jpg"))) {
				continue;
			}

			InputStream is = zf.getInputStream(entry);
			String fileRealName="";
			String temp[] = entry.getName().replaceAll("\\\\","/").split("/");
			fileRealName=temp[temp.length-1];
			if (temp.length > 1) {  
				fileRealName = temp[temp.length - 1];  
			}
			if(fileRealName.equals(""))
				continue;
			String finalPictureNameWithPath = pictureDir+"/"+ fileRealName.subSequence(0,fileRealName.length() - 4) + ".jpg";
			File finalFile = new File(finalPictureNameWithPath);
			if (finalFile.exists())
				finalFile.delete();
			FileOutputStream fop = new FileOutputStream(
					finalPictureNameWithPath);
			int len = 0;
			byte[] bs = new byte[4096];
			while ((len = is.read(bs, 0, 4096)) > 0) {
				fop.write(bs, 0, len);
			}
			is.close();
			fop.flush();
			fop.close();
			count++;
		}
		zf.close();
		return count;
	}
	
	//For zip
	private void handleDir(File dir , ZipOutputStream zipOut)throws IOException{ 
		FileInputStream fileIn;
		File[] files;
		int readedBytes;
		byte[] buf=new byte[1024];

		files = dir.listFiles();

		if (files.length == 0) {// 如果目录为空,则单独创建之.
			// ZipEntry的isDirectory()方法中,目录以"/"结尾.
			zipOut.putNextEntry(new ZipEntry(dir.toString() + "/"));
			zipOut.closeEntry();
		} else {// 如果目录不为空,则分别处理目录和文件.
			for (File fileName : files) {
				// System.out.println(fileName);

				if (fileName.isDirectory()) {
					handleDir(fileName, zipOut);
				} else {
					fileIn = new FileInputStream(fileName);
					zipOut.putNextEntry(new ZipEntry(fileName.getName()));

					while ((readedBytes = fileIn.read(buf)) > 0) {
						zipOut.write(buf, 0, readedBytes);
					}

					zipOut.closeEntry();
					fileIn.close();
				}
			}
		}
	}
	
	@RemoteMethod
	public String packageAllPictures() throws Exception{
        File zipDir=null;
        HttpSession curSession = WebContextFactory.get().getSession();
        ZipOutputStream zipOut=null;
        
        if(curSession.getAttribute("institution")==null || ((String)curSession.getAttribute("institution")).isEmpty()){
        	return "{ success: false, errors:{info: '请重新登陆!'}}";
		}
		File pictureDir = new File(curSession.getServletContext().getRealPath(
				"/")
				+ curSession.getAttribute("institution") + "/pictures");
		if(! (pictureDir.exists() && pictureDir.isDirectory())) {
			return "{ success: false, errors:{info: '系统中没有照片!'}}";
		}
		File tempBaseDir = new File(curSession.getServletContext().getRealPath(
				"/")
				+ curSession.getAttribute("institution") + "/tmp");
		if (!(tempBaseDir.exists() && tempBaseDir.isDirectory())) {
			if(!tempBaseDir.mkdirs()) {
				return "{ success: false, errors:{info: '缓存目录创建失败!'}}";
			}
		}
        zipDir = new File(pictureDir.getAbsolutePath()); 
        String zipFileName = tempBaseDir.getAbsolutePath()+"/pictures"+curSession.getAttribute("institution")+".zip";//压缩后生成的zip文件名 
        
        File finalZipFile= new File(zipFileName);
        if(finalZipFile.exists()){
        	if(finalZipFile.isDirectory())
        		deleteDir(finalZipFile);
        	else
        		finalZipFile.delete();
        }
        
        try{ 
        	File zipfile = new File(zipFileName);
        	FileOutputStream Fos = new FileOutputStream(zipfile);
        	BufferedOutputStream Bos = new BufferedOutputStream(Fos);
            zipOut = new ZipOutputStream(Bos); 
            handleDir(zipDir , zipOut);
            zipOut.close();
            Bos.close();
            Fos.close();
            return "{ success: true, errors:{info: '开始下载!',address:'"+
            	WebContextFactory.get().getHttpServletRequest().getContextPath()+"/"+
            	curSession.getAttribute("institution")+"/tmp"+"/pictures"+curSession.getAttribute("institution")+".zip"+"'}}";
        }catch(IOException ioe){ 
            ioe.printStackTrace();
            return "{ success: false, errors:{info: '后台处理出错!'}}";
        }
	}
	
	public static void Copy(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) {
				InputStream inStream = new FileInputStream(oldPath);
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread;
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("error  ");
			e.printStackTrace();
		}
	}
	
	//若为FALSE则表示原图像正确（未生成descFilePath），若为true则根据要求生成了descFilePath
	public static boolean compressPic(String srcFilePath, String descFilePath,int width,int height)  
    {  
		try {  
            BufferedImage src = ImageIO.read(new File(srcFilePath)); // 读入文件  
            if(src.getWidth()<width || src.getHeight()<height){
            	Copy(srcFilePath,descFilePath);
            	return false;
            }
            Image image = src.getScaledInstance(width, height , Image.SCALE_DEFAULT);  
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  //缩放图像  
            Graphics g = tag.getGraphics();  
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图  
            g.dispose();  
            ByteArrayOutputStream bos = new ByteArrayOutputStream();  
            ImageIO.write(tag,"JPEG",bos);
               
            FileOutputStream out = new FileOutputStream(descFilePath);  
            out.write(bos.toByteArray());
            out.close();
            return true;
        } catch (IOException e) {  
            e.printStackTrace();
            return false;
        }
    }
}
