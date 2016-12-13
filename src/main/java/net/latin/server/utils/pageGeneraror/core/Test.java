package net.latin.server.utils.pageGeneraror.core;

import java.io.File;

public class Test {

	/**
	 * @param args
	 */
	private static final String CADENA_CORTE = "src/";
	private static final String CADENA_CORTE2="src\\";
	private static String packageF;
	private static CharSequence clientF;
	private static String rpcF;
	private static String caseName;
	private static String pageName;

	public static void main(String[] args) {
		getPageData("C://development//eclipse//workspace//sma//src//sma//client//useCases//casoDePrueba//pages//Caca.java");
		System.out.println(packageF);
		System.out.println(clientF);
		System.out.println(caseName);
		System.out.println(rpcF);
		System.out.println(pageName);
	}

	private static void getPageData(String path){


		File arch = new File(path);
		path = arch.getParent();

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
		packageF = packAux;
		clientF = packageF.substring(0, packageF.indexOf(".useCases"));
		rpcF = packageF.substring(0,packageF.indexOf(".pages"))+".rpc";
		caseName = packageF.substring(packageF.indexOf(".useCases"), packageF.indexOf(".pages")).substring(packageF.indexOf(".useCases"));
		pageName = arch.getPath();
		pageName = pageName.replace("/", ".");
		pageName = pageName.replace("\\", ".");
		pageName = pageName.substring(pageName.indexOf(".pages"),pageName.indexOf(".java")).substring(".pages.".length());
	}


}
