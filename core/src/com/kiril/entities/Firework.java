package com.kiril.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Firework {

	final float FRAME_LENGHT = 0.07f;
	final int OFFSET = 0;
	final int SIZE = 100;
	
	Animation anim = null;
	float x, y;
	float statetime;
	
	public boolean remove = false;
	
	
	public Firework(float x, float y){
		this.x = x - OFFSET;
		this.y = y - OFFSET;
		statetime = 0;
		
		if(anim == null){
			anim = new Animation(FRAME_LENGHT, TextureRegion.split(new Texture("fireworks.png"), SIZE, SIZE)[0]);
		}
	}
	
	public void update(float deltaTime){
		statetime += deltaTime;
		if(anim.isAnimationFinished(statetime))
			remove = true;
	}
	
	public void render(SpriteBatch batch){
		batch.draw(anim.getKeyFrame(statetime), x, y);
	}
	
	public void dispose(){
		
	}
	
}