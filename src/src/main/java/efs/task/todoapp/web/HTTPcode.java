package efs.task.todoapp.web;

public enum HTTPcode {
    OK (200),
    CREATED (201),
    BAD_REQUEST (400),
    UNAUTHORIZED (401),
    FORBIDDEN (403),
    NOT_FOUND (404),
    CONFLICT (409);

    private final int code;

    HTTPcode(int status) {
        this.code = status;
    }
    public int getValue() {
        return code;
    }
}
