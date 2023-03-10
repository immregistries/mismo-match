package org.immregistries.mismo.match;

/**
 * Simple functions that are used by this project. 
 *
 */
public class StringUtils {	
	public static boolean isNotEmpty(String strValue) {
		if(strValue==null || strValue.trim().equals("")) {
			return false;
		}
		return true;
	}
	
	public static boolean isEmpty(String strValue) {
		if(strValue==null || strValue.trim().equals("")) {
			return true;
		}
		return false;
	}
}
