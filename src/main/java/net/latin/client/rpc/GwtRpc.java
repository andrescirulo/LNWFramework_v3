package net.latin.client.rpc;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.ServiceDefTarget;


/**
 * Utility for accesing the RPC servers
 * @author Matias Leone
 */
public class GwtRpc {

	private final static String DEFAULT_SERVER_NAME = "__defaultServer";
	private static GwtRpc instance;

	private Map<String,GwtRpcServerInfo> serverMap;


	private GwtRpc() {
		this.serverMap = new HashMap<String,GwtRpcServerInfo>();
	}

	public static final GwtRpc getInstance() {
		if( instance == null ) {
			instance =  new GwtRpc();
		}
		return instance;
	}


	/**
	 * Register a RPC server
	 * @param serverName: name to identify the server
	 * @param url: url of the server
	 * @param creator: creator of the specific interface class of the server
	 */
	public void addServer( String serverName, String url, GwtServerCreator creator ) {
		GwtRpcServerInfo info = new GwtRpcServerInfo();
		info.setName( serverName );

		if( url.startsWith("/") || url.startsWith(creator.getBasePath()) ){
			info.setUrl( url );
		}else {
			info.setUrl( creator.getBasePath() + url );
		}
		info.setServer( (GwtRpcInterfaceAsync)creator.createServer() );
		ServiceDefTarget endpoint = (ServiceDefTarget) info.getServer();
		endpoint.setServiceEntryPoint( info.getUrl() );
		this.addNewServer( info );
	}

	/**
	 * Register a RPC server with a default server name
	 * @param url: url of the server
	 * @param creator: creator of the specific interface class of the server
	 */
	public void addServer( String url, GwtServerCreator creator ) {
		this.addServer( DEFAULT_SERVER_NAME, url, creator );
	}


	/**
	 * Add a new server. If the server has already been added, it skips the registration
	 * @param info
	 */
	private void addNewServer(GwtRpcServerInfo info  ) {
		if( this.serverMap.containsKey( info.getName() ) ) {
			return;

		} else {
			this.serverMap.put( info.getName(), info );
		}
	}


	/**
	 * Search for a register RPC server
	 * @param serverName: id of the registered server
	 * @return a RPC server
	 */
	public GwtRpcInterfaceAsync getServer( String serverName ) {
		return this.getExistingServer(serverName).getServer();
	}

	/**
	 * True if the server has already been registered
	 */
	public boolean hasServer( String serverName ) {
		return this.serverMap.containsKey( serverName );
	}


	/**
	 * Search for the default RPC server
	 * @return default RPC server
	 */
	public GwtRpcInterfaceAsync getServer() {
		return this.getServer( DEFAULT_SERVER_NAME );
	}

	/**
	 * Search for the url of a register RPC server
	 * @param serverName: id of the registered server
	 * @return url of the server
	 */
	public String getUrl( String serverName ) {
		return this.getExistingServer(serverName).getUrl();
	}

	/**
	 * Search for the url of the default RPC server
	 * @return url of the server
	 */
	public String getUrl() {
		return this.getUrl( DEFAULT_SERVER_NAME );
	}


	/**
	 * @param serverName
	 * @return and existing server
	 */
	private GwtRpcServerInfo getExistingServer( String serverName ) {
		if( this.serverMap.containsKey( serverName ) ) {
			return (GwtRpcServerInfo)this.serverMap.get( serverName );

		} else {
			throw new RuntimeException( "There is not exist a server with the name: " + serverName );
		}
	}


}









