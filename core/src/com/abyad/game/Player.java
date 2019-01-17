package com.abyad.game;

import java.util.ArrayList;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.actor.ui.MagicRingMenu;
import com.abyad.actor.ui.PlayerDisplay;
import com.abyad.controls.DisabledController;
import com.abyad.controls.GamepadController;
import com.abyad.controls.KeyboardController;
import com.abyad.controls.PlayerController;

public class Player {

	public static ArrayList<String> characterNames = new ArrayList<String>();
	private PlayerController controller;			//The controller the player is using
	private PlayerCharacter character;				//The player character that this player controls
	private PlayerDisplay display;
	private MagicRingMenu ringMenu;
	private int number;								//The player number
	private int selectedName;
	private boolean isActive;
	
	private static boolean haveKeyboardController;
	
	public Player(int num, AbyssAdventureGame game) {
		number = num;
		try {
			controller = new GamepadController();	//This tries to make the player have the gamepad controller
		} catch (Exception e) {
			if (!haveKeyboardController) {
				controller = new KeyboardController();
				haveKeyboardController = true;
			}
			else {
				controller = new DisabledController();
			}
		}
		if (num == 1) isActive = true;
		selectedName = num % characterNames.size();
		character = new PlayerCharacter(this, 0, 0);
		display = new PlayerDisplay(this);
		ringMenu = new MagicRingMenu(this);
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public void setActive(boolean active) {
		isActive = active;
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
	
	public String getCharacterName() {
		return characterNames.get(selectedName);
	}
	
	public int getNumber() {
		return number;
	}
	
	public void toggleRingMenu() {
		if (ringMenu.getStage() == null) {
			ringMenu.beginExpanding();
			character.getStage().addActor(ringMenu);
		}
		else {
			ringMenu.closeMenu();
		}
	}
	
	public boolean isRingMenuActive() {
		return ringMenu.isMenuActive();
	}

	public void rotateRingMenu(int direction) {
		ringMenu.rotate(direction);
	}

	public int getSelectedMagic() {
		return ringMenu.getSelection();
	}
	
	public void changeSelectedCharacter(int newSelectedName) {
		selectedName = newSelectedName;
		character.updateSpriteSheet();
	}
	
	public void changeSelectedWeapon(String newWeapon) {
		character.changeWeapon(newWeapon);
	}
	
	public String getWeapon() {
		return character.getWeapon();
	}
	
	public void changeSelectedSpecial(String newSpecial) {
		character.changeSpecial(newSpecial);
	}
	
	public String getSpecial() {
		return character.getSpecial();
	}
}
