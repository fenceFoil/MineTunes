package com.minetunes.speech;

import net.minecraft.src.Minecraft;

import com.minetunes.config.MinetunesConfig;
import com.sun.speech.freetts.VoiceManager;

public class Speech {
	private String name;
	private com.sun.speech.freetts.Voice systemVoice;

	public static Speech kevin = new Speech("kevin16", 100, 120);

	public Speech(String name, float pitch, float rate) {
		this.name = name;
		systemVoice = VoiceManager.getInstance().getVoice(this.name);
		systemVoice.allocate();
		systemVoice.setPitch(pitch);
		systemVoice.setRate(rate);
	}

	public void queue(final String thingToSay) {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				say(thingToSay);
			}

		});
		t.start();
	}

	public void say(String[] thingsToSay) {
		for (int i = 0; i < thingsToSay.length; i++) {
			say(thingsToSay[i]);
		}
	}

	public void say(String thingToSay) {
		if (!MinetunesConfig.getBoolean("speech.enabled")) {
			return;
		}
		
		try {
//			systemVoice
//					.setVolume(Minecraft.getMinecraft().gameSettings.soundVolume);
		} catch (Exception e) {
		}
		systemVoice.speak(thingToSay);
	}

	public void dispose() {
		systemVoice.deallocate();
	}
}