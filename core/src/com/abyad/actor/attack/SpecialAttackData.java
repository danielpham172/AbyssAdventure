package com.abyad.actor.attack;

public abstract class SpecialAttackData extends AttackData{

	public SpecialAttackData(int[] attackLengths) {
		super(attackLengths);
	}

	public int getRequiredMana() {
		return 0;
	}
	
	public int getRequiredPartialMana() {
		return 0;
	}
	
	public boolean isHold() {
		return false;
	}
	
	public int getHoldLength() {
		return 0;
	}
	public String getDesc() {
		return "";
	}
}
