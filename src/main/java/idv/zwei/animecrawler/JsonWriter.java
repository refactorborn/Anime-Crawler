package idv.zwei.animecrawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonWriter {
	private File file;
	private List<Information> informations;
	
	public JsonWriter(List<Information> informations) {
		this.informations = informations;
	}
	
	public void setFilePath(String file) {
		this.file = new File(file);
	}
	
	public void setFilePath(File file) {
		this.file = file;
	}
	
	public void write() {
		if (file == null) {
			try {
				String executePath = getClass()
								  .getProtectionDomain()
								  .getCodeSource()
								  .getLocation()
								  .toURI()
								  .getPath();
				
				String rootPath = executePath.substring(0, executePath.lastIndexOf("/"));
				
				setFilePath(rootPath + File.separator + "animes.json");
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		} 
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(informations, informations.getClass());
		
		try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
			bw.write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
