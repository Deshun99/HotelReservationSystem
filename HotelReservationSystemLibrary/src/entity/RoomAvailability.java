/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author wong-
 */
@Entity
public class RoomAvailability implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date availabiltyRecordDate;
    
    @Column(nullable = false)
    private Integer totalReservations;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private RoomType roomType;
    
    public RoomAvailability() {
    }

    public RoomAvailability(Date date, RoomType roomType) {
        this.availabiltyRecordDate = date;
        this.totalReservations = 0;
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
    
    public Integer getAvailableRooms(){
        return roomType.getTotalRooms() - totalReservations;
    }
    
    public Boolean getAvailability(){
        int numberOfAvailableRm = roomType.getTotalRooms() - totalReservations;
        if(numberOfAvailableRm > 0){
            return true;
        } else{
            return false;
        }
    }

    /**
     * @return the availabiltyRecordDate
     */
    public Date getAvailabiltyRecordDate() {
        return availabiltyRecordDate;
    }

    /**
     * @param availabiltyRecordDate the availabiltyRecordDate to set
     */
    public void setAvailabiltyRecordDate(Date availabiltyRecordDate) {
        this.availabiltyRecordDate = availabiltyRecordDate;
    }

    /**
     * @return the totalReservations
     */
    public Integer getTotalReservations() {
        return totalReservations;
    }

    /**
     * @param totalReservations the totalReservations to set
     */
    public void setTotalReservations(Integer totalReservations) {
        this.totalReservations = totalReservations;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RoomAvailability)) {
            return false;
        }
        RoomAvailability other = (RoomAvailability) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AvailabilityRecord[ id=" + id + " ]";
    }
    
    public void addOneReservation(){
        this.totalReservations++;
    }
    
}
