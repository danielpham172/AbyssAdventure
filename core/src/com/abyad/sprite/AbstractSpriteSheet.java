package com.abyad.sprite;

import java.util.LinkedHashMap;

import com.abyad.utils.Assets;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class AbstractSpriteSheet {
	protected LinkedHashMap<String, TextureRegion> sprites;			//A map of sprite regions linked to a string
	
	public static LinkedHashMap<String, AbstractSpriteSheet> spriteSheets = new LinkedHashMap<String, AbstractSpriteSheet>();
	static {
		spriteSheets.put("ZOMBIE", new EntitySprite(Assets.manager.get(Assets.zombie)));
		spriteSheets.put("BOY_1", new PlayerSprite(Assets.manager.get(Assets.boy1), Assets.manager.get(Assets.boy1_head)));
		spriteSheets.put("GIRL_1", new PlayerSprite(Assets.manager.get(Assets.girl1), Assets.manager.get(Assets.girl1_head)));
		
		spriteSheets.put("WIND_SLASH", new ProjectileSprite(Assets.manager.get(Assets.windSlash), 4, 1, 6));
		
		String[][] chestNames = { {"NORMAL_CLOSED", "NORMAL_OPEN"}, {"RARE_CLOSED", "RARE_OPEN"} };
		spriteSheets.put("CHEST", new BasicSprite(Assets.manager.get(Assets.treasureChest), chestNames.length, chestNames[0].length, chestNames));
		
		String[][] buttonNames = { {"A", "B"}, {"X", "Y"} };
		spriteSheets.put("UI_BUTTONS", new BasicSprite(Assets.manager.get(Assets.buttons), 2, 2, buttonNames));
		spriteSheets.put("HEALTH_CELL", new BarSprite(Assets.manager.get(Assets.healthCell), 5, 11));
		spriteSheets.put("MANA_CELL", new BarSprite(Assets.manager.get(Assets.manaCell), 5, 11));
		
		String[][] relicNames = { {"TON_WEIGHT"} };
		spriteSheets.put("RELICS", new BasicSprite(Assets.manager.get(Assets.relics), relicNames.length, relicNames[0].length, relicNames));
		
		String[][] pickupsNames = { {"HEART", "GOLD"} };
		spriteSheets.put("PICKUPS", new BasicSprite(Assets.manager.get(Assets.pickups), pickupsNames.length, pickupsNames[0].length, pickupsNames));
		
		String[][] carryingNames = { {"KEY"} };
		spriteSheets.put("CARRY", new BasicSprite(Assets.manager.get(Assets.carrying), carryingNames.length, carryingNames[0].length, carryingNames));
		
		String[][] capsuleNames = { {"LIFE_CAPSULE", "MANA_CAPSULE"} };
		spriteSheets.put("CAPSULES", new BasicSprite(Assets.manager.get(Assets.capsules), capsuleNames.length, capsuleNames[0].length, capsuleNames));
	}
	
	public AbstractSpriteSheet() {
		sprites = new LinkedHashMap<String, TextureRegion>();		//Initialize the hashmap
	}
	
	public TextureRegion getSprite(String name) {
		return sprites.get(name);									//Get the specified texture
	}
}
