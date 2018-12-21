package com.abyad.data;

import com.abyad.actor.entity.AbstractEntity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class HitEvent {

	private Actor source;
	private AbstractEntity victim;
	
	private int damage;
	private Vector2 knockbackVelocity;
	private int knockbackLength;
	
	public HitEvent(Actor source, AbstractEntity victim, int damage, Vector2 knockbackVelocity, int knockbackLength) {
		this.source = source;
		this.victim = victim;
		this.damage = damage;
		this.knockbackVelocity = knockbackVelocity;
		this.knockbackLength = knockbackLength;
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
	
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public void setKnockbackVelocity(Vector2 knockbackVelocity) {
		this.knockbackVelocity = knockbackVelocity;
	}
	
	public void setKnockbackLength(int knockbackLength) {
		this.knockbackLength = knockbackLength;
	}
}
