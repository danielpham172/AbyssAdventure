package com.abyad.actor.attack;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.game.Player;
import com.badlogic.gdx.math.Rectangle;

public abstract class AttackData {

	public static LinkedHashMap<String, AttackData> basicAttacks = new LinkedHashMap<String, AttackData>();
	public static LinkedHashMap<String, SpecialAttackData> specialAttacks = new LinkedHashMap<String, SpecialAttackData>();
	static {
		basicAttacks.put("SWORD", new BasicSwordAttack());
		basicAttacks.put("STAFF", new BasicStaffAttack());
		basicAttacks.put("SPEAR", new BasicSpearAttack());
		
		specialAttacks.put("SPIN_SLASH", new SpinSlash());
		specialAttacks.put("WIND_BLADE", new WindBlade());
		specialAttacks.put("MEDITATE", new Meditate());
		specialAttacks.put("PIERCE_CHARGE", new PierceCharge());
	}
	
	private int[] attackLengths;
	
	public AttackData(int[] attackLengths) {
		this.attackLengths = attackLengths;
	}
	public abstract void initiateAttack(PlayerCharacter player);
	public abstract void useAttack(PlayerCharacter player, int framesSinceLast);
	public abstract ArrayList<Rectangle> getHurtboxes(PlayerCharacter player, int framesSinceLast);
	public boolean isFinishedAttacking(PlayerCharacter player, int framesSinceLast) {
		return (framesSinceLast >= attackLengths[attackLengths.length - 1]);
	}
	public abstract String getName();
	public int getFrame(int framesSinceLast) {
		int frame = 0;
		while (frame < attackLengths.length && framesSinceLast >= attackLengths[frame]) {
			frame++;
		}
		if (frame >= attackLengths.length) frame = attackLengths.length - 1;
		return frame;
	}
	public void reset(PlayerCharacter player) {
		//Normally don't need to do anything
	}
}
