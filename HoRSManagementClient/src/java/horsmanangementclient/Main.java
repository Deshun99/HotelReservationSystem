/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanangementclient;

import ejb.session.singleton.SystemHelperRemote;
import ejb.session.stateful.RoomReservationControllerRemote;
import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import ejb.session.stateless.PartnerEntitySessionBeanRemote;
import ejb.session.stateless.RoomEntitySessionBeanRemote;
import entity.Employee;
import java.util.List;
import javax.ejb.EJB;

/**
 *
 * @author wong-
 */
public class Main {

    @EJB(name = "SystemHelperRemote")
    private static SystemHelperRemote systemHelperRemote;

    @EJB(name = "RoomReservationControllerRemote")
    private static RoomReservationControllerRemote roomReservationControllerRemote;

    @EJB(name = "RoomEntitySessionBeanRemote")
    private static RoomEntitySessionBeanRemote roomEntitySessionBeanRemote;

    @EJB(name = "PartnerEntitySessionBeanRemote")
    private static PartnerEntitySessionBeanRemote partnerEntitySessionBeanRemote;

    @EJB(name = "EmployeeEntitySessionBeanRemote")
    private static EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote;
    
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        MainApp mainApp = new MainApp(roomEntitySessionBeanRemote, employeeEntitySessionBeanRemote, partnerEntitySessionBeanRemote, roomReservationControllerRemote,  systemHelperRemote);
        mainApp.runApp();
        
//        List<Employee> employees = employeeEntitySessionBeanRemote.retrieveAllEmployees();
//        
//        for(Employee employee: employees) {
//            System.out.println("Employee: employeeId=" + employee.getEmployeeId() + "; employeeName=" + employee.getEmployeeName());
//        }
    }
    
}
