package net.latin.server.utils.pageGeneraror.core.server;

import java.util.List;

/**
 * Contiene la info necesaria hacerca de un metodo
 * para poder crear las interfaces rpc y el  server
 * @author Santiago Aimetta
 *
 */
public class ServerMethod {
	private List<String>  anotations;
	private String coments;
	private String name;
	private List<ServerParameter> parameters;
	private ServerParameter returnType;
	private String code;
	private List<String> execptions;








	public ServerMethod(List<String> anotations, String coments, String name,
			List<ServerParameter> parameters, ServerParameter returnType,
			String code, List<String> excep) {
		super();
		this.anotations = anotations;
		this.coments = coments;
		this.name = name;
		this.parameters = parameters;
		this.returnType = returnType;
		this.code = code;
		this.execptions = excep;
	}

	public String getComents() {
		if(coments!=""){
			StringBuffer comentarios = new StringBuffer();
			comentarios.append("\t" + "/**" + "\n");
			comentarios.append("\t*\n");
			comentarios.append("\t*"+" "+coments+"\n");
			comentarios.append("\t*\n");
			comentarios.append("\t*/\n");

			return comentarios.toString();
		}else{
			return "\n";
		}

	}

	public String getComentsWithAnotations(){
		if(coments!="" || anotations!=null){

			StringBuffer comentarios = new StringBuffer();
			comentarios.append("\t/**\n");
			comentarios.append("\t*\n");
			comentarios.append("\t*"+" "+coments+"\n");
			if(anotations != null ){
				for (String anotation : this.getAnotations()) {
					comentarios.append("\t*"+" "+anotation+"\n");
				}
			}

			comentarios.append("\t*/\n");
			return comentarios.toString();
		}else{
			return "\n";
		}
	}



	public String getCode() {
		return "\t"+code+"\n\n\n";
	}



	public List<String> getAnotations() {
		return anotations;
	}

	public void setAnotations(List<String> anotations) {
		this.anotations = anotations;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ServerParameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<ServerParameter> parameters) {
		this.parameters = parameters;
	}

	public ServerParameter getReturnType() {
		return returnType;
	}

	public void setReturnType(ServerParameter returnType) {
		this.returnType = returnType;
	}

	public void setComents(String coments) {
		this.coments = coments;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMethodForRpc(){
		StringBuffer m = new StringBuffer();
		m.append(this.getComentsWithAnotations());
		String parametros = "";
		for (ServerParameter p : this.getParameters()) {
			parametros+=" "+p.getType()+" "+p.getName()+", ";
		}

		m.append("\tpublic "+returnType.getType()+" "+name+"( "+parametros.subSequence(0, parametros.length()-2)+")");
		if(execptions!=null){
			String exepBlock = "throws ";
			for (String e : this.execptions) {
				exepBlock+=e+" ,";
			}
			exepBlock = exepBlock.substring(0,exepBlock.length()-1);
			m.append(exepBlock);
		}
		m.append(";\n\n");
		return m.toString();

	}

	public String getMethodForRpcAsync(){
		StringBuffer m = new StringBuffer();
		m.append(this.getComents());
		String parametros = "";
		for (ServerParameter p : this.getParameters()) {
			parametros+=" "+p.getType()+" "+p.getName()+", ";
		}

		m.append("\tpublic void "+name+"( "+parametros+" AsyncCallback callback  );\n\n");
		return m.toString();
	}

	public String getMethodForCase(){
		StringBuffer m = new StringBuffer();
		m.append(this.getComents());
		String parametros = "";
		for (ServerParameter p : this.getParameters()) {
			parametros+=" "+p.getType()+" "+p.getName()+", ";
		}
		m.append("\tpublic "+returnType.getType()+" "+name+"( "+parametros.subSequence(0, parametros.length()-2)+"){\n\n");
		m.append( "\t\t" + this.getCode());
		m.append("\t}\n\n");
		return m.toString();
	}







}
