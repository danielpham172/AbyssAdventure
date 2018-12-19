package com.abyad.controls;

public class KeyControl {
	private int keyNumber;
	private int type; //0 = key/gamepad button, 1 = axis, 2 = POV
	private int direction; //for gamepad axis
	
	public KeyControl(int keyNumber, int type) {
		this.keyNumber = keyNumber;
		this.type = type;
		direction = 0;
	}
	
	public KeyControl(int keyNumber, int type, int direction) {
		this.keyNumber = keyNumber;
		this.type = type;
		this.direction = direction;
	}
	
	public int getKeyCode() {
		return keyNumber;
	}
	
	public int getKeyType() {
		return type;
	}
	
	public int getDirection() {
		return direction;
	}
}
