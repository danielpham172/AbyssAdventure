package com.abyad.actor.mapobjects;

import java.util.ArrayList;

import com.abyad.actor.cosmetic.BattleText;
import com.abyad.actor.cosmetic.CosmeticParticle;
import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.actor.mapobjects.items.carrying.CorpseItem;
import com.abyad.actor.tile.AbstractTile;
import com.abyad.actor.tile.FloorTile;
import com.abyad.interfaces.Interactable;
import com.abyad.sprite.AbstractSpriteSheet;
import com.abyad.utils.Assets;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ReviveStatue extends Actor implements Interactable{

	private FloorTile floor;
	private ArrayList<Rectangle> interactBox;
	private boolean isInteractable;
	
	private Rectangle hitbox;
	
	private static TextureRegion goddessStatue = AbstractSpriteSheet.spriteSheets.get("STATUE").getSprite("GODDESS_STATUE");
	private static Rectangle baseHitbox = new Rectangle(0, 0, 22, 22);
	private static Rectangle baseInteractBox = new Rectangle(0, 0, 32, 32);
	private static final int HEAL_GOLD_COST = 50;
	private static final int REVIVE_GOLD_COST = 100;
	
	private int timeSinceLastParticle;
	private boolean active;
	
	private static final float FONT_SCALE = 0.25f;
	private static BitmapFont font = Assets.manager.get(Assets.font);
	private static GlyphLayout healText = new GlyphLayout();
	private static GlyphLayout reviveText = new GlyphLayout();
	private static final float SPACING = 4f;
	
	static {
		font.getData().setScale(FONT_SCALE);
		healText.setText(font, "HEAL YOURSELF FOR " + HEAL_GOLD_COST + " GOLD!");
		reviveText.setText(font, "REVIVE A PLAYER FOR " + REVIVE_GOLD_COST + " GOLD!");
		font.getData().setScale(1.0f);
	}
	
	public ReviveStatue(FloorTile floor) {
		this.floor = floor;
		
		setOrigin(AbstractTile.TILE_SIZE / 2, AbstractTile.TILE_SIZE / 2);
		setX(floor.getCenter().x);
		setY(floor.getCenter().y);
		
		interactBox = new ArrayList<Rectangle>();
		interactBox.add((new Rectangle(0, 0, baseInteractBox.width * AbstractTile.TILE_SCALE, baseInteractBox.height * AbstractTile.TILE_SCALE))
				.setCenter(floor.getCenter()));
		
		hitbox = new Rectangle(0, 0, baseHitbox.width * AbstractTile.TILE_SCALE, baseHitbox.height * AbstractTile.TILE_SCALE);
		hitbox.setCenter(floor.getCenter());
		floor.addCollisionBox(hitbox);
		
		interactables.add(this);
		active = true;
	}
	
	@Override
	public void act(float delta) {
		if (active) {
			if (timeSinceLastParticle == 0) {
				CosmeticParticle.spawnParticle(AbstractSpriteSheet.spriteSheets.get("PARTICLES").getSprite("YELLOW"),
						getX(), getY() - getOriginY() + 8f, 0f, 10f, 4f, 0f, 0.25f, 0.2f, 0.0f, 20, 0.5f, 0.8f, 120, 0.2f, 1, getStage());
				timeSinceLastParticle = 5;
			}
			else {
				timeSinceLastParticle -= 1;
			}
		}
	}
	
	@Override
	public void draw(Batch batch, float a) {
		if (inView()) {
			batch.draw(goddessStatue, getX() - getOriginX(), getY() - getOriginY(),
					getOriginX(), getOriginY(), goddessStatue.getRegionWidth(), goddessStatue.getRegionHeight(), AbstractTile.TILE_SCALE, AbstractTile.TILE_SCALE, getRotation());
			if (isInteractable) {
				font.getData().setScale(FONT_SCALE);
				float fontX = floor.getCenter().x - (reviveText.width / 2);
				float fontY = floor.getCenter().y + (AbstractTile.TILE_LENGTH * 1.2f) - (reviveText.height / 2);
				font.draw(batch, reviveText, fontX, fontY);
				fontX = floor.getCenter().x - (healText.width / 2);
				fontY += SPACING + (healText.height / 2) + (reviveText.height / 2);
				font.draw(batch, healText, fontX, fontY);
				font.getData().setScale(1.0f);
			}
		}
		
		isInteractable = false;
		//batch.draw(AbstractTile.box, hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
	}
	
	public boolean inView() {
		return getStage().getCamera().frustum.boundsInFrustum(getX(), getY() + (AbstractTile.TILE_LENGTH / 2), 0,
				AbstractTile.TILE_LENGTH / 2, AbstractTile.TILE_LENGTH, 0);
	}
	
	@Override
	public ArrayList<Rectangle> getInteractBox() {
		return interactBox;
	}

	@Override
	public boolean interact(PlayerCharacter source) {
		if (active) {
			if (source.getHeldItem() instanceof CorpseItem) {
				if (source.getGold() >= REVIVE_GOLD_COST) {
					CorpseItem corpse = (CorpseItem)source.getHeldItem();
					corpse.startReviveProcess(source);
					source.removeHeldItem();
					source.modifyGold(-REVIVE_GOLD_COST);
					getStage().addActor(corpse);
					active = false;
					interactables.remove(this);
					return true;
				}
				else {
					BattleText notEnoughGoldText = new BattleText("Not enough gold!", getX(), getY(), new Vector2(0, 1.0f), 1.0f, 25);
					notEnoughGoldText.setScale(0.2f);
					getStage().addActor(notEnoughGoldText);
					return true;
				}
			}
			else {
				if (source.getGold() >= HEAL_GOLD_COST) {
					source.modifyHP(source.getMaxHP());
					source.addMana(source.getMaxMana());
					source.modifyGold(-HEAL_GOLD_COST);
					active = false;
					interactables.remove(this);
					return true;
				}
				else {
					BattleText notEnoughGoldText = new BattleText("Not enough gold!", getX(), getY(), new Vector2(0, 1.0f), 1.0f, 25);
					notEnoughGoldText.setScale(0.2f);
					getStage().addActor(notEnoughGoldText);
					return true;
				}
			}
		}
		return false;
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
