package net.latin.server.fileUpload;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.latin.server.persistence.UserContext;


public class FileContainer {

	private static Map<String, List<FileUploaded>> filecontainer = new HashMap<String, List<FileUploaded>>();

	public static List<FileUploaded> get(String key) {
		return filecontainer.get(key);
	}

	public static boolean containsKey(String key) {
		return filecontainer.containsKey(key);
	}

	public static List<FileUploaded> put(String key, List<FileUploaded> value) {
		return filecontainer.put(key, value);
	}

	public static List<FileUploaded> remove(String key) {
		return filecontainer.remove(key);
	}

	public static FileToShowOnClient getFileFromCurrentSession(String fileId) {
		if (FileContainer.containsKey(UserContext.getInstance().getRequest().getSession().getId())) {
			List<FileUploaded> files = FileContainer.get(UserContext.getInstance().getRequest().getSession()
					.getId());
			for (FileUploaded flashFileUploaded : files) {
				if (flashFileUploaded.getId().equals(fileId)) {
					FileToShowOnClient flashFileToShowOnClient = new FileToShowOnClient(flashFileUploaded
							.getFile().getName(), flashFileUploaded.getFile().get());
					return flashFileToShowOnClient;
				}
			}
		}
		return null;
	}

	public static List<FileUploaded> getAllFromCurrentSession() {
		return get(UserContext.getInstance().getRequest().getSession().getId());
	}
	
	/**
	 * Saca un archivo de la lista de archivos subidos en la session. Retorna el archivo (null en caso de no 
	 * encontrarlo)
	 * @param fileId
	 * @return
	 */
	public static FileUploaded removeFileFromCurrentSession(String fileId){
		if (FileContainer.containsKey(UserContext.getInstance().getRequest().getSession().getId())) {
			int i = 0;
			FileUploaded fileToRemove = null;
			List<FileUploaded> files = FileContainer.get(UserContext.getInstance().getRequest().getSession()
					.getId());
			for (FileUploaded flashFileUploaded : files) {
				if (flashFileUploaded.getId().equals(fileId)) {
					fileToRemove = flashFileUploaded;
					break;
				} else {
					i++;
				}
			}
			
			if(fileToRemove != null){
				files.remove(i);
				return fileToRemove;
			} else {
				return null;
			}
			
		} else {
			return null;
		}
	}

}
