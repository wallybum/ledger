package app.ledger.user.domain.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "사용자 ID", example = "1")
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "사용자 이메일", example = "test@example.com")
    private String email;

    @Schema(description = "사용자 이름", example = "홍길동")
    private String name;

    @Column(nullable = true)
    @Schema(description = "소셜 로그인 제공자", example = "GOOGLE")
    private String provider; // "GOOGLE", "APPLE", null(자체 가입)

    @Column(nullable = true)
    @Schema(description = "소셜 제공자의 사용자 고유 식별자", example = "google-12345")
    private String providerId;// 외부 로그인 사용자의 고유 식별자

    @Enumerated(EnumType.STRING)
    @Schema(description = "사용자 권한", example = "USER")
    private Role role; // "USER", "ADMIN" 등

    @CreatedDate
    @Schema(description = "생성 일시", example = "2024-05-27T10:15:30")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Schema(description = "수정 일시", example = "2024-05-27T10:16:00")
    private LocalDateTime updatedAt;
}