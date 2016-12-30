package net.latin.client.widget.base;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.HistoryListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import gwt.material.design.client.ui.MaterialContainer;
import gwt.material.design.client.ui.MaterialHeader;
import gwt.material.design.client.ui.MaterialNavBar;
import gwt.material.design.client.ui.MaterialPanel;
import net.latin.client.i18n.LnwI18n;
import net.latin.client.i18n.LnwI18nImpl;
import net.latin.client.rpc.DefaultGwtCallbackErrorHandler;
import net.latin.client.rpc.GwtAsyncCallback;
import net.latin.client.rpc.GwtRpc;
import net.latin.client.rpc.GwtServerCreator;
import net.latin.client.rpc.commonUseCase.CommonUseCaseClient;
import net.latin.client.rpc.commonUseCase.CommonUseCaseClientAsync;
import net.latin.client.rpc.commonUseCase.InitialInfo;
import net.latin.client.widget.base.errorPage.GwtErrorPage;
import net.latin.client.widget.base.helpPage.GwtDefaultHelpPage;
import net.latin.client.widget.base.helpPage.GwtHelpPage;
import net.latin.client.widget.button.GwtButton;
import net.latin.client.widget.link.GwtExternalLink;
import net.latin.client.widget.menu.GwtMenuBar;
import net.latin.client.widget.menu.GwtMenuBarListener;
import net.latin.client.widget.modal.GwtModal;
import net.latin.client.widget.modal.GwtModalPopup;
import net.latin.client.widget.msg.GwtMsg;
import net.latin.client.widget.msg.GwtMsgFormat;
import net.latin.client.widget.msg.GwtMsgNotification;

/**
 * Controls all the navigation in the client tier of a GWT application. It's the
 * GWT EntryPoint class, which is the first object to be created by GWT. Every
 * application must create it own implementation of the controller and sets all
 * the configuration methods.
 *
 */
public abstract class GwtController implements EntryPoint, HistoryListener, CloseHandler<Window>, ResizeHandler {

	/**
	 * Html panel which shows a loading dialog
	 */
	private static final String LOADING_PANEL = "loadingPanel";

	/**
	 * Html panel which contains the whole gwt application
	 */
	protected static final String GWT_APPLICATION_PANEL = "gwtApplicationPanel";

	/**
	 * Html id which contains the logout button
	 */
	private static final String GWT_LOGOUT_SLOT = "gwtLogoutSlot";

	/**
	 * Token utilizado para evitar volver atrás con el boton del browser
	 */
	private static final String HISTORY_TOKEN = "_";

	/**
	 * CSS
	 */
	private static final String CSS_MAIN_COMPONENT = "GwtControllerMainComponent";
	private static final String CSS_PAGES_PANEL = "GwtControllerPagesPanel";
	private static final String CSS_FRAME_PANEL = "GwtControllerFramePanel";
	private static final String CSS_LOGOUT_BUTTON = "GwtControllerLogoutButton";
	private static final String CSS_CAMBIAR_PERFIL_BUTTON = "GwtControllerCambiarPerfilButton";

	/**
	 * Key used by security issues
	 */
	public final static String COMMON_USE_CASE_SERVER_KEY = "__CommonUseCaseServer";
	/**
	 * Key used by the persistence utils server
	 */
	public final static String GWT_PERSISTENCE_UTILS_SERVER_KEY = "__GwtPersistenceUtilsCase";

	/**
	 * Key used to find the default LnwI18n
	 */
	public static final String I18N_DEFAULT_KEY = "default";

	/**
	 * Key used to find erros in getInitialInfo
	 */
	private static final String ERRORS_KEY = "errors";

	/**
	 * Global access to reach the controller. <br>
	 * The controller is the first object to be created, so it's always <br>
	 * initialized.
	 */
	public static GwtController instance;
	/**
	 * Global access to reach de current I18n config. <br>
	 * The controller is the first object to be created, so it's always <br>
	 * initialized.
	 */
	public static LnwI18n defaultI18n;

	private final Map<String, Object> applicationContext;
	private final Map<String, GwtPageGroup> pageGroups;
	private final HashMap<String, LnwI18n> i18nMap;
	protected final MaterialPanel mainComponent;
	private final FlowPanel pagesPanel;
	private final GwtMsgNotification msg;
	private final Frame framePanel;
	private final GwtModalPopup modalPopup;
//	private final GwtModalBackground modalBackground;
	private final DefaultGwtCallbackErrorHandler errorHandler;
	private GwtErrorPage errorPage;
	private GwtHelpPage helpPage;
	private GwtMenuBar menu;
	private GwtButton logoutButton;
	private String basePath;
	private CommonUseCaseClientAsync commonUseCaseServer;
	private String currentAccess;
	private String firstPageDesc;
	private String sessionExpiredUrl;
	protected String loginGroup;
	private InitialInfo initialInfo;
	protected GwtPageGroup lastGroupSelected;

	private Button reloginButton;

	private String sauUrl;
	private String sauSession;

	private RootPanel reloginSlot;
	
	private GwtGroupLoader groupLoader;

	private MaterialNavBar toolbar;

	private FlowPanel componentsPanel;

	public GwtController() {
		// final variables initialization
		pageGroups = new HashMap<String, GwtPageGroup>();
		applicationContext = new HashMap<String, Object>();
		i18nMap = new HashMap<String, LnwI18n>();
		mainComponent = new MaterialPanel();
		pagesPanel = new FlowPanel();
		msg = new GwtMsgNotification();
		framePanel = new Frame();
		modalPopup = new GwtModalPopup();
		errorHandler = new DefaultGwtCallbackErrorHandler();
		groupLoader = createGroupLoader();
		
		// TODO hacer un metodo marcar menu
		// set static instance
		GwtController.instance = this;

		configure();
	
		// manage I18n config
		createI18n();
	
		this.basePath = registerServerBasePath();
		// register and render error page
		this.errorPage = registerErrorPage();
		this.errorPage.getErrorPage().render();
		this.helpPage = registerHelpPage();
		this.firstPageDesc = registerMainPageDescription();
		this.sessionExpiredUrl = registerSessionExpiredUrl();
		visualInit();
		createCommonUseCaseServer();
		registerWindowListener();
		registerPageGroups();
		loadLoginGroup();
	}



	protected abstract GwtGroupLoader createGroupLoader();


	/**
	 * ########################################################################
	 * #####################BEGIN CONFIGURATION METHODS########################
	 * ########################################################################
	 */

	/**
	 * Lets to the subclasses do specifics configurations.
	 */
	protected void configure() {
	}

	/**
	 * Register here all the PageGroup you want to use in your application.
	 * <p>
	 * Example:
	 *
	 * <pre>
	 * this.addPageGroup(&quot;MyGroup&quot;, new MyGroup());
	 * </pre>
	 *
	 */
	protected abstract void registerPageGroups();

	/**
	 * Register here the id of the Login Group. The Login Group would be the
	 * first group to be shown, before the menu is built.
	 * <p>
	 * The Login Group should be register in <code>registerPageGroups()</code>
	 * <p>
	 * Once the user has been logged successfully in the Login Group, you must
	 * call <code>initAppWithUserLogged()</code> in the Controller
	 * <p>
	 * If the application has not login group, <code>return null</code>.
	 *
	 */
	protected abstract String registerLoginGroup();

	/**
	 * Sets base url to use in the client tier.
	 * <p>
	 * Example:
	 *
	 * <pre>
	 * return &quot;/myProject/&quot;;
	 * </pre>
	 */
	protected abstract String registerServerBasePath();

	/**
	 * Sets the name of the first PageGroup to be shown.
	 */
	protected abstract String registerFirstPageGroup();

	/**
	 * Sets the url of the common use case server.
	 * <p>
	 * Example:
	 *
	 * <pre>
	 * return &quot;commonUseCaseServer.do&quot;;
	 * </pre>
	 */
	protected abstract String registerCommonUseCaseServer();

	/**
	 * Sets the url of the persistence server, used by GwtPersistenceUtilsClient
	 * and GwtPersistenceUtilsCase.
	 * <p>
	 * Example:
	 *
	 * <pre>
	 * return &quot;__persistenceServer.do&quot;;
	 * </pre>
	 *
	 * @return
	 */
	protected abstract String registerPersistenceServer();

	/**
	 * Sets the application specific error page. You can use
	 * <code>GwtDefaultErrorPage</code>.
	 */
	protected abstract GwtErrorPage registerErrorPage();

	/**
	 * Permite indicar la implementacion de GWT.UncaughtExceptionHandler a usar
	 * por la aplicacion.
	 *
	 * Por defecto no se especifica una implementacion por lo tanto no se
	 * manejan las Uncaught Exceptions. Esto es para mantener el comportamiento
	 * de las aplicaciones existentes.
	 *
	 * @return Implementacion a usar o null si no se quiere activar el handler.
	 */
	protected GWT.UncaughtExceptionHandler registerUncaughtExceptionHandler() {
		return null;
	}

	/**
	 * Sets the application specific help page. By the default it is uses
	 * <code>GwtDefaultHelpPage</code>
	 */
	protected GwtHelpPage registerHelpPage() {
		return new GwtDefaultHelpPage();
	}

	/**
	 * Sets the main page title to be shown as the first menu entry
	 */
	protected abstract String registerMainPageDescription();

	/**
	 * Sets the url that will be opened if an user tries to do call to the
	 * server with his session expired.
	 */
	protected abstract String registerSessionExpiredUrl();

	/**
	 * Sets a builder to creates the General Info Panel, shows in the
	 * application menu.
	 */
//	protected GeneralInfoPanelBuilder registerGeneralInfoPanelBuilder() {
//		return new GeneralInfoPanelBuilder();
//	}

	/**
	 * Sets the application LnwI18n. There must be one with the key
	 * <code>GwtController.I18N_DEFAULT_KEY</code>
	 */
	protected void registerLnwI18n(Map<String, LnwI18n> i18nMap) {
		i18nMap.put(I18N_DEFAULT_KEY, new LnwI18nImpl());
	}

	/**
	 * Sets the aplication toolbar. It will be shown at the top of the menu. By
	 * default it is used <code>GwtMenuBarToolBar</code> with all its button
	 * desactivated
	 */
//	protected GwtMenuBarToolBar registerMenuBarToolBar() {
//		return new GwtDefaultMenuBarToolBar(false, false);
//	}


	/**
	 * ########################################################################
	 * #######################END CONFIGURATION METHODS########################
	 * ########################################################################
	 */

	/**
	 * Creates visual content
	 */
	protected void visualInit() {
		// main component
		// menu
		menu = new GwtMenuBar(this);

		componentsPanel = new FlowPanel();
		componentsPanel.getElement().setAttribute("main", "");
		componentsPanel.setStyleName(CSS_PAGES_PANEL);
		componentsPanel.setHeight(Window.getClientHeight()-64 + "px");
		Window.addResizeHandler(this);

		toolbar = new MaterialNavBar();
		toolbar.addStyleName("toolbar");
		toolbar.setActivates("sidenav");
		
		
		MaterialHeader mainHeader = new MaterialHeader();
		mainHeader.add(toolbar);
		mainHeader.add(menu);
		MaterialContainer mainContainer = new MaterialContainer();
		mainContainer.add(componentsPanel);
		mainComponent.add(mainHeader);
		mainComponent.add(mainContainer);
		// header panel
		VerticalPanel headerPanel = new VerticalPanel();
//		headerPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		headerPanel.setSize("100%", "0px");
		componentsPanel.add(headerPanel);

		// add msg panel to header
		headerPanel.add(msg.render());

		// pages panel
		componentsPanel.add(pagesPanel);

		// frame panel for external links
		framePanel.setVisible(false);
		framePanel.setStyleName(CSS_FRAME_PANEL);

		pagesPanel.add(framePanel);

		logoutButton = new GwtButton();
		logoutButton.setVisible(false);
		logoutButton.setStyleName(CSS_LOGOUT_BUTTON);
		logoutButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				GwtController.instance.logout();
			}
		});

		// Si hay un slot para el boton de logout, agrego el boton al slot
		RootPanel logoutSlot = RootPanel.get(GWT_LOGOUT_SLOT);
		if (logoutSlot != null) {
			logoutSlot.add(logoutButton);
		}
		
		reloginSlot = RootPanel.get("divCambiarPerfil");
		if (reloginSlot != null) {
			reloginButton=new Button("Cambiar Perfil");
			reloginButton.setStyleName("GwtControllerCambiarPerfilButton");
			reloginButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					GwtController.instance.relogin();
				}
			});
			reloginSlot.add(reloginButton);
		}
	}
	
	public void setReloginVisible(Boolean visible){
		if (reloginSlot!=null){
			reloginSlot.setVisible(visible);
		}
	}

	public void doRelogin(){
		relogin();
	}
	
	protected void relogin() {
		Hidden hiddenSessionId;
		FormPanel postForm = new FormPanel("_self");
		postForm.addSubmitHandler(new SubmitHandler() {
			public void onSubmit(SubmitEvent event) {}
		});
		postForm.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			public void onSubmitComplete(SubmitCompleteEvent event) {}
		});
		postForm.setMethod(FormPanel.METHOD_POST);
		postForm.setVisible(false);
		VerticalPanel auxPanel = new VerticalPanel();
		postForm.add(auxPanel);
		
		hiddenSessionId = new Hidden("sessionId");
		hiddenSessionId.setValue(sauSession);
		auxPanel.add(hiddenSessionId);
		RootPanel.get().add(postForm);
		postForm.setAction(sauUrl + "Inicio.do");
		postForm.submit();
	}


	/**
	 * Load the id of the Login Group, if there is any
	 */
	protected void loadLoginGroup() {
		this.loginGroup = null;
		String groupName = registerLoginGroup();
		if (groupName != null) {
			if (!pageGroups.containsKey(groupName)) {
				throw new RuntimeException("The login group was not registered in the registerPageGroups() method");
			} else {
				this.loginGroup = groupName;
			}
		}
	}

	public void onModuleLoad() {
		RootPanel.get().add(mainComponent);

		initUncaughtExceptionHandler();

		initHistoryManagement();

		// check if there is a Login Group
		if (this.loginGroup != null) {
			hideHtmlLoadingMessage();
			// show login group
			showPageGroup(this.loginGroup);

		} else {
			// init the application
			initAppWithUserLogged();
		}
	}
	

	/**
	 * Inicializa un manejador de excepciones que no fueron tratadas y pueden
	 * volver inestable al sistema. Permite que el usuario se de cuenta de que
	 * algo esta mal y advertirle que no confie en lo mostrado y avise al
	 * administrador.
	 *
	 * Si esta corriendo en Hosted Mode o no se a especificado un handler para
	 * estos casos, no hace nada.
	 */
	protected void initUncaughtExceptionHandler() {
		GWT.UncaughtExceptionHandler handler = registerUncaughtExceptionHandler();

		if (!GWT.isScript() || handler == null)
			return;

		GWT.setUncaughtExceptionHandler(handler);
	}

	/**
	 * Carga el Token inicial para el manejo del History
	 */
	protected void initHistoryManagement() {
		String initToken = History.getToken();
		if (initToken.length() == 0) {
			initToken = HISTORY_TOKEN;
		}
		History.newItem(HISTORY_TOKEN);
		History.addHistoryListener(this);
	}

	/**
	 * Call this method when the user has logged successfully to the system. It
	 * builds the menu entries accodting to user grants.
	 */
	public void initAppWithUserLogged() {
		buildMenu();
		setMenuBarVisible(true);
		// solo muestro el boton de logout si hay un Login registrado
		if (loginGroup != null) {
			logoutButton.setVisible(true);
		}
	}

	/**
	 * Creates the CommonUseCase server to be used for security issues
	 */
	protected void createCommonUseCaseServer() {
		String url = this.registerCommonUseCaseServer();
		GwtRpc.getInstance().addServer(COMMON_USE_CASE_SERVER_KEY, url, new GwtServerCreator() {
			public Object createServer() {
				return GWT.create(CommonUseCaseClient.class);
			}

			public String getBasePath() {
				return GwtController.this.getBasePath();
			}
		});
		this.commonUseCaseServer = (CommonUseCaseClientAsync) GwtRpc.getInstance()
				.getServer(COMMON_USE_CASE_SERVER_KEY);
	}

	/**
	 * Registra un WindowListener para manejar eventos de la ventana del Browser
	 */
	protected void registerWindowListener() {
		Window.addCloseHandler(this);
	}

	/**
	 * Cuando la ventana del Browser se esta cerrando
	 */
	public String onWindowClosing() {
		return null;
	}

	/**
	 * Cuando la ventana del Browser se cerró. Avisamos al server para que
	 * cierre la session.
	 */
	public void onWindowClosed() {
		commonUseCaseServer.onUserExit(new GwtAsyncCallback(errorHandler) {
			public void onSuccess(Object result) {
			}
		});
	}

	public void logout() {
		onWindowClosed();

		// Environment reset
		this.initialInfo = null;
		this.currentAccess = null;
		this.menu.resetWidget();
		clearAppContext();

		logoutButton.setVisible(false);
		GwtModal.unblockScreen();
		clearMessages();

		// check if there is a Login Group
		if (this.loginGroup != null) {
			showPageGroup(this.loginGroup);
		} else {
			initAppWithUserLogged();
		}
	}

	/**
	 * Calls the I18n registration and sets the default I18n
	 */
	private void createI18n() {
		registerLnwI18n(i18nMap);
		LnwI18n lnwI18n = findI18n(I18N_DEFAULT_KEY);
		GwtController.defaultI18n = lnwI18n;
	}

	/**
	 * @return the specified I18n
	 */
	private LnwI18n findI18n(String key) {
		LnwI18n lnwI18n = (LnwI18n) i18nMap.get(key);
		if (lnwI18n == null) {
			throw new RuntimeException("No hay ningún I18n llamado " + I18N_DEFAULT_KEY);
		}
		return lnwI18n;
	}

	/**
	 * Generates the menu entries based on the information sent by the security
	 * server
	 */
	protected void buildMenu() {
		commonUseCaseServer.getInitialInfo(new GwtAsyncCallback(errorHandler) {
			public void onSuccess(Object result) {
				InitialInfo info = (InitialInfo) result;
				buildMenuCallback(info);
				initToolbar(info.getAplicacionDescripcion());
			}
		});
	}

	protected void initToolbar(String appDescripcion) {
		HTMLPanel h1=new HTMLPanel("h5",appDescripcion);
		h1.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		
		toolbar.add(h1);
	}



	/**
	 * Async answer for asking the menu info to the security server. We build
	 * the menu.
	 */
	protected void buildMenuCallback(InitialInfo initialInfo) {
		this.initialInfo = initialInfo;
		hideHtmlLoadingMessage();
		sauUrl = initialInfo.getSauUrl();
		sauSession=initialInfo.getSessionIdSau();
		this.menu.resetWidget();
		this.menu.build(initialInfo);
		showFirstPageGroup();
		List<CustomBean> errorList = initialInfo.getAdditionalInfo().getList(ERRORS_KEY);
		if (errorList != null && !errorList.isEmpty()) {
			errorOnInitialInfo(errorList);
		}
	}

	protected void errorOnInitialInfo(List<CustomBean> errorList) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < errorList.size(); i++) {
			sb.append(((CustomBean) errorList.get(0)).getString("msg"));
			if (i != errorList.size() - 1) {
				sb.append('\n');
			}
		}
		Window.alert(sb.toString());
	}

	/**
	 * Hides the loading panel in html
	 */
	protected void hideHtmlLoadingMessage() {
		RootPanel.get(LOADING_PANEL).setVisible(false);
	}

	/**
	 * Register a new Page Group to the controller
	 *
	 * @param groupName
	 * @param pageGroup
	 */
	public void addPageGroup(String groupName, GwtPageGroup pageGroup) {
		pageGroups.put(groupName, pageGroup);
		pageGroup.setController(this);
		pageGroup.init(groupName);
	}

	/**
	 * Try to acces to the first page from a Page Group. It checks access first.
	 *
	 * @param pageGroup
	 */
	public void tryShowPageGroup(String pageGroup) {
		checkAccess(pageGroup);
	}

	/**
	 * Shows the first page from a Page Group
	 *
	 * @param pageGroup
	 */
	public void showPageGroup(final String pageGroup) {

		// por defecto se cambia de grupo
		boolean changeGroup = true;
		// notify onUseCaseExit() in last page
		if (lastGroupSelected != null) {
			changeGroup = lastGroupSelected.onUseCaseExit();
		}
		// si es necesario cambiar de caso de uso
		if (changeGroup) {
			GwtModal.blockScreen();
			GwtPageGroup group = (GwtPageGroup) pageGroups.get(pageGroup);
			if (group==null){
				groupLoader.loadGroup(pageGroup,this);
			}
			else{
				finishGroupLoad(group);
			}
		}
	}
	public void finishGroupLoading(String pageGroup){
		GwtPageGroup group = (GwtPageGroup) pageGroups.get(pageGroup);
		finishGroupLoad(group);
	}
	
	private void finishGroupLoad(GwtPageGroup group){
		// udpate last group
		lastGroupSelected = group;
		GwtModal.unblockScreen();
		group.showGroup();
	}

	/**
	 * Shows a page in the pageGroups panel, and hide other pages
	 *
	 * @param pageGroup
	 * @param pageName
	 */
	public void showPage(GwtPage page) {
		hideAllPages();
		pagesPanel.add(page);
		componentsPanel.getElement().setScrollTop(0);
	}

	/**
	 * Request a page from a pageGroup that is not the current pageGroup
	 */
	public void showPage(String sourceGroupName, String destinyGroupName, String sourcePageName, String pageName,
			Map<String, Object> groupContext) {
		GwtPageGroup group = (GwtPageGroup) pageGroups.get(destinyGroupName);

		// FIXME check access to use case
		group.requestPage(sourceGroupName, sourcePageName, pageName, groupContext);
	}

	/**
	 * Hides all pages from the pageGroups panel
	 */
	public void hideAllPages() {
		pagesPanel.clear();
		clearMessages();
	}

	/**
	 * Shows the first pageGroup
	 */
	public void showFirstPageGroup() {
		tryShowPageGroup(registerFirstPageGroup());
	}

	/**
	 * Goes to an url which is not a GwtPage. It will be shown in an IFRAME.
	 *
	 * @param url
	 */
	public void showExternalLink(String url) {
		hideAllPages();
		pagesPanel.add(framePanel);
		framePanel.setUrl(url);
		framePanel.setVisible(true);
	}

	/**
	 * Retrocedemos a una window determinada, de otro grupo.
	 */
	public void goBarckwardToWindow(String groupName, String pageName, Map<String, Object> groupContext) {
		GwtPageGroup group = (GwtPageGroup) pageGroups.get(groupName);
		group.requestBackwardPage(pageName, groupContext);
	}

	/**
	 * Asks the security server for user access to a PageGroup. If the user has
	 * no access, the action is denied.
	 */
	private void checkAccess(String pageGroup) {
		this.currentAccess = pageGroup;
		//TENDRIA QUE BLOQUEAR LA PANTALLA ACA
		GwtModal.blockScreen();
		this.commonUseCaseServer.checkAccess(pageGroup, new GwtAsyncCallback(errorHandler) {
			public void onSuccess(Object result) {
				checkAccessCallback((Boolean) result);
			}
		});
	}

	/**
	 * Correct answer for the security access.
	 *
	 * @param boolean1
	 */
	private void checkAccessCallback(Boolean rta) {
		if (rta.booleanValue()) {
			showPageGroup(this.currentAccess);
		} else {
			doAccessDenied(currentAccess);
		}
	}

	/**
	 * Shows the error page
	 */
	private void showErrorPage() {
		GwtPage page = errorPage.getErrorPage();
		page.beforeOnVisible();
		showPage(page);
	}

	/**
	 * Treats errors
	 */
	public void doUnexpectedFailure(String errorMsg, String errorCode) {
		GwtModal.unblockScreen();
		errorPage.setServerError(errorMsg, errorCode);
		showErrorPage();
	}

	/**
	 * Treats errors
	 */
	public void doUnexpectedFailure(String errorMsg) {
		doUnexpectedFailure(errorMsg, null);
	}

	/**
	 * Treats errors
	 */
	public void doUnexpectedFailure(Throwable t) {
		doUnexpectedFailure("", t.getMessage());
	}

	/**
	 * Treats unauthorized access.
	 *
	 * @param currentAccess
	 */
	private void doAccessDenied(String currentAccess) {
		doSessionExperied();
		// En vez de mostrar un mensaje de acceso denegado, vamos a la pagina de
		// session expirada
		// errorPage.setAccessDeniedError(currentAccess);
		// showErrorPage();
	}

	public void doSessionExperied() {
		GwtExternalLink.openLocation(this.sessionExpiredUrl);
	}

	public void onHistoryChanged(String historyToken) {
		History.newItem(HISTORY_TOKEN);
	}

	/**
	 * @param name
	 * @return an object saved in the application context
	 */
	public Object getFromAppContext(String name) {
		return this.applicationContext.get(name);
	}

	/**
	 * Add an object to the application context
	 *
	 * @param name
	 * @param value
	 */
	public void addToAppContext(String name, Object value) {
		this.applicationContext.put(name, value);
	}

	/**
	 * Removes and object from the application context
	 *
	 * @param name
	 */
	public void removeFromAppContext(String name) {
		this.applicationContext.remove(name);
	}

	/**
	 * @param name
	 * @return true if the object is in the application context
	 */
	public boolean isInAppContext(String name) {
		return this.applicationContext.containsKey(name);
	}

	/**
	 * Clear all the objects of the application context
	 */
	public void clearAppContext() {
		this.applicationContext.clear();
	}

	/**
	 * @return the basePath
	 */
	public String getBasePath() {
		return basePath;
	}

	/**
	 * Show an Alert message
	 *
	 * @param text
	 */
	public void addAlertMessage(String text) {
		msg.addAlertMessage(text);
	}

	/**
	 * Show an Alert message
	 *
	 * @param text
	 * @param param
	 */
	public void addAlertMessage(String text, Object param) {
		this.addAlertMessage(text, new Object[] { param });
	}

	/**
	 * Show an Alert message
	 *
	 * @param text
	 * @param params
	 */
	public void addAlertMessage(String text, Object[] params) {
		msg.addAlertMessage(GwtMsgFormat.getMsg(text, params));
	}

	/**
	 * Show a list of Error message
	 *
	 * @param messagesList
	 */
	public void addAllErrorMessages(List<String> messagesList) {
		msg.addAllErrorMessages(messagesList);
	}

	/**
	 * Show a list of Ok messages
	 *
	 * @param messagesList
	 */
	public void addAllOkMessages(List messagesList) {
		msg.addAllOkMessages(messagesList);
	}

	/**
	 * Show a list of Alert messages
	 *
	 * @param messagesList
	 */
	public void addAllAlertMessages(List messagesList) {
		msg.addAllAlertMessages(messagesList);
	}

	/**
	 * Show an Error message
	 *
	 * @param text
	 */
	public void addErrorMessage(String text) {
		msg.addErrorMessage(text);
	}

	/**
	 * Show an Error message
	 *
	 * @param text
	 * @param param
	 */
	public void addErrorMessage(String text, Object param) {
		this.addErrorMessage(text, new Object[] { param });
	}

	/**
	 * Show an Error message
	 *
	 * @param text
	 * @param params
	 */
	public void addErrorMessage(String text, Object[] params) {
		msg.addErrorMessage(GwtMsgFormat.getMsg(text, params));
	}

	/**
	 * Show a Loading message
	 *
	 * @param text
	 */
	public void addLoadingMessage(String text) {
		msg.addLoadingMessage(text);
	}

	/**
	 * Show a Loading message
	 *
	 * @param text
	 * @param param
	 */
	public void addLoadingMessage(String text, Object param) {
		this.addLoadingMessage(text, new Object[] { param });
	}

	/**
	 * Show a Loading message
	 *
	 * @param text
	 * @param params
	 */
	public void addLoadingMessage(String text, Object[] params) {
		msg.addLoadingMessage(GwtMsgFormat.getMsg(text, params));
	}

	/**
	 * Show an Ok message
	 *
	 * @param text
	 */
	public void addOkMessage(String text) {
		msg.addOkMessage(text);
	}

	/**
	 * Show a message
	 *
	 * @param text
	 */
	public void addMessage(GwtMsg gwtMsg) {
		this.msg.addMessage(gwtMsg);
	}

	/**
	 * Show an Ok message
	 *
	 * @param text
	 * @param param
	 */
	public void addOkMessage(String text, Object param) {
		this.addOkMessage(text, new Object[] { param });
	}

	/**
	 * Show an Ok message
	 *
	 * @param text
	 * @param params
	 */
	public void addOkMessage(String text, Object[] params) {
		msg.addOkMessage(GwtMsgFormat.getMsg(text, params));
	}

	/**
	 * Remove all current messages
	 */
	public void clearMessages() {
		msg.clear();
	}

	/**
	 * @return first page description
	 */
	public String getFirstPageDesc() {
		return firstPageDesc;
	}

	/**
	 * If true, shows a special menu entry to acces the Welcome UseCase.
	 */
	public boolean showFirstWelcomeEntry() {
		return true;
	}

	/**
	 * @return the initialInfo, which contains general user info
	 */
	public InitialInfo getInitialInfo() {
		return initialInfo;
	}

	/**
	 * @return the current LnwI18n
	 */
	public final LnwI18n getI18n() {
		return GwtController.defaultI18n;
	}

	/**
	 * Cambia la implementacion default de I18n. Debido a esto, todos los
	 * GwtPageGroup de la aplicacion deben crear nuevamente sus paginas, por lo
	 * que puede ser operacion lenta. Luego de esto se muestra la pagina de
	 * inicio.
	 */
	public void setDefaultI18n(String key) {
		// change I18n
		GwtController.defaultI18n = findI18n(key);

		// hide current page
		hideAllPages();

		// reload pages
		Set set = pageGroups.entrySet();
		GwtPageGroup group;
		for (Iterator iterator = set.iterator(); iterator.hasNext();) {
			group = (GwtPageGroup) ((Entry) iterator.next()).getValue();
			group.reloadPages();
		}

		// reload error page
		this.errorPage = registerErrorPage();

		showFirstPageGroup();
	}

	public CommonUseCaseClientAsync getCommonUseCaseServer() {
		return this.commonUseCaseServer;
	}

	/**
	 * Bloquea la pantalla y los eventos.
	 *
	 * @param msg
	 */
	public final void blockScreen(String msg) {
		modalPopup.showPopup(msg);
	}

	/**
	 * Bloquea la pantalla y los eventos.
	 */
	public final void blockScreen() {
		modalPopup.showPopup();
	}

	/**
	 * Desbloquea la pantalla
	 */
	public final void unblockScreen() {
		modalPopup.close();
	}


	/**
	 * Programatic shows or hides the menuBar
	 */
	public void setMenuBarVisible(boolean visible) {
		if (visible){
			menu.show();
		}
		else{
			menu.hide();
		}
	}

	/**
	 * Makes an RPC call to retrieve the HTML documentation for the current
	 * useCase and display the GwtHelpPage registered in the GwtController.
	 */
	public void showCurrentCaseDocumentation() {
		if (lastGroupSelected != null) {
			String groupName = lastGroupSelected.getGroupName();
			String pageName = lastGroupSelected.getCurrentPage().getPageName();
			commonUseCaseServer.getCaseDocumentation(groupName, pageName, new GwtAsyncCallback(errorHandler) {
				public void onSuccess(Object result) {
					String groupName = lastGroupSelected.getGroupName();
					String pageName = lastGroupSelected.getCurrentPage().getPageName();
					helpPage.showCaseDocumentation(groupName, pageName, (String) result);
				}
			});
		}
	}
	
	/**
	 * @return obtiene el nombre de la pagina actualmente seleccionada (ultima)
	 */
	public String getCurrentPageName() {
		return lastGroupSelected != null && lastGroupSelected.getCurrentPage() != null ? lastGroupSelected.getCurrentPage().getPageName() : null;
	}

	public void addMenuBarListener(GwtMenuBarListener listener) {
		menu.addListener(listener);
	}

	public void unselectAllMenuItems() {
		menu.hideAll();
		menu.unselectAllMenuItems();
	}

	public void onResize(ResizeEvent event){
		componentsPanel.setHeight(Window.getClientHeight()-64 + "px");
	}
}
