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
import javafx.scene.layout.VBox;
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
    DeSubscribe odpisz;
    
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
    int flag = 0;
    String Comm;
    String DateExit = null;
   
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
        
        lInfo = new Label();
        lInfo.setId("lOpisMsgBox1-Black");
         
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
                        lInfo.setText("");
                    }
                }                 
            )
        );
        
        timeline.setCycleCount(1440);
        timeline.play();
        
        try {
            date_return = formatter_three.parse(resultDate_three);
            } catch (ParseException ex) {
        }
           
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
                
                if(ItemIdPart==-1) exit.setDisable(true);
                else {
                    exit.setDisable(false);
                    getData();
                } 
                
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
                
                flag = 0;
                exit.setDisable(false);
                
                if(changeTime.isSelected()){
                    timeTextField.setText(resultDate_two);
                    hbGridTime.setVisible(true);
                    
                }
                else {
                    hbGridTime.setVisible(false);
                    try {
                        date_return = formatter_three.parse(resultDate_three);
                    } catch (ParseException ex) {

                    }
                }
   
            }
        });
        
        timeTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
               
                flag = 0;
                exit.setDisable(false);
                
                try {
                    date_return = formatter_three.parse(resultDate_four + " " + timeTextField.getText() + ":00");
                } catch (ParseException ex) {

                }   
                              
             }
        }); 
        
        exit = new Button("Powrót");
        exit.setId("windows7-default");
        exit.setMinWidth(320);
        exit.setDisable(true);
        hbGridButton = new HBox();
        hbGridButton.setId("hbGridAddTitle");
        hbGridButton.getChildren().addAll(exit);
        gridSubscribe.add(hbGridButton, 0, 5, 2, 1);  
        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) { 

                if(flag==0) {
                    if(date_exit.before(date_return)) {
                        msgBox1();
                        odpisz.hide();
                        lInfo.setText("");
                    }
                    else {
                        lInfo.setText("Godzina powrótu jest wcześniejsza\nniż godzina wyjścia!");
                        flag = 1;
                        exit.setDisable(true);
                    }
                }
            }
        });
        
        HBox lInfoBox = new HBox();
        lInfoBox.setMinHeight(50);
        lInfoBox.setAlignment(Pos.CENTER);
        lInfoBox.getChildren().add(lInfo);
        
        gridSubscribe.add(lInfoBox, 0, 6, 2, 1); 
                
        SceneSubscribe = new Scene(gridSubscribe);
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
            st.close();
            
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
        
        String query;
        
        if(changeTime.isSelected()){
                query = "Update main_exre set "
                         + " return_date =" 
                         + " '" + resultDate_four + " " + timeTextField.getText() + ":00',"
                         + " comm = '" + taComment.getText() + "',"
                         + " id_user_return =" + userId 
                         + " where id_exre = " + id_exre;
        }
        else {
            query = "Update main_exre set "
                     + " return_date =" 
                     + " '" + resultDate_three + "',"
                     + " comm = '" + taComment.getText() + "',"
                     + " id_user_return =" + userId 
                     + " where id_exre = " + id_exre;
        }
         
        try {
            st = db.con.createStatement();
            st.execute(query);
            st.close();
        } catch (SQLException ex) {
           
        }          
        
        changeStatus();

        }
        
    public void changeStatus() {
        
        String query = "UPDATE exitreturn SET exit_return = 0 where id_part = " + dataComboPar.get(ItemIdPart).id_part;
    
        try {
            
            st = db.con.createStatement();
            st.execute(query);
            st.close();
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
            st.close();
            
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
            st.close();
        }    
        catch (SQLException ex) {} 
                           
     }
    
    public void msgBox1() {

        final Stage changePas = new Stage();
        GridPane changePasUserPane = new GridPane();
        changePasUserPane.setId("gridMsgBox1");
        scenetitle.setText("Potwierdzenie");
        changePasUserPane.add(hbSceneTitle, 0, 0, 2, 1);

        VBox opisPane = new VBox();
        opisPane.setId("vboxMsgBox1");

        Label one = new Label("Wypisany w dniu:");
        one.setId("lOpisMsgBox1-Black");
        Label two = new Label(DateExit.substring(0, 11));
        two.setId("lOpisMsgBox1-Red");
        HBox one_two = new HBox();
        one_two.setId("hboxMsgBox1");
        one_two.getChildren().addAll(one, two);
        Label three = new Label("o godzinie:");
        three.setId("lOpisMsgBox1-Black");
        Label four = new Label(DateExit.substring(11, 16));
        four.setId("lOpisMsgBox1-Red");
        HBox three_four = new HBox();
        three_four.setId("hboxMsgBox1");
        three_four.getChildren().addAll(three, four);
        Label five = new Label(dataComboPar.get(ItemIdPart).toString());
        five.setId("lOpisMsgBox1-Red2");
        HBox l_five = new HBox();
        l_five.setAlignment(Pos.CENTER);
        l_five.setId("hboxMsgBox2");
        l_five.getChildren().addAll(five);
        Label seven = new Label("powrócił w dniu:");
        seven.setId("lOpisMsgBox1-Black");
        Label eight = new Label(resultDate_four);
        eight.setId("lOpisMsgBox1-Red");
        HBox seven_eight = new HBox();
        seven_eight.setId("hboxMsgBox1");
        seven_eight.getChildren().addAll(seven, eight);
        Label nine = new Label("o godzinie:");
        nine.setId("lOpisMsgBox1-Black");
        Label ten = new Label();
        if(changeTime.isSelected()){
            ten.setText(timeTextField.getText());
        }
        else {
           ten.setText(resultDate_two); 
        }
        ten.setId("lOpisMsgBox1-Red");
        HBox nine_ten = new HBox();
        nine_ten.setId("hboxMsgBox1");
        nine_ten.getChildren().addAll(nine, ten);

    opisPane.getChildren().addAll(one_two, three_four, l_five, seven_eight, nine_ten);        
    changePasUserPane.add(opisPane, 0, 1, 2, 1);

    Button yes = new Button("TAK");
    yes.setId("windows7-default");
    yes.setMinWidth(100);
    yes.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            ReturnPar();
            timeline.stop();
            window.NewDataAdd();
            changePas.close();
            msgBo2();
        }
    });
    changePasUserPane.add(yes, 0, 2);

    Button no = new Button("NIE");
    no.setId("windows7-default");
    no.setMinWidth(100);
    no.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            changePas.close();
            participant.getSelectionModel().clearSelection();
            taComment.clear();
            changeTime.setSelected(false);
            odpisz.show();
        }
    });

    changePasUserPane.add(no, 1, 2);

    Scene changePasUser = new Scene(changePasUserPane);
    changePasUser.getStylesheets().add(Testowa.class.getResource("Subscribe.css").toExternalForm());
    changePas.setScene(changePasUser);
    changePas.setTitle("Potwierdzenie powrotu");
    changePas.show();
}
    
    public void msgBo2() {
        
        final Stage changePas = new Stage();
        GridPane  changePasUserPane = new GridPane();
        changePasUserPane.setId("gridAdd");
        
        Label opis = new Label("Wybierz działanie:");

        opis.setId("lOpisMsgBox1-Black");
        
        HBox opisBox = new HBox();
        opisBox.setAlignment(Pos.CENTER);
        opisBox.getChildren().add(opis);
        
        changePasUserPane.add(opisBox, 0, 0, 2, 1);
        
        Button yes = new Button("Kolejny powrót");
        yes.setId("windows7-default");
        yes.setMinWidth(120);
        yes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changePas.close();
                refreshCombo();
                participant.getSelectionModel().clearSelection();
                taComment.clear();
                changeTime.setSelected(false);
                odpisz.show();
            }
        });
        changePasUserPane.add(yes, 0, 1);
        
        Button no = new Button("Zakończ");
        no.setId("windows7-default");
        no.setMinWidth(120);
        no.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changePas.close();
                odpisz.close();
            }
        });
        
        changePasUserPane.add(no, 1, 1);
        
        Scene changePasUser = new Scene(changePasUserPane);
        changePasUser.getStylesheets().add(Testowa.class.getResource("Subscribe.css").toExternalForm());
        changePas.setScene(changePasUser);
        changePas.setTitle("Wiadomość");
        changePas.show();
    }

}