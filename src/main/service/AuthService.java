package TockTiMan.service;

import TockTiMan.domain.*;
import TockTiMan.dto.MemberRequestDto;
import TockTiMan.dto.MemberResponseDto;
import TockTiMan.dto.TokenDto;
import TockTiMan.dto.TokenRequestDto;
import TockTiMan.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AuthorityRepository authorityRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * 회원가입
     */
    @Transactional
    public MemberResponseDto signup(MemberRequestDto memberRequestDto) {
        // 이메일 중복 회원 검증
        if (memberRepository.existsByEmail(memberRequestDto.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 이메일입니다.");
        }

        Authority authority = authorityRepository
                .findByAuthorityStatus(AuthorityEnum.ROLE_USER).orElseThrow(()->new RuntimeException("권한 정보가 없습니다."));

        Set<Authority> set = new HashSet<>();
        set.add(authority);

        Member member = memberRequestDto.toMember(passwordEncoder, set);
        return MemberResponseDto.of(memberRepository.save(member));
    }
    /**
     * 로그인
     */
    @Transactional
    public TokenDto login(MemberRequestDto memberRequestDto) {
        // Dto의 email, password를 받고 UsernamePasswordAuthenticationToken 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.toAuthentication();

        // authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // refreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        return tokenDto;
    }

    /**
     * 재발급
     */
    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        // refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // access Token에서 Authentication객체 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // DB에서 member_id를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // refresh Token이 다르면
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // refreshToken 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return tokenDto;
    }
}