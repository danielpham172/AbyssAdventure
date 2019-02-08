package com.abyad.usables;

import java.util.LinkedHashMap;

import com.abyad.actor.entity.PlayerCharacter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class InventoryItem {

	private String name;
	private String desc;
	private TextureRegion tex;
	
	private int count;
	
	private static LinkedHashMap<Class, Integer> inventoryItemClasses = new LinkedHashMap<Class, Integer>();
	private static LinkedHashMap<Class, Integer> inventoryRareItemClasses = new LinkedHashMap<Class, Integer>();
	private static int totalWeight = 0;
	private static int rareTotalWeight = 0;
	static {
		inventoryItemClasses.put(PuddingItem.class, 35);
		inventoryItemClasses.put(DeluxePuddingItem.class, 12);
		inventoryItemClasses.put(UltimatePuddingItem.class, 3);
		
		for (Class key : inventoryItemClasses.keySet()) {
			totalWeight += inventoryItemClasses.get(key);
		}
		
		inventoryRareItemClasses.put(DeluxePuddingItem.class, 2);
		inventoryRareItemClasses.put(UltimatePuddingItem.class, 1);
		for (Class key : inventoryRareItemClasses.keySet()) {
			rareTotalWeight += inventoryRareItemClasses.get(key);
		}
	}
	
	public InventoryItem(String name, String desc, TextureRegion tex) {
		this.name = name;
		this.desc = desc;
		this.tex = tex;
		count = 1;
	}
	
	public abstract boolean canUse(PlayerCharacter player);
	public abstract void use(PlayerCharacter player);
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return desc;
	}
	
	public TextureRegion getTexture() {
		return tex;
	}
	
	public int getCount() {
		return count;
	}
	
	public void incrementCount() {
		count += 1;
	}
	
	public void decrementCount() {
		count -= 1;
	}
	
	public static InventoryItem createRandomItem() {
		try {
			int weightChoice = (int)(Math.random() * totalWeight);
			for (Class key : inventoryItemClasses.keySet()) {
				weightChoice -= inventoryItemClasses.get(key);
				if (weightChoice < 0) {
					InventoryItem randomItem = (InventoryItem)key.newInstance();
					return randomItem;
				}
			}
			return (InventoryItem)inventoryItemClasses.keySet().iterator().next().newInstance();
		} catch (InstantiationException e) {
			// Needs a default constructor;
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			// Maybe because constructor can't be accessed?
			e.printStackTrace();
			return null;
		}
	}
	
	public static InventoryItem createRandomRareItem() {
		try {
			int weightChoice = (int)(Math.random() * rareTotalWeight);
			for (Class key : inventoryRareItemClasses.keySet()) {
				weightChoice -= inventoryRareItemClasses.get(key);
				if (weightChoice < 0) {
					InventoryItem randomItem = (InventoryItem)key.newInstance();
					return randomItem;
				}
			}
			return (InventoryItem)inventoryRareItemClasses.keySet().iterator().next().newInstance();
		} catch (InstantiationException e) {
			// Needs a default constructor;
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			// Maybe because constructor can't be accessed?
			e.printStackTrace();
			return null;
		}
	}
}
