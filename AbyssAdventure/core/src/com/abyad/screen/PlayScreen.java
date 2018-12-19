package com.abyad.screen;

import com.abyad.game.AbyssAdventureGame;
import com.abyad.stage.PlayStage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

public class PlayScreen implements Screen{

	private AbyssAdventureGame game;
	private PlayStage playStage;
	
	public PlayScreen(AbyssAdventureGame game) {
		this.game = game;
		playStage = new PlayStage(game);
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
		Gdx.gl.glClearColor(0f, 0f, 0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
		playStage.getViewport().apply();
		playStage.act();
		playStage.draw();
		
	}

	@Override
	public void resize(int width, int height) {
		playStage.getViewport().update(width, height);
		
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
