package net.latin.server.utils.caseMaker.ui;

import java.io.File;

import javax.swing.filechooser.FileFilter;
/***
 * Filtro customizado,se setea con la extension q debe ser
 *  ".extension" y la descripcion
 *
 * @author Santiago Aimetta
 */
public class CustomFilter extends FileFilter{
	private String extension = "";
	private String descripcion = "";




	public CustomFilter(String extension, String descripcion) {
		super();
		this.extension = extension;
		this.descripcion = descripcion;
	}

	@Override
	public boolean accept(File f) {
		return f.isDirectory() || f.getName().toLowerCase().endsWith(extension);
	}

	@Override
	public String getDescription() {
		return descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}



}
