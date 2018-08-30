package com.imooc.o2o.web.shopadmin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.dto.Result;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ProductCategoryStateEnum;
import com.imooc.o2o.exceptions.ProductCategoryOperationException;
import com.imooc.o2o.service.ProductCategoryService;

@Controller//说明这个类是Controller的
@RequestMapping("/shopadmin")//设置一个一级路由,标明是在/shopadmin文件夹下面的，是在店家管理系统下面的操作
public class ProductCategoryManagementController {
	
	@Autowired//将其注入Spring框架
	private ProductCategoryService productCategoryService;
	
	@RequestMapping(value="/getproductcategorylist",method=RequestMethod.GET)
	@ResponseBody
	//也可以使用Map来实现,这里因为返回值就只是一个链表，所以用一种新的方法Result来实现
	private Result<List<ProductCategory>> getProductCategoryList(HttpServletRequest request){
		//To be remove
//		Shop shop=new Shop();
//		shop.setShopId(1L);
//		request.getSession().setAttribute("currentShop",shop);
		
		Shop currentShop=(Shop)request.getSession().getAttribute("currentShop");
		List<ProductCategory> list=null;
		if(currentShop!=null && currentShop.getShopId()>0) {
			//如果不为空，那么将商品店铺的信息返回一个list表中，然后封装到Result中返回给前台
			list=productCategoryService.getProductCategoryList(currentShop.getShopId());
			return new Result<List<ProductCategory>>(true,list);
		}
		//如果currentShop为 空，就代表对此商品类别没有操作权限，此时返回错误的信息，不予操作
		else {
			ProductCategoryStateEnum ps=ProductCategoryStateEnum.INNER_ERROR;
			return new Result<List<ProductCategory>>(false,ps.getState(),ps.getStateInfo());
		}
	}
	
	@RequestMapping(value="/addproductcategorys",method=RequestMethod.POST)
	@ResponseBody
	private Map<String,Object> addProductCategorys(@RequestBody List<ProductCategory> productCategoryList,
			HttpServletRequest request){
		Map<String,Object> modelMap=new HashMap<String, Object>();//定义一个Map用来接收返回值类型
		Shop currentShop=(Shop)request.getSession().getAttribute("currentShop");
		for(ProductCategory pc:productCategoryList) {
			pc.setShopId(currentShop.getShopId());
		}
		if(productCategoryList!=null && productCategoryList.size()>0) {
			try {
				ProductCategoryExecution pe=productCategoryService.batchAddProductCategory(productCategoryList);
				if(pe.getState()==ProductCategoryStateEnum.SUCCESS.getState()) {
					modelMap.put("success",true);

				}
				else {
					modelMap.put("success",false);
					modelMap.put("errMsg",pe.getStateInfo());
				}
			} catch (ProductCategoryOperationException e) {
				modelMap.put("success",false);
				modelMap.put("errMsg",e.toString());
				return modelMap;
			}
		}
		else {
			modelMap.put("success",false);
			modelMap.put("errMsg","请至少输入一个商品类别");
		}
		return modelMap;
	}
	@RequestMapping(value="/removeproductcategory",method=RequestMethod.POST)
	@ResponseBody
	private Map<String,Object> removeProductCategory(Long productCategoryId,HttpServletRequest request){
		Map<String, Object> modelMap=new HashMap<String, Object>();
		if(productCategoryId!=null && productCategoryId>0 ) {
			try {
				Shop currentShop=(Shop)request.getSession().getAttribute("currentShop");//request主要是用来取出Session对象的
				ProductCategoryExecution pe=productCategoryService.deleteProductCategory(productCategoryId,currentShop.getShopId());
				if(pe.getState()==ProductCategoryStateEnum.SUCCESS.getState()) {
					modelMap.put("success",true);
				}
				else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			} catch (ProductCategoryOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg",e.toString());
				return modelMap;
			}
		}
		else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请至少选择一个商品类别");
		}
		return modelMap;
	}
}
