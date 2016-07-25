package net.latin.server.security.config;
/**
 * Abstracción de el tag CommonAccess de GeneralConfig.xml
 *
 * @author Santiago Aimetta
 */
public class CommonAccess {
	private String id = "";

	public CommonAccess() {
	}

	public CommonAccess(String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}






}
