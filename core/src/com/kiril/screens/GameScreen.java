package com.kiril.screens;


import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Align;
import com.kiril.entities.Asteroid;
import com.kiril.entities.BossEnemy;
import com.kiril.entities.Bullet;
import com.kiril.entities.Collectibles;
import com.kiril.entities.Explosion;
import com.kiril.entities.Shield;
import com.kiril.entities.ShootingBulletsEnemy;
import com.kiril.entities.ShootingEnemy;
import com.kiril.entities.ShootingFreezingBulletsEnemy;
import com.kiril.entities.SimpleEnemy;
import com.kiril.main.game.MainGame;
import com.kiril.tools.AdHandler;
import com.kiril.tools.CollisionRect;
import com.kiril.tools.LevelGenerator;
import com.kiril.tools.PlayerModes;
import com.kiril.tools.ScorePoints;


public class GameScreen implements Screen{
	
	/************************main game variables ***************************/
	
	//constants
	float SPEED = 340; 
	int SHIP_WIDTH = 100;
	int SHIP_HEIGHT = 110;
	float SHIP_ANIMATION_SPEED = 0.1f;
	float SHOOT_WAIT_TIME = 0.3f; 
	
	//player
	float x; 
	float y;
	boolean isPlayerHit;
	boolean isPlayerDead;
	
	//animation variables
	int roll;
	float stateTime;
	
	//background scrolling variables
	float bgY;
	float bgvelY;
	
	//objects
	MainGame game;
	Animation[] rolls;
	LevelGenerator lg;
	Random rand;
	Preferences prefs;
	
	//ads
	AdHandler handler;
	boolean toggle;
	
	
	//sounds
	Sound laserPlayer, coin, explosionPlayerSound, shield, playerMode, hit, tools, scoreCounter, fireworks;
	Sound explosionSound, bulletsUpdate, grats, freeze, confirmButton;
	boolean scoreCounterStart = false;
	boolean fireworks1Start, fireworks2Start, fireworks3Start; 
	boolean gratsPlayed = false;
	
	//music
	float volume;
	Music musicLevel, musicLevelWin;
	
	
	//entities lists
	ArrayList<Bullet> bullets;
	ArrayList<Asteroid> asteroids;
	ArrayList<Explosion> explosions;
	ArrayList<ScorePoints> scorepoints;
	ArrayList<Shield> shields;
	ArrayList<SimpleEnemy> simpleEnemies;
	ArrayList<ShootingEnemy> shootingEnemies;
	ArrayList<PlayerModes> playerModes;
	ArrayList<Collectibles> collectibles;
	ArrayList<ShootingBulletsEnemy> shootingBulletsEnemies;
	ArrayList<BossEnemy> bossEnemies;
	ArrayList<ShootingFreezingBulletsEnemy> shootingFreezingBulletsEnemies;
	
	
	//shooting time variables
	float shootTimer;
	
	//in game variables
	int lives;
	int score;
	int level;
	int coinsTotal = 0;
	int coinsCaught = 0;
	int bonus;
	int lucky = 1;
	int fireBullets = 2;
	boolean isFasterBullets;
	boolean isSlowerPlayer;
	boolean oneExplosion;
	boolean music, sfx, replay, progressBar;
	
	
	//collision rect for the player
	CollisionRect playerRect;
	
	//font text
	BitmapFont fontText;
	FreeTypeFontGenerator generatorText;
	FreeTypeFontParameter parameterText;

	//health
	float health = 1f;
	Texture blank;
	Texture healthbar;
	Texture healthbarBack;
	
	//player modes counter
	int playerModesCounter;
	
	//timers
	//player not taking demage after had been hit
	float playerInvincibleTimer; boolean startPlayerInvincibleTimer;
	float shieldSpawnTimer; boolean startShieldSpawnTimer;
	float toolsSpawnTimer; boolean startToolsSpawnTimer;
	float freezingBulletsSpawnTimer; boolean startFreezingBulletsSpawnTimer; boolean isFreezingBullets;
	float luckCloversSpawnTimer; boolean startluckCloversSpawnTimer;
	float tripleFireBulletsSpawnTimer; boolean startTripleFireBulletsSpawnTimer;
	float quadFireBulletsSpawnTimer; boolean startquadFireBulletsSpawnTimer;
	float fasterPlayerSpawnTimer; boolean startfasterPlayerSpawnTimer;
	float fasterBulletsSpawnTimer; boolean startfasterBulletsSpawnTimer;
	
	//is player dead timer
	float timeAfterDeath; boolean isTimeAfterDeathOver;
	
	//is end level timers
	float endLevelTimer; boolean startEndLevelTimer; boolean endLevel;
	float greetingTime;
	float alpha_colorGreeting;
	boolean isGreeting;
	float GreetingY;
	
	//end level timers
	float tempScore, tempCoinsCaught, tempBonus = 0; 
	boolean star1, star2, star3 = false; int stars = 0;
	boolean isScoreFinished = false;
	
	//random number variables
	private float countDown;
	private int randomNumber;
	
	//in game arrow controls variables
	boolean isRightArrowTouched = false;
	boolean isLeftArrowTouched = false;
	
	
	/*********************************************PAUSE MENU VARIABLES **********************************/
	boolean isPaused = false;
	//not sure if i need that
	boolean isEscTouched = false;
	
	boolean isSoundMute;
	
	//textures
	Texture bg;
	Texture land;
	Texture bgPouse;
	Texture arrow;
	Texture arrowMoveWhite;
	Texture arrowMoveBlue;
	Texture soundOff;
	
	//font buttons
	BitmapFont fontButt;
	FreeTypeFontGenerator generatorButt;
	FreeTypeFontParameter parameterButt;
	
	//layouts
	GlyphLayout resumeLayout;
	GlyphLayout mainmenuLayout;
	GlyphLayout exitLayout;
	
	//buttons color
	Color color;
	
	float resumeX, resumeY;
	float mainmenuX, mainmenuY;
	float exitX, exitY;
	float arrowMoveX, arrowMoveY;
	float HEALTH_BAR_X;
	float HEALTH_BAR_Y;
	
	/*****************************************END LEVEL SCORE MENU VARIABLES****************************************/
	
	boolean isEndLevel = false;
	
	//texture regions for the passing the level greetings
	TextureRegion good, nice, superb, excellent;
	
	//highscore
	int highscore;
	boolean isNewHighscore = false;
	
	//textures
	Texture bgEndLevel;
	Texture scoreStar;
	Texture nextButtonBlack, nextButtonBlue;
	Texture endLevelGreetings;
	Texture newHighscore;
	
	//font buttons
	BitmapFont fontScore, fontCoinsBonus;
	FreeTypeFontGenerator generatorScore, generatorCoinsBonus;
	FreeTypeFontParameter parameterScore, parameterCoinsBonus;
	
	//layouts
	GlyphLayout scoreLayout;
	GlyphLayout coinsLayout;
	GlyphLayout bonusLayout;
	
	//buttons color
	Color scoreColor;
	
	//star splash animation 
	float FRAME_LENGHT = 0.08f;
	int SIZE = 100;
	Animation animStar = new Animation(FRAME_LENGHT, TextureRegion.split(new Texture("fireworks2.png"), SIZE, SIZE)[0]);
	float star1Time, star2Time, star3Time = 0;
	
	
	
	/**********************************************************CONSTRUCTOR*****************************************************/
	public GameScreen (MainGame game, int level, AdHandler handler, boolean toggle){
		this.game = game;
		this.level = level;
		this.toggle = toggle;
		this.handler = handler;
		
		//catch the back button on android
		Gdx.input.setCatchBackKey(true);

		//ads
		if(MainGame.IS_MOBILE)
		handler.showAds(toggle);

		
		//player x and y coordinates
		y = 45;
		x = MainGame.WIDTH / 2 - SHIP_WIDTH  / 2;
		
		//entities lists
		bullets = new ArrayList<Bullet>();
		asteroids = new ArrayList<Asteroid>();
		explosions = new ArrayList<Explosion>();
		scorepoints = new ArrayList<ScorePoints>();
		shields = new ArrayList<Shield>();
		simpleEnemies = new ArrayList<SimpleEnemy>();
		shootingEnemies = new ArrayList<ShootingEnemy>();
		shootingBulletsEnemies = new ArrayList<ShootingBulletsEnemy>();
		bossEnemies = new ArrayList<BossEnemy>();
		shootingFreezingBulletsEnemies = new ArrayList<ShootingFreezingBulletsEnemy>();
		collectibles = new ArrayList<Collectibles>();
		playerModes = new ArrayList<PlayerModes>();
		
		//random
		rand = new Random();
			
		if(level != 18 && level != 29)
		lives = 3;
		if(level == 18 || level == 29)
		lives = 1;
		
		//random bonus lol
		if(level < 3)
		bonus = rand.nextInt((600) - 100) + 100;
		if(level >= 3 && level < 7)
		bonus = rand.nextInt((1000) - 300) + 300;
		if(level >= 7 && level < 12)
		bonus = rand.nextInt((1600) - 600) + 600;
		if(level == 12)
		bonus = rand.nextInt((800) - 200) + 200;
		if(level > 12 && level < 16)
		bonus = rand.nextInt((3000) - 1000) + 1000;
		if(level >= 16 && level < 26)
		bonus = rand.nextInt((3000) - 1500) + 1500;
		if(level >= 26)
		bonus = rand.nextInt((3500) - 1700) + 1700;
		
		//level generator
		lg = new LevelGenerator(this, level);
	
		//shooting timer
		shootTimer = 0;
		
		//player animation
		rolls = new Animation[6];
		roll = 1;

		//loading data from save file
		prefs = Gdx.app.getPreferences("spaceraiders");
		music = prefs.getBoolean("music", true);
		sfx = prefs.getBoolean("sfx", true);
		replay = prefs.getBoolean("replay", true);
		progressBar = prefs.getBoolean("progressBar", true);
		highscore = prefs.getInteger("highscore");
		
		//player texture region and animation
		TextureRegion[][] rollSpriteSheet = TextureRegion.split(new Texture("player2.png"), SHIP_WIDTH, SHIP_HEIGHT);
		rolls[roll] = new Animation(SHIP_ANIMATION_SPEED, rollSpriteSheet[0]);
		
		//background texture
		bg = new Texture("background.png");
		land = new Texture("planetLand.png");
		arrowMoveWhite = new Texture("buttons/arrowMoveWhite.png");
		arrowMoveBlue = new Texture("buttons/arrowMoveBlue.png");

		//volume
		volume = 0.05f;
		
		//sounds
		laserPlayer = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/playerLaser.ogg"));
		coin = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/coin.ogg"));
		explosionPlayerSound = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/explosionPlayer.ogg"));
		shield = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/shield.ogg"));
		playerMode = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/playerMode.ogg"));
		explosionSound = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/explosionSound.ogg"));
		hit = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/hit.ogg"));
		tools = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/tools.ogg"));
		scoreCounter = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/score.ogg"));
		fireworks = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/fireworks.ogg"));
		bulletsUpdate = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/bulletsUpdate.ogg"));
		grats = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/grats.ogg"));
		freeze = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/freeze.ogg"));
		confirmButton = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/confirmButton.ogg"));
	
		//music and scrolling background speed for every level
		switch(level){
		case 1: musicLevel = Gdx.audio.newMusic(Gdx.files.internal("audio/music/01_05_31game.ogg")); bgvelY = 1.9f; break;
		case 2: musicLevel = Gdx.audio.newMusic(Gdx.files.internal("audio/music/02game.ogg")); bgvelY = 2.1f; break;
		case 3: musicLevel = Gdx.audio.newMusic(Gdx.files.internal("audio/music/03_13_21game.ogg")); bgvelY = 2.1f; break;
		case 4: musicLevel = Gdx.audio.newMusic(Gdx.files.internal("audio/music/04_11_29_22game.ogg")); bgvelY = 2.1f; break;
		case 5: musicLevel = Gdx.audio.newMusic(Gdx.files.internal("audio/music/01_05_31game.ogg")); bgvelY = 2.1f; break;
		case 6: musicLevel = Gdx.audio.newMusic(Gdx.files.internal("audio/music/06_15_23_32game.ogg")); bgvelY = 2.1f; break;
		case 7: musicLevel = Gdx.audio.newMusic(Gdx.files.internal("audio/music/07_16_25game.ogg")); bgvelY = 2.4f; break;
		case 8: musicLevel = Gdx.audio.newMusic(Gdx.files.internal("audio/music/08_18_27_34game.ogg")); bgvelY = 2.9f; break;
		case 9: musicLevel = Gdx.audio.newMusic(Gdx.files.internal("audio/music/09_19_26_35game.ogg")); bgvelY = 2.2f; break;
		case 10: musicLevel = Gdx.audio.newMusic(Gdx.files.internal("audio/music/10_17_30game.ogg")); bgvelY = 1.4f; break;
		case 11: musicLevel = Gdx.audio.newMusic(Gdx.files.internal("audio/music/04_11_29_22game.ogg")); bgvelY = 2.3f; break;
		case 12: musicLevel = Gdx.audio.newMusic(Gdx.files.internal("audio/music/12boss_28_36.ogg")); bgvelY = 3.2f; break;
		case 13: musicLevel = Gdx.audio.newMusic(Gdx.files.internal("audio/music/03_13_21game.ogg")); bgvelY = 3.2f; break;
		case 14: musicLevel = Gdx.audio.newMusic(Gdx.files.internal("audio/music/14_20_24_33game.ogg")); bgvelY = 2.8f; break;
		case 15: musicLevel = Gdx.audio.newMusic(Gdx.files.internal("audio/music/06_15_23_32game.ogg")); bgvelY = 2.8f; break;
		case 16: musicLevel = Gdx.audio.newMusic(Gdx.files.internal("audio/music/07_16_25game.ogg")); bgvelY = 3.1f; break;
		case 17: musicLevel = Gdx.audio.newMusic(Gdx.files.internal("audio/music/10_17_30game.ogg")); bgvelY = 2.1f; break;
		case 18: musicLevel = Gdx.audio.newMusic(Gdx.files.internal("audio/music/08_18_27_34game.ogg")); bgvelY = 3.2f; break;
		case 19: musicLevel = Gdx.audio.newMusic(Gdx.files.internal("audio/music/09_19_26_35game.ogg")); bgvelY = 2.2f; break;
		case 20: musicLevel = Gdx.audio.newMusic(Gdx.files.internal("audio/music/14_20_24_33game.ogg")); bgvelY = 1.9f; break;
		case 21: musicLevel = Gdx.audio.newMusic(Gdx.files.internal("audio/music/03_13_21game.ogg")); bgvelY = 2.4f; break;
		case 22: musicLevel = Gdx.audio.newMusic(Gdx.files.internal("audio/music/14_20_24_33game.ogg")); bgvelY = 2.6f; break;
		case 23: musicLevel = Gdx.audio.newMusic(Gdx.files.internal("audio/music/06_15_23_32game.ogg")); bgvelY = 2.6f; break;
		case 24: musicLevel = Gdx.audio.newMusic(Gdx.files.internal("audio/music/12boss_28_36.ogg")); bgvelY = 4.3f; break;
		case 25: musicLevel = Gdx.audio.newMusic(Gdx.files.internal("audio/music/07_16_25game.ogg")); bgvelY = 3.2f; break;
		case 26: musicLevel = Gdx.audio.newMusic(Gdx.files.internal("audio/music/09_19_26_35game.ogg")); bgvelY = 3.3f; break;
		case 27: musicLevel = Gdx.audio.newMusic(Gdx.files.internal("audio/music/08_18_27_34game.ogg")); bgvelY = 3.4f; break;
		case 28: musicLevel = Gdx.audio.newMusic(Gdx.files.internal("audio/music/12boss_28_36.ogg")); bgvelY = 3.9f; break;
		case 29: musicLevel = Gdx.audio.newMusic(Gdx.files.internal("audio/music/04_11_29_22game.ogg")); bgvelY = 3.5f; break;
		case 30: musicLevel = Gdx.audio.newMusic(Gdx.files.internal("audio/music/10_17_30game.ogg")); bgvelY = 1.9f; break;
		case 31: musicLevel = Gdx.audio.newMusic(Gdx.files.internal("audio/music/01_05_31game.ogg")); bgvelY = 2.4f; break;
		case 32: musicLevel = Gdx.audio.newMusic(Gdx.files.internal("audio/music/06_15_23_32game.ogg")); bgvelY = 2.4f; break;
		case 33: musicLevel = Gdx.audio.newMusic(Gdx.files.internal("audio/music/14_20_24_33game.ogg")); bgvelY = 2.4f; break;
		case 34: musicLevel = Gdx.audio.newMusic(Gdx.files.internal("audio/music/08_18_27_34game.ogg")); bgvelY = 3.9f; break;
		case 35: musicLevel = Gdx.audio.newMusic(Gdx.files.internal("audio/music/09_19_26_35game.ogg")); bgvelY = 4.3f; break;
		case 36: musicLevel = Gdx.audio.newMusic(Gdx.files.internal("audio/music/04_11_29_22game.ogg")); bgvelY = 2.9f; break;
		}
		
		//set volume nad looping
		musicLevel.setVolume(volume);
		musicLevel.setLooping(true);
	
		//music for the level win
		musicLevelWin = Gdx.audio.newMusic(Gdx.files.internal("audio/music/levelwin.ogg"));
		musicLevelWin.setVolume(0.9f);
		
		//play the music
		if(music)
			musicLevel.play();
	
		//player collision rect
		playerRect = new CollisionRect(x, y, SHIP_WIDTH - 5, SHIP_HEIGHT - 15);
		
		
		//initialize fonts
		//text font
		generatorText = new FreeTypeFontGenerator(Gdx.files.internal("fonts/vanilla.ttf"));
		parameterText = new FreeTypeFontParameter();
		parameterText.size = 16;
		fontText = generatorText.generateFont(parameterText);
		
		//buttons font 
		generatorButt = new FreeTypeFontGenerator(Gdx.files.internal("fonts/venus.ttf"));
		parameterButt = new FreeTypeFontParameter();
		parameterButt.size = 28;
		fontButt = generatorButt.generateFont(parameterButt);
		
		color = new Color(1, 1, 1, 0.9f);
		
		//layouts
		resumeLayout = new GlyphLayout(fontButt, "Resume", color, 0, Align.left, false);
		mainmenuLayout = new GlyphLayout(fontButt, "Main Menu", color, 0, Align.left, false);
		exitLayout = new GlyphLayout(fontButt, "Exit Game", color, 0, Align.left, false);
		
		resumeX = MainGame.WIDTH / 1.55f - resumeLayout.width;
		resumeY = MainGame.HEIGHT / 1.9f + resumeLayout.height;
		mainmenuX = resumeX;
		mainmenuY = resumeY - 70;
		exitX = mainmenuX;
		exitY = mainmenuY - 70;
		
		arrowMoveX = MainGame.WIDTH - 100;
		arrowMoveY = MainGame.HEIGHT / 3.5f;
		
		
		//arow for the buttons
		arrow = new Texture("buttons/arrow.png");
		
		//score
		score = 0;
		
		//health
		blank = new Texture("blank.png");
		healthbar = new Texture("healthbar.png");
		healthbarBack = new Texture("healthbarBack.png");
		HEALTH_BAR_X = 5;
		HEALTH_BAR_Y = MainGame.HEIGHT - 66;
		
		//posed background
		bgPouse = new Texture("glass.png");
		
		//is sound muted
		if(music || sfx)
			isSoundMute = false;
		else
			isSoundMute = true;
			
		
		//sound on and off
		soundOff = new Texture("soundOFF.png");
		
			
		//textures
		endLevelGreetings = new Texture("endLevelGreetings350x57.png");
		good = new TextureRegion(endLevelGreetings, 0, 0, 350, 57);
		nice = new TextureRegion(endLevelGreetings, 350, 0, 350, 57);
		superb = new TextureRegion(endLevelGreetings, 350*2, 0, 350, 57);
		excellent = new TextureRegion(endLevelGreetings, 350*3, 0, 380, 57);
		
		bgEndLevel = new Texture("endLevelScreen2.png");
		scoreStar = new Texture("scoreStar.png");
		nextButtonBlack = new Texture("buttons/nextButtonBlack.png");
		nextButtonBlue = new Texture("buttons/nextButtonBlue.png");
		newHighscore = new Texture("new.png");
		//font for the score
		generatorScore = new FreeTypeFontGenerator(Gdx.files.internal("fonts/venus.ttf"));
		parameterScore = new FreeTypeFontParameter();
		parameterScore.size = 45;
		fontScore = generatorScore.generateFont(parameterScore);
		
		generatorCoinsBonus = new FreeTypeFontGenerator(Gdx.files.internal("fonts/venus.ttf"));
		parameterCoinsBonus = new FreeTypeFontParameter();
		parameterCoinsBonus.size = 24;
		fontCoinsBonus = generatorCoinsBonus.generateFont(parameterCoinsBonus);
		
		//timers
		//player not taking demage after had been hit
		playerInvincibleTimer = 6; startPlayerInvincibleTimer = false;
		shieldSpawnTimer = 20; startShieldSpawnTimer = false;
		toolsSpawnTimer = 10; startToolsSpawnTimer = false;
		freezingBulletsSpawnTimer = 30; startFreezingBulletsSpawnTimer = false; isFreezingBullets = false;
		luckCloversSpawnTimer = 30; startluckCloversSpawnTimer = false;
		tripleFireBulletsSpawnTimer = 40; startTripleFireBulletsSpawnTimer = false;
		quadFireBulletsSpawnTimer = 40; startquadFireBulletsSpawnTimer = false;
		fasterPlayerSpawnTimer = 20; startfasterPlayerSpawnTimer = false;
		fasterBulletsSpawnTimer = 25; startfasterBulletsSpawnTimer = false;
		
		//is player dead timer
		timeAfterDeath = 3; isTimeAfterDeathOver = false;
		
		//is end level timers
		if(level != 12 && level != 24)
		endLevelTimer = 6; 
		if(level == 12 || level == 24)
		endLevelTimer = 14; 
		
		
		startEndLevelTimer = false; endLevel = false;
		greetingTime = 2; alpha_colorGreeting = 1; isGreeting = false; GreetingY = 440;
		
	}
	
	@Override
	public void show() {
		
	}
	/***********************************************UPDATE METHOD***************************************************************/
	public void update(float delta){
	
		//if the game is not paused
		if(!isPaused){
		
		//scrolling the background
		backgroundAnimations();	
		
		timers(delta);
		
		//if the level is ended update only the timers and the background
		if(!isEndLevel){
			
		
		//checks if the player pressed any keys
		keyListener(delta);
			
		//update the music - can make it batter 
		if(lg.getRoll() >= 4 && lg.getRoll() < 5)
			volume = 0.2f;
		if(lg.getRoll() >= 3.5 && lg.getRoll() < 4)
			volume = 0.3f;
		if(lg.getRoll() >= 3 && lg.getRoll() < 3.5)
			volume = 0.4f;
		if(lg.getRoll() >= 2.5 && lg.getRoll() < 3)
			volume = 0.5f;
		if(lg.getRoll() >= 2 && lg.getRoll() < 2.5)
			volume = 0.6f;
		if(lg.getRoll() >= 1.5 && lg.getRoll() < 2)
			volume = 0.8f;
		if(lg.getRoll() >= 1 && lg.getRoll() < 1.5)
			volume = 0.9f;
		
		musicLevel.setVolume(volume);
		
		
		//generating random number every 3 milliseconds
		randNumGenerator();
		
		//updating and spawning the entities
		lg.update(delta);
		
		//if the player is dead
		if(lives <= 0){
			isPlayerDead = true;
			if(isTimeAfterDeathOver){
			game.setScreen(new GameOverScreen(game, score, level, handler, true));
			this.dispose();
			}
		}
		
		//if the score is lower than 0
		if(score <= 0)
			score = 0;
		
		//update bullets
		ArrayList<Bullet> bulletsToRemove = new ArrayList<Bullet>();
		for(Bullet bullet : bullets){
			bullet.update(delta);
			if(bullet.remove){
				bulletsToRemove.add(bullet);
			}
			
		}
	
		//update the asteroids
		ArrayList<Asteroid> asteroidsToRemove = new ArrayList<Asteroid>();
		for(Asteroid asteroid : asteroids){
			asteroid.update(delta);
			if(asteroid.remove){
				asteroidsToRemove.add(asteroid);
			}
		}
		
		//update the simpleEnemy (Enemy 1)
		ArrayList<SimpleEnemy> simpleEnemiesToRemove = new ArrayList<SimpleEnemy>();
		for(SimpleEnemy simpleEnemy : simpleEnemies){
			simpleEnemy.update(delta);
			if(simpleEnemy.remove){
				simpleEnemiesToRemove.add(simpleEnemy);
			}
		}		

		//update the shootingEnemy (Enemy 2)
		ArrayList<ShootingEnemy> shootingEnemiesToRemove = new ArrayList<ShootingEnemy>();
		for(ShootingEnemy shootingEnemy : shootingEnemies){
			shootingEnemy.update(delta);
			if(shootingEnemy.remove){
				shootingEnemiesToRemove.add(shootingEnemy);
			}
		}	
		
		//update the shootingBulletsEnemy (Enemy 3)
		ArrayList<ShootingBulletsEnemy> shootingBulletsEnemiesToRemove = new ArrayList<ShootingBulletsEnemy>();
		for(ShootingBulletsEnemy shootingBulletEnemy : shootingBulletsEnemies){
			shootingBulletEnemy.update(delta);
			if(shootingBulletEnemy.remove){
				shootingBulletsEnemiesToRemove.add(shootingBulletEnemy);
			}
			
			//update the bullets
			if(shootingBulletEnemy.shoot){
				bullets.add(new Bullet(shootingBulletEnemy.getX(), "enemyBullet", shootingBulletEnemy.getY()+20, false));
				bullets.add(new Bullet(shootingBulletEnemy.getX()+55, "enemyBullet", shootingBulletEnemy.getY()+20, false));
				shootingBulletEnemy.shoot = false;
			}
		}	
			
		
		//update the shootingFreezingBullets enemy (Enemy 5)
		ArrayList<ShootingFreezingBulletsEnemy> shootingFreezingBulletsEnemiesToRemove = new ArrayList<ShootingFreezingBulletsEnemy>();
		for(ShootingFreezingBulletsEnemy shootingFreezingBulletEnemy : shootingFreezingBulletsEnemies){
			shootingFreezingBulletEnemy.update(delta);
			if(shootingFreezingBulletEnemy.remove){
				shootingFreezingBulletsEnemiesToRemove.add(shootingFreezingBulletEnemy);
			}
			
			//update the bullets
			if(shootingFreezingBulletEnemy.shoot){
				bullets.add(new Bullet(shootingFreezingBulletEnemy.getX() + 30, "enemyFreezingBullet", shootingFreezingBulletEnemy.getY()+20, false));
				shootingFreezingBulletEnemy.shoot = false;
			}
		}
		
		
		//update the boss enemy (Enemy 5)
		ArrayList<BossEnemy> bossEnemiesToRemove = new ArrayList<BossEnemy>();
		for(BossEnemy bossEnemy : bossEnemies){
			bossEnemy.update(delta);
			if(bossEnemy.remove){
				bossEnemiesToRemove.add(bossEnemy);
			}
			
			//if the boss is boss 2
			
			if(bossEnemy.getBossType() == "boss2"){
			//update the bullets
			if(bossEnemy.isShoot()){
				if(bossEnemy.getShootingStage() == 1){
					bullets.add(new Bullet(bossEnemy.getX()+10, "bossBullet", bossEnemy.getY()+25, isFasterBullets));
					bullets.add(new Bullet(bossEnemy.getX()+90, "bossBullet", bossEnemy.getY()+25, isFasterBullets));
				}
				if(bossEnemy.getShootingStage() == 2){
					bullets.add(new Bullet(bossEnemy.getX()+50, "bossBullet", bossEnemy.getY(), isFasterBullets));
					bullets.add(new Bullet(bossEnemy.getX()+100, "bossBullet", bossEnemy.getY()+25, isFasterBullets));
					bullets.add(new Bullet(bossEnemy.getX(), "bossBullet", bossEnemy.getY()+25, isFasterBullets));
				}
				if(bossEnemy.getShootingStage() == 3){
					bullets.add(new Bullet(bossEnemy.getX()+100, "bossBulletRight", bossEnemy.getY()+25, isFasterBullets));
					bullets.add(new Bullet(bossEnemy.getX(), "bossBulletLeft", bossEnemy.getY()+25, isFasterBullets));
					bullets.add(new Bullet(bossEnemy.getX()+50, "bossBullet", bossEnemy.getY()+25, isFasterBullets));
				}
				if(bossEnemy.getShootingStage() == 4){
					bullets.add(new Bullet(bossEnemy.getX(), "bossBullet", bossEnemy.getY()+25, isFasterBullets));
					bullets.add(new Bullet(bossEnemy.getX()+100, "bossBullet", bossEnemy.getY()+25, isFasterBullets));
				}
				bossEnemy.setShoot(false);
			}
			}
			if(bossEnemy.getBossType() == "boss1"){
				//update the bulles
				if(bossEnemy.shootBullet1){
					//right
					bullets.add(new Bullet(bossEnemy.getX()+75, "enemySmartBulletRight1", bossEnemy.getY() + 20, isFasterBullets));
					bullets.add(new Bullet(bossEnemy.getX()+100, "enemySmartBulletRight2", bossEnemy.getY() + 20, isFasterBullets));
					//mid
					bullets.add(new Bullet(bossEnemy.getX()+50, "enemySmartBullet", bossEnemy.getY()+25, isFasterBullets));
					//left
					bullets.add(new Bullet(bossEnemy.getX()+25, "enemySmartBulletLeft1", bossEnemy.getY() + 20, isFasterBullets));
					bullets.add(new Bullet(bossEnemy.getX(), "enemySmartBulletLeft2", bossEnemy.getY() + 20, isFasterBullets));
					bossEnemy.shootBullet1 = false;
				}
			}
		}
		
		
		//update explosions
		ArrayList<Explosion> explosionsToRemove = new ArrayList<Explosion>();
		for(Explosion explosion : explosions){
			explosion.update(delta);
			
			if(explosion.remove){
				explosionsToRemove.add(explosion);
				if(explosion.getType() == "playerExplosion"){
					if(level != 12)
						shields.add(new Shield(x - 50, y, this, 6f));
					if(level == 12)
						shields.add(new Shield(x - 50, y, this, 4f));
						playerModes.add(new PlayerModes("shieldMode", this));
						
						isPlayerHit = false;
						playerModesCounter++;
						if(sfx)
						shield.play(1f);
				}
			}
		}
		
		//update player modes
		ArrayList<PlayerModes> playerModesToRemove = new ArrayList<PlayerModes>();
		int indexCurrent;
		for(PlayerModes playerMode : playerModes){
			playerMode.update(delta);
			if(playerMode.remove){
				playerModesToRemove.add(playerMode);
				indexCurrent = playerModes.indexOf(playerMode);
				
					for(int i = indexCurrent; i < playerModes.size(); i++){
						playerModes.get(i).setDEFAULT_Y(playerModes.get(i).getDEFAULT_Y() + 45);
					}
			}
			
		}

		//update the collectibles
		ArrayList<Collectibles> collectiblesToRemove = new ArrayList<Collectibles>();
		for(Collectibles collectible : collectibles){
			collectible.update(delta);
			if(collectible.remove)
				collectiblesToRemove.add(collectible);
		}
		
		
		//update the scorepoints
		ArrayList<ScorePoints> pointsToRemove = new ArrayList<ScorePoints>();
		for(ScorePoints sp : scorepoints){
			sp.update(delta);
			if(sp.remove)
				pointsToRemove.add(sp);
		}
		
			
		//update shield
		ArrayList<Shield> shieldsToRemove = new ArrayList<Shield>();
		for(Shield shield : shields){
			shield.update(delta);
			if(shield.remove){
				shieldsToRemove.add(shield);
				
			}
		}
		
		
		//check if the playerModes are gone
		if(playerModes.isEmpty())
		playerModesCounter = 0;
		
		//after player moves update collision rect
		playerRect.move(x, y);
		
		
		//after all updates check for collision between bullets and asteroids
		for(Bullet bullet : bullets){
			for(Asteroid asteroid : asteroids){
				//if the bullet is from shooting freezing bullets enemy
				if(bullet.getCollisionRect().collidesWith(asteroid.getCollisionRect()) && bullet.getType() == "enemyFreezingBullet"){
					asteroidsToRemove.add(asteroid);
					explosions.add(new Explosion(asteroid.getX(), asteroid.getY(), "basicExplosion"));
					bulletsToRemove.add(bullet);
				}
				
				if(bullet.getCollisionRect().collidesWith(asteroid.getCollisionRect()) && bullet.getType() != "enemyFreezingBullet"){
					//collision accured
					if(sfx)
						explosionSound.play(0.3f);
					bulletsToRemove.add(bullet);
					score+=200;
					scorepoints.add(new ScorePoints("plus200"));
					
					asteroidsToRemove.add(asteroid);
					explosions.add(new Explosion(asteroid.getX(), asteroid.getY(), "basicExplosion"));
					
					
					//randomly generated coin
					if(randomNumber <= lucky*5 ){
						collectibles.add(new Collectibles(asteroid.getX(), asteroid.getY(), "coin"));
						coinsTotal++;
					}
					
					//randomly generated tools after stage n
					if(level > 1){
						if((randomNumber == 11 || randomNumber == 10) && !startToolsSpawnTimer && health <= 0.94){
						startToolsSpawnTimer = true;
						collectibles.add(new Collectibles(asteroid.getX(), asteroid.getY(), "tools"));
						}
					}
					
					//randomly generated collectible shield after stage n
					if(level != 1 && level != 11 && level != 17  && level != 23 && level != 28){
							if(randomNumber == 12 && !startShieldSpawnTimer){
							startShieldSpawnTimer = true;
							collectibles.add(new Collectibles(asteroid.getX(), asteroid.getY(), "collectibleShield"));
							}
					}
					
					
					//randomly generated collectible luck clover after stage n
						if(randomNumber == 14 && !startluckCloversSpawnTimer){
						startluckCloversSpawnTimer = true;
						collectibles.add(new Collectibles(asteroid.getX(), asteroid.getY(), "luckClover"));
						}
					
					
					//randomly generated collectible freezing bullet after stage n
						if(level > 1 && level != 5 && level != 6 && level != 9 && level != 11 && level != 16 && level != 17 && level != 23 && level != 26 && level != 28)
							if(randomNumber == 13 && !startFreezingBulletsSpawnTimer){
							startFreezingBulletsSpawnTimer = true;
							collectibles.add(new Collectibles(asteroid.getX(), asteroid.getY(), "freezingBullets"));
							}
						if(level == 5 && lg.getShootingEnemiesCounter() < 12)
							if(randomNumber == 13 && !startFreezingBulletsSpawnTimer){
							startFreezingBulletsSpawnTimer = true;
							collectibles.add(new Collectibles(asteroid.getX(), asteroid.getY(), "freezingBullets"));
							}
					
					//randomly generated collectible triple bullet after stage n
					if(level > 1 && level != 11 && level != 17 && level != 23 && level != 26 && level != 28){
						if(randomNumber == 15 && !startTripleFireBulletsSpawnTimer){
						startTripleFireBulletsSpawnTimer = true;
						collectibles.add(new Collectibles(asteroid.getX(), asteroid.getY(), "tripleFireBullets"));
						}
					}
					
					//randomly generated collectible quad bullet after stage n
					if(level > 2 && level != 5 && level != 11 && level != 17 && level != 20 && level != 23 && level != 26 && level != 28){
						if(randomNumber == 16 && !startquadFireBulletsSpawnTimer){
						startquadFireBulletsSpawnTimer = true;
						collectibles.add(new Collectibles(asteroid.getX(), asteroid.getY(), "quadFireBullets"));
						}
					}
					
					//randomly generated collectible fasterPlayer after stage n
						if(randomNumber >= 17 && !startfasterPlayerSpawnTimer){
						startfasterPlayerSpawnTimer = true;
						collectibles.add(new Collectibles(asteroid.getX(), asteroid.getY(), "fasterPlayer"));
						}
				}
			}
		}
		
		//check for collision between bullets and simple enemies (Enemy 1)
		for(Bullet bullet : bullets){
			for(SimpleEnemy simpleEnemy : simpleEnemies){
				//if the bullet is from a freezing enemy
				if(bullet.getCollisionRect().collidesWith(simpleEnemy.getCollisionRect()) && bullet.getType() == "enemyFreezingBullet"){
					simpleEnemy.setFreezed(true);
					bulletsToRemove.add(bullet);
				}
				//if the bullet is not an enemy bullet
				if(bullet.getCollisionRect().collidesWith(simpleEnemy.getCollisionRect()) && bullet.getType() != "enemyFreezingBullet"){
					if(simpleEnemy.getHealth() >= 2){
						bulletsToRemove.add(bullet);
						if(sfx)
						hit.play(0.5f);
						simpleEnemy.setHit(true); 
						simpleEnemy.setHealth(simpleEnemy.getHealth()-1);
						if(isFreezingBullets){
							simpleEnemy.setFreezed(true);
						}
						explosions.add(new Explosion(bullet.getX()-5, bullet.getY() + 30, "hitExplosion"));
					}else{
					//collision accured
					if(sfx)
					explosionSound.play(0.3f);
						
					bulletsToRemove.add(bullet);
					
					scorepoints.add(new ScorePoints("plus500"));
					
					simpleEnemiesToRemove.add(simpleEnemy);
					explosions.add(new Explosion(simpleEnemy.getX(), simpleEnemy.getY(),  "basicExplosion"));
					score+=500;
					
					//randomly generated coin
					if(randomNumber <= lucky*5 ){
						//coins.add(new Coin(simpleEnemy.getX(), simpleEnemy.getY()));
						collectibles.add(new Collectibles(simpleEnemy.getX(), simpleEnemy.getY(), "coin"));
						coinsTotal++;
					}
					
					//randomly generated tools after stage n
						if((randomNumber == 11 || randomNumber == 10) && !startToolsSpawnTimer && health <= 0.94 && level != 9 && level != 16 && level != 11 && level != 17 && level != 28 && level != 24){
						startToolsSpawnTimer = true;
						collectibles.add(new Collectibles(simpleEnemy.getX(), simpleEnemy.getY(), "tools"));
						}
					
					if((level == 9 || level == 16) && lg.getSimpleEnemiesCounter() == 39 && !startToolsSpawnTimer){
						startToolsSpawnTimer = true;
						collectibles.add(new Collectibles(MainGame.WIDTH / 2 - 20, MainGame.HEIGHT, "tools"));
						collectibles.add(new Collectibles(MainGame.WIDTH / 2 - 20, MainGame.HEIGHT + 40, "tools"));
					}
					if((level == 11|| level == 17 || level == 23 || level == 28) && (lg.getSimpleEnemiesCounter() == 72 || lg.getSimpleEnemiesCounter() == 73) && !startToolsSpawnTimer){
						startToolsSpawnTimer = true;
						if(level == 11 || level == 17)
						collectibles.add(new Collectibles(MainGame.WIDTH / 2 - 20, MainGame.HEIGHT, "tools"));
						collectibles.add(new Collectibles(MainGame.WIDTH / 2 - 20, MainGame.HEIGHT + 40, "tools"));
						collectibles.add(new Collectibles(MainGame.WIDTH / 2 - 20, MainGame.HEIGHT + 80, "tools"));
					}
					if(level == 24 && lg.isSpawnTools()){
						lg.setSpawnTools(false);
						collectibles.add(new Collectibles(MainGame.WIDTH / 2 - 20, MainGame.HEIGHT, "tools"));
						
					}
					
					//randomly generated collectible shield after stage n
					if(level > 3 && level != 11 && level != 17 && level != 23 && level != 28){
						if(randomNumber == 12 && !startShieldSpawnTimer){
							startShieldSpawnTimer = true;
							collectibles.add(new Collectibles(simpleEnemy.getX(), simpleEnemy.getY(), "collectibleShield"));
							}
					}
						
					if(level == 2 && lg.getSimpleEnemiesCounter() > 5){
						if(randomNumber == 12 && !startShieldSpawnTimer){
						startShieldSpawnTimer = true;
						collectibles.add(new Collectibles(simpleEnemy.getX(), simpleEnemy.getY(), "collectibleShield"));
						}
					}
					if(level == 3 && lg.getSimpleEnemiesCounter() > 15){
						if(randomNumber == 12 && !startShieldSpawnTimer){
						startShieldSpawnTimer = true;
						collectibles.add(new Collectibles(simpleEnemy.getX(), simpleEnemy.getY(), "collectibleShield"));
						}
					}
					
					//randomly generated collectible freezing bullet after stage n
					if(level != 5 && level != 6 && level != 9 && level != 11  && level != 13 && level != 16 && level != 17 && level != 18 && level != 26 && level != 28 && level != 29 && level != 24)
						if(randomNumber == 13 && !startFreezingBulletsSpawnTimer){
						startFreezingBulletsSpawnTimer = true;
						collectibles.add(new Collectibles(simpleEnemy.getX(), simpleEnemy.getY(), "freezingBullets"));
						}
					if(level == 5 && lg.getShootingEnemiesCounter() < 12)
						if(randomNumber == 13 && !startFreezingBulletsSpawnTimer){
						startFreezingBulletsSpawnTimer = true;
						collectibles.add(new Collectibles(simpleEnemy.getX(), simpleEnemy.getY(), "freezingBullets"));
						}
					if((level == 9 || level == 16) && lg.isHalfPausePassed() && lg.getShootingEnemiesCounter() < 22){
						collectibles.add(new Collectibles(MainGame.WIDTH / 2 - 20, MainGame.HEIGHT, "freezingBullets"));
					}
					if((level == 11 || level == 17 || level == 23 || level == 28) && lg.isHalfPausePassed() && lg.getSimpleEnemiesCounter() == 95){
						lg.setHalfPausePassed(false);
						collectibles.add(new Collectibles(MainGame.WIDTH / 2 - 20, MainGame.HEIGHT, "freezingBullets"));
						collectibles.add(new Collectibles(MainGame.WIDTH / 2 + 10, MainGame.HEIGHT, "tools"));
					}
					
					if(level == 13 && lg.isHalfPausePassed() && lg.getSimpleEnemiesCounter() < 10 && !startFreezingBulletsSpawnTimer && !startfasterPlayerSpawnTimer){
						lg.setHalfPausePassed(false);
						startFreezingBulletsSpawnTimer = true;
						startfasterPlayerSpawnTimer = true;
						collectibles.add(new Collectibles(MainGame.WIDTH / 2 - 20, MainGame.HEIGHT, "freezingBullets"));
						collectibles.add(new Collectibles(MainGame.WIDTH / 2 - 60, MainGame.HEIGHT, "fasterPlayer"));
					}
					
					
					//randomly generated collectible luck clover after stage n
						if(randomNumber == 14 && !startluckCloversSpawnTimer){
						startluckCloversSpawnTimer = true;
						collectibles.add(new Collectibles(simpleEnemy.getX(), simpleEnemy.getY(), "luckClover"));
						}
					
					
					//randomly generated collectible triple bullet after stage n
					if(level > 1 && level != 5 && level != 11 && level != 13 && level != 17  && level != 18 && level != 23 && level != 26 && level != 28 && level != 29 && level != 24){
						if(randomNumber == 15 && !startTripleFireBulletsSpawnTimer){
						startTripleFireBulletsSpawnTimer = true;
						collectibles.add(new Collectibles(simpleEnemy.getX(), simpleEnemy.getY(), "tripleFireBullets"));
						}
					}
					if(level == 26 && lg.isHalfPausePassed() == true && lg.getSimpleEnemiesCounter() < 10 && !startTripleFireBulletsSpawnTimer){
						collectibles.add(new Collectibles(MainGame.WIDTH / 2 - 20, MainGame.HEIGHT + 20, "tripleFireBullets"));
					}
					if(level == 24 && lg.isHalfLevelPassed()){
						lg.setHalfLevelPassed(false);
						collectibles.add(new Collectibles(MainGame.WIDTH / 2 - 20, MainGame.HEIGHT + 20, "tripleFireBullets"));
					}

					
					
					//randomly generated collectible quad bullet after stage n
					if(level > 2 && level != 5 && level != 11  && level != 13 && level != 17 && level != 18 && level != 20 && level != 23 && level != 26 && level != 28 && level != 29 && level != 24){
						if(randomNumber == 16 && !startquadFireBulletsSpawnTimer){
						startquadFireBulletsSpawnTimer = true;
						collectibles.add(new Collectibles(simpleEnemy.getX(), simpleEnemy.getY(), "quadFireBullets"));
						}
					}
					if((level == 11 || level == 17 || level == 23 || level == 28) && lg.isHalfPausePassed() && !startquadFireBulletsSpawnTimer && lg.getSimpleEnemiesCounter() < 45){
						lg.setHalfPausePassed(false);
						startquadFireBulletsSpawnTimer = true;
						if(level == 11)
						collectibles.add(new Collectibles(MainGame.WIDTH / 2 - 20, MainGame.HEIGHT + 20, "quadFireBullets"));
						if(level == 17 || level == 23 || level == 28)
						collectibles.add(new Collectibles(MainGame.WIDTH / 2 - 20, MainGame.HEIGHT + 20, "pentaFireBullets"));
						collectibles.add(new Collectibles(MainGame.WIDTH/2 + 10, MainGame.HEIGHT, "collectibleShield"));
						collectibles.add(new Collectibles(MainGame.WIDTH/2 - 10, MainGame.HEIGHT+ 60, "fasterBullets"));
						collectibles.add(new Collectibles(MainGame.WIDTH / 2 - 10, MainGame.HEIGHT + 100, "tools"));
						
					}
					
					//randomly generated collectible fasterPlayer after stage n
						if(randomNumber == 17 && !startfasterPlayerSpawnTimer  && level != 13  && level != 18 && level != 29){
						startfasterPlayerSpawnTimer = true;
						collectibles.add(new Collectibles(simpleEnemy.getX(), simpleEnemy.getY(), "fasterPlayer"));
						}
				
					
					//randomly generated collectible fasterBullets after stage n
						if(level > 4){
						if(randomNumber == 18 && !startfasterBulletsSpawnTimer){
						startfasterBulletsSpawnTimer = true;
						collectibles.add(new Collectibles(simpleEnemy.getX(), simpleEnemy.getY(), "fasterBullets"));
						}
						}
					}
				}
			}
		}
		
		//check for collision between bullets and shooting enemies (Enemy 2)
		for(Bullet bullet : bullets){
			for(ShootingEnemy shootingEnemy : shootingEnemies){
				//if the bullet is from a freezing enemy
				if(bullet.getCollisionRect().collidesWith(shootingEnemy.getCollisionRect()) && bullet.getType() == "enemyFreezingBullet" ){
					shootingEnemy.setFreezed(true);
					bulletsToRemove.add(bullet);
				}
				//if the bullet is not an enemy bullet
				if(bullet.getCollisionRect().collidesWith(shootingEnemy.getCollisionRect()) && bullet.getType() != "enemyFreezingBullet"){
					if(shootingEnemy.getHealth() >= 2){
						bulletsToRemove.add(bullet);
						if(sfx)
						hit.play(0.5f);
						shootingEnemy.setHit(true); 
						shootingEnemy.setHealth(shootingEnemy.getHealth()-1);
						if(isFreezingBullets){
							shootingEnemy.setFreezed(true);
						}
						explosions.add(new Explosion(bullet.getX()-5, bullet.getY() + 30, "hitExplosion"));
					}else{
						//collision accured and the enemy is destroyed
						if(sfx)
						explosionSound.play(0.3f);
					bulletsToRemove.add(bullet);
					
					scorepoints.add(new ScorePoints("plus600"));
					
					shootingEnemiesToRemove.add(shootingEnemy);
					explosions.add(new Explosion(shootingEnemy.getX(), shootingEnemy.getY(), "basicExplosion"));
					score+=600;
					
					//randomly generated coin
					if(randomNumber <= lucky*5 ){ 
						collectibles.add(new Collectibles(shootingEnemy.getX(), shootingEnemy.getY(), "coin"));
						coinsTotal++;
						
					}
					
					
					
					//randomly generated tools after stage n
					if((randomNumber == 11 || randomNumber == 10) && !startToolsSpawnTimer && health <= 0.94 && level != 8){
						if(randomNumber == 11 && !startToolsSpawnTimer){
						startToolsSpawnTimer = true;
						//tools.add(new Tools(shootingEnemy.getX(), shootingEnemy.getY()));
						collectibles.add(new Collectibles(shootingEnemy.getX(), shootingEnemy.getY(), "tools"));
						}
					}
					if(level == 8){
						if(lg.isHalfPausePassed() && !startToolsSpawnTimer){
						startToolsSpawnTimer = true;
						collectibles.add(new Collectibles(MainGame.WIDTH / 2 - 20, MainGame.HEIGHT, "tools"));
						collectibles.add(new Collectibles(MainGame.WIDTH / 2 - 20, MainGame.HEIGHT + 40, "tools"));
						}
					}
					
					//randomly generated collectible shield after stage n
					if(level > 3 && level != 11 && level != 17 && level != 23 && level != 28) {
						if(randomNumber == 12 && !startShieldSpawnTimer){
						startShieldSpawnTimer = true;
						//collectibleShields.add(new ShieldCollectible(shootingEnemy.getX(), shootingEnemy.getY()));
						collectibles.add(new Collectibles(shootingEnemy.getX(), shootingEnemy.getY(), "collectibleShield"));
						}
					}
					
					//randomly generated collectible freezing bullet after stage n
					if(level != 5 && level != 6 && level != 9 && level != 11 && level != 16 && level != 17 && level != 26 && level != 28 && level != 31)
						if(randomNumber == 13 && !startFreezingBulletsSpawnTimer){
						startFreezingBulletsSpawnTimer = true;
						collectibles.add(new Collectibles(shootingEnemy.getX(), shootingEnemy.getY(), "freezingBullets"));
						}
					if(level == 5 && lg.getShootingEnemiesCounter() < 12){
						if(randomNumber == 13 && !startFreezingBulletsSpawnTimer){
						startFreezingBulletsSpawnTimer = true;
						collectibles.add(new Collectibles(shootingEnemy.getX(), shootingEnemy.getY(), "freezingBullets"));
						}
					}
					if(level == 31 && !startFreezingBulletsSpawnTimer && lg.isHalfPausePassed() && lg.getShootingEnemiesCounter() < 12){
						startFreezingBulletsSpawnTimer = true;
						collectibles.add(new Collectibles(MainGame.WIDTH/2 - 10, MainGame.HEIGHT + 700, "freezingBullets"));
					}
					
					
					//randomly generated collectible luck clover after stage n
						if(randomNumber == 14 && !startluckCloversSpawnTimer){
						startluckCloversSpawnTimer = true;
						collectibles.add(new Collectibles(shootingEnemy.getX(), shootingEnemy.getY(), "luckClover"));
						}
					
					
					//randomly generated collectible triple bullet after stage n
					if(level > 2 && level != 5 && level != 11 && level != 17 && level != 23 && level != 26 && level != 28 && level != 31){
						if(randomNumber == 15 && !startTripleFireBulletsSpawnTimer){
						startTripleFireBulletsSpawnTimer = true;
						collectibles.add(new Collectibles(shootingEnemy.getX(), shootingEnemy.getY(), "tripleFireBullets"));
						}
					}
					
					//randomly generated collectible quad bullet after stage n
					if(level > 2 && level != 5 && level != 11 && level != 17 && level != 20 && level != 23 && level != 26 && level != 28 && level != 31){
						if(randomNumber == 16 && !startquadFireBulletsSpawnTimer){
						startquadFireBulletsSpawnTimer = true;
						collectibles.add(new Collectibles(shootingEnemy.getX(), shootingEnemy.getY(), "quadFireBullets"));
						}
					}
					if((level == 5 || level == 20) && lg.isHalfPausePassed() && lg.getShootingEnemiesCounter() < 17 && !startquadFireBulletsSpawnTimer){
						startquadFireBulletsSpawnTimer = true;
						collectibles.add(new Collectibles(MainGame.WIDTH/2 - 10, MainGame.HEIGHT, "quadFireBullets"));
						collectibles.add(new Collectibles(MainGame.WIDTH/2 + 10, MainGame.HEIGHT, "collectibleShield"));
						collectibles.add(new Collectibles(MainGame.WIDTH/2, MainGame.HEIGHT + 50, "fasterBullets"));
						collectibles.add(new Collectibles(MainGame.WIDTH/2, MainGame.HEIGHT + 100, "tools"));
						collectibles.add(new Collectibles(MainGame.WIDTH/2, MainGame.HEIGHT + 150, "tools"));
					}
					if(level == 8 && lg.getShootingEnemiesCounter() == 4 && !startquadFireBulletsSpawnTimer){
						startquadFireBulletsSpawnTimer = true;
						collectibles.add(new Collectibles(MainGame.WIDTH/2, MainGame.HEIGHT + 50, "quadFireBullets"));
						
					}
					
					//randomly generated collectible fasterPlayer after stage n
					if(level > 5){
						if(randomNumber == 17 && !startfasterPlayerSpawnTimer){
						startfasterPlayerSpawnTimer = true;
						collectibles.add(new Collectibles(shootingEnemy.getX(), shootingEnemy.getY(), "fasterPlayer"));
						}
					}
					
					//randomly generated collectible fasterBullets after stage n
					if(level > 5){
						if(randomNumber == 18 && !startfasterBulletsSpawnTimer){
						startfasterBulletsSpawnTimer = true;
						collectibles.add(new Collectibles(shootingEnemy.getX(), shootingEnemy.getY(), "fasterBullets"));
						}
					}
					
					
				}
				
				}
			}
		
		}		
		//check for collision between bullets and shooting bullets enemies (Enemy 3)
		for(Bullet bullet : bullets){
			for(ShootingBulletsEnemy shootingBulletsEnemy : shootingBulletsEnemies){
				//if the bullet is from a freezing enemy
				if(bullet.getCollisionRect().collidesWith(shootingBulletsEnemy.getCollisionRect()) && bullet.getType() == "enemyFreezingBullet"){
					shootingBulletsEnemy.setFreezed(true);
					bulletsToRemove.add(bullet);	
				}
				//if the bullet is not an enemy bullet
				if(bullet.getCollisionRect().collidesWith(shootingBulletsEnemy.getCollisionRect()) && bullet.getType() != "enemyBullet" && bullet.getType() != "enemyFreezingBullet"){
					if(shootingBulletsEnemy.getHealth() >= 2){
						bulletsToRemove.add(bullet);
						if(sfx)
						hit.play(0.5f);
						shootingBulletsEnemy.setHit(true); 
						shootingBulletsEnemy.setHealth(shootingBulletsEnemy.getHealth()-1);
						if(isFreezingBullets){
							shootingBulletsEnemy.setFreezed(true);
						}
						explosions.add(new Explosion(bullet.getX()-5, bullet.getY() + 30, "hitExplosion"));
					}else{
						//collision accured and the enemy is destroyed
						if(sfx)
						explosionSound.play(0.3f);
					bulletsToRemove.add(bullet);
					
					scorepoints.add(new ScorePoints("plus600"));
					
					shootingBulletsEnemiesToRemove.add(shootingBulletsEnemy);
					explosions.add(new Explosion(shootingBulletsEnemy.getX(), shootingBulletsEnemy.getY(), "basicExplosion"));
					score+=600;
					
					//randomly generated coin
					if(randomNumber <= lucky*5 ){ 
						collectibles.add(new Collectibles(shootingBulletsEnemy.getX(), shootingBulletsEnemy.getY(), "coin"));
						coinsTotal++;
						
					}
					
					
					
					//randomly generated tools after stage n
						if((randomNumber == 11 || randomNumber == 10) && !startToolsSpawnTimer && health <= 0.94){
						startToolsSpawnTimer = true;
						collectibles.add(new Collectibles(shootingBulletsEnemy.getX(), shootingBulletsEnemy.getY(), "tools"));
						}
					
					
					//randomly generated collectible shield after stage n
					if(level > 4 && level != 11 && level != 17 && level != 23 && level != 28){
						if(randomNumber == 12 && !startShieldSpawnTimer){
						startShieldSpawnTimer = true;
						collectibles.add(new Collectibles(shootingBulletsEnemy.getX(), shootingBulletsEnemy.getY(), "collectibleShield"));
						}
					}
					
					//randomly generated collectible freezing bullet after stage n
					if(level != 6 && level != 9 && level != 11 && level != 16 && level != 17 && level != 23 && level != 26 && level != 27 && level != 28){
						if(randomNumber == 13 && !startFreezingBulletsSpawnTimer){
						startFreezingBulletsSpawnTimer = true;
						collectibles.add(new Collectibles(shootingBulletsEnemy.getX(), shootingBulletsEnemy.getY(), "freezingBullets"));
						}
					}
					if(level == 6 && lg.isHalfPausePassed() && lg.getShootingBulletsEnemiesCounter() < 36 && !startFreezingBulletsSpawnTimer){
						startFreezingBulletsSpawnTimer = true;
						collectibles.add(new Collectibles(MainGame.WIDTH/2 - 20, MainGame.HEIGHT+100 + 70, "freezingBullets"));
						collectibles.add(new Collectibles(MainGame.WIDTH/2 - 20, MainGame.HEIGHT+100, "collectibleShield"));
						collectibles.add(new Collectibles(MainGame.WIDTH/2 - 20, MainGame.HEIGHT+100 + 130, "tools"));
						collectibles.add(new Collectibles(MainGame.WIDTH/2 - 20, MainGame.HEIGHT+100 + 180, "tools"));
						collectibles.add(new Collectibles(MainGame.WIDTH/2 - 20, MainGame.HEIGHT+100 + 180, "tools"));
					}
					
					if(level == 27 && lg.isHalfPausePassed() && lg.getShootingBulletsEnemiesCounter() < 157 && !startFreezingBulletsSpawnTimer){
						startFreezingBulletsSpawnTimer = true;
						collectibles.add(new Collectibles(MainGame.WIDTH/2 - 20, MainGame.HEIGHT+100 + 70, "freezingBullets"));
					}
					
					//randomly generated collectible luck clover after stage n
					if(level != 11){
						if(randomNumber == 14 && !startluckCloversSpawnTimer){
						startluckCloversSpawnTimer = true;
						collectibles.add(new Collectibles(shootingBulletsEnemy.getX(), shootingBulletsEnemy.getY(), "luckClover"));
						}
					}
					
					//randomly generated collectible triple bullet after stage n
					if(level != 11 && level != 17 && level != 23 && level != 26 && level != 28){
						if(randomNumber == 15 && !startTripleFireBulletsSpawnTimer){
						startTripleFireBulletsSpawnTimer = true;
						collectibles.add(new Collectibles(shootingBulletsEnemy.getX(), shootingBulletsEnemy.getY(), "tripleFireBullets"));
						}
					}
					
					//randomly generated collectible quad bullet after stage n
					if(level != 11 && level != 17 && level != 20 && level != 23 && level != 26 && level != 28){
						if(randomNumber == 16 && !startquadFireBulletsSpawnTimer){
						startquadFireBulletsSpawnTimer = true;
						collectibles.add(new Collectibles(shootingBulletsEnemy.getX(), shootingBulletsEnemy.getY(), "quadFireBullets"));
						}
					}
					
					
					//randomly generated collectible fasterPlayer after stage n
					if(level > 6){
						if(randomNumber == 17 && !startfasterPlayerSpawnTimer){
						startfasterPlayerSpawnTimer = true;
						collectibles.add(new Collectibles(shootingBulletsEnemy.getX(), shootingBulletsEnemy.getY(), "fasterPlayer"));
						}
					}
					
					//randomly generated collectible fasterBullets after stage n
					if(lg.getAsteroidCounter() > 1){
						if(randomNumber == 18 && !startfasterBulletsSpawnTimer){
						startfasterBulletsSpawnTimer = true;
						collectibles.add(new Collectibles(shootingBulletsEnemy.getX(), shootingBulletsEnemy.getY(), "fasterBullets"));
						}
					}
				}
				
				}
			}
		
		}
		
		
		//check for collision between bullets and shooting freezing bullets enemies (Enemy 5)
		for(Bullet bullet : bullets){
			for(ShootingFreezingBulletsEnemy shootingFreezingBulletsEnemy : shootingFreezingBulletsEnemies){
				if(bullet.getCollisionRect().collidesWith(shootingFreezingBulletsEnemy.getCollisionRect()) && bullet.getType() != "enemyBullet" && bullet.getType() != "enemyFreezingBullet"){
					if(shootingFreezingBulletsEnemy.getHealth() >= 2){
						bulletsToRemove.add(bullet);
						if(sfx)
						hit.play(0.5f);
						shootingFreezingBulletsEnemy.setHit(true); 
						shootingFreezingBulletsEnemy.setHealth(shootingFreezingBulletsEnemy.getHealth()-1);
						explosions.add(new Explosion(bullet.getX()-5, bullet.getY() + 30, "hitExplosion"));
					}
					else{
						//collision accured and the enemy is destroyed
						if(sfx)
						explosionSound.play(0.3f);
					bulletsToRemove.add(bullet);
					
					scorepoints.add(new ScorePoints("plus600"));
					
					shootingFreezingBulletsEnemiesToRemove.add(shootingFreezingBulletsEnemy);
					explosions.add(new Explosion(shootingFreezingBulletsEnemy.getX(), shootingFreezingBulletsEnemy.getY(), "basicExplosion"));
					score+=600;
					
					//randomly generated coin
					if(randomNumber <= lucky*5 ){ 
						collectibles.add(new Collectibles(shootingFreezingBulletsEnemy.getX(), shootingFreezingBulletsEnemy.getY(), "coin"));
						coinsTotal++;
					}
					
					//randomly generated tools after stage n
					if((randomNumber == 11 || randomNumber == 10) && !startToolsSpawnTimer && health <= 0.94){
					startToolsSpawnTimer = true;
					collectibles.add(new Collectibles(shootingFreezingBulletsEnemy.getX(), shootingFreezingBulletsEnemy.getY(), "tools"));
					}
					
					
					//randomly generated collectible shield after stage n
					if(randomNumber == 12 && !startShieldSpawnTimer && level != 17 && level != 23 && level != 28){
					startShieldSpawnTimer = true;
					collectibles.add(new Collectibles(shootingFreezingBulletsEnemy.getX(), shootingFreezingBulletsEnemy.getY(), "collectibleShield"));
					}
					
					
					//randomly generated collectible freezing bullet after stage n
					if(randomNumber == 13 && !startFreezingBulletsSpawnTimer && level != 16 && level != 17 && level != 18 && level != 26 && level != 28 && level != 29){
					startFreezingBulletsSpawnTimer = true;
					collectibles.add(new Collectibles(shootingFreezingBulletsEnemy.getX(), shootingFreezingBulletsEnemy.getY(), "freezingBullets"));
					}
				
					
					//randomly generated collectible luck clover after stage n
					if(randomNumber == 14 && !startluckCloversSpawnTimer){
					startluckCloversSpawnTimer = true;
					collectibles.add(new Collectibles(shootingFreezingBulletsEnemy.getX(), shootingFreezingBulletsEnemy.getY(), "luckClover"));
					}

					
					//randomly generated collectible triple bullet after stage n
					if(level != 14 && level != 17 && level != 18 && level != 23 && level != 26 && level != 28 && level != 29){
						if(randomNumber == 15 && !startTripleFireBulletsSpawnTimer){
						startTripleFireBulletsSpawnTimer = true;
						collectibles.add(new Collectibles(shootingFreezingBulletsEnemy.getX(), shootingFreezingBulletsEnemy.getY(), "tripleFireBullets"));
						}
					}
					if((level == 18 || level == 29) && lg.isHalfPausePassed() && !startTripleFireBulletsSpawnTimer && lg.getShootingFreezingBulletsEnemiesCounter() < 7){
						startTripleFireBulletsSpawnTimer = true;
						collectibles.add(new Collectibles(MainGame.WIDTH / 2 - 20, MainGame.HEIGHT + 20, "tripleFireBullets"));
						collectibles.add(new Collectibles(MainGame.WIDTH / 2 + 20, MainGame.HEIGHT + 20, "fasterPlayer"));
					}
					
					//randomly generated collectible quad bullet after stage n
					if(level != 14 && level != 17 && level != 18 && level != 20 && level != 23 && level != 26 && level != 28 && level != 29){
						if(randomNumber == 16 && !startquadFireBulletsSpawnTimer){
						startquadFireBulletsSpawnTimer = true;
						collectibles.add(new Collectibles(shootingFreezingBulletsEnemy.getX(), shootingFreezingBulletsEnemy.getY(), "quadFireBullets"));
						}
					}
					
					//randomly generated collectible fasterPlayer after stage n
					if(level > 12  && level != 18 && level != 29){
						if(randomNumber == 17 && !startfasterPlayerSpawnTimer){
						startfasterPlayerSpawnTimer = true;
						collectibles.add(new Collectibles(shootingFreezingBulletsEnemy.getX(), shootingFreezingBulletsEnemy.getY(), "fasterPlayer"));
						}
					}
					
					//randomly generated collectible fasterBullets after stage n
					if(level > 12){
						if(randomNumber == 18 && !startfasterBulletsSpawnTimer){
						startfasterBulletsSpawnTimer = true;
						collectibles.add(new Collectibles(shootingFreezingBulletsEnemy.getX(), shootingFreezingBulletsEnemy.getY(), "fasterBullets"));
						}
					}
				}
				
				}
			}
		
		}
		
		
		//check for collision between bullets and boss
		for(BossEnemy bossEnemy : bossEnemies){
			for(Bullet bullet: bullets){
				
				//if there is shield on the boss
				if(bullet.getCollisionRect().collidesWith(bossEnemy.getCollisionRect()) && (bullet.getType() != "bossBullet" && bullet.getType() != "bossBulletRight" && bullet.getType() != "bossBulletLeft" && bullet.getType() != "enemyBullet" && bullet.getType() != "enemySmartBullet" && bullet.getType() != "enemySmartBulletRight1" && bullet.getType() != "enemySmartBulletRight2" && bullet.getType() != "enemySmartBulletLeft1" && bullet.getType() != "enemySmartBulletLeft2") && bossEnemy.shield){
					bullet.setReverseBulletDirection(true);
					bullet.setDEFAULT_SPEED(400);
					score -= 100;
					scorepoints.add(new ScorePoints("minus100"));
					explosions.add(new Explosion(bullet.getX(), bullet.getY() - 20, "basicExplosion"));
				}
				
				//if there is not a shield on the boss
				if(bossEnemy.getCollisionRect().collidesWith(bullet.getCollisionRect()) && (bullet.getType() != "enemyBullet" && bullet.getType() != "bossBullet" && bullet.getType() != "bossBulletRight" && bullet.getType() != "bossBulletLeft" && bullet.getType() != "enemyBullet" && bullet.getType() != "enemySmartBullet" && bullet.getType() != "enemySmartBulletRight1" && bullet.getType() != "enemySmartBulletRight2" && bullet.getType() != "enemySmartBulletLeft1" && bullet.getType() != "enemySmartBulletLeft2") && !bossEnemy.shield){
					
					//if the boss have health
					if(bossEnemy.getHealth() > 0){
						bulletsToRemove.add(bullet);
						bossEnemy.setHit(true); 
						bossEnemy.setHealth(bossEnemy.getHealth()-1);
						score += 100;
						scorepoints.add(new ScorePoints("plus100"));
						explosions.add(new Explosion(bullet.getX()-5, bullet.getY() + 30, "hitExplosion"));
						if(sfx)
							hit.play(0.5f);
						
						//for boss 2
						if(bossEnemy.getBossType() == "boss2"){
							
							//check how much does the boss have left so the enemies will spawn
							if(bossEnemy.getHealth() <= 150)
								lg.setSpawnEnemiesWithBoss(true);
							
							//check if the boss is half HP so a triple bullets spawn
							if((int)bossEnemy.getHealth() == 95){
								lg.setHalfLevelPassed(true);
							}
							
							//check every 50HP you take from the boss so tools are spawned
							lg.setCounterForTools(lg.getCounterForTools()+1);
							if(lg.getCounterForTools() == 50){
								lg.setSpawnTools(true);
								lg.setCounterForTools(0);
							}
						}
						
					}
					else{
					//collision accured and the enemy is destroyed
					bulletsToRemove.add(bullet);
					bossEnemiesToRemove.add(bossEnemy);
					explosions.add(new Explosion(bossEnemy.getX(), bossEnemy.getY(), "enemyExplosion"));
					scorepoints.add(new ScorePoints("plus4000"));
					score+=4000;
					
					lg.setFirstBossKilled(true);
					//20 coins
					if(coinsTotal < 21)
					for(int i = 0; i < 20; i++){
					collectibles.add(new Collectibles(rand.nextInt(MainGame.WIDTH - 40), rand.nextInt(MainGame.HEIGHT * 2) + 720, "coin"));
					coinsTotal++;
					}
				}
					
					
				}
			}
		}
		
		
		//check collision between player and boss
		for(BossEnemy bossEnemy : bossEnemies){
			if(bossEnemy.getCollisionRect().collidesWith(playerRect) && !isPlayerHit && !startPlayerInvincibleTimer && !isPlayerDead){
				if(sfx)
					explosionPlayerSound.play();
					startPlayerInvincibleTimer = true;
					isPlayerHit = true;
					--lives;
					if(score > 100)
					score /= 2;
					explosions.add(new Explosion(x, y, "playerExplosion"));
			}
		}
		
		
		//check for collision between player and asteroids
		for(Asteroid asteroid : asteroids){
			if(asteroid.getCollisionRect().collidesWith(playerRect) && !isPlayerHit && !startPlayerInvincibleTimer && !isPlayerDead){
				if(sfx)
				explosionPlayerSound.play();
				asteroidsToRemove.add(asteroid);
				startPlayerInvincibleTimer = true;
				isPlayerHit = true;
				--lives;
				if(score > 100)
				score /= 2;
				explosions.add(new Explosion(x, y, "playerExplosion"));
				
			}
		}
		//check for collision between player and simple enemies (Enemy 1)
		for(SimpleEnemy simpleEnemy : simpleEnemies){
			if(simpleEnemy.getCollisionRect().collidesWith(playerRect) && !isPlayerHit && !startPlayerInvincibleTimer && !isPlayerDead){
				if(sfx)
				explosionPlayerSound.play();
				simpleEnemiesToRemove.add(simpleEnemy);
				startPlayerInvincibleTimer = true;
				isPlayerHit = true;
				--lives;
				if(score > 100)
				score /= 2;
				explosions.add(new Explosion(x, y, "playerExplosion"));
			}
		}
	
		//check for collision between player and shooting enemies (Enemy 2)
		for(ShootingEnemy shootingEnemy : shootingEnemies){
			if(shootingEnemy.getCollisionRect().collidesWith(playerRect) && !isPlayerHit && !startPlayerInvincibleTimer && !isPlayerDead){
				if(sfx)
				explosionPlayerSound.play();
				shootingEnemiesToRemove.add(shootingEnemy);
				startPlayerInvincibleTimer = true;
				isPlayerHit = true;
				--lives;
				if(score > 100)
				score /= 2;
				explosions.add(new Explosion(x, y, "playerExplosion"));
			}
		}
		
		//check for collision between player and shooting bullets enemies (Enemy 3)
		for(ShootingBulletsEnemy shootingBulletsEnemy : shootingBulletsEnemies){
			if(shootingBulletsEnemy.getCollisionRect().collidesWith(playerRect) && !isPlayerHit && !startPlayerInvincibleTimer && !isPlayerDead){
				if(sfx)
				explosionPlayerSound.play();
				shootingBulletsEnemiesToRemove.add(shootingBulletsEnemy);
				startPlayerInvincibleTimer = true;
				isPlayerHit = true;
				--lives;
				if(score > 100)
				score /= 2;
				explosions.add(new Explosion(x, y, "playerExplosion"));
			}
		}
		
		
		//check for collision between player and boss enemies 
		for(BossEnemy bossEnemy : bossEnemies){
			if(bossEnemy.getCollisionRect().collidesWith(playerRect) && !isPlayerHit && !startPlayerInvincibleTimer && !isPlayerDead){
				bossEnemiesToRemove.add(bossEnemy);
				startPlayerInvincibleTimer = true;
				isPlayerHit = true;
				--lives;
				if(score > 100)
				score /= 2;
				explosions.add(new Explosion(x, y, "playerExplosion"));
			}
		}
		
		//check for collision between player and shooting freezing bullets enemies (Enemy 5)
		for(ShootingFreezingBulletsEnemy shootingFreezingBulletsEnemy : shootingFreezingBulletsEnemies){
			if(shootingFreezingBulletsEnemy.getCollisionRect().collidesWith(playerRect) && !isPlayerHit && !startPlayerInvincibleTimer && !isPlayerDead){
				if(sfx)
				explosionPlayerSound.play();
				shootingFreezingBulletsEnemiesToRemove.add(shootingFreezingBulletsEnemy);
				startPlayerInvincibleTimer = true;
				isPlayerHit = true;
				--lives;
				if(score > 100)
				score /= 2;
				explosions.add(new Explosion(x, y, "playerExplosion"));
			}
		}
		
		
		//check for collision between player and shooting enemies BEAMS (Enemy 2)
		for(ShootingEnemy shootingEnemy : shootingEnemies){
			if(shootingEnemy.getBeamRect().collidesWith(playerRect) && !isPlayerHit && shootingEnemy.shoot && shields.isEmpty() && !startPlayerInvincibleTimer && !isPlayerDead){
				if(sfx)
				explosionPlayerSound.play();
				startPlayerInvincibleTimer = true;
				isPlayerHit = true;
				--lives;
				if(score > 100)
				score /= 2;
				explosions.add(new Explosion(x, y, "playerExplosion"));
				shootingEnemy.setCollides(true);
			}
		}	
		
		
		//check for collison between bullets and player
			for(Bullet bullet : bullets){
				if(bullet.getCollisionRect().collidesWith(playerRect) && (bullet.getType() == "bossBullet" ||  bullet.getType() == "bossBulletRight" || bullet.getType() == "bossBulletLeft" || bullet.getType() == "enemySmartBullet" || bullet.getType() == "enemySmartBulletRight1" || bullet.getType() == "enemySmartBulletRight2" || bullet.getType() == "enemySmartBulletLeft1" || bullet.getType() == "enemySmartBulletLeft2" || bullet.getType() == "enemyBullet" || (bullet.getType() == "simpleBullet" && bullet.isReverseBulletDirection())) && !isPlayerHit && !startPlayerInvincibleTimer && !isPlayerDead){
					if(sfx)
					explosionPlayerSound.play();
					bulletsToRemove.add(bullet);
					isPlayerHit = true;
					startPlayerInvincibleTimer = true;
					--lives;
					if(score > 100)
					score /= 2;
					explosions.add(new Explosion(x, y, "playerExplosion"));
				}
				//if the bullet is freezing
				if(bullet.getCollisionRect().collidesWith(playerRect) && bullet.getType() == "enemyFreezingBullet" && !isPlayerHit && !startPlayerInvincibleTimer && !isPlayerDead){
					bulletsToRemove.add(bullet);
					if(sfx)
					freeze.play(1f);
					if(score > 1000)
					score-=100;
					scorepoints.add(new ScorePoints("minus100"));
					isSlowerPlayer = true;
					
					for(PlayerModes playerMode : playerModes){
						if(playerMode.getType() == "slowerPlayerMode")
							playerMode.remove = true;
						if(playerMode.getType() == "fasterPlayerMode")
							playerMode.remove = true;
					}
					playerModes.add(new PlayerModes("slowerPlayerMode", this));
				}
			}
				

		
		//check collision betweeen asteroids and bottom of the screen
		for(Asteroid asteroid : asteroids){
			if(asteroid.getY() <  40){
				asteroidsToRemove.add(asteroid);
				explosions.add(new Explosion(asteroid.getX(), asteroid.getY()- 20, "basicExplosion"));
				
				//if the player is not dead
				if(!isPlayerDead){
					if(score >= 100){
						score -= 300;
						scorepoints.add(new ScorePoints("minus300"));
					}
					health -= 0.02f;
				}
			}
		}
		
		
		//check for collision between simple enemies (Enemy 1) and bottom of the screen
		for(SimpleEnemy simpleEnemy : simpleEnemies){
			if(simpleEnemy.getY() < 40){
				explosions.add(new Explosion(simpleEnemy.getX(), simpleEnemy.getY()- 20, "basicExplosion"));
				simpleEnemiesToRemove.add(simpleEnemy);
				//if the player is not dead
				if(!isPlayerDead){
					if(score >= 500){
						score -= 400;
						scorepoints.add(new ScorePoints("minus400"));
					}
					health -= 0.08f;
				}
			}
		}
		
		
	
		//check for collision between shooting enemies (Enemy 2) and bottom of the screen
		for(ShootingEnemy shootingEnemy : shootingEnemies){
			if(shootingEnemy.getY() < 40){
				shootingEnemiesToRemove.add(shootingEnemy);
				explosions.add(new Explosion(shootingEnemy.getX(), shootingEnemy.getY()- 20, "basicExplosion"));
				//if the player is not dead
				if(!isPlayerDead){
					if(score >= 100){
						score -= 400;
						scorepoints.add(new ScorePoints("minus400"));
					}
					health -= 0.04f;
				}
			}
		}		
		
		//check for collision between shooting bullet enemies (Enemy 3) and bottom of the screen
		for(ShootingBulletsEnemy shootingBulletsEnemy : shootingBulletsEnemies){
			if(shootingBulletsEnemy.getY() < 40){
				shootingBulletsEnemiesToRemove.add(shootingBulletsEnemy);
				explosions.add(new Explosion(shootingBulletsEnemy.getX(), shootingBulletsEnemy.getY()- 20, "basicExplosion"));
				//if the player is not dead
				if(!isPlayerDead){
					if(score >= 100){
						score -= 400;
						scorepoints.add(new ScorePoints("minus400"));
					}
					health -= 0.03f;
				}
			}
		}		
		
	
		
		//check for collision between shooting freezing bullet enemies (Enemy 5) and bottom of the screen
		for(ShootingFreezingBulletsEnemy shootingFreezingBulletsEnemy : shootingFreezingBulletsEnemies){
			if(shootingFreezingBulletsEnemy.getY() < 40){
				shootingFreezingBulletsEnemiesToRemove.add(shootingFreezingBulletsEnemy);
				explosions.add(new Explosion(shootingFreezingBulletsEnemy.getX(), shootingFreezingBulletsEnemy.getY()- 20, "basicExplosion"));
				//if the player is not dead
				if(!isPlayerDead){
					if(score >= 100){
						score -= 400;
						scorepoints.add(new ScorePoints("minus400"));
					}
					health -= 0.04f;
				}
			}
		}	
		
		//check for collision between  shooting enemy BEAMS and bottom
		for(ShootingEnemy shootingEnemy : shootingEnemies){
			if(!shootingEnemy.isCollides() && shootingEnemy.shoot && !shootingEnemy.isDidHit()){
				
				//if the player is not dead
				if(!isPlayerDead){
					health -= 0.04f;
					score -=300;
					scorepoints.add(new ScorePoints("minus300"));
				}
				shootingEnemy.setDidHit(true);
		
			}
		}
		
	
		//check for collison between shooting enemies bullets(Enemy 3) and bottom of the screen
		for(Bullet bullet : bullets){
			if(bullet.getType() == "enemyBullet"){
				if(bullet.getY() <= 50){
					bulletsToRemove.add(bullet);
					explosions.add(new Explosion(bullet.getX(), bullet.getY()- 20, "basicExplosion"));
					
					//if the player is not dead
					if(!isPlayerDead){
						if(score >= 100){
							score -= 100;
							scorepoints.add(new ScorePoints("minus100"));
						}
						health -= 0.01f;
					}
				}
			}
		}
		
		//check for collison between shooting enemies bullets(Enemy 3) and bottom of the screen
		for(Bullet bullet : bullets){
			if(bullet.getType() == "bossBullet" || bullet.getType() == "bossBulletRight" || bullet.getType() == "bossBulletLeft"){
				if(bullet.getY() <= 50){
					bulletsToRemove.add(bullet);
					explosions.add(new Explosion(bullet.getX(), bullet.getY()- 20, "basicExplosion"));
					
					//if the player is not dead
					if(!isPlayerDead){
						if(score >= 100){
							score -= 100;
							scorepoints.add(new ScorePoints("minus100"));
						}
						health -= 0.01f;
					}
				}
			}
		}
		
		//check for collison between shooting enemies freezing bullets(Enemy 5) and bottom of the screen
		for(Bullet bullet : bullets){
			if(bullet.getType() == "enemyFreezingBullet"){
				if(bullet.getY() <= 50){
					bulletsToRemove.add(bullet);
					explosions.add(new Explosion(bullet.getX(), bullet.getY()- 20, "hitExplosion"));
					
					//if the player is not dead
					if(!isPlayerDead){
						if(score >= 100){
							score -= 100;
							scorepoints.add(new ScorePoints("minus100"));
						}
						health -= 0.01f;
					}
				}
			}
		}
		
		//check for collison between smart enemies bullets(Enemy 4) and bottom of the screen
		for(Bullet bullet : bullets){
			if(!lg.isFirstBossKilled() && (bullet.getType() == "enemySmartBullet" || bullet.getType() == "enemySmartBulletRight1" || bullet.getType() == "enemySmartBulletRight2" || bullet.getType() == "enemySmartBulletLeft1" || bullet.getType() == "enemySmartBulletLeft2") || bullet.isReverseBulletDirection()){
				if(bullet.getY() <= 50){
					bulletsToRemove.add(bullet);
					explosions.add(new Explosion(bullet.getX(), bullet.getY()- 20, "basicExplosion"));
					
					//if the player is not dead
					if(!isPlayerDead){
						if(score >= 100){
							score -= 100;
							scorepoints.add(new ScorePoints("minus100"));
						}
						health -= 0.02f;
					}
				}
			}
		}
				
		//check for collision between collectibles and the bottom of the screen
		for(Collectibles collectible : collectibles){
			if(collectible.getY() < - 30){
				collectiblesToRemove.add(collectible);
			}
		}
		
		//check collision between bullets and the end of the screen
		for(Bullet bullet : bullets){
			if(bullet.getY() > MainGame.HEIGHT + 30){
				bulletsToRemove.add(bullet);
			}
		}
		
	
		
		//check for collision between asteroids and shooting enemy BEAMS
		for(Asteroid asteroid : asteroids){
			for(ShootingEnemy shootingEnemy : shootingEnemies){
				if(asteroid.getCollisionRect().collidesWith(shootingEnemy.getBeamRect())){
					asteroidsToRemove.add(asteroid);
					explosions.add(new Explosion(asteroid.getX(), asteroid.getY(), "basicExplosion"));
					
				
				}
			}
		}	
		
		

		
		///////////////////////////////////////////////////////////////////////////////////////
		//check for collision collectibles  and player
				for(Collectibles collectible : collectibles){
					if(collectible.getCollisionRect().collidesWith(playerRect) && !isPlayerHit && !isPlayerDead){
						collectiblesToRemove.add(collectible);
						if(collectible.getType() == "coin"){
						if(sfx)
						coin.play(0.5f);
						scorepoints.add(new ScorePoints("plus1000"));
						score += 1000;
						coinsCaught++;
						
						}
						
						if(collectible.getType() == "tools" && !isPlayerDead){
							if(sfx)
							tools.play(0.7f);
							score += 100;
							scorepoints.add(new ScorePoints("plus100"));
							if(health < 1)
							health += 0.08f;
						}
						if(collectible.getType() == "collectibleShield"){
							if(level != 11 && level != 17 && level != 20 && level != 23 && level != 28){
							shields.add(new Shield(x - 50, y, this, 6f));
							if(sfx)
							shield.play(1f);
							playerModes.add(new PlayerModes("shieldMode", this));
							playerModesCounter++;
							score += 100;
							scorepoints.add(new ScorePoints("plus100"));
							}
							if(level == 11 || level == 17 || level == 23 || level == 28){
								shields.add(new Shield(x - 50, y, this, 16f));
								playerModes.add(new PlayerModes("shieldMode", this));
								if(sfx)
								shield.play(1f);
								playerModesCounter++;
								score += 100;
								scorepoints.add(new ScorePoints("plus100"));
							}
							if(level == 20){
								shields.add(new Shield(x - 50, y, this, 9f));
								playerModes.add(new PlayerModes("shieldMode", this));
								if(sfx)
								shield.play(1f);
								playerModesCounter++;
								score += 100;
								scorepoints.add(new ScorePoints("plus100"));
							}
							
						}
						if(collectible.getType() == "freezingBullets"){
							//if there is other bullet mode activated
							for(PlayerModes playerMode : playerModes){
								if(playerMode.getType() == "tripleFireBulletsMode" || playerMode.getType() == "quadFireBulletsMode" || playerMode.getType() == "freezingBulletsMode"){
									playerMode.remove = true;
									playerModesCounter--;
								}
							}
							if(sfx)
							bulletsUpdate.play();
							fireBullets = 2;
							isFreezingBullets = true;
							playerModes.add(new PlayerModes("freezingBulletsMode", this));
							playerModesCounter++;
							
						}
						if(collectible.getType() == "luckClover"){
							playerModes.add(new PlayerModes("luckMode", this));
							if(sfx)
							playerMode.play(0.4f);
							playerModesCounter++;
							lucky = 2;
						}
						
						if(collectible.getType() == "tripleFireBullets"){
							//if there is other bullet mode activated
							for(PlayerModes playerMode : playerModes){
								if(playerMode.getType() == "freezingBulletsMode" || playerMode.getType() == "quadFireBulletsMode" || playerMode.getType() == "pentaBulletsMode" || playerMode.getType() == "tripleFireBulletsMode" ){
									isFreezingBullets = false;
									playerMode.remove = true;
									playerModesCounter--;
								}
							}
							if(sfx)
							bulletsUpdate.play();
							playerModes.add(new PlayerModes("tripleFireBulletsMode", this));
							playerModesCounter++;
							fireBullets = 3;
						}
						
						if(collectible.getType() == "quadFireBullets"){
							//if there is other bullet mode activated
							for(PlayerModes playerMode : playerModes){
								if(playerMode.getType() == "freezingBulletsMode" || playerMode.getType() == "tripleFireBulletsMode" || playerMode.getType() == "pentaBulletsMode" || playerMode.getType() == "quadFireBulletsMode"){
									isFreezingBullets = false;
									playerMode.remove = true;
									playerModesCounter--;
									
								}
							}
							if(sfx)
							bulletsUpdate.play();
							playerModes.add(new PlayerModes("quadFireBulletsMode", this));
							playerModesCounter++;
							fireBullets = 4;
						}
						
						if(collectible.getType() == "pentaFireBullets"){
							//if there is other bullet mode activated
							for(PlayerModes playerMode : playerModes){
								if(playerMode.getType() == "freezingBulletsMode" || playerMode.getType() == "tripleFireBulletsMode" || playerMode.getType() == "quadFireBulletsMode"){
									isFreezingBullets = false;
									playerMode.remove = true;
									playerModesCounter--;
									
								}
							}
							if(sfx)
							bulletsUpdate.play();
							playerModes.add(new PlayerModes("pentaFireBulletsMode", this));
							playerModesCounter++;
							fireBullets = 5;
						}
						
						if(collectible.getType() == "fasterPlayer"){
							//if there is slower player mode remove it
							for(PlayerModes playerMode : playerModes){
								if(playerMode.getType() == "slowerPlayerMode"){
									playerMode.remove = true;
									isSlowerPlayer = false;
								}
							}
							playerModes.add(new PlayerModes("fasterPlayerMode", this));
							if(sfx)
							playerMode.play(0.5f);
							playerModesCounter++;
							SPEED = 450;
						}
						
						if(collectible.getType() == "fasterBullets"){
							playerModes.add(new PlayerModes("fasterBulletsMode", this));
							if(sfx)
							playerMode.play(0.5f);
							playerModesCounter++;
							isFasterBullets = true;
						}
					}
				}
		


		
				
		//check for collision between shield and etities
		for(Shield shield : shields){
			//shooting enemies
			for(ShootingEnemy shootingEnemy : shootingEnemies){
				if(shield.getRect().collidesWith(shootingEnemy.getBeamRect())){
					//collision accured
					shootingEnemy.startPoint = 150;
					shootingEnemy.setCollides(true);
				
				}
			}
			//asteroids
			for(Asteroid asteroid : asteroids){
				if(shield.getRect().collidesWith(asteroid.getCollisionRect())){
					//collision accured
					asteroidsToRemove.add(asteroid);
					explosions.add(new Explosion(asteroid.getX(), asteroid.getY()-30, "basicExplosion"));
					
				
				}
			}
			//simple enemies
			for(SimpleEnemy simpleEnemy : simpleEnemies){
				if(shield.getRect().collidesWith(simpleEnemy.getCollisionRect())){
					//collision accured
					simpleEnemiesToRemove.add(simpleEnemy);
					explosions.add(new Explosion(simpleEnemy.getX(), simpleEnemy.getY()-30, "basicExplosion"));
					
				}
			}
			//shooting enemies
			for(ShootingEnemy shootingEnemy : shootingEnemies){
				if(shield.getRect().collidesWith(shootingEnemy.getCollisionRect())){
					//collision accured
					shootingEnemiesToRemove.add(shootingEnemy);
					explosions.add(new Explosion(shootingEnemy.getX(), shootingEnemy.getY()-30, "basicExplosion"));
					
				}
			}
			//shooting bullets enemies
			for(ShootingBulletsEnemy shootingBulletsEnemy : shootingBulletsEnemies){
				if(shield.getRect().collidesWith(shootingBulletsEnemy.getCollisionRect())){
					//collision accured
					shootingBulletsEnemiesToRemove.add(shootingBulletsEnemy);
					explosions.add(new Explosion(shootingBulletsEnemy.getX(), shootingBulletsEnemy.getY()-30, "basicExplosion"));
					
				}
			}
			//shooting freezing bullets enemies
			for(ShootingFreezingBulletsEnemy shootingFreezingBulletsEnemy : shootingFreezingBulletsEnemies){
				if(shield.getRect().collidesWith(shootingFreezingBulletsEnemy.getCollisionRect())){
					//collision accured
					shootingFreezingBulletsEnemiesToRemove.add(shootingFreezingBulletsEnemy);
					explosions.add(new Explosion(shootingFreezingBulletsEnemy.getX(), shootingFreezingBulletsEnemy.getY()-30, "basicExplosion"));
					
				}
			}
			//enemy bullets and reversed bullets
			for(Bullet bullet : bullets){
				if(shield.getRect().collidesWith(bullet.getCollisionRect()) && (bullet.getType() == "enemySmartBullet" || bullet.getType() == "enemySmartBulletRight1" || bullet.getType() == "enemySmartBulletRight2" || bullet.getType() == "enemySmartBulletLeft1" || bullet.getType() == "enemySmartBulletLeft2" || bullet.getType() == "bossBullet" || bullet.getType() == "bossBulletRight" || bullet.getType() == "bossBulletLeft" || bullet.getType() == "enemyBullet" || bullet.isReverseBulletDirection() || bullet.getType() == "enemyFreezingBullet") ){
					//collision accured
					bulletsToRemove.add(bullet);
					explosions.add(new Explosion(bullet.getX(), bullet.getY()-30, "basicExplosion"));
					
				}
			}
		}
			
	
		
		//disposing the redundant objects
		for(Bullet bullet : bulletsToRemove)
			bullet.dispose();
		for(SimpleEnemy simpleEnemy : simpleEnemiesToRemove)
			simpleEnemy.dispose();
		for(ShootingEnemy shootingEnemy : shootingEnemiesToRemove)
			shootingEnemy.dispose();
		for(ShootingBulletsEnemy shootingBulletsEnemy : shootingBulletsEnemiesToRemove)
			shootingBulletsEnemy.dispose();
		for(Explosion explosion : explosionsToRemove)
			explosion.dispose();
		for(ShootingFreezingBulletsEnemy shootingFreezingBulletsEnemy : shootingFreezingBulletsEnemiesToRemove)
			shootingFreezingBulletsEnemy.dispose();
		for(ScorePoints scorepoints : pointsToRemove)
			scorepoints.dispose();
		for(Collectibles collectibles : collectiblesToRemove)
			collectibles.dispose();
		for(PlayerModes playerModes : playerModesToRemove)
			playerModes.dispose();
		for(Shield shield : shieldsToRemove)
			shield.dispose();
		
		
		//removing objects
		bullets.removeAll(bulletsToRemove);
		explosions.removeAll(explosionsToRemove);
		scorepoints.removeAll(pointsToRemove);
		asteroids.removeAll(asteroidsToRemove);
		shields.removeAll(shieldsToRemove);		
		simpleEnemies.removeAll(simpleEnemiesToRemove);
		shootingEnemies.removeAll(shootingEnemiesToRemove);
		shootingBulletsEnemies.removeAll(shootingBulletsEnemiesToRemove);
		shootingFreezingBulletsEnemies.removeAll(shootingFreezingBulletsEnemiesToRemove);
		bossEnemies.removeAll(bossEnemiesToRemove);
		collectibles.removeAll(collectiblesToRemove);
		playerModes.removeAll(playerModesToRemove);
		bossEnemies.removeAll(bossEnemiesToRemove);
	}
		}
	}
	
														/*RENDER METHOD*/
	@Override
	public void render(float delta) {

		update(delta);
		
		//autofill screen *not necessery*
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.batch.begin();
	
		
		//background
		game.batch.draw(bg, 0, bgY);
		game.batch.draw(bg, 0, bgY + MainGame.HEIGHT);
		
		if(!isEndLevel){
			
		//arrows for the movement
		//right
		if(isRightArrowTouched)
			game.batch.draw(arrowMoveBlue, arrowMoveX, arrowMoveY);
		else
			game.batch.draw(arrowMoveWhite, arrowMoveX, arrowMoveY);
		//left
		if(isLeftArrowTouched)
			game.batch.draw(arrowMoveBlue, 20, arrowMoveY, arrowMoveWhite.getWidth(), arrowMoveWhite.getHeight(), 0, 0, arrowMoveWhite.getWidth(), arrowMoveBlue.getHeight(), true, false);
		else
			game.batch.draw(arrowMoveWhite, 20, arrowMoveY, arrowMoveBlue.getWidth(), arrowMoveBlue.getHeight(), 0, 0, arrowMoveWhite.getWidth(), arrowMoveBlue.getHeight(), true, false);
		

		//player animation
		if(isSlowerPlayer)
			game.batch.setColor(Color.BLUE);
		if(!isSlowerPlayer)
			game.batch.setColor(Color.WHITE);
		
		if(!isPlayerDead && !isPlayerHit)
		game.batch.draw(rolls[roll].getKeyFrame(stateTime, true),x, y, SHIP_WIDTH, SHIP_HEIGHT);
		
		game.batch.setColor(Color.WHITE);
		
		//level generator rendering (1,2,3 countdown)
		lg.render(game.batch);
		
		//bullets rendering
		for(Bullet bullet: bullets){
			bullet.render(game.batch);
		}
		
		game.batch.draw(land, 0, 0);
		
		//collectibles rendering
		for(Collectibles collectible : collectibles){
			collectible.render(game.batch);
		}
		
		//player modes rendering
		for(PlayerModes playerMode : playerModes){
			playerMode.render(game.batch);
		}
		
		//asteroids rendering
		for(Asteroid asteroid: asteroids){
			asteroid.render(game.batch);
		}
		
		//simple enemies (Enemy 1) rendering
		for(SimpleEnemy simpleEnemy: simpleEnemies){
			simpleEnemy.render(game.batch);
		}

		//shooting enemies (Enemy 2) rendering
		for(ShootingEnemy shootingEnemy: shootingEnemies){
			shootingEnemy.render(game.batch);
		}
		
		//shooting bullets enemies (Enemy 3) rendering
		for(ShootingBulletsEnemy shootingBulletsEnemy: shootingBulletsEnemies){
			shootingBulletsEnemy.render(game.batch);
		}
		
		
		//shooting freezing bullets enemies (Enemy 5) rendering
		for(ShootingFreezingBulletsEnemy shootingFreezingBulletsEnemy: shootingFreezingBulletsEnemies){
			shootingFreezingBulletsEnemy.render(game.batch);
		}
		
		//boss rendering
		for(BossEnemy bossEnemy: bossEnemies){
			bossEnemy.render(game.batch);
		}
		
		//explosion rendering
		for(Explosion explosion: explosions){
			explosion.render(game.batch);
		}
		

		//scorepoints rendering
		for(ScorePoints sp: scorepoints){
			sp.render(game.batch);
		}
		
		if(!isPlayerDead){
			//shield rendering
			for(Shield shield: shields){
				shield.render(game.batch);
			}
		}
		
		//health
		game.batch.setColor(Color.WHITE);
		game.batch.draw(healthbarBack, HEALTH_BAR_X, HEALTH_BAR_Y);
		healthUpdate();
		game.batch.draw(blank, HEALTH_BAR_X + 65, HEALTH_BAR_Y + 42, (healthbar.getWidth() - 194) * health, 10);
		game.batch.setColor(Color.GREEN);
		game.batch.draw(blank, MainGame.WIDTH - 100, HEALTH_BAR_Y + 5, lives * 22, 12);
		game.batch.setColor(Color.WHITE);
		game.batch.draw(healthbar, HEALTH_BAR_X, HEALTH_BAR_Y);
		
		
		//rendering the score
		GlyphLayout scoreLayout = new GlyphLayout(fontText, "" + score);
		fontText.draw(game.batch, scoreLayout, HEALTH_BAR_X + 380, HEALTH_BAR_Y + 54);
		
		
		//render the pouse menu
		pouseMenu();
		
		
		}
		//render the End level menu
		endLevelMenu();
				
		game.batch.end();
		
	}

	public void timers(float delta){
		if(!isPaused){
			//updateing the stateTime and timers
			
			stateTime += delta;
			
			//Shield Spawning Timer
			if(startShieldSpawnTimer)
				shieldSpawnTimer -= delta;
			if(shieldSpawnTimer <= 0){
				shieldSpawnTimer = 20;
				startShieldSpawnTimer = false;
			}
			
			//Tools Spawning Timer
			if(startToolsSpawnTimer)
				toolsSpawnTimer -= delta;
			if(toolsSpawnTimer <= 0){
				toolsSpawnTimer = 10;
				startToolsSpawnTimer = false;
			}
			
			//Freezing Bullets spawning Timer
			if(startFreezingBulletsSpawnTimer)
				freezingBulletsSpawnTimer -= delta;
			if(freezingBulletsSpawnTimer <= 0){
				freezingBulletsSpawnTimer = 30;
				startFreezingBulletsSpawnTimer = false;
			}
			
			//luck clover spawning Timer
			if(startluckCloversSpawnTimer)
				luckCloversSpawnTimer -= delta;
			if(luckCloversSpawnTimer <= 0){
				luckCloversSpawnTimer = 30;
				startluckCloversSpawnTimer = false;
			}
			
			//triple fire bullets spawning Timer
			if(startTripleFireBulletsSpawnTimer)
				tripleFireBulletsSpawnTimer -= delta;
			if(tripleFireBulletsSpawnTimer <= 0){
				tripleFireBulletsSpawnTimer = 30;
				startTripleFireBulletsSpawnTimer = false;
			}
			
			//quad fire bullets spawning Timer
			if(startquadFireBulletsSpawnTimer)
				quadFireBulletsSpawnTimer -= delta;
			if(quadFireBulletsSpawnTimer <= 0){
				quadFireBulletsSpawnTimer = 30;
				startquadFireBulletsSpawnTimer = false;
			}
			
			//fasterPlayer spawning Timer
			if(startfasterPlayerSpawnTimer)
				fasterPlayerSpawnTimer -= delta;
			if(fasterPlayerSpawnTimer <= 0){
				fasterPlayerSpawnTimer = 20;
				startfasterPlayerSpawnTimer = false;
			}
			
			//fasterBullets spawning Timer
			if(startfasterBulletsSpawnTimer)
				fasterBulletsSpawnTimer -= delta;
			if(fasterBulletsSpawnTimer <= 0){
				fasterBulletsSpawnTimer = 20;
				startfasterBulletsSpawnTimer = false;
			}
			
			//timer after death
			if(isPlayerDead){
				timeAfterDeath -= delta;
				if(level != 12 && level != 24)
				volume -= delta * 0.5f;
				if(level == 12 || level == 24)
				volume -= delta * 0.08f;
				
				if(volume <= 0)
					volume = 0;
				
				if(timeAfterDeath <= 0)
					isTimeAfterDeathOver = true;
			}
			
			
			//End Level Timer
			if(startEndLevelTimer){
				endLevelTimer -= delta;
				if(level != 12 && level != 24)
				volume -= delta * 0.15f;
				if(level == 12 || level == 24)
				volume -= delta * 0.06f;
				if(volume <= 0)
					volume = 0;
			}
			
			if(endLevelTimer >= 1 && endLevelTimer <= 2.7f){
				isGreeting = true;
				
				//playing the greeting sound one time
				if(!gratsPlayed){
					if(sfx)
					grats.play(0.7f);
					gratsPlayed = true;
				}
				
				alpha_colorGreeting -= delta/1.7f;
				GreetingY -= 150*delta;
			}
			else
				isGreeting = false;
			
			if(endLevelTimer <= 0){
				endLevelTimer = 6;
				endLevel = true;
				startEndLevelTimer = false;
			}	
			
			//updateing the timer for player invincible time
			if(startPlayerInvincibleTimer)
				playerInvincibleTimer -= delta;
			if(playerInvincibleTimer <=0){
				playerInvincibleTimer = 6;
				startPlayerInvincibleTimer = false;
			}
			
			//updating the counter for the random number generator
			countDown -= delta;
			
		}
		
		if(isEndLevel){
		
			//stop music
			musicLevel.stop();
			
			if(music)
			musicLevelWin.play();
			
			//timer for the score
			if(level < 6){
			tempScore += delta*7000; 
			if(!scoreCounterStart){
				if(sfx)
			scoreCounter.loop(0.8f);
			scoreCounterStart = true;
			}
			}
			if(level >= 6 && level < 12){
			tempScore += delta*12000; 
			if(!scoreCounterStart){
				if(sfx)
				scoreCounter.loop(0.8f);
				scoreCounterStart = true;
				}
			}
			if(level == 12){
			tempScore += delta*6000; 
			if(!scoreCounterStart){
				if(sfx)
				scoreCounter.loop(0.8f);
				scoreCounterStart = true;
				}
			}
			if(level >= 12 && level < 20){
			tempScore += delta*23000; 
			if(!scoreCounterStart){
				if(sfx)
				scoreCounter.loop(0.8f);
				scoreCounterStart = true;
				}
			}
			if(level >= 20 && level <= 36){
			tempScore += delta*29000; 
			if(!scoreCounterStart){
				if(sfx)
				scoreCounter.loop(0.8f);
				scoreCounterStart = true;
				}
			}
			
			
			//checking if the new score is bigger than highscore
			if(score > highscore){
				isNewHighscore = true;
				
			}
		
			if(level == 1){
			if(tempScore >= 3000){ star1 = true; stars = 1; star1Time += delta; } 
			if(tempScore >= 12000) { star2 = true; stars = 2; star2Time += delta;} 
			if(tempScore >= 18000) { star3 = true; stars = 3; star3Time += delta;}
			}
			if(level == 2){
			if(tempScore >= 6000){ star1 = true; stars = 1; star1Time += delta;} 
			if(tempScore >= 15000) { star2 = true; stars = 2; star2Time += delta;} 
			if(tempScore >= 20000) { star3 = true; stars = 3; star3Time += delta;}
			}
			if(level == 3){
			if(tempScore >= 5000){ star1 = true; stars = 1; star1Time += delta;} 
			if(tempScore >= 16000) { star2 = true; stars = 2; star2Time += delta;} 
			if(tempScore >= 27000) { star3 = true; stars = 3; star3Time += delta;}
			}
			if(level == 4){
			if(tempScore >= 8000){ star1 = true; stars = 1; star1Time += delta;} 
			if(tempScore >= 18000) { star2 = true; stars = 2; star2Time += delta;} 
			if(tempScore >= 28000) { star3 = true; stars = 3; star3Time += delta;}
			}
			if(level == 5){
			if(tempScore >= 14000){ star1 = true; stars = 1; star1Time += delta;} 
			if(tempScore >= 24000) { star2 = true; stars = 2; star2Time += delta;} 
			if(tempScore >= 35000) { star3 = true; stars = 3; star3Time += delta;}
			}
			if(level == 6){
			if(tempScore >= 20000){ star1 = true; stars = 1; star1Time += delta;} 
			if(tempScore >= 38000) { star2 = true; stars = 2; star2Time += delta;} 
			if(tempScore >= 52000) { star3 = true; stars = 3; star3Time += delta;}
			}
			if(level == 7){
			if(tempScore >= 16000){ star1 = true; stars = 1; star1Time += delta;} 
			if(tempScore >= 31000) { star2 = true; stars = 2; star2Time += delta;} 
			if(tempScore >= 45000) { star3 = true; stars = 3; star3Time += delta;}
			}
			if(level == 8){
			if(tempScore >= 17000){ star1 = true; stars = 1; star1Time += delta;} 
			if(tempScore >= 33000) { star2 = true; stars = 2; star2Time += delta;} 
			if(tempScore >= 48000) { star3 = true; stars = 3; star3Time += delta;}
			}
			if(level == 9){
			if(tempScore >= 18000){ star1 = true; stars = 1; star1Time += delta;} 
			if(tempScore >= 40000) { star2 = true; stars = 2; star2Time += delta;} 
			if(tempScore >= 58000) { star3 = true; stars = 3; star3Time += delta;}
			}
			if(level == 10){
			if(tempScore >= 20000){ star1 = true; stars = 1; star1Time += delta;} 
			if(tempScore >= 40000) { star2 = true; stars = 2; star2Time += delta;} 
			if(tempScore >= 57000) { star3 = true; stars = 3; star3Time += delta;}
			}
			if(level == 11){
			if(tempScore >= 25000){ star1 = true; stars = 1; star1Time += delta;} 
			if(tempScore >= 57000) { star2 = true; stars = 2; star2Time += delta;} 
			if(tempScore >= 79000) { star3 = true; stars = 3; star3Time += delta;}
			}
			if(level == 12){
			if(tempScore >= 1000){ star1 = true; stars = 1; star1Time += delta;} 
			if(tempScore >= 8000 && lives > 1) { star2 = true; stars = 2; star2Time += delta;} 
			if(tempScore >= 14000 && lives > 2) { star3 = true; stars = 3; star3Time += delta;}
			}
			if(level == 13){
			if(tempScore >= 25000){ star1 = true; stars = 1; star1Time += delta;} 
			if(tempScore >= 49000) { star2 = true; stars = 2; star2Time += delta;} 
			if(tempScore >= 60000) { star3 = true; stars = 3; star3Time += delta;}
			}
			if(level == 14){
			if(tempScore >= 33000){ star1 = true; stars = 1; star1Time += delta;} 
			if(tempScore >= 70000) { star2 = true; stars = 2; star2Time += delta;} 
			if(tempScore >= 94000) { star3 = true; stars = 3; star3Time += delta;}
			}
			if(level == 15){
			if(tempScore >= 33000){ star1 = true; stars = 1; star1Time += delta;} 
			if(tempScore >= 70000) { star2 = true; stars = 2; star2Time += delta;} 
			if(tempScore >= 100000) { star3 = true; stars = 3; star3Time += delta;}
			}
			if(level == 16){
			if(tempScore >= 32000){ star1 = true; stars = 1; star1Time += delta;} 
			if(tempScore >= 66000) { star2 = true; stars = 2; star2Time += delta;} 
			if(tempScore >= 90000) { star3 = true; stars = 3; star3Time += delta;}
			}
			if(level == 17){
			if(tempScore >= 38000){ star1 = true; stars = 1; star1Time += delta;} 
			if(tempScore >= 76000) { star2 = true; stars = 2; star2Time += delta;} 
			if(tempScore >= 100000) { star3 = true; stars = 3; star3Time += delta;}
			}
			if(level == 18){
			if(tempScore >= 36000){ star1 = true; stars = 1; star1Time += delta;} 
			if(tempScore >= 60000) { star2 = true; stars = 2; star2Time += delta;} 
			if(tempScore >= 80000) { star3 = true; stars = 3; star3Time += delta;}
			}
			if(level == 19){
			if(tempScore >= 27000){ star1 = true; stars = 1; star1Time += delta;} 
			if(tempScore >= 60000) { star2 = true; stars = 2; star2Time += delta;} 
			if(tempScore >= 72000) { star3 = true; stars = 3; star3Time += delta;}
			}
			if(level == 20){
			if(tempScore >= 30000){ star1 = true; stars = 1; star1Time += delta;} 
			if(tempScore >= 60000) { star2 = true; stars = 2; star2Time += delta;} 
			if(tempScore >= 80000) { star3 = true; stars = 3; star3Time += delta;}
			}
			if(level == 21){
			if(tempScore >= 25000){ star1 = true; stars = 1; star1Time += delta;} 
			if(tempScore >= 50000) { star2 = true; stars = 2; star2Time += delta;} 
			if(tempScore >= 70000) { star3 = true; stars = 3; star3Time += delta;}
			}
			if(level == 36){
			if(tempScore >= 48000){ star1 = true; stars = 1; star1Time += delta;} 
			if(tempScore >= 100000) { star2 = true; stars = 2; star2Time += delta;} 
			if(tempScore >= 160000) { star3 = true; stars = 3; star3Time += delta;}
			}
			if(level == 23){
			if(tempScore >= 37000){ star1 = true; stars = 1; star1Time += delta;} 
			if(tempScore >= 80000) { star2 = true; stars = 2; star2Time += delta;} 
			if(tempScore >= 110000) { star3 = true; stars = 3; star3Time += delta;}
			}
			if(level == 22){
			if(tempScore >= 17000){ star1 = true; stars = 1; star1Time += delta;} 
			if(tempScore >= 40000) { star2 = true; stars = 2; star2Time += delta;} 
			if(tempScore >= 70000) { star3 = true; stars = 3; star3Time += delta;}
			}
			if(level == 25){
			if(tempScore >= 30000){ star1 = true; stars = 1; star1Time += delta;} 
			if(tempScore >= 70000) { star2 = true; stars = 2; star2Time += delta;} 
			if(tempScore >= 100000) { star3 = true; stars = 3; star3Time += delta;}
			}
			if(level == 26){
			if(tempScore >= 40000){ star1 = true; stars = 1; star1Time += delta;} 
			if(tempScore >= 90000) { star2 = true; stars = 2; star2Time += delta;} 
			if(tempScore >= 150000) { star3 = true; stars = 3; star3Time += delta;}
			}
			if(level == 27){
			if(tempScore >= 30000){ star1 = true; stars = 1; star1Time += delta;} 
			if(tempScore >= 80000) { star2 = true; stars = 2; star2Time += delta;} 
			if(tempScore >= 130000) { star3 = true; stars = 3; star3Time += delta;}
			}
			if(level == 28){
			if(tempScore >= 38000){ star1 = true; stars = 1; star1Time += delta;} 
			if(tempScore >= 86000) { star2 = true; stars = 2; star2Time += delta;} 
			if(tempScore >= 130000) { star3 = true; stars = 3; star3Time += delta;}
			}
			if(level == 29){
			if(tempScore >= 38000){ star1 = true; stars = 1; star1Time += delta;} 
			if(tempScore >= 76000) { star2 = true; stars = 2; star2Time += delta;} 
			if(tempScore >= 125000) { star3 = true; stars = 3; star3Time += delta;}
			}
			if(level == 30){
			if(tempScore >= 48000){ star1 = true; stars = 1; star1Time += delta;} 
			if(tempScore >= 110000) { star2 = true; stars = 2; star2Time += delta;} 
			if(tempScore >= 160000) { star3 = true; stars = 3; star3Time += delta;}
			}
			if(level == 31){
			if(tempScore >= 19000){ star1 = true; stars = 1; star1Time += delta;} 
			if(tempScore >= 56000) { star2 = true; stars = 2; star2Time += delta;} 
			if(tempScore >= 81000) { star3 = true; stars = 3; star3Time += delta;}
			}
			if(level == 32){
			if(tempScore >= 40000){ star1 = true; stars = 1; star1Time += delta;} 
			if(tempScore >= 110000) { star2 = true; stars = 2; star2Time += delta;} 
			if(tempScore >= 150000) { star3 = true; stars = 3; star3Time += delta;}
			}
			if(level == 33){
			if(tempScore >= 30000){ star1 = true; stars = 1; star1Time += delta;} 
			if(tempScore >= 70000) { star2 = true; stars = 2; star2Time += delta;} 
			if(tempScore >= 95000) { star3 = true; stars = 3; star3Time += delta;}
			}
			if(level == 34){
			if(tempScore >= 30000){ star1 = true; stars = 1; star1Time += delta;} 
			if(tempScore >= 80000) { star2 = true; stars = 2; star2Time += delta;} 
			if(tempScore >= 130000) { star3 = true; stars = 3; star3Time += delta;}
			}
			if(level == 35){
			if(tempScore >= 40000){ star1 = true; stars = 1; star1Time += delta;} 
			if(tempScore >= 100000) { star2 = true; stars = 2; star2Time += delta;} 
			if(tempScore >= 170000) { star3 = true; stars = 3; star3Time += delta;}
			}
			if(level == 24){
			if(tempScore >= 15000){ star1 = true; stars = 1; star1Time += delta;} 
			if(tempScore >= 30000) { star2 = true; stars = 2; star2Time += delta;} 
			if(tempScore >= 40000 && lives == 3) { star3 = true; stars = 3; star3Time += delta;}
			}
			
		
			if(tempScore >= score) {
				tempScore = score;
				isScoreFinished = true;
				
			}
			
			
			//timer for the coins
			tempCoinsCaught += delta*15;
			if(tempCoinsCaught >= coinsCaught)
				tempCoinsCaught = coinsCaught;
			
			//timer for the bonus
			tempBonus += delta *800;
			if(tempBonus >= bonus)
				tempBonus = bonus;
			
		}
	}
	
	//generating random number every half a second
	public void randNumGenerator(){	
		if (countDown <= 0.1) {
		    Random random = new Random();
		    randomNumber= random.nextInt(20) + 1;
		    countDown += 0.1; // add one second
		    }
		
	}
	
	//scrolling the background
	public void backgroundAnimations(){
		if(bgY > -MainGame.HEIGHT){
			bgY-=bgvelY;
			}
			else
				bgY = 0;
			
	}
	

	//setting the color for the health
	public void healthUpdate(){
		
		if(health > 1)
			health = 1;
		
		if(health > 0.9f)
			game.batch.setColor(0, 1, 0, 1);
		else if(health > 0.8f)
			game.batch.setColor(0, 0.91f, 0, 1);
		else if(health > 0.7f)
			game.batch.setColor(0, 0.88f, 0, 1);
		else if(health > 0.6f)
			game.batch.setColor(0, 0.84f, 0, 1);
		else if(health > 0.5f)
			game.batch.setColor(0, 0.80f, 0, 1);
		else if(health > 0.4f)
			game.batch.setColor(0, 0.76f, 0, 1);
		else if(health > 0.3f)
			game.batch.setColor(0, 0.73f, 0, 1);
		else if(health > 0.2f)
			game.batch.setColor(0, 0.70f, 0, 1);
		else if(health > 0.1f)
			game.batch.setColor(0, 0.66f, 0, 1);
		else if(health > 0.05f)
			game.batch.setColor(0, 0.62f, 0, 1);
		else if(health > 0.02f)
			game.batch.setColor(0, 0.56f, 0, 1);
		
		
		if(health <= 0.02f){
			health = 0f;
			game.batch.setColor(0, 0.56f, 0, 1);
			if(!oneExplosion){
			explosions.add(new Explosion(x, y, "playerExplosion"));
			oneExplosion = true;
			}
			isPlayerDead = true;
			if(isTimeAfterDeathOver){
				game.setScreen(new GameOverScreen(game, score, level, handler, true));
				this.dispose();
				}
		
		}
		
		
	}
	
	public void keyListener(float delta){
		
		//PAUSE MENU
		if((Gdx.input.isKeyJustPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACK)) && !isPlayerDead){
			if(sfx)
			confirmButton.play(0.6f);
			isPaused = true;
			isEscTouched = true;
			
		}
	
		// game.cam.getInputInGameWorld().x
		
		
		int highestpointer = -1; // Setting to -1 because if the pointer is -1 at the end of the loop, then it would be clear that there was no touch 
		for(int pointer = 0; pointer < 20; pointer++) {
		     if(Gdx.input.isTouched(pointer)) { // First check if there is a touch in the first place
		         // int touchX = Gdx.input.getX(pointer); // Get x position of touch in screen coordinates (far left of screen will be 0)
		          float touchX = game.cam.getInputInGameWorld().x;
		          if(touchX < arrowMoveWhite.getWidth() + 90  || touchX >= arrowMoveX - 40) { // Enter your logic here
		               highestpointer = pointer; 
		          } // Note that if the touch is in neither button, the highestpointer will remain what ever it was previously
		     }
		} // At the end of the loop, the highest pointer int would be the most recent touch, or -1

		// And to handle actual movement you need to pass the highest pointer into Gdx.input.getX()
		if(!isPlayerHit && !isPlayerDead) { // Minor improvement: only check this once
			//right movement
		     if(Gdx.input.isKeyPressed(Keys.RIGHT) || (highestpointer > -1 && Gdx.input.getX(highestpointer) >= arrowMoveX - 40)) {
		        x+=SPEED*Gdx.graphics.getDeltaTime();
		        isRightArrowTouched = true;
		        
		      //rightscreen boundries
				if(x + SHIP_WIDTH >= MainGame.WIDTH)
					x = 480 - SHIP_WIDTH;
		        
		     }
		     //left movement
		     else if(Gdx.input.isKeyPressed(Keys.LEFT) || (highestpointer > -1 && Gdx.input.getX(highestpointer) < arrowMoveWhite.getWidth() + 300)) {
		          x-=SPEED*Gdx.graphics.getDeltaTime();
			      isLeftArrowTouched = true;
			      
					//left screen boundries
					if(x <=0)
						x = 0;
		     }
		     else{
		    	 isRightArrowTouched = false; 
		    	 isLeftArrowTouched = false;
		     }
		}
		
		
		//shooting code
		shootTimer += delta;
		if(((isRight() || isLeft()) && !isPlayerHit && !isPlayerDead) && shootTimer >= SHOOT_WAIT_TIME){
			shootTimer = 0;
			
			if(sfx)
			laserPlayer.play(1f);
			
			if(!isFreezingBullets){
				if(fireBullets == 2){
				bullets.add(new Bullet(x + 2, "simpleBullet", y + 75, isFasterBullets));
				bullets.add(new Bullet(x  + SHIP_WIDTH - 12, "simpleBullet", y + 75, isFasterBullets));
					}
				
				if(fireBullets == 3){
				bullets.add(new Bullet(x + SHIP_WIDTH / 2 - 5, "simpleBullet", y+100, isFasterBullets));
				bullets.add(new Bullet(x + 2, "simpleBullet", y + 75, isFasterBullets));
				bullets.add(new Bullet(x  + SHIP_WIDTH - 12, "simpleBullet", y + 75, isFasterBullets));
					}
				if(fireBullets == 4){
				bullets.add(new Bullet(x + SHIP_WIDTH / 2 - 20, "simpleBullet", y+100, isFasterBullets));
				bullets.add(new Bullet(x + SHIP_WIDTH / 2 + 20, "simpleBullet", y+100, isFasterBullets));
				bullets.add(new Bullet(x + 2, "leftFireBullet", y + 75, isFasterBullets));
				bullets.add(new Bullet(x  + SHIP_WIDTH - 12, "rightFireBullet", y + 75, isFasterBullets));
					}
				if(fireBullets == 5){
				bullets.add(new Bullet(x + SHIP_WIDTH / 2 - 20, "simpleBullet", y+100, isFasterBullets));
				bullets.add(new Bullet(x + SHIP_WIDTH / 2, "simpleBullet", y+140, isFasterBullets));
				bullets.add(new Bullet(x + SHIP_WIDTH / 2 + 20, "simpleBullet", y+100, isFasterBullets));
				bullets.add(new Bullet(x + 2, "leftFireBullet", y + 75, isFasterBullets));
				bullets.add(new Bullet(x  + SHIP_WIDTH - 12, "rightFireBullet", y + 75, isFasterBullets));
					}
			}
			if(isFreezingBullets){
				bullets.add(new Bullet(x + 2, "freezingBullet", y + 75, isFasterBullets));
				bullets.add(new Bullet(x  + SHIP_WIDTH - 12, "freezingBullet", y + 75, isFasterBullets));
				}
			}
		
	}
	
	
	private boolean isRight(){
		return Gdx.input.isKeyPressed(Keys.RIGHT) || (Gdx.input.isTouched() && game.cam.getInputInGameWorld().x >= 240);
	}

	
	private boolean isLeft(){
		return Gdx.input.isKeyPressed(Keys.LEFT) || (Gdx.input.isTouched() && game.cam.getInputInGameWorld().x < 240);
	}
	
	
	public void pouseMenu(){
	//if paused
		if(isPaused){
			
			//drawing
			game.batch.draw(bgPouse, 0, 0);
			
			if(isSoundMute)
				game.batch.draw(soundOff, 108, 432);
			
			if(!lg.isProgressBarCheck())
				game.batch.draw(soundOff, 315, 432);
			
			fontButt.draw(game.batch, resumeLayout, resumeX, resumeY);
			fontButt.draw(game.batch, mainmenuLayout, mainmenuX, mainmenuY);
			fontButt.draw(game.batch, exitLayout, exitX, exitY);
			
			float touchX = game.cam.getInputInGameWorld().x;
			//reversing the getY 
			float touchY = MainGame.HEIGHT - game.cam.getInputInGameWorld().y + mainmenuLayout.height;
			
			//IF THE RESUME BUTTON IS HOVERED
			if(touchX > resumeX && touchX < resumeX + resumeLayout.width){
				if(touchY > resumeY && touchY < resumeY + resumeLayout.height){
					if(Gdx.input.justTouched()){
						//confirm button sound
						if(sfx)
						confirmButton.play(0.6f);
					//resume button touched
					isPaused = false;
					isEscTouched = false;
					}
					else
					game.batch.draw(arrow, resumeX - arrow.getWidth() + 6, resumeY - arrow.getHeight() + 5);
				}
			}
			
			//If the main menu is hovered
			if(touchX > mainmenuX && touchX < mainmenuX + mainmenuLayout.width){
				if(touchY > mainmenuY && touchY < mainmenuY + mainmenuLayout.height){
					if(Gdx.input.justTouched()){
						//confirm button sound
						if(sfx)
						confirmButton.play(0.6f);
					//main menu button touched
					isPaused = false;
					isEscTouched = false;
					this.dispose();
					game.setScreen(new MainMenuScreen(game, true, handler, false));
					}
					else
					game.batch.draw(arrow, mainmenuX - arrow.getWidth() + 6, mainmenuY - arrow.getHeight() + 5);
				}
			}
			
			//If the exit is hovered
			if(touchX > exitX && touchX < exitX + exitLayout.width){
				if(touchY > exitY && touchY < exitY + exitLayout.height){
					if(Gdx.input.justTouched()){
					//exit button touched
					isPaused = false;
					isEscTouched = false;
					this.dispose();
					Gdx.app.exit();
					}
					else
					game.batch.draw(arrow, exitX - arrow.getWidth() + 6, exitY - arrow.getHeight() + 5);
				}
			}
			
			//if the mute sound button it touched
			if(touchX > 95 && touchX < 180){
				if(touchY > 435 && touchY < 500){
					if(Gdx.input.justTouched()){
						if(isSoundMute){
					isSoundMute = false;
					prefs.putBoolean("music", true); music = true;
					prefs.putBoolean("sfx", true); sfx = true;
					prefs.flush();
					musicLevel.play();
						}
						else if(!isSoundMute){
					confirmButton.play(0.6f);
					isSoundMute = true;
					prefs.putBoolean("music", false); music = false;
					prefs.putBoolean("sfx", false); sfx = false;
					prefs.flush();
					musicLevel.pause();
						}
					}
					
				}
			}
	
			//if the progress bar button is touched
			if(touchX > 315 && touchX < 400){
				if(touchY > 420 && touchY < 520){
					if(Gdx.input.justTouched()){
						if(sfx)
							confirmButton.play(0.6f);
						if(lg.isProgressBarCheck()){
					lg.setProgressBarCheck(false);
					prefs.putBoolean("progressBar", false);
					prefs.flush();
						}
						else if(!lg.isProgressBarCheck()){
					lg.setProgressBarCheck(true);
					prefs.putBoolean("progressBar", true);
					prefs.flush();
						}
					}
					
				}
			}
		}
	}
	
	
	public void endLevelMenu(){
	
		scoreColor = new Color(192, 192, 192, 0.8f);
		//layouts
		scoreLayout = new GlyphLayout(fontScore, " " + (int)tempScore, scoreColor, 0, Align.left, false);
		coinsLayout =  new GlyphLayout(fontCoinsBonus, "" + (int)tempCoinsCaught + "/" + coinsTotal, scoreColor, 0, Align.left, false);
		bonusLayout =  new GlyphLayout(fontCoinsBonus, "" + (int)tempBonus, scoreColor, 0, Align.left, false);
		
		
		if(level == 1){
			if(isGreeting){ //move it outside the if statements later
				game.batch.setColor(1, 1, 1, alpha_colorGreeting);
				if(score <= 5000)
					game.batch.draw(good, 120, GreetingY);
				if(score > 5000 && score <= 10000)
					game.batch.draw(nice, 140, GreetingY);
				if(score > 10000 && score <= 17000)
					game.batch.draw(superb, 90, GreetingY);
				if(score > 17000)
					game.batch.draw(excellent, 50, GreetingY);
			}
		}
		if(level == 2){
			if(isGreeting){
				game.batch.setColor(1, 1, 1, alpha_colorGreeting);
				if(score <= 6000)
					game.batch.draw(good, 120, GreetingY);
				if(score > 7000 && score <= 12000)
					game.batch.draw(nice, 140, GreetingY);
				if(score > 12000 && score <= 22000)
					game.batch.draw(superb, 90, GreetingY);
				if(score > 22000)
					game.batch.draw(excellent, 50, GreetingY);
			}
		}
		if(level == 3){
			if(isGreeting){
				game.batch.setColor(1, 1, 1, alpha_colorGreeting);
				if(score <= 5000)
					game.batch.draw(good, 120, GreetingY);
				if(score > 5000 && score <= 9000)
					game.batch.draw(nice, 140, GreetingY);
				if(score > 9000 && score <= 20000)
					game.batch.draw(superb, 90, GreetingY);
				if(score > 20000)
					game.batch.draw(excellent, 50, GreetingY);
			}
		}
		if(level == 4){
			if(isGreeting){
				game.batch.setColor(1, 1, 1, alpha_colorGreeting);
				if(score <= 7000)
					game.batch.draw(good, 120, GreetingY);
				if(score > 7000 && score <= 15000)
					game.batch.draw(nice, 140, GreetingY);
				if(score > 15000 && score <= 28000)
					game.batch.draw(superb, 90, GreetingY);
				if(score > 28000)
					game.batch.draw(excellent, 50, GreetingY);
			}
		}
		if(level == 5){
			if(isGreeting){
				game.batch.setColor(1, 1, 1, alpha_colorGreeting);
				if(score <= 10000)
					game.batch.draw(good, 120, GreetingY);
				if(score > 10000 && score <= 20000)
					game.batch.draw(nice, 140, GreetingY);
				if(score > 20000 && score <= 36000)
					game.batch.draw(superb, 90, GreetingY);
				if(score > 36000)
					game.batch.draw(excellent, 50, GreetingY);
			}
		}
		if(level == 6){
			if(isGreeting){
				game.batch.setColor(1, 1, 1, alpha_colorGreeting);
				if(score <= 20000)
					game.batch.draw(good, 120, GreetingY);
				if(score > 20000 && score <= 35000)
					game.batch.draw(nice, 140, GreetingY);
				if(score > 35000 && score <= 50000)
					game.batch.draw(superb, 90, GreetingY);
				if(score > 50000)
					game.batch.draw(excellent, 50, GreetingY);
			}
		}
		if(level == 7){
			if(isGreeting){
				game.batch.setColor(1, 1, 1, alpha_colorGreeting);
				if(score <= 20000)
					game.batch.draw(good, 120, GreetingY);
				if(score > 20000 && score <= 30000)
					game.batch.draw(nice, 140, GreetingY);
				if(score > 30000 && score <= 40000)
					game.batch.draw(superb, 90, GreetingY);
				if(score > 40000)
					game.batch.draw(excellent, 50, GreetingY);
			}
		}
		if(level == 8){
			if(isGreeting){
				game.batch.setColor(1, 1, 1, alpha_colorGreeting);
				if(score <= 20000)
					game.batch.draw(good, 120, GreetingY);
				if(score > 20000 && score <= 34000)
					game.batch.draw(nice, 140, GreetingY);
				if(score > 34000 && score <= 43000)
					game.batch.draw(superb, 90, GreetingY);
				if(score > 43000)
					game.batch.draw(excellent, 50, GreetingY);
			}
		}
		if(level == 9){
			if(isGreeting){
				game.batch.setColor(1, 1, 1, alpha_colorGreeting);
				if(score <= 22000)
					game.batch.draw(good, 120, GreetingY);
				if(score > 22000 && score <= 37000)
					game.batch.draw(nice, 140, GreetingY);
				if(score > 37000 && score <= 48000)
					game.batch.draw(superb, 90, GreetingY);
				if(score > 48000)
					game.batch.draw(excellent, 50, GreetingY);
			}
		}
		if(level == 10){
			if(isGreeting){
				game.batch.setColor(1, 1, 1, alpha_colorGreeting);
				if(score <= 15000)
					game.batch.draw(good, 120, GreetingY);
				if(score > 15000 && score <= 32000)
					game.batch.draw(nice, 140, GreetingY);
				if(score > 32000 && score <= 40000)
					game.batch.draw(superb, 90, GreetingY);
				if(score > 40000)
					game.batch.draw(excellent, 50, GreetingY);
			}
		}
		if(level == 11){
			if(isGreeting){
				game.batch.setColor(1, 1, 1, alpha_colorGreeting);
				if(score <= 25000)
					game.batch.draw(good, 120, GreetingY);
				if(score > 25000 && score <= 42000)
					game.batch.draw(nice, 140, GreetingY);
				if(score > 42000 && score <= 65000)
					game.batch.draw(superb, 90, GreetingY);
				if(score > 65000)
					game.batch.draw(excellent, 50, GreetingY);
			}
		}
		if(level == 12){
			if(isGreeting){
				game.batch.setColor(1, 1, 1, alpha_colorGreeting);
				if(score <= 5000 && lives < 2)
					game.batch.draw(good, 120, GreetingY);
				if(score > 5000 && lives < 2)
					game.batch.draw(nice, 140, GreetingY);
				if(lives == 2)
					game.batch.draw(superb, 90, GreetingY);
				if(lives == 3)
					game.batch.draw(excellent, 50, GreetingY);
			}
		}
		if(level == 13){
			if(isGreeting){
				game.batch.setColor(1, 1, 1, alpha_colorGreeting);
				if(score <= 25000)
					game.batch.draw(good, 120, GreetingY);
				if(score > 25000 && score <= 42000)
					game.batch.draw(nice, 140, GreetingY);
				if(score > 42000 && score <= 60000)
					game.batch.draw(superb, 90, GreetingY);
				if(score > 60000)
					game.batch.draw(excellent, 50, GreetingY);
			}
		}
		if(level == 14){
			if(isGreeting){
				game.batch.setColor(1, 1, 1, alpha_colorGreeting);
				if(score <= 65000)
					game.batch.draw(good, 120, GreetingY);
				if(score > 65000 && score <= 70000)
					game.batch.draw(nice, 140, GreetingY);
				if(score > 70000 && score <= 94000)
					game.batch.draw(superb, 90, GreetingY);
				if(score > 94000)
					game.batch.draw(excellent, 50, GreetingY);
			}
		}
		if(level == 15){
			if(isGreeting){
				game.batch.setColor(1, 1, 1, alpha_colorGreeting);
				if(score <= 45000)
					game.batch.draw(good, 120, GreetingY);
				if(score > 45000 && score <= 65000)
					game.batch.draw(nice, 140, GreetingY);
				if(score > 65000 && score <= 94000)
					game.batch.draw(superb, 90, GreetingY);
				if(score > 94000)
					game.batch.draw(excellent, 50, GreetingY);
			}
		}
		if(level == 16){
			if(isGreeting){
				game.batch.setColor(1, 1, 1, alpha_colorGreeting);
				if(score <= 55000)
					game.batch.draw(good, 120, GreetingY);
				if(score > 55000 && score <= 85000)
					game.batch.draw(nice, 140, GreetingY);
				if(score > 85000 && score <= 94000)
					game.batch.draw(superb, 90, GreetingY);
				if(score > 94000)
					game.batch.draw(excellent, 50, GreetingY);
			}
		}
		if(level == 17){
			if(isGreeting){
				game.batch.setColor(1, 1, 1, alpha_colorGreeting);
				if(score <= 65000)
					game.batch.draw(good, 120, GreetingY);
				if(score > 65000 && score <= 95000)
					game.batch.draw(nice, 140, GreetingY);
				if(score > 95000 && score <= 110000)
					game.batch.draw(superb, 90, GreetingY);
				if(score > 110000)
					game.batch.draw(excellent, 50, GreetingY);
			}
		}
		if(level == 18){
			if(isGreeting){
				game.batch.setColor(1, 1, 1, alpha_colorGreeting);
				if(score <= 35000)
					game.batch.draw(good, 120, GreetingY);
				if(score > 35000 && score <= 55000)
					game.batch.draw(nice, 140, GreetingY);
				if(score > 55000 && score <= 80000)
					game.batch.draw(superb, 90, GreetingY);
				if(score > 80000)
					game.batch.draw(excellent, 50, GreetingY);
			}
		}
		if(level == 19){
			if(isGreeting){
				game.batch.setColor(1, 1, 1, alpha_colorGreeting);
				if(score <= 35000)
					game.batch.draw(good, 120, GreetingY);
				if(score > 35000 && score <= 55000)
					game.batch.draw(nice, 140, GreetingY);
				if(score > 55000 && score <= 80000)
					game.batch.draw(superb, 90, GreetingY);
				if(score > 80000)
					game.batch.draw(excellent, 50, GreetingY);
			}
		}
		if(level == 20){
			if(isGreeting){
				game.batch.setColor(1, 1, 1, alpha_colorGreeting);
				if(score <= 45000)
					game.batch.draw(good, 120, GreetingY);
				if(score > 45000 && score <= 65000)
					game.batch.draw(nice, 140, GreetingY);
				if(score > 65000 && score <= 80000)
					game.batch.draw(superb, 90, GreetingY);
				if(score > 80000)
					game.batch.draw(excellent, 50, GreetingY);
			}
		}
		if(level == 21){
			if(isGreeting){
				game.batch.setColor(1, 1, 1, alpha_colorGreeting);
				if(score <= 40000)
					game.batch.draw(good, 120, GreetingY);
				if(score > 40000 && score <= 60000)
					game.batch.draw(nice, 140, GreetingY);
				if(score > 60000 && score <= 75000)
					game.batch.draw(superb, 90, GreetingY);
				if(score > 75000)
					game.batch.draw(excellent, 50, GreetingY);
			}
		}
		if(level == 36){
			if(isGreeting){
				game.batch.setColor(1, 1, 1, alpha_colorGreeting);
				if(score <= 70000)
					game.batch.draw(good, 120, GreetingY);
				if(score > 70000 && score <= 95000)
					game.batch.draw(nice, 140, GreetingY);
				if(score > 95000 && score <= 145000)
					game.batch.draw(superb, 90, GreetingY);
				if(score > 145000)
					game.batch.draw(excellent, 50, GreetingY);
			}
		}
		if(level == 23){
			if(isGreeting){
				game.batch.setColor(1, 1, 1, alpha_colorGreeting);
				if(score <= 65000)
					game.batch.draw(good, 120, GreetingY);
				if(score > 65000 && score <= 95000)
					game.batch.draw(nice, 140, GreetingY);
				if(score > 95000 && score <= 120000)
					game.batch.draw(superb, 90, GreetingY);
				if(score > 120000)
					game.batch.draw(excellent, 50, GreetingY);
			}
		}
		if(level == 22){
			if(isGreeting){
				game.batch.setColor(1, 1, 1, alpha_colorGreeting);
				if(score <= 17000)
					game.batch.draw(good, 120, GreetingY);
				if(score > 17000 && score <= 40000)
					game.batch.draw(nice, 140, GreetingY);
				if(score > 40000 && score <= 70000)
					game.batch.draw(superb, 90, GreetingY);
				if(score > 70000)
					game.batch.draw(excellent, 50, GreetingY);
			}
		}
		if(level == 25){
			if(isGreeting){
				game.batch.setColor(1, 1, 1, alpha_colorGreeting);
				if(score <= 25000)
					game.batch.draw(good, 120, GreetingY);
				if(score > 25000 && score <= 70000)
					game.batch.draw(nice, 140, GreetingY);
				if(score > 70000 && score <= 100000)
					game.batch.draw(superb, 90, GreetingY);
				if(score > 100000)
					game.batch.draw(excellent, 50, GreetingY);
			}
		}
		if(level == 26){
			if(isGreeting){
				game.batch.setColor(1, 1, 1, alpha_colorGreeting);
				if(score <= 55000)
					game.batch.draw(good, 120, GreetingY);
				if(score > 55000 && score <= 90000)
					game.batch.draw(nice, 140, GreetingY);
				if(score > 90000 && score <= 190000)
					game.batch.draw(superb, 90, GreetingY);
				if(score > 190000)
					game.batch.draw(excellent, 50, GreetingY);
			}
		}
		if(level == 27){
			if(isGreeting){
				game.batch.setColor(1, 1, 1, alpha_colorGreeting);
				if(score <= 45000)
					game.batch.draw(good, 120, GreetingY);
				if(score > 45000 && score <= 70000)
					game.batch.draw(nice, 140, GreetingY);
				if(score > 70000 && score <= 140000)
					game.batch.draw(superb, 90, GreetingY);
				if(score > 140000)
					game.batch.draw(excellent, 50, GreetingY);
			}
		}
		if(level == 28){
			if(isGreeting){
				game.batch.setColor(1, 1, 1, alpha_colorGreeting);
				if(score <= 35000)
					game.batch.draw(good, 120, GreetingY);
				if(score > 35000 && score <= 80000)
					game.batch.draw(nice, 140, GreetingY);
				if(score > 80000 && score <= 140000)
					game.batch.draw(superb, 90, GreetingY);
				if(score > 140000)
					game.batch.draw(excellent, 50, GreetingY);
			}
		}
		if(level == 29){
			if(isGreeting){
				game.batch.setColor(1, 1, 1, alpha_colorGreeting);
				if(score <= 35000)
					game.batch.draw(good, 120, GreetingY);
				if(score > 35000 && score <= 70000)
					game.batch.draw(nice, 140, GreetingY);
				if(score > 70000 && score <= 135000)
					game.batch.draw(superb, 90, GreetingY);
				if(score > 135000)
					game.batch.draw(excellent, 50, GreetingY);
			}
		}
		if(level == 30){
			if(isGreeting){
				game.batch.setColor(1, 1, 1, alpha_colorGreeting);
				if(score <= 70000)
					game.batch.draw(good, 120, GreetingY);
				if(score > 70000 && score <= 95000)
					game.batch.draw(nice, 140, GreetingY);
				if(score > 95000 && score <= 155000)
					game.batch.draw(superb, 90, GreetingY);
				if(score > 155000)
					game.batch.draw(excellent, 50, GreetingY);
			}
		}
		if(level == 31){
			if(isGreeting){
				game.batch.setColor(1, 1, 1, alpha_colorGreeting);
				if(score <= 30000)
					game.batch.draw(good, 120, GreetingY);
				if(score > 30000 && score <= 55000)
					game.batch.draw(nice, 140, GreetingY);
				if(score > 55000 && score <= 81000)
					game.batch.draw(superb, 90, GreetingY);
				if(score > 81000)
					game.batch.draw(excellent, 50, GreetingY);
			}
		}
		if(level == 32){
			if(isGreeting){
				game.batch.setColor(1, 1, 1, alpha_colorGreeting);
				if(score <= 50000)
					game.batch.draw(good, 120, GreetingY);
				if(score > 50000 && score <= 89000)
					game.batch.draw(nice, 140, GreetingY);
				if(score > 89000 && score <= 150000)
					game.batch.draw(superb, 90, GreetingY);
				if(score > 150000) 
					game.batch.draw(excellent, 50, GreetingY);
			}
		}
		if(level == 33){
			if(isGreeting){
				game.batch.setColor(1, 1, 1, alpha_colorGreeting);
				if(score <= 19000)
					game.batch.draw(good, 120, GreetingY);
				if(score > 19000 && score <= 55000)
					game.batch.draw(nice, 140, GreetingY);
				if(score > 55000 && score <= 90000)
					game.batch.draw(superb, 90, GreetingY);
				if(score > 90000)
					game.batch.draw(excellent, 50, GreetingY);
			}
		}
		if(level == 34){
			if(isGreeting){
				game.batch.setColor(1, 1, 1, alpha_colorGreeting);
				if(score <= 39000)
					game.batch.draw(good, 120, GreetingY);
				if(score > 39000 && score <= 75000)
					game.batch.draw(nice, 140, GreetingY);
				if(score > 75000 && score <= 140000)
					game.batch.draw(superb, 90, GreetingY);
				if(score > 140000)
					game.batch.draw(excellent, 50, GreetingY);
			}
		}
		if(level == 35){
			if(isGreeting){
				game.batch.setColor(1, 1, 1, alpha_colorGreeting);
				if(score <= 39000)
					game.batch.draw(good, 120, GreetingY);
				if(score > 39000 && score <= 85000)
					game.batch.draw(nice, 140, GreetingY);
				if(score > 85000 && score <= 160000)
					game.batch.draw(superb, 90, GreetingY);
				if(score > 160000)
					game.batch.draw(excellent, 50, GreetingY);
			}
		}
		
		if(level == 24){
			if(isGreeting){
				game.batch.setColor(1, 1, 1, alpha_colorGreeting);
				if(score <= 10000)
					game.batch.draw(good, 120, GreetingY);
				if(score > 10000 && score <= 20000)
					game.batch.draw(nice, 140, GreetingY);
				if(score > 20000 && score <= 30000)
					game.batch.draw(superb, 90, GreetingY);
				if(score > 30000)
					game.batch.draw(excellent, 50, GreetingY);
			}
		}
		
		game.batch.setColor(Color.WHITE);
		
		if(isEndLevel){
		game.batch.draw(bgEndLevel, 0, 0);
		fontScore.draw(game.batch, scoreLayout, 110 , 400);
		fontCoinsBonus.draw(game.batch, coinsLayout, 100, 250);
		fontCoinsBonus.draw(game.batch, bonusLayout, 275, 250);
		
		if(star1){
		game.batch.draw(scoreStar, 70, 500);
		
		if(!animStar.isAnimationFinished(star1Time)){
		game.batch.draw(animStar.getKeyFrame(star1Time), 20, 460);
		game.batch.draw(animStar.getKeyFrame(star1Time), 22, 480);
		game.batch.draw(animStar.getKeyFrame(star1Time), 30, 490);
		game.batch.draw(animStar.getKeyFrame(star1Time), 72, 440);
		game.batch.draw(animStar.getKeyFrame(star1Time), 80, 500);
		}
		
		if(!fireworks1Start){
			if(sfx)
			fireworks.play(0.8f);
			fireworks1Start = true;
		}
		
		}
		if(star2){
		game.batch.draw(scoreStar, 70 + (scoreStar.getWidth() + 35), 500);
		if(!animStar.isAnimationFinished(star2Time)){
		game.batch.draw(animStar.getKeyFrame(star2Time), 20 + (scoreStar.getWidth() + 35), 460);
		game.batch.draw(animStar.getKeyFrame(star2Time), 35 + (scoreStar.getWidth() + 35), 440);
		game.batch.draw(animStar.getKeyFrame(star2Time), 50 + (scoreStar.getWidth() + 35), 480);
		game.batch.draw(animStar.getKeyFrame(star2Time), 45 + (scoreStar.getWidth() + 35), 420);
		game.batch.draw(animStar.getKeyFrame(star2Time), 60 + (scoreStar.getWidth() + 35), 500);
		game.batch.draw(animStar.getKeyFrame(star2Time), 75 + (scoreStar.getWidth() + 35), 490);
		}
		if(!fireworks2Start){
			if(sfx)
			fireworks.play(0.8f);
			fireworks2Start = true;
		}
		
		}
		if(star3){
		game.batch.draw(scoreStar, 70 + (scoreStar.getWidth() + 35)*2, 500);
		if(!animStar.isAnimationFinished(star3Time)){
		game.batch.draw(animStar.getKeyFrame(star3Time), 25 + (scoreStar.getWidth() + 35)*2, 460);
		game.batch.draw(animStar.getKeyFrame(star3Time), 35 + (scoreStar.getWidth() + 35)*2, 440);
		game.batch.draw(animStar.getKeyFrame(star3Time), 55 + (scoreStar.getWidth() + 35)*2, 430);
		game.batch.draw(animStar.getKeyFrame(star3Time), 45 + (scoreStar.getWidth() + 35)*2, 480);
		game.batch.draw(animStar.getKeyFrame(star3Time), 65 + (scoreStar.getWidth() + 35)*2, 420);
		game.batch.draw(animStar.getKeyFrame(star3Time), 75 + (scoreStar.getWidth() + 35)*2, 490);
		game.batch.draw(animStar.getKeyFrame(star3Time), 80 + (scoreStar.getWidth() + 35)*2, 500);
		}
		if(!fireworks3Start){
			if(sfx)
			fireworks.play(0.8f);
			fireworks3Start = true;
		}
		
	
		
		}
		
		if(!isScoreFinished){
			game.batch.draw(nextButtonBlack, MainGame.WIDTH / 2 - nextButtonBlack.getWidth() / 2, 50);
			
		}
		else{
			//if the score is finished loading 
			scoreCounter.stop();
			
			//if there is a new highscore draw the texture
			if(isNewHighscore){
			game.batch.draw(newHighscore, scoreLayout.width + 40, 380);
			}
			
			game.batch.draw(nextButtonBlue, MainGame.WIDTH / 2 - nextButtonBlack.getWidth() / 2, 50);
			float touchX = game.cam.getInputInGameWorld().x;
			//reversing the getY 
			float touchY = MainGame.HEIGHT - game.cam.getInputInGameWorld().y;
			
			//touching the next button
			if(touchX >= MainGame.WIDTH / 2 - nextButtonBlack.getWidth() / 2 && touchX <= MainGame.WIDTH / 2 - nextButtonBlack.getWidth() / 2 + nextButtonBlack.getWidth()){
				if(touchY >= 50 && touchY <= 50+nextButtonBlack.getHeight()){
					if(Gdx.input.justTouched()){
						
						//confirm button sound
						if(sfx)
						confirmButton.play(0.6f);
						if(level != 36)
						game.setScreen(new SelectLevelScreen(game, level, stars, score, false, handler, true));
						if(level == 36)
						game.setScreen(new Credits(game, handler, true));
						musicLevelWin.stop();
						this.dispose();
						
					}
				}
			}
		}
		}
	}
	
	
	public void resize(int width, int height) {
		
	}

	
	public void pause() {
		
	}

	
	public void resume() {
		
	}

	
	public void hide() {
		
	}
	

	public int getScore() {
		return score;
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
	
	
	public boolean isStartEndLevelTimer() {
		return startEndLevelTimer;
	}

	public void setStartEndLevelTimer(boolean startEndLevelTimer) {
		this.startEndLevelTimer = startEndLevelTimer;
	}

	public float getEndLevelTimer() {
		return endLevelTimer;
	}

	public void setEndLevelTimer(float endLevelTimer) {
		this.endLevelTimer = endLevelTimer;
	}

	public boolean isEndLevel() {
		return endLevel;
	}
	
	public boolean isisEndLevel(){
		return isEndLevel;
	}
	
	public void setisEndLevel(boolean isEndLevel){
		this.isEndLevel = isEndLevel;
	}

	

	public ArrayList<SimpleEnemy> getSimpleEnemies() {
		return simpleEnemies;
	}

	public void setSimpleEnemies(ArrayList<SimpleEnemy> simpleEnemies) {
		this.simpleEnemies = simpleEnemies;
	}

	public ArrayList<Asteroid> getAsteroids() {
		return asteroids;
	}

	public void setAsteroids(ArrayList<Asteroid> asteroids) {
		this.asteroids = asteroids;
	}
	
	

	public ArrayList<ShootingBulletsEnemy> getShootingBulletsEnemies() {
		return shootingBulletsEnemies;
	}

	public void setShootingBulletsEnemies(ArrayList<ShootingBulletsEnemy> shootingBulletsEnemies) {
		this.shootingBulletsEnemies = shootingBulletsEnemies;
	}

	public void setFreezingBullets(boolean isFreezingBullets) {
		this.isFreezingBullets = isFreezingBullets;
	}

	public ArrayList<ShootingEnemy> getShootingEnemies() {
		return shootingEnemies;
	}

	public void setShootingEnemies(ArrayList<ShootingEnemy> shootingEnemies) {
		this.shootingEnemies = shootingEnemies;
	}
	
	
	
	public ArrayList<ShootingFreezingBulletsEnemy> getShootingFreezingBulletsEnemies() {
		return shootingFreezingBulletsEnemies;
	}

	public void setShootingFreezingBulletsEnemies(ArrayList<ShootingFreezingBulletsEnemy> shootingFreezingBulletsEnemies) {
		this.shootingFreezingBulletsEnemies = shootingFreezingBulletsEnemies;
	}

	
	public ArrayList<BossEnemy> getBossEnemies() {
		return bossEnemies;
	}

	public void setBossEnemies(ArrayList<BossEnemy> bossEnemies) {
		this.bossEnemies = bossEnemies;
	}

	public int playerModesNumber(){
		return playerModes.size();
	}
	
	public int playerModesCounter(){
		return playerModesCounter;
	}
	
	
	

	public boolean isSlowerPlayer() {
		return isSlowerPlayer;
	}

	public void setSlowerPlayer(boolean isSlowerPlayer) {
		this.isSlowerPlayer = isSlowerPlayer;
	}

	public float getSPEED() {
		return SPEED;
	}

	public void setSPEED(float sPEED) {
		SPEED = sPEED;
	}

	public int getFireBullets() {
		return fireBullets;
	}

	public void setFireBullets(int fireBullets) {
		this.fireBullets = fireBullets;
	}

	public int getLucky() {
		return lucky;
	}

	public void setLucky(int lucky) {
		this.lucky = lucky;
	}
	
	
	public boolean isFasterBullets() {
		return isFasterBullets;
	}

	public void setFasterBullets(boolean isFasterBullets) {
		this.isFasterBullets = isFasterBullets;
	}
	
	

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public void dispose() {
		fontText.dispose();
		blank.dispose();
		bg.dispose();
		healthbar.dispose();
		healthbarBack.dispose();
		land.dispose();
		bgPouse.dispose();
		arrow.dispose();
		arrowMoveBlue.dispose();
		arrowMoveWhite.dispose();
		bgEndLevel.dispose();
		scoreStar.dispose();
		nextButtonBlack.dispose();
		nextButtonBlue.dispose();
		endLevelGreetings.dispose();
		newHighscore.dispose();
		
		//sounds
		coin.dispose();
		explosionPlayerSound.dispose();
		explosionSound.dispose();
		laserPlayer.dispose();
		shield.dispose();
		bulletsUpdate.dispose();
		fireworks.dispose();
		playerMode.dispose();
		hit.dispose();
		tools.dispose();
		scoreCounter.dispose();
		grats.dispose();
		freeze.dispose();
		confirmButton.dispose();
		
		//music
		musicLevel.dispose();
		musicLevelWin.dispose();

		
		//disposing entities used in this class
	
		for(Collectibles collectible : collectibles){
			collectible.dispose();
		}
		
		for(PlayerModes playerMode : playerModes){
			playerMode.dispose();
		}
		
		for(Explosion explosion: explosions){
			explosion.dispose();
		}
		
		for(Bullet bullet: bullets){
			bullet.dispose();
		}
		
		for(ShootingEnemy shootingEnemy: shootingEnemies){
			shootingEnemy.dispose();
		}
		
		for(SimpleEnemy simpleEnemy: simpleEnemies){
			simpleEnemy.dispose();
		}
		
		for(Asteroid asteroid: asteroids){
			asteroid.dispose();
		}
		
		for(ShootingBulletsEnemy shootingBulletsEnemy: shootingBulletsEnemies){
			shootingBulletsEnemy.dispose();
		}
		
		for(ShootingFreezingBulletsEnemy shootingFreezingBulletsEnemy: shootingFreezingBulletsEnemies){
			shootingFreezingBulletsEnemy.dispose();
		}
		
		for(Shield shield: shields){
			shield.dispose();
		}
		
		for(BossEnemy bossEnemy : bossEnemies){
			bossEnemy.dispose();
		}

		System.out.println("Game Screen Disposed.");
		
		
	}



}