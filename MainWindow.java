/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testowa;

import java.sql.ResultSet;
import java.sql.Statement;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
    private HBox hbuttonMenu, hbnameLogin; 
    private Button buttonAdmin, buttonClose, confirm;
    private Label uTextNow, Zdarzenie;
    private Label nameLogin, userText;
    public ComboBox participant;
    public Label two, four, six;
    public Button buttonExit, buttonHome, buttonExitBig, buttonHomeBig;
    private ObservableList<ParticipantData> dataComboPar;
    public  TableColumn lp;
    public  TableView<ExreData> table;
    public ObservableList<ExreData> data;
    public final String user;
    public final String nameU;
    public final int role;
    public final int id_user;
    int ItemIdPart=-1;
       
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
        borderPane.setCenter(table);
        
        prepareBorderPaneBottom();
        borderPane.setBottom(bottomPane);
        
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
                panelAdmin.window = window;
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
        hbuttonMenu.setId("rich-blue");
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
                    + "on m.id_user_exit=u.id_part where m.return_date is NULL and p.active=0;";
                     
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

        lp = new TableColumn("Lp.");
        lp.setCellValueFactory(new PropertyValueFactory("id_exre"));
        lp.setMinWidth(58);
        lp.setMaxWidth(58);
        final TableColumn  name = new TableColumn("Nazwisko i Imię");
        name.setCellValueFactory(new PropertyValueFactory("FullNamePar"));
        name.setMinWidth(200);
        name.setMaxWidth(200);
        final TableColumn target = new TableColumn("Cel wyjścia");
        target.setCellValueFactory(new PropertyValueFactory("Target"));
        target.setMinWidth(200);
        target.setMaxWidth(200);
        final TableColumn exit = new TableColumn("Wyszedł");
        exit.setMinWidth(173);
        exit.setMaxWidth(173);
        final TableColumn dataExit = new TableColumn("dnia");
        dataExit.setCellValueFactory(new PropertyValueFactory("DateEx"));
        dataExit.setMinWidth(88);
        dataExit.setMaxWidth(88);
        final TableColumn timeExit = new TableColumn("godz.");
        timeExit.setCellValueFactory(new PropertyValueFactory("HourEx"));
        timeExit.setMinWidth(85);
        timeExit.setMaxWidth(85);
        final TableColumn adres = new TableColumn("Przewidywane miejsce\nTel. kontaktowy");
        adres.setCellValueFactory(new PropertyValueFactory("Place"));
        adres.setMinWidth(210);
        adres.setMaxWidth(210);
        final TableColumn cameBack = new TableColumn("Powrócił");
        cameBack.setMinWidth(173);
        cameBack.setMaxWidth(173);
        final TableColumn datacameBack = new TableColumn("dnia");
        datacameBack.setCellValueFactory(new PropertyValueFactory("DateRe"));
        datacameBack.setMinWidth(88);
        datacameBack.setMaxWidth(88);
        final TableColumn timecameBack = new TableColumn("godz.");
        timecameBack.setCellValueFactory(new PropertyValueFactory("HourRe"));
        timecameBack.setMinWidth(85);
        timecameBack.setMaxWidth(85);
        final TableColumn comments = new TableColumn("Uwagi");
        comments.setCellValueFactory(new PropertyValueFactory("Comm"));
        comments.setMinWidth(170);
        comments.setMaxWidth(170);
        
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

    }
    
    private void prepareBorderPaneBottom(){
        
        bottomPane = new BorderPane();
        bottomPane.setId("bottomPane");
        
        Zdarzenie = new Label();
        Zdarzenie.setId("windows7Zda");
        
        uTextNow = new Label("Ostatnie zdarzenie:");
        uTextNow.setId("lOpisMsgBox1-Black");
         
        HBox topBox = new HBox();
        topBox.setId("rich-blue");
        topBox.setAlignment(Pos.CENTER);
        topBox.setMinWidth(1200);
        topBox.setMaxWidth(1200);
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
        stackbottomPaneL.getChildren().addAll(buttonExitBig, buttonHomeBig);
        
        bottomPane.setLeft(stackbottomPaneL);
        
        Label scenetitle = new Label("Statystyki");
        scenetitle.setId("gridAddTitle");
        HBox hbSceneTitle = new HBox();
        hbSceneTitle.setId("hbGridAddTitle");
        hbSceneTitle.getChildren().add(scenetitle);
        
        Label one = new Label("Stan osobowy:");
        one.setId("lOpisMsgBox1-Black1");
        two = new Label();
        two.setId("lOpisMsgBox1-Red");
        HBox one_two = new HBox();
        one_two.setAlignment(Pos.CENTER_RIGHT);
        one_two.setId("windows7Stat");
        one_two.getChildren().addAll(one, two);
        
        Label three = new Label("Wypisanych:");
        three.setId("lOpisMsgBox1-Black1");
        four = new Label();
        four.setId("lOpisMsgBox1-Red");
        HBox three_four = new HBox();
        three_four.setAlignment(Pos.CENTER_RIGHT);
        three_four.setId("windows7Stat");
        three_four.getChildren().addAll(three, four);
        
        Label five = new Label("Obecnych:");
        five.setId("lOpisMsgBox1-Black1");
        six = new Label();
        six.setId("lOpisMsgBox1-Red");
        HBox five_six = new HBox();
        five_six.setAlignment(Pos.CENTER_RIGHT);
        five_six.setId("windows7Stat");
        five_six.getChildren().addAll(five, six);
        
        VBox stackbottomPaneR = new VBox();
        stackbottomPaneR.setId("stackbottomPaneR");
        stackbottomPaneR.getChildren().addAll(hbSceneTitle, one_two, three_four, five_six);
        
        bottomPane.setRight(stackbottomPaneR);
        
        Label lParticipant = new Label("Wybierz uczestnika, aby zobaczyć wypisy:");
        lParticipant.setId("lOpisMsgBox1-Black");
        participant = new ComboBox();
        participant.setMinWidth(200);
        participant.setMaxWidth(200);
        
        confirm = new Button("Potwierdź");
        confirm.setId("windows7");
        confirm.setDisable(true);
        
        participant.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ParticipantData>() {           
            @Override
            public void changed(ObservableValue<? extends ParticipantData> ov, ParticipantData t, ParticipantData t1) {
                
                ItemIdPart = participant.getSelectionModel().getSelectedIndex();
                
                if(ItemIdPart==-1) confirm.setDisable(true);
                else confirm.setDisable(false); 
                
            }
        });
        
        confirm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               participant.getSelectionModel().clearSelection();
               refreshTableDataConfirm();
               table.setItems(data);
            }
        });
        
        HBox hbGridComBoxPar = new HBox();
        hbGridComBoxPar.setId("rich-blue");
        hbGridComBoxPar.setAlignment(Pos.CENTER);
        hbGridComBoxPar.getChildren().addAll(lParticipant, participant, confirm);
        
        VBox stackbottomPaneC = new VBox();
        stackbottomPaneC.setId("stackbottomPaneC");
        stackbottomPaneC.getChildren().add(hbGridComBoxPar);
        
        bottomPane.setCenter(stackbottomPaneC);
                           
    }
    
    public void refreshCombo(){
       
        dataComboPar = FXCollections.observableArrayList();
        
        try {
            
            st = db.con.createStatement();
            
            String sql2 = "select id_part, first_name, last_name from participants where active=0 ;";
                     
            ResultSet rs = st.executeQuery(sql2);
            
            while(rs.next()) {
                ParticipantData unit = new ParticipantData(
                        rs.getInt("id_part"), 
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        2
                );
                dataComboPar.add(unit);        
            }
            
            participant.setItems(dataComboPar);

        } catch(Exception ex) {}
    }
    
    public void wypisAction() {
        buttonExit.setDisable(true);
        buttonExitBig.setDisable(true);
        Subscribe wypisz = new Subscribe();
        wypisz.db = db;
        wypisz.userId = id_user;
        wypisz.window = window;
        wypisz.windowSQL = windowSQL;
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
        odpisz.windowSQL = windowSQL;
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
    
    public void setStan(String a) {
        two.setText(a);    
    }
    
    public void setWyp(String a) {
        four.setText(a);    
    }
    
    public void setObc(String a) {
        six.setText(a);    
    }
    
    public void refreshTableDataConfirm(){
        
        data.clear();
       
//        data = FXCollections.observableArrayList();
        
        try {
            
            st = db.con.createStatement();
            
            String query = "Select m.id_exre, p.first_name, p.last_name, t.target_name, m.exit_date, m.return_date, "
                    + "m.place, m.comm from main_exre m join participants p "
                    + "on m.id_part=p.id_part join targets t on m.id_target=t.id_target "
                    + " where p.id_part=" + dataComboPar.get(ItemIdPart).id_part + ";";
                     
            ResultSet rs = st.executeQuery(query);
            
            while(rs.next()) {
                ExreData unit = new ExreData(
                        rs.getInt("m.id_exre"), 
                        rs.getString("p.first_name"),
                        rs.getString("p.last_name"),
                        rs.getString("t.target_name"),
                        rs.getString("m.exit_date"),
                        rs.getString("m.return_date"),
                        rs.getString("m.place"),
                        rs.getString("m.comm"),
                        " ",
                        " "
                );
                data.add(unit);        
            }
            st.close();
            
        } catch(Exception ex) {}
    }
    
}

