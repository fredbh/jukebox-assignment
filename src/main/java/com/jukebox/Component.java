package com.jukebox;

public class Component {

	private final String name;

	public Component() {
		this.name = "";
	}
	
	public Component(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean compare(String name) {
		return this.name.equals(name);
	}
}
