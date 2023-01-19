package idv.zwei.animecrawler;

import java.util.Arrays;

public class Information {
	public String title;
	public String imgURI;
	public String productionCompany;
	public String[] staff;
	public String[] cast;
	public String[] music;
	public String summary;
	public String HP_URI;
	@Override
	public String toString() {
		return "Title=[" + title + "] " +
				"ImgURI=[" + imgURI + "] " +
				"Production Company=[" + productionCompany + "] " +
				"Staff=[" + (staff == null ? "null" : Arrays.toString(staff)) + "] " +
				"Cast=[" + (cast == null ? "null" : Arrays.toString(cast)) + "] " +
				"HP URI=[" + HP_URI + "]";
	}
}
