package com.artmark.response;

/**
 * @author Ushmodin N.
 * @since 04.01.2016
 */
public class Response<T> {
    public static class Error {
        private String code;
        private String message;

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }

    private boolean success;
    private T body;
    private Error error;

    public static <T> Response<T> success(T body) {
        Response<T> response = new Response<T>();
        response.body = body;
        response.success = true;
        return response;
    }

    public static <T> Response<T> ok() {
        Response<T> response = new Response<T>();
        response.success = true;
        return response;
    }

    public static <T> Response<T> error(String code, String message) {
        Response<T> response = new Response<T>();
        response.success = false;
        response.error = new Error();
        response.error.code = code;
        response.error.message = message;
        return response;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getBody() {
        return body;
    }

    public Error getError() {
        return error;
    }

}
