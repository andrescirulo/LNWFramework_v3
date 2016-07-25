package net.latin.server.persistence.sql.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Clase que representa Types de usuario para enviar como parametros a
 * stored procedures
 *
 * @author Matias Leone
 */
public  class LnwCustomType implements IsSerializable {

		private List<LnwSqlParameter> data = new ArrayList<LnwSqlParameter>();
		private String objectType;

		public LnwCustomType(String objectType) {
			this.objectType = objectType;
		}

		public LnwCustomType add( Object value ) {
			data.add( LnwSqlParameter.adaptData(value) );
			return this;
		}

		public static LnwCustomType create(String objectType) {
			return new LnwCustomType(objectType);
		}

		public String getBuildParameters() {
			StringBuffer buffer = new StringBuffer();
			buffer.append( " " );
			buffer.append( objectType );
			buffer.append( "( " );
			for (int i = 0; i < data.size(); i++) {
				buffer.append( " ?," );
			}

			//quito la última ,
			buffer
				.deleteCharAt( buffer.length() - 1 )
				.append( " )," );

			return buffer.toString();
		}

		public Collection<Object> getBuildValues() {
			return LnwSqlParameter.recoverData(data);
		}

		public String getObjectType() {
			return objectType;
		}

		public List getData() {
			return data;
		}

	}
