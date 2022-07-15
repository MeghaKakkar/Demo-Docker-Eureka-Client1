package com.example.demo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Device {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int deviceId;
	
	private String deviceType;
	
	private String printerAddress;
	
	private int timeoutCount;
	
	private boolean checkMonitored;

	private int certificateId;

	public int getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getPrinterAddress() {
		return printerAddress;
	}

	public void setPrinterAddress(String printerAddress) {
		this.printerAddress = printerAddress;
	}

	public int getTimeoutCount() {
		return timeoutCount;
	}

	public void setTimeoutCount(int timeoutCount) {
		this.timeoutCount = timeoutCount;
	}
	
	public boolean isCheckMonitored() {
		return checkMonitored;
	}

	public void setCheckMonitored(boolean checkMonitored) {
		this.checkMonitored = checkMonitored;
	}
	
	public int getCertificateId() {
		return certificateId;
	}

	public void setCertificateId(int certificateId) {
		this.certificateId = certificateId;
	}
	
}
