package net.latin.server.utils.helpers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.latin.server.utils.exceptions.LnwException;



public class FileUtils {
	private static Log LOG = LogFactory.getLog(FileUtils.class);

	public static void copyFile(String inputFileName, String outputFileName) {
		FileChannel sourceChannel = null;
		FileChannel destinationChannel = null;

		try {
			sourceChannel = new FileInputStream(inputFileName).getChannel();
			destinationChannel = new FileOutputStream(outputFileName).getChannel();

			sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
		}
		catch (IOException e) {
			// TODO Revisar este manejo de excepciones
			throw new RuntimeException(e);
		}
		finally {
			closeFile(inputFileName, sourceChannel);
			closeFile(inputFileName, destinationChannel);
		}
	}

	private static void closeFile(String fileName, FileChannel channel) {
		try {
			channel.close();
		}
		catch (IOException exception) {
			LOG.warn("No se pudo cerrar el archivo: " + fileName, exception);
		}
	}

	public static Collection<File> getFiles(String folderName, String regEx) {
		// REVIEW No tenemos acceso a CollectionFactory desde aquí.
		Collection<File> files = new ArrayList<File>();
		File folder = new File(folderName);
		for(File file : folder.listFiles()) {
			if(file.isFile() && file.getName().matches(regEx)) {
				files.add(file);
			}
		}
		return files;
	}

	public static void deleteRecursive(File file) {
		if(file.isDirectory()) {
			for(File child : file.listFiles()) {
				deleteRecursive(child);
			}
		}
		file.delete();
	}


	/**
	 * Lee un imputStream y lo cierra
	 * @return
	 */
	public static String read(InputStream stream) {
		StringBuilder builder = new StringBuilder();
		try {
			readStream(stream, builder);
			return builder.toString();
		}
		finally {
			try {
				stream.close();
			}
			catch (IOException e) {
				//En un finally solo puedo loguear
				e.printStackTrace();
			}
		}
	}

	/**
	 * 	readStream(stream, builder, 4096)
	 * @see FileUtils#readStream(InputStream, StringBuilder, int)
	 */
	public static void readStream(InputStream stream, StringBuilder builder) {
		readStream(stream, builder, 4096);
	}

	/**
	 * Lee un stream con un bufer, y lo pone en el StringBuilder
	 * @param stream
	 * @param builder
	 * @param bufSize
	 */
	public static void readStream(InputStream stream, StringBuilder builder, int bufSize) {
		byte buf[] = new byte[bufSize];
		int nSize = read(stream, buf);
		while (nSize >= 0) {
			builder.append(new String(buf, 0, nSize));
			nSize = read(stream, buf);
		}
	}

	private static int read(InputStream stream, byte[] buf) {
		try {
			return stream.read(buf);
		}
		catch (IOException e) {
			throw new LnwException("No se pudo leer de " + stream, e);
		}
	}
	public static String hashCode(String s) {
		File file = new File(s);
		return hashCode(file);
	}

	public static String hashCode(File file) {
		String s = "MD5";
		byte abyte0[] = new byte[(int)file.length()];
		MessageDigest messagedigest;
		try {
			FileInputStream fileinputstream = new FileInputStream(file);
			BufferedInputStream bufferedinputstream = new BufferedInputStream(fileinputstream);
			bufferedinputstream.read(abyte0);
			messagedigest = MessageDigest.getInstance(s);
			fileinputstream.close();
		}
		catch (FileNotFoundException filenotfoundexception) {
			throw (new LnwException("No se pudo acceder al archivo " + file.getPath() + " para generar el hashCode", filenotfoundexception)).addMessage("error en clase FileUtils").addMessage("error en metodo: String hashCode(File file)");
		}
		catch (NoSuchAlgorithmException nosuchalgorithmexception) {
			throw LnwException.wrap("No se reconocio el metodo " + s + " para generar el hashCode", nosuchalgorithmexception).addMessage("error en clase FileUtils").addMessage("error en metodo: String hashCode(File file)");
		}
		catch (IOException ioexception) {
			throw LnwException.wrap("Error de entrada/salida al acceder al archivo " + file.getPath() + " para generar el hashCode", ioexception).addMessage("error en clase FileUtils").addMessage("error en metodo: String hashCode(File file)");
		}
		messagedigest.reset();
		messagedigest.update(abyte0);
		byte abyte1[] = messagedigest.digest();
		return MathUtils.byteToHexString(abyte1);
	}

	public static String creteUniqueFileName(String s) {
		return s + (new Date()).getTime();
	}

	public static boolean exist(String s) {
		File file = new File(s);
		return file.exists();
	}

	public static boolean delete(String s) {
		File file = new File(s);
		return file.delete();
	}
	
	public static void delete(File fileToDelete) {
		if(!fileToDelete.exists())
			throw new RuntimeException("No se pudo borrar el archivo: "+fileToDelete.getName() + " porque no existe");
			
		if(!fileToDelete.delete())
			throw new RuntimeException("No se pudo borrar el archivo: "+fileToDelete.getName());
	}
	
	public static String getFileExtension(String fileName) {
		return fileName.substring(fileName.lastIndexOf(".")+1, fileName.length());
	}

	public static String getFileExtension(File f) {
		return getFileExtension(f.getName());
	}



	public static void writeByteArrayToFile(String s, byte[] data) throws IOException {
		File file = new File(s);
		org.apache.commons.io.FileUtils.writeByteArrayToFile(file, data);
	}


	/**
	 * Dado un {@link File} lee el contenido completo y lo mete en un byte[]
	 *
	 * @param file Archivo a leer y poner el contenido en el byte[] devuelto.
	 *
	 * @return byte[] con el contenido completo del {@link File} indicado.
	 *
	 * @throws NullPointerException Si file es null.
	 * @throws LnwException Si el file no existe, si no se puede leer, si es
	 * 						demasiado grande para un byte[], si no se pudo leer
	 * 						completamente y en cualquier otro caso desconocido.
	 */
    public static byte[] getBytesFromFile(File file) {
    	if(file == null) {
    		throw new NullPointerException("file can not be null");
    	}

    	if(!file.exists()) {
    		throw new LnwException("The file " + file.getName() + " does not exists");
    	}

    	if(!file.canRead()) {
    		throw new LnwException("The file " + file.getName() + " can not be readed");
    	}

    	try {
	        InputStream is = new FileInputStream(file);

	        // Get the size of the file
	        long length = file.length();

	        if (length > Integer.MAX_VALUE) {
	        	throw new LnwException("File is too large")
	        				.addMessage("Length: " + length)
	        				.addMessage("Max Length: " + Integer.MAX_VALUE);
	        }

	        // Create the byte array to hold the data
	        byte[] bytes = new byte[(int)length];

	        // Read in the bytes
	        int offset = 0;
	        int numRead = 0;
	        while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
	            offset += numRead;
	        }

	        // Ensure all the bytes have been read in
	        if (offset < bytes.length) {
	            throw new LnwException("Could not completely read file " + file.getName());
	        }

	        // Close the input stream and return bytes
	        is.close();
	        return bytes;
    	} catch (Exception e) {
    		throw LnwException.wrap("Could not get byte array from file " + file.getName(), e);
		}
    }

}
