/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.ReservationEntitySessionBeanLocal;
import entity.ExceptionReport;
import entity.ReservationRecord;
import entity.Room;
import entity.RoomAvailability;
import entity.RoomType;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Schedule;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import util.enumeration.IsOccupiedEnum;
import util.enumeration.StatusEnum;
import util.exception.NoAvailableRoomException;
import util.exception.NoHigherRankException;

/**
 *
 * @author wong-
 */
@Singleton
@LocalBean
@Startup
@Remote(SystemHelperRemote.class)
public class SystemHelper implements SystemHelperRemote {

    @EJB(name = "ReservationEntitySessionBeanLocal")
    private ReservationEntitySessionBeanLocal reservationEntitySessionBeanLocal;

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    public SystemHelper() {
    
    }
    
    //Generate a new availability record for all room type every day for today + 365 day
    @Schedule(hour = "0")
    public void addNewAvailRecordDaily(){
        Query q = em.createQuery("SELECT r FROM RoomType r");
        List<RoomType> roomTypes = q.getResultList();
        for(RoomType r : roomTypes){
            RoomAvailability avail = new RoomAvailability(addDays(new Date(),365), r);
            em.persist(avail);
            em.flush();
            r.addNewRoomAvailability(avail);
        }
    }
    
    //remove the availability record of the day that had just ended so no backdated reservations can be made. 
    @Schedule(hour = "0")
    public void removeOldAvailRecordDaily(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date yesterday = cal.getTime();
        Query q = em.createQuery("SELECT a FROM RoomAvailability a WHERE a.availabiltyRecordDate =:yesterday");
        q.setParameter("yesterday", yesterday, TemporalType.DATE);
        List<RoomAvailability> availabilityRecords = q.getResultList();
        for(RoomAvailability a : availabilityRecords){
            RoomType type = a.getRoomType();
            type.getRoomAvailabilities().remove(a);
            em.remove(a);
        }
    }
    
    
    private Date addDays(Date date, int i){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, i);
        
        return cal.getTime();     
    }
    
    
    //Recursively allocate rooms to all reservation at 2am every day. 
    @Schedule(hour = "2")
    @Override
    public void allocateRoomsDaily(){
        List<ReservationRecord> reservationsForToday = getAllReservation();
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        System.out.println("Hello");
        for(ReservationRecord r : reservationsForToday){
            RoomType type = r.getRoomType();
            ExceptionReport report = new ExceptionReport(today, "", r);
            report = allocateRoom(r, type, report, false);
            //Save the report if its filled in
            if(!report.getErrorReport().isEmpty()){
                em.persist(report);
            }
        }
    }
    
    private ExceptionReport allocateRoom(ReservationRecord reservation, RoomType type, ExceptionReport report, Boolean upgraded){
        try{
            Room room = getAvailableRoom(type);
            reservationEntitySessionBeanLocal.setAssignedRoom(room, reservation);
            return report;
        } catch (NoAvailableRoomException e) {
            try {
                if(upgraded){
                    throw new NoAvailableRoomException("No room available after upgrade.");
                }
                RoomType nextType = getNextRank(type);
                String error = "Allocation Exception: Reservation ID " + reservation.getReservationRecordId() + " - Upgraded from " + reservation.getRoomType().getTypeName() + " to " + nextType.getTypeName();
                report.setErrorReport(error);
                System.err.println(error);
                return allocateRoom(reservation, nextType, report, true);
            } catch (NoHigherRankException | NoAvailableRoomException ex) {
                String error = "Allocation Exception: Reservation ID " + reservation.getReservationRecordId() +  " - No rooms available.";
                System.err.println(error);
                report.setErrorReport(error);
                return report;
            }
        }
    }
    
    private Room getAvailableRoom(RoomType type) throws NoAvailableRoomException{
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        Query q = em.createQuery("SELECT r FROM Room r WHERE r.occupancy = :unoccupied AND r.roomType = :type AND r.status = :available");
        q.setParameter("unoccupied", IsOccupiedEnum.UNOCCUPIED);
        q.setParameter("type", type);
        q.setParameter("available", StatusEnum.AVAILABLE);
        List<Room> list = q.getResultList();
        if(list.isEmpty()){
            throw new NoAvailableRoomException("No room of this type available currently");
        }
        
        return list.get(0);
    }
    
    private List<ReservationRecord> getAllReservation(){
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        
        Query q = em.createQuery("SELECT r FROM ReservationRecord r WHERE r.assignedRoom IS NULL");
        
        return q.getResultList();
    }
    
    private RoomType getNextRank(RoomType currentType) throws NoHigherRankException{
        int currentRank = currentType.getRanking();
        int nextRank = currentRank - 1;
        if(nextRank < 0){
            throw new NoHigherRankException("No Higher Ranks Available.");
        }
        Query query = em.createQuery("SELECT r FROM RoomType r WHERE r.ranking = :nextRank");
        query.setParameter("nextRank", nextRank);
        
        
        return (RoomType) query.getSingleResult();
    }
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
