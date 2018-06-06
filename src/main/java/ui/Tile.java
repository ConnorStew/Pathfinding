package ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import pathfinding.Node;

import java.awt.*;

public class Tile {

    private float width, height, worldX, worldY;
    private int gridX, gridY;

    private Node node;

    private boolean filled;

    public Tile(int gridX, int gridY, float worldX, float worldY, float tileWidth, float tileHeight) {
        filled = true;
        this.gridX = gridX;
        this.gridY = gridY;
        this.worldX = worldX;
        this.worldY = worldY;
        this.width = tileWidth;
        this.height = tileHeight;
    }

    public void render(ShapeRenderer sr) {
        if (filled)
            sr.setColor(Color.BLACK);
        else
            sr.setColor(Color.WHITE);

        sr.set(ShapeRenderer.ShapeType.Filled);
        sr.rect(worldX, worldY, width, height);

        if (filled)
            sr.setColor(Color.WHITE);
        else
            sr.setColor(Color.BLACK);
        sr.set(ShapeRenderer.ShapeType.Line);
        sr.rect(worldX, worldY,width,height);
    }

    public void render(ShapeRenderer sr, Color colour) {
        sr.setColor(colour);

        sr.set(ShapeRenderer.ShapeType.Filled);
        sr.rect(worldX, worldY, width, height);

        sr.setColor(Color.WHITE);
        sr.set(ShapeRenderer.ShapeType.Line);
        sr.rect(worldX, worldY,width,height);
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
        if (!filled)
            node = new Node(new Point(gridX, gridY), worldX, worldY);
        else
            node = null;
    }

    public boolean contains(Vector2 mp) {
        return  (mp.x > worldX && mp.x < worldX + width && mp.y > worldY && mp.y < worldY + height);
    }

    public float getWorldX() {
        return worldX;
    }

    public float getWorldY() {
        return worldY;
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public boolean isFilled() {
        return filled;
    }

    public float getHeight() {
        return height;
    }

    public Node getNode() {
        return node;
    }

    public boolean hasNode() {
        return node != null;
    }
}
