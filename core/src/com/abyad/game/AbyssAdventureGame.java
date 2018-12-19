package com.abyad.game;

import java.util.HashMap;

import com.abyad.actor.entity.ZombieCharacter;
import com.abyad.screen.PlayScreen;
import com.abyad.utils.Assets;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;

public class AbyssAdventureGame extends Game {
	private InputMultiplexer inputMultiplexer = new InputMultiplexer();
	private HashMap<String, Screen> screens = new HashMap<String, Screen>();
	public static final int controllerType = 0;	//0 for keyboard, 1 for controller, ...
	
	@Override
	public void create() {
		Assets.loadAssets();
		PlayScreen playScreen = new PlayScreen(this);
		screens.put("Play", playScreen);
		Player player1 = new Player(1, this);
		//Player player2 = new Player(2, this);
		playScreen.getPlayStage().addActor(player1.getCharacter());
		//playScreen.getPlayStage().addActor(player2.getCharacter());
		//player2.getCharacter().setPosition(player1.getCharacter().getX(), player1.getCharacter().getY());
		for (int i = 0; i < 30; i++) {
			playScreen.getPlayStage().addActor(new ZombieCharacter());
		}
		setScreen(playScreen);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}
	
	public void addInput(InputProcessor input) {
		inputMultiplexer.addProcessor(input);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}
	
	public void removeInput(InputProcessor input) {
		inputMultiplexer.removeProcessor(input);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		screens.get("Play").dispose();
		Assets.dispose();
	}
}
