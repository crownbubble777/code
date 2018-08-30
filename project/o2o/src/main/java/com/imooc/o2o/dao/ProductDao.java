package com.imooc.o2o.dao;




import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.Product;

public interface ProductDao {

	/**
	 * 查询对应的商品总数，返回的是按照这个条件查询的能满足这个条件的所哟的商品总数
	 * @param productCondition
	 * @return
	 */
	int queryProductCount(@Param("productCondition")Product productCondition);
	/**
	 * 查询商品列表并分页，即输入的条件有：商品名(模糊),商品状态，店铺Id，商品类别
	 * @param productCondition
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	List<Product> queryProductList(@Param("productCondition")Product productCondition,@Param("rowIndex")int rowIndex,
			@Param("pageSize")int pageSize);
	/**
	 * 通过productId查询唯一的商品信息
	 * 
	 * @param productId
	 * @return
	 */
	Product queryProductById(long productId);
	/**
	 * 插入商品
	 * @param product
	 * @return
	 */
	int insertProduct(Product product);
	/**
	 * 根据传入的商品更新商品的信息
	 * @param product
	 * @return
	 */
	int updateProduct(Product product);
	/**
	 * 删除商品类别前，将商品类别ID置为空
	 * 接受的参数就是这个类别Id，所以功能就是将这个类别Id下的商品置为空
	 * @param productCategoryId
	 * @return
	 */
	int updateProductCategoryToNull(long productCategoryId);
	
}
