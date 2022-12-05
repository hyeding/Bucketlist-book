package com.mybucketlistbook.error.exception.custom;

import com.mybucketlistbook.error.exception.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException(String email) {
        super(email + " 유저를 찾을 수 없습니다.");
    }
}
