package com.example.demo.model;

import java.util.List;
import com.example.demo.entity.Device;

public class RequiredResponseDevice {
	
	private Device device;
	
	private List<Certificate> certificateList;

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public List<Certificate> getCertificateList() {
		return certificateList;
	}

	public void setCertificateList(List<Certificate> certificateList) {
		this.certificateList = certificateList;
	}
	
}
