package com.imooc.o2o.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.Area;

public class AreaServiceTest extends BaseTest {
	@Autowired
	private AreaService areaService;//一旦写入这句话Spring就会向里面注入实现类
	@Autowired
	private CacheService cacheService;
	@Test
	public void testGetAreaList() {
		List<Area> areaList=areaService.getAreaList();
		assertEquals("西苑",areaList.get(0).getAreaName());
		cacheService.removeFromCache(areaService.AREALISTKEY);
		areaList=areaService.getAreaList();
	}
}
