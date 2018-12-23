package com.abyad.sprite;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class AnimationSprite extends AbstractSpriteSheet{

	public abstract ArrayList<TextureRegion> getNextFrame(String state, Vector2 direction, int framesSinceLast);
}
