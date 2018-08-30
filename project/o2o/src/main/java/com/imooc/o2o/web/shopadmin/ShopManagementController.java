package com.imooc.o2o.web.shopadmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exceptions.ShopOperationException;
import com.imooc.o2o.service.AreaService;
import com.imooc.o2o.service.ShopCategoryService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {
	@Autowired
	private ShopService shopService;
	@Autowired 
	private ShopCategoryService shopCategoryService;
	@Autowired
	private AreaService areaService;
	@RequestMapping(value="/getshopmanagementinfo",method=RequestMethod.GET)
	@ResponseBody
	private Map<String,Object> getShopManagementInfo(HttpServletRequest request){//去管理Session的操作
		Map<String,Object>modelMap=new HashMap<String, Object>();
		long shopId=HttpServletRequestUtil.getLong(request, "shopId");
		if(shopId<=0) {
			Object currentShopObj=request.getSession().getAttribute("currentShop");//如果前端获取不到，就尝试通过Session获得
			if(currentShopObj==null) {
				modelMap.put("redirect",true);//如果还是获取不到，则请求重定向
				modelMap.put("url","/o2o/shopadmin/shoplist");
			}
			else {
				Shop currentShop=(Shop)currentShopObj;
				modelMap.put("redirect",false);
				modelMap.put("shopId",currentShop.getShopId());
			}
		}else {
			Shop currentShop=new Shop();
			currentShop.setShopId(shopId);
			request.getSession().setAttribute("currentShop",currentShop);
			modelMap.put("redirect",false);
		}
		return modelMap;
		
	}
	@RequestMapping(value="/getshoplist",method=RequestMethod.GET)
	@ResponseBody
	private Map<String,Object> getShopList(HttpServletRequest request){//根据用户的信息返回用户所创建的店铺列表
		Map<String,Object> modelMap=new HashMap<String, Object>();
		PersonInfo user=new PersonInfo();
		user.setUserId(1L);
		user.setName("test");
		request.getSession().setAttribute("user",user);
		user=(PersonInfo)request.getSession().getAttribute("user");
		try {
			Shop shopCondition=new Shop();
			shopCondition.setOwner(user);
			ShopExecution se=shopService.getShopList(shopCondition,0,100);
			modelMap.put("shopList",se.getShopList());
			modelMap.put("user",user);
			modelMap.put("success",true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg",e.getMessage());
		}
		return modelMap;
	}
	//指定访问路径
	@RequestMapping(value="/getshopinitinfo",method=RequestMethod.GET)//设置好路由，因为不需要传递任何参数，所以直接用GET方法即可
	//返回成JSON串
	@ResponseBody
	private Map<String,Object> getShopInitInfo(){
		//首先定义一个返回值对象
		Map<String, Object> modelMap=new HashMap<String, Object>();
		//定义一个List来接收ShopCategory的信息
		List<ShopCategory> shopCategoryList=new ArrayList<ShopCategory>();
		//定义一个List来接收区域相关的信息
		List<Area> areaList=new ArrayList<Area>();
		try {//获取两个店铺信息和区域信息、赋值到两个链表然后返回到前台，如果出现错误，就直接放到catch块中
			shopCategoryList=shopCategoryService.getShopCategoryList(new ShopCategory());//获取全部的对象
			areaList=areaService.getAreaList();
			modelMap.put("shopCategoryList",shopCategoryList);
			modelMap.put("areaList",areaList);
			modelMap.put("success",true);
		} catch (Exception e) {
			modelMap.put("success",false);
			modelMap.put("errMsg",e.getMessage());
			
		}
		return modelMap;
	}
	@RequestMapping(value="/getshopbyid",method=RequestMethod.GET)//写入路由，都是小写
	@ResponseBody//返回值是一个JSON串
	private Map<String,Object>getShopById(HttpServletRequest request){
		Map<String, Object>modelMap=new HashMap<String, Object>();
		Long shopId=HttpServletRequestUtil.getLong(request, "shopId");
		if(shopId>-1) {//表示前端已经传进来了
			try {
				Shop shop=shopService.getByShopId(shopId);//获取店铺信息
				List<Area> areaList=areaService.getAreaList();//获取区域列表
				modelMap.put("shop",shop);
				modelMap.put("areaList",areaList);
				modelMap.put("success",true);
			} catch (Exception e) {
				modelMap.put("success",false);
				modelMap.put("errMsg",e.toString());
			}
		}
		else {
			modelMap.put("success",false);
			modelMap.put("errMsg","empty shopId");
		}
		return modelMap;
	}
	
	@RequestMapping(value="/registershop",method=RequestMethod.POST)
	//返回值类型是map类型的，我们需要转换成json类型
	@ResponseBody
	private Map<String,Object> registerShop(HttpServletRequest request){
		Map<String,Object> modelMap=new HashMap<String,Object>();
		if(!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success",false);
			modelMap.put("errMsg","输入了错误的验证码");
			return modelMap;
		}
		//1.接收并转换相应的参数，包括店铺信息以及图片信息
		//首先获取前端传来的店铺信息，并将其转换成实体类
		String shopStr=HttpServletRequestUtil.getString(request, "shopStr");
		ObjectMapper mapper=new ObjectMapper();
		Shop shop=null;
		try {
			shop=mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			modelMap.put("success",false);
			modelMap.put("errMsg",e.getMessage());
			return modelMap;
		}
		//获取前端传来的文件流，并将其保存在shopImg中
		CommonsMultipartFile shopImg=null;//接收图片信息
		//从request本次会话的上下文文件获取相关的内容
		CommonsMultipartResolver commonsMultipartResolver=new CommonsMultipartResolver(request.getSession().getServletContext());
		if(commonsMultipartResolver.isMultipart(request)) {//判断request里面是否有上传的文件流
			MultipartHttpServletRequest multipartHttpServletRequest=(MultipartHttpServletRequest)request;//强制类型转化
			shopImg=(CommonsMultipartFile)multipartHttpServletRequest.getFile("shopImg");
			
		}
		else {
			modelMap.put("success",false);
			modelMap.put("errMsg","上传图片不能为空");
			return modelMap;
		}
		//2.注册店铺
		if(shop!=null && shopImg!=null) {
			//Session TODO
			PersonInfo owner=(PersonInfo)request.getSession().getAttribute("user");//通过Session获取用户的信息
			///PersonInfo owner = new PersonInfo();
			//owner.setUserId(1L);
			shop.setOwner(owner);//将获取到的信息set到Owner里面去
			ShopExecution se;
			try {
				ImageHolder imageHolder=new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
				se=shopService.addShop(shop, imageHolder);
				if(se.getState()==ShopStateEnum.CHECK.getState()) {
					modelMap.put("success",true);
					//该用户可以操作的店铺列表
					@SuppressWarnings("unchecked")
					List<Shop> shopList=(List<Shop>)request.getSession().getAttribute("ShopList");
					if(shopList==null ||shopList.size()==0) {//如果为空就创建出来
						shopList=new ArrayList<Shop>();
					}
					shopList.add(se.getShop());
					request.getSession().setAttribute("shopList",shopList);
					//该用户可以操作的店铺列表
					//List<Shop> shopList=(List<Shop>)request.getSession().getAttribute("shopList");
					if(shopList==null || shopList.size()==0) {
						shopList=new ArrayList<Shop>();
					}
					shopList.add(se.getShop());
					request.getSession().setAttribute("shopList",shopList);
				}else {
					modelMap.put("success",false);
					modelMap.put("errMsg",se.getStateInfo());
				}
			} catch (ShopOperationException e) {
				modelMap.put("success",false);
				modelMap.put("errMsg",e.getMessage());
			}catch (IOException e) {
				modelMap.put("success",false);
				modelMap.put("errMsg",e.getMessage());
			}
			return modelMap;
		}else {
			modelMap.put("success",false);
			modelMap.put("errMsg","请输入店铺信息");
			return modelMap;
		}
	}
	
	@RequestMapping(value="/modifyshop",method=RequestMethod.POST)
	@ResponseBody
	private Map<String,Object>modifyShop(HttpServletRequest request){
		Map<String,Object> modelMap=new HashMap<String, Object>();
		if(!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success",false);
			modelMap.put("errMsg","输入错误的验证码");
			return modelMap;
		}
		//1.接收并转化响应的参数，包括店铺信息以及图片信息
		String shopStr=HttpServletRequestUtil.getString(request,"shopStr");//接收shopStr这个对象
		ObjectMapper mapper=new ObjectMapper();
		Shop shop=null;
			try {
				shop=mapper.readValue(shopStr, Shop.class);//将shopStr转化成Shop实体类
			} catch (Exception e) {//做一些异常的判断
				modelMap.put("success",false);
				modelMap.put("errMsg",e.getMessage());
				return modelMap;
			}
			CommonsMultipartFile shopImg=null;//接收上传过来的文件流
			CommonsMultipartResolver commonsMultipartResolver=new CommonsMultipartResolver(
					request.getSession().getServletContext());
			if(commonsMultipartResolver.isMultipart(request)) {//图片是可上传可不上传
				MultipartHttpServletRequest multipartHttpServletRequest=(MultipartHttpServletRequest)request;
				shopImg=(CommonsMultipartFile)multipartHttpServletRequest.getFile("shopImg");
			}
			//2.修改店铺信息
			if(shop!=null&& shop.getShopId()!=null) {
				PersonInfo owner=new PersonInfo();//首先获取会话里面的用户信息
				owner.setUserId(1L);
				shop.setOwner(owner);
				ShopExecution se;
				try {
					if(shopImg==null)//传入的文件可以为空,那么只是传入一个空
						se=shopService.modifyShop(shop,null);
					else {//如果不为空
						ImageHolder imageHolder=new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());
						se=shopService.modifyShop(shop,imageHolder);
					}
					if(se.getState()==ShopStateEnum.SUCCESS.getState())//在店铺注册的时候默认是在审核中，在modifyshop中为success
						modelMap.put("success",true);
					else {
						modelMap.put("success", false);
						modelMap.put("errMsg",se.getStateInfo());
					}
				} catch (ShopOperationException e) {
					modelMap.put("success",false);
					modelMap.put("errMsg",e.getMessage());
				}catch (IOException e) {
					modelMap.put("success",false);
					modelMap.put("errMsg",e.getMessage());
				}
				return modelMap;
			}
			else {
				modelMap.put("success",false);
				modelMap.put("errMsg","请输入店铺信息");
				return modelMap;
			}
	}
}
