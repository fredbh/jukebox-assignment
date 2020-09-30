package com.jukebox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.boot.web.client.RestTemplateBuilder;

@SpringBootApplication
@EnableCaching
public class JukeboxRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(JukeboxRestApplication.class, args);
	}

}
