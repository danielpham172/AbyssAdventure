package com.abyad.utils;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
	
	public static final AssetManager manager = new AssetManager();
	
	public static final AssetDescriptor<Texture> boy1 = new AssetDescriptor<Texture>("character/boy1/spriteSheet.png", Texture.class);
	public static final AssetDescriptor<Texture> girl1 = new AssetDescriptor<Texture>("character/girl1/spriteSheet.png", Texture.class);
	public static final AssetDescriptor<Texture> zombie = new AssetDescriptor<Texture>("character/zombie/sprite.png", Texture.class);
	public static final AssetDescriptor<Texture> sword = new AssetDescriptor<Texture>("weapon/sword/sprite.png", Texture.class);
	public static final AssetDescriptor<Texture> floorTiles = new AssetDescriptor<Texture>("tile/floorTiles.png", Texture.class);
	public static final AssetDescriptor<Texture> wallTiles = new AssetDescriptor<Texture>("tile/wallTiles.png", Texture.class);
	
	public static void loadAssets() {
		manager.load(boy1);
		manager.load(girl1);
		manager.load(zombie);
		manager.load(sword);
		manager.load(floorTiles);
		manager.load(wallTiles);
		manager.finishLoading();
	}
	
	public static void dispose() {
		manager.dispose();
	}
}
