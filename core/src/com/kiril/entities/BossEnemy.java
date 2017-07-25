package com.kiril.entities;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kiril.main.game.MainGame;
import com.kiril.tools.CollisionRect;

public class BossEnemy {

	//boss type
	String bossType;
	
	//constants
	int velX;
	int velY;
	int WIDTH;
	int HEIGHT;
	
	//coordinates
	float x, y;
	
	//animation variables
	int roll;
	float stateTime;
	Animation[] rolls;	
	
	//removing boolean variables
	public boolean remove = false;
	
	//level
	int level;
	
	//health
	float health;
	boolean isHit;
	float redTime;
	
	//objects
	CollisionRect rect;
	Random rand = new Random();
	
	//textures for all bosses
	Texture blank;
	Texture shieldEnemy;
	
	//lights variables
	float alpha_color = 1;
	float alphaColorTime = 0;
	
	//shield variables
	public boolean shield = true;
	
	//bullets loader timer
	float bulletsLoader;
	
	/******************************************BOSS 1 VARIABLES**********************************************/
	//shooting variables
	float shootTime = 0;
	boolean shoot1 = false;
	public boolean shootBullet1 = false;
	float bulletsShotTime = 0;
	
	
	
	/******************************************BOSS 2 VARIABLES**********************************************/
	//moving variables
	boolean startMoving = false;
	float timeVelY = 6f;
	boolean changeVel = false;
	int YpositionMax = 700;
	int newYpositionMin = 300;
	
	//shooting variables
	//float bulletsLoader = 4;
	boolean shoot = false;
	int shootingStage = 0;
	int timesShot = 0;
	
	
	public BossEnemy(float x, float y, int level, String bossType){
		this.x = x;
		this.y = y;
		this.level = level;
		this.bossType = bossType;
		
		/******************************************BOSS 1 VARIABLES INITIALIZATION**********************************************/
		if(bossType == "boss1"){
			//size
			WIDTH  = 110;
			HEIGHT = 170;
			
			//animation
			rolls = new Animation[11];
			roll = 1;
			TextureRegion[][] rollSpriteSheet = TextureRegion.split(new Texture("smartEnemy.png"), WIDTH, HEIGHT);
			rolls[roll] = new Animation(0.1f, rollSpriteSheet[0]);
		
			//textures
			shieldEnemy = new Texture("shieldEnemy.png");
			
			//health 
			if(level == 12)
			health = 36;
			
			//bullets loader timer
			bulletsLoader = 3;
			
			velY = 2;
		}
		
		
		/******************************************BOSS 2 VARIABLES INITIALIZATION**********************************************/
		if(bossType == "boss2"){
			
		//size
		WIDTH = 105;
		HEIGHT = 170;
		
		//animation
		rolls = new Animation[16];
		roll = 1;
		TextureRegion[][] rollSpriteSheet = TextureRegion.split(new Texture("smartEnemy2.png"), WIDTH, HEIGHT);
		rolls[roll] = new Animation(0.1f, rollSpriteSheet[0]);
		
		//health
		health = 0;
		
		//moving variables
		velX = 200;
		velY = -40;
		
		//textures
		shieldEnemy = new Texture("shieldEnemy2.png");
		
		//bullets loader timer
		bulletsLoader = 4f;
		}

		
		/******************************************VARIABLES FOR ALL BOSSES**********************************************/
		//textures (same for all bosses)
		blank = new Texture("blank.png");
		
		//is hit variables (same for all bosses)
		isHit = false;
		redTime = 0.2f;
			
		//collision rectangles (same for all bosses)
		this.rect = new CollisionRect(x, y, WIDTH, HEIGHT);
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
		
		//updating the collision rect
		rect.move(x, y);
		
		/******************************************BOSS 2 UPDATE**********************************************/
		//update boss1
		if(bossType == "boss1"){
			
			//updating the shooting mechanism
			if(!shoot1)
			bulletsLoader -= deltaTime;
			
			//updating the shooting mechanism
			if(bulletsLoader <= 0){
				bulletsLoader = 4;
				shoot1 = true;
				shield = false;
			}
			if(shoot1){
				shootTime += deltaTime;
				bulletsShotTime += deltaTime;
				
				if(bulletsShotTime >= 1){
					shootBullet1 = true;
					bulletsShotTime = 0;
				}
				
				if(shootTime >= 3f){
					shoot1 = false;
					shield = true;
					shootTime = 0;
				}
				
				alpha_color -=deltaTime;
				if(alpha_color<=0)
					alpha_color = 1;
			}
			
		
		 	//updating the y 
			if(y>=570)
			y -= 100 * deltaTime;
			else
				y -= velY * deltaTime;
			
			//if it gets to the planet
			if(y < (-HEIGHT+50))
				remove = true;
		}
	
		/******************************************BOSS 2 UPDATE**********************************************/
		//update boss2
		if(bossType == "boss2") {
			//moving the enemy
			if(startMoving)
			x += velX * deltaTime;
			y += velY * deltaTime;
			
			//loading the health before it starts to move
			if(!startMoving){
				health+=deltaTime*40;
				System.out.println(health);
			if(health >= 200)
				health = 200;
			}
			
			// start moving
			if(y <= 500 && !startMoving){
			startMoving = true;
			velY = -100;
			velY *= -1;
			shield = false;
			shootingStage = 1;
			}
			
			// after it starts to move
			if(startMoving) {
			//bounds for x
			if(x > MainGame.WIDTH - WIDTH){
			x = MainGame.WIDTH - WIDTH;
			velX *= -1;
			}
			if(x < 0){
			x = 0;
			velX *= -1;
			}

			// generate new min and max position every 6 seconds
			changeVel = false;
			timeVelY -= deltaTime;
			if(timeVelY <= 0){
				changeVel = true;
				timeVelY = 6;
			}
			if (changeVel){
			newYpositionMin = MainGame.HEIGHT - (rand.nextInt((600) - 300) + 300);
			System.out.println("Position Changed! MIN AND MAX:" + newYpositionMin + "|" + YpositionMax);
			}

			//bounds for the random y
			if(y + (HEIGHT - 90) >= YpositionMax){
				y = YpositionMax - (HEIGHT - 91);
				velY *= -1;
			}
			if(y <= newYpositionMin){
				y = newYpositionMin - 1;
				velY *= -1;
			}
		}
			
		//updating the shooting mechanism
		if(shootingStage == 1)
			bulletsLoader -= deltaTime*3;
		if(shootingStage == 2)
			bulletsLoader -= deltaTime*3.7f;
		if(shootingStage == 3)
			bulletsLoader -= deltaTime*3f;
		if(shootingStage == 4)
			bulletsLoader -= deltaTime*4f;
		
		
		//update the alpha color for the lights
		if(shootingStage == 1)
		alpha_color -= deltaTime;
		if(shootingStage == 2)
		alpha_color -= deltaTime*2;
		if(shootingStage == 3)
		alpha_color -= deltaTime*4;
		if(alpha_color <= 0)
			alpha_color = 1;
		
		
		//updating the shooting mechanism
		if(bulletsLoader <= 0){
			bulletsLoader = 4;
			shoot = true;
			timesShot++;
		}
		if(timesShot == 4){
			timesShot = 0;
			shootingStage++;
		}
		if(shootingStage==5)
			shootingStage = 1;

		}
		
	}
	
	public void render(SpriteBatch batch){
		
		/******************************************BOSS 1 RENDER**********************************************/
		if(bossType == "boss1"){
		if(!shoot1){
		//rendering the shooting lights
		batch.setColor(new Color(0,100,0, 1));
		batch.draw(blank, x+10, y+20, 20, 20);
		batch.draw(blank, x+80, y+20, 20, 20);	
		}
		
		if(shoot1){
		//rendering the shooting lights
		batch.setColor(new Color(255,0,255, alpha_color));
		batch.draw(blank, x+10, y+20, 20, 20);
		batch.draw(blank, x+80, y+20, 20, 20);
		}
		
		if(shield){
			batch.setColor(Color.WHITE);
			batch.draw(shieldEnemy, x-45, y - 40);
		}
		
		batch.setColor(new Color(254, 0, 0, 0.8f));
		batch.draw(blank, x+ 11, y + 115, 2.4f*36, 10);
		batch.setColor(Color.GREEN);
		batch.draw(blank, x+ 11, y + 115, 2.4f*health, 10);
		}
	
		/******************************************BOSS 2 RENDER**********************************************/
		if(bossType == "boss2"){
		//white background for the bar loader for the bullets
		batch.setColor(1, 1, 1, 0.8f);
		batch.draw(blank, x + 40, y+59, 24, 24);
		
		//shooting bar loader
		batch.setColor(0.86f, 0, 0, 1);
		batch.draw(blank, x + 40, y + 59, 8, 5.9f * bulletsLoader);
		batch.draw(blank, x + 56, y + 59, 8, 5.9f * bulletsLoader);
		
		//lights
		if(shootingStage == 1 || shootingStage  == 0)
		batch.setColor(0.30f, 1, 0, alpha_color);
		if(shootingStage == 2)
		batch.setColor(1, 0.74f, 0, alpha_color);
		if(shootingStage == 3)
		batch.setColor(1, 0, 0, alpha_color);
		batch.draw(blank, x+17, y+23, 13, 11);
		batch.draw(blank, x+74, y+23, 13, 11);
		
		//health red background for the bar
		batch.setColor(new Color(0.88f, 0.16f, 0.16f, 0.6f));
		batch.draw(blank, x+22, y + 111, 62, 11);
		
		//green health 
		batch.setColor(Color.GREEN);
		batch.draw(blank, x+ 22, y + 111, health / 3.26f, 11);
		
		//shield
		if(shield){
		batch.setColor(Color.WHITE);
		batch.draw(shieldEnemy, x-45, y - 40);
		}
		
		}
		
		//if hit change color to red
		if(isHit)
		batch.setColor(Color.RED);

		else
		batch.setColor(Color.WHITE);
		
		//animation drawing for all bosses
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

	public boolean isRemove() {
		return remove;
	}

	public void setRemove(boolean remove) {
		this.remove = remove;
	}

	public boolean isShoot() {
		return shoot;
	}

	public void setShoot(boolean shoot) {
		this.shoot = shoot;
	}

	public float getHealth() {
		return health;
	}

	public void setHealth(float health) {
		this.health = health;
	}

	public String getBossType() {
		return bossType;
	}

	public void setBossType(String bossType) {
		this.bossType = bossType;
	}

	public int getShootingStage() {
		return shootingStage;
	}

	public void setShootingStage(int shootingStage) {
		this.shootingStage = shootingStage;
	}

	public boolean isHit() {
		return isHit;
	}

	public void setHit(boolean isHit) {
		this.isHit = isHit;
	}

	public boolean isShield() {
		return shield;
	}

	public void setShield(boolean shield) {
		this.shield = shield;
	}
	
	public void dispose(){
		shieldEnemy.dispose();
		blank.dispose();
	}
	
	
	
}
