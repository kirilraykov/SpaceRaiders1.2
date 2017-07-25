package com.kiril.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kiril.main.game.MainGame;
import com.kiril.tools.AdHandler;

public class HelpScreen implements Screen {

	//objects
	MainGame game;
	Preferences prefs;
	
	//list for every music from the main menu
	public static ArrayList<Music> musics = new ArrayList<Music>();
	
	//variables changables in options
	boolean music, sfx, replay, progressBar;
	
	//sounds and music
	Sound confirmButton;
	
	//textures
	Texture background;
	Texture help;
	Texture menuButtons;
	TextureRegion tutorialButtonInactive, tutorialButtonActive, creditsButtonActive, creditsButtonInactive, optionsButtonInactive, optionsButtonActive;
	TextureRegion backButtonInactive, backButtonActive;
	
	
	//background scrolling variables
	float bgY = 0;
	float bgvelY = 0.3f;
	
	//timers for button clicking
	float buttonClickTimer = 0.3f;
	boolean startButtonClickTimer = true;
	float buttonClickTime = 0;
	boolean canClick = false;

	//ads
	AdHandler handler;
	boolean toggle;
	
	public HelpScreen(MainGame game, AdHandler handler, boolean toggle){
		
		//catch the back button on android
		Gdx.input.setCatchBackKey(true);
		
		//objects
		this.game = game;
		this.handler = handler;
		this.toggle = toggle;

		if(MainGame.IS_MOBILE)
		handler.showAds(toggle);
		
		//loading data from save file
		prefs = Gdx.app.getPreferences("spaceraiders");
		music = prefs.getBoolean("music", true);
		sfx = prefs.getBoolean("sfx", true);
		replay = prefs.getBoolean("replay", true);
		progressBar = prefs.getBoolean("progressBar", true);
		
		//textures
		background = new Texture("background.png");
		help = new Texture("help.png");
		
		menuButtons = new Texture("menuButtons.png");
		tutorialButtonInactive = new TextureRegion(menuButtons, 300*4, 70, 285, 60);
		tutorialButtonActive = new TextureRegion(menuButtons, 300*5, 70, 285, 60);
		optionsButtonInactive = new TextureRegion(menuButtons, 0, 140, 264, 57);
		optionsButtonActive = new TextureRegion(menuButtons, 300*1, 140, 264, 57);
		creditsButtonInactive = new TextureRegion(menuButtons, 300*2, 140, 264, 57);
		creditsButtonActive = new TextureRegion(menuButtons, 300*3, 140, 264, 57);
		backButtonInactive = new TextureRegion(menuButtons, 300*4, 140, 60, 60);
		backButtonActive = new TextureRegion(menuButtons, 300*5, 140, 60, 60);
		
		//sounds
		confirmButton = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/confirmButton.ogg"));
		
		
	}
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		
		//background scrolling
		backgroundAnimations();
		
		//timer for the buttons
		buttonClickTimer(delta);
		
		game.batch.begin();
		
		//scrolling background drawing
		game.batch.draw(background, 0, bgY);
		game.batch.draw(background, 0, bgY + MainGame.HEIGHT);
		
		//help layer drawing
		game.batch.draw(help, 0, 0);
		
		buttons();
		
	
		
		game.batch.end();
		
	
		
		
	}

	public void buttonClickTimer(float delta){
		if(startButtonClickTimer){
			//System.out.println("TIMER STARTED");
			buttonClickTime += delta;
			
			if(buttonClickTime >= buttonClickTimer){
				//System.out.println("CAN CLICK");
				buttonClickTime = 0;
				startButtonClickTimer = false;
				canClick = true;
			}
		}
	}
	

	public void backgroundAnimations(){
		if(bgY > -MainGame.HEIGHT){
			bgY-=bgvelY;
			}
			else
				bgY = 0;
			
	}
	
	public void buttons(){
		//touch coordinates (reversing the getY)
		float touchX = game.cam.getInputInGameWorld().x;
		float touchY = MainGame.HEIGHT - game.cam.getInputInGameWorld().y;
		
		//back button on adnroid
		//if the back button is touched
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACK)){
			if(sfx)
				confirmButton.play(0.6f);
				game.setScreen(new MainMenuScreen(game, false, handler, false));
				this.dispose();
			
		}
		
		//tutorial
		if(touchX > 170 && touchX < 170 + tutorialButtonInactive.getRegionWidth() && touchY > 310 && touchY < 310 + tutorialButtonInactive.getRegionHeight()){
			game.batch.draw(tutorialButtonActive, 170 + 10, 310);
				if(Gdx.input.justTouched()  && canClick){
					if(sfx)
					confirmButton.play(0.6f);
					game.setScreen(new TutorialScreen(game, handler, false));
					this.dispose();
				}
			}else
				game.batch.draw(tutorialButtonInactive, 170, 310);
		
		//options
		if(touchX > 30 && touchX < 30 + optionsButtonInactive.getRegionWidth() && touchY > 210 && touchY < 210 + optionsButtonInactive.getRegionHeight()){
			game.batch.draw(optionsButtonActive, 30 + 10, 210);
				if(Gdx.input.justTouched() && canClick){
					if(sfx)
					confirmButton.play(0.6f);
					game.setScreen(new OptionsMenu(game, handler, true));
					System.out.println("OPTIONS");
				}
			}else
				game.batch.draw(optionsButtonInactive, 30, 210);

		//credits
		if(touchX > 130 && touchX < 130 + creditsButtonInactive.getRegionWidth() && touchY > 110 && touchY < 110 + creditsButtonInactive.getRegionHeight()){
			game.batch.draw(creditsButtonActive, 130 + 10, 110);
				if(Gdx.input.justTouched()  && canClick){
					if(sfx)
					confirmButton.play(0.6f);
					game.setScreen(new Credits(game, handler, true));
					this.dispose();
				}
			}else
				game.batch.draw(creditsButtonInactive, 130, 110);
		
		//back
		if(touchX > 190 && touchX < 190 + 90 && touchY > 0 && touchY < 0 + 100){
			game.batch.draw(backButtonActive, 200, 20);
				if(Gdx.input.justTouched()  && canClick){
					if(sfx)
					confirmButton.play(0.6f);
					game.setScreen(new MainMenuScreen(game, false, handler, false));
					this.dispose();
					
				}
			}else
				game.batch.draw(backButtonInactive, 200, 20);
			
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
	

	public ArrayList<Music> getMusics() {
		return musics;
	}
	public static void addMusics(Music music) {
		musics.add(music);
	}
	public static void stopAllMusic(){
		for(Music music : musics){
			music.stop();
			music.dispose();
		}
	}
	public static void pauseAllMusic(){
		for(Music music : musics){
			music.pause();
		}
	}
	public static void resumeAllMusic(){
		for(Music music : musics){
			music.play();
		}
	}
	
	@Override
	public void dispose() {
		menuButtons.dispose();
		help.dispose();
		background.dispose();
		System.out.println("Help Screen Disposed.");
	}

}
