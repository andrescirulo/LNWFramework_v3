package net.latin.server.security.filter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

public interface LnwFilterRule {

	public boolean validar(HttpServletRequest httpRequest);
	
	public void init(List<String> parametros);
}
