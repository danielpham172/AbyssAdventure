package com.abyad.actor.mapobjects.items;

import java.util.ArrayList;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.stage.PlayStage;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class MapItem extends Actor{

	protected Vector2 velocity;
	protected TextureRegion tex;
	
	protected float followRadius;
	protected boolean following;
	
	public MapItem(TextureRegion tex, float followRadius) {
		this.tex = tex;
		setOriginX(tex.getRegionWidth() / 2);
		setOriginY(tex.getRegionHeight() / 2);
		this.followRadius = followRadius;
	}
	
	@Override
	public void act(float delta) {
		if (following) {
			ArrayList<PlayerCharacter> players = PlayerCharacter.getPlayers();
			velocity.set(0, 0);
			
			for (PlayerCharacter player : players) {
				Vector2 distance = new Vector2(player.getCenterX() - getX(), player.getY() - getY());
				if (distance.len() < followRadius) {
					distance.setLength(((followRadius - distance.len()) / followRadius) * 5.0f);
					velocity.add(distance);
				}
			}
			if(velocity.len() > 3.5f) velocity.setLength(3.5f);
			move(velocity.x, velocity.y);
		}
		else {
			if (velocity.len2() > 0) {
				move(velocity.x, velocity.y);
				velocity.setLength(0.95f * velocity.len());
				if (velocity.len() < 0.2f) velocity.setLength(0);
			}
		}
	}
	
	@Override
	public void draw(Batch batch, float a) {
		if (inView()) {
			batch.draw(tex, getX() - getOriginX(), getY() - getOriginY(),
					getOriginX(), getOriginY(), tex.getRegionWidth(), tex.getRegionHeight(),
					getScaleX(), getScaleY(), getRotation());
		}
	}
	
	public boolean inView() {
		return getStage().getCamera().frustum.boundsInFrustum(getX(), getY(), 0,
				tex.getRegionWidth() / 2, tex.getRegionHeight() / 2, 0);
	}
	
	
	public void move(float x, float y) {
		float newX = getX() + x;
		float newY = getY() + y;
		PlayStage stage = (PlayStage)getStage();
		ArrayList<Rectangle> walls = stage.getSurroundingWallBoxes(newX, newY, 1);		//Gets surrounding wall tiles as rectangle collisions
		ArrayList<Rectangle> newCollisionBox = getCollideBox(newX, newY);			//Creates own collision box for future
		if (!isOverlapping(walls, newCollisionBox)) {
			setX(getX() + x);
			setY(getY() + y);
			updateCollideAndInteractBox();
		}
		else {
			ArrayList<Rectangle> wallsX = stage.getSurroundingWallBoxes(newX, getY(), 1);
			ArrayList<Rectangle> newCollisionBoxX = getCollideBox(newX, getY());
			if (!isOverlapping(wallsX, newCollisionBoxX)) {
				float minimumYChange = returnMinimumYChange(wallsX, newCollisionBoxX, y);
				setX(getX() + x);
				setY(getY() + minimumYChange);
				updateCollideAndInteractBox();
			}
			else {
				ArrayList<Rectangle> wallsY = stage.getSurroundingWallBoxes(getX(), newY, 1);
				ArrayList<Rectangle> newCollisionBoxY = getCollideBox(getX(), newY);
				if (!isOverlapping(wallsY, newCollisionBoxY)) {
					float minimumXChange = returnMinimumXChange(wallsY, newCollisionBoxY, x);
					setX(getX() + minimumXChange);
					setY(getY() + x);
					updateCollideAndInteractBox();
				}
				else {
					float minimumXChange = returnMinimumXChange(walls, getCollideBox(), x);
					float minimumYChange = returnMinimumYChange(walls, getCollideBox(), y);
					setX(getX() + minimumXChange);
					setY(getY() + minimumYChange);
					updateCollideAndInteractBox();
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
	
	public void setFollowing(boolean following) {
		this.following = following;
	}
	
	public TextureRegion getTexture() {
		return tex;
	}
	
	public Vector2 getVelocity() {
		return velocity;
	}
	
	public abstract void updateCollideAndInteractBox();
	public abstract ArrayList<Rectangle> getCollideBox();
	public abstract ArrayList<Rectangle> getCollideBox(float x, float y);
	
}
