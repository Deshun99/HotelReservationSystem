/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsreservationclient;

import ejb.session.stateful.RoomReservationControllerRemote;
import ejb.session.stateless.GuestEntitySessionBeanRemote;
import javax.ejb.EJB;


/**
 *
 * @author wong-
 */
public class Main {

    @EJB(name = "GuestEntitySessionBeanRemote")
    private static GuestEntitySessionBeanRemote guestEntitySessionBeanRemote;

    @EJB(name = "RoomReservationControllerRemote")
    private static RoomReservationControllerRemote roomReservationControllerRemote;

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(guestEntitySessionBeanRemote, roomReservationControllerRemote);
        mainApp.runApp();
        
    }
    
}
