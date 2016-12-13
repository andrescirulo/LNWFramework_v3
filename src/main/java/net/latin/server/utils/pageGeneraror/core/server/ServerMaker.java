package net.latin.server.utils.pageGeneraror.core.server;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import net.latin.server.utils.caseMaker.domain.fileMaker.XslUtils;
/***
 * Crea la clase case y las interfaces rpc con los
 * metodos especificados
 * @author Santiago Aimetta
 *
 */
public class ServerMaker {

	private  final String CADENA_CORTE = "src/";
	private  final String CADENA_CORTE2="src\\";
	private final String rpcPahtA = "/client/useCases/";
	private final String rpcPahtB = "/rpc/";
	private final String casePath = "/server/useCases/";
	private String caseName;
	private String path;
	private List<ServerMethod> methods;
	private List<String> rpcImports;
	private List<String> serverImports;
	private List<String> interfaces;

	public ServerMaker(ServerGeneratorElement el) {
		super();
		this.path = el.getProjectPath();
		this.caseName = el.getCaseName();
		this.methods = el.getServerMethods();
		this.rpcImports = el.getRpcImports();
		this.serverImports = el.getServerImports();
		this.interfaces = el.getImplementedInterfaces();
	}


	public void generateServer(){
 		createRpc();
		createAsyncRpc();
		createCase();
	}


	private void createCase() {
		String name = XslUtils.capitalize(caseName)+"Case.java";
		String pathC = path+casePath+caseName+"/";
		String filePath = pathC+name;
		String packageCase = this.getPackage(path+casePath, caseName);
		StringBuffer caso = new StringBuffer();
		caso.append("package "+packageCase+"."+caseName+";\n\n");
		caso.append("import net.latin.server.GwtUseCase;\n");
		caso.append("import "+getPackage(path+rpcPahtA+caseName+rpcPahtB, caseName)
					+"."+XslUtils.capitalize(caseName+"Client")+";\n");
		caso.append("import "+getPackage(path+"/client/UseCaseNames", caseName)+";\n");
		if(this.serverImports !=null){
			for (String importX : serverImports) {
				caso.append("import "+importX+";\n");
			}
		}
		caso.append("public class "+XslUtils.capitalize(caseName)+"Case extends GwtUseCase implements "
				+XslUtils.capitalize(caseName)+"Client ");
		if(interfaces!=null){
			String interfacesBlock = "";
			for (String i : interfaces) {
				interfacesBlock+=" ,"+i;
			}
			caso.append(interfacesBlock);
		}
		caso.append("{\n\n\n");

		caso.append("\t" + "protected String getServiceName() {" + "\n");
		caso.append("\t\t" + "return UseCaseNames."+XslUtils.toConstantFormat(caseName)+";" +"\n");
		caso.append("\t" + "}" + "\n\n");




		for (ServerMethod metodo : this.methods) {
			caso.append(metodo.getMethodForCase());
		}
		caso.append("}");
		this.saveFile(filePath, caso.toString());

	}


	private void createAsyncRpc() {
		String rpcName = XslUtils.capitalize(caseName)+"ClientAsync.java";
		String pathRpc = path+rpcPahtA+caseName+rpcPahtB;
		//String pathRpc = path+"/rpc/";
		String filePath = pathRpc+rpcName;
		String packageRpc = this.getPackage(pathRpc, caseName);
		StringBuffer rpc = new StringBuffer();
		rpc.append("package "+packageRpc+";\n\n");
		rpc.append("import com.google.gwt.user.client.rpc.AsyncCallback;\n");
		rpc.append("import net.latin.client.rpc.GwtRpcInterfaceAsync;\n");
		if(this.rpcImports !=null){
			for (String importX : rpcImports) {
				rpc.append("import "+importX+";\n");
			}
		}
		rpc.append("\n\n\n");
		rpc.append("public interface "+XslUtils.capitalize(caseName)+"ClientAsync extends GwtRpcInterfaceAsync {\n\n\n");
		for (ServerMethod metodo : this.methods) {
			rpc.append(metodo.getMethodForRpcAsync());
		}
		rpc.append("}");
		this.saveFile(filePath, rpc.toString());


	}


	private void createRpc() {
		String rpcName = XslUtils.capitalize(caseName)+"Client.java";
		String pathRpc = path+rpcPahtA+caseName+rpcPahtB;
		//String pathRpc = path+"/rpc/";

		String filePath = pathRpc+rpcName;
		String packageRpc = this.getPackage(pathRpc, caseName);
		StringBuffer rpc = new StringBuffer();
		rpc.append("package "+packageRpc+";\n\n");
		rpc.append("import "+"net.latin.client.rpc.GwtRpcInterface;\n");
		if(this.rpcImports !=null){
			for (String importX : rpcImports) {
				rpc.append("import "+importX+";\n");
			}
		}
		rpc.append("\n\n\n");
		rpc.append("public interface "+XslUtils.capitalize(caseName)+"Client extends GwtRpcInterface {\n\n\n");
		for (ServerMethod metodo : this.methods) {
			rpc.append(metodo.getMethodForRpc());
		}
		rpc.append("}");
		this.saveFile(filePath, rpc.toString());



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



	private void saveFile(String path, String code ){
		BufferedWriter out;
		try {
			out = new BufferedWriter(new FileWriter(path));
			out.write(code);
			out.close();
		} catch (IOException e) {
			throw new RuntimeException("Problemas haciendo save del arichivo " +
					""+path);
		}
	}




}
