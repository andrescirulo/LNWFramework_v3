package net.latin.server.utils.projectData;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que encapusla los datos de un proyecto
 * @author lpinkas
 *
 */
public class ProjectData {

	private List<UseCaseData> useCases = new ArrayList<UseCaseData>();
	private String name;
	private String absolutePath;
	private Nodo strutsConfig;
	private Nodo strutsConfigUseCases;
	private Nodo webXml;
	private Nodo generalConfig;
	private Nodo log4j;
	private Nodo springConfig;
	private Nodo buildProperties;
	private Nodo buildXml;
	private Nodo gwtModule;
	private Nodo gwtJsp;
	private Nodo gwtStyle;
	private Nodo projectStyle;
	private Nodo menuScript;
	private Nodo useCaseNames;
	private Nodo controller;

	public Nodo getMenuScript() {
		return menuScript;
	}

	public void setMenuScript(Nodo menuScript) {
		this.menuScript = menuScript;
	}

	public Nodo getGwtStyle() {
		return gwtStyle;
	}

	public void setGwtStyle(Nodo gwtStyle) {
		this.gwtStyle = gwtStyle;
	}

	public Nodo getProjectStyle() {
		return projectStyle;
	}

	public void setProjectStyle(Nodo projectStyle) {
		this.projectStyle = projectStyle;
	}

	public Nodo getGwtJsp() {
		return gwtJsp;
	}

	public void setGwtJsp(Nodo gwtJsp) {
		this.gwtJsp = gwtJsp;
	}

	public Nodo getGwtModule() {
		return gwtModule;
	}

	public void setGwtModule(Nodo moduloGwt) {
		this.gwtModule = moduloGwt;
	}

	public Nodo getBuildProperties() {
		return buildProperties;
	}

	public void setBuildProperties(Nodo buildProperties) {
		this.buildProperties = buildProperties;
	}

	public Nodo getBuildXml() {
		return buildXml;
	}

	public void setBuildXml(Nodo buildXml) {
		this.buildXml = buildXml;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<UseCaseData> getUseCases() {
		return useCases;
	}

	public void setUseCases(List<UseCaseData> useCases) {
		this.useCases = useCases;
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	public Nodo getStrutsConfig() {
		return strutsConfig;
	}
	public void setStrutsConfig(Nodo strutsConfig) {
		this.strutsConfig = strutsConfig;
	}

	public Nodo getStrutsConfigUseCases() {
		return strutsConfigUseCases;
	}

	public void setStrutsConfigUseCases(Nodo strutsConfigUseCases) {
		this.strutsConfigUseCases = strutsConfigUseCases;
	}

	public Nodo getWebXml() {
		return webXml;
	}

	public void setWebXml(Nodo webXml) {
		this.webXml = webXml;
	}

	public Nodo getGeneralConfig() {
		return generalConfig;
	}

	public void setGeneralConfig(Nodo generalConfig) {
		this.generalConfig = generalConfig;
	}

	public Nodo getLog4j() {
		return log4j;
	}

	public void setLog4j(Nodo log4j) {
		this.log4j = log4j;
	}

	public Nodo getSpringConfig() {
		return springConfig;
	}

	public void setSpringConfig(Nodo springConfig) {
		this.springConfig = springConfig;
	}

	public Nodo getUseCaseNames() {
		return useCaseNames;
	}

	public void setUseCaseNames(Nodo useCaseNames) {
		this.useCaseNames = useCaseNames;
	}

	public Nodo getController() {
		return controller;
	}

	public void setController(Nodo controller) {
		this.controller = controller;
	}

	/**
	 * Busca un caso de uso por el nombre del paquete.
	 * Ejemplo: abmMandamientos (en camel case con la primera en miniscula).
	 * Devuelve null si no lo encuentra
	 */
	public UseCaseData findUseCase(String useCaseName) {
		for (UseCaseData useCase : this.useCases) {
			if(useCase.getNombre().equals(useCaseName)) {
				return useCase;
			}
		}
		return null;
	}
}
