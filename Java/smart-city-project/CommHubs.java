import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class CommHubs {

    public static void findHighInteractionZones(Digraph g, String[] labels) {
        Stack<Integer> stack = new Stack<>();

        boolean[] visited = new boolean[g.V()];

        Digraph reversedG = g.reverse(); // fill the stack with reversed graph to obtain reverse post order traversal 

        for (int i = 0; i < g.V(); i++) { 

        if (!visited[i]) {
            dfsFillStack(reversedG, i, visited, stack);
        } 
    }

        Arrays.fill(visited, false);//reset visited to perform DFS

        List<List<Integer>> sccs = new ArrayList<>(); // create strongly connected components array 


        //perform DFS on orinal graph in stack order

        while (!stack.isEmpty()) {
            int vertex = stack.pop(); // get the last added vertex
            if (!visited[vertex]) // if vertex isnt visited
                {

                List<Integer> component = new ArrayList<>();

                dfsSCC(g, vertex, visited, component);

                sccs.add(component); // every time a DFS finishes, a full "High-Interaction Zone" is identified
            }
        }

        printResults(sccs, labels); // prints total sccs found

    }

    //identifying Hign Interaction Zones
    private static void dfsFillStack(Digraph graph, int vertex, boolean[] visited, Stack<Integer> stack) {
        visited[vertex] = true; // mark as visited 

        for (int w : graph.adj(vertex)) // iterate through adjacent vertices
            {
            if (!visited[w]) // if not visited 
                {
                dfsFillStack(graph, w, visited, stack);
            }
        }
        stack.push(vertex); // add vertex to stack
    }

    //group strongly connected components
    private static void dfsSCC(Digraph graph, int vertex, boolean[] visited, List<Integer> component) {
        visited[vertex] = true; // mark as visited
        component.add(vertex); // add it to same interaction group (specific scc array)
        for (int w : graph.adj(vertex)) // iterate through adjacent vertices
            {
            if (!visited[w]) // if vertex hasnt been visited
                {
                dfsSCC(graph, w, visited, component); // perform dfsSCC in that vertex
            }
                
        }
    }


    //it identifies groups that are "High-Interaction Zones"
    private static void printResults(List<List<Integer>> sccs, String[] labels) {
        int zonesCount = 0; // keeps track of groups that allow communication

        System.out.println("Total SCCs found: " + sccs.size());

        for (int i = 0; i < sccs.size(); i++) {

            List<Integer> scc = sccs.get(i); // get 

            Collections.sort(scc); // sort IDs to get a readable output

            if (scc.size() > 1) // if multiple stations can reach each other
                {
                    zonesCount++;
                } 
            
            String zoneTitle = scc.size() > 1 ? " [HIGH-INTERACTION ZONE]" : "";
            System.out.print("  SCC-" + i + zoneTitle + ": {");
            
            for (int j = 0; j < scc.size(); j++) {

                System.out.print(labels[scc.get(j)] + (j < scc.size() - 1 ? ", " : ""));
            }
            System.out.println("}");
        }
        System.out.println("High-Interaction Zones: " + zonesCount);
    }


}
