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

public class Credits implements Screen{

	//objects
	MainGame game;
	Preferences prefs;
	
	//sounds and music
	Sound confirmButton;
	
	//option variables
	boolean sfx, replay;

	//ads
	AdHandler handler;
	boolean toggle;
	
	//textures
	Texture background;
	Texture credits;
	Texture menuButtons;
	TextureRegion backButtonInactive, backButtonActive;
	
	//background scrolling variables
	float bgY = 0;
	float bgvelY = 0.3f;
	
	public Credits(MainGame game, AdHandler handler, boolean toggle){
		this.game = game;
		this.handler = handler;
		this.toggle = toggle;

		if(MainGame.IS_MOBILE)
		handler.showAds(toggle);

		//textures
		background = new Texture("background.png");
		credits = new Texture("credits.png");
		menuButtons = new Texture("menuButtons.png");
		backButtonInactive = new TextureRegion(menuButtons, 300*4, 140, 60, 60);
		backButtonActive = new TextureRegion(menuButtons, 300*5, 140, 60, 60);
		
		//sounds
		confirmButton = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/confirmButton.ogg"));
		
		//loading data from save file
		prefs = Gdx.app.getPreferences("spaceraiders");
		sfx = prefs.getBoolean("sfx", true);
		
		
		Gdx.input.setCatchBackKey(true);
		

	}
	


	@Override
	public void render(float delta) {
		
		backgroundAnimations();
		
		game.batch.begin();
		
		//scrolling background drawing
		game.batch.draw(background, 0, bgY);
		game.batch.draw(background, 0, bgY + MainGame.HEIGHT);
		
		
		//credits
		game.batch.draw(credits, 0, 0);
		
		//buttons
		buttons();
		
		
		game.batch.end();
	}
	
	public void buttons(){
		//touch coordinates (reversing the getY)
		float touchX = game.cam.getInputInGameWorld().x;
		float touchY = MainGame.HEIGHT - game.cam.getInputInGameWorld().y;
		
		//if the back button is touched
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACK)){
			if(sfx)
				confirmButton.play(0.6f);
				game.setScreen(new HelpScreen(game, handler, true));
				this.dispose();
			
		}
	
		//back
		if((touchX > 180 && touchX < 180 + 100 && touchY > 0 && touchY < 20 + 100) || (Gdx.input.isKeyJustPressed(Keys.BACK)) || (Gdx.input.isKeyJustPressed(Keys.ESCAPE))){
			game.batch.draw(backButtonActive, 200, 20);
				if(Gdx.input.justTouched()){
					if(sfx)
					confirmButton.play(0.6f);
					game.setScreen(new HelpScreen(game, handler, true));
					this.dispose();
					
				}
			}else
				game.batch.draw(backButtonInactive, 200, 20);
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
		System.out.println("Credits Screen Disposed.");
	}

}
