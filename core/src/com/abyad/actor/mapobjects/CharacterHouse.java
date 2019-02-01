package com.abyad.actor.mapobjects;

import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.actor.tile.FloorTile;
import com.abyad.sprite.AbstractSpriteSheet;
import com.abyad.stage.TownStage;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CharacterHouse extends AbstractHouse{

	private static TextureRegion closedHouse = AbstractSpriteSheet.spriteSheets.get("HOUSE").getSprite("CLOSED");
	private static TextureRegion openHouse = AbstractSpriteSheet.spriteSheets.get("HOUSE").getSprite("OPEN");
	
	private static GlyphLayout descText = new GlyphLayout();
	static {
		font.getData().setScale(FONT_SCALE);
		descText.setText(font, "ADD OR CHANGE CHARACTERS!");
		font.getData().setScale(1.0f);
	}
	
	public CharacterHouse(FloorTile floor) {
		super(floor);
	}

	@Override
	public boolean interact(PlayerCharacter source) {
		super.interact(source);
		((TownStage)getStage()).flagCharacterMenu(true);
		return true;
	}

	@Override
	public TextureRegion getClosedHouse() {
		return closedHouse;
	}

	@Override
	public TextureRegion getOpenHouse() {
		return openHouse;
	}

	@Override
	public GlyphLayout getText() {
		return descText;
	}
}
