package com.codefactorygroup.betting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class BettingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BettingApplication.class, args);
	}

}
