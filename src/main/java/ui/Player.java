package ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.awt.*;

public class Player extends Sprite {

    private Point gridPos;

    Player(Point startingPoint) {
        super(new Texture("pirate.png"));
        gridPos = startingPoint;
    }

    public Point getGridPos() {
        return gridPos;
    }

    public void setGridPos(Point newPos) {
        gridPos = newPos;
    }

}
