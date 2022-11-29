package TockTiMan.service;

import TockTiMan.config.jwt.TokenProvider;
import TockTiMan.dto.sign.SignUpRequestDto;
import TockTiMan.repository.refreshToken.RefreshTokenRepository;
import TockTiMan.repository.user.UserRepository;
import TockTiMan.service.auth.AuthService;
import TockTiMan.service.redis.RedisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    AuthService authService;

    @Mock
    AuthenticationManagerBuilder authenticationManagerBuilder;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    TokenProvider tokenProvider;

    @Mock
    RefreshTokenRepository refreshTokenRepository;

    @Mock
    RedisService redisService;

    @BeforeEach
    void beforeEach() {
        authService = new AuthService(authenticationManagerBuilder, userRepository, passwordEncoder, tokenProvider, refreshTokenRepository, redisService);
    }

    @Test
    @DisplayName("signup 서비스 테스트")
    void signupTest() {
        // given
        SignUpRequestDto req = new SignUpRequestDto("username", "1234", "name", "nickname", "honghildong@naver.com");

        // when
        authService.signup(req);

        // then
        verify(passwordEncoder).encode(req.getPassword());
        verify(userRepository).save(any());
    }


//    @Test
//    @DisplayName("signIn 서비스 테스트")
//    void signInTest() {
//        // given
//        LoginRequestDto req = new LoginRequestDto("username", "password");
//        UsernamePasswordAuthenticationToken authenticationToken = req.toAuthentication();
//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
//
//        given(userRepository.findByUsername(any())).willReturn(Optional.of(createUserWithAdminRole()));
//        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
//        given(tokenProvider.generateTokenDto(any())).willReturn(tokenDto);
//
//        // when
//        TokenResponseDto res = authService.signIn(req);
//
//        // then
//        assertThat(res.getAccessToken()).isEqualTo("access");
//        assertThat(res.getRefreshToken()).isEqualTo("refresh");
//
//    }
}