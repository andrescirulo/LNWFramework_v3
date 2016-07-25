package net.latin.server.persistence.sql.core;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Mapeador de objetos a LnwCustomType
 *
 * @author Matias Leone
 */
public interface LnwCustomTypeMapper extends IsSerializable {

	/**
	 * Callback que se llama para mapear un object en un LnwCustomType
	 * @param customType
	 * @param object
	 * @param objData
	 */
	public void mapCustomType( LnwCustomType customType, Object objData );

}
