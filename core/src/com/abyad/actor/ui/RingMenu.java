package com.abyad.actor.ui;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class RingMenu<E> extends Actor{

	protected final float RADIUS;
	protected final int ROTATING_TIME;
	protected final int EXPANDING_TIME;
	protected final float ICON_SCALE;
	
	protected final float MIN_SCALE_ANGLE;
	
	protected int selection;
	
	protected int rotating;
	protected int expanding;
	protected boolean closing;
	
	protected boolean markForRemoval;
	protected int blinkTime;
	
	public RingMenu() {
		RADIUS = 30f;
		ROTATING_TIME = 20;
		EXPANDING_TIME = 20;
		ICON_SCALE = 1.0f;
		MIN_SCALE_ANGLE = 30f;
	}
	
	public RingMenu(float radius, int rotatingTime, int expandingTime, float iconScale, float minScaleAngle) {
		RADIUS = radius;
		ROTATING_TIME = rotatingTime;
		EXPANDING_TIME = expandingTime;
		ICON_SCALE = iconScale;
		MIN_SCALE_ANGLE = minScaleAngle;
		
	}
	
	@Override
	public void act(float delta) {
		if (rotating > 0) rotating--; else if (rotating < 0) rotating++;
		if (expanding > 0 && !closing) {
			expanding--;
		}
		else if (closing){
			expanding++;
			if (expanding == EXPANDING_TIME) markForRemoval = true;
		}
		setPosition(getCenter().x, getCenter().y);
		if (markForRemoval) {
			remove();
		}
		blinkTime = (blinkTime + 1) % 6;
	}
	
	@Override
	public void draw(Batch batch, float a) {
		if (inView()) {
			ArrayList<E> list = getList();
			Vector2 center = getCenter();
			float expandingScale = (1 - ((float)expanding / EXPANDING_TIME));
			Vector2 drawOffsets = new Vector2(0, RADIUS * expandingScale);
			float angleSpacing = 360f / list.size();
			if (rotating != 0) {
				if (angleSpacing >= MIN_SCALE_ANGLE) {
					float offsetRotation = angleSpacing * ((float)rotating / (float)ROTATING_TIME);
					drawOffsets.rotate(offsetRotation);
				}
				else {
					float offsetRotation = angleSpacing * ((float)rotating / (float)(ROTATING_TIME * (angleSpacing / MIN_SCALE_ANGLE)));
					drawOffsets.rotate(offsetRotation);
				}
			}
			drawOffsets.rotate(-270 * (1 - expandingScale));
			ArrayList<E> sortedList = new ArrayList<E>();
			for (int i = selection; i < list.size(); i++) {
				sortedList.add(list.get(i));
			}
			for (int i = 0; i < selection; i++) {
				sortedList.add(list.get(i));
			}
			
			batch.setColor(1.0f, 1.0f, 1.0f, 0.75f);
			float iconScaling = Math.min(ICON_SCALE, ICON_SCALE * (angleSpacing / MIN_SCALE_ANGLE)) * expandingScale;
			for (E obj : sortedList) {
				TextureRegion icon = getIcon(obj);
				if (!blinkIcon(obj)){
					batch.draw(icon, center.x + drawOffsets.x - (icon.getRegionWidth() / 2), center.y + drawOffsets.y - (icon.getRegionHeight() / 2),
							icon.getRegionWidth() / 2, icon.getRegionHeight() / 2, icon.getRegionWidth(), icon.getRegionHeight(),
							iconScaling, iconScaling, 0);
				}
				else if (blinkTime <= 2){
					batch.setColor(1.0f, 1.0f, 1.0f, 0.3f);
					batch.draw(icon, center.x + drawOffsets.x - (icon.getRegionWidth() / 2), center.y + drawOffsets.y - (icon.getRegionHeight() / 2),
							icon.getRegionWidth() / 2, icon.getRegionHeight() / 2, icon.getRegionWidth(), icon.getRegionHeight(),
							iconScaling, iconScaling, 0);
					batch.setColor(1.0f, 1.0f, 1.0f, 0.75f);
				}
				drawOffsets.rotate(angleSpacing);
			}
			batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		}
	}
	
	public abstract TextureRegion getIcon(E obj);
	public abstract ArrayList<E> getList();
	public abstract boolean blinkIcon(E obj);
	public abstract Vector2 getCenter();
	
	public void rotate(int direction) {
		if (!isRotating() && !isExpanding()) {
			int listSize = getList().size();
			if (direction < 0) {
				rotating = (int)Math.min(ROTATING_TIME, ROTATING_TIME * ((360f / listSize) / MIN_SCALE_ANGLE));
				selection = (selection + 1) % listSize;
			}
			else if (direction > 0) {
				rotating = -(int)Math.min(ROTATING_TIME, ROTATING_TIME * ((360f / listSize) / MIN_SCALE_ANGLE));
				selection = (selection + listSize - 1) % listSize;
			}
		}
	}
	
	public boolean inView() {
		Vector2 center = getCenter();
		return getStage().getCamera().frustum.boundsInFrustum(center.x, center.y, 0,
				RADIUS, RADIUS, 0);
	}
	
	@Override
	public boolean remove() {
		closing = false;
		expanding = 0;
		rotating = 0;
		markForRemoval = false;
		
		return super.remove();
	}
	
	public void closeMenu() {
		closing = true;
	}
	
	public void beginExpanding() {
		closing = false;
		expanding = EXPANDING_TIME;
	}
	
	public boolean isMenuActive() {
		return (getStage() != null && !closing);
	}
	
	public int getSelection() {
		return selection;
	}
	
	public boolean isExpanding() {
		return (expanding != 0);
	}
	
	public boolean isRotating() {
		return (rotating != 0);
	}
	
}
