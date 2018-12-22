package com.abyad.sprite;

import java.util.LinkedHashMap;

import com.abyad.utils.Assets;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class AbstractSpriteSheet {
	protected LinkedHashMap<String, TextureRegion> sprites;			//A map of sprite regions linked to a string
	
	public static LinkedHashMap<String, AbstractSpriteSheet> spriteSheets = new LinkedHashMap<String, AbstractSpriteSheet>();
	static {
		spriteSheets.put("ZOMBIE", new EntitySprite(Assets.manager.get(Assets.zombie)));
		spriteSheets.put("BOY_1", new PlayerSprite(Assets.manager.get(Assets.boy1)));
		spriteSheets.put("GIRL_1", new PlayerSprite(Assets.manager.get(Assets.girl1)));
		
		String[][] chestNames = { {"NORMAL_CLOSED", "NORMAL_OPEN"}, {"RARE_CLOSED", "RARE_OPEN"} };
		spriteSheets.put("CHEST", new BasicSprite(Assets.manager.get(Assets.treasureChest), chestNames.length, chestNames[0].length, chestNames));
		
		String[][] buttonNames = { {"A", "B"}, {"X", "Y"} };
		spriteSheets.put("UI_BUTTONS", new BasicSprite(Assets.manager.get(Assets.buttons), 2, 2, buttonNames));
		
		String[][] relicNames = { {"TON_WEIGHT"} };
		spriteSheets.put("RELICS", new BasicSprite(Assets.manager.get(Assets.relics), relicNames.length, relicNames[0].length, relicNames));
		
		String[][] pickupsNames = { {"HEART", "GOLD"} };
		spriteSheets.put("PICKUPS", new BasicSprite(Assets.manager.get(Assets.pickups), pickupsNames.length, pickupsNames[0].length, pickupsNames));
		
		String[][] carryingNames = { {"KEY"} };
		spriteSheets.put("CARRY", new BasicSprite(Assets.manager.get(Assets.carrying), carryingNames.length, carryingNames[0].length, carryingNames));
	}
	
	public AbstractSpriteSheet() {
		sprites = new LinkedHashMap<String, TextureRegion>();		//Initialize the hashmap
	}
	
	public TextureRegion getSprite(String name) {
		return sprites.get(name);									//Get the specified texture
	}
}
