package net.latin.client.rpc;

public interface GwtServerCreator {

	/**
	 * Metodo necesario para especificar en forma explicita el nombre de la interfaz
	 * del server con la cual realizar la comunicacion RPC.
	 *<p>
	 * Hacer esto: <pre>GWT.create(MyServiceClient.class);</pre>
	 * MyServiceClient.class: es la interfaz que extiende de GwtRpcInterface, utilizada
	 * para comunicarse por RPC con el server.
	 *
	 * @return interfaz creada con el metodo <code>GWT.create()</code>
	 */
	public Object createServer();

	/**
	 * Obtiene el path base a usar para obtener el server RPC.
	 */
	public String getBasePath();

}
