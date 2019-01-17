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
	
	private static float goldIconXOffset = -8f;
	private static float goldIconYOffset = -20f;
	private static float goldScale = 1.5f;
	
	private static BitmapFont font = Assets.manager.get(Assets.font);
	private static float goldTextXOffset = 12f;
	private static float goldTextYOffset = -5f;
	private static float goldTextScale = 0.5f;
	
	private static float offsetFromTop = 58f;
	private static float offsetFromBottom = 30f;
	private static float offsetFromRight = 58f;
	private static float offsetFromLeft = 10f;
	
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
		if (player.isActive()) {
			Frustum frustum = getStage().getCamera().frustum;
			if (player.getNumber() == 1) {
				setPosition(frustum.planePoints[3].x + offsetFromLeft, frustum.planePoints[3].y - offsetFromTop);
			}
			if (player.getNumber() == 2) {
				setPosition(frustum.planePoints[2].x - offsetFromRight, frustum.planePoints[2].y - offsetFromTop);
			}
			if (player.getNumber() == 3) {
				setPosition(frustum.planePoints[0].x + offsetFromLeft, frustum.planePoints[0].y + offsetFromBottom);
			}
			if (player.getNumber() == 4) {
				setPosition(frustum.planePoints[1].x - offsetFromRight, frustum.planePoints[1].y + offsetFromBottom);
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
	}
	
	@Override
	public void draw(Batch batch, float a) {
		if (player.isActive()) {
			TextureRegion playerIcon = player.getCharacter().getSprite().getSprite("head");
			float shakeOffset = SHAKE_OFFSET[takingDamage % SHAKE_OFFSET.length] * iconScale;
			float floatOffset = JUMP_OFFSET[healingUp % JUMP_OFFSET.length] * iconScale * 2;
			batch.draw(playerIcon, getX() + shakeOffset, getY() + floatOffset, 0, 0,
					playerIcon.getRegionWidth(), playerIcon.getRegionHeight(), iconScale, iconScale, 0);
			
			ArrayList<TextureRegion> healthBars = healthSprite.getBarTextures(lastHP, lastPartialHP, player.getCharacter().getMaxHP());
			ArrayList<TextureRegion> manaBars = manaSprite.getBarTextures(lastMana, lastPartialMana, player.getCharacter().getMaxMana());
			
			if (player.getNumber() == 1 || player.getNumber() == 3) {
				for (int i = 0; i < healthBars.size(); i++) {
					float x = getX() + firstBarXOffset + (healthSprite.getWidthSpacing() * i * barScale);
					float y = getY() + firstBarYOffset;
					batch.draw(healthBars.get(i), x, y , 0, 0, healthBars.get(i).getRegionWidth(),
							healthBars.get(i).getRegionHeight(), barScale, barScale, 0);
				}
				
				for (int i = 0; i < manaBars.size(); i++) {
					float x = getX() + firstBarXOffset + (manaSprite.getWidthSpacing() * i * barScale);
					float y = getY() + firstBarYOffset + verticalSpacing;
					batch.draw(manaBars.get(i),  x, y , 0, 0, manaBars.get(i).getRegionWidth(),
							manaBars.get(i).getRegionHeight(), barScale, barScale, 0);
				}
				if (healthFlash) {
					float originX = healthBars.get(lastHP - 1).getRegionWidth() / 2;
					float originY = healthBars.get(lastHP - 1).getRegionHeight() / 2;
					float x = getX() + firstBarXOffset + (healthSprite.getWidthSpacing() * (lastHP - 1) * barScale) + (originX * (barScale - 1.0f));
					float y = getY() + firstBarYOffset + (originY * (barScale - 1.0f));
					batch.draw(healthBars.get(lastHP - 1) , x, y, originX, originY, healthBars.get(lastHP - 1).getRegionWidth(),
							healthBars.get(lastHP - 1).getRegionHeight(), barScale * FLASH_SCALE, barScale * FLASH_SCALE, 0);
				}
				if (manaFlash) {
					float originX = manaBars.get(lastMana - 1).getRegionWidth() / 2;
					float originY = manaBars.get(lastMana - 1).getRegionHeight() / 2;
					float x = getX() + firstBarXOffset + (manaSprite.getWidthSpacing() * (lastMana - 1) * barScale) + (originX * (barScale - 1.0f));
					float y = getY() + firstBarYOffset + (originY * (barScale - 1.0f) + verticalSpacing);
					batch.draw(manaBars.get(lastMana - 1), x, y , originX, originY, manaBars.get(lastMana - 1).getRegionWidth(),
							manaBars.get(lastMana - 1).getRegionHeight(), barScale * FLASH_SCALE, barScale * FLASH_SCALE, 0);
				}
			}
			else if (player.getNumber() == 2 || player.getNumber() == 4) {
				float firstHealthBarXOffset = firstBarXOffset + (healthBars.get(0).getRegionWidth() * barScale) - (playerIcon.getRegionWidth() * iconScale);
				float firstManaBarXOffset = firstBarXOffset + (manaBars.get(0).getRegionWidth() * barScale) - (playerIcon.getRegionWidth() * iconScale);
				for (int i = 0; i < healthBars.size(); i++) {
					float x = getX() - firstHealthBarXOffset - (healthSprite.getWidthSpacing() * (healthBars.size() - i - 1) * barScale);
					float y = getY() + firstBarYOffset;
					batch.draw(healthBars.get(i), x, y , 0, 0, healthBars.get(i).getRegionWidth(),
							healthBars.get(i).getRegionHeight(), barScale, barScale, 0);
				}
				
				for (int i = 0; i < manaBars.size(); i++) {
					float x = getX() - firstManaBarXOffset - (manaSprite.getWidthSpacing() * (manaBars.size() - i - 1) * barScale);
					float y = getY() + firstBarYOffset + verticalSpacing;
					batch.draw(manaBars.get(i),  x, y , 0, 0, manaBars.get(i).getRegionWidth(),
							manaBars.get(i).getRegionHeight(), barScale, barScale, 0);
				}
				if (healthFlash) {
					float originX = healthBars.get(lastHP - 1).getRegionWidth() / 2;
					float originY = healthBars.get(lastHP - 1).getRegionHeight() / 2;
					float x = getX() - firstHealthBarXOffset - (healthSprite.getWidthSpacing() * (healthBars.size() - lastHP) * barScale) + (originX * (barScale - 1.0f));
					float y = getY() + firstBarYOffset + (originY * (barScale - 1.0f));
					batch.draw(healthBars.get(lastHP - 1) , x, y, originX, originY, healthBars.get(lastHP - 1).getRegionWidth(),
							healthBars.get(lastHP - 1).getRegionHeight(), barScale * FLASH_SCALE, barScale * FLASH_SCALE, 0);
				}
				if (manaFlash) {
					float originX = manaBars.get(lastMana - 1).getRegionWidth() / 2;
					float originY = manaBars.get(lastMana - 1).getRegionHeight() / 2;
					float x = getX() - firstManaBarXOffset - (manaSprite.getWidthSpacing() * (manaBars.size() - lastMana) * barScale) + (originX * (barScale - 1.0f));
					float y = getY() + firstBarYOffset + (originY * (barScale - 1.0f)) + verticalSpacing;
					batch.draw(manaBars.get(lastMana - 1), x, y , originX, originY, manaBars.get(lastMana - 1).getRegionWidth(),
							manaBars.get(lastMana - 1).getRegionHeight(), barScale * FLASH_SCALE, barScale * FLASH_SCALE, 0);
				}
			}
			
			TextureRegion gold = AbstractSpriteSheet.spriteSheets.get("PICKUPS").getSprite("GOLD");
			batch.draw(gold, getX() + goldIconXOffset, getY() + goldIconYOffset,
					0, 0, gold.getRegionWidth(), gold.getRegionHeight(), goldScale, goldScale, 0);
			String goldText = "" + player.getCharacter().getGold();
			while (goldText.length() < 3) {
				goldText = "0" + goldText;
			}
			font.getData().setScale(goldTextScale);
			font.draw(batch, goldText, getX() + goldTextXOffset, getY() + goldTextYOffset);
			font.getData().setScale(1.0f);
			//font.draw
		}
	}
}
