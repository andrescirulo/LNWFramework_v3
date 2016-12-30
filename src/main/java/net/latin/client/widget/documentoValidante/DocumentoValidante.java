package net.latin.client.widget.documentoValidante;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;

import net.latin.client.rpc.GwtAsyncCallback;
import net.latin.client.widget.base.CustomBean;
import net.latin.client.widget.button.GwtButton;
import net.latin.client.widget.documentoValidante.mvp.WidgetDisplay;
import net.latin.client.widget.documentoValidante.mvp.WidgetPresenter;
import net.latin.client.widget.documentoValidante.requisito.AbstractRequisitoValidantePresenter;
import net.latin.client.widget.documentoValidante.requisito.RequisitoValidanteDisplay;
import net.latin.client.widget.documentoValidante.requisito.cuil.CUILPresenter;
import net.latin.client.widget.documentoValidante.requisito.cuil.CUILView;
import net.latin.client.widget.documentoValidante.requisito.cuitProveedor.CUITProveedorPresenter;
import net.latin.client.widget.documentoValidante.requisito.cuitProveedor.CUITProveedorView;
import net.latin.client.widget.documentoValidante.requisito.dni.DNIPresenter;
import net.latin.client.widget.documentoValidante.requisito.dni.DNIView;
import net.latin.client.widget.documentoValidante.requisito.matriculaFederal.MatriculaFederalPresenter;
import net.latin.client.widget.documentoValidante.requisito.matriculaFederal.MatriculaFederalView;
import net.latin.client.widget.documentoValidante.requisito.matriculaProvincial.MatriculaProvincialPresenter;
import net.latin.client.widget.documentoValidante.requisito.matriculaProvincial.MatriculaProvincialView;
import net.latin.client.widget.documentoValidante.requisito.tomoFolio.TomoFolioPresenter;
import net.latin.client.widget.documentoValidante.requisito.tomoFolio.TomoFolioView;
import net.latin.client.widget.documentoValidante.rpc.IDocumentoValidanteService;
import net.latin.client.widget.documentoValidante.rpc.IDocumentoValidanteServiceAsync;
import net.latin.client.widget.form.GwtDefaultFormElement;
import net.latin.client.widget.form.GwtForm;
import net.latin.client.widget.listener.EnterKeyUpHandler;
import net.latin.client.widget.msg.GwtMsgHandler;

public class DocumentoValidante extends WidgetPresenter<DocumentoValidante.DocumentoValidanteDisplay> {
	
	public static final String FORM_KEY = "Documento Validante";
	
	public static final String BUSCAR = "Buscar";
	public static final String CAMBIAR = "Cambiar";
	
	protected AbstractRequisitoValidantePresenter currentPresenter;
	protected GwtMsgHandler msglistener;
	protected GwtDocumentoValidanteListener doclistener;
	protected Boolean estadoBuscar = true;
	private Long idPerfil = -1L;
	
	protected IDocumentoValidanteServiceAsync server;
	
	private boolean binded = false;
	
	private ClickHandler clickListener;
	private HandlerRegistration handlerRegistration; 
	
	// Keys para guardar los elementos en los CustomBean
	public final static String KEY_APELLIDO = "apellido";
	public final static String KEY_NOMBRE = "nombre";
	public final static String KEY_HABILITADO = "habilitado";
	public final static String KEY_HASH = "hash";
	public static final String KEY_DOC = "dochash";
	public static final String KEY_LIBRE = "free";
	
	public interface DocumentoValidanteDisplay extends WidgetDisplay {
		HasClickHandlers getButton();
		
		void setDocumentoValidante(Widget widget,String width);
		
		void setBotonEnabled(boolean b);
		
		void setBotonText(String buscar);
	}
	
	public DocumentoValidante(DocumentoValidanteDisplay display, GwtMsgHandler msglistener, GwtDocumentoValidanteListener doclistener) {
		super(display);
		this.msglistener = msglistener;
		this.doclistener = doclistener;
		server = IDocumentoValidanteService.Util.getInstance();
	}
	
	public void bindRuntime(TiposDocumentoValidante tipoDoc,String footer) {
		idPerfil = tipoDoc.getId().longValue();
		renderDocumentoValidante(tipoDoc);
		currentPresenter.setFooter(footer);
		doclistener.onTipoDocumentoValidanteDetected(tipoDoc, this);
		
		final DocumentoValidante self = this;
		
		if (clickListener != null) {
			handlerRegistration.removeHandler();
		}
		clickListener = new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (estadoBuscar) {
					// buscar el letrado en el server
					msglistener.clearMessages();
					if (currentPresenter.validate(msglistener)) {
						doclistener.onBuscar(self);
						if (Long.valueOf(-1).equals(idPerfil)) {
							// No se cual es el perfil del usuario
							server.searchLetrado(currentPresenter.getValue(), new GwtAsyncCallback<CustomBean>() {
								public void onSuccess(CustomBean result) {
									if (result.getBoolean("rta").booleanValue()) {
										onSearchLetradoSuccess(result);
									} else {
										onSearchLetradoUnseccesful(result);
									}
								}
							});
						} else {
							// Conozco el perfil de usuario
							server.searchLetrado(currentPresenter.getValue(), idPerfil, new GwtAsyncCallback<CustomBean>() {
								public void onSuccess(CustomBean result) {
									if (result.getBoolean("rta").booleanValue()) {
										onSearchLetradoSuccess(result);
									} else {
										onSearchLetradoUnseccesful(result);
									}
								}
							});
						}
					}
					else{
						currentPresenter.setFocus();
					}
				} else {
					doclistener.onCambiar(self);
					setBuscar(true);
				}
			}
		};
		handlerRegistration=display.getButton().addClickHandler(clickListener);
	}
	
	public void bindRuntime(TiposDocumentoValidante tipoDoc){
		bindRuntime(tipoDoc, "");
	}
	
	private void onSearchLetradoSuccess(CustomBean result) {
		setBuscar(false);
		CustomBean letrado = result.getBean("letrado");
		doclistener.onLetradoFound(letrado);
		if (letrado != null
				&& letrado.getBoolean(DocumentoValidante.KEY_LIBRE) != null
				&& !letrado.getBoolean(DocumentoValidante.KEY_LIBRE)) {
			setBuscar(true);	
		}
	}
	
	private void onSearchLetradoUnseccesful(CustomBean result) {
		addError(result.getString("msg"));
		doclistener.onLetradoFound(null);
	}
	
	public void bind() {
		
		if (!binded) {
			final DocumentoValidante self = this;
			server.getTipoDocumentoValidante(new GwtAsyncCallback<CustomBean>() {
				public void onSuccess(CustomBean result) {
					if (result.getBoolean("rta").booleanValue()) {
						TiposDocumentoValidante tiposDocumentoValidante = TiposDocumentoValidante.valueOf(result.getString("documento"));
						renderDocumentoValidante(tiposDocumentoValidante);
						doclistener.onTipoDocumentoValidanteDetected(tiposDocumentoValidante, self);
					} else {
						addError(result.getString("msg"));
					}
				}
			});
			handlerRegistration=display.getButton().addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if (estadoBuscar) {
						// buscar el letrado en el server por tomo/folio
						msglistener.clearMessages();
						if (currentPresenter.validate(msglistener)) {
							doclistener.onBuscar(self);
							buscarEnServidor();
						}
						else{
							currentPresenter.setFocus();
						}
					} else {
						doclistener.onCambiar(self);
						setBuscar(true);
					}
				}
				
			});
			binded = true;
		} else {
			resetWidget();
		}
	}
	
	protected void buscarEnServidor() {
		server.searchLetrado(currentPresenter.getValue(), new GwtAsyncCallback<CustomBean>() {
			public void onSuccess(CustomBean result) {
				// TODO verificar si lo encontro
				if (result.getBoolean("rta").booleanValue()) {
					setBuscar(false);
					doclistener.onLetradoFound(result.getBean("letrado"));
				} else {
					addError(result.getString("msg"));
					doclistener.onLetradoFound(null);
				}
			}
		});
	}
	
	protected void addError(String msg) {
		ArrayList<String> erroresList = new ArrayList<String>();
		erroresList.add(msg);
		msglistener.addAllErrorMessages(erroresList);
	}
	
	protected void setBuscar(boolean buscar) {
		display.setBotonEnabled(true);
		currentPresenter.setEnabled(buscar);
		currentPresenter.setFocus();
		if (buscar) {
			display.setBotonText(BUSCAR);
		} else {
			display.setBotonText(CAMBIAR);
		}
		estadoBuscar = buscar;
	}
	
	public void resetWidget() {
		setBuscar(true);
		currentPresenter.resetWidget();
	}
	
	public void unbind() {
	}
	
	// FIXME: esto se llamaria en el callback de la respuesta
	public void refreshForm(GwtForm form) {
		GwtDefaultFormElement formElement = (GwtDefaultFormElement) form.getElement(FORM_KEY);
		formElement.getDescriptionLabel().setText(currentPresenter.getLabelFormText());
		if (formElement.getFooterLabel() != null) {
			formElement.getFooterLabel().setText(currentPresenter.getFooterFormText());
		}
	}
	
	public void renderDocumentoValidante(TiposDocumentoValidante doc) {
		switch (doc) {
			case TOMO_FOLIO:
				currentPresenter = new TomoFolioPresenter(new TomoFolioView());
				break;
			case MATRICULA_FEDERAL:
				currentPresenter = new MatriculaFederalPresenter(new MatriculaFederalView());
				break;
			case DNI:
				currentPresenter = new DNIPresenter(new DNIView());
				break;
			case CUIL:
				currentPresenter = new CUILPresenter(new CUILView());
				break;
			case CUIT_PROVEEDOR:
				currentPresenter = new CUITProveedorPresenter(new CUITProveedorView());
				break;
			case MATRICULA_PROVINCIAL:
				currentPresenter = new MatriculaProvincialPresenter(new MatriculaProvincialView());
				break;
			default:
				throw new RuntimeException("Tipo de documento validante no reconocido: " + doc);
		}
		WidgetDisplay widgetDisplay = (WidgetDisplay) currentPresenter.getDisplay();
		display.setDocumentoValidante(widgetDisplay.asWidget(),widgetDisplay.getWidth());
		((RequisitoValidanteDisplay)currentPresenter.getDisplay()).getWidgetForNextFocus().addKeyUpHandler(new EnterKeyUpHandler() {
			protected void accionEnter(KeyUpEvent event) {
				((GwtButton)display.getButton()).click();
			}
		});
		resetWidget();
	}
	
	public CustomBean getValue() {
		return currentPresenter.getValue();
	}
	
	public String getBusinessToShow() {
		
		return currentPresenter.getBusinessToShow();
		
	}
	
	public String getDocumentoValidanteString() {
		
		return currentPresenter.getDocumentoValidanteString();
		
	}
	
	public Map<String, String> getStringMapValue() {
		return currentPresenter.getStringMapValue();
	}
	
	public List<String> getKeys() {
		return currentPresenter.getKeys();
	}
	
	public List<String> getColumnNames() {
		return currentPresenter.getColumnNames();
	}
	
	public void setValue(CustomBean abogado) {
		currentPresenter.setValue(abogado);
		setBuscar(false);
	}
	
	public void setBotonEnabled(boolean b) {
		display.setBotonEnabled(b);
	}
	
	public TiposDocumentoValidante getTipoDocumentoValidante() {
		
		return currentPresenter.getTipoDocumentoValidante();
		
	}
	
	public boolean isBinded() {
		return binded;
	}
	
	public String getLabelFormText() {
		return currentPresenter.getLabelFormText();
	}
	
	public void setIdPerfil(Long idPerfil) {
		this.idPerfil = idPerfil;
	}
	
	public Long getIdPerfil() {
		return idPerfil;
	}
	
	public void unSetTipoDocVal() {
		idPerfil = -1L;
		
	}
	
}
