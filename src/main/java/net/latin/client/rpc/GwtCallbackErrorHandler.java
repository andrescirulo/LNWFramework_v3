package net.latin.client.rpc;


/**
 * Handler para manejar errores luego de un pedido RPC
 *
 * @author Matias Leone
 */
public interface GwtCallbackErrorHandler {

	public void doSessionExperied();

	public void doUnexpectedFailure(Throwable e);

	public void doUnexpectedFailure( String errorMsg );

	public void doUnexpectedFailure( String errorMsg, Throwable t );

}
