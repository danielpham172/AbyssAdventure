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
	private static final float ICON_SCALE = 1.0f;
	private static final float FONT_SCALE = 0.2f;
	private static BitmapFont font = Assets.manager.get(Assets.font);
	
	private static TextureRegion magicSelectCursor = AbstractSpriteSheet.spriteSheets.get("MAGIC_SELECTION").getSprite("SELECTION");
	
	private Player player;
	private int selection;
	
	private int rotating;
	
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
		setPosition(player.getCharacter().getCenterX(), player.getCharacter().getCenterY());
		if (markForRemoval) {
			remove();
			markForRemoval = false;
			rotating = 0;
		}
		blinkTime = (blinkTime + 1) % 6;
	}
	
	@Override
	public void draw(Batch batch, float a) {
		ArrayList<AbstractMagic> magicList = player.getCharacter().getMagicSpells();
		Vector2 center = new Vector2(player.getCharacter().getCenter());
		Vector2 drawOffsets = new Vector2(0, RADIUS);
		float angleSpacing = 360f / magicList.size();
		if (rotating != 0) {
			float offsetRotation = angleSpacing * ((float)rotating / (float)ROTATING_TIME);
			drawOffsets.rotate(offsetRotation);
		}
		ArrayList<AbstractMagic> sortedMagicList = new ArrayList<AbstractMagic>();
		for (int i = selection; i < magicList.size(); i++) {
			sortedMagicList.add(magicList.get(i));
		}
		for (int i = 0; i < selection; i++) {
			sortedMagicList.add(magicList.get(i));
		}
		
		batch.setColor(1.0f, 1.0f, 1.0f, 0.75f);
		for (AbstractMagic magic : sortedMagicList) {
			TextureRegion icon = magic.getIcon();
			if (player.getCharacter().getMana() >= magic.getManaCost()) {
				batch.draw(icon, center.x + drawOffsets.x - (icon.getRegionWidth() / 2), center.y + drawOffsets.y - (icon.getRegionHeight() / 2),
						icon.getRegionWidth() / 2, icon.getRegionHeight() / 2, icon.getRegionWidth(), icon.getRegionHeight(),
						ICON_SCALE, ICON_SCALE, 0);
			}
			else if (blinkTime <= 2){
				batch.setColor(1.0f, 1.0f, 1.0f, 0.3f);
				batch.draw(icon, center.x + drawOffsets.x - (icon.getRegionWidth() / 2), center.y + drawOffsets.y - (icon.getRegionHeight() / 2),
						icon.getRegionWidth() / 2, icon.getRegionHeight() / 2, icon.getRegionWidth(), icon.getRegionHeight(),
						ICON_SCALE, ICON_SCALE, 0);
				batch.setColor(1.0f, 1.0f, 1.0f, 0.75f);
			}
			drawOffsets.rotate(angleSpacing);
		}
		batch.draw(magicSelectCursor, center.x - (magicSelectCursor.getRegionWidth() / 2), center.y + RADIUS - (magicSelectCursor.getRegionHeight() / 2),
					magicSelectCursor.getRegionWidth() / 2, magicSelectCursor.getRegionHeight() / 2, magicSelectCursor.getRegionWidth(), magicSelectCursor.getRegionHeight(),
					ICON_SCALE, ICON_SCALE, 0);
		if (rotating == 0) {
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
	
	public void rotate(int direction) {
		if (rotating == 0) {
			if (direction < 0) {
				rotating = ROTATING_TIME;
				selection = (selection + 1) % player.getCharacter().getMagicSpells().size();
			}
			else if (direction > 0) {
				rotating = -ROTATING_TIME;
				selection = (selection + player.getCharacter().getMagicSpells().size() - 1) % player.getCharacter().getMagicSpells().size();
			}
			AbstractMagic magic = player.getCharacter().getMagicSpells().get(selection);
			font.getData().setScale(FONT_SCALE);
			magicName.setText(font, magic.getName());
			magicDesc.setText(font, "Cost: " + magic.getManaCost());
			font.getData().setScale(1.0f);
		}
	}
	
	public void setToRemove() {
		markForRemoval = true;
	}
	
	public int getSelection() {
		return selection;
	}
}
