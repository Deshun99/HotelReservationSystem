/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Local;
import util.exception.EmployeeNotFoundException;

/**
 *
 * @author wong-
 */
@Local
public interface EmployeeEntitySessionBeanLocal {
    
    public Employee retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException;
    
    public void createNewSysAdmin(String name, String username, String password);
    
    public void createNewOpsManager(String name, String username, String password);
    
    public void createNewSalesManager(String name, String username, String password);
    
    public void createNewGuestRelationsOffr(String name, String username, String password);
    
    public List<Employee> retrieveAllEmployees();
    
}
