package com.abyad.actor.ui;

import java.util.ArrayList;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.sprite.AbstractSpriteSheet;
import com.abyad.stage.PlayStage;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MagicCursor extends Actor{

	private static final float CURSOR_SCALE = 1.0f;
	private PlayerCharacter player;
	private TextureRegion cursor;
	
	private static Rectangle baseBox = new Rectangle(0, 0, 6, 6);
	
	private Rectangle collideBox;
	private Rectangle tempCollideBox;
	
	private static final int TOTAL_TIME_MARKS = 16;
	private static final float TIME_MARK_RADIUS = 9.5f;
	private static final float TIME_MARK_SCALE = 0.2f;
	
	private static TextureRegion emptyMark = AbstractSpriteSheet.spriteSheets.get("CURSOR_TIME_MARKS").getSprite("EMPTY");
	private static TextureRegion filledMark = AbstractSpriteSheet.spriteSheets.get("CURSOR_TIME_MARKS").getSprite("FILLED");
	private static float markOriginX = emptyMark.getRegionWidth() / 2;
	private static float markOriginY = emptyMark.getRegionHeight() / 2;
	
	private int timeMarks;
	private boolean useTimeMarks;
	
	public MagicCursor(PlayerCharacter player) {
		this.player = player;
		cursor = player.getSprite().getSprite("magic_cursor");
		setOriginX(cursor.getRegionWidth() / 2);
		setOriginY(cursor.getRegionHeight() / 2);
		
		collideBox = new Rectangle(baseBox);
		collideBox.setCenter(player.getCenterX(), player.getCenterY());
		tempCollideBox = new Rectangle(collideBox);
		useTimeMarks = true;
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
		Vector2 direction = new Vector2(player.getVelocity());
		if (direction.len() > 0) {
			direction.setLength(12.0f);
		}
		else {
			direction.set(12, 0);
		}
		setX(player.getCenterX());
		setY(player.getCenterY() );
		player.getStage().addActor(this);
		move(direction.x, direction.y);
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
	
	public void updateTimeMarks(float fraction) {
		timeMarks = (int)(TOTAL_TIME_MARKS * fraction);
		if (fraction >= 1.0f) timeMarks = TOTAL_TIME_MARKS;
	}
	
	public void updateTimeMarks(float count, float total) {
		updateTimeMarks(count / total);
	}
	
	public void useTimeMarks(boolean use) {
		useTimeMarks = use;
	}
	
	@Override
	public void draw(Batch batch, float a) {
		cursor = player.getSprite().getSprite("magic_cursor");
		batch.draw(cursor, getX() - getOriginX(), getY() - getOriginY(),
				getOriginX(), getOriginY(), cursor.getRegionWidth(), cursor.getRegionHeight(),
				CURSOR_SCALE, CURSOR_SCALE, getRotation());
		if (useTimeMarks) {
			Vector2 spacing = new Vector2(0, TIME_MARK_RADIUS);
			float angle = 360f / TOTAL_TIME_MARKS;
			spacing.rotate(-angle);
			for (int i = 0; i < TOTAL_TIME_MARKS; i++) {
				float markX = getX() + spacing.x;
				float markY = getY() + spacing.y;
				if (i < timeMarks) {
					batch.draw(filledMark, markX - markOriginX, markY - markOriginY,
							markOriginX, markOriginY, filledMark.getRegionWidth(), filledMark.getRegionHeight(),
							TIME_MARK_SCALE, TIME_MARK_SCALE, getRotation());
				}
				else {
					batch.draw(emptyMark, markX - markOriginX, markY - markOriginY,
							markOriginX, markOriginY, emptyMark.getRegionWidth(), emptyMark.getRegionHeight(),
							TIME_MARK_SCALE, TIME_MARK_SCALE, getRotation());
				}
				spacing.rotate(-angle);
			}
		}
	}
}
