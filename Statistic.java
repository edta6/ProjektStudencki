/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testowa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author xxx
 */
public class Statistic {
    
    DB db;
    Statement st;
    MainWindow window;
    
    int stanosobowy;
    int obecnych;
    int wypisanych;
    
    public Statistic() {}
    
    public void getDataSta() {
        stanosobowy = sqlResult("select count(id_part) from participants where active=0;");
        obecnych = sqlResult("select count(p.id_part) from participants p join exitreturn w on p.id_part=w.id_part where p.active=0 and w.exit_return=0;");
        wypisanych = sqlResult("select count(p.id_part) from participants p join exitreturn w on p.id_part=w.id_part where p.active=0 and w.exit_return=1;");
    }
    
    public Integer sqlResult(String query){
        
        int numberRow=0;
        
        try {
           
            st = db.con.createStatement();
            
            ResultSet rs = st.executeQuery(query);
            
            while(rs.next()) {
                numberRow = rs.getInt(1);
            }
            st.close();
        }    
        catch (SQLException ex) {} 
        
        return numberRow;    
    }
    
    public String getStanOsobowy() {
        
        return Integer.toString(stanosobowy);
        
    }
    
    public String getObecnych() {
        
        return Integer.toString(obecnych);
        
    }
    
    public String getWypisanych() {
        
        return Integer.toString(wypisanych);
        
    }
      
}