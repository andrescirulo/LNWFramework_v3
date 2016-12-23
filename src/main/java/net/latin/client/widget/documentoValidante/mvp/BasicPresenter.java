package net.latin.client.widget.documentoValidante.mvp;


public abstract class BasicPresenter<D extends Display> implements Presenter {

    /**
     * The display for the presenter.
     */
    protected final D display;
	
    public BasicPresenter(D display) {
    	this.display = display;
	}
    
	public D getDisplay() {
		return display;
	}

}
