package net.latin.server.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import net.latin.client.widget.base.CustomBean;
import net.latin.server.persistence.sql.core.LnwDelete;
import net.latin.server.persistence.sql.core.LnwFunction;
import net.latin.server.persistence.sql.core.LnwInsert;
import net.latin.server.persistence.sql.core.LnwQuery;
import net.latin.server.persistence.sql.core.LnwStoredProcedure;
import net.latin.server.persistence.sql.core.LnwUpdate;
import net.latin.server.persistence.sql.core.PreparedStatement;
import net.latin.server.persistence.sql.oracle.LnwQueryOracle;
import net.latin.server.persistence.storedAdapters.LnwDefaultSpringStoredProcedure;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

/**
 * Utilidades para ejecutar querys, inserts, etc.
 *
 * @author Matias Leone
 */
public class LnwPersistenceUtils {

	private static final Log LOG = LogFactory.getLog(LnwPersistenceUtils.class);

	/**
	 * RowMapper de Spring utilizado para mapear resultados a un CustomBean.
	 * Se crea una única instancia y se reutiliza.
	 */
	public static final CustomBeanRowMapper customBeanRowMapper = new CustomBeanRowMapper();

	private LnwPersistenceUtils() {
	}

	/**
	 * Loggea los parametros utilizados en una sentencia SQL,
	 * salvo en stored procedures que usa su propio mecanismo
	 * @param params
	 */
	private static final void logParams(Object[] params) {
		if(LOG.isDebugEnabled()) {
			Object object;
			String param;
			for (int i = 0; i < params.length; i++) {
				object = params[i];
				param = object != null ? object.toString() : "null";
				LOG.debug("Param " + i + ": " + param);
			}
		}
	}

	/**
	 * Ejecuta un insert
	 * @param insert insert ya parametrizado, pero sin llamar a <code>buildInsert()</code>
	 * @return numero de filas afectadas
	 */
	public static int insert( LnwInsert insert ) {
		final PreparedStatement preparedStatement = insert.buildInsert();
		LOG.debug( preparedStatement.getSentencia() );
		logParams(preparedStatement.getParams());
		return SpringUtils.getJdbcTemplate().update( preparedStatement.getSentencia(), preparedStatement.getParams() );
	}

	/**
	 * Ejecuta un insert para un dataSourceName
	 * @param insert insert ya parametrizado, pero sin llamar a <code>buildInsert()</code>
	 * @param dataSourceName nombre del data source
	 * @return numero de filas afectadas
	 *
	 */
	public static int insert( LnwInsert insert, String dataSourceName ) {
		final PreparedStatement preparedStatement = insert.buildInsert();
		LOG.debug( preparedStatement.getSentencia() );
		logParams(preparedStatement.getParams());
		return SpringUtils.getJdbcTemplate(dataSourceName).update( preparedStatement.getSentencia(), preparedStatement.getParams() );
	}

	/**
	 * Ejecuta un update para un dataSourceName
	 * @param insert insert ya parametrizado, pero sin llamar a <code>buildUpdate()</code>
	 * @param dataSourceName nombre del data source
	 * @return numero de filas afectadas
	 */
	public static int update( LnwUpdate update, String dataSourceName ) {
		final PreparedStatement preparedStatement = update.buildUpdate();
		LOG.debug( preparedStatement.getSentencia() );
		logParams(preparedStatement.getParams());
		return SpringUtils.getJdbcTemplate(dataSourceName).update( preparedStatement.getSentencia(), preparedStatement.getParams() );
	}

	/**
	 * Ejecuta un update
	 * @param insert insert ya parametrizado, pero sin llamar a <code>buildUpdate()</code>
	 * @return numero de filas afectadas
	 */
	public static int update( LnwUpdate update ) {
		final PreparedStatement preparedStatement = update.buildUpdate();
		LOG.debug( preparedStatement.getSentencia() );
		logParams(preparedStatement.getParams());
		return SpringUtils.getJdbcTemplate().update( preparedStatement.getSentencia(), preparedStatement.getParams() );
	}

	/**
	 * Ejecuta un delete
	 * @param insert insert ya parametrizado, pero sin llamar a <code>buildDelete()</code>
	 * @return numero de filas afectadas
	 */
	public static int delete( LnwDelete delete ) {
		final PreparedStatement preparedStatement = delete.buildDelete();
		LOG.debug( preparedStatement.getSentencia() );
		logParams(preparedStatement.getParams());
		return SpringUtils.getJdbcTemplate().update( preparedStatement.getSentencia(), preparedStatement.getParams() );
	}

	/**
	 * Ejecuta un delete para un data source
	 * @param insert insert ya parametrizado, pero sin llamar a <code>buildDelete()</code>
	 * @param dataSourceName nombre del data source
	 * @return numero de filas afectadas
	 */
	public static int delete( LnwDelete delete, String dataSourceName ) {
		final PreparedStatement preparedStatement = delete.buildDelete();
		LOG.debug( preparedStatement.getSentencia() );
		logParams(preparedStatement.getParams());
		return SpringUtils.getJdbcTemplate(dataSourceName).update( preparedStatement.getSentencia(), preparedStatement.getParams() );
	}



	/**
	 * ###########################################################
	 * ############# STORED PROCEDURES METHODS ###################
	 * ###########################################################
	 */


	/**
	 * Ejecuta un stored procedure, para un data source especificado
	 * @param stored
	 * @param dataSourceName
	 * @return
	 */
	public static void excuteStoredProcedure( LnwStoredProcedure stored, String dataSourceName ) {
		LOG.debug( "Executed STORED PROCEDURE: " + stored.getProcedure() );
		LnwDefaultSpringStoredProcedure.createSpringStoredProcedure(stored, SpringUtils.getJdbcTemplate(dataSourceName)).executeForVoid();
	}

	/**
	 * Ejecuta un stored procedure
	 * @param stored
	 * @param dataSourceName
	 * @return
	 */
	public static void excuteStoredProcedure( LnwStoredProcedure stored ) {
		LOG.debug( "Executed STORED PROCEDURE: " + stored.getProcedure() );
		LnwDefaultSpringStoredProcedure.createSpringStoredProcedure(stored, SpringUtils.getJdbcTemplate() ).executeForVoid();
	}

	/**
	 * Ejecuta un stored procedure y devuelve su resultado como String, para un data source especificado
	 * @param stored
	 * @param dataSourceName
	 * @return
	 */
	public static String excuteStoredProcedureForString( LnwStoredProcedure stored, String dataSourceName ) {
		LOG.debug( "Executed STORED PROCEDURE: " + stored.getProcedure() );
		return LnwDefaultSpringStoredProcedure.createSpringStoredProcedure(stored, SpringUtils.getJdbcTemplate(dataSourceName) ).executeForString();
	}

	/**
	 * Ejecuta un stored procedure y devuelve su resultado como String
	 * @param stored
	 * @return
	 */
	public static String excuteStoredProcedureForString( LnwStoredProcedure stored ) {
		LOG.debug( "Executed STORED PROCEDURE: " + stored.getProcedure() );
		return LnwDefaultSpringStoredProcedure.createSpringStoredProcedure(stored, SpringUtils.getJdbcTemplate() ).executeForString();
	}

	/**
	 * Ejecuta un stored procedure y devuelve su resultado como Long, para un data source especificado
	 * @param stored
	 * @param dataSourceName
	 * @return
	 */
	public static Long excuteStoredProcedureForLong( LnwStoredProcedure stored, String dataSourceName ) {
		LOG.debug( "Executed STORED PROCEDURE: " + stored.getProcedure() );
		return LnwDefaultSpringStoredProcedure.createSpringStoredProcedure(stored, SpringUtils.getJdbcTemplate(dataSourceName) ).executeForLong();
	}

	/**
	 * Ejecuta un stored procedure y devuelve su resultado como Long
	 * @param stored
	 * @return
	 */
	public static Long excuteStoredProcedureForLong( LnwStoredProcedure stored ) {
		LOG.debug( "Executed STORED PROCEDURE: " + stored.getProcedure() );
		return LnwDefaultSpringStoredProcedure.createSpringStoredProcedure(stored, SpringUtils.getJdbcTemplate() ).executeForLong();
	}

	/**
	 * Ejecuta un stored procedure y devuelve su resultado como Integer, para un data source especificado
	 * @param stored
	 * @param dataSourceName
	 * @return
	 */
	public static Integer excuteStoredProcedureForInt( LnwStoredProcedure stored, String dataSourceName ) {
		LOG.debug( "Executed STORED PROCEDURE: " + stored.getProcedure() );
		return LnwDefaultSpringStoredProcedure.createSpringStoredProcedure(stored, SpringUtils.getJdbcTemplate(dataSourceName) ).executeForInt();
	}

	/**
	 * Ejecuta un stored procedure y devuelve su resultado como Integer
	 * @param stored
	 * @return
	 */
	public static Integer excuteStoredProcedureForInt( LnwStoredProcedure stored ) {
		LOG.debug( "Executed STORED PROCEDURE: " + stored.getProcedure() );
		return LnwDefaultSpringStoredProcedure.createSpringStoredProcedure(stored, SpringUtils.getJdbcTemplate() ).executeForInt();
	}

	/**
	 * Ejecuta un stored procedure y devuelve su resultado como Boolean, para un data source especificado
	 * @param stored
	 * @param dataSourceName
	 * @return
	 */
	public static Boolean excuteStoredProcedureForBoolean( LnwStoredProcedure stored, String dataSourceName ) {
		LOG.debug( "Executed STORED PROCEDURE: " + stored.getProcedure() );
		return LnwDefaultSpringStoredProcedure.createSpringStoredProcedure(stored, SpringUtils.getJdbcTemplate(dataSourceName) ).executeForBoolean();
	}

	/**
	 * Ejecuta un stored procedure y devuelve su resultado como Boolean
	 * @param stored
	 * @return
	 */
	public static Boolean excuteStoredProcedureForBoolean( LnwStoredProcedure stored ) {
		LOG.debug( "Executed STORED PROCEDURE: " + stored.getProcedure() );
		return LnwDefaultSpringStoredProcedure.createSpringStoredProcedure(stored, SpringUtils.getJdbcTemplate() ).executeForBoolean();
	}

	/**
	 * Ejecuta un stored procedure y devuelve su resultado como Float, para un data source especificado
	 * @param stored
	 * @param dataSourceName
	 * @return
	 */
	public static Float excuteStoredProcedureForFloat( LnwStoredProcedure stored, String dataSourceName ) {
		LOG.debug( "Executed STORED PROCEDURE: " + stored.getProcedure() );
		return LnwDefaultSpringStoredProcedure.createSpringStoredProcedure(stored, SpringUtils.getJdbcTemplate(dataSourceName) ).executeForFloat();
	}

	/**
	 * Ejecuta un stored procedure y devuelve su resultado como Float
	 * @param stored
	 * @return
	 */
	public static Float excuteStoredProcedureForFloat( LnwStoredProcedure stored ) {
		LOG.debug( "Executed STORED PROCEDURE: " + stored.getProcedure() );
		return LnwDefaultSpringStoredProcedure.createSpringStoredProcedure(stored, SpringUtils.getJdbcTemplate() ).executeForFloat();
	}

	/**
	 * Ejecuta un stored procedure y devuelve su resultado como Double, para un data source especificado
	 * @param stored
	 * @param dataSourceName
	 * @return
	 */
	public static Double excuteStoredProcedureForDouble( LnwStoredProcedure stored, String dataSourceName ) {
		LOG.debug( "Executed STORED PROCEDURE: " + stored.getProcedure() );
		return LnwDefaultSpringStoredProcedure.createSpringStoredProcedure(stored, SpringUtils.getJdbcTemplate(dataSourceName) ).executeForDouble();
	}

	/**
	 * Ejecuta un stored procedure y devuelve su resultado como Double
	 * @param stored
	 * @return
	 */
	public static Double excuteStoredProcedureForDouble( LnwStoredProcedure stored ) {
		LOG.debug( "Executed STORED PROCEDURE: " + stored.getProcedure() );
		return LnwDefaultSpringStoredProcedure.createSpringStoredProcedure(stored, SpringUtils.getJdbcTemplate() ).executeForDouble();
	}

	/**
	 * Ejecuta un stored procedure y devuelve su resultado como Date, para un data source especificado
	 * @param stored
	 * @param dataSourceName
	 * @return
	 */
	public static Date excuteStoredProcedureForDate( LnwStoredProcedure stored, String dataSourceName ) {
		LOG.debug( "Executed STORED PROCEDURE: " + stored.getProcedure() );
		return LnwDefaultSpringStoredProcedure.createSpringStoredProcedure(stored, SpringUtils.getJdbcTemplate(dataSourceName) ).executeForDate();
	}

	/**
	 * Ejecuta un stored procedure y devuelve su resultado como Date
	 * @param stored
	 * @return
	 */
	public static Date excuteStoredProcedureForDate( LnwStoredProcedure stored ) {
		LOG.debug( "Executed STORED PROCEDURE: " + stored.getProcedure() );
		return LnwDefaultSpringStoredProcedure.createSpringStoredProcedure(stored, SpringUtils.getJdbcTemplate() ).executeForDate();
	}

	/**
	 * Ejecuta un stored procedure que retorne un Cursor como último parámetro
	 * y devuelve su resultado como una Lista de CustomBeans, para un data source especificado
	 * @param stored
	 * @param dataSourceName
	 * @return
	 */
	public static List<CustomBean> excuteStoredProcedureForCursor( LnwStoredProcedure stored, String dataSourceName ) {
		LOG.debug( "Executed STORED PROCEDURE: " + stored.getProcedure() );
		return LnwDefaultSpringStoredProcedure.createSpringStoredProcedure(stored, SpringUtils.getJdbcTemplate(dataSourceName) ).executeForCursor();
	}

	/**
	 * Ejecuta un stored procedure que retorne un Cursor como último parámetro
	 * y devuelve su resultado como una Lista de CustomBeans
	 * @param stored
	 * @return
	 */
	public static List<CustomBean> excuteStoredProcedureForCursor( LnwStoredProcedure stored ) {
		LOG.debug( "Executed STORED PROCEDURE: " + stored.getProcedure() );
		return LnwDefaultSpringStoredProcedure.createSpringStoredProcedure(stored, SpringUtils.getJdbcTemplate() ).executeForCursor();
	}

	/**
	 * ###########################################################
	 * ################## FUNCTION METHODS #######################
	 * ###########################################################
	 */

	/**
	 * Ejecuta una funcion y devuelve su resultado como Object, para un data source especificado
	 * @param stored
	 * @param dataSourceName
	 * @return
	 */
	public static Object excuteFunction( LnwFunction stored, String dataSourceName ) {
		final PreparedStatement preparedStatement = stored.buildFunction();
		LOG.debug( preparedStatement.getSentencia() );
		logParams(preparedStatement.getParams());
		return SpringUtils.getJdbcTemplate(dataSourceName).queryForObject( preparedStatement.getSentencia(), preparedStatement.getParams(), Object.class );
	}

	/**
	 * Ejecuta una funcion y devuelve su resultado como Object
	 * @param stored
	 * @param dataSourceName
	 * @return
	 */
	public static Object excuteFunction( LnwFunction stored ) {
		final PreparedStatement preparedStatement = stored.buildFunction();
		LOG.debug( preparedStatement.getSentencia() );
		logParams(preparedStatement.getParams());
		return SpringUtils.getJdbcTemplate().queryForObject( preparedStatement.getSentencia(), preparedStatement.getParams(), Object.class );
	}

	/**
	 * Ejecuta una funcion y devuelve su resultado como String
	 * @param stored
	 * @return
	 */
	public static String excuteFunctionForString( LnwFunction stored, String dataSourceName ) {
		final PreparedStatement preparedStatement = stored.buildFunction();
		LOG.debug( preparedStatement.getSentencia() );
		logParams(preparedStatement.getParams());
		return (String)SpringUtils.getJdbcTemplate(dataSourceName).queryForObject( preparedStatement.getSentencia(), preparedStatement.getParams(), Object.class );
	}
	
	/**
	 * Ejecuta una funcion y devuelve su resultado como String
	 * @param stored
	 * @return
	 */
	public static String excuteFunctionForString( LnwFunction stored ) {
		final PreparedStatement preparedStatement = stored.buildFunction();
		LOG.debug( preparedStatement.getSentencia() );
		logParams(preparedStatement.getParams());
		return (String)SpringUtils.getJdbcTemplate().queryForObject( preparedStatement.getSentencia(), preparedStatement.getParams(), Object.class );
	}

	/**
	 * Ejecuta una funcion y devuelve su resultado como Long
	 * @param stored
	 * @return
	 */
	public static Long excuteFunctionForLong( LnwFunction stored ) {
		final PreparedStatement preparedStatement = stored.buildFunction();
		LOG.debug( preparedStatement.getSentencia() );
		logParams(preparedStatement.getParams());
		return SpringUtils.getJdbcTemplate().queryForObject( preparedStatement.getSentencia(), preparedStatement.getParams(),Long.class);
	}

	/**
	 * Ejecuta una funcion y devuelve su resultado como Integer
	 * @param stored
	 * @return
	 */
	public static Integer excuteFunctionForInt( LnwFunction stored ) {
		final PreparedStatement preparedStatement = stored.buildFunction();
		LOG.debug( preparedStatement.getSentencia() );
		logParams(preparedStatement.getParams());
		return SpringUtils.getJdbcTemplate().queryForObject( preparedStatement.getSentencia(), preparedStatement.getParams(),Integer.class );
	}

	/**
	 * Ejecuta una funcion y devuelve su resultado como Boolean
	 * @param stored
	 * @return
	 */
	public static Boolean excuteFunctionForBoolean( LnwFunction stored ) {
		final PreparedStatement preparedStatement = stored.buildFunction();
		LOG.debug( preparedStatement.getSentencia() );
		logParams(preparedStatement.getParams());
		return (Boolean)SpringUtils.getJdbcTemplate().queryForObject( preparedStatement.getSentencia(), preparedStatement.getParams(), Boolean.class );
	}




	/**
	 * ###########################################################
	 * ##################### QUERY METHODS #######################
	 * ###########################################################
	 */



	/**
	 * Realiza un query para buscar un único objeto
	 * @param query query ya parametrizado, pero sin llamar a <code>buildQuery()</code>
	 * @param mapper mapeador del resultado
	 * @return objeto encontrado
	 */
	public static Object queryForObject( LnwQuery query, RowMapper mapper ) {
		final PreparedStatement statement = query.buildQuery();
		LOG.debug( statement.getSentencia() );
		logParams(statement.getParams());
		try {
			return SpringUtils.getJdbcTemplate().queryForObject(statement.getSentencia(),
					statement.getParams(), mapper );
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}


	/**
	 * Realiza un query para buscar un único objeto
	 * @param query query ya parametrizado, pero sin llamar a <code>buildQuery()</code>
	 * @param dataSourceName nombre del data source
	 * @param mapper mapeador del resultado para un determinado dataSourceName
	 * @return objeto encontrado
	 */
	public static Object queryForObject( LnwQuery query, String dataSourceName,RowMapper mapper ) {
		final PreparedStatement statement = query.buildQuery();
		LOG.debug( statement.getSentencia() );
		logParams(statement.getParams());
		try {
		return SpringUtils.getJdbcTemplate(dataSourceName).queryForObject(statement.getSentencia(),
				statement.getParams(), mapper );
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/**
	 * Realiza un query para buscar una lista de resultados
	 * @param query query ya parametrizado, pero sin llamar a <code>buildQuery()</code>
	 * @param mapper mapeador del resultado
	 * @return lista de objetos encontrados
	 */
	public static List query( LnwQuery query, RowMapper mapper ) {
		final PreparedStatement statement = query.buildQuery();
		LOG.debug( statement.getSentencia() );
		logParams(statement.getParams());
		return SpringUtils.getJdbcTemplate().query(statement.getSentencia(),
				statement.getParams(), mapper );
	}

	/**
	 * Realiza un query para buscar una lista de resultados para un determinado dataSourceName
	 * @param query query ya parametrizado, pero sin llamar a <code>buildQuery()</code>
	 * @param dataSourceName nombre del data source
	 * @param mapper mapeador del resultado
	 * @return lista de objetos encontrados
	 */
	public static List query( LnwQuery query, String dataSourceName,RowMapper mapper ) {
		final PreparedStatement statement = query.buildQuery();
		LOG.debug( statement.getSentencia() );
		logParams(statement.getParams());
		return SpringUtils.getJdbcTemplate(dataSourceName).query(statement.getSentencia(),
				statement.getParams(), mapper );
	}

	public static Long queryForLong(LnwQuery query){
		final PreparedStatement statement = query.buildQuery();
		LOG.debug( statement.getSentencia() );
		logParams(statement.getParams());
		try {
			return SpringUtils.getJdbcTemplate().queryForObject(statement.getSentencia(),statement.getParams(),Long.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	public static Long queryForLong(LnwQuery query, String sauDataSource) {
		final PreparedStatement statement = query.buildQuery();
		LOG.debug( statement.getSentencia() );
		logParams(statement.getParams());
		try {
			return SpringUtils.getJdbcTemplate(sauDataSource).queryForObject(statement.getSentencia(),statement.getParams(),Long.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public static Integer queryForInt(LnwQuery query){
		final PreparedStatement statement = query.buildQuery();
		LOG.debug( statement.getSentencia() );
		logParams(statement.getParams());
		try {
			return SpringUtils.getJdbcTemplate().queryForObject(statement.getSentencia(),statement.getParams(),Integer.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	public static Integer queryForInt(LnwQuery query, String sauDataSource){
		final PreparedStatement statement = query.buildQuery();
		LOG.debug( statement.getSentencia() );
		logParams(statement.getParams());
		try {
			return SpringUtils.getJdbcTemplate(sauDataSource).queryForObject(statement.getSentencia(),statement.getParams(),Integer.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/**
	 * Realiza un query para buscar un único String
	 * @param query query ya parametrizado, pero sin llamar a <code>buildQuery()</code>
	 * @return String encontrado o null
	 */
	public static String queryForString( LnwQuery query ) {
		final PreparedStatement statement = query.buildQuery();
		LOG.debug( statement.getSentencia() );
		logParams(statement.getParams());
		try {
			return (String) SpringUtils.getJdbcTemplate().queryForObject(statement.getSentencia(),
					statement.getParams(), new RowMapper() {
						public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
							return rs.getString( 1 );
						}
			});
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/**
	 * Realiza un query para buscar un único String
	 * @param query query ya parametrizado, pero sin llamar a <code>buildQuery()</code>
	 * @param dataSourceName nombre del data source
	 * @return String encontrado o null
	 */
	public static String queryForString( LnwQuery query, String dataSourceName ) {
		final PreparedStatement statement = query.buildQuery();
		LOG.debug( statement.getSentencia() );
		logParams(statement.getParams());
		try {
			return (String) SpringUtils.getJdbcTemplate( dataSourceName ).queryForObject(statement.getSentencia(),
					statement.getParams(), new RowMapper() {
						public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
							return rs.getString( 1 );
						}
			});
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/**
	 * Realiza un query para buscar un único Boolean
	 * @param query query ya parametrizado, pero sin llamar a <code>buildQuery()</code>
	 * @return Boolean encontrado o null
	 */
	public static Boolean queryForBoolean( LnwQuery query ) {
		final PreparedStatement statement = query.buildQuery();
		LOG.debug( statement.getSentencia() );
		logParams(statement.getParams());
		try {
			return (Boolean) SpringUtils.getJdbcTemplate().queryForObject(statement.getSentencia(),
					statement.getParams(), new RowMapper() {
						public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
							return rs.getBoolean( 1 );
						}
			});
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/**
	 * Realiza un query para buscar un único Boolean
	 * @param query query ya parametrizado, pero sin llamar a <code>buildQuery()</code>
	 * @param dataSourceName nombre del data source
	 * @return Boolean encontrado o null
	 */
	public static Boolean queryForBoolean( LnwQuery query, String dataSourceName ) {
		final PreparedStatement statement = query.buildQuery();
		LOG.debug( statement.getSentencia() );
		logParams(statement.getParams());
		try {
			return (Boolean) SpringUtils.getJdbcTemplate( dataSourceName ).queryForObject(statement.getSentencia(),
					statement.getParams(), new RowMapper() {
						public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
							return rs.getBoolean( 1 );
						}
			});
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/**
	 * Realiza un query para buscar un único Object
	 * @param query query ya parametrizado, pero sin llamar a <code>buildQuery()</code>
	 * @return Object encontrado o null
	 */
	public static Object queryForObject( LnwQuery query ) {
		final PreparedStatement statement = query.buildQuery();
		LOG.debug( statement.getSentencia() );
		logParams(statement.getParams());
		try {
			return (Object) SpringUtils.getJdbcTemplate().queryForObject(statement.getSentencia(),
					statement.getParams(), new RowMapper() {
						public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
							return rs.getObject( 1 );
						}
			});
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/**
	 * Realiza un query para buscar un único Object
	 * @param query query ya parametrizado, pero sin llamar a <code>buildQuery()</code>
	 * @param dataSourceName nombre del data source
	 * @return Object encontrado o null
	 */
	public static Object queryForObject( LnwQuery query, String dataSourceName ) {
		final PreparedStatement statement = query.buildQuery();
		LOG.debug( statement.getSentencia() );
		logParams(statement.getParams());
		try {
			return (Object) SpringUtils.getJdbcTemplate( dataSourceName ).queryForObject(statement.getSentencia(),
					statement.getParams(), new RowMapper() {
						public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
							return rs.getObject( 1 );
						}
			});
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}


	/**
	 * Realiza un query para buscar un único objeto del tipo Custom Bean, utilizando
	 * un CustomBeanRowMapper para cargarlo
	 * @param query query ya parametrizado, pero sin llamar a <code>buildQuery()</code>
	 * @return custom bean encontrado
	 */
	public static CustomBean queryForCustomBean( LnwQuery query ) {
		return (CustomBean) queryForObject( query, customBeanRowMapper );
	}
	/**
	 * Realiza un query para buscar un único objeto del tipo Custom Bean, utilizando
	 * un CustomBeanRowMapper para cargarlo
	 * @param query query ya parametrizado, pero sin llamar a <code>buildQuery()</code>
	 * @return custom bean encontrado
	 */
	public static CustomBean queryForCustomBean( LnwQuery query,String dataSourceName ) {
		return (CustomBean) queryForObject( query,dataSourceName, customBeanRowMapper );
	}

	/**
	 * Realiza un query para buscar una lista de CustomBeans, utilizando
	 * un CustomBeanRowMapper para cargarlo
	 * @param query query ya parametrizado, pero sin llamar a <code>buildQuery()</code>
	 * @return custom bean encontrado
	 */
	public static List<CustomBean> queryForCustomBeanList( LnwQuery query ) {
		return query( query, customBeanRowMapper );
	}

	/**
	 * Realiza un query para buscar una lista de CustomBeans, utilizando
	 * un CustomBeanRowMapper para cargarlo
	 * @param query query ya parametrizado, pero sin llamar a <code>buildQuery()</code>
	 * @return custom bean encontrado
	 */
	public static List<CustomBean> queryForCustomBeanList( LnwQuery query, String dataSourceName ) {
		return query( query, dataSourceName, customBeanRowMapper );
	}

	public static Long getNextSequenceValue(String sequenceName){
		LnwQuery query=new LnwQueryOracle();
		query.addFrom("DUAL");
		query.addColumn(sequenceName + ".nextval","seqnum");
		return queryForLong(query);
	}



}
