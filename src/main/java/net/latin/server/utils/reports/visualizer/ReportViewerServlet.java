package net.latin.server.utils.reports.visualizer;

import javax.servlet.http.HttpServletRequest;

import net.latin.server.persistence.UserContext;
import net.latin.server.utils.fileTypes.FileToShowOnClient;

/**
 * Servlet que interactura con GwtReportViewer para sacar del contexto
 * un pdf y mostrarlo en un iframe
 *
 * @author Matias Leone
 */
public class ReportViewerServlet extends FileVisualizer {

	public final static String REPORT_SESSION_KEY = "__LNW_REPORT_VISUALIZER_FILE_KEY";

	/**
	 * Carga un archivo en la session para ser utilizado luego
	 * @param file
	 */
	public static void setFileToShow(FileToShowOnClient file) {
		UserContext.getInstance().getRequest().getSession().setAttribute( REPORT_SESSION_KEY, file );
	}


	@Override
	protected FileToShowOnClient getFileToShow(HttpServletRequest request) {
		return (FileToShowOnClient) request.getSession().getAttribute( REPORT_SESSION_KEY );
	}

}
