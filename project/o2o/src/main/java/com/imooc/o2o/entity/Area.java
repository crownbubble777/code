package com.imooc.o2o.entity;

import java.util.Date;

public class Area {
	//在本类中都使用引用数据类型，这样默认为空;基本数据类型会给变量附上初值
		private Integer areaId;//区域ID
		private String areaName;//区域名称
		private Integer priority;//权重，权重越大，区域越靠前
		private Date createTime;//创建时间
		private Date lastEditTime;//更新时间
		
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
		
		public Integer getAreaId() {
			return areaId;
		}
		public void setAreaId(Integer areaId) {
			this.areaId = areaId;
		}
		public String getAreaName() {
			return areaName;
		}
		public void setAreaName(String areaName) {
			this.areaName = areaName;
		}
		public Date getLastEditTime() {
			return lastEditTime;
		}
		public void setLastEditTime(Date lastEditTime) {
			this.lastEditTime = lastEditTime;
		}
	
}
