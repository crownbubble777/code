package com.imooc.o2o.service;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.exceptions.ShopOperationException;
//其中包含一个方法：添加店铺
public interface ShopService {
	/**
	 * 根据shopCondition分页返回响应的店铺列表
	 * @param shopCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public ShopExecution getShopList(Shop shopCondition,int pageIndex,int pageSize);
	/**
	 * 通过店铺Id获取店铺信息
	 * @param shopId
	 * @return
	 */
	Shop getByShopId(long shopId);
	/**
	 * 更新店铺信息，包括对图片的处理
	 * @param shop
	 * @param thumbnail
	 * @return
	 * @throws ShopOperationException
	 */
	ShopExecution modifyShop(Shop shop,ImageHolder thumbnail) throws ShopOperationException;
	/**
	 * 注册店铺信息，包括对图片的处理
	 * @param shop
	 * @param thumbnail
	 * @return
	 * @throws ShopOperationException
	 */

	ShopExecution addShop(Shop shop, ImageHolder thumbnail)throws ShopOperationException;		
}
