package com.abyad.interfaces;

import java.util.ArrayList;

import com.abyad.actor.entity.PlayerCharacter;
import com.badlogic.gdx.math.Rectangle;

public interface Interactable {

	public static ArrayList<Interactable> interactables = new ArrayList<Interactable>();
	
	public ArrayList<Rectangle> getInteractBox();
	public boolean interact(PlayerCharacter source);
	public void setCanInteract(boolean flag);
}
