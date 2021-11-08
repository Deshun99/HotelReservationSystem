/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holidayreservationsystem;

import java.text.ParseException;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import ws.client.EntityMismatchException_Exception;
import ws.client.InvalidLoginCredentialException_Exception;
import ws.client.Partner;
import ws.client.PartnerReservationWebService;
import ws.client.PartnerReservationWebService_Service;
import ws.client.ReservationRecord;
import ws.client.ReservationRecordNotFoundException_Exception;
import ws.client.ReservationTicket;
import ws.client.ReservationTicketWrapper;



/**
 *
 * @author wong-
 */
public class HolidayReservationSystem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws DatatypeConfigurationException, ParseException {
        MainApp mainApp = new MainApp();
        mainApp.runApp();
    }
    
    public static Partner partnerLogin(java.lang.String arg0,java.lang.String arg1) throws InvalidLoginCredentialException_Exception {
        PartnerReservationWebService_Service service = new PartnerReservationWebService_Service();
        PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        return port.partnerLogin(arg0, arg1);
    }
    
    public static List<ReservationRecord> viewAllPartnerReservation(Long partnerId) {
        PartnerReservationWebService_Service service = new PartnerReservationWebService_Service();
        PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        return port.viewAllPartnerReservation(partnerId);
    }
    
    public static String viewReservationDetails(Long reservationId, Long partnerId) throws EntityMismatchException_Exception, ReservationRecordNotFoundException_Exception {
        PartnerReservationWebService_Service service = new PartnerReservationWebService_Service();
        PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        return port.viewReservationDetails(reservationId, partnerId);
    }
    
    public static ReservationTicketWrapper searchRoom(javax.xml.datatype.XMLGregorianCalendar arg0, javax.xml.datatype.XMLGregorianCalendar arg1) {
        PartnerReservationWebService_Service service = new PartnerReservationWebService_Service();
        PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        return port.searchRoom(arg0, arg1);
    }
    
    public static ReservationTicket unwrapRoomTicket(ReservationTicketWrapper reservationTicketWrapper) {
        PartnerReservationWebService_Service service = new PartnerReservationWebService_Service();
        PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        return port.unwrapRoomTicket(reservationTicketWrapper);
    }
    
    public static List<ReservationRecord> partnerReserveRooms(ReservationTicket ticketWrapper, Long partnerId, String guestEmail){
        PartnerReservationWebService_Service service = new PartnerReservationWebService_Service();
        PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        return port.partnerReserveRooms(ticketWrapper, partnerId, guestEmail);
    }
    
}

