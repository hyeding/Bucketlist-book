package com.mybucketlistbook.error.exception.custom;

import com.mybucketlistbook.error.exception.ErrorCode;
import com.mybucketlistbook.error.exception.InvalidValueException;

public class CurrentPasswordNotMatchException extends InvalidValueException {

    public CurrentPasswordNotMatchException(String email) {
        super(email, ErrorCode.CURRENT_PASSWORD_NOT_MATCH_EXCEPTION);
    }
}