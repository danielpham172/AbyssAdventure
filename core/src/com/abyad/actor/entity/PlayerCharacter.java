package com.abyad.actor.entity;

import java.util.ArrayList;

import com.abyad.actor.attack.AttackData;
import com.abyad.actor.attack.SpecialAttackData;
import com.abyad.actor.mapobjects.items.CarryingItem;
import com.abyad.actor.ui.MagicCursor;
import com.abyad.controls.PlayerController;
import com.abyad.data.HitEvent;
import com.abyad.game.Player;
import com.abyad.interfaces.Interactable;
import com.abyad.magic.AbstractMagic;
import com.abyad.relic.Relic;
import com.abyad.sprite.AbstractSpriteSheet;
import com.abyad.sprite.EntitySprite;
import com.abyad.utils.Assets;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class PlayerCharacter extends HumanoidEntity{

	private static ArrayList<PlayerCharacter> players = new ArrayList<PlayerCharacter>();		//List of all characters
	
	private Player player;			//The player object this character is associated to
	
	private Vector2 knockbackVelocity = new Vector2(0, 0);		//Velocity of knockback
	private int knockbackLength = 0;							//Length of knockback
	
	private boolean attackHeld = false;							//Boolean to check if the attack button is held
	private boolean specialHeld = false;						//Boolean to check if the special button is held
	private boolean attacking;									//Boolean to check if the character is in the middle of attacking
	private String weapon;										//The weapon being used (as we might have different weapons)
	private AttackData basicAttack;								//The basic attack of the character
	private String specialName;
	private SpecialAttackData specialAttack;
	
	private ArrayList<Interactable> interactableObjects = new ArrayList<Interactable>();
	private ArrayList<Relic> relics = new ArrayList<Relic>();
	private CarryingItem heldItem;
	
	private boolean magicHeld = false;
	private boolean rSwapHeld = false;
	private boolean lSwapHeld = false;
	private ArrayList<AbstractMagic> magicSpells = new ArrayList<AbstractMagic>();
	private AbstractMagic castingMagic;
	private MagicCursor cursor;
	private boolean casting;
	
	private int mp;
	private int partialMP;
	private int maxMP;
	
	private int gold;
	
	private final float MAX_SPEED = 2.5f;
	
	private int spawnInLength;
	
	public PlayerCharacter(Player player, float x, float y) {
		super(x, y);
		
		this.player = player;
		sprite = (EntitySprite)AbstractSpriteSheet.spriteSheets.get(player.getCharacterName());
		weapon = "SWORD";
		basicAttack = AttackData.basicAttacks.get(weapon);
		if (player.getNumber() == 1) {
			specialName = "SPIN_SLASH";
		}
		else {
			specialName = "WIND_BLADE";
		}
		specialAttack = AttackData.specialAttacks.get(specialName);
		magicSpells.add(AbstractMagic.magicList.get("MAGIC BOLT"));
		magicSpells.add(AbstractMagic.magicList.get("HEALING FIELD"));
		cursor = new MagicCursor(this);
		
		updateHitbox();
		maxHP = hp = 3;
		maxMP = mp = 3;
		players.add(this);
	}
	
	/**
	 * Where all the controls of the character lies.
	 */
	@Override
	public void act(float delta) {
		super.act(delta);
		if (!markForRemoval) {
			PlayerController controller = player.getController();
			//The following two lines are used to check the movement of the character. Note that the controller is returning a float
			//The values are getting squared just as a sensitivity thing.
			float xChange = (float)Math.pow(controller.rightPressed(), 2) - (float)Math.pow(controller.leftPressed(), 2);
			float yChange = (float)Math.pow(controller.upPressed(), 2) - (float)Math.pow(controller.downPressed(), 2);
			
			//Activate relic passives
			for (Relic relic : relics) {
				relic.update();
				if (!relic.isOnCooldown() && Math.random() < relic.getActivationRate()) {
					relic.onPassive(this);
				}
			}
			
			if (spawnInLength > 0) {
				spawnInLength--;
				height = spawnInLength * 2;
				velocity.set(1.0f, 0).setAngle(-90f * (int)((spawnInLength / 4) % 5));
				setState("FALLING");
				if (spawnInLength == 0) spawnIn();
			}
			//Knockback the player if currently in knockback
			else if (knockbackLength > 0) {
				move(knockbackVelocity);
				knockbackLength--;
			}
			else if (!attacking && !casting) {
				
				interactableObjects.clear();
				if (!player.isRingMenuActive()) {
					for (Interactable interactable : Interactable.interactables) {
						if (!(interactable instanceof CarryingItem) || !isHoldingItem()) {
							if (isOverlapping(interactable.getInteractBox(), getCollideBox())) {
								interactableObjects.add(interactable);
								interactable.setCanInteract(true);
							}
						}
					}
				}
				
				//If not in an attack animation
				if (controller.attackPressed() && !attackHeld) {
					if (!player.isRingMenuActive()) {
						//This checks to see if the player has pressed the attack button
						//First see if the player can interact with something
						boolean interactedWithSomething = false;
						for (Interactable interactable : interactableObjects) {
							if (interactable.interact(this)) {
								interactedWithSomething = true;
							}
						}
						//If no interactions happen, then try attacking or dropping item
						if (!interactedWithSomething) {
							if (!isHoldingItem()) {
								//If there is nothing to interact and not holding an item, just attack
								attacking = true;
								basicAttack.initiateAttack(this);
								setState("BASIC_ATTACK - " + weapon);	//Set the state to attacking
							}
							else {
								//Drop item holding
								dropItemOnGround();
							}
						}
					}
					else {
						int selection = player.getSelectedMagic();
						castingMagic = magicSpells.get(selection);
						if (getMana() >= castingMagic.getManaCost()) {
							removeMana(castingMagic.getManaCost());
							setState("CASTING");
							casting = true;
							cursor.spawnInCursor();
							player.toggleRingMenu();
						}
					}
				}
				else if (controller.specialPressed() && !specialHeld && !isHoldingItem() && getMana() >= specialAttack.getRequiredMana() && !player.isRingMenuActive()) {
					//This checks to see if the player has pressed the special button
					attacking = true;
					specialAttack.initiateAttack(this);
					setState("SPECIAL_ATTACK - " + specialName);
					removeMana(specialAttack.getRequiredMana());
				}
				else {
					if (xChange == 0 && yChange == 0) {
						//No movement? the character is idling
						if (!isHoldingItem()) {
							setState("IDLE");
						}
						else {
							setState("CARRYING_IDLE");
						}
					}
					else {
						//This happens if the character is moving, scale the change to match velocity
						if (!isHoldingItem()) {
							velocity.x = xChange * MAX_SPEED;
							velocity.y = yChange * MAX_SPEED;
							if (velocity.len() > MAX_SPEED) velocity.setLength(MAX_SPEED);	//Sometimes the velocity is over the actual max speed, so set it back
							setState("MOVE", velocity.len() / 2.0f);		//Set the state and use partial frames if the character is moving slow
							move(velocity);
						}
						else {
							//If carrying an item, slow by weight, a set correct state
							float slowedSpeed =  MAX_SPEED / (heldItem.getWeight() + 1.0f);
							velocity.x = xChange * slowedSpeed;
							velocity.y = yChange * slowedSpeed;
							if (velocity.len() > slowedSpeed) velocity.setLength(slowedSpeed);
							setState("CARRYING_MOVE", velocity.len() / 2.0f);		//Set the state and use partial frames if the character is moving slow
							move(velocity);
						}
					}
					
					if (controller.magicPressed() && !magicHeld && !isHoldingItem()) {
						player.toggleRingMenu();
					}
					if (player.isRingMenuActive()) {
						if (controller.rightSwapPressed() && !rSwapHeld) {
							player.rotateRingMenu(1);
						}
						if (controller.leftSwapPressed() && !lSwapHeld) {
							player.rotateRingMenu(-1);
						}
					}
				}
			}
			else if (attacking){
				//Sets the state into attacking
				if (state.contains("BASIC_ATTACK")) {
					setState("BASIC_ATTACK - " + weapon);
					basicAttack.useAttack(this, framesSinceLast);
					if (basicAttack.isFinishedAttacking(this, framesSinceLast)) attacking = false;		//Ends the attack after a certain amount of frames
				}
				else if (state.contains("SPECIAL_ATTACK")) {
					setState("SPECIAL_ATTACK - " + specialName);
					specialAttack.useAttack(this, framesSinceLast);
					if (specialAttack.isFinishedAttacking(this, framesSinceLast)) attacking = false;	//Ends the attack after a certain amount of frames
				}
			}
			else if (casting) {
				if (state.equals("CASTING")) {
					setState("CASTING");
					if (xChange != 0 || yChange != 0) {
						Vector2 move = new Vector2(xChange * MAX_SPEED, yChange * MAX_SPEED);
						if (move.len() > MAX_SPEED) velocity.setLength(MAX_SPEED);
						cursor.move(move.x, move.y);
					}
					velocity.set(cursor.getX() - getCenterX(), cursor.getY() - getCenterY()).setLength(0.5f);
					castingMagic.addsParticle(this);
					
					if (framesSinceLast > castingMagic.getCastTime() && !controller.attackPressed()) {
						Vector2 cursorPositon = getCursorPosition();
						castingMagic.castMagic(this, cursorPositon);
						setState("FINISH_CASTING");
					}
				}
				else if (state.equals("FINISH_CASTING")) {
					setState("FINISH_CASTING");
					if (framesSinceLast > castingMagic.getAfterTime()) {
						cursor.remove();
						casting = false;
					}
				}
			}
			attackHeld = controller.attackPressed();
			specialHeld = controller.specialPressed();
			magicHeld = controller.magicPressed();
			rSwapHeld = controller.rightSwapPressed();
			lSwapHeld = controller.leftSwapPressed();
		}
	}
	
	public void setSpawnInLength(int length) {
		spawnInLength = length;
	}
	
	public void spawnIn() {
		if (!players.contains(this)) players.add(this);
		if (!getEntities().contains(this)) getEntities().add(this);
		setState("IDLE");
		attacking = false;
		casting = false;
		knockbackLength = 0;
		invulnLength = 0;
		velocity.setToRandomDirection();
		updateHitbox();
	}
	
	public void resetCharacter() {
		relics.clear();
		modifyMaxHP(3 - getMaxHP());
		modifyMaxMana(3 - getMaxMana());
		gold = 0;
	}
	
	public boolean isSpawningIn() {
		return spawnInLength > 0;
	}
	
	public void pickupRelic(Relic relic) {
		relics.add(relic);
	}
	
	public ArrayList<Relic> getRelics(){
		return relics;
	}
	
	public boolean carryItem(CarryingItem carry) {
		if (!isHoldingItem()) {
			heldItem = carry;
			return true;
		}
		else return false;
	}
	
	public void dropItemOnGround() {
		heldItem.setPosition(getCenterX(), getCenterY());
		if (!state.equals("CARRYING_IDLE")) {
			heldItem.getVelocity().set(velocity);
		}
		else {
			heldItem.getVelocity().setLength(0);
		}
		getStage().addActor(heldItem);
		heldItem.spawn();
		heldItem = null;
	}
	
	public CarryingItem getHeldItem() {
		return heldItem;
	}
	
	public boolean isHoldingItem() {
		return heldItem != null;
	}
	
	public void removeHeldItem() {
		heldItem = null;
	}
	
	public int getMana() {
		return mp;
	}
	
	public int getPartialMana() {
		return partialMP;
	}
	
	public int getMaxMana() {
		return maxMP;
	}
	
	public void modifyMaxMana(int modification) {
		maxMP += modification;
		if (maxMP <= 0) maxMP = 1;
		if (maxMP < mp) {
			mp = maxMP;
			partialMP = 0;
		}
	}
	
	public void addMana(int add) {
		mp += add;
		if (mp > maxMP) mp = maxMP;
	}
	
	public void removeMana(int sub) {
		mp -= sub;
		if (mp < 0) mp = 0;
	}
	
	public void addPartialMana(int add) {
		if (mp < maxMP) {
			partialMP += add;
			while (partialMP >= 4) {
				addMana(1);
				partialMP -= 4;
			}
		}
	}
	
	public boolean isCursorActive() {
		return (cursor.getStage() != null);
	}
	
	public Vector2 getCursorPosition() {
		return new Vector2(cursor.getX(), cursor.getY());
	}
	
	public Vector2 getVisionCenter() {
		if (!isSpawningIn()) {
			Vector2 facing = new Vector2(velocity);
			facing.setLength(24.0f);
			return new Vector2(getCenterX() + facing.x, getCenterY() + facing.y);
		}
		else {
			return getCenter();
		}
	}
	
	public ArrayList<AbstractMagic> getMagicSpells(){
		return magicSpells;
	}
	
	public int getGold() {
		return gold;
	}
	
	public void modifyGold(int modification) {
		gold += modification;
	}
	
	public int getKnockbackLength() {
		return knockbackLength;
	}
	
	@Override
	public void draw(Batch batch, float a) {
		//drawHitbox(batch, a);
		if (inView() && state.equals("CASTING")) {
			castingMagic.drawMagic(batch, a, this, framesSinceLast);
		}
		super.draw(batch, a);
		if (inView()) {
			if (isHoldingItem()) {
				batch.draw(heldItem.getTexture(), getX() - getOriginX(), getY() + (getOriginY() / 4),
						getOriginX(), getOriginY(), heldItem.getTexture().getRegionWidth(), heldItem.getTexture().getRegionHeight(),
						getScaleX(), getScaleY(), getRotation());
			}
		}
	}
	
	/**
	 * Debug tool to draw hitboxes
	 * @param batch
	 * @param a
	 */
	public void drawHitbox(Batch batch, float a) {
		for (Rectangle hitbox : hitboxes) {
			batch.draw(Assets.redBox, hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
		}
		
		if (attacking) {
			if (state.contains("BASIC_ATTACK")) {
				for (Rectangle hurtbox : basicAttack.getHurtboxes(this, framesSinceLast)) {
					batch.draw(Assets.greenBox, hurtbox.getX(), hurtbox.getY(), hurtbox.getWidth(), hurtbox.getHeight());
				}
			}
			else if (state.contains("SPECIAL_ATTACK")) {
				for (Rectangle hurtbox : specialAttack.getHurtboxes(this, framesSinceLast)) {
					batch.draw(Assets.greenBox, hurtbox.getX(), hurtbox.getY(), hurtbox.getWidth(), hurtbox.getHeight());
				}
			}
		}
	}
	
	/**
	 * The team, all players are on the same team so they can't hurt each other.
	 */
	@Override
	public String getTeam() {
		return "PLAYERS";
	}
	
	public void updateSpriteSheet() {
		sprite = (EntitySprite)AbstractSpriteSheet.spriteSheets.get(player.getCharacterName());
	}
	
	/**
	 * Method overriden to set knockback and invuln period for the player. Also to lower hp
	 */
	@Override
	public void takeDamage(HitEvent event) {
		if (!isInvuln()) {
			
			//Activate relic defense effects (modifies the event)
			for (Relic relic : relics) {
				if (!relic.isOnCooldown() && Math.random() < relic.getActivationRate()) {
					relic.onDefense(this, event);
				}
			}
			if (isHoldingItem()) {
				dropItemOnGround();
			}
			
			knockbackVelocity = event.getKnockbackVelocity();
			knockbackLength = event.getKnockbackLength();
			modifyHP(-event.getDamage());
			invulnLength = 40;
			
			if (casting) {
				casting = false;
				cursor.remove();
			}
		}
	}
	
	@Override
	public boolean remove() {
		players.remove(this);		//Removes this player from the array
		return super.remove();
	}
	
	public static ArrayList<PlayerCharacter> getPlayers(){
		return players;
	}

}
