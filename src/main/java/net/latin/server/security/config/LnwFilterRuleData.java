package net.latin.server.security.config;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;

public class LnwFilterRuleData {//LNWFILTERRULEDATA

	private String className;
	private List<String> params = new ArrayList<String>();
	
	public LnwFilterRuleData(){
		
	}
	
	public LnwFilterRuleData(String className, List parametrosFiltro){
		super();
		this.className = className;
		for (Object parametroFiltro : parametrosFiltro) {
			this.params.add( ((Element)parametroFiltro).getText() );
		}
	}
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public List<String> getParams() {
		return params;
	}
	public void setParams(List<String> params) {
		this.params = params;
	}
	
	
	
}
