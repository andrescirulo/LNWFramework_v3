package net.latin.server.utils.helpers;

/**
 * Metodos matematicos, ver que pasa con el IEEEremainder de Math
 * @author jsantoro
 */
public class MathUtils {

    /**
     * Calcula y devuelve el resto de la division de dos numeros,
     * en caso de que el divisor sea cero devuelve Double.NaN
     */
    public static double remainder(double dividendo, double divisor){
        if(divisor == 0)
            return Double.NaN;
        double resultado = dividendo / divisor;
        resultado = Math.floor(resultado);
        return (dividendo - (resultado * divisor));
    }

	public static String byteToHexString(byte abyte0[]) {
		StringBuffer stringbuffer = new StringBuffer();
		for (int i = 0; i < abyte0.length; i++) {
			stringbuffer.append(Integer.toHexString(0xff & abyte0[i]));
			stringbuffer.append(" ");
		}

		return stringbuffer.toString();
	}
}
