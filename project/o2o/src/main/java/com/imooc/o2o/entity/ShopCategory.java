package com.imooc.o2o.entity;

import java.util.Date;

public class ShopCategory {
	private Long ShopCategoryId;
	private String ShopCategoryName;
	private String ShopCategoryDesc;//店铺描述
	private String shopCategoryImg;
	private Integer priority;//权重
	private Date createTime;
	private Date lastEditTime;
	private ShopCategory parent;//上级ID
	public Long getShopCategoryId() {
		return ShopCategoryId;
	}
	public void setShopCategoryId(Long shopCategoryId) {
		ShopCategoryId = shopCategoryId;
	}
	public String getShopCategoryName() {
		return ShopCategoryName;
	}
	public void setShopCategoryName(String shopCategoryName) {
		ShopCategoryName = shopCategoryName;
	}
	public String getShopCategoryDesc() {
		return ShopCategoryDesc;
	}
	public void setShopCategoryDesc(String shopCategoryDesc) {
		ShopCategoryDesc = shopCategoryDesc;
	}
	public String getShopCategoryImg() {
		return shopCategoryImg;
	}
	public void setShopCategoryImg(String shopCategoryImg) {
		this.shopCategoryImg = shopCategoryImg;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastEditTime() {
		return lastEditTime;
	}
	public void setLastEditTime(Date lastEditTime) {
		this.lastEditTime = lastEditTime;
	}
	public ShopCategory getParent() {
		return parent;
	}
	public void setParent(ShopCategory parent) {
		this.parent = parent;
	}

}
