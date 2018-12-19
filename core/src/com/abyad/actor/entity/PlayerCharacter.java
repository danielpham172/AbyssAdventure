package com.abyad.actor.entity;

import java.util.ArrayList;

import com.abyad.controls.PlayerController;
import com.abyad.game.Player;
import com.abyad.sprite.PlayerSprite;
import com.abyad.utils.Assets;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PlayerCharacter extends HumanoidEntity{

	private static ArrayList<PlayerCharacter> players = new ArrayList<PlayerCharacter>();
	
	private Player player;
	
	private Vector2 knockbackVelocity = new Vector2(0, 0);
	private int knockbackLength = 0;
	
	private boolean attackHeld = false;
	private boolean attacking;
	private String weapon;
	
	public PlayerCharacter(Player player) {
		super();
		
		this.player = player;
		if (player.getNumber() == 1) {
			sprite = new PlayerSprite(Assets.manager.get(Assets.girl1));
		}
		else {
			sprite = new PlayerSprite(Assets.manager.get(Assets.boy1));
		}
		weapon = "SWORD";
		updateHitbox();
		
		players.add(this);
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		PlayerController controller = player.getController();
		float xChange = (float)Math.pow(controller.rightPressed(), 2) - (float)Math.pow(controller.leftPressed(), 2);
		float yChange = (float)Math.pow(controller.downPressed(), 2) - (float)Math.pow(controller.upPressed(), 2);
		
		if (knockbackLength > 0) {
			move(knockbackVelocity);
			knockbackLength--;
		}
		else if (!attacking) {
			if (controller.attackPressed() && !attackHeld) {
				attacking = true;
				if (state.equals("IDLE")) {
					velocity.setAngle(((int)(velocity.angle() + 45) / 90) * 90.0f);
					velocity.setLength(1.5f);
				}
				setState("ATTACK - " + weapon);
			}
			else if (xChange == 0 && yChange == 0) {
				setState("IDLE");
			}
			else {
				velocity.x = xChange * 2.5f;
				velocity.y = yChange * 2.5f;
				if (velocity.len() > 2.5f) velocity.setLength(2.5f);
				setState("MOVE", velocity.len() / 2.0f);
				//System.out.println(xChange + "," + yChange);
				move(velocity);
			}
		}
		else {
			setState("ATTACK - " + weapon);
			if (weapon.equals("SWORD")) {
				velocity.setLength(velocity.len() / 1.08f);
				move(velocity);
				checkCollisions();
				if (framesSinceLast >= 30) attacking = false;
			}
		}
		attackHeld = controller.attackPressed();
	}
	
	public void checkCollisions() {
		ArrayList<Rectangle> hurtboxes = getHurtbox();
		ArrayList<AbstractEntity> entities = AbstractEntity.getEntities();
		for (AbstractEntity entity : entities) {
			if (!isSameTeam(entity)) {
				ArrayList<Rectangle> otherHitbox = entity.getHitbox();
				if (isHit(hurtboxes, otherHitbox)) {
					Vector2 knockback = new Vector2(entity.getCenterX() - getCenterX(), entity.getCenterY() - getCenterY());
					knockback.setLength(4.0f);
					entity.takeDamage(this, 0, knockback, 8);
				}
			}
		}
	}
	
	public boolean isHit(ArrayList<Rectangle> hurtboxes, ArrayList<Rectangle> others) {
		for (Rectangle other : others) {
			for (Rectangle hurtbox : hurtboxes) {
				if (other.overlaps(hurtbox)) return true;
			}
		}
		return false;
	}
	
	@Override
	public void draw(Batch batch, float a) {
		//drawHitbox(batch, a);
		super.draw(batch, a);
	}
	
	public void drawHitbox(Batch batch, float a) {
		for (Rectangle hitbox : hitboxes) {
			Pixmap pixmap = new Pixmap((int)hitbox.getWidth(), (int)hitbox.getHeight(), Format.RGBA8888 );
			pixmap.setColor( 1, 0, 0, 0.25f );
			pixmap.fill();
			Texture box = new Texture( pixmap );
			pixmap.dispose();
			
			batch.draw(box, hitbox.getX(), hitbox.getY());
		}
		
		for (Rectangle hurtbox : getHurtbox()) {
			Pixmap pixmap = new Pixmap((int)hurtbox.getWidth(), (int)hurtbox.getHeight(), Format.RGBA8888 );
			pixmap.setColor( 0, 1, 0, 0.25f );
			pixmap.fill();
			Texture box = new Texture( pixmap );
			pixmap.dispose();
			
			batch.draw(box, hurtbox.getX(), hurtbox.getY());
		}
	}
	
	@Override
	public String getTeam() {
		return "PLAYERS";
	}
	
	@Override
	public void takeDamage(Actor source, int damage, Vector2 knockback, int kbLength) {
		if (!isInvuln()) {
			knockbackVelocity = knockback.cpy();
			knockbackLength = kbLength;
			invulnLength = 40;
		}
	}
	
	
	public ArrayList<Rectangle> getHurtbox(){
		ArrayList<Rectangle> hurtboxes = new ArrayList<Rectangle>();
		if (state.equals("ATTACK - SWORD")) {
			int[] attackLengths = {10, 14, 18, 30};
			int frame = 0;
			int dir = (int)((velocity.angle() + 45) / 90) % 4; //0 - Right, 1 - Back, 2 - Left, 3 - Front
			float xOffset = 0;
			float yOffset = 0;
			while (frame < 4 && framesSinceLast >= attackLengths[frame]) {
				frame++;
			}
			if (frame >= 4) frame = 3;
			
			if (frame == 1) {
				//Left - Right
				Rectangle hurtbox;
				if (dir % 2 == 0) {
					hurtbox = new Rectangle(getCenterX() - 8, getCenterY() - 5, 16, 10);
					xOffset = (1 - dir) * 4.5f;
					yOffset = (dir - 1) * 6;
				}
				//Front - Back
				else {
					hurtbox = new Rectangle(getCenterX() - 5, getCenterY() - 8, 10, 16);
					xOffset = (2 - dir) * 6;
					yOffset = (2 - dir) * 4.5f;
				}
				hurtbox.setPosition(hurtbox.getX() + xOffset, hurtbox.getY() + yOffset);
				hurtboxes.add(hurtbox);
			}
			
			if (frame == 2) {
				//Left - Right
				Rectangle hurtbox;
				if (dir % 2 == 0) {
					hurtbox = new Rectangle(getCenterX() - 6, getCenterY() - 10, 12, 20);
					xOffset = (1 - dir) * 8.0f;
				}
				//Front - Back
				else {
					hurtbox = new Rectangle(getCenterX() - 10, getCenterY() - 6, 20, 12);
					yOffset = (2 - dir) * 8.0f;
				}
				hurtbox.setPosition(hurtbox.getX() + xOffset, hurtbox.getY() + yOffset);
				hurtboxes.add(hurtbox);
			}
			return hurtboxes;
		}
		return hurtboxes;
	}
	
	@Override
	public boolean remove() {
		players.remove(this);
		return super.remove();
	}
	
	public static ArrayList<PlayerCharacter> getPlayers(){
		return players;
	}

}
