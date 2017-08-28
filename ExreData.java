/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testowa;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author xxx
 */
public class ExreData {
    
    private final SimpleIntegerProperty id_exre;
    String Par_first_name;
    String Par_last_name;
    String Target_name;
    String Exit_date;
    String place;
    String comm;
    String User_first_name;
    String User_last_name;
    SimpleStringProperty fullnamePar;
    
    public ExreData ( Integer id, String pfn, String pln, String tn,
                      String ed, String p, String c, String ufn, String uln) {
        this.id_exre= new SimpleIntegerProperty(id);
        this.Par_first_name= pfn;
        this.Par_last_name= pln;
        this.Target_name=tn;
        this.Exit_date=ed;
        this.place=p;
        this.comm=c;
        this.User_first_name=ufn;
        this.User_last_name=uln;   
    }
    
    @Override
    public String toString() {
        
        String fullName = Par_first_name + " " + Par_last_name;
        
        return fullName;
    }
    
    public Integer getId() {
        return id_exre.get();
    }
    
//    public String getName() {
//        return Par_first_name.get();
//    }
    
    public final StringProperty FullNameParProperty() { 
        
        fullnamePar = new SimpleStringProperty(Par_first_name + " " + Par_last_name);
        
        return fullnamePar; 
    }
            
    
    
    
}
