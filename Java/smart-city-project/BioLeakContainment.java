import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BioLeakContainment {

    public static List<Integer> dfsReachable(Digraph g, int source, String[] labels) {
        
        List<Integer> reachable = new ArrayList<>(); // create array that stores IDs

        boolean[] visited = new boolean[g.V()]; // create array to track visited vertices 

        dfs(g, source, visited, reachable);

        return reachable;
    }

    private static void dfs(Digraph graph, int vertex, boolean[] visited, List<Integer> reachable) {

        visited[vertex] = true; //mark it  as visited 

        reachable.add(vertex);// adds id to reachable 

        for (int w : graph.adj(vertex)) // iterate through adjacent vertices
        {
            if (!visited[w]) // if its not visited, visit it 
                {
                    dfs(graph, w, visited, reachable); // perform dfs in that vertex
                }
        }

    }


    public static List<List<Integer>> bfsLayers(Digraph g, int source, String[] labels) {

       List<List<Integer>> layers = new ArrayList<>(); // create array 

       int[] distTo = new int[g.V()]; // create array to keep track of the distances from source

        Arrays.fill(distTo, -1); // fill with -1 to avoid confusion (rather than 0)
        
        Queue<Integer> queue = new LinkedList<>(); // create queue

        queue.add(source); // add vertex

        distTo[source] = 0; // distance to source is 0 beacuse it is itself

        while (!queue.isEmpty()) 
            {
            int vertex = queue.poll(); // get the vertex at top of queue
            int dist = distTo[vertex]; // save the distance from source
            
            // Ensure the layers list is large enough for the current distance
            while (layers.size() <= dist) {

                layers.add(new ArrayList<>());
            }

            layers.get(dist).add(vertex); // add it to layers array 

            for (int w : g.adj(vertex)) // looks at all adjacent vertices
                { 
                if (distTo[w] == -1) // if vertex hasnt been visited 
                    {
                    distTo[w] = dist + 1; // if found an unvisited vertex, add 1
                    queue.add(w); // add adjacent vertex
                }
            }
        }

        //sort each layer for readable output 
        for (List<Integer> layer : layers) {
            
            Collections.sort(layer);
        }

        return layers; 
    }
}

