package com.abyad.actor.tile;

import java.util.ArrayList;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.interfaces.Interactable;
import com.abyad.magic.AbstractMagic;
import com.abyad.sprite.AbstractSpriteSheet;
import com.abyad.stage.TownStage;
import com.abyad.utils.Assets;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class EnterDungeonTile extends WallTile implements Interactable{

	private TextureRegion closedDoorTex;
	private TextureRegion openDoorTex;
	
	private ArrayList<Rectangle> interactBox;
	private boolean isInteractable;
	
	private static final float FONT_SCALE = 0.2f;
	private static BitmapFont font = Assets.manager.get(Assets.font);
	private static GlyphLayout enterDungeonText = new GlyphLayout();
	
	static {
		font.getData().setScale(FONT_SCALE);
		enterDungeonText.setText(font, "ENTER DUNGEON!");
		font.getData().setScale(1.0f);
	}
	
	public EnterDungeonTile(TextureRegion closedDoorTex, TextureRegion openDoorTex, int row, int col, float rotation, boolean isFrontWall) {
		super(null, row, col, rotation, isFrontWall);
		this.closedDoorTex = closedDoorTex;
		this.openDoorTex = openDoorTex;
		
		interactBox = new ArrayList<Rectangle>();
		Vector2 direction = new Vector2(TILE_LENGTH / 5, 0);
		for (int i = 0; i < 4; i++) {
			Rectangle interact = new Rectangle(getBox());
			interact.setCenter(interact.getX() + direction.x, interact.getY() + direction.y);
			interactBox.add(interact);
			
			direction.rotate90(1);
		}
		interactBox.add(getBox());
		interactables.add(this);
	}
	
	@Override
	public void draw(Batch batch, float a) {
		if (isInteractable) {
			tex = openDoorTex;
		}
		else {
			tex = closedDoorTex;
		}
		super.draw(batch, a);
		if (isInteractable) {
			font.getData().setScale(FONT_SCALE);
			float fontX = getCenter().x - (enterDungeonText.width / 2);
			float fontY = getCenter().y + TILE_LENGTH;
			font.draw(batch, enterDungeonText, fontX, fontY);
			font.getData().setScale(1.0f);
		}
		
		isInteractable = false;
	}
	
	@Override
	public ArrayList<Rectangle> getInteractBox() {
		return interactBox;
	}

	@Override
	public boolean interact(PlayerCharacter source) {
		((TownStage)getStage()).setReadyForNextLevel(true);
		return true;
	}

	@Override
	public void setCanInteract(boolean flag) {
		isInteractable = flag;
	}

	@Override
	public boolean remove() {
		interactables.remove(this);
		return super.remove();
	}
}
