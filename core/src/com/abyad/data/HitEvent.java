package com.abyad.data;

import java.util.ArrayList;

import com.abyad.actor.entity.AbstractEntity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class HitEvent {

	private Actor source;
	private AbstractEntity victim;
	
	private int damage;
	private Vector2 knockbackVelocity;
	private int knockbackLength;
	private ArrayList<StatusEffectData> statusEffects;
	private float invulnModifier;
	
	public HitEvent(Actor source, AbstractEntity victim, int damage, Vector2 knockbackVelocity, int knockbackLength) {
		this.source = source;
		this.victim = victim;
		this.damage = damage;
		this.knockbackVelocity = knockbackVelocity;
		this.knockbackLength = knockbackLength;
		this.statusEffects = new ArrayList<StatusEffectData>();
		invulnModifier = 1.0f;
	}
	
	public HitEvent(Actor source, AbstractEntity victim, int damage, Vector2 knockbackVelocity, int knockbackLength, StatusEffectData...statusEffects) {
		this(source, victim, damage, knockbackVelocity, knockbackLength);
		for (StatusEffectData statusEffect : statusEffects) {
			this.statusEffects.add(statusEffect);
		}
	}
	
	public Actor getAttacker() {
		return source;
	}
	
	public AbstractEntity getDefender() {
		return victim;
	}
	
	public int getDamage() {
		return damage;
	}
	
	public Vector2 getKnockbackVelocity() {
		return knockbackVelocity;
	}
	
	public int getKnockbackLength() {
		return knockbackLength;
	}
	
	public float getInvulnModifier() {
		return invulnModifier;
	}
	
	public ArrayList<StatusEffectData> getStatusEffects(){
		return statusEffects;
	}
	
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public void setKnockbackVelocity(Vector2 knockbackVelocity) {
		this.knockbackVelocity = knockbackVelocity;
	}
	
	public void setKnockbackLength(int knockbackLength) {
		this.knockbackLength = knockbackLength;
	}
	
	public void setInvulnModifier(float invulnModifier) {
		this.invulnModifier =  invulnModifier;
	}
	
	public void addStatusEffect(StatusEffectData data) {
		statusEffects.add(data);
	}
	
	public void addStatusEffect(String name, float potency, int time) {
		addStatusEffect(new StatusEffectData(name, potency, time));
	}
	
	public void addStatusEffect(String name, float potency) {
		addStatusEffect(new StatusEffectData(name, potency));
	}
}
