package com.mybucketlistbook.error.exception.custom;

import com.mybucketlistbook.dto.SignUpDto;
import com.mybucketlistbook.error.exception.ErrorCode;
import com.mybucketlistbook.error.exception.InvalidValueException;

public class EmailDuplicateException extends InvalidValueException {

    public EmailDuplicateException(final SignUpDto signUpDto) {
        super(signUpDto.getEmail(), ErrorCode.EMAIL_DUPLICATION);
    }
}
