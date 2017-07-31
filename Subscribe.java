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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author andrzej
 */
public class Subscribe extends Stage {
    
    DB db;
    Statement st;
    User login;
    MainWindow window;
    
    private Scene SceneSubscribe;
    private ComboBox uczestnicy;
    private TextField exitTarget;
    private TextField exitAdres;
    private TextField exitNote;
    private ObservableList<String> data;
    private Button exit;
    
    public Subscribe(){
        prepareScene();
    }
    
    private void prepareScene(){
        
        Text scenetitle = new Text("Logowanie");
        
        HBox hbscenetitle = new HBox();
        hbscenetitle.getChildren().add(scenetitle);
        hbscenetitle.setAlignment(Pos.CENTER);
        
        uczestnicy = new ComboBox();
        exitTarget = new TextField();
        exitAdres  = new TextField();
        exitNote = new TextField();
        exit = new Button("Wypisz");
        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            exitPeopleActionPerformed();
            close();
            }
        });
        
        VBox vboxAll = new VBox();
        vboxAll.getChildren().addAll(hbscenetitle, uczestnicy, exitTarget, exitAdres, exitNote, exit);
        vboxAll.setSpacing(10);
        SceneSubscribe = new Scene(vboxAll, 500, 400);
        SceneSubscribe.getStylesheets().add(Testowa.class.getResource("Subscribe.css").toExternalForm());
        setScene(SceneSubscribe);
        setTitle("The book out and trips");
    } 
    
    public void refreshCombo(){
       
        data = FXCollections.observableArrayList();
        
        try {
            
            st = db.con.createStatement();
            
            String sql2 = "SELECT imie_nazwisko FROM uczestnik WHERE wypis=1";
                     
            ResultSet rs = st.executeQuery(sql2);
            
            while(rs.next()) {
                String unit = rs.getString("imie_nazwisko");
                data.add(unit); 
            }
           uczestnicy.setItems(data); 
        } catch(Exception ex) {}
    }
    
                                         
    private void exitPeopleActionPerformed() {                                           
        
        czas date = new czas();
         
        try {
        
        st = db.con.createStatement();
        
        String cb1 = (String) uczestnicy.getValue();  
        String tf2 = exitTarget.getText();
        String tf3 = exitAdres.getText();
        String tf4 = exitNote.getText();
        
        String sql = "INSERT INTO wypis (imie_nazwisko,cel_wyjscia,w_data,adres,uwagi,exituser) VALUES "
                + "('" + cb1 + "','" + tf2 + "','"+ date.getcalendarDate() 
                + "','" + tf3 + "', '" + tf4 + "', '" + login.getUser() + "')";
        
        String sql1 = "update uczestnik set wypis=1 where imie_nazwisko='" + cb1 + "'" ;
        
        st.executeUpdate(sql);
        st.executeUpdate(sql1);
             
        } catch (SQLException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
}
