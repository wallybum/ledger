package app.ledger.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "소셜 사용자 등록 요청 DTO")
public class SocialUserDto {
    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "유효한 이메일 형식이어야 합니다")
    @Schema(description = "사용자 이메일", example = "test@example.com", required = true)
    private String email;

    @NotBlank(message = "이름은 필수입니다")
    @Schema(description = "사용자 이름", example = "홍길동", required = true)
    private String name;

    @Schema(description = "소셜 로그인 제공자 (GOOGLE, APPLE 등)", example = "GOOGLE")
    private String provider;

    @Schema(description = "소셜 제공자의 사용자 고유 식별자", example = "google-12345")
    private String providerId;
}
