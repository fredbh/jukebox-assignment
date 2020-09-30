package com.jukebox;

public class SettingWrapper {
	
	private Setting[] settings;
	
	public SettingWrapper() {
		this.settings = null;
	}
	
	public SettingWrapper(Setting[] settings) {
		this.settings = settings;
	}
	
	public Setting[] getSettings() {
		return this.settings;
	}

}
