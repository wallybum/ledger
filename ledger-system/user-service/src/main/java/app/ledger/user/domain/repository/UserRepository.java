package app.ledger.user.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import app.ledger.user.domain.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    /* 회원가입 시 중복 체크 용 */
    boolean existsByEmail(String email);

    /* 소셜 로그인 중복 체크 용 */
    boolean existsByProviderAndProviderId(String provider, String providerId);

    /* 이메일 기반 사용자 조회 용 */
    Optional<User> findByEmail(String email);

}