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
public class RoomNotFoundException extends Exception{

    /**
     * Creates a new instance of <code>RoomNotFoundException</code> without
     * detail message.
     */
    public RoomNotFoundException() {
    }

    /**
     * Constructs an instance of <code>RoomNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public RoomNotFoundException(String msg) {
        super(msg);
    }
}
