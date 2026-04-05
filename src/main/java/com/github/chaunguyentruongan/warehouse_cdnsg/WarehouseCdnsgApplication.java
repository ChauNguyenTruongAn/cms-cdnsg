package com.github.chaunguyentruongan.warehouse_cdnsg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WarehouseCdnsgApplication {

	public static void main(String[] args) {
		SpringApplication.run(WarehouseCdnsgApplication.class, args);
	}

}
