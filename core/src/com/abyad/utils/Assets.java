package com.abyad.utils;

import java.util.LinkedHashMap;

import com.abyad.magic.AbstractMagic;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Pixmap.Format;

public class Assets {
	
	public static final AssetManager manager = new AssetManager();
	
	//Characters
	public static final AssetDescriptor<Texture> boy1 = new AssetDescriptor<Texture>("character/boy1/spriteSheet.png", Texture.class);
	public static final AssetDescriptor<Texture> boy1_head = new AssetDescriptor<Texture>("character/boy1/head.png", Texture.class);
	public static final AssetDescriptor<Texture> girl1 = new AssetDescriptor<Texture>("character/girl1/spriteSheet.png", Texture.class);
	public static final AssetDescriptor<Texture> girl1_head = new AssetDescriptor<Texture>("character/girl1/head.png", Texture.class);
	
	//Monsters/NPCS
	public static final AssetDescriptor<Texture> zombie = new AssetDescriptor<Texture>("character/zombie/sprite.png", Texture.class);
	
	//Magic List
	public static final LinkedHashMap<String, LinkedHashMap<String, AssetDescriptor<Texture>>> magicAssets = new LinkedHashMap<String, LinkedHashMap<String, AssetDescriptor<Texture>>>();
	
	//Projectiles
	public static final AssetDescriptor<Texture> windSlash = new AssetDescriptor<Texture>("projectile/windSlash/sprite.png", Texture.class);
	public static final AssetDescriptor<Texture> magicBoltProjectile = new AssetDescriptor<Texture>("projectile/magicBolt/projectile.png", Texture.class);
	
	//Items
	public static final AssetDescriptor<Texture> relics = new AssetDescriptor<Texture>("items/relics.png", Texture.class); 
	public static final AssetDescriptor<Texture> pickups = new AssetDescriptor<Texture>("items/pickups.png", Texture.class); 
	public static final AssetDescriptor<Texture> carrying = new AssetDescriptor<Texture>("items/carrying.png", Texture.class); 
	public static final AssetDescriptor<Texture> capsules = new AssetDescriptor<Texture>("items/capsules.png", Texture.class); 
	
	//Animation Stuffs/Weapons
	public static final AssetDescriptor<Texture> deathAnim = new AssetDescriptor<Texture>("other/deathSprite.png", Texture.class);
	public static final AssetDescriptor<Texture> sword = new AssetDescriptor<Texture>("weapon/sword/sprite.png", Texture.class);
	
	//Dungeon Tiles
	public static final AssetDescriptor<Texture> floorTiles = new AssetDescriptor<Texture>("tile/floorTiles.png", Texture.class);
	public static final AssetDescriptor<Texture> wallTiles = new AssetDescriptor<Texture>("tile/wallTiles.png", Texture.class);
	public static final AssetDescriptor<Texture> stairTile = new AssetDescriptor<Texture>("tile/stairTiles.png", Texture.class);
	
	//Map Objects
	public static final AssetDescriptor<Texture> treasureChest = new AssetDescriptor<Texture>("object/treasureChest.png", Texture.class);
	
	//UI things
	public static final AssetDescriptor<Texture> buttons = new AssetDescriptor<Texture>("ui/buttons.png", Texture.class);
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
	}
	
	
	public static void loadAssets() {
		//Player Characters
		manager.load(boy1);
		manager.load(boy1_head);
		manager.load(girl1);
		manager.load(girl1_head);
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
		//Items
		manager.load(relics);
		manager.load(pickups);
		manager.load(carrying);
		manager.load(capsules);
		//Animation
		manager.load(deathAnim);
		//Weapon
		manager.load(sword);
		//Dungeon Tiles and Objects
		manager.load(floorTiles);
		manager.load(wallTiles);
		manager.load(stairTile);
		manager.load(treasureChest);
		//UI Stuff
		manager.load(buttons);
		manager.load(healthCell);
		manager.load(manaCell);
		manager.load(font);
		manager.finishLoading();
	}
	
	public static void dispose() {
		manager.dispose();
	}
}
