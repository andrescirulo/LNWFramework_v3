package net.latin.client.widget.list;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.user.client.Event;
import com.vaadin.polymer.vaadin.widget.VaadinComboBox;

import net.latin.client.widget.GwtWidgetUtils;
import net.latin.client.widget.base.LnwWidget;
import net.latin.client.widget.listener.EnterKeyPressHandler;

/**
 * ListBox that holds a reference to object which represent each text shown in
 * the list
 * 
 */
public class GwtListBox<T> extends VaadinComboBox implements LnwWidget {
	
	private List<T> modelList = new ArrayList<T>();
	private GwtListBoxAdapter<T> adapter;
	private Comparator searchComparator;
	private T elementoSeleccionar;
	private LnwWidget nextFocus;
	
	public GwtListBox() {
		setItems(getNativeArray());
		addDomHandler(new EnterKeyPressHandler(){
			protected void accionEnter(KeyPressEvent event) {
				if (nextFocus != null) {
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {
						public void execute() {
							nextFocus.setFocus();
						}
					});
				}
			}} , KeyPressEvent.getType());
	}
	
	private native JsArray getNativeArray()/*-{ 
		return []; 
	}-*/;
	
	/**
	 * Add a new item at the end of the list
	 * 
	 * @param itemText
	 *            : text to show in the list
	 * @param itemModel
	 *            : object related with the text
	 */
	public void addElement(String itemText, T itemModel) {
		this.modelList.add(itemModel);
		addItem(getItems(),itemText);
	}
	private native void addItem(JsArray items,String valor)/*-{
		items.push(valor);
	}-*/;
	
	
	/**
	 * Add a new item at the end of the list
	 * 
	 * @param itemModel
	 *            : object related with the adapter
	 */
	public void addElement(T itemModel) {
		this.addElement(adapter.getListBoxDescription(itemModel), itemModel);
	}
	
	/**
	 * Add a new item at the end of the list with tooltip
	 * 
	 * @param itemText
	 *            : text to show in the list
	 * @param itemModel
	 *            : object related with the text
	 * @param tooltip
	 *            : text to show in the tooltip
	 */
	public void addElementWithTooltip(String itemText, T itemModel, String tooltip) {
		this.modelList.add(itemModel);
		
		SelectElement select = (SelectElement) this.getElement().cast();
		OptionElement option = Document.get().createOptionElement();
		option.setText(itemText);
		option.setValue(itemText);
		option.setTitle(tooltip);
		
		select.add(option, null);
	}
	
	/**
	 * Add a list of object to the list with tooltip
	 * 
	 * @param itemList
	 *            : list of object to add
	 * @param maxLenght
	 *            : maxLenght of each item (in caracters)
	 */
	public void addAllWithTooltip(List<T> itemList, Integer maxLenght) {
		for (T t : itemList) {
			String texto = adapter.getListBoxDescription(t);
			if (texto != null && texto.length() > maxLenght) {
				texto = texto.substring(0, maxLenght - 3) + "...";
			}
			this.addElementWithTooltip(texto, t, adapter.getListBoxDescription(t));
		}
	}
	
	/**
	 * Add a list of object to the list
	 * 
	 * @param itemList
	 *            : list of object to add
	 * @param adapter
	 */
	public void addAll(List<T> itemList, GwtListBoxAdapter<T> adapter) {
		for (T t : itemList) {
			this.addElement(adapter.getListBoxDescription(t), t);
		}
	}
	
	/**
	 * Add a list of object to the list. Using an Adapter previously setted
	 * 
	 * @param itemList
	 *            : list of object to add
	 */
	public void addAll(List<T> itemList) {
		for (T t : itemList) {
			this.addElement(adapter.getListBoxDescription(t), t);
		}
	}
	
	/**
	 * Add a new item at a specific index of the list
	 * 
	 * @param itemText
	 *            : text to show in the list
	 * @param itemModel
	 *            : object related with the text
	 * @param index
	 *            : position to add the item
	 */
	public void insertItem(String itemText, T itemModel, int index) {
		this.insertItem(getItems(),itemText, index);
		this.modelList.add(index, itemModel);
	}
	
	private native void insertItem(JsArray items,String itemText, int index)/*-{
		items.splice(index, 0, itemText);
	}-*/;
	
	/**
	 * Add a new item at a specific index of the list
	 * T is a String
	 * 
	 * @param itemText
	 *            : text to show in the list
	 * @param itemModel
	 *            : object related with the text 
	 * @param index
	 *            : position to add the item
	 */
	public void insertStringItem(String itemText, T itemModel, int index) {
		this.insertItem(getItems(),itemText, index);
		this.modelList.add(index, itemModel);
	}
	
	/**
	 * @param index
	 * @return the object related with the text in the position specified
	 */
	public T getElement(int index) {
		if (index < 0 || index >= this.modelList.size()) {
			throw new RuntimeException("No hay items en la posicion: " + index);
		}
		return this.modelList.get(index);
		
	}
	
	/**
	 * @return the selected object in the list
	 */
	public T getSelectedElement() {
		return this.getElement(this.getSelectedIndex(getItems()));
	}
	
	public native int getSelectedIndex(JsArray items)/*-{
		for (var i=0;i<items.length;i++){
			if (items[i]==this.getValue()){
				return i;
			}
		}
		return -1;
	}-*/;
	
	
	public void removeItem(int index) {
		this.removeItemAt(getItems(),index);
		this.modelList.remove(index);
	}
	
	private native int removeItemAt(JsArray items,int index)/*-{
		items.splice(index,1);
	}-*/;
	
	/**
	 * Removes the item at the specified index.
	 * 
	 * @param itemModel
	 *            object related with a text in the list
	 */
	public void removeItem(T itemModel) {
		this.removeItem(this.getItemIndex(itemModel));
	}
	
	/**
	 * @param itemModel
	 *            object related with a text in the list
	 * @return the index of the object specified
	 */
	private int getItemIndex(T itemModel) {
		for (int i = 0; i < this.modelList.size(); i++) {
			if (this.modelList.get(i).equals(itemModel)) {
				return i;
			}
		}
		throw new RuntimeException("No se ha encontrado el item correspondiente al objeto: " + itemModel.toString());
	}
	
	/**
	 * @param itemModel
	 *            object related with a text in the list
	 * @param comparator
	 *            item comparator
	 * @return the index of the object specified
	 */
	private int getItemIndex(T itemModel, Comparator comparator) {
		for (int i = 0; i < this.modelList.size(); i++) {
			if (comparator.compare(this.modelList.get(i), itemModel) == 0) {
				return i;
			}
		}
		throw new RuntimeException("No se ha encontrado el item correspondiente al objeto: " + itemModel.toString());
	}
	
	/**
	 * Selecciona el item especificado. Se utiliza el comparator previamente
	 * seteado con <code>setComparator</code>. Si no hay, se busca por
	 * <code>equals</code>. <br>
	 * Las comparaciones por <code>equals</code> no funcionan con objetos que
	 * han sido creados en distintos momentos. <br>
	 * Ejemplo: un objeto cargado inicialemente en el ListBox no es igual a uno
	 * similar traído luego en un pedido RPC.
	 * 
	 * @param itemModel
	 */
	public void setSelectedItem(T itemModel) {
		int itemIndex;
		if (searchComparator != null) {
			itemIndex = this.getItemIndex(itemModel, searchComparator);
		} else {
			itemIndex = this.getItemIndex(itemModel);
		}

		this.setSelectedIndex(getItems(),itemIndex);
	}
	
	private native void setSelectedIndex(JsArray items,int itemIndex)/*-{
		this.setValue(items[itemIndex]);
	}-*/;

	/**
	 * Select the specified item, search with a comparator. El item pasado para
	 * comparar es el segundo elemento que será pasado como parámetro al
	 * Comparator.
	 * 
	 * @param itemModel
	 * @param comparator
	 */
	public void setSelectedItem(T itemModel, Comparator comparator) {
		this.setSelectedIndex(getItems(),this.getItemIndex(itemModel, comparator));
	}
	
	/**
	 * Permite saber si un item especifico es parte de los elementos de la
	 * lista.
	 * 
	 * @param itemModel
	 *            Item a chequear si existe en la lista o no
	 * 
	 * @return True si existe, false en caso contrario o si itemModel es null.
	 */
	public boolean containsItem(T itemModel) {
		if (itemModel == null) {
			return false;
		}
		
		try {
			if (searchComparator != null) {
				this.getItemIndex(itemModel, searchComparator);
			} else {
				this.getItemIndex(itemModel);
			}
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Borra la lista de opciones y si tiene seteado el elmentoSeleccionar lo
	 * agrega primero en la lista
	 */
	public void clear() {
		super.clear();
		this.modelList.clear();
		if (elementoSeleccionar != null) {
			insertItem(adapter.getListBoxDescription(elementoSeleccionar), elementoSeleccionar, 0);
		}
	}
	
	/**
	 * Setea el primer item de la lista.
	 */
	public void resetWidget() {
		if (!modelList.isEmpty()) {
			this.setSelectedIndex(getItems(),0);
		}
	}
	
	public void setFocus() {
		GwtWidgetUtils.setFocus(this);
	}
	
	/**
	 * Carga un adapter para saber que detalle visual mostrar en el ListBox
	 * 
	 * @param adapter
	 */
	public void setAdapter(GwtListBoxAdapter<T> adapter) {
		this.adapter = adapter;
	}
	
	/**
	 * Carga un adapter simplificado para trabajar con CustomBeans
	 * 
	 * @param key
	 *            key del CustomBean que se desea obtener para mostrar en el
	 *            detalle visual del ListBox
	 */
	public void setCustomBeanAdapter(String key) {
		this.adapter = (GwtListBoxAdapter<T>) new CustomBeanListBoxAdapter(key);
	}
	
	/**
	 * Redefinido para soportar doble click sobre los elementos del ListBox
	 */
	protected void onLoad() {
		super.onLoad();
		sinkEvents(Event.ONDBLCLICK);
	}
	
	/**
	 * Setea el comparator para utilizar como default en
	 * <code>setSelectedItem()</code>. <br>
	 * El item pasado para comparar es el segundo elemento que será pasado como
	 * parámetro al Comparator.
	 */
	public void setComparator(Comparator searchComparator) {
		this.searchComparator = searchComparator;
	}
	
	/**
	 * Debe tener seteado un adapter
	 * 
	 * @param elementoSeleccionar
	 */
	public void setSeleccionar(T elementoSeleccionar) {
		this.elementoSeleccionar = elementoSeleccionar;
		insertItem(adapter.getListBoxDescription(elementoSeleccionar), elementoSeleccionar, 0);
	}
	
	/**
	 * Setea el elemento al que se debe hacer foco al presionar la tecla enter
	 * 
	 * @param widget
	 */
	public void setNextFocus(LnwWidget widget) {
		nextFocus = widget;
	}

	public List<T> getAllItems() {
		return modelList;
	}
	
	public T getSeleccionar(){
		return elementoSeleccionar;
	}

}
