package kr.hhplus.be.server.common.exception;

public class CustomException extends RuntimeException {
    private final BaseErrorCode errorCode;

    public CustomException(BaseErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
