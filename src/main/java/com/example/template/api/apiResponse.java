package com.example.template.api;

public class apiResponse<T> {

        private int code;
        private String message;
        private T data;

        public apiResponse(int code, String message, T data) {
            this.code = code;
            this.message = message;
            this.data = data;
        }

        public static <T> apiResponse<T> success(T data) {
            return new apiResponse<>(200, "성공", data);
        }

        public static <T> apiResponse<T> error(String message, int code) {
            return new apiResponse<>(code, message, null);
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public T getData() {
            return data;
        }


}
