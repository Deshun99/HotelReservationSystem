/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author wong-
 */
public class RoomUpgradeException extends Exception{

    /**
     * Creates a new instance of <code>RoomUpgradeException</code> without
     * detail message.
     */
    public RoomUpgradeException() {
    }

    /**
     * Constructs an instance of <code>RoomUpgradeException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public RoomUpgradeException(String msg) {
        super(msg);
    }
}
