package com.notepad.entity.enumeration;

import java.io.Serializable;

/**
* The UserType enum to define visitor or registered user
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
public enum UserType implements Serializable {

	VISITOR, REGISTERED;
	
	public String getUserType() {
        return this.name();
    }
	
}
