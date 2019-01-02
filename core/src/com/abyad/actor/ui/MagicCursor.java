package com.abyad.actor.ui;

import java.util.ArrayList;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.stage.PlayStage;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MagicCursor extends Actor{

	private static final float CURSOR_SCALE = 1.0f;
	private PlayerCharacter player;
	private TextureRegion cursor;
	
	private static Rectangle baseBox = new Rectangle(0, 0, 6, 6);
	
	private Rectangle collideBox;
	private Rectangle tempCollideBox;
	
	public MagicCursor(PlayerCharacter player) {
		this.player = player;
		cursor = player.getSprite().getSprite("magic_cursor");
		setOriginX(cursor.getRegionWidth() / 2);
		setOriginY(cursor.getRegionHeight() / 2);
		
		collideBox = new Rectangle(baseBox);
		collideBox.setCenter(player.getCenterX(), player.getCenterY());
		tempCollideBox = new Rectangle(collideBox);
	}
	
	
	/**
	 * Makes the cursor unable to move outside of walls
	 */
	public void move(float x, float y) {
		float newX = getX() + x;
		float newY = getY() + y;
		PlayStage stage = (PlayStage)getStage();
		ArrayList<Rectangle> walls = stage.getSurroundingWallBoxes(newX, newY, 1);		//Gets surrounding wall tiles as rectangle collisions
		Rectangle newCollisionBox = getCollideBox(newX, newY);			//Creates own collision box for future
		if (!isOverlapping(walls, newCollisionBox)) {
			setPosition(getX() + x, getY() + y);
		}
		else {
			//Vector2 alignment = getAlignmentChange(newCollisionBox);		//Get alignment changes to go against a wall (stay in tile)
			ArrayList<Rectangle> wallsX = stage.getSurroundingCollisionBoxes(newX, getY(), 1);
			Rectangle newCollisionBoxX = getCollideBox(newX, getY());
			if (!isOverlapping(wallsX, newCollisionBoxX)) {
				float minimumYChange = returnMinimumYChange(wallsX, newCollisionBoxX, y);
				setPosition(getX() + x, getY() + minimumYChange);
			}
			else {
				ArrayList<Rectangle> wallsY = stage.getSurroundingCollisionBoxes(getX(), newY, 1);
				Rectangle newCollisionBoxY = getCollideBox(getX(), newY);
				if (!isOverlapping(wallsY, newCollisionBoxY)) {
					float minimumXChange = returnMinimumXChange(wallsY, newCollisionBoxY, x);
					setPosition(getX() + minimumXChange, getY() + y);
				}
				else {
					float minimumXChange = returnMinimumXChange(walls, getCollideBox(), x);
					float minimumYChange = returnMinimumYChange(walls, getCollideBox(), y);
					setPosition(getX() + minimumXChange, getY() + minimumYChange);
				}
			}
		}
		
		updateCollideBox();
	}
	
	public void spawnInCursor() {
		setX(player.getCenterX());
		setY(player.getCenterY() - 0.5f);
		updateCollideBox();
	}
	
	private void updateCollideBox() {
		collideBox.setCenter(getX(), getY());
	}
	
	private Rectangle getCollideBox() {
		return collideBox;
	}
	
	private Rectangle getCollideBox(float x, float y) {
		tempCollideBox.setCenter(x, y);
		return tempCollideBox;
	}
	
	private boolean isOverlapping(ArrayList<Rectangle> other, Rectangle collideBox) {
		for (Rectangle o : other) {
			if (collideBox.overlaps(o)) return true;
		}
		return false;
	}
	
	private float returnMinimumXChange(ArrayList<Rectangle> walls, Rectangle collisionBox, float initialX) {
		float minimumXChange = initialX;
		float bottomY = collisionBox.getY();
		float topY = collisionBox.getY() + collisionBox.getHeight();
		float xCollision = collisionBox.getX();
		float width = collisionBox.getWidth();
		
		for (Rectangle box : walls) {
			if (bottomY < box.getY() + box.getHeight() && topY > box.getY()) {
				if (initialX < 0 && box.getX() < xCollision) {
					float xChange = (box.getX() + box.getWidth()) - xCollision;
					if (xChange > minimumXChange) minimumXChange = xChange;
				}
				else if (initialX > 0 && box.getX() > xCollision) {
					float xChange = box.getX() - (xCollision + width);
					if (xChange < minimumXChange) minimumXChange = xChange;
				}
			}
		}
		
		return minimumXChange;
	}
	
	private float returnMinimumYChange(ArrayList<Rectangle> walls, Rectangle collisionBox, float initialY) {
		float minimumYChange = initialY;
		
		float leftX = collisionBox.getX();
		float rightX = collisionBox.getX() + collisionBox.getWidth();
		float yCollision = collisionBox.getY();
		float height = collisionBox.getHeight();
		
		for (Rectangle box : walls) {
			if (leftX < box.getX() + box.getWidth() && rightX > box.getX()) {
				if (initialY < 0 && box.getY() < yCollision) {
					float yChange = (box.getY() + box.getHeight()) - yCollision;
					if (yChange > minimumYChange) minimumYChange = yChange;
				}
				else if (initialY > 0 && box.getY() > yCollision) {
					float yChange = box.getY() - (yCollision + height);
					if (yChange < minimumYChange) minimumYChange = yChange;
				}
			}
		}
		
		return minimumYChange;
	}
	
	@Override
	public void draw(Batch batch, float a) {
		batch.draw(cursor, getX() - getOriginX(), getY() - getOriginY(),
				getOriginX(), getOriginY(), cursor.getRegionWidth(), cursor.getRegionHeight(),
				CURSOR_SCALE, CURSOR_SCALE, getRotation());
	}
}
