/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    
    private Integer floor;
    
    private Integer unit;
    
    private String roomNumber;
    
    private Date isOccupiedTo;
    
    private StatusEnum status;
    
    private IsOccupiedEnum occupancy;

    public Room() {
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
     * @return the isOccupiedTo
     */
    public Date getIsOccupiedTo() {
        return isOccupiedTo;
    }

    /**
     * @param isOccupiedTo the isOccupiedTo to set
     */
    public void setIsOccupiedTo(Date isOccupiedTo) {
        this.isOccupiedTo = isOccupiedTo;
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
