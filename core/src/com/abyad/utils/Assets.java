package com.abyad.utils;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;

public class Assets {
	
	public static final AssetManager manager = new AssetManager();
	
	public static final AssetDescriptor<Texture> boy1 = new AssetDescriptor<Texture>("character/boy1/spriteSheet.png", Texture.class);
	public static final AssetDescriptor<Texture> girl1 = new AssetDescriptor<Texture>("character/girl1/spriteSheet.png", Texture.class);
	
	public static final AssetDescriptor<Texture> zombie = new AssetDescriptor<Texture>("character/zombie/sprite.png", Texture.class);
	
	public static final AssetDescriptor<Texture> relics = new AssetDescriptor<Texture>("items/relics.png", Texture.class); 
	public static final AssetDescriptor<Texture> pickups = new AssetDescriptor<Texture>("items/pickups.png", Texture.class); 
	public static final AssetDescriptor<Texture> carrying = new AssetDescriptor<Texture>("items/carrying.png", Texture.class); 
	
	public static final AssetDescriptor<Texture> deathAnim = new AssetDescriptor<Texture>("other/deathSprite.png", Texture.class);
	public static final AssetDescriptor<Texture> sword = new AssetDescriptor<Texture>("weapon/sword/sprite.png", Texture.class);
	
	public static final AssetDescriptor<Texture> floorTiles = new AssetDescriptor<Texture>("tile/floorTiles.png", Texture.class);
	public static final AssetDescriptor<Texture> wallTiles = new AssetDescriptor<Texture>("tile/wallTiles.png", Texture.class);
	public static final AssetDescriptor<Texture> stairTile = new AssetDescriptor<Texture>("tile/stairTiles.png", Texture.class);
	
	public static final AssetDescriptor<Texture> treasureChest = new AssetDescriptor<Texture>("object/treasureChest.png", Texture.class);
	
	public static final AssetDescriptor<Texture> buttons = new AssetDescriptor<Texture>("ui/buttons.png", Texture.class);
	
	public static Texture redBox;		//Texture used for debugging hitboxes for the tile
	public static Texture greenBox;		//Texture used for debugging hitboxes for the tile
	static {
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
	}
	
	
	public static void loadAssets() {
		manager.load(boy1);
		manager.load(girl1);
		manager.load(zombie);
		manager.load(relics);
		manager.load(pickups);
		manager.load(carrying);
		manager.load(deathAnim);
		manager.load(sword);
		manager.load(floorTiles);
		manager.load(wallTiles);
		manager.load(stairTile);
		manager.load(treasureChest);
		manager.load(buttons);
		manager.finishLoading();
	}
	
	public static void dispose() {
		manager.dispose();
	}
}
