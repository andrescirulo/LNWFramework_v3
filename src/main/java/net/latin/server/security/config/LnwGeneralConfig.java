package net.latin.server.security.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;

import net.latin.server.commonUseCase.CommonUseCaseImpl;
import net.latin.server.commonUseCase.DefaultCommonUseCaseClientImpl;
import net.latin.server.persistence.user.LnwUser;
import net.latin.server.security.filter.LnwFilterRule;
import net.latin.server.utils.exceptions.LnwException;
import net.latin.server.utils.xml.XmlUtils;

/**
 * Configuracion general de toda la aplicacion.
 * Levanta un XML ubicado en el Classpath en GeneralConfig.xml
 *
 * @author Matias Leone
 */
public class LnwGeneralConfig {

	private static final String TEMP_DIR_UPLOAD_SERVLET = "tempDirUploadServlet";
	private static final String FILE_SIZE_IN_MEMORY_MAX_UPLOAD_SERVLET = "fileSizeInMemoryMaxUploadServlet";
	private static final String FILE_SIZE_MAX_UPLOAD_SERVLET = "fileSizeMaxUploadServlet";
	private static final String XML_PATH = "GeneralConfig.xml";
	public final static String GROUP_TAG = "Group";
	public final static String APPLICATION_USER_TAG = "ApplicationUser";
	public final static String APPLICATION_TAG = "Application";
	public final static String GROUPS_TAG = "Groups";
	public final static String PUBLIC_GROUPS_TAG = "PublicGroups";
	public final static String USE_CASE_TAG = "UseCase";
	public final static String EXTERNAL_TAG = "External";
	public final static String NAME_ATTR = "name";
	public final static String DESCRIPTION_ATTR = "description";
	public final static String ID_ATTR = "id";
	public final static String TITLE_ATTR = "title";
	public final static String URL_ATTR = " url";
	public final static String COMMONS_TAG = "CommonAccess";
	private static final String ACCESS_TAG = "Access";
	private static final String ACCESS_ID = "id";
	private static final String EURLS_TAG = "FilterExcludedUrls";
	private static final String STRUTS_CONFIG_USE_CASES_PATH_TAG = "StrutsConfigUseCasesPath";
	private static final String CLIENT_PACKAGE_PATH_TAG = "ClientPackagePath";
	private static final String SERVER_PACKAGE_PATH_TAG = "ServerPackagePath";
	private static final String REPORTS_CLASSPATH_TAG = "ReportsClasspath";
	private static final String REPORTS_TESTING_OUTPUT_FOLDER_TAG = "ReportsTestingOutputFolder";
	private static final String REPORTS_PASSWORD_TAG = "ReportsPassword";
	private static final String COMMON_USE_CASE_IMPL_TAG = "CommonUseCaseImpl";
	private static final String FILTER_RULES_TAG = "FilterRules";
	private static final String FILTER_RULE_TAG = "FilterRule";
	private static final String PARAM_TAG = "param";
	private static final String CLASS = "class";


	private String applicationName;
	private List<LnwMenuGroup> groups = new ArrayList<LnwMenuGroup>();
	private List<LnwMenuGroup> publicGroups = new ArrayList<LnwMenuGroup>();
	private String applicationUser;
	private String applicationDescription;
	private static LnwGeneralConfig instance = null;
	private List<CommonAccess> commons = new ArrayList<CommonAccess>();
	private List<String> excludedUrls = new ArrayList<String>();
	private String strutsConfigUseCasesPath;
	private String clientPackagePath;
	private String serverPackPath;
	private String reportsClasspath;
	private String reportsTestingOutputFolder;
	private String reportsPassword;
	private int fileSizeMaxUploadServlet;
	private int fileSizeInMemoryMaxUploadServlet;
	private String tempDirUploadServlet;
	private CommonUseCaseImpl commonUseCaseImpl;
	private List<LnwFilterRuleData> filterRules = new ArrayList<LnwFilterRuleData>();
	private List<LnwFilterRule> filtros = new ArrayList<LnwFilterRule>();
	
	/**
	 * Singleton
	 * @return instancia
	 */
	public static final synchronized LnwGeneralConfig getInstance(){
		if( instance == null ){
			instance = new LnwGeneralConfig();
		}
		return instance;
	}


	private LnwGeneralConfig() {
		loadXml();
	}

	/**
	 * Carga la configuracion inicial a partir del generalConfig.xml
	 * @param xmlPath
	 */
	public void loadXml() {
		try {
			//get xml
			File file = new File( LnwGeneralConfig.class.getClassLoader().getResource(XML_PATH).toURI());
//			File file = new File("C:\\development\\eclipse\\workspace\\ssm\\config\\GeneralConfig.xml");
			Document document = XmlUtils.loadDocument( file.getAbsolutePath(), false );

			Element root = document.getRootElement();

			//application name and description
			Element appEl = root.getChild( APPLICATION_TAG );
			applicationName = appEl.getAttributeValue( NAME_ATTR );
			applicationDescription = appEl.getAttributeValue( DESCRIPTION_ATTR );

			//application user
			Element appUserEl = root.getChild( APPLICATION_USER_TAG );
			applicationUser = appUserEl.getText();

			//read StrutsConfigUseCasesPath
			Element struConPath = root.getChild( STRUTS_CONFIG_USE_CASES_PATH_TAG );
			this.strutsConfigUseCasesPath = struConPath.getText();

			//read ClientPackagePath
			Element clientPackPath = root.getChild( CLIENT_PACKAGE_PATH_TAG );
			this.clientPackagePath = clientPackPath.getText();

			//read ServerPackagePath
			Element serverPackPathEl = root.getChild( SERVER_PACKAGE_PATH_TAG );
			this.serverPackPath = serverPackPathEl.getText();

			//read HbmFolder
			Element reportsClasspathEl = root.getChild(REPORTS_CLASSPATH_TAG);
			this.reportsClasspath = reportsClasspathEl.getText();

			//read resports testing output folder
			Element reportsTestingOutForEl = root.getChild(REPORTS_TESTING_OUTPUT_FOLDER_TAG);
			this.reportsTestingOutputFolder = reportsTestingOutForEl.getText();

			//read resports password
			Element reportsPaswordEl = root.getChild(REPORTS_PASSWORD_TAG);
			this.reportsPassword = reportsPaswordEl.getText();

			//read fileSizeMax
			Element fileSizeMaxUploadServletEl = root.getChild(FILE_SIZE_MAX_UPLOAD_SERVLET);
			this.fileSizeMaxUploadServlet = Integer.parseInt(fileSizeMaxUploadServletEl.getText());

			//read sizeThresHold
			Element fileSizeInMemoryMaxUploadServletEl = root.getChild(FILE_SIZE_IN_MEMORY_MAX_UPLOAD_SERVLET);
			this.fileSizeInMemoryMaxUploadServlet = Integer.parseInt(fileSizeInMemoryMaxUploadServletEl.getText());

			//read sizeThresHold
			Element tempDirUploadServletEl = root.getChild(TEMP_DIR_UPLOAD_SERVLET);
			if(tempDirUploadServletEl != null){
				this.tempDirUploadServlet = tempDirUploadServletEl.getText();
			}

			//read resports password
			Element commonUseCaseImplEl = root.getChild(COMMON_USE_CASE_IMPL_TAG);
			crearCommonUseCaseImpl( commonUseCaseImplEl );

			//read groups
			Element groupsMain = root.getChild( GROUPS_TAG );
			List<Element> groupsEl = groupsMain.getChildren( GROUP_TAG );
			for (Element element : groupsEl) {
				groups.add( new LnwMenuGroup( element ) );
			}
			
			//read public groups
			Element publicGroupsMain = root.getChild( PUBLIC_GROUPS_TAG );
			if (publicGroupsMain!=null){
				List<Element> publicGroupsEl = publicGroupsMain.getChildren( GROUP_TAG );
				for (Element element : publicGroupsEl) {
					publicGroups.add( new LnwMenuGroup( element ) );
				}
			}

			//read commons
			Element commonsMain = root.getChild(COMMONS_TAG);
			List<Element> access = commonsMain.getChildren(ACCESS_TAG);
			for (Element element : access) {
				commons.add(new CommonAccess( element.getAttributeValue(ACCESS_ID) ));
			}

			//read excludedUrls
			Element eUrls = root.getChild(EURLS_TAG);
			List<Element> urls = eUrls.getChildren();
			for (Element element : urls) {
				excludedUrls.add(element.getText());
			}
			
			//read filterRules
			Element fRules = root.getChild(FILTER_RULES_TAG);
			if (fRules != null){
				List<Element> filterRulesList = fRules.getChildren(FILTER_RULE_TAG);
				for (Element element : filterRulesList){
					filterRules.add(new LnwFilterRuleData(element.getAttributeValue(CLASS), element.getChildren(PARAM_TAG)));
				}
			}
//		} catch (URISyntaxException e) {
//			throw new LnwException( "Error al levantar el xml: " + XML_PATH );
		} catch (Exception e) {
			throw new LnwException( "Error al levantar el xml: " + XML_PATH );
		}

	}


	public List<LnwMenuGroup> getGroups() {
		return groups;
	}
	public List<LnwMenuGroup> getPublicGroups() {
		return publicGroups;
	}

	/**
	 * Retorna un item con ese id y title
	 * @param groupName
	 * @param id
	 * @return
	 */
	public LnwMenuItem getLnwMenuItems( String groupName, String id){
		for (LnwMenuGroup grupo : this.groups) {
			if( grupo.getId().matches(id) && grupo.getTitle().matches(groupName)){
				List<LnwMenuItem> items = grupo.getMenuItems();
				for (LnwMenuItem lnwMenuItem : items) {
					if( lnwMenuItem.getId().matches(id) && lnwMenuItem.getTitle().matches(groupName)){
						return lnwMenuItem;
					}
				}
			}
		}
		throw new RuntimeException("No se encontro el item con id: "+id+"y title: "+groupName );
	}


	/**
	 * @return nombre de la aplicacion
	 */
	public String getApplicationName() {
		return applicationName;
	}


	/**
	 * @return nombre de la clase que se utiliza como usuario de la aplicacion
	 */
	public String getApplicationUser() {
		return applicationUser;
	}


	public LnwMenuGroup getGroup(String grupo) {
		for (LnwMenuGroup menuGrupo : groups) {
			if (menuGrupo.getId().equals(grupo)){
				return menuGrupo;
			}
		}
		throw new LnwException("No se encontro el grupo " + grupo);
	}


	public String getApplicationDescription() {
		return applicationDescription;
	}


	public List<CommonAccess> getCommonAccess() {
		return commons;
	}

	public final List<String> getFilterExcludedUrls(){
		return this.excludedUrls;
	}


	public String getStrutsConfigUseCasesPath() {
		return strutsConfigUseCasesPath;
	}


	public String getClientPackagePath() {
		return clientPackagePath;
	}


	public String getServerPackPath() {
		return serverPackPath;
	}


	public String getReportsClasspath() {
		return reportsClasspath;
	}


	public String getReportsTestingOutputFolder() {
		return reportsTestingOutputFolder;
	}


	public String getReportsPassword() {
		return reportsPassword;
	}

	/**
	 * Crea una instancia del usuario de la aplicacion
	 * basandose en la implementacion concreta especificada en el GeneralConfig.xml
	 * @return
	 */
	public LnwUser createApplicationUser() {
		String className = getApplicationUser();
		try {
			return (LnwUser) Class.forName(className).newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Error al crear el application user con la clase " + className);
		}
	}


	public int getFileSizeMaxUploadServlet() {
		return fileSizeMaxUploadServlet;
	}


	public int getFileSizeInMemoryMaxUploadServlet() {
		return fileSizeInMemoryMaxUploadServlet;
	}


	public String getTempDirUploadServlet() {
		return tempDirUploadServlet;
	}

	/**
	 * Crea la implementacion actual de CommounUseCaseImpl
	 */
	private void crearCommonUseCaseImpl(Element element) {
		//si no hay configuracion, crear default
		if(element == null || element.getText() == null || element.getText().isEmpty() ) {
			commonUseCaseImpl = new DefaultCommonUseCaseClientImpl();

		//sino, instanciar por reflection
		} else {
			try {
				commonUseCaseImpl = (CommonUseCaseImpl) Class.forName(element.getText()).newInstance();
			} catch (Throwable e) {
				throw new RuntimeException( "GeneralConfig: no se pudo configurar la CommonUseCaseImpl" );
			}
		}
	}

	/**
	 * Retorna el fully-qualified-name de commonUseCaseImpl
	 */
	public CommonUseCaseImpl getCommonUseCaseImpl() {
		return commonUseCaseImpl;
	}


	public List<LnwFilterRuleData> getFilterRules() {
		return filterRules;
	}


	public void setFilterRules(List<LnwFilterRuleData> filterRules) {
		this.filterRules = filterRules;
	}

	public void addNewFilter(LnwFilterRule filter){
		this.filtros.add(filter);
	}


	public List<LnwFilterRule> getFiltros() {
		return filtros;
	}


	public void setFiltros(List<LnwFilterRule> filtros) {
		this.filtros = filtros;
	}


	public boolean hasPublicGroups() {
		return publicGroups!=null && publicGroups.size()>0;
	}
	


}








