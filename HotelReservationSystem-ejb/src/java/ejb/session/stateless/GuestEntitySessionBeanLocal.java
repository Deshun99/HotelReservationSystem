/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Guest;
import javax.ejb.Local;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author wong-
 */
@Local
public interface GuestEntitySessionBeanLocal {
    
    public void registerNewGuest(String name, String username, String password, String emailAddress);

    public Guest guestLogin(String username, String password) throws InvalidLoginCredentialException;
    
}
