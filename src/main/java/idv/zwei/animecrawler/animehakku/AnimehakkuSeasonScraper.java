package idv.zwei.animecrawler.animehakku;


import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import idv.zwei.animecrawler.Season;
import idv.zwei.animecrawler.SeasonScraper;

public class AnimehakkuSeasonScraper extends SeasonScraper {
	
	public AnimehakkuSeasonScraper(String source) {
		super(source);
	}

	public Season getSeason() throws IOException {
		var seasonMap = new Season();
		Document summaryPageDoc = Jsoup.connect(source).timeout(30000).get();
		Elements season = summaryPageDoc.select(".selectMenuEnd select option");
		for (Element option: season) {
			String key = option.text();
			String subHrefValue = option.attr("value").trim().replace("/program/", "");

			seasonMap.put(key, subHrefValue);
		}
		return seasonMap;
	}
	
	
}
