package com.jukebox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

@Component
public class SimpleSettingRepository implements SettingRepository {
	
	@Override
	@Cacheable("settings")
	public HashMap<String, Setting> getAllSettings() {
		RestTemplate restTemplate = new RestTemplate();
		// this is where we call the API and build the settings from the return
		HashMap<String, Setting> allSettings = new HashMap<String, Setting>();
		SettingWrapper settingWrap = restTemplate.getForObject("http://my-json-server.typicode.com/touchtunes/tech-assignment/settings", SettingWrapper.class);
		Setting[] settingArray = settingWrap.getSettings();
		for (int i = 0; i < settingArray.length; i++) {
			allSettings.put(settingArray[i].getId(), settingArray[i]);
		}
		return allSettings;
	};
}
