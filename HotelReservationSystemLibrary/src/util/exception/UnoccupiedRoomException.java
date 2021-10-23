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
public class UnoccupiedRoomException extends Exception {

    /**
     * Creates a new instance of <code>UnoccupiedRoomException</code> without
     * detail message.
     */
    public UnoccupiedRoomException() {
    }

    /**
     * Constructs an instance of <code>UnoccupiedRoomException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UnoccupiedRoomException(String msg) {
        super(msg);
    }
}
