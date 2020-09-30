package com.jukebox;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Jukebox {
	private final String id;
	private final String model;
	private Component[] components;
	
	public Jukebox() {
		this.id = "";
		this.model = "";
		this.components = null;
	}
	
	public Jukebox(String id, String model, Component[] components) {
		this.id = id;
		this.model = model;
		this.components = components;
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getModel() {
		return this.model;
	}
	
	public Component[] getComponents() {
		return this.components;
	}
	
	public void setComponents(Component[] components) {
		this.components = components;
	}
}
