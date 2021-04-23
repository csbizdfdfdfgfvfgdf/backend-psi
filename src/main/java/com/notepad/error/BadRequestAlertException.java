package com.notepad.error;


import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
* The BadRequestAlertException defines custom bad request exeption 
* that sets custom error page to redirect on exeption
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
public class BadRequestAlertException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    private final String entityName;

    private final String errorKey;
    
    @Override
    public Throwable fillInStackTrace() {
		return this;
    }

    public BadRequestAlertException(String defaultMessage, String entityName, String errorKey) {
        this(ErrorConstants.DEFAULT_TYPE, defaultMessage, entityName, errorKey);
    }

    public BadRequestAlertException(URI type, String defaultMessage, String entityName, String errorKey) {
        super(type, defaultMessage, Status.BAD_REQUEST, null, null, null, getAlertParameters(entityName, errorKey));
        this.entityName = entityName;
        this.errorKey = errorKey;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getErrorKey() {
        return errorKey;
    }

    private static Map<String, Object> getAlertParameters(String entityName, String errorKey) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("message", "error." + errorKey);
        parameters.put("params", entityName);
        return parameters;
    }
}
