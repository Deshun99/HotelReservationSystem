/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ExceptionReport;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
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
@Remote
public interface RoomEntitySessionBeanRemote {

    public RoomType returnNewRoomTypeEntity(String typeName, String description, String bedType, Integer capacity, String amenities, int i);

    public void createNewPublishedRate(String rateName, BigDecimal ratePerNight, Date startDate, Date endDate, Long roomTypeId) throws RoomTypeNotFoundException;

    public void createNewNormalRate(String rateName, BigDecimal ratePerNight, Date startDate, Date endDate, Long roomTypeId) throws RoomTypeNotFoundException;

    public void createNewPeakRate(String rateName, BigDecimal ratePerNight, Date startDate, Date endDate, Long roomTypeId) throws RoomTypeNotFoundException;

    public void createNewPromotionRate(String rateName, BigDecimal ratePerNight, Date startDate, Date endDate, Long roomTypeId) throws RoomTypeNotFoundException;

    public Integer getNumberOfRoomsAvailable(RoomType type, Date date) throws RoomTypeUnavailableException;

    public BigDecimal getRatePerNight(RoomType roomType, Date date) throws RoomRateNotFoundException;

    public List<RoomRate> getValidRateList(RoomType roomType, Date date, RateTypeEnum rateType);

    public BigDecimal getPrevailingRatePerNight(List<RoomRate> rateList);

    public BigDecimal getPublishedRatePerNight(RoomType roomType, Date date) throws RoomRateNotFoundException;
    
    public void createNewRoom(Integer floor, Integer unit, String roomType) throws RoomTypeNotFoundException;

    public List<RoomType> getRoomTypesByRanking();

    public Long createNewRoomType(String typeName, String description, String bedType, Integer capacity, String amenities, int i);

    public String viewRoomTypeDetails(String typeName) throws RoomTypeNotFoundException;

    public Room retrieveRoomByRoomNumber(String roomNumber) throws RoomNotFoundException;

    public Boolean deleteRoomType(String typeName) throws RoomTypeNotFoundException;

    public void updateRoomRate(Long roomRateId, BigDecimal ratePerNight, Date startDate, Date endDate, StatusEnum status) throws RoomRateNotFoundException;

    public Boolean deleteRoomRate(Long roomRateId) throws RoomRateNotFoundException, LastAvailableRateException;

    public List<RoomRate> retrieveAllRoomRates();

    public void updateRoom(String roomNumber, String roomType, StatusEnum status) throws RoomNotFoundException, RoomTypeNotFoundException;

    public void updateRoomType(String typeName, String newDescription, String newBedType, Integer newCapacity, String newAmenities) throws RoomTypeNotFoundException;

    public Boolean deleteRoom(String roomNumber) throws RoomNotFoundException;

    public List<Room> retrieveAllRooms();

    public String viewRoomDetails(Room room);

    public List<ExceptionReport> getListOfExceptionReportsByDate(Date date);
    
    public RoomType retrieveRoomTypeByTypeName(String typeName) throws RoomTypeNotFoundException;
}
