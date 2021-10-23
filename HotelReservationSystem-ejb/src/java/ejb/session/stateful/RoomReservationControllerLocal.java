/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import entity.ReservationRecord;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.EarlyCheckInUnavailableException;
import util.exception.EntityMismatchException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ReservationRecordNotFoundException;
import util.exception.RoomNotAssignedException;
import util.exception.RoomUpgradeException;
import util.exception.UnoccupiedRoomException;
import util.objects.ReservationTicket;

/**
 *
 * @author wong-
 */
@Local
public interface RoomReservationControllerLocal {

    public String retrieveReservationDetails(Long resId) throws ReservationRecordNotFoundException, EntityMismatchException;

    public void guestLogin(String username, String password) throws InvalidLoginCredentialException;

    public void guestLogout();

    public ArrayList<ReservationRecord> retrieveAllReservation();

    public ReservationTicket searchRooms(Date startDate, Date endDate, Boolean isWalkIn);

    public ArrayList<ReservationRecord> reserveRoom(ReservationTicket ticket);

    public void setGuestEmail(String email);

    public List<ReservationRecord> getReservationListByEmail(String email);

    public String checkInRoom(Long reservationId) throws EarlyCheckInUnavailableException, RoomNotAssignedException, RoomUpgradeException;

    public String checkOutRoom(String roomNumber) throws UnoccupiedRoomException, ReservationRecordNotFoundException;

    public void assignWalkInRoom(ArrayList<ReservationRecord> reservations);
    
}
