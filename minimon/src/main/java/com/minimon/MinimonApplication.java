package com.minimon;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MinimonApplication {
	
	static String driverPath = "";

	
	
	public static String getDriverPath() {
		return driverPath;
	}



	public static void setDriverPath(String driverPath) {
		MinimonApplication.driverPath = driverPath+File.separator+"chromedriver.exe";
	}



	public static void main(String[] args) {
		SpringApplication.run(MinimonApplication.class, args);
	}

}
