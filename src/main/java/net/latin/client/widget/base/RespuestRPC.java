package net.latin.client.widget.base;

import java.util.List;

/**
 * Es un contenedor de datos para la comunicacion entre los clientes y el
 * servidor
 * 
 */
public class RespuestRPC<T extends GwtBusinessObject> extends SimpleRespuestRPC {

	private T businessObject;
	private List<T> businessObjects;
	private CustomBean valoresExtra;

	public RespuestRPC() {
		valoresExtra = new CustomBean();
	}

	public T getBusinessObject() {
		return businessObject;
	}

	public void setBusinessObject(T businessObject) {
		this.businessObject = businessObject;
	}

	public void setBusinessObjectsList(List<T> businessObjects) {
		this.businessObjects = businessObjects;
	}

	public List<T> getBusinessObjectsList() {
		return businessObjects;
	}

	public void putValorExtra(String key, Object value) {
		valoresExtra.put(key, value);
	}

	public CustomBean getValoresExtra() {
		return valoresExtra;
	}

}
