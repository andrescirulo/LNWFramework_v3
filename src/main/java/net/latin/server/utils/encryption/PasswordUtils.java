package net.latin.server.utils.encryption;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;

import net.latin.server.utils.exceptions.LnwException;

public class PasswordUtils {

	/**
	 * Compara un password encriptado contra uno sin encriptar. Se utiliza el
	 * algoritmo de sha.
	 * 
	 */
	public static boolean compare(String passEncrypt, String pass) {
		return encrypt(pass).equals(passEncrypt);
	}

	public static String encrypt(String pass) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA");
			md.update(pass.getBytes("UTF-8"));
			return new String(Base64.encodeBase64(md.digest()));
		} catch (Exception e) {
			throw new LnwException("No se pudo encriptar el password.", e);
		}
	}

	public static String encryptBytes(byte[] blob) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA");
			md.update(blob);
			return new String(Base64.encodeBase64(md.digest()));
		} catch (Exception e) {
			throw new LnwException("No se pudo encriptar el password.", e);
		}
	}

	public static void main(String[] args) {
		try {
//			String pass = "30633260687";
//
//			System.out.println("cadena: " + pass);
//
//			MessageDigest md = MessageDigest.getInstance("SHA");
//			md.update(pass.getBytes("UTF-8"));
//
//			byte[] digest = md.digest();
//			System.out.println("digest: " + toList(digest));
//			System.out.println("digest.len: " + new String(digest));
//			System.out.println("digest.len: " + digest.length);
//
//			System.out.println("digest.base16: " + toBase16(toList(digest)));
//
//			byte[] encodeBase64 = Base64.encodeBase64(digest);
//			System.out.println("encodeBase64: " + toList(encodeBase64));
//			System.out.println("encodeBase64: " + new String(encodeBase64));
//			System.out.println("encodeBase64 len: "
//					+ new String(encodeBase64).length());
			List<String> lista=new ArrayList<String>();
			lista.add("20086310482");
			lista.add("27314431621");
			lista.add("20285609993");
			lista.add("27312337997");
			lista.add("27309397555");
			lista.add("27392041937");
			lista.add("27343919498");
			lista.add("27349659439");
			lista.add("27362941461");
			lista.add("20342775382");
			lista.add("27394664591");
			lista.add("20361152817");
			lista.add("20341287279");
			lista.add("27288841824");
			lista.add("27341645331");
			lista.add("27382154814");
			lista.add("20347261875");
			lista.add("27319982758");
			lista.add("20242725086");
			lista.add("27321815397");
			lista.add("20263442181");
			lista.add("11111111111");
			lista.add("20999999999");
			lista.add("27238067648");
			lista.add("27374322287");
			lista.add("20365583618");
			lista.add("27116123024");
			lista.add("27251001591");
			lista.add("20384271910");
			lista.add("20127639893");
			lista.add("27334245840");
			lista.add("27219388476");
			lista.add("20380034701");
			lista.add("27215586257");
			lista.add("20380695848");
			lista.add("27335044768");
			lista.add("27343299961");
			lista.add("20394595889");
			lista.add("20314233744");
			lista.add("23366885919");
			lista.add("20357115273");
			lista.add("20370757837");
			lista.add("20109752550");
			lista.add("20065576962");
			lista.add("23204731969");
			lista.add("27295374891");
			lista.add("27366927722");
			lista.add("27257395389");
			lista.add("20361568789");
			lista.add("20275298973");
			lista.add("27165377988");
			lista.add("20182216756");

			for (String elem:lista){
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(elem.getBytes("UTF-8"));
				byte[] digest = md.digest();
				byte[] encodeBase64 = Base64.encodeBase64(digest);
//				System.out.println(elem +";"+ new String(encodeBase64));
				System.out.println(new String(encodeBase64));
			}
		} catch (Exception e) {
			System.out.println("mal!!!!");
			e.printStackTrace();
		}
	}

	private static String toBase16(List<Byte> list) {
		StringBuffer sb = new StringBuffer();
		for (Byte byte1 : list) {
			int int1 = 0;
			if (byte1 < 0) {
				int1 = 256 + byte1;
			} else {
				int1 = byte1;
			}
			sb.append(decToHexa(int1 / 16));
			sb.append(decToHexa(int1 % 16));
		}

		return sb.toString();
	}

	private static char decToHexa(int i) {
		return "0123456789ABCDEF".charAt(i);
	}

	private static List<Byte> toList(byte[] encodeBase64) {
		List<Byte> bytes = new ArrayList<Byte>();
		for (byte b : encodeBase64) {
			bytes.add(b);
		}
		return bytes;
	}

	public static String generarPassword() {
		String passNuevo = String.valueOf(new Random().nextInt());
		passNuevo = PasswordUtils.encrypt(passNuevo).replaceAll("[^a-zA-Z0-9]",
				"");
		passNuevo = passNuevo.length() > 8 ? passNuevo.substring(0, 8)
				: passNuevo;
		return passNuevo;
	}
}
