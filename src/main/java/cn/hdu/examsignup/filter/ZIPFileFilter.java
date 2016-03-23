package cn.hdu.examsignup.filter;

import java.io.File;
import java.io.FileFilter;

public class ZIPFileFilter implements FileFilter{
	@Override
	public boolean accept(File pathname) {
		String filename = pathname.getName();
		if(filename.endsWith(".zip")){
			return true;
		}else{
			return false;
		}
	}
}
