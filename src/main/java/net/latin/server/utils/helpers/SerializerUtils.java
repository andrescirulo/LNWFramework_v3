package net.latin.server.utils.helpers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import net.latin.server.utils.exceptions.LnwException;

public class SerializerUtils {

	public SerializerUtils() {
	}

	public static void serializar(String s, Serializable serializable) {
		try {
			ObjectOutputStream objectoutputstream = new ObjectOutputStream(new FileOutputStream(s));
			objectoutputstream.writeObject(serializable);
			objectoutputstream.close();
		}
		catch (Exception exception) {
			throw LnwException.wrap("fallo al intentar serializar el objeto " + serializable + " con destino en " + s, exception);
		}
	}

	public static Object deserializar(String s) {
		Object obj = null;
		try {
			ObjectInputStream objectinputstream = new ObjectInputStream(new FileInputStream(s));
			obj = objectinputstream.readObject();
			objectinputstream.close();
		}
		catch (Exception exception) {
			throw new LnwException("No se pudo deserializar el archivo " + s, exception);
		}
		return obj;
	}
}
