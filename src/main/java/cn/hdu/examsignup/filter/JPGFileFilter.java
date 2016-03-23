package cn.hdu.examsignup.filter;

import java.io.File;
import java.io.FileFilter;

public class JPGFileFilter implements FileFilter{

	@Override
	public boolean accept(File pathname) {
		String filename = pathname.getName().toLowerCase();
		if(filename.endsWith(".jpg")){
			return true;
		}else{
			return false;
		}
	}
}