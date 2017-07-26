package com.kiril.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kiril.main.game.MainGame;
import com.kiril.tools.AdHandler;

public class OptionsMenu implements Screen{

	//objects
	MainGame game;
	Preferences prefs;
	
	//sounds and music
	Sound confirmButton;
	
	//option variables
	boolean music, sfx, replay, progressBar;

	//ads
	AdHandler handler;
	boolean toggle;
	
	//textures
	Texture background;
	Texture optionsMenu;
	Texture checkWindow;
	Texture menuButtons;
	TextureRegion backButtonInactive, backButtonActive;
	
	//background scrolling variables
	float bgY = 0;
	float bgvelY = 0.3f;
	
	public OptionsMenu(MainGame game, AdHandler handler, boolean toggle){
		this.game = game;
		this.handler = handler;
		this.toggle = toggle;

		if(MainGame.IS_MOBILE)
		handler.showAds(toggle);

		//textures
		background = new Texture("background.png");
		optionsMenu = new Texture("optionsMenu.png");
		checkWindow = new Texture("optionsCheck.png");
		menuButtons = new Texture("menuButtons.png");
		backButtonInactive = new TextureRegion(menuButtons, 300*4, 140, 60, 60);
		backButtonActive = new TextureRegion(menuButtons, 300*5, 140, 60, 60);
		
		//sounds
		confirmButton = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/confirmButton.ogg"));
		
		//loading data from save file
		prefs = Gdx.app.getPreferences("spaceraiders");
		music = prefs.getBoolean("music", true);
		sfx = prefs.getBoolean("sfx" , true);
		replay = prefs.getBoolean("replay", true);
		progressBar = prefs.getBoolean("progressBar", true);
		
		Gdx.input.setCatchBackKey(true);
		

	}
	


	@Override
	public void render(float delta) {
		
		backgroundAnimations();
		
		game.batch.begin();
		
		//scrolling background drawing
		game.batch.draw(background, 0, bgY);
		game.batch.draw(background, 0, bgY + MainGame.HEIGHT);
		
		//options menu window
		game.batch.draw(optionsMenu, 0, 0);
		
		//buttons
		buttons();
		
		//music
		if(music)game.batch.draw(checkWindow, 320, 482); 
		//sfx
		if(sfx)game.batch.draw(checkWindow, 320, 404); 
		//auto replay
		if(replay)game.batch.draw(checkWindow, 320, 316); 
		//progress bar
		if(progressBar)game.batch.draw(checkWindow, 320, 224); 
		
		game.batch.end();
	}
	
	public void buttons(){
		//touch coordinates (reversing the getY)
		float touchX = game.cam.getInputInGameWorld().x;
		float touchY = MainGame.HEIGHT - game.cam.getInputInGameWorld().y;
		
	
		
		//music
		if(touchX > 300 && touchX < 300 + 80 && touchY > 470 && touchY < 470 + 80){
				if(Gdx.input.justTouched() && !music){
					if(sfx)
					confirmButton.play(0.6f);
					music = true;
					HelpScreen.resumeAllMusic();
				
				}
				else if(Gdx.input.justTouched() && music){
					if(sfx)
					confirmButton.play(0.6f);
					music = false;
					HelpScreen.pauseAllMusic();
				}
				
			}
		
		//sfx
		if(touchX > 300 && touchX < 300 + 90 && touchY > 390 && touchY < 390 + 80){
				if(Gdx.input.justTouched() && !sfx){
					if(sfx)
					confirmButton.play(0.6f);
					sfx = true;
				}
				else if(Gdx.input.justTouched() && sfx){
					//if(sfx)
					confirmButton.play(0.6f);
					sfx = false;
				}
			}
		
		//auto replay
		if(touchX > 300 && touchX < 300 + 90 && touchY > 300 && touchY < 300 + 90){
				if(Gdx.input.justTouched() && !replay){
					if(sfx)
					confirmButton.play(0.6f);
					replay = true;
				}
				else if(Gdx.input.justTouched() && replay){
					if(sfx)
					confirmButton.play(0.6f);
					replay = false;
				}
			}
		
		//progress bar
		if(touchX > 300 && touchX < 300 + 80 && touchY > 210 && touchY < 210 + 80){
				if(Gdx.input.justTouched() && !progressBar){
					if(sfx)
					confirmButton.play(0.6f);
					progressBar = true;
				}
				else if(Gdx.input.justTouched() && progressBar){
					if(sfx)
					confirmButton.play(0.6f);
					progressBar = false;
				}
			}
		
		//back
		if((touchX > 190 && touchX < 190 + 100 && touchY > 0 && touchY < 20 + 100) || (Gdx.input.isKeyJustPressed(Keys.BACK)) || (Gdx.input.isKeyJustPressed(Keys.ESCAPE))){
			game.batch.draw(backButtonActive, 200, 20);
				if(Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACK)){
					if(sfx)
					confirmButton.play(0.6f);
					prefs.putBoolean("music", music);
					prefs.putBoolean("sfx", sfx);
					prefs.putBoolean("replay", replay);
					prefs.putBoolean("progressBar", progressBar);
					prefs.flush();
					game.setScreen(new HelpScreen(game, handler, true));
					this.dispose();
					
				}
			}else
				game.batch.draw(backButtonInactive, 200, 20);
		
		//if the back button is touched
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACK)){
			if(sfx)
				confirmButton.play(0.6f);
				game.setScreen(new MainMenuScreen(game, true, handler, false));
				this.dispose();
			
		}
		
	}
	
	public void backgroundAnimations(){
		if(bgY > -MainGame.HEIGHT){
			bgY-=bgvelY;
			}
			else
				bgY = 0;
			
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
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
		background.dispose();
		confirmButton.dispose();
		
		System.out.println("Options Menu Disposed.");
	}

}
