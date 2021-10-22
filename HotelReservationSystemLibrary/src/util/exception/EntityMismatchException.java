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
public class EntityMismatchException extends Exception{

    /**
     * Creates a new instance of <code>EntityMismatchException</code> without
     * detail message.
     */
    public EntityMismatchException() {
    }

    /**
     * Constructs an instance of <code>EntityMismatchException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public EntityMismatchException(String msg) {
        super(msg);
    }
}
