package com.notepad.error;

/**
* The EmailAlreadyUsedException inherits custom BadRequestAlertException
* with its own error page
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
public class EmailAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public EmailAlreadyUsedException() {
        super(ErrorConstants.EMAIL_ALREADY_USED_TYPE, "Email is already in use!", "userManagement", "emailexists");
    }
}
