package com.kiril.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Align;
import com.kiril.main.game.MainGame;
import com.kiril.tools.AdHandler;

public class GameOverScreen implements Screen {

	//objects
	MainGame game;
	Preferences prefs;
	
	//changable variables from options menu
	boolean music, sfx, replay;
	
	//numbers for the replay countdown
	Texture zero, one, two, three, four, five;
	Texture replayWindow;
	Texture blank;
	boolean isCountdownFinished = false;
	boolean startCountDown = false;
	float countDownTime = 6f;
	float progressBar;
	
	//variables
	int score, highscore, level, highlevel;
	
	//music and sounds
	Music musicGameOver;
	Sound scoreCounter; boolean scoreCounterStart = false;
	Sound confirmButton;
	
	
	//score counter variables
	float tempScore; 
	boolean isScoreFinished = false;
	float scoreCounterTime = 0.6f;
	boolean startCounter = false;
	
	//background
	Texture bg;
	float bgY = 0;
	float bgvelY = 0.8f;
	Texture bg2;
	
	//font
	BitmapFont font;
	FreeTypeFontGenerator generator;
	FreeTypeFontParameter parameter;
	
	//layouts
	GlyphLayout scoreLayout;
	GlyphLayout scoreNumLayout;
	GlyphLayout highscoreLayout;
	GlyphLayout quitLayout;
	GlyphLayout mainmenuLayout;
	GlyphLayout replayLayout;
	
	//color for the font
	Color color;
	
	//buttons parameters
	float mainMenuX, mainMenuY, quitX, quitY, replayX, replayY;
	
	//arrow
	Texture arrow;

	//ads
	AdHandler handler;
	boolean toggle;
	
	public GameOverScreen(MainGame game, int score, int level, AdHandler handler, boolean toggle){
		this.game = game; 
		this.score = score;
		this.level = level;
		this.handler = handler;
		this.toggle = toggle;

		if(MainGame.IS_MOBILE)
		handler.showAds(toggle);
		
		//catch the back button on android
		Gdx.input.setCatchBackKey(true);
		
		//loading data from save file
		prefs = Gdx.app.getPreferences("spaceraiders");
		music = prefs.getBoolean("music", true);
		replay = prefs.getBoolean("replay", true);
		sfx = prefs.getBoolean("sfx", true);
		
		
		bg = new Texture("background.png");
		bg2 = new Texture("gameoverMenu.png");
		
		if(replay){
		replayWindow = new Texture("replayProgressBar.png");
		blank = new Texture("blank.png");
		one = new Texture("numbers/1replay.png");
		two = new Texture("numbers/2replay.png");
		three = new Texture("numbers/3replay.png");
		four = new Texture("numbers/4replay.png");
		five = new Texture("numbers/5replay.png");
		zero = new Texture("numbers/0replay.png");
		}
		
		musicGameOver = Gdx.audio.newMusic(Gdx.files.internal("audio/music/gameover.ogg"));
		musicGameOver.setVolume(0.9f);
		musicGameOver.setLooping(true);
		if(music)
		musicGameOver.play();
		
		scoreCounter = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/score.ogg"));
		confirmButton = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/confirmButton.ogg"));
		
		
		//loading highscore from save file
		Preferences prefs = Gdx.app.getPreferences("spaceraiders");
		//search for the highscore tag and sets the highscore to that value, if it doesn't have highscore it sets it to 0
		this.highscore = prefs.getInteger("highscore", 0);
		this.highlevel = prefs.getInteger("highlevel", 1);
		
		
		//check if score beats highscore
		if(score > highscore){
			prefs.putInteger("highscore", score);
			prefs.flush(); //saves the file
		}
		
		//checks level
		if(level > highlevel){
			prefs.putInteger("highlevel", level);
			prefs.flush(); //saves the file
		}
		
		//initialize font
		generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/venus.ttf"));
		parameter = new FreeTypeFontParameter();
		parameter.size = 24;
		font = generator.generateFont(parameter);
	
		//initialize color
		color = new Color(11, 9, 218, 0.9f);
		
		//layouts initialize
		scoreLayout = new GlyphLayout(font, "Score: ", color, 0, Align.left, false);
		highscoreLayout = new GlyphLayout(font, "Highscore: \n \t  " + highscore, color, 0, Align.left, false);
		mainmenuLayout = new GlyphLayout(font, "Main menu", color, 0, Align.left, false);
		quitLayout = new GlyphLayout(font, "quit game", color, 0, Align.left, false);
		replayLayout = new GlyphLayout(font, "Replay", color, 0, Align.left, false);
		
		//initialize buttons parameters
		mainMenuX = MainGame.WIDTH / 2 - scoreLayout.width / 1.6f;
		mainMenuY = MainGame.HEIGHT / 2.25f;
		replayX = mainMenuX;
		replayY = mainMenuY - 60;
		quitX = mainMenuX;
		quitY = mainMenuY - 120;
		
		//initialize arrow button
		arrow = new Texture("buttons/arrow.png");

	}
	
	public void update(float delta){
		
		//background scrolling
		backgroundScrolling();
		
		//score counting
		scoreCounterTime -= delta;
		if(scoreCounterTime <= 0){
			startCounter = true;
		}
		if(startCounter){
			if(!scoreCounterStart){
				if(sfx)
			scoreCounter.loop(0.9f);
			scoreCounterStart = true;
			}
			
			if(score <= 15000)
			tempScore += delta * 4500;
			if(score > 15000 && score <= 40000)
				tempScore += delta * 10000;
			if(score > 40000 && score <= 80000)
				tempScore += delta * 18000;
			if(score > 80000)
				tempScore += delta * 24000;
			
			if(tempScore >= score){
				tempScore = score;
				scoreCounter.stop();
				isScoreFinished = true;
				startCountDown = true;
			}
		}
		
		//replay countdown
		if(replay){
			if(startCountDown){
				countDownTime -= delta;
				progressBar += delta * 52;
				
				if(progressBar >= 275){
					progressBar = 275;
				}
				
				if(countDownTime  <= 0.50){
					countDownTime = 0.50f;
					startCountDown = false;
					game.setScreen(new GameScreen(game, level, handler, false));
					this.dispose();
				}
			}
		}
	}

	@Override
	public void render(float delta) {
		
	
		
		//update method
		update(delta);
		
		game.batch.begin();
		
		//background
		game.batch.draw(bg, 0, bgY);
		game.batch.draw(bg, 0, bgY + MainGame.HEIGHT);
		
		game.batch.draw(bg2, 0, 0);
		
		//score
		font.draw(game.batch, scoreLayout, MainGame.WIDTH / 2 - scoreLayout.width / 2 - 88, MainGame.HEIGHT - 165);
		
		//initializing the score layout here because we are chainging it
		scoreNumLayout = new GlyphLayout(font, "" + (int)tempScore);
		
		font.draw(game.batch, scoreNumLayout, MainGame.WIDTH / 2 - scoreLayout.width / 2 + 45, MainGame.HEIGHT - 165);
		font.draw(game.batch, highscoreLayout, MainGame.WIDTH / 2 - scoreLayout.width / 2 - 88, MainGame.HEIGHT - 215);
		
		//buttons draw
		font.draw(game.batch, mainmenuLayout, mainMenuX, mainMenuY);
		font.draw(game.batch, replayLayout, replayX, replayY);
		font.draw(game.batch, quitLayout, quitX, quitY);
		
		//replay bar
		if(replay){
			game.batch.draw(blank, 98, 70, (int)progressBar, 12);
			game.batch.draw(replayWindow, 90, 50);
			if(startCountDown){
			if(countDownTime >= 0 && countDownTime < 1) game.batch.draw(zero, 270, 85);
			if(countDownTime >= 1 && countDownTime < 2) game.batch.draw(one, 270, 85);
			if(countDownTime >= 2 && countDownTime < 3) game.batch.draw(two, 270, 85);
			if(countDownTime >= 3 && countDownTime < 4) game.batch.draw(three, 270, 85);
			if(countDownTime >= 4 && countDownTime < 5) game.batch.draw(four, 270, 85);
			if(countDownTime >= 5 && countDownTime < 6) game.batch.draw(five, 270, 85);
			}
		}
		
		
		//buttons test for click is here because we are drawing in it
		buttonsTouched();
		
		game.batch.end();

	}
	
	//if button are being touched
	public void buttonsTouched(){
		float touchX = game.cam.getInputInGameWorld().x;
		//reversing the getY 
		float touchY = MainGame.HEIGHT - game.cam.getInputInGameWorld().y;
		
		
			//IF THE MAIN MENU BUTTON IS HOVERED
			if(touchX > mainMenuX - 20 && touchX < mainMenuX + mainmenuLayout.width+20){
				if(touchY > mainMenuY - 30 && touchY < mainMenuY + mainmenuLayout.height+20){
					if(Gdx.input.justTouched()){
						if(sfx)
							confirmButton.play(0.6f);
					game.setScreen(new MainMenuScreen(game, true, handler, false));
					musicGameOver.stop();
					this.dispose();
					}
					else
					game.batch.draw(arrow, mainMenuX - arrow.getWidth() + 6, mainMenuY - arrow.getHeight() + 5);
				}
			}
			
			//IF THE REPLAY BUTTON IS TOUCHED
			if(touchX > replayX - 20 && touchX < replayX  + replayLayout.width+20){
				if(touchY > replayY -30 && touchY < replayY + replayLayout.height+10){
					if(Gdx.input.justTouched()){
						if(sfx)
							confirmButton.play(0.6f);
					game.setScreen(new GameScreen(game, level, handler, false));
					musicGameOver.stop();
					this.dispose();
					}
					else
					game.batch.draw(arrow, replayX - arrow.getWidth() + 6, replayY - arrow.getHeight() + 5);
				}
			}
		
			
			//IF THE EXIT BUTTON IS TOUCHED
			if(touchX > quitX - 20 && touchX < quitX + quitLayout.width + 20){
				if(touchY > quitY - 40 && touchY < quitY + quitLayout.height+10){
					if(Gdx.input.justTouched()){
					Gdx.app.exit();
					}
					else
					game.batch.draw(arrow, quitX - arrow.getWidth() + 6, quitY - arrow.getHeight() + 5);
				}
			}
			
		
	}
	
	public void backgroundScrolling(){
		if(bgY > -MainGame.HEIGHT){
			bgY-=bgvelY;
			}
			else
				bgY = 0;
			
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

	@Override
	public void dispose() {
		musicGameOver.dispose();
		arrow.dispose();
		confirmButton.dispose();
		generator.dispose();
		scoreCounter.dispose();
		bg.dispose();
		bg2.dispose();
		font.dispose();
		
		if(replay){
			one.dispose();
			two.dispose();
			three.dispose();
			four.dispose();
			five.dispose();
			zero.dispose();
			replayWindow.dispose();
			blank.dispose();
		}
		
		System.out.println("Game Over Screen Disposed.");
	}

	

}
