package net.latin.server.persistence.storedAdapters;

import org.springframework.jdbc.core.JdbcTemplate;

import net.latin.server.persistence.sql.core.LnwStoredProcedure;


public class LnwSqlServerSpringStoredProcedure extends
		LnwDefaultSpringStoredProcedure {

	public LnwSqlServerSpringStoredProcedure(LnwStoredProcedure procedure,
			JdbcTemplate jdbcTemplate) {
		super(procedure, jdbcTemplate);
	}

}
