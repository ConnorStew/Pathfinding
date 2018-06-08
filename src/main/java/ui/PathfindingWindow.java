package ui;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import pathfinding.Node;
import pathfinding.VisibilityGraph;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;

public class PathfindingWindow implements ApplicationListener {

    OrthographicCamera cam;

    BitmapFont font;

    ShapeRenderer sr;

    private VisibilityGraph vg;

    private SpriteBatch sb;

    HashMap<Point, Tile> tiles = new HashMap<Point, Tile>();

    public static final int tileAmount = 32;

    public static float tileWidth, tileHeight;

    private float inputTime = 0;

    Player player;

    @Override
    public void create() {
        cam = new OrthographicCamera(900,700);
        sr = new ShapeRenderer();
        sr.setAutoShapeType(true);
        sb = new SpriteBatch();
        vg = new VisibilityGraph();
        player = new Player(new Point(1,1));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("TheLightFont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 12;

        font = generator.generateFont(parameter);
        font.setColor(Color.BLUE);

        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
        generator.dispose();

        font.setUseIntegerPositions(false);

        final float windowWidth = Gdx.graphics.getWidth();
        final float windowHeight = Gdx.graphics.getHeight();
        tileWidth = (windowWidth / tileAmount);
        tileHeight = (windowHeight / tileAmount);

        for (int x = 0; x < tileAmount; x++)
            for (int y = 0; y < tileAmount; y++)
                tiles.put(new Point(x,y), new Tile(x, y,tileWidth * x, tileHeight * y, tileWidth, tileHeight));

        for (int x = 1; x < tileAmount - 1; x++)
            for (int y = 1; y < tileAmount - 1; y++)
                tiles.get(new Point(x,y)).setFilled(false);

        fillRect(20,10,10,5);
        fillRect(10,15,5,5);
        fillRect(10,20,5,10);
        fillRect(5,20,10,10);
        fillRect(5,10,10,4);
        fillRect(2,2,10,10);
        fillRect(13,12,7,2);
        fillRect(18,2,7,5);
        fillRect(18,20,10,8);
        tiles.get(new Point(30,14)).setFilled(true);


        /*
        for (int x = 0; x < PathfindingWindow.tileAmount; x++)
            for (int y = 0; y < PathfindingWindow.tileAmount; y++)
                vg.addNode(tiles.get(new Point(x,y)).getNode());
                */
    }

    private void fillRect(int startX, int startY, int width, int height) {
        for (int x = startX; x < startX + width; x++)
            for (int y = startY; y < startY + height; y++)
                tiles.get(new Point(x,y)).setFilled(true);
    }

    @Override
    public void render() {
        inputTime += Gdx.graphics.getDeltaTime();

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && inputTime > 1000 / 1000) {
            Vector2 mp = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            inputTime = 0;
            for (int x = 0; x < tileAmount; x++) {
                for (int y = 0; y < tileAmount; y++) {
                    if (tiles.get(new Point(x,y)).contains(mp)) {

                        Tile clickedTile = tiles.get(new Point(x, tileAmount - y - 1)); //invert y axis
                        //vg.initialise(clickedTile.getNode());

                        if (vg.isReady())
                            new Thread(){
                                @Override
                                public void run() {
                                    vg.aStar(true);

                                }
                            }.start();

                        break;
                    }
                }
            }
        }


        /*
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            Vector2 mp = new Vector2(Gdx.input.getX(), Gdx.input.getY());

            for (int x = 0; x < tileAmount; x++) {
                for (int y = 0; y < tileAmount; y++) {
                    if (tiles.get(new Point(x, y)).contains(mp)) {
                        Tile clickedTile = tiles.get(new Point(x, tileAmount - y - 1)); //invert y axis
                        vg.clear();
                        vg.setStart(vg.getNodeAt(player.getGridPos()));
                        vg.setGoal(clickedTile.getNode());
                        if (vg.isReady())
                            vg.aStar(true);

                        break;
                    }
                }
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        */
        sr.begin();

        for (int x = 0; x < PathfindingWindow.tileAmount; x++) {
            for (int y = 0; y < PathfindingWindow.tileAmount; y++) {
                tiles.get(new Point(x, y)).render(sr);
            }
        }


        sr.end();

        vg.renderDebug(sb, sr, font);

        Point oldPos = player.getGridPos();
        Point newPos = new Point(player.getGridPos());

        if (Gdx.input.isKeyPressed(Input.Keys.W))
            newPos.y++;

        if (Gdx.input.isKeyPressed(Input.Keys.S))
            newPos.y--;

        if (Gdx.input.isKeyPressed(Input.Keys.D))
            newPos.x++;

        if (Gdx.input.isKeyPressed(Input.Keys.A))
            newPos.x--;

        Point newPlayerWorldPos = vg.toWorldPos(newPos);
        Point oldPlayerWorldPos = vg.toWorldPos(oldPos);

        if (newPlayerWorldPos != null) {
            player.setGridPos(newPos);
        }

        sb.begin();
        sb.draw(player, oldPlayerWorldPos.x,  oldPlayerWorldPos.y);
        sb.end();

    }


    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {}
}
