/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testowa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Andrzej Pawlik
 */

public class DB {
        
        public boolean isError=false;
        Connection con;
     
        public DB () {}
    
        public void DBConect(String db_user, String db_password) {
            
            String host =  "jdbc:mysql://localhost:3306/ohp?useUnicode=true&characterEncoding=utf-8";
        
            try {
                isError=false;
                con = DriverManager.getConnection(host,db_user,db_password);
            }
            catch ( SQLException err ) {
                isError=true;
            }
        }
    }  