package com.imooc.o2o.dto;
/**
 * 封装json对象，所有返回结果都使用它
 * @author Administrator
 *
 */
public class Result<T> {//写法和ShopExecution差不多，只不过是一个泛型类
	private boolean success;//是否成功的标志
	private T data;//成功返回的数据，接收的是某一个店铺下面的商品类别列表
	private String errorMsg;//错误信息
	private int errorCode;//错误的状态码
	
	//成功的构造器
	public Result(boolean success,T data) {
		this.success=success;
		this.data=data;
	}
	
	//错误时的构造器
	public Result(boolean success,int errorCode,String errorMsg) {
		this.success=success;
		this.errorMsg=errorMsg;
		this.errorCode=errorCode;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}
