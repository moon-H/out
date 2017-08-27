package com.whl.client.gateway.model.wallet;

public class MobileModel {
 private String modelNum;
 private String modelName;
 private String manufacturer;
public String getModelNum() {
	return modelNum;
}
public void setModelNum(String modelNum) {
	this.modelNum = modelNum;
}
public String getModelName() {
	return modelName;
}
public void setModelName(String modelName) {
	this.modelName = modelName;
}
public String getManufacturer() {
	return manufacturer;
}
public void setManufacturer(String manufacturer) {
	this.manufacturer = manufacturer;
}
@Override
public String toString() {
	return "MobileModel [modelNum=" + modelNum + ", modelName=" + modelName
			+ ", manufacturer=" + manufacturer + "]";
}
 
 
 
}
