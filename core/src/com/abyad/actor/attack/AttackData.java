package com.abyad.actor.attack;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.abyad.actor.entity.PlayerCharacter;
import com.badlogic.gdx.math.Rectangle;

public abstract class AttackData {

	public static LinkedHashMap<String, AttackData> basicAttacks = new LinkedHashMap<String, AttackData>();
	public static LinkedHashMap<String, SpecialAttackData> specialAttacks = new LinkedHashMap<String, SpecialAttackData>();
	static {
		basicAttacks.put("SWORD", new BasicSwordAttack());
		
		specialAttacks.put("SPIN_SLASH", new SpinSlash());
		specialAttacks.put("WIND_BLADE", new WindBlade());
	}
	
	public abstract void initiateAttack(PlayerCharacter player);
	public abstract void useAttack(PlayerCharacter player, int framesSinceLast);
	public abstract ArrayList<Rectangle> getHurtboxes(PlayerCharacter player, int framesSinceLast);
	public abstract boolean isFinishedAttacking(PlayerCharacter player, int framesSinceLast);
}
