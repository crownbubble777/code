package com.imooc.o2o.util;

import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestUtil {
	//1.将request里面的key值转换成int
	public static int getInt(HttpServletRequest request,String key) {
		try {
			return Integer.decode(request.getParameter(key));//将request参数中的key提取出来然后将其转换成整型
		} catch (Exception e) {
			return -1;//如果转换失败，就返回-1
		}
	}
	//1.将request里面的key值转换成long
	public static long getLong(HttpServletRequest request,String key) {
		try {
			return Long.valueOf(request.getParameter(key));//将request参数中的key提取出来然后将其转换成整型
		} catch (Exception e) {
			return -1;//如果转换失败，就返回-1
		}
	}
	//3.将request里面的key值转换成double
	public static double getDouble(HttpServletRequest request,String key) {
		try {
			return Double.valueOf(request.getParameter(key));//将request参数中的key提取出来然后将其转换成整型
		} catch (Exception e) {
			return -1d;//如果转换失败，就返回-1
		}
	}
	//4.将request里面的key值转换成boolean
	public static boolean getBoolean(HttpServletRequest request,String key) {
		try {
			return Boolean.valueOf(request.getParameter(key));//将request参数中的key提取出来然后将其转换成整型
		} catch (Exception e) {
			return false;//如果转换失败，就返回-1
		}
	}
	//
	public static String getString(HttpServletRequest request,String key) {
		try {
			String result=request.getParameter(key);
			if(result!=null) {
				result=result.trim();//去掉左右两侧的空格
			}
			if("".equals(result)) {
				result=null;
			}
			return result;
		} catch (Exception e) {
			return null;
		}
	}
}
