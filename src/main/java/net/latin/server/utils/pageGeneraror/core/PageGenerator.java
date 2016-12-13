package net.latin.server.utils.pageGeneraror.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.jdom2.Document;
import org.jdom2.Element;

import net.latin.server.utils.caseMaker.domain.fileMaker.XslUtils;
import net.latin.server.utils.xml.XmlUtils;

/**
 * Generador de codigo layout de una GwtPage
 *
 * A partir de el path de la pagina y una lista de PageGeneretorElements
 * se genera el codigo de la pagina y se agrega a la clase cuyo path ha sido
 * enviado como parametro
 *
 * @author Santiago Aimetta
 */
public class PageGenerator {

	private static final String ATT_CLASS = "class";
	private static final String TAG_GENERATOR = "generator";
	public static final String DESIGNER_XML_EXT = ".Designer.xml";
	public static final String JAVA_PAGE_EXTENSION = ".java";
	private static final String PAGES = ".pages";
	private static final String USE_CASES = ".useCases";

	private static String packageF;
	private static String clientF;
	private static String rpcF;
	private static String caseName;
	private static String pageName;
	private String pagePath;
	private List<PageGeneratorElement> widgets;
	private Set<String> imports;
	private Set<String> interfaces;
	private static final String CADENA_CORTE = "src/main/java/";
	private static final String CADENA_CORTE2="src\\main\\java\\";


	public PageGenerator(String pagePath, List<PageGeneratorElement> widgets) {
		super();
		this.pagePath = pagePath;
		this.widgets = widgets;
		this.getAllImports_Interfaces();

	}


	public void generatePage() {
		//comienzo a generar el codigo de la pagina
		StringBuffer code = addPackage_Imports();

		//imports fijos..ahora compilo y agrego los imports de todos los widgets
		code.append(this.getAllImports());
		//armo el nombre de la clase..public class Pagina1 extends y implements
		code.append(this.getPageName());
		//agrego variables globales
		code.append(this.getGlobalVariables());
		//agrego el constructor
		code.append(this.getConstructor());
		//agrego el server
		code.append(this.getServerCode());
		//cierre del constructor y metodos fijos de la page (on visible)
		code.append(this.getCloseConstructor());
		//agrego el codigo de los widgets q va dentro del metodo init
		code.append(this.getInitComponents());
		//agrego el codigo de los metodos de los widgets
		code.append(this.methodsCode());


		//codigo ya generado
		File page = new File(pagePath);
		if (!page.canWrite()) {
			throw new RuntimeException("No se pudo escribir el archivo: " + pagePath);
	    }
        Writer output = null;
        try {
          output = new BufferedWriter( new FileWriter(page) );
          output.write( code.toString() );
        } catch (IOException e) {
			throw new RuntimeException("No se pudo escribir el archivo: " + pagePath, e);
		}
        finally {
          if (output != null)
			try {
				output.close();
			} catch (IOException e) {
				throw new RuntimeException("No se pudo cerrar el archivo: " + pagePath, e);
			}
        }


        //generar Designer
        //generateDesigner();

	}

	/**
	 * Crea una pagina sin ningún widget.
	 * Solo el encabezado general
	 * @param pagePath
	 */
	public static void generateEmptyPage(String pagePath) {
		PageGenerator generator = new PageGenerator(pagePath, new ArrayList<PageGeneratorElement>());
		generator.generatePage();
	}


	/**
	 * Genera un XML con el designer de la pagina
	 * @param pagePath
	 */
	private void generateDesigner() {
		String designerPath = pagePath.replace( ".java", DESIGNER_XML_EXT );

		try {
			//crear xml
			Document document = new Document();
			Element root = new Element("PageDesign");


			//agarrar todos los Element de cada generator
			for (PageGeneratorElement generator : this.widgets) {
				System.out.println(generator.getClass().getName());
				root.addContent( generator.saveToXml() );
			}

			//guardar xml
			document.setRootElement(root);
			XmlUtils.saveDocument(document, designerPath);

		} catch (Exception e) {
			throw new RuntimeException("No se pudo crear el archivo Designer: " + designerPath, e);
		}
	}

	/**
	 * Crea y carga una lista de Generators en base a un XML de Designer persistido
	 * anteriormente.
	 * @param designerPath
	 * @return
	 */
	public static List<PageGeneratorElement> loadDesigner(String designerPath) {
		try {
			List<PageGeneratorElement> generators = new ArrayList<PageGeneratorElement>();

			//Cargar XML
			Document document = XmlUtils.loadDocument(designerPath,false);
			Element root = document.getRootElement();

			//Instanciar todos los Generators que de primera linea que tiene el Designer
			List<Element> children = root.getChildren(TAG_GENERATOR);
			for (Element element : children) {
				String clasz = element.getAttributeValue( ATT_CLASS );
				PageGeneratorElement generator = (PageGeneratorElement) Class.forName(clasz).newInstance();

				//pedir a cada generator de primera linea que instancie en forma recursiva sus generators internos
				generator.loadFromXml(element);
				generator.setProperties();
				generators.add(generator);
			}

			return generators;

		} catch (Exception e) {
			throw new RuntimeException("Error al cargar un XML de Desginer: " + designerPath, e);
		}
	}


	private StringBuffer addPackage_Imports() {
		PageGenerator.getPageData(pagePath);
		StringBuffer code = new StringBuffer();
		code.append("\n");
		code.append("package "+packageF+";\n\n");
		code.append("import "+clientF+".UseCaseNames;\n");
		code.append("import "+rpcF+"."+XslUtils.capitalize(caseName)+"ClientAsync;\n");
		code.append("import net.latin.client.rpc.GwtAsyncCallback;\n");
		code.append("import net.latin.client.widget.base.GwtPage;\n");
		code.append("import net.latin.client.rpc.GwtRpc;\n");
		return code;
	}

	private String getInitComponents() {
		StringBuffer code = new StringBuffer();
		code.append( "\n" );
		code.append( "\t/**\n" );
		code.append( "\t* NO MODIFICAR" + "\n" );
		code.append( "\t* Metodo generado con utilidad PageMaker\n" );
		code.append( "\t* Modificar con esta herramienta\n" );
		code.append( "\t*/" );
		code.append("\n\tprivate void initComponents() {\n");
		code.append(this.getWidgetConstructorCode());
		code.append("\t}\n"+ getOnVisibleCode());



		return code.toString();
	}


	private String methodsCode() {
		StringBuffer code = new StringBuffer();
		for (PageGeneratorElement widget : this.widgets) {
			code.append(widget.getMethodsCode());
		}
		return "\t"+code.toString()+"\n\n\n}";
	}


	private String getCloseConstructor() {
		StringBuffer code = new StringBuffer();
		code.append( "\n\t\t" + "initComponents();" + "\n");
		code.append( "\t" + "}" + "\n" );
		return code.toString();
	}


	private String getOnVisibleCode() {
		StringBuffer code = new StringBuffer();
		code.append( "\n" );
		code.append( "\t/**\n" );
		code.append( "\t* Cuando la pagina se hace visible\n" );
		code.append( "\t*/" );

		code.append( "\n\t" + "public void onVisible() {");

		//agregamos el codigo de onVisible de los widgets
		for (PageGeneratorElement widget : this.widgets) {
			code.append(widget.getOnVisibleCode());
		}

		code.append( "\n\t" + "}" + "\n");

		return code.toString();
	}


	private String getWidgetConstructorCode() {
		StringBuffer code = new StringBuffer();
		for (PageGeneratorElement widget : this.widgets) {
			code.append(widget.getConstructorCode());
			for (String addCode : widget.getAddToPageVariables()) {
				code.append("\t\t" + "this.add("+addCode+");\n");
			}
		}
		return "\t\t"+code.toString();
	}


	private String getConstructor() {
		return "\n\tpublic "+pageName+"() {";
	}


	private String getGlobalVariables() {
		StringBuffer globalCode = new StringBuffer();
		globalCode.append("\n\tprivate "+XslUtils.capitalize(caseName)+"ClientAsync server;\n");
		for (PageGeneratorElement widget : this.widgets) {
			for (String code : widget.getGlobalCode()) {
				globalCode.append("\t"+code+";\n");
			}
		}
		return globalCode.toString();
	}


	private String getServerCode() {
		//server = (CasoDePruebaClientAsync)GwtRpc.getInstance().getServer( UseCaseNames.CASO_DE_PRUEBA );
		StringBuffer codeSvr = new StringBuffer();
		codeSvr.append("\n\n\t\t//Cofiguramos el server con la interfaz asincronica y el nombre del caso de uso\n");
		codeSvr.append("\t\tserver = ("+XslUtils.capitalize(caseName)+"ClientAsync) GwtRpc.getInstance().getServer( UseCaseNames."+XslUtils.toConstantFormat(XslUtils.capitalize(caseName))+" );\n");


		return codeSvr.toString();
	}


	private String getPageName() {
		StringBuffer code = new StringBuffer();
		code.append("\npublic class "+pageName+" extends GwtPage ");
		//agrego los implements
		code.append(this.getAllInterfaces());
		return code.toString();
	}



	private void getAllImports_Interfaces() {
		//recorro los widget y compilo sus imports en un set
		 imports = new TreeSet<String>();
		 interfaces =  new TreeSet<String>();
		for (PageGeneratorElement widget : this.widgets) {
			imports.addAll(widget.getImports());
			interfaces.addAll(widget.getImplementedInterfaces());
		}

	}


	private String getAllImports() {
		//armo el buffer con todos los imports
		StringBuffer importCode = new StringBuffer();
		for (String imporT : imports) {
			importCode.append(imporT+";\n");
		}


		return importCode.toString();
	}

	private String getAllInterfaces(){
		//armo el buffer con todas las interfaces
		StringBuffer implementsCode = new StringBuffer();
		if(!interfaces.isEmpty()){
			implementsCode.append(" implements");
			for (String inter : interfaces) {
				implementsCode.append(" "+inter+",");
			}
			String code = implementsCode.toString();
			//quito la coma q pongo de mas
			return code.substring(0,code.length()-1)+" {\n";
		}else{
			return " {\n";
		}
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
		clientF = packageF.substring(0, packageF.indexOf(USE_CASES));
		rpcF = packageF.substring(0,packageF.indexOf(PAGES))+".rpc";
		caseName = packageF.substring(packageF.indexOf(USE_CASES), packageF.indexOf(PAGES)).substring(USE_CASES.length() + 1);
		pageName = arch.getPath();
		pageName = pageName.replace("/", ".");
		pageName = pageName.replace("\\", ".");
		System.out.println(pageName);
		System.out.println(pageName.indexOf(PAGES));
		System.out.println(pageName.indexOf(JAVA_PAGE_EXTENSION));
		pageName = pageName.substring(pageName.indexOf(PAGES),pageName.lastIndexOf(JAVA_PAGE_EXTENSION)).substring((PAGES+".").length());
	}



}
