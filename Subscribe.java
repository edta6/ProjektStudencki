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
 * @author andrzej
 */
public final class Subscribe extends Stage {
    
    DB db;
    Statement st;
    MainWindow window;
    Subscribe wypisz;
    
    private Scene SceneSubscribe;
    private GridPane gridSubscribe;
    private StackPane rozmiar, spTime, spChangeTime, spPlace, spComment;
    private HBox hbSceneTitle, hbGridComBoxPar, hbGridComBoxTar, hbGridData;
    private HBox hbGridTime, hbGridButton;
    private ComboBox participant, target;
    private ObservableList<ParticipantData> dataComboPar;
    private ObservableList<TargetData> dataComboTar;
    private Button exit;
    private Text scenetitle;
    private Label lParticipant, lTarget, lData, lPlace, lComment;
    private TextArea taPlace, taComment;
    private Locale currentLocale;
    private Date data;
    private Timeline timeline;
    private SimpleDateFormat formatter, formatter_two, formatter_three, formatter_four;
    private String resultDate, resultDate_two, resultDate_three, resultDate_four;
    private TimeTextField timeTextField;
    private CheckBox changeTime;
    int ItemIdPart=-1, ItemIdTarg=-1;
    int userId;
    
    public Subscribe(){
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
        participant.setMaxWidth(200);
        
        participant.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ParticipantData>() {           
            @Override
            public void changed(ObservableValue<? extends ParticipantData> ov, ParticipantData t, ParticipantData t1) {
                
                ItemIdPart = participant.getSelectionModel().getSelectedIndex();
                
                if(ItemIdPart==-1 || ItemIdTarg ==-1 || taPlace.getText().equals("")) exit.setDisable(true);
                else exit.setDisable(false); 
                
            }
        }); 
        
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
        
        target.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TargetData>() {           
            @Override
            public void changed(ObservableValue<? extends TargetData> ov, TargetData t, TargetData t1) {
                
                ItemIdTarg = target.getSelectionModel().getSelectedIndex();
                
                if(ItemIdPart==-1 || ItemIdTarg ==-1 || taPlace.getText().equals("")) exit.setDisable(true);
                else exit.setDisable(false); 
                
            }
        });
        
        hbGridComBoxTar = new HBox();
        hbGridComBoxTar.setId("hbGridAddTitle");
        hbGridComBoxTar.getChildren().addAll(rozmiar, target);
        gridSubscribe.add(hbGridComBoxTar, 0, 3, 2, 1);  
        
        lPlace = new Label("Miejsce pobytu: ");
        lPlace.setId("lNamePar");
        gridSubscribe.add(lPlace, 0, 4);  
        
        taPlace = new TextArea();
        taPlace.setPrefRowCount(2);
        taPlace.setPrefColumnCount(100);
        taPlace.setWrapText(true);
        taPlace.setPrefWidth(200);
        taPlace.setPromptText("Wpisz przewidywane miejsce pobytu");
        
        taPlace.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
            
                if(ItemIdPart==-1 || ItemIdTarg ==-1 || taPlace.getText().equals("")) exit.setDisable(true);
                else exit.setDisable(false); 
                
            }
        });
            
        spPlace = new StackPane();
        spPlace.setMinHeight(50);
        spPlace.getChildren().add(taPlace);
        gridSubscribe.add(spPlace, 1, 4);
        
        lComment = new Label("Uwagi:");
        lComment.setId("lNamePar");
        gridSubscribe.add(lComment, 0, 5);
        
        taComment = new TextArea();
        taComment.setPrefRowCount(2);
        taComment.setPrefColumnCount(100);
        taComment.setWrapText(true);
        taComment.setPrefWidth(200);
        taComment.setPromptText("Wpisz ewentualne uwagi");
        spComment = new StackPane();
        spComment.setMinHeight(50);
        spComment.getChildren().add(taComment);
        gridSubscribe.add(spComment, 1, 5);
  
        changeTime = new CheckBox("Zmień czas: ");
        changeTime.setId("lNamePar");
        spChangeTime = new StackPane();
        spChangeTime.getChildren().add(changeTime);
        spChangeTime.setMinWidth(200);
        spChangeTime.setAlignment(Pos.TOP_LEFT);
        gridSubscribe.add(spChangeTime, 0, 6); 
        
        timeTextField = new TimeTextField(resultDate_two);
        spTime = new StackPane();
        spTime.getChildren().add(timeTextField);
        spTime.setMaxWidth(50);
        hbGridTime = new HBox();
        hbGridTime.getChildren().addAll(spTime);
        hbGridTime.setVisible(false);
        gridSubscribe.add(hbGridTime, 1, 6); 
        
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
        
        exit = new Button("Wypisz");
        exit.setId("windows7-default");
        exit.setMinWidth(320);
        exit.setDisable(true);
        hbGridButton = new HBox();
        hbGridButton.setId("hbGridAddTitle");
        hbGridButton.getChildren().addAll(exit);
        gridSubscribe.add(hbGridButton, 0, 7, 2, 1);  
        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                msgBox1();
                wypisz.hide();
            }
        });
        
        SceneSubscribe = new Scene(gridSubscribe, 500, 500);
        SceneSubscribe.getStylesheets().add(Testowa.class.getResource("Subscribe.css").toExternalForm());
        setScene(SceneSubscribe);
        setTitle("Formularz wypisu uczestnika");
    } 
    
    public void refreshCombo(){
       
        dataComboPar = FXCollections.observableArrayList();
        
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
                dataComboPar.add(unit);        
            }
            
            participant.setItems(dataComboPar);

        } catch(Exception ex) {}
    }
    
    public void refreshComboTar(){
       
        dataComboTar = FXCollections.observableArrayList();
        
        try {
            
            st = db.con.createStatement();
            
            String query = "SELECT id_target, target_name FROM targets where active = 0 ;";
                     
            ResultSet rs = st.executeQuery(query);
            
            while(rs.next()) {
                TargetData unit = new TargetData(
                        rs.getInt("id_target"), 
                        rs.getString("target_name"),
                        0
                );
                dataComboTar.add(unit);        
            }
            
            target.setItems(dataComboTar);

        } catch(Exception ex) {}
    }
    
    public void exit_participant() {
        
        String query;
        
        if(changeTime.isSelected()){
            query = "INSERT INTO main_exre (id_part, id_target, exit_date, place,"
                     + " comm, id_user_exit) Values "
                     + "(" + dataComboPar.get(ItemIdPart).id_part + ","
                     + dataComboTar.get(ItemIdTarg).id_target + ","
                     + " '" + resultDate_four + " " + timeTextField.getText() + ":00',"
                     + " '" + taPlace.getText() + "',"
                     + " '" + taComment.getText() + "',"
                     + userId + ");";
        }
        else {
            query = "INSERT INTO main_exre (id_part, id_target, exit_date, place,"
                     + " comm, id_user_exit) Values "
                     + "(" + dataComboPar.get(ItemIdPart).id_part + ","
                     + dataComboTar.get(ItemIdTarg).id_target + ","
                     + " '" + resultDate_three + "',"
                     + " '" + taPlace.getText() + "',"
                     + " '" + taComment.getText() + "',"
                     + userId + ");";  
        }
          
        try {
            st = db.con.createStatement();
            st.execute(query);  
        } catch (SQLException ex) {
           
        }
        
        changeStatus();
     }
    
    private void changeStatus() {
        
        String query = "UPDATE exitreturn SET exit_return = 1 where id_part = " + dataComboPar.get(ItemIdPart).id_part;
    
        try {
            
            st = db.con.createStatement();
            st.execute(query);    
        } catch (SQLException ex) {
            
        }
     }
    
    public void msgBox1() {
        
        final Stage changePas = new Stage();
        GridPane  changePasUserPane = new GridPane();
        changePasUserPane.setId("gridMsgBox1");
        scenetitle.setText("Potwierdzenie");
        changePasUserPane.add(hbSceneTitle, 0, 0, 2, 1);
        
        VBox opisPane = new VBox();
        opisPane.setId("vboxMsgBox1");
        
        Label one = new Label("Czy chcesz wypisać:");
        one.setId("lOpisMsgBox1-Black");
        Label two = new Label(dataComboPar.get(ItemIdPart).toString());
        two.setId("lOpisMsgBox1-Red2");
        HBox l_two = new HBox();
        l_two.setAlignment(Pos.CENTER);
        l_two.setId("hboxMsgBox2");
        l_two.getChildren().addAll(two);
        Label three = new Label("w dniu:");
        three.setId("lOpisMsgBox1-Black");
        Label four = new Label(resultDate_four);
        four.setId("lOpisMsgBox1-Red");
        HBox three_four = new HBox();
        three_four.setId("hboxMsgBox1");
        three_four.getChildren().addAll(three, four);
        Label five = new Label("o godzinie:");
        five.setId("lOpisMsgBox1-Black");
        Label six = new Label();
        if(changeTime.isSelected()){
            six.setText(timeTextField.getText());
        }
        else {
           six.setText(resultDate_two); 
        }
        six.setId("lOpisMsgBox1-Red");
        HBox five_six = new HBox();
        five_six.setId("hboxMsgBox1");
        five_six.getChildren().addAll(five, six);
        Label seven = new Label("do:");
        seven.setId("lOpisMsgBox1-Black");
        Label eight = new Label(taPlace.getText());
        eight.setId("lOpisMsgBox1-Red");
        HBox seven_eight = new HBox();
        seven_eight.setId("hboxMsgBox1");
        seven_eight.getChildren().addAll(seven, eight);
        Label nine = new Label("w celu:");
        nine.setId("lOpisMsgBox1-Black");
        Label ten = new Label(dataComboTar.get(ItemIdTarg).target);
        ten.setId("lOpisMsgBox1-Red");
        HBox nine_ten = new HBox();
        nine_ten.setId("hboxMsgBox1");
        nine_ten.getChildren().addAll(nine, ten);
        
        opisPane.getChildren().addAll(one, l_two, three_four, five_six, seven_eight, nine_ten);        
        changePasUserPane.add(opisPane, 0, 1, 2, 1);
        
        Button yes = new Button("TAK");
        yes.setId("windows7-default");
        yes.setMinWidth(80);
        yes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                exit_participant();
                timeline.stop();
                window.NewDataAdd();
                changePas.close();
                msgBo2();
            }
        });
        changePasUserPane.add(yes, 0, 2);
        
        Button no = new Button("NIE");
        no.setId("windows7-default");
        no.setMinWidth(80);
        no.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changePas.close();
                participant.getSelectionModel().clearSelection();
                target.getSelectionModel().clearSelection();
                taPlace.clear();
                taComment.clear();
                changeTime.setSelected(false);
                wypisz.show();
            }
        });
        
        changePasUserPane.add(no, 1, 2);
        
        Scene changePasUser = new Scene(changePasUserPane);
        changePasUser.getStylesheets().add(Testowa.class.getResource("Subscribe.css").toExternalForm());
        changePas.setScene(changePasUser);
        changePas.setTitle("Potwierdzenie wypisu");
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
        
        Button yes = new Button("Kolejny wypis");
        yes.setId("windows7-default");
        yes.setMinWidth(120);
        yes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changePas.close();
                refreshCombo();
                participant.getSelectionModel().clearSelection();
                target.getSelectionModel().clearSelection();
                taPlace.clear();
                taComment.clear();
                changeTime.setSelected(false);
                wypisz.show();
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
                wypisz.close();
            }
        });
        
        changePasUserPane.add(no, 1, 1);
        
        Scene changePasUser = new Scene(changePasUserPane);
        changePasUser.getStylesheets().add(Testowa.class.getResource("Subscribe.css").toExternalForm());
        changePas.setScene(changePasUser);
        changePas.setTitle("Wiadomość");
        changePas.show();
        System.out.println(changePas.getWidth());
    }
                                        
}