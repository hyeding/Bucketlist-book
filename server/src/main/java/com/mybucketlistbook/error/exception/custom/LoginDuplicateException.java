package com.mybucketlistbook.error.exception.custom;

import com.mybucketlistbook.error.exception.ErrorCode;
import com.mybucketlistbook.error.exception.InvalidValueException;

public class LoginDuplicateException extends InvalidValueException {

    public LoginDuplicateException(String email) {
        super(email, ErrorCode.LOGIN_DUPLICATION);
    }

}