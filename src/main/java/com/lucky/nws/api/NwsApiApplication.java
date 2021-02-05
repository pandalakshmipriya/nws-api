package com.lucky.nws.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.lucky.nws.api.config","com.lucky.nws.api.controller","com.lucky.nws.api.service"
		,"com.lucky.nws.api.service.impl"})
public class NwsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(NwsApiApplication.class, args);
	}

}
