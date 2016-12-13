package net.latin.server.utils.caseMaker.domain.fileMaker;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Set;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Singleton
 * Crea un .Java con el xml y el xsl aporpiados y
 * los situa en el package especifico
 *
 * @author Santiago Aimetta
 */
public class XslTransformer {
	private static XslTransformer instance = null;




	private XslTransformer() {
	}

	public  static XslTransformer getInstance(){
		if( instance == null ){
			instance = new XslTransformer();
		}
		return instance;
	}

/**
 * Crea un archivo fileName a partir de un xml y un xsl
 * Ejemplo de invocacion
 *
 * getJavaFile("C:\bla.xml","C:\bla.xsl","bla.java","c:\archivosGenerado\","miCasoDeUso");
 *
 * @param pathXml
 * @param pathXsl
 * @param fileName
 * @param path
 * @param UseCaseName
 *
 * s@throws FileNotFoundException
 * @throws TransformerConfigurationException
 * @throws FileNotFoundException
 * @throws TransformerException
 */
	public void transform( String pathXml, String pathXsl, String fileName, String path) throws TransformerConfigurationException, FileNotFoundException{


		Transformer transformer = getTransformer(pathXsl);
		//realizo la transformacion
		try {
			 StreamResult result  = new StreamResult(new FileOutputStream(path+fileName));

			transformer.transform(new StreamSource(pathXml), result);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FileNotFoundException();
		} catch (TransformerException e) {
			e.printStackTrace();
			throw new TransformerConfigurationException();
		}


	}




	public void transformUsingParams( String pathXml, String pathXsl, String fileName, String path, String paramName, String paramValue) throws TransformerConfigurationException, FileNotFoundException{


		Transformer transformer = getTransformer(pathXsl);
		//realizo la transformacion
		try {
			 StreamResult result  = new StreamResult(new FileOutputStream(path+fileName));
			 transformer.setParameter(paramName, paramValue);
			transformer.transform(new StreamSource(pathXml), result);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FileNotFoundException();
		} catch (TransformerException e) {
			e.printStackTrace();
			throw new TransformerConfigurationException();
		}


	}



	public void transformUsingParams(String pathXml, String pathXsl, String fileName, String path, Map <String,Object> mapaParametros) throws TransformerConfigurationException, FileNotFoundException{


		Transformer transformer = getTransformer(pathXsl);
		//realizo la transformacion
		try {
			 StreamResult result  = new StreamResult(new FileOutputStream(path+fileName));
			 stearParametros(transformer,mapaParametros).transform(new StreamSource(pathXml), result);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FileNotFoundException();
		} catch (TransformerException e) {
			e.printStackTrace();
			throw new TransformerConfigurationException();
		}


	}

	private Transformer stearParametros(Transformer transformer, Map <String,Object> mapaParametros) {

		for (Map.Entry<String, Object> entry : (Set<Map.Entry<String, Object>>) mapaParametros.entrySet()) {
			if(entry.getValue() != null){
				transformer.setParameter(entry.getKey(), entry.getValue().toString());
			}
		}
		return transformer;

	}

	private Transformer getTransformer(String pathXsl)
			throws TransformerFactoryConfigurationError,
			TransformerConfigurationException {
		//Me traigo una instancia del factory para crear mi transformer
		TransformerFactory tFactory = TransformerFactory.newInstance();
		//me creo el transformer con el xsl pasado como parametro
		Transformer transformer = null;
		try {
			transformer = tFactory.newTransformer(new StreamSource(pathXsl));
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
			throw new TransformerConfigurationException();
		}
		return transformer;
	}

}
