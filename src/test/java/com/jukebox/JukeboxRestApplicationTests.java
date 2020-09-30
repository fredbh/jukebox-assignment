package com.jukebox;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

import com.jukebox.JukeboxController;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class JukeboxRestApplicationTests {
	
	@LocalServerPort
	private int port;

	@Autowired
	private JukeboxController controller;
	
	@Autowired
	private TestRestTemplate restTemplate;


	@Test
	public void contextLoads() throws Exception {
		assertThat(controller).isNotNull();
	}
	
	@Test
	public void jukeboxShouldReturnAnObjectMessage() throws Exception {
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/jukeboxes?settingId=207797de-5857-4c60-a69b-80eea28bcce8",
				String.class)).contains("id");
	}
	
	@Test
	public void jukeboxShouldReturnAModelMessage() throws Exception {
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/jukeboxes?settingId=207797de-5857-4c60-a69b-80eea28bcce8&model=fusion",
				String.class)).contains("fusion");
	}
	
	@Test
	public void jukeboxShouldReturnNothingIfLimitZeroMessage() throws Exception {
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/jukeboxes?settingId=207797de-5857-4c60-a69b-80eea28bcce8&limit=0",
				String.class)).contains("[]");
	}
	
	@Test
	public void jukeboxShouldReturnNothingWithOffsetZeroMessage() throws Exception {
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/jukeboxes?settingId=207797de-5857-4c60-a69b-80eea28bcce8&offset=0",
				String.class)).contains("[]");
	}
}
