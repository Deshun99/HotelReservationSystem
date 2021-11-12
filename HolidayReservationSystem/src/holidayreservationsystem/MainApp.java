/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holidayreservationsystem;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ws.client.EntityMismatchException_Exception;
import ws.client.InvalidLoginCredentialException_Exception;
import ws.client.Partner;
import ws.client.ReservationRecord;
import ws.client.ReservationRecordNotFoundException_Exception;
import ws.client.ReservationTicket;
import ws.client.ReservationTicketWrapper;
import ws.client.RoomType;

/**
 *
 * @author wong-
 */
public class MainApp {
    
    private Partner currentPartner;
    private boolean loggedIn;

    public MainApp() {
    }
    
    
    public void runApp() throws DatatypeConfigurationException, ParseException{
        
        Scanner sc = new Scanner(System.in);
        
        while(true){
            System.out.println("****Welcome to the Holiday Reservation System****");
            System.out.println("(1) Partner Login");
            System.out.println("(2) Search Hotel Room");
            System.out.println("(3) Exit");
            
            System.out.print("Select an option> ");
            String response = sc.nextLine().trim();
            
            System.out.println();
            switch(response){
                case "1":
                    partnerLogin();
                    break;
                case "2":
                    searchHotelRoom();
                    break;
                case "3":
                    System.out.println("\n****Exiting system****");
                    System.exit(0);
                    break;
                default:
                    System.err.println("Invalid command");
            }
        }   
    }
    
    private void partnerLogin() throws DatatypeConfigurationException, ParseException{
        
        Scanner sc = new Scanner(System.in);
        System.out.println("****Partner Login****");
        System.out.print("Enter username> ");
        String username = sc.nextLine().trim();
        System.out.print("Enter password> ");
        String password = sc.nextLine().trim();
        
        try{
            currentPartner = HolidayReservationSystem.partnerLogin(username, password);
            System.out.println("\nLogin Successful");
            loggedIn = true;
            partnerMenu();
        } catch(InvalidLoginCredentialException_Exception e){
            System.err.println("\n" + e.getMessage());
        }
    }
    
    private ReservationTicketWrapper searchHotelRoom() throws DatatypeConfigurationException, ParseException{
        
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
                System.err.println("\nCheck in date must be before checkout date.\n");
                return null;
            }
            
            GregorianCalendar gcal = new GregorianCalendar();
            XMLGregorianCalendar xgcal1;
            XMLGregorianCalendar xgcal2;
            
            gcal.setTime(startDate);
            xgcal1= DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
            
            gcal.setTime(endDate);
            xgcal2= DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
            
            
            System.out.println("\n****Available Rooms****");
            
            ReservationTicketWrapper ticket = HolidayReservationSystem.searchRoom(xgcal1, xgcal2);
            
            for(int j = 0; j < ticket.getReservationDescriptions().size(); j++) {
                System.out.println(ticket.getReservationDescriptions().get(j));
                System.out.println();
            }
            
                     
            System.out.println("\n****End of list****\n");
            
            while(loggedIn){
                System.out.print("Reserve a room? (Y/N)> ");
                String resp = sc.nextLine().trim();
                switch(resp){
                    case "Y":
                        reserveHotelRoom(ticket);
                        return ticket;
                    case "N":
                        return null;
                        
                    default:
                        System.out.println("\nPlease choose a valid option");
                        break;
                }
            }
            return ticket;
        } catch (ParseException ex) {
            System.err.println("\nPlease enter valid date format\n");
            return null;
        }
    }
    
    private void reserveHotelRoom(ReservationTicketWrapper wrapperTicket){
        
//        ReservationTicket ticket = HolidayReservationSystem.unwrapRoomTicket(wrapperTicket);
//     
//        Scanner sc = new Scanner(System.in);
//        for(int i = 0; i < ticket.getAvailableRoomTypes().size(); i++){
//            RoomType type = ticket.getAvailableRoomTypes().get(i);
//            System.out.print("Enter number of " + type.getTypeName() + " to reserve> ");
//            int num = sc.nextInt();
//            sc.nextLine();
//            if(num < 0 || num > ticket.getRespectiveNumberOfRoomsRemaining().get(i)){
//                ticket.getRespectiveNumberReserved().add(0);
//                System.out.println("****Invalid Number, 0 rooms of this type will be reserved****\n");
//            }else{
//                ticket.getRespectiveNumberReserved().add(num);
//                System.out.println("****" + num + " of " + type.getTypeName() + " added to cart****\n");
//            }
//        }
//        
//        HolidayReservationSystem.partnerReserveRooms(ticket, currentPartner.getPartnerId(), currentPartner.getEmailAddress());
        System.out.println("Reservation Successful.");
    }
    
    private void partnerMenu() throws DatatypeConfigurationException, ParseException{
        
        Scanner sc = new Scanner(System.in);
        
        while(true){
            System.out.println("\n****Welcome to the Holiday Reservation System****");
            System.out.println("(1) Search/Reserve Hotel Room");
            System.out.println("(2) View My Reservation Detail");
            System.out.println("(3) View All Reservations");
            System.out.println("(4) Log Out");
            
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
                    partnerLogout();
                    return;
                    
                default:
                    System.err.println("Please enter a valid command");
            }
        }
    }
    
    private void viewReservationDetail(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter reservation ID> ");
        Long resId = new Long(sc.nextInt());
        sc.nextLine();
        try {
            System.out.println("\n****View Reservation Detail****");
            String details = HolidayReservationSystem.viewReservationDetails(resId, currentPartner.getPartnerId());
            System.out.println(details);
        } catch (ReservationRecordNotFoundException_Exception | EntityMismatchException_Exception ex) {
            System.err.println(ex.getMessage());
        } 
    }
    
    private void viewAllReservations(){
        System.out.println("\n****Viewing all Reservation Records****");
        List<ReservationRecord> reservations = HolidayReservationSystem.viewAllPartnerReservation(currentPartner.getPartnerId());
        if(reservations.isEmpty()){
            System.err.println("\nNo reservation records available.\n");
            return;
        }
        
        for(ReservationRecord r: reservations){
            DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
            String start = dateFormat.format(r.getStartDate());
            String end = dateFormat.format(r.getEndDate());
            System.out.println( "Reservation ID: " + r.getReservationRecordId().toString() + "\n" +
                                "Start Date: " + start + "\n" + 
                                "End Date: " + end + "\n" +
                                "Room Type Reserved: " + r.getRoomType().getTypeName() + "\n" +
                                "Bill: $" + r.getBill());
            System.out.println();
        }
        
        System.out.println("****End of Reservation Records****\n");
        
    }
    
    private void partnerLogout(){
        loggedIn = false;
        System.out.println("\n****Logout successful****\n");
    }
    
    
}



