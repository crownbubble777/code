package com.imooc.o2o.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.ShopDao;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exceptions.ShopOperationException;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PageCalculator;
import com.imooc.o2o.util.PathUtil;
//接口的实现类
@Service
public class ShopServiceImpl implements ShopService {
	@Autowired
	private ShopDao shopDao;
	@Override
	@Transactional//添加一个事务的标签，表示整个过程是需要事务的支持的
	public ShopExecution addShop(Shop shop, ImageHolder thumbnail)throws ShopOperationException {
		//空值判断，即检查传入的参数是不是合法
		if(shop==null)
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		//添加店铺，将所有的问题都放在try...cathch块中
		try {//向数据库中添加信息
			//给店铺信息赋初始值
			shop.setEnableStatus(0);//未上架，在审核中，正处于CHECK状态
			shop.setCreateTime(new Date());//设置默认的时间
			shop.setLastEditTime(new Date());
			//添加店铺信息
			int effectedNum=shopDao.insertShop(shop);//插入店铺信息
			if(effectedNum<=0)//此时说明添加失败
				throw new ShopOperationException("店铺创建失败");//用RuntimeException是为了将事务提交并回滚
			else {//此时说明添加成功
				//1.判断传入图片，如果不为空，那么就存储图片
				if(thumbnail.getImage() != null) {
					try {
						addShopImg(shop,thumbnail);//这个方法可能会返回异常，因此直接用try...catch块处理，一旦图片存储成功我们就能在Shop中得到图片的存储地址
					} catch (Exception e) {
						throw new ShopOperationException("addShopImg error:"+e.getMessage());
					}
					effectedNum=shopDao.updateShop(shop);//更新店铺的图片地址
					if(effectedNum<=0)//如果图片地址更新失败，那么直接抛出RuntimeException
						throw new ShopOperationException("更新图片地址失败");//直接抛出异常
				}
			}
			} catch (Exception e) {
			throw new ShopOperationException("addShop error:"+e.getMessage());
		}
		return new ShopExecution(ShopStateEnum.CHECK,shop);//返回店铺的状态值和店铺的名字
	}
	
	private void addShopImg(Shop shop, ImageHolder thumbnail) {
		String dest=PathUtil.getShopImagePath(shop.getShopId());// 获取shop图片目录的相对值路径
		String shopImgAddr=ImageUtil.generateThumbnail(thumbnail, dest);
		shop.setShopImg(shopImgAddr);
	}

	@Override
	public Shop getByShopId(long shopId) {
		return shopDao.queryByShopId(shopId);//在dao层就已经写好了
	}

	@Override
	public ShopExecution modifyShop(Shop shop, ImageHolder thumbnail) throws ShopOperationException {
		if(shop==null ||shop.getShopId()==null)
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		else {
			//1.判断是否需要处理图片
			try {
					//改了if(thumbnail.getImage()!=null) {
					if(thumbnail.getImage()!=null || thumbnail.getImageName() != null || !"".equals(thumbnail.getImageName())) {
						//如果原来图片是存在的化，先进行删除，在进行添加
						Shop tempShop=shopDao.queryByShopId(shop.getShopId());
						if(tempShop.getShopImg()!=null) {//删除之前的地址
							ImageUtil.deleteFileOrPath(tempShop.getShopImg());
						}
						//addShop(shop,thumbnail);
						addShopImg(shop,thumbnail);//改了
					}
				//2.更新店铺信息
				shop.setLastEditTime(new Date());
				int effectedNum=shopDao.updateShop(shop);//接回update的返回值，一般是一条，如果不是一条就错了
				if(effectedNum<=0)
					return new ShopExecution(ShopStateEnum.INNER_ERROR);
				else {
					shop=shopDao.queryByShopId(shop.getShopId());
					return new ShopExecution(ShopStateEnum.SUCCESS,shop);
				}
			} catch (Exception e) {
				throw new ShopOperationException("modifyShop error:"+e.getMessage());
			}
		}
	}

	@Override
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
		//要先实现pageIndex和rowIndex的转换，在util新编写一个工具类
		int rowIndex=PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<Shop> shopList=shopDao.queryShopList(shopCondition, rowIndex, pageSize);
		int count=shopDao.queryShopCount(shopCondition);
		ShopExecution se=new ShopExecution();
		if(shopList!=null) {
			se.setShopList(shopList);
			se.setCount(count);
		}
		else {
			se.setState(ShopStateEnum.INNER_ERROR.getState());
		}
		return se;
	}
}
