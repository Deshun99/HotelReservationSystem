/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.objects;

import entity.RoomType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author wong-
 */
public class ReservationTicketWrapper implements Serializable {
    private Date startDate;
    private Date endDate;
    private ArrayList<String> reservationDescriptions = new ArrayList<>();
    private ArrayList<String> respectiveRoomTypeName = new ArrayList<>();
    private ArrayList<Integer> respectiveRoomRemaining = new ArrayList<>();
    private ArrayList<BigDecimal> respectiveTotalBill = new ArrayList<>(); 
    private ArrayList<Integer> respectiveNumberToReserve = new ArrayList<>();

    public ReservationTicketWrapper() {
    }
    
    public ReservationTicketWrapper(ReservationTicket ticket){
        for(int i = 0; i < ticket.getAvailableRoomTypes().size(); i++){
            RoomType type = ticket.getAvailableRoomTypes().get(i);
            String descriptions;
            descriptions = "(" + i + ")" + type.getTypeName() + "\n" +
                            type.getDescription() + "\n" +
                            "Amenities: " + type.getAmenities() + "\n" +
                            "Capacity: " + type.getCapacity() + "\n" +
                            "Rooms Available: " + ticket.getRespectiveNumberOfRoomsRemaining().get(i) + "\n" +
                            "Cost: " + ticket.getRespectiveTotalBill().get(i);
            startDate = ticket.getStartDate();
            endDate = ticket.getEndDate();
            reservationDescriptions.add(descriptions);
            respectiveRoomTypeName.add(type.getTypeName());
            respectiveTotalBill.add(ticket.getRespectiveTotalBill().get(i));
            respectiveRoomRemaining.add(ticket.getRespectiveNumberOfRoomsRemaining().get(i));
        }
    }

    public ArrayList<Integer> getRespectiveRoomRemaining() {
        return respectiveRoomRemaining;
    }

    public void setRespectiveRoomRemaining(ArrayList<Integer> respectiveRoomRemaining) {
        this.respectiveRoomRemaining = respectiveRoomRemaining;
    }

    public ArrayList<String> getReservationDescriptions() {
        return reservationDescriptions;
    }

    public void setReservationDescriptions(ArrayList<String> reservationDescriptions) {
        this.reservationDescriptions = reservationDescriptions;
    }

    public ArrayList<String> getRespectiveRoomTypeName() {
        return respectiveRoomTypeName;
    }

    public void setRespectiveRoomTypeName(ArrayList<String> respectiveRoomTypeName) {
        this.respectiveRoomTypeName = respectiveRoomTypeName;
    }

    public ArrayList<Integer> getRespectiveNumberToReserve() {
        return respectiveNumberToReserve;
    }

    public void setRespectiveNumberToReserve(ArrayList<Integer> respectiveNumberToReserve) {
        this.respectiveNumberToReserve = respectiveNumberToReserve;
    }

    public ArrayList<BigDecimal> getRespectiveTotalBill() {
        return respectiveTotalBill;
    }

    public void setRespectiveTotalBill(ArrayList<BigDecimal> respectiveTotalBill) {
        this.respectiveTotalBill = respectiveTotalBill;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
}
