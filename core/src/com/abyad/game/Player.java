package com.abyad.game;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.controls.GamepadController;
import com.abyad.controls.KeyboardController;
import com.abyad.controls.PlayerController;

public class Player {

	private PlayerController controller;			//The controller the player is using
	private PlayerCharacter character;				//The player character that this player controls
	private int number;								//The player number
	
	public Player(int num, AbyssAdventureGame game) {
		number = num;
		try {
<<<<<<< HEAD
			switch (game.controllerType)
			{
				case 0:
					controller = new KeyboardController();
					break;
				case 1:
					controller = new GamepadController();
					break;
				default:
					controller = new KeyboardController();
					break;
			}
=======
			controller = new GamepadController();		//This tries to make the player have the gamepad controller
>>>>>>> branch 'master' of https://github.com/The-Guild-of-Canada/AbyssAdventure
		} catch (Exception e) {
			
		}
		character = new PlayerCharacter(this);
	}
	
	public PlayerController getController() {
		return controller;
	}
	
	public PlayerCharacter getCharacter() {
		return character;
	}
	
	public int getNumber() {
		return number;
	}
}
