package net.latin.server.utils.reports.visualizer;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.latin.server.utils.exceptions.LnwException;
import net.latin.server.utils.fileTypes.FileToShowOnClient;

/**
 * Implementacion base de un servlet que se encarga de mostrar un archivo
 * generado por la aplicacion.
 *
 * @author Martin D'Aloia
 */
public abstract class FileVisualizer extends HttpServlet {

	private static final long serialVersionUID = -5566210417021457114L;

	private static final Log LOG = LogFactory.getLog(FileVisualizer.class);

	/**
	 * Devuelve el archivo a mostrar en el cliente, sacandolo de la session
	 * @param request
	 */
	protected abstract FileToShowOnClient getFileToShow(HttpServletRequest request);


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//obtener archivo de la session que se quiere imprimir
		FileToShowOnClient file = this.getFileToShow(request);
		if(file == null) {
			response.setContentType("text/html");
			response.setContentLength(0);
			LOG.info("Se llamo al servlet FileVisualizer para mostrar un archivo pero no existia en la session.");
			return;
		}

		setNoCache(response);
		response.setContentType(file.getContentType());
		response.setContentLength(file.getLength());
		response.setHeader("Content-disposition","inline; filename=" + file.getName());

		byte[] contentToWrite = {};
		contentToWrite = file.getContent();
		LOG.info("Mostrando archivo: " + file.getName());

		try {
			OutputStream os = response.getOutputStream();
			os.write(contentToWrite);
			os.flush();
			os.close();
		} catch (IOException e) {
			String error = e.toString();

			// Si es un org.apache.catalina.connector.ClientAbortException el toString() tiene en nombre de la clase.
			// Si bien esto es feo hacerlo asi, me evita depender de un jar propio de tomcat.
			if(error != null && error.contains("ClientAbortException")) {
				String message = "El cliente cancelo el pedido por lo que no se pudo enviar el Stream de bytes del archivo: " + file.getName()
									+ " ContentType: " + file.getContentType();

				LOG.info(message);
			} else {
				String message = "No se pudo enviar al cliente el Stream de bytes del archivo: " + file.getName()
									+ " ContentType: " + file.getContentType();

				LOG.error(message, e);
				throw new LnwException(message, e );
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}


	/**
	 * Setea en el response los headers necesarios para que no se haga cache
	 * de los que se va a enviar como respuesta.
	 */
	private void setNoCache(HttpServletResponse response) {
		//FIXME ver como manejar bien la cache para descarga de PDF y archivos en general
		response.setHeader("Pragma", "");
		response.setHeader("Cache-Control", "");
		response.setDateHeader("max-age", 0);
		response.setDateHeader("Expires", 0);
	}
}
