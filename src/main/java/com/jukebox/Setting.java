package com.jukebox;

public class Setting {
	private final String id;
	private final String[] requires;
	
	public Setting() {
		this.id = "";
		this.requires = new String[]{};
	}
	
	public Setting(String id, String[] requires) {
		this.id = id;
		this.requires = requires;
	}
	
	public String getId() {
		return this.id;
	}
	
	public String[] getRequires() {
		return this.requires;
	}
}
