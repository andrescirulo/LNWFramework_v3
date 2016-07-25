package net.latin.server.persistence.sql.core.exceptions;

import com.google.gwt.user.client.rpc.IsSerializable;

public class LnwNotImplementedException extends RuntimeException implements
		IsSerializable {

	private static final String DEFAULT_MESSAGE = "Code is not implemented";

	public LnwNotImplementedException() {
		super(DEFAULT_MESSAGE);
	}

	public LnwNotImplementedException(String message, Throwable cause) {
		super(message, cause);
	}

	public LnwNotImplementedException(String message) {
		super(message);
	}

	public LnwNotImplementedException(Throwable cause) {
		super(DEFAULT_MESSAGE,cause);
	}

}
