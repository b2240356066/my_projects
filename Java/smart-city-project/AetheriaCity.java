import java.io.File;
import java.util.Collections;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * AetheriaCity – Main entry point.
 *
 * Parse AetheriaCity.xml, build the required graphs, and call your
 * implementations in sequence.
 *
 * Usage:
 *   javac *.java -d .
 *   java AetheriaCity <AetheriaCity>
 */
public class AetheriaCity {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("Usage: java AetheriaCity <xml-file>");
            return;
        }
        Document doc = parseXML(args[0]);

        printBanner("AETHERIA CITY - Full System Boot Report");

        // TODO: Parse <districts>, build Graph, call PowerGrid

        printBanner("PART A  |  Power Grid");

        NodeList districtNodes = doc.getElementsByTagName("district");

        //initialize an undirected graph where vertices are districts 
        Graph powerGridGraph = new Graph(districtNodes.getLength()); 
        String[] districtNames = new String[districtNodes.getLength()];
        
        for (int i = 0; i < districtNodes.getLength(); i++) 
            {
            Element district = (Element) districtNodes.item(i);
            int u = Integer.parseInt(getText(district, "id").substring(1));
            districtNames[u] = getText(district, "name");

            // Add undirected edges for power cables with costs
            NodeList links = district.getElementsByTagName("link");
            for (int j = 0; j < links.getLength(); j++) {

                Element link = (Element) links.item(j);
                int v = Integer.parseInt(getText(link, "target").substring(1));
                double cost = Double.parseDouble(getText(link, "cableCost"));
                powerGridGraph.addEdge(new Edge(u, v, cost));

            }
        }

        //Check for connectivity and then compute mst 
        System.out.println("City fully connected? " + (PowerGrid.isConnected(powerGridGraph) ? "YES " : "NO"));
        List<Edge> mst = PowerGrid.kruskalMST(powerGridGraph);

        // Sort MST edges primarily by weight, then by vertex ID 

        Collections.sort(mst, (e1, e2) -> {
        if (Double.compare(e1.weight(), e2.weight()) != 0) 
            {
        return Double.compare(e1.weight(), e2.weight());
        }
            int v1 = e1.either();
            int v2 = e2.either();
            return Integer.compare(v1, v2);
        });


        double totalMSTCost = 0;
        System.out.println("\nMinimum Spanning Tree (" + mst.size() + " cables):");
        for (Edge e : mst) {
            int v = e.either();
            int w = e.other(v);
            System.out.printf("  %-22s  %-23s cost: %.0f\n", districtNames[v], districtNames[w], e.weight());
            totalMSTCost += e.weight();
        }
        System.out.println("Total minimum cable cost: " + (int)totalMSTCost + " units");


        // TODO: Parse <bootDependencies>, build Digraph, call BootSequence

        printBanner("PART B  |  Master Boot Sequence (valid order)");
        NodeList bootNodes = doc.getElementsByTagName("s");

        // Initialize a directed graph for system boot dependencies
        Digraph bootDigraph = new Digraph(bootNodes.getLength());
        String[] bootNames = new String[bootNodes.getLength()];


        for (int i = 0; i < bootNodes.getLength(); i++) {
            Element s = (Element) bootNodes.item(i);
            int u = Integer.parseInt(getText(s, "id").substring(1));
            bootNames[u] = getText(s, "name");

            // Add directed edges where v is a requirement that must boot before u
            NodeList reqs = s.getElementsByTagName("req");
            for (int j = 0; j < reqs.getLength(); j++) {
                int v = Integer.parseInt(reqs.item(j).getTextContent().trim().substring(1));
                bootDigraph.addEdge(v, u); 
            }
        }
        
        System.out.println("=== Part B: Master Boot Sequence ===");

        // Compute the topological order or detect deadlocks
        List<Integer> sequence = BootSequence.computeBootSequence(bootDigraph, bootNames);

        if (sequence != null) {
            System.out.println("Valid boot sequence found:");
            for (int i = 0; i < sequence.size(); i++) {
                int id = sequence.get(i);
                System.out.println("  Step  " + (i + 1) + ": " + bootNames[id] + " (id=" + id + ")");
            }
        }

        // TODO: Parse <fiberLinks>, build Digraph, call CommHubs
        
        printBanner("PART C  |  Secure Communication Hubs (SCCs)");
        System.out.println("=== Part C: Secure Communication Hubs ===");

        NodeList stationNodes = doc.getElementsByTagName("station");

        // Initialize a directed graph representing one-way fiber links
        Digraph fiberDigraph = new Digraph(stationNodes.getLength());
        String[] stationNames = new String[stationNodes.getLength()];

        for (int i = 0; i < stationNodes.getLength(); i++) {
            Element station = (Element) stationNodes.item(i);
            int u = Integer.parseInt(getText(station, "id").substring(1));
            stationNames[u] = getText(station, "name");

            // Map the fiber network links
            NodeList outLinks = station.getElementsByTagName("link");
            for (int j = 0; j < outLinks.getLength(); j++) {
                Element link = (Element) outLinks.item(j);
                int v = Integer.parseInt(getText(link, "target").substring(1));
                fiberDigraph.addEdge(u, v);
            }
        }

        // Identify strongly connected components as "High-Interaction Zones"
        CommHubs.findHighInteractionZones(fiberDigraph, stationNames);


        // TODO: Parse <leakScenarios>, call BioLeakContainment

        printBanner("PART D  |  Bio-Leak Containment Protocol");

        String leakSourceStr = getText((Element) doc.getElementsByTagName("scenario").item(0), "sourceId");
        int leakSource = Integer.parseInt(leakSourceStr.substring(1));
        System.out.println("Leak origin: " + stationNames[leakSource] + " (id=" + leakSource + ")");

        //Use DFS to Find all stations downstream of the leak

        List<Integer> atRisk = BioLeakContainment.dfsReachable(fiberDigraph, leakSource, stationNames);
        System.out.println("\n=== Part D, Task 1 " + stationNames[leakSource] + "(" + leakSource + ") ===");
        System.out.println("Stations at risk (" + atRisk.size() + "):");
        for (int id : atRisk) System.out.println("  " + stationNames[id] + "(" + id + ")");


        //Use BFS to group stations by distance for priority evacuation
        
        List<List<Integer>> layers = BioLeakContainment.bfsLayers(fiberDigraph, leakSource, stationNames);
        System.out.println("\n=== Part D, Task 2 " + stationNames[leakSource] + "(" + leakSource + ") ===");
        System.out.println("Evacuation priority layers:");
        for (int d = 0; d < layers.size(); d++) {
            String label = (d == 0) ? "Layer 0 (source)     : " : "Layer " + d + " (priority " + d + "): ";
            System.out.print("  " + label);
            for (int i = 0; i < layers.get(d).size(); i++) {
                int id = layers.get(d).get(i);
                System.out.print(stationNames[id] + "(" + id + ")" + (i < layers.get(d).size() - 1 ? ", " : ""));
            }
            System.out.println();
        }



        
    }

    // ── XML helpers ──────────────────────────────────────────────────

    private static Document parseXML(String filename) throws Exception {
        File file = new File(filename + ".xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        doc.getDocumentElement().normalize();
        return doc;
    }

    private static String getText(Element parent, String tag) {
        NodeList nl = parent.getElementsByTagName(tag);
        if (nl.getLength() == 0) return "";
        return nl.item(0).getTextContent().trim();
    }

    static void printBanner(String title) {
        String line = "═".repeat(60);
        System.out.println("\n" + line);
        System.out.println("  " + title);
        System.out.println(line);
    }
}
