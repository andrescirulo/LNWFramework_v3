package net.latin.client.validation.target;

import net.latin.client.validation.GwtValidationUtils;

/**
 * Complex validations beetween multiple texts
 *
 * @author Matias Leone
 */
public class MultipleStringTarget extends ValidationTarget {

	private String[] texts;

	public MultipleStringTarget( String[] texts ) {
		this.texts = texts;
	}


	/**
	 * at least one text must not be empty
	 */
	public MultipleStringTarget atLeastOne( String errorMsg ) {
		if( isOk() ) {

			boolean encontro = false;
			for (int i = 0; i < texts.length; i++) {
				if(GwtValidationUtils.validateRequired(texts[i])){
					encontro = true;
				}
			}
			if(!encontro){
				this.errorMsg = errorMsg;
			}
		}
		return this;
	}


}
