package com.abyad.actor.ui;

import java.util.ArrayList;

import com.abyad.game.Player;
import com.abyad.magic.AbstractMagic;
import com.abyad.sprite.AbstractSpriteSheet;
import com.abyad.utils.Assets;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

public class MagicSelectMenu extends ScrollSelectionMenu<String>{

	private static ArrayList<String> magicNamesList = new ArrayList<String>();
	static {
		for (String key : AbstractMagic.magicList.keySet()) {
			magicNamesList.add(key);
		}
	}
	
	private Player player;
	
	private boolean rSwapHeld = true;
	private boolean lSwapHeld = true;
	private boolean attackHeld = true;
	
	private boolean isReady;
	private int readyTime;
	
	private static final float FONT_SCALE = 1.0f;
	private static final float DESCRIPTION_SCALE = 0.5f;
	private static final float SPACING = 60f;
	private static final float DESCRIPTION_SPACING = 50f;
	
	private static BitmapFont font = Assets.manager.get(Assets.font);
	
	private GlyphLayout magicName;
	private GlyphLayout magicDescription;
	private static GlyphLayout readyText;
	
	static {
		readyText = new GlyphLayout();
		font.getData().setScale(FONT_SCALE * 2.0f);
		font.setColor(0.0f, 1.0f, 0.0f, 0.75f);
		readyText.setText(font, "READY");
		font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		font.getData().setScale(1.0f);
	}
	
	public MagicSelectMenu(Player player) {
		super(6.0f, 18);
		this.player = player;
		magicName = new GlyphLayout();
		magicDescription = new GlyphLayout();
		setSelection(0);
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
		}
		
		rSwapHeld = player.getController().rightSwapPressed();
		lSwapHeld = player.getController().leftSwapPressed();
		attackHeld = player.getController().attackPressed();
		super.act(delta);
	}
	
	@Override
	public void draw(Batch batch, float a) {
		if (inView()) {
			Vector2 center = getCenter();
			
			if (player.isActive()) {
				super.draw(batch, a);
				font.getData().setScale(FONT_SCALE);
				float fontX = center.x - (magicName.width / 2);
				float fontY = center.y - SPACING + (magicName.height / 2);
				font.draw(batch, magicName, fontX, fontY);
				
				font.getData().setScale(FONT_SCALE * DESCRIPTION_SCALE);
				fontX = center.x - (magicDescription.width / 2);
				fontY -= DESCRIPTION_SPACING - (magicName.height / 2) - (magicDescription.height / 2);
				font.draw(batch, magicDescription, fontX, fontY);

				font.getData().setScale(1.0f);
				if (isReady) {
					font.getData().setScale(FONT_SCALE * 2.0f);
					float readyX = center.x - (readyText.width / 2);
					float readyY = center.y + (readyText.height / 2);
					font.draw(batch, readyText, readyX, readyY);
					font.getData().setScale(1.0f);
				}
			}
		}
	}
	
	@Override
	public TextureRegion getIcon(String obj) {
		return AbstractMagic.magicList.get(obj).getIcon();
	}

	@Override
	public ArrayList<String> getList() {
		return magicNamesList;
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
		magicName.setText(font, AbstractMagic.magicList.get(getList().get(selection)).getName());
		font.getData().setScale(DESCRIPTION_SCALE * FONT_SCALE);
		String desc = AbstractMagic.magicList.get(getList().get(selection)).getDescription();
		magicDescription.setText(font, desc);
		magicDescription.setText(font,  desc, 0, desc.length(), Color.WHITE, (float)Math.min(350.0f, magicDescription.width), Align.center, true, null);
		font.getData().setScale(1.0f);
		player.changeStartingSpell(AbstractMagic.magicList.get(getList().get(getSelection())));
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public boolean isReady() {
		return (isReady || !player.isActive()) && readyTime > 60;
	}
	
	public void resetStatus() {
		rSwapHeld = true;
		lSwapHeld = true;
		attackHeld = true;
		isReady = false;
		readyTime = 0;
	}

}
