import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.application.Application;
import javafx.stage.Window;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.application.Platform;
import java.util.ArrayList;
import java.util.List;

public class FrontendDeveloperTests extends ApplicationTest {

    @BeforeEach
    public void setup() throws Exception {
    GraphADT<String, Double> graph = new DijkstraGraph();
  	Backend backend = new Backend(graph);
  	backend.loadGraphData("campus.dot");
       Frontend.setBackend(backend);
	   ApplicationTest.launch(Frontend.class);
    }
    
   	
    @Test
    public void testShortestPathControlsExistence() {
        try {

	        Label label2 = lookup("#src123").query();
            assertEquals("Path Start Selector: ",label2.getText());
            label2 = lookup("#dst").query();
            assertEquals("Path End Selector: ",label2.getText());
       } catch (Exception e) {
            System.out.println(e);
           assertEquals(0, 1);
       }
    }


    @Test
    public void testSubmitExistence() {
        try {
            Button button2 = lookup("#findPath").query();
            assertEquals("Submit/Find Button",button2.getText());
            button2 = lookup("#findRange").query();
            assertEquals("Find Locations",button2.getText());	
        } catch (Exception e) {
            assertEquals(0, 1);
        }
    }

    @Test
    public void testForQuitButtonExistence() {
        try {
            Button button2 = lookup("#quit").query();
            assertEquals("Quit",button2.getText());
            button2 = lookup("#abt").query();
            assertEquals("About",button2.getText());
        } catch (Exception e) {
            assertEquals(0, 1);
        }
    }
    
    @Test
    public void testPartnerShortestPath() {
    	try {
    		GraphADT<String, Double> graph = new DijkstraGraph();
  		Backend backend = new Backend(graph);
  		backend.loadGraphData("campus.dot");
  		
  		List<String> result = backend.findShortestPath("Sterling Hall", "Thomas C. Chamberlin Hall");
  		
  		ArrayList<String> expected = new ArrayList<>();
  		expected.add("Sterling Hall");
  		expected.add("Birge Hall");
  		expected.add("Thomas C. Chamberlin Hall");	
    		
    		assertEquals(result, expected);
    	} catch (Exception e) {
    		System.out.println(e);
    		assertEquals(0, 1);
    	}
    }
    
    public void testPartnerReachable() {
    	try {
    		GraphADT<String, Double> graph = new DijkstraGraph();
  		Backend backend = new Backend(graph);
  		backend.loadGraphData("campus.dot");
  		
  		List<String> result = backend.getReachableLocations("Van Vleck Hall", 150);
  		
  		ArrayList<String> expected = new ArrayList<>();
  		expected.add("Van Vleck Hall");
  		expected.add("Sterling Hall");	
    		
    		assertEquals(result, expected);	
    		
    	} catch (Exception e) {
    		System.out.println(e);
    		assertEquals(0, 1);
    	}
    }

    @Test
    public void testIntegrationInit() {
	try {
	    GraphADT<String, Double> graph = new DijkstraGraph();
  		Backend backend = new Backend(graph);
  		backend.loadGraphData("campus.dot");
       		Frontend.setBackend(backend);
	   
	} catch (Exception e) {
	    assertEquals(0, 1);
	}
    }

    @Test
    public void testIntegerationList() {
	try {
        GraphADT<String, Double> graph = new DijkstraGraph();
	    Backend backend = new Backend(graph);
	    backend.loadGraphData("campus.dot");
	    ArrayList<String> locs = (ArrayList<String>) backend.getListOfAllLocations();
	    System.out.println(locs);
	    ArrayList<String> locsToTest = new ArrayList<String>();
	    locsToTest.add("Van Hise Hall");
	    locsToTest.add("Fleet and Service Garage");
	    locsToTest.add("Ogg Residence Hall");
	    locsToTest.add("Thomas C. Chamberlin Hall");
	    locsToTest.add("Babcock Hall");

	    for (int i = 0; i < locsToTest.size(); i++) {
		boolean flag = false;
		System.out.println(locsToTest.get(i));
		for (int j = 0; j < locs.size(); j++) {

		    if (locs.get(j).equals(locsToTest.get(i))) {
			flag = true;
		    }
		}

		assertEquals(flag, true);
		
	    }
	} catch (Exception e) {
	    System.out.println("=======================================");
	    System.out.println(e);
	    System.out.println("=======================================");
	    assertEquals(0, 1);
	}
    }

    public static void main(String[] args) {
    	try {
    GraphADT<String, Double> graph = new DijkstraGraph();
  	Backend backend = new Backend(graph);
  	backend.loadGraphData("campus.dot");
       	Frontend.setBackend(backend);
        Application.launch(Frontend.class); 
        } catch (Exception e) {
        	;
        }
    }
}
