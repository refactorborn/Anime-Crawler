package idv.zwei.animecrawler.animatetimes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import idv.zwei.animecrawler.Information;
import idv.zwei.animecrawler.JPKeyWordConstant;

public class CardScraper implements Callable<Information> {

	Element container;
	public CardScraper(Element container) {
		this.container = container;
	}

	@Override
	public Information call() throws Exception {
		var info = new Information();
		
		int productionIndex = container.ownText().indexOf("(C)");
		if (productionIndex != -1) {
			info.summary = container.ownText().substring(0, productionIndex);					
		} else {
			info.summary = container.ownText();
		}
		Elements dataBlock = container.select("center, table, div.maybe-legacy > a");
		
		for(Element el: dataBlock) {
			if ("center".equals(el.tagName())) {
				Elements img = el.select("div.c-image-a > img");
				info.imgURI = img.attr("src");
			} else if("table".equals(el.tagName())) {
				Elements trs = el.select("tbody tr");
				parseTableInfo(trs, info);
			} else if ("a".equals(el.tagName())) {
				String text = el.text().trim();
				if (text.indexOf(JPKeyWordConstant.HOME_PAGE) != -1) {
					String href = el.attr("href");
					info.HP_URI = href;
				}
			}
		}
		
		return info;
	}
	
	public void parseTableInfo(Elements trs, Information info) {
		for (Element el: trs) {
			String subtitle = el.select("td").first().text().trim();
			String content = el.select("th").first().text().trim();
			if (JPKeyWordConstant.TITLE.equals(subtitle)) {
				info.title = content;
			} else if (JPKeyWordConstant.CAST.equals(subtitle)) {
				info.cast = content.split(" ");
			} else if (JPKeyWordConstant.STAFF.equals(subtitle)) {
				String[] tempStaff = content.split(" ");
				ArrayList<String> staff = new ArrayList<String>(Arrays.asList(tempStaff));
				for (int i = 0; i < tempStaff.length; i++) {
					String text = tempStaff[i];
					if (text.startsWith(JPKeyWordConstant.PRODUCTION)) {
						info.productionCompany = text.substring(JPKeyWordConstant.PRODUCTION.length());
						staff.remove(i);
					}
				}
				info.staff = staff.toArray(String[]::new);
			} else if (Arrays.asList(JPKeyWordConstant.MUSIC).indexOf(subtitle) != -1) {
				info.music = content.replaceAll("アニメイト通販での購入はこちら", "").split(" ");
			}
		}
		
	}
}
