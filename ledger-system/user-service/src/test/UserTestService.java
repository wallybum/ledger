package app.ledger.user.service;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import app.ledger.user.domain.repository.UserRepository;

public class UserTestService {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

     @Test
    void registerExternal_shouldSaveNewUser() {
        // given
        SocialUserDto dto = new SocialUserDto();
        dto.setEmail("test@example.com");
        dto.setName("Test User");
        dto.setProvider("GOOGLE");
        dto.setProviderId("google-123");

        when(userRepository.existsByProviderAndProviderId(dto.getProvider(), dto.getProviderId())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        User user = userService.registerExternal(dto);

        // then
        assertEquals("test@example.com", user.getEmail());
        assertEquals("Test User", user.getName());
        assertEquals("GOOGLE", user.getProvider());
        assertEquals("google-123", user.getProviderId());
        assertEquals(Role.USER, user.getRole());
    }

    @Test
    void registerExternal_shouldThrowExceptionWhenUserExists() {
        // given
        SocialUserDto dto = new SocialUserDto();
        dto.setProvider("GOOGLE");
        dto.setProviderId("google-123");

        when(userRepository.existsByProviderAndProviderId(dto.getProvider(), dto.getProviderId())).thenReturn(true);

        // when & then
        assertThrows(UserAlreadyExistsException.class, () -> userService.registerExternal(dto));
    }

    @Test
    void getUserByEmail_shouldReturnUser() {
        // given
        String email = "test@example.com";
        User user = User.builder().email(email).build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // when
        User result = userService.getUserByEmail(email);

        // then
        assertEquals(email, result.getEmail());
    }

    @Test
    void getUserByEmail_shouldThrowExceptionWhenNotFound() {
        // given
        String email = "notfound@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when & then
        assertThrows(IllegalArgumentException.class, () -> userService.getUserByEmail(email));
    }
}
