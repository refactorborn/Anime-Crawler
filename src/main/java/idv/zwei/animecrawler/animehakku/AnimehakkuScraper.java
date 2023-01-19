package idv.zwei.animecrawler.animehakku;

import java.io.File;
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
import idv.zwei.animecrawler.JsonWriter;
import idv.zwei.animecrawler.Scraper;
import idv.zwei.animecrawler.ui.UpdateListener;

public class AnimehakkuScraper extends Scraper {

	public AnimehakkuScraper(UpdateListener listener, String uri, String filepath) {
		super(listener, uri, filepath);
	}

	@Override
	public void start() {
		ExecutorService executor = Executors.newFixedThreadPool(4);
        try {
        	Document summaryPageDoc = Jsoup.connect(uri).timeout(30000).get();
			Elements eles = summaryPageDoc.select(".animeSeasonContainer .animeSeasonBox");
			
			List<Future<Information>> results = new ArrayList<>();
			
			for (Element ele : eles) {
				Elements imgBoxes = ele.select(".seasonBoxImg");
				Element imgBox = imgBoxes.first();
				if (imgBox == null)
					continue;
				
				String href = imgBox.children().get(0).absUrl("href");
				if (href.isEmpty() || href.isBlank())
					continue;
				
				results.add(executor.submit(new PageScraper(href)));
			}
			
			AtomicInteger counter = new AtomicInteger(0);
			List<Information> informations = results.stream().map(page -> {
				try {
					Information info = page.get(30L, TimeUnit.SECONDS);
					counter.getAndIncrement();
					listener.updateProgress((double) counter.get() / results.size());
					listener.appendMessage("title[" + info.title + "] completed!\n");
					return info;
				} catch (Exception e) {
					counter.getAndIncrement();
					listener.updateProgress((double) counter.get() / results.size());
					listener.appendMessage("[Exception] " + e.getMessage() + "\n");
					return null;
				}
			}).collect(Collectors.toList());
			
			listener.appendMessage("All the information has been collected.\n");
			listener.appendMessage("Write data to json file\n");
			JsonWriter jw = new JsonWriter(informations);
			String path = filepath + File.separator + "Animehakku_" + new Date().getTime() + ".json";
			jw.setFilePath(path);
			jw.write();
			listener.appendMessage("The json file [" + path + "] has been exported\n");
		} catch (Exception e) {
			e.printStackTrace();
			listener.appendMessage("[FATAL] AnimehakkuScraper failed error: " + e.getMessage());
		} finally {
			if (executor != null && !executor.isShutdown()) {
				executor.shutdownNow();
			}
		}
	}
}
