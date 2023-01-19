package idv.zwei.animecrawler;

import java.io.IOException;

public abstract class SeasonScraper {
	protected String source;
	
	public SeasonScraper(String source) {
		this.source = source;
	}
	
	public abstract Season getSeason() throws IOException;
}
