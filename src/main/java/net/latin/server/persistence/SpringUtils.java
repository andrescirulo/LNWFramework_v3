package net.latin.server.persistence;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.nativejdbc.CommonsDbcpNativeJdbcExtractor;
import org.springframework.jdbc.support.nativejdbc.SimpleNativeJdbcExtractor;
import org.springframework.transaction.support.TransactionTemplate;

import net.latin.server.utils.collections.CollectionFactory;
import net.latin.server.utils.exceptions.LnwException;

/**
 * Utilidades de acceso al contenedor de Spring del archivo config/SpringConfig.xml.
 * Utilizado principalmente para la configuracion de JDBC.
 *
 * @author Fernando Diaz
 */
public class SpringUtils {


	/**
	 * Ubicacion del xml de configuracion de Spring
	 */
	private static final String PATH_XML_SPRING = "SpringConfig.xml";

	/**
	 * Nombre del bean para el dataSource general de la aplicacion
	 */
	private static final String APPLICATION_DATA_SOURCE = "applicationDataSource";

	/**
	 * Nombre del bean para guardar las properties propias de cada app
	 */
	private static final String APPLICATION_PROPERTIES = "applicationProperties";
	private static JdbcTemplate jdbcTemplate;
	private static TransactionTemplate transactionTemplate;
	private static final DefaultListableBeanFactory factory;
	private static final Map<String, TransactionTemplate> transactionMap;
	private static final Map<String, JdbcTemplate> connectionMap;
	private static LnwProperties applicationProperties;
	private static Boolean USE_SPRING_PERSISTENCE_TEMPLATES;



	/**
	 * No se puede instanciar
	 */
	private SpringUtils() {
	}


	/**
	 * Levantamos el bean factory de Spring y el data source
	 */
	static {

		connectionMap = new HashMap<String, JdbcTemplate>();
		transactionMap = new HashMap<String, TransactionTemplate>();

		//get SpringConfig.xml
		FileSystemResource resource = null;
		try {
			File file = new File(SpringUtils.class.getClassLoader().getResource(PATH_XML_SPRING).toURI() );
			resource  = new FileSystemResource( file );
		} catch (Exception e) {
			throw new LnwException( "No se pudo levantar la configuracion necesaria para Spring. Path: " + PATH_XML_SPRING );
		}


		//creates Bean Factory
		factory = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
		reader.loadBeanDefinitions(resource);
		
		if(useSpringPersistenceTemplates()) {
			//cache important access
			jdbcTemplate = getJdbcTemplate(APPLICATION_DATA_SOURCE);
			transactionTemplate = getTransactionTemplate(APPLICATION_DATA_SOURCE);
		}
	}

	/**
	 * Indica si se debe usar y configurar los templates de pesistencia de Spring
	 * o si se deja a la aplicacion que lo haga por su cuenta (por ejemplo para
	 * usar Hibernate).
	 *
	 * Es configurable via la propiedad "use.spring.persistence.templates" en el
	 * SpringConfig.xml
	 *
	 * Por defecto y para mantener la compatibilidad hacia atras, si esta propiedad
	 * no esta configurada se devuelve true.
	 */
	public static boolean useSpringPersistenceTemplates() {
		//la cacheamos para que no la pida constantemente y afecte la performance.
		if(USE_SPRING_PERSISTENCE_TEMPLATES == null) {
			String useSpring = getProperty("use.spring.persistence.templates");

			USE_SPRING_PERSISTENCE_TEMPLATES = (useSpring == null || Boolean.parseBoolean(useSpring));
		}

		return USE_SPRING_PERSISTENCE_TEMPLATES;
	}


	/**
	 * Devuelve un punto de salida a partir del cual se puede interactuar con JDBC,
	 * para el dataSource APPLICATION_DATA_SOURCE
	 * Ejemplo:<pre>jdbcTemplate.query("select * from ejemplo_user",new RowMapper()...);</pre>
	 */
	public static final JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	/**
	 * Devuelve un punto de salida a partir del cual se puede interactuar con JDBC, para un DataSource especifico
	 */
	public static final JdbcTemplate getJdbcTemplate( String dataSourceName ){
		if( !connectionMap.containsKey(dataSourceName)){
			try{

				//creo el dataSource
				//Get application data source
				final DataSource dataSource = (DataSource) factory.getBean( dataSourceName );

				//Creates jdbcTemplate to play with JDBC
				final JdbcTemplate template = new JdbcTemplate(dataSource);


				//configurar NativeExtractor segun el tipo de DataSource seteado
				//DBCP
				//ALTA CHANTADA
//				if( dataSource instanceof BasicDataSource ) {
				if ("BasicDataSource".equals(dataSource.getClass().getSimpleName())){
					template.setNativeJdbcExtractor( new CommonsDbcpNativeJdbcExtractor() );
				//Simple JDBC
				} else {
					template.setNativeJdbcExtractor( new SimpleNativeJdbcExtractor() );
				}


				//store JdbcTemplate
				connectionMap.put(dataSourceName, template);

			} catch( BeansException e){
				throw new LnwException("No se encontro el dataSource requerido "+dataSourceName+" ",e);
			}
		}
		return connectionMap.get(dataSourceName);
	}

	/**
	 * Devuelve un manejador de transacciones para el dataSource APPLICATION_DATA_SOURCE
	 * Ejemplo:<pre>transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(TransactionStatus transactionStatus)  {
				try {
					update1();
					update2();
				}catch (Exception e) {
					transactionStatus.setRollbackOnly();
				}
			}

		});</pre>
	 */
	public static final TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}

	/**
	 * Devuelve un manejador de transacciones para el dataSource especificado
	 * @param dataSourceName
	 * @return
	 */
	public static final TransactionTemplate getTransactionTemplate( String dataSourceName ) {
		//lazy-init
		if( !transactionMap.containsKey(dataSourceName)){

			//creates a transaction manager
			final DataSource dataSource = getJdbcTemplate(dataSourceName).getDataSource();
			final DataSourceTransactionManager manager = new DataSourceTransactionManager(dataSource);
			final TransactionTemplate transactionTemplate = new TransactionTemplate(manager);

			//store transactionTemplate
			transactionMap.put(dataSourceName, transactionTemplate);
		}

		return transactionMap.get(dataSourceName);
	}


	/**
	 * @return beanFactory para acceder a los beans de SpringConfig.xml
	 */
	public static final DefaultListableBeanFactory getBeanFactory() {
		return factory;
	}

	/**
	 * Devuelve una property de applicationProperties del SpringConfig.xml
	 * @param key
	 * @return
	 */
	public static final String getProperty( String key ) {
		//lazy init
		if( applicationProperties == null ) {
			applicationProperties = (LnwProperties)factory.getBean( APPLICATION_PROPERTIES );
		}

		return applicationProperties.getProperties().getProperty(key);
	}
	
	/**
	 * Devuelve una property de applicationProperties del SpringConfig.xml
	 * @param key
	 * @return
	 */
	public static final String getProperty( String key ,String defaultValue) {
		//lazy init
		if( applicationProperties == null ) {
			applicationProperties = (LnwProperties)factory.getBean( APPLICATION_PROPERTIES );
		}
		
		return applicationProperties.getProperties().getProperty(key,defaultValue);
	}

	/**
	 * @param propertyName propertyBuscada
	 * @param regex expresion regular para el split
	 * @return this.getProperty(propertyName).split(regex) o null
	 */
	public static String[] getSplitProperty(String propertyName, String regex) {
		String string = getProperty(propertyName);
		return string != null ? string.split(regex) : null;
	}

	/**
	 * @return getSplitProperty(propertyName, ";")
	 */
	public static String[] getSplitProperty(String propertyName) {
		return getSplitProperty(propertyName, ";");
	}


	/**
	 * Utilizar este metodo para properties que sean multivaluadas. Se toma como separador la coma (,).
	 *
	 * @param propertyName la key de la property.
	 * @return una collection con todos los valores de la clave
	 */
	public static List<String> getAllProperties(String propertyName) {
		String property = getProperty(propertyName);
		StringTokenizer tokenizer = new StringTokenizer(property, ",");

		List<String> propertyValues = CollectionFactory.createList();

		while (tokenizer.hasMoreTokens()) {
			propertyValues.add(tokenizer.nextToken());
		}

		return propertyValues;
	}


}
