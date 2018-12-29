package com.abyad.game;

import java.util.ArrayList;
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
	private ArrayList<Player> players = new ArrayList<Player>();
	public static final int controllerType = 1;	//0 for keyboard, 1 for controller, ...
	
	@Override
	public void create() {
		Assets.loadAssets();
		
		//Generate Players
		Player player1 = new Player(1, this);
		players.add(player1);
		//Player player2 = new Player(2, this);
		//players.add(player2);
		
		PlayScreen playScreen = new PlayScreen(this);
		screens.put("Play", playScreen);
		setScreen(playScreen);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}
	
	public ArrayList<Player> getPlayers(){
		return players;
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
