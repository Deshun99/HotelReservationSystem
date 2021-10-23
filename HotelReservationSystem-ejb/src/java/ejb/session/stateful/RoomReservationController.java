/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import ejb.session.stateless.GuestEntitySessionBeanLocal;
import ejb.session.stateless.ReservationEntitySessionBeanLocal;
import entity.Guest;
import entity.ReservationRecord;
import entity.Room;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.IsOccupiedEnum;
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
@Stateful
public class RoomReservationController implements RoomReservationControllerLocal, RoomReservationControllerRemote {

    @EJB(name = "ReservationEntitySessionBeanLocal")
    private ReservationEntitySessionBeanLocal reservationEntitySessionBeanLocal;
    
    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @EJB(name = "GuestEntitySessionBeanLocal")
    private GuestEntitySessionBeanLocal guestEntitySessionBeanLocal;

    private Guest guest;
    private String email;
    
    public RoomReservationController() {
    }
    
    @Override
    public void guestLogin(String username, String password) throws InvalidLoginCredentialException {
        guest = guestEntitySessionBeanLocal.guestLogin(username, password);
    }
    
    @Override
    public void guestLogout(){
        guest = null;
    }
    
    @Override
    public void setGuestEmail(String email){
        this.email = email;
    }
    
    @Override
    public ArrayList<ReservationRecord> retrieveAllReservation(){
        System.err.println("guest id: " + guest.getReservationRecords());
        return guest.getReservationRecords();
    }
    
    @Override
    public String retrieveReservationDetails(Long resId) throws ReservationRecordNotFoundException, EntityMismatchException{
        return reservationEntitySessionBeanLocal.retrieveReservationDetails(resId, guest.getGuestId());
    }

    @Override
    public ReservationTicket searchRooms(Date startDate, Date endDate, Boolean isWalkIn){
        return reservationEntitySessionBeanLocal.searchRooms(startDate, endDate, isWalkIn);
    }
    
    @Override
    public ArrayList<ReservationRecord> reserveRoom(ReservationTicket ticket){
        
        if(guest != null){
            return reservationEntitySessionBeanLocal.guestReserveRooms(ticket, guest);
        }else{
            return reservationEntitySessionBeanLocal.frontOfficeReserveRooms(ticket, email);
        }
    }
    
    @Override
    public List<ReservationRecord> getReservationListByEmail(String email){
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        Query q = em.createQuery("SELECT r FROM ReservationRecord r WHERE r.guestEmail = :email AND r.startDate = :date AND r.checkInTime IS NULL");
        q.setParameter("email", email);
        q.setParameter("date", date);

        List<ReservationRecord> list = q.getResultList();
        for(ReservationRecord r : list){
            r.getRoomType();
            r.getAssignedRoom();
            r.getException();
        }
        return list;
    }
    
    @Override
    public String checkInRoom(Long reservationId) throws EarlyCheckInUnavailableException, RoomNotAssignedException, RoomUpgradeException{

        ReservationRecord reservation = em.find(ReservationRecord.class, reservationId);
        //no room allocated
        if(reservation.getAssignedRoom() == null){
            throw new RoomNotAssignedException("Room was not allocated. Please check Room Allocation Exception Report.");
        }
        
        Room room = reservation.getAssignedRoom();
        
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        cal.set(Calendar.HOUR, 14);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        
        //currently before 2pm standard check in time
        if(now.before(cal.getTime())){
            if(!checkValidEarlyCheckIn(reservation)){
                throw new EarlyCheckInUnavailableException("Early check in unavailable, please check in after 2pm");
            }
        }

        //Room type reserved != room typed allocataed -> free upgrade
        if(!room.getRoomType().equals(reservation.getRoomType())){
            reservation.setCheckInTime(now);
            room.setOccupancy(IsOccupiedEnum.OCCUPIED);
            throw new RoomUpgradeException("Assigned to Room " + room.getRoomNumber() + " - Room upgraded to " + room.getRoomType().getTypeName());
        }
                  
        reservation.setCheckInTime(now);
        room.setOccupancy(IsOccupiedEnum.OCCUPIED);
        
        return "Assigned to Room " + room.getRoomNumber();
    }
    
    private boolean checkValidEarlyCheckIn(ReservationRecord r){
        Date today = r.getStartDate();
        Query q = em.createQuery("SELECT r FROM ReservationRecord r WHERE r.assignedRoom.roomNumber = :roomNumber AND r.endDate = :today");
        q.setParameter("roomNumber", r.getAssignedRoom().getRoomNumber());
        q.setParameter("today", today);
        try{
            q.getSingleResult();
        }catch(NoResultException e){
            //No booking that ended today -> can do early check in
            return true;
        }
        
        //There exist a booking that just ended today -> no early check in
        return false;
    }
    
    @Override
    public String checkOutRoom(String roomNumber) throws UnoccupiedRoomException, ReservationRecordNotFoundException{    
        Query q = em.createQuery("SELECT r FROM ReservationRecord r WHERE r.assignedRoom.roomNumber = :roomNumber");
        q.setParameter("roomNumber", roomNumber);
        
        try{
            ReservationRecord r = (ReservationRecord) q.getSingleResult();
            if(r.getAssignedRoom().getOccupancy().equals(IsOccupiedEnum.UNOCCUPIED)){
                throw new UnoccupiedRoomException("Room not occupied - unable to check out.");
            }
            Calendar cal = Calendar.getInstance();
            Date now = cal.getTime();
            r.setCheckOutTime(now);
            r.getAssignedRoom().setOccupancy(IsOccupiedEnum.UNOCCUPIED);
            r.getAssignedRoom().setIsOccupiedUntil(null);
            return "\nRoom " + roomNumber +   " checked out successfully";
        }catch(NoResultException | NonUniqueResultException e){
            throw new ReservationRecordNotFoundException("No reservation records for this room is found");
        }
    }
    
    @Override
    public void assignWalkInRoom(ArrayList<ReservationRecord> reservations){
        for(ReservationRecord r : reservations){
            r = em.merge(r);
            Query q = em.createQuery("SELECT r FROM Room r WHERE r.occupancy = :notOccupied AND r.roomType = :type");
            q.setParameter("notOccupied", IsOccupiedEnum.UNOCCUPIED);
            q.setParameter("type", r.getRoomType());
            List<Room> rooms = q.getResultList();
            reservationEntitySessionBeanLocal.setAssignedRoom(rooms.get(0), r);
        }
    }

    
}
