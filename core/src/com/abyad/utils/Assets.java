package com.abyad.utils;

import java.util.LinkedHashMap;

import com.abyad.game.Player;
import com.abyad.magic.AbstractMagic;
import com.abyad.mapdata.MapEnvironment;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Pixmap.Format;

public class Assets {
	
	public static final AssetManager manager = new AssetManager();
	
	//Characters
	public static final LinkedHashMap<String, LinkedHashMap<String, AssetDescriptor<Texture>>> characterAssets = new LinkedHashMap<String, LinkedHashMap<String, AssetDescriptor<Texture>>>();
	
	//Monsters/NPCS
	public static final AssetDescriptor<Texture> zombie = new AssetDescriptor<Texture>("character/zombie/sprite.png", Texture.class);
	
	//Magic List
	public static final LinkedHashMap<String, LinkedHashMap<String, AssetDescriptor<Texture>>> magicAssets = new LinkedHashMap<String, LinkedHashMap<String, AssetDescriptor<Texture>>>();
	
	//Projectiles
	public static final AssetDescriptor<Texture> windSlash = new AssetDescriptor<Texture>("projectile/windSlash/sprite.png", Texture.class);
	public static final AssetDescriptor<Texture> magicBoltProjectile = new AssetDescriptor<Texture>("projectile/magicBolt/projectile.png", Texture.class);
	public static final AssetDescriptor<Texture> healingFieldProjectile = new AssetDescriptor<Texture>("projectile/healingField/projectile.png", Texture.class);
	
	//Items
	public static final AssetDescriptor<Texture> relics = new AssetDescriptor<Texture>("items/relics.png", Texture.class); 
	public static final AssetDescriptor<Texture> pickups = new AssetDescriptor<Texture>("items/pickups.png", Texture.class); 
	public static final AssetDescriptor<Texture> carrying = new AssetDescriptor<Texture>("items/carrying.png", Texture.class); 
	public static final AssetDescriptor<Texture> capsules = new AssetDescriptor<Texture>("items/capsules.png", Texture.class);
	public static final AssetDescriptor<Texture> scroll = new AssetDescriptor<Texture>("items/scroll.png", Texture.class); 
	
	//Animation Stuffs/Weapons
	public static final AssetDescriptor<Texture> deathAnim = new AssetDescriptor<Texture>("other/deathSprite.png", Texture.class);
	public static final AssetDescriptor<Texture> particles = new AssetDescriptor<Texture>("other/particles.png", Texture.class);
	public static final AssetDescriptor<Texture> statusArrows = new AssetDescriptor<Texture>("other/statusArrows.png", Texture.class);
	public static final AssetDescriptor<Texture> sword = new AssetDescriptor<Texture>("weapon/sword/sprite.png", Texture.class);
	public static final AssetDescriptor<Texture> staff = new AssetDescriptor<Texture>("weapon/staff/sprite.png", Texture.class);
	public static final AssetDescriptor<Texture> weaponIcons = new AssetDescriptor<Texture>("weapon/weaponIcons.png", Texture.class);
	
	//Tiles
	public static final LinkedHashMap<String, LinkedHashMap<String, AssetDescriptor<Texture>>> tileAssets = new LinkedHashMap<String, LinkedHashMap<String, AssetDescriptor<Texture>>>();
	public static final LinkedHashMap<String, AssetDescriptor<Texture>> townTiles = new LinkedHashMap<String, AssetDescriptor<Texture>>();
	
	//Map Objects
	public static final AssetDescriptor<Texture> treasureChest = new AssetDescriptor<Texture>("object/treasureChest.png", Texture.class);
	public static final AssetDescriptor<Texture> goddessStatue = new AssetDescriptor<Texture>("object/goddessStatue.png", Texture.class);
	public static final AssetDescriptor<Texture> house = new AssetDescriptor<Texture>("object/house.png", Texture.class);
	public static final AssetDescriptor<Texture> blacksmith = new AssetDescriptor<Texture>("object/blacksmith.png", Texture.class);
	public static final AssetDescriptor<Texture> magicShop = new AssetDescriptor<Texture>("object/magicShop.png", Texture.class);
	
	//UI things
	public static final AssetDescriptor<Texture> buttons = new AssetDescriptor<Texture>("ui/buttons.png", Texture.class);
	public static final AssetDescriptor<Texture> selectionArrows = new AssetDescriptor<Texture>("ui/selectionArrows.png", Texture.class);
	public static final AssetDescriptor<Texture> magicSelectCursor = new AssetDescriptor<Texture>("ui/magicSelection.png", Texture.class);
	public static final AssetDescriptor<Texture> healthCell = new AssetDescriptor<Texture>("ui/health/healthCell.png", Texture.class);
	public static final AssetDescriptor<Texture> manaCell = new AssetDescriptor<Texture>("ui/mana/manaCell.png", Texture.class);
	public static final AssetDescriptor<BitmapFont> font = new AssetDescriptor<BitmapFont>("ui/silkscreen.fnt", BitmapFont.class);
	
	public static Texture redBox;		//Texture used for debugging hitboxes for the tile
	public static Texture greenBox;		//Texture used for debugging hitboxes for the tile
	static {
		//Debug Textures
		Pixmap redPix = new Pixmap(1, 1, Format.RGBA8888 );
		redPix.setColor( 1, 0, 0, 0.25f );
		redPix.fill();
		redBox = new Texture(redPix);
		redPix.dispose();
		
		Pixmap greenPix = new Pixmap(1, 1, Format.RGBA8888 );
		greenPix.setColor( 0, 1, 0, 0.25f );
		greenPix.fill();
		greenBox = new Texture(greenPix);
		greenPix.dispose();
		
		String[] characterList = FileReads.readFileToArray("character/playerCharacters.txt");
		for (String line : characterList) {
			int spaceIndex = line.indexOf(' ');
			String folder = line.substring(0, spaceIndex);
			String name = line.substring(spaceIndex + 1);
			LinkedHashMap<String, AssetDescriptor<Texture>> list = new LinkedHashMap<String, AssetDescriptor<Texture>>();
			list.put("SPRITE", new AssetDescriptor<Texture>("character/" + folder + "/spriteSheet.png", Texture.class));
			list.put("ICON", new AssetDescriptor<Texture>("character/" + folder + "/icons.png", Texture.class));
			characterAssets.put(name, list);
			
			Player.characterNames.add(name);
		}
		
		//Adding magic based on the magic list
		//Probably will do the same thing with relics soon
		for (String key : AbstractMagic.magicList.keySet()) {
			AbstractMagic magic = AbstractMagic.magicList.get(key);
			LinkedHashMap<String, AssetDescriptor<Texture>> list = new LinkedHashMap<String, AssetDescriptor<Texture>>();
			list.put("ICON", new AssetDescriptor<Texture>("magic/" + magic.getAssetsDirectory() + "/icon.png", Texture.class));
			if (magic.drawsMagicCircle()) {
				list.put("CIRCLE", new AssetDescriptor<Texture>("magic/" + magic.getAssetsDirectory() + "/circle.png", Texture.class));
			}
			if (magic.spawnsParticles()) {
				list.put("PARTICLE", new AssetDescriptor<Texture>("magic/" + magic.getAssetsDirectory() + "/particle.png", Texture.class));
			}
			
			magicAssets.put(key, list);
		}
		
		String[] dungeonList = FileReads.readFileToArray("tile/dungeons.txt");
		for (String dungeon : dungeonList) {
			int spaceIndex = dungeon.indexOf(' ');
			String folderName = dungeon.substring(0, spaceIndex);
			String envName = dungeon.substring(spaceIndex + 1);
			String[] dungeonData = FileReads.readFileToArray("tile/" + folderName + "/dungeonData.txt");
			LinkedHashMap<String, AssetDescriptor<Texture>> list = new LinkedHashMap<String, AssetDescriptor<Texture>>();
			for (String tileImages : dungeonData) {
				list.put(tileImages, new AssetDescriptor<Texture>("tile/" + folderName + "/" + tileImages + ".png", Texture.class));
			}
			MapEnvironment environment = new MapEnvironment(envName, folderName);
			
			tileAssets.put(envName, list);
			MapEnvironment.environments.put(envName, environment);
		}
		
		String townDirectory = FileReads.readFileToArray("tile/town.txt")[0];
		String[] townData = FileReads.readFileToArray("tile/" + townDirectory + "/dungeonData.txt");
		for (String tileImages : townData) {
			townTiles.put(tileImages, new AssetDescriptor<Texture>("tile/" + townDirectory + "/" + tileImages + ".png", Texture.class));
		}
		MapEnvironment.townEnvironment = new MapEnvironment("TOWN", townDirectory);
	}
	
	
	public static void loadAssets() {
		//Player Characters
		for (String key : characterAssets.keySet()) {
			for (String imageKey : characterAssets.get(key).keySet()) {
				manager.load(characterAssets.get(key).get(imageKey));
			}
		}
		//Monsters/NPCS
		manager.load(zombie);
		//Magic Stuff
		for (String key : magicAssets.keySet()) {
			for (String imageKey : magicAssets.get(key).keySet()) {
				manager.load(magicAssets.get(key).get(imageKey));
			}
		}
		//Projectiles
		manager.load(windSlash);
		manager.load(magicBoltProjectile);
		manager.load(healingFieldProjectile);
		//Items
		manager.load(relics);
		manager.load(pickups);
		manager.load(carrying);
		manager.load(capsules);
		manager.load(scroll);
		//Animation
		manager.load(deathAnim);
		manager.load(particles);
		manager.load(statusArrows);
		//Weapon
		manager.load(sword);
		manager.load(staff);
		manager.load(weaponIcons);
		//Dungeon Tiles and Objects
		manager.load(treasureChest);
		manager.load(goddessStatue);
		manager.load(house);
		manager.load(blacksmith);
		manager.load(magicShop);
		for (String key : tileAssets.keySet()) {
			for (String imageKey : tileAssets.get(key).keySet()) {
				manager.load(tileAssets.get(key).get(imageKey));
			}
		}
		for (String key : townTiles.keySet()) {
			manager.load(townTiles.get(key));
		}
		//UI Stuff
		manager.load(buttons);
		manager.load(selectionArrows);
		manager.load(magicSelectCursor);
		manager.load(healthCell);
		manager.load(manaCell);
		manager.load(font);
		manager.finishLoading();
	}
	
	public static void dispose() {
		manager.dispose();
	}
}
