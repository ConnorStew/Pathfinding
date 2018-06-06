package pathfinding;

import java.awt.*;

/**
 * A single node in a visibility graph.
 */
public class Node {

    /** The node that preceded this node. */
    private Node parent;

    /** The score given to this node. */
    private float score = Float.MAX_VALUE;

    /** The how many nodes are between this node and the start node. */
    private float g;

    /** The heuristic (non-optimal/guess) distance between the goal and this node. */
    private float h;

    /** The location of this node on the graph. */
    private Point location;

    /** This nodes position in the game world. */
    private float worldX, worldY;

    /**
     * Initialises this node at the given location.
     * @param location the location of this node
     */
    public Node(Point location, float worldX, float worldY) {
        this.location = location;
        this.worldX = worldX;
        this.worldY = worldY;
    }

    /**
     * The best-guess distance between this node and the target.
     * @param target the target node
     * @return the best-guess distance
     */
    private float heuristicDistance(Node target) {
        return (Math.abs(location.x - target.location.x) + Math.abs(location.y - target.location.y));
    }

    /**
     * Calculates the total movement cost from the start node to this node.
     * @return
     */
    private float movementCost() {
        return (parent == null) ? 0 : parent.g + 1;
    }

    /**
     * Calculates the score without updating the nodes current score.
     * @param goal the target node
     * @return the new score
     */
    public float computeScore(Node goal) {
        float tempG = movementCost();
        float tempH = heuristicDistance(goal);
        return tempG + tempH;
    }

    /**
     * Updates the score.
     * @param goal the target node
     */
    public void updateScore(Node goal) {
        h = heuristicDistance(goal);
        g = movementCost();
        score = g + h;
    }

    public float getScore() {
        return score;
    }

    public Point getLocation() {
        return location;
    }

    public int getY() {
        return location.y;
    }

    public int getX() {
        return location.x;
    }

    public float getWorldX() {
        return worldX;
    }

    public float getWorldY() {
        return worldY;
    }

    public void setParent(Node parentNode) {
        parent = parentNode;
    }

    public Node getParent() {
        return parent;
    }
}
