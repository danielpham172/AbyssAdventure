package com.abyad.actor.mapobjects.items;

import java.util.ArrayList;

import com.abyad.actor.entity.PlayerCharacter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class CorpseItem extends CarryingItem{

	private PlayerCharacter player;
	
	private ArrayList<Rectangle> interactBox;
	private ArrayList<Rectangle> temporaryCollideBox;
	
	private static Rectangle baseBox = new Rectangle(0, 0, 16, 16);
	
	private boolean markForRemoval;
	
	public CorpseItem(float x, float y, Vector2 velocity, PlayerCharacter player) {
		super(x, y, velocity, player.getSprite().getSprite("char_f_dead"), player.getSprite().getSprite("char_r_dead"),
				player.getSprite().getSprite("char_l_dead"), player.getSprite().getSprite("char_b_dead"), 2.0f);
		this.player = player;
		
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
	public Vector2 getOffsetDraw(Vector2 direction) {
		int d = (int)((direction.angle() + 45f) / 90f) % 4;
		if (d == 0) {
			return new Vector2(0, (getOriginY() * 1.35f));
		}
		else if (d == 1){
			return new Vector2(0, (getOriginY() * 1.00f));
		}
		else if (d == 2){
			return new Vector2(0, (getOriginY() * 1.35f));
		}
		else{
			return new Vector2(0, (getOriginY() * 1.20f));
		}
	}
	
	@Override
	public ArrayList<Rectangle> getInteractBox() {
		return interactBox;
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
