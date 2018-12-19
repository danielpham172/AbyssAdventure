package com.abyad.actor.tile;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class AbstractTile extends Actor{
	
	private TextureRegion tex;
	private int row;
	private int col;
	
	public static final int TILE_SIZE = 32;
	public static final float TILE_SCALE = 0.75f;
	public static final float TILE_LENGTH = TILE_SIZE * TILE_SCALE;
	
	private static Texture box;
	static {
		Pixmap pixmap = new Pixmap((int)(TILE_LENGTH), (int)(TILE_LENGTH), Format.RGBA8888 );
		pixmap.setColor( 1, 0, 0, 0.25f );
		pixmap.fill();
		box = new Texture( pixmap );
		pixmap.dispose();
	}
	
	private Rectangle hitbox;
	
	public AbstractTile(TextureRegion tex, int row, int col) {
		this.tex = tex;
		this.row = row;
		this.col = col;
		hitbox = new Rectangle(col * TILE_LENGTH, row * TILE_LENGTH, TILE_LENGTH, TILE_LENGTH);
		setX(col * TILE_LENGTH);
		setY(row * TILE_LENGTH);
	}
	
	public AbstractTile(TextureRegion tex, int row, int col, float rotation) {
		this.tex = tex;
		this.row = row;
		this.col = col;
		hitbox = new Rectangle(col * TILE_LENGTH, row * TILE_LENGTH, TILE_LENGTH, TILE_LENGTH);
		setX(col * TILE_LENGTH);
		setY(row * TILE_LENGTH);
		setRotation(rotation);
	}
	
	@Override
	public void act(float delta) {
		//Do nothing
	}
	
	@Override
	public void draw(Batch batch, float a) {
		if (inView()) {
			batch.draw(tex, getX() - ((TILE_SIZE - TILE_LENGTH) / 2), getY() - ((TILE_SIZE - TILE_LENGTH) / 2), TILE_SIZE / 2, TILE_SIZE / 2,
					TILE_SIZE, TILE_SIZE, TILE_SCALE, TILE_SCALE, getRotation());
		}
		//drawHitbox(batch, a);
	}
	
	public boolean inView() {
		return getStage().getCamera().frustum.boundsInFrustum(getCenter().x, getCenter().y, 0,
				TILE_LENGTH / 2, TILE_LENGTH / 2, 0);
	}
	
	public void drawHitbox(Batch batch, float a) {
		Rectangle hitbox = getBox();
		
		batch.draw(box, hitbox.getX(), hitbox.getY());
	}
	
	public Vector2 getCenter() {
		float centerOffset = TILE_LENGTH / 2;
		return new Vector2(getX() + centerOffset, getY() + centerOffset);
	}
	public Rectangle getBox() {
		return hitbox;
	}
}
