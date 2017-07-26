package com.kiril.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kiril.main.game.MainGame;
import com.kiril.tools.AdHandler;

public class SelectLevelScreen implements Screen{

	MainGame game;
	Preferences prefs;
	
	boolean music, sfx, replay, progressBar;
	
	int level, highlevel, stars, score, highscore;
	int page;
	boolean isBeforeTheLevel;
	
	
	Music musicDrums;
	Sound confirmButton, negativeButton;
	
	//background
	Texture bg;
	float bgY = 0;
	float bgvelY = 0.5f;
	
	//buttons
	Texture selectLevel, selectLevel2, selectLevel3;

	Texture locked;
	Texture lockedBoss;
	
	Texture menuButtons;
	TextureRegion backButtonInactive, backButtonActive;
	TextureRegion nextButtonInactive, nextButtonActive;
	
	//arrow
	Texture arrow;
	
	//blank texture for the gold rect
	Texture blank;
	
	//buttons parameters
	int buttWidth = 64, buttHeight = 64;
	
	float level1X = 113, level1Y = MainGame.HEIGHT - 224;
	float level2X = 210, level2Y = MainGame.HEIGHT - 224;
	float level3X = 308, level3Y = MainGame.HEIGHT - 224;
	float level4X = 113, level4Y = MainGame.HEIGHT - 320;
	float level5X = 210, level5Y = MainGame.HEIGHT - 320;
	float level6X = 308, level6Y = MainGame.HEIGHT - 320;
	float level7X = 113, level7Y = MainGame.HEIGHT - 414;
	float level8X = 210, level8Y = MainGame.HEIGHT - 414;
	float level9X = 308, level9Y = MainGame.HEIGHT - 414;
	float level10X = 113, level10Y = MainGame.HEIGHT - 509;
	float level11X = 210, level11Y = MainGame.HEIGHT - 509;
	float level12X = 308, level12Y = MainGame.HEIGHT - 509;


	//ads
	AdHandler handler;
	boolean toggle;

	//level score stars
	int levelStars[];
	
	//timers for button clicking
	float buttonClickTimer;
	boolean startButtonClickTimer;
	float buttonClickTime;
	boolean canClick;
	
	public SelectLevelScreen(MainGame game, int level, int stars, int score, boolean isBeforeTheLevel, AdHandler handler, boolean toggle){
		this.game = game; 
		this.level = level;
		this.stars = stars;
		this.score = score;
		this.isBeforeTheLevel = isBeforeTheLevel;
		this.handler = handler;
		this.toggle = toggle;

		
		//catch the back button on android
		Gdx.input.setCatchBackKey(true);
		
		//loading data from save file
		prefs = Gdx.app.getPreferences("spaceraiders");
		music = prefs.getBoolean("music", true);
		sfx = prefs.getBoolean("sfx", true);
		replay = prefs.getBoolean("replay", true);
		progressBar = prefs.getBoolean("progressBar", true);
		
		musicDrums = Gdx.audio.newMusic(Gdx.files.internal("audio/music/drums.ogg"));
		musicDrums.setVolume(0.9f);
		musicDrums.setLooping(true);
		if(music)
		musicDrums.play();
		
		confirmButton = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/confirmButton.ogg"));
		negativeButton = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/negativeButton.ogg"));
		
		//timer for the buttons click
		buttonClickTimer = 0.2f;
		startButtonClickTimer = true;
		buttonClickTime = 0;
		canClick = false;
		
		bg = new Texture("background.png");
		selectLevel = new Texture("selectlevel.png");
		selectLevel2 = new Texture("selectlevel2.png");
		selectLevel3 = new Texture("selectlevel3.png");
		blank = new Texture("blank.png");
		arrow = new Texture("buttons/arrow.png");
		locked = new Texture("lockedLevel70x70.png");
		lockedBoss = new Texture("lockedLevelBoss.png");
		
		menuButtons = new Texture("menuButtons.png");
		backButtonInactive = new TextureRegion(menuButtons, 300*4, 140, 60, 60);
		backButtonActive = new TextureRegion(menuButtons, 300*5, 140, 60, 60);
		nextButtonInactive = new TextureRegion(menuButtons, 300*4, 140, 60, 60);
		nextButtonInactive.flip(true, false);
		nextButtonActive = new TextureRegion(menuButtons, 300*5, 140, 60, 60);
		nextButtonActive.flip(true, false);
		
		//initialize the levelStars array
		levelStars = new int[36];
		
		//show ads
		if(MainGame.IS_MOBILE)
		handler.showAds(toggle);

		//loading data from save file
		Preferences prefs = Gdx.app.getPreferences("spaceraiders");
		this.highlevel = prefs.getInteger("highlevel", 1);
		this.highscore = prefs.getInteger("highscore", 0);
		this.levelStars[0] = prefs.getInteger("level1stars", 0);
		this.levelStars[1] = prefs.getInteger("level2stars", 0);
		this.levelStars[2] = prefs.getInteger("level3stars", 0);
		this.levelStars[3] = prefs.getInteger("level4stars", 0);
		this.levelStars[4] = prefs.getInteger("level5stars", 0);
		this.levelStars[5] = prefs.getInteger("level6stars", 0);
		this.levelStars[6] = prefs.getInteger("level7stars", 0);
		this.levelStars[7] = prefs.getInteger("level8stars", 0);
		this.levelStars[8] = prefs.getInteger("level9stars", 0);
		this.levelStars[9] = prefs.getInteger("level10stars", 0);
		this.levelStars[10] = prefs.getInteger("level11stars", 0);
		this.levelStars[11] = prefs.getInteger("level12stars", 0);
		this.levelStars[12] = prefs.getInteger("level13stars", 0);
		this.levelStars[13] = prefs.getInteger("level14stars", 0);
		this.levelStars[14] = prefs.getInteger("level15stars", 0);
		this.levelStars[15] = prefs.getInteger("level16stars", 0);
		this.levelStars[16] = prefs.getInteger("level17stars", 0);
		this.levelStars[17] = prefs.getInteger("level18stars", 0);
		this.levelStars[18] = prefs.getInteger("level19stars", 0);
		this.levelStars[19] = prefs.getInteger("level20stars", 0);
		this.levelStars[20] = prefs.getInteger("level21stars", 0);
		this.levelStars[21] = prefs.getInteger("level22stars", 0);
		this.levelStars[22] = prefs.getInteger("level23stars", 0);
		this.levelStars[23] = prefs.getInteger("level24stars", 0);
		this.levelStars[24] = prefs.getInteger("level25stars", 0);
		this.levelStars[25] = prefs.getInteger("level26stars", 0);
		this.levelStars[26] = prefs.getInteger("level27stars", 0);
		this.levelStars[27] = prefs.getInteger("level28stars", 0);
		this.levelStars[28] = prefs.getInteger("level29stars", 0);
		this.levelStars[29] = prefs.getInteger("level30stars", 0);
		this.levelStars[30] = prefs.getInteger("level31stars", 0);
		this.levelStars[31] = prefs.getInteger("level32stars", 0);
		this.levelStars[32] = prefs.getInteger("level33stars", 0);
		this.levelStars[33] = prefs.getInteger("level34stars", 0);
		this.levelStars[34] = prefs.getInteger("level35stars", 0);
		this.levelStars[35] = prefs.getInteger("level36stars", 0);
		
		
		
		//check if score beats highscore
				if(score > highscore){
					prefs.putInteger("highscore", score);
					prefs.flush(); //saves the file
				}
		
		
		//if this screen is called from the game screen (when a level is passed)
		if(!isBeforeTheLevel){
			//setting the highlevel
			if(level >= highlevel){
				highlevel++;
		prefs.putInteger("highlevel", highlevel);
		prefs.flush(); //saves the file
			}
			
			//setting the stars
			if(level == 1){
				if(stars >= levelStars[0]){
					levelStars[0] = stars;
					prefs.putInteger("level1stars", levelStars[0]);
					prefs.flush(); 
				}
			}
			if(level == 2){
				if(stars >= levelStars[1]){
					levelStars[1] = stars;
					prefs.putInteger("level2stars", levelStars[1]);
					prefs.flush(); 
				}
			}
			if(level == 3){
				if(stars >= levelStars[2]){
					levelStars[2] = stars;
					prefs.putInteger("level3stars", levelStars[2]);
					prefs.flush(); 
				}
			}
			if(level == 4){
				if(stars >= levelStars[3]){
					levelStars[3] = stars;
					prefs.putInteger("level4stars", levelStars[3]);
					prefs.flush(); 
				}
			}
			if(level == 5){
				if(stars >= levelStars[4]){
					levelStars[4] = stars;
					prefs.putInteger("level5stars", levelStars[4]);
					prefs.flush(); 
				}
			}
			if(level == 6){
				if(stars >= levelStars[5]){
					levelStars[5] = stars;
					prefs.putInteger("level6stars", levelStars[5]);
					prefs.flush(); 
				}
			}
			if(level == 7){
				if(stars >= levelStars[6]){
					levelStars[6] = stars;
					prefs.putInteger("level7stars", levelStars[6]);
					prefs.flush(); 
				}
			}
			if(level == 8){
				if(stars >= levelStars[7]){
					levelStars[7] = stars;
					prefs.putInteger("level8stars", levelStars[7]);
					prefs.flush(); 
				}
			}
			if(level == 9){
				if(stars >= levelStars[8]){
					levelStars[8] = stars;
					prefs.putInteger("level9stars", levelStars[8]);
					prefs.flush(); 
				}
			}
			if(level == 10){
				if(stars >= levelStars[9]){
					levelStars[9] = stars;
					prefs.putInteger("level10stars", levelStars[9]);
					prefs.flush(); 
				}
			}
			if(level == 11){
				if(stars >= levelStars[10]){
					levelStars[10] = stars;
					prefs.putInteger("level11stars", levelStars[10]);
					prefs.flush(); 
				}
			}
			if(level == 12){
				if(stars >= levelStars[11]){
					levelStars[11] = stars;
					prefs.putInteger("level12stars", levelStars[11]);
					prefs.flush(); 
				}
			}
			if(level == 13){
				if(stars >= levelStars[12]){
					levelStars[12] = stars;
					prefs.putInteger("level13stars", levelStars[12]);
					prefs.flush(); 
				}
			}
			if(level == 14){
				if(stars >= levelStars[13]){
					levelStars[13] = stars;
					prefs.putInteger("level14stars", levelStars[13]);
					prefs.flush(); 
				}
			}
			if(level == 15){
				if(stars >= levelStars[14]){
					levelStars[14] = stars;
					prefs.putInteger("level15stars", levelStars[14]);
					prefs.flush(); 
				}
			}
			if(level == 16){
				if(stars >= levelStars[15]){
					levelStars[15] = stars;
					prefs.putInteger("level16stars", levelStars[15]);
					prefs.flush(); 
				}
			}
			if(level == 17){
				if(stars >= levelStars[16]){
					levelStars[16] = stars;
					prefs.putInteger("level17stars", levelStars[16]);
					prefs.flush(); 
				}
			}
			if(level == 18){
				if(stars >= levelStars[17]){
					levelStars[17] = stars;
					prefs.putInteger("level18stars", levelStars[17]);
					prefs.flush(); 
				}
			}
			if(level == 19){
				if(stars >= levelStars[18]){
					levelStars[18] = stars;
					prefs.putInteger("level19stars", levelStars[18]);
					prefs.flush(); 
				}
			}
			if(level == 20){
				if(stars >= levelStars[19]){
					levelStars[19] = stars;
					prefs.putInteger("level20stars", levelStars[19]);
					prefs.flush(); 
				}
			}
			if(level == 21){
				if(stars >= levelStars[20]){
					levelStars[20] = stars;
					prefs.putInteger("level21stars", levelStars[20]);
					prefs.flush(); 
				}
			}
			if(level == 22){
				if(stars >= levelStars[21]){
					levelStars[21] = stars;
					prefs.putInteger("level22stars", levelStars[21]);
					prefs.flush(); 
				}
			}
			if(level == 23){
				if(stars >= levelStars[22]){
					levelStars[22] = stars;
					prefs.putInteger("level23stars", levelStars[22]);
					prefs.flush(); 
				}
			}
			if(level == 24){
				if(stars >= levelStars[23]){
					levelStars[23] = stars;
					prefs.putInteger("level24stars", levelStars[23]);
					prefs.flush(); 
				}
			}
			if(level == 25){
				if(stars >= levelStars[24]){
					levelStars[24] = stars;
					prefs.putInteger("level25stars", levelStars[24]);
					prefs.flush(); 
				}
			}
			if(level == 26){
				if(stars >= levelStars[25]){
					levelStars[25] = stars;
					prefs.putInteger("level26stars", levelStars[25]);
					prefs.flush(); 
				}
			}
			if(level == 27){
				if(stars >= levelStars[26]){
					levelStars[26] = stars;
					prefs.putInteger("level27stars", levelStars[26]);
					prefs.flush(); 
				}
			}
			if(level == 28){
				if(stars >= levelStars[27]){
					levelStars[27] = stars;
					prefs.putInteger("level28stars", levelStars[27]);
					prefs.flush(); 
				}
			}
			if(level == 29){
				if(stars >= levelStars[28]){
					levelStars[28] = stars;
					prefs.putInteger("level29stars", levelStars[28]);
					prefs.flush(); 
				}
			}
			if(level == 30){
				if(stars >= levelStars[29]){
					levelStars[29] = stars;
					prefs.putInteger("level30stars", levelStars[29]);
					prefs.flush(); 
				}
			}
			if(level == 31){
				if(stars >= levelStars[30]){
					levelStars[30] = stars;
					prefs.putInteger("level31stars", levelStars[30]);
					prefs.flush(); 
				}
			}
			if(level == 32){
				if(stars >= levelStars[31]){
					levelStars[31] = stars;
					prefs.putInteger("level32stars", levelStars[31]);
					prefs.flush(); 
				}
			}
			if(level == 33){
				if(stars >= levelStars[32]){
					levelStars[32] = stars;
					prefs.putInteger("level33stars", levelStars[32]);
					prefs.flush(); 
				}
			}
			if(level == 34){
				if(stars >= levelStars[33]){
					levelStars[33] = stars;
					prefs.putInteger("level34stars", levelStars[33]);
					prefs.flush(); 
				}
			}
			if(level == 35){
				if(stars >= levelStars[34]){
					levelStars[34] = stars;
					prefs.putInteger("level35stars", levelStars[34]);
					prefs.flush(); 
				}
			}
			if(level == 36){
				if(stars >= levelStars[35]){
					levelStars[35] = stars;
					prefs.putInteger("level36stars", levelStars[35]);
					prefs.flush(); 
				}
			}
			
		
		}
		
		//check for the highlevel, so the page is correct
		if(highlevel <= 12)
		page = 1;
		if(highlevel > 12)
		page = 2;
		if(highlevel > 24)
		page = 3;
		
	
	}
	
	public void update(float delta){
		
		buttonClickTimer(delta);
		
		//background scrolling
		backgroundScrolling();
		
		
	}

	@Override
	public void render(float delta) {
		
		//update method
		update(delta);
		
		game.batch.begin();
		
		//background
		game.batch.draw(bg, 0, bgY);
		game.batch.draw(bg, 0, bgY + MainGame.HEIGHT);
		
		
		game.batch.setColor(Color.GOLD);
		
		if(page == 1){
		game.batch.draw(blank, level1X + 2, level1Y + 2, levelStars[0] * 20 , 20);
		game.batch.draw(blank, level2X + 2, level2Y + 2, levelStars[1] * 20 , 20);
		game.batch.draw(blank, level3X + 2, level3Y + 2, levelStars[2] * 20 , 20);
		game.batch.draw(blank, level4X + 2, level4Y + 2, levelStars[3] * 20 , 20);
		game.batch.draw(blank, level5X + 2, level5Y + 2, levelStars[4] * 20 , 20);
		game.batch.draw(blank, level6X + 2, level6Y + 2, levelStars[5] * 20 , 20);
		game.batch.draw(blank, level7X + 2, level7Y + 2, levelStars[6] * 20 , 20);
		game.batch.draw(blank, level8X + 2, level8Y + 2, levelStars[7] * 20 , 20);
		game.batch.draw(blank, level9X + 2, level9Y + 2, levelStars[8] * 20 , 20);
		game.batch.draw(blank, level10X + 2, level10Y + 2, levelStars[9] * 20 , 20);
		game.batch.draw(blank, level11X + 2, level11Y + 2, levelStars[10] * 20 , 20);
		game.batch.draw(blank, level12X + 2, level12Y + 2, levelStars[11] * 20 , 20);
		}
		if(page == 2){
		game.batch.draw(blank, level1X + 2, level1Y + 2, levelStars[12] * 20 , 20);
		game.batch.draw(blank, level2X + 2, level2Y + 2, levelStars[13] * 20 , 20);
		game.batch.draw(blank, level3X + 2, level3Y + 2, levelStars[14] * 20 , 20);
		game.batch.draw(blank, level4X + 2, level4Y + 2, levelStars[15] * 20 , 20);
		game.batch.draw(blank, level5X + 2, level5Y + 2, levelStars[16] * 20 , 20);
		game.batch.draw(blank, level6X + 2, level6Y + 2, levelStars[17] * 20 , 20);
		game.batch.draw(blank, level7X + 2, level7Y + 2, levelStars[18] * 20 , 20);
		game.batch.draw(blank, level8X + 2, level8Y + 2, levelStars[19] * 20 , 20);
		game.batch.draw(blank, level9X + 2, level9Y + 2, levelStars[20] * 20 , 20);
		game.batch.draw(blank, level10X + 2, level10Y + 2, levelStars[21] * 20 , 20);
		game.batch.draw(blank, level11X + 2, level11Y + 2, levelStars[22] * 20 , 20);
		game.batch.draw(blank, level12X + 2, level12Y + 2, levelStars[23] * 20 , 20);	
		}
		if(page == 3){
		game.batch.draw(blank, level1X + 2, level1Y + 2, levelStars[24] * 20 , 20);
		game.batch.draw(blank, level2X + 2, level2Y + 2, levelStars[25] * 20 , 20);
		game.batch.draw(blank, level3X + 2, level3Y + 2, levelStars[26] * 20 , 20);
		game.batch.draw(blank, level4X + 2, level4Y + 2, levelStars[27] * 20 , 20);
		game.batch.draw(blank, level5X + 2, level5Y + 2, levelStars[28] * 20 , 20);
		game.batch.draw(blank, level6X + 2, level6Y + 2, levelStars[29] * 20 , 20);
		game.batch.draw(blank, level7X + 2, level7Y + 2, levelStars[30] * 20 , 20);
		game.batch.draw(blank, level8X + 2, level8Y + 2, levelStars[31] * 20 , 20);
		game.batch.draw(blank, level9X + 2, level9Y + 2, levelStars[32] * 20 , 20);
		game.batch.draw(blank, level10X + 2, level10Y + 2, levelStars[33] * 20 , 20);
		game.batch.draw(blank, level11X + 2, level11Y + 2, levelStars[34] * 20 , 20);
		game.batch.draw(blank, level12X + 2, level12Y + 2, levelStars[35] * 20 , 20);	
		}
		game.batch.setColor(Color.WHITE);
		
		if(page == 1)
		game.batch.draw(selectLevel, 0, 0);
		if(page == 2)
		game.batch.draw(selectLevel2, 0, 0);
		if(page == 3)
		game.batch.draw(selectLevel3, 0, 0);
		
		lockLevels();
		
		
		//buttons test for click is here because we are drawing in it
		buttonsTouched();
		
		game.batch.end();

	}
	
	public void lockLevels(){
		switch(highlevel){
		case 1:
			if(page == 1){
			game.batch.draw(locked, level2X , level2Y);
			game.batch.draw(locked, level3X , level3Y);
			game.batch.draw(locked, level4X , level4Y);
			game.batch.draw(locked, level5X , level5Y);
			game.batch.draw(locked, level6X , level6Y);
			game.batch.draw(locked, level7X , level7Y);
			game.batch.draw(locked, level8X , level8Y);
			game.batch.draw(locked, level9X , level9Y);
			game.batch.draw(locked, level10X , level10Y);
			game.batch.draw(locked, level11X , level11Y);
			game.batch.draw(lockedBoss, level12X , level12Y);
			}
			if(page == 2){
			game.batch.draw(locked, level1X , level1Y);
			game.batch.draw(locked, level2X , level2Y);
			game.batch.draw(locked, level3X , level3Y);
			game.batch.draw(locked, level4X , level4Y);
			game.batch.draw(locked, level5X , level5Y);
			game.batch.draw(locked, level6X , level6Y);
			game.batch.draw(locked, level7X , level7Y);
			game.batch.draw(locked, level8X , level8Y);
			game.batch.draw(locked, level9X , level9Y);
			game.batch.draw(locked, level10X , level10Y);
			game.batch.draw(locked, level11X , level11Y);
			game.batch.draw(lockedBoss, level12X , level12Y);
			}
			if(page == 3){
			game.batch.draw(locked, level1X , level1Y);
			game.batch.draw(locked, level2X , level2Y);
			game.batch.draw(locked, level3X , level3Y);
			game.batch.draw(locked, level4X , level4Y);
			game.batch.draw(locked, level5X , level5Y);
			game.batch.draw(locked, level6X , level6Y);
			game.batch.draw(locked, level7X , level7Y);
			game.batch.draw(locked, level8X , level8Y);
			game.batch.draw(locked, level9X , level9Y);
			game.batch.draw(locked, level10X , level10Y);
			game.batch.draw(locked, level11X , level11Y);
			game.batch.draw(locked, level12X , level12Y);
			}
			break;
		case 2:
			if(page == 1){
			game.batch.draw(locked, level3X , level3Y);
			game.batch.draw(locked, level4X , level4Y);
			game.batch.draw(locked, level5X , level5Y);
			game.batch.draw(locked, level6X , level6Y);
			game.batch.draw(locked, level7X , level7Y);
			game.batch.draw(locked, level8X , level8Y);
			game.batch.draw(locked, level9X , level9Y);
			game.batch.draw(locked, level10X , level10Y);
			game.batch.draw(locked, level11X , level11Y);
			game.batch.draw(lockedBoss, level12X , level12Y);
			}
			if(page == 2){
			game.batch.draw(locked, level1X , level1Y);
			game.batch.draw(locked, level2X , level2Y);
			game.batch.draw(locked, level3X , level3Y);
			game.batch.draw(locked, level4X , level4Y);
			game.batch.draw(locked, level5X , level5Y);
			game.batch.draw(locked, level6X , level6Y);
			game.batch.draw(locked, level7X , level7Y);
			game.batch.draw(locked, level8X , level8Y);
			game.batch.draw(locked, level9X , level9Y);
			game.batch.draw(locked, level10X , level10Y);
			game.batch.draw(locked, level11X , level11Y);
			game.batch.draw(lockedBoss, level12X , level12Y);
			}
			if(page == 3){
			game.batch.draw(locked, level1X , level1Y);
			game.batch.draw(locked, level2X , level2Y);
			game.batch.draw(locked, level3X , level3Y);
			game.batch.draw(locked, level4X , level4Y);
			game.batch.draw(locked, level5X , level5Y);
			game.batch.draw(locked, level6X , level6Y);
			game.batch.draw(locked, level7X , level7Y);
			game.batch.draw(locked, level8X , level8Y);
			game.batch.draw(locked, level9X , level9Y);
			game.batch.draw(locked, level10X , level10Y);
			game.batch.draw(locked, level11X , level11Y);
			game.batch.draw(locked, level12X , level12Y);
			}
			break;
		case 3:
			if(page == 1){
			game.batch.draw(locked, level4X , level4Y);
			game.batch.draw(locked, level5X , level5Y);
			game.batch.draw(locked, level6X , level6Y);
			game.batch.draw(locked, level7X , level7Y);
			game.batch.draw(locked, level8X , level8Y);
			game.batch.draw(locked, level9X , level9Y);
			game.batch.draw(locked, level10X , level10Y);
			game.batch.draw(locked, level11X , level11Y);
			game.batch.draw(lockedBoss, level12X , level12Y);
			}
			if(page == 2){
			game.batch.draw(locked, level1X , level1Y);
			game.batch.draw(locked, level2X , level2Y);
			game.batch.draw(locked, level3X , level3Y);
			game.batch.draw(locked, level4X , level4Y);
			game.batch.draw(locked, level5X , level5Y);
			game.batch.draw(locked, level6X , level6Y);
			game.batch.draw(locked, level7X , level7Y);
			game.batch.draw(locked, level8X , level8Y);
			game.batch.draw(locked, level9X , level9Y);
			game.batch.draw(locked, level10X , level10Y);
			game.batch.draw(locked, level11X , level11Y);
			game.batch.draw(lockedBoss, level12X , level12Y);
			}
			if(page == 3){
			game.batch.draw(locked, level1X , level1Y);
			game.batch.draw(locked, level2X , level2Y);
			game.batch.draw(locked, level3X , level3Y);
			game.batch.draw(locked, level4X , level4Y);
			game.batch.draw(locked, level5X , level5Y);
			game.batch.draw(locked, level6X , level6Y);
			game.batch.draw(locked, level7X , level7Y);
			game.batch.draw(locked, level8X , level8Y);
			game.batch.draw(locked, level9X , level9Y);
			game.batch.draw(locked, level10X , level10Y);
			game.batch.draw(locked, level11X , level11Y);
			game.batch.draw(locked, level12X , level12Y);
			}
			break;
			
		case 4:
			if(page == 1){
			game.batch.draw(locked, level5X , level5Y);
			game.batch.draw(locked, level6X , level6Y);
			game.batch.draw(locked, level7X , level7Y);
			game.batch.draw(locked, level8X , level8Y);
			game.batch.draw(locked, level9X , level9Y);
			game.batch.draw(locked, level10X , level10Y);
			game.batch.draw(locked, level11X , level11Y);
			game.batch.draw(lockedBoss, level12X , level12Y);
			}
			if(page == 2){
			game.batch.draw(locked, level1X , level1Y);
			game.batch.draw(locked, level2X , level2Y);
			game.batch.draw(locked, level3X , level3Y);
			game.batch.draw(locked, level4X , level4Y);
			game.batch.draw(locked, level5X , level5Y);
			game.batch.draw(locked, level6X , level6Y);
			game.batch.draw(locked, level7X , level7Y);
			game.batch.draw(locked, level8X , level8Y);
			game.batch.draw(locked, level9X , level9Y);
			game.batch.draw(locked, level10X , level10Y);
			game.batch.draw(locked, level11X , level11Y);
			game.batch.draw(lockedBoss, level12X , level12Y);
			}
			if(page == 3){
			game.batch.draw(locked, level1X , level1Y);
			game.batch.draw(locked, level2X , level2Y);
			game.batch.draw(locked, level3X , level3Y);
			game.batch.draw(locked, level4X , level4Y);
			game.batch.draw(locked, level5X , level5Y);
			game.batch.draw(locked, level6X , level6Y);
			game.batch.draw(locked, level7X , level7Y);
			game.batch.draw(locked, level8X , level8Y);
			game.batch.draw(locked, level9X , level9Y);
			game.batch.draw(locked, level10X , level10Y);
			game.batch.draw(locked, level11X , level11Y);
			game.batch.draw(locked, level12X , level12Y);
			}
			break;
			
		case 5:
			if(page == 1){
			game.batch.draw(locked, level6X , level6Y);
			game.batch.draw(locked, level7X , level7Y);
			game.batch.draw(locked, level8X , level8Y);
			game.batch.draw(locked, level9X , level9Y);
			game.batch.draw(locked, level10X , level10Y);
			game.batch.draw(locked, level11X , level11Y);
			game.batch.draw(lockedBoss, level12X , level12Y);
			}
			if(page == 2){
			game.batch.draw(locked, level1X , level1Y);
			game.batch.draw(locked, level2X , level2Y);
			game.batch.draw(locked, level3X , level3Y);
			game.batch.draw(locked, level4X , level4Y);
			game.batch.draw(locked, level5X , level5Y);
			game.batch.draw(locked, level6X , level6Y);
			game.batch.draw(locked, level7X , level7Y);
			game.batch.draw(locked, level8X , level8Y);
			game.batch.draw(locked, level9X , level9Y);
			game.batch.draw(locked, level10X , level10Y);
			game.batch.draw(locked, level11X , level11Y);
			game.batch.draw(lockedBoss, level12X , level12Y);
			}
			if(page == 3){
			game.batch.draw(locked, level1X , level1Y);
			game.batch.draw(locked, level2X , level2Y);
			game.batch.draw(locked, level3X , level3Y);
			game.batch.draw(locked, level4X , level4Y);
			game.batch.draw(locked, level5X , level5Y);
			game.batch.draw(locked, level6X , level6Y);
			game.batch.draw(locked, level7X , level7Y);
			game.batch.draw(locked, level8X , level8Y);
			game.batch.draw(locked, level9X , level9Y);
			game.batch.draw(locked, level10X , level10Y);
			game.batch.draw(locked, level11X , level11Y);
			game.batch.draw(locked, level12X , level12Y);
			}
			break;
			
		case 6:
			if(page == 1){
			game.batch.draw(locked, level7X , level7Y);
			game.batch.draw(locked, level8X , level8Y);
			game.batch.draw(locked, level9X , level9Y);
			game.batch.draw(locked, level10X , level10Y);
			game.batch.draw(locked, level11X , level11Y);
			game.batch.draw(lockedBoss, level12X , level12Y);
			}
			if(page == 2){
			game.batch.draw(locked, level1X , level1Y);
			game.batch.draw(locked, level2X , level2Y);
			game.batch.draw(locked, level3X , level3Y);
			game.batch.draw(locked, level4X , level4Y);
			game.batch.draw(locked, level5X , level5Y);
			game.batch.draw(locked, level6X , level6Y);
			game.batch.draw(locked, level7X , level7Y);
			game.batch.draw(locked, level8X , level8Y);
			game.batch.draw(locked, level9X , level9Y);
			game.batch.draw(locked, level10X , level10Y);
			game.batch.draw(locked, level11X , level11Y);
			game.batch.draw(lockedBoss, level12X , level12Y);
			}
			if(page == 3){
			game.batch.draw(locked, level1X , level1Y);
			game.batch.draw(locked, level2X , level2Y);
			game.batch.draw(locked, level3X , level3Y);
			game.batch.draw(locked, level4X , level4Y);
			game.batch.draw(locked, level5X , level5Y);
			game.batch.draw(locked, level6X , level6Y);
			game.batch.draw(locked, level7X , level7Y);
			game.batch.draw(locked, level8X , level8Y);
			game.batch.draw(locked, level9X , level9Y);
			game.batch.draw(locked, level10X , level10Y);
			game.batch.draw(locked, level11X , level11Y);
			game.batch.draw(locked, level12X , level12Y);
			}
			break;
			
		case 7:
			if(page == 1){
			game.batch.draw(locked, level8X , level8Y);
			game.batch.draw(locked, level9X , level9Y);
			game.batch.draw(locked, level10X , level10Y);
			game.batch.draw(locked, level11X , level11Y);
			game.batch.draw(lockedBoss, level12X , level12Y);
			}
			if(page == 2){
			game.batch.draw(locked, level1X , level1Y);
			game.batch.draw(locked, level2X , level2Y);
			game.batch.draw(locked, level3X , level3Y);
			game.batch.draw(locked, level4X , level4Y);
			game.batch.draw(locked, level5X , level5Y);
			game.batch.draw(locked, level6X , level6Y);
			game.batch.draw(locked, level7X , level7Y);
			game.batch.draw(locked, level8X , level8Y);
			game.batch.draw(locked, level9X , level9Y);
			game.batch.draw(locked, level10X , level10Y);
			game.batch.draw(locked, level11X , level11Y);
			game.batch.draw(lockedBoss, level12X , level12Y);
			}
			if(page == 3){
			game.batch.draw(locked, level1X , level1Y);
			game.batch.draw(locked, level2X , level2Y);
			game.batch.draw(locked, level3X , level3Y);
			game.batch.draw(locked, level4X , level4Y);
			game.batch.draw(locked, level5X , level5Y);
			game.batch.draw(locked, level6X , level6Y);
			game.batch.draw(locked, level7X , level7Y);
			game.batch.draw(locked, level8X , level8Y);
			game.batch.draw(locked, level9X , level9Y);
			game.batch.draw(locked, level10X , level10Y);
			game.batch.draw(locked, level11X , level11Y);
			game.batch.draw(locked, level12X , level12Y);
			}
			break;
			
		case 8:
			if(page == 1){
			game.batch.draw(locked, level9X , level9Y);
			game.batch.draw(locked, level10X , level10Y);
			game.batch.draw(locked, level11X , level11Y);
			game.batch.draw(lockedBoss, level12X , level12Y);
			}
			if(page == 2){
			game.batch.draw(locked, level1X , level1Y);
			game.batch.draw(locked, level2X , level2Y);
			game.batch.draw(locked, level3X , level3Y);
			game.batch.draw(locked, level4X , level4Y);
			game.batch.draw(locked, level5X , level5Y);
			game.batch.draw(locked, level6X , level6Y);
			game.batch.draw(locked, level7X , level7Y);
			game.batch.draw(locked, level8X , level8Y);
			game.batch.draw(locked, level9X , level9Y);
			game.batch.draw(locked, level10X , level10Y);
			game.batch.draw(locked, level11X , level11Y);
			game.batch.draw(lockedBoss, level12X , level12Y);
			}
			if(page == 3){
			game.batch.draw(locked, level1X , level1Y);
			game.batch.draw(locked, level2X , level2Y);
			game.batch.draw(locked, level3X , level3Y);
			game.batch.draw(locked, level4X , level4Y);
			game.batch.draw(locked, level5X , level5Y);
			game.batch.draw(locked, level6X , level6Y);
			game.batch.draw(locked, level7X , level7Y);
			game.batch.draw(locked, level8X , level8Y);
			game.batch.draw(locked, level9X , level9Y);
			game.batch.draw(locked, level10X , level10Y);
			game.batch.draw(locked, level11X , level11Y);
			game.batch.draw(locked, level12X , level12Y);
			}
			
		case 9:
			if(page == 1){
			game.batch.draw(locked, level10X , level10Y);
			game.batch.draw(locked, level11X , level11Y);
			game.batch.draw(lockedBoss, level12X , level12Y);
			}
			if(page == 2){
			game.batch.draw(locked, level1X , level1Y);
			game.batch.draw(locked, level2X , level2Y);
			game.batch.draw(locked, level3X , level3Y);
			game.batch.draw(locked, level4X , level4Y);
			game.batch.draw(locked, level5X , level5Y);
			game.batch.draw(locked, level6X , level6Y);
			game.batch.draw(locked, level7X , level7Y);
			game.batch.draw(locked, level8X , level8Y);
			game.batch.draw(locked, level9X , level9Y);
			game.batch.draw(locked, level10X , level10Y);
			game.batch.draw(locked, level11X , level11Y);
			game.batch.draw(lockedBoss, level12X , level12Y);
			}
			if(page == 3){
			game.batch.draw(locked, level1X , level1Y);
			game.batch.draw(locked, level2X , level2Y);
			game.batch.draw(locked, level3X , level3Y);
			game.batch.draw(locked, level4X , level4Y);
			game.batch.draw(locked, level5X , level5Y);
			game.batch.draw(locked, level6X , level6Y);
			game.batch.draw(locked, level7X , level7Y);
			game.batch.draw(locked, level8X , level8Y);
			game.batch.draw(locked, level9X , level9Y);
			game.batch.draw(locked, level10X , level10Y);
			game.batch.draw(locked, level11X , level11Y);
			game.batch.draw(locked, level12X , level12Y);
			}
			break;
			
		case 10:
			if(page == 1){
			game.batch.draw(locked, level11X , level11Y);
			game.batch.draw(lockedBoss, level12X , level12Y);
			}
			if(page == 2){
			game.batch.draw(locked, level1X , level1Y);
			game.batch.draw(locked, level2X , level2Y);
			game.batch.draw(locked, level3X , level3Y);
			game.batch.draw(locked, level4X , level4Y);
			game.batch.draw(locked, level5X , level5Y);
			game.batch.draw(locked, level6X , level6Y);
			game.batch.draw(locked, level7X , level7Y);
			game.batch.draw(locked, level8X , level8Y);
			game.batch.draw(locked, level9X , level9Y);
			game.batch.draw(locked, level10X , level10Y);
			game.batch.draw(locked, level11X , level11Y);
			game.batch.draw(lockedBoss, level12X , level12Y);
			}
			if(page == 3){
			game.batch.draw(locked, level1X , level1Y);
			game.batch.draw(locked, level2X , level2Y);
			game.batch.draw(locked, level3X , level3Y);
			game.batch.draw(locked, level4X , level4Y);
			game.batch.draw(locked, level5X , level5Y);
			game.batch.draw(locked, level6X , level6Y);
			game.batch.draw(locked, level7X , level7Y);
			game.batch.draw(locked, level8X , level8Y);
			game.batch.draw(locked, level9X , level9Y);
			game.batch.draw(locked, level10X , level10Y);
			game.batch.draw(locked, level11X , level11Y);
			game.batch.draw(locked, level12X , level12Y);
			}
			break;
			
		case 11:
			if(page == 1){
			game.batch.draw(lockedBoss, level12X , level12Y);
			}
			if(page == 2){
			game.batch.draw(locked, level1X , level1Y);
			game.batch.draw(locked, level2X , level2Y);
			game.batch.draw(locked, level3X , level3Y);
			game.batch.draw(locked, level4X , level4Y);
			game.batch.draw(locked, level5X , level5Y);
			game.batch.draw(locked, level6X , level6Y);
			game.batch.draw(locked, level7X , level7Y);
			game.batch.draw(locked, level8X , level8Y);
			game.batch.draw(locked, level9X , level9Y);
			game.batch.draw(locked, level10X , level10Y);
			game.batch.draw(locked, level11X , level11Y);
			game.batch.draw(lockedBoss, level12X , level12Y);
			}
			if(page == 3){
			game.batch.draw(locked, level1X , level1Y);
			game.batch.draw(locked, level2X , level2Y);
			game.batch.draw(locked, level3X , level3Y);
			game.batch.draw(locked, level4X , level4Y);
			game.batch.draw(locked, level5X , level5Y);
			game.batch.draw(locked, level6X , level6Y);
			game.batch.draw(locked, level7X , level7Y);
			game.batch.draw(locked, level8X , level8Y);
			game.batch.draw(locked, level9X , level9Y);
			game.batch.draw(locked, level10X , level10Y);
			game.batch.draw(locked, level11X , level11Y);
			game.batch.draw(locked, level12X , level12Y);
			}
			break;
			
		case 12:
			if(page == 2){
			game.batch.draw(locked, level1X , level1Y);
			game.batch.draw(locked, level2X , level2Y);
			game.batch.draw(locked, level3X , level3Y);
			game.batch.draw(locked, level4X , level4Y);
			game.batch.draw(locked, level5X , level5Y);
			game.batch.draw(locked, level6X , level6Y);
			game.batch.draw(locked, level7X , level7Y);
			game.batch.draw(locked, level8X , level8Y);
			game.batch.draw(locked, level9X , level9Y);
			game.batch.draw(locked, level10X , level10Y);
			game.batch.draw(locked, level11X , level11Y);
			game.batch.draw(lockedBoss, level12X , level12Y);
			}
			if(page == 3){
			game.batch.draw(locked, level1X , level1Y);
			game.batch.draw(locked, level2X , level2Y);
			game.batch.draw(locked, level3X , level3Y);
			game.batch.draw(locked, level4X , level4Y);
			game.batch.draw(locked, level5X , level5Y);
			game.batch.draw(locked, level6X , level6Y);
			game.batch.draw(locked, level7X , level7Y);
			game.batch.draw(locked, level8X , level8Y);
			game.batch.draw(locked, level9X , level9Y);
			game.batch.draw(locked, level10X , level10Y);
			game.batch.draw(locked, level11X , level11Y);
			game.batch.draw(locked, level12X , level12Y);
			}
			break;
			
		case 13:
			if(page == 2){
				game.batch.draw(locked, level2X , level2Y);
				game.batch.draw(locked, level3X , level3Y);
				game.batch.draw(locked, level4X , level4Y);
				game.batch.draw(locked, level5X , level5Y);
				game.batch.draw(locked, level6X , level6Y);
				game.batch.draw(locked, level7X , level7Y);
				game.batch.draw(locked, level8X , level8Y);
				game.batch.draw(locked, level9X , level9Y);
				game.batch.draw(locked, level10X , level10Y);
				game.batch.draw(locked, level11X , level11Y);
				game.batch.draw(lockedBoss, level12X , level12Y);
			}
			if(page == 3){
				game.batch.draw(locked, level1X , level1Y);
				game.batch.draw(locked, level2X , level2Y);
				game.batch.draw(locked, level3X , level3Y);
				game.batch.draw(locked, level4X , level4Y);
				game.batch.draw(locked, level5X , level5Y);
				game.batch.draw(locked, level6X , level6Y);
				game.batch.draw(locked, level7X , level7Y);
				game.batch.draw(locked, level8X , level8Y);
				game.batch.draw(locked, level9X , level9Y);
				game.batch.draw(locked, level10X , level10Y);
				game.batch.draw(locked, level11X , level11Y);
				game.batch.draw(locked, level12X , level12Y);
				}
			break;
		case 14:
			if(page == 2){
				game.batch.draw(locked, level3X , level3Y);
				game.batch.draw(locked, level4X , level4Y);
				game.batch.draw(locked, level5X , level5Y);
				game.batch.draw(locked, level6X , level6Y);
				game.batch.draw(locked, level7X , level7Y);
				game.batch.draw(locked, level8X , level8Y);
				game.batch.draw(locked, level9X , level9Y);
				game.batch.draw(locked, level10X , level10Y);
				game.batch.draw(locked, level11X , level11Y);
				game.batch.draw(lockedBoss, level12X , level12Y);
			}
			if(page == 3){
				game.batch.draw(locked, level1X , level1Y);
				game.batch.draw(locked, level2X , level2Y);
				game.batch.draw(locked, level3X , level3Y);
				game.batch.draw(locked, level4X , level4Y);
				game.batch.draw(locked, level5X , level5Y);
				game.batch.draw(locked, level6X , level6Y);
				game.batch.draw(locked, level7X , level7Y);
				game.batch.draw(locked, level8X , level8Y);
				game.batch.draw(locked, level9X , level9Y);
				game.batch.draw(locked, level10X , level10Y);
				game.batch.draw(locked, level11X , level11Y);
				game.batch.draw(locked, level12X , level12Y);
				}
			break;
		case 15:
			if(page == 2){
				game.batch.draw(locked, level4X , level4Y);
				game.batch.draw(locked, level5X , level5Y);
				game.batch.draw(locked, level6X , level6Y);
				game.batch.draw(locked, level7X , level7Y);
				game.batch.draw(locked, level8X , level8Y);
				game.batch.draw(locked, level9X , level9Y);
				game.batch.draw(locked, level10X , level10Y);
				game.batch.draw(locked, level11X , level11Y);
				game.batch.draw(lockedBoss, level12X , level12Y);
			}
			if(page == 3){
				game.batch.draw(locked, level1X , level1Y);
				game.batch.draw(locked, level2X , level2Y);
				game.batch.draw(locked, level3X , level3Y);
				game.batch.draw(locked, level4X , level4Y);
				game.batch.draw(locked, level5X , level5Y);
				game.batch.draw(locked, level6X , level6Y);
				game.batch.draw(locked, level7X , level7Y);
				game.batch.draw(locked, level8X , level8Y);
				game.batch.draw(locked, level9X , level9Y);
				game.batch.draw(locked, level10X , level10Y);
				game.batch.draw(locked, level11X , level11Y);
				game.batch.draw(locked, level12X , level12Y);
				}
			break;
		case 16:
			if(page == 2){
				game.batch.draw(locked, level5X , level5Y);
				game.batch.draw(locked, level6X , level6Y);
				game.batch.draw(locked, level7X , level7Y);
				game.batch.draw(locked, level8X , level8Y);
				game.batch.draw(locked, level9X , level9Y);
				game.batch.draw(locked, level10X , level10Y);
				game.batch.draw(locked, level11X , level11Y);
				game.batch.draw(lockedBoss, level12X , level12Y);
			}
			if(page == 3){
				game.batch.draw(locked, level1X , level1Y);
				game.batch.draw(locked, level2X , level2Y);
				game.batch.draw(locked, level3X , level3Y);
				game.batch.draw(locked, level4X , level4Y);
				game.batch.draw(locked, level5X , level5Y);
				game.batch.draw(locked, level6X , level6Y);
				game.batch.draw(locked, level7X , level7Y);
				game.batch.draw(locked, level8X , level8Y);
				game.batch.draw(locked, level9X , level9Y);
				game.batch.draw(locked, level10X , level10Y);
				game.batch.draw(locked, level11X , level11Y);
				game.batch.draw(locked, level12X , level12Y);
				}
			break;
		case 17:
			if(page == 2){
				game.batch.draw(locked, level6X , level6Y);
				game.batch.draw(locked, level7X , level7Y);
				game.batch.draw(locked, level8X , level8Y);
				game.batch.draw(locked, level9X , level9Y);
				game.batch.draw(locked, level10X , level10Y);
				game.batch.draw(locked, level11X , level11Y);
				game.batch.draw(lockedBoss, level12X , level12Y);
			}
			if(page == 3){
				game.batch.draw(locked, level1X , level1Y);
				game.batch.draw(locked, level2X , level2Y);
				game.batch.draw(locked, level3X , level3Y);
				game.batch.draw(locked, level4X , level4Y);
				game.batch.draw(locked, level5X , level5Y);
				game.batch.draw(locked, level6X , level6Y);
				game.batch.draw(locked, level7X , level7Y);
				game.batch.draw(locked, level8X , level8Y);
				game.batch.draw(locked, level9X , level9Y);
				game.batch.draw(locked, level10X , level10Y);
				game.batch.draw(locked, level11X , level11Y);
				game.batch.draw(locked, level12X , level12Y);
				}
			break;
		case 18:
			if(page == 2){
				game.batch.draw(locked, level7X , level7Y);
				game.batch.draw(locked, level8X , level8Y);
				game.batch.draw(locked, level9X , level9Y);
				game.batch.draw(locked, level10X , level10Y);
				game.batch.draw(locked, level11X , level11Y);
				game.batch.draw(lockedBoss, level12X , level12Y);
			}
			if(page == 3){
				game.batch.draw(locked, level1X , level1Y);
				game.batch.draw(locked, level2X , level2Y);
				game.batch.draw(locked, level3X , level3Y);
				game.batch.draw(locked, level4X , level4Y);
				game.batch.draw(locked, level5X , level5Y);
				game.batch.draw(locked, level6X , level6Y);
				game.batch.draw(locked, level7X , level7Y);
				game.batch.draw(locked, level8X , level8Y);
				game.batch.draw(locked, level9X , level9Y);
				game.batch.draw(locked, level10X , level10Y);
				game.batch.draw(locked, level11X , level11Y);
				game.batch.draw(locked, level12X , level12Y);
				}
			break;
		case 19:
			if(page == 2){
				game.batch.draw(locked, level8X , level8Y);
				game.batch.draw(locked, level9X , level9Y);
				game.batch.draw(locked, level10X , level10Y);
				game.batch.draw(locked, level11X , level11Y);
				game.batch.draw(lockedBoss, level12X , level12Y);
			}
			if(page == 3){
				game.batch.draw(locked, level1X , level1Y);
				game.batch.draw(locked, level2X , level2Y);
				game.batch.draw(locked, level3X , level3Y);
				game.batch.draw(locked, level4X , level4Y);
				game.batch.draw(locked, level5X , level5Y);
				game.batch.draw(locked, level6X , level6Y);
				game.batch.draw(locked, level7X , level7Y);
				game.batch.draw(locked, level8X , level8Y);
				game.batch.draw(locked, level9X , level9Y);
				game.batch.draw(locked, level10X , level10Y);
				game.batch.draw(locked, level11X , level11Y);
				game.batch.draw(locked, level12X , level12Y);
				}
			break;
		case 20:
			if(page == 2){
				game.batch.draw(locked, level9X , level9Y);
				game.batch.draw(locked, level10X , level10Y);
				game.batch.draw(locked, level11X , level11Y);
				game.batch.draw(lockedBoss, level12X , level12Y);
			}
			if(page == 3){
				game.batch.draw(locked, level1X , level1Y);
				game.batch.draw(locked, level2X , level2Y);
				game.batch.draw(locked, level3X , level3Y);
				game.batch.draw(locked, level4X , level4Y);
				game.batch.draw(locked, level5X , level5Y);
				game.batch.draw(locked, level6X , level6Y);
				game.batch.draw(locked, level7X , level7Y);
				game.batch.draw(locked, level8X , level8Y);
				game.batch.draw(locked, level9X , level9Y);
				game.batch.draw(locked, level10X , level10Y);
				game.batch.draw(locked, level11X , level11Y);
				game.batch.draw(locked, level12X , level12Y);
				}
			break;
		case 21:
			if(page == 2){
				game.batch.draw(locked, level10X , level10Y);
				game.batch.draw(locked, level11X , level11Y);
				game.batch.draw(lockedBoss, level12X , level12Y);
			}
			if(page == 3){
				game.batch.draw(locked, level1X , level1Y);
				game.batch.draw(locked, level2X , level2Y);
				game.batch.draw(locked, level3X , level3Y);
				game.batch.draw(locked, level4X , level4Y);
				game.batch.draw(locked, level5X , level5Y);
				game.batch.draw(locked, level6X , level6Y);
				game.batch.draw(locked, level7X , level7Y);
				game.batch.draw(locked, level8X , level8Y);
				game.batch.draw(locked, level9X , level9Y);
				game.batch.draw(locked, level10X , level10Y);
				game.batch.draw(locked, level11X , level11Y);
				game.batch.draw(locked, level12X , level12Y);
				}
			break;
		case 22:
			if(page == 2){
				game.batch.draw(locked, level11X , level11Y);
				game.batch.draw(lockedBoss, level12X , level12Y);
			}
			if(page == 3){
				game.batch.draw(locked, level1X , level1Y);
				game.batch.draw(locked, level2X , level2Y);
				game.batch.draw(locked, level3X , level3Y);
				game.batch.draw(locked, level4X , level4Y);
				game.batch.draw(locked, level5X , level5Y);
				game.batch.draw(locked, level6X , level6Y);
				game.batch.draw(locked, level7X , level7Y);
				game.batch.draw(locked, level8X , level8Y);
				game.batch.draw(locked, level9X , level9Y);
				game.batch.draw(locked, level10X , level10Y);
				game.batch.draw(locked, level11X , level11Y);
				game.batch.draw(locked, level12X , level12Y);
				}
			break;
		case 23:
			if(page == 2){
				game.batch.draw(lockedBoss, level12X , level12Y);
			}
			if(page == 3){
				game.batch.draw(locked, level1X , level1Y);
				game.batch.draw(locked, level2X , level2Y);
				game.batch.draw(locked, level3X , level3Y);
				game.batch.draw(locked, level4X , level4Y);
				game.batch.draw(locked, level5X , level5Y);
				game.batch.draw(locked, level6X , level6Y);
				game.batch.draw(locked, level7X , level7Y);
				game.batch.draw(locked, level8X , level8Y);
				game.batch.draw(locked, level9X , level9Y);
				game.batch.draw(locked, level10X , level10Y);
				game.batch.draw(locked, level11X , level11Y);
				game.batch.draw(locked, level12X , level12Y);
				}
			break;
		case 24:
			if(page == 3){
				game.batch.draw(locked, level1X , level1Y);
				game.batch.draw(locked, level2X , level2Y);
				game.batch.draw(locked, level3X , level3Y);
				game.batch.draw(locked, level4X , level4Y);
				game.batch.draw(locked, level5X , level5Y);
				game.batch.draw(locked, level6X , level6Y);
				game.batch.draw(locked, level7X , level7Y);
				game.batch.draw(locked, level8X , level8Y);
				game.batch.draw(locked, level9X , level9Y);
				game.batch.draw(locked, level10X , level10Y);
				game.batch.draw(locked, level11X , level11Y);
				game.batch.draw(locked, level12X , level12Y);
			}
			break;
		case 25:
			if(page == 3){
				game.batch.draw(locked, level2X , level2Y);
				game.batch.draw(locked, level3X , level3Y);
				game.batch.draw(locked, level4X , level4Y);
				game.batch.draw(locked, level5X , level5Y);
				game.batch.draw(locked, level6X , level6Y);
				game.batch.draw(locked, level7X , level7Y);
				game.batch.draw(locked, level8X , level8Y);
				game.batch.draw(locked, level9X , level9Y);
				game.batch.draw(locked, level10X , level10Y);
				game.batch.draw(locked, level11X , level11Y);
				game.batch.draw(locked, level12X , level12Y);
			}
			break;
		case 26:
			if(page == 3){
				game.batch.draw(locked, level3X , level3Y);
				game.batch.draw(locked, level4X , level4Y);
				game.batch.draw(locked, level5X , level5Y);
				game.batch.draw(locked, level6X , level6Y);
				game.batch.draw(locked, level7X , level7Y);
				game.batch.draw(locked, level8X , level8Y);
				game.batch.draw(locked, level9X , level9Y);
				game.batch.draw(locked, level10X , level10Y);
				game.batch.draw(locked, level11X , level11Y);
				game.batch.draw(locked, level12X , level12Y);
			}
			break;
		case 27:
			if(page == 3){
				game.batch.draw(locked, level4X , level4Y);
				game.batch.draw(locked, level5X , level5Y);
				game.batch.draw(locked, level6X , level6Y);
				game.batch.draw(locked, level7X , level7Y);
				game.batch.draw(locked, level8X , level8Y);
				game.batch.draw(locked, level9X , level9Y);
				game.batch.draw(locked, level10X , level10Y);
				game.batch.draw(locked, level11X , level11Y);
				game.batch.draw(locked, level12X , level12Y);
			}
			break;
		case 28:
			if(page == 3){
				game.batch.draw(locked, level5X , level5Y);
				game.batch.draw(locked, level6X , level6Y);
				game.batch.draw(locked, level7X , level7Y);
				game.batch.draw(locked, level8X , level8Y);
				game.batch.draw(locked, level9X , level9Y);
				game.batch.draw(locked, level10X , level10Y);
				game.batch.draw(locked, level11X , level11Y);
				game.batch.draw(locked, level12X , level12Y);
			}
			break;
		case 29:
			if(page == 3){
				game.batch.draw(locked, level6X , level6Y);
				game.batch.draw(locked, level7X , level7Y);
				game.batch.draw(locked, level8X , level8Y);
				game.batch.draw(locked, level9X , level9Y);
				game.batch.draw(locked, level10X , level10Y);
				game.batch.draw(locked, level11X , level11Y);
				game.batch.draw(locked, level12X , level12Y);
			}
			break;
		case 30:
			if(page == 3){
				game.batch.draw(locked, level7X , level7Y);
				game.batch.draw(locked, level8X , level8Y);
				game.batch.draw(locked, level9X , level9Y);
				game.batch.draw(locked, level10X , level10Y);
				game.batch.draw(locked, level11X , level11Y);
				game.batch.draw(locked, level12X , level12Y);
			}
			break;
		case 31:
			if(page == 3){
				game.batch.draw(locked, level8X , level8Y);
				game.batch.draw(locked, level9X , level9Y);
				game.batch.draw(locked, level10X , level10Y);
				game.batch.draw(locked, level11X , level11Y);
				game.batch.draw(locked, level12X , level12Y);
			}
			break;
		case 32:
			if(page == 3){
				game.batch.draw(locked, level9X , level9Y);
				game.batch.draw(locked, level10X , level10Y);
				game.batch.draw(locked, level11X , level11Y);
				game.batch.draw(locked, level12X , level12Y);
			}
			break;
		case 33:
			if(page == 3){
				game.batch.draw(locked, level10X , level10Y);
				game.batch.draw(locked, level11X , level11Y);
				game.batch.draw(locked, level12X , level12Y);
			}
			break;
		case 34:
			if(page == 3){
				game.batch.draw(locked, level11X , level11Y);
				game.batch.draw(locked, level12X , level12Y);
			}
			break;
		case 35:
			if(page == 3){
				game.batch.draw(locked, level12X , level12Y);
			}
			break;
			
			
		}
		
		

	}
	
	
	//if button are being touched
	public void buttonsTouched(){
		float touchX = game.cam.getInputInGameWorld().x;
		//reversing the getY 
		float touchY = MainGame.HEIGHT - game.cam.getInputInGameWorld().y;
		
		
		//if the back button is touched
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACK)){
			if(sfx)
				confirmButton.play(0.6f);
				game.setScreen(new MainMenuScreen(game, true, handler, false));
				musicDrums.stop();  
				this.dispose();
			
		}
		
			//IF THE Button 1 IS HOVERED
			if(touchX > level1X && touchX < level1X + buttWidth){
				if(touchY > level1Y && touchY < level1Y + buttHeight){
					if(Gdx.input.justTouched() && canClick && page == 1){
						if(sfx)
					confirmButton.play(0.6f);
					System.out.println("Level: 1");
					game.setScreen(new GameScreen(game, 1, handler, false));
					musicDrums.stop(); 
					this.dispose();
					}
					
					if(Gdx.input.justTouched() && highlevel >= 13 && canClick && page == 2){
						if(sfx)
					confirmButton.play(0.6f);
					System.out.println("Level: 13");
					game.setScreen(new GameScreen(game, 13, handler, false));
					musicDrums.stop(); 
					this.dispose();
					}else if(Gdx.input.justTouched() && canClick && page == 2)if(sfx)negativeButton.play();
					
					if(Gdx.input.justTouched() && highlevel >= 25 && canClick && page == 3){
						if(sfx)
					confirmButton.play(0.6f);
					System.out.println("Level: 25");
					game.setScreen(new GameScreen(game, 25, handler, false));
					musicDrums.stop(); 
					this.dispose();
					}else if(Gdx.input.justTouched() && canClick && page == 3)if(sfx)negativeButton.play();
					
				}
			}
			
			//IF THE Button 2 IS HOVERED
			if(touchX > level2X && touchX < level2X + buttWidth){
				if(touchY > level2Y && touchY < level2Y + buttHeight){
					if(Gdx.input.justTouched() && highlevel >=2 && canClick && page == 1){
						if(sfx)
					confirmButton.play(0.6f);
					System.out.println("Level: 2");
					game.setScreen(new GameScreen(game, 2, handler, false));
					musicDrums.stop(); 
					this.dispose();
					}else if(Gdx.input.justTouched() && canClick && page == 1)if(sfx)negativeButton.play();
					
					if(Gdx.input.justTouched() && highlevel >= 14 && canClick && page == 2){
						if(sfx)
					confirmButton.play(0.6f);
					System.out.println("Level: 14");
					game.setScreen(new GameScreen(game, 14, handler, false));
					musicDrums.stop(); 
					this.dispose();
					}else if(Gdx.input.justTouched() && canClick && page == 2)if(sfx)negativeButton.play();
					
					if(Gdx.input.justTouched() && highlevel >= 26 && canClick && page == 3){
						if(sfx)
					confirmButton.play(0.6f);
					System.out.println("Level: 26");
					game.setScreen(new GameScreen(game, 26, handler, false));
					musicDrums.stop(); 
					this.dispose();
					}else if(Gdx.input.justTouched() && canClick && page == 3)if(sfx)negativeButton.play();
				}
			}
			
			//IF THE Button 3 IS HOVERED
			if(touchX > level3X && touchX < level3X + buttWidth){
				if(touchY > level3Y && touchY < level3Y + buttHeight){
					if(Gdx.input.justTouched() && highlevel >=3 && canClick && page == 1){
						if(sfx)
					confirmButton.play(0.6f);
					System.out.println("Level: 3");
					game.setScreen(new GameScreen(game, 3, handler, false));
					musicDrums.stop(); 
					this.dispose();
					}else if(Gdx.input.justTouched() && canClick && page == 1)if(sfx)negativeButton.play();
					
					if(Gdx.input.justTouched() && highlevel >= 15 && canClick && page == 2){
						if(sfx)
					confirmButton.play(0.6f);
					System.out.println("Level: 15");
					game.setScreen(new GameScreen(game, 15, handler, false));
					musicDrums.stop(); 
					this.dispose();
					}else if(Gdx.input.justTouched() && canClick && page == 2)if(sfx)negativeButton.play();
					
					if(Gdx.input.justTouched() && highlevel >= 27 && canClick && page == 3){
						if(sfx)
					confirmButton.play(0.6f);
					System.out.println("Level: 27");
					game.setScreen(new GameScreen(game, 27, handler, false));
					musicDrums.stop(); 
					this.dispose();
					}else if(Gdx.input.justTouched() && canClick && page == 3)if(sfx)negativeButton.play();
				}
			}
			
			//IF THE Button 4 IS HOVERED
			if(touchX > level4X && touchX < level4X + buttWidth){
				if(touchY > level4Y && touchY < level4Y + buttHeight){
					if(Gdx.input.justTouched() && highlevel >=4 && canClick && page == 1){
						if(sfx)
					confirmButton.play(0.6f);
					System.out.println("Level: 4");
					game.setScreen(new GameScreen(game, 4, handler, false));
					musicDrums.stop(); 
					this.dispose();
					}else if(Gdx.input.justTouched() && canClick && page == 1)if(sfx)negativeButton.play();
					
					if(Gdx.input.justTouched() && highlevel >= 16 && canClick && page == 2){
						if(sfx)
					confirmButton.play(0.6f);
					System.out.println("Level: 16");
					game.setScreen(new GameScreen(game, 16, handler, false));
					musicDrums.stop(); 
					this.dispose();
					}else if(Gdx.input.justTouched() && canClick && page == 2)if(sfx)negativeButton.play();
					
					if(Gdx.input.justTouched() && highlevel >= 28 && canClick && page == 3){
						if(sfx)
					confirmButton.play(0.6f);
					System.out.println("Level: 28");
					game.setScreen(new GameScreen(game, 28, handler, false));
					musicDrums.stop(); 
					this.dispose();
					}else if(Gdx.input.justTouched() && canClick && page == 3)if(sfx)negativeButton.play();
				}
			}
			
			//IF THE Button 5 IS HOVERED
			if(touchX > level5X && touchX < level5X + buttWidth){
				if(touchY > level5Y && touchY < level5Y + buttHeight){
					if(Gdx.input.justTouched() && highlevel >=5 && canClick && page == 1){
						if(sfx)
					confirmButton.play(0.6f);
					System.out.println("Level: 5");
					game.setScreen(new GameScreen(game, 5, handler, false));
					musicDrums.stop(); 
					this.dispose();
					}else if(Gdx.input.justTouched() && canClick && page == 1)if(sfx)negativeButton.play();
					
					if(Gdx.input.justTouched() && highlevel >= 17 && canClick && page == 2){
						if(sfx)
					confirmButton.play(0.6f);
					System.out.println("Level: 17");
					game.setScreen(new GameScreen(game, 17, handler, false));
					musicDrums.stop(); 
					this.dispose();
					}else if(Gdx.input.justTouched() && canClick && page == 2)if(sfx)negativeButton.play();
					
					if(Gdx.input.justTouched() && highlevel >= 29 && canClick && page == 3){
						if(sfx)
					confirmButton.play(0.6f);
					System.out.println("Level: 29");
					game.setScreen(new GameScreen(game, 29, handler, false));
					musicDrums.stop(); 
					this.dispose();
					}else if(Gdx.input.justTouched() && canClick && page == 3)if(sfx)negativeButton.play();
					
				}
			}


			//IF THE Button 6 IS HOVERED
			if(touchX > level6X && touchX < level6X + buttWidth){
				if(touchY > level6Y && touchY < level6Y + buttHeight){
					if(Gdx.input.justTouched() && highlevel >=6 && canClick && page == 1){
						if(sfx)
					confirmButton.play(0.6f);
					System.out.println("Level: 6");
					game.setScreen(new GameScreen(game, 6, handler, false));
					musicDrums.stop(); 
					this.dispose();
					}else if(Gdx.input.justTouched() && canClick && page == 1)if(sfx)negativeButton.play();
					
					if(Gdx.input.justTouched() && highlevel >= 18 && canClick && page == 2){
						if(sfx)
					confirmButton.play(0.6f);
					System.out.println("Level: 18");
					game.setScreen(new GameScreen(game, 18, handler, false));
					musicDrums.stop(); 
					this.dispose();
					}else if(Gdx.input.justTouched() && canClick && page == 2)if(sfx)negativeButton.play();
					
					if(Gdx.input.justTouched() && highlevel >= 30 && canClick && page == 3){
						if(sfx)
					confirmButton.play(0.6f);
					System.out.println("Level: 30");
					game.setScreen(new GameScreen(game, 30, handler, false));
					musicDrums.stop(); 
					this.dispose();
					}else if(Gdx.input.justTouched() && canClick && page == 3)if(sfx)negativeButton.play();
					
				}
			}
			
			//IF THE Button 7 IS HOVERED
			if(touchX > level7X && touchX < level7X + buttWidth){
				if(touchY > level7Y && touchY < level7Y + buttHeight){
					if(Gdx.input.justTouched() && highlevel >=7 && canClick && page == 1){
						if(sfx)
					confirmButton.play(0.6f);
					System.out.println("Level: 7");
					game.setScreen(new GameScreen(game, 7, handler, false));
					musicDrums.stop(); 
					this.dispose();
					}else if(Gdx.input.justTouched() && canClick && page == 1)if(sfx)negativeButton.play();
					
					if(Gdx.input.justTouched() && highlevel >= 19 && canClick && page == 2){
						if(sfx)
					confirmButton.play(0.6f);
					System.out.println("Level: 19");
					game.setScreen(new GameScreen(game, 19, handler, false));
					musicDrums.stop(); 
					this.dispose();
					}else if(Gdx.input.justTouched() && canClick && page == 2)if(sfx)negativeButton.play();
					
					if(Gdx.input.justTouched() && highlevel >= 31 && canClick && page == 3){
						if(sfx)
					confirmButton.play(0.6f);
					System.out.println("Level: 31");
					game.setScreen(new GameScreen(game, 31, handler, false));
					musicDrums.stop(); 
					this.dispose();
					}else if(Gdx.input.justTouched() && canClick && page == 3)if(sfx)negativeButton.play();
					
				}
			}
			
			//IF THE Button 8 IS HOVERED
			if(touchX > level8X && touchX < level8X + buttWidth){
				if(touchY > level8Y && touchY < level8Y + buttHeight){
					if(Gdx.input.justTouched() && highlevel >=8 && canClick && page == 1){
						if(sfx)
					confirmButton.play(0.6f);
					System.out.println("Level: 8");
					game.setScreen(new GameScreen(game, 8, handler, false));
					musicDrums.stop(); 
					this.dispose();
					}else if(Gdx.input.justTouched() && canClick && page == 1)if(sfx)negativeButton.play();
					
					if(Gdx.input.justTouched() && highlevel >= 20 && canClick && page == 2){
						if(sfx)
					confirmButton.play(0.6f);
					System.out.println("Level: 20");
					game.setScreen(new GameScreen(game, 20, handler, false));
					musicDrums.stop(); 
					this.dispose();
					}else if(Gdx.input.justTouched() && canClick && page == 2)if(sfx)negativeButton.play();
					
					if(Gdx.input.justTouched() && highlevel >= 32 && canClick && page == 3){
						if(sfx)
					confirmButton.play(0.6f);
					System.out.println("Level: 32");
					game.setScreen(new GameScreen(game, 32, handler, false));
					musicDrums.stop(); 
					this.dispose();
					}else if(Gdx.input.justTouched() && canClick && page == 3)if(sfx)negativeButton.play();
					
				}
			}
			
			//IF THE Button 9 IS HOVERED
			if(touchX > level9X && touchX < level9X + buttWidth){
				if(touchY > level9Y && touchY < level9Y + buttHeight){
					if(Gdx.input.justTouched() && highlevel >=9 && canClick && page == 1){
						if(sfx)
					confirmButton.play(0.6f);
					System.out.println("Level: 9");
					game.setScreen(new GameScreen(game, 9, handler, false));
					musicDrums.stop(); 
					this.dispose();
					}else if(Gdx.input.justTouched() && canClick && page == 1)if(sfx)negativeButton.play();
					
					if(Gdx.input.justTouched() && highlevel >= 21 && canClick && page == 2){
						if(sfx)
					confirmButton.play(0.6f);
					System.out.println("Level: 21");
					game.setScreen(new GameScreen(game, 21, handler, false));
					musicDrums.stop(); 
					this.dispose();
					}else if(Gdx.input.justTouched() && canClick && page == 2)if(sfx)negativeButton.play();
					
					if(Gdx.input.justTouched() && highlevel >= 33 && canClick && page == 3){
						if(sfx)
					confirmButton.play(0.6f);
					System.out.println("Level: 33");
					game.setScreen(new GameScreen(game, 33, handler, false));
					musicDrums.stop(); 
					this.dispose();
					}else if(Gdx.input.justTouched() && canClick && page == 3)if(sfx)negativeButton.play();
					
				}
			}
			
			//IF THE Button 10 IS HOVERED
			if(touchX > level10X && touchX < level10X + buttWidth){
				if(touchY > level10Y && touchY < level10Y + buttHeight){
					if(Gdx.input.justTouched() && highlevel >=10 && canClick && page == 1){
						if(sfx)
					confirmButton.play(0.6f);
					System.out.println("Level: 10");
					game.setScreen(new GameScreen(game, 10, handler, false));
					musicDrums.stop(); 
					this.dispose();
					}else if(Gdx.input.justTouched() && canClick && page == 1)if(sfx)negativeButton.play();
					
					if(Gdx.input.justTouched() && highlevel >= 22 && canClick && page == 2){
						if(sfx)
					confirmButton.play(0.6f);
					System.out.println("Level: 22");
					game.setScreen(new GameScreen(game, 22, handler, false));
					musicDrums.stop(); 
					this.dispose();
					}else if(Gdx.input.justTouched() && canClick && page == 2)if(sfx)negativeButton.play();
					
					if(Gdx.input.justTouched() && highlevel >= 34 && canClick && page == 3){
						if(sfx)
					confirmButton.play(0.6f);
					System.out.println("Level: 34");
					game.setScreen(new GameScreen(game, 34, handler, false));
					musicDrums.stop(); 
					this.dispose();
					}else if(Gdx.input.justTouched() && canClick && page == 3)if(sfx)negativeButton.play();
					
				}
			}
			
			//IF THE Button 11 IS HOVERED
			if(touchX > level11X && touchX < level11X + buttWidth){
				if(touchY > level11Y && touchY < level11Y + buttHeight){
					if(Gdx.input.justTouched() && highlevel >=11 && canClick && page == 1){
						if(sfx)
					confirmButton.play(0.6f);
					System.out.println("Level: 11");
					game.setScreen(new GameScreen(game, 11, handler, false));
					musicDrums.stop(); 
					this.dispose();
					}else if(Gdx.input.justTouched() && canClick && page == 1)if(sfx)negativeButton.play();
					
					if(Gdx.input.justTouched() && highlevel >= 23 && canClick && page == 2){
						if(sfx)
					confirmButton.play(0.6f);
					System.out.println("Level: 23");
					game.setScreen(new GameScreen(game, 23, handler, false));
					musicDrums.stop(); 
					this.dispose();
					}else if(Gdx.input.justTouched() && canClick && page == 2)if(sfx)negativeButton.play();
					
					if(Gdx.input.justTouched() && highlevel >= 35 && canClick && page == 3){
						if(sfx)
					confirmButton.play(0.6f);
					System.out.println("Level: 35");
					game.setScreen(new GameScreen(game, 35, handler, false));
					musicDrums.stop(); 
					this.dispose();
					}else if(Gdx.input.justTouched() && canClick && page == 3)if(sfx)negativeButton.play();
				}
			}
			
			//IF THE Button 12 IS HOVERED
			if(touchX > level12X && touchX < level12X + buttWidth){
				if(touchY > level12Y && touchY < level12Y + buttHeight){
					if(Gdx.input.justTouched() && highlevel >=12 && canClick && page == 1){
						if(sfx)
					confirmButton.play(0.6f);
					System.out.println("Level: 12");
					game.setScreen(new GameScreen(game, 12, handler, false));
					musicDrums.stop(); 
					this.dispose();
					}else if(Gdx.input.justTouched() && canClick && page == 1)if(sfx)negativeButton.play();
					
					if(Gdx.input.justTouched() && highlevel >= 24 && canClick && page == 2){
						if(sfx)
					confirmButton.play(0.6f);
					System.out.println("Level: 24");
					game.setScreen(new GameScreen(game, 24, handler, false));
					musicDrums.stop(); 
					this.dispose();
					}else if(Gdx.input.justTouched() && canClick && page == 2)if(sfx)negativeButton.play();
					
					if(Gdx.input.justTouched() && highlevel >= 36 && canClick && page == 3){
						if(sfx)
					confirmButton.play(0.6f);
					System.out.println("Level: 36");
					game.setScreen(new GameScreen(game, 36, handler, false));
					musicDrums.stop(); 
					this.dispose();
					}else if(Gdx.input.justTouched() && canClick && page == 3)if(sfx)negativeButton.play();
					
				}
			}
			
			//back
			if(touchX > 130 && touchX < 130 + backButtonInactive.getRegionWidth() + 20 && touchY > 0 && touchY < 40 + backButtonInactive.getRegionHeight()){
				game.batch.draw(backButtonActive, 140, 20);
					if(Gdx.input.justTouched() && canClick){
						if(page == 1){
							if(sfx)
						confirmButton.play(0.6f);
						game.setScreen(new MainMenuScreen(game, true, handler, false));
						musicDrums.stop();  
						this.dispose();
						}
						if(page == 2){
							page = 1;
							if(sfx)
							confirmButton.play(0.6f); 
							canClick = false;
							startButtonClickTimer = true;
						}
						if(page == 3){
							page = 2;
							if(sfx)
							confirmButton.play(0.6f); 
							canClick = false;
							startButtonClickTimer = true;
						}
						
					}
				}else
					game.batch.draw(backButtonInactive, 140, 20);
			
			//next
			if(touchX > 280 && touchX < 280 + nextButtonInactive.getRegionWidth() + 20 && touchY > 0 && touchY < 40 + nextButtonInactive.getRegionHeight() && page < 3){
				game.batch.draw(nextButtonActive, 290, 20);
					if(Gdx.input.justTouched() && page == 1 && canClick){
						if(sfx)
						confirmButton.play(0.6f); 
						canClick = false;
						startButtonClickTimer = true;
						page = 2;
					}
					if(Gdx.input.justTouched() && page == 2 && canClick){
						if(sfx)
						confirmButton.play(0.6f); 
						canClick = false;
						startButtonClickTimer = true;
						page = 3;
					}
				}else{
					if(page < 3)
					game.batch.draw(nextButtonInactive, 290, 20);
				}
	}
	
	public void backgroundScrolling(){
		if(bgY > -MainGame.HEIGHT){
			bgY-=bgvelY;
			}
			else
				bgY = 0;
			
	}
	
	public void buttonClickTimer(float delta){
		if(startButtonClickTimer){
			buttonClickTime += delta;
			
			if(buttonClickTime >= buttonClickTimer){
				buttonClickTime = 0;
				startButtonClickTimer = false;
				canClick = true;
			}
		}
	}
	
	public void show() {
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	
	
	public int[] getLevelStars() {
		return levelStars;
	}

	public void setLevelStars(int[] levelStars) {
		this.levelStars = levelStars;
	}

	@Override
	public void dispose() {
		bg.dispose();

		System.out.println("Select Level Screen disposed.");
	}

	

}
