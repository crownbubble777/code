package com.imooc.o2o.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.ProductDao;
import com.imooc.o2o.dao.ProductImgDao;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductImg;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.exceptions.ProductOperationException;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PageCalculator;
import com.imooc.o2o.util.PathUtil;
@Service
public class ProductServiceImpl implements ProductService  {
	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductImgDao productImgDao;
	@Override
	public ProductExecution getProductList(Product productCondition,int pageIndex,int pageSize) {
		//页码转换成数据库的行码，并调用Dao层取回指定页码的商品列表
		int rowIndex=PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<Product> productList=productDao.queryProductList(productCondition, rowIndex, pageSize);
		//基于同样的查询条件返回该查询条件下的商品总数
		int count=productDao.queryProductCount(productCondition);
		ProductExecution pe=new ProductExecution();
		pe.setProductList(productList);
		pe.setCount(count);
		return pe;
	}

	@Override
	@Transactional
	//1.处理缩略图，获取缩略图相对路径并赋值给product
	//2.往tb_product写入商品信息，获取productId,一旦创建成功，就会把获得的productId赋值给product作为参数
	//3.结合productId批量处理商品详情图
	//4.将商品详情图列表批量插入tb_product_img中
	public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgHolderList)
			throws ProductOperationException {
		//空值判断
		if(product!=null &&product.getShop()!=null && product.getShop().getShopId()!=null) {
			//给商品设置默认的属性
			product.setCreateTime(new Date());
			product.setLastEditTime(new Date());
			//默认上架的状态,就是说在前端展示系统中能够看到这个商品
			product.setEnableStatus(1);
			//若商品缩略图不为空则添加
			if(thumbnail!=null)
				addThumbnail(product,thumbnail);
			//2.添加商品信息
			try {
				//向tb_product中创建商品信息
				int effectedNum=productDao.insertProduct(product);
				//查看是否添加成功
				if(effectedNum<=0)
					throw new ProductOperationException("创建商品失败");
			} catch (Exception e) {
				throw new ProductOperationException(e.toString());
			}
			//若商品详情图不为空则添加
			if(productImgHolderList!=null && productImgHolderList.size()>0) {
				addProductImgList(product,productImgHolderList);
			}
			return new ProductExecution(ProductStateEnum.SUCCESS,product);
		}
		else //传参为空则返回空值错误信息
			return new ProductExecution(ProductStateEnum.EMPTY);
	}
	/**
	 * 批量添加图片
	 * @param product
	 * @param productImgHolderList
	 */
	private void addProductImgList(Product product, List<ImageHolder> productImgHolderList) {
		//获取图片的根路径，这里直接存放在相应的店铺文件夹底下
		String dest=PathUtil.getShopImagePath(product.getShop().getShopId());
		//建立链表便于之后批量的存入数据库中
		List<ProductImg> productImgList=new ArrayList<ProductImg>();
		//遍历图片一次去处理，并添加进productImg实体类中
		for (ImageHolder productImgHolder : productImgHolderList) {
			String imgAddr=ImageUtil.generateNormalImg(productImgHolder,dest);
			ProductImg productImg=new ProductImg();//接收路径
			productImg.setImgAddr(imgAddr);
			productImg.setProductId(product.getProductId());
			productImg.setCreateTime(new Date());
			productImgList.add(productImg);
		}
		//如果确实有图片需要添加，则执行批量添加的操作
		if(productImgList.size()>0) {
			try {
				int effectedNum=productImgDao.batchInsertProductImg(productImgList);
				if(effectedNum<=0)
					throw new ProductOperationException("创建商品详情图片失败");
			} catch (Exception e) {
				throw new ProductOperationException("创建商品详情图片失败:"+e.toString());
			}
		}
	}
	/**
	 * 添加缩略图
	 * @param product
	 * @param thumbnail
	 */
	private void addThumbnail(Product product, ImageHolder thumbnail) {
		//先获取基准路径，以这个路径作为根目录，在它的下面去保存图片
		String dest=PathUtil.getShopImagePath(product.getShop().getShopId());
		String thumbnailAddr=ImageUtil.generateThumbnail(thumbnail, dest);//调用之前写好的genenrateThumbnail方法并返回相对路径
		product.setImgAddr(thumbnailAddr);//将返回的相对路径存在product的图片地址中
	}
	@Override
	public Product getProductById(long productId) {//在商品编辑之前获得商品的信息
		return productDao.queryProductById(productId);
	}
	@Override
	@Transactional
	//1.若缩略图参数有值，则处理缩略图;若原先存在缩略图则先删除再添加新图，之后获取缩略图相对路径并赋值给product
	//2.若商品详情图列表参数有值，对商品详情图列表进行同样的操作(先做物理图片的清除)
	//3.将tb_product_img下面的该商品原先的商品详情图记录全部都删除(再做数据库图片的清除)
	//4.更新tb_product和tb_product_img的信息
	public ProductExecution modifyProduct(Product product, ImageHolder thumbnail,
			List<ImageHolder> productImgHolderList) throws ProductOperationException {
		//空值判断
		if(product!=null&&product.getShop()!=null&&product.getShop().getShopId()!=null) {
			//给商品设置上默认属性
			product.setLastEditTime(new Date());
			//若商品缩略图不为空且原有缩略图不为空则删除原有缩略图并添加
			if(thumbnail!=null) {
				//先获取一遍原有信息，因为原有信息里有原有地址
				Product tempProduct=productDao.queryProductById(product.getProductId());
				if(tempProduct.getImgAddr()!=null) {//如果原有地址不为空，就调用ImageUtil去删除原有路径下的图片
					ImageUtil.deleteFileOrPath(tempProduct.getImgAddr());
				}
				addThumbnail(product, thumbnail);
			}
			//2.如果有新存入的商品详情图，则将原来的先删除，并添加新的图片
			if(productImgHolderList!=null&&productImgHolderList.size()>0) {
				deleteProductImgList(product.getProductId());
				addProductImgList(product, productImgHolderList);
			}
			try {//更新商品信息
				int effectedNum=productDao.updateProduct(product);
				if(effectedNum<=0) {
					throw new ProductOperationException("更新商品信息失败");
				}
				return new ProductExecution(ProductStateEnum.SUCCESS,product);
			} catch (Exception e) {
				throw new ProductOperationException("更新商品信息失败:"+e.toString());
			}
		}else {
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}
	/**
	 * 删除某个商品下的所有详情图
	 * @param productId
	 */
	private void deleteProductImgList(Long productId) {
		//根据productId获取原来的图片
		List<ProductImg> productImgList=productImgDao.queryProductImgList(productId);
		//干掉原来的图片
		for (ProductImg productImg : productImgList) {
			ImageUtil.deleteFileOrPath(productImg.getImgAddr());
		}
		//删除数据库里原有的图片信息
		productImgDao.deleteProductImgByProductId(productId);
	}
	
}
