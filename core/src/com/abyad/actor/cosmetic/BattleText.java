package com.abyad.actor.cosmetic;

import com.badlogic.gdx.math.Vector2;

public class BattleText extends DisplayText {

	private Vector2 velocity;
	private float velocityChange;
	private int lifetime;
	private int maxLifetime;
	private boolean fade;
	
	public BattleText(String text, Vector2 velocity, float change, int lifetime) {
		this(text, 0, 0, velocity, change, lifetime, false);
	}
	public BattleText(String text, float x, float y, Vector2 velocity, float change, int lifetime) {
		this(text, x, y, velocity, change, lifetime, false);
	}
	public BattleText(String text, float x, float y, Vector2 velocity, float change, int lifetime, boolean fade) {
		super(text, x, y);
		this.velocity = velocity;
		this.velocityChange = change;
		maxLifetime = lifetime;
		this.fade = fade;
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		setX(getX() + velocity.x);
		setY(getY() + velocity.y);
		velocity.scl(velocityChange);
		lifetime++;
		
		if (fade) setTransparency(1.0f - ((float)lifetime / (float)maxLifetime));
		
		if (lifetime > maxLifetime) remove();
	}

}
