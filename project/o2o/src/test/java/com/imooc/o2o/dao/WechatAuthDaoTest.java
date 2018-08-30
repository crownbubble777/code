package com.imooc.o2o.dao;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.WechatAuth;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WechatAuthDaoTest extends BaseTest{
	@Autowired
	private WechatAuthDao wechatAuthDao;
	@Test
	public void testAInsertWechatAuth() throws Exception{
		//新增一条微信账号
		WechatAuth wechatAuth=new WechatAuth();
		PersonInfo personInfo=new PersonInfo();
		personInfo.setUserId(1L);
		//给微信绑定上用户信息
		wechatAuth.setPersonInfo(personInfo);
		//随意设置上openId
		wechatAuth.setOpenId("dafffgg");
		wechatAuth.setCreateTime(new Date());
		int effectedNum=wechatAuthDao.insertWechatAuth(wechatAuth);
		assertEquals(1,effectedNum);
	}
	@Test
	public void testBQueryWechatAuthByOpenId() throws Exception{
		WechatAuth wechatAuth=wechatAuthDao.queryWechatInfoByOpenId("dafffgg");
		assertEquals("测试",wechatAuth.getPersonInfo().getName());
	}
}
