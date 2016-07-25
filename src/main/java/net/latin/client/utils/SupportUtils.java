package net.latin.client.utils;

public class SupportUtils {

	public static native boolean supportsAudio()/*-{
		return $wnd.Modernizr.audio;
	}-*/;
	
	public static native boolean supportsBorderRadius()/*-{
		return $wnd.Modernizr.borderradius;
	}-*/;
	
	public static native boolean supportsBoxShadow()/*-{
		return $wnd.Modernizr.boxshadow;
	}-*/;
	
	public static native boolean supportsCssAnimation()/*-{
		return $wnd.Modernizr.cssanimations;
	}-*/;
	
	public static native boolean supportsCssGradients()/*-{
		return $wnd.Modernizr.cssgradients;
	}-*/;
	
	public static native boolean supportsFontFace()/*-{
		return $wnd.Modernizr.fontface;
	}-*/;
	
	public static native boolean supportsFormData()/*-{
		return (!!$wnd.ProgressEvent && !!$wnd.FormData);
	}-*/;
	
	public static native boolean supportsOpacity()/*-{
		return $wnd.Modernizr.opacity;
	}-*/;
	
	public static native boolean supportsVideo()/*-{
		return $wnd.Modernizr.video;
	}-*/;
}
