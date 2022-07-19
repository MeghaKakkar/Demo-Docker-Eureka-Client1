package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.example.demo.entity.Device;
import com.example.demo.model.Certificate;
import com.example.demo.model.RequiredResponseDevice;
import com.example.demo.repo.DeviceRepo;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/device")
public class DeviceController {

	@Autowired
	private DeviceRepo deviceRepo;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public static final String CERT_SERVICE ="certificateservice" ;
	
	@RequestMapping(path ="/test")
	public ResponseEntity<String> test() {
		return new ResponseEntity<>("Welcome to DeviceController", HttpStatus.OK);
	}
	
	@GetMapping(path = "/getDevices")
	public ResponseEntity<java.util.List<Device>> getAllDevices() {
		List<Device> listDevice = deviceRepo.findAll();
		return new ResponseEntity<>(listDevice, HttpStatus.OK);
	}
	
	@GetMapping(path = "/getDeviceById/{deviceId}")
	public ResponseEntity<List<Device>> getDeviceById(@PathVariable Integer deviceId) {
		Optional<Device> device = deviceRepo.findById(deviceId);
		List<Device> deviceList = new ArrayList<Device>();
		if (device.isPresent()) {
			deviceList.add(device.get());
		}
		return new ResponseEntity(deviceList, HttpStatus.OK);
	}
	
	@PostMapping(path = "/addDevice")
	public ResponseEntity<Device> addDevice(@RequestBody Device device) {
		return new ResponseEntity<>(deviceRepo.save(device), HttpStatus.OK);
	}
	
	@PostMapping(path ="/addDeviceWithCert")
	public ResponseEntity<Device> addDeviceWithCert(@RequestBody Device newDevice) {
		System.out.println("Device received : "+newDevice.getDeviceId()+" "+newDevice.isCheckMonitored()+" "+newDevice.getCertificateId());
		List<Certificate> certificate = new ArrayList<Certificate>();
		certificate = restTemplate.getForObject("http://CERTIFICATE-SERVICE/certificate/getCert/"+newDevice.getCertificateId(), List.class);
		if(certificate.isEmpty()) {
			return new ResponseEntity("Invalid Certificate", HttpStatus.BAD_REQUEST);
		} else {
			Device device = deviceRepo.save(newDevice);
			return new ResponseEntity<>(device, HttpStatus.OK);
		}
	}
	
	@GetMapping(path = "/getDevicesAndCertificateDetails/{deviceId}")
	@CircuitBreaker(name=CERT_SERVICE,fallbackMethod = "handleServiceTimeout")
	public ResponseEntity<RequiredResponseDevice> getDevicesAndCertificateDetails(@PathVariable Integer deviceId) {
		RequiredResponseDevice requiredResponse=new RequiredResponseDevice();
		List<Device> listOfDevices=new ArrayList<Device>();
		Optional<Device> device=deviceRepo.findById(deviceId);
		if(device.isPresent()) {
			listOfDevices.add(device.get());
			requiredResponse.setDevice(device.get());
			ParameterizedTypeReference<List<Certificate>> typeRef=new ParameterizedTypeReference<List<Certificate>>() {
			};
			ResponseEntity<List<Certificate>> responseEntity=restTemplate.exchange("http://CERTIFICATE-SERVICE/certificate/getCert/"+device.get().getCertificateId(), HttpMethod.GET, null, typeRef);
			List<Certificate> certificates=responseEntity.getBody();
			if(!certificates.isEmpty()) {
				requiredResponse.setCertificateList(certificates);
			}
		}
		return new ResponseEntity<RequiredResponseDevice>(requiredResponse, HttpStatus.OK);
	}
	
	public ResponseEntity<RequiredResponseDevice> handleServiceTimeout(@PathVariable Integer deviceId, Exception e) {
		RequiredResponseDevice requiredResponse=new RequiredResponseDevice();
		Device device=deviceRepo.findById(deviceId).get();
		requiredResponse.setDevice(device);
		return new ResponseEntity<RequiredResponseDevice>(requiredResponse, HttpStatus.OK);
	}
	
}
