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
import javax.ejb.Local;
import util.exception.EntityMismatchException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ReservationRecordNotFoundException;

/**
 *
 * @author wong-
 */
@Local
public interface PartnerEntitySessionBeanLocal {
    
    public String viewReservationDetail(Long reservationId, Long partnerId) throws EntityMismatchException, ReservationRecordNotFoundException;

    public ArrayList<ReservationRecord> retrieveAllPartnerReservations(Long partnerId);

    public Partner retrievePartnerById(Long id);

    public Long partnerLogin(String username, String password) throws InvalidLoginCredentialException;

    public List<Partner> retrieveAllPartners();

    public void createNewPartner(String partnerName, String username, String password, String emailAddress);
    
}
