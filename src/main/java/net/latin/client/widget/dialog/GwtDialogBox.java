package net.latin.client.widget.dialog;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.polymer.paper.widget.PaperDialog;
import com.vaadin.polymer.paper.widget.PaperToolbar;

/**
 * Dialog Box de LNW.
 * Tiene eventos:
 * <code>onBeforeShow</code>
 * <code>onAfterShow</code>
 * <code>onHide</code>
 *
 *
 */
public class GwtDialogBox extends PaperDialog {

	private static final String CSS_BODY = "GwtDialogBoxBody";
	private VerticalPanel mainPanel;
	protected boolean alreadyVisible;
	protected PaperToolbar toolbar;
//	private boolean modal;

	/**
	 * Creates a new Dialog Box, originally hidden
	 */
	public GwtDialogBox() {
		this(true);
	}
	/**
	 * Creates a new Dialog Box, originally hidden
	 */
	public GwtDialogBox(boolean modal) {
//		super( false, modal );
		super();
//		this.modal = modal;
		this.alreadyVisible = false;
		this.mainPanel = new VerticalPanel();
		this.mainPanel.setHorizontalAlignment( VerticalPanel.ALIGN_CENTER );
		this.mainPanel.setVerticalAlignment( VerticalPanel.ALIGN_MIDDLE );
		this.mainPanel.setStyleName( CSS_BODY );
		this.mainPanel.setWidth( "100%" );
		this.toolbar=new PaperToolbar();
		setWithBackdrop(modal);
		this.getElement().getStyle().setProperty( "zIndex", "11");

		this.close();
	}

	/**
	 * Add a widget to the Dialog
	 */
	public void add(Widget w) {
		this.mainPanel.add( w );
	}

	/**
	 * Remove a widget from the dialog
	 */
	public boolean remove(Widget w) {
		return this.mainPanel.remove( w );
	}

	/**
	 * Render the dialog and let it ready to be shown
	 */
	public GwtDialogBox render() {
		this.clear();
		super.add(this.toolbar);
		super.add( this.mainPanel );
		return this;
	}

	public void show() {
		this.onBeforeShow();
		if( !this.alreadyVisible ) {
			super.center();
			this.alreadyVisible = true;
		}
		this.onAfterShow();
	}

	public void close() {
		this.onClose();
		if( this.alreadyVisible ) {
			super.close();
			this.alreadyVisible = false;
		}
	}

	/**
	 * Show or hide the Dialog depending on the current state of it
	 */
	public void showHide() {
		if( this.alreadyVisible ) {
			this.close();
		} else {
			this.show();
		}
	}

	/**
	 * Template method call before the Dialog become hidden
	 */
	protected void onClose() {
	}

	/**
	 * Template metod call before the Dialog become visible
	 */
	protected void onBeforeShow() {
	}

	/**
	 * Template metod call after the Dialog become visible
	 */
	protected void onAfterShow() {
	}

	public void setWidth(String width) {
		super.setWidth(width);
		this.mainPanel.setWidth(width);
	}

	public void setHeight(String height) {
		super.setHeight(height);
		this.mainPanel.setHeight(height);
	}

	public void setTitleText(String titleText){
		toolbar.add(new Label(titleText));
	}
}






