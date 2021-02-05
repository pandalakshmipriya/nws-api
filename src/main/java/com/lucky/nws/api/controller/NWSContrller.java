/**
 * 
 */
package com.lucky.nws.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.lucky.nws.api.service.WeatherForecastService;
import com.lucky.nws.api.service.impl.WeatherForecastServiceImpl;

/**
 * @author Lakshmipriya
 *
 */
@RestController
public class NWSContrller {
	
	@Autowired
	private WeatherForecastService service;
	
	@GetMapping("/nws/{latitude},{longitude}/forecast")
	public ResponseEntity<?> getForeCast(@PathVariable double latitude,@PathVariable double longitude){
		ResponseEntity<Object> result = service.getForeCast(latitude,longitude);
		return result;
	}

}
