package net.latin.server.utils.projectData;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que encapsula los datos de un caso de uso
 * @author ftemoli
 *
 */
public class UseCaseData {

	private String nombre;
	private Nodo group;
	private Nodo remoteProcesureCall;
	private Nodo remoteProcedureCallAsynch;
	private Nodo serverUseCase;
	private Nodo constants;
	private Nodo specification;
	private List<Nodo> pages = new ArrayList<Nodo>();
	private String absoluteClientPath;
	private String absoluteServerPath;



	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public List<Nodo> getPages() {
		return pages;
	}
	public void setPages(List<Nodo> pages) {
		this.pages = pages;
	}
	public Nodo getGroup() {
		return group;
	}
	public void setGroup(Nodo group) {
		this.group = group;
	}
	public Nodo getRemoteProcesureCall() {
		return remoteProcesureCall;
	}
	public void setRemoteProcesureCall(Nodo remoteProcesureCall) {
		this.remoteProcesureCall = remoteProcesureCall;
	}
	public Nodo getRemoteProcedureCallAsynch() {
		return remoteProcedureCallAsynch;
	}
	public void setRemoteProcedureCallAsynch(Nodo remoteProcedureCallAsynch) {
		this.remoteProcedureCallAsynch = remoteProcedureCallAsynch;
	}
	public Nodo getServerUseCase() {
		return serverUseCase;
	}
	public void setServerUseCase(Nodo serverUseCase) {
		this.serverUseCase = serverUseCase;
	}
	public Nodo getConstants() {
		return constants;
	}
	public void setConstants(Nodo constants) {
		this.constants = constants;
	}
	public String getAbsoluteClientPath() {
		return absoluteClientPath;
	}
	public void setAbsoluteClientPath(String absoluteClientPath) {
		this.absoluteClientPath = absoluteClientPath;
	}
	public String getAbsoluteServerPath() {
		return absoluteServerPath;
	}
	public void setAbsoluteServerPath(String absoluteServerPath) {
		this.absoluteServerPath = absoluteServerPath;
	}
	/**
	 * @return the specification
	 */
	public Nodo getSpecification() {
		return specification;
	}
	/**
	 * @param specification the specification to set
	 */
	public void setSpecification(Nodo specification) {
		this.specification = specification;
	}

}
