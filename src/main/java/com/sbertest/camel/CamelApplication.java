package com.sbertest.camel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;

@EnableKafka
@SpringBootApplication
public class CamelApplication {

	@KafkaListener(topics="fakelogin")
	public void msgListener(String msg){
		System.out.println("Kafka listened: " + msg);
	}

	public static void main(String[] args) {
		SpringApplication.run(CamelApplication.class, args);
	}

}
