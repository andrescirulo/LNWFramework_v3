package net.latin.server.utils.clonarUseCases.domain;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.latin.server.utils.caseMaker.domain.CaseMaker;
import net.latin.server.utils.caseMaker.domain.fileMaker.XslUtils;
import net.latin.server.utils.projectData.Nodo;
import net.latin.server.utils.projectData.ProjectData;
import net.latin.server.utils.projectData.UseCaseData;


/**
 *
 * @author Maxi Ariosti
 */
public class ClonarUseCases {

	private static ClonarUseCases instance;




	public static ClonarUseCases getInstance(){
		if(instance==null){
			instance = new ClonarUseCases();
		}
		return instance;
	}

	/**Se encarga de hacer rename a todos los archivos del Caso de uso, mas los de configuración
	 *
	 * @param oldCaseName nombre del Caso de Uso Ej: perfilAlta
	 * @param clonCaseName nombre del Caso de Uso Ej: perfilBaja
	 * @param projectPath
	 * @param projectData
	 */
	public String clonarUseCases (String caseName,String clonCaseName, String projectPath, ProjectData projectData) {

		String text;
		List<String> pages = new ArrayList<String>(); ;
		CaseMaker caseMaker = new CaseMaker();

		UseCaseData useCase = this.getUseCase(projectData.getUseCases(), caseName);

		for (Nodo page : useCase.getPages()) {
			pages.add(page.getName());
		}

		text = caseMaker.makeUseCase(clonCaseName, pages);
		this.clonarArchivosUseCase(caseName,clonCaseName,projectData);

		return text;
}




	/**
	 *
	 * @param caseName nombre del Caso de Uso Ej: perfilAlta
	 * @param clonCaseName nombre del Caso de Uso Ej: perfilBaja
	 * @param projectData
	 */
	private void clonarArchivosUseCase(String caseName, String clonCaseName, ProjectData projectData) {
		List pagesUseCase;
		UseCaseData useCase;


		useCase = this.getUseCase(projectData.getUseCases(), caseName);

		if(useCase == null) {
			System.err.println("ERROR: No exite Caso de Uso <"+ caseName + "> se cancela la Clonación.");
			return;
		}
		String clientPath = useCase.getAbsoluteClientPath();
		String serverPath = useCase.getAbsoluteServerPath();

		clientPath = clientPath.replaceAll(caseName, clonCaseName);
		serverPath = serverPath.replaceAll(caseName, clonCaseName);



		pagesUseCase = useCase.getPages();

		//**************************************************
		//				C	L	I	E	N	T
		//**************************************************
		System.out.println("  **** CLONANDO CLIENT *****" + '\n');


//		this.clonarDirectorio(clientPath);
//		this.clonarDirectorio(clientPath + "\\constants");
//		this.clonarDirectorio(clientPath + "\\pages");
//		this.clonarDirectorio(clientPath + "\\rpc");



		//*****************
		//	Clono pages
		//*****************

		for (Iterator iterator = pagesUseCase.iterator(); iterator.hasNext();) {
			Nodo page = (Nodo) iterator.next();

			this.clonFile(caseName,clonCaseName,page,projectData);

		}


		//****************
		//	Clono rpc
		//****************
		this.clonFile(caseName,clonCaseName,useCase.getRemoteProcesureCall(),projectData);
		this.clonFile(caseName,clonCaseName,useCase.getRemoteProcedureCallAsynch(),projectData);


		//***********************
		//	Clono constants
		//************************
		this.clonFile(caseName,clonCaseName,useCase.getConstants(),projectData);


		//***********************
		//	Clono Group.java
		//************************
		this.clonFile(caseName,clonCaseName,useCase.getGroup(),projectData);





		//**************************************************
		//				S	E	R	V	E	R
		//**************************************************
		System.out.println(" **** ARCHIVOS EN SERVER *****" + '\n');

		this.clonarDirectorio(serverPath);

		this.clonFile(caseName,clonCaseName,useCase.getServerUseCase(),projectData);





	}

	/** Modifica el contenido de un archivo reemplando las distintas variantes
     *  que puede presentar el viejo nombre del caso de uso por su nuevo nombre.
     *
	 * @param caseName nombre del Caso de Uso Ej: perfilAlta
	 * @param clonCaseName nombre del Caso de Uso Ej: perfilBaja
	 * @param nodo Datos del archivo a modificar
	 * @param projectData
	 * @return true se modifico el archivo, de lo contrario false
	 */
	private boolean clonFile(String caseName, String clonCaseName,Nodo nodo,ProjectData projectData) {

		int matches = 0;
		BufferedReader input = null;
		String line;
		StringBuffer output = new StringBuffer();

		String serverName = caseName.substring(0,1).toUpperCase() + caseName.substring(1) + "Case";
		String clonServerName = clonCaseName.substring(0,1).toUpperCase() + clonCaseName.substring(1) + "Case";

		String groupName = caseName.substring(0,1).toUpperCase() + caseName.substring(1) + "Group";
		String clonGroupName = clonCaseName.substring(0,1).toUpperCase() + clonCaseName.substring(1) + "Group";
//
//		String groupNamePunto = caseName.substring(0,1).toUpperCase() + caseName.substring(1) + "Group.";
//		String clonGroupNamePunto = clonCaseName.substring(0,1).toUpperCase() + clonCaseName.substring(1) + "Group.";

		String capitalizeName = XslUtils.capitalize(caseName);
		String clonCapitalizeName = XslUtils.capitalize(clonCaseName);

		String constantName = XslUtils.toConstantFormat(caseName);
		String clonConstantName = XslUtils.toConstantFormat(clonCaseName);

		String filePath = nodo.getAbsolutePath();

		input = bufferedReaderFile(filePath,projectData.getName(),projectData.getController().getName());

		if(input == null) {
			return false;
		}

		try {
			while ((line = input.readLine())!= null){

				// EJ. si busco "perfilAlta" cambia por "perfilBaja"
				if ( line.contains(caseName)) {
					line = line.replaceAll(caseName, clonCaseName);
					matches++;
				}

				// EJ. si busco "PerfilAltaGroup." cambia por "PerfilBajaGroup."
//				if ( line.contains(groupNamePunto)) {
//					line = line.replaceAll(groupNamePunto, clonGroupNamePunto);
//					matches++;
//				}

				// EJ. si busco "PerfilAlta" cambia por "PerfilBaja"
				if( line.contains(capitalizeName)) {
					line = line.replaceAll(capitalizeName, clonCapitalizeName);
					matches++;
				}

				// EJ. si busco "PerfilAltaGroup " cambia por "PerfilBajaGroup "
				if ( line.contains(groupName)) {
					line = line.replaceAll(groupName, clonGroupName);
					matches++;
				}

				// EJ. si busco "PERFIL_ALTA" cambia por "PERFIL_BAJA"
				if( line.contains(constantName)) {
					line = line.replaceAll(constantName, clonConstantName);
					matches++;
				}

				// EJ. si busco "PerfilAltaCase" cambia por "PerfilBajaCase"
				if( line.contains(serverName)) {
					line = line.replaceAll(serverName, clonServerName);
					matches++;
				}

				output.append(line);
				output.append('\n');

			}
			input.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		filePath = filePath.replaceAll(caseName, clonCaseName);
		filePath = filePath.replaceAll(capitalizeName, clonCapitalizeName);

		if (this.createAndWriteFile (filePath, output)) {
			System.out.println("Archivo Clonado en: " + filePath);
			return false;
		}

		return true;
	}

	/**Crea a un direcotrio clonado
	 *
	 */
	private boolean clonarDirectorio( String clonPath) {

		try {

			File clon = new File(clonPath);

			clon.mkdir();
			return true;

		}catch (SecurityException  e) {
			System.err.println("> ERROR al crear: " + clonPath);
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
			System.err.println("> ERROR-ARCHIVO: " + filePath );
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
			outfile.close();

			return true;
		}
		catch (IOException e)    {
			System.err.println("> ERROR: No creó el archivo " + filePath );
			e.printStackTrace();  }

		return false;
	}




}
