package idv.zwei.animecrawler;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Season {
	private LinkedHashMap<String, String> season = new LinkedHashMap<String, String>();
	
	public Season() {
	}
	
	public void put(String key, String value) {
		season.put(key, value);
	}
	
	public String get(String key) {
		return season.get(key);
	}
	
	public String[] getKeys() {
		return season.keySet().toArray(String[]::new);
	}
	
	public String[] getValues() {
		return season.values().toArray(String[]::new);
	}
	
	public HashMap<String, String> getMap() {
		return season;
	}
}