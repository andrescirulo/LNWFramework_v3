package net.latin.client.widget.msg;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.TextBox;

import gwt.material.design.client.constants.FlexAlignItems;
import gwt.material.design.client.constants.FlexDirection;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import net.latin.client.widget.base.GwtVisualComponent;
import net.latin.client.widget.separator.GwtSpace;

/**
 *
 */
public class GwtMsgNotification extends GwtVisualComponent {

	private final static String CSS_DIV_PRINCIPAL = "MessageBoxDiv";

	private MaterialPanel mainPanel;
	private TextBox focusInput;
	private GwtSpace spacer;

	/**
	 * Collection of messages
	 */
	public List<GwtMsg> messages = new ArrayList<GwtMsg>();

	public GwtMsgNotification() {

	}

	public GwtVisualComponent render() {
		mainPanel = new MaterialPanel();
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

	}


	public void addMessage( GwtMsg msg ) {
		this.messages.add(msg);
		
		MaterialLabel lbl = new MaterialLabel(msg.getText());
		
		MaterialPanel panel = new MaterialPanel();
		panel.setFlexDirection(FlexDirection.ROW);
		panel.setShadow(1);
		panel.add(msg.getType().getIcon());
		panel.add(lbl);
		panel.setMarginTop(5);
		panel.setMarginBottom(5);
		panel.setFlexAlignItems(FlexAlignItems.CENTER);
		panel.setBackgroundColor(msg.getType().getBgColor());
		mainPanel.add(panel);
		
		this.checkVisibility();
		this.focusTable();
	}

	public void addMessage( GwtMsgTypeEnum type, String text,int ttl ) {
		this.addMessage(new GwtMsg(type,text,ttl));
	}
	public void addMessage( GwtMsgTypeEnum type, String text ) {
		this.addMessage(new GwtMsg(type,text));
	}

	public void addOkMessage( String text ) {
		this.addMessage( GwtMsgTypeEnum.OK, text );
	}

	public void addErrorMessage( String text ) {
		this.addMessage( GwtMsgTypeEnum.ERROR, text );
	}

	public void addAlertMessage( String text ) {
		this.addMessage( GwtMsgTypeEnum.ALERT, text );
	}

	public void addLoadingMessage( String text ) {
		this.addMessage( GwtMsgTypeEnum.LOADING, text );
	}

	public void addOkMessage( String text,int ttl) {
		this.addMessage( GwtMsgTypeEnum.OK, text,ttl );
	}

	public void addErrorMessage( String text,int ttl ) {
		this.addMessage( GwtMsgTypeEnum.ERROR, text,ttl );
	}

	public void addAlertMessage( String text,int ttl ) {
		this.addMessage( GwtMsgTypeEnum.ALERT, text,ttl );
	}

	public void addLoadingMessage( String text,int ttl ) {
		this.addMessage( GwtMsgTypeEnum.LOADING, text,ttl );
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
				//ES UNO MAS PORQUE ESTA EL FOCUSINPUT
				this.mainPanel.remove(i+1);
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



	private void checkVisibility() {
		boolean question = this.getTotalMsg() > 0;
		mainPanel.setVisible( question );
		this.spacer.setVisible(question);
	}


	public int getTotalMsg() {
		return messages.size();
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
