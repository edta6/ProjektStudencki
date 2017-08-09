/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testowa;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Andrzej Pawlik
 */
public class DataWypis {
    
    private final SimpleIntegerProperty userId; 
    private final SimpleStringProperty userName;
    private final SimpleStringProperty target;
    private final SimpleStringProperty dataExit;
    private final SimpleStringProperty timeExit;
    private final SimpleStringProperty adress;
    private final SimpleStringProperty datacameBack;
    private final SimpleStringProperty timecameBack;
    private final SimpleStringProperty comments;

    public DataWypis(Integer lp, String name, String ftarget, String dataE,
                      String timeE, String fadress, String dataB, String timeB,
                      String fcomments
                    ){
       this.userId = new SimpleIntegerProperty(lp);
       this.userName = new SimpleStringProperty(name);
       this.target = new SimpleStringProperty(ftarget);
       this.dataExit = new SimpleStringProperty(dataE);
       this.timeExit = new SimpleStringProperty(timeE);
       this.adress =  new SimpleStringProperty(fadress);
       this.datacameBack = new SimpleStringProperty(dataB);
       this.timecameBack = new SimpleStringProperty(timeB);
       this.comments = new SimpleStringProperty(fcomments);
    }
    
    public Integer getUserId() {
        return userId.get();
    }

    public String getUserName() {
        return userName.get();
    }
    
    public String getTarget() {
        return target.get();
    }
    
    public String getDataExit() {
        return dataExit.get();
    }
    
    public String getTimeExit() {
        return timeExit.get();
    }

    public String getAdress() {
        return adress.get();
    }

    public String getDatacameBack() {
        return datacameBack.get();
    }
     
    public String getTimecameBack() {
        return timecameBack.get();
    }    
    
    public String getComments() {
        return comments.get();
    }        
}
