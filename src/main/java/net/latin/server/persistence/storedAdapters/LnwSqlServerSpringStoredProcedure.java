package net.latin.server.persistence.storedAdapters;

import net.latin.server.persistence.sql.core.LnwStoredProcedure;

import org.springframework.jdbc.core.JdbcTemplate;


public class LnwSqlServerSpringStoredProcedure extends
		LnwDefaultSpringStoredProcedure {

	public LnwSqlServerSpringStoredProcedure(LnwStoredProcedure procedure,
			JdbcTemplate jdbcTemplate) {
		super(procedure, jdbcTemplate);
	}

}
