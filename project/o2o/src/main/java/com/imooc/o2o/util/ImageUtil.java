package com.imooc.o2o.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.exceptions.ShopOperationException;


import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
//用来封装Thumbnailators的方法来处理图片
/**
 * 将小黄人的图片进行压缩并且加上水印
 * @author Administrator
 *
 */
public class ImageUtil {
	private static String basePath=Thread.currentThread().getContextClassLoader().getResource("").getPath();//获取到classpath的绝对值路径
	//下面随机生成不同的文件名方法中要用到时间以及随机对象
	private static final SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
	private static final Random r=new Random();
	private static Logger logger=LoggerFactory.getLogger(ImageUtil.class);
	
	/**
	 * 处理缩略图，并返回新生成图片的相对路径
	 * @param thumbnail
	 * @param targetAddr
	 * @return
	 */
	public static String generateThumbnail(ImageHolder thumbnail, String targetAddr){
		String realFileName=getRandomFileName();//获得不重复的随机名
		String extension=getFileExtension(thumbnail.getImageName());//获取文件的扩展名png，jpg
		makeDirPath(targetAddr);//如果目标路径不存在，那么自动创建
		String relativeAddr=targetAddr+realFileName+extension;//获取文件存储的相对路径(带文件名)
		logger.debug("current relativeAddr is:"+relativeAddr);
		File dest=new File(PathUtil.getImgBasePath()+relativeAddr);//获取文件要保存的目标路径
		logger.debug("current complete addr is:"+PathUtil.getImgBasePath()+relativeAddr);
		logger.debug("basePath is:"+basePath);
		try {//调用Thumbnails生成带有水印的图片
			Thumbnails.of(thumbnail.getImage()).size(200,200)
			.watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File(basePath+"/watermark.jpg")),0.25f)
			.outputQuality(0.8f).toFile(dest);
		} catch (IOException e) {
			throw new ShopOperationException("图片创建失败: "+e.toString());
		}
		return relativeAddr;//返回图片的相对路径
	}
	/**
	 * 生成随机文件，要求每个文件的名字都是不一样的，为了满足这个要求可以用
	 * “当前年月日小时分钟秒钟+5位随机数”这种形式
	 * @return
	 */
	public static String getRandomFileName() {
		//获取随机的五位数，范围是10000-99999
		int rannum=r.nextInt(89999)+10000;//r.nextInt的意思是在0~89999中取值，再加上10000就表示小于99999
		String nowTimestr=sDateFormat.format(new Date());
		return nowTimestr+rannum;//字符串加上整型将会自动转换成整型
	}
	/**
	 * 获取输入文件的扩展名
	 * @param thumbnail
	 * @return
	 */
	private static String getFileExtension(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));//获取原来的名字，输入文件流的文件名
	}
	/**
	 * 创建目标路径上所涉及到的目录，即、home/work/xiangze/xxx.jpg
	 * 那么home、work、xiangze这三个文件夹都得自己创建出来
	 * @param targetAddr
	 */
	private static void makeDirPath(String targetAddr) {
		String realFileParentPath=PathUtil.getImgBasePath()+targetAddr;
		File dirPath=new File(realFileParentPath);
		if(!dirPath.exists()) {
			dirPath.mkdirs();//如果不存在，就递归的创建出来(会将一路上的文件都给创建出来)
		}
	}
	public static void main(String[] args) throws IOException {
		Thumbnails.of(new File("/Users/Administrator/Image/xiaohuangren.jpg"))
		.size(200,200).watermark(Positions.BOTTOM_RIGHT,
		ImageIO.read(new File(basePath+"/watermark.jpg")),0.25f).outputQuality(0.8f)
		.toFile("/Users/Administrator/Image/xiaohuangrennew.jpg");
		}
	/**
	 * storePath是文件路径还是目录的路径，如果storePath是文件路径则删除该文件
	 * 如果storePath是目录路径则删除目录下的所有文件
	 */
	public static void deleteFileOrPath(String storePath) {
		File fileOrPath=new File(PathUtil.getImgBasePath()+storePath);
		if(fileOrPath.exists()) {
			if(fileOrPath.isDirectory()) {//如果是目录，则删除目录下的所有文件
				File files[]=fileOrPath.listFiles();
				for(int i=0;i<files.length;i++) {
					files[i].delete();
				}
			}
			//如果不是目录而是文件就直接删除
			fileOrPath.delete();
		}
	}
	/**
	 * 处理详情图，并返回新生成图片的相对值路径
	 * 和generateThumbnail是一样的只是大小不一样
	 * @param productImgHolder
	 * @param dest
	 * @return
	 */
	public static String generateNormalImg(ImageHolder thumbnail, String targetAddr) {
		//获取不重复的随机名
		String realFileName=getRandomFileName();
		//获取文件的扩展名,如png,jpg等
		String extension=getFileExtension(thumbnail.getImageName());
		//如果目标路径不存在，则自动创建
		makeDirPath(targetAddr);
		//获取文件存储的相对路径(带文件名)
		String relativeAddr=targetAddr+realFileName+extension;
		logger.debug("current relativeAddr is:"+relativeAddr);
		//获取文件要保存到的目标路径
		File dest=new File(PathUtil.getImgBasePath()+relativeAddr);
		logger.debug("current complete addr is:"+PathUtil.getImgBasePath()+relativeAddr);
		//调用Thumbnail生成带有水印的图片
		try {
			Thumbnails.of(thumbnail.getImage()).size(337,640)//就这里不一样，大小的透明度不一样
					.watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File(basePath+"/watermark.jpg")),0.25f)
					.outputQuality(0.9f).toFile(dest);
		} catch (Exception e) {
			logger.error(e.toString());
			throw new RuntimeException("创建缩图片失败:"+extension.toString());
		}
		//返回图片相对路径地址
		return relativeAddr;
	}
}
