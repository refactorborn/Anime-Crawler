package idv.zwei.animecrawler.ui;

public interface UpdateListener {
	void updateProgress(double percent);
	void appendMessage(String message);
}
