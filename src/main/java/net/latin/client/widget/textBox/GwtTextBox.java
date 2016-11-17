package net.latin.client.widget.textBox;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.impl.TextBoxImpl;

import gwt.material.design.client.ui.MaterialTextBox;
import net.latin.client.widget.base.LnwWidget;

public class GwtTextBox extends MaterialTextBox implements LnwWidget {

	private static TextBoxImpl impl = GWT.create(TextBoxImpl.class);
	
	private List<GwtClipboardListener> clipboardListeners;
	
	public GwtTextBox() {
		clipboardListeners=new ArrayList<GwtClipboardListener>();
		registerClipboardEvents(this.getElement());
	}
	

	public void resetWidget() {
		this.setValue("");
	}

	public void setFocus() {
		setFocus(true);
	}

	//REGISTRO LOS EVENTOS DE CORTAR Y PEGAR
	private native void registerClipboardEvents(Element element)
    /*-{
        var that = this;
        element.oncut = function()
        {
            that.@net.latin.client.widget.textBox.GwtTextBox::onCut(Lcom/google/gwt/user/client/Event;)(event);
        };
        element.onpaste = function()
        {
            that.@net.latin.client.widget.textBox.GwtTextBox::onPaste(Lcom/google/gwt/user/client/Event;)(event);
        };
    }-*/;
    
    private void onCut(Event event)
    {
    	Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				for (GwtClipboardListener listener:clipboardListeners){
		    		listener.onCut();
		    	}
			}
		});
    	
    } 
    private void onPaste(Event event)
    {
    	Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				for (GwtClipboardListener listener:clipboardListeners){
					listener.onPaste();
		    	}
			}
		});
    } 

    public void addClipboardListener(GwtClipboardListener listener){
    	clipboardListeners.add(listener);
    }
    
    
    /**
     * Gets the text currently selected within this text box.
     * 
     * @return the selected text, or an empty string if none is selected
     */
    public String getSelectedText() {
      int start = getCursorPos();
      if (start < 0) {
        return "";
      }
      int length = getSelectionLength();
      return getValue().substring(start, start + length);
    }

    /**
     * Gets the length of the current text selection.
     * 
     * @return the text selection length
     */
    public int getSelectionLength() {
      return impl.getSelectionLength(getElement());
    }

    /**
     * Gets the current position of the cursor (this also serves as the beginning
     * of the text selection).
     * 
     * @return the cursor's position
     */
    public int getCursorPos() {
      return impl.getCursorPos(getElement());
    }
}
