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
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Andrzej Pawlik
 */
public class MainWindow extends Stage {

    DB db;
    Statement st;
    MainWindow window;
    MainWindowSQL windowSQL;
    
    private Scene sceneMainWindow;
    private BorderPane borderPane, bottomPane;
    private StackPane stackTable, mainStack;
    public HBox hbuttonMenu, hbnameLogin;
    public Button buttonExit, buttonHome, buttonExitBig, buttonHomeBig; 
    private Button buttonAdmin, buttonClose;
    private Label uTextNow, Zdarzenie;
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
                buttonExitBig.setDisable(true);
                buttonHomeBig.setDisable(true);  
            } 
            else {
                buttonExit.setDisable(false);
                buttonHome.setDisable(false);
                buttonExitBig.setDisable(false);
                buttonHomeBig.setDisable(false); 
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
        borderPane.setTop(hbuttonMenu);
        
        prepareBorderPaneCenter();
        borderPane.setCenter(stackTable);
        
        prepareBorderPaneBottom();
        borderPane.setBottom(mainStack);
        
        sceneMainWindow = new Scene(borderPane, 1190, 640);
        sceneMainWindow.getStylesheets().add(Testowa.class.getResource("MainWindow.css").toExternalForm());
        setScene(sceneMainWindow);
        setTitle("The book out and trips");
    }
    
    private void prepareBorderPaneTop () { //pierwszy wiersz w oknie, definicja guzikow i ich zachowan
    
        buttonExit = new Button("Wypisy");
        buttonExit.setId("windows7");
        buttonExit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                wypisAction();
            }
        });
        
        buttonHome = new Button("Powroty");
        buttonHome.setId("windows7");
        buttonHome.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                odpiszAction();
            }
        });
        
        buttonAdmin = new Button("Panel Administratora");
        buttonAdmin.setId("windows7");
        buttonAdmin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AdminPane panelAdmin = new AdminPane();
                panelAdmin.db = db;
                panelAdmin.resizableProperty().setValue(Boolean.FALSE);
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
        
        Label pomoc = new Label(" ");
        StackPane enterMenuStack = new StackPane();
        enterMenuStack.getChildren().add(pomoc);
        enterMenuStack.setAlignment(Pos.CENTER_RIGHT);
        
        hbuttonMenu = new HBox();
        hbuttonMenu.setId("hbuttonMenu");
        hbuttonMenu.setAlignment(Pos.CENTER_LEFT);
        hbuttonMenu.setMinWidth(1200);
        hbuttonMenu.setMaxWidth(1200);
        hbuttonMenu.getChildren().addAll(buttonExit, buttonHome, buttonAdmin, enterMenuStack, hbnameLogin, buttonClose);
        HBox.setHgrow(enterMenuStack, Priority.ALWAYS); 
    }
    
    public void refreshTableData(){
       
        data = FXCollections.observableArrayList();
        
        try {
            
            st = db.con.createStatement();
            
            String query = "Select m.id_exre, p.first_name, p.last_name, t.target_name, m.exit_date, "
                    + "m.place, m.comm, u.first_name, u.last_name from main_exre m join participants p "
                    + "on m.id_part=p.id_part join targets t on m.id_target=t.id_target join user_ohp u "
                    + "on m.id_user_exit=u.id_part where m.return_date is NULL;";
                     
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
//        table.setMaxSize(1198, 300);
//        table.setMinSize(1198, 300);
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
        stackTable.setMinWidth(1198);
        stackTable.setMaxWidth(1198);
        stackTable.getChildren().add(table);
    }
    
    private void prepareBorderPaneBottom(){
        
        bottomPane = new BorderPane();
        bottomPane.setId("bottomPane");
        
        mainStack = new StackPane();
//        mainStack.setMaxSize(1200, 250);
//        mainStack.setMinSize(1200, 250);
        mainStack.getChildren().add(bottomPane);

        Zdarzenie = new Label();
        Zdarzenie.setId("lOpisMsgBox1-Black2");
        
        uTextNow = new Label("Ostatnie zdarzenie:");
        uTextNow.setId("lOpisMsgBox1-Black");
         
        HBox topBox = new HBox();
        topBox.setId("hboxMsgBox3");
        topBox.setAlignment(Pos.CENTER);
        topBox.setMinSize(1200, 43);
        topBox.setMaxSize(1200, 43);
        topBox.getChildren().addAll(uTextNow, Zdarzenie);
       
        bottomPane.setTop(topBox);
        
        buttonExitBig = new Button("Wypisy");
        buttonExitBig.setId("windows7");
        buttonExitBig.setMaxSize(180, 75);
        buttonExitBig.setMinSize(180, 75);
        buttonExitBig.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                wypisAction();
            }
        });
        
        buttonHomeBig = new Button("Powroty");
        buttonHomeBig.setId("windows7");
        buttonHomeBig.setMaxSize(180, 75);
        buttonHomeBig.setMinSize(180, 75);
        buttonHomeBig.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                odpiszAction();
            }
        });
        
        VBox stackbottomPaneL = new VBox();
        stackbottomPaneL.setId("stackbottomPaneL");
        stackbottomPaneL.setMaxSize(260, 210);
        stackbottomPaneL.setMinSize(260, 210);
        stackbottomPaneL.getChildren().addAll(buttonExitBig, buttonHomeBig);
        
        bottomPane.setLeft(stackbottomPaneL);
        
        Text scenetitle = new Text("Statystyki");
        scenetitle.setId("gridAddTitle");
        HBox hbSceneTitle = new HBox();
        hbSceneTitle.setId("hbGridAddTitle");
        hbSceneTitle.getChildren().add(scenetitle);
        
        Label one = new Label("Stan osobowy:");
        one.setId("lOpisMsgBox1-Black");
        Label two = new Label("0");
        two.setId("lOpisMsgBox1-Red");
        HBox one_two = new HBox();
        one_two.setAlignment(Pos.CENTER_RIGHT);
        one_two.setId("hboxMsgBox2");
        one_two.getChildren().addAll(one, two);
        
        Label three = new Label("Wypisanych: ");
        three.setId("lOpisMsgBox1-Black");
        Label four = new Label("0");
        four.setId("lOpisMsgBox1-Red");
        HBox three_four = new HBox();
        three_four.setAlignment(Pos.CENTER_RIGHT);
        three_four.setId("hboxMsgBox2");
        three_four.getChildren().addAll(three, four);
        
        Label five = new Label("Obecnych:");
        five.setId("lOpisMsgBox1-Black");
        Label six = new Label("0");
        six.setId("lOpisMsgBox1-Red");
        HBox five_six = new HBox();
        five_six.setAlignment(Pos.CENTER_RIGHT);
        five_six.setId("hboxMsgBox2");
        five_six.getChildren().addAll(five, six);
        
        VBox stackbottomPaneR = new VBox();
        stackbottomPaneR.setId("stackbottomPaneR");
        stackbottomPaneR.setMaxSize(260, 210);
        stackbottomPaneR.setMinSize(260, 210);
        stackbottomPaneR.getChildren().addAll(hbSceneTitle, one_two, three_four, five_six);
        
        bottomPane.setRight(stackbottomPaneR);
        
      
        
        Label uTextNow2 = new Label("Dół do zrobienia!");

        StackPane stackbottomPaneC = new StackPane();
        stackbottomPaneC.setMaxSize(599, 200);
        stackbottomPaneC.setMinSize(599, 200);
        stackbottomPaneC.setAlignment(Pos.CENTER);
        stackbottomPaneC.getChildren().add(uTextNow2);
        
        bottomPane.setCenter(stackbottomPaneC);
                           
    }
    
    public void wypisAction() {
        buttonExit.setDisable(true);
        buttonExitBig.setDisable(true);
        Subscribe wypisz = new Subscribe();
        wypisz.db = db;
        wypisz.userId = id_user;
        wypisz.window = window;
        wypisz.wypisz = wypisz;
        wypisz.resizableProperty().setValue(Boolean.FALSE);
        wypisz.refreshCombo();
        wypisz.refreshComboTar();
        wypisz.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(final WindowEvent event) {
                buttonExit.setDisable(false);
                buttonExitBig.setDisable(false); 
            }
        });
        wypisz.show();    
    }
    
    public void odpiszAction() {
        buttonHome.setDisable(true);
        buttonHomeBig.setDisable(true);
        DeSubscribe odpisz = new DeSubscribe();
        odpisz.db = db;
        odpisz.userId = id_user;
        odpisz.window = window;
        odpisz.odpisz = odpisz;
        odpisz.refreshCombo();
        odpisz.resizableProperty().setValue(Boolean.FALSE);
        odpisz.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(final WindowEvent event) {
                buttonHome.setDisable(false);
                buttonHomeBig.setDisable(false); 
            }
        });
        odpisz.show();    
    }
    
    public void setZdarzenie(String a) {
        Zdarzenie.setText(a);    
    }
    
}
