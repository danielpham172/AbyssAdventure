package com.abyad.actor.ui;

import java.util.ArrayList;

import com.abyad.game.Player;
import com.abyad.sprite.AbstractSpriteSheet;
import com.abyad.sprite.BarSprite;
import com.abyad.utils.Assets;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PlayerDisplay extends Actor{

	private Player player;
	
	private BarSprite healthSprite;
	private BarSprite manaSprite;
	
	private static float iconScale = 3.0f;
	private static float barScale = 1.0f;
	private static float verticalSpacing = -18f;
	private static float firstBarXOffset = 50f;
	private static float firstBarYOffset = 26f;
	
	private static float goldIconXOffset = 0f;
	private static float goldIconYOffset = -20f;
	private static float goldScale = 1.0f;
	
	private static BitmapFont font = Assets.manager.get(Assets.font);
	private static float goldTextXOffset = 20f;
	private static float goldTextYOffset = -5f;
	
	private static float offsetFromTop = 58f;
	private static float offsetFromBottom = 30f;
	
	private static final int UP_DELAY_CONSTANT = 5;
	private static final int DOWN_DELAY_CONSTANT = 2;
	
	private int lastHP;
	private int lastPartialHP;
	private int healthAnimDelay;
	
	private int lastMana;
	private int lastPartialMana;
	private int manaAnimDelay;
	
	private int takingDamage;
	private static final float[] SHAKE_OFFSET =
		{0.0f, 1.0f, 2.0f, 1.0f, 0.0f, -1.0f, -2.0f, -1.0f};
	private int healingUp;
	private static final float[] JUMP_OFFSET =
		{0.0f, 0.19f, 0.36f, 0.51f, 0.64f, 0.75f, 0.84f, 0.91f, 0.96f, 1.0f,
				1.0f, 0.96f, 0.91f, 0.84f, 0.75f, 0.64f, 0.51f, 0.36f, 0.19f, 0.0f};
	private boolean manaFlash;
	private boolean healthFlash;
	private static final float FLASH_SCALE = 1.2f;
	
	public PlayerDisplay(Player player) {
		super();
		this.player = player;
		
		healthSprite = (BarSprite)AbstractSpriteSheet.spriteSheets.get("HEALTH_CELL");
		manaSprite = (BarSprite)AbstractSpriteSheet.spriteSheets.get("MANA_CELL");
	}
	
	@Override
	public void act(float delta) {
		Frustum frustum = getStage().getCamera().frustum;
		if (player.getNumber() == 1) {
			setPosition(frustum.planePoints[3].x + 10, frustum.planePoints[3].y - offsetFromTop);
		}
		if (player.getNumber() == 2) {
			setPosition(frustum.planePoints[0].x + 10, frustum.planePoints[0].y + offsetFromBottom);
		}
		
		if (takingDamage > 0) takingDamage--;
		if (healingUp > 0) healingUp--;
		if (healthAnimDelay == 0) {
			healthFlash = false;
			if (lastHP * 4 + lastPartialHP < player.getCharacter().getHP() * 4) {
				if (healingUp == 0) healingUp = UP_DELAY_CONSTANT * 4;
				takingDamage = 0;
				lastPartialHP++;
				if (lastPartialHP >= 4) {
					lastHP++;
					lastPartialHP -= 4;
					healthFlash = true;
				}
				healthAnimDelay = UP_DELAY_CONSTANT;
			}
			else if (lastHP * 4 + lastPartialHP > player.getCharacter().getHP() * 4) {
				if (takingDamage == 0) takingDamage = DOWN_DELAY_CONSTANT * 4;
				healingUp = 0;
				lastPartialHP--;
				if (lastPartialHP < 0) {
					lastHP--;
					lastPartialHP += 4;
				}
				healthAnimDelay = DOWN_DELAY_CONSTANT;
			}
		}
		else {
			healthAnimDelay--;
		}
		
		if (manaAnimDelay == 0) {
			manaFlash = false;
			if (lastMana * 4 + lastPartialMana < player.getCharacter().getMana() * 4 + player.getCharacter().getPartialMana()) {
				lastPartialMana++;
				if (lastPartialMana >= 4) {
					lastMana++;
					lastPartialMana -= 4;
					manaFlash = true;
				}
				manaAnimDelay = UP_DELAY_CONSTANT;
			}
			else if (lastMana * 4 + lastPartialMana > player.getCharacter().getMana() * 4 + player.getCharacter().getPartialMana()) {
				lastPartialMana--;
				if (lastPartialMana < 0) {
					lastMana--;
					lastPartialMana += 4;
				}
				manaAnimDelay = DOWN_DELAY_CONSTANT;
			}
		}
		else {
			manaAnimDelay--;
		}
	}
	
	@Override
	public void draw(Batch batch, float a) {
		TextureRegion playerIcon = player.getCharacter().getSprite().getSprite("head");
		float shakeOffset = SHAKE_OFFSET[takingDamage % SHAKE_OFFSET.length] * iconScale;
		float floatOffset = JUMP_OFFSET[healingUp % JUMP_OFFSET.length] * iconScale * 2;
		batch.draw(playerIcon, getX() + shakeOffset, getY() + floatOffset, 0, 0,
				playerIcon.getRegionWidth(), playerIcon.getRegionHeight(), iconScale, iconScale, 0);
		
		//ArrayList<TextureRegion> healthBars = healthSprite.getBarTextures(player.getCharacter().getHP(), 0, player.getCharacter().getMaxHP());
		//ArrayList<TextureRegion> manaBars = manaSprite.getBarTextures(player.getCharacter().getMana(),
		//		player.getCharacter().getPartialMana(), player.getCharacter().getMaxMana());
		
		ArrayList<TextureRegion> healthBars = healthSprite.getBarTextures(lastHP, lastPartialHP, player.getCharacter().getMaxHP());
		ArrayList<TextureRegion> manaBars = manaSprite.getBarTextures(lastMana, lastPartialMana, player.getCharacter().getMaxMana());
		
		for (int i = 0; i < healthBars.size(); i++) {
			batch.draw(healthBars.get(i), getX() + firstBarXOffset + (healthSprite.getWidthSpacing() * i * barScale),
					getY() + firstBarYOffset, 0, 0, healthBars.get(i).getRegionWidth(), healthBars.get(i).getRegionHeight(),
					barScale, barScale, 0);
		}
		
		for (int i = 0; i < manaBars.size(); i++) {
			batch.draw(manaBars.get(i), getX() + firstBarXOffset + (manaSprite.getWidthSpacing() * i * barScale),
					getY() + firstBarYOffset + verticalSpacing, 0, 0, manaBars.get(i).getRegionWidth(),
					manaBars.get(i).getRegionHeight(), barScale, barScale, 0);
		}
		if (healthFlash) {
			float originX = healthBars.get(lastHP - 1).getRegionWidth() / 2;
			float originY = healthBars.get(lastHP - 1).getRegionHeight() / 2;
			batch.draw(healthBars.get(lastHP - 1), getX() + firstBarXOffset + (healthSprite.getWidthSpacing() * (lastHP - 1) * barScale) + (originX * (barScale - 1.0f)),
					getY() + firstBarYOffset + (originY * (barScale - 1.0f)), originX, originY, healthBars.get(lastHP - 1).getRegionWidth(),
					healthBars.get(lastHP - 1).getRegionHeight(), barScale * FLASH_SCALE, barScale * FLASH_SCALE, 0);
		}
		if (manaFlash) {
			float originX = manaBars.get(lastMana - 1).getRegionWidth() / 2;
			float originY = manaBars.get(lastMana - 1).getRegionHeight() / 2;
			batch.draw(manaBars.get(lastMana - 1), getX() + firstBarXOffset + (manaSprite.getWidthSpacing() * (lastMana - 1) * barScale) + (originX * (barScale - 1.0f)),
					getY() + firstBarYOffset + verticalSpacing + (originY * (barScale - 1.0f)), originX, originY, manaBars.get(lastMana - 1).getRegionWidth(),
					manaBars.get(lastMana - 1).getRegionHeight(), barScale * FLASH_SCALE, barScale * FLASH_SCALE, 0);
		}
		
		TextureRegion gold = AbstractSpriteSheet.spriteSheets.get("PICKUPS").getSprite("GOLD");
		batch.draw(gold, getX() + goldIconXOffset, getY() + goldIconYOffset,
				0, 0, gold.getRegionWidth(), gold.getRegionHeight(), goldScale, goldScale, 0);
		font.draw(batch, "x" + player.getCharacter().getGold(), getX() + goldTextXOffset, getY() + goldTextYOffset);
		//font.draw
	}
}
