package net.latin.client.widget.format;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.latin.client.validation.GwtValidationUtils;

public class GwtFormatUtils {

	public static final int DDMMAAAA = 1;
	public static final int AAAAMMDD = 2;
	public static final int DD_MM_AAAA = 3;


	/**
	 * Given a text, returns that text or null, if the text is empty or null
	 * @param text
	 * @return text or null
	 */
	public static String getStringOrNull( String text ) {
		if( GwtValidationUtils.validateRequired(text) ) {
			return text;
		} else {
			return null;
		}
	}

	/**
	 * Given a text, returns that text or empty, if the text is empty or null
	 * @param text
	 * @return text or null
	 */
	public static String getStringOrEmpty( String text ) {
		if( GwtValidationUtils.validateRequired(text) ) {
			return text;
		} else {
			return "";
		}
	}

	/**
	 * Given a text, returns a Long or null, if the text is empty or null.
	 * It suppose the text is valid for a long.
	 * @param text
	 * @return Long or null
	 */
	public static Long getLongOrNull( String text ) {
		if( GwtValidationUtils.validateRequired(text) ) {
			return new Long( text );
		} else {
			return null;
		}
	}


	/**
	 * Given a list, if it's null then creates and returns an empty list, if it's not,
	 * returns the same list
	 * @param list
	 * @return o new empty list or the given list
	 */
	@SuppressWarnings("rawtypes")
	public static List getListOrCreates(List list) {
		if( list == null ) {
			return new ArrayList();
		} else {
			return list;
		}
	}

	/**
	 * Given a Long, returns 0 if it's null, or the same Long if itsn't null
	 * @param num
	 * @return cero or the same Long
	 */
	public static Long getLongOrCero(Long num) {
		if( num == null ) {
			return new Long( 0 );
		} else {
			return num;
		}
	}

	/**
	 * Format a Date object with the form dd/mm/yyyy
	 * @param date
	 */
	public static String formatDate( Date date ) {
		if(date == null) return "";
		return GwtValidationUtils.getDate(date);
	}

	/**
	 * Format an Hour with the form hh/mm/ss
	 */
	@SuppressWarnings("deprecation")
	public static String formatHour(Date date) {
		if(date == null) return "";
		return (date.getHours()<10 ? "0"+date.getHours():date.getHours()) + ":" + 
		(date.getMinutes()<10 ? "0"+date.getMinutes():date.getMinutes() )+ ":" + 
		(date.getSeconds()<10 ? "0"+date.getSeconds():date.getSeconds());
	}

	/**
	 * Format a Date with the form dd/mm/yyyy - hh:mm:ss
	 */
	public static String formatDateAndHour(Date date) {
		if(date == null) return "";
		return formatDate(date) + " - " + formatHour(date);
	}


	/**
	 * String del tipo ddmmaaaa
	 * @param fecha
	 * @return dd/mm/aaaa, si no es del formato especificado retorna ""
	 */
	public static String formatDate(String fecha) {
		if(fecha == null) return "";
		if(fecha.length()==8){
			String dia = fecha.substring(0,2);
			String mes = fecha.substring(2,4);
			String anio= fecha.substring(4,8);
			return dia+"/"+mes+"/"+anio;
		}
		else {
			return "";
		}
	}

	/**
	 * Entero del tipo aaaammdd
	 * @param fecha
	 * @return dd/mm/aaaa
	 */
	public static String formatDate(int fecha) {
		String dia = Integer.toString(fecha%100);
		String mes = Integer.toString((fecha%10000)/100);
		String anio= Integer.toString(fecha/10000);

		return (dia.length()==1 ? "0"+dia : dia)+"/"+
		(mes.length()==1 ? "0"+mes : mes)+"/"+
		anio;
	}
	
	/**
	 * Convierte una fecha Date en un String segun el tipo especificado
	 *
	 * @param fecha Date a convertir
	 * @param tipoFormato formato deseado en la conversion (GwtFormaterUtils.DD_MM_AAAA,
	 * GwtFormaterUtils.DDMMAAAA, GwtFormaterUtils.AAAAMMDD)
	 * @return String de la fecha en el formato especificado.
	 * 		   Si el formato no es valido devuele una cadena vacia
	 */
	@SuppressWarnings("deprecation")
	public static String formatDateToString(Date fecha, int tipoFormato) {
		int dia, mes, anio;
		if (fecha==null)
		{
			return "";
		}
		switch (tipoFormato) {
		case DD_MM_AAAA:
			return formatDate(fecha);

		case DDMMAAAA:
			dia = fecha.getDate();
			mes = fecha.getMonth();
			anio = fecha.getYear();
			return "" +(dia<10?"0"+dia:dia) + (++mes<10?"0"+mes:mes) + (1900+anio);

		case AAAAMMDD:
			dia = fecha.getDate();
			mes = fecha.getMonth();
			anio = fecha.getYear();
			return "" + (1900+anio) + (++mes<10?"0"+mes:mes) + (dia<10?"0"+dia:dia);

		default:
			return "";
		}
	}

	/**
	 * Convierte una fecha Date en un Integer segun el tipo especificado
	 *
	 * @param fecha
	 * @param tipoFormato formato deseado en la conversion (GwtFormaterUtils.DD_MM_AAAA,
	 * GwtFormaterUtils.DDMMAAAA, GwtFormaterUtils.AAAAMMDD)
	 * @return Integer de la fecha en el formato especificado.
	 * 		   Si el formato no es valido devuele null
	 */
	@SuppressWarnings("deprecation")
	public static Integer formatDateToInteger(Date fecha, int tipoFormato) {
		int dia, mes, anio;

		switch (tipoFormato) {
		case DDMMAAAA:
			dia = fecha.getDate();
			mes = fecha.getMonth();
			anio = fecha.getYear();
			return  dia * 1000000 + ++mes * 10000 + (1900+anio);

		case AAAAMMDD:
			dia = fecha.getDate();
			mes = fecha.getMonth();
			anio = fecha.getYear();
			return (1900+anio) * 10000 + ++mes * 100 + dia;

		default:
			return null;
		}
	}

	/**
	 * Convierte una fecha String en un Date segun el tipo especificado
	 *
	 * @param fecha
	 * @param tipoFormato formato del String fecha (GwtFormaterUtils.DD_MM_AAAA,
	 * GwtFormaterUtils.DDMMAAAA, GwtFormaterUtils.AAAAMMDD)
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Date formatStringToDate(String fecha, int tipoFormato) {
		String dia, mes, anio;

		if(fecha == null) return null;

		switch (tipoFormato) {
		case DD_MM_AAAA:
			return GwtValidationUtils.getDate(fecha, "/");
		case DDMMAAAA:
			dia = fecha.substring(0,2);
			mes = fecha.substring(2,4);
			anio= fecha.substring(4,8);
			return new Date(Integer.valueOf(anio) - 1900, Integer.valueOf(mes) -1, Integer.valueOf(dia));
		case AAAAMMDD:
			anio= fecha.substring(0,4);
			mes = fecha.substring(4,6);
			dia = fecha.substring(6,8);
			return new Date(Integer.valueOf(anio) - 1900, Integer.valueOf(mes) -1, Integer.valueOf(dia));
		default:
			return null;
		}
	}

	/**
	 * Convierte una fecha Integer en un Date segun el tipo especificado
	 *
	 * @param fecha
	 * @param tipoFormato formato del Integer fecha (GwtFormaterUtils.DD_MM_AAAA,
	 * GwtFormaterUtils.DDMMAAAA, GwtFormaterUtils.AAAAMMDD)
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Date formatIntegerToDate(Integer fecha, int tipoFormato) {
		int dia, mes, anio;

		if(fecha == null) return null;

		switch (tipoFormato) {
		case DDMMAAAA:
			anio= fecha % 10000;
			fecha /= 10000;
			mes = fecha % 100;
			fecha /= 100;
			dia = fecha;
			return new Date(anio - 1900, mes -1, dia);
		case AAAAMMDD:
			dia = fecha % 100;
			fecha /= 100;
			mes = fecha % 100;
			fecha /= 100;
			anio= fecha;
			return new Date(anio - 1900, mes -1, dia);
		default:
			return null;
		}
	}

	/**
	 * Convierte la fecha en formato Integer (ddmmaaaa o aaaammdd) a un string del tipo: dd/mm/aaaa
	 *
	 * @param fecha
	 * @param tipoFormato
	 * @return
	 */
	public static String formatIntegerToStringDate(Integer fecha, int tipoFormato) {
		Date date = formatIntegerToDate(fecha, tipoFormato);

		return formatDate(date);
	}
	/**
	 *
	 * @param fecha
	 * @param tipoFormato
	 * @return
	 */
	public static java.sql.Date formatDateToSqlDate(java.util.Date fecha) {
		return fecha==null?null:new java.sql.Date(fecha.getTime());
	}
	/**
	 *
	 * @param fecha
	 * @param tipoFormato
	 * @return
	 */
	public static java.sql.Date formatStringToSqlDate(String fecha, int tipoFormato) {

		return formatDateToSqlDate(formatStringToDate(fecha, GwtFormatUtils.AAAAMMDD));
	}

	/**
	 * Convierte la fecha en formato String (ddmmaaaa o aaaammdd) a un string del tipo: dd/mm/aaaa
	 *
	 * @param fecha
	 * @param tipoFormato
	 * @return
	 */
	public static String formatStringToStringDate(String fecha, int tipoFormato) {
		Date date = formatStringToDate(fecha, tipoFormato);

		return formatDate(date);
	}

	/**
	 * Given a text, returns a Integer or null, if the text is empty or null.
	 * It suppose the text is valid for a integer.
	 * @param text
	 * @return Integer or null
	 */
	public static Integer getIntegerOrNull(String text) {
		if( GwtValidationUtils.validateRequired(text) ) {
			return new Integer( text );
		} else {
			return null;
		}
	}

	/**
	 * Sugiere el DNI a partir de un CUIL sin guiones (Si el cuil esta rellenado
	 * con ceros a izquierda los ignora para formar el dni<br/>
	 * ej. 20319659987 -> 31965998 <br/>
	 * ej. 20000123456 -> 123456 <br/>
	 * @param cuil
	 * @return
	 */
	public static String formatDniFromCuil(String cuil) {
		String dni = cuil.substring(2, 10).trim();
		
		int len = dni.length();
		int st = 0;
		int off = 0;      
		char[] val = dni.toCharArray();    

		while ((st < len) && (val[off + st] <= '0')) {
		    st++;
		}
		return ((st > 0) || (len < dni.length())) ? dni.substring(st, len) : dni;
	}
	
	/**
	 * Limita el tamaño de la cadena al numero de caracteres indicado
	 * @param cadena
	 * @param caracteres
	 * @return
	 */
	public static String limitStringTo(String cadena,int caracteres){
		if (cadena.length()>caracteres){
			return cadena.substring(0,caracteres) + "...";
		}
		else{
			return cadena;
		}
	}
}
