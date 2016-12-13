package net.latin.server.utils.caseMaker.domain.fileMaker;
/**
 * Contiene strings con los datos de la
 * ubicación física de los archivos
 * a generar
 * esta cableado
 *
 * @author Santiago Aimetta
 */
public class FolderData {
	private String rpcFolder;
	private String constantFolder;
	private String pagesFolder;
	private String serverFolder;
	private String groupFolder;

	private String rpcPackage;
	private String constantsPackage;
	private String pagesPackage;
	private String serverPackage;
	private String clientPackage;

	public String getGroupFolder() {
		return groupFolder;
	}

	public void setGroupFolder(String groupFolder) {
		this.groupFolder = groupFolder;
	}

	public String getRpcFolder() {
		return rpcFolder;
	}

	public void setRpcFolder(String rpcFolder) {
		this.rpcFolder = rpcFolder;
	}

	public String getConstantFolder() {
		return constantFolder;
	}

	public void setConstantFolder(String constantFolder) {
		this.constantFolder = constantFolder;
	}

	public String getPagesFolder() {
		return pagesFolder;
	}

	public void setPagesFolder(String pagesFolder) {
		this.pagesFolder = pagesFolder;
	}

	public String getServerFolder() {
		return serverFolder;
	}

	public void setServerFolder(String serverFolder) {
		this.serverFolder = serverFolder;
	}

	public String getRpcSSPackage() {
		return rpcPackage;
	}

	public void setRpcPackage(String prcPackage) {
		this.rpcPackage = prcPackage;
	}

	public String getConstantsPackage() {
		return constantsPackage;
	}

	public void setConstantsPackage(String constantsPackage) {
		this.constantsPackage = constantsPackage;
	}

	public String getPagesPackage() {
		return pagesPackage;
	}

	public void setPagesPackage(String pagesPackage) {
		this.pagesPackage = pagesPackage;
	}

	public String getServerPackage() {
		return serverPackage;
	}

	public void setServerPackage(String serverPackage) {
		this.serverPackage = serverPackage;
	}

	public String getClientPackage() {
		return clientPackage;
	}

	public void setClientPackage(String clientPackage) {
		this.clientPackage = clientPackage;
	}





}
