package com.abyad.table;

import java.util.ArrayList;

import com.abyad.game.Player;
import com.abyad.sprite.AbstractSpriteSheet;
import com.abyad.sprite.BarSprite;
import com.abyad.utils.Assets;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Frustum;
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
	}
	
	@Override
	public void draw(Batch batch, float a) {
		TextureRegion playerIcon = player.getCharacter().getSprite().getSprite("head");
		batch.draw(playerIcon, getX(), getY(), 0, 0,
				playerIcon.getRegionWidth(), playerIcon.getRegionHeight(), iconScale, iconScale, 0);
		
		ArrayList<TextureRegion> healthBars = healthSprite.getBarTextures(player.getCharacter().getHP(), 0, player.getCharacter().getMaxHP());
		ArrayList<TextureRegion> manaBars = manaSprite.getBarTextures(player.getCharacter().getMana(),
				player.getCharacter().getPartialMana(), player.getCharacter().getMaxMana());
		
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
		
		TextureRegion gold = AbstractSpriteSheet.spriteSheets.get("PICKUPS").getSprite("GOLD");
		batch.draw(gold, getX() + goldIconXOffset, getY() + goldIconYOffset,
				0, 0, gold.getRegionWidth(), gold.getRegionHeight(), goldScale, goldScale, 0);
		font.draw(batch, "x" + player.getCharacter().getGold(), getX() + goldTextXOffset, getY() + goldTextYOffset);
		//font.draw
	}
}
