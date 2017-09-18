/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OHP;

/**
 *
 * @author Andrzej Pawlik
 */
public class ValidateUserId {
    
    private int number = 0;
    private final int[] singleNumber = new int[8];
    
    public ValidateUserId (int fnumber){
       
        number=fnumber;
        
        int d = 1;
        int dzielnik = 10;
        
        for(int i=7;i>-1;i--){           
            singleNumber[i]=((number%dzielnik)/d);
            d = d*10;
            dzielnik=dzielnik*10;            
        } 
    }
    
    public boolean checkSum(){
        int suma = 1 * singleNumber[0] +
                   3 * singleNumber[1] +
                   1 * singleNumber[2] +
                   3 * singleNumber[3] +
                   1 * singleNumber[4] +
                   3 * singleNumber[5] +
                   1 * singleNumber[6];
        
        suma %= 10;
        suma = 10 - suma;
        suma %= 10;
 
        return suma == singleNumber[7];
    }
}