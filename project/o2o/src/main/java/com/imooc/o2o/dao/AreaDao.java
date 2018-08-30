package com.imooc.o2o.dao;

import java.util.List;

import com.imooc.o2o.entity.Area;
/**
 * 列出区域列表，返回值是areaList
 * @author Administrator
 *
 */
public interface AreaDao {
	List<Area>queryArea();
}
