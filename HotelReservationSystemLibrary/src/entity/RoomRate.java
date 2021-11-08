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
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import util.enumeration.RateTypeEnum;
import util.enumeration.StatusEnum;

/**
 *
 * @author wong-
 */
@Entity
public class RoomRate implements Serializable {

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
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rateId;
    
    @Column(scale = 2)
    private BigDecimal ratePerNight;
    
    @Column(unique = true, nullable = false)
    private String rateName;
    
    @Column(nullable = false)
    private StatusEnum status;
    
    @Column(nullable = false)
    private RateTypeEnum rateType;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(nullable = false)
    private Date startDate;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date endDate;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private RoomType roomType;

    public RoomRate() {
    }

    public RoomRate(BigDecimal ratePerNight, String rateName, RateTypeEnum rateType, Date startDate, Date endDate) {
        this.ratePerNight = ratePerNight;
        this.rateName = rateName;
        this.rateType = rateType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = StatusEnum.AVAILABLE;
    }
    

    /**
     * @return the ratePerNight
     */
    public BigDecimal getRatePerNight() {
        return ratePerNight;
    }

    /**
     * @param ratePerNight the ratePerNight to set
     */
    public void setRatePerNight(BigDecimal ratePerNight) {
        this.ratePerNight = ratePerNight;
    }

    /**
     * @return the rateName
     */
    public String getRateName() {
        return rateName;
    }

    /**
     * @param rateName the rateName to set
     */
    public void setRateName(String rateName) {
        this.rateName = rateName;
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
     * @return the rateType
     */
    public RateTypeEnum getRateType() {
        return rateType;
    }

    /**
     * @param rateType the rateType to set
     */
    public void setRateType(RateTypeEnum rateType) {
        this.rateType = rateType;
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

    public Long getRateId() {
        return rateId;
    }

    public void setRateId(Long rateId) {
        this.rateId = rateId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rateId != null ? rateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the rateId fields are not set
        if (!(object instanceof RoomRate)) {
            return false;
        }
        RoomRate other = (RoomRate) object;
        if ((this.rateId == null && other.rateId != null) || (this.rateId != null && !this.rateId.equals(other.rateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomRate[ id=" + rateId + " ]";
    }
    
    public Boolean checkAvailability(Date onDate){
        
        //Check the availability based on two conditions
        //1. no end date 
        //2. booking date is after startDate and before endDate
        if(endDate == null || startDate.before(onDate) && endDate.after(onDate)) {
            return true;
        }
        return false;
        
    }
    
    public String getDetails(){
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        String endDateString;
        if(endDate == null){
            endDateString = "-";
        }else{
            endDateString = dateFormat.format(endDate);
        }
        
        return "Rate name: " + rateName + "\n" +
                "Rate per Night: " + ratePerNight + "\n" +
                "Rate Type: " + rateType + "\n" +
                "Start Date: " + dateFormat.format(startDate) + "\n" + 
                "End Date: " + endDateString + "\n" +
                "Status: " + status;
    }
    
}
