package org.parse.contoller;

import java.util.regex.*;

public class RegTest {

	public static void main(String[] args) {
		String str = " // 주석입니다.";
		
//		String pattern[] = { "//.*", ".*//.*", };
		
		String pattern1 = "//.*";			// // 로 시작하는거
		String pattern2 = "^((?!\").)*$";	// " 포함하지 않는거
		String pattern3 = "^((?!//).)*$";	// // 포함하지 않는거
		
		String pattern = "//.*|^((?!\").)*$";
	
		Pattern p = Pattern.compile(pattern);
		
		System.out.println("Pattern: " + p);
		
		Matcher m = p.matcher(str);
		
		if(m.matches()) {
			System.out.println(m);
		}
		
	}

}
