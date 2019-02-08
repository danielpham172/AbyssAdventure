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
	
	private Player player;
	
	public MagicRingMenu(Player player) {
		super();
		this.player = player;
		
		if (!player.getCharacter().getMagicSpells().isEmpty()) {
			setSelection(0);
		}
	}
	
	@Override
	public void rotate(int direction) {
		super.rotate(direction);
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
	
	@Override
	public String getMainText() {
		AbstractMagic magic = getList().get(selection);
		return magic.getName();
	}
	
	@Override
	public String getSubText() {
		AbstractMagic magic = getList().get(selection);
		return "Cost: " + getManaCostText(magic.getManaCost(), magic.getPartialManaCost());
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
