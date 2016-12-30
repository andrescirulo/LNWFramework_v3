package net.latin.client.validation;

import java.util.Date;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyEvent;

import net.latin.client.exceptions.GwtValidationException;
import net.latin.client.validation.utils.ISBNValidator;
import net.latin.client.widget.format.GwtFormatUtils;

@SuppressWarnings("deprecation")
public class GwtValidationUtils {

	public static final String DEFAULT_IP_SEPARATOR = ".";
	public static final String DEFAULT_DATE_SEPARATOR = "/";

	public static final char[] LETERS_WITH_ACCENTS = {'\u00C1',
		'\u00C9',
		'\u00CD',
		'\u00D3',
		'\u00DA',
		'\u00E1',
		'\u00E9',
		'\u00ED',
		'\u00F3',
		'\u00FA'};
	
	//(Enie y letras con dieresis)
	public static final char[] SPECIAL_LETTERS = {'\u00D1','\u00F1', 
		'\u00C4',
		'\u00CB',
		'\u00CF',
		'\u00D6',
		'\u00DC',
		'\u00E4',
		'\u00EB',
		'\u00EF',
		'\u00F6',
		'\u00FC'};

	// Especie de configuracion dado que getDate(String, String) es static
	// no puedo subclasear para modificar el comportamiento.
	// No borrar dado que subproyectos pueden modificar su valor.
	// Por defecto se mantiene el comportamiento que habia hasta ahora.
	public static boolean USE_TIMEZONE_HACK = false;

	public static boolean validateRequired( String text ) {
		return text != null && !text.equals( "" );
	}

	public static boolean validateRequired( Object obj ) {
		return obj != null;
	}

	public static boolean validateEmpty( String text ) {
		return !validateRequired( text );
	}

	public static boolean validateEquals( String text, Object value ) {
		return text.equals( value.toString() );
	}

	public static boolean validateNotEquals( String text, Object value ) {
		return !validateEquals( text, value );
	}

	public static boolean validateMinLength( String text, int length ) {
		return  !( text.length() < length );
	}

	public static boolean validateMaxLength( String text, int length ) {
		return !( text.length() > length );
	}
	
	public static boolean validateMaxWordSize( String text, int size ) {
		int i = 0;
		String[] words = text.split(" ");
		for (; i < words.length && words[i].length() < size; i++);
		return  i == words.length;
	}

	public static boolean validateLength( String text, int length ) {
		return text.length() == length;
	}

	public static boolean validateInteger( String text ) {
		try {
			Integer.parseInt( text );
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static boolean validateLong( String text ) {
		try {
			Long.parseLong( text );
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static boolean validateFloat( String text ) {
		try {
			Float.parseFloat( text );
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static boolean validateDouble( String text ) {
		try {
			Double.parseDouble( text );
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static boolean validateBoolean( String text ) {
		text=text.trim().toLowerCase();
		if(text.equals("1") || text.equals("0") || text.equals("true") || text.equals("false")) {
			return true;
		}
		return false;
	}

	public static boolean validateNumeric( String text ) {
		if( validateInteger(text) ) {
			return true;
		} else if( validateLong(text) ) {
			return true;
		} else if( validateFloat(text) ) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Informa si la combinacion de teclas presionadas corresponden a teclas especiales:
	 * ENTER, SCAPE, CTRL + C, etc.
	 */
	public static boolean isSpecialKey(int keyCode, KeyEvent<?> event) {
		return ( keyCode == KeyCodes.KEY_ALT || keyCode == KeyCodes.KEY_BACKSPACE ||
				keyCode == KeyCodes.KEY_CTRL || keyCode == KeyCodes.KEY_DELETE ||
				keyCode == KeyCodes.KEY_DOWN || keyCode == KeyCodes.KEY_END ||
				keyCode == KeyCodes.KEY_ENTER || keyCode == KeyCodes.KEY_ESCAPE ||
				keyCode == KeyCodes.KEY_HOME || keyCode == KeyCodes.KEY_LEFT ||
				keyCode == KeyCodes.KEY_PAGEDOWN || keyCode == KeyCodes.KEY_PAGEUP ||
				keyCode == KeyCodes.KEY_RIGHT || keyCode == KeyCodes.KEY_SHIFT ||
				keyCode == KeyCodes.KEY_TAB || keyCode == KeyCodes.KEY_UP  ||
				//keyCode == GwtWidgetUtils.KEY_SPACEBAR ||
				( keyCode == 'V' && event.isControlKeyDown()) ||
				( keyCode == 'C' && event.isControlKeyDown() ) );
	}

	public static boolean validateNumeric( int c ) {
		return  ( c >= 48 && c <= 57 ) || ( c >= 96 && c <= 122 ) || (c==188)||(c==190);
	}

	/**
	 * Esta funcion valida que sea un numero del 0 a 9, tanto del teclado comun, como del teclado numerico
	 * @param keyCode del caracter
	 * @return
	 */
	public static boolean validateNumericNatural( int c ) {
		return  ( c >= 48 && c <= 57 ) || ( c >= 96 && c <= 105 );
	}
	
	public static boolean isDigit(char c){
		return c=='0' || 
				c=='1' ||
				c=='2' ||
				c=='3' ||
				c=='4' ||
				c=='5' ||
				c=='6' ||
				c=='7' ||
				c=='8' ||
				c=='9';
	}

	/**
	 * Esta funcion valida que sea un numero del 0 a 9 o el signo -, tanto del teclado comun, como del teclado numerico
	 * @param keyCode del caracter
	 * @return
	 */
	public static boolean validateNumericInteger( int c ) {
		return  ( c >= 48 && c <= 57 ) || c == 45 || ( c >= 96 && c <= 105 ) || c == 109;
	}

	/**
	 * Esta funcion valida que sea un numero del 0 a 9, el punto decimal o el signo -, tanto del teclado comun, como del teclado numerico
	 * @param keyCode del caracter
	 * @return
	 */
	public static boolean validateNumericDecimal( int c ) {
		return  ( c >= 48 && c <= 57 ) || c == 45 || c==46 || ( c >= 96 && c <= 105 ) || c == 109 || c == 110 || c == 190 || c == 188;
	}

	public static boolean validateAlphabetic( int c ) {
		return ( c >= 65 && c <= 90 ) /*||/( c >= 97 && c <= 122 )*/;
	}

	public static boolean validateAlphabetic( String text ) {
		char[] chars = text.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if( !Character.isLetter( chars[ i ] ) && !tieneAcento(chars[i]) && !esLetraEspecial(chars[i])) {
				return false;
			}
		}
		return true;
	}

	public static boolean validateAlphabeticWithSpace( String text ) {
		char[] chars = text.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if( !Character.isLetter( chars[ i ] ) && !Character.isWhitespace(chars[ i ])
					&& !tieneAcento(chars[i]) && !esLetraEspecial(chars[i])) {
					return false;
			}
		}
		return true;
	}

	public static boolean validateAlphaNumeric( String text ) {
		char[] chars = text.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if( !Character.isLetter( chars[ i ] ) && !Character.isDigit( chars[ i ] ) ) {
				return false;
			}
		}
		return true;
	}

	public static double getNumber(String text) {
		try {
			return Integer.parseInt( text );
		} catch (Exception e) {
		}
		try {
			return Long.parseLong( text );
		} catch (Exception e) {
		}
		try {
			return Double.parseDouble( text );
		} catch (Exception e) {
		}
		return -1;
	}

	public static int getInt(String text) {
		try {
			return Integer.parseInt( text );
		} catch (Exception e) {
			return -1;
		}
	}

	public static long getLong(String text) {
		try {
			return Long.parseLong( text );
		} catch (Exception e) {
			return -1;
		}
	}

	public static double getDouble(String text) {
		try {
			return Double.parseDouble( text );
		} catch (Exception e) {
			return -1;
		}
	}

	public static boolean validateSmaller( String text, int value ) {
		return getInt(text) < value;
	}

	public static boolean validateSmallerOrEqual( String text, int value ) {
		return getInt(text) <= value;
	}

	public static boolean validateGreater( String text, int value ) {
		return getInt(text) > value;
	}

	public static boolean validateGreaterOrEqual( String text, int value ) {
		return getInt(text) >= value;
	}

	public static boolean validateSmaller( String text, double value ) {
		return getDouble(text) < value;
	}

	public static boolean validateSmallerOrEqual( String text, double value ) {
		return getDouble(text) <= value;
	}

	public static boolean validateGreater( String text, double value ) {
		return getDouble(text) > value;
	}

	public static boolean validateGreaterOrEqual( String text, double value ) {
		return getDouble(text) >= value;
	}

	public static boolean validateSmallerOrEqual( String text, Number value ) {
		if ( value instanceof Integer ) {
			return getNumber(text) <= value.intValue();
		} else if ( value instanceof Long ) {
			return getNumber(text) <= value.longValue();
		} else if ( value instanceof Float ) {
			return getNumber(text) <= value.floatValue();
		} if ( value instanceof Double){
			return getNumber(text) <= value.doubleValue();
		}
		return false;
	}

	public static boolean validateGreaterOrEqual( String text, Number value ) {
		if ( value instanceof Integer ) {
			return getNumber(text) >= value.intValue();
		} else if ( value instanceof Long ) {
			return getNumber(text) >= value.longValue();
		} else if ( value instanceof Float ) {
			return getNumber(text) >= value.floatValue();
		}else if ( value instanceof Double){
			return getNumber(text) >= value.doubleValue();
		}
		return false;
	}

	public static boolean validateDay( String text ) {
		int n = getInt(text);
		return n > 0 && n < 32;
	}

	public static boolean validateMonth( String text ) {
		int n = getInt(text);
		return n > 0 && n < 13;
	}

	public static boolean validateYear( String text ) {
		return getInt(text) > 1899;
	}

	public static boolean validateYearToday( String text ) {
		return getInt(text) == new Date().getYear() + 1900;
	}

	public static boolean validateYearSmallerOrEqual( String text, int year ) {
		int n = getInt(text);
		return n > 0 && n <= year;
	}

	public static boolean validateYearGreaterOrEqual( String text, int year ) {
		int n = getInt(text);
		return n > 0 && n >= year;
	}

	public static boolean validateYearTodaySmallerOrEqual( String text ) {
		int n = getInt(text);
		return n > 0 && n <= new Date().getYear() + 1900;
	}

	private static String[] getDateArray( String text, String separator ) {
		if( text.indexOf( separator ) == -1 ) {
			return null;
		}
		return text.split( separator );
	}
	
	public static String[] getDateArray( Date date, String separator ) {
		if (date==null){
			return null;
		}
		int dayInt = date.getDate();
		int monthInt = date.getMonth() + 1;
		String day;
		String month;

		//format day
		if( dayInt < 10 ) {
			day = "0" + dayInt;
		} else {
			day = dayInt + "";
		}

		//format month
		if( monthInt < 10 ) {
			month = "0" + monthInt;
		} else {
			month = monthInt + "";
		}
		String[] arrayFecha={"","",""};
		arrayFecha[0]=day;
		arrayFecha[1]=month;
		arrayFecha[2]=(date.getYear() + 1900)+"";
		return arrayFecha;
	}

	public static Date getDate( String text, String separator ) {
		String[] splitArray = getDateArray( text, separator );
		if( splitArray == null || splitArray.length != 3) {
			return null;
		}

		Date date = new Date( getInt( splitArray[ 2 ] ) - 1900, getInt(  splitArray[ 1 ] ) - 1, getInt( splitArray[ 0 ] ) );

		//TODO: Cambiar este hackeo cuando encontremos una solucion buena.
		if(USE_TIMEZONE_HACK) {
			date.setHours(12);
		}

		return date;
	}

	public static String getDate( Date date, String separator ) {
		if (date==null){
			return null;
		}
		int dayInt = date.getDate();
		int monthInt = date.getMonth() + 1;
		String day;
		String month;

		//format day
		if( dayInt < 10 ) {
			day = "0" + dayInt;
		} else {
			day = dayInt + "";
		}

		//format month
		if( monthInt < 10 ) {
			month = "0" + monthInt;
		} else {
			month = monthInt + "";
		}

		return new String( day + separator + month + separator + (date.getYear() + 1900) );
	}

	public static String getDate( Date date ) {
		return GwtValidationUtils.getDate(date, DEFAULT_DATE_SEPARATOR );
	}

	public static boolean validateDate( String text ) {
		return validateDate( text, DEFAULT_DATE_SEPARATOR );
	}

	public static boolean validateDate( String text, String separator ) {
		if (text==null){
			return false;
		}
		String[] splitArray = getDateArray( text, separator );

		if( splitArray == null || splitArray.length != 3 ) {
			return false;
		}

		return validateDate( splitArray );
	}

	private static boolean validateDate( String[] array ) {
		return
			validateNumeric( array[ 0 ] ) && validateNumeric( array[ 1 ] ) && validateNumeric( array[ 2 ] ) &&
			validateDay( array[ 0 ] ) && validateMonth( array[ 1 ] ) && validateYear( array[ 2 ] ) &&
			isCorrectDate( getInt( array[ 0 ] ), getInt( array[ 1 ] ), getInt( array[ 2 ] ) );
	}


	private static boolean isCorrectDate( int day, int month, int year ) {
		boolean bis;
		//vemos a�o bisiesto
		if( month == 2 ) {
			//28 o 29 dias, segun anio bisiesto
			if( ( year % 4 ) == 0 ) {
				if ( ( year % 100 ) == 0 && ( year % 400 ) != 0) {
					bis = false;
				} else {
					bis = true;
				}

			//no bisiesto
			} else {
				bis = false;
			}
			if( !bis ) {
				if( day == 29 ) {
					return false;
				} else {
					if( day > 29 ) {
						return false;
					}
				}
			} else {
				if( day > 29 ) {
					return false;
				}
			}
		}

		if((month == 1) || (month == 3) || (month == 5) || (month == 7) || (month == 8) || (month == 10) || (month == 12)) {
			if( day > 31 ) {
				return false;
			}
		}

		if((month == 4) || (month == 6) || (month == 9) || (month == 11)) {
			if( day > 30 ) {
				return false;
			}
		}

		return true;
	}

	public static boolean validateDateSmaller( String text, String separator, Date date ) {
		if ( !validateDate(text, separator) ) {
			return false;
		}
		Date inputDate = getDate(text, separator);
		if( inputDate == null ) {
			return false;
		}

		if( inputDate.compareTo( date ) < 0 ) {
			return true;
		} else {
			return false;
		}
	}
	public static boolean validateDateBetween( Date fecha, Date desdePeriodo, Date  hastaPeriodo) {

		if (fecha.after(desdePeriodo) && fecha.before(hastaPeriodo)) {
				return true;
			}
		return false;
	}
	public static boolean validateDateBetweenOrEquals( Date fecha, Date desdePeriodo, Date  hastaPeriodo) {

		if((	fecha.compareTo(desdePeriodo) == 0) ||
				(fecha.compareTo(hastaPeriodo) == 0) ||
				(fecha.after(desdePeriodo) && fecha.before(hastaPeriodo)) )
				return true;
		return false;
	}


	public static boolean validateDateSmallerOrEquals( String text, String separator, Date date ) {
		if ( !validateDate(text, separator) ) {
			return false;
		}
		Date inputDate = getDate(text, separator);
		if( inputDate == null ) {
			return false;
		}

		if( inputDate.compareTo( date ) <= 0 ) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean validateDateGreater( String text, String separator, Date date ) {
		if ( !validateDate(text, separator) ) {
			return false;
		}
		Date inputDate = getDate(text, separator);
		if( inputDate == null ) {
			return false;
		}

		if( inputDate.compareTo( date ) > 0 ) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean validateDateGreaterOrEquals( String text, String separator, Date date ) {
		if ( !validateDate(text, separator) ) {
			return false;
		}
		Date inputDate = getDate(text, separator);
		if( inputDate == null ) {
			return false;
		}

		if( inputDate.compareTo( date ) >= 0 ) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean validateDateSmallerToday( String text, String separator ) {
		if ( !validateDate(text, separator) ) {
			return false;
		}
		Date inputDate = getDate(text, separator);
		if( inputDate == null ) {
			return false;
		}

		Date today = new Date();

		return compare(inputDate, today, SMALLER);
	}


	public static boolean validateDateSmallerOrEqualToday( String text, String separator ) {
		if ( !validateDate(text, separator) ) {
			return false;
		}
		Date inputDate = getDate(text, separator);
		if( inputDate == null ) {
			return false;
		}

		Date today = new Date();

		return compare(inputDate, today, SMALLER_EQUAL);
	}


	public static boolean validateDateGreaterToday( String text, String separator ) {
		if ( !validateDate(text, separator) ) {
			return false;
		}
		Date inputDate = getDate(text, separator);
		if( inputDate == null ) {
			return false;
		}

		Date today = new Date();

		return compare(inputDate, today, GREATER);
	}


	public static boolean validateDateGreaterOrEqualsToday( String text, String separator ) {
		if ( !validateDate(text, separator) ) {
			return false;
		}
		Date inputDate = getDate(text, separator);
		if( inputDate == null ) {
			return false;
		}

		Date today = new Date();

		return compare(inputDate, today, GREATER_EQUAL);
	}

	public static final int EQUAL = 0;
	public static final int GREATER = 1;
	public static final int GREATER_EQUAL = 2;
	public static final int SMALLER = 3;
	public static final int SMALLER_EQUAL = 4;

	/**
	 * Permite comparar 2 fechas usando el tipo de operador indicado.
	 *
	 * @param date1 Fecha usanda como LHS.
	 * @param date2 Fecha usanda como RHS.
	 * @param operator Tipo de operacion a realizar entre las 2 fechas. Si no se
	 * 					corresponde con los tipos especificados se tira una
	 * 					Exception.
	 *
	 * @return Boolean que indica el resultado de la comparacion de las 2 fechas.
	 */
	public static boolean compare(Date date1, Date date2, int operator) {
		clearTime(date1);
		clearTime(date2);

		switch (operator) {
			case EQUAL:
				return (date1.compareTo(date2) == 0);
			case GREATER:
				return (date1.compareTo(date2) > 0);
			case GREATER_EQUAL:
				return (date1.compareTo(date2) >= 0);
			case SMALLER:
				return (date1.compareTo(date2) < 0);
			case SMALLER_EQUAL:
				return (date1.compareTo(date2) <= 0);
			default:
				throw new GwtValidationException("No se reconoce el tipo de operador para realizar la comparacion. Tipo: " + operator);
		}
	}

	/**
	 * Dada una fecha setea la hora en 00:00:00.000
	 *
	 * @return La misma instancia de la fecha pasada por parametro pero con la hora en 00:00:00.000
	 */
	public static Date clearTime(Date date) {
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);

		//Lo siguiente es para dejar los millisecons en 0 dado que Date no
		//tiene una forma de setear solo el campo de milliseconds.
		long aux = date.getTime();
		aux /= 1000;
		aux *= 1000;
		date.setTime(aux);

		return date;
	}

	public static boolean validateMail(String mail) {
		return mail.toUpperCase().matches("^[A-Z0-9][A-Z0-9._%+-]*@[A-Z0-9.-]+\\.[A-Z]{2,4}$");
//		return text.indexOf( DEFAULT_IP_SEPARATOR ) != -1 && text.indexOf( "@" ) != -1;
		//return text.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
	}

	public static boolean validateNumericPositive(String text) {
		if( validateIntegerPositive(text) ) {
			return true;
		} else if( validateLongPositive(text) ) {
			return true;
		} else if( validateFloatPositive(text) ) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean validateIntegerPositive(String text) {
		if ( validateInteger(text) && validateIntegerGreaterThanZero(text) ) {
			return true;
		}
		return false;
	}

	private static boolean validateLongPositive(String text) {
		if ( validateLong(text) && validateLongGreaterThanZero(text) ) {
			return true;
		}
		return false;
	}


	private static boolean validateFloatPositive(String text) {
		if ( validateFloat(text) && validateFloatGreaterThanZero(text) ) {
			return true;
		}
		return false;
	}

	private static boolean validateIntegerGreaterThanZero(String text) {
		if (validateInteger(text) && Integer.parseInt(text) > 0) {
			return true;
		}
		return false;
	}

	private static boolean validateLongGreaterThanZero(String text) {
		if (validateLong(text) && Long.parseLong(text) > 0) {
			return true;
		}
		return false;
	}

	public static boolean validateFloatGreaterThanZero(String text) {
		if (validateFloat(text) && Float.parseFloat(text) > 0) {
			return true;
		}
		return false;
	}

	public static boolean validateNumericPositiveOrZero(String text) {
		if( validateIntegerPositiveOrZero(text) ) {
			return true;
		} else if( validateLongPositiveOrZero(text) ) {
			return true;
		} else if( validateFloatPositiveOrZero(text) ) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean validateIntegerPositiveOrZero(String text) {
		if ( validateInteger(text) && validateIntegerGreaterOrEqualThanZero(text) ) {
			return true;
		}
		return false;
	}

	private static boolean validateLongPositiveOrZero(String text) {
		if ( validateLong(text) && validateLongGreaterOrEqualThanZero(text) ) {
			return true;
		}
		return false;
	}


	private static boolean validateFloatPositiveOrZero(String text) {
		if ( validateFloat(text) && validateFloatGreaterOrEqualThanZero(text) ) {
			return true;
		}
		return false;
	}

	private static boolean validateIntegerGreaterOrEqualThanZero(String text) {
		if (Integer.parseInt(text) >= 0) {
			return true;
		}
		return false;
	}

	private static boolean validateLongGreaterOrEqualThanZero(String text) {
		if (Long.parseLong(text) >= 0) {
			return true;
		}
		return false;
	}

	private static boolean validateFloatGreaterOrEqualThanZero(String text) {
		if (Float.parseFloat(text) >= 0) {
			return true;
		}
		return false;
	}

	public static boolean validateRegEx(String text, String regex) {
		return text.matches(regex);
	}

	/**
	 * Valida un CUIL/CUIT.
	 *
	 * @param text El CUIL/CUIT sin guiones. Ej: 201234567890
	 */
	public static boolean validateCuil(String text) {
		return validarCuilSinGuion(text);
	}

	private static boolean validarCuilSinGuion(String cuit) {
		if(cuit == null || "".equals(cuit) || cuit.length() != 11) {
			return false;
		}

		String xyStr, dniStr, digitoStr;
		int digitoTmp;
		int n = cuit.length();

		xyStr = cuit.substring(0, 2);
		dniStr = cuit.substring(2, n-1);
		digitoStr = cuit.substring(n - 1);

		if (xyStr.length() != 2 || dniStr.length() > 8 || digitoStr.length() != 1)
			return false;

		int xyStc;
		int dniStc;

		try {
			xyStc = Integer.parseInt(xyStr);
			dniStc = Integer.parseInt(dniStr);
			digitoTmp = Integer.parseInt(digitoStr);
		} catch (NumberFormatException e) {
			return false;
		}

		if (xyStc != 20 && xyStc != 23 && xyStc != 24 && xyStc != 27 && xyStc != 30 && xyStc != 33 && xyStc != 34 && xyStc != 50 && xyStc != 51)
			return false;

		int digitoStc = calcularDigitoVerificadorCuil(xyStc, dniStc);

		if (digitoStc == digitoTmp && xyStc == Integer.parseInt(xyStr))
			return true;

		return false;
	}

	private static int calcularDigitoVerificadorCuil(int xyStc, int dniStc) {
		long tmp1, tmp2;
		long acum = 0;
		int n = 2;
		tmp1 = xyStc * 100000000L + dniStc;

		for (int i = 0; i < 10; i++) {
			tmp2 = tmp1 / 10;
			acum += (tmp1 - tmp2 * 10L) * n;
			tmp1 = tmp2;
			if (n < 7)
				n++;
			else
				n = 2;
		}

		n = (int) (11 - acum % 11);

		if (n == 10) {
			return 9;
		} else {
			if (n == 11)
				return 0;
			else
				return n;
		}
	}

	public static boolean validateDateEquals(Date date1, Date date2) {
		return date1.compareTo( date2 ) == 0;
	}

	public static boolean validateDateFirstLowerThanSecond(Date date1, Date date2) {
		return date1.compareTo( date2 ) < 0;
	}

	public static boolean validateDateFirstLowerOrEqualThanSecond(Date date1, Date date2) {
		return date1.compareTo( date2 ) <= 0;
	}

	public static boolean validateDateFirstGreaterThanSecond(Date date1, Date date2) {
		return date1.compareTo( date2 ) > 0;
	}

	public static boolean validateDateFirstGreaterOrEqualThanSecond(Date date1, Date date2) {
		return date1.compareTo( date2 ) >= 0;
	}

	/**
	 * Valida que un string tenga al menos numbersCount numeros
	 */
	public static boolean validateWithNumbers( String text, int numbersCount ) {
		char[] charArray = text.toLowerCase().toCharArray();
		char c;
		for (int i = 0; i < charArray.length; i++) {
			c = charArray[i];
			if( c >= 48 && c <= 57 ) {
				numbersCount--;
			}
		}
		return numbersCount <= 0;
	}

	/**
	 * Valida que el texto no tenga espacios en blanco por ningún lado
	 */
	public static boolean validateWithoutSpaces( String text ) {
		return text.indexOf( " " ) == -1;
	}

	public static boolean validateIP(String ip){
		return validateIP(ip, DEFAULT_IP_SEPARATOR);
	}

	public static boolean validateBothOrNone(String a, String b){
		return ( (validateRequired(a) && validateRequired(b)) || (validateEmpty(a) && validateEmpty(b)));
	}

	public static boolean validateIP(String ip, String separator){
		if(ip == null || !ip.contains(separator)) {
			return false;
		} else {
			ip = ip.replace(separator, "-");
			String[] octetos = ip.split("-");

			if (octetos.length != 4 ){
				return false;
			}else{

				if(Integer.parseInt(octetos[0])< 0 || Integer.parseInt(octetos[0])> 255) return false;
				if(Integer.parseInt(octetos[1])< 0 || Integer.parseInt(octetos[1])> 255) return false;
				if(Integer.parseInt(octetos[2])< 0 || Integer.parseInt(octetos[2])> 255) return false;
				if(Integer.parseInt(octetos[3])< 0 || Integer.parseInt(octetos[3])> 255) return false;


				if(Integer.parseInt(octetos[0])==0 && ( Integer.parseInt(octetos[1]) == 0 ||
														Integer.parseInt(octetos[2]) == 0 ||
														Integer.parseInt(octetos[3]) == 0 ))return false;
			}
		}
		return true;
	}

	/**
	 * Valida si dos rangos de fechas se superponen, y devuelve true en caso de que lo hagan.
	 * @param newDesde
	 * @param newHasta
	 * @param oldDesde
	 * @param oldHasta
	 * @return verdadero, en caso de que los rangos se superpongan. Falso en caso contrario.
	 */
	public static boolean validateRangos(String newDesde, String newHasta, String oldDesde, String oldHasta) {
		return validateRangos(GwtFormatUtils.formatStringToDate(newDesde, GwtFormatUtils.AAAAMMDD),
							  GwtFormatUtils.formatStringToDate(newHasta, GwtFormatUtils.AAAAMMDD),
							  GwtFormatUtils.formatStringToDate(oldDesde, GwtFormatUtils.AAAAMMDD),
							  GwtFormatUtils.formatStringToDate(oldHasta, GwtFormatUtils.AAAAMMDD));
	}

	/**
	 * Valida si dos rangos de fechas se superponen, y devuelve true en caso de que lo hagan.
	 * @param newDesde
	 * @param newHasta
	 * @param oldDesde
	 * @param oldHasta
	 * @return verdadero, en caso de que los rangos se superpongan. Falso en caso contrario.
	 */
	public static boolean validateRangos(Date newDesde, Date newHasta, Date oldDesde, Date oldHasta) {

				if (newHasta != null
						&& oldHasta != null
						&& ((newHasta.compareTo(oldDesde) == 0)
							|| (newHasta.compareTo(oldHasta) == 0)
							|| (newHasta.after(oldDesde)
								&& newHasta.before(oldHasta))
							)) {
					return true;
				} else {
					if (newHasta == null && oldHasta == null) {
						return true;
					}else if(newHasta == null) {
						return newDesde.compareTo(oldHasta) <= 0;
					}
				}

				if ((oldHasta != null)) {
					if ((newDesde.compareTo(oldHasta) == 0)
							|| (newDesde.compareTo(oldDesde) == 0)
							|| (newDesde.after(oldDesde) && newDesde.before(oldHasta))
							||	(newDesde.before(oldDesde) && newHasta.after(oldHasta)))
						return true;
				} else {
					if(newHasta.compareTo(oldDesde) >= 0) {
						return true;
					}
				}
			return false;
		}

	public static boolean validateRangoContains(String oldDesde, String oldHasta, String newDesde, String newHasta) {
		return validateRangoContains(GwtFormatUtils.formatStringToDate(oldDesde, GwtFormatUtils.AAAAMMDD),
				  GwtFormatUtils.formatStringToDate(oldHasta, GwtFormatUtils.AAAAMMDD),
				  GwtFormatUtils.formatStringToDate(newDesde, GwtFormatUtils.AAAAMMDD),
				  GwtFormatUtils.formatStringToDate(newHasta, GwtFormatUtils.AAAAMMDD));
	}

	private static boolean validateRangoContains(Date oldDesde, Date oldHasta, Date newDesde, Date newHasta) {

		if(newHasta == null && oldHasta != null) {
			return false;
		}

		if(oldHasta == null) {
			return newDesde.compareTo(oldDesde) >= 0;
		}

		return (newDesde.compareTo(oldDesde) >= 0) && (newHasta.compareTo(oldHasta) <= 0);
	}

	public static boolean validateISBN(String text) {
		return ISBNValidator.isValid(text);
	}

	public static boolean validateAlphaNumericWithoutAccents(String text) {
		char[] chars = text.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if( (!Character.isLetter( chars[ i ] ) && !Character.isDigit( chars[ i ] ))
					|| tieneAcento(chars[ i ])) {
				return false;
			}
		}
		return true;
	}

	public static boolean validateAlphaNumericWithoutAccentsWithSpace(String text) {
		char[] chars = text.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if( (!Character.isLetter( chars[ i ] ) && !Character.isDigit( chars[ i ] ) && !Character.isWhitespace(chars[ i ]))
					|| tieneAcento(chars[ i ])) {
				return false;
			}
		}
		return true;
	}

	private static boolean esLetraEspecial(char c) {
		for(int i = 0; i < SPECIAL_LETTERS.length; i++){
			if(SPECIAL_LETTERS[i]==c){
				return true;
			}
		}
		
		return false;
	}
	private static boolean tieneAcento(char c) {
//		char[] acentos = LETERS_WITH_ACCENTS.toCharArray();
		for(int i = 0; i < LETERS_WITH_ACCENTS.length; i++){
			if(LETERS_WITH_ACCENTS[i]==c){
				return true;
			}
		}
		
		return false;
	}

	public static boolean validateWithoutAccents(String text) {
		char[] chars = text.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if( tieneAcento(chars[ i ])) {
				return false;
			}
		}
		return true;
	}

	public static boolean validateWithLetters(String text) {
		char[] chars = text.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if( Character.isLetter( chars[ i ] ) ) {
				return true;
			}
		}
		return false;
	}

	public static boolean validateWithoutNumbers(String text) {
		char[] chars = text.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if( Character.isDigit( chars[ i ] ) ) {
				return false;
			}
		}
		return true;
	}

	public static boolean validateAlphaNumericWithSpace(String text) {
		char[] chars = text.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if( !Character.isLetter( chars[ i ] ) && !Character.isDigit( chars[ i ] )
					&& !Character.isWhitespace( chars[ i ] ) ) {
				return false;
			}
		}
		return true;
	}

	public static boolean validateAlphaNumericWithSpaceAndBar(String text) {
		char[] chars = text.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if( !Character.isLetter( chars[ i ] ) && !Character.isDigit( chars[ i ] )
					&& !Character.isWhitespace( chars[ i ] ) && chars[ i ] != '/' ) {
				return false;
			}
		}
		return true;
	}

	public static boolean validateHasLetter(String text) {
		char[] chars = text.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if( Character.isLetter( chars[ i ] ) ) {
				return true;
			}
		}
		return false;
	}

	public static boolean validateAlphabeticAndNumeric(String text) {
		boolean withLetter = false, withDigit = false;
		char[] chars = text.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (!Character.isLetter(chars[i]) && !Character.isDigit(chars[i])) {
				return false;
			}
			withLetter |= Character.isLetter(chars[i]);
			withDigit |= Character.isDigit(chars[i]);
		}
		return withLetter && withDigit;
	}
}
