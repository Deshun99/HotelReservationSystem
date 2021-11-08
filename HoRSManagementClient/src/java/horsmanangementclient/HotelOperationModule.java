/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanangementclient;

import ejb.session.singleton.SystemHelperRemote;
import ejb.session.stateless.RoomEntitySessionBeanRemote;
import entity.Employee;
import entity.ExceptionReport;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import util.enumeration.RateTypeEnum;
import util.enumeration.StatusEnum;
import util.exception.LastAvailableRateException;
import util.exception.RoomNotFoundException;
import util.exception.RoomRateNotFoundException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author wong-
 */
public class HotelOperationModule {
    
    private RoomEntitySessionBeanRemote roomEntitySessionBeanRemote;
    private SystemHelperRemote systemHelperRemote;
    
    private Employee currentEmployee;
    private final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
    public HotelOperationModule() {
    }

    public HotelOperationModule(RoomEntitySessionBeanRemote roomSessionBean, SystemHelperRemote systemHelper,  Employee currentEmployee) {
        this.roomEntitySessionBeanRemote = roomSessionBean;
        this.systemHelperRemote = systemHelper;
        this.currentEmployee = currentEmployee;
    }
    
    
    public void runHotelOperationModuleOperationsManager(){
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.println("\n****Welcome to the Hotel Operations Module****");
            System.out.println("(1) Room Type Management");
            System.out.println("(2) Room Management");
            System.out.println("(3) View Room Allocation Exception Report");
            System.out.println("(4) Manually Trigger Daily Room Allocation");
            System.out.println("(5) Return");
            System.out.print("Select an option> ");
            String response = sc.nextLine().trim();
            
            System.out.println();
            switch(response){
                case "1":
                    roomTypeManagement();
                    break;
                    
                case "2":
                    roomManagement();
                    break;
                    
                case "3":
                    viewExceptionReport();
                    break;
                               
                case "4":
                    System.out.println("\n****Manually triggering room allocation****");
                    systemHelperRemote.allocateRoomsDaily();
                    System.out.println("****Allocation complete****\n");
                    break;
                    
                case "5":
                    return;
                    

                    
                default:
                    System.err.println("Please enter a valid command");
            }           
        }
    }
    
    
    private void roomTypeManagement(){
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.println("****Room Type Management****");
            System.out.println("(1) Create new Room Type");
            System.out.println("(2) View/Update/Delete Room Type Details");
            System.out.println("(3) View All Room Types");
            System.out.println("(4) Return");
            System.out.print("Select an option> ");
            String response = sc.nextLine().trim();
            
            System.out.println();
            switch(response){
                case "1":
                    createNewRoomType();
                    break;
                    
                case "2":
                    viewRoomTypeDetails();
                    break;
                    
                case "3":
                    viewAllRoomTypes();
                    break;
                    
                case "4":
                    System.out.println("\n***Returning to home page***\n");
                    return;
                                      
                default:
                    System.err.println("\nPlease input a valid command.\n");
            }
            
        }
    }
    
     private void createNewRoomType(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter name of new Room Type> ");
        String newTypeName = sc.nextLine().trim();
        
        System.out.print("Enter 140 characters description for " + newTypeName + "> ");
        String newDescription = sc.nextLine().trim();
        
        System.out.print("Enter types of bed available in " + newTypeName + "> ");
        String newBeds = sc.nextLine().trim();
        
        System.out.print("Enter capacity of> " + newTypeName);
        Integer capacity = null;
        try {
            capacity = sc.nextInt();
        } catch (InputMismatchException e) {
            System.err.println("Not a valid capacity. Retuning to menu.");
            return;
        }
        sc.nextLine();
        
        System.out.print("Enter amenities available in " + newTypeName + "> ");
        String newAmenities = sc.nextLine().trim();
        
        List<RoomType> roomRanks = roomEntitySessionBeanRemote.getRoomTypesByRanking();
        int i = 0;
        System.out.println("Select the room ranking position to insert the new room in (Smaller number = more premium)");
        for(i = 0; i < roomRanks.size(); i++){
            System.out.println("(" + i + ") " + roomRanks.get(i).getTypeName());
        }
        
        /*for(RoomTypeEntity r: roomRanks){
            System.out.println("(" + i + ") " + r.getTypeName());
            i++;
        }*/
        System.out.println("(" + i + ") === Add as Least Premium ===");
        int newRank = 0;
        try {
            newRank = sc.nextInt();
        } catch (InputMismatchException e) {
            System.err.println("Invalid rank. Returning to menu.");
        }
        if(newRank >= roomRanks.size()){
            newRank = roomRanks.size(); //least premium
        }else if (newRank < 0){
            newRank = 0; //most premium
        }
        
        Long roomTypeId = roomEntitySessionBeanRemote.createNewRoomType(newTypeName, newDescription, newBeds, capacity, newAmenities, newRank);
        
        try {
            System.out.print("Enter Normal Rate per night for " + newTypeName + " $");
            double normalRate = sc.nextDouble();
            roomEntitySessionBeanRemote.createNewNormalRate(newTypeName + " Normal Rate", new BigDecimal(normalRate), new Date(), null, roomTypeId);
            
            System.out.print("Enter Published Rate per night for " + newTypeName + " $");
            double publishedRate = sc.nextDouble();            
            roomEntitySessionBeanRemote.createNewPublishedRate(newTypeName + " Published Rate", new BigDecimal(publishedRate), new Date(), null, roomTypeId);
        } catch (RoomTypeNotFoundException e) {
            System.out.println(e.getMessage());
        }
        
        System.out.println("\n" + newTypeName + " created successfully!\n");
    }
    
    private void viewRoomTypeDetails(){
        Scanner sc = new Scanner(System.in);
        List<RoomType> roomTypes = viewAllRoomTypes();
        if(roomTypes.isEmpty()){
            System.out.println("\nNo Room Types Available\n");
            return;
        }
        System.out.println("Input Room Type number to view/edit details or to delete");
        int typeNum = 0;
        try {
            typeNum = sc.nextInt();
        } catch (InputMismatchException e) {
            System.err.println("Invalid number, returning to main menu");
            return;
        }
        if(typeNum < 0 || typeNum >= roomTypes.size()){
            System.out.println("Invalid number, returning to main menu");
            return;
        }else{
            try {
                String typeName = roomTypes.get(typeNum).getTypeName();
                System.out.println(roomEntitySessionBeanRemote.viewRoomTypeDetails(typeName));
                
                System.out.println("(1) Edit Details");
                System.out.println("(2) Delete Room Type");
                System.out.println("(3) Return");
                System.out.print("Select an option> ");
                String response = sc.nextLine().trim();
            
                System.out.println();
                switch(response){
                    case "1":
                        updateRoomType(typeName);
                        break;
                        
                    case "2":
                        try {
                            if(roomEntitySessionBeanRemote.deleteRoomType(typeName)){
                                System.out.println("\n****Room Type Deleted****\n");
                            }else{
                                System.out.println("Room Type Currently In Use - Room Type marked as DISABLED");
                                System.out.println("Please try again when room type is not in use any more");
                            }
                        } catch (RoomTypeNotFoundException ex) {
                            System.err.println(ex.getMessage());
                        }
                        break;
                    case "3":
                        return;
                }
            } catch (RoomTypeNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    
    private void updateRoomType(String typeName){
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter updated description for " + typeName + "> ");
        String newDescription = sc.nextLine().trim();
        System.out.print("Enter updated amenities for " + typeName + "> ");
        String newAmenities = sc.nextLine().trim();
        System.out.print("Enter updated bed types for " + typeName + "> ");
        String newBeds = sc.nextLine().trim();
        System.out.print("Enter updated capacity for " + typeName + "> ");
        Integer newCap = null;
        try {
            newCap = sc.nextInt();
        } catch (Exception e) {
            System.err.println("Invalid capacity. Returning to menu.");
            return;
        }
        sc.nextLine();

        try{
            roomEntitySessionBeanRemote.updateRoomType(typeName, newDescription, newBeds, newCap, newAmenities);
            System.out.println("\n****Room Type Successfully Updated****\n");
        }catch(RoomTypeNotFoundException e){
            System.err.println(e.getMessage());
        }        
    }
    
    private List<RoomType> viewAllRoomTypes(){
        System.out.println("\n****All Room Types****");
        List<RoomType> roomTypes = roomEntitySessionBeanRemote.getRoomTypesByRanking();
        int i = 0;
        for(RoomType r: roomTypes){
            System.out.println("(" + i + ")" + r.getTypeName());
            i++;
        }
        System.out.println("****End of list****\n");
        return roomTypes;
    }
    
    
    
    private void roomManagement(){
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.println("\n****Room Type Management****");
            System.out.println("(1) Create new Room");
            System.out.println("(2) Update Room");
            System.out.println("(3) Delete Room");
            System.out.println("(4) View All Rooms");
            System.out.println("(5) Return");
            System.out.print("Select an option> ");
            String response = sc.nextLine().trim();
            
            System.out.println();
            switch(response){
                case "1":
                    createNewRoom();
                    break;
                    
                case "2":
                    updateRoom();
                    break;
                    
                case "3":
                    deleteRoom();
                    break;
                    
                case "4":
                    viewAllRooms();
                    break;
                    
                case "5":
                    return;
                                      
                default:
                    System.err.println("Please input a valid command.");
            }
            
        }
    }
    
    private void createNewRoom(){
        Scanner sc = new Scanner(System.in);
        try {
            System.out.println("****Create New Room****");
            System.out.print("Enter Floor Number of New Room> ");
            Integer floor = sc.nextInt();
            System.out.println();
            System.out.print("Enter Unit Number of New Room> ");
            Integer unit = sc.nextInt();
            System.out.println();
            System.out.print("Select Room Type> ");
            List<RoomType> roomTypes = viewAllRoomTypes();
            int typeSelection = sc.nextInt();
            System.out.println();
            String typeName = roomTypes.get(typeSelection).getTypeName();
            roomEntitySessionBeanRemote.createNewRoom(floor, unit, typeName);
            System.out.println("\n****New Room " + floor + "-" + unit +" Created****");
        } catch (RoomTypeNotFoundException | NullPointerException ex) {
            System.err.println(ex.getMessage());
        } catch (InputMismatchException e){
            System.err.println("Invalid number entered.");
        }
        
    }
    
    private void updateRoom(){
        Scanner sc = new Scanner(System.in);
        System.out.println("****Update Room****");
        System.out.print("Enter room floor number> ");
        String floor = sc.nextLine().trim();
        System.out.print("Enter room unit number> ");
        String unit = sc.nextLine().trim();
        
        String roomNumber = floor + "-" + unit;
        try{
            System.out.println(roomEntitySessionBeanRemote.viewRoomDetails(roomEntitySessionBeanRemote.retrieveRoomByRoomNumber(roomNumber)));
            System.out.print("Enter new room type:");
            List<RoomType> roomTypes = viewAllRoomTypes();
            int typeNum =  sc.nextInt();
            sc.nextLine();
            RoomType roomType = roomTypes.get(typeNum);
            System.out.println("Enter new room status \n(1)AVAILABLE \n(2)DISABLED");
            StatusEnum status;
            System.out.print("Select an option> ");
            String response = sc.nextLine().trim();
            
            System.out.println();
            switch(response){
                case "1":
                    status = StatusEnum.AVAILABLE;
                    break;
                case "2":
                    status = StatusEnum.UNAVAILABLE;
                    break;
                default:
                    System.err.println("Not an available status");
                    updateRoom(); //redo
                    return;
            }
            roomEntitySessionBeanRemote.updateRoom(roomNumber, roomType.getTypeName(), status);
            System.out.println("\n****Room Updated****\n");
        }catch(RoomNotFoundException | NullPointerException | InputMismatchException | RoomTypeNotFoundException e){
            System.err.println(e.getMessage());
            return;
        }
    }
    
    private void deleteRoom(){
        Scanner sc = new Scanner(System.in);
        System.out.println("****Delete Room****");
        System.out.print("Enter room floor number> ");
        String floor = sc.nextLine().trim();
        System.out.print("Enter room unit number> ");
        String unit = sc.nextLine().trim();
        
        String roomNumber = floor + "-" + unit;
        
        try{
            if(roomEntitySessionBeanRemote.deleteRoom(roomNumber)){
                System.out.println("\n****"+ roomNumber + " deleted successfully****\n");
            }else{
                System.out.println(roomNumber + " is currently in use. The room had been disabled instead.");
            }
        }catch(RoomNotFoundException e){
            System.err.println(e.getMessage());
            return;
        }
    }
    
    private void viewAllRooms(){
        List<Room> rooms = roomEntitySessionBeanRemote.retrieveAllRooms();
        System.out.println("\n****All Rooms****");
        for(Room room: rooms){
            System.out.println(roomEntitySessionBeanRemote.viewRoomDetails(room));
        }
        System.out.println("\n****End of list****\n");
    }
    
    private void viewExceptionReport(){
        Scanner sc = new Scanner(System.in);
        try {
            System.out.println("\n****View Exception Report****");
            System.out.print("Enter Date of Report to generate (DD/MM/YYYY)> ");
            String dateString = sc.nextLine().trim();
            Date date = dateFormat.parse(dateString);
            
            List<ExceptionReport> report = roomEntitySessionBeanRemote.getListOfExceptionReportsByDate(date);
            if(report.isEmpty()){
                System.out.println("\n****No Exception Report.****\n");
            }else{
                System.out.println("\n****Exception Reports****");
                for(ExceptionReport e:report){
                    System.out.println(e.getErrorReport());
                }
                System.out.println("****End of list****\n");
            }            
        } catch (ParseException ex) {
            System.err.println("Invalid date format. Please try again.");
        }
    }
    
    
    public void runHotelOperationModuleSalesManager(){
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.println("\n****Welcome to the Hotel Operations Module (Sales)****");
            System.out.println("(1) Create new Room Rate");
            System.out.println("(2) View/Delete/Update Room Rate Details");
            System.out.println("(3) View All Room Rates");
            System.out.println("(4) Return");
            System.out.print("Select an option> ");
            String response = sc.nextLine().trim();
            
            System.out.println();
            switch(response){
                case "1":
                    createNewRate();
                    break;
                    
                case "2":
                    viewRoomRateDetails();
                    break;
                    
                case "3":
                    viewAllRoomRates();
                    break;
                    
                case "4":
                    return;
 
                default:
                    System.err.println("Please enter a valid command");
            }           
        }
    }    
    
    private void createNewRate(){
        Scanner sc = new Scanner(System.in);
        System.out.println("****Create New Room Rate****");
        List<RoomType> roomTypesList = viewAllRoomTypes();
        System.out.print("Select Room type> ");
        int roomTypeIndex = 0;
        try {
            roomTypeIndex = sc.nextInt();
        } catch (InputMismatchException e) {
            System.err.println("Invalid selection. Returning to menu.");
            return;
        }
        sc.nextLine();
        RoomType roomType;
        if(roomTypeIndex < 0 || roomTypeIndex >= roomTypesList.size()){
            System.err.println("Invalid Room Type.");
            return;
        }else{
            roomType = roomTypesList.get(roomTypeIndex);
        }
        
        System.out.print("Enter name of new rate> ");
        String rateName = sc.nextLine().trim();
        
        System.out.print("Enter rate per night> $");
        BigDecimal ratePerNight = new BigDecimal(sc.nextDouble());
        
        System.out.print("Enter Start Date (dd/mm/yyyy)> ");
        Date startDate;
        try {
            startDate = dateFormat.parse(sc.nextLine().trim());
        } catch (ParseException ex) {
            System.err.println("Invalid date.");
            return;
        }
        
        System.out.print("Enter End Date (dd/mm/yyyy OR \"-\" if no end date)> ");
        String endDateString = sc.nextLine().trim();
        Date endDate;
        if(endDateString.equals("-")){
            endDate = null;
        }else{
            try{
                endDate = dateFormat.parse(endDateString);
            }catch(ParseException ex){
                System.err.println("Invalid date.");
                return;
            }
        }
        
        System.out.println("Enter Room Rate Type: \n(1)Published Rate \n(2)Normal Rate \n(3)Promotion Rate \n(4)Peak Rate");
        
        // do some changes 
        try {
            switch (sc.next()) {
                case "1":
                    roomEntitySessionBeanRemote.createNewPublishedRate(rateName, ratePerNight, startDate, endDate, roomType.getTypeId());
                    break;
                case "2":
                    roomEntitySessionBeanRemote.createNewNormalRate(rateName, ratePerNight, startDate, endDate, roomType.getTypeId());
                    break;
                case "3":
                    roomEntitySessionBeanRemote.createNewPromotionRate(rateName, ratePerNight, startDate, endDate, roomType.getTypeId());
                    break;
                case "4":
                    roomEntitySessionBeanRemote.createNewPeakRate(rateName, ratePerNight, startDate, endDate, roomType.getTypeId());
            }
        } catch (RoomTypeNotFoundException e) {
            System.err.println(e.getMessage());
        }
        System.out.println("\n****New Room Rate Created****\n");
    }
    
    private void viewRoomRateDetails(){
        Scanner sc = new Scanner(System.in);
        System.out.println("\n****View Room Rate Details****");
        List<RoomRate> roomRates = viewAllRoomRates();
        System.out.print("Select number of Room Rate to view details> ");
        int rateIndex = 0;
        try {
            rateIndex = sc.nextInt();
            sc.nextLine();
        } catch (InputMismatchException e) {
            System.err.println("Invalid selection. Returning to menu.");
            return;
        }
        if(rateIndex < 0 || rateIndex >= roomRates.size()){
            System.err.println("Invalid number. Please try again");
            return;
        }
        RoomRate roomRate = roomRates.get(rateIndex);
        System.out.println("****Room Rate Details****");
        System.out.println(roomRate.getDetails());
        
        System.out.println();
        System.out.println("(1)Update Room Rate\n(2)Delete Room Rate\n(3)Return");
        
        switch(sc.next()){
            case "1":
                updateRoomRate(roomRate);
                break;
                
            case "2":
                deleteRoomRate(roomRate);
                break;
            
            case "3":
                return;
            
            default:
                System.err.println("Invalid command");
                return;           
        }
    }
    
    private void updateRoomRate(RoomRate roomRate){
        Scanner sc = new Scanner(System.in);
        try {

            System.out.println("\n****Update Room Rate****");
            System.out.print("Enter new rate per night: \n$");
            BigDecimal newRatePerNight = new BigDecimal(sc.nextDouble());
            System.out.println("Enter new start date (dd/mm/yyyy)");
            Date newStartDate = dateFormat.parse(sc.next());
            Date endDate;
            
            //Normal / Published rates will always have no end date
            if(roomRate.getRateType().equals(RateTypeEnum.NORMAL) || roomRate.getRateType().equals(RateTypeEnum.PUBLISHED)){
                endDate = null;
            }else{
                System.out.println("Enter new end date (dd/mm/yyyy OR \"-\" if no end date ");
                String endDateString = sc.next();
                if(endDateString.equals("-")){
                    endDate = null;
                }else{
                    endDate = dateFormat.parse(endDateString);
                }
            }
            
            System.out.println("Select Room Rate Status: \n(1)Disable Room Rate \n(2)Make Room Rate Available");
            StatusEnum status;
            switch(sc.next()){
                case "1":
                    status = StatusEnum.UNAVAILABLE;
                    break;
                case "2":
                    status = StatusEnum.AVAILABLE;
                    break;
                default:
                    System.err.println("Invalid status");
                    return;
            }
            
            roomEntitySessionBeanRemote.updateRoomRate(roomRate.getRateId(), newRatePerNight, newStartDate, endDate, status);
            System.out.println("****Room Rate Updated****\n");
        } catch (ParseException ex) {
            System.err.println("Invalid date entered");
            return;
        } catch (RoomRateNotFoundException ex) {
            System.err.println(ex.getMessage());
        }
        
    }
    
    private void deleteRoomRate(RoomRate roomRate){
        try {
            if(roomEntitySessionBeanRemote.deleteRoomRate(roomRate.getRateId())){
                System.out.println("****Room rate deleted****\n");
            }else{
                System.out.println("Room rate is currently in use and had been disabled instead.\n");
            }
        } catch (RoomRateNotFoundException | LastAvailableRateException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    
    private List<RoomRate> viewAllRoomRates(){
        System.out.println("\n****All Room Rates****");
        List<RoomRate> roomRates = roomEntitySessionBeanRemote.retrieveAllRoomRates();
        int i = 0;
        for(RoomRate r: roomRates){
            System.out.println("(" + i + ")" + r.getRateName());
            i++;
        }
        System.out.println("****End of list****\n");
        
        return roomRates;
    }
    
}
