package net.latin.client.widget.documentoValidante.requisito.display;

import com.google.gwt.user.client.ui.HasText;

import net.latin.client.widget.documentoValidante.requisito.RequisitoValidanteDisplay;

public interface TomoFolioDisplay extends RequisitoValidanteDisplay {
	
	HasText getTomo();
	
	HasText getFolio();
	
	void setTomoEnabled(boolean b);
	
	void setFolioEnabled(boolean b);
}
