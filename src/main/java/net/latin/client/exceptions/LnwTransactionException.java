package net.latin.client.exceptions;

import net.latin.client.rpc.GwtBusinessException;

/**
 * Exception utilizada cuando ocurren problemas con la base de datos
 * al ejecutar una transacción.
 * La exception debe ser declarada como chequeada en la firma de los
 * métodos de la RPC de la interfaz Client del caso de uso en cuestión.
 * A diferencia de las demás excepciones chequeadas, el Framework hace
 * ROLLBACK con esta excepción en particular.
 * La excepción no se debe crear con el constructor a mano, sino que se
 * debe utilizar el método <code>GwtUseCase.throwTransactionException()</code>
 *
 */
public class LnwTransactionException extends GwtBusinessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1450169121712651179L;

	public LnwTransactionException() {
	}

	/**
	 * Constructor interno. No utilizar.
	 *
	 * La excepción no se debe crear con el constructor a mano, sino que se
	 * debe utilizar el método <code>GwtUseCase.throwTransactionException()</code>
	 */
	public LnwTransactionException(String message) {
		super(message);
	}


}
