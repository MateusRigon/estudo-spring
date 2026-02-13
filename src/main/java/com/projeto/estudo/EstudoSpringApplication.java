package com.projeto.estudo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;

@EnableRabbit
@SpringBootApplication
public class EstudoSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(EstudoSpringApplication.class, args);
	}

}
