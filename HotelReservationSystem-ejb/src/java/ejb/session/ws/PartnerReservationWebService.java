/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateful.RoomReservationControllerLocal;
import ejb.session.stateless.PartnerEntitySessionBeanLocal;
import ejb.session.stateless.ReservationEntitySessionBeanLocal;
import entity.Partner;
import entity.ReservationRecord;
import java.util.ArrayList;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.jws.WebService;
import util.exception.EntityMismatchException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ReservationRecordNotFoundException;
import util.objects.ReservationTicket;
import util.objects.ReservationTicketWrapper;


/**
 *
 * @author wong-
 */
@WebService(serviceName = "PartnerReservationWebService")
@Stateless
@LocalBean
public class PartnerReservationWebService {

    @EJB(name = "RoomReservationControllerLocal")
    private RoomReservationControllerLocal roomReservationControllerLocal;

    @EJB(name = "PartnerEntitySessionBeanLocal")
    private PartnerEntitySessionBeanLocal partnerEntitySessionBeanLocal;

    @EJB(name = "ReservationEntitySessionBeanLocal")
    private ReservationEntitySessionBeanLocal reservationEntitySessionBeanLocal;
    
    

    public Partner partnerLogin(String username, String password) throws InvalidLoginCredentialException {
        Long partnerId = partnerEntitySessionBeanLocal.partnerLogin(username, password);
        return partnerEntitySessionBeanLocal.retrievePartnerById(partnerId);
    }
    
    public ArrayList<ReservationRecord> viewAllPartnerReservation(Long partnerId) {
        ArrayList<ReservationRecord> reservations = partnerEntitySessionBeanLocal.retrieveAllPartnerReservations(partnerId);
        return reservations;
    }
    
    public String viewReservationDetails(Long reservationId, Long partnerId) throws EntityMismatchException, ReservationRecordNotFoundException {
        return partnerEntitySessionBeanLocal.viewReservationDetail(reservationId, partnerId);
    }
    
    public ReservationTicketWrapper searchRoom(Date startDate, Date endDate) {
        ReservationTicket reservationTicket = reservationEntitySessionBeanLocal.searchRooms(startDate, endDate, false);
        return new ReservationTicketWrapper(reservationTicket);
    }
    
    public ReservationTicket unwrapRoomTicket(ReservationTicketWrapper reservationTicketWrapper) {
        ReservationTicket reservationTicket = reservationEntitySessionBeanLocal.unwrapTicketWrapper(reservationTicketWrapper);
        return reservationTicket;
    }

    public ArrayList<ReservationRecord> partnerReserveRooms(ReservationTicket ticket, Long partnerId, String emailAddress){
        Partner partner = partnerEntitySessionBeanLocal.retrievePartnerById(partnerId);
        //ReservationTicket ticket = reservationEntitySessionBeanLocal.unwrapTicketWrapper(ticketWrapper);
        
        return roomReservationControllerLocal.partnerReserveRoom(ticket, partner);
    }
}
