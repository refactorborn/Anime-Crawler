package idv.zwei.animecrawler.factory;

import idv.zwei.animecrawler.Scraper;
import idv.zwei.animecrawler.Source;
import idv.zwei.animecrawler.animatetimes.AnimatetimesScraper;
import idv.zwei.animecrawler.animehakku.AnimehakkuScraper;
import idv.zwei.animecrawler.ui.UpdateListener;

public class ScraperFactory {
	public static Scraper getScraper(String name, UpdateListener listener) {
		if (name == null || listener == null) {
			return null;
		}
		
		if (name.equals(Source.ANIMEHAKKU.getTitle())) {
			return new AnimehakkuScraper(listener);
		} else if (name.equals(Source.ANIMATE_TIMES.getTitle())) {
			return new AnimatetimesScraper(listener);
		}
		return null;
	}
}
