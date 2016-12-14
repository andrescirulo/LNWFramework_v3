package net.latin.server.commonUseCase;

import java.io.ByteArrayOutputStream;

import javax.servlet.http.HttpSession;

import net.latin.client.rpc.commonUseCase.CommonUseCaseClient;
import net.latin.client.rpc.commonUseCase.InitialInfo;
import net.latin.client.rpc.commonUseCase.LnwTableDocumentData;
import net.latin.server.GwtUseCase;
import net.latin.server.persistence.UserContext;
import net.latin.server.persistence.user.LnwUser;
import net.latin.server.security.config.LnwGeneralConfig;
import net.latin.server.utils.reports.tableDocument.LnwTableDocumentGeneratorListener;
import net.latin.server.utils.reports.visualizer.ReportViewerServlet;

/**
 * Caso de uso utilizado por el framework para validar acceso de los usuarios.
 * Tiene que estar registrado en commons access.
 *
 */
public class CommonUseCase extends GwtUseCase implements CommonUseCaseClient {

	public static final String SESSION_ID_SAU_KEY = "sessionId";
	public static final String INV_SES_AF_US_EX_KEY = "__INVALIDATE_SESSION_AFTER_USER_EXIT__";
	public static final String SAU_URL_KEY = "sauUrl";

	@Override
	protected String getServiceName() {
		return "CommonUseCase";
	}

	@Override
	public InitialInfo getInitialInfo() {
		InitialInfo info = new InitialInfo();
		//cargar datos adicionales
		CommonUseCaseImpl impl = LnwGeneralConfig.getInstance().getCommonUseCaseImpl();
		info.setAdditionalInfo( impl.getInitialInfo() );
		info.setAplicacionDescripcion(LnwGeneralConfig.getInstance().getApplicationDescription());
		
		
		//cargar datos generales
		if(UserContext.getInstance().getUser()!=null){
			info.setSessionIdSau(getRequest().getSession().getAttribute(SESSION_ID_SAU_KEY)+"");
			info.setSauUrl(getRequest().getSession().getAttribute(SAU_URL_KEY)+"");
			
			info.setCategoriaDescripcion(UserContext.getInstance().getUser().getCategoriaDescripcion());
			info.setPerfilDescripcion(UserContext.getInstance().getUser().getPerfilDescripcion());
			info.setNombreCompletoUsuario(UserContext.getInstance().getUser().getNombreCompleto());
			//cargar entradas de menu segun lo que indica la implementacion de cada proyecto
			info.setMenu(impl.getGwtMenu());
		}
		else{
			if (LnwGeneralConfig.getInstance().hasPublicGroups()){
				info.setMenu(impl.getGwtMenu());
			}
			else{
				//QUE HACER SI NO HAY MENU
			}
//			LnwGeneralConfig.getInstance().getPublicGroups();
//			CompositeMenuItem gwtMenu = new CompositeMenuItem();
//			LeafMenuItem menuItem1 = new LeafMenuItem();
//			menuItem1.setName("Inicio");
//			menuItem1.setUrl("InicioCase");
//			
//			CompositeMenuItem cmenu1=new CompositeMenuItem();
//			cmenu1.setName("Menu compuesto");
//			
//			LeafMenuItem menuItem2 = new LeafMenuItem();
//			menuItem2.setName("Link hijo 1");
//			LeafMenuItem menuItem3 = new LeafMenuItem();
//			menuItem3.setName("Link hijo 2");
//			
//			cmenu1.addChild(menuItem2);
//			cmenu1.addChild(menuItem3);
//			
//			gwtMenu.addChild(menuItem1);
//			gwtMenu.addChild(cmenu1);
//			info.setMenu(gwtMenu);
		}
		return info;
	}

	@Override
	public Boolean checkAccess(final String serviceName) {
		//get user data
		final LnwUser user = UserContext.getInstance().getUser();
		if (user == null) {
			getLog().info( "No se pudo chequear el acceso porque el usuario no se encuentra registrado en la session. Caso: " + serviceName);
//			return false;
			//FIXME TEMPORAL PARA NO NECESITAR USUARIO
			return true;
		}

		final long userId = user.getId();
		final String userName = user.getNombreCompleto();

		//check access
		final boolean hasAccess = user.hasAccess(serviceName);

		//loggear resultado
		if(hasAccess) {
			getLog().debug( "Acceso inicial otorgado a User[id,name]: ["+userId + ", " + userName + "]" + " Caso: " + serviceName);
		} else {
			getLog().error( "Acceso inicial DENEGADO a User[id,name]: ["+userId + ", " + userName + "]" + " Caso: " + serviceName);
		}

		return hasAccess;
	}

	@Override
	public void generateTableDocument( String fileName, String fileType, LnwTableDocumentData data ) {
		LnwTableDocumentGeneratorListener tableDocumentGenerator = null;
		try {
			//Establezco el tipo de archivo.
			tableDocumentGenerator = (LnwTableDocumentGeneratorListener)
				Class.forName( fileType ).newInstance();

			//create document
			ByteArrayOutputStream output = tableDocumentGenerator.makeDocument( data );

        	//store the document in session
        	ReportViewerServlet.setFileToShow( tableDocumentGenerator.createAssociation( fileName, output ) );

        	//Retorno si no hubo error
        	return;

		} catch ( Exception e ) {
			e.printStackTrace();
		}
		throw new RuntimeException();
	}

	@Override
	public void onUserExit() {
		
		Boolean invalidate = (Boolean)UserContext.getInstance().getRequest().getSession().getAttribute(INV_SES_AF_US_EX_KEY);
		if(invalidate == null || invalidate){
		//cerramos la session
			HttpSession session = UserContext.getInstance().getRequest().getSession(false);
			if(session != null) session.invalidate();
		}
		if(invalidate != null){
			UserContext.getInstance().getRequest().getSession().removeAttribute(INV_SES_AF_US_EX_KEY);
		}
	}

	@Override
	public String getCaseDocumentation(String useCaseName, String pageName) {
		CommonUseCaseImpl impl = LnwGeneralConfig.getInstance().getCommonUseCaseImpl();
		return impl.getCaseDocumentation(useCaseName, pageName);
	}

}
