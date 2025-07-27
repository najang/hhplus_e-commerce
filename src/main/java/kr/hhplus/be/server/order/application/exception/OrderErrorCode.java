package kr.hhplus.be.server.order.application.exception;

import kr.hhplus.be.server.common.exception.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OrderErrorCode implements BaseErrorCode {
    INVALID_NEGATIVE_USER_ID(HttpStatus.BAD_REQUEST, "유저식별자는 음수일 수 없습니다."),
    ORDER_STATUS_INFORMATION_REQUIRED(HttpStatus.BAD_REQUEST, "주문 상태 정보가 필요합니다."),
    ORDER_PRICE_INFORMATION_REQUIRED(HttpStatus.BAD_REQUEST, "주문 가격 정보가 필요합니다."),
    ORDER_PRODUCT_INFORMATION_REQUIRED(HttpStatus.BAD_REQUEST, "주문 상품 정보가 필요합니다."),
    INVALID_NEGATIVE_TOTAL_PRICE(HttpStatus.BAD_REQUEST, "총 가격은 음수일 수 없습니다."),
    INVALID_NEGATIVE_TOTAL_PRODUCT_PRICE(HttpStatus.BAD_REQUEST, "총 상품 가격은 음수일 수 없습니다."),
    INVALID_NEGATIVE_DISCOUNT_PRICE(HttpStatus.BAD_REQUEST, "할인 가격은 음수일 수 없습니다."),
    INVALID_PRICE_CALCULATION(HttpStatus.BAD_REQUEST, "가격 계산이 올바르지 않습니다."),
    ORDER_INFORMATION_REQUIRED(HttpStatus.BAD_REQUEST, "주문 정보가 필요합니다."),
    PRODUCT_INFORMATION_REQUIRED(HttpStatus.BAD_REQUEST, "상품 정보가 필요합니다."),
    INVALID_NEGATIVE_ORDER_ID(HttpStatus.BAD_REQUEST, "주문식별자는 음수일 수 없습니다."),
    INVALID_NEGATIVE_PRODUCT_ID(HttpStatus.BAD_REQUEST, "상품식별자는 음수일 수 없습니다."),
    PRODUCT_NAME_REQUIRED(HttpStatus.BAD_REQUEST, "상품명을 입력해주세요."),
    INVALID_NEGATIVE_PRICE(HttpStatus.BAD_REQUEST, "상품 가격은 음수일 수 없습니다."),
    ORDER_QUANTITY_MUST_BE_POSITIVE(HttpStatus.BAD_REQUEST, "주문 수량은 양수여야 합니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
