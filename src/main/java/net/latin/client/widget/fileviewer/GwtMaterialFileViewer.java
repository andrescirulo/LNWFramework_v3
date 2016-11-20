package net.latin.client.widget.fileviewer;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Frame;

import net.latin.client.widget.base.GwtController;
import net.latin.client.widget.window.GwtMaterialWindow;

public class GwtMaterialFileViewer extends GwtMaterialWindow {

	private String url="__fileVisualizer";
	private Frame frame;
	
	public GwtMaterialFileViewer() {
		this.getContent().getElement().getStyle().setOverflowX(Overflow.HIDDEN);
		this.getContent().getElement().getStyle().setOverflowY(Overflow.AUTO);
		frame = new Frame();
		this.add(frame);
		setNotMaximize();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public void open() {
		super.open();
		
		frame.setUrl("");
		frame.setUrl(GwtController.instance.getBasePath() + url);
		ajustarSize();
	}
	
	@Override
	protected void onWindowResize() {
		ajustarSize();
	}
	
	protected void ajustarSize() {
		double w = (double) this.getContent().getOffsetWidth();
		double h = (Window.getClientHeight()*0.8)-this.getToolbar().getOffsetHeight();
		
		frame.setWidth(w + "px");
		frame.setHeight(h + "px");
	}
	
}
