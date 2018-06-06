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

import java.awt.*;
import java.util.HashMap;

public class PathfindingWindow implements ApplicationListener {

    OrthographicCamera cam;

    BitmapFont font;

    ShapeRenderer sr;
    private SpriteBatch sb;

    HashMap<Point, Tile> tiles = new HashMap<Point, Tile>();

    LogicThread logicThread;

    public static final int tileAmount = 32;

    @Override
    public void create() {
        cam = new OrthographicCamera(900,700);
        sr = new ShapeRenderer();
        sr.setAutoShapeType(true);
        sb = new SpriteBatch();
        //sb.setProjectionMatrix(cam.combined);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("TheLightFont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 12;

        font = generator.generateFont(parameter);
        font.setColor(Color.BLUE);

        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
        generator.dispose();

        font.setUseIntegerPositions(false);
        //font.getData().setScale(0.75f);

        final float windowWidth = Gdx.graphics.getWidth();
        final float windowHeight = Gdx.graphics.getHeight();
        final float tileWidth = (windowWidth / tileAmount);
        final float tileHeight = (windowHeight / tileAmount);

        for (int x = 0; x < tileAmount; x++)
            for (int y = 0; y < tileAmount; y++)
                tiles.put(new Point(x,y), new Tile(x, y,tileWidth * x, tileHeight * y, tileWidth, tileHeight));

        for (int x = 1; x < tileAmount - 1; x++)
            for (int y = 1; y < tileAmount - 1; y++)
                tiles.get(new Point(x,y)).setFilled(false);

        fillRect(20,10,10,5);
        fillRect(10,20,5,10);
        fillRect(5,20,10,10);
        fillRect(2,2,10,10);
        fillRect(13,12,7,2);
        tiles.get(new Point(30,14)).setFilled(true);

        logicThread = new LogicThread(tiles);
        logicThread.start();
    }

    private void fillRect(int startX, int startY, int width, int height) {
        for (int x = startX; x < startX + width; x++)
            for (int y = startY; y < startY + height; y++)
                tiles.get(new Point(x,y)).setFilled(true);
    }

    @Override
    public void render() {
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            Vector2 mp = new Vector2(Gdx.input.getX(), Gdx.input.getY());

            for (int x = 0; x < tileAmount; x++)
                for (int y = 0; y < tileAmount; y++)
                    if (tiles.get(new Point(x,y)).contains(mp))
                        click(tiles.get(new Point(x, tileAmount - y - 1))); //invert y axis
        }

        logicThread.renderDebug(sb, sr, font);
    }

    private void click(Tile clickedTile) {
        logicThread.click(clickedTile);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
