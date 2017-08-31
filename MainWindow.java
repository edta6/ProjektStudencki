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
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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
    MainWindow window;
    
    private Scene sceneMainWindow;
    private BorderPane borderPane, bottomPane;
    private StackPane stackTable, mainStack;
//    private GridPane bottomPane;
    private HBox hbuttonMenuLeft,  hbuttonMenuRight, hbnameLogin;
    private Button buttonExit, buttonHome, buttonAdmin, buttonClose;
    private Label uTextNow;
    private Label nameLogin, userText;
    public  TableView<ExreData> table;
    public ObservableList<ExreData> data;
    public final String user;
    public final String nameU;
    public final int role;
    public final int id_user;
   
    public MainWindow(String name, String user, int role, int id_part) {
        this.nameU = name;
        this.user = user;
        this.role = role;
        this.id_user = id_part;
        prepareScene();
        if(role==1||user.equalsIgnoreCase("root")) {
            buttonAdmin.setDisable(false);
            buttonAdmin.setVisible(true);
            if(user.equalsIgnoreCase("root")){
                buttonExit.setDisable(true);
                buttonHome.setDisable(true);    
            } 
            else {
                buttonExit.setDisable(false);
                buttonHome.setDisable(false); 
            }    
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
        borderPane.setBottom(mainStack);
        
        sceneMainWindow = new Scene(borderPane, 1200, 650);
        sceneMainWindow.getStylesheets().add(Testowa.class.getResource("MainWindow.css").toExternalForm());
        setScene(sceneMainWindow);
        setTitle("The book out and trips");
    }
    
    private void prepareBorderPaneTop () { //pierwszy wiersz w oknie, definicja guzikow i ich zachowan
    
        buttonExit = new Button("Wypisz");
        buttonExit.setId("windows7");
        buttonExit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Subscribe wypisz = new Subscribe();
                wypisz.db = db;
                wypisz.userId = id_user;
                wypisz.window = window;
                wypisz.wypisz = wypisz;
                wypisz.refreshCombo();
                wypisz.refreshComboTar();
                wypisz.show();
            }
        });
        
        buttonHome = new Button("Powroty");
        buttonHome.setId("windows7");
        buttonHome.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DeSubscribe odpisz = new DeSubscribe();
                odpisz.db = db;
                odpisz.userId = id_user;
                odpisz.window = window;
                odpisz.odpisz = odpisz;
                odpisz.refreshCombo();
                odpisz.show();
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
        userText.setText(nameU);
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
                Platform.exit();
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
    
    public void refreshTableData(){
       
        data = FXCollections.observableArrayList();
        
        try {
            
            st = db.con.createStatement();
            
            String query = "Select m.id_exre, p.first_name, p.last_name, t.target_name, m.exit_date, m.place, m.comm, u.first_name, u.last_name from main_exre m join participants p on m.id_part=p.id_part join targets t on m.id_target=t.id_target join user_ohp u on m.id_user_exit=u.id_part where m.return_date is NULL;";
                     
            ResultSet rs = st.executeQuery(query);
            
            while(rs.next()) {
                ExreData unit = new ExreData(
                        rs.getInt("m.id_exre"), 
                        rs.getString("p.first_name"),
                        rs.getString("p.last_name"),
                        rs.getString("t.target_name"),
                        rs.getString("m.exit_date"),
                        (""),
                        rs.getString("m.place"),
                        rs.getString("m.comm"),
                        rs.getString("u.first_name"),
                        rs.getString("u.last_name")
                );
                data.add(unit);        
            }
            st.close();
            
        } catch(Exception ex) {}
    }
    
    public void NewDataAdd() {
        refreshTableData();
        table.setItems(data);    
    }
    
    private void prepareBorderPaneCenter(){
        
        table = new TableView<>();
        table.setId("table");
        table.setEditable(false);
        
        final TableColumn lp = new TableColumn("Lp.");
        lp.setCellValueFactory(new PropertyValueFactory("id_exre"));
        lp.setPrefWidth(58);
        final TableColumn  name = new TableColumn("Nazwisko i Imię");
        name.setCellValueFactory(new PropertyValueFactory("FullNamePar"));
        name.setPrefWidth(200);
        final TableColumn target = new TableColumn("Cel wyjścia");
        target.setCellValueFactory(new PropertyValueFactory("Target"));
        target.setPrefWidth(200);
        final TableColumn exit = new TableColumn("Wyszedł");
        exit.setPrefWidth(173);
        final TableColumn dataExit = new TableColumn("dnia");
        dataExit.setCellValueFactory(new PropertyValueFactory("DateEx"));
        dataExit.setPrefWidth(88);
        final TableColumn timeExit = new TableColumn("godz.");
        timeExit.setCellValueFactory(new PropertyValueFactory("HourEx"));
        timeExit.setPrefWidth(85);
        final TableColumn adres = new TableColumn("Przewidywane miejsce\n(adres)\nTel. kontaktowy");
        adres.setCellValueFactory(new PropertyValueFactory("Place"));
        adres.setPrefWidth(210);
        final TableColumn cameBack = new TableColumn("Powrócił");
        cameBack.setPrefWidth(173);
        final TableColumn datacameBack = new TableColumn("dnia");
        datacameBack.setCellValueFactory(new PropertyValueFactory("DateRe"));
        datacameBack.setPrefWidth(88);
        final TableColumn timecameBack = new TableColumn("godz.");
        timecameBack.setCellValueFactory(new PropertyValueFactory("HourRe"));
        timecameBack.setPrefWidth(85);
        final TableColumn comments = new TableColumn("Uwagi");
        comments.setCellValueFactory(new PropertyValueFactory("Comm"));
        comments.setPrefWidth(170);
        
        exit.getColumns().addAll(dataExit, timeExit);
        
        exit.getColumns().addListener(new ListChangeListener() {
        public boolean suspended;
        @Override
        public void onChanged(ListChangeListener.Change change) {
            change.next();
            if (change.wasReplaced() && !suspended) {
                this.suspended = true;
                exit.getColumns().setAll(dataExit, timeExit);
                this.suspended = false;
            }
           }
        });
        
        cameBack.getColumns().addAll(datacameBack, timecameBack);
        
        cameBack.getColumns().addListener(new ListChangeListener() {
        public boolean suspended;
        @Override
        public void onChanged(ListChangeListener.Change change) {
            change.next();
            if (change.wasReplaced() && !suspended) {
                this.suspended = true;
                cameBack.getColumns().setAll(datacameBack, timecameBack);
                this.suspended = false;
            }
           }
        });
        
        table.getColumns().addAll(lp, name, target, exit, adres, cameBack, comments);
        
        table.getColumns().addListener(new ListChangeListener() {
        public boolean suspended;

        @Override
        public void onChanged(Change change) {
            change.next();
            if (change.wasReplaced() && !suspended) {
                this.suspended = true;
                table.getColumns().setAll(lp, name, target, exit, adres, cameBack, comments);
                this.suspended = false;
            }
           }
        });

        stackTable = new StackPane();
//        stackTable.setMinSize(1200, 350);
//        stackTable.setMaxSize(1200, 350);
        stackTable.getChildren().add(table);
    }
    
    private void prepareBorderPaneBottom(){
        
        bottomPane = new BorderPane();
        bottomPane.setId("bottomPane");
        
        mainStack = new StackPane();
        mainStack.setMaxSize(1200, 250);
        mainStack.setMinSize(1200, 250);
        mainStack.getChildren().add(bottomPane);

        uTextNow = new Label("Dół do zrobienia!");
        Label uTextNow1 = new Label("Dół do zrobienia!");
        Label uTextNow2 = new Label("Dół do zrobienia!");
        Label uTextNow3 = new Label("Dół do zrobienia!");
        
        StackPane stackbottomPane = new StackPane();
        stackbottomPane.setId("stackbottomPane");
        stackbottomPane.setMaxSize(1200, 45);
        stackbottomPane.setMinSize(1200, 45);
        stackbottomPane.setAlignment(Pos.CENTER_LEFT);
        
        HBox test = new HBox();
        test.setMinSize(1200, 45);
        test.getChildren().add(uTextNow);
        stackbottomPane.getChildren().add(test);
        
        bottomPane.setTop(stackbottomPane);
        
        StackPane stackbottomPaneL = new StackPane();
        stackbottomPaneL.setId("stackbottomPaneLR");
        stackbottomPaneL.setMaxSize(300, 200);
        stackbottomPaneL.setMinSize(300, 200);
        stackbottomPaneL.setAlignment(Pos.CENTER);
        stackbottomPaneL.getChildren().add(uTextNow1);
        
        bottomPane.setLeft(stackbottomPaneL);
        
        StackPane stackbottomPaneC = new StackPane();
        stackbottomPaneC.setMaxSize(599, 200);
        stackbottomPaneC.setMinSize(599, 200);
        stackbottomPaneC.setAlignment(Pos.CENTER);
        stackbottomPaneC.getChildren().add(uTextNow2);
        
        bottomPane.setCenter(stackbottomPaneC);
                
        StackPane stackbottomPaneR = new StackPane();
        stackbottomPaneR.setId("stackbottomPaneLR");
        stackbottomPaneR.setMaxSize(300, 200);
        stackbottomPaneR.setMinSize(300, 200);
        stackbottomPaneR.setAlignment(Pos.CENTER);
        stackbottomPaneR.getChildren().add(uTextNow3);
        
        bottomPane.setRight(stackbottomPaneR);
        
//        StackPane stackbottomPaneB = new StackPane();
//        stackbottomPaneB.setId("stackbottomPane");
//        stackbottomPaneB.setMaxSize(1200, 45);
//        stackbottomPaneB.setMinSize(1200, 45);
//        stackbottomPaneB.setAlignment(Pos.CENTER);
//      
//        bottomPane.setBottom(stackbottomPaneB);
        
    }

}
