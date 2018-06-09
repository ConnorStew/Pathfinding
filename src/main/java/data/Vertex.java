package data;

import java.util.ArrayList;
import java.util.List;

/**
 * A vertex is a single point in a {@link Graph}.
 */
public class Vertex {

    private final String debugName;
    /** The vertex's x coordinate. */
    private float x;

    /** The vertex's y coordinate. */
    private float y;

    /** Vertices connected to this vertex via an {@link Edge}. */
    private ArrayList<Vertex> neighbors;

    /** This vertex's distance from the starting vertex. */
    private float g;

    /** This vertex's heuristic distance from the starting vertex. */
    private float h;

    /** The vertex's distance score based on {@link #g} and {@link #h}. */
    private float f;

    private Vertex parent;

    /**
     * Creates a vertex at the desired position.
     * @param x the vertex's x coordinate
     * @param y the vertex's y coordinate
     */
    public Vertex(float x, float y, String debugName) {
        this.debugName = debugName;
        this.x = x;
        this.y = y;
        f = Float.MAX_VALUE;
        neighbors = new ArrayList<Vertex>();
    }

    public float getY() {
        return y;
    }

    public float getX() {
        return x;
    }

    public void addNeighbour(Vertex toAdd) {
        neighbors.add(toAdd);
    }

    public List<Vertex> getNeighbours() {
        return neighbors;
    }

    public float getScore() {
        return f;
    }

    public float getG() {
        return g;
    }

    public void setG(float g) {
        this.g = g;
    }

    public void setH(float h) {
        this.h = h;
    }

    public float getH() {
        return h;
    }

    public void setScore(float score) {
        this.f = score;
    }

    @Override
    public String toString() {
        return debugName;
    }

    public Vertex getParent() {
        return parent;
    }

    public void setParent(Vertex parent) {
        this.parent = parent;
    }
}
