package idv.zwei.animecrawler.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import idv.zwei.animecrawler.Scraper;
import idv.zwei.animecrawler.Season;
import idv.zwei.animecrawler.Source;
import idv.zwei.animecrawler.factory.ScraperFactory;

import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class CrawlerUI extends JFrame implements UpdateListener {

	private static final long serialVersionUID = 4319317931454292201L;
	private JPanel contentPane;
	private String defaultDesktop = System.getProperty("user.home") + File.separator +"Desktop";
	private static Season seasonData;
	private JProgressBar progressBar;
	private JTextArea messageTextArea;
	private JComboBox<String> seasonComboBox;
	private SourceComboBoxModel sourceModel;
	private JButton setPathButton;
	private JButton startButton;

	public CrawlerUI() {
		CrawlerUI _this = this;
		setTitle("Anime Crawler");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(600, 500));
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel northPanel = new JPanel();
		contentPane.add(northPanel, BorderLayout.NORTH);
		GridBagLayout gbl_northPanel = new GridBagLayout();
		gbl_northPanel.columnWidths = new int[]{0, 0, 0, 0};
		gbl_northPanel.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_northPanel.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_northPanel.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		northPanel.setLayout(gbl_northPanel);

		JLabel filePathLabel = new JLabel("Output path:");
		filePathLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		GridBagConstraints gbc_filePathLabel = new GridBagConstraints();
		gbc_filePathLabel.insets = new Insets(0, 0, 5, 5);
		gbc_filePathLabel.gridx = 0;
		gbc_filePathLabel.gridy = 0;
		northPanel.add(filePathLabel, gbc_filePathLabel);

		JPanel filepathSubPanel = new JPanel();
		GridBagConstraints gbc_filepathSubPanel = new GridBagConstraints();
		gbc_filepathSubPanel.insets = new Insets(0, 0, 5, 0);
		gbc_filepathSubPanel.fill = GridBagConstraints.BOTH;
		gbc_filepathSubPanel.gridx = 2;
		gbc_filepathSubPanel.gridy = 0;
		northPanel.add(filepathSubPanel, gbc_filepathSubPanel);
		filepathSubPanel.setLayout(new BorderLayout(0, 0));

		JLabel setPathLabel = new JLabel(defaultDesktop);
		filepathSubPanel.add(setPathLabel, BorderLayout.CENTER);

		JLabel seasonLabel = new JLabel("Season:");
		seasonLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		GridBagConstraints gbc_seasonLabel = new GridBagConstraints();
		gbc_seasonLabel.insets = new Insets(0, 0, 5, 5);
		gbc_seasonLabel.gridx = 0;
		gbc_seasonLabel.gridy = 2;
		northPanel.add(seasonLabel, gbc_seasonLabel);

		seasonComboBox = new JComboBox<String>();
		seasonComboBox.setEnabled(false);
		GridBagConstraints gbc_seasonComboBox = new GridBagConstraints();
		gbc_seasonComboBox.insets = new Insets(0, 0, 5, 0);
		gbc_seasonComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_seasonComboBox.gridx = 2;
		gbc_seasonComboBox.gridy = 2;
		northPanel.add(seasonComboBox, gbc_seasonComboBox);

		setPathButton = new JButton("Set path");
		setPathButton.setFont(new Font("Arial", Font.PLAIN, 16));
		filepathSubPanel.add(setPathButton, BorderLayout.EAST);

		JLabel sourceLabel = new JLabel("Source from:");
		sourceLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		GridBagConstraints gbc_sourceLabel = new GridBagConstraints();
		gbc_sourceLabel.insets = new Insets(0, 0, 5, 5);
		gbc_sourceLabel.gridx = 0;
		gbc_sourceLabel.gridy = 1;
		northPanel.add(sourceLabel, gbc_sourceLabel);

		sourceModel = new SourceComboBoxModel();
		JComboBox<String> sourceComboBox = new JComboBox<String>(sourceModel);

		GridBagConstraints gbc_sourceComboBox = new GridBagConstraints();
		gbc_sourceComboBox.insets = new Insets(0, 0, 5, 0);
		gbc_sourceComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_sourceComboBox.gridx = 2;
		gbc_sourceComboBox.gridy = 1;
		northPanel.add(sourceComboBox, gbc_sourceComboBox);

		startButton = new JButton("Start");

		startButton.setFont(new Font("Arial", Font.PLAIN, 16));
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 3;
		northPanel.add(startButton, gbc_btnNewButton);

		JPanel centerPanel = new JPanel();
		contentPane.add(centerPanel, BorderLayout.CENTER);
		GridBagLayout gbl_centerPanel = new GridBagLayout();
		gbl_centerPanel.columnWidths = new int[]{0, 0};
		gbl_centerPanel.rowHeights = new int[]{0, 0};
		gbl_centerPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_centerPanel.rowWeights = new double[]{0.0, 1.0};
		centerPanel.setLayout(gbl_centerPanel);

		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setToolTipText("Scrape item progess");
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.insets = new Insets(5, 5, 5, 0);
		gbc_progressBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_progressBar.gridx = 0;
		gbc_progressBar.gridy = 0;
		centerPanel.add(progressBar, gbc_progressBar);

		JScrollPane messageScrollPane = new JScrollPane();
		GridBagConstraints gbc_messageScrollPane = new GridBagConstraints();
		gbc_messageScrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_messageScrollPane.fill = GridBagConstraints.BOTH;
		gbc_messageScrollPane.gridx = 0;
		gbc_messageScrollPane.gridy = 1;
		centerPanel.add(messageScrollPane, gbc_messageScrollPane);

		messageTextArea = new JTextArea();
		messageTextArea.setEditable(false);
		messageScrollPane.setViewportView(messageTextArea);

		setPathButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(defaultDesktop);
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fc.showSaveDialog(_this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File choice = fc.getSelectedFile();
					setPathLabel.setText(choice.getAbsolutePath());
				}
			}
		});

		sourceComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					
					String sourceText = sourceModel.getSelectedItem();
					if (sourceText == null) {
						JOptionPane.showMessageDialog(_this, "Please select source", "Warning", JOptionPane.WARNING_MESSAGE);
						return;
					}
					EventQueue.invokeLater(() -> {
						seasonComboBox.setModel(new DefaultComboBoxModel<String>());
						seasonComboBox.setEnabled(false);
					});
					
					String uri = sourceModel.getUri();
					new Thread(() -> {
						Scraper scraper = ScraperFactory.getScraper(sourceText, _this);
						try {
							seasonData = scraper.getSeasons(uri);
						} catch (Exception e1) {
							seasonData = null;
							appendMessage("Get season data failed! message: " + e1.getMessage());
						}
						EventQueue.invokeLater(() -> {
							String[] model = {} ;
							if (seasonData != null) {
								model = seasonData.getKeys();
							}
							seasonComboBox.setModel(new DefaultComboBoxModel<String>(model));
							seasonComboBox.setEnabled(true);
						});
					}).start();
				}
			}
		});

		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String pathText = setPathLabel.getText();

				if ("".equals(pathText)) {
					JOptionPane.showMessageDialog(_this, "output file cannot be empty", "Warning", JOptionPane.WARNING_MESSAGE);
					return;
				}

				String sourceUri = sourceModel.getUri();
				if (sourceUri == null) {
					JOptionPane.showMessageDialog(_this, "Please select source", "Warning", JOptionPane.WARNING_MESSAGE);
					return;
				}

				String seasonSelected = (String) seasonComboBox.getSelectedItem();
				if (seasonSelected == null) {
					JOptionPane.showMessageDialog(_this, "Please select season", "Warning", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				String uri = getUri();
				String sourceText = sourceModel.getSelectedItem();
				
				updateProgress(0);
				lockButton();
				new Thread(() -> {
					Scraper scraper = ScraperFactory.getScraper(sourceText, _this);
					try {
						scraper.getInformation(uri, pathText);
					} catch (Exception e1) {
						e1.printStackTrace();
					} finally {
						releaseButton();
					}
				}).start();
				appendMessage("Start Scrape " + sourceText + " " + seasonSelected + "...");
			}
		});
	}

	@Override
	public void updateProgress(double percent) {
		final double PERCENT = percent;
		SwingUtilities.invokeLater(() -> progressBar.setValue((int) (100 * PERCENT)));
	}

	@Override
	public void appendMessage(String message) {
		String messageWithLineSeparator = message + System.lineSeparator();
		SwingUtilities.invokeLater(() -> messageTextArea.append(messageWithLineSeparator));
	}

	public String getUri() {
		String seasonSelected = (String) seasonComboBox.getSelectedItem();
		String subUri = seasonData.get(seasonSelected);
		String sourceUri = sourceModel.getUri();
		String sourceSlected = sourceModel.getSelectedItem();
		String uri;
		if (sourceSlected.equals(Source.ANIMEHAKKU.getTitle())) {
			uri = sourceUri + subUri;			
		} else {
			uri = subUri;
		}
		return uri;
	}
	
	public void lockButton() {
		setPathButton.setEnabled(false);
		startButton.setEnabled(false);
	}
	
	public void releaseButton() {
		setPathButton.setEnabled(true);
		startButton.setEnabled(true);
	}
}
