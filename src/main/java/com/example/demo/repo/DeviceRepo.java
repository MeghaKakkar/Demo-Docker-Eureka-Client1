package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.entity.Device;

@Repository
public interface DeviceRepo extends JpaRepository<Device, Integer> {
	
}
