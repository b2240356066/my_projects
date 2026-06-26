import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.*;

public class SpaceportTransitNetwork {

    double shuttleSpeed;
    final double walkSpeed = 1000 / 6.0;

    Station start, end;
    List<ShuttleCorridor> corridors;

    String content;

    int getInt(String v) {
        Pattern p = Pattern.compile(v + "\\s*=\\s*([0-9]+)");
        Matcher m = p.matcher(content);
        m.find();
        return Integer.parseInt(m.group(1));
    }

    double getDouble(String v) {
        // TODO: Regex for double

        Pattern p = Pattern.compile(v + "\\s*=\\s*([0-9]*\\.?[0-9]+)");
        Matcher m = p.matcher(content);

        if (m.find()) return Double.parseDouble(m.group(1));

        return 0.0;
    }

    Point getPoint(String v) {
        // TODO: Regex for point (x,y)
        
        Pattern p = Pattern.compile(v + "\\s*=\\s*\\(\\s*([0-9]+)\\s*,\\s*([0-9]+)\\s*\\)");
        Matcher m = p.matcher(content);

        if (m.find()) return new Point(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
        return null;
    }

    List<ShuttleCorridor> parseCorridors() {
        List<ShuttleCorridor> list = new ArrayList<>();
        // TODO: Parse corridor names and stations

        Matcher nameMatcher = Pattern.compile("corridor_name\\s*=\\s*\"([^\"]+)\"").matcher(content);
        Matcher stationsMatcher = Pattern.compile("corridor_stations\\s*=\\s*((?:\\(\\s*\\d+\\s*,\\s*\\d+\\s*\\)\\s*)+)").matcher(content);

        while (nameMatcher.find() && stationsMatcher.find()) {

            String name = nameMatcher.group(1);
            List<Station> stations = new ArrayList<>();
            Matcher pointM = Pattern.compile("\\(\\s*([0-9]+)\\s*,\\s*([0-9]+)\\s*\\)").matcher(stationsMatcher.group(1));
            int idx = 1;
            while (pointM.find()) {
                stations.add(new Station(new Point(Integer.parseInt(pointM.group(1)), Integer.parseInt(pointM.group(2))), name + " Station " + idx++));
            }
            list.add(new ShuttleCorridor(name, stations));
        }
        return list;
    }

    void readInput(String f) {
        // TODO: Read file and initialize variables
        try {
            // Read entire file content into string for robust regex parsing 
            content = new String(Files.readAllBytes(Paths.get(f)));
            
            shuttleSpeed = getDouble("average_shuttle_speed") * (1000.0 / 60.0);
            
            // Initialize Origin and Destination 
            start = new Station(getPoint("origin_point"), "Origin Point");
            end = new Station(getPoint("destination_point"), "Destination Point");
            
            corridors = parseCorridors();
        } catch (Exception e) {

            
            e.printStackTrace();
        }
    }
}