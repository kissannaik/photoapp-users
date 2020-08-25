package com.kissan.photoappusers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class PhotoappUsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhotoappUsersApplication.class, args);
	}

}
