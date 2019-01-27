package com.abyad.relic;

import java.util.ArrayList;

import com.abyad.actor.entity.AbstractEntity;
import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.data.HitEvent;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Passive items, abstract class for now
 *
 */
public abstract class Relic {

	private float activationRate;
	private int cooldown;
	private TextureRegion tex;
	
	private String name;
	private String desc;
	
	private static ArrayList<Class> relicClasses = new ArrayList<Class>();
	
	static {
		relicClasses.add(TonWeightRelic.class);
		relicClasses.add(PanicCharmRelic.class);
		relicClasses.add(VampiricFangRelic.class);
		relicClasses.add(ManaShieldRelic.class);
	}
	
	public Relic(String name, String desc, float activationRate, TextureRegion tex) {
		this.name = name;
		this.desc = desc;
		this.activationRate = activationRate;
		this.tex = tex;
		cooldown = 0;
	}
	
	public float getActivationRate() {
		return activationRate;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return desc;
	}
	
	public void goOnCooldown() {
		cooldown = 0;
	}
	
	public boolean isOnCooldown() {
		return cooldown > 0;
	}
	
	public TextureRegion getTexture() {
		return tex;
	}
	
	public void update() {
		if (cooldown > 0) cooldown--;
	}
	
	public void onPassive(PlayerCharacter player) {
		//Do nothing
	}
	
	public void onAttack(PlayerCharacter player) {
		//Do nothing
	}
	
	public void onHit(PlayerCharacter player, HitEvent attack, AbstractEntity hit) {
		//Do nothing
	}
	
	public void onDefense(PlayerCharacter player, HitEvent defense) {
		//Do nothing
	}
	
	public static Relic createRandomRelic() {
		try {
			Relic randomRelic = (Relic)relicClasses.get((int)(Math.random() * relicClasses.size())).newInstance();
			return randomRelic;
		} catch (InstantiationException e) {
			// Needs a default constructor;
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			// Maybe because constructor can't be accessed?
			e.printStackTrace();
			return null;
		}
	}
}
