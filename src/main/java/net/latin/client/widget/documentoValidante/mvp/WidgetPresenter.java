package net.latin.client.widget.documentoValidante.mvp;


public abstract class WidgetPresenter<D extends WidgetDisplay> extends BasicPresenter<D> {
    public WidgetPresenter( D display ) {
        super( display );
    }
}
