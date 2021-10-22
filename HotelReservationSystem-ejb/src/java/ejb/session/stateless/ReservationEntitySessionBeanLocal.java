/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Guest;
import entity.Partner;
import entity.ReservationRecord;
import entity.Room;
import java.util.ArrayList;
import java.util.Date;
import javax.ejb.Local;
import util.exception.EntityMismatchException;
import util.exception.ReservationRecordNotFoundException;
import util.objects.ReservationTicket;
import util.objects.ReservationTicketWrapper;

/**
 *
 * @author wong-
 */
@Local
public interface ReservationEntitySessionBeanLocal {
    
    public void setAssignedRoom(Room room, ReservationRecord res);

    public ArrayList<ReservationRecord> frontOfficeReserveRooms(ReservationTicket ticket, String guestEmail);

    public ArrayList<ReservationRecord> partnerReserveRooms(ReservationTicket ticket, Partner partner, String guestEmail);

    public ArrayList<ReservationRecord> guestReserveRooms(ReservationTicket ticket, Guest guest);

    public ReservationTicket searchRooms(Date startDate, Date endDate, Boolean isWalkIn);

    public String retrieveReservationDetails(Long resId, Long guestId) throws ReservationRecordNotFoundException, EntityMismatchException;

    public ReservationTicket unwrapTicketWrapper(ReservationTicketWrapper wrapper);
    
}
