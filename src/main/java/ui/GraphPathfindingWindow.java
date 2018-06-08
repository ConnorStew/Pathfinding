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
import data.Edge;
import data.Graph;
import data.Vertex;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class GraphPathfindingWindow implements ApplicationListener {

    private OrthographicCamera cam;
    private BitmapFont font;
    private ShapeRenderer sr;
    private SpriteBatch sb;

    private Graph graph;

    private LinkedList<Tile> tiles = new LinkedList<Tile>();
    private Tile start, goal;

    private static final int tileAmount = 32;
    private static float tileWidth, tileHeight;
    private float inputTime = 0;

    @Override
    public void create() {
        cam = new OrthographicCamera(900,700);
        sr = new ShapeRenderer();
        sr.setAutoShapeType(true);
        sb = new SpriteBatch();
        graph = new Graph();
        start = null;
        goal = null;

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
                tiles.add(new Tile(x, y,tileWidth * x, tileHeight * y, tileWidth, tileHeight));

        for (Tile tile : tiles)
            if (tile.getGridX() > 0 && tile.getGridX() < tileAmount - 1 && tile.getGridY() > 0 && tile.getGridY() < tileAmount - 1)
                tile.setFilled(false);

        modifyTiles(true,20,10,10,5);
        modifyTiles(true,5,20,10,10);
        modifyTiles(true,5,10,10,4);
        modifyTiles(true,2,2,10,10);
        modifyTiles(true,18,2,7,5);
        modifyTiles(true,18,20,10,8);

        modifyTiles(false,5,4,8,4);

        //add vertices to graph
        for (Tile tile : tiles)
            if (!tile.isFilled())
                graph.addVertex(new Vertex(tile.getCenterX(), tile.getCenterY()));

        //add edges to graph
        for (Tile tile : tiles)
            if (!tile.isFilled())
                addEdges(tile);

        //add neighbours to graph
        for (Tile tile : tiles)
            if (!tile.isFilled())
                addNeighbours(tile);
    }

    private void addNeighbours(Tile tile) {
        Tile left = getTileAt(new Point(tile.getGridX() - 1, tile.getGridY()));
        Tile right = getTileAt(new Point(tile.getGridX() + 1, tile.getGridY()));
        Tile top = getTileAt(new Point(tile.getGridX(), tile.getGridY() + 1));
        Tile bottom = getTileAt(new Point(tile.getGridX(), tile.getGridY() - 1));

        addNeighbour(tile, left);
        addNeighbour(tile, right);
        addNeighbour(tile, top);
        addNeighbour(tile, bottom);
    }

    private void addNeighbour(Tile start, Tile toAdd) {
        if (toAdd != null && !toAdd.isFilled()) {
            Vertex vertexToAdd = graph.getVertexAt(toAdd.getCenterX(), toAdd.getCenterY());
            Vertex baseVertex = graph.getVertexAt(start.getCenterX(), start.getCenterY());
            baseVertex.addNeighbour(vertexToAdd);
        }
    }

    private void addEdges(Tile tile) {
        Tile left = getTileAt(new Point(tile.getGridX() - 1, tile.getGridY()));
        Tile right = getTileAt(new Point(tile.getGridX() + 1, tile.getGridY()));
        Tile top = getTileAt(new Point(tile.getGridX(), tile.getGridY() + 1));
        Tile bottom = getTileAt(new Point(tile.getGridX(), tile.getGridY() - 1));

        addEdge(tile, left);
        addEdge(tile, right);
        addEdge(tile, top);
        addEdge(tile, bottom);
    }

    private void addEdge(Tile start, Tile toAdd) {
        if (toAdd != null && !toAdd.isFilled())
            graph.addEdge(graph.getVertexAt(start.getCenterX(), start.getCenterY()), graph.getVertexAt(toAdd.getCenterX(), toAdd.getCenterY()), 0);
    }

    private void modifyTiles(boolean fill, int startX, int startY, int width, int height) {
        for (Tile tile : tiles)
            if (tile.getGridX() >= startX && tile.getGridX() <= startX + width && tile.getGridY() >= startY && tile.getGridY() <= startY + height)
                tile.setFilled(fill);
    }

    private Tile getTileAt(Point point) {
        for (Tile tile : tiles)
            if (tile.getGridPoint().equals(point))
                return tile;

        return null;
    }

    @Override
    public void render() {
        inputTime += Gdx.graphics.getDeltaTime();

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && inputTime > 0.05) {
            Vector2 mp = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            inputTime = 0;

            for (Tile tile : tiles) {
                if (tile.contains(mp)) {
                    Tile invertedTile = getTileAt(new Point(tile.getGridX(), tileAmount - tile.getGridY() - 1));
                    if (!invertedTile.isFilled()) {
                        if (start == null) {
                            start = invertedTile;
                        } else {
                            goal = invertedTile;
                            new Thread(){
                                @Override
                                public void run() {
                                    graph.aStar(graph.getVertexAt(start.getCenterX(), start.getCenterY()), graph.getVertexAt(goal.getCenterX(), goal.getCenterY()));
                                }
                            }.start();

                        }
                    }
                }
            }
        }

        sr.begin();

        for (Tile tile : tiles) {
            Color fill;
            Color border = Color.BLACK;

            if (tile.isFilled()) {
                fill = Color.BLACK;
                border = Color.WHITE;
            } else {
                fill = Color.WHITE;
            }

            sr.setColor(fill);
            sr.set(ShapeRenderer.ShapeType.Filled);
            sr.rect(tile.getWorldX(), tile.getWorldY(), tileWidth, tileHeight);

            sr.setColor(border);
            sr.set(ShapeRenderer.ShapeType.Line);
            sr.rect(tile.getWorldX(), tile.getWorldY(), tileWidth, tileHeight);
        }

        sr.setColor(Color.BLACK);
        sr.set(ShapeRenderer.ShapeType.Filled);
        for (Edge edge : graph.getEdges()) {
            Vertex v1 = edge.getFirstVertex();
            Vertex v2 = edge.getSecondVertex();
            sr.rectLine(v1.getX(), v1.getY(), v2.getX(), v2.getY(), 5);
        }

        List<Vertex> openList = graph.getOpenList();
        List<Vertex> closedList = graph.getClosedList();
        List<Vertex> path = graph.getPath();

        for (Vertex vertex : graph.getVertices()) {
            Color vertexColour = Color.BLACK;

            if (openList.contains(vertex))
                vertexColour = Color.LIME;

            if (closedList.contains(vertex))
                vertexColour = Color.RED;

            if (path.contains(vertex))
                vertexColour = Color.MAGENTA;

            sr.setColor(vertexColour);
            sr.circle(vertex.getX(), vertex.getY(), 5);
        }

        //drawNeighboursAt(new Point(13,9));
        //drawNeighboursAt(new Point(4,17));

        sr.end();
    }

    /**
     * Draws the vertex at the given point's neighbours.
     * @param gridPoint the point of the vertex
     */
    private void drawNeighboursAt(Point gridPoint) {
        Tile tile = getTileAt(gridPoint);
        Vertex baseVertex = graph.getVertexAt(tile.getCenterX(), tile.getCenterY());

        sr.setColor(Color.ORANGE);
        for (Vertex neighbour : baseVertex.getNeighbours())
            sr.circle(neighbour.getX(), neighbour.getY(), 5);

        sr.setColor(Color.BLUE);
        sr.circle(baseVertex.getX(), baseVertex.getY(), 5);
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
