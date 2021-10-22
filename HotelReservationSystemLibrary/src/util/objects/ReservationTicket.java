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
public class ReservationTicket implements Serializable {

    private Date startDate;
    private Date endDate;
    private ArrayList<RoomType> availableRoomTypes;
    private ArrayList<BigDecimal> respectiveTotalBill;
    private ArrayList<Integer> respectiveNumberOfRoomsRemaining;
    private ArrayList<Integer> respectiveNumberReserved;

    public ReservationTicket(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.availableRoomTypes = new ArrayList<>();
        this.respectiveTotalBill = new ArrayList<>();
        this.respectiveNumberOfRoomsRemaining = new ArrayList<>();
        this.respectiveNumberReserved = new ArrayList<>();
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

    public ArrayList<RoomType> getAvailableRoomTypes() {
        return availableRoomTypes;
    }

    public void setAvailableRoomTypes(ArrayList<RoomType> availableRoomTypes) {
        this.availableRoomTypes = availableRoomTypes;
    }

    public ArrayList<BigDecimal> getRespectiveTotalBill() {
        return respectiveTotalBill;
    }

    public void setRespectiveTotalBill(ArrayList<BigDecimal> respectiveTotalBill) {
        this.respectiveTotalBill = respectiveTotalBill;
    }

    public ArrayList<Integer> getRespectiveNumberOfRoomsRemaining() {
        return respectiveNumberOfRoomsRemaining;
    }

    public void setRespectiveNumberOfRoomsRemaining(ArrayList<Integer> respectiveNumberOfRoomsRemaining) {
        this.respectiveNumberOfRoomsRemaining = respectiveNumberOfRoomsRemaining;
    }

    public ArrayList<Integer> getRespectiveNumberReserved() {
        return respectiveNumberReserved;
    }

    public void setRespectiveNumberReserved(ArrayList<Integer> respectiveNumberReserved) {
        this.respectiveNumberReserved = respectiveNumberReserved;
    }

}
