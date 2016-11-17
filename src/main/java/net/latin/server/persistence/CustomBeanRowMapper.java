package net.latin.server.persistence;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import org.springframework.jdbc.core.RowMapper;

import net.latin.client.widget.base.CustomBean;
import net.latin.server.utils.exceptions.LnwException;

/**
 * Utilidad para mappear en forma automatica un ResultSet en un CustomBean
 *
 * @author Matias Leone
 */
public class CustomBeanRowMapper implements RowMapper {

	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		return fillCustomBean(rs);
	}

	public static CustomBean fillCustomBean(ResultSet rs) {
		try {
			final ResultSetMetaData metaData = rs.getMetaData();
			final CustomBean bean = new CustomBean();
			for (int i = 1; i <= metaData.getColumnCount(); i++) {
				final String columnLabel = metaData.getColumnLabel(i)
						.toLowerCase();
				if (metaData.getColumnType(i) == Types.CLOB) {
					bean.put(columnLabel, rs.getString(i));
				} else {
					bean.put(columnLabel, rs.getObject(i));
				}
				// FIXME cambiamos el nombre de la columna a minuscula, para que
				// funcione en oracle
			}
			;
			return bean;

		} catch (SQLException e) {
			throw new LnwException(
					"Error al intentar llenar un Custom Bean con un ResultSet");
		}
	}

}
