package net.latin.server.security.login;

import java.util.List;

import net.latin.client.widget.menu.data.CompositeMenuItem;
import net.latin.client.widget.menu.data.MenuItem;
import net.latin.server.persistence.UserContext;
import net.latin.server.persistence.user.LnwUser;
import net.latin.server.security.config.LnwGeneralConfig;
import net.latin.server.security.config.LnwMenuGroup;
import net.latin.server.security.config.LnwMenuItem;

/**
 * Builder para generar el menu que necesita GwtController
 *
 * @author Matias Leone
 */
public class LnwMenuBuilder {

	/**
	 * Interface que dado los datos de un item de menu, obtiene de alguna forma
	 * los datos solicitados.
	 * Esto permite por ejemplo ir a una base de datos a buscar los titulos del
	 * menu.
	 *
	 */
	public interface LnwMenuDataGetter {

		/**
		 * Obtiene el titulo a mostrar para el item indicado.
		 */
		public String getTitle(LnwMenuItem item);

		public String getTitle(LnwMenuGroup group);
	}


	/**
	 * Crea la informacion del menu de Gwt en funcion a los permisos de acceso
	 * del usuario en cuestion
	 */
	public static MenuItem buildGwtMenu() {
		return buildGwtMenu(null);
	}

	/**
	 * Crea la informacion del menu de Gwt en funcion a los permisos de acceso
	 * del usuario en cuestion, pero obtiene los datos del menu a traves del
	 * data getter indicado.
	 */
	public static MenuItem buildGwtMenu(LnwMenuDataGetter dataGetter) {
		LnwUser user = UserContext.getInstance().getUser();
		List<LnwMenuGroup> groups = null;
		CompositeMenuItem root = new CompositeMenuItem();
		if (user!=null){
			groups = LnwGeneralConfig.getInstance().getGroups();

			/**
			 * recorremos todos los casos de uso que hay y creamos el
			 * menu solo con los que el usuario puede ver
			 */
			for (LnwMenuGroup group : groups) {
				CompositeMenuItem compItem = new CompositeMenuItem();
				if(dataGetter != null) {
					compItem.setName( dataGetter.getTitle(group) );
				}
				else{
					compItem.setName( group.getTitle() );
				}
				for (LnwMenuItem item : group.getMenuItems()) {
					if( user.hasAccess( item.getId() ) ) {
						MenuItem menuItem = item.buildGwtMenu();
						if(dataGetter != null) {
							menuItem.setName(dataGetter.getTitle(item));
						}
						compItem.addChild( menuItem );
					}
				}
				if ( compItem.getChilds().size() > 0 ) {
					root.addChild( compItem );
				}
			}
		
		}
		else{
			groups = LnwGeneralConfig.getInstance().getPublicGroups();
			
			for (LnwMenuGroup group : groups) {
				CompositeMenuItem compItem = new CompositeMenuItem();
				if(dataGetter != null) {
					compItem.setName( dataGetter.getTitle(group) );
				}
				else{
					compItem.setName( group.getTitle() );
				}
				for (LnwMenuItem item : group.getMenuItems()) {
					MenuItem menuItem = item.buildGwtMenu();
					if(dataGetter != null) {
						menuItem.setName(dataGetter.getTitle(item));
					}
					compItem.addChild( menuItem );
				}
				if ( compItem.getChilds().size() > 0 ) {
					root.addChild( compItem );
				}
			}
		}
		return root;
	}

}
