package com.notepad.error;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class TokenRefreshException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public TokenRefreshException() {
        super(ErrorConstants.INVALID_REFRESH_TOKEN_TYPE, "Refresh token is invalid", Status.BAD_REQUEST);
    }
}
