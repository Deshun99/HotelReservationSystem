/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import util.enumeration.IsOccupiedEnum;
import util.enumeration.StatusEnum;

/**
 *
 * @author wong-
 */
@Entity
public class Room implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;
    
    @Column(nullable = false)
    private Integer floor;
    
    @Column(nullable = false)
    private Integer unit;
    
    @Column(unique = true)
    private String roomNumber;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date isOccupiedUntil;
    
    @Column(nullable = false)
    private StatusEnum status;
    
    @Column(nullable = false)
    private IsOccupiedEnum occupancy;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private RoomType roomType;

    
    @OneToMany(mappedBy = "assignedRoom", cascade ={CascadeType.REMOVE})
    private ArrayList<ReservationRecord> reservationRecords;

    public Room() {
    }

    public Room(Integer floor, Integer unit, RoomType roomType) {
        this.floor = floor;
        this.unit = unit;
        this.roomNumber = floor + "-" + unit;
        this.status = StatusEnum.AVAILABLE;
        this.occupancy = IsOccupiedEnum.UNOCCUPIED;
        this.roomType = roomType;
    }
    

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
     * @return the reservationRecords
     */
    public ArrayList<ReservationRecord> getReservationRecords() {
        return reservationRecords;
    }

    /**
     * @param reservationRecords the reservationRecords to set
     */
    public void setReservationRecords(ArrayList<ReservationRecord> reservationRecords) {
        this.reservationRecords = reservationRecords;
    }
    /**
     * @return the floor
     */
    public Integer getFloor() {
        return floor;
    }

    /**
     * @param floor the floor to set
     */
    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    /**
     * @return the unit
     */
    public Integer getUnit() {
        return unit;
    }

    /**
     * @param unit the unit to set
     */
    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    /**
     * @return the roomNumber
     */
    public String getRoomNumber() {
        return roomNumber;
    }

    /**
     * @param roomNumber the roomNumber to set
     */
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    /**
     * @return the isOccupiedUntil
     */
    public Date getIsOccupiedUntil() {
        return isOccupiedUntil;
    }

    /**
     * @param isOccupiedUntil the isOccupiedUntil to set
     */
    public void setIsOccupiedUntil(Date isOccupiedUntil) {
        this.isOccupiedUntil = isOccupiedUntil;
    }

    /**
     * @return the status
     */
    public StatusEnum getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    /**
     * @return the occupancy
     */
    public IsOccupiedEnum getOccupancy() {
        return occupancy;
    }

    /**
     * @param occupancy the occupancy to set
     */
    public void setOccupancy(IsOccupiedEnum occupancy) {
        this.occupancy = occupancy;
    }


    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomId != null ? roomId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomId fields are not set
        if (!(object instanceof Room)) {
            return false;
        }
        Room other = (Room) object;
        if ((this.roomId == null && other.roomId != null) || (this.roomId != null && !this.roomId.equals(other.roomId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Room[ id=" + roomId + " ]";
    }
    
}
