/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

/**
 *
 * @author wong-
 */
@Entity
public class Partner implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partnerId;

    @Column(unique = true, nullable = false) 
    @Size(min = 1, max = 32)
    private String partnerName;
    
    @Column(unique = true, nullable = false)
    @Size(min = 5, max = 32)
    private String username;
    
    @Size(min = 5, max = 32)
    @Column(nullable = false)
    private String password;
    
    @OneToMany(mappedBy = "reservedByPartner")
    private ArrayList<ReservationRecord> reservationRecords;

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
     * @return the partnerName
     */
    public String getPartnerName() {
        return partnerName;
    }

    /**
     * @param partnerName the partnerName to set
     */
    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }


    public Partner() {
        reservationRecords = new ArrayList<>();
    }

    public Partner(String partnerName, String username, String password) {
        this.partnerName = partnerName;
        this.username = username;
        this.password = password;
    }
    
    
    
    
    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (partnerId != null ? partnerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the partnerId fields are not set
        if (!(object instanceof Partner)) {
            return false;
        }
        Partner other = (Partner) object;
        if ((this.partnerId == null && other.partnerId != null) || (this.partnerId != null && !this.partnerId.equals(other.partnerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Partner[ id=" + partnerId + " ]";
    }
    
}
