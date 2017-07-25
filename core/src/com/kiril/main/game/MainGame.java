package com.kiril.main.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kiril.screens.MainMenuScreen;
import com.kiril.tools.AdHandler;
import com.kiril.tools.GameCamera;


public class MainGame extends Game {
	
	public static final int WIDTH = 480;
	public static final int HEIGHT = 720;
	public static boolean IS_MOBILE = false;
	
	public SpriteBatch batch;
	public GameCamera cam;

	//ads
	AdHandler handler;
	boolean toggle;

	public MainGame(AdHandler handler){
		this.handler = handler;

	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		cam = new GameCamera(WIDTH, HEIGHT);

		this.setScreen(new MainMenuScreen(this, true, handler, false));
		

		//checks if the game is opened on mobile
		if(Gdx.app.getType() == ApplicationType.Android || Gdx.app.getType() == ApplicationType.iOS){
			IS_MOBILE = true;
		}
		
	}

	@Override
	public void render () {
		batch.setProjectionMatrix(cam.combined());
		super.render();
	}
	
	@Override
	public void resize(int width, int height) {
		cam.update(width, height);
		super.resize(width, height);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
	
}

