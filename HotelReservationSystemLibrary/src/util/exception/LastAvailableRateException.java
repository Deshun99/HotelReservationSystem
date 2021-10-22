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
public class LastAvailableRateException extends Exception {

    /**
     * Creates a new instance of <code>LastAvailableRateException</code> without
     * detail message.
     */
    public LastAvailableRateException() {
    }

    /**
     * Constructs an instance of <code>LastAvailableRateException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public LastAvailableRateException(String msg) {
        super(msg);
    }
}
