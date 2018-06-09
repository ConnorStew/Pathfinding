package data;

/**
 * A connection between two {@link Vertex}s.
 */
public class Edge {

    /** The first vertex. */
    private Vertex firstVertex;

    /** The second vertex. */
    private Vertex secondVertex;

    /** How difficult it is to cross this edge. */
    private float weight;

    private final float DEFUALT_WEIGHT = 1;

    Edge(Vertex firstVertex, Vertex secondVertex) {
        this.firstVertex = firstVertex;
        this.secondVertex = secondVertex;
        weight = DEFUALT_WEIGHT;
    }

    public Vertex getFirstVertex() {
        return firstVertex;
    }

    public Vertex getSecondVertex() {
        return secondVertex;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
