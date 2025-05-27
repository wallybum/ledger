package app.ledger.user.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 권한 유형")
public enum Role {

    @Schema(description = "일반 사용자")
    USER,

    @Schema(description = "관리자")
    ADMIN;

    public String getLabel() {
        return switch (this) {
            case USER -> "일반 사용자";
            case ADMIN -> "관리자";
        };
    }
}
