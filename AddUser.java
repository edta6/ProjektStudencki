/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testowa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
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
    private HBox hbGridEditParButton, hbGridUpdatePasButton, hbGridUpdateDelParButton;
    private VBox Main;
    private RestrictiveTextField NamePar, LastNamePar, NickUser;
    private PasswordField PassUserR, PassUser;
    private Button addParButton, editParButton, vievButton, editButton, addButton, closeButton, changePasButton;
    private Button delParButton;
    private ComboBox userCBdata;
    private CheckBox simplerole, adminrole; 
    private Text gridAddTitle; 
    private Label lNamePar, lLastNamePar, lNick, lPassUser, lPassUserR, lInfo, lChoice, lViev;
    private EventHandler key;
    private ObservableList<UserData> data;
    int role ;
    int ItemId;
    
    public AddUser(){
       
       prepareScene();
       createItem();
       prepareViev();
       
       borderPane = new BorderPane();
       borderPane.setTop(hbuttonMenu);
       borderPane.setCenter(Main);
       
       SceneParticipant = new Scene(borderPane, 390, 470);
       
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
                borderPane.setCenter(Main);
            }
        });
        
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                NamePar.clear();
                LastNamePar.clear();
                NickUser.clear();
                PassUser.clear();
                PassUserR.clear();
                simplerole.setSelected(false);
                adminrole.setSelected(false);
                role = -1;
                lNick.setText("Nick: ");
                prepareGridPaneAdd();
                borderPane.setCenter(gridAdd);
            }
        });
       
        editButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                NamePar.clear();
                LastNamePar.clear();
                simplerole.setSelected(false);
                adminrole.setSelected(false);
                prepareGridUpdate();
                borderPane.setCenter(gridUpdate);
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
        
        lInfo = new Label();
        lInfo.setId("lNamePar");
        
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
        
        NamePar.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
               
                NamePar.setOnKeyPressed(key);
                if( !NamePar.getText().equals("")) NamePar.setId("");
                else NamePar.setId("error");
                              
             }
        }); 
        
        gridAdd.add(lLastNamePar, 0, 2);
        gridAdd.add(LastNamePar, 1, 2);
      
        LastNamePar.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
               
                LastNamePar.setOnKeyPressed(key);
                if( !LastNamePar.getText().equals("")) LastNamePar.setId("");
                else LastNamePar.setId("error");
                
             }
        }); 
        
        gridAdd.add(lNick, 0, 3);
        gridAdd.add(NickUser, 1, 3);
        NickUser.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
               
                NickUser.setOnKeyPressed(key);
                if( !NickUser.getText().equals("")) NickUser.setId("");
                else NickUser.setId("error");
                
             }
        });
        
        gridAdd.add(simplerole, 0, 4);
        gridAdd.add(adminrole, 1, 4);
        
        gridAdd.add(lPassUser, 0, 5);
        gridAdd.add(PassUser, 1, 5);
        PassUser.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
               
                PassUser.setOnKeyPressed(key);
                if( !PassUser.getText().equals("")) PassUser.setId("");
                else PassUser.setId("error");
                
             }
        });
                
        gridAdd.add(lPassUserR, 0, 6);
        gridAdd.add(PassUserR, 1, 6);
        PassUserR.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
               
                PassUserR.setOnKeyPressed(key);
                if( !PassUserR.getText().equals("")) PassUserR.setId("");
                else PassUserR.setId("error");
                              
             }
        });
              
        addParButton = new Button("Potwierdź");
        addParButton.setMinWidth(100);
        addParButton.setId("addParButton");
        addParButton.setOnKeyPressed(key);
        
        hbGridAddParButton = new HBox();
        hbGridAddParButton.setId("hbGridAddTitle");
        hbGridAddParButton.getChildren().add(addParButton);
        gridAdd.add(hbGridAddParButton, 0, 7, 2, 1);
        
        gridAdd.add(lInfo, 0, 8, 2, 1);
                 
        addParButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                bodyaddUserButtonAndKey();
            }
        });
        
        key = new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.ENTER) {
                    bodyaddUserButtonAndKey();
                }
            }
        };        
    }

    private void bodyaddUserButtonAndKey() {
        
        if( NamePar.getText().equals("") ||
            LastNamePar.getText().equals("") ||
            NickUser.getText().equals("") ||
            PassUser.getText().equals("") ||
            PassUserR.getText().equals("") ||
            (!simplerole.isSelected() &&
            !adminrole.isSelected())) {
            
                if( NamePar.getText().equals("")) NamePar.setId("error");
                if( LastNamePar.getText().equals("")) LastNamePar.setId("error");
                if( NickUser.getText().equals("")) NickUser.setId("error");
                if( PassUser.getText().equals("")) PassUser.setId("error");
                if( PassUserR.getText().equals("")) PassUserR.setId("error");
            
                lInfo.setText("Wypełnij wszystkie pola!");
        }
        else {
            if(PassUser.getText().equals(PassUserR.getText())){
            
            String addUserGran;   
        
            String addUser = "INSERT INTO user_ohp (first_name, last_name, nick, role)"
                    + " Values ('" + NamePar.getText() + "','" + LastNamePar.getText() + "','"
                    +  NickUser.getText() + "'," + role + ")";
     
            String createUser = "CREATE USER '" + NickUser.getText() + "'@'localhost'"
                       + "IDENTIFIED BY '" + PassUser.getText() + "';";
   
            if(role==1){
                addUserGran = "GRANT ALL PRIVILEGES ON *.* TO '" + NickUser.getText() + "'@'localhost' WITH GRANT OPTION";   
            }
            else addUserGran = "GRANT ALL ON OHP.* TO '" + NickUser.getText() + "'@'localhost'";  
     
            sqlQuery(addUser);
            sqlQuery(createUser);   
            sqlQuery(addUserGran);
        
            NamePar.clear();
            LastNamePar.clear();
            NickUser.clear();
            PassUser.clear();
            PassUserR.clear();
            simplerole.setSelected(false);
            adminrole.setSelected(false);
            role = -1; 
            lInfo.setText("Poprawnie dodano użytkownika!");
            }
            else lInfo.setText("Hasła nie pasują!");
        } 
        
        addUserData();
    }
    
    private void prepareGridUpdate() {
        
        gridUpdate = new GridPane();
        gridUpdate.setId("gridAdd");
        
        gridAddTitle.setText("Zmień Dane Użytkownika");
        gridUpdate.add(hbGridAddTitle, 0, 0, 2, 1);
        
        userCBdata = new ComboBox(data);
        userCBdata.setMinWidth(200);
        
        userCBdata.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<UserData>() {           
            @Override
            public void changed(ObservableValue<? extends UserData> ov, UserData t, UserData t1) {
                comboRefresh(); 
            }
        }); 
        
        lChoice = new Label("Wybierz: ");
        lChoice.setId("lNamePar");
        hbGridUpdateComBoxPar = new HBox();
        hbGridUpdateComBoxPar.setId("hbGridAddTitle");
        hbGridUpdateComBoxPar.getChildren().addAll(lChoice, userCBdata);
        gridUpdate.add(hbGridUpdateComBoxPar, 0, 1, 2, 1);
        
        stack = new StackPane();
        stack.getChildren().add(lNamePar);
        stack.setMinWidth(120);
        stack.setAlignment(Pos.TOP_LEFT);
        gridUpdate.add(stack, 0, 2);       
        gridUpdate.add(NamePar, 1, 2);
        
        gridUpdate.add(lLastNamePar, 0, 3);
        gridUpdate.add(LastNamePar, 1, 3);
        
        lNick.setText("Nick:       ");
        gridUpdate.add(lNick, 0, 4, 2,1);
        
        gridUpdate.add(simplerole, 0, 5);
        gridUpdate.add(adminrole, 1, 5);
        
        editParButton = new Button("Zmień dane Użytkownika");
        editParButton.setId("addParButton");
        editParButton.setMinWidth(240);
        editParButton.setDisable(true);
        hbGridEditParButton = new HBox();
        hbGridEditParButton.setId("hbGridAddTitle");
        hbGridEditParButton.getChildren().add(editParButton);
        gridUpdate.add(hbGridEditParButton, 0, 6, 2, 1);
        
        editParButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            
                String addUserGran;   
                
                String changeUser = "UPDATE user_ohp SET"
                          +  " first_name = '" + NamePar.getText() + "'," 
                          +  " last_name = '" + LastNamePar.getText() + "',"
                          +  " role = " + role  
                          +  " WHERE id_part = " + data.get(ItemId).id_part;
                
                String remove = "REVOKE ALL PRIVILEGES, GRANT OPTION FROM '" + data.get(ItemId).nick + "'@'localhost'";
    
                if(role==1){
                    addUserGran = "GRANT ALL PRIVILEGES ON *.* TO '" + data.get(ItemId).nick + "'@'localhost' WITH GRANT OPTION";   
                }
                else addUserGran = "GRANT ALL ON OHP.* TO '" + data.get(ItemId).nick + "'@'localhost'";  
                
                sqlQuery(changeUser);
                sqlQuery(remove);
                sqlQuery(addUserGran);
                
                NamePar.clear();
                LastNamePar.clear();
                lNick.setText("Nick:       ");
                role = -1;
                
                userCBdata.getSelectionModel().clearSelection();
                
                simplerole.setSelected(false);
                adminrole.setSelected(false);
                   
            }
        });
        
        changePasButton = new Button("Zmień hasło Użytkownika");
        changePasButton.setId("addParButton");
        changePasButton.setMinWidth(240);
        changePasButton.setDisable(true);
        hbGridUpdatePasButton = new HBox();
        hbGridUpdatePasButton.setId("hbGridAddTitle");
        hbGridUpdatePasButton.getChildren().add(changePasButton);
        gridUpdate.add(hbGridUpdatePasButton, 0, 7, 2, 1);
        
        changePasButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            
                final Stage changePas = new Stage();
                GridPane  changePasUserPane = new GridPane();
                changePasUserPane.setId("gridAdd");
                gridAddTitle.setText("Zmień Hasło Użytkownika");
                changePasUserPane.add(hbGridAddTitle, 0, 0, 2, 1);
                
                StackPane rozmiar = new StackPane();
                rozmiar.getChildren().add(lPassUser);
                rozmiar.setMinWidth(120);
                rozmiar.setAlignment(Pos.TOP_LEFT);
                
                changePasUserPane.add(rozmiar, 0, 1);
                changePasUserPane.add(PassUser, 1, 1);
                changePasUserPane.add(lPassUserR, 0, 2);
                changePasUserPane.add(PassUserR, 1, 2);
                
                Button changePassword = new Button("Potwierdź");
                changePassword.setMinWidth(100);
                changePassword.setId("addParButton");
        
                HBox hbchangePasUserPane = new HBox();
                hbchangePasUserPane.setId("hbGridAddTitle");
                hbchangePasUserPane.getChildren().add(changePassword);
                changePasUserPane.add(hbchangePasUserPane, 0, 3, 2, 1);
                changePasUserPane.add(lInfo, 0, 4, 2, 1);
                
                Scene changePasUser = new Scene(changePasUserPane, 400, 250);
                changePasUser.getStylesheets().add(Testowa.class.getResource("AddParticipant.css").toExternalForm());
                changePas.setScene(changePasUser);
                changePas.setTitle("Zmiana Hasła");
                changePas.show();
                
            changePassword.setOnAction(new EventHandler<ActionEvent>() {
            @Override
                public void handle(ActionEvent event) {

                    if( PassUser.getText().equals("") ||
                        PassUserR.getText().equals("")) {

                        if( PassUser.getText().equals("")) PassUser.setId("error");
                        if( PassUserR.getText().equals("")) PassUserR.setId("error");

                        lInfo.setText("Wypełnij wszystkie pola!");
                     }
                    else {
                        if(PassUser.getText().equals(PassUserR.getText())){
                            
                            String pass_new = "SET PASSWORD FOR '" + data.get(ItemId).nick + "'@'localhost' = PASSWORD('" + PassUser.getText() +"');";
                            sqlQuery(pass_new);
                            changePas.close();
                        }
                        else lInfo.setText("Hasła nie pasują!");
                    }

                 }
            });
                   
            }
        });
        
        delParButton = new Button("Usuń Użytkownika");
        delParButton.setId("addParButton");
        delParButton.setMinWidth(240);
        delParButton.setDisable(true);
        hbGridUpdateDelParButton = new HBox();
        hbGridUpdateDelParButton.setId("hbGridAddTitle");
        hbGridUpdateDelParButton.getChildren().add(delParButton);
        gridUpdate.add(hbGridUpdateDelParButton, 0, 8, 2, 1);
        
        delParButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
                String deleteRowTable = "UPDATE user_ohp set nick=Null where "   
                                      + " id_part=" + data.get(ItemId).id_part;   
                
                String deleteUser = "DROP USER '" + data.get(ItemId).nick + "'@'localhost'";
                
                sqlQuery(deleteUser);
                sqlQuery(deleteRowTable);
                
                NamePar.clear();
                LastNamePar.clear();
                lNick.setText("Nick:       ");
                role = -1;
                
                data.remove(ItemId);
                userCBdata.getSelectionModel().clearSelection();
                userCBdata.setItems(data);
                
                simplerole.setSelected(false);
                adminrole.setSelected(false);
            }
        });
    }
    
     private void prepareViev() {
       
        Main = new VBox();
        Main.setId("vbpane");

        String description = "Panel w którym dodajemy:\n- wychowawców OHP,"
                           + " \n- zmieniamy dane wychowawcy (imię, nazwisko) oraz jego status:"
                           + " \n- zwykły, ma tylko możliwość wypisywania uczestników OHP"
                           + " \n- admin ma pełne prawa.\n\n"
                           + " Jest też możliwość zmiany hasła dla danego wychowawcy.\n"
                           + " Nick jest unikalny i nie podlega zmianie.\n" 
                           + " Jest też możliwość usunięcia wychowawcy.\n" ;
         
        lViev = new Label();
        lViev.setText(description);
        lViev.setWrapText(true);
        lViev.setId("lViev");
               
        Main.getChildren().add(lViev);     
    }
    
    private void comboRefresh() {
        
        userCBdata.setItems(data);
        ItemId = userCBdata.getSelectionModel().getSelectedIndex();
        
        if(ItemId==-1){
            NamePar.clear();
            LastNamePar.clear();
            lNick.setText("Nick:       ");
            editParButton.setDisable(true);
            changePasButton.setDisable(true);
            delParButton.setDisable(true);
        }
        else {
                
            NamePar.setText(data.get(ItemId).first_name);
            LastNamePar.setText(data.get(ItemId).last_name);
            
            lNick.setText("Nick:       " + data.get(ItemId).nick);
            
            role = data.get(ItemId).role;
        
            if(role==1) adminrole.setSelected(true);   
            if(role==0) simplerole.setSelected(true);
            editParButton.setDisable(false);
            changePasButton.setDisable(false);
            delParButton.setDisable(false);
              
         }  
    } 
      
    private void sqlQuery(String query) {
        
        try {
            st = db.con.createStatement();
            st.executeUpdate(query);
            st.close();
        } 
        catch (SQLException ex) {
          
        }
    }
    
    public void addUserData() {                                              
  
        data = FXCollections.observableArrayList();
            
        try {
        
            st = db.con.createStatement();
            
            String query = "SELECT id_part, first_name, last_name, nick, role FROM user_ohp";
      
            ResultSet rs = st.executeQuery(query);
        
            while (rs.next()){
                UserData unit = new UserData(
                        rs.getInt("id_part"), 
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("nick"),
                        rs.getInt("role")
                );
                data.add(unit); 
            }
            
            st.close();
                     
        } 
        catch (SQLException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }    
}