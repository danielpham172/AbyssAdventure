package com.abyad.screen;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.game.AbyssAdventureGame;
import com.abyad.game.Player;
import com.abyad.stage.PlayStage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

public class PlayScreen implements Screen{

	private AbyssAdventureGame game;					//The game object
	private PlayStage playStage;						//The level stage (where the map and characters are at)
	private int floor;
	
	public PlayScreen(AbyssAdventureGame game) {
		this.game = game;
		playStage = new PlayStage(game);		//Creates a play stage
		floor = 1;
	}
	
	public PlayStage getPlayStage() {
		return playStage;
	}
	
	/**
	 * Screen Methods
	 */
	
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		//Renders the stage
		Gdx.gl.glClearColor(0f, 0f, 0f, 1.0f);		//Clears the screen to black
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
		playStage.getViewport().apply();	//Applies the camera
		playStage.act();			//Calls the act method for the stage
		playStage.draw();			//Calls the drawing of the stage
		
		if (playStage.isReadyForNextLevel()) {
			playStage.dispose();
			for (Player player : game.getPlayers()) {
				player.getCharacter().setSpawnInLength(100);
				PlayerCharacter.getPlayers().add(player.getCharacter());
			}
			floor++;
			PlayStage nextStage = new PlayStage(game);
			
			playStage = nextStage;
		}
		
	}

	@Override
	public void resize(int width, int height) {
		playStage.getViewport().update(width, height);		//Changes the viewport to match the new size
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		playStage.dispose();
	}

}
