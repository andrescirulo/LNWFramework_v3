package net.latin.server.utils.projectData.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.latin.server.security.config.LnwGeneralConfig;
import net.latin.server.utils.projectData.Nodo;
import net.latin.server.utils.projectData.ProjectData;
import net.latin.server.utils.projectData.UseCaseData;
import net.latin.server.utils.resources.ResourceFinder;

/**
 * Lee la estructura del proyecto
 * @author Santiago Aimetta
 *
 */
public class ProjectStructureReader {
	private static final String CADENA_CORTE = "/src";
	private static final String CADENA_CORTE2 = "\\src";
	private static final String STRUTS_CONFIG = "struts-config.xml";
	private static final String STRUTS_CONFIG_USE_CASES = "struts-config-useCases.xml";
	private static final String WEB_XML = "web.xml";
	private static final String GENERAL_CONFIG = "GeneralConfig.xml";
	private static final String SPRING_CONFIG = "SpringConfig.xml";
	private static final String LOG4J = "log4j.properties";
	private static final String BUILD_PROPERTIES = "build.properties";
	private static final String BUILD_XML = "build.xml";
	private static final String GWT_JSP = "Gwt.jsp";
	private static final String GWT_STYLE = "gwtStyles.css";
	private static final String USE_CASE_NAMES = "UseCaseNames";


	private static ProjectStructureReader instance;
	private static List carpetasIgnoradas = new ArrayList();
	static{
		carpetasIgnoradas.add("CVS");
		carpetasIgnoradas.add(".svn");
	}

	private ProjectStructureReader() {
	}

	/**
	 * singleton
	 * @return
	 */
	public static synchronized ProjectStructureReader getInstance(){
		if(instance == null ){
			instance = new ProjectStructureReader();
		}
		return instance;
	}

	public ProjectData readProjectStructure( String pathGeneralConfig ){

 		String projectName = LnwGeneralConfig.getInstance().getApplicationName();
		//Creo el projectData
		ProjectData projectData = new ProjectData();
		projectData.setName(projectName);
		projectData.setAbsolutePath(ResourceFinder.getLocalProjectPath());


		//me creo una file para ir moviendome en los directorios
		File gralCfg = new File(pathGeneralConfig);
		if ( !gralCfg.exists() ){
			//si no se encuentra...
			throw new RuntimeException("El path "+pathGeneralConfig+" no se ha encontrado");
		}
		//me paro en la base del proyecto
		File project = gralCfg.getParentFile();
		project = project.getParentFile();


		//Armo los path de server y client y de los archivos de configuracion
		String clientUCPath = getClientPath(project);
		String serverUCPath =  getServerPath(project);
		String strutsConfigPath = getStrutsConfigPath(project);
		String configPath = getConfigPath(project);
		String antPath = getAntPath(project);
		String modulePath = getModulePath(project);
		String gwtJspPath = getGwtJspPath(project);
		String stylesPath = getStylesPath(project);
		String scriptsPath = getScriptsPath(project);
		String clientPackagePath = getClientPackagePath(project);

		//Entro al client
		File[] clientFiles = new File(clientUCPath).listFiles();
		//lista de casos de uso
		for (int i = 0; i < clientFiles.length; i++) {

			String caseName = clientFiles[i].getName();
			System.out.println(i+": "+caseName);
			//si no esta ignorada
			if( !carpetasIgnoradas.contains(caseName) ){
				//verifico q exista el case en el server
				//si existe lo manejo si no lo dejo pasar
				System.out.println(i+": "+serverUCPath+"/"+caseName);
				if(   new File(serverUCPath+"/"+caseName).exists()){
					//creo un caso de uso
					UseCaseData casoDeUso = new UseCaseData();
					projectData.getUseCases().add(casoDeUso);
					casoDeUso.setNombre(caseName);
					casoDeUso.setAbsoluteClientPath(clientUCPath+"/"+caseName);
					casoDeUso.setAbsoluteServerPath(serverUCPath+"/"+caseName);

					//me traigo el case del server
					Nodo casoServer = new Nodo();
					String name = capitalize(caseName)+"Case";
					casoServer.setName(name);
					casoServer.setClassName(getPackage(serverUCPath+"/"+caseName+"/"+name));
					casoServer.setAbsolutePath(serverUCPath+"/"+caseName+"/"+name+".java"); //ACA TAMBIEN SETEA EL RELATIVE PATH
					casoDeUso.setServerUseCase(casoServer);

					//archivo de especificacion
					Nodo specification = new Nodo();
					String specificationName = capitalize(caseName)+"Specification";
					specification.setName(specificationName);
					specification.setClassName(null);
					specification.setAbsolutePath(serverUCPath+"/"+caseName+"/docs/"+specificationName+".mht");
					casoDeUso.setSpecification(specification);

					//me traigo el group
					name = capitalize(caseName)+"Group";
					Nodo group = new Nodo();
					group.setName(name);
					group.setClassName(getPackage(clientUCPath+"/"+caseName+"/"+name));
					group.setAbsolutePath(clientUCPath+"/"+caseName+"/"+name+".java");
					casoDeUso.setGroup(group);

					//armo rpc comun
					name = capitalize(caseName)+"Client";
					Nodo clientRpc = new Nodo();
					clientRpc.setName(name);
					clientRpc.setClassName(getPackage(clientUCPath+"/"+caseName+"/rpc/"+name));
					clientRpc.setAbsolutePath(clientUCPath+"/"+caseName+"/rpc/"+name+".java");
					casoDeUso.setRemoteProcesureCall(clientRpc);

					//armo rpc async
					name = capitalize(caseName)+"ClientAsync";
					Nodo clientRpcAsync = new Nodo();
					clientRpcAsync.setName(name);
					clientRpcAsync.setClassName(getPackage(clientUCPath+"/"+caseName+"/rpc/"+name));
					clientRpcAsync.setAbsolutePath(clientUCPath+"/"+caseName+"/rpc/"+name+".java");
					casoDeUso.setRemoteProcedureCallAsynch(clientRpcAsync);

					//armo constants
					name = capitalize(caseName)+"Constants";
					Nodo constants = new Nodo();
					constants.setName(name);
					constants.setClassName(getPackage(clientUCPath+"/"+caseName+"/constants/"+name));
					constants.setAbsolutePath(clientUCPath+"/"+caseName+"/constants/"+name+".java");
					casoDeUso.setConstants(constants);


					//armo las pages
					File pagesFile = new File(clientUCPath+"/"+caseName+"/pages");
					File[] pages = pagesFile.listFiles();
					for (int j = 0; j < pages.length; j++) {
						//creo el nodo page y lo agrego
						// no es de las ignoradas
						Nodo page = new Nodo();
						name = pages[j].getName();
						if(!carpetasIgnoradas.contains(name) && name.endsWith(".java") /*!name.contains(".java.bak") && !name.contains(".Designer.xml")*/){
							String pageName = name.substring(0, name.indexOf(".java"));
							pageName = capitalize(pageName);
							page.setName(pageName);
							page.setClassName(getPackage(clientUCPath+"/"+caseName+"/pages/"+pageName));
							page.setAbsolutePath(clientUCPath+"/"+caseName+"/pages/"+pageName+".java");
							casoDeUso.getPages().add(page);
						}


					}
				}
			}
		}
		///////////////////////////////ARCHIVOS DE CONFIGURACION //////////////////////////////

		//armo el GeneralConfig.xml
		Nodo generalConfig = new Nodo();
		generalConfig.setName(GENERAL_CONFIG);
		generalConfig.setAbsolutePath(configPath + "/" + GENERAL_CONFIG);
		projectData.setGeneralConfig(generalConfig);

		//armo el SpringConfig.xml
		Nodo springConfig = new Nodo();
		springConfig.setName(SPRING_CONFIG);
		springConfig.setAbsolutePath(configPath + "/" + SPRING_CONFIG);
		projectData.setSpringConfig(springConfig);

		//armo el log4j.properties
		Nodo log4j = new Nodo();
		log4j.setName(LOG4J);
		log4j.setAbsolutePath(configPath + "/" + LOG4J);
		projectData.setLog4j(log4j);


		///////////////////////////////ARCHIVOS DE CONFIGURACION DE STRUTS//////////////////////////////

		//armo el struts-config.xml
		Nodo strutsConfig = new Nodo();
		strutsConfig.setName(STRUTS_CONFIG);
		strutsConfig.setAbsolutePath(strutsConfigPath + "/" + STRUTS_CONFIG);
		projectData.setStrutsConfig(strutsConfig);

		//armo el struts-config-useCases.xml
		Nodo strutsConfigUseCases = new Nodo();
		strutsConfigUseCases.setName(STRUTS_CONFIG_USE_CASES);
		strutsConfigUseCases.setAbsolutePath(strutsConfigPath + "/" + STRUTS_CONFIG_USE_CASES);
		projectData.setStrutsConfigUseCases(strutsConfigUseCases);

		//armo el web.xml
		Nodo webXml = new Nodo();
		webXml.setName(WEB_XML);
		webXml.setAbsolutePath(strutsConfigPath + "/" + WEB_XML);
		projectData.setWebXml(webXml);

		///////////////////////////////ARCHIVOS DE ANT//////////////////////////////

		//armo el build.xml
		Nodo buildXml = new Nodo();
		buildXml.setName(BUILD_XML);
		buildXml.setAbsolutePath(antPath + "/" + BUILD_XML);
		projectData.setBuildXml(buildXml);

		//armo el build.properties
		Nodo buildProperties = new Nodo();
		buildProperties.setName(BUILD_PROPERTIES);
		buildProperties.setAbsolutePath(antPath + "/" + BUILD_PROPERTIES);
		projectData.setBuildProperties(buildProperties);

		///////////////////////////////MODULO GWT//////////////////////////////

		//armo el modulo gwt
		Nodo moduloGwt = new Nodo();
		String moduleName = nombreModuloGwt(project);
		moduloGwt.setName(moduleName);
		moduloGwt.setClassName(getPackage(modulePath + "/" + moduleName.substring(0, moduleName.indexOf("."))));
		moduloGwt.setAbsolutePath(modulePath + "/" + moduleName);
		projectData.setGwtModule(moduloGwt);

		//////////////////////////// Gwt.jsp ////////////////////////////////////
		Nodo gwtJsp = new Nodo();
		gwtJsp.setName(GWT_JSP);
		gwtJsp.setAbsolutePath(gwtJspPath + "/" + GWT_JSP);
		projectData.setGwtJsp(gwtJsp);

		/////////////////////////// ESTILOS ////////////////////////////////////
		Nodo gwtStyle = new Nodo();
		gwtStyle.setName(GWT_STYLE);
		gwtStyle.setAbsolutePath(stylesPath + "/" + GWT_STYLE);
		projectData.setGwtStyle(gwtStyle);

		Nodo projectStyle = new Nodo();
		String projectStyleName = nombreEstiloProyecto(project);
		projectStyle.setName(projectStyleName);
		projectStyle.setAbsolutePath(stylesPath + "/" + projectStyleName);
		projectData.setProjectStyle(projectStyle);

		////////////////////////// MENU SCRIPT ///////////////////////////////
		Nodo menuScript = new Nodo();
		String menuScriptName = nombreMenuScriptProyecto(project);
		menuScript.setName(menuScriptName);
		menuScript.setAbsolutePath(scriptsPath + "/" + menuScriptName);
		projectData.setMenuScript(menuScript);

		/////////////////////////USECASENAMES Y CONTROLLER //////////////////
		Nodo useCaseNames = new Nodo();
		useCaseNames.setName(USE_CASE_NAMES);
		useCaseNames.setClassName(getPackage(clientPackagePath + "/" + USE_CASE_NAMES));
		useCaseNames.setAbsolutePath(clientPackagePath + "/" + USE_CASE_NAMES + ".java");
		projectData.setUseCaseNames(useCaseNames);

		Nodo controller = new Nodo();
		String controllerName = nombreController(project);
		controller.setName(controllerName);
		controller.setClassName(getPackage(clientPackagePath + "/" + controllerName));
		controller.setAbsolutePath(clientPackagePath + "/" + controllerName + ".java");
		projectData.setController(controller);



		return projectData;
	}


	private String nombreController(File project) {
		return project.getName().toUpperCase() + "Controller";
	}

	private String nombreMenuScriptProyecto(File project) {
		return "MENU " + project.getName().toUpperCase() + ".launch";
	}

	private String nombreEstiloProyecto(File project) {
		return project.getName().toLowerCase() + "Styles.css";
	}

	private String nombreModuloGwt(File project) {
		return project.getName().substring(0, 1).toUpperCase() + project.getName().substring(1) + ".gwt.xml";
	}

	private String getServerPath(File project) {
		String serverPack =LnwGeneralConfig.getInstance().getServerPackPath();
		String serverUCPath = project+"\\"+serverPack+"\\useCases";
		return serverUCPath;
	}

	private String getClientPath(File project) {
		String clientPack =LnwGeneralConfig.getInstance().getClientPackagePath();
		String clientUCPath = project+"\\"+clientPack+"\\useCases";
		return clientUCPath;
	}

	private String getClientPackagePath(File project){
		String clientPack =LnwGeneralConfig.getInstance().getClientPackagePath();
		String clientUCPath = project+"\\"+clientPack;
		return clientUCPath;
	}

	private String getStrutsConfigPath(File project) {
		return project + "\\webapp\\WEB-INF";

	}

	private String getConfigPath(File project) {
		return project + "\\config";

	}

	private String getAntPath(File project) {
		return project.toString();

	}

	private String getModulePath(File project){
		return project + "\\src\\" + project.getName().toLowerCase();
	}

	private String getGwtJspPath(File project){
		return project + "\\webapp\\pages";
	}

	private String getStylesPath(File project){
		return project + "\\webapp\\script\\utils\\gwt";
	}

	private String getScriptsPath(File project){
		return project + "\\scripts\\scripts";
	}

	private String getPackage(String path){


		//Esto es muy sucio pero es para evitar que se rompa cuando cambia la forma
		//del path.
		String packAux ="";
		try{
			packAux = path.substring((path.indexOf(CADENA_CORTE)));
		}
		catch ( StringIndexOutOfBoundsException e){
			packAux = path.substring((path.indexOf(CADENA_CORTE2)));
		}

		packAux = packAux.replace("/", ".");
		packAux = packAux.replace("\\", ".");
		if(packAux.endsWith(".")){
			packAux = (String) packAux.subSequence(0, packAux.lastIndexOf("."));

		}
		//Corto la parte de src
//		int indice = packAux.substring(path.lastIndexOf(CADENA_CORTE));
		packAux = packAux.substring(CADENA_CORTE.length());
		//le quito el 1er .
		packAux = packAux.substring(1);
		return packAux;
	}

	 private  String capitalize(String str) {
	        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
	    }

	public static void main(String[] args) {
		ProjectStructureReader.getInstance().readProjectStructure(ResourceFinder.getLocalProjectPath()+"/config/GeneralConfig.xml");

	}


}
