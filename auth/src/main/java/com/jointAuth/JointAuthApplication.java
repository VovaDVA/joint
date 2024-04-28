package com.jointAuth;

import 	org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EntityScan({"com.jointAuth.model.user", "com.jointAuth.model.profile"})
@EnableJpaRepositories({"com.jointAuth"})
@EnableScheduling
@SpringBootApplication
public class JointAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(JointAuthApplication.class, args);
	}

}
