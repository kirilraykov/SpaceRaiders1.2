package com.kiril.tools;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Align;
import com.kiril.entities.Asteroid;
import com.kiril.entities.BossEnemy;
import com.kiril.entities.ShootingBulletsEnemy;
import com.kiril.entities.ShootingEnemy;
import com.kiril.entities.ShootingFreezingBulletsEnemy;
import com.kiril.entities.SimpleEnemy;
import com.kiril.main.game.MainGame;
import com.kiril.screens.GameScreen;

public class LevelGenerator {

	Preferences prefs;
	
	//changable variables from options menu
	boolean music, sfx, replay, progressBarCheck;
	
	//Counter when the level starts
	Texture one, two, three, four, five;
	final float DEFAULT_X_FOR_THE_COUNTER = MainGame.WIDTH / 2 - 20;
	final float DEFAULT_Y_FOR_THE_COUNTER = MainGame.HEIGHT / 2;
	float alpha_color;
	int roll;
	boolean startCountDown = false;
	
	//font
	BitmapFont font;
	FreeTypeFontGenerator generator;
	FreeTypeFontParameter parameter;
	
	//layouts
	GlyphLayout levelTitle;
	
	//color for the titles
	Color color;
	
	float titleTime = 6;
	float alpha_colorTitle = 1;
	boolean stopTitle = false;
	
	//sound
	Sound counter;
	Sound fireworks; boolean playOnce = false;
	
	//progress bar variables
	Texture progressBar;
	Texture  blank;
	float lvlProgress;
	int lvlTotalEnemies;
	double lvlEnemiesCounter;
	boolean isLevelDone;
	
	//star splash animation 
	float FRAME_LENGHT = 0.1f;
	int SIZE = 100;
	Animation animStar = new Animation(FRAME_LENGHT, TextureRegion.split(new Texture("fireworks2.png"), SIZE, SIZE)[0]);
	float statetime;
	
	//level pause variables
	float pauseTimePassed = 0;
	boolean isPause = false;
	boolean isHalfPausePassed = false;
	
	//asteroid timer and variables
	float asteroidSpawnTimer;
	int asteroidCounter;
	
	//simple enemies timer and variables
	int simpleEnemiesCounter;
	float simpleEnemySpawnTimer;
	
	
	//shooting enemies timer and variables
	int shootingEnemiesCounter;
	float shootingEnemySpawnTimer;
	
	//shooting bullets enemies timer and variables
	int shootingBulletsEnemiesCounter;
	float shootingBulletsEnemiesSpawnTimer;
	
	//shooting freezing bullets enemies timer and variables
	int shootingFreezingBulletsEnemiesCounter;
	float shootingFreezingBulletsEnemiesSpawnTimer;
	
	//fonts
	BitmapFont fontText;
	FreeTypeFontGenerator generatorText;
	FreeTypeFontParameter parameterText;
	
	//layouts
	GlyphLayout level1TextLayout;
	
	//level
	int level;
	
	//objects
	GameScreen game;
	Random rand;
	ScorePoints sp;
	
	//boss levels variables
	boolean isFirstBossKilled = false;
	boolean spawnEnemiesWithBoss = false;
	boolean isHalfLevelPassed = false;
	boolean spawnTools = false;
	int counterForTools = 0;
	
	public LevelGenerator(GameScreen game, int level){
		this.game = game;
		this.level = level;
		
		//loading data from save file
		prefs = Gdx.app.getPreferences("spaceraiders");
		sfx = prefs.getBoolean("sfx", true);
		progressBarCheck = prefs.getBoolean("progressBar", true);
	
		
		one = new Texture("numbers/1.png");
		two = new Texture("numbers/2.png");
		three = new Texture("numbers/3.png");
		four = new Texture("numbers/4.png");
		five = new Texture("numbers/5.png");
		
		alpha_color = 1;
		roll = 5;
		
		
		//font for the titles
		//initialize font
		generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/orbitron.ttf"));
		parameter = new FreeTypeFontParameter();
		parameter.size = 46;
		font = generator.generateFont(parameter);
		
		color = new Color(0.96f, 0.80f, 0.54f, 1);
		
		counter = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/counter.ogg"));
		fireworks = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/fireworks.ogg"));
		
		progressBar = new Texture("progressBar.png");
		blank = new Texture("blank.png");
		
		//asteroids spawning timer
		asteroidSpawnTimer = 10;
		
		//simple enemies spawning timer
		simpleEnemySpawnTimer = 10;
		
		//shooting enemies spawning timer
		shootingEnemySpawnTimer = 10;
		
		//shooting bullets enemies spawning timer
		shootingBulletsEnemiesSpawnTimer = 10;
		
		//shooting freezing bullets enemies spawning timer
		shootingFreezingBulletsEnemiesSpawnTimer = 10;
		
		//counters
		asteroidCounter = 0;
		simpleEnemiesCounter = 0;
		shootingEnemiesCounter = 0;
		shootingBulletsEnemiesCounter = 0;
		shootingFreezingBulletsEnemiesCounter = 0;
			
		
		rand = new Random();
		

		//text font
		generatorText = new FreeTypeFontGenerator(Gdx.files.internal("fonts/venus.ttf"));
		parameterText = new FreeTypeFontParameter();
		parameterText.size = 15;
		fontText = generatorText.generateFont(parameterText);
		
		level1TextLayout = new GlyphLayout(fontText, "It get harder, I promise");
		
	}
	
	public void update(float delta){
		
		//asteroids timer
		asteroidSpawnTimer -= delta;
		
		//simple enemies timer
		simpleEnemySpawnTimer -= delta;
		
		//shooting enemies timer
		shootingEnemySpawnTimer -= delta;
		
		//shooting bullets enemies timer
		shootingBulletsEnemiesSpawnTimer -= delta;
		
		//shooting freezing Bullets enemies timer
		shootingFreezingBulletsEnemiesSpawnTimer -= delta;
		
		//updating the title time
		titleTime -= delta;
		if(titleTime <= 2){
			alpha_colorTitle -= delta*0.5;
			startCountDown = true;
		}
		if(titleTime <= 0){
			stopTitle = true;
		}
		
		//updating the alpha color
		if(startCountDown){
				if(roll > 0){
				alpha_color -= delta;
				if(alpha_color <= 0){
					if(roll >= 2){
						if(sfx)
							counter.play(1f);
					}
					alpha_color = 1;
					roll--;
				}
			}
		}
		
		
		//fireworks splash
		if(isLevelDone)
			statetime += delta;
		
		//SPAWNING THE ENTITIES
		spawn(delta);
		
	}
	
	public void spawn(float delta ){
		
		//System.out.printf("Asteroids: %d   |   Simple Enemies: %d   |   Shooting Enemies: %d   |   Shooting Bullets Enemies: %d   |   Shooting Freezing Bullets Enemies: %d\n", asteroidCounter, simpleEnemiesCounter, shootingEnemiesCounter, shootingBulletsEnemiesCounter, shootingFreezingBulletsEnemiesCounter);
		
		if(level == 1){
			
			if(asteroidSpawnTimer <= 0){
			//stage 0
		if(asteroidCounter <= 5){
		asteroidSpawnTimer = rand.nextFloat() * (2.5f - 2f) + 2f;
		game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
		game.setAsteroids(game.getAsteroids());
		asteroidCounter++; lvlEnemiesCounter++;
		}
		
		//stage 1
		if(asteroidCounter > 5 && asteroidCounter <= 10){
		asteroidSpawnTimer = rand.nextFloat() * (1.4f - 1) + 1;
		game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
		game.setAsteroids(game.getAsteroids());
		asteroidCounter++; lvlEnemiesCounter++;
		}
		
		//stage 2
		if(asteroidCounter > 10 && asteroidCounter <= 15){
		asteroidSpawnTimer = rand.nextFloat() * (1.1f - 0.9f) + 0.9f;
		game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
		game.setAsteroids(game.getAsteroids());
		asteroidCounter++; lvlEnemiesCounter++;
		}
		
		//stage 3
		if(asteroidCounter > 15 && asteroidCounter <= 17){
		asteroidSpawnTimer = rand.nextFloat() * (0.9f - 0.7f) + 0.7f;
		game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
		game.setAsteroids(game.getAsteroids());
		asteroidCounter++; lvlEnemiesCounter++;
		}
		
		//stage 4
		if(asteroidCounter > 17 && asteroidCounter <= 20){
		asteroidSpawnTimer = rand.nextFloat() * (0.9f - 0.6f) + 0.6f;
		game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
		game.setAsteroids(game.getAsteroids());
		asteroidCounter++; lvlEnemiesCounter++;
		}
		
		//stage 5
		if(asteroidCounter > 20 && asteroidCounter <= 25){
		asteroidSpawnTimer = rand.nextFloat() * (0.9f - 0.5f) + 0.5f;
		game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
		game.setAsteroids(game.getAsteroids());
		asteroidCounter++; lvlEnemiesCounter++;
		}
		
		//stage 6
		if(asteroidCounter > 25 && asteroidCounter <= 30){
		asteroidSpawnTimer = rand.nextFloat() * (0.8f - 0.5f) + 0.3f;
		game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
		game.setAsteroids(game.getAsteroids());
		asteroidCounter++; lvlEnemiesCounter++;
		}
		
		//stage 7
		if(asteroidCounter > 30 && asteroidCounter <= 35){
		asteroidSpawnTimer = rand.nextFloat() * (0.8f - 0.4f) + 0.4f;
		game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
		game.setAsteroids(game.getAsteroids());
		asteroidCounter++; lvlEnemiesCounter++;
		}

		//stage 8
		if(asteroidCounter > 35 && asteroidCounter <= 43){
		asteroidSpawnTimer = rand.nextFloat() * (0.6f - 0.3f) + 0.3f;
		game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
		game.setAsteroids(game.getAsteroids());
		asteroidCounter++; lvlEnemiesCounter++;
		}
		//stage 7
		if(asteroidCounter > 43 && asteroidCounter <= 46){
		asteroidSpawnTimer = rand.nextFloat() * (0.5f - 0.3f) + 0.3f;
		game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
		game.setAsteroids(game.getAsteroids());
		asteroidCounter++; lvlEnemiesCounter++;
		}
		
		//stage 8
		if(asteroidCounter > 46 && asteroidCounter <= 50){
		asteroidSpawnTimer = rand.nextFloat() * (0.8f - 0.6f) + 0.6f;
		game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
		game.setAsteroids(game.getAsteroids());
		asteroidCounter++; lvlEnemiesCounter++;
		}
		
		//stage 9
		if(asteroidCounter > 50 && asteroidCounter <= 55){
		asteroidSpawnTimer = rand.nextFloat() * (0.7f - 0.5f) + 0.5f;
		game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
		game.setAsteroids(game.getAsteroids());
		
		asteroidCounter++; lvlEnemiesCounter++;
		}
		
		//stage 10
		if(asteroidCounter > 55 && asteroidCounter <= 63){
		asteroidSpawnTimer = rand.nextFloat() * (0.5f - 0.1f) + 0.05f;
		game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
		game.setAsteroids(game.getAsteroids());
		
		asteroidCounter++; lvlEnemiesCounter++;
		}
		
		//stage 10
		if(asteroidCounter > 63 && asteroidCounter <= 66){
		asteroidSpawnTimer = rand.nextFloat() * (0.6f - 0.2f) + 0.2f;
		game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
		game.setAsteroids(game.getAsteroids());
		
		asteroidCounter++; lvlEnemiesCounter++;
		}
		
		//stage 10
		if(asteroidCounter > 66 && asteroidCounter <= 70){
		asteroidSpawnTimer = rand.nextFloat() * (0.9f - 0.8f) + 0.9f;
		game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
		game.setAsteroids(game.getAsteroids());
		
		asteroidCounter++; lvlEnemiesCounter++;
		}
		
		if(asteroidCounter >= 70 && game.getAsteroids().isEmpty() && !game.isStartEndLevelTimer() && !game.isEndLevel()){
			game.setStartEndLevelTimer(true);  isLevelDone = true;
		}
			
		if(asteroidCounter >= 70 && game.getAsteroids().isEmpty() && !game.isStartEndLevelTimer() && game.isEndLevel())
			game.setisEndLevel(true);
		}
			
		
		
		}
		
		/* 											LEVEL 2
		 * 											LEVEL 2
		 * 											LEVEL 2 
		 * 											LEVEL 2
		 * 
		 * 
		 */
		
		if(level == 2){
			
		if(asteroidSpawnTimer <= 0){
		
			//Asteroids spawning
			if(asteroidCounter <= 3){
			asteroidSpawnTimer = rand.nextFloat() * (2f - 1.7f) + 1.7f;
			game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
			game.setAsteroids(game.getAsteroids());
			asteroidCounter++; lvlEnemiesCounter++;
				
			}
			//Asteroids spawning
			if(asteroidCounter > 3 && asteroidCounter <= 6){
			asteroidSpawnTimer = rand.nextFloat() * (0.9f - 0.6f) + 0.6f;
			game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
			game.setAsteroids(game.getAsteroids());
			asteroidCounter++; lvlEnemiesCounter++;
				
			}
			//Asteroids spawning
			if(asteroidCounter > 6 && asteroidCounter <= 10){
			asteroidSpawnTimer = rand.nextFloat() * (0.7f - 0.4f) + 0.4f;
			game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
			game.setAsteroids(game.getAsteroids());
			asteroidCounter++; lvlEnemiesCounter++;
				
			}
			//Asteroids spawning
			if(asteroidCounter > 10 && asteroidCounter <= 15){
			asteroidSpawnTimer = rand.nextFloat() * (0.2f - 0.1f) + 0.1f;
			game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
			game.setAsteroids(game.getAsteroids());
			asteroidCounter++; lvlEnemiesCounter++;
				
			}
			//Asteroids spawning
			if(asteroidCounter > 15 && asteroidCounter <= 20){
			asteroidSpawnTimer = rand.nextFloat() * (0.09f - 0.05f) + 0.05f;
			game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
			game.setAsteroids(game.getAsteroids());
			asteroidCounter++; lvlEnemiesCounter++;
				
			}
			//Asteroids spawning
			if(asteroidCounter > 20 && asteroidCounter <= 25){
			asteroidSpawnTimer = rand.nextFloat() * (0.6f - 0.3f) + 0.3f;
			game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
			game.setAsteroids(game.getAsteroids());
			asteroidCounter++; lvlEnemiesCounter++;
				
			}
			//Asteroids spawning
			if(asteroidCounter > 25 && asteroidCounter <= 30){
			asteroidSpawnTimer = rand.nextFloat() * (1f - 0.7f) + 0.7f;
			game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
			game.setAsteroids(game.getAsteroids());
			asteroidCounter++; lvlEnemiesCounter++;
				
			}
			
			//Simple Enemies spawning
			if(simpleEnemySpawnTimer <= 0 && asteroidCounter > 30){
				if(simpleEnemiesCounter >= 0 && simpleEnemiesCounter <= 3){
					simpleEnemySpawnTimer = rand.nextFloat() * (3f - 2.4f) + 2.4f;
						game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
				}
				if(simpleEnemiesCounter > 3 && simpleEnemiesCounter <= 9){
					simpleEnemySpawnTimer = rand.nextFloat() * (2f - 2f) + 2f;
						game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
				}
				if(simpleEnemiesCounter > 9 && simpleEnemiesCounter <= 15 && asteroidCounter <= 35){
					//simple enemies
					simpleEnemySpawnTimer = rand.nextFloat() * (2.2f - 1.8f) + 1.8f;
						game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
						//asteroids
						asteroidSpawnTimer = rand.nextFloat() * (1.3f - 0.8f) + 0.8f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
				}
				if(simpleEnemiesCounter >= 15 && simpleEnemiesCounter <= 25 && asteroidCounter <= 45){
					//simple enemies
					simpleEnemySpawnTimer = rand.nextFloat() * (0.7f - 0.4f) + 0.4f;
						game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
						//asteroids
						asteroidSpawnTimer = rand.nextFloat() * (0.7f - 0.3f) + 0.3f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
				}
				if(simpleEnemiesCounter >= 25 && simpleEnemiesCounter <= 30 && asteroidCounter <= 52){
					//simple enemies
					simpleEnemySpawnTimer = rand.nextFloat() * (0.9f - 0.5f) + 0.5f;
						game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
						//asteroids
						asteroidSpawnTimer = rand.nextFloat() * (0.6f - 0.3f) + 0.3f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
				}
			}
		}
		
		if(asteroidCounter >= 52 && game.getAsteroids().isEmpty() && !game.isStartEndLevelTimer() && !game.isEndLevel()){
			game.setStartEndLevelTimer(true);  isLevelDone = true;
		}
			
		if(asteroidCounter >= 52 && game.getAsteroids().isEmpty() && !game.isStartEndLevelTimer() && game.isEndLevel()){
			game.setisEndLevel(true);
			
		}

		}
		
		
		/* 											LEVEL 3
		 * 											LEVEL 3
		 * 											LEVEL 3 
		 * 											LEVEL 3
		 * 
		 * 
		 */
		
		if(level == 3){
			if(simpleEnemySpawnTimer <= 0){
				if(simpleEnemiesCounter <= 5){
					simpleEnemySpawnTimer = rand.nextFloat() * (1.9f - 0.9f) + 0.9f;
					game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
					game.setSimpleEnemies(game.getSimpleEnemies());
					simpleEnemiesCounter++; lvlEnemiesCounter++;
				}
				
				if(simpleEnemiesCounter > 5 && simpleEnemiesCounter <= 10){
					simpleEnemySpawnTimer = rand.nextFloat() * (1.4f - 0.7f) + 0.7f;
					game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
					game.setSimpleEnemies(game.getSimpleEnemies());
					simpleEnemiesCounter++; lvlEnemiesCounter++;
				}
				
				//asteroids spawning after 10 simple Enemies
				if(asteroidSpawnTimer <= 0 && simpleEnemiesCounter > 10){
					if(asteroidCounter <= 25){
						asteroidSpawnTimer = rand.nextFloat() * (2.7f - 1.3f) + 1.3f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
					}
				}
				
				
				if(simpleEnemiesCounter > 10 && simpleEnemiesCounter <= 35){
					simpleEnemySpawnTimer = rand.nextFloat() * (1.7f - 1f) + 1f;
					game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
					game.setSimpleEnemies(game.getSimpleEnemies());
					simpleEnemiesCounter++; lvlEnemiesCounter++;
				}
				
				//spawning shooting enemies after 25th simple enemy
				if(shootingEnemySpawnTimer <= 0 && simpleEnemiesCounter > 25){
					if(shootingEnemiesCounter <= 10){
					shootingEnemySpawnTimer = rand.nextFloat() * (2.5f - 1.2f) + 1.2f;
					game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 3));
					game.setShootingEnemies(game.getShootingEnemies());
					shootingEnemiesCounter++; lvlEnemiesCounter++;
					}
					
					if(shootingEnemiesCounter > 10 && shootingEnemiesCounter <= 20){
					shootingEnemySpawnTimer = rand.nextFloat() * (1.8f - 0.9f) + 0.9f;
					game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 3));
					game.setShootingEnemies(game.getShootingEnemies());
					shootingEnemiesCounter++; lvlEnemiesCounter++;
					}
				}
				
				if(asteroidCounter >= 26 && game.getAsteroids().isEmpty() && shootingEnemiesCounter > 20 && game.getShootingEnemies().isEmpty() && !game.isStartEndLevelTimer() && !game.isEndLevel()){
					game.setStartEndLevelTimer(true);  isLevelDone = true;
				}
					
				if(asteroidCounter >= 26 && game.getAsteroids().isEmpty() && shootingEnemiesCounter > 20 && game.getShootingEnemies().isEmpty() && !game.isStartEndLevelTimer() && game.isEndLevel()){
					game.setisEndLevel(true);
					
				}
				
				
				
			}
		
		}
		
		/* 											LEVEL 4
		 * 											LEVEL 4
		 * 											LEVEL 4 
		 * 											LEVEL 4
		 * 
		 * 
		 */
		
		if(level == 4){
			if(shootingEnemySpawnTimer <= 0){
				if(shootingEnemiesCounter <= 5){
					shootingEnemySpawnTimer = rand.nextFloat() * (3.6f - 2.2f) + 2.2f;
					game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 4));
					game.setShootingEnemies(game.getShootingEnemies());
					shootingEnemiesCounter++; lvlEnemiesCounter++;
				}
				if(shootingEnemiesCounter > 5 && shootingEnemiesCounter <= 13){
					shootingEnemySpawnTimer = rand.nextFloat() * (6.6f - 4.2f) + 4.2f;
					game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 4));
					game.setShootingEnemies(game.getShootingEnemies());
					shootingEnemiesCounter++; lvlEnemiesCounter++;
				}
				if(shootingEnemiesCounter > 13 && shootingEnemiesCounter <= 20){
					shootingEnemySpawnTimer = rand.nextFloat() * (4.6f - 2.2f) + 2.2f;
					game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 4));
					game.setShootingEnemies(game.getShootingEnemies());
					shootingEnemiesCounter++; lvlEnemiesCounter++;
				}
				if(shootingEnemiesCounter > 20 && shootingEnemiesCounter <= 29){
					shootingEnemySpawnTimer = rand.nextFloat() * (2.6f - 1.2f) + 1.2f;
					game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 4));
					game.setShootingEnemies(game.getShootingEnemies());
					shootingEnemiesCounter++; lvlEnemiesCounter++;
				}
				
				
			}
			
			//spawn simple enemies after the sixth shooting enemy
			if(shootingEnemiesCounter > 5 && simpleEnemySpawnTimer <= 0){
				if(simpleEnemiesCounter <= 10){
					simpleEnemySpawnTimer = rand.nextFloat() * (2.7f - 2f) + 2f;
					game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
					game.setSimpleEnemies(game.getSimpleEnemies());
					simpleEnemiesCounter++; lvlEnemiesCounter++;
				}
				if(simpleEnemiesCounter > 10 && simpleEnemiesCounter <= 20){
					simpleEnemySpawnTimer = rand.nextFloat() * (1.5f - 0.7f) + 0.7f;
					game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
					game.setSimpleEnemies(game.getSimpleEnemies());
					simpleEnemiesCounter++; lvlEnemiesCounter++;
				}
			}
			//spawn simple enemies after 20th shooting enemy (after the first half of the level)
			if(shootingEnemiesCounter > 20 && simpleEnemySpawnTimer <= 0){
				if(simpleEnemiesCounter <= 35){
					simpleEnemySpawnTimer = rand.nextFloat() * (1.5f - 1f) + 1f;
					game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
					game.setSimpleEnemies(game.getSimpleEnemies());
					simpleEnemiesCounter++; lvlEnemiesCounter++;
				}
				if(simpleEnemiesCounter > 35 && simpleEnemiesCounter <= 40){
					simpleEnemySpawnTimer = rand.nextFloat() * (0.5f - 0.2f) + 0.2f;
					game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
					game.setSimpleEnemies(game.getSimpleEnemies());
					simpleEnemiesCounter++; lvlEnemiesCounter++;
				}
				if(simpleEnemiesCounter > 40 && simpleEnemiesCounter <= 46){
					simpleEnemySpawnTimer = rand.nextFloat() * (3.7f - 2.2f) + 2.2f;
					game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
					game.setSimpleEnemies(game.getSimpleEnemies());
					simpleEnemiesCounter++; lvlEnemiesCounter++;
				}
			}
			
			//spawning asteroids after sixth shooting enemy and after the third simple enemy
			if(shootingEnemiesCounter > 5 && simpleEnemiesCounter > 3 && asteroidSpawnTimer <= 0){
				if(asteroidCounter <= 10){
					asteroidSpawnTimer = rand.nextFloat() * (1.7f - 0.9f) + 0.9f;
					game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
					game.setAsteroids(game.getAsteroids());
					asteroidCounter++; lvlEnemiesCounter++;
				}
				if(asteroidCounter > 10 && asteroidCounter <= 30){
					asteroidSpawnTimer = rand.nextFloat() * (1f - 0.4f) + 0.4f;
					game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
					game.setAsteroids(game.getAsteroids());
					asteroidCounter++; lvlEnemiesCounter++;
				}
			}
			//spawn asteroids after 20th shooting enemy
			if(shootingEnemiesCounter > 20 && asteroidSpawnTimer <= 0){
				if(asteroidCounter <= 40){
					asteroidSpawnTimer = rand.nextFloat() * (1.1f - 0.5f) + 0.5f;
					game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
					game.setAsteroids(game.getAsteroids());
					asteroidCounter++; lvlEnemiesCounter++;
				}
				if(asteroidCounter > 40 && asteroidCounter <= 44){
					asteroidSpawnTimer = rand.nextFloat() * (2.1f - 1.5f) + 1.5f;
					game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
					game.setAsteroids(game.getAsteroids());
					asteroidCounter++; lvlEnemiesCounter++;
				}
				
			}
			
			if(simpleEnemiesCounter > 46 && game.getSimpleEnemies().isEmpty() && !game.isStartEndLevelTimer() && !game.isEndLevel()){
				game.setStartEndLevelTimer(true);  isLevelDone = true;
			}
				
			if(simpleEnemiesCounter > 46 && game.getSimpleEnemies().isEmpty() && !game.isStartEndLevelTimer() && game.isEndLevel()){
				game.setisEndLevel(true);
				
			}
			
		}
		
		/* 											LEVEL 5
		 * 											LEVEL 5
		 * 											LEVEL 5 
		 * 											LEVEL 5
		 * 
		 * 
		 */
		
		if(level == 5){
			if(simpleEnemySpawnTimer <= 0){
				if(simpleEnemiesCounter <= 4){
					simpleEnemySpawnTimer = rand.nextFloat() * (4.5f - 3f) + 3f;
					game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
					game.setSimpleEnemies(game.getSimpleEnemies());
					simpleEnemiesCounter++; lvlEnemiesCounter++;
				}
				if(simpleEnemiesCounter > 4 && simpleEnemiesCounter <= 13){
					simpleEnemySpawnTimer = rand.nextFloat() * (1f - 0.4f) + 0.4f;
					game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
					game.setSimpleEnemies(game.getSimpleEnemies());
					simpleEnemiesCounter++; lvlEnemiesCounter++;
				}
				if(simpleEnemiesCounter > 13 && simpleEnemiesCounter <= 26){
					simpleEnemySpawnTimer = rand.nextFloat() * (1.8f - 0.9f) + 0.9f;
					game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
					game.setSimpleEnemies(game.getSimpleEnemies());
					simpleEnemiesCounter++; lvlEnemiesCounter++;
				}
			
				
			}
			
			//spawning asteroids after 3th simple enemy
			if(asteroidSpawnTimer <= 0 && simpleEnemiesCounter > 3){
				if(asteroidCounter <= 15){
				asteroidSpawnTimer = rand.nextFloat() * (0.7f - 0.3f) + 0.3f;
				game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
				game.setAsteroids(game.getAsteroids());
				asteroidCounter++; lvlEnemiesCounter++;
				}
				if(asteroidCounter > 15 && asteroidCounter <= 25){
				asteroidSpawnTimer = rand.nextFloat() * (1.6f - 1.2f) + 1.2f;
				game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
				game.setAsteroids(game.getAsteroids());
				asteroidCounter++; lvlEnemiesCounter++;
				}
			}
			
			//spawning shooting enemies after 14th simple enemy
			if(shootingEnemySpawnTimer <= 0 && simpleEnemiesCounter > 14){
				if(shootingEnemiesCounter <= 10){
					shootingEnemySpawnTimer = rand.nextFloat() * (2.4f - 1.5f) + 1.5f;
					game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 5));
					game.setShootingEnemies(game.getShootingEnemies());
					shootingEnemiesCounter++; lvlEnemiesCounter++;
				}
				if(shootingEnemiesCounter > 10 && shootingEnemiesCounter <= 14){
					shootingEnemySpawnTimer = rand.nextFloat() * (5.9f - 3f) + 3f;
					game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 5));
					game.setShootingEnemies(game.getShootingEnemies());
					shootingEnemiesCounter++; lvlEnemiesCounter++;
				}
				if(shootingEnemiesCounter == 15){
					isPause = true;
					pause(delta, 3);
				}
			}
			
			//spawning simple enemies after 15th shooting enemy(rampage)
			if(simpleEnemySpawnTimer <= 0 && shootingEnemiesCounter > 14 && !isPause){
				if(simpleEnemiesCounter > 26 && simpleEnemiesCounter <= 50){
					simpleEnemySpawnTimer = rand.nextFloat() * (0.3f - 0.2f) + 0.2f;
					game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
					game.setSimpleEnemies(game.getSimpleEnemies());
					simpleEnemiesCounter++; lvlEnemiesCounter++;
				}
				
			}
			//spawning asteroids after 15th shooting enemy(rampage)
			if(asteroidSpawnTimer <= 0 && shootingEnemiesCounter > 14 && !isPause){
				if(asteroidCounter > 25 && asteroidCounter <= 45){
				asteroidSpawnTimer = rand.nextFloat() * (0.1f - 0.07f) + 0.07f;
				game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
				game.setAsteroids(game.getAsteroids());
				asteroidCounter++; lvlEnemiesCounter++;
				}
			}
			
			//spawning simple enemies after the rampage
			if(simpleEnemySpawnTimer <= 0 && simpleEnemiesCounter > 50 && asteroidCounter > 45){
				if(simpleEnemiesCounter <= 56){
					simpleEnemySpawnTimer = rand.nextFloat() * (1.5f - 1.2f) + 1.2f;
					game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
					game.setSimpleEnemies(game.getSimpleEnemies());
					simpleEnemiesCounter++; lvlEnemiesCounter++;
				}
				if(simpleEnemiesCounter > 56 && simpleEnemiesCounter <= 60){
					simpleEnemySpawnTimer = rand.nextFloat() * (3.5f - 2.2f) + 2.2f;
					game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
					game.setSimpleEnemies(game.getSimpleEnemies());
					simpleEnemiesCounter++; lvlEnemiesCounter++;
				}
				
				
			}
			
			if(simpleEnemiesCounter > 60 && game.getSimpleEnemies().isEmpty() && !game.isStartEndLevelTimer() && !game.isEndLevel()){
				game.setStartEndLevelTimer(true);  isLevelDone = true;
			}
				
			if(simpleEnemiesCounter > 60 && game.getSimpleEnemies().isEmpty() && !game.isStartEndLevelTimer() && game.isEndLevel()){
				game.setisEndLevel(true);
				
			}
			
		}
		
		/* 											LEVEL 6
		 * 											LEVEL 6
		 * 											LEVEL 6 
		 * 											LEVEL 6
		 * 
		 * 
		 */
		
		if(level ==  6){
			if(shootingBulletsEnemiesSpawnTimer <= 0){
				if(shootingBulletsEnemiesCounter <= 3){
					shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (3.5f - 2.2f) + 2.2f;
					game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
					game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
					shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
				}
				if(shootingBulletsEnemiesCounter > 3 && shootingBulletsEnemiesCounter <= 9){
					shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (1.5f - 0.8f) + 0.8f;
					game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
					game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
					shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
				}
				if(shootingBulletsEnemiesCounter > 9 && shootingBulletsEnemiesCounter <= 20){
					shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (2.5f - 1.8f) + 1.8f;
					game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
					game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
					shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
				}
				if(shootingBulletsEnemiesCounter > 20 && shootingBulletsEnemiesCounter <= 33){
					shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (2.8f - 1.9f) + 1.9f;
					game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
					game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
					shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
				}
				
				if(shootingBulletsEnemiesCounter == 34){
					isPause = true;
					pause(delta, 2);
				}
				
				//spawn shooting bullets enemies after the pause
				if(shootingBulletsEnemiesCounter > 33 && shootingBulletsEnemiesCounter <= 46 && !isPause){
					shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (1.1f - 0.9f) + 0.9f;
					game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
					game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
					shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
				}
				if(shootingBulletsEnemiesCounter > 46 && shootingBulletsEnemiesCounter <= 52){
					shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (3.5f - 2.4f) + 0.4f;
					game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
					game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
					shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
				}
				if(shootingBulletsEnemiesCounter > 52 && shootingBulletsEnemiesCounter <= 60){
					shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (4.1f - 3.4f) + 3.4f;
					game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
					game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
					shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
				}
					
				
				
			}
			//spawn simple enemies after 10th shooting bullets enemy
			if(simpleEnemySpawnTimer <= 0 && shootingBulletsEnemiesCounter > 9){
				if(simpleEnemiesCounter <= 9){
					simpleEnemySpawnTimer = rand.nextFloat() * (1.5f - 1.2f) + 1.2f;
					game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
					game.setSimpleEnemies(game.getSimpleEnemies());
					simpleEnemiesCounter++; lvlEnemiesCounter++;
				}
				if(simpleEnemiesCounter > 9 && simpleEnemiesCounter <= 20){
					simpleEnemySpawnTimer = rand.nextFloat() * (1.1f - 0.8f) + 0.8f;
					game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
					game.setSimpleEnemies(game.getSimpleEnemies());
					simpleEnemiesCounter++; lvlEnemiesCounter++;
				}
				if(simpleEnemiesCounter > 20 && simpleEnemiesCounter <= 30){
					simpleEnemySpawnTimer = rand.nextFloat() * (0.7f - 0.5f) + 0.5f;
					game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
					game.setSimpleEnemies(game.getSimpleEnemies());
					simpleEnemiesCounter++; lvlEnemiesCounter++;
				}
				
				//spawn simple enemies after the pause
				if(simpleEnemiesCounter > 30 && simpleEnemiesCounter <= 40 && shootingBulletsEnemiesCounter > 35){
					simpleEnemySpawnTimer = rand.nextFloat() * (0.7f - 0.5f) + 0.5f;
					game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
					game.setSimpleEnemies(game.getSimpleEnemies());
					simpleEnemiesCounter++; lvlEnemiesCounter++;
				}
				
				if(simpleEnemiesCounter > 40 && simpleEnemiesCounter <= 55 && shootingBulletsEnemiesCounter > 52){
					simpleEnemySpawnTimer = rand.nextFloat() * (1.5f - 0.9f) + 0.9f;
					game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
					game.setSimpleEnemies(game.getSimpleEnemies());
					simpleEnemiesCounter++; lvlEnemiesCounter++;
				}
			}
			
			//spawning asteroids after 10th shooting bullets enemy
			if(asteroidSpawnTimer <= 0 && shootingBulletsEnemiesCounter > 13){
				if(asteroidCounter <= 15){
				asteroidSpawnTimer = rand.nextFloat() * (0.8f - 0.5f) + 0.5f;
				game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
				game.setAsteroids(game.getAsteroids());
				asteroidCounter++; lvlEnemiesCounter++;
				}
				if(asteroidCounter > 15 && asteroidCounter <= 30){
				asteroidSpawnTimer = rand.nextFloat() * (0.6f - 0.4f) + 0.4f;
				game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
				game.setAsteroids(game.getAsteroids());
				asteroidCounter++; lvlEnemiesCounter++;
				}
				
				//spawning asteroids after the pause and after the 53th shooting bullets enemy
				if(asteroidCounter > 30 && asteroidCounter <= 42 && shootingBulletsEnemiesCounter > 52){
					asteroidSpawnTimer = rand.nextFloat() * (1.1f - 0.6f) + 0.6f;
					game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
					game.setAsteroids(game.getAsteroids());
					asteroidCounter++; lvlEnemiesCounter++;
					}
			}
			
			if(shootingBulletsEnemiesCounter > 60 && game.getShootingBulletsEnemies().isEmpty() && !game.isStartEndLevelTimer() && !game.isEndLevel()){
				game.setStartEndLevelTimer(true);  isLevelDone = true;
			}
				
			if(shootingBulletsEnemiesCounter > 60 && game.getShootingBulletsEnemies().isEmpty() && !game.isStartEndLevelTimer() && game.isEndLevel()){
				game.setisEndLevel(true);
				
			}
			
		}
			/* 											LEVEL 7
			 * 											LEVEL 7
			 * 											LEVEL 7
			 * 											LEVEL 7
			 * 
			 * 
			 */
			
			if(level == 7){
			if(shootingBulletsEnemiesSpawnTimer <= 0){
				if(shootingBulletsEnemiesCounter <= 5){
					shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (3.5f - 2.2f) + 2.2f;
					game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
					game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
					shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
				}
				if(shootingBulletsEnemiesCounter > 5 && shootingBulletsEnemiesCounter <= 10){
					shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (1.5f - 0.8f) + 0.8f;
					game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
					game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
					shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
				}
				if(shootingBulletsEnemiesCounter > 10 && shootingBulletsEnemiesCounter <= 20){
					shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (1.5f - 0.8f) + 0.8f;
					game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
					game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
					shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
				}
				if(shootingBulletsEnemiesCounter > 20 && shootingBulletsEnemiesCounter <= 30){
					shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (1.9f - 1f) + 1f;
					game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
					game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
					shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
				}
			}
			
			//spawning simple enemies after 11th shooting bullets enemy
			if(simpleEnemySpawnTimer <= 0 && shootingBulletsEnemiesCounter > 10){
				if(simpleEnemiesCounter <= 10){
					simpleEnemySpawnTimer = rand.nextFloat() * (2.9f - 1.9f) + 1.9f;
					game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
					game.setSimpleEnemies(game.getSimpleEnemies());
					simpleEnemiesCounter++; lvlEnemiesCounter++;
				}
				if(simpleEnemiesCounter > 10 && simpleEnemiesCounter <= 20){
					simpleEnemySpawnTimer = rand.nextFloat() * (1.5f - 0.9f) + 0.9f;
					game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
					game.setSimpleEnemies(game.getSimpleEnemies());
					simpleEnemiesCounter++; lvlEnemiesCounter++;
				}
				if(simpleEnemiesCounter > 20 && simpleEnemiesCounter <= 30){
					simpleEnemySpawnTimer = rand.nextFloat() * (1.3f - 0.7f) + 0.7f;
					game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
					game.setSimpleEnemies(game.getSimpleEnemies());
					simpleEnemiesCounter++; lvlEnemiesCounter++;
				}
				if(simpleEnemiesCounter > 30 && simpleEnemiesCounter <= 35){
					simpleEnemySpawnTimer = rand.nextFloat() * (3.3f - 2.7f) + 2.7f;
					game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
					game.setSimpleEnemies(game.getSimpleEnemies());
					simpleEnemiesCounter++; lvlEnemiesCounter++;
				}
			}
			
			//spawning asteroids after 11th shooting bullets enemy
			if(asteroidSpawnTimer <= 0 && shootingBulletsEnemiesCounter > 10){
				if(asteroidCounter <= 30){
					asteroidSpawnTimer = rand.nextFloat() * (1.4f - 0.8f) + 0.8f;
					game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
					game.setAsteroids(game.getAsteroids());
					asteroidCounter++; lvlEnemiesCounter++;
				}
				
				//spawning asteroids after 35th simple enemy
				if(asteroidCounter <= 44 && simpleEnemiesCounter > 35){
					asteroidSpawnTimer = rand.nextFloat() * (0.3f - 0.1f) + 0.1f;
					game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
					game.setAsteroids(game.getAsteroids());
					asteroidCounter++; lvlEnemiesCounter++;
				}
			}
			
			//spawning shooting enemy after 11th shooting bullets enemy
			if(shootingEnemySpawnTimer <= 0 && shootingBulletsEnemiesCounter > 10){
				if(shootingEnemiesCounter <= 10){
					shootingEnemySpawnTimer = rand.nextFloat() * (3.4f - 2.5f) + 2.5f;
					game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 7));
					game.setShootingEnemies(game.getShootingEnemies());
					shootingEnemiesCounter++; lvlEnemiesCounter++;
				}
				if(shootingEnemiesCounter > 10 && shootingEnemiesCounter >= 20){
					shootingEnemySpawnTimer = rand.nextFloat() * (2.4f - 1.5f) + 1.5f;
					game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 7));
					game.setShootingEnemies(game.getShootingEnemies());
					shootingEnemiesCounter++; lvlEnemiesCounter++;
				}
				
				//spawning shooting bullets enemy after 45th asteroid
				if(asteroidCounter > 44 && shootingEnemiesCounter <= 18){
					shootingEnemySpawnTimer = rand.nextFloat() * (3.4f - 2.5f) + 2.5f;
					game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 7));
					game.setShootingEnemies(game.getShootingEnemies());
					shootingEnemiesCounter++; lvlEnemiesCounter++;
				}
				
			}
			
			if(shootingEnemiesCounter > 18 && game.getShootingEnemies().isEmpty() && !game.isStartEndLevelTimer() && !game.isEndLevel()){
				game.setStartEndLevelTimer(true);  isLevelDone = true;
			}
				
			if(shootingEnemiesCounter > 18 && game.getShootingEnemies().isEmpty() && !game.isStartEndLevelTimer() && game.isEndLevel()){
				game.setisEndLevel(true);
				
			}

			}
			/* 											LEVEL 8
			 * 											LEVEL 8
			 * 											LEVEL 8
			 * 											LEVEL 8
			 * 
			 * 
			 */
			
			
			
			if(level == 8){
				if(shootingEnemySpawnTimer <= 0){
					if(shootingEnemiesCounter <= 5){
						shootingEnemySpawnTimer = rand.nextFloat() * (2.4f - 1.9f) + 1.9f;
						game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 8));
						game.setShootingEnemies(game.getShootingEnemies());
						shootingEnemiesCounter++; lvlEnemiesCounter++;
					}
					if(shootingEnemiesCounter > 5 && shootingEnemiesCounter <= 12){
						shootingEnemySpawnTimer = rand.nextFloat() * (1.2f - 0.6f) + 0.6f;
						game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 8));
						game.setShootingEnemies(game.getShootingEnemies());
						shootingEnemiesCounter++; lvlEnemiesCounter++;
					}
					
					//spawning shooting enemies after 23th simple enemy
					if(shootingEnemiesCounter > 12 && shootingEnemiesCounter <= 32 && simpleEnemiesCounter > 22){
						shootingEnemySpawnTimer = rand.nextFloat() * (2.2f - 1.6f) + 1.6f;
						game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 8));
						game.setShootingEnemies(game.getShootingEnemies());
						shootingEnemiesCounter++; lvlEnemiesCounter++;
					}
					
				}
				
				//spawning shooting bullets enemies after 5th shooting enemy
				if(shootingBulletsEnemiesSpawnTimer <= 0 && shootingEnemiesCounter > 5){
					if(shootingBulletsEnemiesCounter <= 10){
						shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (1.1f - 0.5f) + 0.5f;
						game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
						game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
						shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
					}
					//spawning shooting bullets enemies after  19th shooting enemy
					if(shootingBulletsEnemiesCounter > 10 && shootingBulletsEnemiesCounter <= 30 && shootingEnemiesCounter >19){
						shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (2.1f - 1.5f) + 1.5f;
						game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
						game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
						shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
					}
				}
				
				//spawning simple enemies after 5th shooting enemy
				if(simpleEnemySpawnTimer <= 0 && shootingEnemiesCounter > 5){
					if(simpleEnemiesCounter <= 15){
						simpleEnemySpawnTimer = rand.nextFloat() * (1.1f - 0.6f) + 0.6f;
						game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
					}
					if(simpleEnemiesCounter > 15 && simpleEnemiesCounter <= 22){
						simpleEnemySpawnTimer = rand.nextFloat() * (2.3f - 1.8f) + 1.8f;
						game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
					}
					//spawning simple enemies after 19th shooting bullets enemy
					if(simpleEnemiesCounter > 22 && simpleEnemiesCounter <= 53 && shootingBulletsEnemiesCounter > 19){
						simpleEnemySpawnTimer = rand.nextFloat() * (2.3f - 1.8f) + 1.8f;
						game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
					}
					
					if(simpleEnemiesCounter == 54){
						isPause = true;
						pause(delta, 2);
					}
				}
				
				//spawning asteroids after 5th shooting enemy
				if(asteroidSpawnTimer <= 0 && shootingEnemiesCounter > 5){
					if(asteroidCounter <= 10){
						asteroidSpawnTimer = rand.nextFloat() * (1.4f - 1f) + 1f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
					}
					//spawning asteroids after 30th simple enemy
					if(asteroidCounter > 10 && asteroidCounter <= 22 && simpleEnemiesCounter > 30){
						asteroidSpawnTimer = rand.nextFloat() * (2.9f - 2f) + 2f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
					}
					//spawning asteroids after 54th simple enemy
					if(asteroidCounter > 22 && asteroidCounter <= 39 && simpleEnemiesCounter > 53){
						asteroidSpawnTimer = rand.nextFloat() * (0.1f - 0.1f) + 0.1f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
					}
					if(asteroidCounter > 39 && asteroidCounter <= 43 && simpleEnemiesCounter > 53){
						asteroidSpawnTimer = rand.nextFloat() * (1.5f - 1.1f) + 1.1f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
					}
				}
				
				if(asteroidCounter > 43 && game.getAsteroids().isEmpty() && !game.isStartEndLevelTimer() && !game.isEndLevel()){
					game.setStartEndLevelTimer(true);  isLevelDone = true;
				}
					
				if(asteroidCounter > 43 && game.getAsteroids().isEmpty() && !game.isStartEndLevelTimer() && game.isEndLevel()){
					game.setisEndLevel(true);
				}
				
			}
			
			/* 											LEVEL 9
			 * 											LEVEL 9
			 * 											LEVEL 9
			 * 											LEVEL 9
			 * 
			 * 
			 */
			
			if(level == 9){
				if(simpleEnemySpawnTimer <= 0){
					if(simpleEnemiesCounter <= 5){
						simpleEnemySpawnTimer = rand.nextFloat() * (1.3f - 0.8f) + 0.8f;
						game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
					}
					if(simpleEnemiesCounter > 5 && simpleEnemiesCounter <= 20){
						simpleEnemySpawnTimer = rand.nextFloat() * (2.3f - 1.8f) + 1.8f;
						game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
					}
					if(simpleEnemiesCounter > 20 && simpleEnemiesCounter <= 30){
						simpleEnemySpawnTimer = rand.nextFloat() * (2.2f - 1.7f) + 1.7f;
						game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
					}
					if(simpleEnemiesCounter > 30 && simpleEnemiesCounter <= 40){
						simpleEnemySpawnTimer = rand.nextFloat() * (4.6f - 3.1f) + 3.1f;
						game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
					}
					
					if(simpleEnemiesCounter == 41){
						isPause = true;
						pause(delta, 2);
					}
					
					//spawn simple enemies after 39th shooting enemy
					if(shootingEnemiesCounter > 39){
						if(simpleEnemiesCounter > 40 && simpleEnemiesCounter <= 45){
							simpleEnemySpawnTimer = rand.nextFloat() * (2.9f - 2.2f) + 2.2f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
					}
					
				}
				
				//spawn asteroids after 5th simple enemy
				if(asteroidSpawnTimer <= 0 && simpleEnemiesCounter > 5){
					if(asteroidCounter <= 15){
						asteroidSpawnTimer = rand.nextFloat() * (2.5f - 1.5f) + 1.5f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
					}
					if(asteroidCounter > 15 && asteroidCounter <= 30){
						asteroidSpawnTimer = rand.nextFloat() * (1.9f - 0.9f) + 0.9f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
					}
					
					
				}
				
				//spawn shooting enemies after 10th simple enemy
				if(shootingEnemySpawnTimer <= 0 && simpleEnemiesCounter > 9){
					if(shootingEnemiesCounter <= 10){
						shootingEnemySpawnTimer = rand.nextFloat() * (1.9f - 1.2f) + 1.2f;
						game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 9));
						game.setShootingEnemies(game.getShootingEnemies());
						shootingEnemiesCounter++; lvlEnemiesCounter++;
					}
					if(shootingEnemiesCounter > 10 && shootingEnemiesCounter <= 20){
						shootingEnemySpawnTimer = rand.nextFloat() * (3.2f - 1.9f) + 1.9f;
						game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 9));
						game.setShootingEnemies(game.getShootingEnemies());
						shootingEnemiesCounter++; lvlEnemiesCounter++;
					}
					
					//spawn line of shooting enemies after 41th simple enemy
					if(simpleEnemiesCounter > 40 && !isPause && shootingEnemiesCounter < 26){
				
						game.getShootingEnemies().add(new ShootingEnemy(0, MainGame.HEIGHT, 9));
						game.setShootingEnemies(game.getShootingEnemies());
						shootingEnemiesCounter++; lvlEnemiesCounter++;
						game.getShootingEnemies().add(new ShootingEnemy(80, MainGame.HEIGHT, 9));
						game.setShootingEnemies(game.getShootingEnemies());
						shootingEnemiesCounter++; lvlEnemiesCounter++;
						game.getShootingEnemies().add(new ShootingEnemy(160, MainGame.HEIGHT, 9));
						game.setShootingEnemies(game.getShootingEnemies());
						shootingEnemiesCounter++; lvlEnemiesCounter++;
						game.getShootingEnemies().add(new ShootingEnemy(240, MainGame.HEIGHT, 9));
						game.setShootingEnemies(game.getShootingEnemies());
						shootingEnemiesCounter++; lvlEnemiesCounter++;
						game.getShootingEnemies().add(new ShootingEnemy(320, MainGame.HEIGHT, 9));
						game.setShootingEnemies(game.getShootingEnemies());
						shootingEnemiesCounter++; lvlEnemiesCounter++;
						game.getShootingEnemies().add(new ShootingEnemy(400, MainGame.HEIGHT, 9));
						game.setShootingEnemies(game.getShootingEnemies());
						shootingEnemiesCounter++; lvlEnemiesCounter++;
					}
					
					
					if(shootingEnemiesCounter > 26 && shootingEnemiesCounter <= 29){
						shootingEnemySpawnTimer = rand.nextFloat() * (5.9f - 4.8f) + 4.8f;
						game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 9));
						game.setShootingEnemies(game.getShootingEnemies());
						shootingEnemiesCounter++; lvlEnemiesCounter++;
					}
					if(shootingEnemiesCounter > 29 && shootingEnemiesCounter <= 39){
						shootingEnemySpawnTimer = rand.nextFloat() * (2.9f - 1.8f) + 1.8f;
						game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 9));
						game.setShootingEnemies(game.getShootingEnemies());
						shootingEnemiesCounter++; lvlEnemiesCounter++;
					}
					if(shootingEnemiesCounter > 39 && shootingEnemiesCounter <= 46){
						shootingEnemySpawnTimer = rand.nextFloat() * (3.9f - 2.8f) + 2.8f;
						game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 9));
						game.setShootingEnemies(game.getShootingEnemies());
						shootingEnemiesCounter++; lvlEnemiesCounter++;
					}
					
				}
				
				//spawn shooting bullets enemies after 12th simple enemy
				if(shootingBulletsEnemiesSpawnTimer <= 0 && simpleEnemiesCounter > 12){
					if(shootingBulletsEnemiesCounter <= 10){
						shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (3.1f - 1.9f) + 1.9f;
						game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
						game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
						shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
					}
					if(shootingBulletsEnemiesCounter > 10 && shootingBulletsEnemiesCounter <= 14){
						shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (3.9f - 2.6f) + 2.6f;
						game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
						game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
						shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
					}
				}
				
				if(shootingEnemiesCounter > 46 && game.getShootingEnemies().isEmpty() && !game.isStartEndLevelTimer() && !game.isEndLevel()){
					game.setStartEndLevelTimer(true);  isLevelDone = true;
				}
					
				if(shootingEnemiesCounter > 46 && game.getShootingEnemies().isEmpty() && !game.isStartEndLevelTimer() && game.isEndLevel()){
					game.setisEndLevel(true);
				}
				
			}
				/* 											LEVEL 10
				 * 											LEVEL 10
				 * 											LEVEL 10
				 * 											LEVEL 10
				 * 
				 * 
				 */
				//only shooting enemies, can move it later
				if(level == 10){
					if(shootingEnemySpawnTimer <= 0){
						if(shootingEnemiesCounter <= 5){
							shootingEnemySpawnTimer = rand.nextFloat() * (1.9f - 0.8f) + 0.8f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 10));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 5 && shootingEnemiesCounter <= 17){
							shootingEnemySpawnTimer = rand.nextFloat() * (0.9f - 0.6f) + 0.6f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 10));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 17 && shootingEnemiesCounter <= 21){
							shootingEnemySpawnTimer = rand.nextFloat() * (0.6f - 0.3f) + 0.3f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 10));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 21 && shootingEnemiesCounter <= 24){
							shootingEnemySpawnTimer = rand.nextFloat() * (0.3f - 0.2f) + 0.2f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 10));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 24 && shootingEnemiesCounter <= 30){
							shootingEnemySpawnTimer = rand.nextFloat() * (3.3f - 2.2f) + 2.2f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 10));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 30 && shootingEnemiesCounter <= 40){
							shootingEnemySpawnTimer = rand.nextFloat() * (1.3f - 0.7f) + 0.7f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 10));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 40 && shootingEnemiesCounter <= 45){
							shootingEnemySpawnTimer = rand.nextFloat() * (0.3f - 0.3f) + 0.3f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 10));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 45 && shootingEnemiesCounter <= 60){
							shootingEnemySpawnTimer = rand.nextFloat() * (1.3f - 0.9f) + 0.9f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 10));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 60 && shootingEnemiesCounter <= 66){
							shootingEnemySpawnTimer = rand.nextFloat() * (3.3f - 2.9f) + 2.9f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 10));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 66 && shootingEnemiesCounter <= 69){
							shootingEnemySpawnTimer = rand.nextFloat() * (4.3f - 3.9f) + 3.9f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 10));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 69 && shootingEnemiesCounter <= 80){
							shootingEnemySpawnTimer = rand.nextFloat() * (1.7f - 0.9f) + 0.9f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 10));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 80 && shootingEnemiesCounter <= 95){
							shootingEnemySpawnTimer = rand.nextFloat() * (0.8f - 0.4f) + 0.4f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 10));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 95 && shootingEnemiesCounter <= 100){
							shootingEnemySpawnTimer = rand.nextFloat() * (0.2f - 0.1f) + 0.1f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 10));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 100 && shootingEnemiesCounter <= 110){
							shootingEnemySpawnTimer = rand.nextFloat() * (3.2f - 2.1f) + 2.1f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 10));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						if(shootingEnemiesCounter > 110 && game.getShootingEnemies().isEmpty() && !game.isStartEndLevelTimer() && !game.isEndLevel()){
							game.setStartEndLevelTimer(true);  isLevelDone = true;
						}
							
						if(shootingEnemiesCounter > 110 && game.getShootingEnemies().isEmpty() && !game.isStartEndLevelTimer() && game.isEndLevel()){
							game.setisEndLevel(true);
						}
						
					}
					
				}
				
				/* 											LEVEL 11
				 * 											LEVEL 11
				 * 											LEVEL 11
				 * 											LEVEL 11
				 * 
				 * 
				 */
			
				if(level == 11){
					if(simpleEnemySpawnTimer <= 0){
						if(simpleEnemiesCounter <= 3){
							simpleEnemySpawnTimer = rand.nextFloat() * (3.9f - 2.2f) + 2.2f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 3 && simpleEnemiesCounter <= 10){
							simpleEnemySpawnTimer = rand.nextFloat() * (1.6f - 0.7f) + 0.7f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 10 && simpleEnemiesCounter <= 20){
							simpleEnemySpawnTimer = rand.nextFloat() * (2.9f - 1.6f) + 1.6f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 20 && simpleEnemiesCounter <= 30){
							simpleEnemySpawnTimer = rand.nextFloat() * (2.9f - 2.1f) + 2.1f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 30 && simpleEnemiesCounter <= 35){
							simpleEnemySpawnTimer = rand.nextFloat() * (3.4f - 2.3f) + 2.3f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 35 && simpleEnemiesCounter <= 42){
							simpleEnemySpawnTimer = rand.nextFloat() * (4.9f - 3.9f) + 3.9f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						if(simpleEnemiesCounter == 43){
							isPause = true;
							pause(delta, 2);
						}
						
						//spawn simple enemies after the pause (rampage)
						if(simpleEnemiesCounter > 42 && simpleEnemiesCounter <= 62 && !isPause){
							simpleEnemySpawnTimer = rand.nextFloat() * (0.6f - 0.2f) + 0.2f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 62 && simpleEnemiesCounter <= 72){
							simpleEnemySpawnTimer = rand.nextFloat() * (4.6f - 3.2f) + 3.2f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						if(simpleEnemiesCounter > 72 && simpleEnemiesCounter <= 94){
							simpleEnemySpawnTimer = rand.nextFloat() * (2.9f - 1.4f) + 1.4f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						if(simpleEnemiesCounter == 95){
							isPause = true;
							pause(delta, 4);
						}
						
						if(simpleEnemiesCounter > 94 && simpleEnemiesCounter <= 101 && !isPause){
						//spawn a line of simple enemies after 95th
						game.getSimpleEnemies().add(new SimpleEnemy(30, MainGame.HEIGHT, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
						game.getSimpleEnemies().add(new SimpleEnemy(100, MainGame.HEIGHT, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
						game.getSimpleEnemies().add(new SimpleEnemy(170, MainGame.HEIGHT, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
						game.getSimpleEnemies().add(new SimpleEnemy(240, MainGame.HEIGHT, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
						game.getSimpleEnemies().add(new SimpleEnemy(310, MainGame.HEIGHT, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
						game.getSimpleEnemies().add(new SimpleEnemy(380, MainGame.HEIGHT, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
						game.getSimpleEnemies().add(new SimpleEnemy(450, MainGame.HEIGHT, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						if(simpleEnemiesCounter > 101 && simpleEnemiesCounter <= 109){
							simpleEnemySpawnTimer = rand.nextFloat() * (3.9f - 2.4f) + 2.4f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						
						
					}
					
					//spawn shooting enemies after 11th simple enemy
					if(shootingEnemySpawnTimer <= 0 && simpleEnemiesCounter > 10){
						if(shootingEnemiesCounter <= 10){
							shootingEnemySpawnTimer = rand.nextFloat() * (2.6f - 1.6f) + 1.6f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 11));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 10 && shootingEnemiesCounter <= 20){
							shootingEnemySpawnTimer = rand.nextFloat() * (4.3f - 3.6f) + 3.6f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 11));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						//spawn shooting enemies after the pause(rampage)
						if(shootingEnemiesCounter > 20 && shootingEnemiesCounter <= 39 && simpleEnemiesCounter >42 && !isPause) {
							shootingEnemySpawnTimer = rand.nextFloat() * (0.6f - 0.2f) + 0.2f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 11));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						//spawn shooting enemies after the rampage
						if(shootingEnemiesCounter > 39 && shootingEnemiesCounter <= 55 && simpleEnemiesCounter >72) {
							shootingEnemySpawnTimer = rand.nextFloat() * (2.9f - 2.5f) + 2.5f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 11));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
					}
					
					//spawn shooting bullets enemies after 16th simple enemy
					if(shootingBulletsEnemiesSpawnTimer <= 0 && simpleEnemiesCounter > 15){
						if(shootingBulletsEnemiesCounter <= 10){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (2.8f - 2.2f) + 2.2f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						//spawn shooting bullets enemies after the pause(rampage)
						if(shootingBulletsEnemiesCounter > 10 && shootingBulletsEnemiesCounter <= 25 && simpleEnemiesCounter >42  && !isPause){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (0.8f - 0.4f) + 0.4f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						//spawn shooting bullets enemies after the rampage
						if(shootingBulletsEnemiesCounter > 25 && shootingBulletsEnemiesCounter <= 35 && simpleEnemiesCounter >72 ){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (3.8f - 2.6f) + 2.5f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						
					}
					
					//spawn asteroids after 11th simple enemy
					if(asteroidSpawnTimer <= 0 && simpleEnemiesCounter > 10){
						if(asteroidCounter <= 20){
							asteroidSpawnTimer = rand.nextFloat() * (2.9f - 2.1f) + 2.1f;
							game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
							game.setAsteroids(game.getAsteroids());
							asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//spawn asteroids after the pause(rampage)
						if(asteroidCounter > 20 && asteroidCounter <= 40 && simpleEnemiesCounter >42  && !isPause){
							asteroidSpawnTimer = rand.nextFloat() * (0.9f - 0.5f) + 0.5f;
							game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
							game.setAsteroids(game.getAsteroids());
							asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//spawn asteroids after the rampage
						if(asteroidCounter > 40 && asteroidCounter <= 45 && simpleEnemiesCounter >72){
							asteroidSpawnTimer = rand.nextFloat() * (3.9f - 2.5f) + 2.5f;
							game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
							game.setAsteroids(game.getAsteroids());
							asteroidCounter++; lvlEnemiesCounter++;
						}
					}
					
					if(simpleEnemiesCounter > 109 && game.getSimpleEnemies().isEmpty() && !game.isStartEndLevelTimer() && !game.isEndLevel()){
						game.setStartEndLevelTimer(true);  isLevelDone = true;
					}
						
					if(simpleEnemiesCounter > 109 && game.getSimpleEnemies().isEmpty() && !game.isStartEndLevelTimer() && game.isEndLevel()){
						game.setisEndLevel(true);
						
					}
				}
				
				if(level == 12){
					if(shootingBulletsEnemiesSpawnTimer <= 0){
						if(shootingBulletsEnemiesCounter < 1){
						game.getBossEnemies().add(new BossEnemy(MainGame.WIDTH / 2 - 60, MainGame.HEIGHT, 12, "boss1"));
						game.setBossEnemies(game.getBossEnemies());
						shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						if(shootingBulletsEnemiesCounter == 1){
							isPause = true;
							pause(delta, 8);
						}
					}
					
					if(shootingBulletsEnemiesCounter == 1 && !isPause){
						if(shootingEnemiesCounter < 2){
							game.getShootingEnemies().add(new ShootingEnemy(0, MainGame.HEIGHT+20, 12));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
							game.getShootingEnemies().add(new ShootingEnemy(MainGame.WIDTH - 64, MainGame.HEIGHT+20, 12));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
					}
					
					if(isFirstBossKilled && !game.isStartEndLevelTimer() && !game.isEndLevel()){
						game.setStartEndLevelTimer(true);  isLevelDone = true;
					}
						
					if(isFirstBossKilled && !game.isStartEndLevelTimer() && game.isEndLevel()){
						game.setisEndLevel(true);
						
					}
					
				}
				
				if(level == 13){
					if(simpleEnemySpawnTimer <= 0){
						if(simpleEnemiesCounter <= 3){
							simpleEnemySpawnTimer = rand.nextFloat() * (3.9f - 2.2f) + 2.2f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 13));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						if(simpleEnemiesCounter == 4){
							isPause = true;
							pause(delta, 4);
						}
						
						if(simpleEnemiesCounter > 3 && simpleEnemiesCounter <= 10 && !isPause){
							simpleEnemySpawnTimer = rand.nextFloat() * (2.4f - 1.5f) + 1.5f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 13));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 10 && simpleEnemiesCounter <= 20){
							simpleEnemySpawnTimer = rand.nextFloat() * (1.4f - 0.6f) + 0.6f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 13));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 20 && simpleEnemiesCounter <= 30){
							simpleEnemySpawnTimer = rand.nextFloat() * (1f - 0.4f) + 0.4f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 13));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 30 && simpleEnemiesCounter <= 50){
							simpleEnemySpawnTimer = rand.nextFloat() * (0.9f - 0.8f) + 0.8f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 13));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 50 && simpleEnemiesCounter <= 60){
							simpleEnemySpawnTimer = rand.nextFloat() * (0.3f - 0.2f) + 0.2f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 13));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 60 && simpleEnemiesCounter <= 65){
							simpleEnemySpawnTimer = rand.nextFloat() * (3f - 2.4f) + 2.4f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 13));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 65 && simpleEnemiesCounter <= 75){
							simpleEnemySpawnTimer = rand.nextFloat() * (2f - 1f) + 1f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 13));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 75 && simpleEnemiesCounter <= 90){
							simpleEnemySpawnTimer = rand.nextFloat() * (1f - 0.4f) + 0.4f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 13));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 90 && simpleEnemiesCounter <= 100){
							simpleEnemySpawnTimer = rand.nextFloat() * (0.3f - 0.1f) + 0.1f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 13));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 100 && simpleEnemiesCounter <= 110){
							simpleEnemySpawnTimer = rand.nextFloat() * (0.9f - 0.3f) + 0.3f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 13));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 110 && simpleEnemiesCounter <= 120){
							simpleEnemySpawnTimer = rand.nextFloat() * (1.9f - 0.6f) + 0.6f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 13));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 120 && simpleEnemiesCounter <= 125){
							simpleEnemySpawnTimer = rand.nextFloat() * (2.9f - 1.6f) + 1.6f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 13));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 125 && simpleEnemiesCounter <= 130){
							simpleEnemySpawnTimer = rand.nextFloat() * (3.9f - 2.6f) + 2.6f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 13));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 130 && simpleEnemiesCounter <= 135){
							simpleEnemySpawnTimer = rand.nextFloat() * (4.9f - 3.6f) + 3.6f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 13));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
					}
				
				if(simpleEnemiesCounter > 135 && game.getSimpleEnemies().isEmpty() && !game.isStartEndLevelTimer() && !game.isEndLevel()){
					game.setStartEndLevelTimer(true);  isLevelDone = true;
				}
					
				if(simpleEnemiesCounter > 135 && game.getSimpleEnemies().isEmpty() && !game.isStartEndLevelTimer() && game.isEndLevel()){
					game.setisEndLevel(true);
					
				}
				}
				if(level == 14){
					if(shootingFreezingBulletsEnemiesSpawnTimer <= 0){
						if(shootingFreezingBulletsEnemiesCounter <= 6){
							shootingFreezingBulletsEnemiesSpawnTimer = rand.nextFloat() * (4.9f - 3.6f) + 3.6f;
							game.getShootingFreezingBulletsEnemies().add(new ShootingFreezingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 14));
							game.setShootingFreezingBulletsEnemies(game.getShootingFreezingBulletsEnemies());
							shootingFreezingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingFreezingBulletsEnemiesCounter > 6 && shootingFreezingBulletsEnemiesCounter <= 25){
							shootingFreezingBulletsEnemiesSpawnTimer = rand.nextFloat() * (9.9f - 8.6f) + 8.6f;
							game.getShootingFreezingBulletsEnemies().add(new ShootingFreezingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 14));
							game.setShootingFreezingBulletsEnemies(game.getShootingFreezingBulletsEnemies());
							shootingFreezingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingFreezingBulletsEnemiesCounter > 25 && shootingFreezingBulletsEnemiesCounter <= 30){
							shootingFreezingBulletsEnemiesSpawnTimer = rand.nextFloat() * (11.9f - 6.6f) + 6.6f;
							game.getShootingFreezingBulletsEnemies().add(new ShootingFreezingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 14));
							game.setShootingFreezingBulletsEnemies(game.getShootingFreezingBulletsEnemies());
							shootingFreezingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
					}
					
					//spawning shooting bullets enemies after 7th freezing bullet enemy
					if(shootingBulletsEnemiesSpawnTimer <= 0 && shootingFreezingBulletsEnemiesCounter > 6){
						if(shootingBulletsEnemiesCounter <= 6){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (1.8f - 0.6f) + 0.6f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingBulletsEnemiesCounter > 6 && shootingBulletsEnemiesCounter <= 30){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (4.8f - 3.6f) + 3.6f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingBulletsEnemiesCounter > 30 && shootingBulletsEnemiesCounter <= 45){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (9.8f - 4.6f) + 4.6f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
					}
					
					//spawn shooting enemies after 6th shooting bullets enemy
					if(shootingEnemySpawnTimer <= 0 && shootingBulletsEnemiesCounter > 6){
						if(shootingEnemiesCounter <= 10){
							shootingEnemySpawnTimer = rand.nextFloat() * (2.6f - 1.6f) + 1.6f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 14));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 10 && shootingEnemiesCounter <= 25){
							shootingEnemySpawnTimer = rand.nextFloat() * (4.6f - 2.6f) + 2.6f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 14));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 25 && shootingEnemiesCounter <= 40){
							shootingEnemySpawnTimer = rand.nextFloat() * (3.6f - 2.1f) + 2.1f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 14));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 40 && shootingEnemiesCounter <= 50){
							shootingEnemySpawnTimer = rand.nextFloat() * (5.6f - 3.1f) + 3.1f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 14));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
				}
					
					//spawn simple enemies after 5th shooting enemy
					if(simpleEnemySpawnTimer <= 0 && shootingEnemiesCounter >5){
						if(simpleEnemiesCounter <= 15){
							simpleEnemySpawnTimer = rand.nextFloat() * (2.9f - 1.7f) + 1.7f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 14));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 15 && simpleEnemiesCounter <= 50){
							simpleEnemySpawnTimer = rand.nextFloat() * (2.9f - 2.4f) + 2.4f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 14));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 50 && simpleEnemiesCounter <= 65){
							simpleEnemySpawnTimer = rand.nextFloat() * (4.9f - 2.4f) + 2.4f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 14));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 65 && simpleEnemiesCounter <= 70){
							simpleEnemySpawnTimer = rand.nextFloat() * (6.9f - 4.4f) + 4.4f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 14));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						
					}
					
					//spawn asteroids after 35 simple enemy
					if(asteroidSpawnTimer <= 0 && simpleEnemiesCounter > 45){
						if(asteroidCounter <= 15){
							asteroidSpawnTimer = rand.nextFloat() * (3.8f - 2.4f) + 2.4f;
							game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
							game.setAsteroids(game.getAsteroids());
							asteroidCounter++; lvlEnemiesCounter++;
						}
					}
				
				
				if(simpleEnemiesCounter > 70 && game.getSimpleEnemies().isEmpty() && !game.isStartEndLevelTimer() && !game.isEndLevel()){
					game.setStartEndLevelTimer(true);  isLevelDone = true;
				}
					
				if(simpleEnemiesCounter > 70 && game.getSimpleEnemies().isEmpty() && !game.isStartEndLevelTimer() && game.isEndLevel()){
					game.setisEndLevel(true);
					
				}
				
				}
				
				if(level == 15){
					if(shootingEnemySpawnTimer <= 0){
						if(shootingEnemiesCounter <= 3){
							shootingEnemySpawnTimer = rand.nextFloat() * (3.6f - 2.1f) + 2.1f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 15));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 3 && shootingEnemiesCounter <= 15){
							shootingEnemySpawnTimer = rand.nextFloat() * (1.6f - 1.1f) + 1.1f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 15));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 15 && shootingEnemiesCounter <= 30){
							shootingEnemySpawnTimer = rand.nextFloat() * (2.6f - 2.1f) + 2.1f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 15));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 30 && shootingEnemiesCounter <= 55){
							shootingEnemySpawnTimer = rand.nextFloat() * (4.6f - 2.1f) + 2.1f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 15));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
					}
					if(simpleEnemySpawnTimer <= 0 && shootingEnemiesCounter > 12){
						if(simpleEnemiesCounter <= 15){
							simpleEnemySpawnTimer = rand.nextFloat() * (1.1f - 0.4f) + 0.4f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 15 && simpleEnemiesCounter <= 45){
							simpleEnemySpawnTimer = rand.nextFloat() * (3.1f - 1.4f) + 1.4f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 45 && simpleEnemiesCounter <= 50){
							simpleEnemySpawnTimer = rand.nextFloat() * (1.1f - 0.7f) + 0.7f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 50 && simpleEnemiesCounter <= 75){
							simpleEnemySpawnTimer = rand.nextFloat() * (3.1f - 2.7f) + 2.7f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
					}
					
					if(shootingBulletsEnemiesSpawnTimer <= 0 && simpleEnemiesCounter > 12){
						if(shootingBulletsEnemiesCounter <= 35){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (3.8f - 1.6f) + 1.6f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
					}
					
					if(shootingFreezingBulletsEnemiesSpawnTimer <= 0 && shootingEnemiesCounter > 10){
						if(shootingFreezingBulletsEnemiesCounter <= 10){
							shootingFreezingBulletsEnemiesSpawnTimer = rand.nextFloat() * (17.9f - 9.6f) + 9.6f;
							game.getShootingFreezingBulletsEnemies().add(new ShootingFreezingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setShootingFreezingBulletsEnemies(game.getShootingFreezingBulletsEnemies());
							shootingFreezingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						
					}
					
					if(asteroidSpawnTimer <= 0 && simpleEnemiesCounter > 10){
						if(asteroidCounter <= 20){
							asteroidSpawnTimer = rand.nextFloat() * (6.8f - 3.4f) + 3.4f;
							game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
							game.setAsteroids(game.getAsteroids());
							asteroidCounter++; lvlEnemiesCounter++;
						}
					}
					
					if(simpleEnemiesCounter > 75 && game.getSimpleEnemies().isEmpty() && !game.isStartEndLevelTimer() && !game.isEndLevel()){
						game.setStartEndLevelTimer(true);  isLevelDone = true;
					}
						
					if(simpleEnemiesCounter > 75 && game.getSimpleEnemies().isEmpty() && !game.isStartEndLevelTimer() && game.isEndLevel()){
						game.setisEndLevel(true);
						
					}
					
				}
				
				if(level == 16){
					if(simpleEnemySpawnTimer <= 0){
						if(simpleEnemiesCounter <= 5){
							simpleEnemySpawnTimer = rand.nextFloat() * (1.3f - 0.8f) + 0.8f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 16));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 5 && simpleEnemiesCounter <= 20){
							simpleEnemySpawnTimer = rand.nextFloat() * (2.3f - 1.8f) + 1.8f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 16));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 20 && simpleEnemiesCounter <= 30){
							simpleEnemySpawnTimer = rand.nextFloat() * (2.2f - 1.7f) + 1.7f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 16));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 30 && simpleEnemiesCounter <= 40){
							simpleEnemySpawnTimer = rand.nextFloat() * (4.6f - 3.1f) + 3.1f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 16));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						if(simpleEnemiesCounter == 41){
							isPause = true;
							pause(delta, 2);
						}
						
						//spawn simple enemies after 39th shooting enemy
						if(shootingEnemiesCounter > 39){
							if(simpleEnemiesCounter > 40 && simpleEnemiesCounter <= 50){
								simpleEnemySpawnTimer = rand.nextFloat() * (2.9f - 2.2f) + 2.2f;
								game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 16));
								game.setSimpleEnemies(game.getSimpleEnemies());
								simpleEnemiesCounter++; lvlEnemiesCounter++;
							}
						}
						
					}
					
					//spawn asteroids after 5th simple enemy
					if(asteroidSpawnTimer <= 0 && simpleEnemiesCounter > 5){
						if(asteroidCounter <= 15){
							asteroidSpawnTimer = rand.nextFloat() * (2.5f - 1.5f) + 1.5f;
							game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
							game.setAsteroids(game.getAsteroids());
							asteroidCounter++; lvlEnemiesCounter++;
						}
						if(asteroidCounter > 15 && asteroidCounter <= 30){
							asteroidSpawnTimer = rand.nextFloat() * (1.9f - 0.9f) + 0.9f;
							game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
							game.setAsteroids(game.getAsteroids());
							asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//spawn asteroids after 44th shooting enemy
						if(asteroidCounter > 30 && asteroidCounter <= 55 && shootingEnemiesCounter > 44){
							asteroidSpawnTimer = rand.nextFloat() * (1.4f - 0.1f) + 0.1f;
							game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
							game.setAsteroids(game.getAsteroids());
							asteroidCounter++; lvlEnemiesCounter++;
						}
						if(asteroidCounter > 55 && asteroidCounter <= 75 && shootingEnemiesCounter > 54){
							asteroidSpawnTimer = rand.nextFloat() * (0.5f - 0.1f) + 0.1f;
							game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
							game.setAsteroids(game.getAsteroids());
							asteroidCounter++; lvlEnemiesCounter++;
						}
						
						
					}
					
					//spawn shooting enemies after 10th simple enemy
					if(shootingEnemySpawnTimer <= 0 && simpleEnemiesCounter > 9){
						if(shootingEnemiesCounter <= 10){
							shootingEnemySpawnTimer = rand.nextFloat() * (1.9f - 1.2f) + 1.2f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 16));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 10 && shootingEnemiesCounter <= 20){
							shootingEnemySpawnTimer = rand.nextFloat() * (3.2f - 1.9f) + 1.9f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 16));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						//spawn line of shooting enemies after 41th simple enemy
						if(simpleEnemiesCounter > 40 && !isPause && shootingEnemiesCounter < 26){
					
							game.getShootingEnemies().add(new ShootingEnemy(0, MainGame.HEIGHT, 9));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
							game.getShootingEnemies().add(new ShootingEnemy(80, MainGame.HEIGHT, 9));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
							game.getShootingEnemies().add(new ShootingEnemy(160, MainGame.HEIGHT, 9));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
							game.getShootingEnemies().add(new ShootingEnemy(240, MainGame.HEIGHT, 9));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
							game.getShootingEnemies().add(new ShootingEnemy(320, MainGame.HEIGHT, 9));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
							game.getShootingEnemies().add(new ShootingEnemy(400, MainGame.HEIGHT, 9));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						
						if(shootingEnemiesCounter > 26 && shootingEnemiesCounter <= 29){
							shootingEnemySpawnTimer = rand.nextFloat() * (5.9f - 4.8f) + 4.8f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 16));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 29 && shootingEnemiesCounter <= 39){
							shootingEnemySpawnTimer = rand.nextFloat() * (2.9f - 1.8f) + 1.8f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 16));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 39 && shootingEnemiesCounter <= 65){
							shootingEnemySpawnTimer = rand.nextFloat() * (3.9f - 2.2f) + 2.2f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 16));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						
					}
					
					//spawn shooting freezing bullets enemies
					if(shootingFreezingBulletsEnemiesSpawnTimer <= 0 && simpleEnemiesCounter > 5){
						if(shootingFreezingBulletsEnemiesCounter <= 6){
							shootingFreezingBulletsEnemiesSpawnTimer = rand.nextFloat() * (17.9f - 9.6f) + 9.6f;
							game.getShootingFreezingBulletsEnemies().add(new ShootingFreezingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setShootingFreezingBulletsEnemies(game.getShootingFreezingBulletsEnemies());
							shootingFreezingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
					}
					
					//spawn shooting bullets enemies after 12th simple enemy
					if(shootingBulletsEnemiesSpawnTimer <= 0 && simpleEnemiesCounter > 12){
						if(shootingBulletsEnemiesCounter <= 10){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (3.1f - 1.9f) + 1.9f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 16));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingBulletsEnemiesCounter > 10 && shootingBulletsEnemiesCounter <= 20){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (3.9f - 2.6f) + 2.6f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 16));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
					}
					
					if(shootingEnemiesCounter > 65 && game.getShootingEnemies().isEmpty() && !game.isStartEndLevelTimer() && !game.isEndLevel()){
						game.setStartEndLevelTimer(true);  isLevelDone = true;
					}
						
					if(shootingEnemiesCounter > 65 && game.getShootingEnemies().isEmpty() && !game.isStartEndLevelTimer() && game.isEndLevel()){
						game.setisEndLevel(true);
					}
				}
				
				if(level == 17){
					if(simpleEnemySpawnTimer <= 0){
						if(simpleEnemiesCounter <= 3){
							simpleEnemySpawnTimer = rand.nextFloat() * (3.9f - 2.2f) + 2.2f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 3 && simpleEnemiesCounter <= 10){
							simpleEnemySpawnTimer = rand.nextFloat() * (1.6f - 0.7f) + 0.7f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 10 && simpleEnemiesCounter <= 20){
							simpleEnemySpawnTimer = rand.nextFloat() * (2.9f - 1.6f) + 1.6f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 20 && simpleEnemiesCounter <= 30){
							simpleEnemySpawnTimer = rand.nextFloat() * (2.9f - 2.1f) + 2.1f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 30 && simpleEnemiesCounter <= 35){
							simpleEnemySpawnTimer = rand.nextFloat() * (3.4f - 2.3f) + 2.3f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 35 && simpleEnemiesCounter <= 42){
							simpleEnemySpawnTimer = rand.nextFloat() * (4.9f - 3.9f) + 3.9f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						if(simpleEnemiesCounter == 43){
							isPause = true;
							pause(delta, 2);
						}
						
						//spawn simple enemies after the pause (rampage)
						if(simpleEnemiesCounter > 42 && simpleEnemiesCounter <= 65 && !isPause){
							simpleEnemySpawnTimer = rand.nextFloat() * (0.4f - 0.2f) + 0.2f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 65 && simpleEnemiesCounter <= 72){
							simpleEnemySpawnTimer = rand.nextFloat() * (4.6f - 3.2f) + 3.2f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						if(simpleEnemiesCounter > 72 && simpleEnemiesCounter <= 94){
							simpleEnemySpawnTimer = rand.nextFloat() * (2.9f - 1.4f) + 1.4f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						if(simpleEnemiesCounter == 95){
							isPause = true;
							pause(delta, 4);
						}
						
						if(simpleEnemiesCounter > 94 && simpleEnemiesCounter <= 101 && !isPause){
						//spawn a line of simple enemies after 95th
						game.getSimpleEnemies().add(new SimpleEnemy(30, MainGame.HEIGHT, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
						game.getSimpleEnemies().add(new SimpleEnemy(100, MainGame.HEIGHT, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
						game.getSimpleEnemies().add(new SimpleEnemy(170, MainGame.HEIGHT, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
						game.getSimpleEnemies().add(new SimpleEnemy(240, MainGame.HEIGHT, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
						game.getSimpleEnemies().add(new SimpleEnemy(310, MainGame.HEIGHT, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
						game.getSimpleEnemies().add(new SimpleEnemy(380, MainGame.HEIGHT, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
						game.getSimpleEnemies().add(new SimpleEnemy(450, MainGame.HEIGHT, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						if(simpleEnemiesCounter > 101 && simpleEnemiesCounter <= 109){
							simpleEnemySpawnTimer = rand.nextFloat() * (3.9f - 2.4f) + 2.4f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						
						
					}
					
					//spawn shooting enemies after 11th simple enemy
					if(shootingEnemySpawnTimer <= 0 && simpleEnemiesCounter > 10){
						if(shootingEnemiesCounter <= 10){
							shootingEnemySpawnTimer = rand.nextFloat() * (2.6f - 1.6f) + 1.6f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 17));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 10 && shootingEnemiesCounter <= 20){
							shootingEnemySpawnTimer = rand.nextFloat() * (4.3f - 3.6f) + 3.6f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 17));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						//spawn shooting enemies after the pause(rampage)
						if(shootingEnemiesCounter > 20 && shootingEnemiesCounter <= 45 && simpleEnemiesCounter >42 && !isPause) {
							shootingEnemySpawnTimer = rand.nextFloat() * (0.4f - 0.1f) + 0.1f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 17));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						//spawn shooting enemies after the rampage
						if(shootingEnemiesCounter > 45 && shootingEnemiesCounter <= 60 && simpleEnemiesCounter >72) {
							shootingEnemySpawnTimer = rand.nextFloat() * (2.9f - 2.5f) + 2.5f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 17));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
					}
					
					//spawn shooting bullets enemies after 16th simple enemy
					if(shootingBulletsEnemiesSpawnTimer <= 0 && simpleEnemiesCounter > 15){
						if(shootingBulletsEnemiesCounter <= 10){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (2.8f - 2.2f) + 2.2f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						//spawn shooting bullets enemies after the pause(rampage)
						if(shootingBulletsEnemiesCounter > 10 && shootingBulletsEnemiesCounter <= 25 && simpleEnemiesCounter >42  && !isPause){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (0.8f - 0.4f) + 0.4f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						//spawn shooting bullets enemies after the rampage
						if(shootingBulletsEnemiesCounter > 25 && shootingBulletsEnemiesCounter <= 35 && simpleEnemiesCounter >72 ){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (3.8f - 2.6f) + 2.5f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						
					}
					
					if(shootingFreezingBulletsEnemiesSpawnTimer <= 0){
						if(shootingFreezingBulletsEnemiesCounter <= 10){
							shootingFreezingBulletsEnemiesSpawnTimer = rand.nextFloat() * (17.9f - 9.6f) + 9.6f;
							game.getShootingFreezingBulletsEnemies().add(new ShootingFreezingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setShootingFreezingBulletsEnemies(game.getShootingFreezingBulletsEnemies());
							shootingFreezingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
					}
					
					//spawn asteroids after 11th simple enemy
					if(asteroidSpawnTimer <= 0 && simpleEnemiesCounter > 10){
						if(asteroidCounter <= 20){
							asteroidSpawnTimer = rand.nextFloat() * (2.9f - 2.1f) + 2.1f;
							game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
							game.setAsteroids(game.getAsteroids());
							asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//spawn asteroids after the pause(rampage)
						if(asteroidCounter > 20 && asteroidCounter <= 40 && simpleEnemiesCounter >42  && !isPause){
							asteroidSpawnTimer = rand.nextFloat() * (0.9f - 0.5f) + 0.5f;
							game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
							game.setAsteroids(game.getAsteroids());
							asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//spawn asteroids after the rampage
						if(asteroidCounter > 40 && asteroidCounter <= 45 && simpleEnemiesCounter >72){
							asteroidSpawnTimer = rand.nextFloat() * (3.9f - 2.5f) + 2.5f;
							game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
							game.setAsteroids(game.getAsteroids());
							asteroidCounter++; lvlEnemiesCounter++;
						}
					}
					
					if(simpleEnemiesCounter > 109 && game.getSimpleEnemies().isEmpty() && !game.isStartEndLevelTimer() && !game.isEndLevel()){
						game.setStartEndLevelTimer(true);  isLevelDone = true;
					}
						
					if(simpleEnemiesCounter > 109 && game.getSimpleEnemies().isEmpty() && !game.isStartEndLevelTimer() && game.isEndLevel()){
						game.setisEndLevel(true);
						
					}
				}
				
				if(level == 18){
					if(shootingFreezingBulletsEnemiesSpawnTimer <= 0){
						if(shootingFreezingBulletsEnemiesCounter <= 5){
							shootingFreezingBulletsEnemiesSpawnTimer = rand.nextFloat() * (3.9f - 2.6f) + 2.6f;
							game.getShootingFreezingBulletsEnemies().add(new ShootingFreezingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 18));
							game.setShootingFreezingBulletsEnemies(game.getShootingFreezingBulletsEnemies());
							shootingFreezingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						if(shootingFreezingBulletsEnemiesCounter > 5 && shootingFreezingBulletsEnemiesCounter <= 50){
							shootingFreezingBulletsEnemiesSpawnTimer = rand.nextFloat() * (1.9f - 1.1f) + 1.1f;
							game.getShootingFreezingBulletsEnemies().add(new ShootingFreezingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 18));
							game.setShootingFreezingBulletsEnemies(game.getShootingFreezingBulletsEnemies());
							shootingFreezingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						if(shootingFreezingBulletsEnemiesCounter == 4){
							isPause = true;
							pause(delta, 3);
						}
					}
					
					if(simpleEnemySpawnTimer <= 0 && shootingFreezingBulletsEnemiesCounter > 6){
						if(simpleEnemiesCounter <= 10){
							simpleEnemySpawnTimer = rand.nextFloat() * (2.9f - 1.4f) + 1.4f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 18));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 10 && simpleEnemiesCounter <= 30){
							simpleEnemySpawnTimer = rand.nextFloat() * (1.3f - 0.4f) + 0.4f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 18));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 30 && simpleEnemiesCounter <= 40){
							simpleEnemySpawnTimer = rand.nextFloat() * (0.6f - 0.2f) + 0.2f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 18));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 40 && simpleEnemiesCounter <= 70){
							simpleEnemySpawnTimer = rand.nextFloat() * (1.3f - 0.6f) + 0.6f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 18));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 70 && simpleEnemiesCounter <= 75){
							simpleEnemySpawnTimer = rand.nextFloat() * (3.3f - 1.6f) + 1.6f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 18));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
					}
					
					if(simpleEnemiesCounter > 75 && game.getSimpleEnemies().isEmpty() && !game.isStartEndLevelTimer() && !game.isEndLevel()){
						game.setStartEndLevelTimer(true);  isLevelDone = true;
					}
						
					if(simpleEnemiesCounter > 75 && game.getSimpleEnemies().isEmpty() && !game.isStartEndLevelTimer() && game.isEndLevel()){
						game.setisEndLevel(true);
						
					}
					
				}
				
				if(level == 19){
					if(shootingBulletsEnemiesSpawnTimer <= 0){
						if(shootingBulletsEnemiesCounter <= 5){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (3.5f - 2.2f) + 2.2f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 19));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingBulletsEnemiesCounter > 5 && shootingBulletsEnemiesCounter <= 10){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (1.5f - 0.8f) + 0.8f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 19));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingBulletsEnemiesCounter > 10 && shootingBulletsEnemiesCounter <= 20){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (1.5f - 0.8f) + 0.8f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 19));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingBulletsEnemiesCounter > 20 && shootingBulletsEnemiesCounter <= 30){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (1.9f - 1f) + 1f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 19));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
					}
					
					//spawning simple enemies after 11th shooting bullets enemy
					if(simpleEnemySpawnTimer <= 0 && shootingBulletsEnemiesCounter > 10){
						if(simpleEnemiesCounter <= 10){
							simpleEnemySpawnTimer = rand.nextFloat() * (2.9f - 1.9f) + 1.9f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 19));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 10 && simpleEnemiesCounter <= 20){
							simpleEnemySpawnTimer = rand.nextFloat() * (1.5f - 0.9f) + 0.9f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 19));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 20 && simpleEnemiesCounter <= 30){
							simpleEnemySpawnTimer = rand.nextFloat() * (1.3f - 0.7f) + 0.7f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 19));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 30 && simpleEnemiesCounter <= 35){
							simpleEnemySpawnTimer = rand.nextFloat() * (3.3f - 2.7f) + 2.7f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 19));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
					}
					
					if(shootingFreezingBulletsEnemiesSpawnTimer <= 0 && shootingBulletsEnemiesCounter > 15){
						if(shootingFreezingBulletsEnemiesCounter <= 10){
							shootingFreezingBulletsEnemiesSpawnTimer = rand.nextFloat() * (4.9f - 3.1f) + 3.1f;
							game.getShootingFreezingBulletsEnemies().add(new ShootingFreezingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 19));
							game.setShootingFreezingBulletsEnemies(game.getShootingFreezingBulletsEnemies());
							shootingFreezingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
					}
					
					//spawning asteroids after 11th shooting bullets enemy
					if(asteroidSpawnTimer <= 0 && shootingBulletsEnemiesCounter > 10){
						if(asteroidCounter <= 30){
							asteroidSpawnTimer = rand.nextFloat() * (1.4f - 0.8f) + 0.8f;
							game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
							game.setAsteroids(game.getAsteroids());
							asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//spawning asteroids after 35th simple enemy
						if(asteroidCounter <= 50 && simpleEnemiesCounter > 35){
							asteroidSpawnTimer = rand.nextFloat() * (0.3f - 0.1f) + 0.1f;
							game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
							game.setAsteroids(game.getAsteroids());
							asteroidCounter++; lvlEnemiesCounter++;
						}
					}
					
					//spawning shooting enemy after 11th shooting bullets enemy
					if(shootingEnemySpawnTimer <= 0 && shootingBulletsEnemiesCounter > 10){
						if(shootingEnemiesCounter <= 10){
							shootingEnemySpawnTimer = rand.nextFloat() * (3.4f - 2.5f) + 2.5f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 19));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 10 && shootingEnemiesCounter <= 20){
							shootingEnemySpawnTimer = rand.nextFloat() * (2.4f - 1.5f) + 1.5f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 19));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 20 && shootingEnemiesCounter <= 34){
							shootingEnemySpawnTimer = rand.nextFloat() * (2.9f - 0.9f) + 0.9f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 19));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						
					
						
					}
					
					if(shootingEnemiesCounter > 34 && game.getShootingEnemies().isEmpty() && !game.isStartEndLevelTimer() && !game.isEndLevel()){
						game.setStartEndLevelTimer(true);  isLevelDone = true;
					}
						
					if(shootingEnemiesCounter > 34 && game.getShootingEnemies().isEmpty() && !game.isStartEndLevelTimer() && game.isEndLevel()){
						game.setisEndLevel(true);
						
					}

				}
				
				if(level == 20){

					if(simpleEnemySpawnTimer <= 0){
						if(simpleEnemiesCounter <= 4){
							simpleEnemySpawnTimer = rand.nextFloat() * (4.5f - 3f) + 3f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 20));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 4 && simpleEnemiesCounter <= 13){
							simpleEnemySpawnTimer = rand.nextFloat() * (1f - 0.4f) + 0.4f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 20));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 13 && simpleEnemiesCounter <= 26){
							simpleEnemySpawnTimer = rand.nextFloat() * (1.8f - 0.9f) + 0.9f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 20));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
					
						
					}
					
					//spawning shooting freezing enemeis after 3th simple enemy
					if(shootingFreezingBulletsEnemiesSpawnTimer <= 0 && simpleEnemiesCounter > 3){
						if(shootingFreezingBulletsEnemiesCounter <= 20){
							shootingFreezingBulletsEnemiesSpawnTimer = rand.nextFloat() * (4.9f - 3.1f) + 3.1f;
							game.getShootingFreezingBulletsEnemies().add(new ShootingFreezingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 20));
							game.setShootingFreezingBulletsEnemies(game.getShootingFreezingBulletsEnemies());
							shootingFreezingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
					}
					
					//spawning asteroids after 3th simple enemy
					if(asteroidSpawnTimer <= 0 && simpleEnemiesCounter > 3){
						if(asteroidCounter <= 15){
						asteroidSpawnTimer = rand.nextFloat() * (0.7f - 0.3f) + 0.3f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
						if(asteroidCounter > 15 && asteroidCounter <= 25){
						asteroidSpawnTimer = rand.nextFloat() * (1.6f - 1.2f) + 1.2f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
					}
					
					//spawning shooting enemies after 14th simple enemy
					if(shootingEnemySpawnTimer <= 0 && simpleEnemiesCounter > 14){
						if(shootingEnemiesCounter <= 10){
							shootingEnemySpawnTimer = rand.nextFloat() * (2.4f - 1.5f) + 1.5f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 20));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 10 && shootingEnemiesCounter <= 14){
							shootingEnemySpawnTimer = rand.nextFloat() * (5.9f - 3f) + 3f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 20));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter == 15){
							isPause = true;
							pause(delta, 3);
						}
					}
					
					//spawning simple enemies after 15th shooting enemy(rampage)
					if(simpleEnemySpawnTimer <= 0 && shootingEnemiesCounter > 14 && !isPause){
						if(simpleEnemiesCounter > 26 && simpleEnemiesCounter <= 50){
							simpleEnemySpawnTimer = rand.nextFloat() * (0.3f - 0.2f) + 0.2f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 20));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						
					}
					//spawning asteroids after 15th shooting enemy(rampage)
					if(asteroidSpawnTimer <= 0 && shootingEnemiesCounter > 14 && !isPause){
						if(asteroidCounter > 25 && asteroidCounter <= 45){
						asteroidSpawnTimer = rand.nextFloat() * (0.1f - 0.07f) + 0.07f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
					}
					
					//spawning simple enemies after the rampage
					if(simpleEnemySpawnTimer <= 0 && simpleEnemiesCounter > 50 && asteroidCounter > 45){
						if(simpleEnemiesCounter <= 56){
							simpleEnemySpawnTimer = rand.nextFloat() * (1.5f - 1.2f) + 1.2f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 20));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 56 && simpleEnemiesCounter <= 60){
							simpleEnemySpawnTimer = rand.nextFloat() * (3.5f - 2.2f) + 2.2f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 20));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 60 && simpleEnemiesCounter <= 80){
							simpleEnemySpawnTimer = rand.nextFloat() * (2.5f - 1.2f) + 1.2f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 20));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 80 && simpleEnemiesCounter <= 90){
							simpleEnemySpawnTimer = rand.nextFloat() * (0.8f - 0.3f) + 0.3f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 20));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 90 && simpleEnemiesCounter <= 100){
							simpleEnemySpawnTimer = rand.nextFloat() * (3.5f - 2.2f) + 2.2f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 20));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						
					}
					
					//spawning shooting enemies after 60th simple enemy
					if(shootingEnemySpawnTimer <= 0 && simpleEnemiesCounter > 60){
						if(shootingEnemiesCounter <= 39){
							shootingEnemySpawnTimer = rand.nextFloat() * (2.9f - 1.9f) + 1.9f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 20));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
					}
					
					//spawn asteroids after 60th simple enemy
					if(asteroidSpawnTimer <= 0 && simpleEnemiesCounter > 60){
						if(asteroidCounter <= 70){
							asteroidSpawnTimer = rand.nextFloat() * (0.9f - 0.3f) + 0.3f;
							game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
							game.setAsteroids(game.getAsteroids());
							asteroidCounter++; lvlEnemiesCounter++;
						}
					}
					
					if(simpleEnemiesCounter > 100 && game.getSimpleEnemies().isEmpty() && !game.isStartEndLevelTimer() && !game.isEndLevel()){
						game.setStartEndLevelTimer(true);  isLevelDone = true;
					}
						
					if(simpleEnemiesCounter > 100 && game.getSimpleEnemies().isEmpty() && !game.isStartEndLevelTimer() && game.isEndLevel()){
						game.setisEndLevel(true);
						
					}
				}
				
				if(level == 21){
					if(shootingEnemySpawnTimer <= 0){
						if(shootingEnemiesCounter <= 5){
							shootingEnemySpawnTimer = rand.nextFloat() * (1.9f - 0.8f) + 0.8f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 5 && shootingEnemiesCounter <= 20){
							shootingEnemySpawnTimer = rand.nextFloat() * (0.7f - 0.5f) + 0.5f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 20 && shootingEnemiesCounter <= 25){
							shootingEnemySpawnTimer = rand.nextFloat() * (0.4f - 0.3f) + 0.3f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 25 && shootingEnemiesCounter <= 31){
							shootingEnemySpawnTimer = rand.nextFloat() * (0.3f - 0.2f) + 0.2f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 31 && shootingEnemiesCounter <= 35){
							shootingEnemySpawnTimer = rand.nextFloat() * (3.3f - 2.2f) + 2.2f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 35 && shootingEnemiesCounter <= 45){
							shootingEnemySpawnTimer = rand.nextFloat() * (1.3f - 0.7f) + 0.7f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 45 && shootingEnemiesCounter <= 50){
							shootingEnemySpawnTimer = rand.nextFloat() * (0.2f - 0.1f) + 0.1f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 50 && shootingEnemiesCounter <= 65){
							shootingEnemySpawnTimer = rand.nextFloat() * (1.3f - 0.9f) + 0.9f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 65 && shootingEnemiesCounter <= 71){
							shootingEnemySpawnTimer = rand.nextFloat() * (3.3f - 2.9f) + 2.9f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 71 && shootingEnemiesCounter <= 75){
							shootingEnemySpawnTimer = rand.nextFloat() * (4.3f - 3.9f) + 3.9f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 75 && shootingEnemiesCounter <= 90){
							shootingEnemySpawnTimer = rand.nextFloat() * (1.6f - 0.8f) + 0.8f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 90 && shootingEnemiesCounter <= 105){
							shootingEnemySpawnTimer = rand.nextFloat() * (0.8f - 0.4f) + 0.4f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 105 && shootingEnemiesCounter <= 110){
							shootingEnemySpawnTimer = rand.nextFloat() * (0.2f - 0.1f) + 0.1f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 110 && shootingEnemiesCounter <= 115){
							shootingEnemySpawnTimer = rand.nextFloat() * (3.2f - 2.1f) + 2.1f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 115 && shootingEnemiesCounter <= 125){
							shootingEnemySpawnTimer = rand.nextFloat() * (1.7f - 0.9f) + 0.9f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 125 && shootingEnemiesCounter <= 130){
							shootingEnemySpawnTimer = rand.nextFloat() * (0.4f - 0.1f) + 0.1f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 130 && shootingEnemiesCounter <= 140){
							shootingEnemySpawnTimer = rand.nextFloat() * (1.4f - 1.1f) + 1.1f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 140 && shootingEnemiesCounter <= 145){
							shootingEnemySpawnTimer = rand.nextFloat() * (3.4f - 2.1f) + 2.1f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						if(shootingEnemiesCounter > 145 && game.getShootingEnemies().isEmpty() && !game.isStartEndLevelTimer() && !game.isEndLevel()){
							game.setStartEndLevelTimer(true);  isLevelDone = true;
						}
							
						if(shootingEnemiesCounter > 145 && game.getShootingEnemies().isEmpty() && !game.isStartEndLevelTimer() && game.isEndLevel()){
							game.setisEndLevel(true);
						}
						
					}
				}
				
				
				
				if(level == 22){
					
					if(asteroidSpawnTimer <= 0){
					
						//Asteroids spawning
						if(asteroidCounter <= 5){
						asteroidSpawnTimer = rand.nextFloat() * (2f - 1.7f) + 1.7f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
							
						}
						//Asteroids spawning
						if(asteroidCounter > 5 && asteroidCounter <= 15){
						asteroidSpawnTimer = rand.nextFloat() * (0.9f - 0.6f) + 0.6f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
							
						}
						//Asteroids spawning
						if(asteroidCounter > 15 && asteroidCounter <= 30){
						asteroidSpawnTimer = rand.nextFloat() * (0.7f - 0.4f) + 0.4f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
							
						}
						//Asteroids spawning
						if(asteroidCounter > 30 && asteroidCounter <= 55){
						asteroidSpawnTimer = rand.nextFloat() * (0.2f - 0.1f) + 0.1f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
							
						}
						//Asteroids spawning
						if(asteroidCounter > 55 && asteroidCounter <= 70){
						asteroidSpawnTimer = rand.nextFloat() * (0.09f - 0.05f) + 0.05f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
							
						}
						//Asteroids spawning
						if(asteroidCounter > 70 && asteroidCounter <= 75){
						asteroidSpawnTimer = rand.nextFloat() * (0.6f - 0.3f) + 0.3f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
							
						}
						//Asteroids spawning
						if(asteroidCounter > 75 && asteroidCounter <= 85){
						asteroidSpawnTimer = rand.nextFloat() * (1.9f - 0.7f) + 0.7f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}

						//Asteroids spawning
						if(asteroidCounter > 85 && asteroidCounter <= 95){
						asteroidSpawnTimer = rand.nextFloat() * (0.9f - 0.5f) + 0.5f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//Asteroids spawning
						if(asteroidCounter > 95 && asteroidCounter <= 115){
						asteroidSpawnTimer = rand.nextFloat() * (0.8f - 0.1f) + 0.1f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//Asteroids spawning
						if(asteroidCounter > 115 && asteroidCounter <= 135){
						asteroidSpawnTimer = rand.nextFloat() * (0.5f - 0.1f) + 0.1f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//Asteroids spawning
						if(asteroidCounter > 135 && asteroidCounter <= 150){
						asteroidSpawnTimer = rand.nextFloat() * (0.08f - 0.06f) + 0.04f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//Asteroids spawning
						if(asteroidCounter > 150 && asteroidCounter <= 160){
						asteroidSpawnTimer = rand.nextFloat() * (0.3f - 0.1f) + 0.1f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//Asteroids spawning
						if(asteroidCounter > 160 && asteroidCounter <= 180){
						asteroidSpawnTimer = rand.nextFloat() * (0.4f - 0.1f) + 0.1f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//Asteroids spawning
						if(asteroidCounter > 180 && asteroidCounter <= 190){
						asteroidSpawnTimer = rand.nextFloat() * (0.8f - 0.3f) + 0.3f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//Asteroids spawning
						if(asteroidCounter > 190 && asteroidCounter <= 195){
						asteroidSpawnTimer = rand.nextFloat() * (3f - 1.6f) + 1.6f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//Asteroids spawning
						if(asteroidCounter > 195 && asteroidCounter <= 200){
						asteroidSpawnTimer = rand.nextFloat() * (2.2f - 1.1f) + 1.1f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//Asteroids spawning
						if(asteroidCounter > 200 && asteroidCounter <= 230){
						asteroidSpawnTimer = rand.nextFloat() * (0.2f - 0.1f) + 0.1f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//Asteroids spawning
						if(asteroidCounter > 230 && asteroidCounter <= 240){
						asteroidSpawnTimer = rand.nextFloat() * (0.09f - 0.04f) + 0.04f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//Asteroids spawning
						if(asteroidCounter > 240 && asteroidCounter <= 245){
						asteroidSpawnTimer = rand.nextFloat() * (5f - 3f) + 3f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//Asteroids spawning
						if(asteroidCounter > 245 && asteroidCounter <= 260){
						asteroidSpawnTimer = rand.nextFloat() * (0.09f - 0.06f) + 0.06f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//Asteroids spawning
						if(asteroidCounter > 260 && asteroidCounter <= 270){
						asteroidSpawnTimer = rand.nextFloat() * (0.1f - 0.1f) + 0.1f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//Asteroids spawning
						if(asteroidCounter > 270 && asteroidCounter <= 280){
						asteroidSpawnTimer = rand.nextFloat() * (0.3f - 0.1f) + 0.1f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//Asteroids spawning
						if(asteroidCounter > 280 && asteroidCounter <= 295){
						asteroidSpawnTimer = rand.nextFloat() * (0.7f - 0.4f) + 0.4f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//Asteroids spawning
						if(asteroidCounter > 295 && asteroidCounter < 300){
						asteroidSpawnTimer = rand.nextFloat() * (6f - 3f) + 3f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
					
					}
					
					if(asteroidCounter >= 300 && game.getAsteroids().isEmpty() && !game.isStartEndLevelTimer() && !game.isEndLevel()){
						game.setStartEndLevelTimer(true);  isLevelDone = true;
					}
						
					if(asteroidCounter >= 300 && game.getAsteroids().isEmpty() && !game.isStartEndLevelTimer() && game.isEndLevel()){
						game.setisEndLevel(true);
						
					}
				}
				

				
				if(level == 23){
					if(simpleEnemySpawnTimer <= 0){
						if(simpleEnemiesCounter <= 3){
							simpleEnemySpawnTimer = rand.nextFloat() * (3.9f - 2.2f) + 2.2f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 3 && simpleEnemiesCounter <= 10){
							simpleEnemySpawnTimer = rand.nextFloat() * (1.6f - 0.7f) + 0.7f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 10 && simpleEnemiesCounter <= 20){
							simpleEnemySpawnTimer = rand.nextFloat() * (2.9f - 1.6f) + 1.6f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 20 && simpleEnemiesCounter <= 30){
							simpleEnemySpawnTimer = rand.nextFloat() * (2.9f - 2.1f) + 2.1f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 30 && simpleEnemiesCounter <= 35){
							simpleEnemySpawnTimer = rand.nextFloat() * (3.4f - 2.3f) + 2.3f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 35 && simpleEnemiesCounter <= 42){
							simpleEnemySpawnTimer = rand.nextFloat() * (4.9f - 3.9f) + 3.9f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						if(simpleEnemiesCounter == 43){
							isPause = true;
							pause(delta, 2);
						}
						
						//spawn simple enemies after the pause (rampage)
						if(simpleEnemiesCounter > 42 && simpleEnemiesCounter <= 65 && !isPause){
							simpleEnemySpawnTimer = rand.nextFloat() * (0.4f - 0.2f) + 0.2f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 65 && simpleEnemiesCounter <= 72){
							simpleEnemySpawnTimer = rand.nextFloat() * (4.6f - 3.2f) + 3.2f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						if(simpleEnemiesCounter > 72 && simpleEnemiesCounter <= 94){
							simpleEnemySpawnTimer = rand.nextFloat() * (2.9f - 1.4f) + 1.4f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						if(simpleEnemiesCounter == 95){
							isPause = true;
							pause(delta, 4);
						}
						
						if(simpleEnemiesCounter > 94 && simpleEnemiesCounter <= 101 && !isPause){
						//spawn a line of simple enemies after 95th
						game.getSimpleEnemies().add(new SimpleEnemy(30, MainGame.HEIGHT, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
						game.getSimpleEnemies().add(new SimpleEnemy(100, MainGame.HEIGHT, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
						game.getSimpleEnemies().add(new SimpleEnemy(170, MainGame.HEIGHT, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
						game.getSimpleEnemies().add(new SimpleEnemy(240, MainGame.HEIGHT, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
						game.getSimpleEnemies().add(new SimpleEnemy(310, MainGame.HEIGHT, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
						game.getSimpleEnemies().add(new SimpleEnemy(380, MainGame.HEIGHT, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
						game.getSimpleEnemies().add(new SimpleEnemy(450, MainGame.HEIGHT, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						if(simpleEnemiesCounter > 101 && simpleEnemiesCounter <= 109){
							simpleEnemySpawnTimer = rand.nextFloat() * (3.9f - 2.4f) + 2.4f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						
						
						
					}
					
					//spawn shooting enemies after 11th simple enemy
					if(shootingEnemySpawnTimer <= 0 && simpleEnemiesCounter > 10){
						if(shootingEnemiesCounter <= 10){
							shootingEnemySpawnTimer = rand.nextFloat() * (2.6f - 1.6f) + 1.6f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 17));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 10 && shootingEnemiesCounter <= 20){
							shootingEnemySpawnTimer = rand.nextFloat() * (4.3f - 3.6f) + 3.6f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 17));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						//spawn shooting enemies after the pause(rampage)
						if(shootingEnemiesCounter > 20 && shootingEnemiesCounter <= 45 && simpleEnemiesCounter >42 && !isPause) {
							shootingEnemySpawnTimer = rand.nextFloat() * (0.4f - 0.1f) + 0.1f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 17));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						//spawn shooting enemies after the rampage
						if(shootingEnemiesCounter > 45 && shootingEnemiesCounter <= 60 && simpleEnemiesCounter >72) {
							shootingEnemySpawnTimer = rand.nextFloat() * (2.9f - 2.5f) + 2.5f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 17));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
					}
					
					//spawn shooting bullets enemies after 16th simple enemy
					if(shootingBulletsEnemiesSpawnTimer <= 0 && simpleEnemiesCounter > 15){
						if(shootingBulletsEnemiesCounter <= 10){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (2.8f - 2.2f) + 2.2f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						//spawn shooting bullets enemies after the pause(rampage)
						if(shootingBulletsEnemiesCounter > 10 && shootingBulletsEnemiesCounter <= 25 && simpleEnemiesCounter >42  && !isPause){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (0.8f - 0.4f) + 0.4f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						//spawn shooting bullets enemies after the rampage
						if(shootingBulletsEnemiesCounter > 25 && shootingBulletsEnemiesCounter <= 35 && simpleEnemiesCounter >72 ){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (3.8f - 2.6f) + 2.5f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						
					}
					
					if(shootingFreezingBulletsEnemiesSpawnTimer <= 0){
						if(shootingFreezingBulletsEnemiesCounter <= 10){
							shootingFreezingBulletsEnemiesSpawnTimer = rand.nextFloat() * (17.9f - 9.6f) + 9.6f;
							game.getShootingFreezingBulletsEnemies().add(new ShootingFreezingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setShootingFreezingBulletsEnemies(game.getShootingFreezingBulletsEnemies());
							shootingFreezingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
					}
					
					//spawn asteroids after 11th simple enemy
					if(asteroidSpawnTimer <= 0 && simpleEnemiesCounter > 10){
						if(asteroidCounter <= 20){
							asteroidSpawnTimer = rand.nextFloat() * (2.9f - 2.1f) + 2.1f;
							game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
							game.setAsteroids(game.getAsteroids());
							asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//spawn asteroids after the pause(rampage)
						if(asteroidCounter > 20 && asteroidCounter <= 40 && simpleEnemiesCounter >42  && !isPause){
							asteroidSpawnTimer = rand.nextFloat() * (0.9f - 0.5f) + 0.5f;
							game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
							game.setAsteroids(game.getAsteroids());
							asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//spawn asteroids after the rampage
						if(asteroidCounter > 40 && asteroidCounter <= 45 && simpleEnemiesCounter >72){
							asteroidSpawnTimer = rand.nextFloat() * (3.9f - 2.5f) + 2.5f;
							game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
							game.setAsteroids(game.getAsteroids());
							asteroidCounter++; lvlEnemiesCounter++;
						}
					}
					
					if(simpleEnemiesCounter > 109 && game.getSimpleEnemies().isEmpty() && !game.isStartEndLevelTimer() && !game.isEndLevel()){
						game.setStartEndLevelTimer(true);  isLevelDone = true;
					}
						
					if(simpleEnemiesCounter > 109 && game.getSimpleEnemies().isEmpty() && !game.isStartEndLevelTimer() && game.isEndLevel()){
						game.setisEndLevel(true);
						
					}
				}
				
				if(level == 24){
					if(asteroidSpawnTimer <= 0){
						if(asteroidCounter < 1){
							game.getBossEnemies().add(new BossEnemy(MainGame.WIDTH / 2 - 60, MainGame.HEIGHT, 36, "boss2"));
							asteroidCounter++; lvlEnemiesCounter++;
						}
					}
					
					if(simpleEnemySpawnTimer <= 0 && spawnEnemiesWithBoss && !isFirstBossKilled){
						if(simpleEnemiesCounter <= 10){
							simpleEnemySpawnTimer = rand.nextFloat() * (6.9f - 4.4f) + 4.4f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 14));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; //lvlEnemiesCounter++;
						}
					}
					
					if(isFirstBossKilled && !game.isStartEndLevelTimer() && !game.isEndLevel()){
						game.setStartEndLevelTimer(true);  isLevelDone = true;
					}
						
					if(isFirstBossKilled && !game.isStartEndLevelTimer() && game.isEndLevel()){
						game.setisEndLevel(true);
						
					}
				}
				
				if(level == 25){
					if(shootingEnemySpawnTimer <= 0){
						if(shootingEnemiesCounter <= 3){
							shootingEnemySpawnTimer = rand.nextFloat() * (3.6f - 2.1f) + 2.1f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 15));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 3 && shootingEnemiesCounter <= 15){
							shootingEnemySpawnTimer = rand.nextFloat() * (1.6f - 1.1f) + 1.1f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 15));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 15 && shootingEnemiesCounter <= 30){
							shootingEnemySpawnTimer = rand.nextFloat() * (2.6f - 2.1f) + 2.1f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 15));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 30 && shootingEnemiesCounter <= 55){
							shootingEnemySpawnTimer = rand.nextFloat() * (4.6f - 2.1f) + 2.1f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 15));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
					}
					if(simpleEnemySpawnTimer <= 0 && shootingEnemiesCounter > 12){
						if(simpleEnemiesCounter <= 15){
							simpleEnemySpawnTimer = rand.nextFloat() * (1.1f - 0.4f) + 0.4f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 15 && simpleEnemiesCounter <= 45){
							simpleEnemySpawnTimer = rand.nextFloat() * (3.1f - 1.4f) + 1.4f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 45 && simpleEnemiesCounter <= 50){
							simpleEnemySpawnTimer = rand.nextFloat() * (1.1f - 0.7f) + 0.7f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 50 && simpleEnemiesCounter <= 75){
							simpleEnemySpawnTimer = rand.nextFloat() * (3.1f - 2.7f) + 2.7f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
					}
					
					if(shootingBulletsEnemiesSpawnTimer <= 0 && simpleEnemiesCounter > 12){
						if(shootingBulletsEnemiesCounter <= 35){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (3.8f - 1.6f) + 1.6f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
					}
					
					if(shootingFreezingBulletsEnemiesSpawnTimer <= 0 && shootingEnemiesCounter > 10){
						if(shootingFreezingBulletsEnemiesCounter <= 8){
							shootingFreezingBulletsEnemiesSpawnTimer = rand.nextFloat() * (17.9f - 9.6f) + 9.6f;
							game.getShootingFreezingBulletsEnemies().add(new ShootingFreezingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setShootingFreezingBulletsEnemies(game.getShootingFreezingBulletsEnemies());
							shootingFreezingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						
					}
					
					if(asteroidSpawnTimer <= 0 && simpleEnemiesCounter > 10){
						if(asteroidCounter <= 20){
							asteroidSpawnTimer = rand.nextFloat() * (6.8f - 3.4f) + 3.4f;
							game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
							game.setAsteroids(game.getAsteroids());
							asteroidCounter++; lvlEnemiesCounter++;
						}
					}
					
					if(simpleEnemiesCounter > 75 && game.getSimpleEnemies().isEmpty() && !game.isStartEndLevelTimer() && !game.isEndLevel()){
						game.setStartEndLevelTimer(true);  isLevelDone = true;
					}
						
					if(simpleEnemiesCounter > 75 && game.getSimpleEnemies().isEmpty() && !game.isStartEndLevelTimer() && game.isEndLevel()){
						game.setisEndLevel(true);
						
					}
				}
				
				if(level == 26){
					//simple enemies
					if(simpleEnemySpawnTimer <= 0){
						if(simpleEnemiesCounter <= 8){
							simpleEnemySpawnTimer = rand.nextFloat() * (2.1f - 1.7f) + 1.7f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
							
						}
						
						//pause
						if(simpleEnemiesCounter == 9){
							isPause = true;
							pause(delta, 6);
						}
						
						if(simpleEnemiesCounter > 8 && simpleEnemiesCounter <= 25 && !isPause){
							simpleEnemySpawnTimer = rand.nextFloat() * (1.1f - 0.7f) + 0.7f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 25 && simpleEnemiesCounter <= 120){
							simpleEnemySpawnTimer = rand.nextFloat() * (1.7f - 1.1f) + 1.1f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
					}
					
					//shooting enemies
					if(shootingEnemySpawnTimer <= 0 && simpleEnemiesCounter > 12){
						if(shootingEnemiesCounter <= 20){
							shootingEnemySpawnTimer = rand.nextFloat() * (1.6f - 1.1f) + 1.1f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 15));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 20 && shootingEnemiesCounter <= 30){
							shootingEnemySpawnTimer = rand.nextFloat() * (0.9f - 0.7f) + 0.7f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 15));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 30 && shootingEnemiesCounter <= 95){
							shootingEnemySpawnTimer = rand.nextFloat() * (3.1f - 1.4f) + 1.4f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 15));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						
					}
					
					//shooting freezing bullets enemies
					if(shootingFreezingBulletsEnemiesSpawnTimer <= 0 && shootingEnemiesCounter > 5){
						if(shootingFreezingBulletsEnemiesCounter <= 12){
							shootingFreezingBulletsEnemiesSpawnTimer = rand.nextFloat() * (12.9f - 6.6f) + 6.6f;
							game.getShootingFreezingBulletsEnemies().add(new ShootingFreezingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setShootingFreezingBulletsEnemies(game.getShootingFreezingBulletsEnemies());
							shootingFreezingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
					}
					
					//shooting bullets enemies
					if(shootingBulletsEnemiesSpawnTimer <= 0 && shootingEnemiesCounter > 10){
						if(shootingBulletsEnemiesCounter <= 20){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (3.8f - 1.6f) + 1.6f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingBulletsEnemiesCounter > 20 && shootingBulletsEnemiesCounter <= 40){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (4.8f - 2.6f) + 2.6f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
					}
					
					//asteroids
					if(asteroidSpawnTimer <= 0 && shootingEnemiesCounter > 20){
						if(asteroidCounter <= 45){
							asteroidSpawnTimer = rand.nextFloat() * (3f - 1.5f) + 1.5f;
							game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
							game.setAsteroids(game.getAsteroids());
							asteroidCounter++; lvlEnemiesCounter++;
						}
						if(asteroidCounter > 45 && asteroidCounter <= 50 && shootingEnemiesCounter > 95){
							asteroidSpawnTimer = rand.nextFloat() * (1f - 0.5f) + 0.5f;
							game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
							game.setAsteroids(game.getAsteroids());
							asteroidCounter++; lvlEnemiesCounter++;
						}
						if(asteroidCounter > 50 && asteroidCounter <= 65){
							asteroidSpawnTimer = rand.nextFloat() * (0.5f - 0.1f) + 0.1f;
							game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
							game.setAsteroids(game.getAsteroids());
							asteroidCounter++; lvlEnemiesCounter++;
						}
						if(asteroidCounter > 60 && asteroidCounter <= 90){
							asteroidSpawnTimer = rand.nextFloat() * (0.1f - 0.05f) + 0.05f;
							game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
							game.setAsteroids(game.getAsteroids());
							asteroidCounter++; lvlEnemiesCounter++;
						}
						if(asteroidCounter > 90 && asteroidCounter <= 110){
							asteroidSpawnTimer = rand.nextFloat() * (0.2f - 0.1f) + 0.1f;
							game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
							game.setAsteroids(game.getAsteroids());
							asteroidCounter++; lvlEnemiesCounter++;
						}
						if(asteroidCounter > 110 && asteroidCounter <= 120){
							asteroidSpawnTimer = rand.nextFloat() * (0.9f - 0.5f) + 0.5f;
							game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
							game.setAsteroids(game.getAsteroids());
							asteroidCounter++; lvlEnemiesCounter++;
						}
						if(asteroidCounter > 120 && asteroidCounter <= 125){
							asteroidSpawnTimer = rand.nextFloat() * (3.9f - 2.8f) + 2.8f;
							game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
							game.setAsteroids(game.getAsteroids());
							asteroidCounter++; lvlEnemiesCounter++;
						}
					}
					
					if(asteroidCounter > 125 && game.getAsteroids().isEmpty() && !game.isStartEndLevelTimer() && !game.isEndLevel()){
						game.setStartEndLevelTimer(true);  isLevelDone = true;
					}
						
					if(asteroidCounter > 125 && game.getAsteroids().isEmpty() && !game.isStartEndLevelTimer() && game.isEndLevel()){
						game.setisEndLevel(true);
						
					}
				}
				
				if(level == 27){
					if(shootingBulletsEnemiesSpawnTimer <= 0){
						if(shootingBulletsEnemiesCounter <= 5){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (3.8f - 1.6f) + 1.6f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingBulletsEnemiesCounter > 5 && shootingBulletsEnemiesCounter <= 15){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (1.8f - 0.6f) + 0.6f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingBulletsEnemiesCounter > 15 && shootingBulletsEnemiesCounter <= 25){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (1.1f - 0.6f) + 0.6f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingBulletsEnemiesCounter > 25 && shootingBulletsEnemiesCounter <= 45){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (0.8f - 0.6f) + 0.6f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingBulletsEnemiesCounter > 45 && shootingBulletsEnemiesCounter <= 50){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (0.3f - 0.2f) + 0.2f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingBulletsEnemiesCounter > 50 && shootingBulletsEnemiesCounter <= 54){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (5.8f - 3.6f) + 3.6f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingBulletsEnemiesCounter > 54 && shootingBulletsEnemiesCounter <= 65){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (1.8f - 0.6f) + 0.6f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingBulletsEnemiesCounter > 65 && shootingBulletsEnemiesCounter <= 85){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (0.9f - 0.6f) + 0.6f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingBulletsEnemiesCounter > 85 && shootingBulletsEnemiesCounter <= 95){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (0.9f - 0.4f) + 0.4f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingBulletsEnemiesCounter > 95 && shootingBulletsEnemiesCounter <= 100){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (0.5f - 0.1f) + 0.1f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingBulletsEnemiesCounter > 100 && shootingBulletsEnemiesCounter <= 105){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (4.3f - 2.1f) + 2.1f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingBulletsEnemiesCounter > 105 && shootingBulletsEnemiesCounter <= 115){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (1.3f - 1.1f) + 1.1f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingBulletsEnemiesCounter > 115 && shootingBulletsEnemiesCounter <= 145){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (0.9f - 0.4f) + 0.4f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingBulletsEnemiesCounter > 145 && shootingBulletsEnemiesCounter <= 155){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (1.9f - 1.1f) + 1.1f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						if(shootingBulletsEnemiesCounter == 156){
							isPause = true;
							pause(delta, 5);
						}
						
						if(shootingBulletsEnemiesCounter > 155 && shootingBulletsEnemiesCounter <= 160 && !isPause){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (1.9f - 1.1f) + 1.1f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingBulletsEnemiesCounter > 160 && shootingBulletsEnemiesCounter <= 195){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (0.8f - 0.7f) + 0.7f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingBulletsEnemiesCounter > 195 && shootingBulletsEnemiesCounter <= 200){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (0.6f - 0.2f) + 0.2f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingBulletsEnemiesCounter > 200 && shootingBulletsEnemiesCounter <= 210){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (1.1f - 0.9f) + 0.9f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingBulletsEnemiesCounter > 210 && shootingBulletsEnemiesCounter <= 215){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (2.1f - 1.9f) + 1.9f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingBulletsEnemiesCounter > 215 && shootingBulletsEnemiesCounter <= 220){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (4.1f - 2.9f) + 2.9f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
					}
					
					if(shootingBulletsEnemiesCounter > 220 && game.getShootingBulletsEnemies().isEmpty() && !game.isStartEndLevelTimer() && !game.isEndLevel()){
						game.setStartEndLevelTimer(true);  isLevelDone = true;
					}
						
					if(shootingBulletsEnemiesCounter > 220 && game.getShootingBulletsEnemies().isEmpty() && !game.isStartEndLevelTimer() && game.isEndLevel()){
						game.setisEndLevel(true);
						
					}
				}
				
				if(level == 28){

					if(simpleEnemySpawnTimer <= 0){
						if(simpleEnemiesCounter <= 3){
							simpleEnemySpawnTimer = rand.nextFloat() * (3.9f - 2.2f) + 2.2f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 3 && simpleEnemiesCounter <= 10){
							simpleEnemySpawnTimer = rand.nextFloat() * (1.6f - 0.7f) + 0.7f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 10 && simpleEnemiesCounter <= 20){
							simpleEnemySpawnTimer = rand.nextFloat() * (2.9f - 1.6f) + 1.6f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 20 && simpleEnemiesCounter <= 30){
							simpleEnemySpawnTimer = rand.nextFloat() * (2.9f - 2.1f) + 2.1f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 30 && simpleEnemiesCounter <= 35){
							simpleEnemySpawnTimer = rand.nextFloat() * (3.4f - 2.3f) + 2.3f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 35 && simpleEnemiesCounter <= 42){
							simpleEnemySpawnTimer = rand.nextFloat() * (4.9f - 3.9f) + 3.9f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						if(simpleEnemiesCounter == 43){
							isPause = true;
							pause(delta, 2);
						}
						
						//spawn simple enemies after the pause (rampage)
						if(simpleEnemiesCounter > 42 && simpleEnemiesCounter <= 65 && !isPause){
							simpleEnemySpawnTimer = rand.nextFloat() * (0.4f - 0.2f) + 0.2f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 65 && simpleEnemiesCounter <= 72){
							simpleEnemySpawnTimer = rand.nextFloat() * (4.6f - 3.2f) + 3.2f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						if(simpleEnemiesCounter > 72 && simpleEnemiesCounter <= 94){
							simpleEnemySpawnTimer = rand.nextFloat() * (2.9f - 1.4f) + 1.4f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						if(simpleEnemiesCounter == 95){
							isPause = true;
							pause(delta, 5);
						}
						
						if(simpleEnemiesCounter > 94 && simpleEnemiesCounter <= 101 && !isPause){
						//spawn a line of simple enemies after 95th
						game.getSimpleEnemies().add(new SimpleEnemy(30, MainGame.HEIGHT, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
						game.getSimpleEnemies().add(new SimpleEnemy(100, MainGame.HEIGHT, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
						game.getSimpleEnemies().add(new SimpleEnemy(170, MainGame.HEIGHT, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
						game.getSimpleEnemies().add(new SimpleEnemy(240, MainGame.HEIGHT, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
						game.getSimpleEnemies().add(new SimpleEnemy(310, MainGame.HEIGHT, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
						game.getSimpleEnemies().add(new SimpleEnemy(380, MainGame.HEIGHT, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
						game.getSimpleEnemies().add(new SimpleEnemy(450, MainGame.HEIGHT, 2));
						game.setSimpleEnemies(game.getSimpleEnemies());
						simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						if(simpleEnemiesCounter > 101 && simpleEnemiesCounter <= 109){
							simpleEnemySpawnTimer = rand.nextFloat() * (3.9f - 2.4f) + 2.4f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 109 && simpleEnemiesCounter <= 130){
							simpleEnemySpawnTimer = rand.nextFloat() * (1.6f - 0.4f) + 0.4f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 130 && simpleEnemiesCounter <= 139){
							simpleEnemySpawnTimer = rand.nextFloat() * (2.9f - 1.4f) + 1.4f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						
						
					}
					
					//spawn shooting enemies after 11th simple enemy
					if(shootingEnemySpawnTimer <= 0 && simpleEnemiesCounter > 10){
						if(shootingEnemiesCounter <= 10){
							shootingEnemySpawnTimer = rand.nextFloat() * (2.6f - 1.6f) + 1.6f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 17));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 10 && shootingEnemiesCounter <= 20){
							shootingEnemySpawnTimer = rand.nextFloat() * (4.3f - 3.6f) + 3.6f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 17));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						//spawn shooting enemies after the pause(rampage)
						if(shootingEnemiesCounter > 20 && shootingEnemiesCounter <= 45 && simpleEnemiesCounter >42 && !isPause) {
							shootingEnemySpawnTimer = rand.nextFloat() * (0.4f - 0.1f) + 0.1f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 17));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						//spawn shooting enemies after the rampage
						if(shootingEnemiesCounter > 45 && shootingEnemiesCounter <= 60 && simpleEnemiesCounter >72) {
							shootingEnemySpawnTimer = rand.nextFloat() * (2.9f - 2.5f) + 2.5f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 17));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
					}
					
					//spawn shooting bullets enemies after 16th simple enemy
					if(shootingBulletsEnemiesSpawnTimer <= 0 && simpleEnemiesCounter > 15){
						if(shootingBulletsEnemiesCounter <= 10){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (2.8f - 2.2f) + 2.2f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						//spawn shooting bullets enemies after the pause(rampage)
						if(shootingBulletsEnemiesCounter > 10 && shootingBulletsEnemiesCounter <= 25 && simpleEnemiesCounter >42  && !isPause){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (0.8f - 0.4f) + 0.4f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						//spawn shooting bullets enemies after the rampage
						if(shootingBulletsEnemiesCounter > 25 && shootingBulletsEnemiesCounter <= 35 && simpleEnemiesCounter >72 ){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (3.8f - 2.6f) + 2.5f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 17));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						
					}
					
					if(shootingFreezingBulletsEnemiesSpawnTimer <= 0){
						if(shootingFreezingBulletsEnemiesCounter <= 10){
							shootingFreezingBulletsEnemiesSpawnTimer = rand.nextFloat() * (17.9f - 9.6f) + 9.6f;
							game.getShootingFreezingBulletsEnemies().add(new ShootingFreezingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 15));
							game.setShootingFreezingBulletsEnemies(game.getShootingFreezingBulletsEnemies());
							shootingFreezingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
					}
					
					//spawn asteroids after 11th simple enemy
					if(asteroidSpawnTimer <= 0 && simpleEnemiesCounter > 10){
						if(asteroidCounter <= 20){
							asteroidSpawnTimer = rand.nextFloat() * (2.9f - 2.1f) + 2.1f;
							game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
							game.setAsteroids(game.getAsteroids());
							asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//spawn asteroids after the pause(rampage)
						if(asteroidCounter > 20 && asteroidCounter <= 40 && simpleEnemiesCounter >42  && !isPause){
							asteroidSpawnTimer = rand.nextFloat() * (0.9f - 0.5f) + 0.5f;
							game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
							game.setAsteroids(game.getAsteroids());
							asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//spawn asteroids after the rampage
						if(asteroidCounter > 40 && asteroidCounter <= 45 && simpleEnemiesCounter >72){
							asteroidSpawnTimer = rand.nextFloat() * (3.9f - 2.5f) + 2.5f;
							game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
							game.setAsteroids(game.getAsteroids());
							asteroidCounter++; lvlEnemiesCounter++;
						}
					}
					
					if(simpleEnemiesCounter > 139 && game.getSimpleEnemies().isEmpty() && !game.isStartEndLevelTimer() && !game.isEndLevel()){
						game.setStartEndLevelTimer(true);  isLevelDone = true;
					}
						
					if(simpleEnemiesCounter > 139 && game.getSimpleEnemies().isEmpty() && !game.isStartEndLevelTimer() && game.isEndLevel()){
						game.setisEndLevel(true);
						
					}
				}
				
				if(level == 29){
					if(shootingFreezingBulletsEnemiesSpawnTimer <= 0){
						if(shootingFreezingBulletsEnemiesCounter <= 5){
							shootingFreezingBulletsEnemiesSpawnTimer = rand.nextFloat() * (3.9f - 2.6f) + 2.6f;
							game.getShootingFreezingBulletsEnemies().add(new ShootingFreezingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 18));
							game.setShootingFreezingBulletsEnemies(game.getShootingFreezingBulletsEnemies());
							shootingFreezingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						if(shootingFreezingBulletsEnemiesCounter > 5 && shootingFreezingBulletsEnemiesCounter <= 75){
							shootingFreezingBulletsEnemiesSpawnTimer = rand.nextFloat() * (1.7f - 1.1f) + 1.1f;
							game.getShootingFreezingBulletsEnemies().add(new ShootingFreezingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 18));
							game.setShootingFreezingBulletsEnemies(game.getShootingFreezingBulletsEnemies());
							shootingFreezingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						if(shootingFreezingBulletsEnemiesCounter == 4){
							isPause = true;
							pause(delta, 5);
						}
					}
					
					if(simpleEnemySpawnTimer <= 0 && shootingFreezingBulletsEnemiesCounter > 6){
						if(simpleEnemiesCounter <= 10){
							simpleEnemySpawnTimer = rand.nextFloat() * (2.6f - 1.4f) + 1.4f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 18));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 10 && simpleEnemiesCounter <= 30){
							simpleEnemySpawnTimer = rand.nextFloat() * (1.3f - 0.4f) + 0.4f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 18));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 30 && simpleEnemiesCounter <= 40){
							simpleEnemySpawnTimer = rand.nextFloat() * (0.6f - 0.2f) + 0.2f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 18));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 40 && simpleEnemiesCounter <= 70){
							simpleEnemySpawnTimer = rand.nextFloat() * (1.3f - 0.6f) + 0.6f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 18));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 70 && simpleEnemiesCounter <= 75){
							simpleEnemySpawnTimer = rand.nextFloat() * (3.3f - 1.6f) + 1.6f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 18));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 75 && simpleEnemiesCounter <= 85){
							simpleEnemySpawnTimer = rand.nextFloat() * (1.3f - 0.6f) + 0.6f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 18));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 85 && simpleEnemiesCounter <= 100){
							simpleEnemySpawnTimer = rand.nextFloat() * (0.8f - 0.1f) + 0.1f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 18));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 100 && simpleEnemiesCounter <= 120){
							simpleEnemySpawnTimer = rand.nextFloat() * (1.3f - 0.6f) + 0.6f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 18));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 120 && simpleEnemiesCounter <= 125){
							simpleEnemySpawnTimer = rand.nextFloat() * (2.3f - 1.6f) + 1.6f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 18));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 125 && simpleEnemiesCounter <= 130){
							simpleEnemySpawnTimer = rand.nextFloat() * (4.3f - 3.6f) + 3.6f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 18));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
					}
					
					if(simpleEnemiesCounter > 130 && game.getSimpleEnemies().isEmpty() && !game.isStartEndLevelTimer() && !game.isEndLevel()){
						game.setStartEndLevelTimer(true);  isLevelDone = true;
					}
						
					if(simpleEnemiesCounter > 130 && game.getSimpleEnemies().isEmpty() && !game.isStartEndLevelTimer() && game.isEndLevel()){
						game.setisEndLevel(true);
						
					}
					
				}
				
				if(level == 30){
					if(shootingFreezingBulletsEnemiesSpawnTimer <= 0){
						if(shootingFreezingBulletsEnemiesCounter <= 6){
							shootingFreezingBulletsEnemiesSpawnTimer = rand.nextFloat() * (4.9f - 3.6f) + 3.6f;
							game.getShootingFreezingBulletsEnemies().add(new ShootingFreezingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 22));
							game.setShootingFreezingBulletsEnemies(game.getShootingFreezingBulletsEnemies());
							shootingFreezingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingFreezingBulletsEnemiesCounter > 6 && shootingFreezingBulletsEnemiesCounter <= 25){
							shootingFreezingBulletsEnemiesSpawnTimer = rand.nextFloat() * (9.9f - 8.6f) + 8.6f;
							game.getShootingFreezingBulletsEnemies().add(new ShootingFreezingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 22));
							game.setShootingFreezingBulletsEnemies(game.getShootingFreezingBulletsEnemies());
							shootingFreezingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingFreezingBulletsEnemiesCounter > 25 && shootingFreezingBulletsEnemiesCounter <= 30){
							shootingFreezingBulletsEnemiesSpawnTimer = rand.nextFloat() * (11.9f - 6.6f) + 6.6f;
							game.getShootingFreezingBulletsEnemies().add(new ShootingFreezingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 22));
							game.setShootingFreezingBulletsEnemies(game.getShootingFreezingBulletsEnemies());
							shootingFreezingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
					}
					
					//spawning shooting bullets enemies after 7th freezing bullet enemy
					if(shootingBulletsEnemiesSpawnTimer <= 0 && shootingFreezingBulletsEnemiesCounter > 6){
						if(shootingBulletsEnemiesCounter <= 6){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (1.8f - 0.6f) + 0.6f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 22));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingBulletsEnemiesCounter > 6 && shootingBulletsEnemiesCounter <= 30){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (4.8f - 3.6f) + 3.6f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 22));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingBulletsEnemiesCounter > 30 && shootingBulletsEnemiesCounter <= 45){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (9.8f - 4.6f) + 4.6f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 22));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						//after 70th simple enemy
						if(shootingBulletsEnemiesCounter > 45 && shootingBulletsEnemiesCounter <= 55 && simpleEnemiesCounter > 73){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (4.8f - 2.6f) + 2.6f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 22));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
					}
					
					//spawn shooting enemies after 6th shooting bullets enemy
					if(shootingEnemySpawnTimer <= 0 && shootingBulletsEnemiesCounter > 6){
						if(shootingEnemiesCounter <= 10){
							shootingEnemySpawnTimer = rand.nextFloat() * (2.6f - 1.6f) + 1.6f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 22));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 10 && shootingEnemiesCounter <= 25){
							shootingEnemySpawnTimer = rand.nextFloat() * (4.6f - 2.6f) + 2.6f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 22));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 25 && shootingEnemiesCounter <= 40){
							shootingEnemySpawnTimer = rand.nextFloat() * (3.6f - 2.1f) + 2.1f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 22));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 40 && shootingEnemiesCounter <= 50){
							shootingEnemySpawnTimer = rand.nextFloat() * (5.6f - 3.1f) + 3.1f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 22));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						//after 70th simple enemy
						if(shootingEnemiesCounter > 50 && shootingEnemiesCounter <= 65 && simpleEnemiesCounter > 70){
							shootingEnemySpawnTimer = rand.nextFloat() * (3.6f - 3.1f) + 3.1f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 22));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
				}
					
					//spawn simple enemies after 5th shooting enemy
					if(simpleEnemySpawnTimer <= 0 && shootingEnemiesCounter >5){
						if(simpleEnemiesCounter <= 15){
							simpleEnemySpawnTimer = rand.nextFloat() * (2.9f - 1.7f) + 1.7f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 22));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 15 && simpleEnemiesCounter <= 50){
							simpleEnemySpawnTimer = rand.nextFloat() * (2.9f - 2.4f) + 2.4f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 22));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 50 && simpleEnemiesCounter <= 65){
							simpleEnemySpawnTimer = rand.nextFloat() * (4.9f - 2.4f) + 2.4f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 22));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 65 && simpleEnemiesCounter <= 70){
							simpleEnemySpawnTimer = rand.nextFloat() * (6.9f - 4.4f) + 4.4f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 22));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 70 && simpleEnemiesCounter <= 100){
							simpleEnemySpawnTimer = rand.nextFloat() * (1.9f - 1.4f) + 1.4f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 22));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						
					}
					
					//spawn asteroids after 35 simple enemy
					if(asteroidSpawnTimer <= 0 && simpleEnemiesCounter > 45){
						if(asteroidCounter <= 15){
							asteroidSpawnTimer = rand.nextFloat() * (3.8f - 2.4f) + 2.4f;
							game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
							game.setAsteroids(game.getAsteroids());
							asteroidCounter++; lvlEnemiesCounter++;
						}
					}
				
				
				if(simpleEnemiesCounter > 100 && game.getSimpleEnemies().isEmpty() && !game.isStartEndLevelTimer() && !game.isEndLevel()){
					game.setStartEndLevelTimer(true);  isLevelDone = true;
				}
					
				if(simpleEnemiesCounter > 100 && game.getSimpleEnemies().isEmpty() && !game.isStartEndLevelTimer() && game.isEndLevel()){
					game.setisEndLevel(true);
					
				}
				
				}
				
				if(level == 31){
					
					if(shootingEnemySpawnTimer <= 0 && shootingEnemiesCounter < 134 && !isPause){
						
							game.getShootingEnemies().add(new ShootingEnemy(0, MainGame.HEIGHT, 9));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
							game.getShootingEnemies().add(new ShootingEnemy(80, MainGame.HEIGHT, 9));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
							game.getShootingEnemies().add(new ShootingEnemy(160, MainGame.HEIGHT, 9));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
							game.getShootingEnemies().add(new ShootingEnemy(240, MainGame.HEIGHT, 9));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
							game.getShootingEnemies().add(new ShootingEnemy(320, MainGame.HEIGHT, 9));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
							game.getShootingEnemies().add(new ShootingEnemy(400, MainGame.HEIGHT, 9));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
							
							if(shootingEnemiesCounter < 12)
							shootingEnemySpawnTimer = 9;
							else if(shootingEnemiesCounter >= 12 && shootingEnemiesCounter < 24)
							shootingEnemySpawnTimer = 6;	
							else if(shootingEnemiesCounter >= 24 &&  shootingEnemiesCounter < 48)
							shootingEnemySpawnTimer = 5.2f;	
							else if(shootingEnemiesCounter >= 48 &&  shootingEnemiesCounter < 96)
							shootingEnemySpawnTimer = 4.6f;	
							else if(shootingEnemiesCounter >= 96 &&  shootingEnemiesCounter < 108)
								shootingEnemySpawnTimer = 4.1f;	
							else if(shootingEnemiesCounter >= 108 &&  shootingEnemiesCounter < 114)
								shootingEnemySpawnTimer = 6;
							else if(shootingEnemiesCounter >= 114 &&  shootingEnemiesCounter < 122)
								shootingEnemySpawnTimer = 8;
							else if(shootingEnemiesCounter >= 122 &&  shootingEnemiesCounter < 134)
								shootingEnemySpawnTimer = 10;
							
					}
					
					if(shootingEnemiesCounter == 6){
						isPause = true;
						pause(delta, 5);
					}
					
					if(shootingEnemiesCounter > 137 && game.getShootingEnemies().isEmpty() && !game.isStartEndLevelTimer() && !game.isEndLevel()){
						game.setStartEndLevelTimer(true);  isLevelDone = true;
					}
						
					if(shootingEnemiesCounter > 137 && game.getShootingEnemies().isEmpty() && !game.isStartEndLevelTimer() && game.isEndLevel()){
						game.setisEndLevel(true);
						
					}
					
				}
				
				if(level == 32){
					if(shootingFreezingBulletsEnemiesSpawnTimer <= 0){
						if(shootingFreezingBulletsEnemiesCounter <= 6){
							shootingFreezingBulletsEnemiesSpawnTimer = rand.nextFloat() * (4.9f - 3.6f) + 3.6f;
							game.getShootingFreezingBulletsEnemies().add(new ShootingFreezingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 14));
							game.setShootingFreezingBulletsEnemies(game.getShootingFreezingBulletsEnemies());
							shootingFreezingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingFreezingBulletsEnemiesCounter > 6 && shootingFreezingBulletsEnemiesCounter <= 25){
							shootingFreezingBulletsEnemiesSpawnTimer = rand.nextFloat() * (9.9f - 8.6f) + 8.6f;
							game.getShootingFreezingBulletsEnemies().add(new ShootingFreezingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 14));
							game.setShootingFreezingBulletsEnemies(game.getShootingFreezingBulletsEnemies());
							shootingFreezingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingFreezingBulletsEnemiesCounter > 25 && shootingFreezingBulletsEnemiesCounter <= 33){
							shootingFreezingBulletsEnemiesSpawnTimer = rand.nextFloat() * (11.9f - 6.6f) + 6.6f;
							game.getShootingFreezingBulletsEnemies().add(new ShootingFreezingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 14));
							game.setShootingFreezingBulletsEnemies(game.getShootingFreezingBulletsEnemies());
							shootingFreezingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
					}
					
					//spawning shooting bullets enemies after 7th freezing bullet enemy
					if(shootingBulletsEnemiesSpawnTimer <= 0 && shootingFreezingBulletsEnemiesCounter > 6){
						if(shootingBulletsEnemiesCounter <= 6){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (1.8f - 0.6f) + 0.6f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingBulletsEnemiesCounter > 6 && shootingBulletsEnemiesCounter <= 30){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (4.8f - 3.6f) + 3.6f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingBulletsEnemiesCounter > 30 && shootingBulletsEnemiesCounter <= 50){
							shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (7.8f - 4.6f) + 4.6f;
							game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
							game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
							shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
						}
					}
					
					//spawn shooting enemies after 6th shooting bullets enemy
					if(shootingEnemySpawnTimer <= 0 && shootingBulletsEnemiesCounter > 6){
						if(shootingEnemiesCounter <= 10){
							shootingEnemySpawnTimer = rand.nextFloat() * (2.6f - 1.6f) + 1.6f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 14));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 10 && shootingEnemiesCounter <= 25){
							shootingEnemySpawnTimer = rand.nextFloat() * (4.6f - 2.6f) + 2.6f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 14));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 25 && shootingEnemiesCounter <= 40){
							shootingEnemySpawnTimer = rand.nextFloat() * (3.6f - 2.1f) + 2.1f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 14));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(shootingEnemiesCounter > 40 && shootingEnemiesCounter <= 60){
							shootingEnemySpawnTimer = rand.nextFloat() * (4.6f - 2.6f) + 2.6f;
							game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 14));
							game.setShootingEnemies(game.getShootingEnemies());
							shootingEnemiesCounter++; lvlEnemiesCounter++;
						}
				}
					
					//spawn simple enemies after 5th shooting enemy
					if(simpleEnemySpawnTimer <= 0 && shootingEnemiesCounter >5){
						if(simpleEnemiesCounter <= 15){
							simpleEnemySpawnTimer = rand.nextFloat() * (2.9f - 1.7f) + 1.7f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 14));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 15 && simpleEnemiesCounter <= 60){
							simpleEnemySpawnTimer = rand.nextFloat() * (2.6f - 2.4f) + 2.4f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 14));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 60 && simpleEnemiesCounter <= 75){
							simpleEnemySpawnTimer = rand.nextFloat() * (4.9f - 2.4f) + 2.4f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 14));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						if(simpleEnemiesCounter > 75 && simpleEnemiesCounter <= 85){
							simpleEnemySpawnTimer = rand.nextFloat() * (6.9f - 4.4f) + 4.4f;
							game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 14));
							game.setSimpleEnemies(game.getSimpleEnemies());
							simpleEnemiesCounter++; lvlEnemiesCounter++;
						}
						
						
					}
					
					//spawn asteroids after 35 simple enemy
					if(asteroidSpawnTimer <= 0 && simpleEnemiesCounter > 55){
						if(asteroidCounter <= 15){
							asteroidSpawnTimer = rand.nextFloat() * (3.6f - 2.4f) + 2.4f;
							game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
							game.setAsteroids(game.getAsteroids());
							asteroidCounter++; lvlEnemiesCounter++;
						}
					}
				
				
				if(simpleEnemiesCounter > 85 && game.getSimpleEnemies().isEmpty() && !game.isStartEndLevelTimer() && !game.isEndLevel()){
					game.setStartEndLevelTimer(true);  isLevelDone = true;
				}
					
				if(simpleEnemiesCounter > 85 && game.getSimpleEnemies().isEmpty() && !game.isStartEndLevelTimer() && game.isEndLevel()){
					game.setisEndLevel(true);
					
				}
				
				}
				
					if(level == 33){
					
					if(asteroidSpawnTimer <= 0){
					
						//Asteroids spawning
						if(asteroidCounter <= 5){
						asteroidSpawnTimer = rand.nextFloat() * (2f - 1.7f) + 1.7f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
							
						}
						//Asteroids spawning
						if(asteroidCounter > 5 && asteroidCounter <= 15){
						asteroidSpawnTimer = rand.nextFloat() * (0.9f - 0.6f) + 0.6f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
							
						}
						//Asteroids spawning
						if(asteroidCounter > 15 && asteroidCounter <= 30){
						asteroidSpawnTimer = rand.nextFloat() * (0.7f - 0.4f) + 0.4f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
							
						}
						//Asteroids spawning
						if(asteroidCounter > 30 && asteroidCounter <= 55){
						asteroidSpawnTimer = rand.nextFloat() * (0.2f - 0.1f) + 0.1f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
							
						}
						//Asteroids spawning
						if(asteroidCounter > 55 && asteroidCounter <= 70){
						asteroidSpawnTimer = rand.nextFloat() * (0.09f - 0.05f) + 0.05f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
							
						}
						//Asteroids spawning
						if(asteroidCounter > 70 && asteroidCounter <= 75){
						asteroidSpawnTimer = rand.nextFloat() * (0.6f - 0.3f) + 0.3f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
							
						}
						//Asteroids spawning
						if(asteroidCounter > 75 && asteroidCounter <= 85){
						asteroidSpawnTimer = rand.nextFloat() * (1.9f - 0.7f) + 0.7f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}

						//Asteroids spawning
						if(asteroidCounter > 85 && asteroidCounter <= 95){
						asteroidSpawnTimer = rand.nextFloat() * (0.9f - 0.5f) + 0.5f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//Asteroids spawning
						if(asteroidCounter > 95 && asteroidCounter <= 115){
						asteroidSpawnTimer = rand.nextFloat() * (0.8f - 0.1f) + 0.1f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//Asteroids spawning
						if(asteroidCounter > 115 && asteroidCounter <= 135){
						asteroidSpawnTimer = rand.nextFloat() * (0.5f - 0.1f) + 0.1f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//Asteroids spawning
						if(asteroidCounter > 135 && asteroidCounter <= 150){
						asteroidSpawnTimer = rand.nextFloat() * (0.08f - 0.06f) + 0.04f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//Asteroids spawning
						if(asteroidCounter > 150 && asteroidCounter <= 160){
						asteroidSpawnTimer = rand.nextFloat() * (0.3f - 0.1f) + 0.1f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//Asteroids spawning
						if(asteroidCounter > 160 && asteroidCounter <= 180){
						asteroidSpawnTimer = rand.nextFloat() * (0.4f - 0.1f) + 0.1f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//Asteroids spawning
						if(asteroidCounter > 180 && asteroidCounter <= 190){
						asteroidSpawnTimer = rand.nextFloat() * (0.8f - 0.3f) + 0.3f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//Asteroids spawning
						if(asteroidCounter > 190 && asteroidCounter <= 195){
						asteroidSpawnTimer = rand.nextFloat() * (3f - 1.6f) + 1.6f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//Asteroids spawning
						if(asteroidCounter > 195 && asteroidCounter <= 200){
						asteroidSpawnTimer = rand.nextFloat() * (2.2f - 1.1f) + 1.1f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//Asteroids spawning
						if(asteroidCounter > 200 && asteroidCounter <= 230){
						asteroidSpawnTimer = rand.nextFloat() * (0.2f - 0.1f) + 0.1f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//Asteroids spawning
						if(asteroidCounter > 230 && asteroidCounter <= 240){
						asteroidSpawnTimer = rand.nextFloat() * (0.09f - 0.04f) + 0.04f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//Asteroids spawning
						if(asteroidCounter > 240 && asteroidCounter <= 245){
						asteroidSpawnTimer = rand.nextFloat() * (5f - 3f) + 3f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//Asteroids spawning
						if(asteroidCounter > 245 && asteroidCounter <= 260){
						asteroidSpawnTimer = rand.nextFloat() * (0.09f - 0.06f) + 0.06f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//Asteroids spawning
						if(asteroidCounter > 260 && asteroidCounter <= 270){
						asteroidSpawnTimer = rand.nextFloat() * (0.1f - 0.1f) + 0.1f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//Asteroids spawning
						if(asteroidCounter > 270 && asteroidCounter <= 280){
						asteroidSpawnTimer = rand.nextFloat() * (0.3f - 0.1f) + 0.1f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//Asteroids spawning
						if(asteroidCounter > 280 && asteroidCounter <= 295){
						asteroidSpawnTimer = rand.nextFloat() * (0.7f - 0.4f) + 0.4f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//Asteroids spawning
						if(asteroidCounter > 295 && asteroidCounter <= 310){
						asteroidSpawnTimer = rand.nextFloat() * (0.2f - 0.1f) + 0.1f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//Asteroids spawning
						if(asteroidCounter > 310 && asteroidCounter <= 320){
						asteroidSpawnTimer = rand.nextFloat() * (0.9f - 0.4f) + 0.4f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//Asteroids spawning
						if(asteroidCounter > 320 && asteroidCounter <= 380){
						asteroidSpawnTimer = rand.nextFloat() * (0.3f - 0.1f) + 0.1f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
						
						//Asteroids spawning
						if(asteroidCounter > 380 && asteroidCounter <= 385){
						asteroidSpawnTimer = rand.nextFloat() * (6f - 3f) + 3f;
						game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
						game.setAsteroids(game.getAsteroids());
						asteroidCounter++; lvlEnemiesCounter++;
						}
					
					}
					
					if(asteroidCounter > 385 && game.getAsteroids().isEmpty() && !game.isStartEndLevelTimer() && !game.isEndLevel()){
						game.setStartEndLevelTimer(true);  isLevelDone = true;
					}
						
					if(asteroidCounter > 385 && game.getAsteroids().isEmpty() && !game.isStartEndLevelTimer() && game.isEndLevel()){
						game.setisEndLevel(true);
						
					}
				}
					
					
					if(level == 34){
						if(shootingEnemySpawnTimer <= 0){
							if(shootingEnemiesCounter <= 5){
								shootingEnemySpawnTimer = rand.nextFloat() * (1.9f - 0.8f) + 0.8f;
								game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(shootingEnemiesCounter > 5 && shootingEnemiesCounter <= 20){
								shootingEnemySpawnTimer = rand.nextFloat() * (0.7f - 0.5f) + 0.5f;
								game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(shootingEnemiesCounter > 20 && shootingEnemiesCounter <= 25){
								shootingEnemySpawnTimer = rand.nextFloat() * (0.4f - 0.3f) + 0.3f;
								game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(shootingEnemiesCounter > 25 && shootingEnemiesCounter <= 31){
								shootingEnemySpawnTimer = rand.nextFloat() * (0.3f - 0.2f) + 0.2f;
								game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(shootingEnemiesCounter > 31 && shootingEnemiesCounter <= 35){
								shootingEnemySpawnTimer = rand.nextFloat() * (3.3f - 2.2f) + 2.2f;
								game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(shootingEnemiesCounter > 35 && shootingEnemiesCounter <= 45){
								shootingEnemySpawnTimer = rand.nextFloat() * (1.3f - 0.7f) + 0.7f;
								game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(shootingEnemiesCounter > 45 && shootingEnemiesCounter <= 50){
								shootingEnemySpawnTimer = rand.nextFloat() * (0.2f - 0.1f) + 0.1f;
								game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(shootingEnemiesCounter > 50 && shootingEnemiesCounter <= 65){
								shootingEnemySpawnTimer = rand.nextFloat() * (1.3f - 0.9f) + 0.9f;
								game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(shootingEnemiesCounter > 65 && shootingEnemiesCounter <= 71){
								shootingEnemySpawnTimer = rand.nextFloat() * (3.3f - 2.9f) + 2.9f;
								game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(shootingEnemiesCounter > 71 && shootingEnemiesCounter <= 75){
								shootingEnemySpawnTimer = rand.nextFloat() * (4.3f - 3.9f) + 3.9f;
								game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(shootingEnemiesCounter > 75 && shootingEnemiesCounter <= 90){
								shootingEnemySpawnTimer = rand.nextFloat() * (1.6f - 0.8f) + 0.8f;
								game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(shootingEnemiesCounter > 90 && shootingEnemiesCounter <= 105){
								shootingEnemySpawnTimer = rand.nextFloat() * (0.8f - 0.4f) + 0.4f;
								game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(shootingEnemiesCounter > 105 && shootingEnemiesCounter <= 110){
								shootingEnemySpawnTimer = rand.nextFloat() * (0.2f - 0.1f) + 0.1f;
								game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(shootingEnemiesCounter > 110 && shootingEnemiesCounter <= 115){
								shootingEnemySpawnTimer = rand.nextFloat() * (3.2f - 2.1f) + 2.1f;
								game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(shootingEnemiesCounter > 115 && shootingEnemiesCounter <= 125){
								shootingEnemySpawnTimer = rand.nextFloat() * (1.7f - 0.9f) + 0.9f;
								game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(shootingEnemiesCounter > 125 && shootingEnemiesCounter <= 130){
								shootingEnemySpawnTimer = rand.nextFloat() * (0.4f - 0.1f) + 0.1f;
								game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(shootingEnemiesCounter > 130 && shootingEnemiesCounter <= 140){
								shootingEnemySpawnTimer = rand.nextFloat() * (2.4f - 1.1f) + 1.1f;
								game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(shootingEnemiesCounter > 140 && shootingEnemiesCounter <= 145){
								shootingEnemySpawnTimer = rand.nextFloat() * (0.7f - 0.4f) + 0.4f;
								game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(shootingEnemiesCounter > 145 && shootingEnemiesCounter <= 170){
								shootingEnemySpawnTimer = rand.nextFloat() * (1.7f - 0.8f) + 0.8f;
								game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(shootingEnemiesCounter > 170 && shootingEnemiesCounter <= 175){
								shootingEnemySpawnTimer = rand.nextFloat() * (0.7f - 0.3f) + 0.3f;
								game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(shootingEnemiesCounter > 175 && shootingEnemiesCounter <= 185){
								shootingEnemySpawnTimer = rand.nextFloat() * (2.7f - 1.6f) + 1.6f;
								game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(shootingEnemiesCounter > 185 && shootingEnemiesCounter <= 190){
								shootingEnemySpawnTimer = rand.nextFloat() * (0.1f - 0.08f) + 0.08f;
								game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(shootingEnemiesCounter > 190 && shootingEnemiesCounter <= 195){
								shootingEnemySpawnTimer = rand.nextFloat() * (1.7f - 1.1f) + 1.1f;
								game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(shootingEnemiesCounter > 195 && shootingEnemiesCounter <= 200){
								shootingEnemySpawnTimer = rand.nextFloat() * (2.7f - 1.1f) + 1.1f;
								game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
							}
							
							if(shootingEnemiesCounter > 200 && shootingEnemiesCounter <= 206){
								game.getShootingEnemies().add(new ShootingEnemy(0, MainGame.HEIGHT, 9));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
								game.getShootingEnemies().add(new ShootingEnemy(80, MainGame.HEIGHT, 9));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
								game.getShootingEnemies().add(new ShootingEnemy(160, MainGame.HEIGHT, 9));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
								game.getShootingEnemies().add(new ShootingEnemy(240, MainGame.HEIGHT, 9));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
								game.getShootingEnemies().add(new ShootingEnemy(320, MainGame.HEIGHT, 9));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
								game.getShootingEnemies().add(new ShootingEnemy(400, MainGame.HEIGHT, 9));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
							}
							
							if(shootingEnemiesCounter > 206 && shootingEnemiesCounter <= 210){
								shootingEnemySpawnTimer = rand.nextFloat() * (4.7f - 3.1f) + 3.1f;
								game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 21));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
							}
							
							
							if(shootingEnemiesCounter > 210 && game.getShootingEnemies().isEmpty() && !game.isStartEndLevelTimer() && !game.isEndLevel()){
								game.setStartEndLevelTimer(true);  isLevelDone = true;
							}
								
							if(shootingEnemiesCounter > 210 && game.getShootingEnemies().isEmpty() && !game.isStartEndLevelTimer() && game.isEndLevel()){
								game.setisEndLevel(true);
							}
							
						}
					}
					
					if(level == 35){
						if(shootingFreezingBulletsEnemiesSpawnTimer <= 0){
							if(shootingFreezingBulletsEnemiesCounter <= 6){
								shootingFreezingBulletsEnemiesSpawnTimer = rand.nextFloat() * (4.9f - 3.6f) + 3.6f;
								game.getShootingFreezingBulletsEnemies().add(new ShootingFreezingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 14));
								game.setShootingFreezingBulletsEnemies(game.getShootingFreezingBulletsEnemies());
								shootingFreezingBulletsEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(shootingFreezingBulletsEnemiesCounter > 6 && shootingFreezingBulletsEnemiesCounter <= 25){
								shootingFreezingBulletsEnemiesSpawnTimer = rand.nextFloat() * (9.9f - 8.6f) + 8.6f;
								game.getShootingFreezingBulletsEnemies().add(new ShootingFreezingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 14));
								game.setShootingFreezingBulletsEnemies(game.getShootingFreezingBulletsEnemies());
								shootingFreezingBulletsEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(shootingFreezingBulletsEnemiesCounter > 25 && shootingFreezingBulletsEnemiesCounter <= 33){
								shootingFreezingBulletsEnemiesSpawnTimer = rand.nextFloat() * (11.9f - 6.6f) + 6.6f;
								game.getShootingFreezingBulletsEnemies().add(new ShootingFreezingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 14));
								game.setShootingFreezingBulletsEnemies(game.getShootingFreezingBulletsEnemies());
								shootingFreezingBulletsEnemiesCounter++; lvlEnemiesCounter++;
							}
						}
						
						//spawning shooting bullets enemies after 7th freezing bullet enemy
						if(shootingBulletsEnemiesSpawnTimer <= 0 && shootingFreezingBulletsEnemiesCounter > 6){
							if(shootingBulletsEnemiesCounter <= 6){
								shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (1.8f - 0.6f) + 0.6f;
								game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
								game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
								shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(shootingBulletsEnemiesCounter > 6 && shootingBulletsEnemiesCounter <= 30){
								shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (4.8f - 3.6f) + 3.6f;
								game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
								game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
								shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(shootingBulletsEnemiesCounter > 30 && shootingBulletsEnemiesCounter <= 50){
								shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (7.8f - 4.6f) + 4.6f;
								game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 2));
								game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
								shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
							}
						}
						
						//spawn shooting enemies after 6th shooting bullets enemy
						if(shootingEnemySpawnTimer <= 0 && shootingBulletsEnemiesCounter > 6){
							if(shootingEnemiesCounter <= 10){
								shootingEnemySpawnTimer = rand.nextFloat() * (2.6f - 1.6f) + 1.6f;
								game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 14));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(shootingEnemiesCounter > 10 && shootingEnemiesCounter <= 25){
								shootingEnemySpawnTimer = rand.nextFloat() * (4.6f - 2.6f) + 2.6f;
								game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 14));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(shootingEnemiesCounter > 25 && shootingEnemiesCounter <= 40){
								shootingEnemySpawnTimer = rand.nextFloat() * (3.6f - 2.1f) + 2.1f;
								game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 14));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(shootingEnemiesCounter > 40 && shootingEnemiesCounter <= 60){
								shootingEnemySpawnTimer = rand.nextFloat() * (4.6f - 2.6f) + 2.6f;
								game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 14));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
							}
					}
						
						//spawn simple enemies after 5th shooting enemy
						if(simpleEnemySpawnTimer <= 0 && shootingEnemiesCounter >5){
							if(simpleEnemiesCounter <= 15){
								simpleEnemySpawnTimer = rand.nextFloat() * (2.9f - 1.7f) + 1.7f;
								game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 14));
								game.setSimpleEnemies(game.getSimpleEnemies());
								simpleEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(simpleEnemiesCounter > 15 && simpleEnemiesCounter <= 60){
								simpleEnemySpawnTimer = rand.nextFloat() * (2.6f - 2.4f) + 2.4f;
								game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 14));
								game.setSimpleEnemies(game.getSimpleEnemies());
								simpleEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(simpleEnemiesCounter > 60 && simpleEnemiesCounter <= 75){
								simpleEnemySpawnTimer = rand.nextFloat() * (4.9f - 2.4f) + 2.4f;
								game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 14));
								game.setSimpleEnemies(game.getSimpleEnemies());
								simpleEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(simpleEnemiesCounter > 75 && simpleEnemiesCounter <= 95){
								simpleEnemySpawnTimer = rand.nextFloat() * (1.9f - 0.4f) + 0.4f;
								game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 14));
								game.setSimpleEnemies(game.getSimpleEnemies());
								simpleEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(simpleEnemiesCounter > 95 && simpleEnemiesCounter <= 100){
								simpleEnemySpawnTimer = rand.nextFloat() * (5.9f - 5.4f) + 5.4f;
								game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 14));
								game.setSimpleEnemies(game.getSimpleEnemies());
								simpleEnemiesCounter++; lvlEnemiesCounter++;
							}
							
							
						}
						
						//spawn asteroids after 35 simple enemy
						if(asteroidSpawnTimer <= 0){
							if(asteroidCounter <= 40){
								asteroidSpawnTimer = rand.nextFloat() * (3.6f - 2.4f) + 2.4f;
								game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
								game.setAsteroids(game.getAsteroids());
								asteroidCounter++; lvlEnemiesCounter++;
							}
						}
					
					
					if(simpleEnemiesCounter > 100 && game.getSimpleEnemies().isEmpty() && !game.isStartEndLevelTimer() && !game.isEndLevel()){
						game.setStartEndLevelTimer(true);  isLevelDone = true;
					}
						
					if(simpleEnemiesCounter > 100 && game.getSimpleEnemies().isEmpty() && !game.isStartEndLevelTimer() && game.isEndLevel()){
						game.setisEndLevel(true);
						
					}
					}
					
	
					if(level == 36){
						if(shootingFreezingBulletsEnemiesSpawnTimer <= 0){
							if(shootingFreezingBulletsEnemiesCounter <= 6){
								shootingFreezingBulletsEnemiesSpawnTimer = rand.nextFloat() * (4.9f - 3.6f) + 3.6f;
								game.getShootingFreezingBulletsEnemies().add(new ShootingFreezingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 22));
								game.setShootingFreezingBulletsEnemies(game.getShootingFreezingBulletsEnemies());
								shootingFreezingBulletsEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(shootingFreezingBulletsEnemiesCounter > 6 && shootingFreezingBulletsEnemiesCounter <= 25){
								shootingFreezingBulletsEnemiesSpawnTimer = rand.nextFloat() * (9.9f - 8.6f) + 8.6f;
								game.getShootingFreezingBulletsEnemies().add(new ShootingFreezingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 22));
								game.setShootingFreezingBulletsEnemies(game.getShootingFreezingBulletsEnemies());
								shootingFreezingBulletsEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(shootingFreezingBulletsEnemiesCounter > 25 && shootingFreezingBulletsEnemiesCounter <= 30){
								shootingFreezingBulletsEnemiesSpawnTimer = rand.nextFloat() * (11.9f - 6.6f) + 6.6f;
								game.getShootingFreezingBulletsEnemies().add(new ShootingFreezingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 22));
								game.setShootingFreezingBulletsEnemies(game.getShootingFreezingBulletsEnemies());
								shootingFreezingBulletsEnemiesCounter++; lvlEnemiesCounter++;
							}
						}
						
						//spawning shooting bullets enemies after 7th freezing bullet enemy
						if(shootingBulletsEnemiesSpawnTimer <= 0 && shootingFreezingBulletsEnemiesCounter > 6){
							if(shootingBulletsEnemiesCounter <= 6){
								shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (1.8f - 0.6f) + 0.6f;
								game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 22));
								game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
								shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(shootingBulletsEnemiesCounter > 6 && shootingBulletsEnemiesCounter <= 30){
								shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (4.8f - 3.6f) + 3.6f;
								game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 22));
								game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
								shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(shootingBulletsEnemiesCounter > 30 && shootingBulletsEnemiesCounter <= 45){
								shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (9.8f - 4.6f) + 4.6f;
								game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 22));
								game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
								shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
							}
							
							//after 70th simple enemy
							if(shootingBulletsEnemiesCounter > 45 && shootingBulletsEnemiesCounter <= 55 && simpleEnemiesCounter > 73){
								shootingBulletsEnemiesSpawnTimer = rand.nextFloat() * (4.8f - 2.6f) + 2.6f;
								game.getShootingBulletsEnemies().add(new ShootingBulletsEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 22));
								game.setShootingBulletsEnemies(game.getShootingBulletsEnemies());
								shootingBulletsEnemiesCounter++; lvlEnemiesCounter++;
							}
						}
						
						//spawn shooting enemies after 6th shooting bullets enemy
						if(shootingEnemySpawnTimer <= 0 && shootingBulletsEnemiesCounter > 6){
							if(shootingEnemiesCounter <= 10){
								shootingEnemySpawnTimer = rand.nextFloat() * (2.6f - 1.6f) + 1.6f;
								game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 22));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(shootingEnemiesCounter > 10 && shootingEnemiesCounter <= 25){
								shootingEnemySpawnTimer = rand.nextFloat() * (4.6f - 2.6f) + 2.6f;
								game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 22));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(shootingEnemiesCounter > 25 && shootingEnemiesCounter <= 40){
								shootingEnemySpawnTimer = rand.nextFloat() * (3.6f - 2.1f) + 2.1f;
								game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 22));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(shootingEnemiesCounter > 40 && shootingEnemiesCounter <= 50){
								shootingEnemySpawnTimer = rand.nextFloat() * (5.6f - 3.1f) + 3.1f;
								game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 22));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
							}
							//after 70th simple enemy
							if(shootingEnemiesCounter > 50 && shootingEnemiesCounter <= 65 && simpleEnemiesCounter > 70){
								shootingEnemySpawnTimer = rand.nextFloat() * (3.6f - 3.1f) + 3.1f;
								game.getShootingEnemies().add(new ShootingEnemy(rand.nextInt(MainGame.WIDTH - 90), MainGame.HEIGHT+20, 22));
								game.setShootingEnemies(game.getShootingEnemies());
								shootingEnemiesCounter++; lvlEnemiesCounter++;
							}
					}
						
						//spawn simple enemies after 5th shooting enemy
						if(simpleEnemySpawnTimer <= 0 && shootingEnemiesCounter >5){
							if(simpleEnemiesCounter <= 15){
								simpleEnemySpawnTimer = rand.nextFloat() * (2.9f - 1.7f) + 1.7f;
								game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 22));
								game.setSimpleEnemies(game.getSimpleEnemies());
								simpleEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(simpleEnemiesCounter > 15 && simpleEnemiesCounter <= 50){
								simpleEnemySpawnTimer = rand.nextFloat() * (2.9f - 2.4f) + 2.4f;
								game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 22));
								game.setSimpleEnemies(game.getSimpleEnemies());
								simpleEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(simpleEnemiesCounter > 50 && simpleEnemiesCounter <= 65){
								simpleEnemySpawnTimer = rand.nextFloat() * (4.9f - 2.4f) + 2.4f;
								game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 22));
								game.setSimpleEnemies(game.getSimpleEnemies());
								simpleEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(simpleEnemiesCounter > 65 && simpleEnemiesCounter <= 70){
								simpleEnemySpawnTimer = rand.nextFloat() * (6.9f - 4.4f) + 4.4f;
								game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 22));
								game.setSimpleEnemies(game.getSimpleEnemies());
								simpleEnemiesCounter++; lvlEnemiesCounter++;
							}
							if(simpleEnemiesCounter > 70 && simpleEnemiesCounter <= 100){
								simpleEnemySpawnTimer = rand.nextFloat() * (1.9f - 1.4f) + 1.4f;
								game.getSimpleEnemies().add(new SimpleEnemy(rand.nextInt(MainGame.WIDTH - 30), MainGame.HEIGHT + 50, 22));
								game.setSimpleEnemies(game.getSimpleEnemies());
								simpleEnemiesCounter++; lvlEnemiesCounter++;
							}
							
							
						}
						
						//spawn asteroids after 35 simple enemy
						if(asteroidSpawnTimer <= 0 && simpleEnemiesCounter > 45){
							if(asteroidCounter <= 15){
								asteroidSpawnTimer = rand.nextFloat() * (3.8f - 2.4f) + 2.4f;
								game.getAsteroids().add(new Asteroid(rand.nextInt(MainGame.WIDTH - 30)));
								game.setAsteroids(game.getAsteroids());
								asteroidCounter++; lvlEnemiesCounter++;
							}
						}
					
					
					if(simpleEnemiesCounter > 100 && game.getSimpleEnemies().isEmpty() && !game.isStartEndLevelTimer() && !game.isEndLevel()){
						game.setStartEndLevelTimer(true);  isLevelDone = true;
					}
						
					if(simpleEnemiesCounter > 100 && game.getSimpleEnemies().isEmpty() && !game.isStartEndLevelTimer() && game.isEndLevel()){
						game.setisEndLevel(true);
						
					}
					
					}
	}

	
	
	public void render(SpriteBatch batch){
		
		color = new Color(0.96f, 0.80f, 0.54f, alpha_colorTitle);
		
		if(!stopTitle){
			
			//titles
			if(level == 1)
			levelTitle = new GlyphLayout(font, "A humble\n     beginning...", color, 0, Align.left, false);
			if(level == 2)
			levelTitle = new GlyphLayout(font, "Not too hard,\n          right?", color, 0, Align.left, false);
			if(level == 3)
			levelTitle = new GlyphLayout(font, "A new kid\n   on the block...", color, 0, Align.left, false);
			if(level == 4)
			levelTitle = new GlyphLayout(font, "Still not too\n  hard, I guess?", color, 0, Align.left, false);
			if(level == 5)
			levelTitle = new GlyphLayout(font, "It's getting\n    longer and\n             harder!", color, 0, Align.left, false);
			if(level == 6)
			levelTitle = new GlyphLayout(font, "   It goes \n     both ways.", color, 0, Align.left, false);
			if(level == 7)
			levelTitle = new GlyphLayout(font, "It's getting \n     crowded\n       over here...", color, 0, Align.left, false);
			if(level == 8)
			levelTitle = new GlyphLayout(font, "  This one is \n           easier...", color, 0, Align.left, false);
			if(level == 9)
			levelTitle = new GlyphLayout(font, "Don't get \n    frustrated\n                yet...", color, 0, Align.left, false);
			if(level == 10)
			levelTitle = new GlyphLayout(font, "   Beams...\n    lots of 'em...", color, 0, Align.left, false);
			if(level == 11)
			levelTitle = new GlyphLayout(font, " Hardest one\n         so far...", color, 0, Align.left, false);
			if(level == 12)
			levelTitle = new GlyphLayout(font, "  Don't shoot\n       so much!", color, 0, Align.left, false);
			if(level == 13)
			levelTitle = new GlyphLayout(font, "You made it!\n  The easy part \n          is over!", color, 0, Align.left, false);
			if(level == 14)
			levelTitle = new GlyphLayout(font, " A new baller\n       in da club...", color, 0, Align.left, false);
			if(level == 15)
			levelTitle = new GlyphLayout(font, "  It's all about\n         a variety!", color, 0, Align.left, false);
			if(level == 16)
			levelTitle = new GlyphLayout(font, "It's getting a bit\n   easy, isn't it?", color, 0, Align.left, false);
			if(level == 17)
			levelTitle = new GlyphLayout(font, "They're over \n\n         9000!!!", color, 0, Align.left, false);
			if(level == 18)
			levelTitle = new GlyphLayout(font, " 1 Life only! \n    Good Luck!", color, 0, Align.left, false);
			if(level == 19)
			levelTitle = new GlyphLayout(font, "Did this game \n got under your\n        skin, yet?", color, 0, Align.left, false);
			if(level == 20)
			levelTitle = new GlyphLayout(font, "\n   8x32 = 256", color, 0, Align.left, false);
			if(level == 21)
			levelTitle = new GlyphLayout(font, " Beams...\n   Even more\n       of 'em...", color, 0, Align.left, false);
			if(level == 22)
			levelTitle = new GlyphLayout(font, "300 asteroids\n     incoming.", color, 0, Align.left, false);
			if(level == 23)
			levelTitle = new GlyphLayout(font, "\n    RAMPAGE!", color, 0, Align.left, false);
			if(level == 24)
			levelTitle = new GlyphLayout(font, "   There is\n     a pattern...", color, 0, Align.left, false);
			if(level == 25)
			levelTitle = new GlyphLayout(font, "     It get's\n        better...", color, 0, Align.left, false);
			if(level == 26)
			levelTitle = new GlyphLayout(font, "   $@!T just\n      got real!", color, 0, Align.left, false);
			if(level == 27)
			levelTitle = new GlyphLayout(font, "Bullets...\n Nothing more\n           to say...", color, 0, Align.left, false);
			if(level == 28)
			levelTitle = new GlyphLayout(font, "It's like it's\n   overclocked!", color, 0, Align.left, false);
			if(level == 29)
			levelTitle = new GlyphLayout(font, "1 life only.\nThis time faster.", color, 0, Align.left, false);
			if(level == 30)
			levelTitle = new GlyphLayout(font, "\n              :]", color, 0, Align.left, false);
			if(level == 31)
			levelTitle = new GlyphLayout(font, "\n  Survive this:", color, 0, Align.left, false);
			if(level == 32)
			levelTitle = new GlyphLayout(font, "\nlonger & harder", color, 0, Align.left, false);
			if(level == 33)
			levelTitle = new GlyphLayout(font, "\nEVEN MORE\n    ASTEROIDS!", color, 0, Align.left, false);
			if(level == 34)
			levelTitle = new GlyphLayout(font, "Beams...\n Never enough!", color, 0, Align.left, false);
			if(level == 35)
			levelTitle = new GlyphLayout(font, "You are getting\ncloser & closer!", color, 0, Align.left, false);
			if(level == 36)
			levelTitle = new GlyphLayout(font, "THE END\n\n      for now....", color, 0, Align.left, false);
				
			
			font.draw(batch, levelTitle, 50, 580);
			
		}
		
		batch.setColor(1, 1, 1, alpha_color);
		
		if(startCountDown){
		
			if(roll == 5)
			batch.draw(five, DEFAULT_X_FOR_THE_COUNTER, DEFAULT_Y_FOR_THE_COUNTER);
		
			if(roll == 4)
			batch.draw(four, DEFAULT_X_FOR_THE_COUNTER, DEFAULT_Y_FOR_THE_COUNTER);
		
			if(roll == 3)
			batch.draw(three, DEFAULT_X_FOR_THE_COUNTER, DEFAULT_Y_FOR_THE_COUNTER);
		
			if(roll == 2)
			batch.draw(two, DEFAULT_X_FOR_THE_COUNTER, DEFAULT_Y_FOR_THE_COUNTER);
		
			if(roll == 1)
			batch.draw(one, DEFAULT_X_FOR_THE_COUNTER, DEFAULT_Y_FOR_THE_COUNTER);
		
		}
		
		
		
		batch.setColor(0, 0, 0, 0.5f);
		if(progressBarCheck){
		batch.draw(blank, 454, 565, 15, 90);
		
		}
		
		//levels progress
		batch.setColor(Color.GREEN);
		
		if(progressBarCheck){
			switch(level){
			case 1: batch.draw(blank, 454, 565, 15, (float) (lvlEnemiesCounter * 1.27)); break;
			case 2: batch.draw(blank, 454, 565, 15, (float) (lvlEnemiesCounter * 1.08)); break;
			case 3: batch.draw(blank, 454, 565, 15, (float) (lvlEnemiesCounter * 1.08)); break;
			case 4: batch.draw(blank, 454, 565, 15, (float) (lvlEnemiesCounter * 0.74f)); break;
			case 5: batch.draw(blank, 454, 565, 15, (float) (lvlEnemiesCounter * 0.74f)); break;
			case 6: batch.draw(blank, 454, 565, 15, (float) (lvlEnemiesCounter * 0.56f)); break;
			case 7: batch.draw(blank, 454, 565, 15, (float) (lvlEnemiesCounter * 0.69f)); break;
			case 8: batch.draw(blank, 454, 565, 15, (float) (lvlEnemiesCounter * 0.55f)); break;
			case 9: batch.draw(blank, 454, 565, 15, (float) (lvlEnemiesCounter * 0.65f)); break;
			case 10: batch.draw(blank, 454, 565, 15, (float) (lvlEnemiesCounter * 0.81f)); break;
			case 11: batch.draw(blank, 454, 565, 15, (float) (lvlEnemiesCounter * 0.36f)); break;
			case 12: batch.draw(blank, 454, 565, 15, (float) (lvlEnemiesCounter * 30f)); break;
			case 13: batch.draw(blank, 454, 565, 15, (float) (lvlEnemiesCounter * 0.66f)); break;
			case 14: batch.draw(blank, 454, 565, 15, (float) (lvlEnemiesCounter * 0.42f)); break;
			case 15: batch.draw(blank, 454, 565, 15, (float) (lvlEnemiesCounter * 0.45f)); break;
			case 16: batch.draw(blank, 454, 565, 15, (float) (lvlEnemiesCounter * 0.41f)); break;
			case 17: batch.draw(blank, 454, 565, 15, (float) (lvlEnemiesCounter * 0.34f)); break;
			case 18: batch.draw(blank, 454, 565, 15, (float) (lvlEnemiesCounter * 0.71f)); break;
			case 19: batch.draw(blank, 454, 565, 15, (float) (lvlEnemiesCounter * 0.55f)); break;
			case 20: batch.draw(blank, 454, 565, 15, (float) (lvlEnemiesCounter * 0.39f)); break;
			case 21: batch.draw(blank, 454, 565, 15, (float) (lvlEnemiesCounter * 0.60f)); break;
			case 22: batch.draw(blank, 454, 565, 15, (float) (lvlEnemiesCounter * 0.3f)); break;
			case 23: batch.draw(blank, 454, 565, 15, (float) (lvlEnemiesCounter * 0.34f)); break;
			case 24: batch.draw(blank, 454, 565, 15, (float) (lvlEnemiesCounter * 90f)); break;
			case 25: batch.draw(blank, 454, 565, 15, (float) (lvlEnemiesCounter * 0.45f)); break;
			case 26: batch.draw(blank, 454, 565, 15, (float) (lvlEnemiesCounter * 0.23f)); break;
			case 27: batch.draw(blank, 454, 565, 15, (float) (lvlEnemiesCounter * 0.41f)); break;
			case 28: batch.draw(blank, 454, 565, 15, (float) (lvlEnemiesCounter * 0.31f)); break;
			case 29: batch.draw(blank, 454, 565, 15, (float) (lvlEnemiesCounter * 0.44f)); break;
			case 30: batch.draw(blank, 454, 565, 15, (float) (lvlEnemiesCounter * 0.33f)); break;
			case 31: batch.draw(blank, 454, 565, 15, (float) (lvlEnemiesCounter * 0.65f)); break;
			case 32: batch.draw(blank, 454, 565, 15, (float) (lvlEnemiesCounter * 0.36f)); break;
			case 33: batch.draw(blank, 454, 565, 15, (float) (lvlEnemiesCounter * 0.23f)); break;
			case 34: batch.draw(blank, 454, 565, 15, (float) (lvlEnemiesCounter * 0.43f)); break;
			case 35: batch.draw(blank, 454, 565, 15, (float) (lvlEnemiesCounter * 0.31f)); break;
			case 36: batch.draw(blank, 454, 565, 15, (float) (lvlEnemiesCounter * 0.33f)); break;
			}
		}
	
		
	
		batch.setColor(Color.WHITE);
		
		if(progressBarCheck)
		batch.draw(progressBar, 438, 542);
		
		if(isLevelDone){
			if(!animStar.isAnimationFinished(statetime)){
				if(sfx){
					if(!playOnce){
						fireworks.play(0.8f);
						playOnce = true;
					}
				}
			batch.draw(animStar.getKeyFrame(statetime), 390, 600);
			batch.draw(animStar.getKeyFrame(statetime), 410, 580);
			batch.draw(animStar.getKeyFrame(statetime), 430, 570);
			batch.draw(animStar.getKeyFrame(statetime), 430, 610);
			batch.draw(animStar.getKeyFrame(statetime), 380, 590);
			}
		}
		
	}
	
	private void pause(float deltaTime, float pauseTime){
		pauseTimePassed += deltaTime;
		
		if(pauseTimePassed >= pauseTime/2){}
			isHalfPausePassed = true;
		
		if(pauseTimePassed >= pauseTime){
			isPause = false;
			isHalfPausePassed = true;
		}
	}
	
	public int getAsteroidCounter() {
		return asteroidCounter;
	}

	public void setAsteroidCounter(int asteroidCounter) {
		this.asteroidCounter = asteroidCounter;
	}

	public int getSimpleEnemiesCounter() {
		return simpleEnemiesCounter;
	}

	public int getShootingEnemiesCounter() {
		return shootingEnemiesCounter;
	}

	public boolean isHalfPausePassed() {
		return isHalfPausePassed;
	}
	
	

	public boolean isSpawnEnemiesWithBoss() {
		return spawnEnemiesWithBoss;
	}

	public void setSpawnEnemiesWithBoss(boolean spawnEnemiesWithBoss) {
		this.spawnEnemiesWithBoss = spawnEnemiesWithBoss;
	}

	public void setHalfPausePassed(boolean isHalfPausePassed) {
		this.isHalfPausePassed = isHalfPausePassed;
	}

	public int getShootingBulletsEnemiesCounter() {
		return shootingBulletsEnemiesCounter;
	}

	public void setShootingBulletsEnemiesCounter(int shootingBulletsEnemiesCounter) {
		this.shootingBulletsEnemiesCounter = shootingBulletsEnemiesCounter;
	}

	
	
	public boolean isFirstBossKilled() {
		return isFirstBossKilled;
	}

	public void setFirstBossKilled(boolean isFirstBossKilled) {
		this.isFirstBossKilled = isFirstBossKilled;
	}
	
	

	public int getShootingFreezingBulletsEnemiesCounter() {
		return shootingFreezingBulletsEnemiesCounter;
	}

	public void dispose(){
		fireworks.dispose();
		counter.dispose();
		one.dispose();
		two.dispose();
		three.dispose();
		four.dispose();
		five.dispose();
		
		System.out.println("Level Generator Disposed.");
	}

	
	
	public boolean isLevelDone() {
		return isLevelDone;
	}

	public void setLevelDone(boolean isLevelDone) {
		this.isLevelDone = isLevelDone;
	}

	public int getRoll() {
		return roll;
	}

	public boolean isProgressBarCheck() {
		return progressBarCheck;
	}

	public void setProgressBarCheck(boolean progressBarCheck) {
		this.progressBarCheck = progressBarCheck;
	}

	public boolean isHalfLevelPassed() {
		return isHalfLevelPassed;
	}

	public void setHalfLevelPassed(boolean isHalfLevelPassed) {
		this.isHalfLevelPassed = isHalfLevelPassed;
	}

	public boolean isSpawnTools() {
		return spawnTools;
	}

	public void setSpawnTools(boolean spawnTools) {
		this.spawnTools = spawnTools;
	}

	public int getCounterForTools() {
		return counterForTools;
	}

	public void setCounterForTools(int counterForTools) {
		this.counterForTools = counterForTools;
	}
	
	
	
	
}
