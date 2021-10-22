/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.EmployeeAccessRightsEnum;
import util.exception.EmployeeNotFoundException;

/**
 *
 * @author wong-
 */
@Stateless
public class EmployeeEntitySessionBean implements EmployeeEntitySessionBeanRemote, EmployeeEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    
    @Override
    public void createNewSysAdmin(String name, String username, String password){
        
        Employee sysAdmin = new Employee(name, username, password, EmployeeAccessRightsEnum.SYSADMIN);
        em.persist(sysAdmin);
        em.flush();
        
    }
    
    @Override
    public void createNewOpsManager(String name, String username, String password){
        
        Employee opManager = new Employee(name, username, password, EmployeeAccessRightsEnum.OPERATIONS);
        em.persist(opManager);
        em.flush();
        
    }
    
    @Override
    public void createNewSalesManager(String name, String username, String password){
        
        Employee salesManager = new Employee(name, username, password, EmployeeAccessRightsEnum.SALES);
        em.persist(salesManager);
        em.flush();
        
    }
    @Override
    public void createNewGuestRelationsOfficer(String name, String username, String password){
        
        Employee grOffr = new Employee(name, username, password, EmployeeAccessRightsEnum.GUESTRELATIONS);
        em.persist(grOffr);
        em.flush();
        
    }
    
    @Override
    public Employee retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException{
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.username = :username");
        query.setParameter("username", username);
        
        try{
            return (Employee) query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex){
            throw new EmployeeNotFoundException("Invalid Login Credentials");
        }
    }
    
    
    @Override
    public List<Employee> retrieveAllEmployees(){
        Query query = em.createQuery("SELECT e FROM Employee e");
        List<Employee> list = query.getResultList();
        for(Employee e: list){
            e.getEmployeeName();
            e.getAccessRights();
        }
        
        return list;
    }
}
