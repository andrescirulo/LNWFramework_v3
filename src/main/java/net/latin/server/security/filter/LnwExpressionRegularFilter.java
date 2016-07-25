package net.latin.server.security.filter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;


public class LnwExpressionRegularFilter implements LnwFilterRule{ //LNWFILTERRULE
	private List<String> parametros;
	
	
	public boolean validar(HttpServletRequest httpRequest) {
		return true;
	}

	public void init(List<String> parametros) {
		this.parametros = parametros;
	}

	

	
}
