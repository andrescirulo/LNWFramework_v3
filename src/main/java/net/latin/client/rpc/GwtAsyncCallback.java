package net.latin.client.rpc;


import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;

import net.latin.client.exceptions.GwtObjectNotFoundException;
import net.latin.client.exceptions.LnwTransactionException;

/**
 * Callback with some helps for GwtPage
 *
 * @author Matias Leone
 */
public abstract class GwtAsyncCallback<T> implements AsyncCallback<T> {

	/**
	 * Hack markup that we'll try to find in the exceptions's message
	 */
	public final static String EXPIRED_SESSION_CTE = "__#EXPIRED_SESSION#__";

	private GwtCallbackErrorHandler handler;

	/**
	 * Creates a new AsyncCallback.
	 * We need the page to add some cool features.
	 */
	public GwtAsyncCallback(GwtCallbackErrorHandler handler) {
		this.handler = handler;
	}

	/**
	 * Constructor vacio que no requiere el error handler, se utiliza el DefaultGwtCallbackErrorHandler
	 */
	public GwtAsyncCallback() {
		this(new DefaultGwtCallbackErrorHandler());
	}

	public void onFailure(Throwable caught) {
		try {
			throw caught;
		} catch (LnwTransactionException e) {
			onLnwTransactionException(e);
		} catch (GwtObjectNotFoundException e) {
			onGwtObjectNotFoundException(e);
		} catch( GwtBusinessException e ) {
			onGwtBusinessException(e);
		} catch (InvocationException e) {
			searchForExpiredSession(e);
		} catch (Throwable e) {
			doUnexpectedFailure(e);
		}
	}

	/**
	 * Search a hack markup in the exception message to find out if the user's session
	 * has expired.
	 */
	private void searchForExpiredSession(InvocationException e) {
		if( isExpiredSessionException( e ) ) {
			handler.doSessionExperied();
		} else {
			doUnexpectedFailure( e );
		}
	}

	public static boolean isExpiredSessionException( Throwable e ) {
		String msg = e.getMessage();
		int n = msg.indexOf( EXPIRED_SESSION_CTE );
		return n != -1;
	}

	private void doUnexpectedFailure(Throwable e) {
		handler.doUnexpectedFailure("", e);
	}


	/**
	 * Called when the server-method thows GwtBusinessException
	 */
	public void onGwtBusinessException(GwtBusinessException e) {
		doUnexpectedFailure(e);
	}

	/**
	 * Called when the server-method thows GwtObjectNotFoundException
	 */
	public void onGwtObjectNotFoundException(GwtObjectNotFoundException e) {
		doUnexpectedFailure(e);
	}

	/**
	 * Called when the server-method thows LnwTransactionException
	 */
	public void onLnwTransactionException(LnwTransactionException e) {
		doUnexpectedFailure(e);
	}
}
