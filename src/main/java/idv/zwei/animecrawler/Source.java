package idv.zwei.animecrawler;

public enum Source {
	ANIMEHAKKU("アニメハック", "Animehakku", "https://anime.eiga.com/program/"),
	ANIMATE_TIMES("animate times", "Animatetimes", "https://www.animatetimes.com/tag/details.php?id=1392");
	
	private String title;
	private String classNamePrefix;
	private String uri;
	
	public String getTitle() {
		return title;
	}

	public String getClassNamePrefix() {
		return classNamePrefix;
	}

	public String getUri() {
		return uri;
	}

	private Source(String title, String classNamePrefix, String uri) {
		this.title = title;
		this.classNamePrefix = classNamePrefix;
		this.uri = uri;
	}
}