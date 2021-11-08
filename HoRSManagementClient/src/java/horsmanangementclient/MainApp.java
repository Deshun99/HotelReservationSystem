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
import java.util.Scanner;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author wong-jd
 */
public class MainApp {
    private RoomEntitySessionBeanRemote roomEntitySessionBeanRemote;
    private EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote;
    private PartnerEntitySessionBeanRemote partnerEntitySessionBeanRemote;
    private RoomReservationControllerRemote roomReservationControllerRemote;
    private SystemHelperRemote systemHelperRemote;
    private Employee currentEmployee;
    
    
    public MainApp() {
        
    }

    public MainApp(RoomEntitySessionBeanRemote roomSessionBean, EmployeeEntitySessionBeanRemote employeeSessionBean, PartnerEntitySessionBeanRemote partnerSessionBean, RoomReservationControllerRemote roomReservationController, SystemHelperRemote systemHelper) {
        this();
        
        this.roomEntitySessionBeanRemote = roomSessionBean;
        this.employeeEntitySessionBeanRemote = employeeSessionBean;
        this.partnerEntitySessionBeanRemote = partnerSessionBean;
        this.roomReservationControllerRemote = roomReservationController;
        this.systemHelperRemote = systemHelper;
    }
    
    
    public void runApp(){
        
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.println("****Welcome to Merlion Hotel****");
            System.out.println("(1) Login \n(2) Exit");
            System.out.print("Select an option> ");
            String response = sc.nextLine().trim();
            
            System.out.println();
            switch(response){
                case "1":
                    doLogin();
                    break;
                case "2":
                    System.exit(0);
                default:
                    System.out.println("Please enter a valid command");
            }
        }
    }
    
    public void doLogin(){  
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Username> ");
        String username = sc.nextLine().trim();
        System.out.print("Enter password> ");
        String password = sc.nextLine().trim();
        
        try {
            currentEmployee = employeeEntitySessionBeanRemote.login(username, password);
            System.out.println("\nLogin successful.\n");
            employeeMenu();
        }catch (InvalidLoginCredentialException ex) {
            System.err.println("\nInvalid Credentials. Please Try Again.\n");
        } 
    }
    
    public void employeeMenu(){

        Scanner sc = new Scanner(System.in);
        String response;
        switch(currentEmployee.getAccessRights()){
            case SYSADMIN:
                while(true){
                    System.out.println("\n****Welcome to the Hotel Management Client****");
                    System.out.println("(1)System Administrator Module \n(2)Logout");
                    System.out.print("Select an option> ");
                    response = sc.nextLine().trim();
            
                    System.out.println();
                    if(response.equals("1")){
                        SystemAdministrationModule sysAdminModule = new SystemAdministrationModule(currentEmployee ,employeeEntitySessionBeanRemote, partnerEntitySessionBeanRemote);
                        sysAdminModule.runSysAdminModule();
                    }else if(response.equals("2")){
                        doLogout();
                        break;
                    }else{
                        System.out.println("Enter a valid command");
                    }
                }
                break;
                
            case OPERATIONS:
                while(true){
                    System.out.println("\n****Welcome to the Hotel Management Client****");
                    System.out.println("(1)Hotel Operation (Operation Manager) Module \n(2)Logout");
                    System.out.print("Select an option> ");
                    response = sc.nextLine().trim();
                    
                    System.out.println();
                    if(response.equals("1")){
                        HotelOperationModule opsMod = new HotelOperationModule(roomEntitySessionBeanRemote, systemHelperRemote, currentEmployee);
                        opsMod.runHotelOperationModuleOperationsManager();
                    }else if(response.equals("2")){
                        doLogout();
                        break;
                    }else{
                        System.out.println("Enter a valid command");
                    }
                }
                break;
                
            case SALES:
                while(true){
                    System.out.println("\n****Welcome to the Hotel Management Client****");
                    System.out.println("(1)Hotel Operation Module (Sales Manager) \n(2)Logout");
                    System.out.print("Select an option> ");
                    response = sc.nextLine().trim();
                    
                    System.out.println();
                    if(response.equals("1")){
                        HotelOperationModule opsMod = new HotelOperationModule(roomEntitySessionBeanRemote, systemHelperRemote, currentEmployee);
                        opsMod.runHotelOperationModuleSalesManager();
                    }else if(response.equals("2")){
                        doLogout();
                        break;
                    }else{
                        System.out.println("Enter a valid command");
                    }
                }
                break;
                
            case GUESTRELATIONS:
                while(true){
                    System.out.println("\n****Welcome to the Hotel Management Client****");
                    System.out.println("(1)Front Office Module \n(2)Logout");
                    System.out.print("Select an option> ");
                    response = sc.nextLine().trim();
                    
                    System.out.println();
                    if(response.equals("1")){
                        FrontOfficeModule front = new FrontOfficeModule(roomReservationControllerRemote);
                        front.runFrontOfficeModule();
                    }else if(response.equals("2")){
                        doLogout();
                        break;
                    }else{
                        System.out.println("Enter a valid command");
                    }
                }
                break;

            default:
                while(true){
                    System.out.print("(1) Log Out> ");
                    if(sc.nextLine().trim().equals("1")){
                        doLogout();
                        return;
                    }
                }
        }
    }
    
    public void doLogout(){
        currentEmployee = null;
        System.out.println("Logout successful.\n");
    }
}
