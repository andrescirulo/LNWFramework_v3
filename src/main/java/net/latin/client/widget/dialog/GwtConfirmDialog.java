package net.latin.client.widget.dialog;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.Focusable;

import net.latin.client.widget.GwtWidgetUtils;
import net.latin.client.widget.confirm.GwtConfirm;
import net.latin.client.widget.confirm.GwtConfirmListener;

public class GwtConfirmDialog extends GwtConfirm {

	public GwtConfirmDialog(GwtConfirmListener listener, String title, String question) {
		super(listener, title, question, ORDENBOTONES_SI_NO);
	}

	public GwtConfirmDialog(GwtConfirmListener listener, String title) {
		super(listener, title, "", ORDENBOTONES_SI_NO);
	}

	public void center() {
		close();
		setVisible(true);
		show();

	    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				GwtWidgetUtils.setFocus((Focusable)buttonSi);
			}
		});
	}

}
