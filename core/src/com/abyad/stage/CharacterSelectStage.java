package com.abyad.stage;

import java.util.ArrayList;

import com.abyad.actor.cosmetic.DisplayText;
import com.abyad.actor.ui.CharacterSelectMenu;
import com.abyad.game.AbyssAdventureGame;
import com.abyad.game.Player;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class CharacterSelectStage extends Stage{

	private AbyssAdventureGame game;
	private ArrayList<CharacterSelectMenu> menus;
	
	public CharacterSelectStage(AbyssAdventureGame game) {
		super(new ExtendViewport(800, 450));		//Creates the stage with a viewport
		this.game = game;
		menus = new ArrayList<CharacterSelectMenu>();
		
		for (Player player : game.getPlayers()) {
			CharacterSelectMenu menu = new CharacterSelectMenu(player);
			addActor(menu);
			menus.add(menu);
		}
		
		DisplayText title = new DisplayText("Character Select");
		title.setScale(1.1f);
		title.setPosition(getCamera().frustum.planePoints[2].x / 2, getCamera().frustum.planePoints[2].y);
		addActor(title);
	}
	
	public boolean allIsReady() {
		for (CharacterSelectMenu menu : menus) {
			if (!menu.isReady()) return false;
		}
		return true;
	}
	
	public void resetStatus() {
		for (CharacterSelectMenu menu : menus) {
			menu.resetStatus();
		}
	}
}
