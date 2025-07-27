package kr.hhplus.be.server.coupon.exception;

import kr.hhplus.be.server.common.exception.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CouponErrorCode implements BaseErrorCode {
    COUPON_NAME_REQUIRED(HttpStatus.BAD_REQUEST, "쿠폰명을 입력해주세요."),
    DISCOUNT_TYPE_INFORMATION_REQUIRED(HttpStatus.BAD_REQUEST, "할인 타입 정보가 필요합니다."),
    DISCOUNT_VALUE_MUST_BE_POSITIVE(HttpStatus.BAD_REQUEST, "할인율/금액은 양수여야 합니다."),
    COUPON_VALID_PERIOD_REQUIRED(HttpStatus.BAD_REQUEST, "쿠폰 유효 기간을 입력해주세요."),
    COUPON_QUANTITY_MUST_BE_POSITIVE(HttpStatus.BAD_REQUEST, "쿠폰 수량은 양수여야 합니다."),
    INVALID_NEGATIVE_COUPON_ID(HttpStatus.BAD_REQUEST, "쿠폰식별자는 음수일 수 없습니다."),
    INVALID_NEGATIVE_USER_ID(HttpStatus.BAD_REQUEST, "유저식별자는 음수일 수 없습니다."),
    EXPIRATION_DATETIME_REQUIRED(HttpStatus.BAD_REQUEST, "만료 일시 정보가 필요합니다."),
    COUPON_NOT_USABLE(HttpStatus.BAD_REQUEST, "사용할 수 없는 쿠폰입니다."),
    COUPON_NOT_OWNED_BY_USER(HttpStatus.BAD_REQUEST, "해당 쿠폰을 보유하고 있지 않습니다."),
    INVALID_NEGATIVE_DISCOUNT_AMOUNT(HttpStatus.BAD_REQUEST, "할인 금액은 음수일 수 없습니다."),
    INVALID_DISCOUNT_RATE_RANGE(HttpStatus.BAD_REQUEST, "할인율은 0 이상 100 이하여야 합니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
