package com.github.chaunguyentruongan.warehouse_cdnsg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = PageSerializationMode.VIA_DTO)
@EnableScheduling
public class WarehouseCdnsgApplication {

	public static void main(String[] args) {
		SpringApplication.run(WarehouseCdnsgApplication.class, args);
		System.out.println("CORS ENV = " + System.getenv("APP_CORS_ORIGIN"));
		System.out.println("SECRET ENV = " + System.getenv("APP_SECRET_KEY"));
	}

}
