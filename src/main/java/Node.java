import java.awt.*;

public class Node {

    private Node parent;

    private float score = Float.MAX_VALUE;

    private float g;

    private float h;

    private Point location;

    public Node(Point location) {
        this.location = location;
    }

    public float computeScore(Node goal) {
        float tempG;
        if (parent == null)
            tempG = 0;
        else
            tempG = parent.g + 1;
        float tempH = absoluteDistance(goal);
        return tempG + tempH;
    }

    private float absoluteDistance(Node target) {
        return (Math.abs(location.x - target.location.x) + Math.abs(location.y - target.location.y));
    }

    public void updateScore(Node goal) {
        score = computeScore(goal);
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

    public void setParent(Node parentNode) {
        parent = parentNode;
    }

    public Node getParent() {
        return parent;
    }
}
