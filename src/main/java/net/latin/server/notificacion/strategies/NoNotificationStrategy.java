package net.latin.server.notificacion.strategies;

import java.util.List;

import net.latin.server.notificacion.Mensaje;

public class NoNotificationStrategy implements NotificationStrategy {

private static NoNotificationStrategy instance;
	
	public static NoNotificationStrategy getInstance(){
		if (instance==null){
			instance=new NoNotificationStrategy();
		}
		return instance;
	}
	
	private NoNotificationStrategy(){
		
	}
	
	public String procesar(String response) {
		return response;
	}

	public void updateMessages(List<Mensaje> mensajes) {
	}

}
