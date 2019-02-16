package com.abyad.actor.projectile;

import java.util.ArrayList;

import com.abyad.actor.entity.AbstractEntity;
import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.data.HitEvent;
import com.abyad.magic.AbstractMagic;
import com.abyad.relic.Relic;
import com.abyad.sprite.AbstractSpriteSheet;
import com.abyad.sprite.ProjectileSprite;
import com.abyad.stage.PlayStage;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class RockProjectile extends AbstractProjectile{
	
	private ArrayList<Rectangle> hitboxes = new ArrayList<Rectangle>();
	private ArrayList<Rectangle> tempHitboxes = new ArrayList<Rectangle>();
	
	private static Rectangle baseBox = new Rectangle(0, 0, 9, 9);
	
	public RockProjectile(float x, float y, AbstractEntity source, Vector2 velocity) {
		super(x, y, source);
		
		
		this.velocity = velocity;
		hitboxes = new ArrayList<Rectangle>();
		tempHitboxes = new ArrayList<Rectangle>();
		hitboxes.add(new Rectangle(baseBox));
		tempHitboxes.add(new Rectangle(baseBox));
		updateHitbox();
		
		sprite = (ProjectileSprite)AbstractSpriteSheet.spriteSheets.get("ROCK_SCATTER_PROJECTILE");
		setRotation((float)(Math.random() * 360f));
		setScale(0.75f);
	}
	
	@Override
	public void act(float delta) {
		
		ArrayList<Rectangle> hurtboxes = getHitbox();
		ArrayList<AbstractEntity> entities = AbstractEntity.getEntities();
		boolean isHit = false;
		for (AbstractEntity entity : entities) {
			if (!source.isSameTeam(entity)) {
				ArrayList<Rectangle> otherHitbox = entity.getHitbox();
				if (isOverlapping(hurtboxes, otherHitbox)) {
					//Attacked someone
					isHit = true;
					Vector2 knockback = new Vector2(entity.getCenterX() - getX(), entity.getCenterY() - getY());
					knockback.setLength(3.0f);
					int kbLength = 8;
					int damage = 1;
					HitEvent event = new HitEvent(this, entity, damage, knockback, kbLength);
					//If a player, activate relic hit effects (modifies the event)
					if (source instanceof PlayerCharacter) {
						PlayerCharacter player = (PlayerCharacter)source;
						for (Relic relic : player.getRelics()) {
							if (!relic.isOnCooldown() && Math.random() < relic.getActivationRate()) {
								relic.onMagicHit(player, AbstractMagic.magicList.get("ROCK SCATTER"), event, entity);
							}
						}
					}
					source.applyOnHitStatusEffects(event, entity);
					entity.takeDamage(event);
					
					//Activate kill effects if killed entity
					if (source instanceof PlayerCharacter) {
						PlayerCharacter player = (PlayerCharacter)source;
						if (entity.isDead()) {
							for (Relic relic : player.getRelics()) {
								if (!relic.isOnCooldown() && Math.random() < relic.getActivationRate()) {
									relic.onKill(player, entity);
								}
							}
						}
					}
				}
			}
		}
		
		PlayStage stage = (PlayStage)getStage();
		ArrayList<Rectangle> walls = stage.getSurroundingCollisionBoxes(getX(), getY(), 1);		//Gets surrounding wall tiles as rectangle collisions
		ArrayList<Rectangle> collisionBox = new ArrayList<Rectangle>();
		collisionBox.add(new Rectangle(getX() - 3f, getY() - 3f, 6, 6));
		if (isOverlapping(walls, collisionBox) || isHit) {
			markForRemoval = true;
		}
		
		framesSinceLast++;
		setRotation(getRotation() + velocity.len());
		
		super.act(delta);
	}
	
	@Override
	public void updateHitbox() {
		hitboxes.get(0).setCenter(getX(), getY());
	}
	@Override
	public ArrayList<Rectangle> getHitbox() {
		return hitboxes;
	}
	@Override
	public ArrayList<Rectangle> getHitbox(float x, float y) {
		tempHitboxes.get(0).setCenter(x, y);
		return tempHitboxes;
	}
	@Override
	public Rectangle getViewbox() {
		Rectangle viewbox = new Rectangle(getX() - 8 * getScaleX(), getY() - 8 * getScaleY(), 16 * getScaleX(), 16 * getScaleY());
		return viewbox;
	}
}
