package com.kiril.tools;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kiril.main.game.MainGame;
import com.kiril.screens.GameScreen;

public class PlayerModes {

	//mode type
	String type;
	
	//objects
	GameScreen game;
	TextureRegion luckMode, shieldMode, freezingBulletsMode, tripleFireBulletsMode, quadFireBulletsMode, fasterPlayerMode, fasterBulletsMode,
				  slowerPlayerMode, pentaFireBulletsMode;
	
	
	//coordinates
	final float DEFAULT_X = 10;
	float DEFAULT_Y = MainGame.HEIGHT - 80;
	
	//textures (modes)
	Texture modes;
	
	//fading variables
	float alpha_color = 1;
	float show_lenght;
	float statetime = 0;
	
	//removing boolean
	public boolean remove = false;
	
	
	public PlayerModes(String type, GameScreen game){
		this.type = type;
		this.game = game;
		
	
		DEFAULT_Y = MainGame.HEIGHT - 80 - (game.playerModesNumber() * 45);
		
		//luck mode
		if(type == "luckMode")
			show_lenght = 14;
		
		//shield mode
		if(type == "shieldMode"){
			switch(game.getLevel()){
			case 11: show_lenght = 18f; break;
			case 12: show_lenght = 4f; break;
			case 17: show_lenght = 18f; break;
			case 20: show_lenght = 9f; break;
			case 23: show_lenght = 18f; break;
			default : show_lenght = 6f; break;
			}
		}
		
		//faster player mode
		if(type == "fastPlayerMode")
			show_lenght = 12;
		
		//freezing bullets mode
		if(type == "freezingBulletsMode"){
			switch(game.getLevel()){
			case 13: show_lenght = 140f; break;
			case 27: show_lenght = 40f; break;
			case 31: show_lenght = 240f; break;
			default : show_lenght = 12f; break;
			}
		}
		
		//tripe fire bullets mode
		if(type == "tripleFireBulletsMode"){
			switch(game.getLevel()){
			case 18: show_lenght = 300f; break;
			case 19: show_lenght = 14f; break;
			case 26: show_lenght = 360f; break;
			case 29: show_lenght = 300f; break;
			default : show_lenght = 12f; break;
			}
			
		}
		
		//quad fire bullets mode
		if(type == "quadFireBulletsMode"){
			switch(game.getLevel()){
			case 11: show_lenght = 18; break;
			case 19: show_lenght = 14; break;
			default: show_lenght = 11f; break;
			}
			
		}
		
		
		if(type == "pentaFireBulletsMode"){
			//if(game.getLevel() == 17 || game.getLevel() == 23 || game.getLevel() == 28)
			show_lenght = 18;
		}
		
		//faster player mode
		if(type == "fasterPlayerMode"){
			switch(game.getLevel()){
			case 13: show_lenght = 140; break;
			case 18: show_lenght = 140; break;
			case 29: show_lenght = 140; break;
			default: show_lenght = 12f; break;
			}
			if(game.getLevel() != 13 || game.getLevel() != 18 || game.getLevel() != 29)
			show_lenght = 12;
			if(game.getLevel() == 13 || game.getLevel() == 18 || game.getLevel() == 29)
			show_lenght = 140;
			
		}
		
		//faster bullets mode
		if(type == "fasterBulletsMode")
			show_lenght = 12;
		
		//slower player mode
		if(type == "slowerPlayerMode")
			show_lenght = 10;
		
		modes = new Texture("modes.png");
		luckMode = new TextureRegion(modes, 0, 0, 40, 40);
		freezingBulletsMode = new TextureRegion(modes, 40, 0, 40, 40);
		shieldMode = new TextureRegion(modes, 80, 0, 40, 40);
		quadFireBulletsMode = new TextureRegion(modes, 120, 0, 40, 40);
		fasterPlayerMode = new TextureRegion(modes, 160, 0, 40, 40);
		tripleFireBulletsMode = new TextureRegion(modes, 200, 0, 40, 40);
		fasterBulletsMode = new TextureRegion(modes, 240, 0, 40, 40);
		slowerPlayerMode = new TextureRegion(modes, 280, 0, 40, 40);
		pentaFireBulletsMode = new TextureRegion(modes, 320, 0, 40, 40);
		
	}
	
	public void update(float deltaTime){
		
		statetime += deltaTime;
		
		
		//updating the alpha color for the luck mode
		if(type == "luckMode"){
		if(statetime >= show_lenght - 4)
		alpha_color -= 0.03f;
		if(statetime >= show_lenght - 2)
		alpha_color -= 0.06f;
		if(statetime >= show_lenght - 1)
		alpha_color -= 0.1f;
		
		if(statetime >= show_lenght)
			game.setLucky(1);
		
		}
		
		if(type == "shieldMode"){
		if(statetime >= show_lenght - 2)
		alpha_color -= 0.03f;
		if(statetime >= show_lenght - 1)
		alpha_color -= 0.06f;
		if(statetime >= show_lenght - 0.8)
		alpha_color -= 0.1f;
		
		}
		
		if(type == "freezingBulletsMode"){
		if(statetime >= show_lenght - 3)
		alpha_color -= 0.03f;
		if(statetime >= show_lenght - 2)
		alpha_color -= 0.06f;
		if(statetime >= show_lenght - 0.8)
		alpha_color -= 0.1f;
		
		if(statetime >= show_lenght)
			game.setFreezingBullets(false);
		
		
		}
		
		if(type == "fasterPlayerMode"){
		if(statetime >= show_lenght - 3)
		alpha_color -= 0.03f;
		if(statetime >= show_lenght - 2)
		alpha_color -= 0.06f;
		if(statetime >= show_lenght - 1)
		alpha_color -= 0.1f;
		
		if(statetime >= show_lenght)
			game.setSPEED(320);
		}
		
		if(type == "tripleFireBulletsMode"){
		if(statetime >= show_lenght - 3)
		alpha_color -= 0.03f;
		if(statetime >= show_lenght - 2)
		alpha_color -= 0.06f;
		if(statetime >= show_lenght - 1)
		alpha_color -= 0.1f;
	
		if(statetime >= show_lenght)
		game.setFireBullets(2);
		}
		
		if(type == "quadFireBulletsMode"){
		if(statetime >= show_lenght - 3)
		alpha_color -= 0.03f;
		if(statetime >= show_lenght - 2)
		alpha_color -= 0.06f;
		if(statetime >= show_lenght - 1)
		alpha_color -= 0.1f;
	
		if(statetime >= show_lenght)
		game.setFireBullets(2);
		}
		
		if(type == "pentaFireBulletsMode"){
		if(statetime >= show_lenght - 3)
		alpha_color -= 0.03f;
		if(statetime >= show_lenght - 2)
		alpha_color -= 0.06f;
		if(statetime >= show_lenght - 1)
		alpha_color -= 0.1f;
	
		if(statetime >= show_lenght)
		game.setFireBullets(2);
		}
		
		if(type == "fasterBulletsMode"){
		if(statetime >= show_lenght - 3)
		alpha_color -= 0.03f;
		if(statetime >= show_lenght - 2)
		alpha_color -= 0.06f;
		if(statetime >= show_lenght - 1)
		alpha_color -= 0.1f;
		
		if(statetime >= show_lenght)
			game.setFasterBullets(false);
		}
			
		if(type == "slowerPlayerMode"){
		if(statetime >= show_lenght - 3)
		alpha_color -= 0.03f;
		if(statetime >= show_lenght - 2)
		alpha_color -= 0.06f;
		if(statetime >= show_lenght - 1)
		alpha_color -= 0.1f;
		
		if(statetime >= show_lenght)
			game.setSlowerPlayer(false);
		}

		
		if(statetime >= show_lenght){
			remove = true;
			
			}
		
		
	}
	
	public void render(SpriteBatch batch){
		
		batch.setColor(1, 1, 1, alpha_color);
		
		if(type == "luckMode")	
		batch.draw(luckMode, DEFAULT_X, DEFAULT_Y);
		
		if(type == "shieldMode")
		batch.draw(shieldMode, DEFAULT_X, DEFAULT_Y);
		
		if(type == "freezingBulletsMode")
		batch.draw(freezingBulletsMode, DEFAULT_X, DEFAULT_Y);
		
		if(type == "tripleFireBulletsMode")
		batch.draw(tripleFireBulletsMode, DEFAULT_X, DEFAULT_Y);
		
		if(type == "quadFireBulletsMode")
		batch.draw(quadFireBulletsMode, DEFAULT_X, DEFAULT_Y);
		
		if(type == "pentaFireBulletsMode")
		batch.draw(pentaFireBulletsMode, DEFAULT_X, DEFAULT_Y);
		
		if(type == "fasterPlayerMode")
		batch.draw(fasterPlayerMode, DEFAULT_X, DEFAULT_Y);
		
		if(type == "fasterBulletsMode")
		batch.draw(fasterBulletsMode, DEFAULT_X, DEFAULT_Y);
		
		if(type == "slowerPlayerMode")
		batch.draw(slowerPlayerMode, DEFAULT_X, DEFAULT_Y);
		
		batch.setColor(Color.WHITE);
		
	}
	
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void dispose(){
		modes.dispose();
		System.out.println("PlayerModes Disposed.");
	}

	public float getDEFAULT_Y() {
		return DEFAULT_Y;
	}

	public void setDEFAULT_Y(float dEFAULT_Y) {
		DEFAULT_Y = dEFAULT_Y;
	}
	
	
	
}
