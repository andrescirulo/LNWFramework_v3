package net.latin.client.widget.base.history;

import java.util.ArrayList;
import java.util.List;

import net.latin.client.widget.base.GwtPageGroup;

/**
 * Administra la pila de movimientos entre windows, para
 * poder volver atr�s
 *
 * @author Matias Leone
 */
public class HistoryStackManager {

	private GwtPageGroup pageGroup;
	private List<HistoryMovement> movements;

	public HistoryStackManager( GwtPageGroup pageGroup ) {
		this.pageGroup = pageGroup;
		this.movements = new ArrayList<HistoryMovement>();
	}

	/**
	 * Agrega un movimiento de window
	 */
	public void addMovement( HistoryMovement historyMovement ) {
		movements.add( historyMovement );
	}

	/**
	 * Retrocede a la window anterior, si es que hay alguna en la pila
	 */
	public void goBackward() {
		if( movements.size() > 0 ) {
			int lastIndex = movements.size() - 1;
			HistoryMovement movement = (HistoryMovement) movements.get( lastIndex );
			movement.goBackward( this );
			movements.remove( lastIndex );
		}
	}

	/**
	 * Metodo interno, no utilizar
	 */
	public void goToExternalPage(String groupName, String pageName) {
		pageGroup.goToExternalPage(groupName, pageName);
	}

	/**
	 * Metodo interno, no utilizar
	 */
	public void goToLocalPage(String pageName) {
		pageGroup.goToLocalGroup(pageName);
	}

	/**
	 * Limpia la pila de movimientos
	 */
	public void clear() {
		movements.clear();
	}



}
