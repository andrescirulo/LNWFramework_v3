package net.latin.client.utils.audio;

import com.google.gwt.event.dom.client.EndedEvent;
import com.google.gwt.event.dom.client.EndedHandler;
import com.google.gwt.media.client.Audio;
import com.google.gwt.user.client.ui.RootPanel;

public class HTML5Audio implements AudioStrategy {

	private boolean estaReproduciendo;

	public HTML5Audio() {
	}
	
	public void play(String url) {
		final Audio audio=Audio.createIfSupported();
		audio.addEndedHandler(new EndedHandler() {
			public void onEnded(EndedEvent event) {
				estaReproduciendo = false;
				audio.removeFromParent();
			}
		});
		audio.setSrc(url);
		RootPanel.get().add(audio);
		estaReproduciendo = true;
		audio.play();
	}

	public boolean isPlaying() {
		return estaReproduciendo;
	}

}
