package com.abyad.actor.ui;

import java.util.ArrayList;

import com.abyad.actor.attack.AttackData;
import com.abyad.game.Player;
import com.abyad.utils.Assets;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class SpecialSelectMenu extends ScrollSelectionMenu<String>{

	private WeaponSelectMenu weaponSelect;
	private Player player;
	
	private boolean rSwapHeld = true;
	private boolean lSwapHeld = true;
	
	private static final float SPACING = 100f;
	private static final float FONT_SCALE = 0.6f;
	
	private static BitmapFont font = Assets.manager.get(Assets.font);
	
	private GlyphLayout specialName;
	
	public SpecialSelectMenu(Player player, WeaponSelectMenu weaponSelect) {
		super(2.0f, 54);
		this.player = player;
		this.weaponSelect = weaponSelect;
		specialName = new GlyphLayout();
		//selection = weapons.indexOf(player.getWeapon());
		
		font.getData().setScale(FONT_SCALE);
		specialName.setText(font, AttackData.specialAttacks.get(getList().get(selection)).getName());
		font.getData().setScale(1.0f);
	}
	
	@Override
	public void act(float delta) {
		if (player.getController().rightPressed() > 0.75 && !rSwapHeld && !weaponSelect.isReadying()) {
			select(1);
		}
		else if (player.getController().leftPressed() > 0.75 && !lSwapHeld && !weaponSelect.isReadying()) {
			select(-1);
		}
		
		rSwapHeld = player.getController().rightPressed() > 0.75;
		lSwapHeld = player.getController().leftPressed() > 0.75;
		super.act(delta);
	}
	
	@Override
	public void draw(Batch batch, float a) {
		super.draw(batch, a);
		if (inView()) {
			Vector2 center = getCenter();
			
			font.getData().setScale(FONT_SCALE);
			float fontX = center.x - (specialName.width / 2);
			float fontY = center.y + (specialName.height / 2);
			font.draw(batch, specialName, fontX, fontY);
			font.getData().setScale(1.0f);
		}
	}
	
	@Override
	public TextureRegion getIcon(String obj) {
		return null;
	}

	@Override
	public ArrayList<String> getList() {
		return WeaponSelectMenu.weaponsToSpecials.get(player.getWeapon());
	}

	@Override
	public Vector2 getCenter() {
		Vector2 center = weaponSelect.getCenter();
		return new Vector2(center.x, center.y - SPACING);
	}
	
	@Override
	public void setSelection(int selection) {
		super.setSelection(selection);
		font.getData().setScale(FONT_SCALE);
		specialName.setText(font, AttackData.specialAttacks.get(getList().get(selection)).getName());
		font.getData().setScale(1.0f);
		player.changeSelectedSpecial(getList().get(selection));
	}
	
	public void resetStatus() {
		rSwapHeld = true;
		lSwapHeld = true;
	}
	
	@Override
	public boolean inView() {
		return weaponSelect.inView();
	}
}
