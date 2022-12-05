package com.mybucketlistbook.error.exception.custom;

import com.mybucketlistbook.error.exception.ErrorCode;
import com.mybucketlistbook.error.exception.InvalidValueException;

public class NicknameDuplicateException extends InvalidValueException {
    public NicknameDuplicateException(final String nickname) {
        super(nickname, ErrorCode.NICKNAME_DUPLICATION);
    }
}
