package net.latin.server.notificacion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.latin.server.notificacion.strategies.NoNotificationStrategy;
import net.latin.server.notificacion.strategies.NotificationStrategy;
import net.latin.server.notificacion.strategies.SimpleNotificationStrategy;
import net.latin.server.persistence.LnwPersistenceUtils;
import net.latin.server.persistence.SpringUtils;
import net.latin.server.persistence.sql.core.LnwQuery;
import net.latin.server.persistence.sql.core.RestFcty;
import net.latin.server.persistence.sql.core.restrictions.LnwMultipleAnd;
import net.latin.server.persistence.sql.core.restrictions.LnwMultipleOr;
import net.latin.server.persistence.sql.oracle.LnwQueryOracle;
import net.latin.server.security.config.LnwGeneralConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.RowMapper;


public class GwtNotificationManager {
	private static final String MENSAJES_DATASOURCE = "MENSAJES_DATASOURCE";
	private static NotificationStrategy strategy=null;
	private static Log LOG=LogFactory.getLog(GwtNotificationManager.class);
	
	static{
		updateMessages();
	}
	
	public static String procesar(String response){
		return strategy.procesar(response);
	}
	
	public static void enableNotifications(Boolean enable){
		if (enable){
			strategy=SimpleNotificationStrategy.getInstance();
		}
		else{
			strategy=NoNotificationStrategy.getInstance();
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public static void updateMessages(){
		try{
			String dataSource=SpringUtils.getProperty(MENSAJES_DATASOURCE,"sauDataSource");
			LnwMultipleAnd and=new LnwMultipleAnd();
			LnwMultipleOr or=new LnwMultipleOr();
			or.addRestriction(RestFcty.eq("upper(aplicacion)",LnwGeneralConfig.getInstance().getApplicationName().toUpperCase()));
			or.addRestriction(RestFcty.isNull("aplicacion"));
			
			and.addRestriction(or);
			and.addRestriction(RestFcty.or(RestFcty.isNull("fecha_hasta"), RestFcty.gt("fecha_hasta", new Date())));
			
			
			LnwQuery query=new LnwQueryOracle();
			query.addFrom("MENSAJE");
			query.setWhere(and);
			query.selectAll();
			List<Mensaje> mensajes = LnwPersistenceUtils.query(query, dataSource,new RowMapper() {
				
				public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
					Mensaje mens=new Mensaje();
					mens.setId(rs.getInt("id"));
					mens.setTexto(rs.getString("texto"));
					mens.setTipo(rs.getString("tipo"));
					if (rs.getTimestamp("fecha_desde")!=null){
						Calendar desde=Calendar.getInstance();
						desde.setTime(rs.getTimestamp("fecha_desde"));
						mens.setFechaDesde(desde);
					}
					if (rs.getTimestamp("fecha_hasta")!=null){
						Calendar hasta=Calendar.getInstance();
						hasta.setTime(rs.getTimestamp("fecha_hasta"));
						mens.setFechaHasta(hasta);
					}
					return mens;
				}
			});
			enableNotifications(mensajes.size()>0);
			strategy.updateMessages(mensajes);
		}
		catch(Exception e){
			LOG.error("NO SE PUEDIERON OBTENER LOS MENSAJES",e);
		}
	}
}
