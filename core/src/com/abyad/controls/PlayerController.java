package com.abyad.controls;

import java.util.LinkedHashMap;

public abstract class PlayerController {
	private LinkedHashMap<String, KeyControl> keyMap;
	
	public abstract float upPressed();
	public abstract float downPressed();
	public abstract float leftPressed();
	public abstract float rightPressed();
	
	public abstract boolean attackPressed();
	public abstract boolean specialPressed();
}
