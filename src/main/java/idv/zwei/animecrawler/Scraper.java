package idv.zwei.animecrawler;

import java.util.List;

import idv.zwei.animecrawler.ui.UpdateListener;

public abstract class Scraper {
	// for update UI progress bar
	protected UpdateListener listener;
	
	public Scraper(UpdateListener listener) {
		this.listener = listener;
	}
	
	protected void writeJson(List<Information> informations, String path) {
		listener.appendMessage("All the information has been collected.");
		listener.appendMessage("Write data to json file.");
		JsonWriter jw = new JsonWriter(informations);
		jw.setFilePath(path);
		jw.write();
		listener.appendMessage("The json file [" + path + "] has been exported.");
	}
	
	public abstract void getInformation(String uri, String filepath) throws Exception;
	public abstract Season getSeasons(String uri) throws Exception;
}
