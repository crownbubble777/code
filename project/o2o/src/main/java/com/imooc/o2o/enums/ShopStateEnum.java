package com.imooc.o2o.enums;

public enum ShopStateEnum {
	CHECK(0,"审核中"),OFFLINE(-1,"非法店铺"),SUCCESS(1,"操作成功"),PASS(2,"通过认证"),INNER_ERROR(-1001,"内部系统错误"),NULL_SHOPID(-1002,"ShopId为空"),NULL_SHOP(-1003,"Shop信息为空");
	private int state;
	private String stateInfo;
	//构造器设置成私有的原因是不希望第三方去改变CHECK,....等enum值
	private ShopStateEnum(int state,String stateInfo) {
		this.state=state;
		this.stateInfo=stateInfo;
	}
	/**
	 * 依据传入的state返回响应的enum值
	 */
	public static ShopStateEnum stateOf(int state) {
		for(ShopStateEnum stateEnum:values()) {//value就是包含所有的枚举对象里面的所有的值
			if(stateEnum.getState()==state)
				return stateEnum;
		}
		return null;
	}
	//只用getter方法，不用setter方法，因为不希望程序外面也调用这个方法
	public int getState() {
		return state;
	}
	
	public String getStateInfo() {
		return stateInfo;
	}
	
}
