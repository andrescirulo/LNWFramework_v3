package net.latin.server.utils.reports.visualizer;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.latin.server.utils.exceptions.LnwException;
import net.latin.server.utils.fileTypes.Html;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HtmlVisualizer extends HttpServlet {

	private static final String scriptPrint = "<script>print(\"\");</script>";
	private static final Log LOG = LogFactory.getLog(FileVisualizer.class);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//obtener archivo de la session que se quiere imprimir
		Html file = (Html) request.getSession().getAttribute(ReportViewerServlet.REPORT_SESSION_KEY);
		if(file == null) {
			response.setContentType("text/html");
			response.setContentLength(0);
			LOG.info("Se llamo al servlet HtmlVisualizer para mostrar un archivo pero no existia en la session.");
			return;
		}

		setNoCache(response);
		response.setContentType(file.getContentType());
		response.setHeader("Content-disposition","inline; filename=" + file.getName());
		response.setContentLength(file.getLength());

		String contentToWrite = file.getStringContent();
		String printable = request.getParameter("printable");

		if(printable!= null && printable.matches("\"true\"")){
			contentToWrite = contentToWrite+scriptPrint;
			response.setContentLength(contentToWrite.length());
		}

		LOG.info("Mostrando archivo: " + file.getName());

		try {
			PrintWriter os = response.getWriter();
			os.write(contentToWrite);
			os.flush();
			os.close();
		} catch (IOException e) {
			String message = "No se pudo enviar al cliente el Stream de bytes del archivo: "
				+ file.getName()
				+ " ContentType: "
				+ file.getContentType();

			LOG.error(message, e);
			throw new LnwException(message, e );
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
