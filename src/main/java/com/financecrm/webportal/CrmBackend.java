package com.financecrm.webportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableMongoRepositories
public class CrmBackend {

	public static void main(String[] args) {
		SpringApplication.run(CrmBackend.class, args);
	}

}
