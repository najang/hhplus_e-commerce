package kr.hhplus.be.server.balance.application.exception;

import kr.hhplus.be.server.common.exception.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BalanceErrorCode implements BaseErrorCode {
    INVALID_NEGATIVE_AMOUNT(HttpStatus.BAD_REQUEST, "금액은 음수일 수 없습니다."),
    INVALID_NEGATIVE_USER_ID(HttpStatus.BAD_REQUEST, "유저식별자는 음수일 수 없습니다."),
    BALANCE_INFORMATION_REQUIRED(HttpStatus.BAD_REQUEST, "잔액 정보가 필요합니다."),
    RECHARGE_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "최대 한도를 초과하여 충전할 수 없습니다."),
    INSUFFICIENT_BALANCE(HttpStatus.BAD_REQUEST, "잔액이 부족합니다."),
    INVALID_NEGATIVE_POINT_ID(HttpStatus.BAD_REQUEST, "포인트식별자는 음수일 수 없습니다."),
    AMOUNT_INFORMATION_REQUIRED(HttpStatus.BAD_REQUEST, "금액 정보가 필요합니다."),
    TRANSACTION_TYPE_INFORMATION_REQUIRED(HttpStatus.BAD_REQUEST, "거래 타입 정보가 필요합니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
