package net.latin.server.utils.caseMaker.domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.TransformerException;

import org.jdom2.Document;
import org.jdom2.Element;

import net.latin.server.security.config.LnwGeneralConfig;
import net.latin.server.utils.caseMaker.domain.compositeCarpetas.CarpetaComponible;
import net.latin.server.utils.caseMaker.domain.compositeCarpetas.CarpetaCompuesta;
import net.latin.server.utils.caseMaker.domain.compositeCarpetas.CarpetaSimple;
import net.latin.server.utils.caseMaker.domain.fileMaker.FileData;
import net.latin.server.utils.caseMaker.domain.fileMaker.FileMaker;
import net.latin.server.utils.caseMaker.domain.fileMaker.FolderData;
import net.latin.server.utils.caseMaker.domain.fileMaker.XslUtils;
import net.latin.server.utils.resources.ResourceFinder;
import net.latin.server.utils.xml.XmlUtils;

/**
 * Singleton encargado de crear las carpetas necesarias
 * para un case.
 *
 * @author Santiago Aimetta
 */
public class CaseMaker {

	private static CaseMaker instance = null;
	private static final String PATHXML = "/net/latin/server/utils/caseMaker/domain/fileMaker/xsl/";
	private List<FileData> fileData = new ArrayList<FileData>();
	private static final String CADENA_CORTE = "src/main/java/";
	private static final String CADENA_CORTE2="src\\main\\java\\";
	private String nombreXml = "useCaseData.xml";


	public CaseMaker() {
	}


	/**
	 * Recibe como parámetro el nombre del caso de uso y la lista
	 * nombres de las ventanas.
	 * a partir de este crea las subcarpetas necesarias para el
	 * case y el nombre del mismo.
	 * @param nombreCase
	 * @param pageNames
	 * @return Salida para consola
	 */
	public String makeUseCase(String nombreCase, List<String> pageNames ) {



		String url = ResourceFinder.getLocalProjectPath()+"/"+LnwGeneralConfig.getInstance().getClientPackagePath();
		System.out.println(url);
		File path = new File(url);

		String pathProyecto = path.getPath();

		//Se crean todas las carpetas
		//lado del cliente
		//String clientPath = pathProyecto+"/client";

		String clientPath = pathProyecto;
		//carpetas simples
		CarpetaComponible rpc = new CarpetaSimple("rpc");
		CarpetaComponible constants = new CarpetaSimple("constants");
		CarpetaComponible pages = new CarpetaSimple("pages");

		//carpeta compuesta
		CarpetaComponible clientCase = new CarpetaCompuesta(nombreCase);
		clientCase.agregarSubCarpeta(rpc);
		clientCase.agregarSubCarpeta(constants);
		clientCase.agregarSubCarpeta(pages);
		CarpetaComponible carpetaClientCase = new CarpetaCompuesta("useCases");
		carpetaClientCase.agregarSubCarpeta(clientCase);
		CarpetaComponible client = new CarpetaCompuesta(clientPath);
		client.agregarSubCarpeta(carpetaClientCase);

		//lado del server
		//String serverPath = pathProyecto+"/server/useCases";

		String serverPath = (new File(pathProyecto)).getParent();
		serverPath+="/server/useCases";
		//carpetas simples
		CarpetaComponible carpetaDocumentacion = new CarpetaSimple("docs");
		CarpetaComponible carpetaCase = new CarpetaCompuesta(nombreCase);
		CarpetaComponible server = new CarpetaCompuesta(serverPath);
		carpetaCase.agregarSubCarpeta(carpetaDocumentacion);
		server.agregarSubCarpeta(carpetaCase);
		//inicia proceso de creacion

		//creo carpetas client
		for (String pathAux : client.getPath()) {
			System.out.println("Creada carpeta: "+pathAux);
			File carpeta = new File(pathAux);
			carpeta.mkdirs();
			this.fileData.add(new FileData(pathAux));
		}
		//creo carpetas server
		for (String pathAux : server.getPath()) {
			System.out.println("Creada carpeta: "+pathAux);
			File carpeta = new File(pathAux);
			carpeta.mkdirs();
			this.fileData.add(new FileData(pathAux));
		}

		//recorro todo y agrego los nombres y convierto a package
		//me baso en el orden de ingreso para ir metiendolos en el
		//xml, su orden queda harcodeado
		//Tambien armo el folderData con las ubicaciones
		//físicas
		//Ejemplo de el orden
//		C:\development\eclipse\workspace\sma\src/client//useCases/testCase/rpc
//		C:\development\eclipse\workspace\sma\src/client//useCases/testCase/constants
//		C:\development\eclipse\workspace\sma\src/client//useCases/testCase/pages
//		C:\development\eclipse\workspace\sma\src/server/useCases//testCase

		//Capitalizo el nombre del case
		String caseName = XslUtils.capitalize(nombreCase);
		//sirve para armar los nombres de las clases


		//Creo el archivo xml para el proyecto para utilizarlo en la transformación xsl
		//de los .java

		Document document = new Document();
		Element bean = new Element( "bean" );
		Element useCaseName = new Element("useCaseName");
		useCaseName.setText(caseName);
		bean.addContent(useCaseName);
		//Agrego el nombre de la aplicación al XML
		Element applicationName = new Element("appName");
		applicationName.setText(XslUtils.toLowerCase(LnwGeneralConfig.getInstance().getApplicationName()));
		bean.addContent(applicationName);

		String text ="";
		//Creo del folderData
		FolderData folderData = new FolderData();
		//RPC
		Element rpcT = new Element("rpc");
		//convierto a package
		String rpcPath = fileData.get(0).getPath();
		text = getPackage(rpcPath, caseName);
		folderData.setRpcPackage(text);
		rpcT.setText(text);
		bean.addContent(rpcT);
		folderData.setRpcFolder(rpcPath);
		//GROUP
		Element groupT = new Element("group");
		//convierto a package
		String groupPath = clientPath+"/useCases/"+nombreCase+"/";
		text = getPackage(groupPath, caseName);
		folderData.setClientPackage(text);
		groupT.setText(text);
		bean.addContent(groupT);
		folderData.setGroupFolder(groupPath);
		//CONSTANTS
		Element constantT = new Element("constants");
		String constantsPath = fileData.get(1).getPath();
		//convierto a package
		text = getPackage(constantsPath,caseName);
		folderData.setConstantsPackage(text);
		constantT.setText(text);
		bean.addContent(constantT);
		folderData.setConstantFolder(constantsPath);
		//PAGES
		Element pagesT = new Element("pages");
		String pagesPath = fileData.get(2).getPath();
		//convierto a package
		text = getPackage(pagesPath, caseName);
		folderData.setPagesPackage(text);
		pagesT.setText(text);
		bean.addContent(pagesT);
		folderData.setPagesFolder(pagesPath);
		//SERVER
		Element serverT = new Element("server");
		//vuelvo al padre, ya q si no lo hago creo el case en docs
		String serverTPath = new File(fileData.get(3).getPath()).getParent();
		//convierto a package
		text = getPackage(serverTPath, caseName);
		folderData.setServerPackage(text);
		serverT.setText(text);
		bean.addContent(serverT);
		folderData.setServerFolder(serverTPath);

		//Agrego al xml todo lo necesario para las multiples ventanas
		//es obligatorio que la coleccion de nombres tenga al menos 1 elemento
		Element firstPageT = new Element("firstPage");
		firstPageT.setText(pageNames.get(0));
		bean.addContent(firstPageT);

		for (String pName : pageNames) {
				Element nombreP = new Element("pageName");
				nombreP.setText(pName);
				bean.addContent(nombreP);
		}

		document.setRootElement( bean );


		File res = new File(ResourceFinder.getFile( PATHXML ).getPath()+"/"+nombreXml) ;
		XmlUtils.saveDocument(document, res.getAbsolutePath() );
		System.out.println(res.getAbsolutePath());

		//Creo todos los archivos
		try {
			FileMaker.getInstance().makeFiles(pathProyecto,nombreCase,folderData, pageNames);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("No se encontró el archivo xml "+res.getAbsolutePath());
		} catch (TransformerException e) {
			throw new RuntimeException("Fallá la transformación xsl");
		}


		//Genero las salidas para la consola
		String salidaConsola = "/*Configure el texto a pegar en GeneralConfig.xml*/\n";
		salidaConsola+= "<!--"+XslUtils.toConstantFormat(XslUtils.capitalize(nombreCase))+" -->\n";
		salidaConsola+="<Group id='"+nombreCase+"' title='COMPLETAR'>\n";
		salidaConsola+="\t<UseCase id='"+nombreCase+"'  title='COMPLETAR'/>\n";
		salidaConsola+="</Group>";
		salidaConsola+="\n";
		return salidaConsola;





	}




	private String getPackage(String path,String useCaseName){

		//Busco la cadena de corte //src si no esta va a tirar exception y mostrar por pantalla
		//q no se pudieron generar los archivos
		String useCaseNameAux =  Character.toLowerCase(useCaseName.charAt(0)) + useCaseName.substring(1);

		//Esto es muy sucio pero es para evitar que se rompa cuando cambia la forma
		//del path.
		String packAux ="";
		try{
			packAux = path.substring((path.indexOf(CADENA_CORTE)));
		}
		catch ( StringIndexOutOfBoundsException e){
			packAux = path.substring((path.indexOf(CADENA_CORTE2)));
		}

		packAux = packAux.replace("/", ".");
		packAux = packAux.replace("\\", ".");
		if(packAux.endsWith(".")){
			packAux = (String) packAux.subSequence(0, packAux.lastIndexOf("."));

		}
		//Corto la parte de src
//		int indice = packAux.substring(path.lastIndexOf(CADENA_CORTE));
		packAux = packAux.substring(CADENA_CORTE.length());
		return packAux;
	}

}
