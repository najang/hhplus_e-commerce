package kr.hhplus.be.server.product.application.exception;

import kr.hhplus.be.server.common.exception.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProductErrorCode implements BaseErrorCode {
    PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST, "상품을 찾을 수 없습니다."),
    INVALID_NEGATIVE_PRODUCT_ID(HttpStatus.BAD_REQUEST, "상품식별자는 음수일 수 없습니다."),
    INVALID_NEGATIVE_PRICE(HttpStatus.BAD_REQUEST, "상품 가격은 음수일 수 없습니다."),
    ORDER_DATE_INFORMATION_REQUIRED(HttpStatus.BAD_REQUEST, "주문날짜 정보가 필요합니다."),
    INVALID_NEGATIVE_ORDER_QUANTITY	(HttpStatus.BAD_REQUEST, "주문 수량은 음수일 수 없습니다."),
    PRODUCT_NAME_REQUIRED(HttpStatus.BAD_REQUEST, "상품명을 입력해주세요."),
    STOCK_INFORMATION_REQUIRED(HttpStatus.BAD_REQUEST, "재고 정보가 필요합니다."),
    INVALID_NEGATIVE_STOCK(HttpStatus.BAD_REQUEST, "재고는 음수가 될 수 없습니다."),
    INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "재고가 부족합니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
