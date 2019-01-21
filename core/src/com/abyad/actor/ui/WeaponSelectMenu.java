package com.abyad.actor.ui;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.abyad.game.Player;
import com.abyad.sprite.AbstractSpriteSheet;
import com.abyad.utils.Assets;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Vector2;

public class WeaponSelectMenu extends ScrollSelectionMenu<String>{

	public static ArrayList<String> weapons = new ArrayList<String>();
	public static LinkedHashMap<String, ArrayList<String>> weaponsToSpecials = new LinkedHashMap<String, ArrayList<String>>();
	
	static {
		weapons.add("SWORD");
		weaponsToSpecials.put("SWORD", new ArrayList<String>());
		weaponsToSpecials.get("SWORD").add("SPIN_SLASH");
		weaponsToSpecials.get("SWORD").add("WIND_BLADE");
		
		weapons.add("STAFF");
		weaponsToSpecials.put("STAFF", new ArrayList<String>());
		weaponsToSpecials.get("STAFF").add("MEDITATE");
	}
	
	private Player player;
	private SpecialSelectMenu specialSelect;
	
	private boolean rSwapHeld = true;
	private boolean lSwapHeld = true;
	private boolean attackHeld = true;
	
	private boolean isReady;
	private int readyTime;
	
	private static final float OFFSET = 30f;
	
	private static final float FONT_SCALE = 1.0f;
	private static final float SPACING = 60f;
	
	private static BitmapFont font = Assets.manager.get(Assets.font);
	
	private GlyphLayout weaponName;
	private static GlyphLayout readyText;
	
	static {
		readyText = new GlyphLayout();
		font.getData().setScale(FONT_SCALE * 2.0f);
		font.setColor(0.0f, 1.0f, 0.0f, 0.75f);
		readyText.setText(font, "READY");
		font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		font.getData().setScale(1.0f);
	}
	
	public WeaponSelectMenu(Player player) {
		super(6.0f, 18);
		this.player = player;
		weaponName = new GlyphLayout();
		selection = weapons.indexOf(player.getWeapon());
		specialSelect = new SpecialSelectMenu(player, this);
		
		font.getData().setScale(FONT_SCALE);
		weaponName.setText(font, getList().get(selection));
		font.getData().setScale(1.0f);
	}
	
	@Override
	public void act(float delta) {
		if (player.isActive()) {
			if (isReady) {
				readyTime++;
			}
			else {
				readyTime = 0;
			}
			
			if (player.getController().rightSwapPressed() && !rSwapHeld && !isReady) {
				select(1);
				
			}
			else if (player.getController().leftSwapPressed() && !lSwapHeld && !isReady) {
				select(-1);
			}
			else if (player.getController().attackPressed() && !attackHeld) {
				isReady = !isReady;
			}
			
			rSwapHeld = player.getController().rightSwapPressed();
			lSwapHeld = player.getController().leftSwapPressed();
			attackHeld = player.getController().attackPressed();
			specialSelect.act(delta);
			super.act(delta);
		}
	}
	
	@Override
	public void draw(Batch batch, float a) {
		if (player.isActive()) {
			super.draw(batch, a);
			if (inView()) {
				specialSelect.draw(batch, a);
				Vector2 center = getCenter();
				
				font.getData().setScale(FONT_SCALE);
				float fontX = center.x - (weaponName.width / 2);
				float fontY = center.y - SPACING + (weaponName.height / 2);
				font.draw(batch, weaponName, fontX, fontY);
				font.getData().setScale(1.0f);
				if (isReady) {
					font.getData().setScale(FONT_SCALE * 2.0f);
					float readyX = center.x - (readyText.width / 2);
					float readyY = center.y + (readyText.height / 2) - OFFSET;
					font.draw(batch, readyText, readyX, readyY);
					font.getData().setScale(1.0f);
				}
			}
		}
	}
	
	@Override
	public TextureRegion getIcon(String obj) {
		return AbstractSpriteSheet.spriteSheets.get("WEAPON_ICONS").getSprite(obj);
	}

	@Override
	public ArrayList<String> getList() {
		return weapons;
	}

	@Override
	public Vector2 getCenter() {
		Frustum frustum = getStage().getCamera().frustum;
		float minX = frustum.planePoints[0].x;
		float minY = frustum.planePoints[0].y;
		float maxX = frustum.planePoints[2].x;
		float maxY = frustum.planePoints[2].y;
		
		float midX = (minX + maxX) / 2;
		float midY = (minY + maxY) / 2;
		
		if (player.getNumber() == 1) {
			return new Vector2((midX + minX) / 2, (midY + maxY) / 2 + OFFSET); 
		}
		else if (player.getNumber() == 2) {
			return new Vector2((midX + maxX) / 2, (midY + maxY) / 2 + OFFSET); 
		}
		else if (player.getNumber() == 3) {
			return new Vector2((midX + minX) / 2, (midY + minY) / 2 + OFFSET); 
		}
		else if (player.getNumber() == 4) {
			return new Vector2((midX + maxX) / 2, (midY + minY) / 2 + OFFSET); 
		}
		return null;
	}
	
	@Override
	public void setSelection(int selection) {
		super.setSelection(selection);
		font.getData().setScale(FONT_SCALE);
		weaponName.setText(font, getList().get(selection));
		font.getData().setScale(1.0f);
		player.changeSelectedWeapon(weapons.get(selection));
		specialSelect.setSelection(0);
	}
	
	public boolean isReady() {
		return (isReady && readyTime > 60) || (!player.isActive());
	}
	
	public boolean isReadying() {
		return isReady || (!player.isActive());
	}
	
	public void resetStatus() {
		rSwapHeld = true;
		lSwapHeld = true;
		attackHeld = true;
		isReady = false;
		readyTime = 0;
		specialSelect.resetStatus();
	}
}
