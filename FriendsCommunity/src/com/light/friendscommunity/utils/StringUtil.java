package com.light.friendscommunity.utils;

public class StringUtil {
	
	/**
	 * 
	 * @param str
	 * @return true if the string is null or length-0
	 */
	public static boolean isEmpty(String str){
		if(str == null || str.length()==0){
			return true;
		}else{
			return false;
		}
	}

}
