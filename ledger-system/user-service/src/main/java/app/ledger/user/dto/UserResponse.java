package app.ledger.user.dto;

import app.ledger.user.domain.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "사용자 응답 DTO")
public class UserResponse {
    @Schema(description = "사용자 이메일", example = "test@example.com")
    private String email;
    @Schema(description = "사용자 이름", example = "홍길동")
    private String name;
    @Schema(description = "사용자 권한", example = "USER")
    private Role role;
}
