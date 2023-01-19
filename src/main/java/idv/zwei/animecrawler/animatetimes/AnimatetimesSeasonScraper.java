package idv.zwei.animecrawler.animatetimes;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import idv.zwei.animecrawler.Season;
import idv.zwei.animecrawler.SeasonScraper;

public class AnimatetimesSeasonScraper extends SeasonScraper {

	public AnimatetimesSeasonScraper(String source) {
		super(source);
	}

	@Override
	public Season getSeason() throws IOException {
		var seasonMap = new Season();
		Document summaryPageDoc = Jsoup.connect(source).timeout(30000).get();
		Element container = summaryPageDoc.select(".l-box--article .maybe-legacy").first();
		Element animeListBlock = container.child(1);
		/**
		 *  a lot of <br />
		 *  (#ﾟДﾟ)
		 */
		List<Element> anchors = animeListBlock.select("a")
							.stream()
							.filter(anchor -> anchor.attr("href").startsWith("https://www.animatetimes.com/tag/details") && anchor.text().endsWith("アニメ一覧"))
							.collect(Collectors.toList());
		for (Element a: anchors) {
			String key = a.text();
			String href = a.attr("href").trim();
			seasonMap.put(key, href);
		}
		return seasonMap;
	}
}
