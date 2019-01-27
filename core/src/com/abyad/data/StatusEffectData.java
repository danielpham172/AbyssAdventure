package com.abyad.data;

import com.abyad.actor.entity.AbstractEntity;

public class StatusEffectData {

	private String name;
	private float potency;
	private int time;
	private boolean infiniteTime;
	private boolean remove;
	
	public StatusEffectData(String name, float potency) {
		this.name = name;
		this.potency = potency;
		infiniteTime = true;
	}
	
	public StatusEffectData(String name, float potency, int time) {
		this.name = name;
		this.potency = potency;
		this.time = time;
	}
	
	public String getName() {
		return name;
	}
	
	public float getPotency() {
		return potency;
	}
	
	public int getTime() {
		return time;
	}
	
	public boolean infiniteTime() {
		return infiniteTime;
	}
	
	public void update() {
		if (!infiniteTime && time > 0) {
			time--;
		}
	}
	
	public boolean remove() {
		return ((!infiniteTime && time <= 0) || (remove));
	}
	
	//Similar to relics, but time limited status effect instead
	public void onPassive(AbstractEntity entity) {
		//Do nothing
	}
	
	public void onAttack(AbstractEntity entity) {
		//Do nothing
	}
	
	public void onHit(AbstractEntity entity, HitEvent attack, AbstractEntity hit) {
		//Do nothing
	}
	
	public void onDefense(AbstractEntity entity, HitEvent defense) {
		//Do nothing
	}
}
