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

/**
 *
 * @author xxx
 */
public class MainWindowSQL {
    
    Statement st;
    DB db;
    
    public MainWindowSQL() {}
    
    String exit_date   = "";
    String return_date = "";
    String date;
    int flaga;
    
    public String sqlName() {
        
        flaga = -1;
        String blad = "1900-01-01";
        String query = "select max(exit_date), max(return_date) from main_exre;";
        
        try {
           
            st = db.con.createStatement();
            
            ResultSet rs = st.executeQuery(query);
            
            while(rs.next()) {
               exit_date = rs.getString("max(exit_date)");
               return_date = rs.getString("max(return_date)");
            }
            st.close();
        }    
        catch (SQLException ex) {} 
        
        SimpleDateFormat formatter_three = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        Date exit = new Date();
        Date powrot = new Date();
        
        if(exit_date != null || return_date != null ) {
        try {
            exit = formatter_three.parse(exit_date);
            powrot = formatter_three.parse(return_date);
        } catch (ParseException ex) {}
         
        if(exit.before(powrot)) {
            flaga = 1;
            return return_date;
        }
        else {
            flaga = 0;
            return exit_date;
        }  
        }
        else{
            flaga = 0;
            return blad;
        }        
    }
    
    String p_first_name;
    String p_last_name;
    String m_exit_date;
    String m_return_date;
    String u_first_name;
    String u_last_name;
    
    public void Zdarzenie() {
        
        date = sqlName();
        String query;
        if(flaga==1) {
            query = "Select p.first_name, p.last_name, m.exit_date, m.return_date, u.first_name, "
                    + "u.last_name from main_exre m join participants p on m.id_part=p.id_part "
                    + "join user_ohp u on m.id_user_return=u.id_part where m.return_date = "
                    + "'" + date + "';";    
        }
        else {
            query = "Select p.first_name, p.last_name, m.exit_date, m.return_date, u.first_name, "
                    + "u.last_name from main_exre m join participants p on m.id_part=p.id_part "
                    + "join user_ohp u on m.id_user_exit=u.id_part where m.exit_date = "
                    + "'" + date + "';";   
        }

        try {
           
            st = db.con.createStatement();
            
            ResultSet rs = st.executeQuery(query);
            
            while(rs.next()) {
                   p_first_name = rs.getString("p.first_name");
                   p_last_name  = rs.getString("p.last_name");
                   m_exit_date  = rs.getString("m.exit_date");
                   m_return_date= rs.getString("m.return_date");
                   u_first_name = rs.getString("u.first_name");
                   u_last_name  = rs.getString("u.last_name"); 
            }
            st.close();
        }    
        catch (SQLException ex) {}   
        
    }
    
    public String getFullNamePar () {
        
        String FullNamePar = p_last_name + " " + p_first_name;
        
        return FullNamePar;        
    }
    
    public String getFullNameUsr () {
        
        String FullNameUsr = u_last_name + " " + u_first_name;
        
        return FullNameUsr;        
    }
    
    public String getDate() {
        
        String blad = "null ";
        
        if(m_exit_date != null || m_return_date != null ) {
            if(flaga==1) {
                return m_return_date.substring(0, 11);
            }
            else {
                return m_exit_date.substring(0, 11);
            }
        }
        else return blad;

    }
    
    public String getTime() {
        
        String blad = "null";
        
        if(m_exit_date != null || m_return_date != null ) {
            if(flaga==1) {
                return m_return_date.substring(11, 16);
            }
            else {
                return m_exit_date.substring(11, 16);
            }
        }
        else return blad;

    }
    
    public String opisZda () {
       
        String opis;
        
        if(flaga==1) {
            opis = "Wypis " + getFullNamePar () + " w dniu " + getDate()
                   + "o godzinie " + getTime() + " potwierdzony przez " + getFullNameUsr ();
            return opis;
        }
        else {
            opis = "Powrót " + getFullNamePar () + " w dniu " + getDate()
                   + "o godzinie " + getTime() + " potwierdzony przez " + getFullNameUsr ();
            return opis;  
        }
        
    }
 
}
