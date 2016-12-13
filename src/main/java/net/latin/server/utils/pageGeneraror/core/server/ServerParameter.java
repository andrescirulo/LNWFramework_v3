package net.latin.server.utils.pageGeneraror.core.server;

/**
 * Representa un parametro
 *
 * @author Santiago Aimetta
 *
 */
public class ServerParameter {

	private String name;
	private String type;




	public ServerParameter(String name, String type) {
		super();
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}



}
