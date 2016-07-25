package net.latin.client.widget.base;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.latin.client.widget.base.history.ExternalHistoryMovement;
import net.latin.client.widget.base.history.HistoryStackManager;
import net.latin.client.widget.base.history.LocalHistoryMovement;
import net.latin.client.widget.msg.GwtMsg;

/**
 * <br>It's a group of GwtPages. They must be created and regitered in method <code>registerPages</code>
 * <br>It's the client-side of a UseCase. It has an RPC server interface associated.
 * All the pages in this group
 * use this server.
 *
 *
 */
public abstract class GwtPageGroup {

	private final Map<String,GwtPage> pagesMap;
	private final Map<String,Object> clientContext;
	private final HistoryStackManager stackManager;
	private GwtController controller;
	private boolean firstTime;
	private String currentPageId;
	private String groupName;

	public GwtPageGroup() {
		pagesMap = new HashMap<String,GwtPage>();
		firstTime = true;
		clientContext = new HashMap<String,Object>();
		stackManager = new HistoryStackManager( this );
		currentPageId = null;

	}

	/**
	 * Inicializa al grupo.
	 * Interno, no utilizar.
	 * @param groupName
	 */
	public void init( String groupName ) {
		this.groupName = groupName;
		//call the abstract register methods
		registerRpcServers();
	}

	/**
	 * Register all the RPC Servers to be used in the use case.
	 * <p>
	 * Example: <pre>GwtRpc.getInstance().addServer( "/proyect/url.do" , this );</pre>
	 */
	protected abstract void registerRpcServers();


	/**
	 * Register all the pages to be shown in the use case.
	 * Example: <code>this.addPage(  FORMA_DE_INGRESO ,new FormaDeIngreso() );</code>
	 *
	 */
	protected abstract void registerPages();

	/**
	 * Set the name of the first page of the use case to be shown.
	 * The page must be previously registered with that name
	 * @return
	 */
	protected abstract String registerFirstPage();


	/**
	 * Shows the first page of a group
	 */
	public void showGroup() {
		this.clearClientContext();
		stackManager.clear();
		checkInitialize();
		showPage( registerFirstPage() );
	}

	/**
	 * Lazy initialization
	 */
	private void checkInitialize() {
		if( firstTime ) {
			registerPages();
			firstTime = false;
		}
	}

	/**
	 * Vuelve a crear todas las paginas.
	 */
	public void reloadPages() {
		//limpiamos mapa de paginas
		pagesMap.clear();
		//marcamos para que se vuelvan a cargar las paginas la prox vez que entren
		firstTime = true;

		//limpiamos todo
		currentPageId = null;
		this.clearClientContext();
		stackManager.clear();
	}


	/**
	 * Register a new page
	 * @param pageName: name for referencing the page later
	 * @param component: component to add
	 */
	public void addPage( String pageName, GwtPage page ) {
		this.pagesMap.put( pageName , page );
		page.init( this, pageName );
	}

	/**
	 * Unregister a page
	 * @param pageName: name of the registered page
	 */
	public void removePage( String pageName ) {
		if( !this.pagesMap.containsKey( pageName ) ) {
			throw new RuntimeException( "No hay ninguna pagina registrada con el nombre: " + pageName );
		}
		this.pagesMap.remove( pageName );
	}

	/**
	 * Makes visible the page with the name specified, and hide the others.
	 * The page must belong to the the current PageGroup.
	 * @param pageName: registered name of the page
	 */
	public void showPage( String pageName ) {
		if( !this.pagesMap.containsKey( pageName ) ) {
			throw new RuntimeException( "No hay ninguna pagina registrada con el nombre: " + pageName );
		}
		updateStackManager();

		//notify onHide() in previous page
		notifyOnHide(currentPageId);

		currentPageId = pageName;
		GwtPage page = (GwtPage)this.pagesMap.get( pageName );
		this.addOnlyOnePage(page);
	}

	/**
	 * Makes visible the page with the name specified, in the page group specified.
	 * Se cierra el grupo de origen y se transfiere el contexto desde este al otro.
	 * @param pageGroup
	 * @param pageName
	 */
	public void showPage( String pageGroup, String pageName ) {
		String auxWinId = currentPageId;
		currentPageId = null;

		//notify onHide() in previous page
		notifyOnHide(auxWinId);

		//seguimos manteniendo el contexto y el stackManager
		controller.showPage( this.groupName, pageGroup, auxWinId, pageName, this.clientContext );
	}

	/**
	 * Notify to a page that it has been hidden.
	 */
	private void notifyOnHide(String pageName) {
		if(pageName != null) ((GwtPage)this.pagesMap.get( pageName )).onHide();
	}

	/**
	 * Internal method call when a page from other Page Group has been requested
	 * @param pageName
	 * @param context
	 */
	protected void requestPage(String sourceGroupName, String sourcePageName, String pageName,
			Map<String,Object> externalGroupContext) {
		checkInitialize();
		this.injectInContext(externalGroupContext);
		stackManager.clear();
		stackManager.addMovement( new ExternalHistoryMovement( sourceGroupName, sourcePageName ) );
		showPage( pageName );
	}

	/**
	 * Metodo interno, no utilizar.
	 * El controller llama a este metodo cuando se esta volviendo hacia atras, desde
	 * una page de un grupo hacia otra de otro grupo.
	 * Se inyecta el contexto del grupo anterior.
	 */
	public void requestBackwardPage(String pageName, Map<String,Object> externalGroupContext) {
		checkInitialize();
		injectInContext( externalGroupContext );
		GwtPage page = (GwtPage)this.pagesMap.get( pageName );
		currentPageId = pageName;

		//hacemos visible la ventana pero no llamamos a onVisible(), para mantener los datos originales
		page.render();
		this.controller.showPage( page );
	}


	/**
	 * Cleans the map and add the contents of the new map
	 * @param context
	 */
	private void injectInContext(Map<String,Object> context) {
		this.clearClientContext();
		this.clientContext.putAll(context);
		
		/////////////Modificado
//		if(context != this.clientContext){ // solo lo hago si el objeto que viene por parametro es diferente al que tengo
//			this.clearClientContext();
//			this.clientContext.putAll(context);
//		}

	}

	/**
	 * Guardamos en el stackManager el movimiento a la page anterior
	 */
	private void updateStackManager() {
		//si el grupo no estaba oculto
		if( currentPageId != null ) {
			stackManager.addMovement( new LocalHistoryMovement( currentPageId ) );
		}
	}

	/**
	 * Hide all pages
	 */
	public void hideAll() {
		controller.hideAllPages();
	}


	/**
	 * Add a widget to the main panel
	 * @param component
	 */
	private void addOnlyOnePage( GwtPage page ) {
		page.render();
		this.controller.showPage( page );
		page.beforeOnVisible();
	}

	/**
	 * Metodo interno, no utilizar.
	 * El stackManager llama a este metodo cuando quiere retroceder
	 * a una page local al grupo
	 */
	public void goToLocalGroup(String pageName) {
		//escondemos la ventana pero no llamamos a onHide(), para no refrescar los datos
		GwtPage page = (GwtPage)this.pagesMap.get( pageName );
		currentPageId = pageName;
		page.render();
		//no se llama a onVisible() para no refrescar los datos
		this.controller.showPage( page );
	}

	/**
	 * Metodo interno, no utilizar.
	 * El stackManager llama a este metodo cuando quiere retroceder
	 * a una page perteneciente a otro grupo.
	 * Limpiamos el stackManager, y seteamos
	 * el grupo como escondido
	 */
	public void goToExternalPage( String groupName, String winName ) {
		//escondemos la ventana pero no llamamos a onHide(), para no refrescar los datos
		//GwtPage page = (GwtPage)this.pagesMap.get( currentPageId );
		currentPageId = null;

		controller.goBarckwardToWindow( groupName, winName, clientContext );
	}

	/**
	 * Regresa a la pagina anterior, registrada en el stack de movimientos
	 */
	public void goBackward() {
		stackManager.goBackward();
	}

	/**
	 * Internal method. Do not use it.
	 * Notify all pages to clear their stuff
	 */
	public boolean onUseCaseExit() {
		GwtPage page;
		Set<Entry<String,GwtPage>> entrySet = this.pagesMap.entrySet();
		//es para saber si se abandona el caso de uso o no, por defecto si
		boolean exitValue;
		boolean leaveCase = true;
		for (Iterator<Entry<String,GwtPage>> iterator = entrySet.iterator(); iterator.hasNext();) {
			page = iterator.next().getValue();
			exitValue = page.beforeOnUseCaseExit();
			if(!exitValue){
				leaveCase = exitValue;
			}
		}
		return leaveCase;

	}


	/**
	 * Internal injecton. Do not use it.
	 */
	public void setController(GwtController controller) {
		this.controller = controller;
	}

	/**
	 * @return Apllication controller
	 */
	public GwtController getController() {
		return this.controller;
	}

	public void doSessionExperied() {
		controller.doSessionExperied();
	}

	public void doUnexpectedException(Throwable e) {
		controller.doUnexpectedFailure( e );
	}

	/**
	 * @return current page of the group or null if none
	 */
	public GwtPage getCurrentPage() {
		if(currentPageId != null) {
			return (GwtPage) pagesMap.get(currentPageId);
		}
		return null;
	}







	/**
	 * @param text
	 * @param param
	 * @see com.net.latin.navigation.gwt.client.widget.base.GwtController#addAlertMessage(java.lang.String, java.lang.Object)
	 */
	public void addAlertMessage(String text, Object param) {
		controller.addAlertMessage(text, param);
	}

	/**
	 * @param text
	 * @param params
	 * @see com.net.latin.navigation.gwt.client.widget.base.GwtController#addAlertMessage(java.lang.String, java.lang.Object[])
	 */
	public void addAlertMessage(String text, Object[] params) {
		controller.addAlertMessage(text, params);
	}

	/**
	 * @param text
	 * @see com.net.latin.navigation.gwt.client.widget.base.GwtController#addAlertMessage(java.lang.String)
	 */
	public void addAlertMessage(String text) {
		controller.addAlertMessage(text);
	}

	/**
	 * @param messagesList
	 * @see com.net.latin.navigation.gwt.client.widget.base.GwtController#addAllAlertMessages(java.util.List)
	 */
	public void addAllAlertMessages(List<String> messagesList) {
		controller.addAllAlertMessages(messagesList);
	}

	/**
	 * @param messagesList
	 * @see com.net.latin.navigation.gwt.client.widget.base.GwtController#addAllErrorMessages(java.util.List)
	 */
	public void addAllErrorMessages(List<String> messagesList) {
		controller.addAllErrorMessages(messagesList);
	}

	/**
	 * @param messagesList
	 * @see com.net.latin.navigation.gwt.client.widget.base.GwtController#addAllOkMessages(java.util.List)
	 */
	public void addAllOkMessages(List<String> messagesList) {
		controller.addAllOkMessages(messagesList);
	}

	/**
	 * @param text
	 * @param param
	 */
	public void addErrorMessage(String text, Object param) {
		controller.addErrorMessage(text, param);
	}

	/**
	 * @param text
	 * @param params
	 * @see com.net.latin.navigation.gwt.client.widget.base.GwtController#addErrorMessage(java.lang.String, java.lang.Object[])
	 */
	public void addErrorMessage(String text, Object[] params) {
		controller.addErrorMessage(text, params);
	}

	/**
	 * @param text
	 * @see com.net.latin.navigation.gwt.client.widget.base.GwtController#addErrorMessage(java.lang.String)
	 */
	public void addErrorMessage(String text) {
		controller.addErrorMessage(text);
	}

	/**
	 * @param text
	 * @param param
	 * @see com.net.latin.navigation.gwt.client.widget.base.GwtController#addLoadingMessage(java.lang.String, java.lang.Object)
	 */
	public void addLoadingMessage(String text, Object param) {
		controller.addLoadingMessage(text, param);
	}

	/**
	 * @param text
	 * @param params
	 * @see com.net.latin.navigation.gwt.client.widget.base.GwtController#addLoadingMessage(java.lang.String, java.lang.Object[])
	 */
	public void addLoadingMessage(String text, Object[] params) {
		controller.addLoadingMessage(text, params);
	}

	/**
	 * @param text
	 * @see com.net.latin.navigation.gwt.client.widget.base.GwtController#addLoadingMessage(java.lang.String)
	 */
	public void addLoadingMessage(String text) {
		controller.addLoadingMessage(text);
	}

	/**
	 * @param text
	 * @param param
	 * @see com.net.latin.navigation.gwt.client.widget.base.GwtController#addOkMessage(java.lang.String, java.lang.Object)
	 */
	public void addOkMessage(String text, Object param) {
		controller.addOkMessage(text, param);
	}

	/**
	 * @param text
	 * @param params
	 * @see com.net.latin.navigation.gwt.client.widget.base.GwtController#addOkMessage(java.lang.String, java.lang.Object[])
	 */
	public void addOkMessage(String text, Object[] params) {
		controller.addOkMessage(text, params);
	}

	/**
	 * @param text
	 * @see com.net.latin.navigation.gwt.client.widget.base.GwtController#addOkMessage(java.lang.String)
	 */
	public void addOkMessage(String text) {
		controller.addOkMessage(text);
	}

	public void addMessage(GwtMsg msg) {
		controller.addMessage(msg);
	}

	/**
	 * @param name
	 * @param value
	 */
	public void addToClientContext(String name, Object value) {
		this.clientContext.put(name, value);
	}

	/**
	 *Cleans the clientContext.
	 */
	public void clearClientContext() {
		this.clientContext.clear();
	}

	/**
	 *
	 * @see com.net.latin.navigation.gwt.client.widget.base.GwtController#clearMessages()
	 */
	public void clearMessages() {
		controller.clearMessages();
	}

	/**
	 * @return
	 * @see com.net.latin.navigation.gwt.client.widget.base.GwtController#getBasePath()
	 */
	public String getBasePath() {
		return controller.getBasePath();
	}

	/**
	 * @param name
	 * @return
	 */
	public Object getFromClientContext(String name) {
		return this.clientContext.get(name);
	}

	/**
	 * @param name
	 * @return
	 */
	public boolean isInClientContext(String name) {
		return this.clientContext.containsKey(name);
	}

	/**
	 * @param name
	 */
	public Object removeFromClientContext(String name) {
		return this.clientContext.remove(name);
	}

	/**
	 * @param name
	 * @return
	 * @see com.net.latin.navigation.gwt.client.widget.base.GwtController#getFromAppContext(java.lang.String)
	 */
	public Object getFromAppContext(String name) {
		return controller.getFromAppContext(name);
	}

	/**
	 * @param name
	 * @return
	 * @see com.net.latin.navigation.gwt.client.widget.base.GwtController#isInAppContext(java.lang.String)
	 */
	public boolean isInAppContext(String name) {
		return controller.isInAppContext(name);
	}

	/**
	 * @param name
	 * @see com.net.latin.navigation.gwt.client.widget.base.GwtController#removeFromAppContext(java.lang.String)
	 */
	public void removeFromAppContext(String name) {
		controller.removeFromAppContext(name);
	}

	/**
	 * @param name
	 * @param value
	 * @see com.net.latin.navigation.gwt.client.widget.base.GwtController#addToAppContext(java.lang.String, java.lang.Object)
	 */
	public void addToAppContext(String name, Object value) {
		controller.addToAppContext(name, value);
	}

	/**
	 *
	 * @see com.net.latin.navigation.gwt.client.widget.base.GwtController#clearAppContext()
	 */
	public void clearAppContext() {
		controller.clearAppContext();
	}

	/**
	 * @return nombre del grupo
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * Only call this method from a Login Page.
	 * @see com.net.latin.navigation.gwt.client.widget.base.GwtController
	 */
	public void initAppWithUserLogged() {
		controller.initAppWithUserLogged();
	}

	public void showExternalLink(String url) {
		controller.showExternalLink(url);
	}














}
