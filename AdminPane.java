/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testowa;

import java.sql.Statement;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author pawlia15
 */
public class AdminPane extends Stage {
    
    DB db;
    Statement st;
    
    private final Scene SceneParticipant;
    private VBox vbpane;
    private HBox hbGridAddTitle;
    private Button buttonAddParticipant, buttonAddTarget, buttonAddUser, buttonClose;
    private Text AddTitle;
    
    public AdminPane() {
       
       prepareScene(); 
       
       SceneParticipant = new Scene(vbpane, 400, 400);
       
       SceneParticipant.getStylesheets().add(Testowa.class.getResource("AddParticipant.css").toExternalForm());
       setScene(SceneParticipant);
       setTitle("Add Participant");    
           
    }
    
     private void prepareScene(){
        
        vbpane = new VBox();
        vbpane.setId("vbpane");
        
        AddTitle = new Text("Panel Administratora");
        AddTitle.setId("gridAddTitle");
        hbGridAddTitle = new HBox();
        hbGridAddTitle.setId("hbGridAddTitle");
        hbGridAddTitle.getChildren().add(AddTitle);
        
        buttonAddParticipant = new Button("Dodaj/Zmień dane uczestników OHP");
        buttonAddParticipant.setId("windows7-default");
        buttonAddParticipant.setMinWidth(320);
        buttonAddParticipant.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AddParticipant participant = new AddParticipant();
                participant.db = db;
                participant.addParticipantData();
                participant.show();
                close();
            }
        });
        
        buttonAddTarget = new Button("Dodaj nowy cel wyjścia");
        buttonAddTarget.setId("windows7-default");
        buttonAddTarget.setMinWidth(320);
        buttonAddTarget.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AddTarget target = new AddTarget(); 
                target.db = db;
                target.addTargetData();
                target.show();
                close();
            }
        });
        
        buttonAddUser = new Button("Dodaj/Zmień dane wychowawcy");
        buttonAddUser.setId("windows7-default");
        buttonAddUser.setMinWidth(320);
        buttonAddUser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
        
        buttonClose = new Button("Zamknij");
        buttonClose.setId("windows7-default");
        buttonClose.setMinWidth(320);
        buttonClose.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                close();
            }
        });
        
        vbpane.getChildren().addAll(hbGridAddTitle, buttonAddParticipant, buttonAddTarget, buttonAddUser, buttonClose);   
     }    
}
