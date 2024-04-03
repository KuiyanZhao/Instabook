package com.instabook.common.model;

import lombok.Data;

/**
 * format return items
 * @param <T>
 */
@Data
public class R<T> {
    private T data;

    private int code;

    private String msg;

    public R(T data, int code, String msg) {
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    public static <T> R<T> success(T data) {
        return new R<>(data, 200, null);
    }

    public static <T> R<T> error(String message) {
        return new R<>(null, 500, message);
    }

    public static <T> R<T> error(int code, String message) {
        return new R<>(null, code, message);
    }
}
