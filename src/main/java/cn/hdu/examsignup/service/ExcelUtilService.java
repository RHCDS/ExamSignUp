package cn.hdu.examsignup.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.hdu.examsignup.controller.ExcelUtilController;

@Service
public class ExcelUtilService {

	private Logger logger = LoggerFactory.getLogger(ExcelUtilController.class);

	public JSONObject convertExcelToJson(InputStream fromInputStream,
			InputStream sampleInputStream) throws IOException {
		Workbook fromWorkbook;
		Workbook sampleWorkbook;
		JSONArray excelArray = new JSONArray();
		JSONObject result = new JSONObject();
		List<String> title = new ArrayList<String>();
		try {
			fromWorkbook = Workbook.getWorkbook(fromInputStream);
			sampleWorkbook = Workbook.getWorkbook(sampleInputStream);

			Sheet fromSheet = fromWorkbook.getSheet(0);
			Sheet sampleSheet = sampleWorkbook.getSheet(0);

			if (fromSheet.getColumns() < sampleSheet.getColumns()) {
				logger.debug("from: " + fromSheet.getColumns()
						+ " columns.\tsample: " + sampleSheet.getColumns()
						+ " colums");
				System.out.println(fromSheet.getColumns());
				System.out.println(sampleSheet.getColumns());
				result.put("success", false);
				result.put("msg", "所给Excel格式与样表列长度值不匹配！");
				result.put("excelArray", null);
				return result;
			}

			for (int i = 0; i < sampleSheet.getColumns(); i++) {
				if (!sampleSheet.getCell(i, 0).getContents().trim().equals(fromSheet
						.getCell(i, 0).getContents().trim())) {
					result.put("success", false);
					result.put("msg", "所给Excel字段与样表字段不匹配!");
					result.put("excelArray", null);
					return result;
				}
			}

			for (int i = 0; i < sampleSheet.getColumns(); i++) {
				title.add(sampleSheet.getCell(i, 1).getContents());
			}
            System.out.println(fromSheet.getRows());
			for (int i = 1; i < fromSheet.getRows(); i++) {
				JSONObject onLine = new JSONObject();
				int blankamount=0;
				for (int j = 0; j < fromSheet.getColumns(); j++) {
					 if(fromSheet.getCell(j, i).getContents()=="" )blankamount++;
				}
				if(blankamount==fromSheet.getColumns()) continue;//如果是一条空行记录，则直接跳过
				for (int j = 0; j < fromSheet.getColumns(); j++) {
					onLine.put(title.get(j), fromSheet.getCell(j, i).getContents());
				}
				excelArray.add(onLine);
			}

			result.put("success", true);
			result.put("msg", "转换成功.");
			result.put("excelArray", excelArray);
			return result;

		} catch (BiffException e) {
			e.printStackTrace();
		}

		result.put("success", false);
		result.put("msg", "发生异常!");
		result.put("excelArray", null);
		return null;
	}
	//判断excel字段是否都在
	public String checkExcelHeader(InputStream fromInputStream,
			InputStream sampleInputStream){
		try {
			Workbook fromWorkbook = Workbook.getWorkbook(fromInputStream);
			Workbook sampleWorkbook = Workbook.getWorkbook(sampleInputStream);
			Sheet fromSheet = fromWorkbook.getSheet(0);
			Sheet sampleSheet = sampleWorkbook.getSheet(0);
			a:
			for (int i = 0; i < sampleSheet.getColumns(); i++) {
				String header = sampleSheet.getCell(i, 0).getContents().trim();
				b:
				for(int j = 0; j < fromSheet.getColumns(); j++){
					if(header.equals(fromSheet.getCell(j, 0).getContents().trim())){
						break b;
					}else{
						if(j==fromSheet.getColumns()-1){
							return  "{ success: false, errors:{info: '<" + header +">字段空缺或与样表中的字段名不一致！'}}";
						}
					}
				}
			}
			return "passcheck";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return  "{ success: false, errors:{info: '后台出错！'}}";
		}

		
	}
	
	//a,b表中字段对应位置
	public List<Map> positionRelation(Sheet sampleSheet,Sheet fromSheet){
		try {
			List<Map> positionRelation = new ArrayList<Map>();
			a:
			for (int i = 0; i < sampleSheet.getColumns(); i++) {
				String header = sampleSheet.getCell(i, 0).getContents().trim();
				Map positon = new HashMap();
				positon.put("headerA", sampleSheet.getCell(i, 1).getContents().trim());
				b:
				for(int j = 0; j < fromSheet.getColumns(); j++){
					if(header.equals(fromSheet.getCell(j, 0).getContents().trim())){
						positon.put("headerB", j);
						break b;
					}else{
						if(j==fromSheet.getColumns()-1){
							return  null;
						}
					}
				}
				positionRelation.add(positon);
			}
			return positionRelation;
		} catch (Exception e) {
			return null;
		}
		
	}
	
	//转换考生exceltojson
	
	public JSONObject convertstudentExcelToJson(InputStream fromInputStream,
			InputStream sampleInputStream) throws IOException {
		Workbook fromWorkbook;
		Workbook sampleWorkbook;
		JSONArray excelArray = new JSONArray();
		JSONObject result = new JSONObject();
		List<String> title = new ArrayList<String>();
		try {
			fromWorkbook = Workbook.getWorkbook(fromInputStream);
			sampleWorkbook = Workbook.getWorkbook(sampleInputStream);

			Sheet fromSheet = fromWorkbook.getSheet(0);
			Sheet sampleSheet = sampleWorkbook.getSheet(0);

			if (fromSheet.getColumns() == sampleSheet.getColumns()) {
				for (int i = 0; i < sampleSheet.getColumns(); i++) {
					if (!sampleSheet.getCell(i, 0).getContents().trim().equals(fromSheet
							.getCell(i, 0).getContents().trim())) {
						result.put("success", false);
						result.put("msg", "所给Excel字段与样表字段不匹配!");
						result.put("excelArray", null);
						return result;
					}
				}

				for (int i = 0; i < sampleSheet.getColumns(); i++) {
					title.add(sampleSheet.getCell(i, 1).getContents());
				}
	            System.out.println(fromSheet.getRows());
				for (int i = 1; i < fromSheet.getRows(); i++) {
					JSONObject onLine = new JSONObject();
					int blankamount=0;
					for (int j = 0; j < fromSheet.getColumns(); j++) {
						 if(fromSheet.getCell(j, i).getContents()=="" )blankamount++;
					}
					if(blankamount==fromSheet.getColumns()) continue;//如果是一条空行记录，则直接跳过
					for (int j = 0; j < fromSheet.getColumns(); j++) {
						onLine.put(title.get(j), fromSheet.getCell(j, i).getContents());
					}
					excelArray.add(onLine);
				}

				result.put("success", true);
				result.put("msg", "转换成功.");
				result.put("excelArray", excelArray);
				return result;
			}else{
				List<Map> relations = new ArrayList<Map>();
				for (int i = 0; i < sampleSheet.getColumns(); i++) {
					title.add(sampleSheet.getCell(i, 1).getContents());
				}
				if(this.positionRelation(sampleSheet, fromSheet)!=null){
					relations = this.positionRelation(sampleSheet, fromSheet);
					for (int i = 1; i < fromSheet.getRows(); i++) {
						JSONObject onLine = new JSONObject();
						for(Map relation : relations){
							onLine.put(relation.get("headerA").toString(), fromSheet.getCell(Integer.parseInt(relation.get("headerB").toString()), i).getContents());
						
						}
						excelArray.add(onLine);
					}

				}else{
					result.put("success", false);
					result.put("error", "{info: '后台出错！'}");
					result.put("excelArray", null);
				}
				result.put("success", true);
				result.put("error", "转换成功.");
				result.put("excelArray", excelArray);
				return result;
			}
		} catch (BiffException e) {
			e.printStackTrace();
		}

		result.put("success", false);
		result.put("error", "{info: '后台出错！'}");
		result.put("excelArray", null);
		return result;
	}
}
