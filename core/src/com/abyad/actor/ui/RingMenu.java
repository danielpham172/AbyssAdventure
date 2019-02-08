package com.abyad.actor.ui;

import java.util.ArrayList;

import com.abyad.sprite.AbstractSpriteSheet;
import com.abyad.utils.Assets;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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
	
	protected final TextureRegion CURSOR;
	
	protected GlyphLayout mainText;
	protected GlyphLayout subText;
	protected float FONT_SCALE;
	protected float SPACING;
	
	private static BitmapFont font = Assets.manager.get(Assets.font);
	
	public RingMenu() {
		RADIUS = 30f;
		ROTATING_TIME = 20;
		EXPANDING_TIME = 20;
		ICON_SCALE = 1.0f;
		MIN_SCALE_ANGLE = 30f;
		CURSOR = AbstractSpriteSheet.spriteSheets.get("MAGIC_SELECTION").getSprite("SELECTION"); 
		
		mainText = new GlyphLayout();
		subText = new GlyphLayout();
		FONT_SCALE = 0.2f;
		SPACING = 2f;
	}
	
	public RingMenu(float radius, int rotatingTime, int expandingTime, float iconScale, float minScaleAngle, TextureRegion cursor) {
		RADIUS = radius;
		ROTATING_TIME = rotatingTime;
		EXPANDING_TIME = expandingTime;
		ICON_SCALE = iconScale;
		MIN_SCALE_ANGLE = minScaleAngle;
		CURSOR = cursor;
		
		mainText = new GlyphLayout();
		subText = new GlyphLayout();
		FONT_SCALE = 0.2f;
		SPACING = 2f;
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
			//Draws rotating wheel
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
			//Draws cursor
			if (expanding == 0) {
				batch.draw(CURSOR, center.x - (CURSOR.getRegionWidth() / 2), center.y + RADIUS - (CURSOR.getRegionHeight() / 2),
						CURSOR.getRegionWidth() / 2, CURSOR.getRegionHeight() / 2, CURSOR.getRegionWidth(), CURSOR.getRegionHeight(),
						iconScaling, iconScaling, 0);
			}
			//Draws text
			if (rotating == 0 && expanding == 0 && usesText()) {
				font.getData().setScale(FONT_SCALE);
				float fontX = center.x - (subText.width / 2);
				float fontY = center.y + RADIUS + SPACING + 16;
				font.draw(batch, subText, fontX, fontY);
				
				fontX = center.x - (mainText.width / 2);
				fontY += subText.height + SPACING;
				font.draw(batch, mainText, fontX, fontY);
				font.getData().setScale(1.0f);
			}
			batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		}
	}
	
	public abstract TextureRegion getIcon(E obj);
	public abstract ArrayList<E> getList();
	public abstract boolean blinkIcon(E obj);
	public abstract Vector2 getCenter();
	
	public boolean usesText() {
		return true;
	}
	
	public abstract String getMainText();
	public abstract String getSubText();
	
	public void updateText() {
		font.getData().setScale(FONT_SCALE);
		mainText.setText(font, getMainText());
		subText.setText(font, getSubText());
		font.getData().setScale(1.0f);
	}
	
	public void rotate(int direction) {
		if (!isRotating() && !isExpanding()) {
			int listSize = getList().size();
			if (direction < 0) {
				rotating = (int)Math.min(ROTATING_TIME, ROTATING_TIME * ((360f / listSize) / MIN_SCALE_ANGLE));
				setSelection((selection + 1) % listSize);
			}
			else if (direction > 0) {
				rotating = -(int)Math.min(ROTATING_TIME, ROTATING_TIME * ((360f / listSize) / MIN_SCALE_ANGLE));
				setSelection((selection + listSize - 1) % listSize);
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
	
	public void setSelection(int selection) {
		this.selection = selection;
		if (usesText()) {
			updateText();
		}
	}
	
	public boolean isExpanding() {
		return (expanding != 0);
	}
	
	public boolean isRotating() {
		return (rotating != 0);
	}
	
}
