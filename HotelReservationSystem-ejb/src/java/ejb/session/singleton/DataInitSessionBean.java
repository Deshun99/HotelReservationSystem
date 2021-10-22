/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeEntitySessionBeanLocal;
import entity.Employee;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.EmployeeNotFoundException;

/**
 *
 * @author wong-
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB
    private EmployeeEntitySessionBeanLocal employeeEntitySessionBeanLocal;
   
    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @PostConstruct
    public void postConstruct() {
        
        try {
            employeeEntitySessionBeanLocal.retrieveEmployeeByUsername("admin");
        } catch(EmployeeNotFoundException ex) {
            employeeEntitySessionBeanLocal.createNewSysAdmin("admin", "admin", "password");
            employeeEntitySessionBeanLocal.createNewOpsManager("operationmanager", "operationmanager", "password");
            employeeEntitySessionBeanLocal.createNewSalesManager("salesmanager", "salesmanager", "password");
            employeeEntitySessionBeanLocal.createNewGuestRelationsOfficer("guestrelationsofficer", "guestrelationsofficer", "password");
        }
          
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    
}
