package com.abyad.controls;

import java.util.LinkedHashMap;

import com.badlogic.gdx.Input;

public class ControlConstants {
	public static final LinkedHashMap<String, KeyControl> DEFAULT_KEYBOARD_CONTROLS = new LinkedHashMap<String, KeyControl>();
	public static final LinkedHashMap<String, KeyControl> DEFAULT_GAMEPAD_CONTROLS = new LinkedHashMap<String, KeyControl>();
	public static LinkedHashMap<Integer, PlayerController> playerControls = new LinkedHashMap<Integer, PlayerController>();
	
	static {
		DEFAULT_KEYBOARD_CONTROLS.put("UP", new KeyControl(Input.Keys.UP,0));
		DEFAULT_KEYBOARD_CONTROLS.put("DOWN", new KeyControl(Input.Keys.DOWN,0));
		DEFAULT_KEYBOARD_CONTROLS.put("RIGHT", new KeyControl(Input.Keys.RIGHT,0));
		DEFAULT_KEYBOARD_CONTROLS.put("LEFT", new KeyControl(Input.Keys.LEFT,0));
		DEFAULT_KEYBOARD_CONTROLS.put("ATTACK", new KeyControl(Input.Keys.Z,0));
		DEFAULT_KEYBOARD_CONTROLS.put("SPECIAL", new KeyControl(Input.Keys.X,0));
		DEFAULT_KEYBOARD_CONTROLS.put("MAGIC", new KeyControl(Input.Keys.C, 0));
		DEFAULT_KEYBOARD_CONTROLS.put("ITEMS", new KeyControl(Input.Keys.V, 0));
		DEFAULT_KEYBOARD_CONTROLS.put("R_SWAP", new KeyControl(Input.Keys.A, 0));
		DEFAULT_KEYBOARD_CONTROLS.put("L_SWAP", new KeyControl(Input.Keys.D, 0));
		
		DEFAULT_GAMEPAD_CONTROLS.put("UP", new KeyControl(0, 1, -1));
		DEFAULT_GAMEPAD_CONTROLS.put("DOWN", new KeyControl(0, 1, 1));
		DEFAULT_GAMEPAD_CONTROLS.put("RIGHT", new KeyControl(1, 1, 1));
		DEFAULT_GAMEPAD_CONTROLS.put("LEFT", new KeyControl(1, 1, -1));
		DEFAULT_GAMEPAD_CONTROLS.put("ATTACK", new KeyControl(0, 0));
		DEFAULT_GAMEPAD_CONTROLS.put("SPECIAL", new KeyControl(2, 0));
		DEFAULT_GAMEPAD_CONTROLS.put("MAGIC", new KeyControl(3, 0));
		DEFAULT_GAMEPAD_CONTROLS.put("ITEMS", new KeyControl(1, 0));
		DEFAULT_GAMEPAD_CONTROLS.put("R_SWAP", new KeyControl(5, 0));
		DEFAULT_GAMEPAD_CONTROLS.put("L_SWAP", new KeyControl(4, 0));
	}
}
