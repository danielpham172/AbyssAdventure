package com.abyad.relic;

import java.util.ArrayList;

import com.abyad.actor.attack.AttackData;
import com.abyad.actor.entity.AbstractEntity;
import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.actor.mapobjects.TreasureChest;
import com.abyad.data.AttributeData;
import com.abyad.data.HitEvent;
import com.abyad.magic.AbstractMagic;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Passive items, abstract class for now
 *
 */
public abstract class Relic {

	private float activationRate;
	private int cooldown;
	private TextureRegion tex;
	private int count;
	private ArrayList<AttributeData> attributes;
	
	private String name;
	private String desc;
	
	private static ArrayList<Class> relicClasses = new ArrayList<Class>();
	
	static {
		relicClasses.add(TonWeightRelic.class);
		relicClasses.add(PanicCharmRelic.class);
		relicClasses.add(VampiricFangRelic.class);
		relicClasses.add(ManaShieldRelic.class);
		relicClasses.add(GreenCloakRelic.class);
		relicClasses.add(PocketwatchRelic.class);
		relicClasses.add(RinasScarfRelic.class);
		relicClasses.add(LifetapRelic.class);
	}
	
	public Relic(String name, String desc, float activationRate, TextureRegion tex) {
		this.name = name;
		this.desc = desc;
		this.activationRate = activationRate;
		this.tex = tex;
		cooldown = 0;
		count = 1;
		attributes = new ArrayList<AttributeData>();
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
	
	public int getCount() {
		return count;
	}
	
	public float getAttribute(String name) {
		float total = 0;
		for (AttributeData attribute : attributes) {
			if (attribute.getName().equals(name)) {
				total += attribute.getValue();
			}
		}
		return total;
	}
	
	public void addAttribute(String name, float value) {
		attributes.add(new AttributeData(name, value));
	}
	
	public void incrementCount() {
		count++;
	}
	
	public void update() {
		if (cooldown > 0) cooldown--;
	}
	
	public void onPickup(PlayerCharacter player) {
		//Do nothing
	}
	
	public void onPassive(PlayerCharacter player) {
		//Do nothing
	}
	
	public void onAttack(PlayerCharacter player) {
		//Do nothing
	}
	
	public void onHit(PlayerCharacter player, AttackData attackType, HitEvent attack, AbstractEntity hit) {
		//Do nothing
	}
	
	public void onMagicHit(PlayerCharacter player, AbstractMagic magicType, HitEvent attack, AbstractEntity hit) {
		//Do nothing
	}
	
	public void onDefense(PlayerCharacter player, HitEvent defense) {
		//Do nothing
	}
	
	public void onKill(PlayerCharacter player, AbstractEntity hit) {
		//Do nothing
	}
	
	public void onChestOpen(PlayerCharacter player, TreasureChest chest) {
		//Do nothing
	}
	
	public abstract int getPriority();
	
	@Override
	public boolean equals(Object o) {
		if (this.getClass().getName().equals(o.getClass().getName())) {
			return true;
		}
		else {
			return false;
		}
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
