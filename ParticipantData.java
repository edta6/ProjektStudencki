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
class ParticipantData { //takie same pola jak tabela w MySql
    
    Integer id_part; 
    String first_name;
    String  last_name;
    Integer active; 
    
    public ParticipantData(Integer id_part, String first_name, String last_name, 
                           Integer active
                    ){
       this.id_part = id_part;
       this.first_name = first_name;
       this.last_name = last_name;
       this.active = active;      
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
    
    public Integer getActive() {
        return active;
    }
    
    @Override
    public String toString() {
        
        String fullName = last_name + " " + first_name;
        
        return fullName;
    }
    
}
