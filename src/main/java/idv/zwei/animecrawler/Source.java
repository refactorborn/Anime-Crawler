package idv.zwei.animecrawler;

public enum Source {
	ANIMEHAKKU("アニメハック", "https://anime.eiga.com/program/"),
	ANIMATE_TIMES("animate times", "https://www.animatetimes.com/tag/details.php?id=1392");
	
	private String title;
	private String uri;
	
	public String getTitle() {
		return title;
	}

	public String getUri() {
		return uri;
	}

	private Source(String title, String uri) {
		this.title = title;
		this.uri = uri;
	}
}