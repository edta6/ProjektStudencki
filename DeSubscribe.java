/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testowa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import testowa.TimeTextFieldTest.TimeTextField;

/**
 *
 * @author xxx
 */
public final class DeSubscribe extends Stage {
    
    DB db;
    Statement st;
    MainWindow window;
    
    private Scene SceneSubscribe;
    private GridPane gridSubscribe;
    private StackPane spTime, spChangeTime, spComment;
    private HBox hbSceneTitle, hbGridComBoxPar, hbGridData;
    private HBox hbGridTime, hbGridButton;
    private ComboBox participant;
    private ObservableList<ParticipantData> dataComboPar;
    private Button exit;
    private Text scenetitle;
    private Label lParticipant, lData,  lComment, lInfo;
    private TextArea taComment;
    private Locale currentLocale;
    private Date data, date_exit, date_return;
    private Timeline timeline;
    private SimpleDateFormat formatter, formatter_two, formatter_three, formatter_four;
    private String resultDate, resultDate_two, resultDate_three, resultDate_four;
    private TimeTextField timeTextField;
    private CheckBox changeTime;
    int ItemIdPart;
    int userId;
    int id_exre;
    int date_error = 0;
    String Comm;
    String DateExit;
   
    
    public DeSubscribe(){
        prepareScene();
    }
    
     private void prepareScene(){
        
        gridSubscribe = new GridPane();
        gridSubscribe.setId("gridAdd");
        
        currentLocale = Locale.getDefault();
        formatter = new SimpleDateFormat("EEEEEEEE,   dd MMMMMMMMMMMM yyyy,   HH:mm", currentLocale);
        formatter_two = new SimpleDateFormat("HH:mm", currentLocale);
        formatter_three = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", currentLocale);
        formatter_four = new SimpleDateFormat("yyyy-MM-dd", currentLocale);
        
        data = new Date();
        resultDate = formatter.format(data);
        resultDate_two = formatter_two.format(data);
        resultDate_three = formatter_three.format(data);
        resultDate_four = formatter_four.format(data);
        
        lData = new Label(resultDate);
        lData.setId("lNamePar");
         
        timeline = new Timeline(
            new KeyFrame(
                Duration.seconds(10), 
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        data = new Date();
                        resultDate = formatter.format(data);
                        resultDate_two = formatter_two.format(data);
                        resultDate_three = formatter_three.format(data);
                        resultDate_four = formatter_four.format(data); 
                        lData.setText(resultDate);
                    }
                }                 
            )
        );
        
        timeline.setCycleCount(1440);
        timeline.play();
   
        hbGridData = new HBox();
        hbGridData.setId("hbGridAddTitle");
        hbGridData.getChildren().add(lData);
        gridSubscribe.add(hbGridData, 0, 0, 2, 1);
        
        scenetitle = new Text("Powrócił");
        scenetitle.setId("gridAddTitle");
        hbSceneTitle = new HBox();
        hbSceneTitle.setId("hbGridAddTitle");
        hbSceneTitle.getChildren().add(scenetitle);
        gridSubscribe.add(hbSceneTitle, 0, 1, 2, 1);
        
        lParticipant = new Label("Wybierz uczestnika: ");
        lParticipant.setId("lNamePar");
        participant = new ComboBox();
        participant.setMinWidth(200);
        participant.setMaxWidth(200);
        
        participant.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ParticipantData>() {           
            @Override
            public void changed(ObservableValue<? extends ParticipantData> ov, ParticipantData t, ParticipantData t1) {
                
                ItemIdPart = participant.getSelectionModel().getSelectedIndex();
                getData();
                
            }
        }); 
        
        hbGridComBoxPar = new HBox();
        hbGridComBoxPar.setId("hbGridAddTitle");
        hbGridComBoxPar.getChildren().addAll(lParticipant, participant);
        gridSubscribe.add(hbGridComBoxPar, 0, 2, 2, 1);       
        
        lComment = new Label("Uwagi:");
        lComment.setId("lNamePar");
        gridSubscribe.add(lComment, 0, 3);
        
        taComment = new TextArea();
        taComment.setPrefRowCount(2);
        taComment.setPrefColumnCount(100);
        taComment.setWrapText(true);
        taComment.setPrefWidth(200);
        taComment.setPromptText("Wpisz ewentualne uwagi");
        spComment = new StackPane();
        spComment.setMinHeight(50);
        spComment.getChildren().add(taComment);
        gridSubscribe.add(spComment, 1, 3);
  
        changeTime = new CheckBox("Zmień czas: ");
        changeTime.setId("lNamePar");
        spChangeTime = new StackPane();
        spChangeTime.getChildren().add(changeTime);
        spChangeTime.setMinWidth(200);
        spChangeTime.setAlignment(Pos.TOP_LEFT);
        gridSubscribe.add(spChangeTime, 0, 4); 
        
        timeTextField = new TimeTextField(resultDate_two);
        spTime = new StackPane();
        spTime.getChildren().add(timeTextField);
        spTime.setMaxWidth(50);
        hbGridTime = new HBox();
        hbGridTime.getChildren().addAll(spTime);
        hbGridTime.setVisible(false);
        gridSubscribe.add(hbGridTime, 1, 4); 
        
        changeTime.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue ov,Boolean old_val, Boolean new_val) {
                
                if(changeTime.isSelected()){
                    timeTextField.setText(resultDate_two);
                    hbGridTime.setVisible(true);
                }
                else hbGridTime.setVisible(false);
   
            }
        });
        
        exit = new Button("Powrót");
        exit.setId("windows7-default");
        exit.setMinWidth(320);
        hbGridButton = new HBox();
        hbGridButton.setId("hbGridAddTitle");
        hbGridButton.getChildren().addAll(exit);
        gridSubscribe.add(hbGridButton, 0, 5, 2, 1);  
        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {             
                ReturnPar();
                if(date_error==0) {
                    timeline.stop();
                    window.NewDataAdd();
                    close();
                }
            }
        });
        
        lInfo = new Label();
        lInfo.setId("lNamePar");
        gridSubscribe.add(lInfo, 0, 6, 2, 1); 
        
        SceneSubscribe = new Scene(gridSubscribe, 500, 500);
        SceneSubscribe.getStylesheets().add(Testowa.class.getResource("Subscribe.css").toExternalForm());
        setScene(SceneSubscribe);
        setTitle("Formularz powrotu uczestnika");
    } 
    
    public void refreshCombo(){
       
        dataComboPar = FXCollections.observableArrayList();
        
        try {
            
            st = db.con.createStatement();
            
            String sql2 = "select p.id_part, p.first_name, p.last_name from participants p join exitreturn er on p.id_part = er.id_part where p.active=0 and er.exit_return=1;";
                     
            ResultSet rs = st.executeQuery(sql2);
            
            while(rs.next()) {
                ParticipantData unit = new ParticipantData(
                        rs.getInt("p.id_part"), 
                        rs.getString("p.first_name"),
                        rs.getString("p.last_name"),
                        2
                );
                dataComboPar.add(unit);        
            }
            
            participant.setItems(dataComboPar);

        } catch(Exception ex) {}
    }
    
    public void getData() {
        
        String query =  "Select max(id_exre) from main_exre where id_part = " + dataComboPar.get(ItemIdPart).id_part;
        
        id_exre = sqlResult(query);
        
        String query_comm = "Select comm, exit_date from main_exre where id_exre = " + id_exre;
        
        sqlComm(query_comm);
        
        taComment.setText(Comm);
        
        try {
            date_exit = formatter_three.parse(DateExit);
            
        } catch (ParseException ex) {
            
        }
           
    }
    
    public void ReturnPar() {
        
        try {
            date_return = formatter_three.parse(resultDate_four + " " + timeTextField.getText() + ":00");
        } catch (ParseException ex) {
          
        }
        
        String query = null;
        
        if(changeTime.isSelected()){
            if(date_exit.before(date_return)) {
                query = "Update main_exre set "
                         + " return_date =" 
                         + " '" + resultDate_four + " " + timeTextField.getText() + ":00',"
                         + " comm = '" + taComment.getText() + "',"
                         + " id_user_return =" + userId 
                         + " where id_exre = " + id_exre;
                date_error = 0;
            }
            else {
                lInfo.setText("Godzina powrótu jest wcześniejsza\n niż godzina wyjścia!");
                date_error = 1;
            }
        }
        else {
            query = "Update main_exre set "
                     + " return_date =" 
                     + " '" + resultDate_three + "',"
                     + " comm = '" + taComment.getText() + "',"
                     + " id_user_return =" + userId 
                     + " where id_exre = " + id_exre;
            date_error = 0;
        }
         
        try {
            st = db.con.createStatement();
            st.execute(query);  
        } catch (SQLException ex) {
           
        }          
        
        if(date_error==0) changeStatus();

        }
        
    
    private void changeStatus() {
        
        String query = "UPDATE exitreturn SET exit_return = 0 where id_part = " + dataComboPar.get(ItemIdPart).id_part;
    
        try {
            
            st = db.con.createStatement();
            st.execute(query);    
        } catch (SQLException ex) {
            
        }
     }
    
    
    private Integer sqlResult(String query){
        
        int numberRow=0;
        
        try {
           
            st = db.con.createStatement();
            
            ResultSet rs = st.executeQuery(query);
            
            while(rs.next()) {
                numberRow = rs.getInt(1);
            }
        }    
        catch (SQLException ex) {} 
        
        return numberRow;    
    }
    
    private void sqlComm(String query) {
        
        try {
           
            st = db.con.createStatement();
            
            ResultSet rs = st.executeQuery(query);
            
            while(rs.next()) {
                Comm = rs.getString("comm");
                DateExit = rs.getString("exit_date");
            }
        }    
        catch (SQLException ex) {} 
                           
     }
    
    
}