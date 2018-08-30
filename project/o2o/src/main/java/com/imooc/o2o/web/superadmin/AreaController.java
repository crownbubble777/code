package com.imooc.o2o.web.superadmin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.o2o.entity.Area;
import com.imooc.o2o.service.AreaService;

@Controller
@RequestMapping("/superadmin")
public class AreaController {
	Logger logger=LoggerFactory.getLogger(AreaController.class);
	@Autowired
	private AreaService areaService;
	@RequestMapping(value="/listarea", method=RequestMethod.GET)//了解get和post
	@ResponseBody//自动的返回一个jison对象
	private Map<String,Object> listArea(){
		logger.info("=====start========");
		long startTime=System.currentTimeMillis();
		Map<String,Object> modelMap=new HashMap<String,Object>();//用来存放方法的返回值
		List<Area> list=new ArrayList<Area>();
	try {
		list=areaService.getAreaList();
		modelMap.put("rows",list);
		modelMap.put("total",list.size());
	}catch(Exception e) {
		e.printStackTrace();
		modelMap.put("success", false);
		modelMap.put("errMsg",e.toString());
	}
	logger.error("test error!");
	long endTime=System.currentTimeMillis();
	logger.debug("costTime:[{}ms]",endTime-startTime);//方便调试
	logger.info("=====end=====");
	return modelMap;
}
}

