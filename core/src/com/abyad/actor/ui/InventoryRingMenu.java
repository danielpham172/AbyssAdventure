package com.abyad.actor.ui;

import java.util.ArrayList;

import com.abyad.game.Player;
import com.abyad.usables.InventoryItem;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class InventoryRingMenu extends RingMenu<InventoryItem>{

	private Player player;
	
	public InventoryRingMenu(Player player) {
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
	public TextureRegion getIcon(InventoryItem obj) {
		return obj.getTexture();
	}

	@Override
	public ArrayList<InventoryItem> getList() {
		return player.getCharacter().getItems();
	}

	@Override
	public boolean blinkIcon(InventoryItem obj) {
		return !obj.canUse(player.getCharacter());
	}

	@Override
	public Vector2 getCenter() {
		return player.getCharacter().getCenter();
	}
	
	@Override
	public boolean usesCount() {
		return true;
	}
	
	@Override
	public int getCount(InventoryItem obj) {
		return obj.getCount();
	}
	@Override
	public String getMainText() {
		if (getList().isEmpty()) {
			return "";
		}
		InventoryItem item = getList().get(selection);
		return item.getName();
	}
	
	@Override
	public String getSubText() {
		if (getList().isEmpty()) {
			return "";
		}
		InventoryItem item = getList().get(selection);
		return item.getDescription();
	}
}
