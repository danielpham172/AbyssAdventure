package com.abyad.sprite;

import java.util.LinkedHashMap;

import com.abyad.actor.tile.AbstractTile;
import com.abyad.game.Player;
import com.abyad.magic.AbstractMagic;
import com.abyad.utils.Assets;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class AbstractSpriteSheet {
	protected LinkedHashMap<String, TextureRegion> sprites;			//A map of sprite regions linked to a string
	
	public static LinkedHashMap<String, AbstractSpriteSheet> spriteSheets;
	
	public static void initializeSpriteSheets() {
		spriteSheets = new LinkedHashMap<String, AbstractSpriteSheet>();
		spriteSheets.put("ZOMBIE", new EntitySprite(Assets.manager.get(Assets.zombie)));
		
		for (String name : Player.characterNames) {
			spriteSheets.put(name, new PlayerSprite(Assets.manager.get(Assets.characterAssets.get(name).get("SPRITE")),
					Assets.manager.get(Assets.characterAssets.get(name).get("ICON"))));
		}
		
		spriteSheets.put("WIND_SLASH", new ProjectileSprite(Assets.manager.get(Assets.windSlash), 4, 1, 6));
		spriteSheets.put("MAGIC_BOLT_PROJECTILE", new ProjectileSprite(Assets.manager.get(Assets.magicBoltProjectile), 1, 2, 6, true));
		spriteSheets.put("HEALING_FIELD_PROJECTILE", new ProjectileSprite(Assets.manager.get(Assets.healingFieldProjectile), 1, 2, 6, true));
		
		String[][] chestNames = { {"NORMAL_CLOSED", "NORMAL_OPEN"}, {"RARE_CLOSED", "RARE_OPEN"} };
		spriteSheets.put("CHEST", new BasicSprite(Assets.manager.get(Assets.treasureChest), chestNames));
		
		String[][] houseNames = { {"CLOSED", "OPEN", "ROOF"} };
		spriteSheets.put("HOUSE", new BasicSprite(Assets.manager.get(Assets.house), houseNames));
		
		String[][] blacksmithNames = { {"CLOSED", "OPEN"} };
		spriteSheets.put("BLACKSMITH", new BasicSprite(Assets.manager.get(Assets.blacksmith), blacksmithNames));
		
		String[][] buttonNames = { {"A", "B"}, {"X", "Y"} };
		spriteSheets.put("UI_BUTTONS", new BasicSprite(Assets.manager.get(Assets.buttons), buttonNames));
		String[][] selectionArrowNames = { {"LEFT", "RIGHT"} };
		spriteSheets.put("SELECTION_ARROWS", new BasicSprite(Assets.manager.get(Assets.selectionArrows), selectionArrowNames));
		spriteSheets.put("MAGIC_SELECTION", new BasicSprite(Assets.manager.get(Assets.magicSelectCursor), new String[][] {{"SELECTION"}}));
		spriteSheets.put("HEALTH_CELL", new BarSprite(Assets.manager.get(Assets.healthCell), 5, 11));
		spriteSheets.put("MANA_CELL", new BarSprite(Assets.manager.get(Assets.manaCell), 5, 11));
		
		String[][] weaponIconNames = { {"SWORD", "STAFF"} };
		spriteSheets.put("WEAPON_ICONS", new BasicSprite(Assets.manager.get(Assets.weaponIcons), weaponIconNames));
		
		String[][] relicNames = { {"TON_WEIGHT"} };
		spriteSheets.put("RELICS", new BasicSprite(Assets.manager.get(Assets.relics), relicNames));
		
		String[][] pickupsNames = { {"HEART", "GOLD", "MANA"} };
		spriteSheets.put("PICKUPS", new BasicSprite(Assets.manager.get(Assets.pickups), pickupsNames));
		
		String[][] carryingNames = { {"KEY"} };
		spriteSheets.put("CARRY", new BasicSprite(Assets.manager.get(Assets.carrying), carryingNames));
		
		String[][] capsuleNames = { {"LIFE_CAPSULE", "MANA_CAPSULE"} };
		spriteSheets.put("CAPSULES", new BasicSprite(Assets.manager.get(Assets.capsules), capsuleNames));
		
		spriteSheets.put("SCROLL", new BasicSprite(Assets.manager.get(Assets.scroll),  new String[][]{{ "SCROLL"}}));
		
		String[][] particleNames = { {"RED", "ORANGE", "YELLOW", "GREEN"}, {"CYAN", "BLUE", "PURPLE", "PINK"} };
		spriteSheets.put("PARTICLES",  new BasicSprite(Assets.manager.get(Assets.particles), particleNames));
		
		String[][] statusArrowNames = { {"DOWN", "UP"} };
		spriteSheets.put("STATUS_ARROW",  new BasicSprite(Assets.manager.get(Assets.statusArrows), statusArrowNames));
		
		
		//Magic
		for (String magicName : Assets.magicAssets.keySet()) {
			AbstractMagic magic = AbstractMagic.magicList.get(magicName);
			spriteSheets.put(magicName, new MagicSprite(Assets.magicAssets.get(magicName),
					magic.magicCircleFrames(), magic.magicCircleFrameTime(), magic.particleFrames(), magic.particleFrameTime()));
		}
		
		//Dungeon
		for (String dungeonName : Assets.tileAssets.keySet()) {
			boolean first = true;
			for (String imageKey : Assets.tileAssets.get(dungeonName).keySet()) {
				Texture image = Assets.manager.get(Assets.tileAssets.get(dungeonName).get(imageKey));
				if (first) {
					spriteSheets.put(dungeonName, new BasicSprite(image, image.getHeight() / AbstractTile.TILE_SIZE,
							image.getWidth() / AbstractTile.TILE_SIZE, imageKey));
					first = false;
				}
				else {
					((BasicSprite)spriteSheets.get(dungeonName)).addFullSheet(image, image.getHeight() / AbstractTile.TILE_SIZE,
							image.getWidth() / AbstractTile.TILE_SIZE, imageKey);
				}
			}
		}
		
		for (String imageKey : Assets.townTiles.keySet()) {
			Texture image = Assets.manager.get(Assets.townTiles.get(imageKey));
			if (!spriteSheets.containsKey("TOWN")) {
				spriteSheets.put("TOWN", new BasicSprite(image, image.getHeight() / AbstractTile.TILE_SIZE,
						image.getWidth() / AbstractTile.TILE_SIZE, imageKey));
			}
			else {
				((BasicSprite)spriteSheets.get("TOWN")).addFullSheet(image, image.getHeight() / AbstractTile.TILE_SIZE,
						image.getWidth() / AbstractTile.TILE_SIZE, imageKey);
			}
		}
	}
	
	public AbstractSpriteSheet() {
		sprites = new LinkedHashMap<String, TextureRegion>();		//Initialize the hashmap
	}
	
	public TextureRegion getSprite(String name) {
		return sprites.get(name);									//Get the specified texture
	}
}
