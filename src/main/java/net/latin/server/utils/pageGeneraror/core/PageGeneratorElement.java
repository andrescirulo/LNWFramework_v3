package net.latin.server.utils.pageGeneraror.core;

import java.util.List;

import javax.swing.ImageIcon;

import org.jdom2.Element;

/**
 * Interfaz principal, implementada por cada
 * widget que utilizara pageGenerator.
 * Los metodos deben ir devolviendo el código que se
 * debe agregar en una GwtPage.
 * Respetar las indicaciones de formateo para que la
 * pagina sea generada "linda".
 *
 *
 * @author Santiago Aimetta
 *
 */
public interface PageGeneratorElement {

	/**
	 * Retorna el codigo que se pondra en el constructor
	 * de la pagina que sera generada.
	 * <p>
	 * Ejemplo:<code>
	 * <br>button1 = new Button("click");
	 * <br>button1.setWidth("100px");
	 * </code><p>
	 *
	 * <p>
	 * Dejar un Enter al principio.
	 * El codigo debe tener doble TAB.
	 * Dejar un Enter al final de cada linea.
	 * Agregar punto y coma a cada linea.
	 *
	 *
	 */
	public String getConstructorCode();


	/**
	 * <p>
	 * Retorna una lista con todas las interfaces que
	 * implementa el widget.
	 * Simplemente poner el nombre, sin comas ni nada.
	 * El PageGenerator se encarga de evitar repetidos.
	 */
	public List<String> getImplementedInterfaces();

	/**
	 * Retorna el codigo de los metodos generales que deben
	 * ir en la pagina.
	 *
	 * <p>
	 * Dejar un Enter al principio.
	 * El la firma del metodo debe tener un solo TAB.
	 * El codigo interno debe tener doble TAB.
	 * Dejar un Enter al final de cada linea.
	 * Agregar punto y coma a cada linea que lo necesite.
	 * Dejar un Enter entre cada metodo.
	 *
	 * <br>Si no hay escribir ningún código, devolver vacío.
	 */
	public String getMethodsCode();

	/**
	 * <p>
	 * Retorna una lista de strings,
	 * cada uno representa un import
	 * que es necesario para el uso del widget.
	 * El PageGenarator se encarga de que no se repitan.
	 *
	 * Deben tener la palabra "import".
	 * Deben tener punto y coma al final.
	 * No deben tener ENTER al final.
	 */
	public List<String> getImports();


//	/**
//	 * <p>
//	 * Cada elemento debe poder ser modificable mediante
//	 * un panel Swing que permite setear su propiedades.
//	 *
//	 * <br>Este metodo retorna un panel Swing que contiene las propiedades.
//	 * <br>Se le debe setear el dialog al panel, para que cuando se lo quiera
//	 * cerrar, se pueda hacer: <code>dialog.setVisible(false);</code>
//	 * <p>
//	 */
//	public JPanel getPropertyPanel(PageMakerEditDialog dialog);


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
	public List<String> getGlobalCode();

	/**
	 * <p>
	 * Retorna el nombre de la variable para agregar al componente padre.
	 * <br>Ejemplo: si se creo un boton que se va a agregar a una pagina
	 * se debe devoler el nombre de la variable del boton, para que el
	 * PageGenator pueda hacer <code>this.add( varName );</code>
	 *
	 *<br>
	 * Los elementos compuestos por otros elementos, pueden usar este
	 * metodo para settear sus elementos internos.
	 */
	public List<String> getAddToPageVariables();

	/**
	 * <p>
	 * Retorna el codigo que se desea agregar al metodo
	 * <code>onVisible()</code>de la pagina.
	 * <p>
	 * Cada linea debe empezar con dos TABS y terminar con Enter.
	 * <br>Deben tener punto y coma.
	 */
	public String getOnVisibleCode();

	/**
	 * Retorna un texto descriptivo del Widget que se genera.
	 * El texto debe ser HTML y debe tener las tabulaciones necesarias.
	 */
	public String getDescriptionText();

	/**
	 * Retorna una imagen descriptiva del Widget que se genera.
	 * La imagen debe estar en el paquete <code>net.latin.server.utils.pageGeneraror.images</code>.
	 * Debe ser de 300x150.
	 * Cargarla con el método <code>PageMakerScreen.loadImage()</code>
	 */
	public ImageIcon getDescriptionImage();

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

//	/**
//	 * Devuelve un DragabblePanel si el widget se puede utilizar con algún
//	 * generator de posicionamiento absoluto. En caso contrario devolver null.
//	 * @return DragabblePanel o null
//	 */
//	public DraggablePanel getDraggablePanel();

	/**
	 * Hace que el generator cargue sus valores lógicos internos, en función
	 * de los valores de su interfaz gráfica.
	 * Siempre debe tener valores por default.
	 */
	public void setProperties();


}
