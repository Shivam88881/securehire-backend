package com.curtin.securehire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.curtin.securehire.repository.db")
@EnableElasticsearchRepositories("com.curtin.securehire.repository.es")
public class SecurehireApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurehireApplication.class, args);
	}

}
