package net.latin.client.utils.audio;

import net.latin.client.utils.SupportUtils;

public class AudioManager {
	private AudioStrategy strategy;
	
	public AudioManager() {
		if (SupportUtils.supportsAudio()){
			strategy=new HTML5Audio();
		}
//		else{
//			strategy=new FlashAudio();
//		}
	}
	
	public void play(String url){
		strategy.play(url);
	}
	
	public boolean isPlaying(){
		return strategy.isPlaying();
	}
}
