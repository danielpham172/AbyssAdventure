package com.abyad.controls;

import java.util.LinkedHashMap;

import com.badlogic.gdx.Input;

public class ControlConstants {
	public static final LinkedHashMap<String, KeyControl> DEFAULT_KEYBOARD_CONTROLS = new LinkedHashMap<String, KeyControl>();
	public static final LinkedHashMap<String, KeyControl> DEFAULT_GAMEPAD_CONTROLS = new LinkedHashMap<String, KeyControl>();
	public static LinkedHashMap<Integer, PlayerController> playerControls = new LinkedHashMap<Integer, PlayerController>();
	
	static {
		DEFAULT_KEYBOARD_CONTROLS.put("UP", new KeyControl(Input.Keys.W,0));
		DEFAULT_KEYBOARD_CONTROLS.put("DOWN", new KeyControl(Input.Keys.S,0));
		DEFAULT_KEYBOARD_CONTROLS.put("RIGHT", new KeyControl(Input.Keys.D,0));
		DEFAULT_KEYBOARD_CONTROLS.put("LEFT", new KeyControl(Input.Keys.A,0));
		DEFAULT_KEYBOARD_CONTROLS.put("ATTACK", new KeyControl(Input.Keys.Z,0));
		
		DEFAULT_GAMEPAD_CONTROLS.put("UP", new KeyControl(0, 1, -1));
		DEFAULT_GAMEPAD_CONTROLS.put("DOWN", new KeyControl(0, 1, 1));
		DEFAULT_GAMEPAD_CONTROLS.put("RIGHT", new KeyControl(1, 1, 1));
		DEFAULT_GAMEPAD_CONTROLS.put("LEFT", new KeyControl(1, 1, -1));
		DEFAULT_GAMEPAD_CONTROLS.put("ATTACK", new KeyControl(0, 0));
	}
}
