package com.lavinia.pae;

import org.springframework.boot.SpringApplication;

public class TestPaeApplication {

	public static void main(String[] args) {
		SpringApplication.from(PaeApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
