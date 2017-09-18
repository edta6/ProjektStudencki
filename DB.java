package OHP;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
        
    public boolean isError=false;
    Connection con;

    public DB () {}

    public void DBConect(String db_user, String db_password) {

        String host =  "jdbc:mysql://localhost:3306/"
                    + "?useUnicode=true&characterEncoding=utf-8";

        try {
            isError=false;
            con = DriverManager.getConnection(host,db_user,db_password);
        }
        catch ( SQLException err ) {
            isError=true;
        }
    }           
}  