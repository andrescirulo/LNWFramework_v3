package net.latin.server.utils.projectData;

import net.latin.server.utils.resources.ResourceFinder;

/**
 * Clase que representa un nodo de un proyecto, son los que componen un caso de uso.
 * @author lpinkas
 *
 */
public class Nodo {
	
	private String name;
	private String fullClassName;
	private String relativePath;
	private String absolutePath;


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClassName() {
		return fullClassName;
	}
	public void setClassName(String className) {
		this.fullClassName = className;
	}
	public String getRelativePath() {
		return relativePath;
	}
	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}
	public String getAbsolutePath() {
		return absolutePath;
	}
	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
		
		setRelativePath(absolutePath.substring( ResourceFinder.getLocalProjectPath().length() ));
	}
	
	
}
