/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Partner;
import entity.ReservationRecord;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.EntityMismatchException;
import util.exception.InvalidLoginCredentialException;
import util.exception.PartnerNotFoundException;
import util.exception.ReservationRecordNotFoundException;

/**
 *
 * @author wong-
 */
@Stateless
public class PartnerEntitySessionBean implements PartnerEntitySessionBeanRemote, PartnerEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    public PartnerEntitySessionBean() {
    }

    @Override
    public void createNewPartner(String partnerName, String username, String password, String emailAddress){
        Partner partner = new Partner(partnerName, username, password, emailAddress);
        em.persist(partner);
        
    }
    
    @Override
    public List<Partner> retrieveAllPartners(){
        Query query = em.createQuery("SELECT p FROM Partner p");
        List<Partner> list = query.getResultList();
        
        for(Partner p: list){
            p.getPartnerId();
            p.getPartnerName();
        }
        
        return list;
    }
    
    public Long partnerLogin(String username, String password) throws InvalidLoginCredentialException{
        try{
            Partner partner = retrievePartnerByUsername(username);
            if(partner.getPassword().equals(password)){
                return partner.getPartnerId();
            }else{
                throw new InvalidLoginCredentialException("Invalid Login Credentials");
            }

        }catch(PartnerNotFoundException ex){
            throw new InvalidLoginCredentialException("Invalid Login Credentials");
        }
    }
    

    private Partner retrievePartnerByUsername(String username) throws PartnerNotFoundException{
        Query q = em.createQuery("SELECT p FROM Partner p WHERE p.username = :username");
        q.setParameter("username", username);
        
        try{
            return (Partner) q.getSingleResult();
        }catch(NonUniqueResultException | NoResultException e){
            throw new PartnerNotFoundException("Partner not found.");
        }
    }
    
    @Override
    public Partner retrievePartnerById(Long id){
        return em.find(Partner.class, id); 
    }
    
    @Override
    public ArrayList<ReservationRecord> retrieveAllPartnerReservations(Long partnerId){
        Partner partner = em.find(Partner.class, partnerId);
        ArrayList<ReservationRecord> reservationRecords = partner.getReservationRecords();
        for(ReservationRecord r : reservationRecords){
            r.getReservationRecordId();
            r.getRoomType();
            r.getStartDate();
            r.getEndDate();
        }
        return reservationRecords;
    }
    
    @Override
    public String viewReservationDetail(Long reservationId, Long partnerId) throws EntityMismatchException, ReservationRecordNotFoundException{
        ReservationRecord reservation = em.find(ReservationRecord.class, reservationId);
        if(reservation == null){
            throw new ReservationRecordNotFoundException("Reservation not found");
        }else if(reservation.getReservedByPartner() == null){
            throw new EntityMismatchException("Partner ID Provided does not match with Partner ID of Reservation Record.");
        }else if(reservation.getReservedByPartner().getPartnerId().equals(partnerId)){
            String details = "Reservation id: " + reservationId +
                            "\nReserved by: " + reservation.getReservedByPartner() +
                            "\nStart date: " + reservation.getStartDateAsString()+
                            "\nEnd date: " + reservation.getEndDateAsString();
            return details;
        }else{
            throw new EntityMismatchException("Partner ID Provided does not match with Partner ID of Reservation Record.");
        }
    }

    
}
