package idv.zwei.animecrawler.animehakku;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import idv.zwei.animecrawler.Information;
import idv.zwei.animecrawler.JPKeyWordConstant;

/**
 * change to use AnimehakkuScraper.java 
 * {@linkplain AnimehakkuScraper}
 */

@Deprecated
public class SummaryScraper {
	
	String uri;
	public SummaryScraper(String uri) {
		this.uri = uri;
	}

	private void scrapeSummary() {
		try {
			Document doc = Jsoup.connect(uri).timeout(30000).get();
			Elements eles = doc.select(".animeSeasonContainer .animeSeasonBox");
			var animes = new ArrayList<Information>();
			System.out.println("HTML Head Title: " + doc.title());
			
			for (Element ele : eles) {
				var info = new Information();
				boolean archorSucceeded = proceedBlock("a", ele, info);
				if (!archorSucceeded)
					continue;
				
				boolean dlSucceeded = proceedBlock("d", ele, info);
				if (!dlSucceeded)
					continue;
				
				animes.add(info);
			}
			
			System.out.println(animes.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    public boolean proceedBlock(String block, Element ele, Information info) {
    	boolean res = false;
    	switch (block.toLowerCase()) {
    		case "a":
    			res = proceedArchor(ele, info);
    			break;
    		case "d":
    			res = proceedDL(ele, info);
    			break;
    		case "staff":
    			res = proceedStaff(ele, info);
    			break;
    		default:
    			throw new UnknownError("Unknown Block[" + block + "]");
    	}
    	
    	return res;
    }
    
    public boolean proceedArchor(Element ele, Information info) {
    	Elements imgBoxes = ele.select(".seasonBoxImg");
		Element imgBox = imgBoxes.first();
		
		// anchor tag not exists skip this content.
		if (imgBox == null) 
			return false;
		
		Element img = imgBox.child(0).child(0);
		String title = img.attr("alt");
		String imgURI = img.absUrl("src");
		info.title = title;
		info.imgURI = imgURI;
		return true;
    }
    
    public boolean proceedDL(Element ele, Information info) {
    	Elements detail = ele.select(".seasonAnimeDetail");
		Element dl = detail.first();
		
		if (dl == null)
			return false;
		
		Elements children = dl.children();
		int cSize = children.size();
		if (cSize < 2) 
			return false;
		
		for (int i = 0; i < cSize - 1; i++) {
			Element dtChild = children.get(i);
			Element ddChild = children.get(i + 1);
			
			if (!"dt".equals(dtChild.tagName()) || !"dd".equals(ddChild.tagName())) 
				continue;
			
			String dtText = dtChild.text();
			if (dtText.indexOf(JPKeyWordConstant.PRODUCTION_COMPANY) != -1) {
				info.productionCompany = ddChild.text();
			} else if (dtText.indexOf(JPKeyWordConstant.MAIN_STAFF) != -1) {
				proceedBlock("staff", ddChild, info);
			} else if (dtText.indexOf(JPKeyWordConstant.MAIN_CAST) != -1) {
				String[] cast = ddChild.text().split(", ");
				info.cast = cast;
			}
		}
    	return true;
    }
    
    public boolean proceedStaff(Element ele, Information info) {
    	Elements list = ele.select("ul li");
    	var staff = new ArrayList<String>();
    	for (Element li : list) {
    		String liText = li.text().trim();
    		staff.add(liText);
    	}
    	info.staff = staff.stream().toArray(String[]::new);
    	return true;
    }
}
