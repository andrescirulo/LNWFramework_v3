package net.latin.server.utils.caseMaker.domain.fileMaker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.xml.transform.TransformerException;

import org.jdom2.Document;
import org.jdom2.Element;

import net.latin.server.security.config.LnwGeneralConfig;
import net.latin.server.utils.resources.ResourceFinder;
import net.latin.server.utils.xml.XmlUtils;




/**
 * Singleton
 *
 * Se encarga de crear los archivos a partir del nombre del caso de
 * uso pasado por la interfaz de case maker.
 *
 * @author Santiago Aimetta
 */
public class FileMaker {



	private static final String USECASENAMES_STOP = "UseCaseNames";
	private static final String CONTROLLER_STOP = "registerPageGroups";
	private static final String CONTROLLER_IMPORT_STOP = "GwtController";
	private static final String FILE_MAKER_XLS_PATH = "/net/latin/server/utils/caseMaker/domain/fileMaker/xsl/";
	private final String pathUseCaseData = FILE_MAKER_XLS_PATH + "useCaseData.xml";
	private final String pathGwtPage = FILE_MAKER_XLS_PATH + "xslGwtPage.xsl";
	private final String pathGwtPageGroup = FILE_MAKER_XLS_PATH + "xslGwtPageGroup.xsl";
	private final String pathGwtRpcInterface = FILE_MAKER_XLS_PATH + "xslGwtRpcInterface.xsl";
	private final String pathGwtRpcInterfaceAsync = FILE_MAKER_XLS_PATH + "xslGwtRpcInterfaceAsync.xsl";
	private final String pathGwtUseCase = FILE_MAKER_XLS_PATH + "xslGwtUseCase.xsl";
	private final String pathConstants = FILE_MAKER_XLS_PATH + "xslConstants.xsl";
	private final String comentario = "//NO BORRAR ESTE COMENTARIO,SIRVE PARA GENERACION AUTOMATICA DE CASE MAKER";
	private static FileMaker instancia = null;
	private final String nombreUseCaseNames = "/UseCaseNames.java";
	private final String nombreController = "Controller.java";
	//para generar documentacion html
//	private final String pathTemplateUseCaseSpec = FILE_MAKER_XLS_PATH + "templateSpec.xsl";
//	private final String folderOut = "/docs";

	private  FileMaker() {
		// TODO Auto-generated constructor stub
	}

	public static FileMaker getInstance(){
		if ( instancia == null ){
			instancia = new FileMaker();
		}
		return instancia;
	}
	/**
	 * Crea los archivos .java a partir  a partir de los datos pasados
	 * por la ui de caseMaker.
	 * @param pathUseCase
	 * @param useCaseName
	 * @param pageNames
	 * @param folderData
	 * @throws TransformerException
	 * @throws FileNotFoundException
	 */
	public  void makeFiles(String pathUseCase, String useCaseName, FolderData folderData, List<String> pageNames ) throws FileNotFoundException, TransformerException{

		//pongo el nombre del casoDeUso en mayúscula por que lo voy a necesitar
		//para crear las clases
		String caseName = XslUtils.capitalize(useCaseName);

		//Comienzo a generar los archivos
		//genero la ubicación de los archivos
		File pathBase = ResourceFinder.getFile( "." );
		File resXMl = ResourceFinder.getFile( pathUseCaseData );
		File resXSl = ResourceFinder.getFile( pathGwtRpcInterface );

		//creo la interfaz Client
		XslTransformer.getInstance()
		                  .transform(resXMl.getAbsolutePath(),
		                   resXSl.getAbsolutePath(), caseName+"Client.java", folderData.getRpcFolder()+"/");
		//creo la interfaz ClientAsync
		resXSl = ResourceFinder.getFile( pathGwtRpcInterfaceAsync );

		XslTransformer.getInstance()
        .transform(resXMl.getAbsolutePath(),
         resXSl.getAbsolutePath(), caseName+"ClientAsync.java", folderData.getRpcFolder()+"/");

		//Creo constant

		resXSl = ResourceFinder.getFile( pathConstants );

		XslTransformer.getInstance()
        .transform(resXMl.getAbsolutePath(),
         resXSl.getAbsolutePath(), caseName+"Constants.java", folderData.getConstantFolder()+"/");

		//Creo las pages
		resXSl = ResourceFinder.getFile( pathGwtPage );

		for (int i = 0; i < pageNames.size(); i++) {
			XslTransformer.getInstance()
				.transformUsingParams(resXMl.getAbsolutePath(),
				resXSl.getAbsolutePath(), pageNames.get(i)+".java", folderData.getPagesFolder()+"/","pageName",pageNames.get(i));
		}

		//Creo el group
		resXSl = ResourceFinder.getFile( pathGwtPageGroup );

		XslTransformer.getInstance()
        .transform(resXMl.getAbsolutePath(),
         resXSl.getAbsolutePath(), caseName+"Group.java", folderData.getGroupFolder()+"/");

		//Creo la parte del server

		resXSl = ResourceFinder.getFile( pathGwtUseCase );

		XslTransformer.getInstance()
        .transform(resXMl.getAbsolutePath(),
         resXSl.getAbsolutePath(), caseName+"Case.java", folderData.getServerFolder()+"/");

		//Creo la documentacion (Nadie quiere la documentacion, la comentamos)
		/*resXSl = ResourceFinder.getFile( pathTemplateUseCaseSpec );

		Map <String,Object> mapaParam = new HashMap<String,Object>();
		mapaParam.put("fecha", new Date());
		mapaParam.put("pageName",pageNames);

		XslTransformer.getInstance()
        .transformUsingParams(resXMl.getAbsolutePath(),
         resXSl.getAbsolutePath(), caseName+"Specification.mht", folderData.getServerFolder()+folderOut+"/",mapaParam);*/

		agregarEntradaUseCaseNames(useCaseName, pathUseCase);
		agregarEntradaStrutsConfig(useCaseName,folderData);
		agregarEntradaController(useCaseName, pathUseCase);




	}
	private  void agregarEntradaController(String nombreCase,String pathFile){
		//this.addPageGroup(UseCaseNames.MUSICA_ABM, new MusicaAbmGroup());
		String useCaseName = XslUtils.capitalize(nombreCase);
		String linea;
		
		String pathController = pathFile +"\\"+ LnwGeneralConfig.getInstance().getApplicationName()+nombreController;
		linea = "import " + 
				LnwGeneralConfig.getInstance().getApplicationName().toLowerCase() + 
				"." + "client" + "." + "useCases" + "." + nombreCase + "." + useCaseName + "Group" + ";";
		
		agregarImport(pathController, linea, CONTROLLER_IMPORT_STOP );

		linea = "\t" + "this.addPageGroup(UseCaseNames." +
			XslUtils.toConstantFormat(useCaseName) +
			", new " + 
			useCaseName + 
			"Group());" + "\n";
		linea += "\n" + "\t" + "}";
		
		agregarEntrada(pathController, linea, CONTROLLER_STOP);
	}

	private  void agregarEntradaStrutsConfig(String useCaseName,
		FolderData folderData) {
		String pathStruts = ResourceFinder.getLocalProjectPath()+"/"+LnwGeneralConfig.getInstance().getStrutsConfigUseCasesPath();
		String caseDir = folderData.getServerPackage();
		String nombreCase = XslUtils.capitalize((useCaseName));
		caseDir = caseDir +"."+nombreCase+"Case";

		String struts = "<!--  "+XslUtils.toConstantFormat(nombreCase)+"  --!>\n";
		struts = struts +"<action path='/";
		struts = struts + useCaseName;
		struts = struts + "'\n";
		struts = struts + "     name='DynamicForm'\n";
		struts = struts + "     type='"+caseDir+"'\n";
		struts = struts + "     scope='request'>\n";
		struts = struts + "</action>";
		System.out.println("Se agregó : \n+"+pathStruts);

		//Agrego el texto generado en el struts-config del proyecto
		Document document = XmlUtils.loadDocument( pathStruts, false );
		Element root = document.getRootElement();
//		Element amaps = root.getChild("action-mappings");
		Element amaps = root.getChild("package");
		Element action = new Element("action");
//		action.setAttribute("path", "/"+useCaseName);
//		action.setAttribute("name","DynamicForm");
//		action.setAttribute("type",caseDir );
//		action.setAttribute("scope","request");
		action.setAttribute("name", useCaseName);
		action.setAttribute("class",caseDir );
		amaps.addContent(action);
		XmlUtils.saveDocument(document, pathStruts );



	}
//TODO REGISTRAR EN CONTROLLER
	private  void agregarEntradaUseCaseNames(String nombreCase,String pathFile) {
		//creo el string para agregar
		//public static final String TEST_CASE = null;
		String useCaseName = XslUtils.capitalize(nombreCase);
		String linea = "public static final String "+XslUtils.toConstantFormat(useCaseName)+" = \""+nombreCase+"\";\n";
		//linea += comentario;
		linea += "\n" + "}";
		agregarEntrada(pathFile+nombreUseCaseNames, linea,USECASENAMES_STOP);
	}

	/**
	 * Agrega al archivo especificado una nueva linea donde aparece la cadena de corte,
	 * volver a poner la cadena de corte despues
	 * @param pathFIle
	 * @param newLine
	 * @param cadenaDeCorte
	 */
//	    private  static void agregarEntrada(String pathFIle, String newLine,String cadenaDeCorte) {
//	    	File aFile = new File(pathFIle);
//	    	StringBuffer contents = new StringBuffer();
//			BufferedReader input = null;
//			try {
//			      input = new BufferedReader( new FileReader(aFile) );
//			      String line = null;
//
//			      while (( line = input.readLine()) != null){
//			    	  //antes de que aparesca el } final agrego la nueva linea }
//			    	  if(line.matches(cadenaDeCorte)){
//			        	newLine ="\t"+newLine;
//			        	line = newLine;
//			        }
//			    	contents.append(line);
//			        contents.append(System.getProperty("line.separator"));
//			      }
//
//
//			        if (!aFile.canWrite()) {
//			          throw new IllegalArgumentException("No se pudo escribir el archivo: " + aFile);
//			        }
//
//			        Writer output = null;
//			        try {
//			          output = new BufferedWriter( new FileWriter(aFile) );
//			          output.write( contents.toString() );
//			        }
//			        finally {
//			          if (output != null) output.close();
//			        }
//
//			    }
//			 catch (FileNotFoundException ex) {
//			      ex.printStackTrace();
//			    }
//			    catch (IOException ex){
//			      ex.printStackTrace();
//			    }
//			    finally {
//			      try {
//			        if (input!= null) {
//			          input.close();
//			        }
//			      }
//			      catch (IOException ex) {
//			        ex.printStackTrace();
//			      }
//			    }
//			  }
    private  static void agregarEntrada(String pathFIle, String newLine,String cadenaDeCorte) {
    	File aFile = new File(pathFIle);
    	StringBuffer contents = new StringBuffer();
		BufferedReader input = null;
		try {
		      input = new BufferedReader( new FileReader(aFile) );
		      String line = null;

		      while (( line = input.readLine()) != null){
		    	  //antes de que aparesca el } final agrego la nueva linea }
		    	  if(line.contains(cadenaDeCorte)){
		    		  //agrego la linea con la firma
		    		  contents.append(line);
		    		  contents.append(System.getProperty("line.separator"));
		    		  while(!(line=input.readLine()).contains("}")){
		    			  //leo hasta encontrar una }
		    			  //meto todo el contenido del metodo
		    			  contents.append(line);
		    			  contents.append(System.getProperty("line.separator"));
		    		  }
		    		  //sali del while xq encontre un }
		    		  newLine ="\t"+newLine;
		    		  line = newLine;
		        }
		    	contents.append(line);
		        contents.append(System.getProperty("line.separator"));
		      }


		        if (!aFile.canWrite()) {
		          throw new IllegalArgumentException("No se pudo escribir el archivo: " + aFile);
		        }

		        Writer output = null;
		        try {
		          output = new BufferedWriter( new FileWriter(aFile) );
		          output.write( contents.toString() );
		        }
		        finally {
		          if (output != null) output.close();
		        }

		    }
		 catch (FileNotFoundException ex) {
		      ex.printStackTrace();
		    }
		    catch (IOException ex){
		      ex.printStackTrace();
		    }
		    finally {
		      try {
		        if (input!= null) {
		          input.close();
		        }
		      }
		      catch (IOException ex) {
		        ex.printStackTrace();
		      }
		    }
		  }
    
	private void agregarImport(String pathFile, String newLine, String cadenaDeCorte) {
		boolean ingresado = false;
    	File aFile = new File(pathFile);
    	StringBuffer contents = new StringBuffer();
		BufferedReader input = null;
		try {
			input = new BufferedReader( new FileReader(aFile) );
			String line = null;
			String line_aux = null;
			
			while ( ( line = input.readLine() ) != null ) {
				if( !ingresado && line.trim().equals( "" ) ) {
					if ( ( line_aux = input.readLine() ) != null ) {
						if ( line_aux.contains( cadenaDeCorte ) ) {
							contents.append( newLine );
							contents.append( System.getProperty( "line.separator" ) );
							ingresado = true;
						}
						contents.append( line );
						contents.append( System.getProperty( "line.separator" ) );
						line = line_aux;
					} 
				}
				contents.append( line );
				contents.append( System.getProperty( "line.separator" ) );
			}

			if (!aFile.canWrite()) {
				throw new IllegalArgumentException("No se pudo escribir el archivo: " + aFile);
			}
			
			Writer output = null;
			try {
				output = new BufferedWriter( new FileWriter(aFile) );
				output.write( contents.toString() );
			}
			finally {
				if (output != null) output.close();
			}

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex){
			ex.printStackTrace();
		} finally {
			try {
				if (input!= null) {
					input.close();
				}
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		} 
	}

}
