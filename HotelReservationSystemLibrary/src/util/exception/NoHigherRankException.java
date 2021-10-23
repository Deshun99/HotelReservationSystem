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
public class NoHigherRankException extends Exception {

    /**
     * Creates a new instance of <code>NoHigherRankException</code> without
     * detail message.
     */
    public NoHigherRankException() {
    }

    /**
     * Constructs an instance of <code>NoHigherRankException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoHigherRankException(String msg) {
        super(msg);
    }
}
