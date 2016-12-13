package net.latin.server.utils.renameUseCase.domain;

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

import net.latin.server.utils.caseMaker.domain.fileMaker.XslUtils;
import net.latin.server.utils.projectData.Nodo;
import net.latin.server.utils.projectData.ProjectData;
import net.latin.server.utils.projectData.UseCaseData;

/**
 *
 * @author Maxi Ariosti
 */
public class RenameUseCase {

	private static final String USE_CASE_NAMES_JAVA = "UseCaseNames.java";
	private static final String STRUTS_CONFIG_USE_CASES_XML = "struts-config-useCases.xml";
	private static final String GENERAL_CONFIG_XML = "GeneralConfig.xml";
	private static final String CONTROLLER_JAVA = "Controller.java";
	private static RenameUseCase instance;




	public static RenameUseCase getInstance(){
		if(instance==null){
			instance = new RenameUseCase();
		}
		return instance;
	}

	/**Se encarga de hacer rename a todos los archivos del Caso de uso, mas los de configuración
	 *
	 * @param oldCaseName nombre del Caso de Uso Ej: perfilAlta
	 * @param newCaseName nombre del Caso de Uso Ej: perfilBaja
	 * @param projectPath
	 * @param projectData
	 */
	public void renameUseCase (String oldCaseName,String newCaseName, String projectPath, ProjectData projectData) {


		//  <NAME-PROJECT>Controller.java
		if(!this.procesarController(oldCaseName,newCaseName,projectData)) {return;}

		//UsesCasesNames.java
		if(!this.procesarUseCaseName(oldCaseName,newCaseName,projectData)) {return;}

		//struts-config-UseCase.xml
		if(!this.procesarStratsConfigUseCase(oldCaseName,newCaseName,projectData)) {return;}

		//GeneralConfig.xml
		if(!this.procesarGeneralConfig(oldCaseName,newCaseName,projectData)) {return;}

		this.renombrarArchivosUseCase(oldCaseName,newCaseName,projectData);






}








	/**
	 *
	 * @param oldCaseName nombre del Caso de Uso Ej: perfilAlta
	 * @param newCaseName nombre del Caso de Uso Ej: perfilBaja
	 * @param projectData
	 */
	private void renombrarArchivosUseCase(String oldCaseName, String newCaseName, ProjectData projectData) {
		List pagesUseCase;
		UseCaseData useCase;

		String packageClientPath = projectData.getAbsolutePath() + "\\src\\" + projectData.getName().toLowerCase()+ "\\client\\useCases\\" + oldCaseName ;
		String packageServerPath = projectData.getAbsolutePath() + "\\src\\" + projectData.getName().toLowerCase()+ "\\server\\useCases\\" + oldCaseName ;

		useCase = this.getUseCase(projectData.getUseCases(), oldCaseName);

		if(useCase == null) {
			System.err.println("ERROR: No exite Caso de Uso <"+ oldCaseName + "> se cancela la Modificación.");
			return;
		}

		String rpcClientPath = useCase.getRemoteProcesureCall().getAbsolutePath();
		String rpcClientAsyncPath = useCase.getRemoteProcedureCallAsynch().getAbsolutePath();
		String constantsPath = useCase.getConstants().getAbsolutePath();
		String groupPath = useCase.getGroup().getAbsolutePath();

		pagesUseCase = useCase.getPages();


		//**************************************************
		//				C	L	I	E	N	T
		//**************************************************
		System.out.println("  **** ARCHIVOS EN CLIENT *****" + '\n');

		//*****************
		//	Modifico pages
		//*****************

		for (Iterator iterator = pagesUseCase.iterator(); iterator.hasNext();) {
			Nodo page = (Nodo) iterator.next();

			this.changeFile(oldCaseName,newCaseName,page,projectData);

		}


//		//****************
//		//	modifico rpc
//		//****************
		this.changeFile(oldCaseName,newCaseName,useCase.getRemoteProcesureCall(),projectData);
		this.rename(rpcClientPath,rpcClientPath.replaceAll(XslUtils.capitalize(oldCaseName),XslUtils.capitalize(newCaseName) ));

		this.changeFile(oldCaseName,newCaseName,useCase.getRemoteProcedureCallAsynch(),projectData);
		this.rename(rpcClientAsyncPath,rpcClientAsyncPath.replaceAll(XslUtils.capitalize(oldCaseName),XslUtils.capitalize(newCaseName) ));



//		//***********************
//		//	modifico constants
//		//************************
		this.changeFile(oldCaseName,newCaseName,useCase.getConstants(),projectData);
		this.rename(constantsPath,constantsPath.replaceAll(XslUtils.capitalize(oldCaseName),XslUtils.capitalize(newCaseName) ));



//		//***********************
//		//	modifico Group.java
//		//************************
		this.changeFile(oldCaseName,newCaseName,useCase.getGroup(),projectData);
		this.rename(groupPath,groupPath.replaceAll(XslUtils.capitalize(oldCaseName),XslUtils.capitalize(newCaseName) ));



		if (this.rename(packageClientPath,packageClientPath.replaceAll(oldCaseName,newCaseName ))){
			System.err.println("> Puede que existan archivos que debe ser MODIFICADOS MUNUALEMENTE en el direcrotio del UseCase");
		}



//		//**************************************************
//		//				S	E	R	V	E	R
//		//**************************************************
		System.out.println(" **** ARCHIVOS EN SERVER *****" + '\n');

		//modifica el .java del server
		String serverAbsolutePath = useCase.getServerUseCase().getAbsolutePath();

		this.changeFile(oldCaseName,newCaseName,useCase.getServerUseCase(),projectData);
		this.rename(serverAbsolutePath,serverAbsolutePath.replaceAll(XslUtils.capitalize(oldCaseName),XslUtils.capitalize(newCaseName) ));

		this.rename(packageServerPath,packageServerPath.replaceAll(oldCaseName,newCaseName ));




	}

	/** Modifica el contenido de un archivo reemplando las distintas variantes
     *  que puede presentar el viejo nombre del caso de uso por su nuevo nombre.
     *
	 * @param oldCaseName nombre del Caso de Uso Ej: perfilAlta
	 * @param newCaseName nombre del Caso de Uso Ej: perfilBaja
	 * @param nodo Datos del archivo a modificar
	 * @param projectData
	 * @return true se modifico el archivo, de lo contrario false
	 */
	private boolean changeFile(String oldCaseName, String newCaseName,Nodo nodo,ProjectData projectData) {

		int matches = 0;
		BufferedReader input = null;
		String line;
		StringBuffer output = new StringBuffer();

		String oldServerName = oldCaseName.substring(0,1).toUpperCase() + oldCaseName.substring(1) + "Case";
		String newServerName = newCaseName.substring(0,1).toUpperCase() + newCaseName.substring(1) + "Case";

		String oldGroupName = oldCaseName.substring(0,1).toUpperCase() + oldCaseName.substring(1) + "Group ";
		String newGroupName = newCaseName.substring(0,1).toUpperCase() + newCaseName.substring(1) + "Group ";

		String oldCapitalizeName = XslUtils.capitalize(oldCaseName);
		String newCapitalizeName = XslUtils.capitalize(newCaseName);

		String oldConstantName = XslUtils.toConstantFormat(oldCaseName);
		String newConstantName = XslUtils.toConstantFormat(newCaseName);

		String filePath = nodo.getAbsolutePath();

		input = bufferedReaderFile(filePath,projectData.getName(),projectData.getController().getName());

		if(input == null) {
			return false;
		}

		try {
			while ((line = input.readLine())!= null){

				// EJ. si busco "perfilAlta" cambia por "perfilBaja"
				if ( line.contains(oldCaseName)) {
					line = line.replaceAll(oldCaseName, newCaseName);
					matches++;
				}

				// EJ. si busco "PerfilAlta" cambia por "PerfilBaja"
				if( line.contains(oldCapitalizeName)) {
					line = line.replaceAll(oldCapitalizeName, newCapitalizeName);
					matches++;
				}

				// EJ. si busco "PerfilAltaGroup " cambia por "PerfilBajaGroup "
				if ( line.contains(oldGroupName)) {
					line = line.replaceAll(oldGroupName, newGroupName);
					matches++;
				}


				// EJ. si busco "PERFIL_ALTA" cambia por "PERFIL_BAJA"
				if( line.contains(oldConstantName)) {
					line = line.replaceAll(oldConstantName, newConstantName);
					matches++;
				}

				// EJ. si busco "PerfilAltaCase" cambia por "PerfilBajaCase"
				if( line.contains(oldServerName)) {
					line = line.replaceAll(oldServerName, newServerName);
					matches++;
				}

				output.append(line);
				output.append('\n');

			}
			input.close();

		} catch (IOException e) {
			e.printStackTrace();
		}


		if ( this.deleteFile(filePath)) {

			//filePath = filePath.replaceAll(oldCaseName, newCaseName);
			//filePath = filePath.replaceAll(oldCapitalizeName, newCapitalizeName);

			if (this.createAndWriteFile (filePath, output)) {
				System.out.println("-->   " + matches + " modificaciones ");
			}
		}

		return true;
	}

	/**Renombra a un archivo
	 *
	 * @param oldName debe tener la ruta y el nombre para el viejo archivo
	 * @param newName debe tener la ruta y el nombre para el nuevo  archivo
	 * @return true si el rename fue exitoso, false caso contrario
	 */
	private boolean rename( String oldName, String newName) {

		try {

			File f1 = new File(oldName);
			File f2 = new File(newName);

			if( f1.renameTo(f2)) {

				System.out.println("> RENAME a " + newName + '\n');
				return true;
			}

		}catch (NullPointerException e) {
			System.err.println("> ERROR al renombrar Archivo");
		}

		return false;
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

			System.err.println("> ERROR: " + projectName + " no posee el archivo " + projectUseCaseName );
			return null;
		}

		return input;
	}

	/** Crear y Escribe un archivo
	 *
	 * @param filePath
	 * @param output
	 * @return true si puedo crear y escribir el archivo, false si hubo algun error
	 */
	private boolean createAndWriteFile(String filePath, StringBuffer output) {
		// create and write a new file
		try
		{
			File file = new File(filePath);
			file.createNewFile();

			BufferedWriter outfile = new BufferedWriter(new FileWriter(filePath));
			outfile.write(output.toString());
			System.out.println("> Se creó el Nuevo Archivo " + filePath );
			outfile.close();

			return true;
		}
		catch (IOException e)    {
			System.err.println("> ERROR: No creó el archivo " + filePath );
			e.printStackTrace();  }

		return false;
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
//			File file = new File(filePath);
//			//boolean success = file.delete();
//			if (file.delete()) {
//				return true;
//			}else {
//				System.err.println("> No se borro el archivo " + filePath);
//
//			}
//
//		}
//		catch (SecurityException  e)    {
//			System.err.println("> Error con " + filePath);
//			e.printStackTrace();   }
//
//	return false;

		File file = new File(filePath);
		try {
			FileUtils.forceDelete(file);
			System.out.println("> Se borro el archivo " + filePath );
		} catch (IOException e) {
			System.err.println("Error con " + filePath);
			e.printStackTrace();
			return false;
		}


	return true;
	}


	/**
	 *Se encarga de modificar el contenido del archivo <NAME-PROJECT>Controller.java.
	 *Es necesario que se le indique el nombre del caso de uso a modificar "oldCaseName" por uno nombre
	 *nuevo que se le quiera dar a dicho caso de uso por medio de "newCaseName"
	 *
	 * @param oldCaseName Nombre del Caso de Uso actual a modificar
	 * @param newCaseName Nuevo nombre para el Caso de Uso
	 * @param projectData
	 * @return true si no hubo inconvenientes en la modificación del archivo, de lo contrario false.
	 */
	private boolean procesarController(String oldCaseName, String newCaseName, ProjectData projectData) {

		int matches = 0;
		BufferedReader input = null;
		String line;
		StringBuffer output = new StringBuffer();

		String oldGroupName = oldCaseName.substring(0,1).toUpperCase() + oldCaseName.substring(1) + "Group";
		String newGroupName = newCaseName.substring(0,1).toUpperCase() + newCaseName.substring(1) + "Group";

		String oldConstantName = XslUtils.toConstantFormat(oldCaseName);
		String newConstantName = XslUtils.toConstantFormat(newCaseName);



		UseCaseData useCase = getUseCase(projectData.getUseCases(), oldCaseName);

		if(useCase == null) {
			System.err.println("> ERROR: No exite Caso de Uso <"+ oldCaseName + "> se cancela Rename.");
			return false;
		}

		String filePath = (projectData.getController()).getAbsolutePath();

		input = bufferedReaderFile(filePath,projectData.getName(),projectData.getController().getName());

		if(input == null) {
			return false;
		}

		try {
			while ((line = input.readLine())!= null){


				if ( line.contains(oldCaseName)) {
					line = line.replace(oldCaseName, newCaseName);
					matches++;
				}
				if ( line.contains(oldGroupName)) {
					line = line.replace(oldGroupName, newGroupName);
					matches++;
				}
				if( line.contains(oldConstantName)) {
					line = line.replace(oldConstantName, newConstantName);
					matches++;
				}

				output.append(line);
				output.append('\n');

			}
			input.close();

		} catch (IOException e) {
			e.printStackTrace();
		}


		if ( this.deleteFile(filePath)) {

			if (this.createAndWriteFile (filePath, output)) {
				System.out.println("     " + matches + " modificaciones en " + projectData.getName()+CONTROLLER_JAVA );
			}
		}

		return true;
	}



	/**
	 *
	 * @param oldCaseName nombre del Caso de Uso Ej: perfilAlta
	 * @param newCaseName nombre del Caso de Uso Ej: perfilBaja
	 * @param projectData
	 * @return true si proceso exitosamente, false caso contrario
	 */
	private boolean procesarUseCaseName(String oldCaseName, String newCaseName, ProjectData projectData) {

		int matches = 0;
		BufferedReader input = null;
		String line;
		StringBuffer output = new StringBuffer();

		String oldConstantName = XslUtils.toConstantFormat(oldCaseName);
		String newConstantName = XslUtils.toConstantFormat(newCaseName);


//		String pathFile = projectPath + "\\src\\" + projectName.toLowerCase()+ "\\client\\" + USE_CASE_NAMES_JAVA;
		String filePath = projectData.getUseCaseNames().getAbsolutePath();


		input = bufferedReaderFile (filePath,projectData.getName(),projectData.getUseCaseNames().getName());
		if(input == null) {
			return false;
		}

		try {
			while ((line = input.readLine())!= null){


				if (line.contains(oldCaseName) ) {
					line = line.replaceAll(oldCaseName, newCaseName);
					matches++;
				}
				if( line.contains(oldConstantName)) {
					line = line.replaceAll(oldConstantName, newConstantName);
					matches++;
				}


				output.append(line);
				output.append('\n');
			}
			input.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		if ( this.deleteFile(filePath)) {

			if (this.createAndWriteFile (filePath, output)) {
				System.out.println("     " + matches + " modificaciones en " + USE_CASE_NAMES_JAVA );
			}
		}

		return true;
	}

	/**Crea un nuevo struts-config-useCases.xml a paritr de modificar la
	 * línea en donde aparece el nombre del caso de uso en cuestión
	 *
	 * @param oldCaseName nombre del Caso de Uso Ej: perfilAlta
	 * @param newCaseName nombre del Caso de Uso Ej: perfilBaja
	 * @param absolutePath
	 * @param name
	 * @return true si proceso exitosamente, false caso contrario
	 */
	private boolean procesarStratsConfigUseCase(String oldCaseName,String newCaseName, ProjectData projectData) {

		int matches = 0;
		BufferedReader input = null;
		String line;
		StringBuffer output = new StringBuffer();

		String oldName = oldCaseName.substring(0,1).toUpperCase() + oldCaseName.substring(1) + "Case";
		String newName = newCaseName.substring(0,1).toUpperCase() + newCaseName.substring(1) + "Case";

		//String nameGroup = caseNameDelete.substring(0,1).toUpperCase() + caseNameDelete.substring(1) + "Group";
//		String pathFile = projectPath + "\\webapp\\WEB-INF\\" + STRUTS_CONFIG_USE_CASES_XML;

		String filePath = projectData.getStrutsConfigUseCases().getAbsolutePath();


		input = bufferedReaderFile (filePath,projectData.getName(),STRUTS_CONFIG_USE_CASES_XML);
		if ( input == null) {
			return false;
		}

		try {
			while ((line = input.readLine())!= null){


				if (line.contains("path="+"\"/" + oldCaseName + "\"") ) {
					line = line.replaceAll(oldCaseName, newCaseName);
					matches++;
					line = line.replaceAll(oldName, newName);
					matches++;

				}

				output.append(line);
				output.append('\n');
			}
			input.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		if ( this.deleteFile(filePath)) {

			if (this.createAndWriteFile (filePath, output)) {

				System.out.println("     " + matches + " modificaciones en " + STRUTS_CONFIG_USE_CASES_XML);
			}
		}

		return true;
	}


	/**Modifica en el GeneralConfig.xml la línea donde figura el caso de Uso en cuestion
	 *
	 * @param oldCaseName nombre del Caso de Uso Ej: perfilAlta
	 * @param newCaseName nombre del Caso de Uso Ej: perfilBaja
	 * @param projectData
	 * @return true si proceso exitosamente, false caso contrario
	 */
	private boolean procesarGeneralConfig(String oldCaseName, String newCaseName, ProjectData projectData) {

		int matches = 0;
		BufferedReader input = null;
		String line;
		StringBuffer output = new StringBuffer();

		String filePath = projectData.getGeneralConfig().getAbsolutePath();

		input = bufferedReaderFile (filePath,projectData.getName(),GENERAL_CONFIG_XML);
		if ( input == null) {
			return false;
		}

		try {
			while ((line = input.readLine())!= null){

				if ( line.contains("id='" + oldCaseName + "'") ) {
					line = line.replace(oldCaseName, newCaseName);
					matches++;
				}

				output.append(line);
				output.append('\n');
			}
			input.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		if ( this.deleteFile(filePath)) {

			if (this.createAndWriteFile (filePath, output)) {

				System.out.println("     " + matches + " modificaciones en " + GENERAL_CONFIG_XML);
			}
		}

		return true;
	}
}
