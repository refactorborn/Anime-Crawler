package idv.zwei.animecrawler.animehakku;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import idv.zwei.animecrawler.Information;
import idv.zwei.animecrawler.JPKeyWordConstant;

public class PageScraper implements Callable<Information> {

	private String href;
	public PageScraper(String href) {
		this.href = href;
	}
	
	@Override
	public Information call() {
		Document programPageDoc;
		try {
			programPageDoc = Jsoup.connect(href).timeout(15000).get();
		} catch (IOException e1) {
			throw new RuntimeException("Connect to uri[" + href + "]  timeout", e1);
		}
		Element programContainer = programPageDoc.select(".animeTopContainer").first();
		
		if (programContainer == null)
			return null;
		
		Elements detailBoxes = programContainer.select(".animeDetailBox");
		var info = new Information();
		
		for (Element detailBox : detailBoxes) {
			Element detailLNode = detailBox.select(".animeDetailL").first();
			
			if (detailLNode != null) {
				String title = detailLNode.child(0).textNodes().get(0).text().trim();
				info.title = title;
			}
			
			Element imgNode = detailBox.select(".animeDetailImg").first();
			if (imgNode != null) {
				info.imgURI = imgNode.child(0).absUrl("src");
			}
			
			List<Element> animeDetailListNodes = detailBox.select(".animeDetailList").stream().filter(e -> "dl".equals(e.tagName())).toList();
			for (Element detailList : animeDetailListNodes) {
				if (detailList.childrenSize() != 2)
					continue;
				
				String dtText = detailList.child(0).text().trim();
				if (JPKeyWordConstant.PRODUCTION_COMPANY.equals(dtText)) {
					info.productionCompany = detailList.child(1).select("li").get(0).text();
				} else if (dtText.indexOf(JPKeyWordConstant.STAFF) != -1) {
					info.staff = detailList.child(1).select("li").eachText().stream().toArray(String[]::new);
				} else if (JPKeyWordConstant.SUMMARY.equals(dtText)) {
					info.summary = detailList.child(1).text().trim();
				} else if (Arrays.asList(JPKeyWordConstant.MUSIC).indexOf(dtText) != -1) {
					info.music = detailList.child(1).select("li").get(0).html().split("<br>");
				} else if (JPKeyWordConstant.CAST.equals(dtText)) {
					info.cast = detailList.child(1).select("li").eachText().stream().toArray(String[]::new);
				} else if (dtText.indexOf(JPKeyWordConstant.LINK) != -1) {
					Elements liNodes = detailList.child(1).select("li");
					for (Element li : liNodes) {
						String content = li.text().trim();
						if (content.indexOf(JPKeyWordConstant.HOME_PAGE) == -1)
							continue;
						
						info.HP_URI = li.child(0).absUrl("href");
					}
				}
			}
		}
		
		return info;
	}
}
