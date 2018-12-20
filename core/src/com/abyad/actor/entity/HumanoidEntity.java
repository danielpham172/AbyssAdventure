package com.abyad.actor.entity;

import java.util.ArrayList;

import com.abyad.actor.tile.AbstractTile;
import com.abyad.stage.PlayStage;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class HumanoidEntity extends AbstractEntity{

	protected ArrayList<Rectangle> hitboxes;				//The list of hitboxes for a humanoid. A list because we might need to use multiple rectangles
	protected ArrayList<Rectangle> tempHitboxes;			//Same as above, used to check future hitboxes without having to create a new rectangle everytime
	protected boolean hitWall;				//Boolean to check if the character hit a wall or not when trying to move
	private static int SPRITE_LENGTH = 16;	//The length and height of the sprite
	protected Rectangle viewbox = new Rectangle(0, 0, SPRITE_LENGTH, SPRITE_LENGTH);		//The viewbox used to check whether to draw the sprite or not
	
	public HumanoidEntity() {
		super();
		
		hitboxes = new ArrayList<Rectangle>();
		hitboxes.add(new Rectangle());
		tempHitboxes = new ArrayList<Rectangle>();
		tempHitboxes.add(new Rectangle());
		setOrigin(SPRITE_LENGTH / 2, SPRITE_LENGTH / 2);
		updateHitbox();
	}
	
	/**
	 * Fancy move method for collision detection. Currently works well with non-tile collisions too
	 */
	@Override
	public void move(float x, float y) {
		float newX = getX() + x;
		float newY = getY() + y;
		hitWall = false;
		PlayStage stage = (PlayStage)getStage();
		ArrayList<Rectangle> walls = stage.getSurroundingCollisionBoxes(newX, newY, 1);		//Gets surrounding wall tiles as rectangle collisions
		ArrayList<Rectangle> newCollisionBox = getCollideBox(newX, newY);			//Creates own collision box for future
		if (!isOverlapping(walls, newCollisionBox)) {
			super.move(x, y);		//Move if open
		}
		else {
			//Vector2 alignment = getAlignmentChange(newCollisionBox);		//Get alignment changes to go against a wall (stay in tile)
			hitWall = true;
			ArrayList<Rectangle> wallsX = stage.getSurroundingWallBoxes(newX, getY(), 1);
			ArrayList<Rectangle> newCollisionBoxX = getCollideBox(newX, getY());
			if (!isOverlapping(wallsX, newCollisionBoxX)) {
				float minimumYChange = returnMinimumYChange(wallsX, newCollisionBoxX, y);
				super.move(x, minimumYChange);		//Move x-wise if possible
				//super.move(x, y - alignment.y)
			}
			else {
				ArrayList<Rectangle> wallsY = stage.getSurroundingWallBoxes(getX(), newY, 1);
				ArrayList<Rectangle> newCollisionBoxY = getCollideBox(getX(), newY);
				if (!isOverlapping(wallsY, newCollisionBoxY)) {
					float minimumXChange = returnMinimumXChange(wallsY, newCollisionBoxY, x);
					super.move(minimumXChange, y);	//Move y-wise if possible
					//super.move(x - alignment.x, y);
				}
				else {
					float minimumXChange = returnMinimumXChange(walls, getCollideBox(), x);
					float minimumYChange = returnMinimumYChange(walls, getCollideBox(), y);
					super.move(minimumXChange, minimumYChange);
					//super.move(x - alignment.x, y - alignment.y);		//Fit inside tile only
				}
			}
		}
	}
	
	private float returnMinimumXChange(ArrayList<Rectangle> walls, ArrayList<Rectangle> collisionBoxes, float initialX) {
		float minimumXChange = initialX;
		
		for (Rectangle collisionBox : collisionBoxes) {
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
		}
		
		return minimumXChange;
	}
	
	private float returnMinimumYChange(ArrayList<Rectangle> walls, ArrayList<Rectangle> collisionBoxes, float initialY) {
		float minimumYChange = initialY;
		
		for (Rectangle collisionBox : collisionBoxes) {
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
		}
		
		return minimumYChange;
	}
	
	/**
	 * Collision method for checking overlapping rectangles in list. Could probably be used as a Utility function
	 * @param first		First list of rectangles
	 * @param second	Second list of rectangles
	 * @return		Whether at least one rectangle overlaps
	 */
	protected boolean isOverlapping(ArrayList<Rectangle> first, ArrayList<Rectangle> second) {
		for (Rectangle f : first) {
			for (Rectangle s : second) {
				if (f.overlaps(s)) return true;
			}
		}
		return false;
	}
	
	/**
	 * Creates alignment change to fit into current tile
	 * @param newCollisionBox		Hitboxes that may be outside of the tile
	 * @return		Alignment changes needed
	 */
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
		return getX() - getOriginX() + 8.0f;		//Center of the hitbox
	}
	
	@Override
	public float getCenterY() {
		return getY() - getOriginY() + 5.0f;		//Center of the hitbox
	}
	
	@Override
	public void updateHitbox() {
		int direction = (int)((getVelocity().angle() + 45) / 90) % 4;	//Unused, but may be necessary if direction changes hitbox
		int xCorrection = 0;
		int yCorrection = 0;
		hitboxes.get(0).set(getX() - getOriginX() + 3 + xCorrection, getY() - getOriginY() + yCorrection, 10, 10);
	}
	
	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x, y);
		updateHitbox();		//Overriding so changing position updates the hitbox also
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
