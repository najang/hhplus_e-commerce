package kr.hhplus.be.server.interfaces.balance.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "User", description = "유저 관리")
public interface BalanceApiSpec {

    @Operation(summary = "유저 잔액 조회",
            description = "유저의 현재 잔액을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "잔액 조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BalanceResponse.class)))
    })
    ResponseEntity<BalanceResponse> getUserBalance(@RequestParam @Positive Long userId);

    @Operation(summary = "유저 포인트 충전",
            description = "유저의 포인트를 충전한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포인트 충전 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BalanceResponse.class)))
    })
    ResponseEntity<BalanceResponse> charge(@RequestParam @Positive Long id, @RequestBody @Valid BalanceRequest request);
}
