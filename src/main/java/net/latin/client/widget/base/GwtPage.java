package net.latin.client.widget.base;

import java.util.ArrayList;
import java.util.List;

import net.latin.client.rpc.DefaultGwtCallbackErrorHandler;
import net.latin.client.rpc.GwtCallbackErrorHandler;
import net.latin.client.widget.base.listener.GwtBeforeUseCaseExitListener;
import net.latin.client.widget.base.listener.GwtOnVisibleListener;
import net.latin.client.widget.base.listener.GwtUploadPanelManager;
import net.latin.client.widget.msg.GwtMensajesListener;
import net.latin.client.widget.msg.GwtMsg;
import net.latin.client.widget.msg.GwtMsgNotification;

/**
 * 
 * Represents a page from a Use Case. A page belongs to a GroupPage and must
 * have an unique local name. You can change beetween pages using this local id.
 * The page constructor is called only one time, at the beginning of the
 * application. The method <code>onVisible()</code> is called every time the
 * page is set visibled.
 * 
 * 
 */
public abstract class GwtPage extends GwtVisualComponent implements GwtMensajesListener, GwtCallbackErrorHandler,GwtUploadPanelManager {

	private GwtPageGroup controller;
	private String pageName;
	private final DefaultGwtCallbackErrorHandler errorHandler = new DefaultGwtCallbackErrorHandler();
	private List<GwtOnVisibleListener> onVisibleListeners;
	private List<GwtBeforeUseCaseExitListener> onExitListeners;

	/**
	 * Initialize default settings for all pages
	 */
	public GwtPage() {
		this.setWidth("100%");
		onVisibleListeners = new ArrayList<GwtOnVisibleListener>();
		onExitListeners = new ArrayList<GwtBeforeUseCaseExitListener>();
	}

	/**
	 * Internal method
	 */
	protected void init(GwtPageGroup controller, String pageName) {
		this.controller = controller;
		this.pageName = pageName;
	}

	/**
	 * @return the PageGroup
	 */
	public GwtPageGroup getController() {
		return this.controller;
	}

	protected void beforeOnVisible() {
		for (GwtOnVisibleListener listener : onVisibleListeners) {
			listener.onVisible();
		}
		onVisible();
	}

	/**
	 * Template method called every time after the page has been set visibled.
	 */
	protected void onVisible() {

	}

	/**
	 * Template method called every time the page is hidden.
	 */
	protected void onHide() {
		
	}

	protected boolean beforeOnUseCaseExit() {
		for (GwtBeforeUseCaseExitListener listener : onExitListeners) {
			listener.beforeUseCaseExit();
		}
		return onUseCaseExit();
	}

	/**
	 * Template method called every time the user goes to other useCase and
	 * leave this one. It's called after method <code>onHide()</code> For
	 * default returns true, if returns false the user can't leave this useCase.
	 */
	protected boolean onUseCaseExit() {
		return true;
	}

	/**
	 * Add an alert message to the message bar
	 */
	public void addAlertMessage(String text) {
		controller.addAlertMessage(text);
	}

	/**
	 * Clears the message bar and adds a new alert message to it
	 */
	public void addNewAlertMessage(String text) {
		this.clearMessages();
		controller.addAlertMessage(text);
	}

	/**
	 * Adds many alert messages
	 */
	public void addAllAlertMessages(List<String> messagesList) {
		controller.addAllAlertMessages(messagesList);
	}

	/**
	 * Adds many error messages
	 */
	public void addAllErrorMessages(List<String> messagesList) {
		controller.addAllErrorMessages(messagesList);
	}

	/**
	 * Adds many ok messages
	 */
	public void addAllOkMessages(List<String> messagesList) {
		controller.addAllOkMessages(messagesList);
	}

	/**
	 * Add an error message to the message bar
	 */
	public void addErrorMessage(String text) {
		controller.addErrorMessage(text);
	}

	/**
	 * Clears the message bar and adds a new error message to it
	 */
	public void addNewErrorMessage(String text) {
		this.clearMessages();
		this.addErrorMessage(text);
	}

	/**
	 * Add a loading message to the message bar
	 */
	public void addLoadingMessage(String text) {
		controller.addLoadingMessage(text);
	}

	/**
	 * Clears the message bar and adds a new loading message to it
	 */
	public void addNewLoadingMessage(String text) {
		this.clearMessages();
		this.addLoadingMessage(text);
	}

	/**
	 * Add an ok message to the message bar
	 */
	public void addOkMessage(String text) {
		controller.addOkMessage(text);
	}

	/**
	 * Add an ok message to the message bar, which will survive one page's
	 * change
	 */
	public void addLongOkMessage(String text) {
		controller.addMessage(new GwtMsg(GwtMsgNotification.OK, text, 2));
	}

	/**
	 * Add an error message to the message bar, which will survive one page's
	 * change
	 */
	public void addLongErrorMessage(String text) {
		controller.addMessage(new GwtMsg(GwtMsgNotification.ERROR, text, 2));
	}

	/**
	 * /** Adds many error messages to the message bar, which will survive one
	 * page's change
	 */
	public void addAllLongErrorMessages(List<String> errors) {
		for (String error:errors) {
			controller.addMessage(new GwtMsg(GwtMsgNotification.ERROR, error, 2));
		}
	}

	/**
	 * Add an alert message to the message bar, which will survive one page's
	 * change
	 */
	public void addLongAlertMessage(String text) {
		controller.addMessage(new GwtMsg(GwtMsgNotification.ALERT, text, 2));
	}

	/**
	 * Add a message
	 */
	public void addMessage(GwtMsg msg) {
		controller.addMessage(msg);
	}

	/**
	 * Clears the message bar and adds a new ok message to it
	 */
	public void addNewOkMessage(String text) {
		this.clearMessages();
		this.addOkMessage(text);
	}

	/**
	 * Adds an object to the client context. The object can be recover later by
	 * the specified name.
	 */
	public void addToClientContext(String name, Object value) {
		controller.addToClientContext(name, value);
	}

	/**
	 * Returns true if the object with the key specified is in the client
	 * context.
	 */
	public boolean isInClientContext(String name) {
		return controller.isInClientContext(name);
	}

	/**
	 * Removes the object with the key specified from the client context.
	 */
	public Object removeFromClientContext(String name) {
		return controller.removeFromClientContext(name);
	}

	/**
	 * Clears the client context
	 */
	public void clearClientContext() {
		controller.clearClientContext();
	}

	/**
	 * Clears all the messages of the message bar
	 */
	public void clearMessages() {
		controller.clearMessages();
	}

	/**
	 * Gets an object previously saved in the client context
	 * 
	 * @param name
	 *            id of the object
	 * @return object saved in the client context
	 */
	public Object getFromClientContext(String name) {
		return controller.getFromClientContext(name);
	}

	public void addAlertMessage(String text, Object param) {
		controller.addAlertMessage(text, param);
	}

	public void addAlertMessage(String text, Object[] params) {
		controller.addAlertMessage(text, params);
	}

	public void addErrorMessage(String text, Object param) {
		controller.addErrorMessage(text, param);
	}

	public void addErrorMessage(String text, Object[] params) {
		controller.addErrorMessage(text, params);
	}

	public void addLoadingMessage(String text, Object param) {
		controller.addLoadingMessage(text, param);
	}

	public void addLoadingMessage(String text, Object[] params) {
		controller.addLoadingMessage(text, params);
	}

	public void addOkMessage(String text, Object param) {
		controller.addOkMessage(text, param);
	}

	public void addOkMessage(String text, Object[] params) {
		controller.addOkMessage(text, params);
	}

	/**
	 * Shows other page of this PageGroup. It hides the current page.
	 */
	public void showPage(String pageName) {
		controller.showPage(pageName);
	}

	/**
	 * Shows a page from another PageGroup. It hides the current page.
	 * 
	 * @param pageGroup
	 *            name of the PageGroup
	 * @param pageName
	 *            name of the page
	 */
	public void showPage(String pageGroup, String pageName) {
		controller.showPage(pageGroup, pageName);
	}

	/**
	 * Goes to an url which is not a GwtPage. It will be shown in an IFRAME.
	 * 
	 * @param url
	 */
	public void showExternalLink(String url) {
		controller.showExternalLink(url);
	}

	/**
	 * @return nombre de la page
	 */
	public String getPageName() {
		return pageName;
	}

	/**
	 * Regresa a la pagina anterior, registrada en el stack de movimientos
	 */
	public void goBackward() {
		controller.goBackward();
	}

	public void addToAppContext(String name, Object value) {
		controller.addToAppContext(name, value);
	}

	public void clearAppContext() {
		controller.clearAppContext();
	}

	public Object getFromAppContext(String name) {
		return controller.getFromAppContext(name);
	}

	public boolean isInAppContext(String name) {
		return controller.isInAppContext(name);
	}

	public void removeFromAppContext(String name) {
		controller.removeFromAppContext(name);
	}

	/**
	 * Call this method when the user has logged successfully to the system.
	 * Only call this method from a Login Page.
	 * 
	 * @see com.net.latin.navigation.gwt.client.widget.base.GwtController
	 */
	public void initAppWithUserLogged() {
		controller.initAppWithUserLogged();
	}

	public String getBasePath() {
		return controller.getBasePath();
	}

	public void setFocus() {
		// No debo realizar nada particular
	}

	public void doSessionExperied() {
		errorHandler.doSessionExperied();
	}

	public void doUnexpectedFailure(Throwable e) {
		errorHandler.doUnexpectedFailure(e);
	}

	public void doUnexpectedFailure(String errorMsg) {
		errorHandler.doUnexpectedFailure(errorMsg);
	}

	public void doUnexpectedFailure(String errorMsg, Throwable t) {
		errorHandler.doUnexpectedFailure(errorMsg, t);
	}

	public void addOnVisibleListener(GwtOnVisibleListener listener) {
		onVisibleListeners.add(listener);
	}

	public void addOnUseCaseExitListener(GwtBeforeUseCaseExitListener listener) {
		onExitListeners.add(listener);
	}
}
