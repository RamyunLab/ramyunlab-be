
package ramyunlab_be.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Timestamp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ramyunlab_be.dto.KakaoUserInfoDTO;
import ramyunlab_be.dto.UserDTO;
import ramyunlab_be.entity.UserEntity;
import ramyunlab_be.repository.UserRepository;

@Service
@Slf4j
public class UserService {

    @Value("${kakao.client-id}")
    private String CLIENT_ID; // 카카오 앱 REST API 키

    @Value("${kakao.redirect-uri}")
    private String REDIRECT_URI ; // 서버의 콜백 URI

    @Value("${kakao.token-request-url}")
    private String TOKEN_REQUEST_URL;  // 토큰 정보 가져오는 URL

    @Value("${kakao.user-info-url}")
    private String USER_INFO_URL;    // 유저정보 가져오는 URL

    final private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity create(final UserEntity userEntity) {

        checkId(userEntity);
        checkNickname(userEntity);


        return userRepository.save(userEntity);
    }

    public UserEntity getByCredentials(final String userId, final String password) {
        UserEntity user = userRepository.findByUserId(userId);
        Timestamp userDeletedAt = userRepository.findUserDeletedAtByUserId(userId);
        log.warn("user create service {}, {}, {}, {}", userId, user.getUserId(), password, user.getPassword());

        if (userDeletedAt != null){
            throw new RuntimeException("탈퇴한 회원입니다.");
        }else if(user != null && passwordEncoder.matches(password, user.getPassword())){
            return UserEntity.builder().userId(userId).userIdx(user.getUserIdx()).build();
        }else if(user == null){
            throw new RuntimeException("존재하지 않는 아이디입니다.");
        }else if(!passwordEncoder.matches(password, user.getPassword())){
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        }
        return null;
    }

    public UserDTO checkId(UserEntity userEntity) {

        final String userId = userEntity.getUserId();

        // 아이디 중복 체크\
        if (userRepository.existsByUserId(userId)) {
            log.warn("이미 존재하는 아이디입니다. {}", userId);
            throw new RuntimeException("userId already exists");
        }
        return UserDTO.builder().userId(userId).build();

    }

    public UserDTO checkNickname(UserEntity userEntity) {

        final String nickname = userEntity.getNickname();

        // 닉네임 중복 체크
        if (userRepository.existsByNickname(nickname)) {
            log.warn("이미 존재하는 닉네임입니다. {}", nickname);
            throw new RuntimeException("nickname already exists");
        }

        return UserDTO.builder().nickname(nickname).build();
    }

    public UserEntity delete(final Long userIdx, final String password) {
        UserEntity user = userRepository.findByUserIdx(userIdx)
                                        .orElseThrow(() -> new RuntimeException("로그인을 해주세요."));


        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            userRepository.delete(user);
            return user;
        } else if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        } else throw new RuntimeException("회원탈퇴 실패");
    }

    // 닉네임 변경
    public UserEntity updateNickname(String userIdx, UserDTO userDTO) {
        UserEntity user = userRepository.findByUserIdx(Long.valueOf(userIdx))
                                        .orElseThrow(() -> new RuntimeException("Error shit;;"));
        user = user.toBuilder()
                   .nickname(userDTO.getNickname())
                   .build();
        return userRepository.save(user);
    }

    // 비밀번호 변경 시 비밀번호 확인.
    public boolean confirmPassword(String userIdx, UserDTO userDTO) {
        UserEntity user = userRepository.findByUserIdx(Long.valueOf(userIdx))
                                        .orElseThrow(() -> new RuntimeException("회원 정보 없음."));

        boolean isMatched = passwordEncoder.matches(userDTO.getPassword(), user.getPassword());

        return isMatched;
    }

    // 비밀번호 변경
    public UserEntity updatePassword(String userIdx, UserDTO userDTO) {
        UserEntity user = userRepository.findByUserIdx(Long.valueOf(userIdx))
                                        .orElseThrow(() -> new RuntimeException("회원 정보 없음."));

        user = user.toBuilder()
                   .password(passwordEncoder.encode(userDTO.getPassword()))
                   .build();
        return userRepository.save(user);
    }

    // 소셜 로그인
    public String getKakaoAuthUrl() {
        return "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + CLIENT_ID + "&redirect_uri=" + REDIRECT_URI;
    }

    public UserEntity handleKakaoCallback(String code) throws Exception {
        // 액세스 토큰 발급
        String accessToken = getAccessToken(code);
        log.info("access 토큰!! {}", accessToken);

        // 사용자 정보 요청
        JsonNode userInfo = getUserInfo(accessToken);
        String kakaoId = userInfo.get("id").asText();
        String email = userInfo.get("kakao_account").get("email").asText();
        String nickname = userInfo.get("kakao_account").get("profile").get("nickname").asText();

        // DTO 객체생성 및 데이터 설정
        KakaoUserInfoDTO kakaoUserInfoDTO = KakaoUserInfoDTO.builder()
                                                            .kakaoId(kakaoId)
                                                            .accessToken(accessToken)
                                                            .kakaoName(nickname)
                                                            .build();

        // 사용자 정보를 DB에 저장 또는 업데이트
        return processUserLogin(kakaoUserInfoDTO, email);
    }

    private UserEntity processUserLogin(KakaoUserInfoDTO kakaoUserInfoDTO, String email) {
        String userId = "kakao_" + KakaoUserInfoDTO.extractUserId(email);
        UserEntity user = userRepository.findByUserId(userId);

        if (user == null) {
            user = UserEntity.builder()
                             .userId(userId)
                             .nickname(kakaoUserInfoDTO.getKakaoName())
                             .isAdmin(false)
                             .userDeletedAt(null)
                             .build();

            userRepository.save(user);
        }

        return user;
    }

    private String getAccessToken(String code) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "grant_type=authorization_code"
                      + "&client_id=" + CLIENT_ID
                      + "&redirect_uri=" + REDIRECT_URI
                      + "&code=" + code;

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(TOKEN_REQUEST_URL, HttpMethod.POST, request, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.getBody());

        return jsonNode.get("access_token").asText();
    }

    private JsonNode getUserInfo(String accessToken) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(USER_INFO_URL, HttpMethod.GET, request, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(response.getBody());
    }
}
