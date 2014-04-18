package org.worldsproject.game.runningman.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.worldsproject.game.runningman.RunningMan;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Running Man";
        config.width = 800;
        config.height = 480;
		new LwjglApplication(new RunningMan(), config);
	}
}
