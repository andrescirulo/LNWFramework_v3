package net.latin.server.fileUpload;

import java.util.Map;

import net.latin.server.utils.collections.CollectionFactory;

public class ContentTypes {
	
	private static final String DEFAULT_CONTENT_TYPE = "";
	private static Map<String, String> contentTypes = CollectionFactory.createMap();
	
	static{
		fill();
	}
	
	protected ContentTypes() {
	}
	
	
	private static void fill() {
		//TODO SACARLO DE UN XML
		contentTypes.put("txt", "text/plain");
		contentTypes.put("pdf", "application/pdf");
		contentTypes.put("doc", "application/msword");
		contentTypes.put("rtf", "application/rtf");
		contentTypes.put("xls", "application/vnd.ms-excel");
		
		contentTypes.put("tif", "image/tiff");
		contentTypes.put("jpg", "image/jpeg");
	}
	public static String getContentType(String ext){
		if(ext!=null && contentTypes.containsKey(ext.toLowerCase())){
			return contentTypes.get(ext.toLowerCase());
		}
		return DEFAULT_CONTENT_TYPE;
	}
}
