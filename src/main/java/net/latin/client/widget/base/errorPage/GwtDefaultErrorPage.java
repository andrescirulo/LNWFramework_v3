package net.latin.client.widget.base.errorPage;

import java.util.Date;

import net.latin.client.i18n.LnwI18n;
import net.latin.client.widget.base.GwtController;
import net.latin.client.widget.base.GwtPage;
import net.latin.client.widget.label.GwtLabel;
import net.latin.client.widget.separator.GwtSpace;
import net.latin.client.widget.validation.GwtValidationUtils;

/**
 * Default error page
 *
 */
public class GwtDefaultErrorPage extends GwtPage implements GwtErrorPage {


	private final GwtLabel error;
	private final GwtLabel errorCode;
//	private final GwtForm form;
	private final String accessDeniedMsg;
	private final String errorMsg;

	public GwtDefaultErrorPage() {
		LnwI18n defaultI18n = GwtController.defaultI18n;
		accessDeniedMsg = defaultI18n.GwtDefaultErrorPage_accessDenied_msg;
		errorMsg = defaultI18n.GwtDefaultErrorPage_error_msg;

		this.add( new GwtSpace() );

//		form = new GwtForm( defaultI18n.GwtDefaultErrorPage_formTitle_msg );
//
		error = new GwtLabel();
		error.setWidth( "200px" );
		error.setHeight( "100px" );
//		form.addElement( defaultI18n.GwtDefaultErrorPage_descriptionLabel_text , error );
//
		errorCode = new GwtLabel();
//		form.addElement( defaultI18n.GwtDefaultErrorPage_errorCodeLabel_text , errorCode );
//
//		this.add( form.render() );
		this.add(error);
		this.add(errorCode);
	}

	protected void onVisible() {
	}

	public GwtPage getErrorPage() {
		return this;
	}

	public void setClientError(String msg, Throwable t) {
		clearText();
		if( GwtValidationUtils.validateRequired(msg) ) {
			error.setText(msg);
		} else {
			error.setText(errorMsg);
		}


		//FIXME usar la exception!!!!
	}

	public void setServerError(String msg, String errorCode) {
		if( GwtValidationUtils.validateRequired(msg) ) {
			error.setText(msg);
		} else {
			error.setText(errorMsg);
		}

		if( GwtValidationUtils.validateRequired(errorCode) ) {
			this.errorCode.setText(errorCode);
		} else {
			this.errorCode.setText("");
		}
	}

	public void setAccessDeniedError(String currentPageGroup) {
		clearText();
		this.error.setText(accessDeniedMsg);
		this.errorCode.setText( new Date().toString() );
	}

	private void clearText() {
		this.error.setText("");
		this.errorCode.setText( "" );
	}


}
