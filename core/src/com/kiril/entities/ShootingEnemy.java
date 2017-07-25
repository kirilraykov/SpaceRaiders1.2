package com.kiril.entities;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kiril.tools.CollisionRect;

public class ShootingEnemy{

	//objects
	Preferences prefs;
	
	//variables changeble from options
	boolean music, sfx, replay, progressBar;
	
	//constants
	int SPEED = 100;
	final int WIDTH = 60;
	final int HEIGHT = 82;
	
	boolean collides = false;
	boolean didHit = false;
	
	//shooting beam
	float beamLoader = 3;
	float shootTime = 0;
	public boolean shoot = false;
	
	//laser sound
	Sound laser;
	
	int beamOffSet;
	int level;
	
	//coordinated
	float x, y;
	
	//animation variables
	int roll;
	float stateTime;
	Animation[] rolls;	
	
	//removing boolean variables
	public boolean remove = false;
	
	//beam startPoint
	public float startPoint = 20;
	
	//health
	int health;
	boolean isHit;
	float redTime;
	
	//objects
	CollisionRect rect;
	CollisionRect beamRect;
	
	//textures
	Texture shootingBeam;
	Texture blank;
	
	//if is hit by a freezing bullet
	boolean isFreezed;
	float freezedLenght = 4f;
	float freezedTime;
	
	public ShootingEnemy(float x, float y, int level){
		this.x = x;
		this.y = y;
		this.level = level;
		
		//loading data from save file
		prefs = Gdx.app.getPreferences("spaceraiders");
		sfx = prefs.getBoolean("sfx");
	
		//sound laser
		laser = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/enemyLaser.ogg"));
		
		//animation
		rolls = new Animation[7];
		roll = 1;
		TextureRegion[][] rollSpriteSheet = TextureRegion.split(new Texture("shootingEnemy.png"), WIDTH, HEIGHT);
		
		rolls[roll] = new Animation(0.08f, rollSpriteSheet[0]);
		
		//textures
		shootingBeam = new Texture("laserBeam.png");
		blank = new Texture("blank.png");
		
		//collision rectangles
		this.rect = new CollisionRect(x, y, WIDTH, HEIGHT);
		this.beamRect = new CollisionRect(x + 20, startPoint, 21, (int)y - HEIGHT / 2 - beamOffSet);
		
		//health 
		health = 3;
		isHit = false;
		redTime = 0.2f;
		
		if(level == 21)
			beamLoader = 3.5f;
	}
	
	
		public void update(float deltaTime){
		
		//updating the state timer for the animation
		stateTime += deltaTime;
			
		//updating the shooting mechanism
		if(!isFreezed)
		beamLoader -= deltaTime;
		if(isFreezed)
		beamLoader -= deltaTime/2;
		
		//updating the taking damage mechanism
		if(isHit)
		redTime -= deltaTime;
		
		if(redTime <= 0){
			isHit = false;
			redTime = 0.2f;
		}
		
		//adjusting the beam if there is shield
		if(startPoint == 150) beamOffSet = 100;
		else beamOffSet = -20;
		
		if(beamLoader <= 0){
			beamLoader = 3;
			shoot = true;
			if(level != 0){
				if(sfx)
					laser.play(0.1f);
			
			}
		}
		
		//updating shooting the beam
		if(shoot){
			shootTime += deltaTime;
			
			if(shootTime >= 0.6f){
				shoot = false;
				didHit = false;
				shootTime = 0;
			}
		}
		
		//System.out.println("IS FREEZED : " + isFreezed);
		
		//if it is freezed
		if(isFreezed){
			freezedTime += deltaTime;
			SPEED = 60;
			
			if(freezedTime >= freezedLenght){
				isFreezed = false;
				
			}
		}
		if(!isFreezed){
			if(level == 0)
				SPEED = 0;
			else
			SPEED = 100;
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
			batch.draw(blank, x + 25, y + 20, 10, 7 * beamLoader);
			batch.setColor(Color.WHITE);
			
			batch.setColor(Color.GREEN);
			batch.draw(blank, x + 6, y + 63, 16*health, 8);
			
			if(isHit){
			batch.setColor(Color.RED);
			}
			
			else if(isFreezed)
			batch.setColor(new Color(0,191,255, 0.8f));
			
			else
			batch.setColor(Color.WHITE);
			
			batch.draw(rolls[roll].getKeyFrame(stateTime, true),x, y, WIDTH, HEIGHT);
			
			batch.setColor(Color.WHITE);
			if(shoot)
				batch.draw(shootingBeam, x + 20, startPoint, 21, y - HEIGHT / 2 - beamOffSet);
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
		
		public void dispose(){
			shootingBeam.dispose();
			blank.dispose();
			laser.dispose();
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


		public boolean isCollides() {
			return collides;
		}


		public void setCollides(boolean collides) {
			this.collides = collides;
		}


		public boolean isDidHit() {
			return didHit;
		}


		public void setDidHit(boolean didHit) {
			this.didHit = didHit;
		}


		public boolean isFreezed() {
			return isFreezed;
		}


		public void setFreezed(boolean isFreezed) {
			this.isFreezed = isFreezed;
		}

		
		
		
		
		
}
