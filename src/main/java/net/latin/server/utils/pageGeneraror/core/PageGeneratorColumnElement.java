package net.latin.server.utils.pageGeneraror.core;

import java.util.List;

import org.jdom2.Element;

/**
 * Interfaz básica para generar una columna para la GwtTable
 *
 * @author Santiago Aimetta
 */
public interface PageGeneratorColumnElement {

	/**
	 * Retorna el nombre de la variable q referencia
	 * a la tabla
	 */
	public String getVariableName();

	/**
	 * Retorna el codigo que se pondrá en el constructor
	 * de la tabla que sera generada
	 */
	public String getCode();

	/**
	 * <p>
	 * Retorna el codigo de las variables globales y las constantes que
	 * se deben declarar en la pagina, arriba del constructor.
	 *
	 * <br>Ejemplo: <code>private Tipo variable</code>
	 *
	 * <br>No debe tener Enter.
	 * <br>No debe tener punto y coma.
	 *
	 */
	public String getGlobalCode();

	/**
	 * Retorna el codigo de los metodos del widget,
	 * sean tanto de las interfaces que implementa
	 * como los que se generan comunmente con su uso.
	 */
	public String getMethodsCode();

	/**
	 * Retorna los imports necesarios para la columna
	 */
	public List<String> getImports();

//	/**
//	 * Panel de edicion de la columna
//	 */
//	public JPanel getPropertyPanel(PageMakerEditDialog editDialog);

	/**
	 * Indica si la columna se tiene que usar en la generación de reportes
	 */
	public boolean getReportExport();

	/**
	 * Persiste la informacion del generador necesaria para reconstruir
	 * su interfaz gráfica en otra oportunidad
	 * @return nodo de JDOM
	 */
	public Element saveToXml();

	/**
	 * Carga la interfaz grafica del generador en funcion de informacion
	 * persistida en otra ocasion
	 * @param element nodo de JDOM
	 */
	public void loadFromXml(Element element);

	/**
	 * Hace que se cargue la interfaz gráfica correspondiente al generator.
	 * Se llama siempre después de haber hecho un loadFromXml()
	 */
	public void setProperties();
}
