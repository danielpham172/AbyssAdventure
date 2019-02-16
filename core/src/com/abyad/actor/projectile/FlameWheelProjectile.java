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

public class FlameWheelProjectile extends AbstractProjectile{

	private ArrayList<Rectangle> hitboxes = new ArrayList<Rectangle>();
	private ArrayList<Rectangle> tempHitboxes = new ArrayList<Rectangle>();
	
	//Hitbox Rectangles		length		xOff	yOff (measured center to center)
	/**
	 * 						15			-2.5	0.5
	 * 						11			9.5		3.5
	 * 						11			9.5		-3.5
	 * 						6			-10		-3
	 * 						4			-13		-7
	 */
	
	private static ArrayList<Rectangle> baseHitboxes = new ArrayList<Rectangle>();
	static {
		baseHitboxes.add(new Rectangle(-2.5f, 0.5f, 15, 15));
		baseHitboxes.add(new Rectangle(9.5f, 3.5f, 11, 11));
		baseHitboxes.add(new Rectangle(9.5f, -3.5f, 11, 11));
		baseHitboxes.add(new Rectangle(-10, -3, 6, 6));
		baseHitboxes.add(new Rectangle(-13, -7, 4, 4));
	}
	
	private float centerX;
	private float centerY;
	private float speed;
	private float angleChange;
	
	private static float STANDARD_RADIUS = 32.0f;
	
	public FlameWheelProjectile(float x, float y, AbstractEntity source, float centerX, float centerY, Vector2 velocity) {
		super(x, y, source);
		
		this.centerX = centerX;
		this.centerY = centerY;
		this.speed = velocity.len();
		this.angleChange = velocity.angle();
		
		this.velocity = new Vector2(1, 0);
		updateVelocity();
		
		hitboxes = new ArrayList<Rectangle>();
		tempHitboxes = new ArrayList<Rectangle>();
		
		for (Rectangle baseHitbox : baseHitboxes) {
			hitboxes.add(new Rectangle(baseHitbox));
			tempHitboxes.add(new Rectangle(baseHitbox));
		}
		updateHitbox();
		
		sprite = (ProjectileSprite)AbstractSpriteSheet.spriteSheets.get("FLAME_WHEEL_PROJECTILE");
		setRotation(velocity.angle());
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
								relic.onMagicHit(player, AbstractMagic.magicList.get("FLAME WHEEL"), event, entity);
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
		updateVelocity();
		super.act(delta);
	}
	
	public void updateVelocity() {
		velocity.set(getX() - centerX, getY() - centerY);
		velocity.setAngle(velocity.angle() - 90f + angleChange);
		velocity.setLength(speed * (velocity.len() / STANDARD_RADIUS));
		setRotation(velocity.angle());
	}
	
	@Override
	public void updateHitbox() {
		for (int i = 0; i < hitboxes.size(); i++) {
			Rectangle hitbox = hitboxes.get(i);
			Vector2 offsets = new Vector2(baseHitboxes.get(i).x, baseHitboxes.get(i).y);
			offsets.rotate(getRotation());
			hitbox.setCenter(getX() + offsets.x, getY() + offsets.y);
		}
	}
	
	@Override
	public ArrayList<Rectangle> getHitbox() {
		return hitboxes;
	}
	
	@Override
	public ArrayList<Rectangle> getHitbox(float x, float y) {
		for (int i = 0; i < tempHitboxes.size(); i++) {
			Rectangle hitbox = tempHitboxes.get(i);
			Vector2 offsets = new Vector2(baseHitboxes.get(i).x, baseHitboxes.get(i).y);
			offsets.rotate(getRotation());
			hitbox.setCenter(x + offsets.x, + offsets.y);
		}
		return tempHitboxes;
	}
	
	@Override
	public Rectangle getViewbox() {
		Rectangle viewbox = new Rectangle(getX() - 16, getY() - 16, 32, 32);
		return viewbox;
	}
	
}
