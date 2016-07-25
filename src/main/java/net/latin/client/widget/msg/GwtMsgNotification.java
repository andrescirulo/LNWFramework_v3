package net.latin.client.widget.msg;

import java.util.ArrayList;
import java.util.List;

import net.latin.client.widget.base.GwtVisualComponent;
import net.latin.client.widget.separator.GwtSpace;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Tool for messages notificacition.
 * It allow to show a list of messages, with an image an a text, accoding to the
 * category of the message.
 *
 * @author Matias Leone
 *
 */
public class GwtMsgNotification extends GwtVisualComponent {

	private final static String CSS_DIV_PRINCIPAL = "MessageBoxDiv";
	private final static String CSS_CELDA_IMAGE = "MessageBoxCeldaImagen";

	/**
	 * Css Imagen - Css Texto
	 */
	private final static String[] CSS_MSG_OK = new String[]{ "MessageBoxImageOK", "MessageBoxCeldaTextoOk" };
	private final static String[] CSS_MSG_ERROR = new String[]{ "MessageBoxImageError", "MessageBoxCeldaTextoError" };
	private final static String[] CSS_MSG_ALERT = new String[]{ "MessageBoxImageAlert", "MessageBoxCeldaTextoAlert" };
	private final static String[] CSS_MSG_LOADING = new String[]{ "MessageBoxImageLoading", "MessageBoxCeldaTextoLoading" };

	/**
	 * Css Array
	 */
	private final static Object[] CSS_MSG = new Object[]{ CSS_MSG_OK, CSS_MSG_ERROR, CSS_MSG_ALERT, CSS_MSG_LOADING };

	/**
	 * Types of messages
	 */
	public final static int OK = 0;
	public final static int ERROR = 1;
	public final static int ALERT = 2;
	public final static int LOADING = 3;

	private VerticalPanel mainPanel;
	private FlexTable table;
	private TextBox focusInput;
	private GwtSpace spacer;

	/**
	 * Collection of messages
	 */
	public List<GwtMsg> messages = new ArrayList<GwtMsg>();

	public GwtMsgNotification() {

	}

	public GwtVisualComponent render() {
		mainPanel = new VerticalPanel();
		mainPanel.setStyleName( CSS_DIV_PRINCIPAL );
		mainPanel.setVisible( false );
		this.createMsgTable();
		super.add( mainPanel );
		createSpacer();
		return super.render();
	}

	private void createSpacer() {
		this.spacer = new GwtSpace();
		this.spacer.setVisible( false );
		super.add( this.spacer );
	}


	private void createMsgTable() {
		//create hidden input for focus
		this.focusInput = new TextBox();
		this.focusInput.setVisible( false );
		this.focusInput.setMaxLength( 1 );
		this.focusInput.setWidth( "1px" );
		this.mainPanel.add( this.focusInput );

		table = new FlexTable();
		table.setWidth( "100%" );
		table.setBorderWidth( 0 );
		table.setCellPadding( 0 );
		table.setCellSpacing( 0 );
		this.mainPanel.add( table );
	}


	public void addMessage( GwtMsg msg ) {
		this.messages.add(msg);
		if( msg.getType() < 0 || msg.getType() >= CSS_MSG.length ) {
			throw new RuntimeException( "Type of message incorrect: " + msg.getType() );
		}
		//create a row
		int n = this.table.insertRow( this.getTotalMsg() );
		this.createIconCell(msg.getType(), n);
		this.createTextCell(msg.getType(), msg.getText(), n);
		this.checkVisibility();
		this.focusTable();
	}

	public void addMessage( int type, String text,int ttl ) {
		this.addMessage(new GwtMsg(type,text,ttl));
	}
	public void addMessage( int type, String text ) {
		this.addMessage(new GwtMsg(type,text));
	}

	public void addOkMessage( String text ) {
		this.addMessage( OK, text );
	}

	public void addErrorMessage( String text ) {
		this.addMessage( ERROR, text );
	}

	public void addAlertMessage( String text ) {
		this.addMessage( ALERT, text );
	}

	public void addLoadingMessage( String text ) {
		this.addMessage( LOADING, text );
	}

	public void addOkMessage( String text,int ttl) {
		this.addMessage( OK, text,ttl );
	}

	public void addErrorMessage( String text,int ttl ) {
		this.addMessage( ERROR, text,ttl );
	}

	public void addAlertMessage( String text,int ttl ) {
		this.addMessage( ALERT, text,ttl );
	}

	public void addLoadingMessage( String text,int ttl ) {
		this.addMessage( LOADING, text,ttl );
	}


	public void addAllOkMessages( List<String> messagesList ) {
		for (String msg:messagesList){
			this.addOkMessage(msg);
		}
	}

	public void addAllErrorMessages( List<String> messagesList ) {
		for (String msg:messagesList){
			this.addErrorMessage(msg);
		}
	}

	public void addAllAlertMessages( List<String> messagesList ) {
		for (String msg:messagesList){
			this.addAlertMessage(msg);
		}
	}

	/**
	 * Clears all the messages with one ttl left
	 */
	public void clear() {
		//see if it is something to clear
		if( this.getTotalMsg() == 0 ) {
			return;
		}
		for (int i = this.messages.size() -1 ; i >= 0; i--) {
			GwtMsg msg = (GwtMsg)messages.get(i);
			msg.reduce();
			if( ! msg.isAlive()){
				this.table.removeRow( i );
				this.messages.remove(i);
			}
		}

		this.checkVisibility();
	}


	private void focusTable() {
		this.focusInput.setVisible( true );
		this.focusInput.setFocus( true );
		this.focusInput.setVisible( false );
	}

	private void createTextCell(int type, String text, int row) {
		this.table.insertCell( row, 1 );
		this.table.getCellFormatter().setStyleName( row, 1,  ((String[])CSS_MSG[ type ])[ 1 ] );
		this.table.setText( row, 1, text );
	}


	private void createIconCell(int type, int row) {
		//create icon cell
		this.table.insertCell( row, 0 );
		this.table.getCellFormatter().setStyleName( row, 0, CSS_CELDA_IMAGE );
		this.table.getCellFormatter().setHorizontalAlignment( row, 0,  VerticalPanel.ALIGN_CENTER );
		this.table.getCellFormatter().setVerticalAlignment( row, 0,  VerticalPanel.ALIGN_MIDDLE );

		//create image
		VerticalPanel iconPanel = new VerticalPanel();
		iconPanel.setStyleName( ((String[])CSS_MSG[ type ])[ 0 ] );
		iconPanel.add( new Label() );
		this.table.setWidget( row, 0, iconPanel );
	}


	private void checkVisibility() {
		boolean question = this.getTotalMsg() > 0;
		mainPanel.setVisible( question );
		this.spacer.setVisible(question);
	}


	public int getTotalMsg() {
		return this.table.getRowCount();
	}

	public List<GwtMsg> getMessages() {
		return messages;
	}

	public void setMessages(List<GwtMsg> messages) {
		this.messages = messages;
	}

	public void resetWidget() {
		//No debo realizar nada particular
	}

	public void setFocus() {
		//No debo realizar nada particular
	}

}
