package com.edwin.curing.exception;

/**
 * dao顶层异常
 * 
 * @author jinming.wu
 * @date 2014-8-21
 */
public class DAOException extends RuntimeException {

    private static final long serialVersionUID = 2296605860929196393L;

    /**
     * No-args constructor
     */
    public DAOException() {
        super();
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * 
     * @param message
     */
    public DAOException(String message) {
        super(message);
    }

    /**
     * Constructs a new runtime exception with the specified detail message and cause.
     * 
     * @param message
     * @param cause
     */
    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new runtime exception with the specified cause.
     * 
     * @param cause
     */
    public DAOException(Throwable cause) {
        super(cause);
    }
}
