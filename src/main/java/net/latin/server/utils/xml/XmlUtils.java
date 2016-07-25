package net.latin.server.utils.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * Utilidades generales sobre manejo de archivos XML
 * @author Matias Leone, Fernando Diaz
 *
 */
public class XmlUtils {

	private XmlUtils() {

	}

	/**
	 * Carga el documento especificado por path, utilizando JDOM
	 * @param path: direccion del xml
	 * @return Document xml
	 */
	public static Document loadDocument( String path ) {
		return loadDocument(path, true);
	}

	/**
	 * Carga el documento especificado por path, utilizando JDOM
	 * @param path: direccion del xml
	 * @param validationActive: validaciones activas
	 * @return Document xml
	 */
	public static Document loadDocument( String path, boolean validationActive ) {
		try {
			XMLReaders validation = validationActive? XMLReaders.DTDVALIDATING: XMLReaders.NONVALIDATING;
			SAXBuilder builder = new SAXBuilder( validation );
			Document doc = builder.build( new File( path ) );
			return doc;
		} catch (JDOMException e) {
			throw new RuntimeException("Error al intentar cargar un xml desde el path: " + path, e);
		} catch (IOException e) {
			throw new RuntimeException("Error al intentar cargar un xml desde el path: " + path, e);
		}

	}

	/**
	 * Carga el documento desde un input stream, utilizando JDOM
	 * @param path: direccion del xml
	 * @param validationActive: validaciones activas
	 * @return Document xml
	 */
	public static Document loadDocument( InputStream input, boolean validationActive ) {
		try {
			XMLReaders validation = validationActive? XMLReaders.DTDVALIDATING: XMLReaders.NONVALIDATING;
			SAXBuilder builder = new SAXBuilder( validation );
			Document doc = builder.build( input );
			return doc;
		} catch (JDOMException e) {
			throw new RuntimeException("Error al intentar cargar un xml");
		} catch (IOException e) {
			throw new RuntimeException("Error al intentar cargar un xml");
		}
	}

	public static Document loadDocumentFromString( String strXml ) {
		try {
			SAXBuilder builder = new SAXBuilder( XMLReaders.NONVALIDATING );
			StringReader reader = new StringReader( strXml );
			Document doc = builder.build( reader );
			return doc;
		} catch (JDOMException e) {
			throw new RuntimeException("Error al intentar cargar un xml desde un string. Xml: " + strXml, e);
		} catch (IOException e) {
			throw new RuntimeException("Error al intentar cargar un xml desde un string. Xml: " + strXml, e);
		}
	}

	/**
	 * Graba el documento JDOM en el path especificado
	 */
	public static void saveDocument( Document doc, String path ) {
		try {
			Format format = Format.getPrettyFormat();
			XMLOutputter fmt = new XMLOutputter( format );
			File file = new File( path );
			OutputStream stream = new FileOutputStream( file );
			fmt.output( doc, stream );
		} catch (FileNotFoundException e) {
			throw new RuntimeException( "No se encontro el archivo: " + path, e );
		} catch (IOException e) {
			throw new RuntimeException( "Error al guardar el archivo: " + path, e );
		}
	}
	
	/**
	 * Devuelve el xml como un String
	 */
	public static String getStringFromXml( Document doc ){
		Format format = Format.getPrettyFormat();
		XMLOutputter fmt = new XMLOutputter( format );
		return fmt.outputString(doc);
	}




}
