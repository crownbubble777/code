package com.imooc.o2o.service;

import java.util.List;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.exceptions.ProductOperationException;

public interface ProductService {
	/**
	 * 查询商品列表分页，可输入的条件有:商品名(模糊)，商品状态，店铺类别，店铺Id,商品类别，条件可能是单独的一个也可能是四者的组合
	 * 功能：根据输入的商品条件获得商品列表
	 * pageIndex表示是哪一页的数据,pageSize表示这一页的哪一条数据
	 */
	ProductExecution getProductList(Product productCondition,int pageIndex,int pageSize);
	/**
	 * 添加商品信息和图片处理,其中重要的是addProduct方法，有三个参数，商品，缩略图，详情图
	 * @return
	 * @throws ProductOperationException
	 */
	ProductExecution addProduct(Product product,ImageHolder thumbnail,List<ImageHolder> productImgHolderList) 
			throws ProductOperationException;
	/**
	 * 通过商品Id查询唯一的商品信息，在商品编辑之前先获得商品的信息
	 * @param productId
	 * @return
	 */
	Product getProductById(long productId);
	/**
	 * 修改商品信息以及图片处理
	 * @param product
	 * @param thumbnail
	 * @param productImgHolderList
	 * @return
	 * @throws ProductOperationException
	 */
	ProductExecution modifyProduct(Product product,ImageHolder thumbnail,List<ImageHolder>productImgHolderList)
	throws ProductOperationException;
}
