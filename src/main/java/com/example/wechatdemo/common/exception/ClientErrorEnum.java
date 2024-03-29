package com.example.wechatdemo.common.exception;

import lombok.Getter;

@Getter
public enum ClientErrorEnum {

    TokenError(401,"token error"),
    UserNotExist(404,"user not exist"),
    UserExist(409,"user exist"),
    AlreadyFriend(409,"you are already friends"),

    Blocked(409, "you are blocked"),
    ApplicationNotExist(409, "application is miss, please refresh and try again"),

    AlreadyReplied(409, "you have already replied"),
    ParamError(409, "param not legal"),
    RelationshipNotExist(409, "you are not friends"),
    DataNotExist(409, "data not exist");

    private final int code;
    private final String message;

    ClientErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
