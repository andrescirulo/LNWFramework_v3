package net.latin.client.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.StyleInjector;

public class StylesManager {
	private static List<String> stylesInjected=new ArrayList<String>();
	
	public static void injectStyle(String styleName,String style){
		if (!stylesInjected.contains(styleName)){
			stylesInjected.add(styleName);
			StyleInjector.inject(style, true);
		}
	}
	
}
