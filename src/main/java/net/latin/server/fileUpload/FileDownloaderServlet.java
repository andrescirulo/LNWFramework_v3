package net.latin.server.fileUpload;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.latin.server.persistence.UserContext;
import net.latin.server.utils.exceptions.LnwException;

/**
 * Implementacion de un servlet que se encarga de descargar un archivo,
 * (igual al visualizer pero como attachment).
 *
 * @author lpinkas
 */
public class FileDownloaderServlet extends HttpServlet {

	private static final long serialVersionUID = -5743376818623706851L;

	public final static String FILE_SESSION_KEY = "__LNW_FILE_DOWNLOADER_KEY";

	private static final Log LOG = LogFactory.getLog(FileDownloaderServlet.class);

	/**
	 * Devuelve el archivo a bajar en el cliente, sacandolo de la session
	 * @param request
	 */
	public static void setFileToShow(FileToShowOnClient file) {
		UserContext.getInstance().getRequest().getSession().setAttribute( FILE_SESSION_KEY, file );
	}


	protected FileToShowOnClient getFileToShow(HttpServletRequest request) {
		return (FileToShowOnClient) request.getSession().getAttribute( FILE_SESSION_KEY );
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		FileToShowOnClient file = this.getFileToShow(request);

		byte[] contentToWrite = {};
		if(file == null) {
			response.setContentType("text/html");
			response.setContentLength(0);
			LOG.info("Se llamo al servlet para descargar un archivo pero no existia en la session.");
		} else {
			setNoCache(response);
			response.setContentType(file.getContentType());
			response.setContentLength(file.getLength());
			response.setHeader("Content-disposition","attachment; filename=\""+ file.getName() + "\"");

			contentToWrite = file.getContent();

			LOG.info("Descargando: " + file.getName());
		}

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
		response.setHeader("Pragma", "");
		response.setHeader("Cache-Control", "");
		response.setDateHeader("max-age", 0);
		response.setDateHeader("Expires", 0);
	}
}
