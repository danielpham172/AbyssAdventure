package com.abyad.data;

import java.util.ArrayList;

import com.abyad.actor.entity.AbstractEntity;

public class StatusEffectData {

	private String name;
	private float potency;
	private ArrayList<AttributeData> attributes;
	private int time;
	private boolean infiniteTime;
	private boolean remove;
	
	public StatusEffectData(String name, float potency) {
		this.name = name;
		this.potency = potency;
		infiniteTime = true;
		attributes = new ArrayList<AttributeData>();
	}
	
	public StatusEffectData(String name, float potency, int time) {
		this.name = name;
		this.potency = potency;
		this.time = time;
		attributes = new ArrayList<AttributeData>();
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
}
