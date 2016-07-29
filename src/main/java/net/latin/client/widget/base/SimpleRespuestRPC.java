package net.latin.client.widget.base;

public class SimpleRespuestRPC extends GwtBusinessObject {
	
	private Boolean respuesta=true;
	private String mensaje;
	
	public Boolean getRespuesta() {
		return respuesta;
	}
	
	public String getMensaje() {
		return mensaje;
	}
	
	public void setRespuesta(Boolean respuesta) {
		this.respuesta = respuesta;
	}
	
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	public void setError(String mensaje) {
		this.respuesta = false;
		this.mensaje = mensaje;
	}
	
}
