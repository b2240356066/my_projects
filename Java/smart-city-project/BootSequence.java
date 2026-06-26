import java.util.ArrayList;
import java.util.List;

public class BootSequence {

    public static List<Integer> computeBootSequence(Digraph g, String[] labels) {

       List<Integer> sequence = new ArrayList<>();
       int[] state = new int[g.V()]; // 0 = not visited, 1 = visiting , 2 = already visited

       for (int i= g.V()-1; i >= 0; i--){

        if(state[i] == 0) // if vertex is not visited 
        {
            if(hasCycle(g, i, state, sequence)) 
                // if a cycle is found, the boot sequence is impossible
                {

                System.out.println("SYSTEM INFEASIBLE: Circular reference (deadlock) detected!");
                return null;
            }

        }
    }
       
       return sequence;

    }

    private static boolean hasCycle(Digraph graph, int vertex, int[] state, List<Integer> sequence){

        state[vertex] = 1; // currently visiting this vertex

        for( int w : graph.adj(vertex)){

            if(state[w] == 1){
                // If we encounter a vertex that is already being visited that means there is a cycle 
                System.out.println("  [Cycle detected] Edge " + vertex + " -> " + w + " creates a circular dependency.");
                return true; // found a cycle

            }

            if(state[w] == 0 && hasCycle(graph, w, state, sequence))// to check if adjacent vertex has a cycle
            {
                return true;
            }

        }

        state[vertex] = 2; // Mark as visited
        sequence.add(vertex); // Add to sequence

        return false; // return false if cycle is not found
    }
}
