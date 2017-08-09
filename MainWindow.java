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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Andrzej Pawlik
 */
public class MainWindow extends Stage {

    DB db;
    Statement st;
    User login;
    
    private Scene sceneMainWindow;
    private BorderPane borderPane;
    private StackPane stackTable;
    private GridPane bottomPane;
    private HBox hbuttonMenuLeft,  hbuttonMenuRight, hbnameLogin;
    public  TableView<DataWypis> table; 
    private Button buttonExit, buttonHome, buttonAdmin, buttonClose;
    private Label uTextNow;
    private Label nameLogin, userText;
    public final String user;
    public final int role;
   
    public MainWindow(String user, int role) {
        this.user = user;
        this.role = role;
        prepareScene();
        if(role==1||user.equalsIgnoreCase("root")) {
            buttonAdmin.setDisable(false);
            buttonAdmin.setVisible(true);
        }
        else {
            buttonAdmin.setDisable(true);
            buttonAdmin.setVisible(false);
        }
    }
    
    private void prepareScene(){
        
        borderPane = new BorderPane();
        
        prepareBorderPaneTop ();
        borderPane.setTop(hbuttonMenuLeft);
        
        prepareBorderPaneCenter();
        borderPane.setCenter(stackTable);
        
        prepareBorderPaneBottom();
        borderPane.setBottom(bottomPane);
        
        sceneMainWindow = new Scene(borderPane, 1200, 650);
        sceneMainWindow.getStylesheets().add(Testowa.class.getResource("MainWindow.css").toExternalForm());
        setScene(sceneMainWindow);
        setTitle("The book out and trips");
    }
    
    private void prepareBorderPaneTop () {
    
        buttonExit = new Button("Wypisz");
        buttonExit.setId("windows7");
        buttonExit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Subscribe wypisz = new Subscribe();
                wypisz.db = db;
                wypisz.login = login;
                wypisz.refreshCombo();
                wypisz.show();
            }
        });
        
        buttonHome = new Button("Powroty");
        buttonHome.setId("windows7");
        buttonHome.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
 
            }
        });
        
        buttonAdmin = new Button("Panel Administratora");
        buttonAdmin.setId("windows7");
        buttonAdmin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AdminPane panelAdmin = new AdminPane();
                panelAdmin.db = db;
                panelAdmin.showAndWait();
            }
        });
        
        nameLogin = new Label();
        userText = new Label();
        userText.setText(user);
        userText.setId("userText");
        nameLogin.setText("Użytkownik: ");
        hbnameLogin = new HBox();
        hbnameLogin.getChildren().addAll(nameLogin, userText);
        hbnameLogin.setId("windows7");
        
        buttonClose = new Button("Zamknij");
        buttonClose.setId("windows7");
        buttonClose.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            close();
            }
        });
        
        hbuttonMenuLeft = new HBox();
        hbuttonMenuRight = new HBox();
        hbuttonMenuRight.setAlignment(Pos.CENTER_RIGHT);
        hbuttonMenuRight.setMinWidth(760);
        hbuttonMenuRight.getChildren().addAll(hbnameLogin, buttonClose);
        hbuttonMenuRight.setId("hbuttonMenu");
        hbuttonMenuLeft.setId("hbuttonMenu");
        hbuttonMenuLeft.setAlignment(Pos.CENTER_LEFT);
        hbuttonMenuLeft.setMinWidth(1200);
        hbuttonMenuLeft.getChildren().addAll(buttonExit, buttonHome, buttonAdmin, hbuttonMenuRight);
    }
    
    private void prepareBorderPaneCenter(){
        
        table = new TableView<>();
        table.setId("table");
        table.setEditable(false);
        
        TableColumn lp = new TableColumn("Lp.");
        lp.setCellValueFactory(new PropertyValueFactory<>("userId"));
        lp.setPrefWidth(60);
        TableColumn  name = new TableColumn("Nazwisko i Imię");
        name.setCellValueFactory(new PropertyValueFactory<>("userName"));
        name.setPrefWidth(200);
        TableColumn target = new TableColumn("Cel wyjścia");
        target.setCellValueFactory(new PropertyValueFactory<>("target"));
        target.setPrefWidth(200);
        TableColumn exit = new TableColumn("Wyszedł");
        exit.setPrefWidth(180);
        TableColumn dataExit = new TableColumn("dnia");
        dataExit.setCellValueFactory(new PropertyValueFactory<>("dataExit"));
        dataExit.setPrefWidth(90);
        TableColumn timeExit = new TableColumn("godz.");
        timeExit.setCellValueFactory(new PropertyValueFactory<>("timeExit"));
        timeExit.setPrefWidth(90);
        TableColumn adres = new TableColumn("Przewidywane miejsce\n(adres)\nTel. kontaktowy");
        adres.setCellValueFactory(new PropertyValueFactory<>("adress"));
        adres.setPrefWidth(210);
        TableColumn cameBack = new TableColumn("Powrócił");
        cameBack.setPrefWidth(180);
        TableColumn datacameBack = new TableColumn("dnia");
        datacameBack.setCellValueFactory(new PropertyValueFactory<>("datacameBack"));
        datacameBack.setPrefWidth(90);
        TableColumn timecameBack = new TableColumn("godz.");
        timecameBack.setCellValueFactory(new PropertyValueFactory<>("timecameBack"));
        timecameBack.setPrefWidth(90);
        TableColumn comments = new TableColumn("Uwagi");
        comments.setCellValueFactory(new PropertyValueFactory<>("comments"));
        comments.setPrefWidth(170);
        
        exit.getColumns().addAll(dataExit, timeExit);
        cameBack.getColumns().addAll(datacameBack, timecameBack);
        
        table.getColumns().addAll(lp, name, target, exit, adres, cameBack, comments);
        
        stackTable = new StackPane();
        stackTable.getChildren().add(table);
    }
    
    private void prepareBorderPaneBottom(){
        
        bottomPane = new GridPane();
        bottomPane.setPrefHeight(200.0);
        uTextNow = new Label("Dół do zrobienia!");
        bottomPane.add(uTextNow, 0, 1);
        
    }
    
    private void userRole() {
        
         try {
        
            st = db.con.createStatement();
            
            String query = "Select role from user_ohp where nick like '" + user + "'";
      
            ResultSet rs = st.executeQuery(query);
            
            

         } catch (SQLException ex) {
            
        }  
    }
    
}