
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
import entity.RoomAvailability;
import entity.RoomType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.IsOccupiedEnum;
import util.enumeration.StatusEnum;
import util.exception.EntityMismatchException;
import util.exception.ReservationRecordNotFoundException;
import util.exception.RoomRateNotFoundException;
import util.exception.RoomTypeUnavailableException;
import util.objects.ReservationTicket;
import util.objects.ReservationTicketWrapper;

/**
 *
 * @author wong-
 */
@Stateless
public class ReservationEntitySessionBean implements ReservationEntitySessionBeanRemote, ReservationEntitySessionBeanLocal {

    @EJB
    private RoomEntitySessionBeanLocal roomEntitySessionBeanLocal;

    
    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    
    @Override
    public String retrieveReservationDetails(Long resId, Long guestId) throws ReservationRecordNotFoundException, EntityMismatchException{
        ReservationRecord reservationRecord =  em.find(ReservationRecord.class, resId);
        if(reservationRecord == null){
            throw new ReservationRecordNotFoundException("Reservation Record not found");
        }else if(reservationRecord.getReservedByGuest() == null){
            throw new EntityMismatchException("Guest Id provided does not match Guest Id in reservation record");
        }else if(!reservationRecord.getReservedByGuest().getGuestId().equals(guestId)){
            throw new EntityMismatchException("Guest Id provided does not match Guest Id in reservation record");
        }else{
            String details;
            if (reservationRecord.getAssignedRoom() == null) {
                details = "Reservation Id: " + reservationRecord.getReservationRecordId() + "\n"
                        + "Start Date: " + reservationRecord.getStartDateAsString() + "\n"
                        + "End Date: " + reservationRecord.getEndDateAsString() + "\n"
                        + "Bill: $" + reservationRecord.getBill() + "\n"
                        + "Assigned Room: Room not assigned yet" + "\n";
            } else {
                details = "Reservation Id: " + reservationRecord.getReservationRecordId() + "\n"
                        + "Start Date: " + reservationRecord.getStartDateAsString() + "\n"
                        + "End Date: " + reservationRecord.getEndDateAsString() + "\n"
                        + "Bill: $" + reservationRecord.getBill() + "\n"
                        + "Assigned Room: " + reservationRecord.getAssignedRoom().getRoomNumber() + "\n";
            }
            return details;
        }
    }
    
    @Override
    public ReservationTicket searchRooms(Date startDate, Date endDate, Boolean isWalkIn){
        ReservationTicket reservationTicket = new ReservationTicket(startDate, endDate);
        Query q = em.createQuery("SELECT r FROM RoomType r WHERE r.status = :available");
        q.setParameter("available", StatusEnum.AVAILABLE);
        List<RoomType> typeList = q.getResultList();
        for(RoomType type : typeList){
            Calendar start = Calendar.getInstance();
            start.setTime(startDate);
            Calendar end = Calendar.getInstance();
            end.setTime(endDate);
            
            BigDecimal totalBill = new BigDecimal(0);
            Integer numRoomsRemaining = Integer.MAX_VALUE;
            boolean flag = false;
            for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
                try{
                    Integer numAvail = roomEntitySessionBeanLocal.getNumberOfRoomsAvailable(type, date);
                    if(numAvail <= 0){ //no room available for 1 day means room type is not available for that search
                        System.err.println("No room available");
                        flag = false;
                        break;
                    }else{
                        numRoomsRemaining = Math.min(numAvail, numRoomsRemaining);
                        if(isWalkIn){
                            totalBill = totalBill.add(roomEntitySessionBeanLocal.getPublishedRatePerNight(type, date));
                        }else{  //partner or guest 
                            totalBill = totalBill.add(roomEntitySessionBeanLocal.getRatePerNight(type, date));
                        }
                        flag = true;
                    }
                }catch(RoomTypeUnavailableException | RoomRateNotFoundException e){
                    System.err.println(e.getMessage());
                    flag = false;
                    break;
                }
            }
            if(flag){
                reservationTicket.getAvailableRoomTypes().add(type);
                reservationTicket.getRespectiveNumberOfRoomsRemaining().add(numRoomsRemaining);
                reservationTicket.getRespectiveTotalBill().add(totalBill);
            }
        }
        
        return reservationTicket;
    }
     
    @Override
    public ArrayList<ReservationRecord> guestReserveRooms(ReservationTicket ticket, Guest guest){
        ArrayList<ReservationRecord> reservations = new ArrayList<>();
        for(int i = 0; i < ticket.getAvailableRoomTypes().size(); i++){ //for each room types
            for(int j = 0; j < ticket.getRespectiveNumberReserved().get(i); j++){ //for each room booked
                ReservationRecord record = new ReservationRecord(ticket.getAvailableRoomTypes().get(i), ticket.getStartDate(), ticket.getEndDate(), guest);
                em.persist(record);
                record.setBill(ticket.getRespectiveTotalBill().get(i));
                guest.getReservationRecords().add(record);
                reservations.add(record);
                updateAvailabilityRecord(ticket.getAvailableRoomTypes().get(i), ticket.getStartDate(), ticket.getEndDate());
                System.err.println("i = " + i + " j = " + j + "reserved");
            }
        }
        return reservations;
    }
    
    
    @Override
    public ArrayList<ReservationRecord> frontOfficeReserveRooms(ReservationTicket ticket, String guestEmail){
        ArrayList<ReservationRecord> reservations = new ArrayList<>();
        for(int i = 0; i < ticket.getAvailableRoomTypes().size(); i++){ //for each room types
            for(int j = 0; j < ticket.getRespectiveNumberReserved().get(i); j++){ //for each room booked
                ReservationRecord record = new ReservationRecord(ticket.getAvailableRoomTypes().get(i), ticket.getStartDate(), ticket.getEndDate(), guestEmail);
                em.persist(record);
                record.setBill(ticket.getRespectiveTotalBill().get(i));
                reservations.add(record);
                updateAvailabilityRecord(ticket.getAvailableRoomTypes().get(i), ticket.getStartDate(), ticket.getEndDate());
            }
        }
        return reservations;
    }

    @Override
    public ArrayList<ReservationRecord> partnerReserveRooms(ReservationTicket ticket, Partner partner, String guestEmail){
        ArrayList<ReservationRecord> reservations = new ArrayList<>();
        for(int i = 0; i < ticket.getAvailableRoomTypes().size(); i++){ //for each room types
            for(int j = 0; j < ticket.getRespectiveNumberReserved().get(i); j++){ //for each room booked
                ReservationRecord record = new ReservationRecord(ticket.getAvailableRoomTypes().get(i), ticket.getStartDate(), ticket.getEndDate(), guestEmail, partner);
                em.persist(record);
                record.setBill(ticket.getRespectiveTotalBill().get(i));
                partner.getReservationRecords().add(record);
                reservations.add(record);
                updateAvailabilityRecord(ticket.getAvailableRoomTypes().get(i), ticket.getStartDate(), ticket.getEndDate());
            }
        }
        return reservations;
    } 
    
    @Override
    public ReservationTicket unwrapTicketWrapper(ReservationTicketWrapper wrapper){
        ReservationTicket ticket = new ReservationTicket(wrapper.getStartDate(), wrapper.getEndDate());

        for(int i = 0; i < wrapper.getReservationDescriptions().size(); i++){
            Query q = em.createQuery("SELECT r FROM RoomType r WHERE r.typeName = :name");
            q.setParameter("name", wrapper.getRespectiveRoomTypeName().get(i));
            RoomType type = (RoomType) q.getSingleResult();
            ticket.getAvailableRoomTypes().add(type);
            ticket.getRespectiveTotalBill().add(wrapper.getRespectiveTotalBill().get(i));
            ticket.getRespectiveNumberReserved().add(wrapper.getRespectiveNumberToReserve().get(i));
        }
        
        return ticket;
    }
    
    
    private void updateAvailabilityRecord(RoomType type, Date startDate, Date endDate){
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()){
            Query q = em.createQuery("SELECT a FROM RoomAvailability a WHERE a.availabiltyRecordDate = :date AND a.roomType = :type");
            q.setParameter("date", date);
            q.setParameter("type", type);
            RoomAvailability avail = (RoomAvailability) q.getSingleResult();
            avail.addOneReservation();
        }
        
    }
    
    @Override
    public void setAssignedRoom(Room room, ReservationRecord res){
        
        res.setAssignedRoom(room);
        room.setOccupancy(IsOccupiedEnum.OCCUPIED);
        room.setIsOccupiedUntil(res.getEndDate());
    }  

    
    
}
