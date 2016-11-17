package net.latin.client.widget.menu;

import com.google.gwt.core.client.JavaScriptObject;

import gwt.material.design.addins.client.iconmorph.MaterialIconMorph;

public class GwtMaterialIconMorph extends MaterialIconMorph {

	public void morph(){
		doMorph(this.getElement());
	}
	
	private native void doMorph(JavaScriptObject elem)/*-{
		elem.classList.toggle('morphed');
	}-*/;

	/*Quita el listener que hace que al hacer click haga morph*/
	public void removeClickMorph() {
		getElement().removeAttribute("onclick");		
	}
	
}
