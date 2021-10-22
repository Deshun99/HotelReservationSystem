/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import util.enumeration.StatusEnum;

/**
 *
 * @author wong-
 */
@Entity
public class RoomType implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long typeId;
    
    @Column(length = 100, nullable = false)
    private String typeName;
    
    private Integer totalRooms;
    
    @Column(length = 140, nullable = false)
    private String description;
    
    @Column(length = 140, nullable = false)
    private String bedType;
    
    private Integer capacity;
    
    @Column(length = 140, nullable = false)
    private String amenities;
    
    @Column(nullable = false)
    private Integer ranking;
    
    @Column(nullable = false)
    private StatusEnum status;
    
    @OneToMany(mappedBy = "roomType", cascade = {CascadeType.REMOVE})
    private ArrayList<Room> rooms;
    
    @OneToMany(mappedBy = "roomType", cascade = {CascadeType.REMOVE})
    private ArrayList<RoomRate> roomRate;
    
    @OneToMany(mappedBy = "roomType", cascade = {CascadeType.REMOVE})
    private ArrayList<RoomAvailability> roomAvailabilities;

    public RoomType() {
        rooms = new ArrayList<>();
        roomRate = new ArrayList<>();
        roomAvailabilities = new ArrayList<>();
    }

    public RoomType(String typeName, String description, String bedType, Integer capacity, String amenities) {
        this.typeName = typeName;
        this.description = description;
        this.bedType = bedType;
        this.capacity = capacity;
        this.amenities = amenities;
        this.totalRooms = 0;
        this.status = StatusEnum.AVAILABLE;
    }
    

    /**
     * @return the rooms
     */
    public ArrayList<Room> getRooms() {
        return rooms;
    }

    /**
     * @param rooms the rooms to set
     */
    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    /**
     * @return the roomRate
     */
    public ArrayList<RoomRate> getRoomRate() {
        return roomRate;
    }

    /**
     * @param roomRate the roomRate to set
     */
    public void setRoomRate(ArrayList<RoomRate> roomRate) {
        this.roomRate = roomRate;
    }

    /**
     * @return the roomAvailabilities
     */
    public ArrayList<RoomAvailability> getRoomAvailabilities() {
        return roomAvailabilities;
    }

    /**
     * @param roomAvailabilities the roomAvailabilities to set
     */
    public void setRoomAvailabilities(ArrayList<RoomAvailability> roomAvailabilities) {
        this.roomAvailabilities = roomAvailabilities;
    }

    /**
     * @return the typeName
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * @param typeName the typeName to set
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
     * @return the totalRooms
     */
    public Integer getTotalRooms() {
        return totalRooms;
    }

    /**
     * @param totalRooms the totalRooms to set
     */
    public void setTotalRooms(Integer totalRooms) {
        this.totalRooms = totalRooms;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the bedType
     */
    public String getBedType() {
        return bedType;
    }

    /**
     * @param bedType the bedType to set
     */
    public void setBedType(String bedType) {
        this.bedType = bedType;
    }

    /**
     * @return the capacity
     */
    public Integer getCapacity() {
        return capacity;
    }

    /**
     * @param capacity the capacity to set
     */
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    /**
     * @return the amenities
     */
    public String getAmenities() {
        return amenities;
    }

    /**
     * @param amenities the amenities to set
     */
    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    /**
     * @return the ranking
     */
    public Integer getRanking() {
        return ranking;
    }

    /**
     * @param ranking the ranking to set
     */
    public void setRanking(Integer ranking) {
        this.ranking = ranking;
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
    
    public void addRoom(Room room){
        rooms.add(room);
        totalRooms++;
    }
    
    public void removeRoom(Room room){
        rooms.remove(room);
        totalRooms--;
    }
    
    public void addNewRoomRate(RoomRate newRoomRate) {
        roomRate.add(newRoomRate);
    }


    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }
    
    public void upgradeRank(){
        ranking--;
    }
    
    public void downgradeRank(){
        ranking++;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (typeId != null ? typeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the typeId fields are not set
        if (!(object instanceof RoomType)) {
            return false;
        }
        RoomType other = (RoomType) object;
        if ((this.typeId == null && other.typeId != null) || (this.typeId != null && !this.typeId.equals(other.typeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomType[ id=" + typeId + " ]";
    }
    
    public void addNewRoomAvailability(RoomAvailability availability) {
        this.roomAvailabilities.add(availability);
    }
    
}
