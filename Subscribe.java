/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testowa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author andrzej
 */
public final class Subscribe extends Stage {
    
    DB db;
    Statement st;
    User login;
    MainWindow window;
    
    private Scene SceneSubscribe;
    private GridPane gridSubscribe;
    private StackPane rozmiar;
    private HBox hbSceneTitle, hbGridComBoxPar, hbGridComBoxTar, hbGridData;
    private ComboBox participant, target;
    private ObservableList<ParticipantData> dataCombo;
    private Button exit;
    private Text scenetitle;
    private Label lParticipant, lTarget, lData;
    private Date data;
    private SimpleDateFormat formatter;
    
    public Subscribe(){
        prepareScene();
    }
    
    private void prepareScene(){
        
        gridSubscribe = new GridPane();
        gridSubscribe.setId("gridAdd");
        
        data = new Date();
        Locale currentLocale = Locale.getDefault();
        formatter = new SimpleDateFormat("EEEEEEEE,   dd MMMMMMMMMMMM yyyy,   HH:mm", currentLocale);
        String resultDate = formatter.format(data);
        lData = new Label(resultDate);
        lData.setId("lNamePar");
        
        hbGridData = new HBox();
        hbGridData.setId("hbGridAddTitle");
        hbGridData.getChildren().add(lData);
        gridSubscribe.add(hbGridData, 0, 0, 2, 1);
        
        scenetitle = new Text("Wypisz");
        scenetitle.setId("gridAddTitle");
        hbSceneTitle = new HBox();
        hbSceneTitle.setId("hbGridAddTitle");
        hbSceneTitle.getChildren().add(scenetitle);
        gridSubscribe.add(hbSceneTitle, 0, 1, 2, 1);
        
        lParticipant = new Label("Wybierz uczestnika: ");
        lParticipant.setId("lNamePar");
        participant = new ComboBox();
        participant.setMinWidth(200);
        hbGridComBoxPar = new HBox();
        hbGridComBoxPar.setId("hbGridAddTitle");
        hbGridComBoxPar.getChildren().addAll(lParticipant, participant);
        gridSubscribe.add(hbGridComBoxPar, 0, 2, 2, 1);  
        
        lTarget = new Label("Cel Wyjścia: ");
        lTarget.setId("lNamePar");
        rozmiar = new StackPane();
        rozmiar.getChildren().add(lTarget);
        rozmiar.setMinWidth(200);
        rozmiar.setAlignment(Pos.TOP_LEFT);
        target = new ComboBox();
        target.setMinWidth(200);
        hbGridComBoxTar = new HBox();
        hbGridComBoxTar.setId("hbGridAddTitle");
        hbGridComBoxTar.getChildren().addAll(rozmiar, target);
        gridSubscribe.add(hbGridComBoxTar, 0, 3, 2, 1);  
        
        exit = new Button("Wypisz");
        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            
            
            }
        });
        
        SceneSubscribe = new Scene(gridSubscribe, 500, 400);
        SceneSubscribe.getStylesheets().add(Testowa.class.getResource("Subscribe.css").toExternalForm());
        setScene(SceneSubscribe);
        setTitle("Formularz wypisu uczestnika");
    } 
    
    
    
    
    public void refreshCombo(){
       
        dataCombo = FXCollections.observableArrayList();
        
        try {
            
            st = db.con.createStatement();
            
            String sql2 = "select p.id_part, p.first_name, p.last_name from participants p join exitreturn er on p.id_part = er.id_part where p.active=0 and er.exit_return=0;";
                     
            ResultSet rs = st.executeQuery(sql2);
            
            while(rs.next()) {
                ParticipantData unit = new ParticipantData(
                        rs.getInt("p.id_part"), 
                        rs.getString("p.first_name"),
                        rs.getString("p.last_name"),
                        2
                );
                dataCombo.add(unit);        
            }
            
            participant.setItems(dataCombo);
//            participant.setMinWidth(200);

        } catch(Exception ex) {}
    }
    
                                         
}
