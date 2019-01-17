package com.abyad.actor.ui;

import java.util.ArrayList;

import com.abyad.actor.tile.AbstractTile;
import com.abyad.game.Player;
import com.abyad.magic.AbstractMagic;
import com.abyad.sprite.AbstractSpriteSheet;
import com.abyad.utils.Assets;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Vector2;

public class CharacterSelectMenu extends ScrollSelectionMenu<String>{

	private Player player;
	
	private boolean rSwapHeld = true;
	private boolean lSwapHeld = true;
	private boolean attackHeld = true;
	private boolean specialHeld = true;
	
	private boolean isReady;
	private int readyTime;
	
	private static final float FONT_SCALE = 1.0f;
	private static final float SPACING = 60f;
	
	private static BitmapFont font = Assets.manager.get(Assets.font);
	
	private GlyphLayout characterName;
	private static GlyphLayout readyText;
	
	static {
		readyText = new GlyphLayout();
		font.getData().setScale(FONT_SCALE * 2.0f);
		font.setColor(0.0f, 1.0f, 0.0f, 0.75f);
		readyText.setText(font, "READY");
		font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		font.getData().setScale(1.0f);
	}
	
	public CharacterSelectMenu(Player player) {
		super(6.0f, 18);
		this.player = player;
		characterName = new GlyphLayout();
		selection = Player.characterNames.indexOf(player.getCharacterName());
		
		font.getData().setScale(FONT_SCALE);
		characterName.setText(font, getList().get(selection));
		font.getData().setScale(1.0f);
	}
	
	@Override
	public void act(float delta) {
		if (isReady || !player.isActive()) {
			readyTime++;
		}
		else {
			readyTime = 0;
		}
		
		if (player.isActive()) {
			if (player.getController().rightSwapPressed() && !rSwapHeld && !isReady) {
				select(1);
			}
			else if (player.getController().leftSwapPressed() && !lSwapHeld && !isReady) {
				select(-1);
			}
			else if (player.getController().attackPressed() && !attackHeld) {
				isReady = !isReady;
			}
			else if (player.getController().specialPressed() && !specialHeld) {
				player.setActive(false);
			}
		}
		else {
			if (player.getController().attackPressed() && !attackHeld) {
				player.setActive(true);
			}
		}
		
		rSwapHeld = player.getController().rightSwapPressed();
		lSwapHeld = player.getController().leftSwapPressed();
		attackHeld = player.getController().attackPressed();
		specialHeld = player.getController().specialPressed();
		super.act(delta);
	}
	
	@Override
	public void draw(Batch batch, float a) {
		if (inView()) {
			Vector2 center = getCenter();
			
			if (player.isActive()) {
				super.draw(batch, a);
				font.getData().setScale(FONT_SCALE);
				float fontX = center.x - (characterName.width / 2);
				float fontY = center.y - SPACING + (characterName.height / 2);
				font.draw(batch, characterName, fontX, fontY);
				font.getData().setScale(1.0f);
				if (isReady) {
					font.getData().setScale(FONT_SCALE * 2.0f);
					float readyX = center.x - (readyText.width / 2);
					float readyY = center.y + (readyText.height / 2);
					font.draw(batch, readyText, readyX, readyY);
					font.getData().setScale(1.0f);
				}
			}
			else {
				TextureRegion aButton = AbstractSpriteSheet.spriteSheets.get("UI_BUTTONS").getSprite("A");
				float xOrigin = aButton.getRegionWidth() / 2;
				float yOrigin = aButton.getRegionHeight() / 2;
				batch.draw(aButton, center.x - xOrigin, center.y - yOrigin, xOrigin, yOrigin, aButton.getRegionHeight(),
						aButton.getRegionWidth(), ICON_SCALE, ICON_SCALE, 0);
			}
		}
	}
	
	@Override
	public TextureRegion getIcon(String obj) {
		return AbstractSpriteSheet.spriteSheets.get(obj).getSprite("head");
	}

	@Override
	public ArrayList<String> getList() {
		return Player.characterNames;
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
			return new Vector2((midX + minX) / 2, (midY + maxY) / 2); 
		}
		else if (player.getNumber() == 2) {
			return new Vector2((midX + maxX) / 2, (midY + maxY) / 2); 
		}
		else if (player.getNumber() == 3) {
			return new Vector2((midX + minX) / 2, (midY + minY) / 2); 
		}
		else if (player.getNumber() == 4) {
			return new Vector2((midX + maxX) / 2, (midY + minY) / 2); 
		}
		return null;
	}
	
	@Override
	public void setSelection(int selection) {
		super.setSelection(selection);
		font.getData().setScale(FONT_SCALE);
		characterName.setText(font, getList().get(selection));
		font.getData().setScale(1.0f);
		player.changeSelectedCharacter(selection);
	}
	
	public boolean isReady() {
		return (isReady || !player.isActive()) && readyTime > 60;
	}
	
	public void resetStatus() {
		rSwapHeld = true;
		lSwapHeld = true;
		attackHeld = true;
		specialHeld = true;
		isReady = false;
		readyTime = 0;
	}

}
