import javax.xml.parsers.*;
import java.util.*;
import org.w3c.dom.*;
import java.io.File;

/**
 * DiagnosticCatalogue
 * Responsible for:
 *   Step 1 -- loadFromXML()
 *             Parses diagnostic_catalogue.xml and builds the test map.
 *   Step 2 -- computeDerivedCosts()
 *             Computes processing_cost for DERIVED tests.
 *             DERIVED tests depend exclusively on RAW tests.
 *             COMPOSITE tests always have no processing_cost.
 */
public class DiagnosticCatalogue {

    // =========================================================================
    // Inner class -- do NOT change field names or types
    // =========================================================================
    /**
     * Represents a single diagnostic test in the catalogue.
     * RAW tests:
     *   - sampleType is one of: BLOOD, URINE, TISSUE, NONE
     *   - cost is the collection_cost read from XML
     *   - inputs is empty
     * DERIVED tests:
     *   - sampleType is null (not applicable)
     *   - cost is computed by computeDerivedCosts() in Step 2
     *   - inputs contains only RAW test IDs
     * COMPOSITE tests:
     *   - sampleType is null (not applicable)
     *   - initial cost is 0 (pure aggregation, no processing cost of its own)
     *   - inputs contains only DERIVED or COMPOSITE test IDs
     */
    public static class Test {

        public String       id;
        public String       name;
        public String       type;        // "RAW", "DERIVED", or "COMPOSITE"
        public String       sampleType;  // meaningful for RAW only
        public int          cost;        // collection_cost (RAW) or processing_cost (DERIVED) or 0 (COMPOSITE)
        public List<String> inputs;      // direct dependency IDs (empty for RAW)

        public Test() {
            inputs = new ArrayList<>();
        }

        /** Returns true if this test is of type RAW. */
        public boolean isRaw() {
            return "RAW".equalsIgnoreCase(type);
        }

        /** Returns true if this test is of type DERIVED. */
        public boolean isDerived() {
            return "DERIVED".equalsIgnoreCase(type);
        }

        /** Returns true if this test is of type COMPOSITE. */
        public boolean isComposite() {
            return "COMPOSITE".equalsIgnoreCase(type);
        }

        @Override
        public String toString() {
            return id + " [" + type + ", cost=" + cost + "]";
        }
    }

    // =========================================================================
    // Fields -- do NOT change names or types
    // =========================================================================
    /**
     * All tests in the catalogue, keyed by test ID.
     * Populated by loadFromXML(). LinkedHashMap preserves XML insertion order
     * which keeps output deterministic.
     */
    private Map<String, Test> tests = new LinkedHashMap<>();

    // =========================================================================
    // Step 1 -- TODO: implement this method
    // =========================================================================
    /**
     * Parses diagnostic_catalogue.xml and populates the tests map.
     * Uses standard Java javax.xml.parsers -- no external libraries.
     * XML format:
     *   RAW:      <test id=".." name=".." type="RAW"
     *                   sample_type=".." collection_cost=".."/>
     *   DERIVED:  <test id=".." name=".." type="DERIVED">
     *               <input ref=".."/>  (only RAW refs)
     *             </test>
     *   COMPOSITE:<test id=".." name=".." type="COMPOSITE">
     *               <input ref=".."/>  (DERIVED or COMPOSITE refs)
     *             </test>
     *
     * @param filePath path to diagnostic_catalogue.xml
     */
    public void loadFromXML(String filePath) {
        // TODO: implement Step 1

        try {
            //Create XML parser
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Parse XML file into a document
            Document doc = builder.parse(new File(filePath));

            // Get all <test> elements
            NodeList testNodes = doc.getElementsByTagName("test");

            for (int i = 0; i < testNodes.getLength(); i++) {

                Element element = (Element) testNodes.item(i);

                // Create new Test object
                Test test = new Test();
                test.id = element.getAttribute("id");
                test.name = element.getAttribute("name");
                test.type = element.getAttribute("type");

                if (test.isRaw()) {
                    // RAW tests are directly read sample type and collection cost
                    test.sampleType = element.getAttribute("sample_type");
                    test.cost = Integer.parseInt(element.getAttribute("collection_cost"));
                }
                else {
                    // DERIVED or COMPOSITE test are computed from one or more RAW tests.
                    NodeList inputNodes = element.getElementsByTagName("input");
                    for (int j = 0; j < inputNodes.getLength(); j++) {
                        Element inputEl = (Element) inputNodes.item(j);
                        test.inputs.add(inputEl.getAttribute("ref")); // Store dependency id
                    }
                }
                tests.put(test.id, test); // Add test to map
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================================================================
    // Step 2 -- TODO: implement this method
    // =========================================================================
    /**
     * Computes and assigns processing_cost for every DERIVED test.
     * Cost rule:
     *   DERIVED tests depend only on RAW tests directly.
     *   Iterate over the test's direct inputs
     *   Count the number of distinct non-NONE sample types among them.
     *   That count is the processing_cost.
     * COMPOSITE tests are left at cost = 0 and must be skipped here. 
     * They are pure aggregations and carry no processing cost of their own.
     * Prints a summary line for each DERIVED test:
     *   <id>    processing_cost: <value>
     * surrounded by ##COST COMPUTATION## markers.
     */
    public void computeDerivedCosts() {
        System.out.println("##COST COMPUTATION##");

        // TODO: implement Step 2
        // Use System.out.printf("%-25s processing_cost: %d%n", id, cost);

        for (Test test : tests.values()) {

            // Only DERIVED tests need processing cost
            if (test.isDerived()) {

                // Removes duplicates
                Set <String> distinctSampleTypes = new HashSet<>();

                for (String inputId: test.inputs) {

                    Test inputTest = getTest(inputId);

                    // Check if it's valid and not NONE
                    if( inputTest != null && inputTest.sampleType != null && !inputTest.sampleType.equals("NONE") ) {

                        // Add sample type
                        distinctSampleTypes.add(inputTest.sampleType);
                    }
                }

                test.cost = distinctSampleTypes.size(); // Processing cost is equal to number of distinct sample types
                System.out.printf("%-25s processing_cost: %d%n", test.id, test.cost); // Print result

            }
        }

        System.out.println("##COST COMPUTATION COMPLETED##");
        System.out.println();
    }

    // =========================================================================
    // Accessors -- do NOT change these
    // =========================================================================

    /** Returns the Test object for the given ID, or null if not found. */
    public Test getTest(String id) {
        return tests.get(id);
    }

    /** Returns an unmodifiable view of all tests in the catalogue. */
    public Map<String, Test> getAllTests() {
        return Collections.unmodifiableMap(tests);
    }
}
