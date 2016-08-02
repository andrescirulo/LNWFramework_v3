package net.latin.client.widget.list;

import net.latin.client.widget.base.CustomBean;

/**
 * Adapter de ListBox simplificado para una key de CustomBean
 *
 */
public class CustomBeanListBoxAdapter implements GwtListBoxAdapter<CustomBean> {

	private String key;

	/**
	 * @param key clave de CustomBean que se quiere mostrar en la descripcion
	 * del ListBox
	 */
	public CustomBeanListBoxAdapter(String key) {
		this.key = key;
	}

	public String getListBoxDescription(CustomBean itemModel) {
		return itemModel.getString( key );
	}

}
