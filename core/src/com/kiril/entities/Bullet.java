package com.kiril.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kiril.main.game.MainGame;
import com.kiril.tools.CollisionRect;

public class Bullet {

	int DEFAULT_SPEED = 500;
	int DEFAULT_ENEMY_BULLET_SPEED = 300;
	int DEFAULT_Y = 75;
	
	//bullets texture regions
	TextureRegion simpleBullet, freezingBullet, enemyBullet, rightFireBullet, leftFireBullet,
				  enemySmartBullet, enemySmartBulletRight1, enemySmartBulletRight2, enemySmartBulletLeft1, enemySmartBulletLeft2,
				  enemyFreezingBullet, bossBullet, bossBulletRight, bossBulletLeft;
	
	//bullets spriteSheet
	private Texture bullets;
	
	//bullet coordinates
	float x, y;
	
	//collision rect for the bullet
	CollisionRect rect;
	
	//removing the bullet
	public boolean remove = false;
	
	//is faster bullets mode activated
	boolean isFasterBullets;
	
	//type for the bullets
	String type;
	
	//reversing the player bullets and making the enemies
	boolean reverseBulletDirection = false;
	boolean isBulletReversed = false;
	
	public Bullet(float x, String type, float y, boolean isFasterBullets){
		this.x = x;
		this.type = type;
		this.y = y;
		this.isFasterBullets = isFasterBullets;
		
		if(type == "simpleBullet" || type == "freezingBullet" || type == "rightFireBullet" || type == "leftFireBullet"){
		if(isFasterBullets)
			DEFAULT_SPEED *= 1.3f;
		else
			DEFAULT_SPEED = 500;
		}
		
		this.rect = new CollisionRect(x, y, 9, 37);
		
		bullets = new Texture("bullets37x37.png");
		
		if(type == "simpleBullet")
			simpleBullet = new TextureRegion(bullets, 0, 0, 9, 37);
		if(type == "freezingBullet")
			freezingBullet = new TextureRegion(bullets, 37, 0, 9, 37);
		if(type == "enemyBullet")
			enemyBullet = new TextureRegion(bullets, 2*37, 0, 8, 33);
		if(type == "rightFireBullet")
			rightFireBullet = new TextureRegion(bullets, 3*37, 0, 23, 37);
		if(type == "leftFireBullet")
			leftFireBullet = new TextureRegion(bullets, 4*37+10, 0, 27, 37);
		if(type == "enemySmartBullet")
			enemySmartBullet = new TextureRegion(bullets, 5*37, 0, 7, 30);
		if(type == "enemySmartBulletRight1")
			enemySmartBulletRight1 = new TextureRegion(bullets, 6*37, 0, 14, 35);
		if(type == "enemySmartBulletRight2")
			enemySmartBulletRight2 = new TextureRegion(bullets, 7*37, 0, 19, 35);
		if(type == "enemySmartBulletLeft1")
			enemySmartBulletLeft1 = new TextureRegion(bullets, 8*37, 0, 14, 35);
		if(type == "enemySmartBulletLeft2")
			enemySmartBulletLeft2 = new TextureRegion(bullets, 9*37, 0, 19, 35);
		if(type == "enemyFreezingBullet")
			enemyFreezingBullet = new TextureRegion(bullets, 10*37, 0, 9, 32);
		if(type == "bossBullet")
			bossBullet = new TextureRegion(bullets, 11*37, 0, 7, 38);
		if(type == "bossBulletRight")
			bossBulletRight = new TextureRegion(bullets, 12*37, 0, 22, 34);
		if(type == "bossBulletLeft")
			bossBulletLeft = new TextureRegion(bullets, 13*37, 0, 23, 34);
	}
	
	public void update(float deltaTime){
		
		if(y > MainGame.HEIGHT)
			remove = true;
		
		//if the bullet is from the enemy destroy it 
		if(type == "enemyBullet" || type == "enemySmartBullet" || type == "enemySmartBulletRight1" || type == "enemySmartBulletRight2" || type == "enemySmartBulletLeft1" || type == "enemySmartBulletLeft2" || type == "enemyFreezingBullet" || type == "bossBullet" || type == "bossBulletRight" || type == "bossBulletLeft"){
			if(y < 0 || x < 0 || x > MainGame.WIDTH)
				remove = true;
		}
		
		//if the bullet is from the player
		if(type == "simpleBullet" || type == "freezingBullet"){
			if(!reverseBulletDirection)
				y+= DEFAULT_SPEED * deltaTime;
			if(reverseBulletDirection)
				y-= DEFAULT_SPEED * deltaTime;
		}
		
		if(type == "rightFireBullet"){
			x+=70 * deltaTime;
			y+= DEFAULT_SPEED * deltaTime;
		}
		if(type == "leftFireBullet"){
			x-=70 * deltaTime;
			y+= DEFAULT_SPEED * deltaTime;
		}
		
		//if the bullet is from the enemy
		if(type == "enemyBullet")
		y-= DEFAULT_ENEMY_BULLET_SPEED * deltaTime;
		
		//if the bullet is from the freezing bullets enemy
		if(type == "enemyFreezingBullet")
			y-= (DEFAULT_ENEMY_BULLET_SPEED+20) * deltaTime;
		
		//if the bullet is from the smart enemy
		if(type == "enemySmartBullet")
			y-= DEFAULT_ENEMY_BULLET_SPEED * deltaTime;
			
		if(type == "enemySmartBulletRight1" || type == "bossBulletRight"){
			y-= DEFAULT_ENEMY_BULLET_SPEED * deltaTime;
			x+=80 * deltaTime;
			
			}
		if(type == "enemySmartBulletRight2"){
			y-= DEFAULT_ENEMY_BULLET_SPEED * deltaTime;
			x+=160 * deltaTime;
			
			}
		if(type == "enemySmartBulletLeft1" || type == "bossBulletLeft"){
			y-= DEFAULT_ENEMY_BULLET_SPEED * deltaTime;
			x -= 80*deltaTime;
			
			}
		if(type == "enemySmartBulletLeft2"){
			y-= DEFAULT_ENEMY_BULLET_SPEED * deltaTime;
			x -= 160*deltaTime;
			
			}
		
		//if the bullet is from the boss
		if(type == "bossBullet")
			y-= DEFAULT_ENEMY_BULLET_SPEED * deltaTime;
		
		
		
		rect.move(x, y);
		
	}
	
	public void render(SpriteBatch batch){
		if(type == "simpleBullet")
		batch.draw(simpleBullet, x, y);
		if(type == "freezingBullet")
		batch.draw(freezingBullet, x, y);
		if(type == "enemyBullet")
		batch.draw(enemyBullet, x, y);
		if(type == "rightFireBullet")
		batch.draw(rightFireBullet, x, y);
		if(type == "leftFireBullet")
		batch.draw(leftFireBullet, x, y);
		if(type == "enemySmartBullet")
		batch.draw(enemySmartBullet, x, y);
		if(type == "enemySmartBulletRight1")
		batch.draw(enemySmartBulletRight1, x, y);
		if(type == "enemySmartBulletRight2")
		batch.draw(enemySmartBulletRight2, x, y);
		if(type == "enemySmartBulletLeft1")
		batch.draw(enemySmartBulletLeft1, x, y);
		if(type == "enemySmartBulletLeft2")
		batch.draw(enemySmartBulletLeft2, x, y);
		if(type == "enemyFreezingBullet")
		batch.draw(enemyFreezingBullet, x, y);
		if(type == "bossBullet")
		batch.draw(bossBullet, x, y);
		if(type == "bossBulletRight")
		batch.draw(bossBulletRight, x, y);
		if(type == "bossBulletLeft")
		batch.draw(bossBulletLeft, x, y);
	}
	
	
	public CollisionRect getCollisionRect(){
		return rect;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	

	public int getDEFAULT_SPEED() {
		return DEFAULT_SPEED;
	}

	public void setDEFAULT_SPEED(int dEFAULT_SPEED) {
		DEFAULT_SPEED = dEFAULT_SPEED;
	}
	
	

	public boolean isReverseBulletDirection() {
		return reverseBulletDirection;
	}

	public void setReverseBulletDirection(boolean reverseBulletDirection) {
		this.reverseBulletDirection = reverseBulletDirection;
	}

	public void dispose(){
		bullets.dispose();
	}
	
}