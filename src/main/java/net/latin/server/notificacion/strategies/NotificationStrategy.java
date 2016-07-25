package net.latin.server.notificacion.strategies;

import java.util.List;

import net.latin.server.notificacion.Mensaje;


public interface NotificationStrategy {
	public String procesar(String response);
	public void updateMessages(List<Mensaje> mensajes);
}
