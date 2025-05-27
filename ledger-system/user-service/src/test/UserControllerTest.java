package app.ledger.user.service;

/**
 * UserController에 대한 단위 테스트
 * 실제 서비스 로직(UserService)은 Mock 처리하고,
 * Controller 계층의 요청-응답 흐름 및 JSON 직렬화/역직렬화 검증
 */
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc; // HTTP 요청을 시뮬레이션 하기 위한 객체

    @Autowired
    private ObjectMapper objectMapper; // DTO를 JSON 문자열로 변환하기 위한 도구

    @MockBean
    private UserService userService; // 실제 빈 대신 Mock으로 대체

    private SocialUserDto socialUserDto;

    @BeforeEach
    void setUp() {
        socialUserDto = new SocialUserDto();
        socialUserDto.setEmail("test@example.com");
        socialUserDto.setName("Test User");
        socialUserDto.setProvider("GOOGLE");
        socialUserDto.setProviderId("google-123");
    }

    /**
     * [POST] /users/external/register 테스트
     * SocialUserDto를 JSON으로 보내면 UserResponse로 반환되어야 함
     */
    @Test
    void registerExternalUser_shouldReturnUserResponse() throws Exception {
        User mockUser = User.builder()
                .email(socialUserDto.getEmail())
                .name(socialUserDto.getName())
                .provider(socialUserDto.getProvider())
                .providerId(socialUserDto.getProviderId())
                .role(Role.USER)
                .build();

        Mockito.when(userService.registerExternal(any())).thenReturn(mockUser);
        Mockito.when(userService.toResponse(any())).thenCallRealMethod();

        mockMvc.perform(post("/users/external/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(socialUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    /**
     * [POST] /users/external/register 테스트 - 중복 소셜 사용자 등록 시 예외 처리 검증
     */
    @Test
    void registerExternalUser_shouldReturnConflict_whenUserAlreadyExists() throws Exception {
        Mockito.when(userService.registerExternal(any()))
                .thenThrow(new UserAlreadyExistsException("이미 등록된 사용자입니다"));

        mockMvc.perform(post("/users/external/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(socialUserDto)))
                .andExpect(status().isConflict())
                .andExpect(content().string("이미 등록된 사용자입니다"));
    }

    /**
     * [POST] /users/external/register 테스트 - 입력값 유효성 검증 실패 시 응답 확인
     */
    @Test
    void registerExternalUser_shouldReturnBadRequest_whenEmailMissing() throws Exception {
        socialUserDto.setEmail("");

        mockMvc.perform(post("/users/external/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(socialUserDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("email")));
    }

    /**
     * [GET] /users/email/{email} 테스트 - 이메일로 사용자 조회 성공 시 응답 확인
     */
    @Test
    void getUserByEmail_shouldReturnUserResponse() throws Exception {
        User mockUser = User.builder()
                .email("test@example.com")
                .name("Test User")
                .role(Role.USER)
                .build();

        Mockito.when(userService.getUserByEmail("test@example.com")).thenReturn(mockUser);
        Mockito.when(userService.toResponse(any())).thenCallRealMethod();

        mockMvc.perform(get("/users/email/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.role").value("USER"));
    }
}
