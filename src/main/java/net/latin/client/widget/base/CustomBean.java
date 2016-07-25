package net.latin.client.widget.base;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * Repositorio genérico de datos, que se guardan bajo una clave
 * de la forma [key-value].
 * Se pueden agregar o quitar datos en forma dinámica.
 * Los datos guardados son serealizados a String para poder
 * ser transmitidos en pedidos RPC de GWT.
 * Los tipos de datos que se pueden almacenar son:
 * <b>Valores primitivos:</b>String, Integer, Long, Float, Double, Boolean.
 * <b>Date</b>
 * <b>List</b>
 * <b>Arrays:</b> tienen que ser arrays de objetos. Ejemplo Integer[] y no int[].
 * <b>Otro CustomBean</b>
 *
 * Posee algunas características similares al patrón de diseño Active Records
 * </pre>
 *
 * @author Matias Leone
 *
 */
public class CustomBean extends GwtBusinessObject{

	/**
	 * Repositorio de propiedades [key-CustomBean] para el CustomBean
	 */
	private List<CustomBeanElement> repositorio;

	/**
	 * Valor en string para un objeto que sea de tipo primitivo
	 */
	private String objString;

	/**
	 * Valor lista para un objeto que sea del tipo array.
	 */
	private List<CustomBean> objList;

	/**
	 * Constructor default para ser serializable
	 */
	public CustomBean() {
		initRepositorio();
	}


	/**
	 * Agrega un objeto bajo la key definida.
	 * Si ya existe la key, será reemplazada.
	 * @param key
	 * @param obj
	 * @return
	 */
	public final CustomBean put( String key, Object obj ){
		initRepositorio();

		//serializar
		final CustomBean beanToAdd = new CustomBean().serialize(obj);

		//buscar si ya estaba
		final int index = searchElement(key);
		//si no estaba, agregarlo al final
		if( index < 0 ) {
			this.repositorio.add( new CustomBeanElement(key, beanToAdd ) );

		//sino, reemplazar el existente
		} else {
			this.repositorio.set( index, new CustomBeanElement(key, beanToAdd ) );
		}
		return this;
	}

	/**
	 * Agrega array bajo la key definida.
	 * Si ya existe la key, será reemplazada.
	 * Solo funciona con array de objectos. Ejemplo Integer[] (en vez de int[])
	 * @param key
	 * @param obj
	 * @return
	 */
	public final CustomBean put( String key, Object[] array ) {
		initRepositorio();

		//serializar
		final CustomBean beanToAdd = new CustomBean().serialize(array);

		//buscar si ya estaba
		final int index = searchElement(key);
		//si no estaba, agregarlo al final
		if( index < 0 ) {
			this.repositorio.add( new CustomBeanElement(key, beanToAdd ) );

		//sino, reemplazar el existente
		} else {
			this.repositorio.set( index, new CustomBeanElement(key, beanToAdd ) );
		}
		return this;
	}

	/**
	 * <pre>
	 * Carga el CustomBean con una unica propiedad.
	 * Esa propiedad puede ser de tres tipos:
	 * <b>CustomBean:</b> se sacan todos sus valores y se cargan en el bean actual
	 * <b>List:</b> todas sus variables son agregadas a la lista interna <code>objList</code>
	 * <b>Valor primitivo</b> (Long, Date, String, etc): se convierte a String de la forma mas conveniente
	 * </pre>
	 */
	private final CustomBean serialize( Object obj ) {
		//chequear null
		if( obj == null ) {
			//devolvemos null, porque no es necesario el bean
			return null;

			//limpiar todo
			//clear();
			//return this;
		}

		//Determinar de que forma almacenar el objeto en un contenido serializable, según su tipo

		//CUSTOM BEAN
		if( obj instanceof CustomBean ) {
			//cargar el actual con todas las cosas del bean
			final CustomBean bean = (CustomBean) obj;
			this.objString = bean.objString;
			this.objList = bean.objList;
			this.repositorio = bean.repositorio;

		//DATE
		} else if( obj instanceof Date ) {
			//pedir time y convertir a string
			objString = String.valueOf( ((Date)obj).getTime() );

		//LIST
		} else if( obj instanceof List ) {
			initObjList();
			//cargar cada propiedad objList
			final List dataList = (List)obj;
			final int size = dataList.size();
			for (int i = 0; i < size; i++) {
				//crear CustomBean con valor primitivo
				objList.add( new CustomBean().serialize( dataList.get( i ) ) );
			}

		//PRIMITIVE VALUE
		} else {
			//almacenar string
			objString = obj.toString();
		}

		return this;
	}

	/**
	 * Carga el CustomBean con una unica propiedad.
	 * Todas las variables del array son agregadas a la lista interna <code>objList</code>.
	 */
	public final CustomBean serialize( Object[] array ) {
		//chequear null
		if( array == null ) {
			//devolvemos null, porque no es necesario el bean
			return null;

			//limpiar todo
			//clear();
			//return this;
		}

		////cargar cada propiedad en objList
		initObjList();
		final int length = array.length;
		for (int i = 0; i < length; i++) {
			//crear CustomBean con valor primitivo
			objList.add( new CustomBean().serialize(array[ i ]) );
		}

		return this;
	}

	/**
	 * Inicializacion lazy de objList
	 */
	private final void initObjList() {
		//inicializacion lazy de lista
		if( objList == null ) {
			objList = new ArrayList<CustomBean>();
		}
	}

	/**
	 * Inicializacion lazy del repositorio
	 */
	private final void initRepositorio() {
		if( this.repositorio == null ) {
			this.repositorio = new ArrayList<CustomBeanElement>();
		}
	}

	/**
	 * Busca la posicion de un elemento bajo la key especificado
	 * @param key
	 * @return posición o -1 si no la encontró
	 */
	private final int searchElement( String key ) {
		if( repositorio == null ) return -1;

		final int size = repositorio.size();
		for (int i = 0; i < size; i++) {
			if( ((CustomBeanElement)repositorio.get( i )).key.equals( key ) ) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Quita una propiedad definida bajo la key especificada.
	 * @param key propiedad a eliminar.
	 * @rerturn true si la key existía y se eliminó y false si no existía.
	 */
	public final boolean remove( String key ) {
		final int index = searchElement(key);
		if( index < 0 ) return false;
		this.repositorio.remove( index );
		return true;
	}

	/**
	 * Limpia el CustomBean
	 */
	public final void clear() {
		repositorio = null;
		objList = null;
		objString = null;
	}

	/**
	 * Retorna una propiedad del tipo <code>Object</code>, bajo la key especificada,
	 * o null en caso de no existir.
	 * @param key propiedad deseada
	 * @return <code>Object</code> de la propiedad o null.
	 */
	public final Object get(String key){
		final int index = searchElement(key);
		if( index < 0 ) return null;
		return ((CustomBeanElement)this.repositorio.get( index )).value;
	}

	/**
	 * Retorna una propiedad del tipo <code>String</code>, bajo la key especificada,
	 * o null en caso de no existir.
	 * @param key propiedad deseada
	 * @return <code>String</code> de la propiedad o null.
	 */
	public final String getString(String key){
		try{
			return ((CustomBean)this.get(key)).getString();
		}catch (Exception e) {
			return null;
		}
	}

	/**
	 * @return valor <code>String</code> primitivo del CustomBean
	 */
	private final String getString(){
		return objString;
	}

	/**
	 * Retorna una propiedad del tipo <code>Double</code>, bajo la key especificada,
	 * o null en caso de no existir.
	 * @param key propiedad deseada
	 * @return <code>Double</code> de la propiedad o null.
	 */
	public final Double getDouble(String key){
		try{
			return ((CustomBean)this.get(key)).getDouble();
		}catch (Exception e) {
			return null;
		}
	}

	/**
	 * @return valor <code>Double</code> primitivo del CustomBean
	 */
	private final Double getDouble(){
		try{
			return Double.valueOf( objString );
		}catch (Exception e) {
			return null;
		}
	}

	/**
	 * Retorna una propiedad del tipo <code>Float</code>, bajo la key especificada,
	 * o null en caso de no existir.
	 * @param key propiedad deseada
	 * @return <code>Float</code> de la propiedad o null.
	 */
	public final Float getFloat(String key){
		try{
			return ((CustomBean)this.get(key)).getFloat();
		}catch (Exception e) {
			return null;
		}
	}

	/**
	 * @return valor <code>Float</code> primitivo del CustomBean
	 */
	private final Float getFloat(){
		try{
			return Float.valueOf( objString );
		}catch (Exception e) {
			return null;
		}
	}

	/**
	 * Retorna una propiedad del tipo <code>Long</code>, bajo la key especificada,
	 * o null en caso de no existir.
	 * @param key propiedad deseada
	 * @return <code>Long</code> de la propiedad o null.
	 */
	public final Long getLong(String key){
		try{
			return ((CustomBean)this.get(key)).getLong();
		}catch (Exception e) {
			return null;
		}
	}

	/**
	 * @return valor <code>Long</code> primitivo del CustomBean
	 */
	private final Long getLong(){
		try{
			return Long.valueOf( objString );
		}catch (Exception e) {
			return null;
		}
	}

	/**
	 * Retorna una propiedad del tipo <code>Integer</code>, bajo la key especificada,
	 * o null en caso de no existir.
	 * @param key propiedad deseada
	 * @return <code>Integer</code> de la propiedad o null.
	 */
	public final Integer getInt(String key){
		try{
			return ((CustomBean)this.get(key)).getInt();
		}catch (Exception e) {
			return null;
		}
	}

	/**
	 * @return valor <code>Integer</code> primitivo del CustomBean
	 */
	private final Integer getInt(){
		try{
			return Integer.valueOf( objString );
		}catch (Exception e) {
			return null;
		}
	}

	/**
	 * Retorna una propiedad del tipo <code>Boolean</code>, bajo la key especificada,
	 * o null en caso de no existir.
	 * @param key propiedad deseada
	 * @return <code>Boolean</code> de la propiedad o null.
	 */
	public final Boolean getBoolean(String key){
		try{
			return ((CustomBean)this.get(key)).getBoolean();
		}catch (Exception e) {
			return null;
		}
	}

	/**
	 * @return valor <code>Boolean</code> primitivo del CustomBean
	 */
	private final Boolean getBoolean(){
		try{
			return Boolean.valueOf( objString );
		}catch (Exception e) {
			return null;
		}
	}

	/**
	 * Retorna una propiedad del tipo <code>Date</code>, bajo la key especificada,
	 * o null en caso de no existir.
	 * @param key propiedad deseada
	 * @return <code>Date</code> de la propiedad o null.
	 */
	public final Date getDate(String key) {
		try{
			return ((CustomBean)this.get(key)).getDate();
		}catch (Exception e) {
			return null;
		}
	}

	/**
	 * @return valor <code>Date</code> primitivo del CustomBean
	 */
	private final Date getDate() {
		try{
			return new Date( Long.valueOf(objString).longValue() );
		}catch (Exception e) {
			return null;
		}
	}

	/**
	 * Retorna una propiedad del tipo <code>List</code>, bajo la key especificada,
	 * o null en caso de no existir.
	 * Cada elemento de la lista es un CustomBean.
	 * @param key propiedad deseada
	 * @return <code>List</code> de la propiedad o null.
	 */
	public final List getList(String key) {
		try{
			return ((CustomBean)this.get(key)).getList();
		}catch (Exception e) {
			return null;
		}
	}

	/**
	 * @return valor <code>List</code> primitivo del CustomBean
	 */
	private final List getList() {
		return objList;
	}

	/**
	 * Se crea una nueva lista de Strings con la key especificada.
	 * Los valrores que se encuentran dentro de la lista especificada deben ser
	 * String, de lo contrario se devuelve null.
	 * @param key propiedad deseada
	 * @return nueva lista de Strings creada o null.
	 */
	public final List<String> getListString(String key) {
		try {
			//creamos una nueva lista y metemos todos los valores String de la key especificada
			final List list = getList(key);
			final List<String> stringList = new ArrayList<String>();
			final int size = list.size();
			for (int i = 0; i < size; i++) {
				stringList.add( ((CustomBean)list.get( i )).getString() );
			}
			return stringList;

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Se crea una nueva lista de Integer con la key especificada.
	 * Los valrores que se encuentran dentro de la lista especificada deben ser
	 * Integer, de lo contrario se devuelve null.
	 * @param key propiedad deseada
	 * @return nueva lista de Integer creada o null.
	 */
	public final List<Integer> getListInteger(String key) {
		try {
			//creamos una nueva lista y metemos todos los valores String de la key especificada
			final List list = getList(key);
			final List<Integer> integerList = new ArrayList<Integer>();
			final int size = list.size();
			for (int i = 0; i < size; i++) {
				integerList.add( ((CustomBean)list.get( i )).getInt() );
			}
			return integerList;

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Se crea una nueva lista de Long con la key especificada.
	 * Los valrores que se encuentran dentro de la lista especificada deben ser
	 * Long, de lo contrario se devuelve null.
	 * @param key propiedad deseada
	 * @return nueva lista de Long creada o null.
	 */
	public final List<Long> getListLong(String key) {
		try {
			//creamos una nueva lista y metemos todos los valores String de la key especificada
			final List list = getList(key);
			final List<Long> longList = new ArrayList<Long>();
			final int size = list.size();
			for (int i = 0; i < size; i++) {
				longList.add( ((CustomBean)list.get( i )).getLong() );
			}
			return longList;

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Se crea una nueva lista de Float con la key especificada.
	 * Los valrores que se encuentran dentro de la lista especificada deben ser
	 * Float, de lo contrario se devuelve null.
	 * @param key propiedad deseada
	 * @return nueva lista de Float creada o null.
	 */
	public final List<Float> getListFloat(String key) {
		try {
			//creamos una nueva lista y metemos todos los valores String de la key especificada
			final List list = getList(key);
			final List<Float> floatList = new ArrayList<Float>();
			final int size = list.size();
			for (int i = 0; i < size; i++) {
				floatList.add( ((CustomBean)list.get( i )).getFloat() );
			}
			return floatList;

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Se crea una nueva lista de Double con la key especificada.
	 * Los valrores que se encuentran dentro de la lista especificada deben ser
	 * Double, de lo contrario se devuelve null.
	 * @param key propiedad deseada
	 * @return nueva lista de Double creada o null.
	 */
	public final List<Double> getListDouble(String key) {
		try {
			//creamos una nueva lista y metemos todos los valores String de la key especificada
			final List list = getList(key);
			final List<Double> doubleList = new ArrayList<Double>();
			final int size = list.size();
			for (int i = 0; i < size; i++) {
				doubleList.add( ((CustomBean)list.get( i )).getDouble() );
			}
			return doubleList;

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Se crea una nueva lista de Date con la key especificada.
	 * Los valrores que se encuentran dentro de la lista especificada deben ser
	 * Date, de lo contrario se devuelve null.
	 * @param key propiedad deseada
	 * @return nueva lista de Date creada o null.
	 */
	public final List<Date> getListDate(String key) {
		try {
			//creamos una nueva lista y metemos todos los valores String de la key especificada
			final List list = getList(key);
			final List<Date> dateList = new ArrayList<Date>();
			final int size = list.size();
			for (int i = 0; i < size; i++) {
				dateList.add( ((CustomBean)list.get( i )).getDate() );
			}
			return dateList;

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Se crea una nueva lista de Boolean con la key especificada.
	 * Los valrores que se encuentran dentro de la lista especificada deben ser
	 * Boolean, de lo contrario se devuelve null.
	 * @param key propiedad deseada
	 * @return nueva lista de Boolean creada o null.
	 */
	public final List<Boolean> getListBoolean(String key) {
		try {
			//creamos una nueva lista y metemos todos los valores String de la key especificada
			final List list = getList(key);
			final List<Boolean> booleanList = new ArrayList<Boolean>();
			final int size = list.size();
			for (int i = 0; i < size; i++) {
				booleanList.add( ((CustomBean)list.get( i )).getBoolean() );
			}
			return booleanList;

		} catch (Exception e) {
			return null;
		}
	}


	/**
	 * Retorna una propiedad del tipo <code>CustomBean</code>, bajo la key especificada,
	 * o null en caso de no existir.
	 * @param key propiedad deseada
	 * @return <code>CustomBean</code> de la propiedad o null.
	 */
	public final CustomBean getBean(String key) {
		try{
			return ((CustomBean)this.get(key));
		}catch (Exception e) {
			return null;
		}
	}

	/**
	 * Crea un nuevo CustomBean a partir del actual
	 */
	public final CustomBean clone() {
		final CustomBean customBean = new CustomBean();
		customBean.fillWithCustomBean( this );
		return customBean;
	}

	/**
	 * Llena el CustomBean actual a partir de otro.
	 * Todas aquellas propiedades que tengan igual key serán reemplazadas.
	 * @param bean bean con el cual llenar el actual
	 * @return CustomBean actual cargado con las nuevas propiedades
	 */
	public final CustomBean fillWithCustomBean(CustomBean bean) {
		if(bean == null){
			return this;
		}

		//inicializar repositorio
		initRepositorio();

		final List repositorio2 = bean.repositorio;
		int index;
		CustomBeanElement el;
		for (int i = 0; i < repositorio2.size(); i++) {
			el = (CustomBeanElement) repositorio2.get( i );
			index = searchElement( el.key );
			//si no estaba, agregarlo al final
			if( index < 0 ) {
				this.repositorio.add( new CustomBeanElement(el.key, el.value ) );

			//sino, reemplazar el existente
			} else {
				this.repositorio.set( index, new CustomBeanElement( el.key, el.value ) );
			}
		}

		return this;
	}

	/**
	 * <pre>
	 * Devuelve un mapa de valores planos, a partir del custom bean.
	 * Todo se intenta convertir a String.
	 * El mapa tendra una key por cada propiedad del CustomBean, y los value podrán ser:
	 * <i>Un String:</i> si es una propiedad primitiva
	 * <i>Otro mapa:</i> si es una propiedad compuesta (CustomBean)
	 * <i>Una Lista:</i> si es una propiedad List
	 * El CustomBean al que se le pide este objeto debe tener al menos una propiedad
	 * almacenada bajo una clave. No puede ser un CustomBean final.
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	public final Map<String,?> getStringMap() {
		if( repositorio == null ) {
			throw new RuntimeException( "Si pido getStringMap() a un CustomBean que no era final" );
		}

		//llamada recursiva
		Map<String,?> map = (Map<String,?>) this.internalGetStringMap();
		return map;
	}

	/**
	 * Recorre el bean y convierte a String plano todos sus atributos
	 * @param map
	 */
	private final Object internalGetStringMap() {
		////solo objString
		if( objString != null ) {
			return objString;
		}

		//solo objList
		else if(  objList != null ) {
			//cargamos los valores dentro de una lista
			final List<Object> auxList = new ArrayList<Object>();
			final int size = objList.size();
			CustomBean bean;
			for (int i = 0; i < size; i++) {
				bean = (CustomBean) objList.get( i );
				auxList.add( bean != null ? bean.internalGetStringMap() : null );
			}
			return auxList;


		//repositorio
		} else if( repositorio != null ) {
			//cargamos los valores dentro de un mapa
			final int size = repositorio.size();
			CustomBeanElement el;
			final Map<String,Object> auxMap = new HashMap<String,Object>();
			for (int i = 0; i < size; i++) {
				el = (CustomBeanElement) repositorio.get( i );
				auxMap.put( el.key,  el.value != null ? el.value.internalGetStringMap() : null );
			}
			return auxMap;


		//no hay nada
		} else {
			return null;
		}
	}



	public final String toString() {
		//solo objString
		if( objString != null ) {
			return objString;


		//solo objList
		} else if( objList != null ) {
			final StringBuffer buffer = new StringBuffer();
			buffer.append( "[" );
			final int sizeList = objList.size();
			for (int j = 0; j < sizeList; j++) {
				final CustomBean beanSon = ((CustomBean)objList.get( j ));
				buffer.append( (beanSon != null ? beanSon.toString() : "null") + ", " );
			}
			buffer.append( "]" );
			return buffer.toString();

		//repositorio
		} else if( repositorio != null ){
			final StringBuffer buffer = new StringBuffer();
			buffer.append( "CustomBean { " );
			final int size = repositorio.size();
			CustomBeanElement el;
			for (int i = 0; i < size; i++) {
				el = (CustomBeanElement) repositorio.get( i );
				buffer.append( "$" + el.key + "$=" );

				//llamada recursiva
				buffer.append( el.value != null ? el.value.toString() : "null" );
				buffer.append( " " );
			}

			buffer.append( " }" );
			return buffer.toString();

		//no hay nada
		} else {
			return "CustomBean { EMPTY }";
		}

	}

}
