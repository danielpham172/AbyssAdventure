package com.abyad.game;

import java.util.ArrayList;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.actor.ui.InventoryRingMenu;
import com.abyad.actor.ui.MagicRingMenu;
import com.abyad.actor.ui.PlayerDisplay;
import com.abyad.controls.DisabledController;
import com.abyad.controls.GamepadController;
import com.abyad.controls.KeyboardController;
import com.abyad.controls.PlayerController;
import com.abyad.magic.AbstractMagic;

public class Player {

	public static ArrayList<String> characterNames = new ArrayList<String>();
	private PlayerController controller;			//The controller the player is using
	private PlayerCharacter character;				//The player character that this player controls
	private PlayerDisplay display;
	private MagicRingMenu magicMenu;
	private InventoryRingMenu inventoryMenu;
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
		selectedName = (num - 1) % characterNames.size();
		character = new PlayerCharacter(this, 0, 0);
		display = new PlayerDisplay(this);
		magicMenu = new MagicRingMenu(this);
		inventoryMenu = new InventoryRingMenu(this);
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
	
	/**
	 * --MAGIC MENU METHODS---
	 */
	public void openMagicMenu() {
		if (magicMenu.getStage() == null) {
			magicMenu.beginExpanding();
			character.getStage().addActor(magicMenu);
		}
	}

	public void closeMagicMenu() {
		if (magicMenu.getStage() != null) {
			magicMenu.closeMenu();
		}
	}
	
	public void toggleMagicMenu() {
		if (magicMenu.getStage() == null) {
			openMagicMenu();
		}
		else {
			closeMagicMenu();
		}
	}
	
	public boolean isMagicMenuActive() {
		return magicMenu.isMenuActive();
	}

	public void rotateMagicMenu(int direction) {
		magicMenu.rotate(direction);
	}
	
	public void resetMagicMenu() {
		magicMenu.setSelection(0);
	}

	public int getSelectedMagic() {
		return magicMenu.getSelection();
	}
	
	/**
	 * --INVENTORY MENU METHODS--
	 */
	public void openInventoryMenu() {
		if (inventoryMenu.getStage() == null) {
			inventoryMenu.beginExpanding();
			character.getStage().addActor(inventoryMenu);
		}
	}

	public void closeInventoryMenu() {
		if (inventoryMenu.getStage() != null) {
			inventoryMenu.closeMenu();
		}
	}
	
	public void toggleInventoryMenu() {
		if (inventoryMenu.getStage() == null) {
			openInventoryMenu();
		}
		else {
			closeInventoryMenu();
		}
	}
	
	public boolean isInventoryMenuActive() {
		return inventoryMenu.isMenuActive();
	}

	public void rotateInventoryMenu(int direction) {
		inventoryMenu.rotate(direction);
	}
	
	public void resetInventoryMenu() {
		inventoryMenu.setSelection(0);
	}

	public int getSelectedItem() {
		return inventoryMenu.getSelection();
	}
	
	public boolean anyMenuActive() {
		return isInventoryMenuActive() || isMagicMenuActive();
	}
	
	
	/**
	 * --CUSTOMIZATION METHODS--
	 */
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

	public void changeStartingSpell(AbstractMagic magic) {
		character.changeStartingSpell(magic);
		resetMagicMenu();
	}
	
	public AbstractMagic getStartingSpell(AbstractMagic magic) {
		return character.getStartingSpell();
	}
}
