package com.kiril.tools;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ScorePoints {

	TextureRegion score;
	
	Texture scorePoints;
	
	float x, y;
	float statetimeScore, statetimeCount;
	
	float show_lenghtScore;
	float show_lenghtCount;
	float alpha_color;

	public boolean remove = false;
	
	float velY;
	
	String type;
	
	public ScorePoints(String type){
		x = 360;
		y = 650;
		
		this.type = type;
		
		statetimeScore = 0;
		statetimeCount = 0;
		alpha_color = 1;
		show_lenghtScore = 0.7f;
		show_lenghtCount = 1.2f;
		
		velY = 120;
		
		scorePoints = new Texture("scorePoints.png");
		
		if(type == "plus100")
			score = new TextureRegion(scorePoints, 0, 0, 100, 33);
		if(type == "plus500")
			score = new TextureRegion(scorePoints, 1*100, 0, 100, 33);
		if(type == "plus600")
			score = new TextureRegion(scorePoints, 2*100, 0, 100, 33);
		if(type == "plus1000")
			score = new TextureRegion(scorePoints, 3*100, 0, 100, 33);
		if(type == "plus200")
			score = new TextureRegion(scorePoints, 4*100, 0, 100, 33);
		if(type == "minus100")
			score = new TextureRegion(scorePoints, 5*100, 0, 100, 33);
		if(type == "minus300")
			score = new TextureRegion(scorePoints, 6*100, 0, 100, 33);
		if(type == "minus400")
			score = new TextureRegion(scorePoints, 7*100, 0, 100, 33);
		if(type == "minus1000")
			score = new TextureRegion(scorePoints, 8*100, 0, 100, 33);
		if(type == "plus4000")
			score = new TextureRegion(scorePoints, 9*100, 0, 100, 33);

	
	}
	
	public void update(float deltaTime){
		
		//moving down
		y-=velY * deltaTime;
		
		//updating the timer for the score textures
		statetimeScore += deltaTime;
		
		//updating the timer for the counter textures
		statetimeCount += deltaTime;	
		
		//updating the alpha color
		alpha_color -= deltaTime;
		
		
		if(statetimeScore >= show_lenghtScore)
			remove = true;

		if(statetimeCount >= show_lenghtCount)
			remove = true;
	}
	
	public void render(SpriteBatch batch){
		
		batch.setColor(1, 1, 1, alpha_color);
		
		batch.draw(score, x, y);
		

	}
	
	public void dispose(){
		scorePoints.dispose();
		
		
	}
}
