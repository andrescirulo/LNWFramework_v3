package net.latin.client.widget.form;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.TextAlign;
import net.latin.client.widget.base.GwtController;
import net.latin.client.widget.base.GwtVisualComponent;
import net.latin.client.widget.label.GwtLabel;
import net.latin.client.widget.panels.GwtHorizontalPanel;

/**
 * Formulario compuesto por elemtos del tipo [texto, widget]
 * Ejemplo:
 * 			["usuario", TextBox ]
 * 			["pais",   ComboBox ]
 * Hay que ir agregando elementos compuestos para formar el formulario
 *
 * @author Matias Leone
 *
 */
public class GwtForm<T> extends GwtVisualComponent {

	/**
	 * CSS
	 */
	private static final String MAIN_CSS = "form-main";
	private static final String ALTERNATIVE_MAIN_CSS = "GwtFormAlternativeMain";
	private static final String TITLE_CSS = "form-titulo";
	private static final String ELEMENTS_CSS = "form-elements-panel";
	private static final String BUTTONS_CSS = "form-buttons-panel";
	private static final String MESSAGE_PANEL_CSS = "GwtFormMessagePanel";
	private static final String MESSAGE_LABEL_CSS = "GwtFormMessageLabel";
	private static final String RIGHT_PANEL_CSS = "GwtFormRightPanel";

	private final Label labelTitulo;
	private final FlowPanel mainPanel;
	protected final VerticalPanel elementsPanel;
	private final GwtHorizontalPanel mainButtonsPanel;
	private final GwtHorizontalPanel buttonsPanel;
	private final GwtHorizontalPanel leftButtonsPanel;
	protected final List<GwtFormElement> elementList;
	private final HorizontalPanel messagePanel;
	private final Label labelMsg;
	private GwtDataFormManager<T> manager;
	private T dataObj;
	private GwtFormElement autoFocusElement;
	private VerticalPanel rightPanel;
	private List<Widget> widgets;

	/**
	 * Crea un nuevo formulario
	 */
	public GwtForm() {
		this( "", null );
	}

	/**
	 * Crea un nuevo formulario
	 */
	public GwtForm( String title ) {
		this( title, null );
	}

	/**
	 * Crea un nuevo formulario
	 */
	public GwtForm( String Titulo, GwtDataFormManager<T> manager ) {
		widgets=new ArrayList<Widget>();
		this.manager = manager;
		this.elementList = new ArrayList<GwtFormElement>();

		//final variables initialization
		labelTitulo = new Label();
		labelTitulo.addStyleName(TITLE_CSS);
		labelTitulo.addStyleName("animate");
		mainPanel = new FlowPanel();
		
		messagePanel = new HorizontalPanel();
		elementsPanel = new VerticalPanel();
		mainButtonsPanel = new GwtHorizontalPanel();
		buttonsPanel = new GwtHorizontalPanel();
		leftButtonsPanel = new GwtHorizontalPanel();
		labelMsg = new Label();

		rightPanel = new VerticalPanel();


		visualInit(Titulo);
	}

	public void setTitleText(String title) {
		if (title == null || "".equals(title)) {
			this.remove(labelTitulo);
		} else {
			labelTitulo.setText(title);
		}
	}

	/**
	 * Carga el manipulador de datos del formulario
	 */
	public void setDataFormManager(GwtDataFormManager<T> manager) {
		this.manager = manager;
	}

	/**
	 * Carga los valores por defecto en el objeto de datos y
	 * luego los muestra en el formulario
	 * @param dataObj
	 * @return el objeto de datos cargado
	 */
	public T loadDefaultValues() {
		if( this.manager != null ) {
			this.dataObj = this.manager.loadDefaultValues();
			this.manager.loadVisualDefaultForm( this );
		}
		return this.dataObj;
	}

	/**
	 * Carga solamente el formulario visual, con valores por default
	 * @param dataObj
	 */
	public void loadVisualDefaultValues() {
		if( this.manager != null ) {
			this.manager.loadVisualDefaultForm( this );
		}
	}

	/**
	 * Carga el formulario con la informacion del objeto de datos
	 * del formulario
	 */
	public void loadForm() {
		if( this.manager != null ) {
			this.manager.loadForm( this, this.dataObj );
		}
	}

	/**
	 * Carga el objeto de datos con los valores actuales
	 * del formulario
	 * @param dataObj
	 */
	public void loadForm( T dataObj ) {
		this.setDataObject(dataObj);
		if( this.manager != null ) {
			this.manager.loadForm( this, this.dataObj );
		}
	}

	/**
	 * Carga el objeto de datos con los valores actuales
	 * del formulario visual
	 * @return el objeto de datos cargado
	 */
	public T loadDataObject() {
		if( this.manager != null ) {
			this.manager.loadDataObject( this, this.dataObj );
		}
		return this.dataObj;
	}

	/**
	 * Devuelve el objeto de datos con el cual trabaja el formulario
	 * @return
	 */
	public T getDataObject() {
		return this.dataObj;
	}

	/**
	 * Carga el objeto de datos con el cual trabaja el formulario
	 * @param dataObj
	 */
	public void setDataObject( T dataObj ) {
		this.dataObj = dataObj;
	}

	/**
	 * Initialize the visual data of the form
	 * @param Titulo
	 */
	private void visualInit(String Titulo) {
		//Label titulo
		labelTitulo.setText( Titulo );

		//Panel de mensajes
		messagePanel.setStyleName(MESSAGE_PANEL_CSS);
		this.messagePanel.setVisible( false );
		this.messagePanel.setWidth( "100%" );
		labelMsg.setStyleName(MESSAGE_LABEL_CSS);
		this.messagePanel.add( this.labelMsg );

		//Separacion vertical
		Label labelSeparacion = new Label();
		labelSeparacion.setHeight( GwtController.defaultI18n.GwtForm_title_separation );

		//Panel para los elementos
		elementsPanel.setStyleName( ELEMENTS_CSS );

		//Panel a la derecha (ej. una foto)
		rightPanel = new VerticalPanel();
		rightPanel.setStyleName(RIGHT_PANEL_CSS);

		//Panel para botones
		//En el mainButtonsPanel metemos dos paneles para botones, uno para la derecha y otro para la izq
//		mainButtonsPanel.setHorizontalAlignment( HorizontalPanel.ALIGN_CENTER );
		mainButtonsPanel.setTextAlign(TextAlign.CENTER);
		mainButtonsPanel.setWidth( "100%" );

		//panel para botones que estan del lado izquierdo
		//necesitamos un panel aux para poder formatear hacia la izquierda
		leftButtonsPanel.setTextAlign(TextAlign.LEFT);
		leftButtonsPanel.setWidth("50%");
		mainButtonsPanel.add( leftButtonsPanel );

		//panel de la derecha, necesitamos un panel aux para poder formatear hacia la derecha
		buttonsPanel.setTextAlign(TextAlign.RIGHT);
		buttonsPanel.setWidth("50%");
		mainButtonsPanel.add( buttonsPanel );

		HorizontalPanel hp = new HorizontalPanel();
		hp.add(elementsPanel);
		hp.add(rightPanel);
		rightPanel.setVisible(false);
		hp.setWidth("100%");

		//agregar el panel vertical al mainPanel

		this.setStyleName( MAIN_CSS );
		this.mainPanel.add( messagePanel );
		this.mainPanel.add( labelSeparacion );
		this.mainPanel.add( hp );
		this.mainPanel.add( mainButtonsPanel );
		this.mainPanel.addStyleName("form-contenido");
	}

	public void addWidgetToRight(Widget widget) {
		addWidgetToRight(widget, VerticalPanel.ALIGN_RIGHT, VerticalPanel.ALIGN_TOP);
	}

	public void addWidgetToRight(Widget widget, HorizontalAlignmentConstant hal, VerticalAlignmentConstant val) {
		rightPanel.setVisible(true);
		rightPanel.setHorizontalAlignment(hal);
		rightPanel.setVerticalAlignment(val);
		rightPanel.add(widget);
	}

	/**
	 * Do not execute this method.
	 * Use <code>addElement()</code> instead
	 */
	public void add(Widget w) {
		throw new RuntimeException( "Can not use the method add() with GwtForm. Use addElement()" );
	}

	/**
	 * Agrega un elemento al formulario del tipo GwtDefaultFormElement,
	 * que tiene la forma [texto, widget].
	 * El texto se considera la ID del elemento y debe ser único en
	 * todo el formulario.
	 *
	 * @return Indice dentro del formulario (sirve por ejemplo para ocultar
	 * 			todo el elemento dentro del formulario)
	 */
	public int addElementWithLabel( String texto, Widget widget ) {
		//crear elemento default
		GwtDefaultFormElement element = new GwtDefaultFormElement();
		element.setElementId(texto);
		element.setWidget(widget);
		element.buildElement( this );
		widgets.add(widget);
		ajustarEstilo(widget);

		//agregarlo logicamente
		if ( autoFocusElement == null ) { autoFocusElement = element; };
		elementList.add( element );

		return ( elementList.size() - 1 );
	}

	/**
	 * Agrega un elemento al formulario del tipo GwtDefaultFormElement,
	 * que tiene la forma [texto, widget]. Además agrega un texto al pie
	 * del elemento para especificar un comentario, ejemplo o aclaración
	 * de cómo completar el item. Si required es true agrega el asterisco
	 * rojo para indicar que ese item es requerido.
	 * El texto se considera el ID del elemento y debe ser único en
	 * todo el formulario.
	 *
	 * @return Indice dentro del formulario (sirve por ejemplo para ocultar
	 * 			todo el elemento dentro del formulario)
	 */
	public int addElementWithLabelAndFooter( String texto, Widget widget, String footerText, boolean required ) {
		//crear elemento default
		GwtDefaultFormElement element = new GwtDefaultFormElement();
		element.setElementId(texto);
		element.setRequired(required);

		//creamos el panel que engloba al widget
		VerticalPanel vpWidget = new VerticalPanel();
		vpWidget.setHorizontalAlignment( VerticalPanel.ALIGN_LEFT );
		vpWidget.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
		vpWidget.add( widget );
		widgets.add(widget);
		ajustarEstilo(widget);

		element.setFooterText(footerText);
		element.setWidget(vpWidget);
		element.buildElement( this );

		//agregarlo logicamente
		if ( autoFocusElement == null ) { autoFocusElement = element; };
		elementList.add( element );

		return ( elementList.size() - 1 );
	}

	/**
	 * Agrega un elemento al formulario del tipo GwtDefaultFormElement,
	 * que tiene la forma [texto, widget].
	 * El elemento se marca como requerido.
	 * El texto se considera la ID del elemento y debe ser único en
	 * todo el formulario.
	 *
	 * @return Indice dentro del formulario (sirve por ejemplo para ocultar
	 * 			todo el elemento dentro del formulario)
	 */
	public int addRequiredElementWithLabel( String texto, Widget widget ) {
		//crear elemento default
		GwtDefaultFormElement element = new GwtDefaultFormElement();
		element.setElementId(texto);
		element.setWidget(widget);
		element.setRequired(true);
		element.buildElement( this );
		widgets.add(widget);
		ajustarEstilo(widget);
		//agregarlo logicamente
		if ( autoFocusElement == null ) { autoFocusElement = element; };
		elementList.add( element );

		return ( elementList.size() - 1 );
	}

	/**
	 * Agrega un elemento al formulario del tipo GwtSimpleFormElement.
	 * El texto se considera la ID del elemento y debe ser único en
	 * todo el formulario.
	 *
	 * @return Indice dentro del formulario (sirve por ejemplo para ocultar
	 * 			todo el elemento dentro del formulario)
	 */
	public int addElement( String texto, Widget widget ) {
		GwtSimpleFormElement element = new GwtSimpleFormElement();
		element.setElementId(texto);
		element.setWidget(widget);
		element.buildElement( this );
		widgets.add(widget);
		ajustarEstilo(widget);
		//agregarlo logicamente
		if ( autoFocusElement == null ) { autoFocusElement = element; };
		elementList.add( element );

		return ( elementList.size() - 1 );
	}
	
	public int addElementWithFooter(Widget widget ,String footer) {
		return addElementWithFooter( "__randomId_" + Random.nextInt(), widget ,footer);
	}
	/**
	 * Agrega un elemento al formulario del tipo GwtSimpleFormElement con un footer.
	 * El texto se considera la ID del elemento y debe ser único en
	 * todo el formulario.
	 *
	 * @return Indice dentro del formulario (sirve por ejemplo para ocultar
	 * 			todo el elemento dentro del formulario)
	 */
	public int addElementWithFooter( String texto, Widget widget ,String footer) {
		GwtSimpleFormElement element = new GwtSimpleFormElement();
		element.setElementId(texto);
		element.setWidget(widget);
		element.setFooter(footer);
		element.buildElement( this );
		
		widgets.add(widget);
		ajustarEstilo(widget);
		//agregarlo logicamente
		if ( autoFocusElement == null ) { autoFocusElement = element; };
		elementList.add( element );
		
		return ( elementList.size() - 1 );
	}

	/**
	 * Agrega un elemento al fomulario del tipo GwtSimpleFormElement.
	 * Por compatibilidad para atrás, no se pide una ID única al elemento,
	 * sino que se autogenera con Random.
	 * Utilizar preferentemente <code>addSimpleElement( String texto, Widget widget )</code>
	 *
	 * @return Indice dentro del formulario (sirve por ejemplo para ocultar
	 * 			todo el elemento dentro del formulario)
	 */
	public int addElement(Widget widget) {
		return addElement( "__randomId_" + Random.nextInt(), widget );
	}

	/**
	 * Agrega un elemento al formulario, personalizado por el usuario.
	 * No hacer <code>build()</code> al elemento antes de agregarlo. Lo
	 * hace el formulario.
	 *
	 * @return Indice dentro del formulario (sirve por ejemplo para ocultar
	 * 			todo el elemento dentro del formulario)
	 */
	public int addElement( GwtFormElement element ) {
		element.buildElement( this );
		if (element.getElementPanel().getWidgetCount()>0){
			widgets.add(element.getElementPanel().getWidget(0));
			ajustarEstilo(element.getElementPanel().getWidget(0));
		}
		
		//agregarlo logicamente
		if ( autoFocusElement == null ) { autoFocusElement = element; };
		elementList.add( element );

		return ( elementList.size() - 1 );
	}

	/**
	 * Agrega un subtítulo al formulario.
	 * Ver clase <code>GwtFormSubtitle</code>
	 *
	 * @return Indice dentro del formulario (sirve por ejemplo para ocultar
	 * 			todo el elemento dentro del formulario)
	 */
	public int addSubtitle( String title ) {
		return this.addElement( title, new GwtFormSubtitle( title ) );
	}

	/**
	 * Busca y devuelve el elemento con el ID especificado.
	 * Si no encuentra ninguno, devuelve null.
	 */
	public GwtFormElement getElement( String elementId ) {
		int size = elementList.size();
		GwtFormElement element;
		for (int i = 0; i < size; i++) {
			element = (GwtFormElement) elementList.get( i );
			if(element.getElementId().equals( elementId )) {
				return element;
			}
		}
		return null;
	}

	/**
	 * Devuelve el elemento agregado al formulario en la posición especificada.
	 */
	public GwtFormElement getElement( int elementPos ) {
		return (GwtFormElement) elementList.get( elementPos );
	}


	/**
	 * Limpia todos los elementos del formulario.
	 * <br>Cada elemento debe saber como limpiarse correctamente.
	 * <br>Ver interfaz <code>LnwWidget</code>
	 *
	 * Le agregue recursividad:
	 *  * en widgets y elements hasta el final.
	 */
	public void resetWidget() {
		hideMessage();
		int size = elementList.size();
		GwtFormElement element;
		for (int i = 0; i < size; i++) {
			element = (GwtFormElement) elementList.get(i);
			element.resetWidget();
		}
		this.setFocus();
	}


	/**
	 * Agrega un botón al formulario, en el panel derecho.
	 */
	public void addButton( Widget button ) {
		this.buttonsPanel.add( button );
	}

	/**
	 * Agrega un botón al formulario, en el panel izquierdo.
	 */
	public void addLeftButton( Widget button ) {
		this.leftButtonsPanel.add( button );
	}

	/**
	 * Checks if it is necessary to show the buttons panel
	 */
	private void checkButtonsPanelVisibility() {
		if( buttonsPanel.getWidgetCount() == 0  && leftButtonsPanel.getWidgetCount() == 0) {
			mainButtonsPanel.setVisible( false );
		}
	}

	/**
	 * Renderiza el formulario.
	 * Es necesario llamar a este método una vez que ya se agregaron
	 * todos los elementos al mismo.
	 */
	public GwtVisualComponent render() {
		//cargamos todos los elementos
		int size = elementList.size();
		GwtFormElement element;
		for (int i = 0; i < size; i++) {
			element = (GwtFormElement) elementList.get( i );
			//agregado visual
			elementsPanel.add( element.getElementPanel() );
		}

		//agregamos el titulo
		super.add(labelTitulo);
		
		//agregamos el panel principal
		super.add( mainPanel );

		//vemos si hay que mostrar la barra de botones
		checkButtonsPanelVisibility();

		return super.render();
	}

	/**
	 * Show or hide an element of the form
	 * @param elementRow: row of the element
	 * @param flag: show or hide
	 */
	public void setElementVisible( int elementRow, boolean flag ) {
		this.getElement(elementRow).setVisible( flag );
	}

	/**
	 * Infor if an element of the form is visible or not
	 * @param elementRow: row of the element
	 * @return
	 */
	public boolean isElementVisible( int elementRow ) {
		return this.getElement(elementRow).isVisible();
	}


	/**
	 * Muestea un mensaje en la barra de estado del formulario
	 * @param msg
	 */
	public void showMessage( String msg ) {
		this.labelMsg.setText( msg );
		this.messagePanel.setVisible( true );
	}

	/**
	 * Oculta la barra de estado del formulario
	 */
	public void hideMessage() {
		this.messagePanel.setVisible( false );
	}

	/**
	 * Informa si la barra de estado del formulario se encuentra visible
	 */
	public boolean isMessageVisible() {
		return this.messagePanel.isVisible();
	}

	/**
	 * Sets the form's title
	 */
	public void setTitle(String title) {
		labelTitulo.setText( title );
	}

	/**
	 * Alineación horizontal del titulo
	 */
	public void setTitleAligment( HorizontalAlignmentConstant align ) {
		labelTitulo.setHorizontalAlignment(align);
	}

	/**
	 * Hace focus sobre el autoFocusElement.
	 * Para setearlo llamara a <code>setAutoFocusElement()</code>
	 */
	public void setFocus() {
		//delegamos el foco al autoFocusWidget
		if( autoFocusElement == null ) return;

		autoFocusElement.setFocus();
	}

	/**
	 * Carga el elemento al cual se hará focus cuando el usuario llama a
	 * <code>resetWidget()<code>
	 * @param elementId ID del elemento
	 */
	public void setAutoFocusElement( String elementId ) {
		this.autoFocusElement = getElement(elementId);
	}

	/**
	 * Carga el CSS alternativo para el formulario
	 */
	public void setAlternativeStyle() {
		mainPanel.getWidget( 0 ).setStyleName( ALTERNATIVE_MAIN_CSS );
	}

	/**
	 * Carga el CSS normal para el formulario
	 */
	public void setNormalStyle() {
		mainPanel.getWidget( 0 ).setStyleName( MAIN_CSS );
	}

	/**
	 * Remueve todos los elementos (fields) agregados al formulario.
	 * No remueve el titulo ni los botones.
	 */
	public void removeAllElements() {
		elementList.clear();
		elementsPanel.clear();
	}

	/**
	 * Obtiene los botones del panel derecho del formulario.
	 */
	public List<Widget> getButtons() {
		List<Widget> widgets = new ArrayList<Widget>();
		for (int i = 0; i < buttonsPanel.getWidgetCount(); i++) {
			widgets.add(buttonsPanel.getWidget(i));
		}
		return widgets;
	}

	public void setEnabled(boolean enabled){
		setWidgetEnabled(getElement(), enabled);
	}
	
	public void setMainPanelWidth(String width){
		mainPanel.setWidth(width);
	}
	
	private void setWidgetEnabled(final Element element,final Boolean enabled){
		try{
			String elemType=element.getNodeName();
			if (elemType.toUpperCase().equals("INPUT") || elemType.toUpperCase().equals("SELECT") ||
					elemType.toUpperCase().equals("BUTTON")){
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
					public void execute() {
						element.setPropertyBoolean("disabled", !enabled);
					}
				});
			}
			for (int i = 0;i<DOM.getChildCount(element);i++){
				setWidgetEnabled(DOM.getChild(element, i), enabled);
			}
		}
		catch (Exception e){
		}
	}
	protected void ajustarEstilo(Widget widget){
		if (widget instanceof GwtLabel){
			widget.addStyleName("form-element-gwt-label");
		}
	}
}
