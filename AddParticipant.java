/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testowa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Andrzej Pawlik 
 */
public class AddParticipant extends Stage {
    
    DB db;
    Statement st;
    
    private Scene SceneParticipant;
    private BorderPane borderPane;
    private GridPane gridAdd;
    private HBox hbGridAddTitle, hbuttonMenu;
    private VBox Main;
    private RestrictiveTextField NamePar, LastNamePar;
    private Button addParButton, vievButton, editButton, addButton, closeButton;
    private Text gridAddTitle; 
    private Label Year, UserNumber, lNamePar, lLastNamePar;
    int userID; 
    
    public AddParticipant() {
        
       prepareScene();
       prepareGridPaneAdd();
       
       borderPane = new BorderPane();
       borderPane.setTop(hbuttonMenu);
       borderPane.setCenter(Main);
        
       SceneParticipant = new Scene(borderPane, 400, 300);
       
       SceneParticipant.getStylesheets().add(Testowa.class.getResource("AddParticipant.css").toExternalForm());
       setScene(SceneParticipant);
       setTitle("Add Participant");    
    }
    
    private void prepareGridPaneAdd(){
        
        gridAdd = new GridPane();
        gridAdd.setId("gridAdd");
        
        gridAddTitle = new Text("Dodaj Uczestnika");
        gridAddTitle.setId("gridAddTitle");
        hbGridAddTitle = new HBox();
        hbGridAddTitle.setId("hbGridAddTitle");
        hbGridAddTitle.getChildren().add(gridAddTitle);
        gridAdd.add(hbGridAddTitle, 0, 0, 2, 1); 
        
        Year = new Label();
        Year.setId("YearUserNumber");
        gridAdd.add(Year, 0, 1);
        UserNumber = new Label();
        UserNumber.setId("YearUserNumber");
        gridAdd.add(UserNumber, 1, 1);
           
        lNamePar = new Label("Imię: ");
        lNamePar.setId("lNamePar");
        gridAdd.add(lNamePar, 0, 2);
        
        NamePar = new RestrictiveTextField();
        NamePar.setMaxLength(15);
        NamePar.setRestrict("[a-zA-ZąćęłńóśźżĄĘŁŃÓŚŹŻ]");
        gridAdd.add(NamePar, 1, 2);
       
        lLastNamePar = new Label("Nazwisko: ");
        lLastNamePar.setId("lNamePar");
        gridAdd.add(lLastNamePar, 0, 3);
        
        LastNamePar = new RestrictiveTextField();
        LastNamePar.setMaxLength(20);
        LastNamePar.setRestrict("[a-zA-ZąćęłńóśźżĄĘŁŃÓŚŹŻ]");
        gridAdd.add(LastNamePar, 1, 3);
        
        addParButton = new Button("Potwierdź");
        addParButton.setMinWidth(100);
        addParButton.setId("addParButton");
        gridAdd.add(addParButton, 0, 4, 2, 1);
        
        addParButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            addParcitipants();
            }
        });
    }
    
    private void prepareScene(){
       
        vievButton = new Button("Opis");
        vievButton.setMinWidth(100);
        vievButton.setId("windows7-default");
        
        addButton = new Button("Dodaj");
        addButton.setMinWidth(100);
        addButton.setId("windows7-default");
        
        editButton = new Button("Zmień");
        editButton.setMinWidth(100);
        editButton.setId("windows7-default");
        
        closeButton = new Button("Zamknij");
        closeButton.setMinWidth(100);
        closeButton.setId("windows7-default");
       
        hbuttonMenu = new HBox();
        hbuttonMenu.setId("hbuttonMenu");
        hbuttonMenu.getChildren().addAll(vievButton, addButton, editButton, closeButton);
        
        vievButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {    
            borderPane.setCenter(Main);
            }
        });
        
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) { 
            borderPane.setCenter(gridAdd);
            }
        });
       
        editButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {    
            borderPane.setCenter(gridAdd);
            }
        });
        
        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            close();
            }
        });
       
       Main = new VBox();
       Main.getChildren().addAll();         
    }
    
    public void refresh() {
        
        userID = getUserId();
        
        int yearUserID = userID/10000;
        int numberUserID=(userID/10)-(yearUserID*1000); 
        
        String year = "Rok: ";
        String number = "Numer: ";
        
        Year.setText(year + Integer.toString(yearUserID));
        UserNumber.setText(number + Integer.toString(numberUserID));        
    }
    
    private int getUserId() {
        
        int result = 0;
        
        try {
            
            st = db.con.createStatement();
            
            String query = "SELECT max(id_part) FROM participants";
            
            ResultSet rs = st.executeQuery(query);
            
            rs.next();
       
            result=rs.getInt(1);
              
        }  
        
        catch (SQLException ex) {
            
        } 
        
        GeneratorUserId userId = new GeneratorUserId(result);
        return userId.getUserId();
        
    }

    private void addParcitipants() {
        
        int id_part = userID;
        String first_name =  NamePar.getText();
        String last_name  =  LastNamePar.getText();
        
        try {
            
            st = db.con.createStatement();
            
            String addRow = "Insert Into participants (id_part, first_name, last_name, active)"
                          + " Values (" + id_part + ",'" + first_name + "','" 
                          + last_name + "',0)";
            
           st.executeUpdate(addRow);
            
        } catch (SQLException ex) {
          
        }
    }
}