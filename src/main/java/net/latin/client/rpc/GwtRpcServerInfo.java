package net.latin.client.rpc;

/**
 * RPC server information
 *
 * @author Matias Leone
 */
public class GwtRpcServerInfo {

	private String url;
	private String name;
	private GwtRpcInterfaceAsync server;

	public GwtRpcServerInfo() {
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public GwtRpcInterfaceAsync getServer() {
		return server;
	}
	public void setServer(GwtRpcInterfaceAsync server) {
		this.server = server;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}



}
