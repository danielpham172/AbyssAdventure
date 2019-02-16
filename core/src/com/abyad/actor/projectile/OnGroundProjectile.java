package com.abyad.actor.projectile;

import com.abyad.actor.entity.AbstractEntity;

//Class mainly used for drawing purposes
public abstract class OnGroundProjectile extends AbstractProjectile{

	public OnGroundProjectile(float x, float y, AbstractEntity source) {
		super(x, y, source);
	}

}
