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
import javax.ejb.Local;
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
@Local
public interface RoomEntitySessionBeanLocal {
    
    public RoomType returnNewRoomTypeEntity(String typeName, String description, String bedType, Integer capacity, String amenities, int i);

    

    

    public void createNewPeakRate(String rateName, BigDecimal ratePerNight, Date startDate, Date endDate, Long roomTypeId) throws RoomTypeNotFoundException;

    public void createNewPromotionRate(String rateName, BigDecimal ratePerNight, Date startDate, Date endDate, Long roomTypeId) throws RoomTypeNotFoundException;

    public Integer getNumberOfRoomsAvailable(RoomType type, Date date) throws RoomTypeUnavailableException;

    public BigDecimal getRatePerNight(RoomType roomType, Date date) throws RoomRateNotFoundException;

    public List<RoomRate> getValidRateList(RoomType roomType, Date date, RateTypeEnum rateType);

    public BigDecimal getPrevailingRatePerNight(List<RoomRate> rateList);

    public BigDecimal getPublishedRatePerNight(RoomType roomType, Date date) throws RoomRateNotFoundException;
    
    public void createNewRoom(Integer floor, Integer unit, String roomType) throws RoomTypeNotFoundException;

    public RoomType retrieveRoomTypeByTypeName(String typeName) throws RoomTypeNotFoundException;

    public void createNewNormalRate(String rateName, BigDecimal ratePerNight, Date startDate, Date endDate, Long roomTypeId) throws RoomTypeNotFoundException;

    public void createNewPublishedRate(String rateName, BigDecimal ratePerNight, Date startDate, Date endDate, Long roomTypeId) throws RoomTypeNotFoundException;
    
}
