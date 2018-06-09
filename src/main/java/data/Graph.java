package data;

import java.util.*;

/**
 * Look here https://github.com/RavenTheorist/A-Star-Project/blob/master/src/a/star/project/GraphImplementation/Graph.java.
 * https://www.raywenderlich.com/4946/introduction-to-a-pathfinding
 */
public class Graph {

    /** The nodes of the {@link Graph}. */
    private ArrayList<Vertex> vertices = new ArrayList<Vertex>();

    /** The edges connecting {@link #vertices}. */
    private ArrayList<Edge> edges = new ArrayList<Edge>();

    /** List of {@link #vertices} which are still being considered for the {@link #path}. */
    private ArrayList<Vertex> openList = new ArrayList<Vertex>();

    /** List of {@link #vertices} which are not being considered for the {@link #path}. */
    private ArrayList<Vertex> closedList = new ArrayList<Vertex>();

    /** List of {@link #vertices} which create the shortest chat. */
    private ArrayList<Vertex> path = new ArrayList<Vertex>();

    /**
     * Creates an empty graph.
     */
    public Graph() {}

    /**
     * Adds a vertex to the graph.
     * @param vertex the vertex to add
     */
    public void addVertex(Vertex vertex) {
        if (vertices.contains(vertex))
            throw new IllegalArgumentException("No duplicate nodes allowed.");

        vertices.add(vertex);
    }

    /**
     * Adds an edge between two vertices.
     * @param firstVertex the first vertex
     * @param secondVertex the second vertex
     */
    public void addEdge(Vertex firstVertex, Vertex secondVertex) {
        edges.add(new Edge(firstVertex, secondVertex));
    }

    /**
     * Uses the A* algorithm to find the best path between the supplied vertices.
     * @param startVertex the starting vertex
     * @param targetVertex the target vertex
     */
    public void aStar(Vertex startVertex, Vertex targetVertex) {
        openList.clear();
        closedList.clear();
        path.clear();

        final long wait = 50;
        openList.add(startVertex);

        while (!openList.isEmpty()) {
            if (openList.contains(targetVertex)) {
                path.add(targetVertex);

                Vertex current = targetVertex;
                while (current.getParent() != null) {
                    path.add(current.getParent());
                    current = current.getParent();
                    try {
                        Thread.sleep(wait);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return;
            }

            //get lowest vertices
            LinkedList<Vertex> lowestVertices = new LinkedList<Vertex>();
            float lowestScore = Float.MAX_VALUE;

            for (Vertex vertex : openList)
                if (vertex.getScore() < lowestScore)
                    lowestScore = vertex.getScore();

            for (Vertex vertex : openList)
                if (vertex.getScore() == lowestScore)
                    lowestVertices.addFirst(vertex);

            for (Vertex vertex : lowestVertices) {
                closedList.add(vertex);
                openList.remove(vertex);

                //add neighbours that aren't in the list
                for (Vertex neighbour : vertex.getNeighbours()) {
                    if (!openList.contains(neighbour) && !closedList.contains(neighbour)) {
                        //get edge between this vertex and it neighbour
                        Edge edge = getEdgeBetween(vertex, neighbour);

                        if (closedList.contains(neighbour))
                            continue;

                        float tentativeG = vertex.getG() + Math.abs(vertex.getX() - neighbour.getX()) + Math.abs(vertex.getY() - neighbour.getY());;
                        float tentativeH = Math.abs(targetVertex.getX() - neighbour.getX()) + Math.abs(targetVertex.getY() - neighbour.getY());
                        float tentativeScore = (tentativeG + tentativeH) * edge.getWeight();

                        if (!openList.contains(neighbour)) {
                            openList.add(neighbour);
                            setScore(neighbour, vertex, tentativeScore, tentativeG, tentativeH);
                        }

                        //update the score of the neighbouring vertex
                        if (tentativeScore < neighbour.getScore())
                            setScore(neighbour, vertex, tentativeScore, tentativeG, tentativeH);
                    }
                }

            }

            try {
                Thread.sleep(wait);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void setScore(Vertex vertex, Vertex parent, float score, float g, float h) {
        vertex.setScore(score);
        vertex.setG(g);
        vertex.setH(h);
        vertex.setParent(parent);
    }

    /**
     * Gets all edges between the vertices provided (Going from startVertex to endVertex, not the other way around.)
     * @param startVertex the starting vertex
     * @param endVertex the end vertex
     * @return edge between the vertices
     */
    public Edge getEdgeBetween(Vertex startVertex, Vertex endVertex) {
        for (Edge edge : edges)
            if (edge.getFirstVertex().equals(startVertex) && edge.getSecondVertex().equals(endVertex))
                return edge;

        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        return sb.toString();
    }

    /**
     * Gets the vertex at the desired x,y coordinate.
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the vertex at the position or null if none is found
     */
    public Vertex getVertexAt(float x, float y) {
        for (Vertex vertex : vertices)
            if (vertex.getX() == x && vertex.getY() == y)
                return vertex;

        return null;
    }

    public List<Vertex> getOpenList() {
        return Collections.unmodifiableList(openList);
    }

    public List<Vertex> getClosedList() {
        return Collections.unmodifiableList(closedList);
    }

    public List<Vertex> getPath() {
        return Collections.unmodifiableList(path);
    }

    public List<Vertex> getVertices() {
        return Collections.unmodifiableList(vertices);
    }

    public List<Edge> getEdges() {
        return Collections.unmodifiableList(edges);
    }
}
