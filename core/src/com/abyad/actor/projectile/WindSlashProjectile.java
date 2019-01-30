package com.abyad.actor.projectile;

import java.util.ArrayList;

import com.abyad.actor.attack.AttackData;
import com.abyad.actor.entity.AbstractEntity;
import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.data.HitEvent;
import com.abyad.relic.Relic;
import com.abyad.sprite.AbstractSpriteSheet;
import com.abyad.sprite.ProjectileSprite;
import com.abyad.stage.PlayStage;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class WindSlashProjectile extends AbstractProjectile{

	private ArrayList<Rectangle> hitboxes = new ArrayList<Rectangle>();
	private ArrayList<Rectangle> tempHitboxes = new ArrayList<Rectangle>();
	
	private static Rectangle baseBox = new Rectangle(0, 0, 16, 8);
	
	public WindSlashProjectile(float x, float y, AbstractEntity source, Vector2 velocity) {
		super(x, y, source);
		
		
		this.velocity = velocity;
		hitboxes = new ArrayList<Rectangle>();
		tempHitboxes = new ArrayList<Rectangle>();
		
		Rectangle hitbox = new Rectangle(baseBox);
		Rectangle tempHitbox = new Rectangle(baseBox);
		
		hitboxes.add(hitbox);
		tempHitboxes.add(tempHitbox);
		
		sprite = (ProjectileSprite)AbstractSpriteSheet.spriteSheets.get("WIND_SLASH");
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
					knockback.setLength(4.0f);
					int kbLength = 8;
					int damage = 1;
					HitEvent event = new HitEvent(this, entity, damage, knockback, kbLength);
					//If a player, activate relic hit effects (modifies the event)
					if (source instanceof PlayerCharacter) {
						PlayerCharacter player = (PlayerCharacter)source;
						for (Relic relic : player.getRelics()) {
							if (!relic.isOnCooldown() && Math.random() < relic.getActivationRate()) {
								relic.onHit(player, AttackData.specialAttacks.get("WIND_BLADE"), event, entity);
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
		ArrayList<Rectangle> walls = stage.getSurroundingWallBoxes(getX(), getY(), 1);		//Gets surrounding wall tiles as rectangle collisions
		ArrayList<Rectangle> collisionBox = new ArrayList<Rectangle>();
		collisionBox.add(new Rectangle(getX() - 4, getY() - 4, 8, 8));
		if (isOverlapping(walls, collisionBox)) {
			markForRemoval = true;
		}
		
		framesSinceLast++;
		if (isHit) markForRemoval = true;
		
		super.act(delta);
	}
	
	@Override
	public void updateHitbox() {
		Rectangle hitbox = hitboxes.get(0);
		int dir = (int)((velocity.angle() + 45) / 90) % 4;
		
		if (dir % 2 == 0) {
			hitbox.set(getX() - (baseBox.getHeight() / 2), getY() - (baseBox.getWidth() / 2), baseBox.getHeight(), baseBox.getWidth());
		}
		else {
			hitbox.set(getX() - (baseBox.getWidth() / 2), getY() - (baseBox.getHeight() / 2), baseBox.getWidth(), baseBox.getHeight());
		}
		setOrigin(hitbox.getWidth() / 2, hitbox.getHeight());
	}
	@Override
	public ArrayList<Rectangle> getHitbox() {
		return hitboxes;
	}
	@Override
	public ArrayList<Rectangle> getHitbox(float x, float y) {
		Rectangle hitbox = tempHitboxes.get(0);
		int dir = (int)((velocity.angle() + 45) / 90) % 4;
		
		if (dir % 2 == 0) {
			hitbox.set(x, y, baseBox.getHeight(), baseBox.getWidth());
		}
		else {
			hitbox.set(x, y, baseBox.getWidth(), baseBox.getHeight());
		}
		setOrigin(hitbox.getWidth() / 2, hitbox.getHeight());
		return tempHitboxes;
	}
	@Override
	public Rectangle getViewbox() {
		Rectangle viewbox = new Rectangle(getX() - 8, getY() - 8, 16, 16);
		return viewbox;
	}

}
