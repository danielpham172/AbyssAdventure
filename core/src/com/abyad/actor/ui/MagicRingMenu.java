package com.abyad.actor.ui;

import java.util.ArrayList;

import com.abyad.game.Player;
import com.abyad.magic.AbstractMagic;
import com.abyad.sprite.AbstractSpriteSheet;
import com.abyad.utils.Assets;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MagicRingMenu extends Actor{

	private static final float RADIUS = 30f;
	private static final float SPACING = 2f;
	private static final int ROTATING_TIME = 20;
	private static final int EXPANDING_TIME = 20;
	private static final float ICON_SCALE = 1.0f;
	private static final float FONT_SCALE = 0.2f;
	
	private static final float MIN_SCALE_ANGLE = 30f;
	
	private static BitmapFont font = Assets.manager.get(Assets.font);
	
	private static TextureRegion magicSelectCursor = AbstractSpriteSheet.spriteSheets.get("MAGIC_SELECTION").getSprite("SELECTION");
	
	private Player player;
	private int selection;
	
	private int rotating;
	private int expanding;
	private boolean closing;
	
	private boolean markForRemoval;
	private int blinkTime;
	
	private GlyphLayout magicName;
	private GlyphLayout magicDesc;
	
	public MagicRingMenu(Player player) {
		this.player = player;
		magicName = new GlyphLayout();
		magicDesc = new GlyphLayout();
		
		if (!player.getCharacter().getMagicSpells().isEmpty()) {
			AbstractMagic magic = player.getCharacter().getMagicSpells().get(selection);
			font.getData().setScale(FONT_SCALE);
			magicName.setText(font, magic.getName());
			magicDesc.setText(font, "Cost: " + magic.getManaCost());
			font.getData().setScale(1.0f);
		}
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
		setPosition(player.getCharacter().getCenterX(), player.getCharacter().getCenterY());
		if (markForRemoval) {
			remove();
		}
		blinkTime = (blinkTime + 1) % 6;
	}
	
	@Override
	public void draw(Batch batch, float a) {
		if (player.getCharacter().inView()) {
			ArrayList<AbstractMagic> magicList = player.getCharacter().getMagicSpells();
			Vector2 center = new Vector2(player.getCharacter().getCenter());
			float expandingScale = (1 - ((float)expanding / EXPANDING_TIME));
			Vector2 drawOffsets = new Vector2(0, RADIUS * expandingScale);
			float angleSpacing = 360f / magicList.size();
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
			ArrayList<AbstractMagic> sortedMagicList = new ArrayList<AbstractMagic>();
			for (int i = selection; i < magicList.size(); i++) {
				sortedMagicList.add(magicList.get(i));
			}
			for (int i = 0; i < selection; i++) {
				sortedMagicList.add(magicList.get(i));
			}
			
			batch.setColor(1.0f, 1.0f, 1.0f, 0.75f);
			float iconScaling = Math.min(ICON_SCALE, ICON_SCALE * (angleSpacing / MIN_SCALE_ANGLE)) * expandingScale;
			for (AbstractMagic magic : sortedMagicList) {
				TextureRegion icon = magic.getIcon();
				if (player.getCharacter().getMana() >= magic.getManaCost()) {
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
			if (expanding == 0) {
				batch.draw(magicSelectCursor, center.x - (magicSelectCursor.getRegionWidth() / 2), center.y + RADIUS - (magicSelectCursor.getRegionHeight() / 2),
						magicSelectCursor.getRegionWidth() / 2, magicSelectCursor.getRegionHeight() / 2, magicSelectCursor.getRegionWidth(), magicSelectCursor.getRegionHeight(),
						iconScaling, iconScaling, 0);
			}
			if (rotating == 0 && (expanding == 0)) {
				font.getData().setScale(FONT_SCALE);
				float fontX = center.x - (magicDesc.width / 2);
				float fontY = center.y + RADIUS + SPACING + 16;
				font.draw(batch, magicDesc, fontX, fontY);
				
				fontX = center.x - (magicName.width / 2);
				fontY += magicDesc.height + SPACING;
				font.draw(batch, magicName, fontX, fontY);
				font.getData().setScale(1.0f);
			}
			batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		}
	}
	
	public void rotate(int direction) {
		if (rotating == 0 && expanding == 0) {
			int listSize = player.getCharacter().getMagicSpells().size();
			if (direction < 0) {
				rotating = (int)Math.min(ROTATING_TIME, ROTATING_TIME * ((360f / listSize) / MIN_SCALE_ANGLE));
				selection = (selection + 1) % listSize;
			}
			else if (direction > 0) {
				rotating = -(int)Math.min(ROTATING_TIME, ROTATING_TIME * ((360f / listSize) / MIN_SCALE_ANGLE));
				selection = (selection + listSize - 1) % listSize;
			}
			AbstractMagic magic = player.getCharacter().getMagicSpells().get(selection);
			font.getData().setScale(FONT_SCALE);
			magicName.setText(font, magic.getName());
			magicDesc.setText(font, "Cost: " + magic.getManaCost());
			font.getData().setScale(1.0f);
		}
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
}
