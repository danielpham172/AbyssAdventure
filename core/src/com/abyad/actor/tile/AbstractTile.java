package com.abyad.actor.tile;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * The AbstractTile class is used for drawing in the map
 *
 */
public abstract class AbstractTile extends Actor{
	
	private TextureRegion tex;			//The texture of the tile
	private int row;					//What row the tile is in on the map
	private int col;					//What column the tile is in on the map
	
	public static final int TILE_SIZE = 32;			//The size of the texture
	public static final float TILE_SCALE = 0.75f;	//How much to scale the texture
	public static final float TILE_LENGTH = TILE_SIZE * TILE_SCALE;		//The new length of the tile
	
	private static Texture box;		//Texture used for debugging hitboxes for the tile
	static {
		Pixmap pixmap = new Pixmap((int)(TILE_LENGTH), (int)(TILE_LENGTH), Format.RGBA8888 );
		pixmap.setColor( 1, 0, 0, 0.25f );
		pixmap.fill();
		box = new Texture( pixmap );
		pixmap.dispose();
	}
	
	private Rectangle hitbox;		//The hitbox of the tile (the rectangle surrounding it)
	
	/**
	 * Constructor to set the texture, row, and column of the tile. It will autogenerate the x and y given the row and column
	 * @param tex		Texture of the tile
	 * @param row		Row of the tile
	 * @param col		Column of the tile
	 */
	public AbstractTile(TextureRegion tex, int row, int col) {
		this.tex = tex;
		this.row = row;
		this.col = col;
		hitbox = new Rectangle(col * TILE_LENGTH, row * TILE_LENGTH, TILE_LENGTH, TILE_LENGTH);
		setX(col * TILE_LENGTH);
		setY(row * TILE_LENGTH);
	}
	
	/**
	 * Same as above, but allows to rotate the texture of the tile
	 * @param tex		Texture of the tile
	 * @param row		Row of the tile
	 * @param col		Column of the tile
	 * @param rotation	Rotation of the tile texture (should be increments of 90)
	 */
	public AbstractTile(TextureRegion tex, int row, int col, float rotation) {
		this(tex, row, col);
		setRotation(rotation);
	}
	
	@Override
	public void act(float delta) {
		//Do nothing
	}
	
	@Override
	public void draw(Batch batch, float a) {
		if (tex != null && inView()) {
			batch.draw(tex, getX() - ((TILE_SIZE - TILE_LENGTH) / 2), getY() - ((TILE_SIZE - TILE_LENGTH) / 2), TILE_SIZE / 2, TILE_SIZE / 2,
					TILE_SIZE, TILE_SIZE, TILE_SCALE, TILE_SCALE, getRotation());
		}
		//drawHitbox(batch, a);
	}
	
	/**
	 * Checks if the tile is in view of the camera for drawing
	 * @return		True if the tile is inside the camera, flase otherwise
	 */
	public boolean inView() {
		return getStage().getCamera().frustum.boundsInFrustum(getCenter().x, getCenter().y, 0,
				TILE_LENGTH / 2, TILE_LENGTH / 2, 0);
	}
	
	/**
	 * Draws the hitbox of the tile. Unlike the entity drawHitbox methods, this one is optimized
	 * @param batch
	 * @param a
	 */
	public void drawHitbox(Batch batch, float a) {
		Rectangle hitbox = getBox();
		
		batch.draw(box, hitbox.getX(), hitbox.getY());
	}
	
	/**
	 * Gets the center position of the tile as a Vector2
	 * @return
	 */
	public Vector2 getCenter() {
		float centerOffset = TILE_LENGTH / 2;
		return new Vector2(getX() + centerOffset, getY() + centerOffset);
	}
	/**
	 * Returns the hitbox of the tile
	 * @return
	 */
	public Rectangle getBox() {
		return hitbox;
	}
}
