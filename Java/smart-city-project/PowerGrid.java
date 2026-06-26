import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class PowerGrid {

    public static boolean isConnected(Graph g) {

        boolean[] visited = new boolean[g.V()]; // create an array of visited vertices 

        dfs(g, 0, visited); // perform dfs search starting from district 0

        for ( boolean vertex : visited){

            if( !vertex ){ 

            // if any of the vertices is false this means that it is not connected 

                return false;
            }
        }

        return true; // true if all of the vertices are connected

    }

    private static void dfs(Graph graph, int vertex, boolean[] visited) {

        visited[vertex] = true; //mark it  as visited 

        for (Edge edge : graph.adj(vertex)) //iterate through its adjacent vertices
        
        {

            int w = edge.other(vertex); // gets the adjacent vertex's ID

            if (!visited[w]) { // if its not visited, visit it 

                 dfs(graph, w, visited); // perform dfs in that vertex

            }
        }
    }

    public static List<Edge> kruskalMST(Graph g) {
        // most efficient way to link every district while minimizing total cabling cost 

        List<Edge> mst = new ArrayList<>(); // create minimum spanning tree 

        PriorityQueue<Edge> pq = new PriorityQueue<>(); // create priority queue 

        for ( int vertex = 0; vertex < g.V(); vertex++) // iterate through every vertex
            {

            for ( Edge edge : g.adj(vertex)) // look at every cable connected to this vertex
                {

                if(edge.other(vertex) > vertex) // to only add the edge once (if ID is higher, this means it is already visited)
                {

                    pq.add(edge); // keep the cheapest cables at the top
                }

            }
        }


        int[] parent = new int[g.V()]; // create parent array ( to avoid loops )

        for ( int i = 0; i < g.V(); i++){

            parent[i] = i; // fill the array
        }

        while (!pq.isEmpty() ){

            Edge edge = pq.poll();

            int vertex1 = edge.either(); // edge's endpoint
            int vertex2 = edge.other(vertex1); // edge's other endpoint

            int rootVertex1 = find(parent, vertex1); // find root 
            int rootVertex2 = find(parent, vertex2); // find root 

            if(rootVertex1 != rootVertex2){ // if roots are different, they are separate 

                mst.add(edge);

                parent[rootVertex1] = rootVertex2;
            }


        }

        return mst;

    }

    private static int find(int[] parent, int i) {
        if (parent[i] == i){

            return i;

        }
        
        return parent[i] = find(parent, parent[i]); // Path compression
    }

}


