package com.abyad.magic;

import java.util.LinkedHashMap;

import com.abyad.actor.cosmetic.CosmeticParticle;
import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.sprite.AbstractSpriteSheet;
import com.abyad.sprite.MagicSprite;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class AbstractMagic {

	public static LinkedHashMap<String, AbstractMagic> magicList = new LinkedHashMap<String, AbstractMagic>();
	static {
		magicList.put("MAGIC BOLT", new MagicBolt());
		magicList.put("HEALING FIELD", new HealingField());
	}
	
	private String name;
	private String desc;
	private int manaCost;
	private int partialManaCost;
	private int castTime;
	private int afterCastTime;
	private String resourceFolder;
	private MagicSprite spriteSheet;
	
	private static final float CIRCLE_OFFSET_Y = -3f;
	
	public static void initializeSprites() {
		for (String key : magicList.keySet()) {
			magicList.get(key).initializeSpriteSheet();
		}
	}
	
	public AbstractMagic(String name, String desc, int manaCost, int partialManaCost, int castTime, int afterCastTime, String resourceFolder) {
		this.name = name;
		this.desc = desc;
		this.manaCost = manaCost;
		this.partialManaCost = partialManaCost;
		this.castTime = castTime;
		this.afterCastTime = afterCastTime;
		
		this.resourceFolder = resourceFolder;
	}
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return desc;
	}
	
	public int getManaCost() {
		return manaCost;
	}
	
	public int getPartialManaCost() {
		return partialManaCost;
	}
	
	public int getCastTime() {
		return castTime;
	}
	
	public int getAfterTime() {
		return afterCastTime;
	}
	public String getAssetsDirectory() {
		return resourceFolder;
	}
	public TextureRegion getIcon() {
		return spriteSheet.getSprite("ICON");
	}
	public void initializeSpriteSheet() {
		spriteSheet = (MagicSprite)AbstractSpriteSheet.spriteSheets.get(name);
	}
	
	public boolean usesCursor() {
		return true;
	}
	public abstract void castMagic(PlayerCharacter player, Vector2 cursorPosition);
	
	public void drawMagic(Batch batch, float a, PlayerCharacter source, int framesSinceLast) {
		if (drawsMagicCircle()) {
			TextureRegion circle = spriteSheet.getNextFrame("CIRCLE", null, framesSinceLast).get(0);
			batch.draw(circle, source.getCenterX() - (circle.getRegionWidth() / 2),
					source.getCenterY() + CIRCLE_OFFSET_Y - (circle.getRegionHeight() / 2),
					circle.getRegionWidth() / 2, circle.getRegionHeight() / 2,
					circle.getRegionWidth(), circle.getRegionHeight(), 1, 1, 0);
		}
	}
	public boolean drawsMagicCircle() {
		return true;
	}
	public int magicCircleFrames() {
		return 3;
	}
	public int magicCircleFrameTime() {
		return 6;
	}
	
	public void addsParticle(PlayerCharacter source) {
		if (spawnsParticles() && Math.random() < particleDensity()) {
			CosmeticParticle.spawnParticle(spriteSheet.getNextFrame("PARTICLE", null, 0).get(0),
					source.getCenterX(), source.getCenterY() + CIRCLE_OFFSET_Y, 0f, 5f, 0f, 0f, particleSpeed(), 0.2f,
					0.0f, maxParticleAngle(), 1.0f, 1.0f, particleLifetime(), 0.2f, 10, source.getStage());
		}
	}
	public boolean spawnsParticles() {
		return true;
	}
	public float maxParticleAngle() {
		return 30f;
	}
	public float particleSpeed() {
		return 0.45f;
	}
	public float particleDensity() {
		return 0.6f;
	}
	public int particleFrames() {
		return 1;
	}
	public int particleFrameTime() {
		return 10;
	}
	public int particleLifetime() {
		return 18;
	}
	
}
