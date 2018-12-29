package com.abyad.game;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.actor.ui.MagicRingMenu;
import com.abyad.controls.GamepadController;
import com.abyad.controls.KeyboardController;
import com.abyad.controls.PlayerController;
import com.abyad.table.PlayerDisplay;

public class Player {

	private PlayerController controller;			//The controller the player is using
	private PlayerCharacter character;				//The player character that this player controls
	private PlayerDisplay display;
	private MagicRingMenu ringMenu;
	private int number;								//The player number
	
	public Player(int num, AbyssAdventureGame game) {
		number = num;
		try {
			switch (num)
			{
				case 2:
					controller = new KeyboardController();	//This tries to make the player have the keyboard controller
					break;
				case 1:
					controller = new GamepadController();	//This tries to make the player have the gamepad controller
					break;
				default:
					controller = new KeyboardController();
					break;
			}
		} catch (Exception e) {
			
		}
		character = new PlayerCharacter(this, 0, 0);
		display = new PlayerDisplay(this);
		ringMenu = new MagicRingMenu(this);
	}
	
	public PlayerController getController() {
		return controller;
	}
	
	public PlayerCharacter getCharacter() {
		return character;
	}
	
	public PlayerDisplay getDisplay() {
		return display;
	}
	
	public int getNumber() {
		return number;
	}
	
	public void toggleRingMenu() {
		if (ringMenu.getStage() == null) {
			character.getStage().addActor(ringMenu);
		}
		else {
			ringMenu.setToRemove();
		}
	}
	
	public boolean isRingMenuActive() {
		return (ringMenu.getStage() != null);
	}

	public void rotateRingMenu(int direction) {
		ringMenu.rotate(direction);
	}

	public int getSelectedMagic() {
		return ringMenu.getSelection();
	}
}
