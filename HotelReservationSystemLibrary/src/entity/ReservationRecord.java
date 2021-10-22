/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;

/**
 *
 * @author wong-
 */
@Entity
public class ReservationRecord implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationRecordId;
    
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date startDate;
    
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date endDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkInTime;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkOutTime;
    
    @Column(scale = 2)
    private BigDecimal bill;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date reservedOn;
    
    @Email
    @Column(nullable = false)
    private String guestEmail;
    
    @OneToOne
    @JoinColumn(nullable = false)
    private RoomType roomType;
    
    @ManyToOne
    private Room assignedRoom;
    
    @ManyToOne
    private Guest reservedByGuest;
    
    @ManyToOne
    private Partner reservedByPartner;
    
    @OneToOne(mappedBy = "reservation", cascade = {CascadeType.REMOVE})
    private ExceptionReport exception;

    /**
     * @return the roomType
     */
    public RoomType getRoomType() {
        return roomType;
    }

    /**
     * @param roomType the roomType to set
     */
    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    /**
     * @return the assignedRoom
     */
    public Room getAssignedRoom() {
        return assignedRoom;
    }

    /**
     * @param assignedRoom the assignedRoom to set
     */
    public void setAssignedRoom(Room assignedRoom) {
        this.assignedRoom = assignedRoom;
    }

    /**
     * @return the reservedByGuest
     */
    public Guest getReservedByGuest() {
        return reservedByGuest;
    }

    /**
     * @param reservedByGuest the reservedByGuest to set
     */
    public void setReservedByGuest(Guest reservedByGuest) {
        this.reservedByGuest = reservedByGuest;
    }

    /**
     * @return the reservedByPartner
     */
    public Partner getReservedByPartner() {
        return reservedByPartner;
    }

    /**
     * @param reservedByPartner the reservedByPartner to set
     */
    public void setReservedByPartner(Partner reservedByPartner) {
        this.reservedByPartner = reservedByPartner;
    }

    /**
     * @return the exception
     */
    public ExceptionReport getException() {
        return exception;
    }

    /**
     * @param exception the exception to set
     */
    public void setException(ExceptionReport exception) {
        this.exception = exception;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the checkInTime
     */
    public Date getCheckInTime() {
        return checkInTime;
    }

    /**
     * @param checkInTime the checkInTime to set
     */
    public void setCheckInTime(Date checkInTime) {
        this.checkInTime = checkInTime;
    }

    /**
     * @return the checkOutTime
     */
    public Date getCheckOutTime() {
        return checkOutTime;
    }

    /**
     * @param checkOutTime the checkOutTime to set
     */
    public void setCheckOutTime(Date checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    /**
     * @return the bill
     */
    public BigDecimal getBill() {
        return bill;
    }

    /**
     * @param bill the bill to set
     */
    public void setBill(BigDecimal bill) {
        this.bill = bill;
    }

    /**
     * @return the reservedOn
     */
    public Date getReservedOn() {
        return reservedOn;
    }

    /**
     * @param reservedOn the reservedOn to set
     */
    public void setReservedOn(Date reservedOn) {
        this.reservedOn = reservedOn;
    }

    /**
     * @return the guestEmail
     */
    public String getGuestEmail() {
        return guestEmail;
    }

    /**
     * @param guestEmail the guestEmail to set
     */
    public void setGuestEmail(String guestEmail) {
        this.guestEmail = guestEmail;
    }


    public Long getReservationRecordId() {
        return reservationRecordId;
    }

    public void setReservationRecordId(Long reservationRecordId) {
        this.reservationRecordId = reservationRecordId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservationRecordId != null ? reservationRecordId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reservationRecordId fields are not set
        if (!(object instanceof ReservationRecord)) {
            return false;
        }
        ReservationRecord other = (ReservationRecord) object;
        if ((this.reservationRecordId == null && other.reservationRecordId != null) || (this.reservationRecordId != null && !this.reservationRecordId.equals(other.reservationRecordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ReservationRecord[ id=" + reservationRecordId + " ]";
    }
    
    public String getEndDateAsString() {
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        return dateFormat.format(endDate);
    }
    
    public String getStartDateAsString() {
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        return dateFormat.format(startDate);
    }
    
    public ReservationRecord() {
        Calendar cal = Calendar.getInstance();
        this.reservedOn = cal.getTime(); //current timestamp
    }
    
    //HORS Reservation client
    public ReservationRecord(RoomType roomType, Date startDate, Date endDate, Guest reservedByGuest) {
        this();
        
        this.roomType = roomType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.guestEmail = reservedByGuest.getEmailAddress();
        this.reservedByGuest = reservedByGuest;
    }
    
    //Holiday Reservation System
    public ReservationRecord(RoomType roomType, Date startDate, Date endDate, String guestEmail, Partner reservedByPartner) {
        this();
        
        this.roomType = roomType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.guestEmail = guestEmail;
        this.reservedByPartner = reservedByPartner;
    }
    
    //Front Office Module
    public ReservationRecord(RoomType roomType, Date startDate, Date endDate, String guestEmail) {
        this();
        
        this.roomType = roomType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.guestEmail = guestEmail;
    }
}
