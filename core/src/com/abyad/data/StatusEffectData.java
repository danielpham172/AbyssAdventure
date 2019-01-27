package com.abyad.data;

public class StatusEffectData {

	private String name;
	private float potency;
	private int time;
	
	public StatusEffectData(String name, float potency) {
		this.name = name;
		this.potency = potency;
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
}
