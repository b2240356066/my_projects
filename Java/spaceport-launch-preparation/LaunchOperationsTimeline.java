import java.io.File;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class LaunchOperationsTimeline {

    public List<LaunchPlan> readXML(String file) {
        // TODO: Parse XML and populate launch plans

        List<LaunchPlan> list = new ArrayList<>();
        try {

            // Standard Java XML Parsing setup
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(file));
            doc.getDocumentElement().normalize();

            // Extract all LaunchPlan elements 
            NodeList planNodes = doc.getElementsByTagName("LaunchPlan");
            for (int i = 0; i < planNodes.getLength(); i++) {
                Element planElement = (Element) planNodes.item(i);
                
                // Get Plan Name 
                String planName = planElement.getElementsByTagName("PlanName").item(0).getTextContent();
                
                List<Operation> operations = new ArrayList<>();
                NodeList opNodes = planElement.getElementsByTagName("Operation");

                for (int j = 0; j < opNodes.getLength(); j++) {
                    Element opElement = (Element) opNodes.item(j);

                    // Parse Operation details 
                    int code = Integer.parseInt(opElement.getElementsByTagName("Code").item(0).getTextContent());
                    String label = opElement.getElementsByTagName("Label").item(0).getTextContent();
                    int time = Integer.parseInt(opElement.getElementsByTagName("ExecutionTime").item(0).getTextContent());

                    // Parse prerequisites 
                    List<Integer> prereqs = new ArrayList<>();
                    NodeList reqNodes = opElement.getElementsByTagName("RequiresOperation");
                    for (int k = 0; k < reqNodes.getLength(); k++) {
                        prereqs.add(Integer.valueOf(reqNodes.item(k).getTextContent()));
                    }

                    operations.add(new Operation(code, label, time, prereqs));
                }
                list.add(new LaunchPlan(planName, operations));
            }
        } catch (Exception e) {
            // Basic error handling for file reading or parsing issues
            e.printStackTrace();
        }
        return list;
    }

    public void printTimeline(List<LaunchPlan> plans) {
        // TODO: Iterate and print each plan

        for (LaunchPlan plan : plans) {
            plan.print();
        }
    }

}
