package com.notepad.entity.enumeration;

import java.io.Serializable;

public enum UserType implements Serializable {

	VISITOR, REGISTERED;
	
	public String getUserType() {
        return this.name();
    }
	
}
