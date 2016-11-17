package net.latin.server.fileUpload;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.latin.server.persistence.UserContext;
import net.latin.server.security.config.LnwGeneralConfig;

/**
 * Servlet que sirve para usar con la clase GwtUploader, recibe los
 * archivos y los guarda en un mapa
 * 
 */
public class FileUploaderServlet extends HttpServlet {
	
	private static final long serialVersionUID = 4502074553834218029L;
	private static File tempDir;
	private static int fileSizeMaxInMemory;
	private static int fileSizeMax;
	private static final Log LOG = LogFactory.getLog(FileUploaderServlet.class);
//	private final static String FILE_UPLOADED_SESSION_KEY = "__LNW_MULTIPLE_FILE_KEY";
	
	@Override
	public void init() throws ServletException {
		fileSizeMaxInMemory = LnwGeneralConfig.getInstance().getFileSizeInMemoryMaxUploadServlet();
		
		String tempDirUploadServlet = LnwGeneralConfig.getInstance().getTempDirUploadServlet();
		if (tempDirUploadServlet != null) {
			tempDir = new File(tempDirUploadServlet);
		}
		
		fileSizeMax = LnwGeneralConfig.getInstance().getFileSizeMaxUploadServlet();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processRequest(req,resp);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}
	
	public boolean processRequest(HttpServletRequest request, HttpServletResponse resp) {
		try {

			ServletFileUpload upload = createServletFileUpload();
			List files = upload.parseRequest(request);
			if (files.size()>0){
				saveFile((FileItem) files.get(0), request,resp);
			}
		} catch (Exception e) {
			if (e.getCause()!=null && e.getCause().toString().contains("Stream ended unexpectedly")) {
				LOG.debug("El usuario canceló la subida", e);
			} else {
				LOG.error("Error al subir un archivo", e);
			}
			return false;
		}
		return true;
	}
	
	private ServletFileUpload createServletFileUpload() {
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(fileSizeMaxInMemory);
		if (tempDir != null) {
			factory.setRepository(tempDir);
		}
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setSizeMax(fileSizeMax);
		return upload;
	}
	
	protected synchronized static void saveFile(FileItem f, HttpServletRequest request, HttpServletResponse resp) {
		List<FileUploaded> existingFiles;
		String id=request.getHeader("File-Id");
		FileItem file = f;
		
		String sessionId=request.getSession().getId();
		if (!FileContainer.containsKey(sessionId)) {
			existingFiles = new ArrayList<FileUploaded>();
		} else {
			existingFiles = FileContainer.get(sessionId);
		}
		
		FileUploaded fileUploaded = new FileUploaded();
		fileUploaded.setId(id);
		fileUploaded.setFile(file);
		removeFileWithKey(existingFiles, id);
		existingFiles.add(fileUploaded);
		
		FileContainer.put(sessionId, existingFiles);
	}
	
	private static void removeFileWithKey(List<FileUploaded> existingFiles, String componentKey) {
		FileUploaded fileToRemove = null;
		for (FileUploaded file : existingFiles) {
			if (file.getId().equals(componentKey)) {
				fileToRemove = file;
				break;
			}
		}
		if (fileToRemove != null) {
			existingFiles.remove(fileToRemove);
		}
	}
	
	public static List<FileUploaded> getFilesUploaded() {
		return FileContainer.remove(UserContext.getInstance().getRequest().getSession().getId());
	}
	
	public static List<FileUploaded> getFilesUploadedWithoutRemove() {
		return FileContainer.get(UserContext.getInstance().getRequest().getSession().getId());
	}
	
	public static List<FileUploaded> getFilesUploadedWithoutRemove(String session) {
		return FileContainer.get(session);
	}
	
	public static void removeFilesUploaded() {
		FileContainer.remove(UserContext.getInstance().getRequest().getSession().getId());
	}
	
}
