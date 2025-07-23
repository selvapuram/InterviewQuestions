package com.travelperk.testapp;

import org.springframework.boot.SpringApplication;

public class TestApplicationWeb {

	public static void main(String[] args) {
		SpringApplication.from(Application::main).with(TestContainersConfiguration.class).run(args);
	}

}
