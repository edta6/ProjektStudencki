/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testowa;

/**
 *
 * @author xxx
 */
class TargetData {
    
    Integer id_target; 
    String target;
    
    public TargetData(Integer id_target, String target){
       this.id_target = id_target;
       this.target = target;      
    }
    
    public Integer getIdTarget() {
        return id_target;
    }
       
    @Override
    public String toString() {     
        return target;
    }  
}