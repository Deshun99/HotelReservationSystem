/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Guest;
import entity.ReservationRecord;
import java.util.ArrayList;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author wong-
 */
@Stateless
public class GuestEntitySessionBean implements GuestEntitySessionBeanRemote, GuestEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    public GuestEntitySessionBean() {
    
    }
    
    @Override
    public void registerNewGuest(String name, String username, String password, String emailAddress){
        
        Guest newGuest = new Guest(name, username, password, emailAddress);
        em.persist(newGuest);
        
    }
    
    @Override
    public Guest guestLogin(String username, String password) throws InvalidLoginCredentialException{
        Query q = em.createQuery("SELECT g FROM Guest g WHERE g.username = :username");
        q.setParameter("username", username);
        try{
            Guest guest = (Guest) q.getSingleResult();
            if(guest.getPassword().equals(password)){
                ArrayList<ReservationRecord> reservations = guest.getReservationRecords();
                for(ReservationRecord r : reservations){
                    r.getBill();
                    r.getStartDate();
                    r.getEndDate();
                    r.getRoomType().getTypeName();
                }
                return guest;
            }else{
                throw new InvalidLoginCredentialException("Invalid Login Credential");
            }
        }catch(NoResultException | NonUniqueResultException e){
            throw new InvalidLoginCredentialException("Invalid Login Credential");
        }
    }

    
}
