/*
 * TP Algorithmique avencé, implémentation de l'algorithme du plus court chemain de Dijikstra
 * Master1 Reseaux 2015/2016
 */
package jfxdijkstra;

//<editor-fold defaultstate="collapsed" desc="Imports">
//import dijikstra.jfoenix.controls.JFXButton;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import java.awt.Point;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;
//</editor-fold>

/**
 *
 * @author Hadjadj Taha ElAmine
 */
public class FXMLDocumentController implements Initializable {
    
    //<editor-fold defaultstate="collapsed" desc="Declarations des elements de l'interface graphique">
    Dijikstra dijik = new Dijikstra();
    int nNoeud =0;
    Noeudfx selectedNoeud = null;
    List <Noeudfx> circles= new ArrayList<Noeudfx>();
    boolean ajouter = true, rel = false, calculer=false, calculated=false;
    List <Label> distances = new ArrayList<Label>();
    MediaPlayer mp, mp2, mp3;
    
    @FXML
    private Label label, poid,noeudDebut= new Label("debut"),demo;
    @FXML
    private Pane viewer, retour;
    @FXML
    private MediaView media;
    @FXML
    private Group noeuds, intro;
    @FXML
    private Line arret;
    @FXML
    private JFXButton reset, relier, CalcDijik, clear;
    @FXML
    private JFXButton ajout;
    @FXML
    private JFXRadioButton b1,b2,b3;
    
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Controles de l'interface graphique">
    
    /**
     *  L'action qui se produit quand on clique sur l'interface, ajout des noeud
     * @param mouseEvent
     */
    @FXML
    public void handle(MouseEvent mouseEvent) {
        if(((mouseEvent.getSceneX()>0&&mouseEvent.getSceneX()<1200)&&(mouseEvent.getSceneY()>0&&mouseEvent.getSceneY()<800))&& ajouter ){
            if(nNoeud==2)
                AjoutHandle(null);
            if(nNoeud==1)
                reset.setDisable(false);
            
            label.setText("Ajout de Noeuds" + "\n"
                    + "X : Y - " + mouseEvent.getX() + " : " + mouseEvent.getY() + "\n"
                    + "Cliquez n'importe ou pour ajouter un noeud " + "\n"+ "n noeuds " + nNoeud);
            
            if(!(mouseEvent.getSource()).equals(noeuds))
                if(mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED && mouseEvent.getButton()== MouseButton.PRIMARY){
                    if(selectedNoeud!=null){
                        selectedNoeud.isSelected=false;
                        FillTransition ft1 = new FillTransition(Duration.millis(300),selectedNoeud, Color.RED, Color.BLACK);
                        ft1.play();
                        selectedNoeud=null;
                    }
                    nNoeud++;
                    Noeudfx circle = new Noeudfx(mouseEvent.getX(), mouseEvent.getY(), 1, String.valueOf(nNoeud));
                    noeuds.getChildren().add(circle);
                    
                    circle.setOnMousePressed(mouseHandler);
                    circle.setOnMouseReleased(mouseHandler);
                    circle.setOnMouseDragged(mouseHandler);
                    circle.setOnMouseExited(mouseHandler);
                    circle.setOnMouseEntered(mouseHandler);
                    
                    ScaleTransition tr = new ScaleTransition(Duration.millis(100), circle);
                    tr.setByX(10f);
                    tr.setByY(10f);
                    tr.setInterpolator(Interpolator.EASE_OUT);
                    tr.play();
                }
        }
    }
    @FXML
    public void b1Handle(ActionEvent event) {
            b1.setSelected(true);
            b2.setSelected(false);
            b3.setSelected(false);
            
            media.setMediaPlayer(mp);
            mp.setCycleCount(MediaPlayer.INDEFINITE);
            mp.play();
            demo.setText("Pour ajouter un noeud, Selectionez \"Ajouter\"  puis cliquez ou vous joulez l'ajouter.");
    }
    @FXML
    public void b2Handle(ActionEvent event) {
            b1.setSelected(false);
            b2.setSelected(true);
            b3.setSelected(false);
            
            media.setMediaPlayer(mp2);
            mp2.setCycleCount(MediaPlayer.INDEFINITE);
            mp2.setMute(true);
            mp2.play();
            demo.setText("Glissez un noeud pour créer un autre relié avec.");
    }
    
    @FXML
    public void b3Handle(ActionEvent event) {
            b1.setSelected(false);
            b2.setSelected(false);
            b3.setSelected(true);
            
            media.setMediaPlayer(mp3);
            mp3.setCycleCount(MediaPlayer.INDEFINITE);
            mp3.play();
            demo.setText("Pour relier deux noeud, Selectionez \"Rlier\"  puis cliquez sur les noeuds pour créer un arret entre eux.");
    }
    
    
    @FXML
    public void RetourHandle(MouseEvent event) {
        FadeTransition ft = new FadeTransition(Duration.millis(500), intro);
//        ft.setFromValue(1);
        ft.setToValue(0);
        ft.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                intro.getChildren().clear();
            }
        });
        ft.onFinishedProperty();
        ft.play();
    }
    @FXML
    public void ResetHandle(ActionEvent event) {
        nNoeud=0;
        noeuds.getChildren().clear();
        noeuds.getChildren().addAll(viewer,label);
        dijik = new Dijikstra();
        selectedNoeud = null;
        circles= new ArrayList<Noeudfx>();
        ajouter = true; rel = false; calculer=false; calculated=false;
        distances = new ArrayList<Label>();
        ajout.setDisable(ajouter);
        relier.setDisable(true);
        CalcDijik.setDisable(true);
        reset.setDisable(true);
        clear.setDisable(true);
    }
    @FXML
    public void ClearHandle(ActionEvent event) {
        selectedNoeud=null;
        calculated=false;
        for(Noeudfx n: circles){
            n.isSelected=false;
            n.noeud.previous=null;
            n.noeud.minDistance= Double.POSITIVE_INFINITY;
            FillTransition ft1 = new FillTransition(Duration.millis(300),n);
            ft1.setToValue(Color.BLACK);
            ft1.play();
        }
        noeuds.getChildren().remove(noeudDebut);
        
        for(Label l:distances){
            l.setText("distance : infinit");
            noeuds.getChildren().remove(l);
        }
        distances=new ArrayList<Label>();
    }
    @FXML
    public void RelierHandle(ActionEvent event) {
        ajouter=false;
        rel=true;
        calculer=false;
        ajout.setDisable(ajouter);
        relier.setDisable(rel);
        CalcDijik.setDisable(calculer);
    }
    @FXML
    public void CalcHandle(ActionEvent event) {
        ajouter=false;
        rel=false;
        calculer=true;
        ajout.setDisable(ajouter);
        relier.setDisable(rel);
        CalcDijik.setDisable(calculer);
        clear.setDisable(false);
    }
    @FXML
    public void AjoutHandle(ActionEvent event) {
        ajouter=true;
        rel=false;
        calculer=false;
        ajout.setDisable(true);
        CalcDijik.setDisable(false);
        relier.setDisable(false);
    }
    
    EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            if(mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED && mouseEvent.getButton()== MouseButton.PRIMARY){
                Noeudfx circle = (Noeudfx)mouseEvent.getSource();
                
                poid = new Label();
                arret=new Line();
                noeuds.getChildren().add(arret);
                noeuds.getChildren().add(poid);
                
                
                if(!circle.isSelected){
                    if(selectedNoeud!=null){
                        if (rel){
                            arret.setStartX(selectedNoeud.point.x);
                            arret.setStartY(selectedNoeud.point.y);
                            arret.setEndX(circle.point.x);
                            arret.setEndY(circle.point.y);
                            poid.setLayoutX(((selectedNoeud.point.x)+(circle.point.x))/2);
                            poid.setLayoutY(((selectedNoeud.point.y)+(circle.point.y))/2);
                            poid.setText(String.valueOf(
                                    ((int)Math.sqrt(
                                            Math.pow((circle.point.x - (selectedNoeud.point.x)), 2)
                                                    + Math.pow((circle.point.y - (selectedNoeud.point.y)), 2)
                                    ))/10));
                            
                            selectedNoeud.noeud.adjacents.add( new Arret(circle.noeud, Integer.valueOf(poid.getText())) );
                            circle.noeud.adjacents.add( new Arret (selectedNoeud.noeud, Integer.valueOf(poid.getText())) );
                        }
                        if(ajouter || (calculer &&!calculated) || rel){
                            selectedNoeud.isSelected=false;
                            FillTransition ft1 = new FillTransition(Duration.millis(300),selectedNoeud, Color.RED, Color.BLACK);
                            ft1.play();
                        }
                        
                    }
                    FillTransition ft = new FillTransition(Duration.millis(300),circle, Color.BLACK, Color.RED);
                    ft.play();
                    circle.isSelected=true;
                    selectedNoeud=circle;
                    if(calculer && !calculated){
                        dijik.computePaths(circle.noeud);
                        calculated = true;
                    }else if(calculer && calculated){
                        for(Noeudfx n: circles){
                            n.isSelected=false;
                            FillTransition ft1 = new FillTransition(Duration.millis(300),n);
                            ft1.setToValue(Color.BLACK);
                            ft1.play();
                        }
                        List<Noeud> path = dijik.getShortestPathTo(circle.noeud);
                        for(Noeud n: path){
                            FillTransition ft1 = new FillTransition(Duration.millis(300),n.circle);
                            ft1.setToValue(Color.BLUE);
                            ft1.play();
                        }
                        label.setText("Distance to " + circle.noeud + ": " + circle.noeud.minDistance+ " path : "+path);
                        
                    }
                }
                else{
                    circle.isSelected=false;
                    FillTransition ft1 = new FillTransition(Duration.millis(300),circle, Color.RED, Color.BLACK);
                    ft1.play();
                    selectedNoeud=null;
                }
            }
            else if(mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED && mouseEvent.getButton()== MouseButton.PRIMARY){
                if(((mouseEvent.getSceneX()>0&&mouseEvent.getSceneX()<1200)&&(mouseEvent.getSceneY()>0&&mouseEvent.getSceneY()<800))&&((mouseEvent.getSceneX()>=(((Noeudfx)mouseEvent.getSource()).point.x+((Noeudfx)mouseEvent.getSource()).getScaleX()) || mouseEvent.getSceneX()<=(((Noeudfx)mouseEvent.getSource()).point.x)) ||
                        (mouseEvent.getSceneY()>=(((Noeudfx)mouseEvent.getSource()).point.y+((Noeudfx)mouseEvent.getSource()).getScaleY()) || mouseEvent.getSceneY()<=(((Noeudfx)mouseEvent.getSource()).point.y)))){
                    ((Node) mouseEvent.getSource()).setCursor(Cursor.CLOSED_HAND);
                    arret.setStartX(((Noeudfx)mouseEvent.getSource()).point.x);
                    arret.setStartY(((Noeudfx)mouseEvent.getSource()).point.y);
                    arret.setEndX(mouseEvent.getSceneX());
                    arret.setEndY(mouseEvent.getSceneY());
                    poid.setLayoutX(((((Noeudfx)mouseEvent.getSource()).point.x)+(mouseEvent.getSceneX()))/2);
                    poid.setLayoutY(((((Noeudfx)mouseEvent.getSource()).point.y)+(mouseEvent.getSceneY()))/2);
                    poid.setText(String.valueOf(
                            ((int)Math.sqrt(
                                    Math.pow(((mouseEvent.getSceneX()) - (((Noeudfx)mouseEvent.getSource()).point.x)), 2)
                                            + Math.pow(((mouseEvent.getSceneY()) - (((Noeudfx)mouseEvent.getSource()).point.y)), 2)
                            ))/10));
                }
            }
            else if(mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED && mouseEvent.getButton()== MouseButton.PRIMARY){
                if(((mouseEvent.getSceneX()>0&&mouseEvent.getSceneX()<1200)&&(mouseEvent.getSceneY()>0&&mouseEvent.getSceneY()<800))&&((mouseEvent.getSceneX()>=(((Noeudfx)mouseEvent.getSource()).point.x+((Noeudfx)mouseEvent.getSource()).getScaleX()) || mouseEvent.getSceneX()<=(((Noeudfx)mouseEvent.getSource()).point.x-((Noeudfx)mouseEvent.getSource()).getScaleX())) ||
                        (mouseEvent.getSceneY()>=(((Noeudfx)mouseEvent.getSource()).point.y+((Noeudfx)mouseEvent.getSource()).getScaleY()) || mouseEvent.getSceneY()<=(((Noeudfx)mouseEvent.getSource()).point.y-((Noeudfx)mouseEvent.getSource()).getScaleY())))){
                    
                    ((Noeudfx)mouseEvent.getSource()).isSelected=false;
                    FillTransition ft1 = new FillTransition(Duration.millis(300),((Noeudfx)mouseEvent.getSource()), Color.RED, Color.BLACK);
                    ft1.play();
                    selectedNoeud=null;
                    nNoeud++;
                    Noeudfx circle = new Noeudfx(mouseEvent.getSceneX(), mouseEvent.getSceneY(), 1, String.valueOf(nNoeud));
                    noeuds.getChildren().add(circle);
                    
                    ((Noeudfx)mouseEvent.getSource()).noeud.adjacents.add( new Arret(circle.noeud, Integer.valueOf(poid.getText())) );
                    circle.noeud.adjacents.add( new Arret(((Noeudfx)mouseEvent.getSource()).noeud, Integer.valueOf(poid.getText())) );
                    
                    circle.setOnMousePressed(mouseHandler);
                    circle.setOnMouseReleased(mouseHandler);
                    circle.setOnMouseDragged(mouseHandler);
                    
                    ScaleTransition tr = new ScaleTransition(Duration.millis(100), circle);
                    tr.setByX(10f);
                    tr.setByY(10f);
                    tr.setInterpolator(Interpolator.EASE_OUT);
                    tr.play();
                }
            }
        }
        
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            mp = new MediaPlayer(new Media(new URI(FXMLDocumentController.this.getClass().getResource("add.mp4").toString()).toString()));
            mp2 = new MediaPlayer(new Media(new URI(FXMLDocumentController.this.getClass().getResource("dragAdd.mp4").toString()).toString()));
            mp3 = new MediaPlayer(new Media(new URI(FXMLDocumentController.this.getClass().getResource("relier.mp4").toString()).toString()));
        } catch (URISyntaxException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        media.setMediaPlayer(mp);
        mp.setCycleCount(MediaPlayer.INDEFINITE);
        mp.play();
        media.setSmooth(true);

    }
    
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Fonction de calcule de Dijikstra">
    
    public class Arret{
        public final Noeud target;
        public final double poids;
        
        public Arret(Noeud argTarget, double argPoids){
            target = argTarget; poids = argPoids;
        }
        
    }
    class Noeud implements Comparable<Noeud>{
        
        public String name;
        public List <Arret> adjacents = new ArrayList<Arret>();
        public double minDistance = Double.POSITIVE_INFINITY;
        public Noeud previous;
        public Noeudfx circle;
        
        public Noeud(String argName){
            name = argName;
        }
        public Noeud(String argName, Noeudfx c){
            name = argName;
            circle=c;
        }
        public String toString() {
            return name;
        }
        public int compareTo(Noeud other){
            return Double.compare(minDistance, other.minDistance);
        }
        
    }
    
    
    public class Dijikstra {
        public  boolean init=false;
        
        
        
        public void computePaths(Noeud source){
            
            //<editor-fold defaultstate="collapsed" desc="Annimation graphique">
            for(Noeudfx n:circles){
                distances.add(n.distance);
                n.distance.setLayoutX(n.point.x+20);
                n.distance.setLayoutY(n.point.y);
                noeuds.getChildren().add(n.distance);
            }
            noeudDebut.setLayoutX(source.circle.point.x+20);
            noeudDebut.setLayoutY(source.circle.point.y+10);
            noeuds.getChildren().add(noeudDebut);
            SequentialTransition st = new SequentialTransition();
            source.circle.distance.setText("distance : "+0);
            //</editor-fold>
            
            source.minDistance = 0.;
            PriorityQueue<Noeud> noeudQueue = new PriorityQueue<Noeud>();
            noeudQueue.add(source);
            while (!noeudQueue.isEmpty()) {
                Noeud u = noeudQueue.poll();
                
                // Visit each arret exiting u
                for (Arret e : u.adjacents){
                    if(e!=null){
                        Noeud v = e.target;
                        //<editor-fold defaultstate="collapsed" desc="Annimations graphique">
                        
                        FillTransition ft = new FillTransition(Duration.millis(500), v.circle);
                        if(v.circle.getFill()==Color.BLACK)
                            ft.setToValue(Color.ORANGE);
                        st.getChildren().add(ft);
                        
                        //</editor-fold>
                        double poids = e.poids;
                        double distanceThroughU = u.minDistance + poids;
                        if (distanceThroughU < v.minDistance) {
                            //<editor-fold defaultstate="collapsed" desc="Annimations graphiques">
                            
                            FillTransition ft1 = new FillTransition(Duration.millis(500), v.circle);
                            ft1.setToValue(Color.BLUEVIOLET);
                            ft1.setOnFinished(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    v.circle.distance.setText("distance : "+v.minDistance);
                                }
                            }
                            );
                            ft1.onFinishedProperty();
                            st.getChildren().add(ft1);
                            
                            //</editor-fold>
                            noeudQueue.remove(v);
                            v.minDistance = distanceThroughU ;
                            v.previous = u;
                            noeudQueue.add(v);
                        }
                    }
                }
            }
            //<editor-fold defaultstate="collapsed" desc="Annimations graphiques">
            
            st.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    for(Noeudfx n: circles){
                        FillTransition ft1 = new FillTransition(Duration.millis(500),n);
                        ft1.setToValue(Color.BLACK);
                        ft1.play();
                    }
                    FillTransition ft1 = new FillTransition(Duration.millis(500),source.circle);
                    ft1.setToValue(Color.RED);
                    ft1.play();
                }
            }
            );
            st.onFinishedProperty();
            st.play();
            //</editor-fold>
        }
        
        public  List<Noeud> getShortestPathTo(Noeud target){
            List<Noeud> path = new ArrayList<Noeud>();
            for (Noeud noeud = target; noeud != null; noeud = noeud.previous)
                path.add(noeud);
            
            Collections.reverse(path);
            return path;
        }
        
        //<editor-fold defaultstate="collapsed" desc="Exemple sans interface graphique">
        public void runDijik(){
            
            // marker tous noeuds 
            Noeud A = new Noeud("A");
            Noeud B = new Noeud("B");
            Noeud D = new Noeud("D");
            Noeud F = new Noeud("F");
            Noeud K = new Noeud("K");
            Noeud J = new Noeud("J");
            Noeud M = new Noeud("M");
            Noeud O = new Noeud("O");
            Noeud P = new Noeud("P");
            Noeud R = new Noeud("R");
            Noeud Z = new Noeud("Z");
            
            // mettre les arrets et les poids
            A.adjacents.add(new Arret(M, 8));
            A.adjacents.add(new Arret(B,5) );
            B.adjacents.add( new Arret(D, 11) );
            D.adjacents.add( new Arret(B, 11) );
            F.adjacents.add( new Arret(K, 23) );
            K.adjacents.add( new Arret(O, 40) );
            J.adjacents.add( new Arret(K, 25) );
            M.adjacents.add( new Arret(R, 8) );
            O.adjacents.add( new Arret(K, 40) );
            P.adjacents.add( new Arret(Z, 18) );
            R.adjacents.add( new Arret(P, 15) );
            Z.adjacents.add( new Arret(P, 18) );
            
            
            computePaths(A); // démarrer Dijikstra a partir du noeud A
            System.out.println("Distance to " + Z + ": " + Z.minDistance);
            List<Noeud> path = getShortestPathTo(Z);
            
            System.out.println("Path: " + path);
        }
//</editor-fold>
        
    }
    
    class Noeudfx extends Circle{
        Noeud noeud;
        Point point;
        Label distance=new Label("distance : infinit");
        boolean isSelected = false;
        public Noeudfx(double a, double b, double c, String nom){
            super(a, b, c);
            noeud = new Noeud(nom, this);
            point = new Point((int)a, (int)b);
            Label id=new Label(nom);
            noeuds.getChildren().add(id);
            id.setLayoutX(a-20);
            id.setLayoutY(b-20);
            id.setBlendMode(BlendMode.DIFFERENCE);
            circles.add(this);
        }
    }
//</editor-fold>
}
