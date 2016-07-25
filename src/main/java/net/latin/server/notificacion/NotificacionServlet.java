package net.latin.server.notificacion;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NotificacionServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8406040570288239997L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if ("654321".equals(req.getParameter("update"))){
			GwtNotificationManager.updateMessages();
			Calendar ahora=Calendar.getInstance();
			String ahoraString=ahora.get(Calendar.HOUR) + ":" + ahora.get(Calendar.MINUTE) + ":" + ahora.get(Calendar.SECOND);
			resp.getWriter().write(ahoraString + " - Mensajes Actualizados");
		}
		//super.doGet(req, resp);
	}
}
