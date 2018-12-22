package com.abyad.controls;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.utils.Array;

public class GamepadController extends PlayerController{
	public int index;
	private LinkedHashMap<String, KeyControl> keyMap;
	private float nullZone = 0.30f;
	private static ArrayList<Integer> inUse = new ArrayList<Integer>();
	private static Array<Controller> controllers = Controllers.getControllers();

	public GamepadController() throws Exception {
		keyMap = new LinkedHashMap<String, KeyControl>();
		keyMap.putAll(ControlConstants.DEFAULT_GAMEPAD_CONTROLS);
		index = -1;
		for (int i = 0; i < controllers.size; i++) {
			boolean using = false;
			for (Integer integer : inUse) {
				if (integer.intValue() == i) {
					using = true;
					break;
				}
			}
			if (!using) {
				this.index = i;
				inUse.add(index);
				break;
			}
		}
		if (index == -1) throw new Exception("No more controllers");
	}

	@Override
	public float upPressed() {
		KeyControl up = keyMap.get("UP");
		return getPower(up);
	}

	@Override
	public float downPressed() {
		KeyControl down = keyMap.get("DOWN");
		return getPower(down);
	}

	@Override
	public float leftPressed() {
		KeyControl left = keyMap.get("LEFT");
		return getPower(left);
	}

	@Override
	public float rightPressed() {
		KeyControl right = keyMap.get("RIGHT");
		return getPower(right);
	}
	
	private float getPower(KeyControl key) {
		if (key.getKeyType() == 0) {
			return (controllers.get(index).getButton(key.getKeyCode())) ? 1.0f : 0;
		}
		else if (key.getKeyType() == 1) {
			return getAxisAmount(key.getKeyCode(), key.getDirection());
		}
		else {
			return 0;
		}
	}
	private float getAxisAmount(int axis, int direction) {
		float amount = controllers.get(index).getAxis(axis);
		return Math.max((Math.abs(amount) > nullZone) ? amount * direction : 0, 0);
	}

	@Override
	public boolean attackPressed() {
		KeyControl attack = keyMap.get("ATTACK");
		return getPressed(attack);
	}
	
	@Override
	public boolean specialPressed() {
		KeyControl attack = keyMap.get("SPECIAL");
		return getPressed(attack);
	}
	
	private boolean getPressed(KeyControl key) {
		if (key.getKeyType() == 0) {
			return (controllers.get(index).getButton(key.getKeyCode()));
		}
		else if (key.getKeyType() == 1) {
			return controllers.get(index).getAxis(key.getKeyCode()) * key.getDirection() > nullZone;
		}
		else {
			return false;
		}
	}
}
