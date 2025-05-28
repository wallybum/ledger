package app.ledger.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.ledger.user.domain.entity.User;
import app.ledger.user.domain.repository.UserRepository;
import app.ledger.user.dto.SocialUserDto;

/**
 * 실제 애플리케이션을 띄워서 Controller, Service, Repository 흐름을 통합적으로 검증하는 테스트
 */
@ActiveProfiles("test")
@SpringBootTest // Spring Context 전체 로딩 (Controller, Service, Repository 등 실제 환경과 유사)
@AutoConfigureMockMvc // MockMvc 자동 구성 (HTTP 요청/응답 시뮬레이션 가능)
public class UserIntegrationTest {
    @Autowired
    private MockMvc mockMvc; // 실제 HTTP 요청을 시뮬레이션할 수 있는 객체

    @Autowired
    private ObjectMapper objectMapper; // Java 객체를 JSON 문자열로 변환할 때 사용

    @Autowired
    private UserRepository fUserRepository; // 실제 DB에 접근하기 위한 JPA Repository

    @BeforeEach
    void setUp() {
        // 각 테스트 실행 전 DB 초기화
        fUserRepository.deleteAll();
    }

    @Test
    @DisplayName("POST /users/external/register - 소셜 사용자를 등록할 수 있다")
    void registerSocialUser() throws Exception {
        // given: SocialUserDto로 요청 생성
        var request = new SocialUserDto();
        request.setEmail("test@example.com");
        request.setName("홍길동");
        request.setProvider("google");
        request.setProviderId("google-123");

        // when & then
        mockMvc.perform(post("/users/external/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.name").value("홍길동"));
    }

    // @Test
    // @DisplayName("POST /users - 사용자를 생성할 수 있다")
    // void createUser() throws Exception {
    // // given: 테스트에 사용할 사용자 객체
    // User user = new User();
    // user.setEmail("test@example.com");
    // user.setName("홍길동");
    // user.setProvider("google");
    // user.setProviderId("google-123");

    // // when & then: /users 엔드포인트로 POST 요청을 보내고 상태 200과 JSON 필드 확인
    // mockMvc.perform(post("/users")
    // .contentType(MediaType.APPLICATION_JSON)
    // .content(objectMapper.writeValueAsString(user)))
    // .andExpect(status().isOk())
    // .andExpect(jsonPath("$.email").value("test@example.com"))
    // .andExpect(jsonPath("$.name").value("홍길동"));
    // }

    @Test
    @DisplayName("GET /users/email/{email} - 이메일로 사용자를 조회할 수 있다")
    void getUserByEmail() throws Exception {
        // given: 사용자 1명 저장
        User user = new User();
        user.setEmail("tester@example.com");
        user.setName("테스터");
        user.setProvider("naver");
        user.setProviderId("naver-123");
        fUserRepository.save(user);

        // when & then: GET 요청으로 조회 후 응답 JSON 검증
        mockMvc.perform(get("/users/email/{email}", "tester@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("tester@example.com"))
                .andExpect(jsonPath("$.name").value("테스터"));
    }

    // 추후 구현
    // @Test
    // @DisplayName("GET /users - 모든 사용자를 조회할 수 있다")
    // void getAllUsers() throws Exception {
    // // given: 사용자 1명 저장
    // User user = new User();
    // user.setEmail("tester@example.com");
    // user.setName("테스터");
    // user.setProvider("naver");
    // user.setProviderId("naver-123");
    // fUserRepository.save(user);

    // // when & then: /users 엔드포인트로 GET 요청 → JSON 배열 포함 확인
    // mockMvc.perform(get("/users"))
    // .andExpect(status().isOk())
    // .andExpect(jsonPath("$[0].email").value("tester@example.com"));
    // }

    @Test
    @DisplayName("Repository 직접 검증도 가능")
    void repositoryTest() {
        // given
        User user = new User();
        user.setEmail("repo@test.com");
        user.setName("레포유저");
        fUserRepository.save(user);

        // then
        assertThat(fUserRepository.findAll()).hasSize(1);
    }

}
