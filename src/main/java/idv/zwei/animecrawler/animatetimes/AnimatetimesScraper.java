package idv.zwei.animecrawler.animatetimes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import idv.zwei.animecrawler.Information;
import idv.zwei.animecrawler.Scraper;
import idv.zwei.animecrawler.Season;
import idv.zwei.animecrawler.ui.UpdateListener;

/* 
 * div.l-content
 * div.maybe-legacy tag-content
 * div.l-article responsive-iframe basic-style
 * div align="left"
 * -------------
 * c-heading-h2 id=?
 * div align="left"
 * 
 * 
 * This page element is so terrible. Σ(ﾟДﾟ)
 */
public class AnimatetimesScraper extends Scraper {

	public AnimatetimesScraper(UpdateListener listener) {
		super(listener);
	}

	@Override
	public void getInformation(String uri, String filepath) throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(4);
		try {
			Document summaryPageDoc = Jsoup.connect(uri).timeout(30000).get();
			Elements animeList = summaryPageDoc.select(
					"div.l-content > section.l-content-main > div.l-box--article > div.maybe-legacy > div.l-article:nth-child(2) > div[align=left]");
			Elements eachCard = animeList
					.select("div[align=left] > div.maybe-legacy");

			List<Future<Information>> results = new ArrayList<>();
			for (Element el : eachCard) {
				results.add(executor.submit(new CardScraper(el)));
			}
			
			listener.appendMessage("Card size ["+ results.size() + "]");
			
			AtomicInteger counter = new AtomicInteger(0);
			List<Information> informations = results.stream().map(card -> {
				try {
					Information info = card.get(30L, TimeUnit.SECONDS);
					counter.getAndIncrement();
					listener.updateProgress((double) counter.get() / results.size());
					listener.appendMessage("title[" + info.title + "] completed!");
					return info;
				} catch (Exception e) {
					counter.getAndIncrement();
					listener.updateProgress((double) counter.get() / results.size());
					listener.appendMessage("[Exception] " + e.getMessage());
					return null;
				}
			}).collect(Collectors.toList());

			String path = filepath + File.separator + "Animatetimes_" + new Date().getTime() + ".json";
			writeJson(informations, path);
		} catch (Exception e) {
			listener.appendMessage("[FATAL] AnimatetimesScraper failed error: "	+ e.getMessage());
			throw e;
		} finally {
			if (executor != null && !executor.isShutdown()) {
				executor.shutdownNow();
			}
		}
	}

	@Override
	public Season getSeasons(String uri) throws IOException {
		var seasonMap = new Season();
		Document summaryPageDoc = Jsoup.connect(uri).timeout(30000).get();
		Element container = summaryPageDoc
				.select(".l-box--article .maybe-legacy").first();
		Element animeListBlock = container.child(1);
		List<Element> anchors = animeListBlock.select("a").stream()
				.filter(anchor -> anchor.attr("href")
						.startsWith("https://www.animatetimes.com/tag/details")
						&& anchor.text().endsWith("アニメ一覧"))
				.collect(Collectors.toList());
		for (Element a : anchors) {
			String key = a.text();
			String href = a.attr("href").trim();
			seasonMap.put(key, href);
		}
		return seasonMap;
	}
}
