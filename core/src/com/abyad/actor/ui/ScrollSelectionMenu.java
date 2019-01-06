package com.abyad.actor.ui;

import java.util.ArrayList;

import com.abyad.sprite.AbstractSpriteSheet;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class ScrollSelectionMenu<E> extends Actor {

	protected final float ICON_SCALE;
	
	private static final float ARROWS_DISTANCE = 18;
	private static final float ARROWS_OFFSET_Y = 0.5f;
	
	private static TextureRegion leftArrow = AbstractSpriteSheet.spriteSheets.get("SELECTION_ARROWS").getSprite("LEFT");
	private static TextureRegion rightArrow = AbstractSpriteSheet.spriteSheets.get("SELECTION_ARROWS").getSprite("RIGHT");
	
	protected int selection;
	
	protected boolean markForRemoval;
	
	public ScrollSelectionMenu() {
		ICON_SCALE = 1.0f;
	}
	
	public ScrollSelectionMenu(float iconScale) {
		ICON_SCALE = iconScale;
		
	}
	
	@Override
	public void act(float delta) {
		if (markForRemoval) {
			remove();
		}
	}
	
	@Override
	public void draw(Batch batch, float a) {
		if (inView()) {
			ArrayList<E> list = getList();
			Vector2 center = getCenter();
			
			TextureRegion icon = getIcon(list.get(selection));
			batch.draw(icon, center.x - (icon.getRegionWidth() / 2), center.y - (icon.getRegionHeight() / 2),
					icon.getRegionWidth() / 2, icon.getRegionHeight() / 2, icon.getRegionWidth(), icon.getRegionHeight(),
					ICON_SCALE, ICON_SCALE, 0);
			batch.draw(leftArrow, center.x - (leftArrow.getRegionWidth() / 2) - (ARROWS_DISTANCE * ICON_SCALE), center.y - (leftArrow.getRegionHeight() / 2) + (ARROWS_OFFSET_Y * ICON_SCALE),
					leftArrow.getRegionWidth() / 2, leftArrow.getRegionHeight() / 2, leftArrow.getRegionWidth(), leftArrow.getRegionHeight(),
					ICON_SCALE, ICON_SCALE, 0);
			batch.draw(rightArrow, center.x - (rightArrow.getRegionWidth() / 2) + (ARROWS_DISTANCE * ICON_SCALE), center.y - (rightArrow.getRegionHeight() / 2) + (ARROWS_OFFSET_Y * ICON_SCALE),
					rightArrow.getRegionWidth() / 2, rightArrow.getRegionHeight() / 2, rightArrow.getRegionWidth(), rightArrow.getRegionHeight(),
					ICON_SCALE, ICON_SCALE, 0);
		}
	}
	
	public abstract TextureRegion getIcon(E obj);
	public abstract ArrayList<E> getList();
	public abstract Vector2 getCenter();
	
	public void select(int direction) {
		int listSize = getList().size();
		if (direction < 0) {
			selection = (selection + 1) % listSize;
		}
		else if (direction > 0) {
			selection = (selection + listSize - 1) % listSize;
		}
	}
	
	public boolean inView() {
		Vector2 center = getCenter();
		return getStage().getCamera().frustum.boundsInFrustum(center.x, center.y, 0,
				ICON_SCALE * 19, ICON_SCALE * 8, 0);
	}
	
	@Override
	public boolean remove() {
		markForRemoval = false;
		
		return super.remove();
	}
	
	public void closeMenu() {
		markForRemoval = true;
	}
	
	
	public boolean isMenuActive() {
		return (getStage() != null);
	}
	
	public int getSelection() {
		return selection;
	}
}
