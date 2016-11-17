package net.latin.client.utils;

public class ColorUtils {

	public static final String BLACK="000000";
	public static final String WHITE="FFFFFF";
	public static final String PURE_RED="FF0000";
	public static final String PURE_GREEN="00FF00";
	public static final String PURE_BLUE="0000FF";
	public static final String PURE_YELLOW="FFFF00";
	
	public static final String RED="DD4444";
	public static final String GREEN="44DD44";
	public static final String BLUE="4444DD";
	public static final String YELLOW="EEEE64";
	public static final String ORANGE = "F5A500";
	
	/**
	 * Modifica un color de acuerdo a un valor.
	 * Con un valor negativo se oscurece, con un valor positivo se aclara
	 * <br>
	 * El valor se suma a cada uno de los componentes RGB
	 * @param color en formato HTML de 6 caracteres
	 * @param valor
	 * @return
	 */
	public static String cambiarColor(String color,int valor){
		if (color.startsWith("#")){
			color=color.substring(1);
		}
		int[] rgb = fromHexString(color);
		rgb[0]+=valor;
		rgb[1]+=valor;
		rgb[2]+=valor;
		for (int i=0;i<rgb.length;i++){
			if (rgb[i]<0){
				rgb[i]=0;
			}
			else if (rgb[i]>255){
				rgb[i]=255;
			}
		}
		return "#" + toHexString(rgb);
	}
	
	/**
	 * Modifica un color de acuerdo a un porcentaje.
	 * Con un valor menor a 1 se oscurece, con un valor mayor a 1 se aclara
	 * Ej: 0.7 oscurece un 30%. 1.3 aclara un 30%
	 * <br>
	 * Nota: No sirve para el color negro. Utilizar cambiarColor(String color,int valor)
	 * @param color en formato HTML de 6 caracteres
	 * @param porcentaje
	 * @return
	 */
	public static String cambiarColor(String color,double porcentaje){
		if (color.startsWith("#")){
			color=color.substring(1);
		}
		int[] rgb = fromHexString(color);
		rgb[0]*=porcentaje;
		rgb[1]*=porcentaje;
		rgb[2]*=porcentaje;
		for (int i=0;i<rgb.length;i++){
			if (rgb[i]<0){
				rgb[i]=0;
			}
			else if (rgb[i]>255){
				rgb[i]=255;
			}
		}
		return "#" + toHexString(rgb);
	}

	public static int getAvgValue(String color) {
		if (color.startsWith("#")){
			color=color.substring(1);
		}
		int[] rgb = fromHexString(color);
		return (int) ((rgb[0] + rgb[1] + rgb[2])/3.0);
	}
	
	private static int[] fromHexString(String s) {
		
		int length = s.length() / 2;
		int[] bytes = new int[length];
		for (int i = 0; i < length; i++) {
			int val=Integer.parseInt("" + s.charAt(i*2) + s.charAt((i*2)+1), 16);
			bytes[i] = val;
		}
		return bytes;
	}

	/**
	 * Creates HEX String representation of supplied byte array.<br/>
	 * Each byte is represented by a double character element from 00 to ff
	 * 
	 * @param fieldData
	 *            to be tringed
	 * @return
	 */
	private static String toHexString(int[] fieldData) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < fieldData.length; i++) {
			int v = (fieldData[i] & 0xFF);
			if (v <= 0xF) {
				sb.append("0");
			}
			sb.append(Integer.toHexString(v));
		}
		return sb.toString();
	}

}
