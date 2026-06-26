import javax.xml.parsers.*;
import java.util.*;
import org.w3c.dom.*;
import java.io.File;
/**
 * DiagnosticRequest
 *
 * Parses diagnostic_requests.xml and exposes:
 *   - single_target : the test ID for top-down patient burden analysis (Step 3)
 *   - all_targets   : list of test IDs for bottom-up hospital cost analysis (Step 4)
 *
 * XML structure:
 *   <requests>
 *     <single_target ref="overall_health"/>
 *     <all_targets>
 *       <target ref="overall_health"/>
 *       <target ref="cardiovascular_risk"/>
 *     </all_targets>
 *   </requests>
 *
 */
public class DiagnosticRequest {

    private String       singleTarget;
    private List<String> allTargets;

    private DiagnosticRequest() {}

    /**
     * Parses the given XML file and returns a populated DiagnosticRequest.
     *
     * @param filePath path to diagnostic_requests.xml
     * @return populated DiagnosticRequest
     */
    public static DiagnosticRequest loadFromXML(String filePath) {

        DiagnosticRequest req = new DiagnosticRequest();
        req.allTargets = new ArrayList<>(); // Initialize list before filling

        try {
            // Create XML parser
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Parse XML file
            Document doc = builder.parse(new File(filePath));

            // Get single_target element
            NodeList singleNode = doc.getElementsByTagName("single_target");
            if (singleNode.getLength() > 0) {
                // The target id
                req.singleTarget = ((Element) singleNode.item(0)).getAttribute("ref");
            }

            NodeList targetNodes = doc.getElementsByTagName("target");

            for (int i = 0; i < targetNodes.getLength(); i++) {
                // Get all target elements inside allTargets
                req.allTargets.add(((Element) targetNodes.item(i)).getAttribute("ref"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return req;
    }

    /** Returns the single target test ID for top-down analysis. */
    public String getSingleTarget() {
        return singleTarget;
    }

    /** Returns the list of target test IDs for bottom-up analysis. */
    public List<String> getAllTargets() {
        return Collections.unmodifiableList(allTargets);
    }
}
