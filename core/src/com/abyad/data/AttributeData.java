package com.abyad.data;

public class AttributeData {

	private String name;
	private float value;
	
	public AttributeData(String name, float value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	
	public float getValue() {
		return value;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setValue(float value) {
		this.value = value;
	}
}
