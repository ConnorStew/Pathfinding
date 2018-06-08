package ui;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Driver {

    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Pathfinder";
        config.width = 900;
        config.height = 700;
        config.resizable = false;
        new LwjglApplication(new GraphPathfindingWindow(), config);

    }
}
