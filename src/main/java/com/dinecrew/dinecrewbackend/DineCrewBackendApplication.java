package com.dinecrew.dinecrewbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = {"com.dinecrew.dinecrewbackend.pedidos", "com.dinecrew.dinecrewbackend.usuarios"})
public class DineCrewBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DineCrewBackendApplication.class, args);
	}

}
