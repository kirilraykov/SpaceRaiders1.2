package com.kiril.main.game.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.kiril.main.game.MainGame;
	
	

public class DesktopLauncher {
	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		
		config.foregroundFPS = 60;
		config.width = MainGame.WIDTH;
		config.height = MainGame.HEIGHT;
		config.resizable = true;
		config.addIcon("icon/ICON128.png", FileType.Internal);
		config.addIcon("icon/ICON32.png", FileType.Internal);
		config.addIcon("icon/ICON16.png", FileType.Internal);
		new LwjglApplication(new MainGame(null), config);
	}
}
