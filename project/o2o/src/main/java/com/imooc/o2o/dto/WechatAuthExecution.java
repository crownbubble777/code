package com.imooc.o2o.dto;

import java.util.List;

import com.imooc.o2o.entity.WechatAuth;
import com.imooc.o2o.enums.WechatAuthStateEnum;

public class WechatAuthExecution {
	//结果状态
	private int state;
	//状态标识
	private String stateinfo;
	private int count;
	private WechatAuth wechatAuth;
	private List<WechatAuth> wechatAuthList;
	public WechatAuthExecution() {
	}
	//失败的构造器
	public WechatAuthExecution(WechatAuthStateEnum stateEnum) {
		this.state=stateEnum.getState();
		this.stateinfo=stateEnum.getStateInfo();
	}
	
	//成功的构造器
	public WechatAuthExecution(WechatAuthStateEnum stateEnum,WechatAuth wechatAuth) {
		this.state=stateEnum.getState();
		this.stateinfo=stateEnum.getStateInfo();
		this.wechatAuth=wechatAuth;
	}
	//成功的构造器
	public WechatAuthExecution(WechatAuthStateEnum stateEnum,
			List<WechatAuth> wechatAuthList) {
		this.state=stateEnum.getState();
		this.stateinfo=stateEnum.getStateInfo();
		this.wechatAuthList=wechatAuthList;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getStateinfo() {
		return stateinfo;
	}
	public void setStateinfo(String stateinfo) {
		this.stateinfo = stateinfo;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public WechatAuth getWechatAuth() {
		return wechatAuth;
	}
	public void setWechatAuth(WechatAuth wechatAuth) {
		this.wechatAuth = wechatAuth;
	}
	public List<WechatAuth> getWechatAuthList() {
		return wechatAuthList;
	}
	public void setWechatAuthList(List<WechatAuth> wechatAuthList) {
		this.wechatAuthList = wechatAuthList;
	}
	
	

}
