package com.minimon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
public class MinimonApplication {

	static String driverPath = "D:\\minimon";

	public static String getDriverPath() {
		return driverPath;
	}

	public static void setDriverPath(String driverPath) {
		MinimonApplication.driverPath = driverPath;

	}
	public static void main(String[] args) {
		SpringApplication.run(MinimonApplication.class, args);
	}

}
