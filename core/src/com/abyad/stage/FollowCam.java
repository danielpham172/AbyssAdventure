package com.abyad.stage;

import java.util.ArrayList;

import com.abyad.actor.entity.PlayerCharacter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

/**
 * The FollowCam class is a camera for the play stage to always follow the player around
 *
 */
public class FollowCam extends OrthographicCamera{
	
	private boolean firstSetup;
	
	private static final float MAX_CAMERA_MOVE = 20.0f;
	private static final float MIN_CAMERA_MOVE = 0.05f;
	private static final float SLOWDOWN_RADIUS = 200.0f;
	private static final float SLOWING_SENSITIVITY = 1.5f;
	
	public FollowCam() {
		super();
		firstSetup = true;
	}
	
	public FollowCam(float zoom) {
		super();
		super.zoom = zoom;
		firstSetup = true;
	}
	
	/**
	 * This is called within the stage so that the camera continually updates its view
	 */
	@Override
	public void update() {
		setFollowing();
		super.update();
	}
	
	/**
	 * Method used to find where to center the camera. It will center in in between all the players.
	 */
	private void setFollowing() {
		ArrayList<PlayerCharacter> players = PlayerCharacter.getPlayers();
		float maxX = -Float.MAX_VALUE;
		float maxY = -Float.MAX_VALUE;
		float minX = Float.MAX_VALUE;
		float minY = Float.MAX_VALUE;
		for (PlayerCharacter player : players) {
//			maxX = Math.max(player.getCenterX(), maxX);
//			maxY = Math.max(player.getCenterY(), maxY);
//			minX = Math.min(player.getCenterX(), minX);
//			minY = Math.min(player.getCenterY(), minY);
			
			Vector2 visionCenter = player.getVisionCenter();
			maxX = Math.max(visionCenter.x, maxX);
			maxY = Math.max(visionCenter.y, maxY);
			minX = Math.min(visionCenter.x, minX);
			minY = Math.min(visionCenter.y, minY);
			
			if (player.isCursorActive()) {
				Vector2 cursorPosition = player.getCursorPosition();
				maxX = Math.max(cursorPosition.x, maxX);
				maxY = Math.max(cursorPosition.y, maxY);
				minX = Math.min(cursorPosition.x, minX);
				minY = Math.min(cursorPosition.y, minY);
			}
		}
		
		if (players.size() > 0) {
			float centerX = (maxX + minX) / 2;
			float centerY = (maxY + minY) / 2;
			
			if (firstSetup) {
				super.position.x = centerX;
				super.position.y = centerY;
				firstSetup = false;
			}
			else {
				Vector2 move = new Vector2(centerX - super.position.x, centerY - super.position.y);
				if (move.len() > SLOWDOWN_RADIUS) {
					float length = Math.min(MAX_CAMERA_MOVE, move.len());
					move.setLength(length);
				}
				else {
					float length = Math.min(MAX_CAMERA_MOVE * (float)Math.pow(move.len() / SLOWDOWN_RADIUS, SLOWING_SENSITIVITY),
							move.len());
					length = Math.max(length, MIN_CAMERA_MOVE);
					move.setLength(length);
				}
				super.position.x += move.x;
				super.position.y += move.y;
			}
		}
	}
}
