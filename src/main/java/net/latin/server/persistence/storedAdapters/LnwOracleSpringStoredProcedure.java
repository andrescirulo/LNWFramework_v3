package net.latin.server.persistence.storedAdapters;

import java.sql.Connection;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;

import net.latin.client.widget.base.CustomBean;
import net.latin.server.persistence.LnwPersistenceUtils;
import net.latin.server.persistence.sql.core.LnwSqlParameter;
import net.latin.server.persistence.sql.core.LnwStoredProcedure;
import oracle.jdbc.internal.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

/**
 * Implementacion de StoredProcedure para Oracle 10G
 *
 * @author Matias Leone
 */
public class LnwOracleSpringStoredProcedure extends LnwDefaultSpringStoredProcedure {

	public LnwOracleSpringStoredProcedure(LnwStoredProcedure procedure,
			JdbcTemplate jdbcTemplate) {
		super(procedure, jdbcTemplate);
	}

	/**
	 * Implementacion de ORACLE para tipos ARRAY (NESTED TABLE Y VARRAY)
	 */
	@Override
	protected Object getPrimitiveArray(String typeName, Object[] array, JdbcTemplate jdbcTemplate ){
		try {
			//obtener la conection nativa de oracle
			Connection nativeConnection = jdbcTemplate.getNativeJdbcExtractor().getNativeConnection(currentConnection);

			//crear ARRAY con connection nativa
			ArrayDescriptor desc = new ArrayDescriptor(typeName, nativeConnection);
			ARRAY arrayOracle = new ARRAY(desc, nativeConnection, array);

			return arrayOracle;

		} catch (Exception e) {
			throw new RuntimeException("ERROR al crear el array primitivo de oracle con tipo: " + typeName, e );
		}
	}

	/**
	 * Implementación de Oracle para tipos compuestos
	 */
	@Override
	protected Object getPrimitiveStruct(String objectType, List data, JdbcTemplate jdbcTemplate) {
		try {
			//obtener la conection nativa de oracle
			Connection nativeConnection = jdbcTemplate.getNativeJdbcExtractor().getNativeConnection(currentConnection);

			//crear STRUCT con connection nativa
			StructDescriptor structDesc = StructDescriptor.createDescriptor(objectType, nativeConnection);
			Object[] customTypeData = new Object[data.size()];
			for (int i = 0; i < data.size(); i++) {
				customTypeData[i] = ((LnwSqlParameter)data.get(i)).getData();
			}
			STRUCT structOracle = new STRUCT(structDesc, nativeConnection, customTypeData);

			return structOracle;

		} catch (Exception e) {
			throw new RuntimeException("ERROR al crear el STRUCT primitivo de oracle con tipo: " + objectType, e );
		}
	}

	/**
	 * Implementación de oracle para ejecutar un Stored Procedure y devolver un Cursor
	 */
	@Override
	public List<CustomBean> executeForCursor() {
		declareParameter( new SqlOutParameter( outKey, OracleTypes.CURSOR, LnwPersistenceUtils.customBeanRowMapper ) );
		return (List<CustomBean>) internalExecute().get( outKey );
	}


	protected int getTypeForLong() {
		return OracleTypes.NUMBER;
	}

	protected int getTypeForBoolean() {
		return OracleTypes.INTEGER;
	}
}
