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
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author Andrzej Pawlik 
 */
public class AddParticipant extends Stage {
    
    DB db;
    Statement st;
    
    private Scene SceneParticipant;
    private BorderPane borderPane;
    private GridPane gridAdd, gridUpdate;
    private HBox hbGridAddTitle, hbuttonMenu, hbGridAddParButton, hbGridUpdateComBoxPar;
    private VBox Main;
    private RestrictiveTextField NamePar, LastNamePar;
    private Button addParButton, vievButton, editButton, addButton, closeButton;
    private ComboBox participant;
    private CheckBox active; 
    private Text gridAddTitle; 
    private Label Year, UserNumber, lNamePar, lLastNamePar, lParticipant;
    private EventHandler key;
    private ObservableList<ParticipantData> data;
    int userID; 
    
    public AddParticipant() {
        
       prepareScene();
       createItem();
       
       borderPane = new BorderPane();
       borderPane.setTop(hbuttonMenu);
       borderPane.setCenter(Main);
        
       SceneParticipant = new Scene(borderPane, 400, 400);
       
       SceneParticipant.getStylesheets().add(Testowa.class.getResource("AddParticipant.css").toExternalForm());
       setScene(SceneParticipant);
       setTitle("Add Participant");    
    }
    
    private void createItem() {
        
        gridAddTitle = new Text();
        gridAddTitle.setId("gridAddTitle");
        hbGridAddTitle = new HBox();
        hbGridAddTitle.setId("hbGridAddTitle");
        hbGridAddTitle.getChildren().add(gridAddTitle);
         
        Year = new Label();
        Year.setId("YearUserNumber");
        UserNumber = new Label();
        UserNumber.setId("YearUserNumber");
        
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
        
        active = new CheckBox();
        active.setId("lNamePar");
    }
    
    /**
     *  Funkcja prepareGridPaneAdd przygotowuje okno, które 
     *  pozwala dodać osobę do bazy danych, ma w sobie
     *  Listener, który blokuje dodanie samego nazwiska lub imienia,
     *  można dodać osobę za pomocą przycisku addParButton lub poprzez 
     *  naciśnięcie ENTER.
     * 
     *  Głównym zarządcą jest GridPane, w którym są umieszczone wszystkie 
     *  elementy. 
     *  GridPane jest dzieckiem BorderPane umieszczonym 
     *  w Centrum BorderPane.
     */
    private void prepareGridPaneAdd(){
        
        gridAdd = new GridPane();
        gridAdd.setId("gridAdd");
        
        gridAddTitle.setText("Dodaj Uczestnika");
        gridAdd.add(hbGridAddTitle, 0, 0, 2, 1); 
        
        gridAdd.add(Year, 0, 1);
        gridAdd.add(UserNumber, 1, 1);
           
        gridAdd.add(lNamePar, 0, 2);
        
        gridAdd.add(NamePar, 1, 2);
        
        NamePar.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
               
                String first_name =  LastNamePar.getText();
               
                if(t1.equals("") || first_name.equals("")) 
                    addParButton.setDisable(true);
                else {
                    addParButton.setDisable(false);
                    LastNamePar.setOnKeyPressed(key);
                    NamePar.setOnKeyPressed(key);
                }                    
            }
        }); 
       
        gridAdd.add(lLastNamePar, 0, 3);
        gridAdd.add(LastNamePar, 1, 3);
        
        LastNamePar.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
               
                String first_name =  NamePar.getText();
               
                if(t1.equals("") || first_name.equals("")) 
                    addParButton.setDisable(true);
                else {
                    addParButton.setDisable(false);
                    LastNamePar.setOnKeyPressed(key);
                    NamePar.setOnKeyPressed(key);
                }     
            }
        }); 
        
        active.setText("Aktywny");
        active.setSelected(true);
        gridAdd.add(active, 1, 4);
        
        addParButton = new Button("Potwierdź");
        addParButton.setMinWidth(100);
        addParButton.setId("addParButton");
        addParButton.setDisable(true);
        
        hbGridAddParButton = new HBox();
        hbGridAddParButton.setId("hbGridAddTitle");
        hbGridAddParButton.getChildren().add(addParButton);
        gridAdd.add(hbGridAddParButton, 0, 5, 2, 1);
                
        addParButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addParcitipants();
                getUserId();
                userID = getUserId();
                refresh(userID);
                NamePar.clear();
                LastNamePar.clear();
            }
        });
        
        key = new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.ENTER) {
                    addParcitipants();
                    getUserId();
                    userID = getUserId();
                    refresh(userID);
                    NamePar.clear();
                    LastNamePar.clear();
                }
            }
        };        
    }
    
    private void prepareGridPaneUpdate() {
        
        refreshCombo();
        
        gridUpdate = new GridPane();
        gridUpdate.setId("gridAdd");
        
        gridAddTitle.setText("Zmień Dane Uczestnika");
        gridUpdate.add(hbGridAddTitle, 0, 0, 2, 1);
        
        participant = new ComboBox(data);
        participant.setMinWidth(200);
        
        participant.setCellFactory(new Callback<ListView<ParticipantData>,ListCell<ParticipantData>>(){
 
            @Override
            public ListCell<ParticipantData> call(ListView<ParticipantData> p) {
                 
                final ListCell<ParticipantData> cell = new ListCell<ParticipantData>(){
 
                    @Override
                    protected void updateItem(ParticipantData t, boolean bln) {
                        super.updateItem(t, bln);
                         
                        if(t != null){
                            setText(t.first_name + " " + t.last_name);
                        }else{
                            setText(null);
                        }
                    }
  
                };
                 
                return cell;
            }
        });
        
        participant.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ParticipantData>() {           
            @Override
            public void changed(ObservableValue<? extends ParticipantData> ov, ParticipantData t, ParticipantData t1) {
                comboRefresh();
            }
        }); 
        
        
        lParticipant = new Label("Wybierz: ");
        hbGridUpdateComBoxPar = new HBox();
        hbGridUpdateComBoxPar.getChildren().addAll(lParticipant, participant);
        
        gridUpdate.add(hbGridUpdateComBoxPar, 0, 1, 2, 1);
       
        gridUpdate.add(Year, 0, 2);
        gridUpdate.add(UserNumber, 1, 2);
           
        gridUpdate.add(lNamePar, 0, 3);        
        gridUpdate.add(NamePar, 1, 3);
        
        StackPane stack = new StackPane();
        stack.getChildren().add(lLastNamePar);
        stack.setMinWidth(130);
        gridUpdate.add(stack, 0, 4);
        gridUpdate.add(LastNamePar, 1, 4);
        
        gridUpdate.add(active, 1, 5);
        
        active.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue ov,Boolean old_val, Boolean new_val) {
                        
                    if(active.isSelected()){
                        active.setText("Aktywny");
                    }
                    else {
                        active.setText("Nieaktywny");
                    }        
                }
            });        
    }
    
    private void comboRefresh() {
        
        int ItemId;
        int activeStatus;

        ItemId = participant.getSelectionModel().getSelectedIndex();
        
        int itemID = data.get(ItemId).id_part;
        
        refresh(itemID);
        
        NamePar.setText(data.get(ItemId).first_name);
        LastNamePar.setText(data.get(ItemId).last_name);
        
        activeStatus = data.get(ItemId).active;
        
        if(activeStatus==0){
            active.setText("Aktywny");
            active.setSelected(true);    
        }
        else {
            active.setText("Nieaktywny");
            active.setSelected(false);     
        }   
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
            userID = getUserId();
            refresh(userID);
            NamePar.clear();
            LastNamePar.clear();
            prepareGridPaneAdd();    
            borderPane.setCenter(gridAdd);
            }
        });
       
        editButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) { 
            int first = 1;
            refresh(first);
            prepareGridPaneUpdate();    
            borderPane.setCenter(gridUpdate);
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
    
    public void refresh(int userId) {
         
        int yearUserID = userId/10000;
        int numberUserID=(userId/10)-(yearUserID*1000); 
        
        String year = "Rok: ";
        String number = "Numer: ";
        
        Year.setText(year + Integer.toString(yearUserID));
        UserNumber.setText(number + Integer.toString(numberUserID));        
    }
    
    public int getUserId() {
        
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
        
        int statusActive;
        
        if(active.isSelected()) 
            statusActive = 0;
        else 
            statusActive = 1;
        
        int id_part = userID;
        
        String first_name =  NamePar.getText();
        String last_name  =  LastNamePar.getText();
        
        try {
            
            st = db.con.createStatement();
            
            String addRow = "Insert Into participants (id_part, first_name, last_name, active)"
                          + " Values (" + id_part + ",'" + first_name + "','" 
                          + last_name + "'," + statusActive + ")";
            
           st.executeUpdate(addRow);
            
        } catch (SQLException ex) {
          
        }
    }
    
    public void refreshCombo() {                                              
  
        data = FXCollections.observableArrayList();
            
        try {
        
            st = db.con.createStatement();
            
            String query = "SELECT id_part, first_name, last_name, active FROM participants";
      
            ResultSet rs = st.executeQuery(query);
        
            while (rs.next()){
                ParticipantData unit = new ParticipantData(
                        rs.getInt("id_part"), 
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getInt("active")
                );
                data.add(unit); 
            }
                     
        } catch (SQLException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }    
    } 
    
}