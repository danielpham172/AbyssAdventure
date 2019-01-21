package com.abyad.actor.cosmetic;

import com.abyad.utils.Assets;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class DisplayText extends Actor{

	private String text;
	private float transparency;
	
	private static BitmapFont font = Assets.manager.get(Assets.font);
	
	private GlyphLayout glyphText = new GlyphLayout();
	
	public DisplayText(String text) {
		setText(text);
		setTransparency(1.0f);
	}
	
	public DisplayText(String text, float x, float y) {
		setText(text);
		setX(x);
		setY(y);
		setTransparency(1.0f);
	}
	
	public void setText(String text) {
		this.text = text;
		font.getData().setScale(getScaleX());
		font.setColor(1.0f, 1.0f, 1.0f, transparency);
		glyphText.setText(font, text);
		font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		font.getData().setScale(1.0f);
	}
	
	public void setTransparency(float transparency) {
		this.transparency = transparency;
		font.getData().setScale(getScaleX());
		font.setColor(1.0f, 1.0f, 1.0f, transparency);
		glyphText.setText(font, text);
		font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		font.getData().setScale(1.0f);
	}
	
	@Override
	public void setScale(float scaleXY) {
		super.setScale(scaleXY);
		font.getData().setScale(getScaleX());
		glyphText.setText(font, text);
		font.getData().setScale(1.0f);
	}
	
	@Override
	public void draw(Batch batch, float a) {
		if (inView()) {
			float x = getX() - (glyphText.width / 2);
			float y = getY() - (glyphText.height / 2);
			font.getData().setScale(getScaleX());
			font.setColor(1.0f, 1.0f, 1.0f, transparency);
			font.draw(batch, glyphText, x, y);
			font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
			font.getData().setScale(1.0f);
		}
	}
	
	public boolean inView() {
		float x = getX() - (glyphText.width / 2);
		float y = getY() - (glyphText.height / 2);
		return getStage().getCamera().frustum.boundsInFrustum(x, y, 0,
				glyphText.width / 2, glyphText.height / 2, 0);
	}
}
