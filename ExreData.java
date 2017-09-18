/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OHP;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author xxx
 */
public class ExreData {
    
    public final SimpleIntegerProperty id_exre;
    String Par_first_name;
    String Par_last_name;
    String Target_name;
    String Exit_date;
    String Return_date;
    String place;
    String comm;
    String User_first_name;
    String User_last_name;
    
    public ExreData ( Integer id, String pfn, String pln, String tn,
                      String ed, String rd, String p, String c, String ufn, String uln) {
        this.id_exre= new SimpleIntegerProperty(id);
        this.Par_first_name= pfn;
        this.Par_last_name= pln;
        this.Target_name=tn;
        this.Exit_date=ed;
        this.Return_date=rd;
        this.place=p;
        this.comm=c;
        this.User_first_name=ufn;
        this.User_last_name=uln;   
    }
    
    public IntegerProperty id_exreProperty() {
        return id_exre;
    }
        
    public StringProperty FullNameParProperty() { 
        
       SimpleStringProperty fullnamePar = new SimpleStringProperty(Par_last_name + " " + Par_first_name);
        
        return fullnamePar; 
    }
    
    public StringProperty TargetProperty() { 
        
       SimpleStringProperty target = new SimpleStringProperty(Target_name);
        
        return target; 
    }
    
    public StringProperty DateExProperty() { 
       
        String Date = Exit_date.substring(0,10);
        
        SimpleStringProperty DateEx = new SimpleStringProperty(Date);
        
        return DateEx; 
    }
    
    public StringProperty HourExProperty() { 
       
        String Hour = Exit_date.substring(11,16);
        
        SimpleStringProperty HourEx = new SimpleStringProperty(Hour);
        
        return HourEx; 
    }
    
    public StringProperty DateReProperty() { 
       
        String Date; 
        
        if(Return_date.equals("")) Date = ("");
        else Date = Return_date.substring(0,10);
        
        SimpleStringProperty DateRe = new SimpleStringProperty(Date);
        
        return DateRe; 
    }
    
    public StringProperty HourReProperty() { 
       
        String Hour;
        
        if(Return_date.equals("")) Hour = ("");
        else Hour = Return_date.substring(11,16);
        
        SimpleStringProperty HourRe = new SimpleStringProperty(Hour);
        
        return HourRe; 
    }
    
    public StringProperty PlaceProperty() { 
        
        SimpleStringProperty Place = new SimpleStringProperty(place);
        
        return Place; 
    }
    
    public StringProperty CommProperty() { 
        
        SimpleStringProperty Comm = new SimpleStringProperty(comm);
        
        return Comm; 
    }
    
}
