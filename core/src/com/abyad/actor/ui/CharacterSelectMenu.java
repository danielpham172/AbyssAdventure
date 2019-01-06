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
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Vector2;

public class CharacterSelectMenu extends ScrollSelectionMenu<String>{

	private Player player;
	
	private boolean rSwapHeld = true;
	private boolean lSwapHeld = true;
	private boolean attackHeld = true;
	
	private boolean isReady;
	
	private static final float FONT_SCALE = 1.0f;
	private static final float SPACING = 60f;
	
	private static BitmapFont font = Assets.manager.get(Assets.font);
	
	private GlyphLayout characterName;
	private static GlyphLayout readyText;
	
	static {
		readyText = new GlyphLayout();
		font.getData().setScale(FONT_SCALE * 2.0f);
		readyText.setText(font, "READY");
		font.getData().setScale(1.0f);
	}
	
	public CharacterSelectMenu(Player player) {
		super(6.0f);
		this.player = player;
		characterName = new GlyphLayout();
		selection = Player.characterNames.indexOf(player.getCharacterName());
		
		font.getData().setScale(FONT_SCALE);
		characterName.setText(font, getList().get(selection));
		font.getData().setScale(1.0f);
	}
	
	@Override
	public void act(float delta) {
		if (player.getController().rightSwapPressed() && !rSwapHeld && !isReady) {
			select(1);
			player.changeSelectedCharacter(selection);
		}
		else if (player.getController().leftSwapPressed() && !lSwapHeld && !isReady) {
			select(-1);
			player.changeSelectedCharacter(selection);
		}
		else if (player.getController().attackPressed() && !attackHeld) {
			isReady = !isReady;
		}
		
		rSwapHeld = player.getController().rightSwapPressed();
		lSwapHeld = player.getController().leftSwapPressed();
		attackHeld = player.getController().attackPressed();
		super.act(delta);
	}
	
	@Override
	public void draw(Batch batch, float a) {
		super.draw(batch, a);
		if (inView()) {
			Vector2 center = getCenter();
			
			font.getData().setScale(FONT_SCALE);
			float fontX = center.x - (characterName.width / 2);
			float fontY = center.y - SPACING;
			font.draw(batch, characterName, fontX, fontY);
			font.getData().setScale(1.0f);
			if (isReady()) {
				font.getData().setScale(FONT_SCALE * 2.0f);
				float readyX = center.x - (readyText.width / 2);
				float readyY = center.y + (readyText.height / 2);
				batch.setColor(0.0f, 1.0f, 0.0f, 0.75f);
				font.draw(batch, readyText, readyX, readyY);
				font.getData().setScale(1.0f);
				batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
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
			return new Vector2((midX + minX) / 2, (midY + minY) / 2); 
		}
		return null;
	}
	
	@Override
	public void select(int direction) {
		super.select(direction);
		font.getData().setScale(FONT_SCALE);
		characterName.setText(font, getList().get(selection));
		font.getData().setScale(1.0f);
	}
	
	public boolean isReady() {
		return isReady;
	}
	
	public void resetStatus() {
		rSwapHeld = true;
		lSwapHeld = true;
		attackHeld = true;
		isReady = false;
	}

}
