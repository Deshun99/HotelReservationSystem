/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsreservationclient;

import ejb.session.stateful.RoomReservationControllerRemote;
import ejb.session.stateless.GuestEntitySessionBeanRemote;
import entity.Guest;
import entity.ReservationRecord;
import entity.Room;
import entity.RoomType;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import util.exception.EntityMismatchException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ReservationRecordNotFoundException;
import util.objects.ReservationTicket;

/**
 *
 * @author wong-
 */
public class MainApp {
    private GuestEntitySessionBeanRemote guestSessionBeanRemote;
    private RoomReservationControllerRemote roomReservationControllerRemote;
    private boolean loggedIn;
    
    
    public MainApp() {
    }

    public MainApp(GuestEntitySessionBeanRemote guestSessionBeanRemote, RoomReservationControllerRemote roomReservationController) {
        this.guestSessionBeanRemote = guestSessionBeanRemote;
        this.roomReservationControllerRemote = roomReservationController;
    }
    
    public void runApp() {
        
        Scanner sc = new Scanner(System.in);
        
        while(true){
            System.out.println("****Welcome to the Hotel Reservation client****");
            System.out.println("(1) Register as Guest");
            System.out.println("(2) Guest Login");
            System.out.println("(3) Search Hotel Room");
            System.out.println("(4) Exit");
            
            System.out.println();
            
            System.out.print("Select an option> ");
            String response = sc.nextLine().trim();
            
            System.out.println();
            switch(response){
                case "1":
                    registerGuest();
                    break;
                case "2":
                    guestLogin();
                    break;
                case "3":
                    searchHotelRoom();
                    break;
                case "4":
                    System.out.println("\n****Exiting system****");
                    System.exit(0);
                    break;
                default:
                    System.err.println("Invalid command");
            }
        }   
    }
    
    private void registerGuest(){
        
        Scanner sc = new Scanner(System.in);
        
        System.out.println("****Register New Guest****");
        
        System.out.print("Enter name> ");
        String name = sc.nextLine().trim();
        
        System.out.print("Enter username> ");
        String username = sc.nextLine().trim();
        
        System.out.print("Enter password> ");
        String password = sc.nextLine().trim();
        
        System.out.print("Enter email address> ");
        String emailAddress = sc.nextLine().trim();
        
        guestSessionBeanRemote.registerNewGuest(name, username, password, emailAddress);
        System.out.println("\n****New Guest Created!****\n");
    }
    
    private void guestLogin() {
        Scanner sc = new Scanner(System.in);
        System.out.println("****Guest Login****");
        System.out.print("Enter username> ");
        String username = sc.nextLine().trim();
        System.out.print("Enter password> ");
        String password = sc.nextLine().trim();
        
        try{
            roomReservationControllerRemote.guestLogin(username, password);
            System.out.println("\nLogin Successful");
            loggedIn = true;
            guestMenu();
        }catch(InvalidLoginCredentialException e){
            System.err.println("\n" + e.getMessage());
        }
    }
    
    private void guestMenu() {
        
        Scanner sc = new Scanner(System.in);
        
        while(true){
            System.out.println("\n****Welcome to the Hotel Reservation client****");
            System.out.println("(1) Search/Reserve Hotel Room");
            System.out.println("(2) View My Reservation Detail");
            System.out.println("(3) View All Reservations");
            System.out.println("(4) Log Out");
            
            System.out.println();
            
            System.out.print("Select an option> ");
            String response = sc.nextLine().trim();
            System.out.println();
            switch(response){
                case "1":
                    searchHotelRoom();
                    break;
                    
                case "2":
                    viewReservationDetail();
                    break;
                
                case "3":
                    viewAllReservations();
                    break;
                    
                case "4":
                    guestLogOut();
                    return;
                    
                default:
                    System.err.println("Please enter a valid command");
            }
        }
    }
    
    private ReservationTicket searchHotelRoom() {
        
        Scanner sc = new Scanner(System.in);
        
        try {
            System.out.println("****Search Hotel Room****");
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            System.out.println("Kindly note that our hotel only accepts reservations a maximum of 1 year in advance.");
            System.out.print("Enter check in date (dd/mm/yyyy)> ");
            Date startDate = df.parse(sc.nextLine().trim());
            System.out.print("Enter check out date (dd/mm/yyyy)> ");
            Date endDate = df.parse(sc.nextLine().trim());
            
            if(startDate.after(endDate) || startDate.equals(endDate)){
                System.err.println("\nCheck in date must be after checkout date.\n");
                return null;
            }
            
            ReservationTicket ticket = roomReservationControllerRemote.searchRooms(startDate, endDate, false);
            if(ticket.getAvailableRoomTypes().isEmpty()){
                System.err.println("\nThere are no available rooms for your desired check in and check out date.\n");
                return null;
            }
            System.out.println("\n****Available Rooms****");
            for(int i = 0; i < ticket.getAvailableRoomTypes().size(); i++){
                RoomType type = ticket.getAvailableRoomTypes().get(i);
                System.out.println("(" + i + ")" + type.getTypeName());
                System.out.println(type.getDescription());
                System.out.println("Amenities: " + type.getAmenities());
                System.out.println("Capacity: " + type.getCapacity());
                System.out.println("Rooms Available: " + ticket.getRespectiveNumberOfRoomsRemaining().get(i) );
                System.out.println("Cost: " + ticket.getRespectiveTotalBill().get(i));
                System.out.println();
            }
            System.out.println("\n****End of list****\n");
            while(loggedIn){
                System.out.print("Reserve a room? (Y/N)> ");
                String resp = sc.nextLine().trim();
                switch(resp){
                    case "Y":
                        reserveHotelRoom(ticket, startDate);
                        return ticket;
                    case "N":
                        return null;
                        
                    default:
                        System.out.println("Please choose a valid option");
                        break;
                }
            }
            return ticket;
        } catch (ParseException ex) {
            System.err.println("\nPlease enter valid date format\n");
            return null;
        }
    }
    
    private void reserveHotelRoom(ReservationTicket ticket, Date startDate) {
        
        Scanner sc = new Scanner(System.in);
        for(int i = 0; i < ticket.getAvailableRoomTypes().size(); i++){
            RoomType type = ticket.getAvailableRoomTypes().get(i);
            System.out.print("Enter number of " + type.getTypeName() + " to reserve> ");
            int num = sc.nextInt();
            sc.nextLine();
            if(num < 0 || num > ticket.getRespectiveNumberOfRoomsRemaining().get(i)){
                ticket.getRespectiveNumberReserved().add(0);
                System.out.println("****Invalid Number, 0 rooms of this type will be reserved****\n");
            }else{
                ticket.getRespectiveNumberReserved().add(num);
                System.out.println("****" + num + " of " + type.getTypeName() + " added to cart****\n");
            }
        }
        
        ArrayList<ReservationRecord> reservations = roomReservationControllerRemote.reserveRoom(ticket);
        System.out.println("Reservation Successful.");

        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 2);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);

        System.out.println(startDate);
        
        Calendar reference = Calendar.getInstance();
        int day = reference.get(Calendar.DAY_OF_MONTH);
        int month = reference.get(Calendar.MONTH);
        
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);

        if(reference.get(Calendar.HOUR_OF_DAY) >= cal.get(Calendar.HOUR_OF_DAY) && day == start.get(Calendar.DAY_OF_MONTH) && month == start.get(Calendar.MONTH)) {
            
            roomReservationControllerRemote.assignWalkInRoom(reservations);
        }
    }
    
    private void viewReservationDetail(){
        
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter reservation ID> ");
        Long resId = new Long(sc.nextInt());
        sc.nextLine();
        try {
            System.out.println("\n****View Reservation Detail****");
            String details = roomReservationControllerRemote.retrieveReservationDetails(resId);
            System.out.println(details);
        } catch (ReservationRecordNotFoundException | EntityMismatchException ex) {
            System.err.println(ex.getMessage());
        }
        
        
    }
    
    private void viewAllReservations(){
        System.out.println("\n****Viewing all Reservation Records****");
        ArrayList<ReservationRecord> reservations = roomReservationControllerRemote.retrieveAllReservation();
        if(reservations.isEmpty()){
            System.err.println("\nNo reservation records available.\n");
            return;
        }
        
        for(ReservationRecord r: reservations){
                System.out.println("Reservation ID: " + r.getReservationRecordId().toString() + "\n"
                        + "Start Date: " + r.getStartDateAsString() + "\n"
                        + "End Date: " + r.getEndDateAsString() + "\n"
                        + "Room Type Reserved: " + r.getRoomType().getTypeName() + "\n"
                        + "Bill: $" + r.getBill() + "\n");
                System.out.println();
            

        }
        System.out.println("****End of Reservation Records****\n");
        
    }
    
    private void guestLogOut(){
        roomReservationControllerRemote.guestLogout();
        loggedIn = false;
        System.out.println("\n****Logout successful****\n");
    }
  
}

