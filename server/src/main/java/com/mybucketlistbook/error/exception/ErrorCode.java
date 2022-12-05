package com.mybucketlistbook.error.exception;

import com.fasterxml.jackson.annotation.JsonFormat;


@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "C001", "잘못된 입력 값입니다."),
    METHOD_NOT_ALLOWED(405, "C002", "잘못된 입력 값입니다."),
    ENTITY_NOT_FOUND(404, "C003", "엔티티를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(500, "C004", "서버 에러"),
    INVALID_TYPE_VALUE(400, "C005", " 잘못된 유형의 값입니다."),
    HANDLE_ACCESS_DENIED(403, "C006", "접근이 거절되었습니다."),
    UNAUTHORIZED(401, "C007", "권한이 없습니다."),


    // User
    EMAIL_DUPLICATION(409, "U001", "중복된 이메일 입니다."),
    AUTH_EMAIL_SEND_FAIL(500, "U002", "인증 이메일 전송 실패"),
    NICKNAME_DUPLICATION(409, "U003", "중복된 닉네임 입니다."),
    CURRENT_PASSWORD_NOT_MATCH_EXCEPTION(400, "U004", "현재 비밀번호가 일치하지 않습니다."),
    LOGIN_DUPLICATION(409, "U005", "현재 사용자가 로그인되어 있습니다. 이 계정에 로그인할 수 없습니다.");

//
//    // Room
//    ROOM_IS_NOT_AVAILABLE(409, "R001", "Room Status is Not Available"),
//    ROOM_IS_FULL(409, "R001", "Room is Full"),
//    ROOM_IS_ON_GAME(409, "R002", "Room is on Game"),
//    ROOM_IS_OFFLINE(404, "R003", "Room is OFF"),
//    ROOM_PASSWORD_NOT_MATCH(400, "R004", "Room password is Not Match")
//    ;


    private final String code;
    private final String message;
    private int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }

}