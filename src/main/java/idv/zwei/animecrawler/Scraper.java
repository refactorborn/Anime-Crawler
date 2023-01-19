package idv.zwei.animecrawler;

import idv.zwei.animecrawler.ui.UpdateListener;

public abstract class Scraper {
	// for update UI progress bar
	protected UpdateListener listener;
	protected String uri;
	protected String filepath;
	
	public Scraper(UpdateListener listener, String uri, String filepath) {
		this.listener = listener;
		this.uri = uri;
		this.filepath = filepath;
	}
	
	public abstract void start();
}
