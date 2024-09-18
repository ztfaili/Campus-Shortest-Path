import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Backend implements BackendInterface {
  GraphADT<String, Double> graph = new DijkstraGraph();
  List<String> locations = new ArrayList<>();
  
  public Backend(GraphADT<String,Double> graph) {
    this.graph = graph;
  }

  /**
   * Loads graph data from a dot file.
   * @param filename the path to a dot file to read graph data from
   * @throws IOException if there was a problem reading in the specified file
   */
  @Override
  public void loadGraphData(String filename) throws IOException {
    File dotFile = new File(filename);
    try {
      Scanner scnr = new Scanner(dotFile); // takes input of DOT file
      if(scnr.hasNextLine()) { // skips the header line
        scnr.nextLine();  
      }
      while(scnr.hasNextLine()) {
        String line = scnr.nextLine();
        // break when final line reached 
        if(!line.contains("->")) { 
          scnr.close();
          break;
        }
        line = line.trim();
        String[] data = processData(line);
        // get the edge value
        String precedingNode = data[0];
        String outgoingNode = data[1];
        Double edgeWeight = Double.valueOf(data[2].substring(8, data[2].indexOf("]")));
        // insert the current graph node and edge if not in graph
	if(!locations.contains(precedingNode)) {
            locations.add(precedingNode);
	}
        if(!graph.containsNode(precedingNode)) {
          graph.insertNode(precedingNode);  
        }
	if(!graph.containsNode(outgoingNode)) {
	    graph.insertNode(outgoingNode);
	}
        graph.insertEdge(precedingNode, outgoingNode, edgeWeight);
        
        // **** 
        //System.out.println(precedingNode + " -> " + outgoingNode + " seconds: " + edgeWeight); 
      }
    }
    catch (FileNotFoundException e){
      throw new IOException("Problem reading file!");
    }
    
  }
  
  /**
   * Private helper method for loadGraphData that processes each line in the DOT file.
   * 
   * @param line - line to be processed
   * @return a String array with preceding node, outgoing node, and edge value
   */
  private String[] processData(String line) {
    String[] data = line.split("->");
    // get the preceding node
    String precedingNode = data[0];
    precedingNode = precedingNode.replaceAll("\"", "");
    precedingNode = precedingNode.trim();
    
    // split the outgoing node and the edge weight
    data[1] = data[1].trim();
    String[] s = data[1].split("\\[");
    String outgoingNode = s[0];
    outgoingNode = outgoingNode.replaceAll("\"", "");
    outgoingNode = outgoingNode.trim();
    String weight = s[1];
    
    // place the data in an array
    String[] reformattedData = new String[3];
    reformattedData[0] = precedingNode;
    reformattedData[1] = outgoingNode;
    reformattedData[2] = weight;

    return reformattedData;
  }
  
  /**
   * Returns a list of all locations (nodes) available on the backend's graph.
   * @return list of all location names
   */
  @Override
  public List<String> getListOfAllLocations() {
    return locations;
  }

  /**
   * Returns the sequence of locations along the shortest path from startLocation to endLocation, or
   * an empty list if no such path exists.
   * @param startLocation the start location of the path
   * @param endLocation the end location of the path
   * @return a list with the nodes along the shortest path from startLocation to endLocation, or
   *         an empty list if no such path exists
   */
  @Override
  public List<String> findShortestPath(String startLocation, String endLocation) {
      try {
	  return graph.shortestPathData(startLocation, endLocation);
      }
      catch (Exception e) {
	  return new ArrayList<String>();
      }
  }

  /**
   * Returns the walking times in seconds between each two nodes on the shortest path from startLocation
   * to endLocation, or an empty list of no such path exists.
   * @param startLocation the start location of the path
   * @param endLocation the end location of the path
   * @return a list with the walking times in seconds between two nodes along the shortest path from
   *         startLocation to endLocation, or an empty list if no such path exists
   */
  @Override
  public List<Double> getTravelTimesOnPath(String startLocation, String endLocation) {
    List<String> path = graph.shortestPathData(startLocation, endLocation);
    List<Double> times = new ArrayList<>();
    for(int i = 0; i < path.size(); i++) {
      if(i + 1 >= path.size()) {
        break;
      }
      times.add(graph.getEdge(path.get(i), path.get(i + 1)));
    }
    return times;
  }

  /**
   * Returns all locations that are reachable from startLocation in at most timesInSec walking time.
   * @param startLocation the maximum walking time for a destination to be included in the list
   */
  @Override
  public List<String> getReachableLocations(String startLocation, double timesInSec) {
    List<String> reachableLocations = new ArrayList<>();
    // call shortestPathCost on every graph location
    for(String s : locations) {
      try {
	    double cost = graph.shortestPathCost(startLocation, s);
	    if(cost <= timesInSec) {
		reachableLocations.add(s); // shortestPath within timesInSec
	    }
      }
      catch (Exception e) {
	  continue;
      }
      
    }
    return reachableLocations;
  }
  
}

