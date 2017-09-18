/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OHP;

/**
 *
 * @author xxx
 */
public class UserData {
    
    Integer id_part; 
    String  first_name;
    String  last_name;
    String  nick;
    Integer role; 
    
    public UserData (Integer id_part, String first_name, String last_name, 
                      String nick, Integer active
                    ){
       this.id_part = id_part;
       this.first_name = first_name;
       this.last_name = last_name;
       this.nick = nick;
       this.role = active;
    }
    
    public Integer getIdPart() {
        return id_part;
    }

    public String getFirstName() {
        return first_name;
    }
    
    public String getLastName() {
        return last_name;
    }   
    
    public String getNick() {
        return nick;
    }  
    
    public Integer getRole() {
        return role;
    }
    
    @Override
    public String toString() {
        
        String fullName = last_name + " " + first_name;
        
        return fullName;
    } 
}
