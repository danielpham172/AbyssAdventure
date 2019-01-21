package com.abyad.stage;

import com.abyad.actor.cosmetic.DisplayText;
import com.abyad.game.AbyssAdventureGame;
import com.abyad.game.Player;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class PlayHUD extends Stage{

	private AbyssAdventureGame game;
	private DisplayText title;
	public PlayHUD(AbyssAdventureGame game) {
		super(new ExtendViewport(800, 450));		//Creates the stage with a viewport
		this.game = game;
		
		for (Player player : game.getPlayers()) {
			addActor(player.getDisplay());
			//player.getDisplay().setPosition(10, 10);
		}
		
		title = new DisplayText("Town");
		title.setScale(0.8f);
		title.setPosition(getCamera().frustum.planePoints[2].x / 2, getCamera().frustum.planePoints[2].y);
		addActor(title);
	}
	
	public void setTitle(String text) {
		title.setText(text);
	}
}
