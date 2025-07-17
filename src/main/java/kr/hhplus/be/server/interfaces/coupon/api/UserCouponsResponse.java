package kr.hhplus.be.server.interfaces.coupon.api;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserCouponsResponse {

    private Long id;
    private String name;
    private String expiredAt;
    private boolean usedStatus;

    @Builder
    private UserCouponsResponse(Long id, String name, String expiredAt, boolean usedStatus) {
        this.id = id;
        this.name = name;
        this.expiredAt = expiredAt;
        this.usedStatus = usedStatus;
    }

    public static UserCouponsResponse of(Long id, String name, String expiredAt, boolean usedStatus) {
        return new UserCouponsResponse(id, name, expiredAt, usedStatus);
    }
}