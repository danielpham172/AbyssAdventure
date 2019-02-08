package com.abyad.controls;

public class DisabledController extends PlayerController{

	@Override
	public float upPressed() {
		return 0;
	}

	@Override
	public float downPressed() {
		return 0;
	}

	@Override
	public float leftPressed() {
		return 0;
	}

	@Override
	public float rightPressed() {
		return 0;
	}

	@Override
	public boolean attackPressed() {
		return false;
	}

	@Override
	public boolean specialPressed() {
		return false;
	}

	@Override
	public boolean magicPressed() {
		return false;
	}
	
	@Override
	public boolean itemsPressed() {
		return false;
	}

	@Override
	public boolean rightSwapPressed() {
		return false;
	}

	@Override
	public boolean leftSwapPressed() {
		return false;
	}

}
