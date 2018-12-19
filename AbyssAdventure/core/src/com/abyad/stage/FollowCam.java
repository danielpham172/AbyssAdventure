package com.abyad.stage;

import java.util.ArrayList;

import com.abyad.actor.entity.PlayerCharacter;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class FollowCam extends OrthographicCamera{

	public FollowCam() {
		super();
	}
	
	@Override
	public void update() {
		setFollowing();
		super.update();
	}
	
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
