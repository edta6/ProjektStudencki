/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testowa;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author andrzej
 */
 class czas {
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String calendarDate = formatter.format(now);
        SimpleDateFormat formatter1 = new SimpleDateFormat("HH:mm:ss");
        String calendarTime;
        
       void setHours(int hour, int minutes) {
            Date clone = (Date)now.clone();
            clone.setHours(hour);
            clone.setMinutes(minutes);
            calendarTime = formatter1.format(clone);
        }
       
       void setHoursBack(){
            calendarTime = formatter1.format(now);
       }
        
          public String getcalendarDate() {
            return calendarDate;
        }
        
        public String getcalendarTime() {
            return calendarTime;
        }
 }
