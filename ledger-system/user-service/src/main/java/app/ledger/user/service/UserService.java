package app.ledger.user.service;

import org.springframework.stereotype.Service;

import app.ledger.user.domain.entity.Role;
import app.ledger.user.domain.entity.User;
import app.ledger.user.domain.repository.UserRepository;
import app.ledger.user.dto.SocialUserDto;
import app.ledger.user.dto.UserResponse;
import app.ledger.user.exception.UserAlreadyExistsException;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden // Swagger 문서에서는 숨김 처리
@Service
public class UserService {
    private final UserRepository fUserRepository;

    public UserService(UserRepository userRepository) {
        fUserRepository = userRepository;
    }

    /**
     * 소셜 사용자 등록 처리
     * @param socialUserDto 소셜 사용자 정보
     * @return 저장된 사용자
     * @throws UserAlreadyExistsException 중복 사용자 예외
     */
    public User registerExternal(SocialUserDto socialUserDto) {
        if (fUserRepository.existsByProviderAndProviderId(socialUserDto.getProvider(), socialUserDto.getProviderId())) {
            throw new UserAlreadyExistsException("이미 등록된 사용자입니다");
        }

        User user = User.builder()
                .email(socialUserDto.getEmail())
                .name(socialUserDto.getName())
                .provider(socialUserDto.getProvider())
                .providerId(socialUserDto.getProviderId())
                .role(Role.USER)
                .build();

        return fUserRepository.save(user);

    }

    /**
     * 이메일을 통한 사용자 조회
     * @param email 이메일 주소
     * @return 조회된 사용자
     * @throws IllegalArgumentException 사용자가 존재하지 않을 경우
     */
    public User getUserByEmail(String email) {
        return fUserRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    /**
     * 사용자 Entity를 응답 DTO로 변환
     */
    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }
}
