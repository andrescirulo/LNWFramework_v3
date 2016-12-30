package net.latin.server.utils.reports.constructor;

import java.io.File;

import net.sf.jasperreports.engine.JRExporterParameter;

// Referenced classes of package ar.com.latinNet.desarrollo.impresion.constructor:
//			ReportDirector

/**
 * Clase directora para la construcción con formato de salida a archivo.
 * @author ccancinos
 */
public abstract class FileReportDirector extends ReportDirector {

	public FileReportDirector() {
		//Usado para poner el nombre de archivo de salida con un setter.
	}

	/**
	 * Usar preferentemente FileReportDirector(String path, String fileName) en
	 * vez de este constructor, dado que este no crea los directorios, por lo
	 * tanto si no existe se genera una exception.
	 *
	 * @param fileName Ruta y nombre del archivo en donde se guardara el reporte.
	 */
	public FileReportDirector(String fileName) {
		this.setOutputFileName(fileName);
	}

	/**
	 * Constructor que verifica que exista el directorio que contiene el
	 * parametro path (si no existe crear todos los directorios necesarios).
	 *
	 * @param path Ruta del directorio en donde se guardara el reporte.
	 * @param fileName Nombre del archivo en donde se guardara el reporte.
	 */
	public FileReportDirector(String path, String fileName) {
		File file = new File(path);

		if(!file.exists()) {
			file.mkdirs();
		}

		this.setOutputFileName(path + "/" + fileName);
	}

    /**
     * Setea el nombre del archivo destino de la exportación sin incluir la extension del archivo
     * @param fileName El nombre de archivo sin la extensión.
     */
	public FileReportDirector setOutputFileName(String fileName) {
		this.getParameters().put(JRExporterParameter.OUTPUT_FILE_NAME, fileName + "." + this.getFileExtension());
		return this;
	}

    /**
     * Las subclases podrán establecer la extension del archivo en el que se exportará el reporte.
     */
	protected abstract String getFileExtension();
}
