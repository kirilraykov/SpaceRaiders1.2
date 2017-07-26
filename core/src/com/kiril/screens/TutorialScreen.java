package com.kiril.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.kiril.entities.ShootingBulletsEnemy;
import com.kiril.entities.ShootingEnemy;
import com.kiril.entities.ShootingFreezingBulletsEnemy;
import com.kiril.entities.SimpleEnemy;
import com.kiril.main.game.MainGame;
import com.kiril.tools.AdHandler;

public class TutorialScreen implements Screen {

	//objects
	MainGame game;
	Preferences prefs;
	
	//tutorial pages
	int page = 1;
	
	//changable variables
	int firstTimePlay;
	boolean music, sfx, replay, progressBar;
	
	//sounds and music
	Sound confirmButton;

	//ads
	AdHandler handler;
	boolean toggle;

	//fonts
	BitmapFont fontButton, fontText;
	FreeTypeFontGenerator generatorButton, generatorText;
	FreeTypeFontParameter parameterButton, parameterText;
	
	//textures
	Animation notification;
	Texture background, background2, rect, rect2, rect3, rect4;
	Texture modesSpeechBubble, coinsSpeechBubble, collectiblesSpeechBubble, enemySpeechBubble, scorePointsSpeechBubble, shieldSpeechBubble, progressBarSpeechBubble;
	TextureRegion ok;
	TextureRegion nextActive, nextInactive;
	
	//animation statetime
	float statetime;
	
	//layouts
	GlyphLayout infoLayout, skipLayout;
	
	//notification choice
	int choice;
	boolean[] notificationsRead = new boolean[8];
	boolean isAllNotificationsRead = false;
	
	//background scrolling variables
	float bgY = 0;
	float bgvelY = 0.9f;
	
	//enemies
	ShootingBulletsEnemy shootingBulletsEnemy = new ShootingBulletsEnemy(50, 530, 0);
	SimpleEnemy simpleEnemy = new SimpleEnemy(60, 400, 0);
	ShootingEnemy shootingEnemy = new ShootingEnemy(60, 220, 0);
	ShootingFreezingBulletsEnemy shootingFreezingBulletsEnemy = new ShootingFreezingBulletsEnemy(50, 285, 0);
	
	//timers for button clicking
	float buttonClickTimer = 0.3f;
	boolean startButtonClickTimer = false;
	float buttonClickTime = 0;
	boolean canClick = false;
	
	public TutorialScreen(MainGame game, AdHandler handler, boolean toggle){
		
		//objects
		this.game = game;
		this.handler = handler;
		this.toggle = toggle;

		if(MainGame.IS_MOBILE)
		handler.showAds(toggle);

		//loading data from save file
		prefs = Gdx.app.getPreferences("spaceraiders");
		this.firstTimePlay = prefs.getInteger("firstTimePlay", 0);
		//prefs.putBoolean("progressBar", true);
		if(firstTimePlay == 0){
			prefs.putInteger("firstTimePlay", 1);
			//progressBar = prefs.getBoolean("progressBar", true);
			prefs.flush();
		}
		music = prefs.getBoolean("music", true);
		sfx = prefs.getBoolean("sfx", true);
		replay = prefs.getBoolean("replay", true);
		progressBar = prefs.getBoolean("progressBar", true);
		
		Gdx.input.setCatchBackKey(true);
		
		//sounds
		confirmButton = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/confirmButton.ogg"));
		
		//button font and coordinates
		generatorButton = new FreeTypeFontGenerator(Gdx.files.internal("fonts/venus.ttf"));
		parameterButton = new FreeTypeFontParameter();
		parameterButton.size = 30;
		fontButton = generatorButton.generateFont(parameterButton);
		
		
		//textures
		background = new Texture("tutorial.png");
		background2 = new Texture("background.png");
		rect = new Texture("tutorial2.png");
		rect2 = new Texture("tutorial3.png");
		rect3 = new Texture("tutorial4.png");
		rect4 = new Texture("tutorial5.png");
		notification = new Animation(0.05f, TextureRegion.split(new Texture("notification.png"), 40, 40)[0]);
		modesSpeechBubble = new Texture("messeges/modesSpeechBubble.png");
		shieldSpeechBubble = new Texture("messeges/shieldSpeechBubble.png");
		coinsSpeechBubble = new Texture("messeges/coinsSpeechBubble.png");
		collectiblesSpeechBubble = new Texture("messeges/collectiblesSpeechBubble.png");
		enemySpeechBubble = new Texture("messeges/enemySpeechBubble.png");
		scorePointsSpeechBubble = new Texture("messeges/scorePointsSpeechBubble.png");
		progressBarSpeechBubble = new Texture("messeges/progressBarSpeechBubble.png");
		
		ok = new TextureRegion(new Texture("buttons/buttonsNonMenu.png"), 5*128, 0, 60, 60);
		nextInactive = new TextureRegion(new Texture("menuButtons.png"), 4*300, 140, 70, 70);
		nextInactive.flip(true, false);
		nextActive = new TextureRegion(new Texture("menuButtons.png"), 5*300, 140, 70, 70);
		nextActive.flip(true, false);

		
	}
	
	public void update(float delta){
		statetime += delta;
		
		if((Gdx.input.isKeyJustPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACK))){
			game.setScreen(new HelpScreen(game, handler, true));
			this.dispose();
			
		}
		
		//timer for the buttons
		buttonClickTimer(delta);
		
		if(page == 2){
		simpleEnemy.update(delta);
		shootingEnemy.update(delta);
		backgroundScrolling();
		}
		if(page == 3){
		shootingBulletsEnemy.update(delta);
		shootingFreezingBulletsEnemy.update(delta);
		backgroundScrolling();
		}
		if(page == 4 || page == 5)
		backgroundScrolling();
		
	}

	@Override
	public void render(float delta) {
		
		update(delta);
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.batch.begin();
		
		/********************************************PAGE 1**********************************/
		
		//background
		if(page == 1)
		game.batch.draw(background, 0, 0);
		if(page == 2){
		//background
		game.batch.draw(background2, 0, bgY);
		game.batch.draw(background2, 0, bgY + MainGame.HEIGHT);
		game.batch.draw(rect, 0, 0);
		simpleEnemy.render(game.batch);
		shootingEnemy.render(game.batch);
		}
		if(page == 3){
		game.batch.draw(background2, 0, bgY);
		game.batch.draw(background2, 0, bgY + MainGame.HEIGHT);
		game.batch.draw(rect2, 0, 0);
		shootingBulletsEnemy.render(game.batch);
		shootingFreezingBulletsEnemy.render(game.batch);
		}
		if(page == 4){
		game.batch.draw(background2, 0, bgY);
		game.batch.draw(background2, 0, bgY + MainGame.HEIGHT);
		game.batch.draw(rect3, 0, 0);
		}
		if(page == 5){
		game.batch.draw(background2, 0, bgY);
		game.batch.draw(background2, 0, bgY + MainGame.HEIGHT);
		game.batch.draw(rect4, 0, 0);
		
		}
		
		
		//buttons rendering and updating
		buttons();
		
		//speech bubbles rendering
		speechBubbles();
		
		
		
		game.batch.end();
		
	}


	public void backgroundScrolling(){
		if(bgY > -MainGame.HEIGHT){
			bgY-=bgvelY;
			}
			else
				bgY = 0;
			
	}
	
	public void buttons(){
		float touchX = game.cam.getInputInGameWorld().x;
		//reversing the getY 
		float touchY = MainGame.HEIGHT - game.cam.getInputInGameWorld().y;
		
		
		if(page == 1){
		//notifications
		if(!notificationsRead[1])
		game.batch.draw(notification.getKeyFrame(statetime, true), 30, 620); //playermodes - 1
		if(!notificationsRead[2])
		game.batch.draw(notification.getKeyFrame(statetime, true), 280, 610); //collectibles - 2
		if(!notificationsRead[3])
		game.batch.draw(notification.getKeyFrame(statetime, true), 350, 565); //scorepoints - 3
		if(!notificationsRead[4])
		game.batch.draw(notification.getKeyFrame(statetime, true), 160, 140); //shield - 4
		if(!notificationsRead[5])
		game.batch.draw(notification.getKeyFrame(statetime, true), 100, 30); //enemy - 5
		if(!notificationsRead[6])
		game.batch.draw(notification.getKeyFrame(statetime, true), 150, 400); //coin - 6
		if(!notificationsRead[7])
		game.batch.draw(notification.getKeyFrame(statetime, true), 440, 515); //progress bar - 7
		
		//1 playermodes notification
		if(touchX > 20 && touchX < 20 + 65 && touchY > 610 && touchY < 610 + 65  && !notificationsRead[1]){
			if(Gdx.input.justTouched() && choice == 0){
				choice = 1;
				if(sfx)
				confirmButton.play(0.6f);
			}
		}
		//2 collectibles notification
		if(touchX > 270 && touchX < 270 + 65 && touchY > 600 && touchY < 600 + 65 && !notificationsRead[2]){
			if(Gdx.input.justTouched() && choice == 0){
				choice = 2;
				if(sfx)
					confirmButton.play(0.6f);
			}
		}
		//3 scorepoints notification
		if(touchX > 340 && touchX < 340 + 65 && touchY > 555 && touchY < 555 + 65 && !notificationsRead[3]){
			if(Gdx.input.justTouched() && choice == 0){
				choice = 3;
				if(sfx)
					confirmButton.play(0.6f);
			}
		}		
		//4 shield notification
		if(touchX > 150 && touchX < 150 + 65 && touchY > 130 && touchY < 130 + 65 && !notificationsRead[4]){
			if(Gdx.input.justTouched() && choice == 0){
				choice = 4;
				if(sfx)
					confirmButton.play(0.6f);	
			}
		}	
		//5 enemy notification
		if(touchX > 90 && touchX < 90 + 65 && touchY > 20 && touchY < 20 + 65 && !notificationsRead[5]){
			if(Gdx.input.justTouched() && choice == 0){
				choice = 5;
				if(sfx)
					confirmButton.play(0.6f);
			}
		}	
		//6 coin notification
		if(touchX > 140 && touchX < 140 + 65 && touchY > 390 && touchY < 390 + 65 && !notificationsRead[6]){
			if(Gdx.input.justTouched() && choice == 0){
				choice = 6;
				if(sfx)
					confirmButton.play(0.6f);
			}
		}	
		//7 progress bar notification
		if(touchX > 430 && touchX < 430 + 65 && touchY > 505 && touchY < 505 + 65 && !notificationsRead[7]){
			if(Gdx.input.justTouched() && choice == 0){
				choice = 7;
				if(sfx)
					confirmButton.play(0.6f);
			}
		}	
		
		
		switch(choice){
		//player modes ok button
		case 1: if(touchX > 370 && touchX < 370 + 80 && touchY > 310 && touchY < 310 + 90){
			if(Gdx.input.justTouched()){
			choice = 0;
			notificationsRead[1] = true;
			if(sfx)
				confirmButton.play(0.6f);
				}
			}break;
		//collectibles ok button
		case 2: if(touchX > 340 && touchX < 340 + 80 && touchY > 100 && touchY < 100 + 90){
			if(Gdx.input.justTouched()){
			choice = 0;
			notificationsRead[2] = true;
			if(sfx)
				confirmButton.play(0.6f);
				}
			}break;
		//scorePoints ok button
		case 3: if(touchX > 340 && touchX < 340 + 80 && touchY > 250 && touchY < 250 + 90){
			if(Gdx.input.justTouched()){
			choice = 0;
			notificationsRead[3] = true;
			if(sfx)
				confirmButton.play(0.6f);
				}
			}break;
		//shield ok button
		case 4: if(touchX > 290 && touchX < 290 + 80 && touchY > 190 && touchY < 190 + 90){
			if(Gdx.input.justTouched()){
			choice = 0;
			notificationsRead[4] = true;
			if(sfx)
				confirmButton.play(0.6f);
				}
			}break;
		//enemy ok button
		case 5: if(touchX > 330 && touchX < 330 + 80 && touchY > 70 && touchY < 70 + 90){
			if(Gdx.input.justTouched()){
			choice = 0;
			notificationsRead[5] = true;
			if(sfx)
				confirmButton.play(0.6f);
				}
			}break;
		//coins ok button
		case 6: if(touchX > 310 && touchX < 310 + 80 && touchY > 20 && touchY < 20 + 90){
			if(Gdx.input.justTouched()){
			choice = 0;
			notificationsRead[6] = true;
			if(sfx)
				confirmButton.play(0.6f);
				}
			}break;
		//progress bar ok button
		case 7: if(touchX > 350 && touchX < 350 + 80 && touchY > 160 && touchY < 160 + 90){
			if(Gdx.input.justTouched()){
			choice = 0;
			notificationsRead[7] = true;
			if(sfx)
				confirmButton.play(0.6f);
				}
			}break;
		}
		
		//checks if all notifications are read
		if(!isAllNotificationsRead){
			int checked = 0;
		for(int i = 0; i < notificationsRead.length; i++){
			if(notificationsRead[0] != notificationsRead[i])
				checked++;
			
			if(checked >= notificationsRead.length-1)
				isAllNotificationsRead = true;
			
		}
		}
		
		//if all notifications are read draw the button for the next page
		if(isAllNotificationsRead){
			if(touchX > 190 && touchX < 190 + 90 && touchY > 0 && touchY < 0 + 100){
			game.batch.draw(nextActive, 200, 10);
			if(Gdx.input.justTouched()){
				page = 2;
				startButtonClickTimer = true;
				if(sfx)
					confirmButton.play(0.6f);
				}
			}
			else{
				
				game.batch.draw(nextInactive, 200, 10);
			}
		}
	}
		
		if(page == 2){
			if(touchX > 190 && touchX < 190 + 90 && touchY > 0 && touchY < 0 + 100){
				game.batch.draw(nextActive, 200, 0);
				if(Gdx.input.justTouched() && canClick){
					page = 3;
					startButtonClickTimer = true;
					canClick = false;
					if(sfx)
						confirmButton.play(0.6f);
					}
				}
				else{
					
					game.batch.draw(nextInactive, 200, 0);
				}
		}
		
		if(page == 3){
			if(touchX > 190 && touchX < 190 + 90 && touchY > 0 && touchY < 0 + 100){
				game.batch.draw(nextActive, 200, 0);
				if(Gdx.input.justTouched() && canClick){
					page = 4;
					startButtonClickTimer = true;
					canClick = false;
					if(sfx)
						confirmButton.play(0.6f);
					}
				}
				else{
					game.batch.draw(nextInactive, 200, 0);
				}
		}
		if(page == 4){
			if(touchX > 190 && touchX < 190 + 90 && touchY > 0 && touchY < 0 + 100){
				game.batch.draw(nextActive, 200, 0);
				if(Gdx.input.justTouched() && canClick){
					page = 5;
					startButtonClickTimer = true;
					canClick = false;
					if(sfx)
						confirmButton.play(0.6f);
					}
				}
				else{
					game.batch.draw(nextInactive, 200, 0);
				}
		}
		if(page == 5){
			if(touchX > 190 && touchX < 190 + 90 && touchY > 0 && touchY < 0 + 100){
				game.batch.draw(nextActive, 200, 0);
				if(Gdx.input.justTouched() && canClick){
					canClick = false;
					if(sfx)
						confirmButton.play(0.6f);
					game.setScreen(new MainMenuScreen(game, false, handler, false));
					this.dispose();
					}
				}
				else{
					game.batch.draw(nextInactive, 200, 0);
				}
		}
	}
	
	private void speechBubbles(){
		if(page == 1){
		switch(choice){
		case 1: game.batch.draw(modesSpeechBubble, 30 + 45, 340);
				game.batch.draw(ok, 380, 320);
				break;
		case 2: game.batch.draw(collectiblesSpeechBubble, 10, 115);
				game.batch.draw(ok, 350, 115);
				break;
		case 3: game.batch.draw(scorePointsSpeechBubble, 10, 210);
				game.batch.draw(ok, 350, 260);
				break;
		case 4: game.batch.draw(shieldSpeechBubble, 30, 175);
				game.batch.draw(ok, 300, 200);
				break;
		case 5: game.batch.draw(enemySpeechBubble, 80, 70);
				game.batch.draw(ok, 340, 80);
				break;
		case 6: game.batch.draw(coinsSpeechBubble, 20, 40);
				game.batch.draw(ok, 320, 30);
				break;
		case 7: game.batch.draw(progressBarSpeechBubble, 85, 170);
				game.batch.draw(ok, 360, 170);
				break;
		
			}
		
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
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		background.dispose();
		background2.dispose();
		rect.dispose();
		rect2.dispose();
		shootingEnemy.dispose();
		simpleEnemy.dispose();
		shootingBulletsEnemy.dispose();
		shootingFreezingBulletsEnemy.dispose();
		confirmButton.dispose();
		
		System.out.println("Tutorial Screen Disposed.");
	}

}
