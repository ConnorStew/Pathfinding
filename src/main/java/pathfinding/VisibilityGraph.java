package pathfinding;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import ui.PathfindingWindow;

import java.awt.*;
import java.util.LinkedList;

public class VisibilityGraph {

    /** Nodes that could be added to the path. */
    private LinkedList<Node> openList = new LinkedList<Node>();

    /** The path so far. */
    private LinkedList<Node> closedList = new LinkedList<Node>();

    /** The final path. */
    private LinkedList<Node> path = new LinkedList<Node>();

    /** All nodes on the graph. */
    private LinkedList<Node> nodes = new LinkedList<Node>();

    /** The start and goal nodes. */
    private Node start, goal;

    public VisibilityGraph() {}

    public void initialise(Node node) {
        if (start == null) {
            start = node;
        } else {
            goal = node;
        }
    }

    public void addNode(Node toAdd) {
        if (toAdd != null)
            nodes.add(toAdd);
    }

    public void setStart(Node start) {
        this.start = start;
    }

    public void setGoal(Node goal) {
        this.goal = goal;
    }

    public LinkedList<Node> aStar(boolean pause) {
        openList = new LinkedList<Node>();
        closedList = new LinkedList<Node>();
        path.clear();
        openList.push(start);

        while (!openList.isEmpty()) {
            LinkedList<Node> lowestScores = lowestScore(openList);

            for (Node current : lowestScores) {
                if (current.equals(goal)) {
                    backtrack(current.getParent(), pause);
                    return path;
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

                if (pause) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

        return null;
    }

    private void backtrack(Node current, boolean pause) {
        path.add(current);
        while (current.getParent() != null) {
            current = current.getParent();
            path.add(current);

            if (pause) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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

        Node left = getNodeAt(new Point(node.getX() - 1, node.getY()));
        Node right = getNodeAt(new Point(node.getX() + 1, node.getY()));
        Node top = getNodeAt(new Point(node.getX(), node.getY() + 1));
        Node bottom = getNodeAt(new Point(node.getX(), node.getY() - 1));

        if (left != null)
            walkable.add(left);

        if (right != null)
            walkable.add(right);

        if (top != null)
            walkable.add(top);

        if (bottom != null)
            walkable.add(bottom);

        return walkable;
    }

    public Node getNodeAt(Point point) {
        for (Node node : nodes)
            if (node.getLocation().equals(point))
                return node;

        return null;
    }

    public void renderDebug(SpriteBatch sb, ShapeRenderer sr, BitmapFont font) {
        sr.begin();

        for (Node node : nodes) {
            Color toUse;

            if (node.equals(start))
                toUse = Color.YELLOW;
            else if (node.equals(goal))
                toUse = Color.BLUE;
            else if (path.contains(node))
                toUse = Color.MAGENTA;
            else if (openList.contains(node))
                toUse = Color.GREEN;
            else if (closedList.contains(node))
                toUse = Color.RED;
            else
                toUse = Color.WHITE;

            sr.set(ShapeRenderer.ShapeType.Filled);
            sr.setColor(toUse);
            sr.rect(node.getWorldX(), node.getWorldY(), PathfindingWindow.tileWidth, PathfindingWindow.tileHeight);

            sr.setColor(Color.BLACK);
            sr.set(ShapeRenderer.ShapeType.Line);
            sr.rect(node.getWorldX(), node.getWorldY(), PathfindingWindow.tileWidth, PathfindingWindow.tileHeight);
        }

        sr.end();

        /*
        sb.begin();

        final DecimalFormat df = new DecimalFormat("##");
        for (Node node : nodes) {
            String value;
            if (node.getScore() == Float.MAX_VALUE)
                value = "0";
            else
                value = df.format(node.getScore());

            font.draw(sb, value, node.getWorldX(), node.getWorldY() );
            font.draw(sb, String.valueOf(node.getX() + ", " + node.getY()), node.getWorldX(), node.getWorldY());


            if (node.getParent() != null) {
                String toDraw;
                Node parent = node.getParent();

                if (node.getX() > parent.getX())
                    toDraw = ">";
                else if (node.getX() < parent.getX())
                    toDraw = "<";
                else if (node.getY() > parent.getY())
                    toDraw = "^";
                else
                    toDraw = "_";

                font.draw(sb, toDraw, node.getWorldX() + 15, node.getWorldY());
            }
        }

        sb.end();
        */
    }

    public boolean isReady() {
        return (start != null && goal != null);
    }

    public Point toWorldPos(Point gridPos) {
        Node node = getNodeAt(gridPos);
        if (node != null)
            return node.getWorldPoint();
        else
            return null;
    }
}
