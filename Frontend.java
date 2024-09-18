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
import javafx.scene.layout.VBox;

public class Frontend extends Application implements FrontendInterface {

  private static BackendInterface back;
  private boolean showTimes = false; 
  private List<String> shPath;
  private List<Double> pathTimes;
  private Label path = new Label(); 
  private Label reachableLabel = new Label();
  private List<String> out = new ArrayList<>();

  public static void setBackend(BackendInterface back) {
    Frontend.back = back;
  }

  public void start(Stage stage) {
    Pane root = new Pane();
    try {
	back.loadGraphData("campus.dot");
    }
    catch (Exception e) {
	e.printStackTrace();
	System.out.println("Error reading file");
    }
    
    createAllControls(root);

    Scene scene = new Scene(root, 800, 600);
    stage.setScene(scene);
    stage.setTitle("P2: Prototype");
    
    stage.show();
  }

  public void createAllControls(Pane parent) {
    createShortestPathControls(parent);
    createPathListDisplay(parent);
    createAdditionalFeatureControls(parent);
    createAboutAndQuitControls(parent);
    parent.getChildren().add(path);
  }

  public void createShortestPathControls(Pane parent) {
    List<String> locs = back.getListOfAllLocations();
    System.out.println(locs.size());
    Label src = new Label("Path Start Selector: ");
    src.setId("src123");
    src.setLayoutX(16);
    src.setLayoutY(16);

    ComboBox start = new ComboBox();
    start.getItems().addAll(locs);
    start.setLayoutX(150);
    start.setLayoutY(16);

    parent.getChildren().add(src);
    parent.getChildren().add(start);

    Label dst = new Label("Path End Selector: ");
    dst.setId("dst");
    dst.setLayoutX(16);
    dst.setLayoutY(48);
    parent.getChildren().add(dst);

    ComboBox end = new ComboBox();
    end.getItems().addAll(locs);
    end.setLayoutX(150);
    end.setLayoutY(48);
    parent.getChildren().add(end);

    Button find = new Button("Submit/Find Button");
    find.setId("findPath");
    find.setLayoutX(32);
    find.setLayoutY(80);
    parent.getChildren().add(find);

    find.setOnAction(e -> {
      if (start.getValue() != null && end.getValue() != null) {
          shPath = back.findShortestPath(start.getValue().toString(), end.getValue().toString());
          pathTimes = back.getTravelTimesOnPath(start.getValue().toString(), end.getValue().toString());
          System.out.println("From: " + start.getValue().toString() + " to: " + end.getValue().toString());
          createPathListDisplay(parent);
      }
    });
  }

  public void createPathListDisplay(Pane parent) {
    String s = new String();
    s =  "Choose a route to get a path!";
    if (shPath != null) {

      if (showTimes) {
        s = "Results List (with travel times): \n\t";

        for (int i = 0; i < shPath.size(); i++) {
          s = s +  shPath.get(i);

          if (i < pathTimes.size()) {
           s = s + " - (" + pathTimes.get(i).toString() + ") \n\t";
          } else  {
            s = s + " \n\t";
          }
        }
      } else {
        s = "Results List: \n\t";

        for (int i = 0; i < shPath.size(); i++) {
          s = s +  shPath.get(i) + " \n\t";
        }
      }     
    } 
    path.setText(s);
    path.setLayoutX(32);
    path.setLayoutY(112);
  }

  public void createAdditionalFeatureControls(Pane parent) {
    this.createTravelTimesBox(parent);
    this.createFindReachableControls(parent);
  }

  public void createTravelTimesBox(Pane parent) {
    CheckBox showTimesBox = new CheckBox("Show Walking Times");
    showTimesBox.setLayoutX(200);
    showTimesBox.setLayoutY(80);

    showTimesBox.setOnAction(e -> {
      if (showTimesBox.isSelected()) {
        showTimes = true;
      } else {
        showTimes = false;
      }
    });

    parent.getChildren().add(showTimesBox);
  }

  public void createFindReachableControls(Pane parent) {
    List<String> locs = back.getListOfAllLocations();
    Label locationSelector = new Label("Location Selector: ");
    locationSelector.setLayoutX(16);
    locationSelector.setLayoutY(300);

    ComboBox cen = new ComboBox();
    cen.getItems().addAll(locs);
    cen.setLayoutX(150);
    cen.setLayoutY(300);
    parent.getChildren().add(cen);

    parent.getChildren().add(locationSelector);
    Label timeSelector = new Label("Time Selector (in seconds): ");
    timeSelector.setLayoutX(16);
    timeSelector.setLayoutY(332);
    parent.getChildren().add(timeSelector);

    TextField inp = new TextField("200");
    inp.setLayoutX(200);
    inp.setLayoutY(332);
    parent.getChildren().add(inp);

    Button findLocations = new Button("Find Locations");
    findLocations.setLayoutX(16);
    findLocations.setLayoutY(364);
    findLocations.setId("findRange");
    parent.getChildren().add(findLocations);

    
    findLocations.setOnAction(ex-> {
      if (cen.getValue() != null) {
        Double range = 0.0;
        try { 
          range = Double.valueOf(inp.getText().toString());
        } catch (Exception e) { range = 0.0;}
        out = back.getReachableLocations(cen.getValue().toString(), range);

        String s = "";

        s = "Locations in walking distance: \n\t";

       for (int i = 0; i < out.size(); i++) {
         s = s + out.get(i) + "\n\t";
      }
      reachableLabel.setText(s);
      }});

    reachableLabel.setLayoutX(16);
    reachableLabel.setLayoutY(398);
    parent.getChildren().add(reachableLabel);
  }

  public void createAboutAndQuitControls(Pane parent) {
    Button about = new Button("About");
    about.setLayoutX(32);
    about.setLayoutY(560); 
    about.setId("abt");
    parent.getChildren().add(about);

    Button quit = new Button("Quit");
    quit.setLayoutX(96);
    quit.setId("quit");
    quit.setLayoutY(560);
    quit.setOnAction(e -> Platform.exit());
    parent.getChildren().add(quit);
  }
}

