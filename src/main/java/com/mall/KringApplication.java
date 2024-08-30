package com.mall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import io.github.cdimascio.dotenv.Dotenv;

@ComponentScan(basePackages = {"com.mall", "com.mall2"})

@SpringBootApplication
public class KringApplication {

	public static void main(String[] args) {
		
		Dotenv dotenv = Dotenv.configure()
				.filename("test.env")
				.load();
		
		System.setProperty("DB_DCN", dotenv.get("DB_DCN"));
		System.setProperty("DB_URL", dotenv.get("DB_URL"));
		System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));		
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
		
	    System.setProperty("API_KEY_KAKAOMAP", dotenv.get("API_KEY_KAKAOMAP"));
	    System.setProperty("API_KEY_PAYMENT", dotenv.get("API_KEY_PAYMENT"));		

	    System.out.println(dotenv.get("DB_USERNAME"));	    
				
		SpringApplication.run(KringApplication.class, args);
	}

}
