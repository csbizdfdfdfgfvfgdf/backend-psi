package com.notepad.error;

public class UsernameAlreadyUsedException extends BadRequestAlertException {

	private static final long serialVersionUID = 1L;

    public UsernameAlreadyUsedException() {
        super(ErrorConstants.USERNAME_ALREADY_USED_TYPE, "Username already used.", "userManagement", "userexists");
    }
}
