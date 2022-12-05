package com.mybucketlistbook.error.exception.custom;

import com.mybucketlistbook.error.exception.ErrorCode;
import com.mybucketlistbook.error.exception.InvalidValueException;

public class AuthEmailSendFailException extends InvalidValueException {

    public AuthEmailSendFailException(final String email) {
        super("To "+email+", auth email send fail ", ErrorCode.AUTH_EMAIL_SEND_FAIL);
    }
}
