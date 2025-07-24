package kr.hhplus.be.server.order.interfaces.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.hhplus.be.server.order.application.dto.request.OrderRequest;
import kr.hhplus.be.server.order.application.dto.response.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Order", description = "주문 관리")
public interface OrderApiSpec {

    @Operation(summary = "주문 결제",
            description = "주문 및 결제를 진행한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponse.class)))
    })
    ResponseEntity<OrderResponse.OrderDetailResponse> order(@RequestBody @Valid OrderRequest.OrderCreateRequest request);
}
