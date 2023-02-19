//
//  (c) Copyright 2003-2023 Methics Oy. All rights reserved. 
//
package fi.methics.laverca.rest.util;

/**
 * MSS REST API exception
 * <p>Contains an error code and a message
 */
public class MssRestException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public static final String WRONG_PARAM                = "101";
    public static final String MISSING_PARAM              = "102";
    public static final String UNAUTHORIZED_ACCESS        = "104";
    public static final String UNKNOWN_USER               = "105";
    public static final String INVALID_SIGNATURE          = "503";
    public static final String UNABLE_TO_PROVIDE_SERVICES = "780";
    public static final String INTERNAL_ERROR             = "900";

    private final String code;
    
    public MssRestException(String code, String msg) {
        super(msg);
        this.code = code;
    }
    
    public MssRestException(Throwable t) {
        super(t);
        this.code = "900";
    }
    
    public MssRestException(String code, Throwable t) {
        super(t);
        this.code = code;
    }
    
    public String getErrorCode() {
        return this.code;
    }
    
}
