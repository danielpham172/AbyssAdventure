package com.abyad.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.abyad.game.AbyssAdventureGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.title = "Abyss Adventure";
		//config.fullscreen = true;
		config.width = 1920;
		config.height = 1080;
		//config.resizable = false;	// cannot resize window manually
		config.vSyncEnabled = true; // vertical sync is true
		config.foregroundFPS = 60; // setting to 0 disables foreground fps throttling
		config.backgroundFPS = 60; // setting to 0 disables foreground fps throttling
		
		new LwjglApplication(new AbyssAdventureGame(), config);
	}
}
