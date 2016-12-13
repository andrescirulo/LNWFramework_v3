package net.latin.server.utils.deleteUseCase.domain;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;

import net.latin.server.utils.projectData.Nodo;
import net.latin.server.utils.projectData.ProjectData;
import net.latin.server.utils.projectData.UseCaseData;

/**Borra un Caso de Uso
 *
 * @author Maxi Ariosti
 */
public class DeleteUseCase {

	private static final String USE_CASE_NAMES_JAVA = "UseCaseNames.java";
	private static final String STRUTS_CONFIG_USE_CASES_XML = "struts-config-useCases.xml";
	private static final String GENERAL_CONFIG_XML = "GeneralConfig.xml";
	private static final String CONTROLLER_JAVA = "Controller.java";
	private static DeleteUseCase instance;




	public static DeleteUseCase getInstance(){
		if(instance==null){
			instance = new DeleteUseCase();
		}
		return instance;
	}

	/**
	 *
	 * @param caseNameDelete el nombre del Caso de Uso debe respetar la forma Ej: perfilAlta
	 * @param projectPath
	 * @param projectData
	 */
	public void borrarCaso (String caseNameDelete, String projectPath, ProjectData projectData) {


		//  <NAME-PROJECT>Controller.java
		if(!this.procesarController(caseNameDelete,projectPath,projectData.getName())) {return;}

		//UsesCasesNames.java
		if(!this.procesarUseCaseName(caseNameDelete,projectPath,projectData.getName())) {return;}

		//struts-config-UseCase.xml
		if(!this.procesarStrutsConfigUseCase(caseNameDelete,projectData.getStrutsConfigUseCases().getAbsolutePath(),projectData.getName())) {return;}

		//GeneralConfig.xml
		if(!this.procesarGeneralConfig(caseNameDelete,projectData.getGeneralConfig().getAbsolutePath(),projectData.getName())) {return;}

		this.eliminarArchivosUseCase(caseNameDelete,projectData);
	}


	/**Se encarga de borrar todos los archivos que se encuentren dentro de la package de Caso de Uso
	 * como asi tambien el directorio principal del mismo
	 *
	 * @param caseNameDelete nombre del Caso de Uso Ej: perfilAlta
	 * @param projectData
	 */
	private void eliminarArchivosUseCase(String caseNameDelete,ProjectData projectData) {

		List pagesUseCase;
		UseCaseData useCase;
		String packageClientPath = projectData.getAbsolutePath() + "\\src\\" + projectData.getName().toLowerCase()+ "\\client\\useCases\\" + caseNameDelete ;
		String packageServerPath = projectData.getAbsolutePath() + "\\src\\" + projectData.getName().toLowerCase()+ "\\server\\useCases\\" + caseNameDelete ;

		useCase = this.getUseCase(projectData.getUseCases(), caseNameDelete);
		if(useCase == null) {
			System.err.println("ERROR: No exite Caso de Uso <"+ caseNameDelete + "> se cancela la Eliminación.");
			return;
		}
		pagesUseCase = useCase.getPages();


		//**************************************************
		//				C	L	I	E	N	T
		//**************************************************
		System.out.println("   **** ARCHIVOS EN CLIENT *****" + '\n');
		//*****************
		//	elimino pages
		//*****************

		for (Iterator iterator = pagesUseCase.iterator(); iterator.hasNext();) {
			Nodo page = (Nodo) iterator.next();

			this.deleteFile(page.getAbsolutePath());

		}
		this.borrarArchivosCVS(packageClientPath + "\\pages\\");

		//borra el directorio
		this.deleteFile(packageClientPath + "\\pages\\");



		//****************
		//	elimino rpc
		//****************
		this.deleteFile(useCase.getRemoteProcesureCall().getAbsolutePath());
		this.deleteFile(useCase.getRemoteProcedureCallAsynch().getAbsolutePath());
		this.borrarArchivosCVS(packageClientPath + "\\rpc\\");

		//borra el directorio
		this.deleteFile(packageClientPath + "\\rpc\\");



		//***********************
		//	elimino constants
		//************************
		this.deleteFile(useCase.getConstants().getAbsolutePath());
		this.borrarArchivosCVS(packageClientPath + "\\constants\\");

		//borra el directorio
		this.deleteFile(packageClientPath + "\\constants\\");



		//***********************
		//	elimino Group.java
		//************************
		this.deleteFile(useCase.getGroup().getAbsolutePath());


		this.borrarArchivosCVS(packageClientPath);
		if (!this.deleteFile(packageClientPath)) {
			System.err.println("> Existen archivos que debe ser BORRADOS MUNUALEMENTE en el direcrotio del UseCase");
		}


		//**************************************************
		//				S	E	R	V	E	R
		//**************************************************
		System.out.println("   **** ARCHIVOS EN SERVER *****" + '\n');

		String nameDocs = caseNameDelete.substring(0,1).toUpperCase() + caseNameDelete.substring(1) + "Specification.mht";
		//borra el .java del server
		this.deleteFile(useCase.getServerUseCase().getAbsolutePath());
		this.deleteFile(packageServerPath + "\\docs\\" + nameDocs );
		this.borrarArchivosCVS(packageServerPath + "\\docs\\");

		//borro direcotrio docs
		this.deleteFile(packageServerPath + "\\docs\\");
		this.borrarArchivosCVS(packageServerPath);

		//borra el directorio principal del caso de uso en el server
		this.deleteFile(packageServerPath );

	}





	private void borrarArchivosCVS(String pathPackage) {


		this.deleteFileCVS(pathPackage + "CVS\\Entries");
		this.deleteFileCVS(pathPackage + "CVS\\Repository");
		this.deleteFileCVS(pathPackage + "CVS\\Root");
		this.deleteFileCVS(pathPackage + "CVS");


	}

	/**
	 * Devulve un objeto UseCaseData si es que el nombre del Caso se encuentra en la lista de Casos de Uso
	 * @param useCases Lista de Casos de Usos del tipo UseCaseData
	 * @param caseNameDelete nombre del Caso de Uso Ej: perfilAlta
	 * @return un UseCaseData si exite, null caso contrario
	 */
	private UseCaseData getUseCase(List<UseCaseData> useCases, String caseNameDelete) {

		UseCaseData caseFound = null;

		for (Iterator iterator = useCases.iterator(); iterator.hasNext();) {
			UseCaseData useCase = (UseCaseData) iterator.next();

			if (caseNameDelete.equals(useCase.getNombre())) {
				caseFound = useCase;
			}

		}
		return caseFound;
	}

	/**Busca en el GeneralConfig.xml la línea donde figura el caso de Uso
	 * en cuestion que debe borrarse manualmente
	 *
	 * @param caseNameDelete nombre del Caso de Uso Ej: perfilAlta
	 * @param filePath ruta de donde se encuentra el GeneralConfig.xml
	 * @param projectName
	 * @return true si se proceso correctamente el archivo, de lo contrario false
	 */
	private boolean procesarGeneralConfig(String caseNameDelete,String filePath, String projectName) {

		BufferedReader input = null;
		String line;
		int findText=0;


//		String pathFile = projectPath + "\\config\\" + GENERAL_CONFIG_XML;

		input = bufferedReaderFile (filePath,projectName,GENERAL_CONFIG_XML);
		if ( input == null) {
			return false;
		}


		try {
			while ((line = input.readLine())!= null){

				findText++;
				if ( line.contains("id='" + caseNameDelete + "'") ) {
					System.out.println("> Texto encontrado: "+ "'" + caseNameDelete + "'" + "en LINEA " + findText+ " de " + GENERAL_CONFIG_XML + "BORRAR MANUALMENTE");
				}
			}
			input.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	/**Crea un nuevo struts-config-useCases.xml a paritr de excluir la
	 * línea en donde aparece el nombre del caso de uso en cuestión
	 *
	 * @param caseNameDelete nombre del Caso de Uso Ej: perfilAlta
	 * @param filePath ruta de donde se encuentra el struts-config-useCases.xml
	 * @param projectName
	 * @return true si se proceso correctamente el archivo, de lo contrario false
	 */
	private boolean procesarStrutsConfigUseCase(String caseNameDelete, String filePath, String projectName) {

		BufferedReader input = null;
		String line;
		StringBuffer output = new StringBuffer();

		//String nameGroup = caseNameDelete.substring(0,1).toUpperCase() + caseNameDelete.substring(1) + "Group";
//		String pathFile = projectPath + "\\webapp\\WEB-INF\\" + STRUTS_CONFIG_USE_CASES_XML;

		input = bufferedReaderFile (filePath,projectName,STRUTS_CONFIG_USE_CASES_XML);
		if ( input == null) {
			return false;
		}

		try {
			while ((line = input.readLine())!= null){


				if ( !line.contains("path="+"\"/" + caseNameDelete + "\"") ) {
					output.append(line);
					output.append('\n');
				}
			}
			input.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		this.deleteFile(filePath);

		this.createAndWriteFile (filePath, output);

		return true;
	}

	/**
	 *
	 * @param caseNameDelete
	 * @param projectPath
	 * @param projectName
	 * @return
	 */
	private boolean procesarUseCaseName(String caseNameDelete, String projectPath, String projectName) {

		BufferedReader input = null;
		String line;
		StringBuffer output = new StringBuffer();


		//String nameGroup = caseNameDelete.substring(0,1).toUpperCase() + caseNameDelete.substring(1) + "Group";

		String filePath = projectPath + "\\src\\" + projectName.toLowerCase()+ "\\client\\" + USE_CASE_NAMES_JAVA;

		input = bufferedReaderFile (filePath,projectName,USE_CASE_NAMES_JAVA);
		if(input == null) {
			return false;
		}

		try {
			while ((line = input.readLine())!= null){


				if ( !line.contains("= " +"\""+caseNameDelete+"\";") ) {
					output.append(line);
					output.append('\n');
				}else {

					if(( (line = input.readLine())!= null )&& line.length() > 0 ) {
						output.append(line);
						output.append('\n');
					}
				}
			}
			input.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		this.deleteFile(filePath);

		this.createAndWriteFile (filePath, output);

		return true;
	}

	/**
	 *
	 * @param caseNameDelete
	 * @param projectPath
	 * @param projectName
	 * @return
	 */
	private boolean procesarController (String caseNameDelete, String projectPath, String projectName) {


		BufferedReader input = null;
		String line;
		StringBuffer output = new StringBuffer();

		String nameGroup = caseNameDelete.substring(0,1).toUpperCase() + caseNameDelete.substring(1) + "Group";

		String filePath = projectPath + "\\src\\" + projectName.toLowerCase()+ "\\client\\" + projectName.toUpperCase() + CONTROLLER_JAVA;

		try {
			input = new BufferedReader( new FileReader(filePath) );
		} catch (FileNotFoundException e) {
			System.err.println(projectName + " no posee el archivo "  + projectName.toUpperCase() + CONTROLLER_JAVA );
			return false;
		}
		try {
			while ((line = input.readLine())!= null){


				if ( !line.contains(caseNameDelete.toUpperCase()) && !line.contains(nameGroup)) {
					output.append(line);
					output.append('\n');
				}else {

					//para el caso en el que tenga que sacar del registerLoginGroup()
					// ****return UseCaseNames.LOGIN; ***** si el caso de uso a borra
					if(line.contains("return") && line.contains("."+caseNameDelete.toUpperCase())) {
						output.append('\t');
						output.append('\t');
						output.append("return null;");
						output.append('\n');
					}

					if(line.contains("protected String register" + nameGroup)) {
						output.append(line);
						output.append('\n');
					}
				}
			}
			input.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		this.deleteFile(filePath);

		this.createAndWriteFile (filePath, output);


		return true;
	}




	/**Abre un determinado archivo para su lectura
	 *
	 * @param filePath
	 * @param projectName
	 * @param projectUseCaseName
	 * @return null si hubo un error al intentar abrirlo, sino un BufferReader
	 */
	private BufferedReader bufferedReaderFile(String filePath, String projectName,String projectUseCaseName) {

		BufferedReader input = null;

		try {
			input = new BufferedReader( new FileReader(filePath) );
		} catch (FileNotFoundException e) {

			System.err.println("ERROR: " + projectName + " no posee el archivo " + projectUseCaseName );
			return null;
		}

		return input;
	}

	/**Crea y escribe un nuevo archivo
	 *
	 * @param filePath
	 * @param output texto a escribir
	 */
	private void createAndWriteFile(String filePath, StringBuffer output) {
		// create and write a new file
		try
		{
			File file = new File(filePath);
			file.createNewFile();

			BufferedWriter outfile = new BufferedWriter(new FileWriter(filePath));
			outfile.write(output.toString());
			System.out.println("Se creó el archivo " + filePath );
			outfile.close();
		}
		catch (IOException e)    {
			System.err.println("ERROR: No creó el archivo " + filePath );
			e.printStackTrace();  }

	}

	/**Borra el archivo o directorio que se le pasa en el path
	 *
	 * @param filePath
	 * @return true si lo borró, false si no lo borro
	 */
	private boolean deleteFile(String filePath)		{

		// delete file.
//		try
//		{
			File file = new File(filePath);
			try {
				FileUtils.forceDelete(file);
				System.out.println("> Se borro el archivo " + filePath );
			} catch (IOException e) {
				System.err.println("Error con " + filePath);
				e.printStackTrace();
				return false;
			}


//			if (file.delete()) {
//				return true;
//			}else {
//				System.err.println("> No se borro el archivo " + filePath);
//
//			}
//
//		}
//		catch (SecurityException  e)    {
//			System.err.println("Error con " + filePath);
//			e.printStackTrace();   }

	return true;
	}


	private boolean deleteFileCVS(String filePath)		{

		// delete file.
		try
		{
			File file = new File(filePath);
			//boolean success = file.delete();
			if (file.delete()) {
				System.out.println("> Se borro el archivo " + filePath );
				return true;
			}

		}
		catch (SecurityException  e)    {
			System.err.println("Error con " + filePath);
			e.printStackTrace();   }

		return false;
	}


}
