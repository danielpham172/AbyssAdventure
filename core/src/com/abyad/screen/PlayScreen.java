package com.abyad.screen;

import com.abyad.actor.entity.AbstractEntity;
import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.game.AbyssAdventureGame;
import com.abyad.game.Player;
import com.abyad.stage.CharacterSelectStage;
import com.abyad.stage.PlayHUD;
import com.abyad.stage.PlayStage;
import com.abyad.stage.TownStage;
import com.abyad.stage.WeaponSelectStage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

public class PlayScreen implements Screen{

	private AbyssAdventureGame game;					//The game object
	private PlayStage playStage;						//The level stage (where the map and characters are at)
	private PlayHUD playHUD;
	private int floor;
	
	private CharacterSelectStage characterSelectStage;
	private WeaponSelectStage weaponSelectStage;
	private boolean noPlayerHUD;
	
	public PlayScreen(AbyssAdventureGame game) {
		this.game = game;
		//playStage = new PlayStage(game);		//Creates a play stage
		playStage = new TownStage(game);
		playHUD = new PlayHUD(game);
		characterSelectStage = new CharacterSelectStage(game);
		weaponSelectStage = new WeaponSelectStage(game);
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
		if (playStage.isReadyForNextLevel()) {
			if (playStage instanceof TownStage) {
				playStage.dispose();
				floor = 1;
				for (Player player : game.getPlayers()) {
						player.getCharacter().setSpawnInLength(100);
					if (player.isActive()) {
						PlayerCharacter.getPlayers().add(player.getCharacter());
					}
				}
				PlayStage nextStage = new PlayStage(game);
				
				playStage = nextStage;
			}
			else {
				playStage.dispose();
				for (Player player : game.getPlayers()) {
					if (player.isActive()) {
						player.getCharacter().setSpawnInLength(100);
						PlayerCharacter.getPlayers().add(player.getCharacter());
					}
				}
				floor++;
				PlayStage nextStage = new PlayStage(game);
				
				playStage = nextStage;
			}
		}
		else {
			playStage.getViewport().apply();	//Applies the camera
			playStage.act();			//Calls the act method for the stage
			playStage.draw();			//Calls the drawing of the stage
			if (!noPlayerHUD) {
				playHUD.getViewport().apply();
				playHUD.act();
				playHUD.draw();
			}
			if (playStage instanceof TownStage) {
				for (Player player : game.getPlayers()) {
					if (player.isActive()) {
						player.getCharacter().modifyHP(1);
						player.getCharacter().addPartialMana(1);
					}
				}
				
				if (((TownStage)playStage).openCharacterMenu()){
					characterSelectStage.getViewport().apply();
					characterSelectStage.act();
					characterSelectStage.draw();
					noPlayerHUD = true;
					
					if (characterSelectStage.allIsReady()) {
						noPlayerHUD = false;
						((TownStage)playStage).flagCharacterMenu(false);
						for (Player player : game.getPlayers()) {
							if (player.isActive()) {
								PlayerCharacter.getPlayers().add(player.getCharacter());
								AbstractEntity.getEntities().add(player.getCharacter());
								player.getCharacter().markForRemoval(false);
								playStage.addActor(player.getCharacter());
							}
						}
						characterSelectStage.resetStatus();
					}
				}
				if (((TownStage)playStage).openWeaponMenu()){
					weaponSelectStage.getViewport().apply();
					weaponSelectStage.act();
					weaponSelectStage.draw();
					noPlayerHUD = true;
					
					if (weaponSelectStage.allIsReady()) {
						noPlayerHUD = false;
						((TownStage)playStage).flagWeaponMenu(false);
						for (Player player : game.getPlayers()) {
							if (player.isActive()) {
								PlayerCharacter.getPlayers().add(player.getCharacter());
								AbstractEntity.getEntities().add(player.getCharacter());
								player.getCharacter().markForRemoval(false);
								playStage.addActor(player.getCharacter());
							}
						}
						weaponSelectStage.resetStatus();
					}
				}
			}
			else if (playersAreDead()) {
				playStage.dispose();
				for (Player player : game.getPlayers()) {
					if (player.isActive()) {
						player.getCharacter().setSpawnInLength(100);
						player.getCharacter().resetCharacter();
						PlayerCharacter.getPlayers().add(player.getCharacter());
					}
				}
				
				playStage = new TownStage(game);
			}
		}
	}
	
	public boolean playersAreDead() {
		for (Player player : game.getPlayers()) {
			if (!player.getCharacter().isDead() && player.isActive()) return false;
		}
		return true;
	}

	@Override
	public void resize(int width, int height) {
		playStage.getViewport().update(width, height);		//Changes the viewport to match the new size
		playHUD.getViewport().update(width, height);
		characterSelectStage.getViewport().update(width, height);
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
