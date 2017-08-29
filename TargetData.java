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
    Integer active;
    
    public TargetData(Integer id_target, String target, Integer active){
       this.id_target = id_target;
       this.target = target;
       this.active = active;
    }
    
    public Integer getIdTarget() {
        return id_target;
    }
       
    @Override
    public String toString() {     
        return target;
    }  
}