package com.notepad.error;

/**
* The UsernameAlreadyUsedException inherits custom BadRequestAlertException
* with its own error page
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
public class UsernameAlreadyUsedException extends BadRequestAlertException {

	private static final long serialVersionUID = 1L;

    public UsernameAlreadyUsedException() {
        super(ErrorConstants.USERNAME_ALREADY_USED_TYPE, "Username already used.", "userManagement", "userexists");
    }
}
