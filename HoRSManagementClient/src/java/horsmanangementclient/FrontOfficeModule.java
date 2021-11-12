/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanangementclient;

import ejb.session.stateful.RoomReservationControllerRemote;
import entity.ReservationRecord;
import entity.RoomType;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import javax.validation.ConstraintViolationException;
import util.exception.EarlyCheckInUnavailableException;
import util.exception.ReservationRecordNotFoundException;
import util.exception.RoomNotAssignedException;
import util.exception.RoomUpgradeException;
import util.exception.UnoccupiedRoomException;
import util.objects.ReservationTicket;

/**
 *
 * @author wong-
 */
public class FrontOfficeModule {

    private RoomReservationControllerRemote roomReservationControllerRemote;
    
    public FrontOfficeModule() {
    }

    public FrontOfficeModule(RoomReservationControllerRemote roomReservationController) {
        this.roomReservationControllerRemote = roomReservationController;
    }
    
    public void runFrontOfficeModule(){
        
        Scanner sc = new Scanner(System.in);
        
        while(true){
            System.out.println("\n****Welcome to the front office module****");
            System.out.println("(1)Search/Reserve Room \n" +
                            "(2)Check-in Guest \n" +
                            "(3)Check-out Guest \n" +
                            "(4)Return");
            
            System.out.print("Select an option> ");
            String response = sc.nextLine().trim();
            
            System.out.println();
            switch(response){
                case "1":
                    searchRoom();
                    break;
                case "2":
                    checkIn();
                    break;
                case "3":
                    checkOut();
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Please select a valid option");
                    break;
            }
        }
    }
    
    private ReservationTicket searchRoom(){
        Scanner sc = new Scanner(System.in);
        try {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Calendar cal = Calendar.getInstance();
            Date startDate = cal.getTime();
            System.out.print("Enter the check out date (dd/mm/yyyy)> ");
            Date endDate = df.parse(sc.nextLine().trim());
            
            if(startDate.after(endDate) || startDate.equals(endDate)){
                System.err.println("\nCheck in date must be after checkout date.\n");
                return null;
            }
            
            ReservationTicket ticket = roomReservationControllerRemote.searchRooms(startDate, endDate, true);
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
                while(true){
                    System.out.print("Reserve a room? (Y/N)> ");
                    String response = sc.nextLine().trim();
                    System.out.println();
                    switch(response){
                        case "Y":
                            reserveHotelRoom(ticket);
                            return ticket;
                        case "N":
                            return ticket;
                        default:
                            System.out.println("Please choose a valid option");
                            break;
                    }
                }
            
        } catch (ParseException ex) {
            System.err.println("Please enter valid date format");
            return null;
        }
    }
        
    private void reserveHotelRoom(ReservationTicket ticket) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter guest email address > ");
        String email = sc.nextLine().trim();
        try{
            roomReservationControllerRemote.setGuestEmail(email);
        }catch(ConstraintViolationException e){
            System.err.println("Invalid email address");
            return;
        }
        
        for(int i = 0; i < ticket.getAvailableRoomTypes().size(); i++){
            RoomType type = ticket.getAvailableRoomTypes().get(i);
            System.out.println("Enter number of " + type.getTypeName() + " to reserve: ");
            int num = sc.nextInt();
            if(num < 0 || num > ticket.getRespectiveNumberOfRoomsRemaining().get(i)){
                ticket.getRespectiveNumberReserved().add(0);
                System.out.println("\nInvalid Number, 0 rooms of this type will be reserved\n");
            }else{
                ticket.getRespectiveNumberReserved().add(num);
                System.out.println(num + " of " + type.getTypeName() + " added to cart\n");
            }
        }
        
        ArrayList<ReservationRecord> reservations = roomReservationControllerRemote.reserveRoom(ticket);
        System.out.println("Reservation Successful.");
        roomReservationControllerRemote.assignWalkInRoom(reservations);
        System.out.println("****Checking In****");
        System.out.println("===================");
        for(ReservationRecord r : reservations){
            System.out.println("Reservation ID: " + r.getReservationRecordId() + "\n" +
                                        r.getRoomType().getTypeName());
            checkInRoom(r.getReservationRecordId());
            System.out.println("===================");
        }
        System.out.println("****End of Check In****");
    }
    
    
    
    private void checkIn(){
        Scanner sc = new Scanner(System.in);
        try{
            System.out.print("Enter Email Address> ");
            String email = sc.nextLine().trim();
        
            List<ReservationRecord> reservations = roomReservationControllerRemote.getReservationListByEmail(email);
            if(reservations.isEmpty()){
                System.out.println("No reservations made for this guest today");
            }else{
                System.out.println("****Checking In****");
                System.out.println("===================");
                for(ReservationRecord r : reservations){
                    System.out.println("Reservation ID: " + r.getReservationRecordId() + "\n" +
                                        r.getRoomType().getTypeName());
                    checkInRoom(r.getReservationRecordId());
                    System.out.println("===================");
                }
                System.out.println("****End of Check In****");
            }
        }catch(ConstraintViolationException e){
            System.err.println("Invalid email address entered.");
        } 
    }
    
    private void checkInRoom(Long reservationId) {
        try{
            System.out.println(roomReservationControllerRemote.checkInRoom(reservationId));
        } catch (RoomNotAssignedException | RoomUpgradeException | EarlyCheckInUnavailableException ex) {
            System.out.println(ex.getMessage());
        }
    }  
    
    private void checkOut(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter room number> ");
        String roomNumber = sc.nextLine().trim();
        try{
            System.out.println(roomReservationControllerRemote.checkOutRoom(roomNumber));
        }catch(ReservationRecordNotFoundException | UnoccupiedRoomException e){
            System.out.println(e.getMessage());
        }
    }


    

    
    
    
}
