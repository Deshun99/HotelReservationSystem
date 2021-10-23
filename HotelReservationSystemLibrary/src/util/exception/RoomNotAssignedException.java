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
public class RoomNotAssignedException extends Exception {

    /**
     * Creates a new instance of <code>RoomNotAssignedException</code> without
     * detail message.
     */
    public RoomNotAssignedException() {
    }

    /**
     * Constructs an instance of <code>RoomNotAssignedException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public RoomNotAssignedException(String msg) {
        super(msg);
    }
}
