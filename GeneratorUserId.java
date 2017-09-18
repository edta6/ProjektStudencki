/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OHP;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author Andrzej Pawlik
 */
public class GeneratorUserId { //generowanie ID dla uzytkownikow 
    
    private int userId;
    private int[] tabUserId = new int[8];
    GregorianCalendar rok = new GregorianCalendar();
    
    public GeneratorUserId(int fuserId) {
         
        int d = 1;
        int dzielnik = 10;
        
        if( fuserId==0 || (fuserId/10000)!=rok.get(Calendar.YEAR)) {
            
            int year = rok.get(Calendar.YEAR);
    
            year=year*1000+1;
        
            for(int j=6;j>-1;j--){           
                tabUserId[j]=((year%dzielnik)/d);
                d = d*10;
                dzielnik=dzielnik*10;            
            } 
            
            Calculate();
            
        }
        else {
            
            ValidateUserId right = new ValidateUserId(fuserId);
            
            if(right.checkSum()) {
                
            }
            fuserId=fuserId/10;
            fuserId=fuserId+1;
            
                for(int j=6;j>-1;j--){           
                    tabUserId[j]=((fuserId%dzielnik)/d);
                    d = d*10;
                    dzielnik=dzielnik*10;            
                } 
        
            Calculate();
            
        }
    }       
    
    private void Calculate() {
        
        int mnoznik = 1;
        int suma;
        
            suma =  1 * tabUserId[0] +
                    3 * tabUserId[1] +
                    1 * tabUserId[2] +
                    3 * tabUserId[3] +
                    1 * tabUserId[4] +
                    3 * tabUserId[5] +
                    1 * tabUserId[6];
        
            suma %= 10;
            suma = 10 - suma;
            suma %= 10;
        
            tabUserId[7]=suma;
        
            for(int i=7;i>-1;i--){
                userId=userId+tabUserId[i]*mnoznik;
                mnoznik=mnoznik*10;
            }
    }
    
    /**
     * New userId
     *
     * @return The new userId.
     */
    public int getUserId(){
        return userId;
    }
}
