package com.abyad.actor.ui;

import com.abyad.actor.entity.PlayerCharacter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MagicCursor extends Actor{

	private static final float CURSOR_SCALE = 1.0f;
	private PlayerCharacter player;
	private TextureRegion cursor;
	
	public MagicCursor(PlayerCharacter player) {
		this.player = player;
		cursor = player.getSprite().getSprite("magic_cursor");
		setOriginX(cursor.getRegionWidth() / 2);
		setOriginY(cursor.getRegionHeight() / 2);
	}
	
	public void move(float x, float y) {
		setPosition(getX() + x, getY() + y);
	}
	
	@Override
	public void draw(Batch batch, float a) {
		batch.draw(cursor, getX() - getOriginX(), getY() - getOriginY(),
				getOriginX(), getOriginY(), cursor.getRegionWidth(), cursor.getRegionHeight(),
				CURSOR_SCALE, CURSOR_SCALE, getRotation());
	}
}
