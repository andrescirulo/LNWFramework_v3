package net.latin.server.persistence.sql.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import net.latin.server.persistence.sql.core.exceptions.LnwSqlBadStatementException;

/**
 * Wrapper para guardar varios tipos distintos de parametros
 * @author Matias Leone
 */
public class LnwStoredProcedureParameter implements IsSerializable {

	private Collection<LnwSqlParameter> collection = null;
	private List<LnwCustomType> customTypeList = null;
	private LnwSqlParameter parameter = null;
	private LnwCustomType customType = null;
	private String collectionType = null;

	public LnwStoredProcedureParameter() {
	}

	public LnwStoredProcedureParameter(Object object) {
		if(object instanceof Collection) {
			collection = LnwSqlParameter.adaptData((Collection<Object>)object);
		} else if(object instanceof LnwCustomType) {
			customType = (LnwCustomType) object;
		} else {
			parameter = LnwSqlParameter.adaptData(object);
		}
	}

	/**
	 * Crea un parametro que representa una lista de parametros simples
	 * @param collection
	 * @param collectionType
	 * @return
	 */
	public static LnwStoredProcedureParameter buildForArray(Collection collection,String collectionType) {
		LnwStoredProcedureParameter param = new LnwStoredProcedureParameter(collection);
		param.collectionType = collectionType.toUpperCase();
		return param;
	}

	/**
	 * Crea un parametro que representa una lista de Custom Types
	 * @param collection
	 * @param collectionType
	 * @return
	 */
	public static LnwStoredProcedureParameter buildForCustomTypeList( Collection collection, String collectionType ) {
		LnwStoredProcedureParameter param = new LnwStoredProcedureParameter();
		param.customTypeList = new ArrayList<LnwCustomType>();
		param.collectionType = collectionType.toUpperCase();
		for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
			LnwCustomType customType = (LnwCustomType) iterator.next();
			if( customType == null ) {
				throw new LnwSqlBadStatementException( "No se puede usar una lista de Custom Type con uno de ellos en null. Lista: "
						+ collection + ". CollectionType: " + param.collectionType );
			} else {
				param.customTypeList.add( customType );
			}
		}

		return param;
	}

	public final boolean isCollection() {
		return collection != null;
	}

	public final boolean isCustomType() {
		return customType != null;
	}

	public final boolean isCustomTypeCollection() {
		return customTypeList != null;
	}

	public final Object getObject() {
		if(parameter != null) return LnwSqlParameter.recoverData(parameter);
		if(collection != null) return LnwSqlParameter.recoverData(collection);
		if(customType != null) return customType;
		if(customTypeList != null) return customTypeList;

		throw new RuntimeException("The stored parameter has no data");
	}

	public String getCollectionType() {
		if(collectionType != null) return collectionType;

		throw new RuntimeException("The stored parameter has no collectionType");
	}


}
