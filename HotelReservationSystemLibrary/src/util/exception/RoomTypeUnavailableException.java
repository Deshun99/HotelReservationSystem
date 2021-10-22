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
public class RoomTypeUnavailableException extends Exception {

    /**
     * Creates a new instance of <code>RoomTypeUnavailableException</code>
     * without detail message.
     */
    public RoomTypeUnavailableException() {
    }

    /**
     * Constructs an instance of <code>RoomTypeUnavailableException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public RoomTypeUnavailableException(String msg) {
        super(msg);
    }
}
