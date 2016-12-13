package net.latin.server.utils.caseMaker.domain.fileMaker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Diferentes herramientas para trabajar con xsl
 *
 * @author Santiago Aimetta
 */
public class XslUtils {

	/**
	 * <p>
     * Convierte la primera letra del nombre de una clase
     * en mayúscula.
     * <p>
     * Ejemplo: claseDePrueba => ClaseDePrueba
     */
    public static String capitalize(String str) {
        return (str==null || str.length()==0) ? "" : Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    /**
	 * <p>
     * Convierte la primera letra del nombre de una clase
     * en minúscula.
     * <p>
     * Ejemplo: ClaseDePrueba => claseDePrueba
     */
    public static String unCapitalize(String str) {
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }


    public static String toUpperCase(String str){
    	return str.toUpperCase();
    }

    public static String toLowerCase(String str){
    	return str.toLowerCase();
    }

    /**
     * <p>
     * Convierte el nombre de una clase constante.
     * <p>
     * Ejemplo1: ClaseDePrueba => CLASE_DE_PRUEBA.
     * <p>
     * Ejemplo2: claseDePrueba => CLASE_DE_PRUEBA
     * <p>
     * El nombre de la clase no tiene que tener extensión y tiene
     * que ser el nombre simple (sin todo el paquete).
     */
    public static String toConstantFormat(String str){
    	int n = str.length();
    	List<Character> list = new ArrayList<Character>();
    	char c;
    	for (int i = 0; i < n; i++) {
    		c = str.charAt(i);
    		if( i != 0 && Character.isUpperCase(c) ) {
    			list.add('_');
    		}
    		list.add(Character.toUpperCase( c ));
		}

    	StringBuffer buffer = new StringBuffer();
    	for (Character character : list) {
    		buffer.append( character );
		}
    	return buffer.toString();

//    	Vector<Character> vec = new Vector<Character>();
//    	vec.add(str.charAt(0));
//    	for (int i = 0; i < str.length(); i++) {
//			if( Character.isUpperCase(str.charAt(i)) && i != 1 ){
//				vec.add('_');
//				vec.add(Character.toUpperCase(str.charAt(i)));
//			}else{
//				vec.add(Character.toUpperCase(str.charAt(i)));
//			}
//    	}
//    	String value = "";
//    	for (int i = 0; i < vec.size(); i++) {
//			value = value + ""+vec.get(i);
//		}
//    	return value;
    }

    public static void main(String[] args) {
    	System.out.println(toConstantFormat(capitalize("mockUseCase")));
	}

    /**
     * Retorna el nombre del grupo de un caso de uso, a partir
     * del path de la pagina (preferentemente absoluto).
     * Ej: "UseCaseGroup"
     */
    public static String getGroupData(String path){
    	File original = new File(path);
    	String useCaseName = original.getParentFile().getParentFile().getName();
		return capitalize(useCaseName)+"Group";
    }

    /**
     * Retorna el nombre del grupo y la constante de la pagina
     * a partir del path de la pagina (preferentemente el absoluto)
     * ej "UseCaseGroup.MI_PAGINA"
     */
    public static String getGroupPageConstant(String path) {
    	File original = new File(path);
    	String pageName = original.getName();
    	pageName = pageName.substring( 0, pageName.indexOf( "." ));

    	return getGroupData(path) + "." + toConstantFormat(pageName);
    }

    /**
     * Devuelve el nombre lindo de una pagina a partir
     * del path de la pagina (preferentemente el absoluto)
     * Ej: "MiPaginaConNombreLindo"
     */
    public static String getPageNiceName( String path ) {
    	File original = new File(path);
    	String pageName = original.getName();
    	pageName = pageName.substring( 0, pageName.indexOf( "." ));
    	return capitalize(pageName);
    }


    /**
     * Devuelve el fully qualified name de un Group a partir
     * del path de una pagina (preferentemente el absoluto)
     */
    public static String getImportGroup(String path){
		File arch = new File(path);
		path = arch.getParent();

		String packAux ="";
		String CADENA_CORTE ="\\";
		String CADENA_CORTE2="/";
		String SOURCE ="src";
		String USE_CASES ="useCases";
		String PAGES = "pages";

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
		packAux = packAux.substring(CADENA_CORTE.length());
		String packageF = packAux;
		String groupImport = packageF.substring(packageF.lastIndexOf(SOURCE));
		groupImport = groupImport.replace(SOURCE+".", "");
		groupImport = groupImport.replace(PAGES, "");
		String caseName = groupImport.substring(groupImport.lastIndexOf(USE_CASES));
		caseName = caseName.replace(".", "");
		caseName = caseName.replace(USE_CASES, "");
		caseName = capitalize(caseName);


		return groupImport+caseName+"Group";
	}

    /**
     * Devuelve el fully qualified name de UseCaseName a partir
     * del path de una pagina (preferentemente el absoluto)
     */
    public static String getImportUseCaseName( String path ) {
    	File arch = new File(path);
    	//legar hasta client: pages=>caso de uso=>useCases=>client
    	File client = arch.getParentFile().getParentFile().getParentFile().getParentFile();
    	return client.getParentFile().getName() + "." + client.getName() + "." + "UseCaseNames";
    }



}
