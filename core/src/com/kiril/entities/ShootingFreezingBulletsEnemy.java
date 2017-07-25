package com.kiril.entities;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kiril.tools.CollisionRect;

public class ShootingFreezingBulletsEnemy {
	
	//constants
	int SPEED;
	int level;
	final int WIDTH = 70;
	final int HEIGHT = 85;
	
	
	//coordinated
	float x, y;
	
	//animation variables
	int roll;
	float stateTime;
	Animation[] rolls;	
	
	//removing boolean variables
	public boolean remove = false;
	
	//shooting bullets
	float bulletsLoader = 2.3f;
	float shootTime = 0;
	public boolean shoot = false;
	
	//health
	int health;
	boolean isHit;
	float redTime;
	
	//objects
	CollisionRect rect;
	CollisionRect beamRect;
	Random rand;
	
	//textures
	Texture blank;
	
	
	public ShootingFreezingBulletsEnemy(float x, float y, int level){
		this.x = x;
		this.y = y;
		this.level = level;
		
		rand = new Random();
		
		if(level == 0)
		SPEED = 0;
		if(level != 0)
		SPEED = rand.nextInt(40)+40;
		
		//animation
		rolls = new Animation[12];
		roll = 1;
		TextureRegion[][] rollSpriteSheet = TextureRegion.split(new Texture("FreezingEnemy.png"), WIDTH, HEIGHT);
		rolls[roll] = new Animation(0.06f, rollSpriteSheet[0]);
		
		//textures
		blank = new Texture("blank.png");
		
		//collision rectangles
		this.rect = new CollisionRect(x, y, WIDTH, HEIGHT);
		
		//health 
		health = 4;
		isHit = false;
		redTime = 0.2f;
	}
	
	
		public void update(float deltaTime){
		
		//updating the state timer for the animation
		stateTime += deltaTime;
			
		//updating the shooting mechanism
		bulletsLoader -= deltaTime;
		
		if(bulletsLoader <= 0){
			bulletsLoader = 2.3f;
			shoot = true;
		}
		else
			
		
		//updating the taking damage mechanism
		if(isHit)
		redTime -= deltaTime;
		
		if(redTime <= 0){
			isHit = false;
			redTime = 0.2f;
		}
		
		
	 	//updating the y 
		y -= SPEED * deltaTime;
		
		//if it gets to the planet
		if(y < (-HEIGHT+50))
			remove = true;
		
		//updating the collision rect
			rect.move(x, y);
			
		}
			
	public void render(SpriteBatch batch){
		
		batch.setColor(Color.RED);
		//bullet loader
		batch.draw(blank, x + 30, y + 42, 10, 5 * bulletsLoader);
		batch.setColor(Color.WHITE);
		
		batch.setColor(Color.GREEN);
		batch.draw(blank, x + 10, y + 73, 12*health, 7);
		
		if(isHit)
		batch.setColor(Color.RED);
		
		else
		batch.setColor(Color.WHITE);
		
		batch.draw(rolls[roll].getKeyFrame(stateTime, true),x, y, WIDTH, HEIGHT);
		
		
		batch.setColor(Color.WHITE);
		
	}
	

	public CollisionRect getCollisionRect(){
		return rect;
	}
	
	

	public CollisionRect getBeamRect() {
		return beamRect;
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


	public int getHealth() {
		return health;
	}


	public void setHealth(int health) {
		this.health = health;
	}


	public boolean isHit() {
		return isHit;
	}


	public void setHit(boolean isHit) {
		this.isHit = isHit;
	}



	public void dispose(){
		blank.dispose();
	}
}
