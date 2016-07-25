package net.latin.server.notificacion.strategies;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.latin.server.notificacion.Mensaje;
import net.latin.server.persistence.UserContext;

public class SimpleNotificationStrategy implements NotificationStrategy {

	private static Map<String,List<Integer>> mensajesMap;
	private static Map<String,Calendar> antiguedadMap;
	private static Calendar ultimaFechaEliminacion;
	private static List<Mensaje> mensajes;
	private static SimpleNotificationStrategy instance;
	
	public static SimpleNotificationStrategy getInstance(){
		if (instance==null){
			instance=new SimpleNotificationStrategy();
		}
		return instance;
	}
	
	private SimpleNotificationStrategy() {
		mensajesMap=new HashMap<String,List<Integer>>();
		antiguedadMap=new HashMap<String,Calendar>();
		ultimaFechaEliminacion=Calendar.getInstance();
	}

	public String procesar(String response) {
		String finalResponse=response;
		if (response.startsWith("//OK") && !urlExcluida()){
			String mensajesString=getMensajesString();
			if (!mensajesString.equals("")){
				finalResponse= "//OK_MSG[" +  mensajesString + "]" + finalResponse;
				limpiarAntiguos();
			}
		}
		return finalResponse;
	}
	
	private void limpiarAntiguos(){
		Calendar fechaLimite = Calendar.getInstance();
		fechaLimite.add(Calendar.HOUR, -1);
		if (ultimaFechaEliminacion.before(fechaLimite)){
			eliminarAntiguos(fechaLimite);
		}
	}
	
	private synchronized void eliminarAntiguos(Calendar fechaLimite) {
		List<String> aEliminar=new ArrayList<String>();
		for (Entry<String,Calendar> entry:antiguedadMap.entrySet()){
			if (entry.getValue().before(fechaLimite)){
				aEliminar.add(entry.getKey());
			}
		}
		for(String session:aEliminar){
			antiguedadMap.remove(session);
			mensajesMap.remove(session);
		}
		ultimaFechaEliminacion=Calendar.getInstance();
	}

	private boolean urlExcluida() {
		String url=UserContext.getInstance().getRequest().getRequestURL().toString();
		List<String> excluidas =new ArrayList<String>();
		excluidas.add("securityServer.gwt");
		excluidas.add("suggestQuote.gwt");
		for (String  excluida: excluidas) {
			if(url.endsWith(excluida)) {
				return true;
			}
		}
		
		return false;
	}

	private String getMensajesString() {
		String sessionId=UserContext.getInstance().getRequest().getSession().getId();
		String mensajesString="";
		List<Integer> listaEnviados = mensajesMap.get(sessionId);
		if (listaEnviados==null){
			listaEnviados=new ArrayList<Integer>();
			mensajesMap.put(sessionId,listaEnviados);
		}
		for(Mensaje mens:getMensajes()){
			if (!listaEnviados.contains(mens.getId()) && estaEnRangoHorario(mens)){
				mensajesString=mensajesString + mens.getString() + "|";
				listaEnviados.add(mens.getId());
			}
		}
		if (mensajesString.length()>0){
			mensajesString=mensajesString.substring(0,mensajesString.length()-1);
		}
		
		return mensajesString;
	}

	private boolean estaEnRangoHorario(Mensaje mens) {
		Calendar ahora=Calendar.getInstance();
		
		if (mens.getFechaDesde()==null){
			if (mens.getFechaHasta()==null){
				return true;
			}
			else{
				return ahora.before(mens.getFechaHasta());
			}
		}
		else{
			if (mens.getFechaHasta()==null){
				return ahora.after(mens.getFechaDesde());
			}
			else{
				return (ahora.after(mens.getFechaDesde()) && ahora.before(mens.getFechaHasta()));
			}
		}
	}

	public void updateMessages(List<Mensaje> mensajes){
		SimpleNotificationStrategy.mensajes=mensajes;
		limpiarAntiguos();
	}
	
	private List<Mensaje> getMensajes() {
		if (mensajes==null){
			mensajes=new ArrayList<Mensaje>();
		}
		return mensajes;
	}

	

}
