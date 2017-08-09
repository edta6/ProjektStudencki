/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testowa;

import java.sql.SQLException;
import java.sql.Statement;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author pawlia15
 */
public class AddUser extends Stage {
    
    DB db;
    Statement st;
    
    private final Scene SceneParticipant;
    private final BorderPane borderPane;
    private GridPane gridAdd, gridUpdate;
    private StackPane stack;
    private HBox hbGridAddTitle, hbuttonMenu, hbGridAddParButton, hbGridUpdateComBoxPar;
    private HBox hbGridEditParButton;
    private VBox Main;
    private RestrictiveTextField NamePar, LastNamePar, NickUser;
    private PasswordField PassUserR, PassUser;
    private Button addParButton, editParButton, vievButton, editButton, addButton, closeButton;
    private Button pdfButton;
    private ComboBox participant;
    private CheckBox simplerole, adminrole; 
    private Text gridAddTitle; 
    private Label lNamePar, lLastNamePar, lNick, lPassUser, lPassUserR;
    int role;
    
    public AddUser(){
       
       prepareScene();
       createItem();
       
       borderPane = new BorderPane();
       borderPane.setTop(hbuttonMenu);
   
       SceneParticipant = new Scene(borderPane, 400, 400);
       
       SceneParticipant.getStylesheets().add(Testowa.class.getResource("AddParticipant.css").toExternalForm());
       setScene(SceneParticipant);
       setTitle("Add User");      
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
            
            }
        });
        
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                prepareGridPaneAdd();
                borderPane.setCenter(gridAdd);
            }
        });
       
        editButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {   

            }
        });
        
        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            close();
            
            }
        });                       
    }
    
    private void createItem() {
        
        gridAddTitle = new Text();
        gridAddTitle.setId("gridAddTitle");
        hbGridAddTitle = new HBox();
        hbGridAddTitle.setId("hbGridAddTitle");
        hbGridAddTitle.getChildren().add(gridAddTitle);
                 
        lNamePar = new Label("Imię: ");
        lNamePar.setId("lNamePar");
        
        NamePar = new RestrictiveTextField();
        NamePar.setMaxLength(15);
        NamePar.setRestrict("[a-zA-ZąćęłńóśźżĄĘŁŃÓŚŹŻ]");
        
        lLastNamePar = new Label("Nazwisko: ");
        lLastNamePar.setId("lNamePar");
        
        LastNamePar = new RestrictiveTextField();
        LastNamePar.setMaxLength(20);
        LastNamePar.setRestrict("[a-zA-ZąćęłńóśźżĄĘŁŃÓŚŹŻ]");
        
        lNick = new Label("Nick: ");
        lNick.setId("lNamePar");
        NickUser = new RestrictiveTextField();
        NickUser.setMaxLength(20);
        
        lPassUser = new Label("Hasło: ");
        lPassUser.setId("lNamePar");
        PassUser = new PasswordField();
        
        lPassUserR = new Label("Powtórz hasło: ");
        lPassUserR.setId("lNamePar");
        PassUserR = new PasswordField();
        
        simplerole = new CheckBox();
        simplerole.setId("lNamePar");
        simplerole.setText("Zwykły");
        
        adminrole = new CheckBox();
        adminrole.setId("lNamePar");
        adminrole.setText("Admin");
        
        simplerole.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue ov,Boolean old_val, Boolean new_val) {
                        
                if(simplerole.isSelected()){
                    adminrole.setSelected(false);
                    role = 0;
                }   
            }
        });
        
        adminrole.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue ov,Boolean old_val, Boolean new_val) {
                        
                if(adminrole.isSelected()){
                    simplerole.setSelected(false);
                    role = 1;
                }   
            }
        });
        
    }
     
    private void prepareGridPaneAdd(){
        
        gridAdd = new GridPane();
        gridAdd.setId("gridAdd");
        
        gridAddTitle.setText("Dodaj Użytkownika");
        gridAdd.add(hbGridAddTitle, 0, 0, 2, 1); 
           
        gridAdd.add(lNamePar, 0, 1);       
        gridAdd.add(NamePar, 1, 1);
         
        gridAdd.add(lLastNamePar, 0, 2);
        gridAdd.add(LastNamePar, 1, 2);
        
        gridAdd.add(lNick, 0, 3);
        gridAdd.add(NickUser, 1, 3);
        
        gridAdd.add(lPassUser, 0, 4);
        gridAdd.add(PassUser, 1, 4);
        
        gridAdd.add(lPassUserR, 0, 5);
        gridAdd.add(PassUserR, 1, 5);
        
        gridAdd.add(simplerole, 0, 6);
        gridAdd.add(adminrole, 1, 6);
        
        addParButton = new Button("Potwierdź");
        addParButton.setMinWidth(100);
        addParButton.setId("addParButton");
        
        hbGridAddParButton = new HBox();
        hbGridAddParButton.setId("hbGridAddTitle");
        hbGridAddParButton.getChildren().add(addParButton);
        gridAdd.add(hbGridAddParButton, 0, 7, 2, 1);
                
        addParButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                bodyaddUserButtonAndKey();
            }
        });
//        
//        key = new EventHandler<KeyEvent>(){
//            @Override
//            public void handle(KeyEvent ke) {
//                if (ke.getCode() == KeyCode.ENTER) {
//                    bodyaddUserButtonAndKey();
//                }
//            }
//        };        
    }

    private void bodyaddUserButtonAndKey() {
        
     String addUser = "INSERT INTO user_ohp (first_name, last_name, nick, role)"
                    + " Values ('" + NamePar.getText() + "','" + LastNamePar.getText() + "','"
                    +  NickUser.getText() + "'," + role + ")";
     
     String createUser = "CREATE USER '" + NickUser.getText() + "'@'localhost'"
                       + "IDENTIFIED BY '" + PassUser.getText() + "';";
   
    String addUserGran = "GRANT ALL ON ohp.* TO '" + NickUser.getText() + "'@'localhost'";
     
    sqlQuery(addUser);
    sqlQuery(createUser);
    sqlQuery(addUserGran);
    
    }
    
    
    
    private void sqlQuery(String query) {
        
        try {
            st = db.con.createStatement();
            st.executeUpdate(query);    
        } catch (SQLException ex) {
          
        }
    }
    
}
