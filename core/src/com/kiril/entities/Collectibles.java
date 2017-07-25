package com.kiril.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kiril.tools.CollisionRect;

public class Collectibles {
	
	//Collectible type
	String type;

	//collectibles constants
	final int COIN_SIZE = 64;
	final int DEFAULT_velY = 220;
	final float COIN_ANIMATION_SPEED = 0.08f;
	
	//shield
	final int COLLECTIBLE_SHIELD_WIDTH = 40;
	int COLLECTIBLE_SHIELD_HEIGHT = 52;
	
	TextureRegion shieldCollectible, tools, luckClover, freezingBullets, tripleFireBullets, quadFireBullets, fasterPlayer, fasterBullets, pentaFireBullets;
	Texture collectibles;
	
	//tools
	int TOOLS_SIZE = 32;
	
	//luck clover
	int LUCK_CLOVER_SIZE = 30;
	
	//freezing bullets
	int FREEZING_BULLETS_SIZE = 30;
	
	//triple simple bullets 
	int SIMPLE_BULLETS_x3_SIZE = 31;
	
	//faster bullets
	final int FASTER_BULLETS_SIZE = 35;
	
	//coordinates
	float x, y;
	
	//removing the collectibles boolean 
	public boolean remove = false;
	
	//animation variables
	int roll;
	float stateTime;
	Animation[] rolls;
	
	//collision rect for the collectibles
	CollisionRect rect;
	
	
	public Collectibles(float x, float y, String type){
		this.x = x;
		this.y = y;
		this.type = type;
		
		collectibles = new Texture("collectibles60x60.png");
		
		if(type == "coin"){
		//coin animation
		rolls = new Animation[10];
		roll = 1;
		
		//coin texture region and animation
		TextureRegion[][] rollSpriteSheet = TextureRegion.split(new Texture("coins64x64.png"), COIN_SIZE, COIN_SIZE);
		rolls[roll] = new Animation(COIN_ANIMATION_SPEED, rollSpriteSheet[0]);
				
		this.rect = new CollisionRect(x, y, 36, 36);
		}
		
		if(type == "collectibleShield"){
			shieldCollectible = new TextureRegion(collectibles, 0, 0, 40, 52);
			this.rect = new CollisionRect(x, y, 40, 52);
		}
		
		if(type == "tools"){
			tools = new TextureRegion(collectibles, 60, 0, 32, 32);
			this.rect = new CollisionRect(x, y, 32, 32);
		}
		
		if(type == "luckClover"){
			luckClover = new TextureRegion(collectibles, 2*60, 0, 30, 30);
			this.rect = new CollisionRect(x, y, 30, 30);
		}
		
		if(type == "freezingBullets"){
			freezingBullets = new TextureRegion(collectibles, 3*60, 0, 30, 30);
			this.rect = new CollisionRect(x, y, 30, 30);
		}
		
		if(type == "tripleFireBullets"){
			tripleFireBullets = new TextureRegion(collectibles, 4*60, 0, 31, 31);
			this.rect = new CollisionRect(x, y, 30, 30);
		}
		
		if(type == "quadFireBullets"){
			quadFireBullets = new TextureRegion(collectibles, 5*60, 0, 31, 31);
			this.rect = new CollisionRect(x, y, 30, 30);
		}
		
		if(type == "pentaFireBullets"){
			pentaFireBullets = new TextureRegion(collectibles, 8*60, 0, 31, 31);
			this.rect = new CollisionRect(x, y, 30, 30);
		}
		
		if(type == "fasterPlayer"){
			fasterPlayer = new TextureRegion(collectibles, 6*60, 0, 40, 40);
			this.rect = new CollisionRect(x, y, 36, 39);
		}
		
		if(type == "fasterBullets"){
			fasterBullets = new TextureRegion(collectibles, 7*60, 0, 40, 40);
			this.rect = new CollisionRect(x, y, 36, 39);
		}
		
	}
	
	public void update(float deltaTime){
		
		//updating the statetime
		stateTime += deltaTime;
		
		//updating the Y of the coins
		y-= DEFAULT_velY * deltaTime;
		
		//moving the rect with the coin
		rect.move(x, y);
	}
	
	public void render(SpriteBatch batch){
		
		batch.setColor(Color.WHITE);
		
		if(type == "coin")
		batch.draw(rolls[roll].getKeyFrame(stateTime, true),x, y, COIN_SIZE, COIN_SIZE);
		
		if(type == "collectibleShield")
		batch.draw(shieldCollectible,x, y, COLLECTIBLE_SHIELD_WIDTH, COLLECTIBLE_SHIELD_HEIGHT);
		
		if(type == "tools")
		batch.draw(tools, x, y, TOOLS_SIZE, TOOLS_SIZE);
		
		if(type == "luckClover")
		batch.draw(luckClover, x, y, LUCK_CLOVER_SIZE, LUCK_CLOVER_SIZE);
		
		if(type == "freezingBullets")
		batch.draw(freezingBullets, x, y, FREEZING_BULLETS_SIZE, FREEZING_BULLETS_SIZE);
		
		if(type == "tripleFireBullets")
		batch.draw(tripleFireBullets, x, y, SIMPLE_BULLETS_x3_SIZE, SIMPLE_BULLETS_x3_SIZE);
		
		if(type == "quadFireBullets")
			batch.draw(quadFireBullets, x, y, SIMPLE_BULLETS_x3_SIZE, SIMPLE_BULLETS_x3_SIZE);
		
		if(type == "pentaFireBullets")
			batch.draw(pentaFireBullets, x, y, SIMPLE_BULLETS_x3_SIZE, SIMPLE_BULLETS_x3_SIZE);
		
		if(type == "fasterPlayer")
			batch.draw(fasterPlayer, x, y);
		
		if(type == "fasterBullets")
			batch.draw(fasterBullets, x, y);
	}
	
	public CollisionRect getCollisionRect(){
		return rect;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void dispose(){
		collectibles.dispose();
	}
}
