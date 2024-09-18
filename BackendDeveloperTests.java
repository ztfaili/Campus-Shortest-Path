import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.application.Application;
import javafx.stage.Window;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BackendDeveloperTests extends ApplicationTest {

  @BeforeEach
  public void setup() throws Exception {
      Frontend.setBackend(new Backend(new DijkstraGraph<String, Double>()));
      ApplicationTest.launch(Frontend.class);
  }
    
  /**
   * Tests the functionality of the getAllLocations method.
   */
  @Test
  public void testLocations() {
      BackendInterface backend = new Backend(new DijkstraGraph<String, Double>());
    try {
      backend.loadGraphData("campus.dot");
    }
    catch (IOException e) {
	Assertions.assertTrue(false);
    }
    List<String> locations = backend.getListOfAllLocations();
    // list that contiains some of the campus dot file locationsa
    ArrayList<String> campusGraphLocations = new ArrayList<String>();
    // cannot test union south, atmospheric and CS locations because these three get skipped 
    // in loadGraphData method due to the nature of GraphPlaceholder method containsNode
    campusGraphLocations.add("Carillon Tower");
    campusGraphLocations.add("Chemistry Building");
    campusGraphLocations.add("Thomas C. Chamberlin Hall");
    campusGraphLocations.add("Brogden Psychology");
    for(String s : campusGraphLocations) {
      Assertions.assertTrue(locations.contains(s));
    }
    
  }
  
  /**
   * Tests the functionality of the shortest path method.
   */
    @Test
  public void testShortestPath() {
    // Testing hard coded values b/c graph not implemented yet
    BackendInterface backend = new Backend(new DijkstraGraph<String, Double>());
    try {
      backend.loadGraphData("campus.dot");
    }
    catch (IOException e) {}
    List<String> shortestPath = 
        backend.findShortestPath("Union South", "Atmospheric, Oceanic and Space Sciences");
    ArrayList<String> expectedPath = new ArrayList<>(); // shortest path from mem to edu
    expectedPath.add("Union South");
    expectedPath.add("Atmospheric, Oceanic and Space Sciences");
    Assertions.assertEquals(shortestPath, expectedPath);
  }
  
  /**
   * Tests the functionality of the getTravelTimes method. 
   */
  @Test
  public void testTravelTimes() {
    // Testing hard coded values b/c graph not implemented yet
    BackendInterface backend = new Backend(new DijkstraGraph<String, Double>());
    try {
      backend.loadGraphData("campus.dot");
    }
    catch (IOException e) {}
    List<Double> travelTimes = 
	 backend.getTravelTimesOnPath("Union South", "Radio Hall");
    ArrayList<Double> expectedTimes = new ArrayList<>();
    expectedTimes.add(154.9);
    expectedTimes.add(118.9);
    expectedTimes.add(209.8);
    expectedTimes.add(189.1);
    expectedTimes.add(76.6);
    expectedTimes.add(137.5);
    expectedTimes.add(106.3);
    expectedTimes.add(214.8);
    expectedTimes.add(113.0);
    System.out.println(travelTimes);
    for (int i = 0; i < travelTimes.size(); i++) {
    	Double check = travelTimes.get(i) - expectedTimes.get(i);
    	if (check < 0) {check = check * -1;}
    	
    	if (check > 0.0001) {
    		Assertions.assertEquals(1, 0);
    	} 
    }
    
    
  }
  
  /**
   * Tests the functionality of the getReachableLoactions method.
   */
  @Test
  public void testReachableLoactions() {
    // Testing hard coded values b/c graph not implemented yet
    BackendInterface backend = new Backend(new GraphPlaceholder());
    try {
      backend.loadGraphData("campus.dot");
    }
    catch (IOException e) {}
    List<String> locations = backend.getReachableLocations("Helen C White Hall", 400);
    ArrayList<String> expectedLocations = new ArrayList<>();
    expectedLocations.add("Science Hall");
    expectedLocations.add("Radio Hall");
    expectedLocations.add("Brat Stand");
    expectedLocations.add("Grand Central");
    expectedLocations.add("Carillon Tower");
    expectedLocations.add("The Crossing");
    for(String s : expectedLocations) {
      Assertions.assertTrue(locations.contains(s));
    }
  }

    /**
     * Tests that the frontend is able to be initialized with a backend object.
     */
  @Test
  public void integrationTestInitialize() {
      try {	  
	  Frontend.setBackend(new Backend(new DijkstraGraph<String, Double>()));
      }
      catch (Exception e) {
	  Assertions.assertTrue(false);
      }
  }
    /**
     * Tests the reachable locations button.
     */
  @Test
  public void intergrationTestReachable() {                   
      Button reachableButton = lookup("#findRange").query();
      Assertions.assertEquals(reachableButton.getText(), "Find Locations");
      try {
	  clickOn(reachableButton);
      }
      catch (NullPointerException e) {
	  Assertions.assertTrue(false);
      }
  }

  public static void main(String[] args) {
    Application.launch(Frontend.class);
  }

}

