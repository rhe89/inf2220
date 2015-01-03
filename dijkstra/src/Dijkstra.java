import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Roar on 02.12.14.
 */
public class Dijkstra {
    private ArrayList<Node> graph;
    private final int KNOWN = 1, UNKNOWN = 2;

    //Dijkstra
    public void shortestPathFrom(Node source) {
        source.distance = 0;

        ArrayList<Node> queue = new ArrayList<Node>();

        /* Legger til noden det skal finnes distanse fra
        i køen. */
        queue.add(source);


        while (!queue.isEmpty()) {
            //Henter første element i køen
            Node node = queue.get(0);

            //Går gjennom alle node's naboer
            for (Edge e : node.adjList) {
                Node n = e.target;

                /*Setter vekten til kanten og vekten for å gå
                forbi noden (altså kantens vekt + nodens distanse)
                 */
                int weight = e.weight;
                int distanceThroughN = n.distance + weight;

                /*Hvis "n"'s distance nå er mindre enn en tidligere
                satt distanse; oppdater "n"'s distanse og sett forrige-pekeren
                til "n" til "node" (som er "n" sin utgrad
                Legg så til "n" i køen slik at "n"'s naboer etterhvert kan sjekkes
                 */
                if (distanceThroughN < n.minDistance) {
                    queue.remove(n);
                    n.minDistance = distanceThroughN;
                    n.previous = node;
                    queue.add(n);
                }
            }
        }
    }
    public static List<Node> getShortestPathTo(Node target)
    {
        List<Node> path = new ArrayList<Node>();
        for (Node node = target; node != null; node = node.previous)
            path.add(node);
        Collections.reverse(path);
        return path;
    }





    private class Node {
        int distance, minDistance;
        Node previous;
        int known;

        ArrayList<Edge> adjList;


    }

    private class Edge {
        Node target;
        Node from;
        int weight;
    }
}
