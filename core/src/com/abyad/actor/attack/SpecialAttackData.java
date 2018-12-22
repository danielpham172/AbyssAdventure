package com.abyad.actor.attack;

public abstract class SpecialAttackData extends AttackData{

	public int getRequiredMana() {
		return 0;
	}
	
	public boolean isHold() {
		return false;
	}
	
	public int getHoldLength() {
		return 0;
	}
}
