package com.abyad.actor.mapobjects.items.loot;

import java.util.ArrayList;

import com.abyad.actor.cosmetic.BattleText;
import com.abyad.actor.entity.PlayerCharacter;
import com.abyad.magic.AbstractMagic;
import com.abyad.sprite.AbstractSpriteSheet;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MagicScroll extends LootItem {

	private ArrayList<Rectangle> interactBox;
	private ArrayList<Rectangle> temporaryCollideBox;
	
	private AbstractMagic magic;
	private static Rectangle baseBox = new Rectangle(0, 0, 16, 16);
	
	private boolean markForRemoval;
	
	private static TextureRegion scrollTex = AbstractSpriteSheet.spriteSheets.get("SCROLL").getSprite("SCROLL");
	private static float iconScale = 10f / 16f;
	
	public MagicScroll(float x, float y, Vector2 velocity, AbstractMagic magic) {
		super(x, y, velocity, magic.getIcon());
		this.magic = magic;
		
		interactBox = new ArrayList<Rectangle>();
		temporaryCollideBox = new ArrayList<Rectangle>();
		
		Rectangle box = new Rectangle(baseBox);
		Rectangle tempBox = new Rectangle(baseBox);
		
		interactBox.add(box);
		temporaryCollideBox.add(tempBox);
		
		updateCollideAndInteractBox();
	}
	public MagicScroll(float x, float y, Vector2 velocity, String magic) {
		this(x, y, velocity, AbstractMagic.magicList.get(magic));
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		if (markForRemoval) remove();
	}
	@Override
	public void draw(Batch batch, float a) {
		if (isInteractable) {
			setScale(1.35f);
		}
		else {
			setScale(1.0f);
		}
		
		if (inView()) {
			batch.draw(scrollTex, getX() - getOriginX(), getY() - getOriginY(),
					getOriginX(), getOriginY(), tex.getRegionWidth(), tex.getRegionHeight(),
					getScaleX(), getScaleY(), getRotation());
			batch.draw(tex, getX() - getOriginX(), getY() - getOriginY(),
					getOriginX(), getOriginY(), tex.getRegionWidth(), tex.getRegionHeight(),
					getScaleX() * iconScale, getScaleY() * iconScale, getRotation());
		}
		
		isInteractable = false;
	}
	@Override
	public ArrayList<Rectangle> getInteractBox() {
		return interactBox;
	}

	@Override
	public boolean interact(PlayerCharacter source) {
		if (source.hasSpell(magic)) {
			Vector2 upVelocity = new Vector2(0, 1.2f);
			BattleText text = new BattleText("Already know spell!", getX(), getY(), upVelocity.cpy(), 1.0f, 30, false);
			text.setScale(0.25f);
			getStage().addActor(text);
			return false;
		}
		else {
			source.addSpell(magic);
			Vector2 upVelocity = new Vector2(0, 1);
			BattleText nameText = new BattleText("Learned: " + magic.getName(), getX(), getY() + 8f, upVelocity.cpy(), 0.98f, 180, true);
			nameText.setScale(0.4f);
			BattleText descText = new BattleText(magic.getDescription(), getX(), getY() - 8f, upVelocity.cpy(), 0.98f, 180, true);
			descText.setScale(0.2f);
			getStage().addActor(nameText);
			getStage().addActor(descText);
			markForRemoval = true;
			return true;
		}
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
