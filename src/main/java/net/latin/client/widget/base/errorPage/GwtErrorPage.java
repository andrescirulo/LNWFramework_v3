package net.latin.client.widget.base.errorPage;

import net.latin.client.widget.base.GwtPage;

/**
 * Generic interface for an error page
 *
 */
public interface GwtErrorPage {

	/**
	 * Sets a new error which happens on the client side
	 */
	public void setClientError( String msg, Throwable t );

	/**
	 * Sets a new error which happens on the server side
	 */
	public void setServerError( String msg, String errorCode );

	/**
	 * @return the error page to be show to the user
	 */
	public GwtPage getErrorPage();


	/**
	 * Informs that an access violation has ocurred.
	 * @param currentPageGroup current PageGroup the user was trying to access.
	 */
	public void setAccessDeniedError( String currentPageGroup );

}
