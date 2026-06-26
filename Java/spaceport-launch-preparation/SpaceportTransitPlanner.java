import java.util.*;

public class SpaceportTransitPlanner {

    Map<Station, Station> prev = new HashMap<>();
    Map<String, Double> cost = new HashMap<>();

    double dist(Station a, Station b) {
        double dx = a.p.x - b.p.x;
        double dy = a.p.y - b.p.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    void addUndirected(Station a, Station b, double speed) {
        // TODO: Add bidirectional walking edge

        double weight = dist(a, b) / speed;
        a.edges.add(new Edge(b, weight));
        b.edges.add(new Edge(a, weight));
    }

    void addDirected(Station a, Station b, double speed) {
        // TODO: Add directed shuttle edge

        double weight = dist(a, b) / speed;
        a.edges.add(new Edge(b, weight));
    }

    public List<RouteInstruction> solve(SpaceportTransitNetwork net) {

        List<RouteInstruction> route = new ArrayList<>();

        // TODO:
        // 1. Build graph

        List<Station> allNodes = new ArrayList<>();
        allNodes.add(net.start);
        allNodes.add(net.end);

        for (ShuttleCorridor corridor : net.corridors) {
            allNodes.addAll(corridor.stations);
        }
        
        // 2. Add walking edges

        for (int i = 0; i < allNodes.size(); i++) {
            for (int j = i + 1; j < allNodes.size(); j++) {
                addUndirected(allNodes.get(i), allNodes.get(j), net.walkSpeed);
            }
        }

        // 3. Add directed shuttle edges

        Set<Edge> shuttles = new HashSet<>();
        for (ShuttleCorridor c : net.corridors) {
            for (int i = 0; i < c.stations.size() - 1; i++) {
                double time = dist(c.stations.get(i), c.stations.get(i+1)) / net.shuttleSpeed;
                Edge edge = new Edge(c.stations.get(i+1), time);
                c.stations.get(i).edges.add(edge);
                shuttles.add(edge);
            }
        }

        // 4. Run Dijkstra

        PriorityQueue<Station> pq = new PriorityQueue<>(Comparator.comparingDouble(s -> cost.get(s.name)));
        Map<Station, Boolean> usedShuttle = new HashMap<>();

        for (Station s : allNodes) {
            cost.put(s.name, Double.MAX_VALUE);
        }
        
        cost.put(net.start.name, 0.0);
        pq.add(net.start);

        while (!pq.isEmpty()) {
            Station u = pq.poll();
            if (u.equals(net.end)) break;
            for (Edge e : u.edges) {
                double newTime = cost.get(u.name) + e.w;
                if (newTime < cost.get(e.to.name)) {
                    cost.put(e.to.name, newTime);
                    prev.put(e.to, u);
                    usedShuttle.put(e.to, shuttles.contains(e));
                    pq.add(e.to);
                }
            }
        }

        // 5. Reconstruct path

        Station currentStation = net.end;
        List<Station> path = new ArrayList<>();
        List<Boolean> wasShuttle = new ArrayList<>();

        while (currentStation != null) {
            path.add(currentStation);

            if (prev.containsKey(currentStation)) wasShuttle.add(usedShuttle.get(currentStation));
            currentStation = prev.get(currentStation);
        }

        Collections.reverse(path);
        Collections.reverse(wasShuttle);

        for (int i = 0; i < path.size() - 1; i++) {
            boolean shuttle = wasShuttle.get(i);
            double time = shuttle ? dist(path.get(i), path.get(i+1)) / net.shuttleSpeed : dist(path.get(i), path.get(i+1)) / net.walkSpeed;
            route.add(new RouteInstruction(path.get(i).name, path.get(i+1).name, time, shuttle));
        }
        return route;
    }

    public void print(List<RouteInstruction> r) {
        // TODO: Print route exactly in required format

        double totalTime = 0;
        for (RouteInstruction ri : r) totalTime += ri.t;

        // Total travel time rounded to the nearest minute
        System.out.printf("Fastest route takes %d minute(s).\n", Math.round(totalTime));
        System.out.println("Route Instructions");
        System.out.println("------------------");

        for (int i = 0; i < r.size(); i++) {
            RouteInstruction ri = r.get(i);
            String verb = ri.shuttle ? "Take the shuttle from" : "Walk from";
            // Step durations printed as doubles with exactly two digits 
            System.out.printf("%d. %s \"%s\" to \"%s\" for %.2f minutes.\n", 
                               (i + 1), verb, ri.a, ri.b, ri.t);
        }
    }

}