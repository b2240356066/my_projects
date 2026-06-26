import java.util.*;

public class LaunchPlan {

    String name;
    List<Operation> ops;

    public LaunchPlan(String n, List<Operation> o) {
        name = n;
        ops = o;
    }

    /**
     * Computes the earliest possible start times for all operations.
     * You should consider dependency constraints between operations.
     */
    public int[] earliest() {
        // TODO: Implement topological sorting + scheduling

        int numOps = ops.size(); //Number of operations

        int[] earliestBeginTimes = new int[numOps]; // stores Earliest Begin Times

        int[] inDegree = new int[numOps]; 

        // Calculate in-degrees
        for (Operation op : ops){
            for( int prereqCode : op.prereq){
                inDegree[op.code]++;
            }
        }

        // Queue for operations with no prerequisites
        Queue<Integer> noPrereqQueue = new LinkedList<>();
        for (int i = 0; i < numOps; i++) {

            if (inDegree[i] == 0) {

                noPrereqQueue.add(i);
                earliestBeginTimes[i] = 0; 
            }
        }


        // Process the DAG
        while (!noPrereqQueue.isEmpty()) {
            int uCode = noPrereqQueue.poll();
            Operation u = ops.get(uCode);
            int finishU = earliestBeginTimes[uCode] + u.time;

            // Find operations blocked by "u"
            for (Operation v : ops) {
                if (v.prereq.contains(uCode)) {

                    // Earliest start of "v" is the max finish time of its prerequisites
                    earliestBeginTimes[v.code] = Math.max(earliestBeginTimes[v.code], finishU);
                    inDegree[v.code]--;
                    if (inDegree[v.code] == 0) {

                        noPrereqQueue.add(v.code);
                    }
                }
            }
        }
        return earliestBeginTimes;
    }

    /**
     * Computes total time required to complete all operations.
     */
    public int total(int[] schedule) {
        // TODO: Compute maximum finish time

        int maxTime = 0;

        for ( int i=0; i < ops.size() ; i++){
            maxTime = Math.max(maxTime, schedule[i] + ops.get(i).time);
        }
        return maxTime;
    }

    /**
     * Helper function to print separator line
     */
    public static void printLine(int n) {
        for (int i = 0; i < n; i++) System.out.print("-");
        System.out.println();
    }

    /**
     * Prints the launch plan timeline in required format.
     */
    public void print() {

        int[] schedule = earliest();

        int width = 65;

        printLine(width);
        System.out.println("Launch Plan: " + name);
        printLine(width);

        // Header
        System.out.printf("%-8s%-35s%-8s%-8s\n", "Code", "Operation", "Begin", "Finish");

        printLine(width);

        // TODO:
        // Print each operation with:
        // code, label, start time, finish time

        ops.sort(Comparator.comparingInt(o -> o.code));
        
        for (Operation op : ops) {
            int begin = schedule[op.code]; // start 
            int finish = begin + op.time; // end
            System.out.printf("%-8d%-35s%-8d%-8d\n", op.code, op.label, begin, finish);
        }

        printLine(width);

        // TODO:
        // Print total duration in format:
        // Launch-ready in X hour(s).

        System.out.println("Launch-ready in " + total(schedule) + " hour(s).");

        printLine(width);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LaunchPlan)) return false;

        LaunchPlan other = (LaunchPlan) o;

        return name.equals(other.name) && ops.equals(other.ops);
    }
}