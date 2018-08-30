package com.imooc.o2o.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserAccessToken {
	//这几个变量中最重要的是access_token和openid
	//获取到的凭证,加这个标签的作用就是希望通过这个操作直接将值转换成对应的实体类中的字段的值
	@JsonProperty("access_token")
	private String accessToken;
	//凭证有效时间，单位：秒
	@JsonProperty("expires_in")
	private String expiresIn;
	//表示更新令牌，用来获取下一次访问令牌，这里没有太大的用处
	@JsonProperty("refresh_token")
	private String refreshToken;
	//该用户在此公众号下的身份标识，对于此微信号具有唯一性
	@JsonProperty("openid")
	private String openId;
	//表示权限范围，这里可以忽略
	@JsonProperty("scope")
	private String scope;
	
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	
}
