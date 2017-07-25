package com.kiril.screens;
import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kiril.main.game.MainGame;
import com.kiril.tools.AdHandler;

public class MainMenuScreen implements Screen{

	//objects
	MainGame game;
	Random rand;
	Animation notification; 
	Preferences prefs;
	HelpScreen helpScreen;
	
	//in game variables changable in the options
	boolean music, sfx, replay, progressBar;
	
	//music and sounds
	boolean playMusic; 
	Music musicMainMenu;
	
	Sound confirmButton;
	
	//ads
	AdHandler handler;
	boolean toggle;

	//textures
	Texture bg;
	Texture star, meteor;
	//Texture notification;
	Texture menuButtons;
	Texture speechBubble;
	
	//buttons
	TextureRegion exitButtonActive, exitButtonInactive, playButtonActive, playButtonInactive, helpButtonActive, helpButtonInactive, okayButtonInactive, okayButtonActive, noButtonInactive, noButtonActive;
	
	//shooting star variables
	float speedStar = 6f;
	float xStar = 680;
	float yStar = 920;
	
	//if the game is opened for the first time
	int firstTimePlay;
	
	//starting coordinates for x and y
	float playButtonX, helpButtonX, exitButtonX;
	float playButtonY, helpButtonY, exitButtonY; 
	
	float statetime;
	
	//messeges
	boolean isSpeechBubble;

	//timers for button clicking
	float buttonClickTimer;
	boolean startButtonClickTimer;
	float buttonClickTime;
	boolean canClick;
	
	public MainMenuScreen(MainGame game, boolean playMusic, AdHandler handler, boolean toggle){
		this.game = game;
		this.playMusic = playMusic;
		this.handler = handler;
		this.toggle = toggle;

		System.out.println("ADS: "+ toggle);
		if(MainGame.IS_MOBILE)
		handler.showAds(toggle);

		//loading data from save file
		prefs = Gdx.app.getPreferences("spaceraiders");
		this.firstTimePlay = prefs.getInteger("firstTimePlay", 0);
		music = prefs.getBoolean("music", true);
		sfx = prefs.getBoolean("sfx", true);
		replay = prefs.getBoolean("replay", true);
		progressBar = prefs.getBoolean("progressBar", true);
		
		rand = new Random();
		helpScreen = new HelpScreen(game, handler, true);
		
		notification = new Animation(0.04f, TextureRegion.split(new Texture("notification.png"), 40, 40)[0]);
		
		
		//music
		musicMainMenu = Gdx.audio.newMusic(Gdx.files.internal("audio/music/main.ogg"));
		musicMainMenu.setLooping(true);
		if(playMusic && music)
		musicMainMenu.play();
		
		
		confirmButton = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/confirmButton.ogg"));
		
		//menu buttons
		menuButtons = new Texture("menuButtons.png");
		playButtonInactive = new TextureRegion(menuButtons, 0, 0, 297, 56);
		playButtonActive = new TextureRegion(menuButtons, 300*1, 0, 297, 56);
		exitButtonInactive = new TextureRegion(menuButtons, 300*2, 0, 297, 56);
		exitButtonActive = new TextureRegion(menuButtons, 300*3, 0, 297, 56);
		helpButtonInactive = new TextureRegion(menuButtons, 300*4, 0, 188, 56);
		helpButtonActive = new TextureRegion(menuButtons, 300*5, 0, 188, 56);
		okayButtonInactive = new TextureRegion(menuButtons, 0, 70, 163, 34);
		okayButtonActive = new TextureRegion(menuButtons, 300*1, 70, 163, 34);
		noButtonInactive = new TextureRegion(menuButtons, 300*2, 70, 147, 34);
		noButtonActive = new TextureRegion(menuButtons, 300*3, 70, 147, 34);
		
		playButtonX = MainGame.WIDTH / 2 - playButtonInactive.getRegionWidth() / 1.4f ; //playbutton starting X
		playButtonY = MainGame.HEIGHT / 2 - playButtonInactive.getRegionHeight() / 1.5f; //playbutton starting Y
		helpButtonX = playButtonX;
		helpButtonY = playButtonY - 90;
		exitButtonX = playButtonX;
		exitButtonY = playButtonY - 180;
		
		
		//background
		bg = new Texture("backgroundMenu2.png");
		
		//random falling stuff
		star = new Texture("star.png.png");
		
		
		//speech bubbles
		speechBubble = new Texture("messeges/speechBubble.png");
		
		
		
		buttonClickTimer = 0.1f;
		startButtonClickTimer = true;
		buttonClickTime = 0;
		canClick = false;
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}
	
	public void update(float delta){
		
		statetime += delta;
		

		//can click buttons after specific time
		buttonClickTimer(delta);
		
		backgroundScrolling();
		
		
	
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		update(delta);
		
		game.batch.begin();
		
		game.batch.draw(bg, 0, 0);
		
		
		
		game.batch.draw(star, xStar, yStar);
	
		Buttons();
		
		game.batch.end();
		
		
	}

	private void Buttons(){
		
		//touch coordinates (reversing the getY)
		float touchX = game.cam.getInputInGameWorld().x;
		float touchY = MainGame.HEIGHT - game.cam.getInputInGameWorld().y;
	
		//playButton on desktop
		if(touchX > playButtonX && touchX < playButtonX + 300 && touchY > playButtonY && touchY < playButtonY + 70){
			game.batch.draw(playButtonActive, playButtonX + 10, playButtonY);
			//if its touched
				if(Gdx.input.justTouched() && canClick){
					//if the game has been played before
					if(firstTimePlay == 1){	
						if(sfx)
					confirmButton.play(0.6f);
					
					game.setScreen(new SelectLevelScreen(game, 0, 0, 0, true, handler, true));
					musicMainMenu.stop();
					HelpScreen.stopAllMusic();
					
					this.dispose();
					}
					else{
						if(sfx)
							confirmButton.play(0.6f);
						isSpeechBubble = true;
						
					}
				}
		
			}else
				game.batch.draw(playButtonInactive, playButtonX, playButtonY);
		
			//helpButton
			if(touchX > helpButtonX && touchX < helpButtonX + 200 && touchY > helpButtonY && touchY < helpButtonY + 70){
				game.batch.draw(helpButtonActive, helpButtonX + 10, helpButtonY);
				if(Gdx.input.justTouched() && canClick){
					if(sfx)
					confirmButton.play(0.6f);
					game.setScreen(new HelpScreen(game, handler, true));
					HelpScreen.musics.add(musicMainMenu);
					this.dispose();
				}
			}else{
				game.batch.draw(helpButtonInactive, helpButtonX, helpButtonY);
			}
		
			//exit Button
			if(touchX > exitButtonX && touchX < exitButtonX + 300 && touchY > exitButtonY && touchY < exitButtonY + 70){
					game.batch.draw(exitButtonActive, exitButtonX + 10, exitButtonY);
					if(Gdx.input.justTouched() && canClick){
						if(sfx)
						confirmButton.play(0.6f);
						Gdx.app.exit();
						this.dispose();
					}
				}else{
					game.batch.draw(exitButtonInactive, exitButtonX, exitButtonY);
			}
			
			
			//notification if the game is opened for the first time
			if(firstTimePlay == 0)
				game.batch.draw(notification.getKeyFrame(statetime, true), playButtonX + playButtonInactive.getRegionWidth() - 30, playButtonY + playButtonInactive.getRegionHeight() - 20);
			
		
		//if notification is hovered
		if(isSpeechBubble){
		game.batch.draw(speechBubble, playButtonX, playButtonY + playButtonInactive.getRegionHeight() + 20);
		
		
		//okay button
		if(touchX > playButtonX + 10 && touchX < playButtonX + 210 && touchY > playButtonY + 85 && touchY < playButtonY + 170){
			game.batch.draw(okayButtonActive, playButtonX + 30, playButtonY + 93);
			if(Gdx.input.justTouched()){
				if(sfx)
					confirmButton.play(0.6f);
				HelpScreen.musics.add(musicMainMenu);
				game.setScreen(new TutorialScreen(game, handler, true));
				isSpeechBubble = false;
				prefs.putInteger("firstTimePlay", 1);
				prefs.flush(); //saves the file
				this.dispose();
			}
		}else
			game.batch.draw(okayButtonInactive, playButtonX + 30, playButtonY + 93);
			
		//no button
		if(touchX > playButtonX + 265 && touchX < playButtonX + 450  && touchY > playButtonY + 85  && touchY < playButtonY + 170){
			game.batch.draw(noButtonActive, playButtonX + 280, playButtonY + 93);
			if(Gdx.input.justTouched()){
				if(sfx)
					confirmButton.play(0.6f);
				game.setScreen(new SelectLevelScreen(game, 0, 0, 0, true, handler, true));
				musicMainMenu.stop();
				HelpScreen.stopAllMusic();
				this.dispose();
				isSpeechBubble = false;
				prefs.putInteger("firstTimePlay", 1);
				prefs.flush(); //saves the file
			}
		}else
			game.batch.draw(noButtonInactive, playButtonX + 280, playButtonY + 93);
		
		
		}
		
			
		
		
		
		
	}
	
	private void backgroundScrolling(){
		
		if(xStar > -200 && yStar > -200){
			xStar -= speedStar;
			yStar -= speedStar;
			
		}else{
			xStar = rand.nextInt(480) + rand.nextInt(1000) + 100;
			yStar = rand.nextInt(720) + rand.nextInt(2000) + 100;
		}
		
	
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
	
	public void stopMusic(){
		//musicMainMenu.stop();
	}

	

	@Override
	public void dispose() {
		bg.dispose();
		star.dispose();
		menuButtons.dispose();
		//musicMainMenu.dispose();
		System.out.println("Main Menu Screen Disposed.");
	}

}