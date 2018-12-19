package com.abyad.game;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.controls.GamepadController;
import com.abyad.controls.PlayerController;

public class Player {

	private PlayerController controller;			//The controller the player is using
	private PlayerCharacter character;				//The player character that this player controls
	private int number;								//The player number
	
	public Player(int num) {
		number = num;
		try {
			controller = new GamepadController();		//This tries to make the player have the gamepad controller
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
