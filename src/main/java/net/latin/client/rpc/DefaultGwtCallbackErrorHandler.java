package net.latin.client.rpc;

import net.latin.client.widget.base.GwtController;

/**
 * Handler default que delega todos los errores al GwtController
 *
 * @author Matias Leone
 */
public class DefaultGwtCallbackErrorHandler implements GwtCallbackErrorHandler {


	public void doSessionExperied() {
		GwtController.instance.doSessionExperied();
	}


	public void doUnexpectedFailure(Throwable e) {
		GwtController.instance.doUnexpectedFailure(e);
	}


	public void doUnexpectedFailure(String errorMsg) {
		GwtController.instance.doUnexpectedFailure(errorMsg);
	}


	public void doUnexpectedFailure(String errorMsg, Throwable t) {
		GwtController.instance.doUnexpectedFailure(errorMsg, t.getMessage());
	}

}
