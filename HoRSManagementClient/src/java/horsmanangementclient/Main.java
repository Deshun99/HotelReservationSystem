/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanangementclient;

import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import entity.Employee;
import java.util.List;
import javax.ejb.EJB;

/**
 *
 * @author wong-
 */
public class Main {

    @EJB(name = "EmployeeEntitySessionBeanRemote")
    private static EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote;

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        List<Employee> employees = employeeEntitySessionBeanRemote.retrieveAllEmployees();
        
        for(Employee employee: employees) {
            System.out.println("Employee: employeeId=" + employee.getEmployeeId() + "; employeeName=" + employee.getEmployeeName());
        }
    }
    
}
