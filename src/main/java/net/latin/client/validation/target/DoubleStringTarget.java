package net.latin.client.validation.target;

import net.latin.client.validation.GwtValidationUtils;

/**
 * Complex validations beetween two texts
 *
 * @author Matias Leone
 */
public class DoubleStringTarget extends ValidationTarget {

	private String text1;
	private String text2;

	public DoubleStringTarget( String text1, String text2 ) {
		this.text1 = text1;
		this.text2 = text2;
	}

	/**
	 * Only one of the text might be at the same time
	 */
	public DoubleStringTarget onlyOne( String errorMsg ) {
		if( isOk() ) {
			if( (GwtValidationUtils.validateRequired(text1) && GwtValidationUtils.validateRequired(text2)) ||
				(!GwtValidationUtils.validateRequired(text1) && !GwtValidationUtils.validateRequired(text2))) {
				this.errorMsg = errorMsg;
			}
		}
		return this;
	}

	/**
	 * There might both texts
	 */
	public DoubleStringTarget both( String errorMsg ) {
		if( isOk() ) {
			if( !GwtValidationUtils.validateRequired(text1) || !GwtValidationUtils.validateRequired(text2) ) {
				this.errorMsg = errorMsg;
			}
		}
		return this;
	}

	/**
	 * There two texts should be empty
	 */
	public DoubleStringTarget none( String errorMsg ) {
		if( isOk() ) {
			if( GwtValidationUtils.validateRequired(text1) || GwtValidationUtils.validateRequired(text2) ) {
				this.errorMsg = errorMsg;
			}
		}
		return this;
	}

	/**
	 * There might both texts or none
	 */
	public DoubleStringTarget bothOrNone( String errorMsg ) {
		if( isOk() ) {
			if( ( GwtValidationUtils.validateRequired(text1) && !GwtValidationUtils.validateRequired(text2) ) ||
				( !GwtValidationUtils.validateRequired(text1) && GwtValidationUtils.validateRequired(text2) ) ) {
				this.errorMsg = errorMsg;
			}
		}
		return this;
	}

	/**
	 * If the first text isn't empty, then the second text is required
	 */
	public DoubleStringTarget ifFirstThenSecond( String errorMsg ) {
		if( isOk() ) {
			if( GwtValidationUtils.validateRequired(text1) ) {
				if( !GwtValidationUtils.validateRequired(text2) ) {
					this.errorMsg = errorMsg;

				}
			}
		}
		return this;
	}

	/**
	 * One of the two texts must not be empty
	 */
	public DoubleStringTarget atLeastOne( String errorMsg ) {
		if( isOk() ) {
			if( !GwtValidationUtils.validateRequired(text1) && !GwtValidationUtils.validateRequired(text2) ) {
					this.errorMsg = errorMsg;
			}
		}
		return this;
	}

	/**
	 * text1 number < text2 number
	 */
	public DoubleStringTarget firstLowerThanSecond( String errorMsg ) {
		if( isOk() ) {
			if( !(GwtValidationUtils.getLong(text1) < GwtValidationUtils.getLong(text2)) ) {
				this.errorMsg = errorMsg;
			}
		}
		return this;
	}

	/**
	 * text1 number <= text2 number
	 */
	public DoubleStringTarget firstLowerOrEqualThanSecond( String errorMsg ) {
		if( isOk() ) {
			if( !(GwtValidationUtils.getLong(text1) <= GwtValidationUtils.getLong(text2)) ) {
				this.errorMsg = errorMsg;
			}
		}
		return this;
	}
	
	/**
	 * text1 number > text2 number
	 */
	public DoubleStringTarget firstGreaterThanSecond( String errorMsg ) {
		if( isOk() ) {
			if( !(GwtValidationUtils.getLong(text1) > GwtValidationUtils.getLong(text2)) ) {
				this.errorMsg = errorMsg;
			}
		}
		return this;
	}
	
	/**
	 * text1 number >= text2 number
	 */
	public DoubleStringTarget firstGreaterOrEqualThanSecond( String errorMsg ) {
		if( isOk() ) {
			if( !(GwtValidationUtils.getLong(text1) >= GwtValidationUtils.getLong(text2)) ) {
				this.errorMsg = errorMsg;
			}
		}
		return this;
	}

}
