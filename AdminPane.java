/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OHP;

import java.sql.Statement;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author pawlia15
 */
public class AdminPane extends Stage {
    
    DB db;
    Statement st;
    MainWindow window;
    
    private final Scene SceneParticipant;
    private VBox vbpane;
    private HBox hbGridAddTitle;
    private Button buttonAddParticipant, buttonAddTarget, buttonAddUser, buttonClose;
    private Text AddTitle;
    private AddUser user;
    
    public AdminPane() {
       
       prepareScene(); 
       
       SceneParticipant = new Scene(vbpane, 400, 210);
       
       SceneParticipant.getStylesheets().add(Testowa.class.getResource("AddParticipant.css").toExternalForm());
       setScene(SceneParticipant);
       setTitle("Panel Administracyjny");    
           
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
                participant.window = window;
                participant.addParticipantData();
                participant.resizableProperty().setValue(Boolean.FALSE);
                participant.show();
                buttonAdminPane(participant);
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
                target.window = window;
                target.addTargetData();
                target.resizableProperty().setValue(Boolean.FALSE);
                target.show();
                buttonAdminPane(target);
                close();
            }
        });
        
        buttonAddUser = new Button("Dodaj/Zmień dane wychowawcy");
        buttonAddUser.setId("windows7-default");
        buttonAddUser.setMinWidth(320);
        buttonAddUser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                user = new AddUser();
                user.db = db;
                user.user = user;
                user.window = window;
                user.addUserData();
                user.resizableProperty().setValue(Boolean.FALSE);                
                user.show();
                buttonAdminPane(user);
                close();
            }
        });
        
        buttonClose = new Button("Zamknij");
        buttonClose.setId("windows7-default");
        buttonClose.setMinWidth(320);
        buttonClose.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                close();
                window.buttonAdmin.setDisable(false);
            }
        });
        
        vbpane.getChildren().addAll(hbGridAddTitle, buttonAddParticipant, buttonAddTarget, buttonAddUser, buttonClose);   
     }
     
    public void buttonAdminPane(Stage o){
        o.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(final WindowEvent event) {
                window.buttonAdmin.setDisable(false);
            }
        });    
    } 
}
