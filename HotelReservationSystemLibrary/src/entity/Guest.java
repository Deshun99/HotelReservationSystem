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
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

/**
 *
 * @author wong-
 */
@Entity
public class Guest implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long guestId;
    
    @Column(nullable = false)
    @Size(min = 1, max = 32)
    private String guestName;
    
    @Column(nullable = false, unique = true)
    @Size(min = 5, max = 32)
    private String username;
            
    @Column(nullable = false)
    @Size(min = 5, max = 32)
    private String password;
    
    @Column(nullable = false, unique = true)
    @Email(message = "Please enter a valid email address")
    private String emailAddress;
    
    @OneToMany(mappedBy = "reservedByGuest")
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

    public Guest() {
    }

    public Guest(String guestName, String username, String password, String emailAddress) {
        this.guestName = guestName;
        this.username = username;
        this.password = password;
        this.emailAddress = emailAddress;
    }

    
    /**
     * @return the guestName
     */
    public String getGuestName() {
        return guestName;
    }

    /**
     * @param guestName the guestName to set
     */
    public void setGuestName(String guestName) {
        this.guestName = guestName;
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

    /**
     * @return the emailAddress
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * @param emailAddress the emailAddress to set
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Long getGuestId() {
        return guestId;
    }

    public void setGuestId(Long guestId) {
        this.guestId = guestId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (guestId != null ? guestId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the guestId fields are not set
        if (!(object instanceof Guest)) {
            return false;
        }
        Guest other = (Guest) object;
        if ((this.guestId == null && other.guestId != null) || (this.guestId != null && !this.guestId.equals(other.guestId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Guest's Name: " + this.guestName + "\nGuest's Email Address: " + this.emailAddress + "\n";
    }
    
}
