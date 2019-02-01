package com.abyad.actor.ui;

import java.util.ArrayList;

import com.abyad.game.Player;
import com.abyad.magic.AbstractMagic;
import com.abyad.sprite.AbstractSpriteSheet;
import com.abyad.utils.Assets;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class MagicRingMenu extends RingMenu<AbstractMagic>{

	private static final float FONT_SCALE = 0.2f;
	private static final float SPACING = 2f;
	
	private static BitmapFont font = Assets.manager.get(Assets.font);
	
	private static TextureRegion magicSelectCursor = AbstractSpriteSheet.spriteSheets.get("MAGIC_SELECTION").getSprite("SELECTION");
	
	private GlyphLayout magicName;
	private GlyphLayout magicDesc;
	
	private Player player;
	
	public MagicRingMenu(Player player) {
		super();
		this.player = player;
		
		magicName = new GlyphLayout();
		magicDesc = new GlyphLayout();
		
		if (!player.getCharacter().getMagicSpells().isEmpty()) {
			setSelection(0);
		}
	}
	
	@Override
	public void draw(Batch batch, float a) {
		super.draw(batch, a);
		batch.setColor(1.0f, 1.0f, 1.0f, 0.75f);
		Vector2 center = getCenter();
		float expandingScale = (1 - ((float)expanding / EXPANDING_TIME));
		float angleSpacing = 360f / getList().size();
		float iconScaling = Math.min(ICON_SCALE, ICON_SCALE * (angleSpacing / MIN_SCALE_ANGLE)) * expandingScale;
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
	
	@Override
	public void rotate(int direction) {
		super.rotate(direction);
	}
	
	@Override
	public void setSelection(int selection) {
		super.setSelection(selection);
		AbstractMagic magic = getList().get(selection);
		font.getData().setScale(FONT_SCALE);
		magicName.setText(font, magic.getName());
		magicDesc.setText(font, "Cost: " + getManaCostText(magic.getManaCost(), magic.getPartialManaCost()));
		font.getData().setScale(1.0f);
	}
	
	@Override
	public TextureRegion getIcon(AbstractMagic obj) {
		return obj.getIcon();
	}

	@Override
	public ArrayList<AbstractMagic> getList() {
		return player.getCharacter().getMagicSpells();
	}

	@Override
	public boolean blinkIcon(AbstractMagic obj) {
		return (player.getCharacter().getMana() * 4) + player.getCharacter().getPartialMana() < (obj.getManaCost() * 4) + obj.getPartialManaCost();
	}

	@Override
	public Vector2 getCenter() {
		return player.getCharacter().getCenter();
	}
	
	public String getManaCostText(int mana, int partialMana) {
		if (partialMana == 0) return "" + mana;
		else {
			if (partialMana == 1) return mana + ".25";
			if (partialMana == 2) return mana + ".5";
			if (partialMana == 3) return mana + ".75";
		}
		return null;
	}

}
