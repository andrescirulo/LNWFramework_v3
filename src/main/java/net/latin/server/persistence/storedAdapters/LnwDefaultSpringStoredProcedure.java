package net.latin.server.persistence.storedAdapters;

import java.sql.Connection;
import java.sql.Types;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.latin.client.widget.base.CustomBean;
import net.latin.server.persistence.LnwPersistenceUtils;
import net.latin.server.persistence.LnwStoredProcedureJdbcTemplate;
import net.latin.server.persistence.sql.core.LnwCustomType;
import net.latin.server.persistence.sql.core.LnwSqlParameter;
import net.latin.server.persistence.sql.core.LnwStoredProcedure;
import net.latin.server.persistence.sql.core.LnwStoredProcedureParameter;
import net.latin.server.persistence.sql.oracle.LnwStoredProcedureOracle;
import net.latin.server.persistence.sql.sqlServer.LnwStoredProcedureSqlServer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.object.StoredProcedure;

/**
 * Adapter de LnwStoredProcedure para crear un StoredProcedure de Spring
 * y poder ejecutarlo. Maneja el mapeo de parámetros.
 * Extender esta clase si hace falta modificar cosas para bases de datos
 * específicas.
 *
 * @author Matias Leone
 */
public class LnwDefaultSpringStoredProcedure extends StoredProcedure {

	protected static final Log log = LogFactory.getLog(LnwPersistenceUtils.class);

	protected int nameIndex = 0;
	protected final HashMap<String, Object> springParams = new HashMap<String, Object>();
	protected String outKey;
	/**
	 * Connection que se va a utilizar para la ejecucion de este StoredProcedure
	 */
	protected Connection currentConnection;


	protected LnwDefaultSpringStoredProcedure(){
	}

	/**
	 * Crea una instancia de stored procedure de Spring de acuerdo al stored recibido,
	 * según cada tipo de base de datos soportado.
	 * Agregar nuevos IF según surgan nuevas implementaciones.
	 */
	public static LnwDefaultSpringStoredProcedure createSpringStoredProcedure(LnwStoredProcedure stored, JdbcTemplate jdbcTemplate) {
		//AGREGAR AQUI NUEVAS IMPLEMENTACIONES

		//ORACLE
		if(stored instanceof LnwStoredProcedureOracle) {
			return new LnwOracleSpringStoredProcedure(stored, jdbcTemplate);

		//SQL SERVER
		} else if(stored instanceof LnwStoredProcedureSqlServer){
			return new LnwSqlServerSpringStoredProcedure(stored, jdbcTemplate);

		//DEFAULT
		} else{
			return new LnwDefaultSpringStoredProcedure(stored, jdbcTemplate);
		}
	}



	public LnwDefaultSpringStoredProcedure( LnwStoredProcedure procedure, JdbcTemplate jdbcTemplate ) {
		//crear decorator de JDBC Template para tener una unica connection ya creada
		final LnwStoredProcedureJdbcTemplate spJdbcTemplate = new LnwStoredProcedureJdbcTemplate(jdbcTemplate);

		//pedir nueva connection
		currentConnection = spJdbcTemplate.createNewConnection();


		//cargar JdbcTemplate
		setJdbcTemplate(spJdbcTemplate);
		setFunction(false);
		setSql( procedure.getProcedure() );

		//cargar parametros y valores del stored
		final List<LnwStoredProcedureParameter> params = procedure.getValues();
		String key;
		for (LnwStoredProcedureParameter param : params) {
			key = nameIndex + "";

			if( param == null ) {
				//null
				prepararNull(key);
			} else if( param.isCollection() ) {
				//collecciones de tipos simples
				prepararColeccion(jdbcTemplate, key, param);
			} else if( param.isCustomType() ) {
				//custom type
				prepararCustomType(jdbcTemplate, key, param);
			} else if( param.isCustomTypeCollection() ) {
				//collecciones de custom type
				prepararCustomTypeColeccion(jdbcTemplate, key, param);
			} else {
				//tipo primitivo
				prepararTipoPrimitivo(key, param);
			}
			nameIndex++;
		}

		//guardamos la posible clave de resultado
		outKey = nameIndex + "";

	}


	/**
	 * Concatena los valores de un array para poder loguearlos
	 */
	protected String logArrayValues(Object[] array) {
		if(array.length == 0) return "Empty";

		final StringBuffer buffer = new StringBuffer();
		buffer.append( "[" );
		for (int i = 0; i < array.length; i++) {
			buffer.append( array[i] );
			buffer.append( ", " );
		}
		buffer.delete( buffer.length() - 2, buffer.length());
		buffer.append( "]" );
		return buffer.toString();
	}

	protected Map internalExecute() {
		compile();
		try {
			final Map result = execute( springParams );
			return result;
		}catch (Exception e) {
			throw new RuntimeException(e);
		}finally {
			//cerrar connection abierta para este stored
			((LnwStoredProcedureJdbcTemplate)this.getJdbcTemplate()).closeExistingConnection();
		}
	}

	public String executeForString() {
		declareParameter( new SqlOutParameter( outKey, getTypeForString() ) );
		return ((String) internalExecute().get( outKey )).trim();
	}
	public String executeForCompleteString() {
		declareParameter( new SqlOutParameter( outKey, getTypeForString() ) );
		return ((String) internalExecute().get( outKey ));
	}

	public Integer executeForInt() {
		declareParameter( new SqlOutParameter( outKey, getTypeForInt() ) );
		return ((Number) internalExecute().get( outKey )).intValue();
	}

	public Long executeForLong() {
		declareParameter( new SqlOutParameter( outKey, getTypeForLong() ) );
		return ((Number) internalExecute().get( outKey )).longValue();
	}

	public Float executeForFloat() {
		declareParameter( new SqlOutParameter( outKey, getTypeForFloat() ) );
		return ((Number) internalExecute().get( outKey )).floatValue();
	}
	public Double executeForDouble() {
		declareParameter( new SqlOutParameter( outKey, getTypeForDouble() ) );
		return ((Number) internalExecute().get( outKey )).doubleValue();
	}

	public Boolean executeForBoolean() {
		declareParameter( new SqlOutParameter( outKey, getTypeForBoolean()) );
		int intValue = ((Number) internalExecute().get( outKey )).intValue();
		return intValue != 0 ? true : false;
	}

	public Date executeForDate() {
		declareParameter( new SqlOutParameter( outKey, getTypeForDate() ) );
		return (Date) internalExecute().get( outKey );
	}

	public List<CustomBean> executeForCursor() {
		throw new RuntimeException("Metodo no implementado");
	}

	public void executeForVoid() {
		internalExecute();
	}


	protected void prepararNull(String key) {
		declareParameter( new SqlParameter( key, getTypeForUnknown() ) );
		springParams.put( key, null );
		log.debug("Param "+ key + ": " + "null");
	}

	/**
	 * Agrega un parametro del tipo Collection
	 */
	protected void prepararColeccion(JdbcTemplate jdbcTemplate, String key,
			LnwStoredProcedureParameter param) {
		declareParameter( new SqlParameter( key, Types.ARRAY, param.getCollectionType() ) );

		final Object[] array = ((Collection)param.getObject()).toArray();
		springParams.put( key, this.getPrimitiveArray(param.getCollectionType(),array,jdbcTemplate) );

		//log values
		log.debug("Param "+ key + ": " + logArrayValues(array ));
	}

	/**
	 * Metodo que retorna una instancia del tipo array primitiva de la base
	 */
	protected Object getPrimitiveArray(String typeName, Object[] array,JdbcTemplate jdbcTemplate){
		throw new RuntimeException("Metodo no implementado");
	}

	/**
	 * Agrega un parametro del tipo custom de la base de datos
	 */
	protected void prepararCustomType(JdbcTemplate jdbcTemplate, String key,
			LnwStoredProcedureParameter param) {
		final LnwCustomType customType = ((LnwCustomType)param.getObject());
		declareParameter( new SqlParameter( key, Types.STRUCT, customType.getObjectType() ) );

		springParams.put( key, this.getPrimitiveStruct(customType.getObjectType(),customType.getData(),jdbcTemplate) );

		//log values
		log.debug("Param "+ key + ": " + customType.getClass().getSimpleName() + " : " + customType.getObjectType().toUpperCase() + " : "+ logueoCustomType(customType) );
	}

	/**
	 * Loggear un CustomType
	 */
	private String logueoCustomType(LnwCustomType customType) {
		return  "(" + getDataDelCustomTypeForLogging(customType) + ")" ;
	}

	/**
	 * Obtener data del CustomType para loggear
	 */
	private String getDataDelCustomTypeForLogging(LnwCustomType customType) {
		String data = "";
		final List <LnwSqlParameter> dataCustomType = customType.getData();
		for (LnwSqlParameter dataC : dataCustomType) {
			if (dataC!=null && dataC.getData()!=null)
			{
				data += dataC.getData().toString() + ", ";
			}
		}
		return data.substring(0, data.lastIndexOf(", "));
	}

	/**
	 * Obtiene una structura JDBC para un CustomType
	 */
	protected Object getPrimitiveStruct(String objectType, List data, JdbcTemplate jdbcTemplate) {
		throw new RuntimeException("Metodo no implementado");
	}

	/**
	 * Agrega un parametro del tipo Collection custom de la base de datos
	 */
	protected void prepararCustomTypeColeccion(JdbcTemplate jdbcTemplate, String key, LnwStoredProcedureParameter param) {
		declareParameter( new SqlParameter( key, Types.ARRAY, param.getCollectionType() ) );

		final List collectionCustom = (List)param.getObject();
		final Object[] customTypeData = new Object[collectionCustom.size()];
		for (int i = 0; i < collectionCustom.size(); i++) {
			LnwCustomType object = (LnwCustomType)collectionCustom.get(i);
			customTypeData[i] = getPrimitiveStruct(object.getObjectType(), object.getData(), jdbcTemplate);
		}

		springParams.put( key, getPrimitiveArray(param.getCollectionType(),customTypeData,jdbcTemplate) );

		//log values
		log.debug("Param "+ key + ": LnwCustomTypeCollection : " + param.getCollectionType() + " : [ " + logueoCustomTypeCollection(collectionCustom) + " ]");
	}

	/**
	 * Loguear collection de CustomType
	 */
	private String logueoCustomTypeCollection(List collectionCustom) {
		final StringBuffer logueoCollection = new StringBuffer();
		for (Object object : collectionCustom) {
			logueoCollection.append( logueoCustomType((LnwCustomType)object) + ", " );
			logueoCollection.append( ", " );
		}
		String aux = logueoCollection.toString();

		return aux.substring(0, aux.lastIndexOf(", ")>0?aux.lastIndexOf(", "):0);
	}

	/**
	 * Agrega un parametro del tipo primitivo de la base de datos
	 */
	protected void prepararTipoPrimitivo(String key, LnwStoredProcedureParameter param) {
		declareParameter( new SqlParameter( key, getTypeForUnknown() ) );
		springParams.put( key, param.getObject() );

		//log values
		log.debug("Param "+ key + ": " + param.getObject());
	}

	protected int getTypeForString() {
		return Types.VARCHAR;
	}
	protected int getTypeForInt() {
		return Types.INTEGER;
	}
	protected int getTypeForLong() {
		return Types.BIGINT;
	}
	protected int getTypeForFloat() {
		return Types.FLOAT;
	}
	protected int getTypeForDouble() {
		return Types.DOUBLE;
	}
	protected int getTypeForBoolean() {
		return Types.BOOLEAN;
	}
	protected int getTypeForDate() {
		return Types.DATE;
	}
	protected int getTypeForUnknown() {
		return SqlTypeValue.TYPE_UNKNOWN;
	}

}
