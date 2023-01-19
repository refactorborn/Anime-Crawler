package idv.zwei.animecrawler.ui;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import idv.zwei.animecrawler.Source;


public class SourceComboBoxModel extends AbstractListModel<String> implements ComboBoxModel<String> {

	protected String[] sourceList = {Source.ANIMEHAKKU.getTitle(), Source.ANIMATE_TIMES.getTitle()};
	String selection = "Select source to get season data";
	private static final long serialVersionUID = -9035072268413363960L;

	@Override
	public int getSize() {
		return sourceList.length;
	}

	@Override
	public String getElementAt(int index) {
		return sourceList[index];
	}

	@Override
	public void setSelectedItem(Object item) {
		selection = (String) item;
	}

	@Override
	public String getSelectedItem() {
		return selection;
	}

	public String getUri() {	
		if (selection.equals(Source.ANIMEHAKKU.getTitle())) {
			return Source.ANIMEHAKKU.getUri();
		} else if (selection.equals(Source.ANIMATE_TIMES.getTitle())) {			
			return Source.ANIMATE_TIMES.getUri();
		} else {
			return null;
		}
	}


}
