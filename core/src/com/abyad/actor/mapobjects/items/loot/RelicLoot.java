package com.abyad.actor.mapobjects.items.loot;

import java.util.ArrayList;

import com.abyad.actor.cosmetic.BattleText;
import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.relic.Relic;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class RelicLoot extends LootItem{

	private ArrayList<Rectangle> interactBox;
	private ArrayList<Rectangle> temporaryCollideBox;
	
	private Relic relic;
	private static Rectangle baseBox = new Rectangle(0, 0, 16, 16);
	
	private boolean markForRemoval;
	
	public RelicLoot(float x, float y, Vector2 velocity, Relic relic) {
		super(x, y, velocity, relic.getTexture());
		this.relic = relic;
		
		interactBox = new ArrayList<Rectangle>();
		temporaryCollideBox = new ArrayList<Rectangle>();
		
		Rectangle box = new Rectangle(baseBox);
		Rectangle tempBox = new Rectangle(baseBox);
		
		interactBox.add(box);
		temporaryCollideBox.add(tempBox);
		
		updateCollideAndInteractBox();
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		if (markForRemoval) remove();
	}
	@Override
	public ArrayList<Rectangle> getInteractBox() {
		return interactBox;
	}

	@Override
	public boolean interact(PlayerCharacter source) {
		if (!markForRemoval) {
			source.pickupRelic(relic);
			Vector2 upVelocity = new Vector2(0, 1);
			BattleText nameText = new BattleText(relic.getName(), getX(), getY() + 8f, upVelocity.cpy(), 0.98f, 180, true);
			nameText.setScale(0.4f);
			BattleText descText = new BattleText(relic.getDescription(), getX(), getY() - 8f, upVelocity.cpy(), 0.98f, 180, true);
			descText.setScale(0.2f);
			getStage().addActor(nameText);
			getStage().addActor(descText);
		}
		markForRemoval = true;
		return true;
	}

	@Override
	public void updateCollideAndInteractBox() {
		Rectangle box = interactBox.get(0);
		box.setPosition(getX() - (box.getWidth() / 2), getY() - (box.getHeight() / 2));
	}

	@Override
	public ArrayList<Rectangle> getCollideBox() {
		return interactBox;
	}

	@Override
	public ArrayList<Rectangle> getCollideBox(float x, float y) {
		temporaryCollideBox.get(0).setPosition(x - (temporaryCollideBox.get(0).getWidth() / 2), y - (temporaryCollideBox.get(0).getHeight() / 2));
		return temporaryCollideBox;
	}

}
