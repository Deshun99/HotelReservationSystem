/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanangementclient;

import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import ejb.session.stateless.PartnerEntitySessionBeanRemote;
import entity.Employee;
import entity.Partner;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author wong-
 */
public class SystemAdministrationModule {
    private EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote;
    private PartnerEntitySessionBeanRemote partnerEntitySessionBeanRemote;
    private Employee sysAdmin;
 
    
    public SystemAdministrationModule() {
        
    }

    public SystemAdministrationModule(Employee sysAdmin, EmployeeEntitySessionBeanRemote employeeSessionBean, PartnerEntitySessionBeanRemote partnerSessionBean) {
        this();
        
        this.sysAdmin = sysAdmin;
        this.employeeEntitySessionBeanRemote = employeeSessionBean;
        this.partnerEntitySessionBeanRemote = partnerSessionBean;
    }
    
    
    /*
    Navigation menu for SystemAdminEntity when they log into the Management Client.
    Allows sysadmins to access methods to create employee & partners, and view all employess & partners
    */
    public void runSysAdminModule(){
        Scanner sc = new Scanner(System.in);
        
        while(true){
            System.out.println("\n****Welcome to the System Administration Module****");
            System.out.println("(1) Create New Employee \n(2) View All Employee \n(3) Create New Partner \n(4) View All Partners \n(5) Return");
            System.out.print("Select an option> ");
            String response = sc.nextLine().trim();
                    
            System.out.println();
            switch(response){
                case "1":
                    createEmployee();
                    break;
                case "2":
                    viewAllEmployees();
                    break;
                case "3":
                    createPartner();
                    break;
                case "4":
                    viewAllPartners();
                    break;
                case "5":
                    System.out.println("\n****Logging Out****\n");
                    return;
                default:
                    System.err.println("\nInvalid input please try again.\n");
            }
        }
    }
    
    /*
    method for sysadmin to create a new employee (Sysadmin, ops manager, sales manager, guest relation offr) account 
    */
    public void createEmployee(){
        Scanner sc = new Scanner(System.in);
        System.out.println("**** Create a new Employee Account****");
        System.out.print("Enter employee's name> ");
        String name = sc.nextLine().trim();
        System.out.print("Enter username> ");
        String username = sc.nextLine().trim();
        System.out.print("Enter password> ");
        String password = sc.nextLine().trim();
        
        System.out.println("\nCreate new employee as:\n(1)System Administrator \n(2)Operation Manager \n(3)Sales Manager \n(4)Guest Relation Officer \n(5)Cancel");
        System.out.print("Select an option> ");
        String response = sc.nextLine().trim();
                    
        System.out.println();
        switch(response){
            case "1":
                employeeEntitySessionBeanRemote.createNewSysAdmin(name, username, password);
                System.out.println("New System Administrator created.\n");                    
                break;
            case "2":
                employeeEntitySessionBeanRemote.createNewOpsManager(name, username, password);
                System.out.println("New Operation Manager created.\n");
                break;
            case "3":
                employeeEntitySessionBeanRemote.createNewSalesManager(name, username, password);
                System.out.println("New Sales Manager created.\n");
                break;
            case "4":
                employeeEntitySessionBeanRemote.createNewGuestRelationsOfficer(name, username, password);
                System.out.println("New Guest Relations Officer created.\n");
                break;
            case "5":
                return;
            default:
                System.err.println("Invalid input please try again.\n");
        }

    }
    
    /*
    Method for sysadmin to retrieve and view all employees in database (id and name)
    */
    public void viewAllEmployees(){
        List<Employee> list = employeeEntitySessionBeanRemote.retrieveAllEmployees();
        System.out.println("\n****Viewing All Employees****");
        for(Employee e: list){
            System.out.println(e.toString());
        }
        System.out.println("****End of List****\n");
    }
    
    /*
    Method for sysadmin to create new partner account 
    */
    public void createPartner(){
        Scanner sc = new Scanner(System.in);
        System.out.println("**** Create a new Partner Account****");
        System.out.print("Enter partners's name> ");
        String name = sc.nextLine().trim();
        System.out.print("Enter username> ");
        String username = sc.nextLine().trim();
        System.out.print("Enter password> ");
        String password = sc.nextLine().trim();
        System.out.print("Enter email> ");
        String email = sc.nextLine().trim();
        
        partnerEntitySessionBeanRemote.createNewPartner(name, username, password, email);
        System.out.println("New partner created.");
    }
    
    /*
    Method for sysadmin to retrieve and view all partners in database (id and name)
    */
    public void viewAllPartners(){
        List<Partner> list = partnerEntitySessionBeanRemote.retrieveAllPartners();
        System.out.println("\n****Viewing All Partners****");
        for(Partner p: list){
            System.out.println(p.toString());
        }
        System.out.println("****End of List****\n");
    }
    
}
