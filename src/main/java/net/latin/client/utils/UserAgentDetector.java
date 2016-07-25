package net.latin.client.utils;

public class UserAgentDetector {

	public static native String getUserAgent() /*-{
		return navigator.userAgent.toLowerCase();
	}-*/;

	public static Boolean isMSIE() {
		return getUserAgent().contains("msie");
	};
	
	public static Boolean isMSIE(int version) {
		return getUserAgent().contains("msie " + version);
	};

	public static Boolean isChrome() {
		return getUserAgent().contains("chrome");
	};

	public static Boolean isFX() {
		return getUserAgent().contains("firefox");
	};

	public static Boolean isSafari() {
		return getUserAgent().contains("AppleWebKit");
	};

	public static Boolean isOpera() {
		return getUserAgent().contains("opera");
	};

}
