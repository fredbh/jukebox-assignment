package com.jukebox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

@Component
public class SimpleJukeboxRepository implements JukeboxRepository {

	@Override
	@Cacheable("jukeboxes")
	public ArrayList<Jukebox> getAllJukeboxes() {
		RestTemplate restTemplate = new RestTemplate();
		// this is where we call the API and build the jukeboxes from the return
		ArrayList<Jukebox> allJukeboxes = new ArrayList<Jukebox>();
		Jukebox[] jukeboxArray = restTemplate.getForObject("http://my-json-server.typicode.com/touchtunes/tech-assignment/jukes", Jukebox[].class);
		
		for (int i = 0; i < jukeboxArray.length; i++) {
			allJukeboxes.add(jukeboxArray[i]);
		}
		
		return allJukeboxes;
	}

}
