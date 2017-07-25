package com.kiril.entities;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kiril.tools.CollisionRect;

public class SimpleEnemy{

	//constants
	int SPEED;
	final int WIDTH = 50;
	final int HEIGHT = 72;
	
	boolean didHit = false;
	
	
	
	//level for the speed
	int level;
	
	//health
	int health;
	boolean isHit;
	float redTime;
	
	//coordinated
	float x, y;
	
	//animation variables
	int roll;
	float stateTime;
	Animation[] rolls;	
	
	//removing boolean variables
	public boolean remove = false;
	
	//objects
	CollisionRect rect;
	Random rand;
	
	//textures
	Texture blank;
	
	//if is hit by a freezing bullet
	boolean isFreezed;
	float freezedLenght = 3f;
	float freezedTime;
	
	
	public SimpleEnemy(float x, float y, int level){
		this.x = x;
		this.y = y;
		this.level = level;
		
		rand= new Random();
		
		switch(level){
		case 0: SPEED = 0; break;
		case 1: SPEED = rand.nextInt(100)+100; break;
		case 2: SPEED = rand.nextInt(100)+100; break;
		case 3: SPEED = rand.nextInt(100)+100; break;
		case 4: SPEED = rand.nextInt(100)+100; break;
		case 5: SPEED = rand.nextInt(100)+100; break;
		case 6: SPEED = rand.nextInt(100)+100; break;
		case 7: SPEED = rand.nextInt(100)+100; break;
		case 8: SPEED = rand.nextInt(100)+100; break;
		case 9: SPEED = rand.nextInt(100)+100; break;
		case 10: SPEED = rand.nextInt(100)+100; break; 
		case 11: SPEED = rand.nextInt(100)+100; break;
		case 12: SPEED = rand.nextInt(100)+100; break;
		case 13: SPEED = rand.nextInt(200)+180; break;
		case 16: SPEED = rand.nextInt(100)+170; break;
		case 18: SPEED = rand.nextInt(200)+180; break;
		case 23: SPEED = rand.nextInt(90)+190; break;
		case 25: SPEED = rand.nextInt(100)+190; break;
		case 29: SPEED = rand.nextInt(230)+170; break;
		case 30: SPEED = rand.nextInt(130)+170; break;
		
		default: SPEED = rand.nextInt(100)+150;
		break;
		}
		
		
		rolls = new Animation[12];
		roll = 1;
		
		TextureRegion[][] rollSpriteSheet = TextureRegion.split(new Texture("simpleEnemy.png"), WIDTH, HEIGHT);
		rolls[roll] = new Animation(0.09f, rollSpriteSheet[0]);

		blank = new Texture("blank.png");
		
		this.rect = new CollisionRect(x, y, WIDTH, HEIGHT);
		
		//health 
		health = 2;
		isHit = false;
		redTime = 0.2f;
	}
	
	
		public void update(float deltaTime){
		
		//updating the state timer for the animation
		stateTime += deltaTime;
			
	 	
		
		//updating the taking damage mechanism
		if(isHit)
		redTime -= deltaTime;
		
		if(redTime <= 0){
			isHit = false;
			redTime = 0.2f;
		}
		
		//if it is freezed
		if(isFreezed){
			freezedTime += deltaTime;
			SPEED = 60;
			
			if(freezedTime >= freezedLenght){
				isFreezed = false;
				
			}
		}
		
		if(!isFreezed){
			if(level == 13)
			SPEED = rand.nextInt(200)+180;
			if(level < 13 && level != 0)
			SPEED = rand.nextInt(100)+110;
			if(level > 13 && level != 0 && level != 16)
			SPEED = rand.nextInt(100)+160;
			if(level == 16)
			SPEED = rand.nextInt(100)+170;
			if(level == 18)
			SPEED = rand.nextInt(200)+200;
				
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
			//health
			batch.setColor(Color.GREEN);
			batch.draw(blank, x + 2, y + 63, 22*health, 7);
			
			//if hit
			if(isHit){
			batch.setColor(Color.RED);
			}
			
			//if freezed
			else if(isFreezed)
			batch.setColor(new Color(0,191,255, 0.8f));
			
			else
			batch.setColor(Color.WHITE);
			
			batch.draw(rolls[roll].getKeyFrame(stateTime, true),x, y, WIDTH, HEIGHT);
			
			batch.setColor(Color.WHITE);
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
		
		
		
		public boolean isHit() {
			return isHit;
		}


		public void setHit(boolean isHit) {
			this.isHit = isHit;
		}


		public boolean isFreezed() {
			return isFreezed;
		}
		


		public int getHealth() {
			return health;
		}


		public void setHealth(int health) {
			this.health = health;
		}


		public void setFreezed(boolean isFreezed) {
			this.isFreezed = isFreezed;
		}

		
		public int getSPEED() {
			return SPEED;
		}


		public void setSPEED(int sPEED) {
			SPEED = sPEED;
		}


		public void dispose(){
			blank.dispose();
		}
}
