package com.kiril.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Explosion {

	Animation animBasic, animHit;
	float x, y;
	float statetime;
	String type;
	
	public boolean remove = false;
	
	//basic explosion
	float BASIC_EXPLOSION_FRAME_LENGHT = 0.07f;
	int BASIC_EXPLOSION_SIZE = 64;
	
	//hit explosion 
	float HIT_EXPLOSION_FRAME_LENGHT = 0.01f;
	int HIT_EXPLOSION_SIZE = 48;
	
	//explosion for the enemy and the player
	float PLAYER_ENEMY_EXPLOSION_FRAME_LENGHT = 0.04f;
	int PLAYER_ENEMY_EXPLOSION_SIZE = 128;
	Texture explosion;
	TextureRegion[] animationFrames;
	Animation animPlayerEnemy;
	
	public Explosion(float x, float y, String type){
		this.x = x;
		this.y = y;
		this.type = type;
		
		statetime = 0;
		
		if(type == "basicExplosion")
		animBasic = new Animation(BASIC_EXPLOSION_FRAME_LENGHT, TextureRegion.split(new Texture("explosion2.png"), BASIC_EXPLOSION_SIZE, BASIC_EXPLOSION_SIZE)[0]);
		if(type == "playerExplosion" || type == "enemyExplosion"){
			
			 explosion = new Texture("explosion128.png");
		     TextureRegion[][] tmpFrames = TextureRegion.split(explosion,128,128);
		     animationFrames = new TextureRegion[44];
		     int index = 0;
		      for (int i = 0; i < 4; i++){
		         for(int j = 0; j < 11; j++) {
		            animationFrames[index++] = tmpFrames[i][j];
		         }
		      }

		      animPlayerEnemy = new Animation(PLAYER_ENEMY_EXPLOSION_FRAME_LENGHT,animationFrames);
			
		}
		
		if(type == "hitExplosion"){
		animHit = new Animation(HIT_EXPLOSION_FRAME_LENGHT, TextureRegion.split(new Texture("explosionHit48.png"), HIT_EXPLOSION_SIZE, HIT_EXPLOSION_SIZE)[0]);
		}
	}
	
	public void update(float deltaTime){
		statetime += deltaTime;
		
		if(type == "basicExplosion"){
			if(animBasic.isAnimationFinished(statetime))
			remove = true;
		}
		if(type == "playerExplosion" || type == "enemyExplosion"){
			if(animPlayerEnemy.isAnimationFinished(statetime))
			remove = true;
		}
		
		if(type == "hitExplosion"){
			if(animHit.isAnimationFinished(statetime))
			remove = true;
		}
	}
	
	public void render(SpriteBatch batch){
		batch.setColor(Color.WHITE);
		if(type == "basicExplosion")
		batch.draw(animBasic.getKeyFrame(statetime), x, y);
		if(type == "playerExplosion" || type == "enemyExplosion")
		batch.draw(animPlayerEnemy.getKeyFrame(statetime), x, y);
		if(type == "hitExplosion")
		batch.draw(animHit.getKeyFrame(statetime), x, y);
	}
	
	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public void dispose(){
		if(type == "playerExplosion" || type == "enemyExplosion")
		explosion.dispose();
	}
	
}
