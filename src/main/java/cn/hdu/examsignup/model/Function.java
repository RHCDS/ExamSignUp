package cn.hdu.examsignup.model;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.directwebremoting.annotations.DataTransferObject;

@DataTransferObject
public class Function {
	public static String initSearchHql_whereString_(String filters){
		try{
			String hqlFilter=" where  1=1 ";
			HashSet perchSet = new HashSet(0);
			if(filters.equals("")){
				return hqlFilter;
			}
			String filterString=filters.replaceAll("[\"]", "");
			filterString = filterString.substring(1, filterString.length() - 1);
			String[] arrFilter=filterString.split(",");
			for(String filter:arrFilter)
			{
				if(filter.startsWith("except"))
					continue;
				if(filter.startsWith("null")){
					String[] optionString = filter.split(":",2);
					String[] arrOption = optionString[0].split("\\$");
					if(optionString[1].equals("true")){
						hqlFilter += arrOption[1] + " " + arrOption[2].replaceAll("[-]", ".").replaceAll("&", "") + " is not null ";
					}
					else{
						//hqlFilter += arrOption[1] + " " + arrOption[2].replaceAll("[-]", ".").replaceAll("&", "") + " is not null ";
					}
					continue;
//					String optionString=filter.split(":",2)[0];
//					String filterValue=filter.split(":",2) [1];
				}
				String optionString=filter.split(":",2)[0];
				String filterValue=filter.split(":",2) [1];
				String[] arrOption=optionString.split("\\$");
				if(!(filterValue.equals(""))||arrOption[2].equals("!="))
				{
					String perchString = arrOption[1].replaceAll("[-]", "");
					if(perchSet.contains(perchString)){
						perchString += "_1";
					}
					perchSet.add(perchString);
//					addPerch(perchSet, perchString);
					hqlFilter += arrOption[0] + " " + arrOption[1].replaceAll("[-]", ".").replaceAll("&", "") + " " + arrOption[2] + " " +arrOption[3].replace(arrOption[3], ":" + perchString)+ " ";
//					String 
//					optionString=optionString.replace(arrOption[1], arrOption[1].replaceAll("[-]", "."));
//					optionString=optionString.replace(arrOption[3],""+arrOption[3].replace(arrOption[3], ":" + arrOption[1].replaceAll("[-]", ""))+"").replaceAll("\\$", " ");
//					if(arrOption.length > 4){
//						optionString = optionString.replace(arrOption[4], "");
//					}
//					hqlFilter+=optionString + " ";
				}
			}
			System.out.println("function-initSearchHql success:" + hqlFilter);
			return hqlFilter;
		}catch(Exception e){
			System.out.println("function-initSearchHql error:" + e.getMessage());
			return null;
		}
	}
	
	public static Map initSearchValues(String filters){
		try{
			Map<String, Object> valuesMap = new HashMap<String, Object>();
			if(filters.equals("")){
				return valuesMap;
			}
			String filterString=filters.replaceAll("[\"]", "");
			filterString = filterString.substring(1, filterString.length() - 1);
			String[] arrFilter=filterString.split(",");
			for(String filter:arrFilter)
			{
				if(filter.startsWith("except"))
					continue;
				if(filter.startsWith("null"))
					continue;
				String optionString=filter.split(":",2)[0];
				String valueString=filter.split(":",2) [1];
				String[] arrOption=optionString.split("\\$");
				if(!(valueString.equals(""))||arrOption[2].equals("!="))
				{
					String perchString = arrOption[1].replaceAll("[-]", "");
					String valueType = arrOption.length > 4 ? arrOption[4] : "String";
					valueString = arrOption[3].replace("value", valueString);
					Object value = valueTransform(valueString, valueType);
					if(valuesMap.containsKey(perchString)){
						perchString += "_1";
					}
					valuesMap.put(perchString, value);
				}
			}
			System.out.println("function-initSearchValues success:" + valuesMap.size());
			return valuesMap;
		}catch(Exception e){
			System.out.println("function-initSearchValues error:" + e.getMessage());
			return null;
		}
	}
	
	public static Object valueTransform(String valueString, String valueType) throws Exception{
		if(valueType.equals("String")){
			return valueString;
		}
		else if(valueType.equals("Short")){
			return Short.parseShort(valueString);
		}
		else if(valueType.equals("Integer")){
			return Integer.parseInt(valueString);
		}
		else if(valueType.equals("Double")){
			return Double.parseDouble(valueString);
		}
		else if(valueType.equals("Long")){
			return Long.parseLong(valueString);
		}
		else if(valueType.equals("Boolean")){
			return Boolean.parseBoolean(valueString);
		}
		else if(valueType.equals("Date")){
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(valueString.replace("T", " "));
		}
		else
			return valueString;
	}
}
