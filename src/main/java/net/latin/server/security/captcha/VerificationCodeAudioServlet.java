package net.latin.server.security.captcha;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class VerificationCodeAudioServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.setHeader("Pragma", "public");
    	response.setHeader("cache-control", "max-age=0,no-cache, no-store,must-revalidate, proxy-revalidate, s-maxage=0");
    	response.setDateHeader("Expires", 0);
    	response.setContentType("audio/mpeg3");

        HttpSession session = request.getSession(true);
        
        ByteArrayOutputStream baos=(ByteArrayOutputStream) session.getAttribute(VerificationCodeServlet.VERIFICATION_CODE_KEY + ".sound");
        if (baos!=null){
        	response.getOutputStream().write(baos.toByteArray());
        }
        response.flushBuffer();
        
	}
}
