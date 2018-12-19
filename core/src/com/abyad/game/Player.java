package com.abyad.game;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.controls.GamepadController;
import com.abyad.controls.KeyboardController;
import com.abyad.controls.PlayerController;

public class Player {

	private PlayerController controller;
	private PlayerCharacter character;
	private int number;
	
	public Player(int num, AbyssAdventureGame game) {
		number = num;
		try {
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
