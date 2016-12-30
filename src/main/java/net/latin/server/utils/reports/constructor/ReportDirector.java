package net.latin.server.utils.reports.constructor;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRAbstractExporter;

/**
 * Clase abstracta de la cual extienden los directores de formato de salida.
 * @author ccancinos
 * @author bfalese
 */
public abstract class ReportDirector {

	private Map paramMap;

	protected ReportDirector() {
		this.paramMap = new HashMap();
	}

    /**
     * Template method para la inicialización de la exportación
     */
	public final JRAbstractExporter prepareToExport() {
		JRAbstractExporter jrabstractexporter = this.getExporter();
		this.setSecurityParameters();
		jrabstractexporter.setParameters(this.getParameters());
		this.initExporter(jrabstractexporter);
		return jrabstractexporter;
	}

    /**
     * Determina el exporter uitlizado
     */
	public abstract JRAbstractExporter getExporter();

    /**
     * Permite definir la seguridad del reporte.
     * Por defecto no realiza ninguna accion.
     */
	protected void setSecurityParameters() {
	};

    /**
     * Realiza inicalizaciones propias del tipo de exportación que realiza el director.
     */
	protected void initExporter(JRAbstractExporter jrabstractexporter) {
		//Por default no hace nada
	}

	protected Map getParameters() {
		return paramMap;
	}

	protected void setParameters(Map map) {
		this.paramMap = map;
	}
}
