package net.latin.client.widget.base.history;

/**
 * History de un movimiento a una page de otro grupo
 *
 * @author Matias Leone
 */
public class ExternalHistoryMovement implements HistoryMovement {

	private String groupName;
	private String pageName;

	public ExternalHistoryMovement( String groupName, String pageName ) {
		this.groupName = groupName;
		this.pageName = pageName;
	}

	public void goBackward(HistoryStackManager stackManager) {
		stackManager.goToExternalPage(groupName, pageName);
	}

}
