package com.notepad.error;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
* The InvalidPasswordException inherits custom AbstractThrowableProblem
* with its own error page
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
public class InvalidPasswordException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public InvalidPasswordException() {
        super(ErrorConstants.INVALID_PASSWORD_TYPE, "Incorrect password", Status.BAD_REQUEST);
    }
}
