import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;

public class LogicThread extends Thread {

    private float inputTime = 0;

    private boolean paused = false;

    /** Nodes that could be added to the path. */
    private LinkedList<Node> openList = new LinkedList<Node>();

    /** The path so far. */
    private LinkedList<Node> closedList = new LinkedList<Node>();

    private LinkedList<Node> path = new LinkedList<Node>();

    private HashMap<Point, Tile> tiles;

    private Tile start, goal;
    private boolean running = true;
    private static final int tileAmount = 32;
    private boolean initialised = false;

    LogicThread(HashMap<Point, Tile> tiles) {
        this.tiles = tiles;
    }

    @Override
    public void run() {
        while (running) {
            inputTime += Gdx.graphics.getDeltaTime();
            if (inputTime > 510 * 1000 && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                inputTime = 0;
                paused = false;
            }

            while (!paused && initialised) {
                step();
                paused = true;
            }
        }
    }

    private void step() {
        aStar(start.getNode(), goal.getNode());
    }

    private void aStar(Node start, Node goal) {
        openList = new LinkedList<Node>();
        openList.push(start);
        closedList = new LinkedList<Node>();

        while (!openList.isEmpty()) {
            LinkedList<Node> lowestScores = lowestScore(openList);

            for (Node current : lowestScores) {
                if (current.equals(goal)) {
                    backtrack(current, current.getParent());
                    return;
                }

                openList.remove(current);
                closedList.add(current);

                for (Node neighbor : getAdjacentNodes(current)) {
                    if (closedList.contains(neighbor))
                        continue;

                    if (!openList.contains(neighbor))
                        openList.add(neighbor);

                    float tentativeScore = current.computeScore(goal);
                    if (tentativeScore > neighbor.getScore())
                        continue; //not a better path

                    neighbor.setParent(current);
                    neighbor.updateScore(goal);
                }

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void backtrack(Node parent, Node current) {
        path.add(current);
        while (current.getParent() != null) {
            current = current.getParent();
            path.add(current);

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private LinkedList<Node> lowestScore(LinkedList<Node> openList) {
        LinkedList<Node> lowestNodes = new LinkedList<Node>();

        Node lowest = openList.getFirst();
        for (Node current : openList)
            if (current.getScore() < lowest.getScore())
                lowest = current;

        for (Node current : openList)
            if (current.getScore() == lowest.getScore())
                lowestNodes.addFirst(current);

        return lowestNodes;
    }

    private LinkedList<Node> getAdjacentNodes(Node node) {
        LinkedList<Node> walkable = new LinkedList<Node>();

        Tile left = tiles.get(new Point(node.getX() - 1, node.getY()));
        Tile right = tiles.get(new Point(node.getX() + 1, node.getY()));
        Tile top = tiles.get(new Point(node.getX(), node.getY() + 1));
        Tile bottom = tiles.get(new Point(node.getX(), node.getY() - 1));

        if (left.hasNode())
            walkable.add(left.getNode());

        if (right.hasNode())
            walkable.add(right.getNode());

        if (top.hasNode())
            walkable.add(top.getNode());

        if (bottom.hasNode())
            walkable.add(bottom.getNode());

        return walkable;
    }

    private void evaluateAdjacentNodes(Node parentNode) {
        ///Node currentNode = getLowestScoreInOpenList();

        openList.remove(parentNode);

        if (closedList.contains(goal.getNode()))
            return;

        LinkedList<Node> adjacentNodes = getAdjacentNodes(tiles.get(parentNode.getLocation()).getNode());

        for (Node node : adjacentNodes) {
            Node goalNode = goal.getNode();
            node.setParent(parentNode);

            if (closedList.contains(node))
                continue;

            if (!openList.contains(node)) {
                node.updateScore(goalNode);
                openList.add(node);
            } else {
                if (node.computeScore(goalNode) < node.getScore()) {
                    node.updateScore(goalNode);
                    parentNode.updateScore(goalNode);
                }

                //gotta update parent score
                //test if using the current G score make the aSquare F score lower, if yes update the parent because it means its a better path
            }
        }

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        closedList.add(getLowestScoreInOpenList());
        openList.remove(getLowestScoreInOpenList());
    }


    private Node getLowestScoreInOpenList() {
        Node lowest = null;
        for (Node current : openList)
            if (!tiles.get(current.getLocation()).isFilled() && (lowest == null || current.getScore() < lowest.getScore()))
                lowest = current;

        return lowest;
    }


    private Node lowestWalkableNodeByOrder(Tile tile) {
        Tile lowest = null;

        //return by top, left, bottom then right

        Tile left = tiles.get(new Point(tile.getGridX() - 1, tile.getGridY()));
        Tile right = tiles.get(new Point(tile.getGridX() + 1, tile.getGridY()));
        Tile top = tiles.get(new Point(tile.getGridX(), tile.getGridY() + 1));
        Tile bottom = tiles.get(new Point(tile.getGridX(), tile.getGridY() - 1));


        if (!path.contains(top.getNode()) && top.getNode() != goal.getNode() && closedList.contains(top.getNode()))
            lowest = top;

        if (!path.contains(left.getNode()) && left != goal && closedList.contains(left.getNode()) && (lowest == null || left.getNode().getScore() < lowest.getNode().getScore()))
            lowest =  left;

        if (!path.contains(bottom.getNode()) && lowest != goal && closedList.contains(bottom.getNode()) && (lowest == null || bottom.getNode().getScore() < lowest.getNode().getScore()))
            lowest =  bottom;

        if (!path.contains(right.getNode()) && right != goal && closedList.contains(right.getNode()) && (lowest == null || right.getNode().getScore() < lowest.getNode().getScore()))
            lowest =  right;

        if (lowest != null && lowest.hasNode())
            return lowest.getNode();
        else
            return null;
    }


    public void click(Tile clickedTile) {
        if (start == null) {
            start = clickedTile;
        } else if (goal == null) {
            goal = clickedTile;
            //addSquares(getAdjacentNodes(start), start);
            closedList.add(start.getNode());
            initialised = true;
        } else {
            //togglePause();
        }
    }

    public void renderDebug(SpriteBatch sb, ShapeRenderer sr, BitmapFont font) {
        sr.begin();

        for (int x = 0; x < tileAmount; x++) {
            for (int y = 0; y < tileAmount; y++) {
                Tile currentTile = tiles.get(new Point(x, y));

                if (currentTile.equals(start))
                    currentTile.render(sr, Color.YELLOW);
                else if (currentTile.equals(goal))
                    currentTile.render(sr, Color.BLUE);
                else if (path.contains(currentTile.getNode()))
                    currentTile.render(sr, Color.MAGENTA);
                else if (openList.contains(currentTile.getNode()))
                    currentTile.render(sr, Color.GREEN);
                else if (closedList.contains(currentTile.getNode()))
                    currentTile.render(sr, Color.RED);
                else
                    currentTile.render(sr);
            }
        }


        sr.end();

        sb.begin();

        DecimalFormat df = new DecimalFormat("##");
        for (int x = 0; x < tileAmount; x++) {
            for (int y = 0; y < tileAmount; y++) {
                Tile currentTile = tiles.get(new Point(x, y));
                if (currentTile.hasNode()) {
                    String value;
                    if (currentTile.getNode().getScore() == Float.MAX_VALUE)
                        value = "0";
                    else
                        value = df.format(currentTile.getNode().getScore());

                    font.draw(sb, value, currentTile.getWorldX(), currentTile.getWorldY() + currentTile.getHeight());
                    font.draw(sb, String.valueOf(currentTile.getNode().getX() + ", " + currentTile.getNode().getY()), currentTile.getWorldX(), currentTile.getWorldY() + currentTile.getHeight() - 10);


                    if (currentTile.getNode().getParent() != null) {
                        String toDraw;
                        Node node = currentTile.getNode();
                        Node parent = currentTile.getNode().getParent();

                        if (node.getX() > parent.getX())
                            toDraw = ">";
                        else if (node.getX() < parent.getX())
                            toDraw = "<";
                        else if (node.getY() > parent.getY())
                            toDraw = "^";
                        else
                            toDraw = "_";


                        font.draw(sb, toDraw, currentTile.getWorldX() + 15, currentTile.getWorldY() + currentTile.getHeight());
                    }
                }
            }
        }

        sb.end();
    }
}
