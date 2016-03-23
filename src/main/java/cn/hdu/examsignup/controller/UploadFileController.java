package cn.hdu.examsignup.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UploadFileController {

	@RequestMapping(value = "UpLoadSingleFile", method = RequestMethod.POST)
	public ModelAndView upLoadSingleFile(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest
				.getFile("file");
		convertExcelToJson(file.getInputStream());

		String name = multipartRequest.getParameter("name");
		System.out.println("name: " + name);
		// 获得文件名：
		String realFileName = file.getOriginalFilename();
		System.out.println("获得文件名：" + realFileName);
		// 获取路径
		// String ctxPath = request.getSession().getServletContext()
		// .getRealPath("/")
		// + "images/";
		// 创建文件
		// File dirPath = new File(ctxPath);
		// if (!dirPath.exists()) {
		// dirPath.mkdir();
		// }
		// File uploadFile = new File(ctxPath + realFileName);
		// FileCopyUtils.copy(file.getBytes(), uploadFile);
		// request.setAttribute("files", loadFiles(request));
		return null;
	}

	public void convertExcelToJson(InputStream excelFile) throws IOException {
		Workbook w;
		try {
			w = Workbook.getWorkbook(excelFile);
			Sheet sheet = w.getSheet(0);
			System.out.println(sheet.getColumns());
			System.out.println(sheet.getRows());

			for (int i = 0; i < sheet.getRows(); i++) {
				for (int j = 0; j < sheet.getColumns(); j++) {
					Cell cell = sheet.getCell(j, i);
					System.out.println("row:"+i+"colum:"+j+"\t\t"+cell.getContents());
				}
			}
		} catch (BiffException e) {
			e.printStackTrace();
		}
	}
}