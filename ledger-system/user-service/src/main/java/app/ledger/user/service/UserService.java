package app.ledger.user.service;

import org.springframework.stereotype.Service;

import app.ledger.user.domain.entity.Role;
import app.ledger.user.domain.entity.User;
import app.ledger.user.domain.repository.UserRepository;
import app.ledger.user.dto.SocialUserDto;
import app.ledger.user.dto.UserResponse;
import app.ledger.user.exception.UserAlreadyExistsException;

@Service
public class UserService {
    private final UserRepository fUserRepository;

    public UserService(UserRepository userRepository) {
        fUserRepository = userRepository;
    }

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

    public User getUserByEmail(String email) {
        return fUserRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }
}
