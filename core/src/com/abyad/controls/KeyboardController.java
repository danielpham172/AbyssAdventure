package com.abyad.controls;

import java.util.LinkedHashMap;

import com.badlogic.gdx.Gdx;

public class KeyboardController extends PlayerController{
	private LinkedHashMap<String, KeyControl> keyMap;
	
	public KeyboardController()	{
		keyMap = new LinkedHashMap<String, KeyControl>();
		keyMap.putAll(ControlConstants.DEFAULT_KEYBOARD_CONTROLS);
	}
	
	@Override
	public float upPressed() {
		return keyPressed("UP");
	}

	@Override
	public float downPressed() {
		return keyPressed("DOWN");
	}

	@Override
	public float leftPressed() {
		return keyPressed("LEFT");
	}

	@Override
	public float rightPressed() {
		return keyPressed("RIGHT");
	}

	@Override
	public boolean attackPressed() {
		return keyPressedBool("ATTACK");
	}
	
	@Override
	public boolean specialPressed() {
		return keyPressedBool("SPECIAL");
	}
	
	@Override
	public boolean magicPressed() {
		return keyPressedBool("MAGIC");
	}
	
	@Override
	public boolean itemsPressed() {
		return keyPressedBool("ITEMS");
	}

	@Override
	public boolean rightSwapPressed() {
		return keyPressedBool("R_SWAP");
	}

	@Override
	public boolean leftSwapPressed() {
		return keyPressedBool("L_SWAP");
	}
	
	private float keyPressed(String k)	{
		KeyControl key = keyMap.get(k);
		return (Gdx.input.isKeyPressed(key.getKeyCode())) ? 1.0f : 0;
	}
	
	private boolean keyPressedBool(String k)	{
		KeyControl key = keyMap.get(k);
		return Gdx.input.isKeyPressed(key.getKeyCode());
	}
}
