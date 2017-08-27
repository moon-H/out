package com.whl.client.gateway.model.wristband;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

public class Device extends DataSupport implements Serializable{
	private String name = "";
	private String address = "";
	private String defaultLanuch ;

	public Device() {
	}
	
	public Device(String name, String address, String defaultLanuch) {
		this.name = name;
		this.address = address;
		this.defaultLanuch =defaultLanuch;
	}
	
	public Device(String name, String address) {
		this.name = name;
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDefaultLanuch() {
		return defaultLanuch;
	}

	public void setDefaultLanuch(String defaultLanuch) {
		this.defaultLanuch = defaultLanuch;
	}

	@Override
	public String toString() {
		return "Device{" +
				"name='" + name + '\'' +
				", address='" + address + '\'' +
				", defaultLanuch='" + defaultLanuch + '\'' +
				'}';
	}
}
