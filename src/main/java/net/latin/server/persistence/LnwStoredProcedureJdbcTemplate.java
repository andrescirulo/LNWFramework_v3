package net.latin.server.persistence;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 * Decorator de JdbcTemplate que contiene un Decorator de DataSource que permite
 * crear una una connection y mantenerla abierta hasta que se pida cerrar explicitamente.
 * Es útil a la hora de ejecutar StoredProcedures que requieren un connection para
 * obtener la metadata de parametros complejos, como ser ARRAY o STRUCT
 *
 * @author Matias Leone
 */
public class LnwStoredProcedureJdbcTemplate extends JdbcTemplate {

	private JdbcTemplate decoretedJdbcT;
	private OneConnectionDataSource oneConnectionDS;
	
	private Log LOG=LogFactory.getLog(LnwStoredProcedureJdbcTemplate.class);

	public LnwStoredProcedureJdbcTemplate( JdbcTemplate jdbcTemplate ) {
		decoretedJdbcT = jdbcTemplate;
		oneConnectionDS = new OneConnectionDataSource(jdbcTemplate.getDataSource());
	}

	/**
	 * Crea una nueva connection que se mantiene abierta hasta que se
	 * pida cerrar explicitamente con createNewConnection()
	 */
	public Connection createNewConnection() {
		return oneConnectionDS.createNewConnection();
	}

	/**
	 * Cierra connection que se habia creado previamente con createNewConnection()
	 */
	public void closeExistingConnection() {
		oneConnectionDS.closeExistingConnection();
	}

	/**
	 * No devuelve el DataSource original, sino uno que crea y mantiene
	 * una única Connection: OneConnectionDataSource
	 */
	public DataSource getDataSource() {
		return decoretedJdbcT.getDataSource();
	}

	/**
	 * Solo crea una connection y la mantiene, en vez de pedir una nueva
	 * cuando le hagan getConnection()
	 *
	 * @author Matias Leone
	 */
	private class OneConnectionDataSource implements DataSource {

		private DataSource decoretedDS;
		private Connection currentConnection;

		public OneConnectionDataSource( DataSource dataSource ) {
			this.decoretedDS =  dataSource;
		}

		/**
		 * Crea una nueva connection que se mantiene abierta hasta que se
		 * pida cerrar explicitamente
		 */
		public Connection createNewConnection() {
			try {
				currentConnection = decoretedDS.getConnection();
				return currentConnection;
			} catch (SQLException e) {
				throw new RuntimeException( "Error al crear una connection desde OneConnectionDataSource", e );
			}
		}

		/**
		 * No pude una nueva connection, sino que devuelve la connection ya creada
		 */
		public Connection getConnection() throws SQLException {
			if(currentConnection == null) throw new RuntimeException( "No se llamó a createNewConnection() antes de pedir una connection en OneConnectionDataSource");
			return currentConnection;
		}

		/**
		 * Cierra connection que se habia creado previamente con createNewConnection()
		 */
		public void closeExistingConnection()
		{
			if(currentConnection == null) throw new RuntimeException( "No se llamó a createNewConnection() antes de pedir una connection en OneConnectionDataSource");
			try {
				currentConnection.close();
				currentConnection = null;
			} catch (SQLException e) {
				throw new RuntimeException( "Error al cerrar una connection desde OneConnectionDataSource", e );
			}
		}



		/**
		 * ##################################
		 * DELEGATES METHODS DE DATA_SOURCE
		 * ##################################
		 */

		/**
		 * @param username
		 * @param password
		 * @return
		 * @throws SQLException
		 * @see javax.sql.DataSource#getConnection(java.lang.String, java.lang.String)
		 */
		public Connection getConnection(String username, String password)
				throws SQLException {
			return decoretedDS.getConnection(username, password);
		}

		/**
		 * @return
		 * @throws SQLException
		 * @see javax.sql.CommonDataSource#getLoginTimeout()
		 */
		public int getLoginTimeout() throws SQLException {
			return decoretedDS.getLoginTimeout();
		}

		/**
		 * @return
		 * @throws SQLException
		 * @see javax.sql.CommonDataSource#getLogWriter()
		 */
		public PrintWriter getLogWriter() throws SQLException {
			return decoretedDS.getLogWriter();
		}

		/**
		 * @param iface
		 * @return
		 * @throws SQLException
		 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
		 */
		public boolean isWrapperFor(Class<?> iface) throws SQLException {
			return decoretedDS.isWrapperFor(iface);
		}

		/**
		 * @param seconds
		 * @throws SQLException
		 * @see javax.sql.CommonDataSource#setLoginTimeout(int)
		 */
		public void setLoginTimeout(int seconds) throws SQLException {
			decoretedDS.setLoginTimeout(seconds);
		}

		/**
		 * @param out
		 * @throws SQLException
		 * @see javax.sql.CommonDataSource#setLogWriter(java.io.PrintWriter)
		 */
		public void setLogWriter(PrintWriter out) throws SQLException {
			decoretedDS.setLogWriter(out);
		}

		/**
		 * @param <T>
		 * @param iface
		 * @return
		 * @throws SQLException
		 * @see java.sql.Wrapper#unwrap(java.lang.Class)
		 */
		public <T> T unwrap(Class<T> iface) throws SQLException {
			return decoretedDS.unwrap(iface);
		}

		public Logger getParentLogger() throws SQLFeatureNotSupportedException {
			return Logger.getAnonymousLogger();
		}

	}










	/**
	 * ##################################
	 * DELEGATES METHODS DE JDBC_TEMPLATE
	 * ##################################
	 */

	/**
	 *
	 * @see org.springframework.jdbc.support.JdbcAccessor#afterPropertiesSet()
	 */
	public void afterPropertiesSet() {
		decoretedJdbcT.afterPropertiesSet();
	}

	/**
	 * @param sql
	 * @param pss
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#batchUpdate(java.lang.String, org.springframework.jdbc.core.BatchPreparedStatementSetter)
	 */
	public int[] batchUpdate(String sql, BatchPreparedStatementSetter pss)
			throws DataAccessException {
		return decoretedJdbcT.batchUpdate(sql, pss);
	}

	/**
	 * @param sql
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#batchUpdate(java.lang.String[])
	 */
	public int[] batchUpdate(String[] sql) throws DataAccessException {
		return decoretedJdbcT.batchUpdate(sql);
	}

	/**
	 * @param csc
	 * @param declaredParameters
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#call(org.springframework.jdbc.core.CallableStatementCreator, java.util.List)
	 */
	public Map call(CallableStatementCreator csc, List declaredParameters)
			throws DataAccessException {
		return decoretedJdbcT.call(csc, declaredParameters);
	}

	/**
	 * @param obj
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		return decoretedJdbcT.equals(obj);
	}

	/**
	 * @param csc
	 * @param action
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#execute(org.springframework.jdbc.core.CallableStatementCreator, org.springframework.jdbc.core.CallableStatementCallback)
	 */
	public Object execute(CallableStatementCreator csc,
			CallableStatementCallback action) throws DataAccessException {
		return decoretedJdbcT.execute(csc, action);
	}

	/**
	 * @param action
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#execute(org.springframework.jdbc.core.ConnectionCallback)
	 */
	public Object execute(ConnectionCallback action) throws DataAccessException {
		return decoretedJdbcT.execute(action);
	}

	/**
	 * @param psc
	 * @param action
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#execute(org.springframework.jdbc.core.PreparedStatementCreator, org.springframework.jdbc.core.PreparedStatementCallback)
	 */
	public Object execute(PreparedStatementCreator psc,
			PreparedStatementCallback action) throws DataAccessException {
		return decoretedJdbcT.execute(psc, action);
	}

	/**
	 * @param action
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#execute(org.springframework.jdbc.core.StatementCallback)
	 */
	public Object execute(StatementCallback action) throws DataAccessException {
		return decoretedJdbcT.execute(action);
	}

	/**
	 * @param callString
	 * @param action
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#execute(java.lang.String, org.springframework.jdbc.core.CallableStatementCallback)
	 */
	public Object execute(String callString, CallableStatementCallback action)
			throws DataAccessException {
		return decoretedJdbcT.execute(callString, action);
	}

	/**
	 * @param sql
	 * @param action
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#execute(java.lang.String, org.springframework.jdbc.core.PreparedStatementCallback)
	 */
	public Object execute(String sql, PreparedStatementCallback action)
			throws DataAccessException {
		return decoretedJdbcT.execute(sql, action);
	}

	/**
	 * @param sql
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#execute(java.lang.String)
	 */
	public void execute(String sql) throws DataAccessException {
		decoretedJdbcT.execute(sql);
	}

	/**
	 * @return
	 * @see org.springframework.jdbc.support.JdbcAccessor#getExceptionTranslator()
	 */
	public SQLExceptionTranslator getExceptionTranslator() {
		return decoretedJdbcT.getExceptionTranslator();
	}

	/**
	 * @return
	 * @see org.springframework.jdbc.core.JdbcTemplate#getFetchSize()
	 */
	public int getFetchSize() {
		return decoretedJdbcT.getFetchSize();
	}

	/**
	 * @return
	 * @see org.springframework.jdbc.core.JdbcTemplate#getMaxRows()
	 */
	public int getMaxRows() {
		return decoretedJdbcT.getMaxRows();
	}

	/**
	 * @return
	 * @see org.springframework.jdbc.core.JdbcTemplate#getNativeJdbcExtractor()
	 */
	public NativeJdbcExtractor getNativeJdbcExtractor() {
		return decoretedJdbcT.getNativeJdbcExtractor();
	}

	/**
	 * @return
	 * @see org.springframework.jdbc.core.JdbcTemplate#getQueryTimeout()
	 */
	public int getQueryTimeout() {
		return decoretedJdbcT.getQueryTimeout();
	}

	/**
	 * @return
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return decoretedJdbcT.hashCode();
	}

	/**
	 * @return
	 * @see org.springframework.jdbc.core.JdbcTemplate#isIgnoreWarnings()
	 */
	public boolean isIgnoreWarnings() {
		return decoretedJdbcT.isIgnoreWarnings();
	}

	/**
	 * @return
	 * @see org.springframework.jdbc.support.JdbcAccessor#isLazyInit()
	 */
	public boolean isLazyInit() {
		return decoretedJdbcT.isLazyInit();
	}

	/**
	 * @return
	 * @see org.springframework.jdbc.core.JdbcTemplate#isSkipResultsProcessing()
	 */
	public boolean isSkipResultsProcessing() {
		return decoretedJdbcT.isSkipResultsProcessing();
	}

	/**
	 * @param psc
	 * @param pss
	 * @param rse
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(org.springframework.jdbc.core.PreparedStatementCreator, org.springframework.jdbc.core.PreparedStatementSetter, org.springframework.jdbc.core.ResultSetExtractor)
	 */
	public Object query(PreparedStatementCreator psc,
			PreparedStatementSetter pss, ResultSetExtractor rse)
			throws DataAccessException {
		return decoretedJdbcT.query(psc, pss, rse);
	}

	/**
	 * @param psc
	 * @param rse
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(org.springframework.jdbc.core.PreparedStatementCreator, org.springframework.jdbc.core.ResultSetExtractor)
	 */
	public Object query(PreparedStatementCreator psc, ResultSetExtractor rse)
			throws DataAccessException {
		return decoretedJdbcT.query(psc, rse);
	}

	/**
	 * @param psc
	 * @param rch
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(org.springframework.jdbc.core.PreparedStatementCreator, org.springframework.jdbc.core.RowCallbackHandler)
	 */
	public void query(PreparedStatementCreator psc, RowCallbackHandler rch)
			throws DataAccessException {
		decoretedJdbcT.query(psc, rch);
	}

	/**
	 * @param psc
	 * @param rowMapper
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(org.springframework.jdbc.core.PreparedStatementCreator, org.springframework.jdbc.core.RowMapper)
	 */
	public List query(PreparedStatementCreator psc, RowMapper rowMapper)
			throws DataAccessException {
		return decoretedJdbcT.query(psc, rowMapper);
	}

	/**
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @param rse
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, java.lang.Object[], int[], org.springframework.jdbc.core.ResultSetExtractor)
	 */
	public Object query(String sql, Object[] args, int[] argTypes,
			ResultSetExtractor rse) throws DataAccessException {
		return decoretedJdbcT.query(sql, args, argTypes, rse);
	}

	/**
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @param rch
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, java.lang.Object[], int[], org.springframework.jdbc.core.RowCallbackHandler)
	 */
	public void query(String sql, Object[] args, int[] argTypes,
			RowCallbackHandler rch) throws DataAccessException {
		decoretedJdbcT.query(sql, args, argTypes, rch);
	}

	/**
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @param rowMapper
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, java.lang.Object[], int[], org.springframework.jdbc.core.RowMapper)
	 */
	public List query(String sql, Object[] args, int[] argTypes,
			RowMapper rowMapper) throws DataAccessException {
		return decoretedJdbcT.query(sql, args, argTypes, rowMapper);
	}

	/**
	 * @param sql
	 * @param args
	 * @param rse
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, java.lang.Object[], org.springframework.jdbc.core.ResultSetExtractor)
	 */
	public Object query(String sql, Object[] args, ResultSetExtractor rse)
			throws DataAccessException {
		return decoretedJdbcT.query(sql, args, rse);
	}

	/**
	 * @param sql
	 * @param args
	 * @param rch
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, java.lang.Object[], org.springframework.jdbc.core.RowCallbackHandler)
	 */
	public void query(String sql, Object[] args, RowCallbackHandler rch)
			throws DataAccessException {
		decoretedJdbcT.query(sql, args, rch);
	}

	/**
	 * @param sql
	 * @param args
	 * @param rowMapper
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, java.lang.Object[], org.springframework.jdbc.core.RowMapper)
	 */
	public List query(String sql, Object[] args, RowMapper rowMapper)
			throws DataAccessException {
		return decoretedJdbcT.query(sql, args, rowMapper);
	}

	/**
	 * @param sql
	 * @param pss
	 * @param rse
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, org.springframework.jdbc.core.PreparedStatementSetter, org.springframework.jdbc.core.ResultSetExtractor)
	 */
	public Object query(String sql, PreparedStatementSetter pss,
			ResultSetExtractor rse) throws DataAccessException {
		return decoretedJdbcT.query(sql, pss, rse);
	}

	/**
	 * @param sql
	 * @param pss
	 * @param rch
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, org.springframework.jdbc.core.PreparedStatementSetter, org.springframework.jdbc.core.RowCallbackHandler)
	 */
	public void query(String sql, PreparedStatementSetter pss,
			RowCallbackHandler rch) throws DataAccessException {
		decoretedJdbcT.query(sql, pss, rch);
	}

	/**
	 * @param sql
	 * @param pss
	 * @param rowMapper
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, org.springframework.jdbc.core.PreparedStatementSetter, org.springframework.jdbc.core.RowMapper)
	 */
	public List query(String sql, PreparedStatementSetter pss,
			RowMapper rowMapper) throws DataAccessException {
		return decoretedJdbcT.query(sql, pss, rowMapper);
	}

	/**
	 * @param sql
	 * @param rse
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, org.springframework.jdbc.core.ResultSetExtractor)
	 */
	public Object query(String sql, ResultSetExtractor rse)
			throws DataAccessException {
		return decoretedJdbcT.query(sql, rse);
	}

	/**
	 * @param sql
	 * @param rch
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, org.springframework.jdbc.core.RowCallbackHandler)
	 */
	public void query(String sql, RowCallbackHandler rch)
			throws DataAccessException {
		decoretedJdbcT.query(sql, rch);
	}

	/**
	 * @param sql
	 * @param rowMapper
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#query(java.lang.String, org.springframework.jdbc.core.RowMapper)
	 */
	public List query(String sql, RowMapper rowMapper)
			throws DataAccessException {
		return decoretedJdbcT.query(sql, rowMapper);
	}

	/**
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForInt(java.lang.String, java.lang.Object[], int[])
	 */
	public Integer queryForInt(String sql, Object[] args, int[] argTypes)
			throws DataAccessException {
		return decoretedJdbcT.queryForObject(sql, args, argTypes,Integer.class);
	}

	/**
	 * @param sql
	 * @param args
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForInt(java.lang.String, java.lang.Object[])
	 */
	public Integer queryForInt(String sql, Object[] args)
			throws DataAccessException {
		return decoretedJdbcT.queryForObject(sql, args, Integer.class);
	}

	/**
	 * @param sql
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForInt(java.lang.String)
	 */
	public Integer queryForInt(String sql) throws DataAccessException {
		return decoretedJdbcT.queryForObject(sql,Integer.class);
	}

	/**
	 * @param sql
	 * @param elementType
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForList(java.lang.String, java.lang.Class)
	 */
	public List queryForList(String sql, Class elementType)
			throws DataAccessException {
		return decoretedJdbcT.queryForList(sql, elementType);
	}

	/**
	 * @param sql
	 * @param args
	 * @param elementType
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForList(java.lang.String, java.lang.Object[], java.lang.Class)
	 */
	public List queryForList(String sql, Object[] args, Class elementType)
			throws DataAccessException {
		return decoretedJdbcT.queryForList(sql, args, elementType);
	}

	/**
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @param elementType
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForList(java.lang.String, java.lang.Object[], int[], java.lang.Class)
	 */
	public List queryForList(String sql, Object[] args, int[] argTypes,
			Class elementType) throws DataAccessException {
		return decoretedJdbcT.queryForList(sql, args, argTypes, elementType);
	}

	/**
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForList(java.lang.String, java.lang.Object[], int[])
	 */
	public List queryForList(String sql, Object[] args, int[] argTypes)
			throws DataAccessException {
		return decoretedJdbcT.queryForList(sql, args, argTypes);
	}

	/**
	 * @param sql
	 * @param args
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForList(java.lang.String, java.lang.Object[])
	 */
	public List queryForList(String sql, Object[] args)
			throws DataAccessException {
		return decoretedJdbcT.queryForList(sql, args);
	}

	/**
	 * @param sql
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForList(java.lang.String)
	 */
	public List queryForList(String sql) throws DataAccessException {
		return decoretedJdbcT.queryForList(sql);
	}

	/**
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForLong(java.lang.String, java.lang.Object[], int[])
	 */
	public Long queryForLong(String sql, Object[] args, int[] argTypes)
			throws DataAccessException {
		return decoretedJdbcT.queryForObject(sql,args,argTypes,Long.class);
	}

	/**
	 * @param sql
	 * @param args
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForLong(java.lang.String, java.lang.Object[])
	 */
	public Long queryForLong(String sql, Object[] args)
			throws DataAccessException {
		return decoretedJdbcT.queryForObject(sql,args,Long.class);
	}

	/**
	 * @param sql
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForLong(java.lang.String)
	 */
	public Long queryForLong(String sql) throws DataAccessException {
		return decoretedJdbcT.queryForObject(sql,Long.class);
	}

	/**
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForMap(java.lang.String, java.lang.Object[], int[])
	 */
	public Map queryForMap(String sql, Object[] args, int[] argTypes)
			throws DataAccessException {
		return decoretedJdbcT.queryForMap(sql, args, argTypes);
	}

	/**
	 * @param sql
	 * @param args
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForMap(java.lang.String, java.lang.Object[])
	 */
	public Map queryForMap(String sql, Object[] args)
			throws DataAccessException {
		return decoretedJdbcT.queryForMap(sql, args);
	}

	/**
	 * @param sql
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForMap(java.lang.String)
	 */
	public Map queryForMap(String sql) throws DataAccessException {
		return decoretedJdbcT.queryForMap(sql);
	}

	/**
	 * @param sql
	 * @param requiredType
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForObject(java.lang.String, java.lang.Class)
	 */
	public Object queryForObject(String sql, Class requiredType)
			throws DataAccessException {
		return decoretedJdbcT.queryForObject(sql, requiredType);
	}

	/**
	 * @param sql
	 * @param args
	 * @param requiredType
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForObject(java.lang.String, java.lang.Object[], java.lang.Class)
	 */
	public Object queryForObject(String sql, Object[] args, Class requiredType)
			throws DataAccessException {
		return decoretedJdbcT.queryForObject(sql, args, requiredType);
	}

	/**
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @param requiredType
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForObject(java.lang.String, java.lang.Object[], int[], java.lang.Class)
	 */
	public Object queryForObject(String sql, Object[] args, int[] argTypes,
			Class requiredType) throws DataAccessException {
		return decoretedJdbcT.queryForObject(sql, args, argTypes, requiredType);
	}

	/**
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @param rowMapper
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForObject(java.lang.String, java.lang.Object[], int[], org.springframework.jdbc.core.RowMapper)
	 */
	public Object queryForObject(String sql, Object[] args, int[] argTypes,
			RowMapper rowMapper) throws DataAccessException {
		return decoretedJdbcT.queryForObject(sql, args, argTypes, rowMapper);
	}

	/**
	 * @param sql
	 * @param args
	 * @param rowMapper
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForObject(java.lang.String, java.lang.Object[], org.springframework.jdbc.core.RowMapper)
	 */
	public Object queryForObject(String sql, Object[] args, RowMapper rowMapper)
			throws DataAccessException {
		return decoretedJdbcT.queryForObject(sql, args, rowMapper);
	}

	/**
	 * @param sql
	 * @param rowMapper
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForObject(java.lang.String, org.springframework.jdbc.core.RowMapper)
	 */
	public Object queryForObject(String sql, RowMapper rowMapper)
			throws DataAccessException {
		return decoretedJdbcT.queryForObject(sql, rowMapper);
	}

	/**
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForRowSet(java.lang.String, java.lang.Object[], int[])
	 */
	public SqlRowSet queryForRowSet(String sql, Object[] args, int[] argTypes)
			throws DataAccessException {
		return decoretedJdbcT.queryForRowSet(sql, args, argTypes);
	}

	/**
	 * @param sql
	 * @param args
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForRowSet(java.lang.String, java.lang.Object[])
	 */
	public SqlRowSet queryForRowSet(String sql, Object[] args)
			throws DataAccessException {
		return decoretedJdbcT.queryForRowSet(sql, args);
	}

	/**
	 * @param sql
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#queryForRowSet(java.lang.String)
	 */
	public SqlRowSet queryForRowSet(String sql) throws DataAccessException {
		return decoretedJdbcT.queryForRowSet(sql);
	}

	/**
	 * @param dbName
	 * @see org.springframework.jdbc.support.JdbcAccessor#setDatabaseProductName(java.lang.String)
	 */
	public void setDatabaseProductName(String dbName) {
		decoretedJdbcT.setDatabaseProductName(dbName);
	}

	/**
	 * @param dataSource
	 * @see org.springframework.jdbc.support.JdbcAccessor#setDataSource(javax.sql.DataSource)
	 */
	public void setDataSource(DataSource dataSource) {
		decoretedJdbcT.setDataSource(dataSource);
	}

	/**
	 * @param exceptionTranslator
	 * @see org.springframework.jdbc.support.JdbcAccessor#setExceptionTranslator(org.springframework.jdbc.support.SQLExceptionTranslator)
	 */
	public void setExceptionTranslator(
			SQLExceptionTranslator exceptionTranslator) {
		decoretedJdbcT.setExceptionTranslator(exceptionTranslator);
	}

	/**
	 * @param fetchSize
	 * @see org.springframework.jdbc.core.JdbcTemplate#setFetchSize(int)
	 */
	public void setFetchSize(int fetchSize) {
		decoretedJdbcT.setFetchSize(fetchSize);
	}

	/**
	 * @param ignoreWarnings
	 * @see org.springframework.jdbc.core.JdbcTemplate#setIgnoreWarnings(boolean)
	 */
	public void setIgnoreWarnings(boolean ignoreWarnings) {
		decoretedJdbcT.setIgnoreWarnings(ignoreWarnings);
	}

	/**
	 * @param lazyInit
	 * @see org.springframework.jdbc.support.JdbcAccessor#setLazyInit(boolean)
	 */
	public void setLazyInit(boolean lazyInit) {
		decoretedJdbcT.setLazyInit(lazyInit);
	}

	/**
	 * @param maxRows
	 * @see org.springframework.jdbc.core.JdbcTemplate#setMaxRows(int)
	 */
	public void setMaxRows(int maxRows) {
		decoretedJdbcT.setMaxRows(maxRows);
	}

	/**
	 * @param extractor
	 * @see org.springframework.jdbc.core.JdbcTemplate#setNativeJdbcExtractor(org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor)
	 */
	public void setNativeJdbcExtractor(NativeJdbcExtractor extractor) {
		decoretedJdbcT.setNativeJdbcExtractor(extractor);
	}

	/**
	 * @param queryTimeout
	 * @see org.springframework.jdbc.core.JdbcTemplate#setQueryTimeout(int)
	 */
	public void setQueryTimeout(int queryTimeout) {
		decoretedJdbcT.setQueryTimeout(queryTimeout);
	}

	/**
	 * @param skipResultsProcessing
	 * @see org.springframework.jdbc.core.JdbcTemplate#setSkipResultsProcessing(boolean)
	 */
	public void setSkipResultsProcessing(boolean skipResultsProcessing) {
		decoretedJdbcT.setSkipResultsProcessing(skipResultsProcessing);
	}

	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return decoretedJdbcT.toString();
	}

	/**
	 * @param psc
	 * @param generatedKeyHolder
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#update(org.springframework.jdbc.core.PreparedStatementCreator, org.springframework.jdbc.support.KeyHolder)
	 */
	public int update(PreparedStatementCreator psc, KeyHolder generatedKeyHolder)
			throws DataAccessException {
		return decoretedJdbcT.update(psc, generatedKeyHolder);
	}

	/**
	 * @param psc
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#update(org.springframework.jdbc.core.PreparedStatementCreator)
	 */
	public int update(PreparedStatementCreator psc) throws DataAccessException {
		return decoretedJdbcT.update(psc);
	}

	/**
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#update(java.lang.String, java.lang.Object[], int[])
	 */
	public int update(String sql, Object[] args, int[] argTypes)
			throws DataAccessException {
		return decoretedJdbcT.update(sql, args, argTypes);
	}

	/**
	 * @param sql
	 * @param args
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#update(java.lang.String, java.lang.Object[])
	 */
	public int update(String sql, Object[] args) throws DataAccessException {
		return decoretedJdbcT.update(sql, args);
	}

	/**
	 * @param sql
	 * @param pss
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#update(java.lang.String, org.springframework.jdbc.core.PreparedStatementSetter)
	 */
	public int update(String sql, PreparedStatementSetter pss)
			throws DataAccessException {
		return decoretedJdbcT.update(sql, pss);
	}

	/**
	 * @param sql
	 * @return
	 * @throws DataAccessException
	 * @see org.springframework.jdbc.core.JdbcTemplate#update(java.lang.String)
	 */
	public int update(String sql) throws DataAccessException {
		return decoretedJdbcT.update(sql);
	}



}
