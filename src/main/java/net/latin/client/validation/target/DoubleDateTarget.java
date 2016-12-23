package net.latin.client.validation.target;

import net.latin.client.validation.GwtValidationUtils;
import net.latin.client.widget.date.GwtDatePicker;

public class DoubleDateTarget extends ValidationTarget {

	private GwtDatePicker date1;
	private GwtDatePicker date2;

	public DoubleDateTarget(GwtDatePicker date1, GwtDatePicker date2) {
		this.date1 = date1;
		this.date2 = date2;
	}
	
	/**
	 * There might both dates or none
	 */
	public DoubleDateTarget bothOrNone( String errorMsg ) {
		if( isOk() ) {
			if( ( validateDate(date1) && !validateDate(date2) ) ||
				( !validateDate(date1) && validateDate(date2) ) ) 
			{
				this.errorMsg = errorMsg;
			}
		}
		return this;
	}

	/**
	 * date1 <= date2
	 */
	public DoubleDateTarget firstLowerOrEqualThanSecond( String errorMsg ) {
		if( isOk() ) {
			if ( validateDate( date1 ) && validateDate( date2 ) 
					&& !GwtValidationUtils.compare ( 
							date1.getDate(), date2.getDate(), GwtValidationUtils.SMALLER_EQUAL ) ) 
			{
				this.errorMsg = errorMsg;
			}
		}
		return this;
	}	
	
	private boolean validateDate( GwtDatePicker date ) {
		return GwtValidationUtils.validateDate( GwtValidationUtils.getDate(date.getDate(), GwtDatePicker.SEPARATOR ), GwtValidationUtils.DEFAULT_DATE_SEPARATOR );
	}
	

}
