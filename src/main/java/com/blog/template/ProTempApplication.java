package com.blog.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author 19624
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class ProTempApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProTempApplication.class, args);
	}

}
