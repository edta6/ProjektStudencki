/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OHP;

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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
 * @author spychark
 */
public class AddTarget extends Stage{

    DB db;
    Statement st;
    MainWindow window;
    
    private final Scene SceneTarget;
    private final BorderPane borderPane;
    private GridPane gridAdd, gridUpdate;
    private StackPane stack;
    private HBox hbGridAddTitle, hbuttonMenu, hbGridAddParButton, hbGridUpdateComBoxPar;
    private HBox hbGridEditParButton;
    private VBox Main;
    private RestrictiveTextField LastNamePar;
    private Button addParButton, editParButton, vievButton, editButton, addButton, closeButton;
    private ComboBox target;
    private CheckBox active; 
    private Text gridAddTitle; 
    private Label lLastNamePar, lParticipant, lViev;
    private EventHandler key, keyUpdate;
    private ObservableList<TargetData> data;
    int IdPart;
    int ItemId;
    int activeStatus;
    
    public AddTarget() {
        
       prepareScene();
       createItem();
       prepareViev();
       
       borderPane = new BorderPane();
       borderPane.setTop(hbuttonMenu);
       borderPane.setCenter(Main);
        
       SceneTarget = new Scene(borderPane, 390, 300);
       
       SceneTarget.getStylesheets().add(Testowa.class.getResource("AddParticipant.css").toExternalForm());
       setScene(SceneTarget);
       setTitle("Dodaj cel wyjścia");    
    }
  
    private void createItem() {
        
        gridAddTitle = new Text();
        gridAddTitle.setId("gridAddTitle");
        hbGridAddTitle = new HBox();
        hbGridAddTitle.setId("hbGridAddTitle");
        hbGridAddTitle.getChildren().add(gridAddTitle);
                         
        lLastNamePar = new Label("Cel wyjścia: ");
        lLastNamePar.setId("lNamePar");
        
        LastNamePar = new RestrictiveTextField();
        LastNamePar.setMaxLength(20);
        LastNamePar.setRestrict("[ a-zA-ZąćęłńóśźżĄĘŁŃÓŚŹŻ]");
        
        active = new CheckBox();
        active.setId("lNamePar");
        
        active.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue ov,Boolean old_val, Boolean new_val) {
                        
                if(active.isSelected()){
                    active.setText("Aktywny");
                    activeStatus = 0;
                }
                else {
                    active.setText("Nieaktywny");
                    activeStatus = 1;
                }        
            } 
        });        
    }
    
    private void prepareGridPaneAdd(){
        
        gridAdd = new GridPane();
        gridAdd.setId("gridAdd");
        
        gridAddTitle.setText("Dodaj Cel Wyjścia");
        gridAdd.add(hbGridAddTitle, 0, 0, 2, 1); 
               
        gridAdd.add(lLastNamePar, 0, 1);
        gridAdd.add(LastNamePar, 1, 1);
        
        LastNamePar.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
               
                        if(t1.equals("")) 
                            addParButton.setDisable(true);
                        else {
                            addParButton.setDisable(false);
                            LastNamePar.setOnKeyPressed(key);
                        }           
             }
        }); 
        
        active.setText("Aktywny");
        active.setSelected(true);
        gridAdd.add(active, 1, 2);
        
        addParButton = new Button("Potwierdź");
        addParButton.setMinWidth(100);
        addParButton.setId("addParButton");
        addParButton.setDisable(true);
        
        hbGridAddParButton = new HBox();
        hbGridAddParButton.setId("hbGridAddTitle");
        hbGridAddParButton.getChildren().add(addParButton);
        gridAdd.add(hbGridAddParButton, 0, 3, 2, 1);
                
        addParButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                bodyaddParButtonAndKey();
            }
        });
        
        key = new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.ENTER) {
                    bodyaddParButtonAndKey();
                }
            }
        };        
    }
    
    private void bodyaddParButtonAndKey() {
        
        String addTarget = "Insert Into targets (target_name, active)"
                          + " Values ('" + LastNamePar.getText() + "'," + activeStatus+ ");";
        
        sqlQuery(addTarget);
        addTargetData();
        LastNamePar.clear();                
    }
        
    private void prepareGridPaneUpdate() {
                
        gridUpdate = new GridPane();
        gridUpdate.setId("gridAdd");
        
        gridAddTitle.setText("Zmień Cel Wyjścia");
        gridUpdate.add(hbGridAddTitle, 0, 0, 2, 1);
        
        target = new ComboBox(data);
        target.setMinWidth(200);
        target.setMaxWidth(200);
        
        comboItemRefresh();
        
        target.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TargetData>() {           
            @Override
            public void changed(ObservableValue<? extends TargetData> ov, TargetData t, TargetData t1) {
                comboRefresh(); 
            }
        }); 
                
        LastNamePar.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                
                if(t1.equals("")) 
                    editParButton.setDisable(true);
                else {
                    editParButton.setDisable(false);
                    LastNamePar.setOnKeyPressed(keyUpdate);
                }      
             }
         });
        
        lParticipant = new Label("Wybierz: ");
        lParticipant.setId("lNamePar");
        hbGridUpdateComBoxPar = new HBox();
        hbGridUpdateComBoxPar.setId("hbGridAddTitle");
        hbGridUpdateComBoxPar.getChildren().addAll(lParticipant, target);
        
        gridUpdate.add(hbGridUpdateComBoxPar, 0, 1, 2, 1);       
        
        stack = new StackPane();
        stack.getChildren().add(lLastNamePar);
        stack.setMinWidth(130);
        gridUpdate.add(stack, 0, 2);
        gridUpdate.add(LastNamePar, 1, 2);        
        gridUpdate.add(active, 1, 3);
        
        editParButton = new Button("Zmień dane");
        editParButton.setId("addParButton");
        editParButton.setDisable(true);
        hbGridEditParButton = new HBox();
        hbGridEditParButton.setId("hbGridAddTitle");
        hbGridEditParButton.getChildren().add(editParButton);
        gridUpdate.add(hbGridEditParButton, 0, 4, 2, 1);
        
        editParButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                bodyEditParButtonandKey();
            }
        }); 
        
        keyUpdate = new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.ENTER) {
                    bodyEditParButtonandKey();
                }
            }
        };      
    }
    
    private void bodyEditParButtonandKey() {
            
        String changeParticipant = "UPDATE targets SET"
                          +  " target_name = '" + LastNamePar.getText() + "',"
                          +  " active = " + activeStatus
                          +  " WHERE id_target = " + IdPart;
        
        sqlQuery(changeParticipant);
        target.getSelectionModel().clearSelection();
        comboItemRefresh();
    } 
        
    private void comboItemRefresh() {
      
        addTargetData();

        for (TargetData data1 : data) {
            target.setItems(data);
        }  
    }
    
    private void comboRefresh() {
     
        ItemId = target.getSelectionModel().getSelectedIndex();
        
        if(ItemId==-1){

            LastNamePar.clear();
            editParButton.setDisable(true);

         }
        else {
        
            IdPart = data.get(ItemId).id_target;      
            LastNamePar.setText(data.get(ItemId).target);
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
            setTitle("Opis");
            }
        });
        
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            LastNamePar.clear();
            prepareGridPaneAdd();    
            borderPane.setCenter(gridAdd);
            setTitle("Dodaj cel wyjścia");
            }
        });
       
        editButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            LastNamePar.clear();    
            prepareGridPaneUpdate();    
            borderPane.setCenter(gridUpdate);
            setTitle("Zmień cel wyjścia");
            }
        });
        
        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            close();
            window.buttonAdmin.setDisable(false);
            }
        });                       
    }
    
    private void prepareViev() {
       
        Main = new VBox();
        Main.setId("vbpane");
        
        String description = "Panel w którym dodajemy:\n\n- cel wyjścia uczestników OHP,"
                           + " \n\n- aktywny oznacza, iż cel wyjścia jest widoczny dla zwykłych użytkowników,"
                           + " \n\n- nieaktywny oznacza, iż cel wyjścia jest nie widoczny dla zwykłych użytkowników.\n\n";

        lViev = new Label();
        lViev.setText(description);
        lViev.setWrapText(true);
        lViev.setId("lViev");
        
        Main.getChildren().addAll(lViev);     
    }
    
    private void sqlQuery(String query) {
        
        try {
            st = db.con.createStatement();
            st.executeUpdate(query);
            st.close();
        } catch (SQLException ex) {
          
        }
    }
    
    public void addTargetData() {                                              
  
        data = FXCollections.observableArrayList();
            
        try {
        
            st = db.con.createStatement();
            
            String query = "SELECT id_target, target_name, active FROM targets";
      
            ResultSet rs = st.executeQuery(query);
        
            while (rs.next()){
                TargetData unit = new TargetData(
                        rs.getInt("id_target"), 
                        rs.getString("target_name"),
                        rs.getInt("active")
                );
                data.add(unit); 
            }
            
            st.close();
                     
        } catch (SQLException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }  
}
