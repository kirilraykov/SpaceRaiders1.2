package com.kiril.entities;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kiril.main.game.MainGame;
import com.kiril.tools.CollisionRect;

public class Asteroid {

	//asteroid constants
	int WIDTH = 30;
	int HEIGHT = 30;
	
	//speed
	int SPEED = 250;
	
	//animation variables
	int roll;
	float stateTime;
	Animation[] rolls;	
	
	//asteroid coordinates
	float x, y;
	
	//global boolean variables to check if I have to remove it
	public boolean remove = false;
	
	//objects
	CollisionRect rect;
	Random random;
	
	public Asteroid(float x){
		this.x = x;
		this.y = MainGame.HEIGHT;
		
		//animation initialization
		rolls = new Animation[11];
		roll = 1;
		TextureRegion[][] rollSpriteSheet = TextureRegion.split(new Texture("meteorsprite.png"), WIDTH, HEIGHT);
		rolls[roll] = new Animation(0.2f, rollSpriteSheet[0]);
		
		//rect for collision
		this.rect = new CollisionRect(x, x, WIDTH, HEIGHT);
		
		//random
		random = new Random();
		
	}
	
	public void update(float deltaTime){
		//updating the stateTime for the animatio
		stateTime += deltaTime;
		
		//updating the y of the asteroid
		y -= SPEED * deltaTime;
		
		//updating the rectangle for collision
		rect.move(x, y);
		
	}
	
	public void render(SpriteBatch batch){
		batch.draw(rolls[roll].getKeyFrame(stateTime, true),x, y, WIDTH, HEIGHT);
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
	
	
	
	public int getWIDTH() {
		return WIDTH;
	}

	public void dispose(){
		
	}
	
}