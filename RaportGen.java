/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OHP;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author xxx
 */
public class RaportGen extends Stage {
    
    DB db;
    Statement st;
    MainWindow window;
    
    private final Scene SceneParticipant;
    private BorderPane borderPane, borderPaneBody;
    private StackPane stack;
    private HBox hbGridAddTitle, hbuttonMenu;
    private VBox Main, borderPaneBodyVboxTop, partVbox, timeVbox;
    private Button vievButton, editButton, closeButton;
    private Button pdfButtonPar, pdfButtonTime;
    private ComboBox participant;
    private RadioButton participantCheckBox, timeCheckBox; 
    private Text gridAddTitle; 
    private Label lViev, lTextAction;
    private ObservableList<ParticipantData> dataComboPar;
    private ObservableList<ExreData> data;
    int ItemIdPart=-1;
    
    public RaportGen(){
       
       prepareScene();
       prepareViev();
       
       borderPane = new BorderPane();
       borderPane.setTop(hbuttonMenu);
       borderPane.setCenter(Main);
       
       SceneParticipant = new Scene(borderPane, 390, 470);
       
       SceneParticipant.getStylesheets().add(Testowa.class.getResource("AddParticipant.css").toExternalForm());
       setScene(SceneParticipant);
       setTitle("Generator Raportów");
       
    }
    
    private void prepareScene(){
       
        vievButton = new Button("Opis");
        vievButton.setMinWidth(100);
        vievButton.setId("windows7-default");
                
        editButton = new Button("Generator Raportow");
        editButton.setMinWidth(200);
        editButton.setId("windows7-default");
        
        closeButton = new Button("Zamknij");
        closeButton.setMinWidth(100);
        closeButton.setId("windows7-default");
       
        hbuttonMenu = new HBox();
        hbuttonMenu.setId("hbuttonMenu");
        hbuttonMenu.getChildren().addAll(vievButton, editButton, closeButton);
        
        vievButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                borderPane.setCenter(Main);
            }
        });
               
        editButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                prepareBodyGen();
                borderPane.setCenter(borderPaneBody);
            }
        });
        
        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                close();
                window.generetRaport.setDisable(false);
             }
        });
        
        stack = new StackPane();
        stack.setId("partVbox");
        
        preparePartGen();
        prepareTimeGen();
    }
    
    private void prepareBodyGen() {
        
        borderPaneBody = new BorderPane();
        
        borderPaneBodyVboxTop = new VBox();
        borderPaneBodyVboxTop.setId("borderPaneBodyVboxTop");
        
        gridAddTitle = new Text("Wybierz rodzaj Raportu");
        gridAddTitle.setId("gridAddTitle");
        hbGridAddTitle = new HBox();
        hbGridAddTitle.setId("hbGridAddTitle");
        hbGridAddTitle.getChildren().add(gridAddTitle);
        
        participantCheckBox = new RadioButton("uczestnik");
        participantCheckBox.setId("lNamePar");
        
        participantCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue ov,Boolean old_val, Boolean new_val) {
                        
                if(participantCheckBox.isSelected()){
                    participant.setItems(dataComboPar);
                    borderPaneBody.setCenter(partVbox);
                    timeCheckBox.setSelected(false);
                }
                else if(timeCheckBox.isSelected()) {
                    borderPaneBody.setCenter(timeVbox);
                    participantCheckBox.setSelected(false);
                    participant.getSelectionModel().clearSelection();
                }
                else{
                    borderPaneBody.setCenter(stack);
                    participant.getSelectionModel().clearSelection();
                }
                    
            }
        }); 
        
        timeCheckBox = new RadioButton("okres czasu");
        timeCheckBox.setId("lNamePar");
        
        timeCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue ov,Boolean old_val, Boolean new_val) {
                        
                if(timeCheckBox.isSelected()){
                    borderPaneBody.setCenter(timeVbox);
                    participantCheckBox.setSelected(false);
                    participant.getSelectionModel().clearSelection();
                }
                else if(participantCheckBox.isSelected()) {
                    participant.setItems(dataComboPar);
                    borderPaneBody.setCenter(partVbox);
                    timeCheckBox.setSelected(false);
                }
                else {
                    borderPaneBody.setCenter(stack);
                    participant.getSelectionModel().clearSelection();
                }
            }
        }); 
        
        borderPaneBodyVboxTop.getChildren().addAll(hbGridAddTitle, participantCheckBox, timeCheckBox);
        borderPaneBody.setTop(borderPaneBodyVboxTop);
        borderPaneBody.setCenter(stack);
        
    }
    
    private void prepareViev() {
       
        Main = new VBox();
        Main.setId("vbpane");
        
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
         
        Main.getChildren().addAll(lViev);     
    }
    
    private void preparePartGen() {
        
        partVbox = new VBox();
        partVbox.setId("partVbox");
        
        Text title = new Text("Wybierz uczestnika:");
        title.setId("gridAddTitle");

        participant = new ComboBox();
        participant.setMinWidth(200);
        participant.setMaxWidth(200);
        
        participant.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ParticipantData>() {           
            @Override
            public void changed(ObservableValue<? extends ParticipantData> ov, ParticipantData t, ParticipantData t1) {
                
                ItemIdPart = participant.getSelectionModel().getSelectedIndex();
                
                if(ItemIdPart==-1)
                    pdfButtonPar.setDisable(true);
                else
                   pdfButtonPar.setDisable(false); 
            }
        }); 
        
        pdfButtonPar = new Button("Generuj");
        pdfButtonPar.setId("windows7-default");
        pdfButtonPar.setMinWidth(200);
        pdfButtonPar.setDisable(true);
        
        partVbox.getChildren().addAll(title, participant, pdfButtonPar);
        
        pdfButtonPar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
                refreshTableDataConfirm();
                Date name = new Date();
                SimpleDateFormat formatter_three = new SimpleDateFormat("yyyy_MM_dd_HH_mm");
                String resultDate_three = formatter_three.format(name);
                
                String path1 = System.getProperty("user.home") + "/Desktop/Raport_" + resultDate_three + ".pdf";
                String DEST = path1.replace("\\", "/");

                try {
                    createPdf(DEST);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(AddParticipant.class.getName()).log(Level.SEVERE, null, ex);
  
                }
                
                data.clear();
             }
        });   
        
    }
    
    private void prepareTimeGen() {
        
        timeVbox = new VBox();
        timeVbox.setId("partVbox");
        
        Text title_two = new Text("Wybierz datę początkową:");
        title_two.setId("gridAddTitle");
        
        Text title_three = new Text("Wybierz datę końcową:");
        title_three.setId("gridAddTitle");
        
        pdfButtonTime = new Button("Generuj");
        pdfButtonTime.setId("windows7-default");
        pdfButtonTime.setMinWidth(200);
        
        timeVbox.getChildren().addAll(title_two, title_three, pdfButtonTime);
        
        pdfButtonTime.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                close();
             }
        }); 
          
    }
    
    public void addParticipantData() {                                              
  
        dataComboPar = FXCollections.observableArrayList();
            
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
                dataComboPar.add(unit);    
            }
            
            st.close();  
                     
        } catch (SQLException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }  
    
    private void createPdf(String dest) throws FileNotFoundException  {
        
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);
        PageOrientationsEventHandler eventHandler = new PageOrientationsEventHandler();
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, eventHandler);
        
        try (Document document = new Document(pdf)) {
            
            
            PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN, "CP1250", true);
            document.setFont(font);
            document.add(new Paragraph("Raport z Książki Wyjść i Wyjazdów dla uczestnika"));
            document.add(new Paragraph(data.get(2).FullNameParProperty().getValue()));
            Table table = new Table(6);
            table.addCell(new Cell(2,1).setTextAlignment(TextAlignment.CENTER).add("Lp"));
            table.addCell(new Cell(2,1).setTextAlignment(TextAlignment.CENTER).add("Cel Wyjśćia"));
            table.addCell(new Cell(1,2).setTextAlignment(TextAlignment.CENTER).add("Wyszedł"));
            table.addCell(new Cell(2,1).setTextAlignment(TextAlignment.CENTER).add("Przewidywane miejsce pobytu"));
            table.addCell(new Cell(1,2).setTextAlignment(TextAlignment.CENTER).add("Powrócił"));
            table.addCell(new Cell(1,1).setTextAlignment(TextAlignment.CENTER).add("dnia"));
            table.addCell(new Cell(1,1).setTextAlignment(TextAlignment.CENTER).add("godzina"));
            table.addCell(new Cell(1,1).setTextAlignment(TextAlignment.CENTER).add("dnia"));
            table.addCell(new Cell(1,1).setTextAlignment(TextAlignment.CENTER).add("godzina"));
//            table.addCell(new Cell(2,1).setTextAlignment(TextAlignment.CENTER).add("Uwagi"));
//            for (ExreData data1 : data) {
//                table.addCell(Integer.toString(data1.id_exre.getValue()));
//                table.addCell(data1.TargetProperty().getValue());
//          }
            document.add(table);
            eventHandler.setOrientation(SEASCAPE);
            document.close();
    }   catch (IOException ex) {
            Logger.getLogger(AddParticipant.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
   
     public void refreshTableDataConfirm(){
       
        data = FXCollections.observableArrayList();
        
        try {
            
            st = db.con.createStatement();
            
            String query = "Select m.id_exre, p.first_name, p.last_name, t.target_name, m.exit_date, "
                    + "IFNULL(m.return_date,'') as return_date, "
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
                        rs.getString("return_date"),
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
    
    public static class PageOrientationsEventHandler implements IEventHandler {
        protected PdfNumber orientation = PORTRAIT;
 
        public void setOrientation(PdfNumber orientation) {
            this.orientation = orientation;
        }
 
        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            docEvent.getPage().put(PdfName.Rotate, orientation);
        }
    }
    
    public static final PdfNumber INVERTEDPORTRAIT = new PdfNumber(180);
    public static final PdfNumber LANDSCAPE = new PdfNumber(90);
    public static final PdfNumber PORTRAIT = new PdfNumber(0);
    public static final PdfNumber SEASCAPE = new PdfNumber(270);
     
}
