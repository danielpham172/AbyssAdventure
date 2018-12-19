package com.abyad.stage;

import java.util.ArrayList;

import com.abyad.actor.entity.PlayerCharacter;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * The FollowCam class is a camera for the play stage to always follow the player around
 *
 */
public class FollowCam extends OrthographicCamera{

	public FollowCam() {
		super();
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
			maxX = Math.max(player.getX(), maxX);
			maxY = Math.max(player.getY(), maxY);
			minX = Math.min(player.getX(), minX);
			minY = Math.min(player.getY(), minY);
		}
		
		if (players.size() > 0) {
			float centerX = (maxX + minX) / 2;
			float centerY = (maxY + minY) / 2;
			
			super.position.x = centerX;
			super.position.y = centerY;
		}
	}
}
