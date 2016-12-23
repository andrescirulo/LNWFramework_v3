package net.latin.client.widget.documentoValidante.rpc;

import com.google.gwt.core.client.GWT;

import net.latin.client.rpc.GwtRpc;
import net.latin.client.rpc.GwtRpcInterface;
import net.latin.client.rpc.GwtServerCreator;
import net.latin.client.widget.base.CustomBean;
import net.latin.client.widget.base.GwtController;

public interface IDocumentoValidanteService extends GwtRpcInterface {
	
	public CustomBean searchLetrado(CustomBean value);
	
	public CustomBean getTipoDocumentoValidante();
	
	public CustomBean searchLetradoUsuario(CustomBean value);
	
	public CustomBean searchLetrado(CustomBean value, Long idRequisito);
	
	public static class Util {
		public static final String DOCUMENTO_VALIDANTE_SERVER_NAME = "__documentoValidante";
		private static IDocumentoValidanteServiceAsync instance;
		
		public static IDocumentoValidanteServiceAsync getInstance() {
			if (instance == null) {
				GwtRpc.getInstance().addServer(DOCUMENTO_VALIDANTE_SERVER_NAME, "__documentoValidante.gwt", new GwtServerCreator() {
					public String getBasePath() {
						return GwtController.instance.getBasePath();
					}
					
					public Object createServer() {
						return GWT.create(IDocumentoValidanteService.class);
					}
				});
				instance = (IDocumentoValidanteServiceAsync) GwtRpc.getInstance().getServer(DOCUMENTO_VALIDANTE_SERVER_NAME);
			}
			return instance;
		}
	}
	
}
