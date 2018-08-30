package com.imooc.o2o.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.ProductCategoryDao;
import com.imooc.o2o.dao.ProductDao;
import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.enums.ProductCategoryStateEnum;
import com.imooc.o2o.exceptions.ProductCategoryOperationException;
import com.imooc.o2o.service.ProductCategoryService;
@Service//表示的是我们需要Spring来托管这个类
public class ProductCategoryServiceImpl implements ProductCategoryService {
	@Autowired
	private ProductCategoryDao productCategoryDao;//Spring在实现中动态的注入ProductCategoryDao中MyBatis的实现类
	@Autowired
	private ProductDao productDao;
	@Override
	public List<ProductCategory> getProductCategoryList(Long shopId) {
		return productCategoryDao.queryProductCategoryList(shopId);//不需要做测试因为这个方法在Dao层测试了
	}
	@Override
	public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList)
			throws ProductCategoryOperationException {
		if(productCategoryList!=null && productCategoryList.size()>0) {
			try {
				int effectedNum=productCategoryDao.batchInsertProductCategory(productCategoryList);
				if(effectedNum<=0)
					throw new ProductCategoryOperationException("店铺类别创建失败");
				else {
					return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
				}
			} catch (Exception e) {
				throw new ProductCategoryOperationException("batchAddProductCategory error:"+e.getMessage());
			}
		}
		//如果这个列表为空就直接抛出emptyless的异常
		else {
			return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
		}
	}
	@Override
	@Transactional//是因为这里有两步，所以要调用数据库里面的事务，这样可以实现如果第二步有错误或者还未编辑第一步也不会急于提交，这样就达到了回滚的目的
	public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId)
			throws ProductCategoryOperationException {
		// TODO 将此商品类别下的商品类别Id置为空,实现这个方法
		//解除tb_product里面的商品与该productcategoryId的关联
		try {
			int effectedNum=productDao.updateProductCategoryToNull(productCategoryId);
			if(effectedNum<0)
				throw new ProductCategoryOperationException("商品类别更新失败");
		} catch (Exception e) {
			throw new ProductCategoryOperationException("deleteProductCategory error:"+e.getMessage());
		}
		//删除productCategory
		try {
			int effectedNum=productCategoryDao.deleteProductCategory(productCategoryId, shopId);
			if(effectedNum<=0)
				throw new ProductCategoryOperationException("商品类别删除失败");
			else {
				return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
			}
		} catch (Exception e) {
			throw new ProductCategoryOperationException("deleteProductCategory error:"+e.getMessage());
		}
	}
}
