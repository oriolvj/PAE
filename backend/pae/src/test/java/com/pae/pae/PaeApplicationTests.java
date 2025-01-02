package com.pae.pae;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PaeApplicationTests {

	@Test
	void contextLoads() {
		System.out.println(System.getenv("DB_USER"));
		System.out.println(System.getenv("DB_URL"));
		System.out.println(System.getenv("DB_PASSWORD"));
	}

}
