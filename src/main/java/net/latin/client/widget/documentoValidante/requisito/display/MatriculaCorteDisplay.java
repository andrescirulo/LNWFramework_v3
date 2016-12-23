package net.latin.client.widget.documentoValidante.requisito.display;

import com.google.gwt.user.client.ui.HasText;

import net.latin.client.widget.documentoValidante.requisito.RequisitoValidanteDisplay;

public interface MatriculaCorteDisplay extends RequisitoValidanteDisplay {
	HasText getMatriculaCorte();
	
	void setMatriculaCorteEnabled(boolean b);
}
