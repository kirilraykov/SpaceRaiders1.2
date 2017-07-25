package com.kiril.entities;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kiril.screens.GameScreen;
import com.kiril.tools.CollisionRect;

public class Shield {
	
	Texture shield;
	
	public float x, y;
	float statetime;
	
	float show_lenght;
	float alpha_color;

	Random rand;
	
	public boolean remove = false;
	public boolean isShield = false;
	
	GameScreen game;
	
	//collision rect
	CollisionRect rect;
	
	public Shield(float x, float y, GameScreen game, float show_lenght){
		this.x = x;
		this.y = y;
		this.game = game;
		this.show_lenght = show_lenght;
		isShield = true;
		
		statetime = 0;
		alpha_color = 1;
		
		rand = new Random();
		
		shield = new Texture("shield.png");
		
		//collision rect
		this.rect = new CollisionRect(x, y, 200, 128);
	
	}
	
	public void update(float deltaTime){
	
		this.x = game.getX() - 50;
		this.y = game.getY() + 20;
		
		//updating the timer
		statetime += deltaTime;
		
		//updating the alpha color
		if(game.getLevel() != 12){
		if(statetime >= show_lenght - 3)
		alpha_color -= 0.03f;
		if(statetime >= show_lenght - 2)
			alpha_color -= 0.06f;
		if(statetime >= show_lenght - 1)
			alpha_color -= 0.1f;
		}
		
		if(game.getLevel() == 12){
		if(statetime >= show_lenght - 2)
			alpha_color -= 0.03f;
		if(statetime >= show_lenght - 1)
			alpha_color -= 0.06f;
		if(statetime >= show_lenght - 0.5f)
			alpha_color -= 0.1f;
		
		}
		
		
		if(statetime >= show_lenght){
			remove = true;
			isShield = false;
		}
		
		//moving the rect with the shield
		rect.move(x, y);
	}
	
	public void render(SpriteBatch batch){
		
		//if(statetime <= 4)
		batch.setColor(Color.WHITE);
		//else
		batch.setColor(1, 1, 1, alpha_color);
		
			
		batch.draw(shield, x, y);

	}

	public CollisionRect getRect() {
		return rect;
	}

	public void dispose(){
		shield.dispose();
	}
	
}
