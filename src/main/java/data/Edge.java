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

    Edge(Vertex firstVertex, Vertex secondVertex, float weight) {
        this.firstVertex = firstVertex;
        this.secondVertex = secondVertex;
        this.weight = weight;
    }

    public Vertex getFirstVertex() {
        return firstVertex;
    }

    public Vertex getSecondVertex() {
        return secondVertex;
    }

}
