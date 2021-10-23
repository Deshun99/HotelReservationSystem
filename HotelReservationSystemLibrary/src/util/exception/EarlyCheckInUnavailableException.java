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
public class EarlyCheckInUnavailableException extends Exception {

    /**
     * Creates a new instance of <code>EarlyCheckInUnavailableException</code>
     * without detail message.
     */
    public EarlyCheckInUnavailableException() {
    }

    /**
     * Constructs an instance of <code>EarlyCheckInUnavailableException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public EarlyCheckInUnavailableException(String msg) {
        super(msg);
    }
}
