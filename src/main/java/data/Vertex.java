package data;

import java.util.ArrayList;
import java.util.List;

/**
 * A vertex is a single point in a {@link Graph}.
 */
public class Vertex {

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

    /**
     * Creates a vertex at the desired position.
     * @param x the vertex's x coordinate
     * @param y the vertex's y coordinate
     */
    public Vertex(float x, float y) {
        this.x = x;
        this.y = y;
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
}
