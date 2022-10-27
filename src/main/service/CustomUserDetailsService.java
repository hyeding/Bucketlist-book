package TockTiMan.service;

import TockTiMan.domain.Authority;
import TockTiMan.domain.Member;
import TockTiMan.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByEmail(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    private UserDetails createUserDetails(Member member) {
        List<SimpleGrantedAuthority> authList = member.getAuthorities()
                .stream()
                .map(Authority::getAuthorityStatus)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        authList .forEach(o-> log.debug("authList -> {}",o.getAuthority()));

        return new User(
                String.valueOf(member.getId()),
                member.getPassword(),
                authList
        );
    }
}