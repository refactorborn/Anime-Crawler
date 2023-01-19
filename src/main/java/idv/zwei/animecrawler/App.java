package idv.zwei.animecrawler;

import java.awt.EventQueue;

import idv.zwei.animecrawler.ui.CrawlerUI;

public class App {
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				CrawlerUI frame = new CrawlerUI();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
