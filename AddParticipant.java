/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testowa;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import javafx.geometry.Pos;
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
 * @author Andrzej Pawlik 
 */
public class AddParticipant extends Stage { //klasa okienka dodawania uczestnikow
    
    DB db;
    Statement st;
    
    private final Scene SceneParticipant;
    private final BorderPane borderPane;
    private GridPane gridAdd, gridUpdate;
    private StackPane stack;
    private HBox hbGridAddTitle, hbuttonMenu, hbGridAddParButton, hbGridUpdateComBoxPar;
    private HBox hbGridEditParButton;
    private VBox Main;
    private RestrictiveTextField NamePar, LastNamePar;
    private Button addParButton, editParButton, vievButton, editButton, addButton, closeButton;
    private Button pdfButton;
    private ComboBox participant;
    private CheckBox active; 
    private Text gridAddTitle; 
    private Label Year, UserNumber, lNamePar, lLastNamePar, lParticipant, lViev, lTextAction;
    private EventHandler key, keyUpdate;
    private ObservableList<ParticipantData> data;
    int userID;
    int IdPart;
    int ItemId;
    int activeStatus;
    
    /**
     * Konstruktor bezargumentowy, tworzący okienko.
     * Dwie funkcje preprareScene() oraz createItem() zawierają wszystkie elemety.
     * 
     */
    public AddParticipant() {
        
       prepareScene();
       createItem();
       prepareViev();
       
       borderPane = new BorderPane();
       borderPane.setTop(hbuttonMenu);
       borderPane.setCenter(Main);
        
       SceneParticipant = new Scene(borderPane, 390, 400);
       
       SceneParticipant.getStylesheets().add(Testowa.class.getResource("AddParticipant.css").toExternalForm());
       setScene(SceneParticipant);
       setTitle("Add Participant");    
    }
    /*
    * klasa tworzaca wszystkie polskie znaki
    */
    private void createItem() {
        
        gridAddTitle = new Text();
        gridAddTitle.setId("gridAddTitle");
        hbGridAddTitle = new HBox();
        hbGridAddTitle.setId("hbGridAddTitle");
        hbGridAddTitle.getChildren().add(gridAddTitle);
         
        Year = new Label();
        Year.setId("YearUserNumber");
        UserNumber = new Label();
        UserNumber.setId("YearUserNumber");
        
        lNamePar = new Label("Imię: ");
        lNamePar.setId("lNamePar");
        
        NamePar = new RestrictiveTextField();
        NamePar.setMaxLength(15);
        NamePar.setRestrict("[a-zA-ZąćęłńóśźżĄĘŁŃÓŚŹŻ]");
        
        lLastNamePar = new Label("Nazwisko: ");
        lLastNamePar.setId("lNamePar");
        
        LastNamePar = new RestrictiveTextField(); //klasa tworzaca polskie znaki bez liczb
        LastNamePar.setMaxLength(20);
        LastNamePar.setRestrict("[a-zA-ZąćęłńóśźżĄĘŁŃÓŚŹŻ]");
        
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
    
    /**
     *  Funkcja prepareGridPaneAdd przygotowuje okno, które 
     *  pozwala dodać osobę do bazy danych, ma w sobie
     *  Listener, który blokuje dodanie samego nazwiska lub imienia,
     *  można dodać osobę za pomocą przycisku addParButton lub poprzez 
     *  naciśnięcie ENTER.
     * 
     *  Głównym zarządcą jest GridPane, w którym są umieszczone wszystkie 
     *  elementy. 
     *  GridPane jest dzieckiem BorderPane umieszczonym 
     *  w Centrum BorderPane.
     */
    private void prepareGridPaneAdd(){
        
        gridAdd = new GridPane();
        gridAdd.setId("gridAdd");
        
        gridAddTitle.setText("Dodaj Uczestnika");
        gridAdd.add(hbGridAddTitle, 0, 0, 2, 1); 
        
        gridAdd.add(Year, 0, 1);
        gridAdd.add(UserNumber, 1, 1);
           
        gridAdd.add(lNamePar, 0, 2);       
        gridAdd.add(NamePar, 1, 2);
         
        NamePar.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
               
                bodyListenerTextField(t1, LastNamePar.getText());
                              
             }
        }); 
       
        gridAdd.add(lLastNamePar, 0, 3);
        gridAdd.add(LastNamePar, 1, 3);
        
        LastNamePar.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
               
                bodyListenerTextField(t1, NamePar.getText());
                   
             }
        }); 
        
        active.setText("Aktywny");
        active.setSelected(true);
        gridAdd.add(active, 1, 4);
        
        addParButton = new Button("Potwierdź");
        addParButton.setMinWidth(100);
        addParButton.setId("addParButton");
        addParButton.setDisable(true);
        
        hbGridAddParButton = new HBox();
        hbGridAddParButton.setId("hbGridAddTitle");
        hbGridAddParButton.getChildren().add(addParButton);
        gridAdd.add(hbGridAddParButton, 0, 5, 2, 1);
                
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
        
        String addParticipant = "Insert Into participants (id_part, first_name, last_name, active)"
                          + " Values (" + userID + ",'" + NamePar.getText() + "','" 
                          + LastNamePar.getText() + "'," + activeStatus + ")";
        
        String addExitReturn = "Insert Into exitreturn (id_part, exit_return)"
                             + " Values (" + userID + ",0)";
        
        sqlQuery(addParticipant);
        sqlQuery(addExitReturn);
        addParticipantData();
        userID = getUserId();
        refresh(userID);
        NamePar.clear();
        LastNamePar.clear();                
    }
    
    private void bodyListenerTextField(String t1, String ex){
        if(t1.equals("") || ex.equals("")) 
            addParButton.setDisable(true);
        else {
            addParButton.setDisable(false);
            LastNamePar.setOnKeyPressed(key);
            NamePar.setOnKeyPressed(key);
        }         
    }
    
    private void prepareGridPaneUpdate() {
                
        gridUpdate = new GridPane();
        gridUpdate.setId("gridAdd");
        
        gridAddTitle.setText("Zmień Dane Uczestnika");
        gridUpdate.add(hbGridAddTitle, 0, 0, 2, 1);
        
        participant = new ComboBox(data);
        participant.setMinWidth(200);
        
        comboItemRefresh();
        
        participant.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ParticipantData>() {           
            @Override
            public void changed(ObservableValue<? extends ParticipantData> ov, ParticipantData t, ParticipantData t1) {
                comboRefresh(); 
            }
        }); 
        
        NamePar.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
               
                bodyListenerEditParButtonandKey(t1, LastNamePar.getText());                  
            }
        }); 
        
        LastNamePar.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                
                bodyListenerEditParButtonandKey(t1, NamePar.getText());
            }
        });
        
        lParticipant = new Label("Wybierz: ");
        lParticipant.setId("lNamePar");
        hbGridUpdateComBoxPar = new HBox();
        hbGridUpdateComBoxPar.setId("hbGridAddTitle");
        hbGridUpdateComBoxPar.getChildren().addAll(lParticipant, participant);
        gridUpdate.add(hbGridUpdateComBoxPar, 0, 1, 2, 1);       
        gridUpdate.add(Year, 0, 2);
        Year.setText("Rok:");
        gridUpdate.add(UserNumber, 1, 2);   
        UserNumber.setText("Numer:");
        gridUpdate.add(lNamePar, 0, 3);        
        gridUpdate.add(NamePar, 1, 3);
        
        stack = new StackPane();
        stack.getChildren().add(lLastNamePar);
        stack.setMinWidth(130);
        stack.setAlignment(Pos.TOP_LEFT);
        gridUpdate.add(stack, 0, 4);
        gridUpdate.add(LastNamePar, 1, 4);        
        gridUpdate.add(active, 1, 5);
        
        editParButton = new Button("Zmień dane Uczestnika");
        editParButton.setId("addParButton");
        editParButton.setDisable(true);
        hbGridEditParButton = new HBox();
        hbGridEditParButton.setId("hbGridAddTitle");
        hbGridEditParButton.getChildren().add(editParButton);
        gridUpdate.add(hbGridEditParButton, 0, 6, 2, 1);
        
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
            
        String changeParticipant = "UPDATE participants SET"
                          +  " first_name = '" + NamePar.getText() + "'," 
                          +  " last_name = '" + LastNamePar.getText() + "',"
                          +  " active = " + activeStatus 
                          +  " WHERE id_part = " + IdPart;
        
        sqlQuery(changeParticipant);
        participant.getSelectionModel().clearSelection();
        comboItemRefresh();
    }
    
    private void bodyListenerEditParButtonandKey (String t1, String ex){
        if(t1.equals("") || ex.equals("")) 
            editParButton.setDisable(true);
        else {
            editParButton.setDisable(false);
            LastNamePar.setOnKeyPressed(keyUpdate);
            NamePar.setOnKeyPressed(keyUpdate);
            }         
    }
    
    private void comboItemRefresh() {
      
        addParticipantData();

        for (ParticipantData data1 : data) {
            participant.setItems(data);
        }  
    }
    
    private void comboRefresh() {
     
        ItemId = participant.getSelectionModel().getSelectedIndex();
        
        if(ItemId==-1){
            NamePar.clear();
            LastNamePar.clear();
            editParButton.setDisable(true);
            Year.setText("Rok:");
            UserNumber.setText("Numer:");  
        }
        else {
        
            IdPart = data.get(ItemId).id_part;
        
            refresh(IdPart);
        
            NamePar.setText(data.get(ItemId).first_name);
            LastNamePar.setText(data.get(ItemId).last_name);
        
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
            }
        });
        
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            userID = getUserId();
            refresh(userID);
            NamePar.clear();
            LastNamePar.clear();
            prepareGridPaneAdd();    
            borderPane.setCenter(gridAdd);
            }
        });
       
        editButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {  
            NamePar.clear();
            LastNamePar.clear();    
            prepareGridPaneUpdate();    
            borderPane.setCenter(gridUpdate);
            }
        });
        
        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            close();
            }
        });                       
    }
    
    private void prepareViev() {
       
        Main = new VBox();
        Main.setId("vbpane");
        pdfButton = new Button("Generuj raport uczestników");
        pdfButton.setId("windows7-default");
        pdfButton.setMinWidth(320);
        
        String description = "Panel w którym dodajemy:\n- uczestników OHP,"
                           + " \n- zmieniamy dane uczestnika (imię, nazwisko) oraz jego status:"
                           + " \n- aktywny oznacza, iż uczestnik jest zaewidencjonowany,"
                           + " \n- nieaktywny oznacza, iż uczestnik odszedł z OHP.\n\n"
                           + " Jest też możliwość wygenerowania raportu w postaci pliku PDF,"
                           + " który zostanie zapisany na Pulpicie.";
         
        lViev = new Label();
        lViev.setText(description);
        lViev.setWrapText(true);
        lViev.setId("lViev");
        
        lTextAction = new Label();
        lTextAction.setWrapText(true);
        lTextAction.setId("lViev");
        
        pdfButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            
                String path1 = System.getProperty("user.home") + "/Desktop/Raport_uczestnikow.pdf";
                String DEST = path1.replace("\\", "/");
                String text = "Raport został zapisany na Pulpicie plik Raport_uczestnikow.pdf";
                int flaga=1;
               
                try {
                    createPdf(DEST);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(AddParticipant.class.getName()).log(Level.SEVERE, null, ex);
                    flaga = 0;
                    text = ex.getMessage();  
                }
                
                if(flaga==0) {
                   lTextAction.setText(text); 
                }
                else {
                    lTextAction.setText(text); 
                }
                
            }
        });
       
       Main.getChildren().addAll(lViev, pdfButton, lTextAction);     
    }
    
    private void createPdf(String dest) throws FileNotFoundException  {
        
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);
        try (Document document = new Document(pdf)) {
            
            PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN, "CP1250", true);

            document.setFont(font);
            document.add(new Paragraph("Wykaz uczestników dodanych do bazy"));
            Table table = new Table(4);
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add("Numer"));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add("Imię"));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add("Nazwisko"));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add("Aktywny"));
            for (ParticipantData data1 : data) {
                int numer = data1.id_part;
                int yearUserID1 = numer/10000;
                int numberUserID2=(numer/10)-(yearUserID1*1000);
                table.addCell(Integer.toString(numberUserID2));
                table.addCell(data1.first_name);
                table.addCell(data1.last_name);
                if(data1.active==0)  table.addCell("TAK");
                else table.addCell("NIE");
        }
            document.add(table);
    }   catch (IOException ex) {
            Logger.getLogger(AddParticipant.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    public void refresh(int userId) {
         
        int yearUserID = userId/10000;
        int numberUserID=(userId/10)-(yearUserID*1000); 
          
        Year.setText("Rok: " + Integer.toString(yearUserID));
        UserNumber.setText("Numer: " + Integer.toString(numberUserID));        
    }
    
    public int getUserId() {
        
        int result;
        
        if(data.size()== 0) result = 0; 
        else  result = data.get(data.size()-1).id_part;
        GeneratorUserId userId = new GeneratorUserId(result);
        return userId.getUserId();        
    }

    private void sqlQuery(String query) {
        
        try {
            st = db.con.createStatement();
            st.executeUpdate(query);
            st.close();
        } catch (SQLException ex) {
          
        }
    }
    
    public void addParticipantData() {                                              
  
        data = FXCollections.observableArrayList();
            
        try {
        
            st = db.con.createStatement();
            
            String query = "SELECT id_part, first_name, last_name, active FROM participants";
      
            ResultSet rs = st.executeQuery(query);
        
            while (rs.next()){
                ParticipantData unit = new ParticipantData(
                        rs.getInt("id_part"), 
                        rs.getString("first_name"),
                        rs.getString("last_name"),
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
