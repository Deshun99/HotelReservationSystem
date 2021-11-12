/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ExceptionReport;
import entity.ReservationRecord;
import entity.Room;
import entity.RoomAvailability;
import entity.RoomRate;
import entity.RoomType;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.validation.ConstraintViolationException;
import util.enumeration.IsOccupiedEnum;
import util.enumeration.RateTypeEnum;
import util.enumeration.StatusEnum;
import util.exception.LastAvailableRateException;
import util.exception.RoomNotFoundException;
import util.exception.RoomRateNotFoundException;
import util.exception.RoomTypeNotFoundException;
import util.exception.RoomTypeUnavailableException;


/**
 *
 * @author wong-
 */
@Stateless
public class RoomEntitySessionBean implements RoomEntitySessionBeanRemote, RoomEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    @Resource
    private EJBContext eJBContext;
    

    public RoomEntitySessionBean() {
    }


    @Override
    public Long createNewRoomType(String typeName, String description, String bedType, Integer capacity, String amenities, int i) {
        RoomType newRoomType =  returnNewRoomTypeEntity(typeName, description, bedType, capacity, amenities, i);
        
        return newRoomType.getTypeId();
    }
    
    
    public Date addDays(Date date, int i){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, i);
        
        return cal.getTime();     
    }
    
    public Date setHoursMinsToZero(Date date){
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        
        return cal.getTime();
    }
    
    @Override
    public RoomType returnNewRoomTypeEntity(String typeName, String description, String bedType, Integer capacity, String amenities, int i) {
        RoomType newRoomType = new RoomType(typeName, description, bedType, capacity, amenities);
        insertRoomRank(newRoomType, i);
        em.persist(newRoomType);            
        
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        date = setHoursMinsToZero(date);
        for(int j = 0; j <= 365; j++){ //create next 365 days of availability record in advance.
            RoomAvailability avail = new RoomAvailability(date, newRoomType);
            em.persist(avail);
            newRoomType.addNewRoomAvailability(avail); 
            date = addDays(date, 1);
        }
        em.flush();
        return newRoomType;
    }
      
    @Override
    public String viewRoomTypeDetails(String typeName) throws RoomTypeNotFoundException {
        RoomType roomType;
        try{
            roomType = retrieveRoomTypeByTypeName(typeName);
        }catch (RoomTypeNotFoundException e) {
            throw e;
        }
        
        String details = ("Type Name: " + roomType.getTypeName() +
                          "\nDescription: " + roomType.getDescription() +
                          "\nAmenities: " + roomType.getAmenities() + 
                          "\nBed Type: " + roomType.getBedType() +
                          "\nCapacity: " + roomType.getCapacity() +
                          "\nStatus: " + roomType.getStatus() +
                          "\nTotal Rooms: " + roomType.getTotalRooms()) + "\n";
        
        return details;
    }
    

    @Override
    public void updateRoomType(String typeName, String newDescription, String newBedType, Integer newCapacity, String newAmenities, String newName) throws RoomTypeNotFoundException {
        
        try{
            RoomType thisRoomType = retrieveRoomTypeByTypeName(typeName);
            thisRoomType.setTypeName(newName);
            thisRoomType.setAmenities(newAmenities);
            thisRoomType.setDescription(newDescription);
            thisRoomType.setBedType(newBedType);
            thisRoomType.setCapacity(newCapacity);
        }catch (RoomTypeNotFoundException | ConstraintViolationException  e) {
            eJBContext.setRollbackOnly();
            throw e;
        }

    }
    
    /**
     *
     * @param typeName
     * @return false if roomType disabled, true if roomType deleted
     * @throws RoomTypeNotFoundException
     */

    @Override
    public Boolean deleteRoomType(String typeName) throws RoomTypeNotFoundException {
        RoomType thisRoomType;
        try{
            thisRoomType = retrieveRoomTypeByTypeName(typeName);
        } catch (RoomTypeNotFoundException e) {
            throw e;
        }
        
        //get a list of rooms of type typeName that is occupied 
        Query query = em.createQuery("SELECT room FROM Room room WHERE room.roomType.typeName = :typename AND room.occupancy = :occupancy");
        query.setParameter("typename", typeName);
        query.setParameter("occupancy", IsOccupiedEnum.OCCUPIED);
        if(query.getResultList().isEmpty()) { //No rooms of the type are occupied
            deleteRoomRank(thisRoomType);
            em.remove(thisRoomType); 
            return true;
        }else { //Some room of the type are occupied
            thisRoomType.setStatus(StatusEnum.NOT_AVAILABLE);
            return false;
        }        
    }
    
    @Override
    public List<RoomType> getRoomTypesByRanking(){
        Query q = em.createQuery("SELECT r FROM RoomType r ORDER BY r.ranking ASC");
        return q.getResultList();
        
    }

    
    private void insertRoomRank(RoomType newType, int rank){
        List<RoomType> roomTypes = getRoomTypesByRanking();
        newType.setRanking(rank);
        for(RoomType r : roomTypes){
            if(r.getRanking()>= newType.getRanking()&& !r.getTypeId().equals(newType.getTypeId())){
                r.downgradeRank();
            }
        }
    }
    
    private void deleteRoomRank(RoomType typeToDelete){
        Query q = em.createQuery("SELECT r FROM RoomType r");
        List<RoomType> roomTypes = q.getResultList();
        for(RoomType r : roomTypes){
            if(r.getRanking()>= typeToDelete.getRanking()){
                r.upgradeRank();
            }
        }
    }
    
    
    @Override
    public void createNewRoom(Integer floor, Integer unit, String roomType) throws RoomTypeNotFoundException { 
        try {
            RoomType thisRoomType = retrieveRoomTypeByTypeName(roomType);
            Room newRoom = new Room(floor, unit, thisRoomType);          
            em.persist(newRoom);
            thisRoomType.addRoom(newRoom);
        } catch (RoomTypeNotFoundException roomTypeNotFoundException) {
            throw roomTypeNotFoundException;
            
        }
    }
    
    //update the status and roomtype of a room

    @Override
    public void updateRoom(String roomNumber, String roomType, StatusEnum status) throws RoomNotFoundException, RoomTypeNotFoundException {
        try {
            Room thisRoom = retrieveRoomByRoomNumber(roomNumber);
            RoomType oldRoomType = thisRoom.getRoomType();
            RoomType newRoomType = retrieveRoomTypeByTypeName(roomType);
            oldRoomType.removeRoom(thisRoom);
            newRoomType.addRoom(thisRoom);
            thisRoom.setRoomType(newRoomType);
            thisRoom.setStatus(status);            
        } catch (RoomNotFoundException | RoomTypeNotFoundException e) {
            eJBContext.setRollbackOnly();
            throw e;
        }
    }
    

    @Override
    public Boolean deleteRoom(String roomNumber) throws RoomNotFoundException {  
        try {
            Room thisRoom = retrieveRoomByRoomNumber(roomNumber);
            RoomType thisRoomType = thisRoom.getRoomType();
            if(thisRoom.getOccupancy().equals(IsOccupiedEnum.UNOCCUPIED)) {
                thisRoomType.removeRoom(thisRoom);
                em.remove(thisRoom);
                return true;
            }else {
                thisRoom.setStatus(StatusEnum.NOT_AVAILABLE);
                return false;
            }           
        } catch(RoomNotFoundException e) {
            throw e;
        }
    }



    public RoomType retrieveRoomTypeByTypeName(String typeName) throws RoomTypeNotFoundException {
        Query q = em.createQuery("SELECT r FROM RoomType r WHERE r.typeName = :typename");
        q.setParameter("typename", typeName);
        try{ 
            return (RoomType) q.getSingleResult();
        }catch(NoResultException | NonUniqueResultException e) {  
            throw new RoomTypeNotFoundException("No such room type found");
        }
    }
    
    @Override
    public void createNewPublishedRate(String rateName, BigDecimal ratePerNight, Date startDate, Date endDate, Long roomTypeId) throws RoomTypeNotFoundException {
        RoomType roomType = em.find(RoomType.class, roomTypeId);
        if(roomType == null){
            throw new RoomTypeNotFoundException("Room Type Not Found");
        }        
        RoomRate newPublishedRate = new RoomRate(ratePerNight, rateName, RateTypeEnum.PUBLISHED, startDate, endDate);
        em.persist(newPublishedRate);

        roomType.addNewRoomRate(newPublishedRate);
        newPublishedRate.setRoomType(roomType);
        
    }
    
    /**
     *
     * @param rateName
     * @param ratePerNight
     * @param startDate
     * @param endDate
     * @param roomTypeId
     * @throws RoomTypeNotFoundException
     */
    @Override
    public void createNewNormalRate(String rateName, BigDecimal ratePerNight, Date startDate, Date endDate, Long roomTypeId) throws RoomTypeNotFoundException {
        RoomType roomType = em.find(RoomType.class, roomTypeId);
        if(roomType == null){      
            throw new RoomTypeNotFoundException("Room Type Not Found");
        }
        RoomRate newNormalRate = new RoomRate(ratePerNight, rateName, RateTypeEnum.NORMAL, startDate, endDate);
        em.persist(newNormalRate);

        roomType.addNewRoomRate(newNormalRate);
        newNormalRate.setRoomType(roomType);
    }
    
    @Override
    public void createNewPeakRate(String rateName, BigDecimal ratePerNight, Date startDate, Date endDate, Long roomTypeId) throws RoomTypeNotFoundException {
        RoomType roomType = em.find(RoomType.class, roomTypeId);
        if(roomType == null){
            throw new RoomTypeNotFoundException("Room Type Not Found");
        }        
        RoomRate newPeakRate = new RoomRate(ratePerNight, rateName, RateTypeEnum.PEAK, startDate, endDate);
        em.persist(newPeakRate);

        roomType.addNewRoomRate(newPeakRate);
        newPeakRate.setRoomType(roomType);
    }
    
    @Override
    public void createNewPromotionRate(String rateName, BigDecimal ratePerNight, Date startDate, Date endDate, Long roomTypeId) throws RoomTypeNotFoundException {
        RoomType roomType = em.find(RoomType.class, roomTypeId);
        if(roomType == null){
            throw new RoomTypeNotFoundException("Room Type Not Found");
        }
        RoomRate newPromotionRate = new RoomRate(ratePerNight, rateName, RateTypeEnum.PROMOTION, startDate, endDate);
        em.persist(newPromotionRate);  

        roomType.addNewRoomRate(newPromotionRate);
        newPromotionRate.setRoomType(roomType);
    }
    

    @Override
    public Room retrieveRoomByRoomNumber(String roomNumber) throws RoomNotFoundException {
        
        Query q = em.createQuery("SELECT r FROM Room r WHERE r.roomNumber = :roomnumber");
        q.setParameter("roomnumber", roomNumber);
            try{
               return (Room) q.getSingleResult();
            } catch (NoResultException | NonUniqueResultException e) {
                  throw new RoomNotFoundException("No such room found");
                }        
    }

    @Override
    public List<Room> retrieveAllRooms(){
        Query q = em.createQuery("SELECT r FROM Room r ORDER BY r.roomNumber ASC");
        return q.getResultList();
    }
    
    @Override
    public String viewRoomDetails(Room room){
        return "Room Number " + room.getRoomNumber() + ", " + room.getRoomType().getTypeName() + ", " + room.getStatus();
    }
    
 
    @Override
    public List<ExceptionReport> getListOfExceptionReportsByDate(Date date){
        Query q = em.createQuery("SELECT e FROM ExceptionReport e WHERE e.exceptionReportDate = :date");
        q.setParameter("date", date);
        
        return q.getResultList();
    }
    
    @Override
    public List<RoomRate> retrieveAllRoomRates(){
        Query q = em.createQuery("SELECT r FROM RoomRate r");
        return q.getResultList();
    }
    
 
    @Override
    public Boolean deleteRoomRate(Long roomRateId) throws RoomRateNotFoundException, LastAvailableRateException{
        RoomRate rate = em.find(RoomRate.class, roomRateId);
        if (rate == null){
            throw new RoomRateNotFoundException("Room Rate Not Found");
        }
        
        Date today = new Date();
        if(rate.getRateType().equals(RateTypeEnum.NORMAL) || rate.getRateType().equals(RateTypeEnum.PUBLISHED)){
            Query q = em.createQuery("SELECT r FROM RoomRate r WHERE r.rateType = :rateType");
            q.setParameter("rateType", rate.getRateType());
            
            List<RoomRate> rates = q.getResultList();
            int validRates = 0;
            if(rates.size() == 1){
                throw new LastAvailableRateException("You cannot delete the last valid Normal or Published rate");
            }else{
                for(RoomRate r :rates){
                    if(r.getStartDate().before(today)){
                        validRates++;
                    }
                }
                if(validRates <= 1){
                    throw new LastAvailableRateException("You cannot delete the last valid Normal or Published rate");
                }
            }
        }
        

        if(today.after(rate.getStartDate()) && ((rate.getEndDate() == null) || today.before(rate.getEndDate()))){ //rate still valid
            rate.setStatus(StatusEnum.NOT_AVAILABLE);
            return false;
        }else{
            rate.getRoomType().getRoomRate().remove(rate);
            em.remove(rate);
            return true;
        }
    }
    

    @Override
    public void updateRoomRate(Long roomRateId, BigDecimal ratePerNight, Date startDate, Date endDate, StatusEnum status) throws RoomRateNotFoundException{
        RoomRate roomRate = em.find(RoomRate.class, roomRateId);
        if(roomRate == null){
            throw new RoomRateNotFoundException("Room Rate not found.");
        }
        roomRate.setRatePerNight(ratePerNight);
        roomRate.setStartDate(startDate);
        roomRate.setEndDate(endDate);
        roomRate.setStatus(status);
        
    }
    

    public Integer getNumberOfRoomsAvailable(RoomType type, Date date) throws RoomTypeUnavailableException{
        Query query = em.createQuery("SELECT a FROM RoomAvailability a WHERE a.roomType = :type AND a.availabiltyRecordDate =:date");
        query.setParameter("type", type);
        query.setParameter("date", date, TemporalType.DATE);
        
        try{
            RoomAvailability avail = (RoomAvailability) query.getSingleResult();
            return avail.getAvailableRooms();
        }catch(NoResultException e){
            System.err.println("Availability Record not found");
            throw new RoomTypeUnavailableException("Availability Record not found");
        }
        
    }
    
    
    public BigDecimal getRatePerNight(RoomType roomType, Date date) throws RoomRateNotFoundException{
        List<RoomRate> normalList, peakList, promoList;
        normalList = getValidRateList(roomType, date, RateTypeEnum.NORMAL);
        peakList = getValidRateList(roomType, date, RateTypeEnum.PEAK);
        promoList = getValidRateList(roomType, date, RateTypeEnum.PROMOTION);
        
        if(!promoList.isEmpty()){
            return getPrevailingRatePerNight(promoList);
        }else if(!peakList.isEmpty()){
            return getPrevailingRatePerNight(peakList);
        }else if(!normalList.isEmpty()){
            return getPrevailingRatePerNight(normalList);
        }else{
            throw new RoomRateNotFoundException("No valid room rates found");
        }
    }
    
    
    public List<RoomRate> getValidRateList(RoomType roomType, Date date, RateTypeEnum rateType){
        //Query q = em.createQuery("SELECT r FROM RoomRateEntity r WHERE r.startDate <= :date AND (r.endDate >= :date OR r.endDate IS NULL)"
        //                        + "AND r.status = :status AND r.rateType = :rateType AND r.roomType = :roomType");
        
        Query q = em.createQuery("SELECT r FROM RoomRate r WHERE ((:date BETWEEN r.startDate AND r.endDate) OR (:date >= r.startDate AND r.endDate IS NULL)) AND r.status = :status AND r.rateType = :rateType AND r.roomType = :roomType" );
        q.setParameter("date", date, TemporalType.DATE);
        //q.setParameter("null", (Date) null, TemporalType.DATE);
        q.setParameter("status", StatusEnum.AVAILABLE);
        q.setParameter("rateType", rateType);
        q.setParameter("roomType", roomType);
        
        return q.getResultList();
    }
    
    
    
    public BigDecimal getPrevailingRatePerNight(List<RoomRate> rateList){
        BigDecimal prevailingRate = rateList.get(0).getRatePerNight();
        
        for(RoomRate rate : rateList){
            prevailingRate = prevailingRate.min(rate.getRatePerNight());
        }
        return prevailingRate;
    }
    
    
    public BigDecimal getPublishedRatePerNight(RoomType roomType, Date date) throws RoomRateNotFoundException{
        
        List<RoomRate> publishedRates = getValidRateList(roomType, date, RateTypeEnum.PUBLISHED);
        if(publishedRates.isEmpty()){
            throw new RoomRateNotFoundException("No valid room rates found.");
        }
        
        return getPrevailingRatePerNight(publishedRates);
        
    }
    
    
    //Generate a new avail record for all room type every day for today + 365 day
    /*@Schedule(hour = "0")
    public void addNewAvailRecordDaily(){
        Query q = em.createQuery("SELECT r FROM RoomTypeEntity r");
        List<RoomTypeEntity> roomTypes = q.getResultList();
        for(RoomTypeEntity r : roomTypes){
            AvailabilityRecordEntity avail = new AvailabilityRecordEntity(addDays(new Date(),365), r);
            r.addNewAvailabilityRecord(avail);
        }
    }*/

    
}
