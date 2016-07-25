package net.latin.client.widget.base.history;

/**
 * History de un movimiento a una page local a un grupo
 *
 * @author Matias Leone
 */
public class LocalHistoryMovement implements HistoryMovement {

	private String pageName;

	public LocalHistoryMovement( String pageName ) {
		this.pageName = pageName;
	}

	public void goBackward(HistoryStackManager stackManager) {
		stackManager.goToLocalPage(pageName);
	}

}
