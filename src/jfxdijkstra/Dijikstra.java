/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxdijkstra;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;

/**
 *
 * @author HP
 */
public class Dijikstra extends Application{
    
    private Path path;
    private Label label = new Label("Wait mouse");
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        Group racine = new Group();
        stage.getIcons().add(new Image("jfxdijkstra/dijkstraexample.png"));
        stage.setTitle("Dijkstra");
        Scene scene = new Scene(root, 1200, 800);
        stage.setResizable(false);
        stage.setHeight(800);
        stage.setWidth(1200);
        scene.getStylesheets().add
         (Dijikstra.class.getResource("css.css").toExternalForm());
        
        stage.setScene(scene);
        scene.setOnMouseClicked(mouseHandler);
        scene.setOnMouseDragged(mouseHandler);
        scene.setOnMouseEntered(mouseHandler);
        scene.setOnMouseExited(mouseHandler);
        scene.setOnMouseMoved(mouseHandler);
        scene.setOnMousePressed(mouseHandler);
        scene.setOnMouseReleased(mouseHandler);
        
        path = new Path();
        path.setStrokeWidth(1);
        path.setStroke(Color.BLACK);
        path.setSmooth(true);
        racine.getChildren().add(label);
        racine.getChildren().add(path);
        stage.setScene(scene);
        stage.show();         
    }
    EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {            
            label.setText(mouseEvent.getEventType() + "\n"
                    + "X : Y - " + mouseEvent.getX() + " : " + mouseEvent.getY() + "\n"
                    + "SceneX : SceneY - " + mouseEvent.getSceneX() + " : " + mouseEvent.getSceneY() + "\n"
                    + "ScreenX : ScreenY - " + mouseEvent.getScreenX() + " : " + mouseEvent.getScreenY());
            
            if(mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED){
                path.getElements().clear();
                path.getElements().add(new MoveTo(mouseEvent.getX(), mouseEvent.getY()));
            }else if(mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED){
                path.getElements().add(new LineTo(mouseEvent.getX(), mouseEvent.getY()));
            }
        }
        
    };
    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
