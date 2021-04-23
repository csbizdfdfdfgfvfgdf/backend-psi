package com.notepad.error;

import java.net.URI;

/**
* The ErrorConstants class defines constants to use within the whole app
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
public final class ErrorConstants {

    public static final String ERR_CONCURRENCY_FAILURE = "error.concurrencyFailure";
    public static final String ERR_VALIDATION = "error.validation";
    public static final String PROBLEM_BASE_URL = "/problem";
    public static final URI DEFAULT_TYPE = URI.create(PROBLEM_BASE_URL + "/problem-with-message");
    public static final URI CONSTRAINT_VIOLATION_TYPE = URI.create(PROBLEM_BASE_URL + "/constraint-violation");
    public static final URI INVALID_PASSWORD_TYPE = URI.create(PROBLEM_BASE_URL + "/invalid-password");
    public static final URI EMAIL_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/email-already-used");
    public static final URI USERNAME_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/username-already-used");

    private ErrorConstants() {
    	
    }
}
