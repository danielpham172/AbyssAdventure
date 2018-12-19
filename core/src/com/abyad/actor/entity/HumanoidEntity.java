package com.abyad.actor.entity;

import java.util.ArrayList;

import com.abyad.actor.tile.AbstractTile;
import com.abyad.stage.PlayStage;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class HumanoidEntity extends AbstractEntity{

	protected ArrayList<Rectangle> hitboxes;
	protected ArrayList<Rectangle> tempHitboxes;
	protected boolean hitWall;
	private static int SPRITE_LENGTH = 16;
	protected Rectangle viewbox = new Rectangle(0, 0, SPRITE_LENGTH, SPRITE_LENGTH);
	
	public HumanoidEntity() {
		super();
		
		hitboxes = new ArrayList<Rectangle>();
		hitboxes.add(new Rectangle());
		tempHitboxes = new ArrayList<Rectangle>();
		tempHitboxes.add(new Rectangle());
		setOrigin(SPRITE_LENGTH / 2, SPRITE_LENGTH / 2);
		updateHitbox();
	}
	
	@Override
	public void move(float x, float y) {
		float newX = getX() + x;
		float newY = getY() + y;
		hitWall = false;
		PlayStage stage = (PlayStage)getStage();
		ArrayList<Rectangle> walls = stage.getSurroundingWallBoxes(newX, newY, 1);
		ArrayList<Rectangle> newCollisionBox = getCollideBox(newX, newY);
		if (!isColliding(walls, newCollisionBox)) {
			super.move(x, y);
		}
		else {
			Vector2 alignment = getAlignmentChange(newCollisionBox);
			hitWall = true;
			ArrayList<Rectangle> wallsX = stage.getSurroundingWallBoxes(newX, getY(), 1);
			ArrayList<Rectangle> newCollisionBoxX = getCollideBox(newX, getY());
			if (!isColliding(wallsX, newCollisionBoxX)) {
				super.move(x, y - alignment.y);
			}
			else {
				ArrayList<Rectangle> wallsY = stage.getSurroundingWallBoxes(getX(), newY, 1);
				ArrayList<Rectangle> newCollisionBoxY = getCollideBox(getX(), newY);
				if (!isColliding(wallsY, newCollisionBoxY)) {
					super.move(x - alignment.x, y);
				}
				else {
					super.move(x - alignment.x, y - alignment.y);
				}
			}
		}
	}
	
	private boolean isColliding(ArrayList<Rectangle> first, ArrayList<Rectangle> second) {
		for (Rectangle f : first) {
			for (Rectangle s : second) {
				if (f.overlaps(s)) return true;
			}
		}
		return false;
	}
	
	private Vector2 getAlignmentChange(ArrayList<Rectangle> newCollisionBox) {
		float tileLength = AbstractTile.TILE_SIZE * AbstractTile.TILE_SCALE;
		Rectangle currentTile = new Rectangle((int)(getX() / tileLength) * tileLength, (int)(getY() / tileLength) * tileLength,
				tileLength, tileLength);
		float boundedX = 0;
		float boundedY = 0;
		boolean first = true;
		for (Rectangle collideBox : newCollisionBox) {
			float newBoundedX = collideBox.getX() - MathUtils.clamp(collideBox.getX(), currentTile.getX(), currentTile.getX() + tileLength - collideBox.getWidth());
			float newBoundedY = collideBox.getY() - MathUtils.clamp(collideBox.getY(), currentTile.getY(), currentTile.getY() + tileLength - collideBox.getHeight());
			if (first || Math.abs(newBoundedX) < Math.abs(boundedX)) boundedX = newBoundedX;
			if (first || Math.abs(newBoundedY) < Math.abs(boundedY)) boundedY = newBoundedY;
			first = false;
		}
		return new Vector2(boundedX, boundedY);
	}
	
	@Override
	public float getCenterX() {
		return getX() - getOriginX() + 8.0f;
	}
	
	@Override
	public float getCenterY() {
		return getY() - getOriginY() + 5.0f;
	}
	
	@Override
	public void updateHitbox() {
		int direction = (int)((getVelocity().angle() + 45) / 90) % 4;
		int xCorrection = 0;
		int yCorrection = 0;
		hitboxes.get(0).set(getX() - getOriginX() + 3 + xCorrection, getY() - getOriginY() + yCorrection, 10, 10);
	}
	
	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x, y);
		updateHitbox();
	}
	
	@Override
	public ArrayList<Rectangle> getHitbox() {
		// TODO Auto-generated method stub
		return hitboxes;
	}
	
	@Override
	public ArrayList<Rectangle> getHitbox(float x, float y) {
		float xDiff = x - getX();
		float yDiff = y - getY();
		for (int i = 0; i < hitboxes.size(); i++) {
			Rectangle hitbox = hitboxes.get(i);
			Rectangle newHitbox = tempHitboxes.get(i);
			newHitbox.set(hitbox.getX() + xDiff, hitbox.getY() + yDiff, hitbox.getWidth(), hitbox.getHeight());
		}
		return tempHitboxes;
	}
	
	@Override
	public ArrayList<Rectangle> getCollideBox() {
		// TODO Auto-generated method stub
		return hitboxes;
	}
	
	@Override
	public ArrayList<Rectangle> getCollideBox(float x, float y) {
		float xDiff = x - getX();
		float yDiff = y - getY();
		for (int i = 0; i < hitboxes.size(); i++) {
			Rectangle hitbox = hitboxes.get(i);
			Rectangle newHitbox = tempHitboxes.get(i);
			newHitbox.set(hitbox.getX() + xDiff, hitbox.getY() + yDiff, hitbox.getWidth(), hitbox.getHeight());
		}
		return tempHitboxes;
	}
	
	@Override
	public Rectangle getViewbox() {
		viewbox.setX(getX() - getOriginX());
		viewbox.setY(getY() - getOriginY());
		return viewbox;
	}
}
