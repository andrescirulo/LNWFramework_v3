package net.latin.server.persistence;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

/**
 * Clase de ayuda para crear un StoredProcedure customizado. Evita el BUG de
 * dejar abierta una connection al utilizar tipos de parametros raros que piden
 * una connection para ser configurados.
 * 
 * @author Alejandro Sanchez
 */
public abstract class LnwSimpleSpringStoredProcedure extends StoredProcedure {
	
	private static final Log log = LogFactory.getLog(LnwSimpleSpringStoredProcedure.class);
	
	protected Connection currentConnection;
	
	public LnwSimpleSpringStoredProcedure(JdbcTemplate jdbcTemplate) {
		
		// crear decorator de JDBC Template para tener una unica connection ya
		// creada
		final LnwStoredProcedureJdbcTemplate spJdbcTemplate = new LnwStoredProcedureJdbcTemplate(jdbcTemplate);
		
		// pedir nueva connection
		currentConnection = spJdbcTemplate.createNewConnection();
		
		// cargar JdbcTemplate
		setJdbcTemplate(spJdbcTemplate);
	}
	
	@Override
	public Map execute(Map inParams) throws DataAccessException {
		Map result;
		try {
//			if(log.isDebugEnabled()){
				log.debug("Call: " + this.getSql());
				log.debug("DeclaredParameters:" + getParameterNames());
				log.debug("parameters:" + inParams);
//			}
			result = super.execute(inParams);
			return result;
		} catch (DataAccessException e) {
			throw e;
		} finally {
			
			// cerrar connection abierta para este stored
			((LnwStoredProcedureJdbcTemplate) this.getJdbcTemplate()).closeExistingConnection();
		}
	}

	private List getParameterNames() {
		List<String> parameterName = new ArrayList<String>();
		for (Object obj : this.getDeclaredParameters()) {
			parameterName.add(((SqlParameter)obj).getName());
		}
		return parameterName;
	}
	
}
