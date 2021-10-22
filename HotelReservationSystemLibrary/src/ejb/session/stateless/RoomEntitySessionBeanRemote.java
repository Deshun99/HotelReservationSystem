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

    public Long createNewRoomType(String typeName, String description, String bedType, Integer capacity, String amenities, int i);

    public RoomType returnNewRoomTypeEntity(String typeName, String description, String bedType, Integer capacity, String amenities, int i);

    public String viewRoomTypeDetails(String typeName) throws RoomTypeNotFoundException;

    public void updateRoomType(String typeName, String newDescription, String newBedType, Integer newCapacity, String newAmenities) throws RoomTypeNotFoundException;

    public Boolean deleteRoomType(String typeName) throws RoomTypeNotFoundException;

    public void createNewRoom(Integer floor, Integer unit, String roomType) throws RoomTypeNotFoundException;

    public void updateRoom(String roomNumber, String roomType, StatusEnum status) throws RoomNotFoundException, RoomTypeNotFoundException;

    public Boolean deleteRoom(String roomNumber) throws RoomNotFoundException;

    public RoomType retrieveRoomTypeByTypeName(String typeName) throws RoomTypeNotFoundException;

    public void createNewNormalRate(String rateName, BigDecimal ratePerNight, Date startDate, Date endDate, Long roomTypeId) throws RoomTypeNotFoundException;

    public void createNewPeakRate(String rateName, BigDecimal ratePerNight, Date startDate, Date endDate, Long roomTypeId) throws RoomTypeNotFoundException;

    public List<RoomType> getRoomTypesByRanking();

    public BigDecimal getRatePerNight(RoomType roomType, Date date) throws RoomRateNotFoundException;

    public List<RoomRate> getValidRateList(RoomType roomType, Date date, RateTypeEnum rateType);

    public BigDecimal getPublishedRatePerNight(RoomType roomType, Date date) throws RoomRateNotFoundException;

    public BigDecimal getPrevailingRatePerNight(List<RoomRate> rateList);

    public void updateRoomRate(Long roomRateId, BigDecimal ratePerNight, Date startDate, Date endDate, StatusEnum status) throws RoomRateNotFoundException;

    public Integer getNumberOfRoomsAvailable(RoomType type, Date date) throws RoomTypeUnavailableException;

    public Boolean deleteRoomRate(Long roomRateId) throws RoomRateNotFoundException, LastAvailableRateException;

    public List<RoomRate> retrieveAllRoomRates();

    public List<ExceptionReport> getListOfExceptionReportsByDate(Date date);

    public String viewRoomDetails(Room room);

    public List<Room> retrieveAllRooms();

    public Room retrieveRoomByRoomNumber(String roomNumber) throws RoomNotFoundException;

    public void createNewPromotionRate(String rateName, BigDecimal ratePerNight, Date startDate, Date endDate, Long roomTypeId) throws RoomTypeNotFoundException;

    public void createNewPublishedRate(String rateName, BigDecimal ratePerNight, Date startDate, Date endDate, Long roomTypeId) throws RoomTypeNotFoundException;
    
}
