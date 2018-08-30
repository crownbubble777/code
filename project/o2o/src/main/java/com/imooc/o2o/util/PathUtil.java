package com.imooc.o2o.util;

public class PathUtil {
	/**
	 * 提供两类路径：(1)返回项目图片的根路径(根路径)(2)依据不同的也无需求返回项目图片的子路径
	 */
	private static String seperator=System.getProperty("file.separator");
	public static String getImgBasePath() {
		//依据不同的操作系统选择不同的路径
		String os=System.getProperty("os.name");
		String basePath="";
		if(os.toLowerCase().startsWith("win")) {
//			basePath="C:/BaiDuNetdiskDownload/image/";
			basePath="C:/Users/Administrator/Imag/";
		}
		else {
			basePath="/Users/Administrator/Imag";
		}
		basePath=basePath.replace("/",seperator);
		return basePath;
	}
	public static String getShopImagePath(long shopId) {
		String imagePath="/upload/images/item/shop/"+shopId+"/";//这里可能有点问题
		return imagePath.replace("/",seperator);
	}
}
