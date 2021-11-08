/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeEntitySessionBeanLocal;
import ejb.session.stateless.PartnerEntitySessionBeanLocal;
import ejb.session.stateless.RoomEntitySessionBeanLocal;
import entity.Employee;
import entity.RoomType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import util.exception.EmployeeNotFoundException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author wong-
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB
    private EmployeeEntitySessionBeanLocal employeeEntitySessionBeanLocal;

    @EJB(name = "RoomEntitySessionBeanLocal")
    private RoomEntitySessionBeanLocal roomEntitySessionBeanLocal;

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @EJB(name = "PartnerEntitySessionBeanLocal")
    private PartnerEntitySessionBeanLocal partnerEntitySessionBeanLocal;

    @PostConstruct
    public void postConstruct() {

        Date today = new Date();

        try {
            employeeEntitySessionBeanLocal.retrieveEmployeeByUsername("sysadmin");
        } catch (EmployeeNotFoundException ex) {
            employeeEntitySessionBeanLocal.createNewSysAdmin("sysadmin", "sysadmin", "password");
            employeeEntitySessionBeanLocal.createNewOpsManager("opmanager", "opmanager", "password");
            employeeEntitySessionBeanLocal.createNewSalesManager("salesmanager", "salesmanager", "password");
            employeeEntitySessionBeanLocal.createNewGuestRelationsOfficer("guestrelo", "guestrelo", "password");
        }

        try {
            RoomType deluxeRoom = roomEntitySessionBeanLocal.returnNewRoomTypeEntity("Deluxe Room", "A comfortable room that will satisfy any", "1 Queen Size", 1, "Mini fridge, bathroom, television, internet", 0);
            roomEntitySessionBeanLocal.createNewNormalRate("Deluxe Room Normal Rate", new BigDecimal(50.00), today, null, deluxeRoom.getTypeId());
            roomEntitySessionBeanLocal.createNewPublishedRate("Deluxe Room Published Rate", new BigDecimal(100.00), today, null, deluxeRoom.getTypeId());
            for (int i = 1; i <= 5; i++) {
                roomEntitySessionBeanLocal.createNewRoom(1, i, "Deluxe Room");
            }

            RoomType premierRoom = roomEntitySessionBeanLocal.returnNewRoomTypeEntity("Premier Room", "A premium room that will satisfy any", "1 King Size", 1, "Mini fridge, bathroom, television, internet", 0);
            roomEntitySessionBeanLocal.createNewNormalRate("Premier Room Normal Rate", new BigDecimal(100.00), today, null, premierRoom.getTypeId());
            roomEntitySessionBeanLocal.createNewPublishedRate("Premier Room Published Rate", new BigDecimal(200.00), today, null, premierRoom.getTypeId());
            for (int i = 1; i <= 5; i++) {
                roomEntitySessionBeanLocal.createNewRoom(2, i, "Premier Room");
            }

            RoomType familyRoom = roomEntitySessionBeanLocal.returnNewRoomTypeEntity("Family Room", "A comfortable room that will a family will love", "1 Queen Size, 1 Single", 3, "Mini fridge, bathroom, television, internet", 0);
            roomEntitySessionBeanLocal.createNewNormalRate("Family Room Normal Rate", new BigDecimal(150.00), today, null, familyRoom.getTypeId());
            roomEntitySessionBeanLocal.createNewPublishedRate("Family Room Published Rate", new BigDecimal(300.00), today, null, familyRoom.getTypeId());
            for (int i = 1; i <= 5; i++) {
                roomEntitySessionBeanLocal.createNewRoom(3, i, "Family Room");
            }

            RoomType juniorSuite = roomEntitySessionBeanLocal.returnNewRoomTypeEntity("Junior Suite", "A premium suite that will satisfy any family", "2 King Size", 4, "Mini fridge, bathroom, television, internet", 0);
            roomEntitySessionBeanLocal.createNewNormalRate("Junior Suite Normal Rate", new BigDecimal(200.00), today, null, juniorSuite.getTypeId());
            roomEntitySessionBeanLocal.createNewPublishedRate("Junior Suite Published Rate", new BigDecimal(400.00), today, null, juniorSuite.getTypeId());
            for (int i = 1; i <= 5; i++) {
                roomEntitySessionBeanLocal.createNewRoom(4, i, "Junior Suite");
            }

            RoomType grandSuite = roomEntitySessionBeanLocal.returnNewRoomTypeEntity("Grand Suite", "The grandest suite we have to offer", "2 King Size", 4, "Full size fridge, 2 bathrooms, 2 television, internet, sofa", 0);
            roomEntitySessionBeanLocal.createNewNormalRate("Grand Suite Normal Rate", new BigDecimal(250.00), today, null, grandSuite.getTypeId());
            roomEntitySessionBeanLocal.createNewPublishedRate("Grand Suite Published Rate", new BigDecimal(500.00), today, null, grandSuite.getTypeId());
            for (int i = 1; i <= 5; i++) {
                roomEntitySessionBeanLocal.createNewRoom(5, i, "Grand Suite");
            }

        } catch (RoomTypeNotFoundException roomTypeNotFoundException) {
            System.err.println(roomTypeNotFoundException.getMessage());
        } catch (ConstraintViolationException e) {
            Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
            for (ConstraintViolation<?> v : violations) {
                System.err.println(v.getMessage());
            }
        }
    }
}
// Add business logic below. (Right-click in editor and choose
// "Insert Code > Add Business Method")

