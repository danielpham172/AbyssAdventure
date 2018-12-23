package com.abyad.stage;

import com.abyad.game.AbyssAdventureGame;
import com.abyad.game.Player;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class PlayHUD extends Stage{

	private AbyssAdventureGame game;
	public PlayHUD(AbyssAdventureGame game) {
		super(new ExtendViewport(800, 450));		//Creates the stage with a viewport
		this.game = game;
		
		for (Player player : game.getPlayers()) {
			addActor(player.getDisplay());
			//player.getDisplay().setPosition(10, 10);
		}
	}
}
