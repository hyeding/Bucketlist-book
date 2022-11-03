package TockTiMan.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;

import static TockTiMan.domain.AuthorityEnum.ROLE_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Redis CRUD Test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberTest {
    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member("asdf@naver.com", "홍길동", "1234", "010-0000-0000", ROLE_USER);
    }

    @AfterEach
    void teardown() {
        memberRepository.deleteById(member.getId());
    }

    @Test
    @DisplayName("Redis 에 데이터를 저장하면 정상적으로 조회되어야 한다")
    void redis_save_test() {
        // given
        memberRepository.save(member);

        // when
        Member persistMember = memberRepository.findById(member.getId())
                .orElseThrow(RuntimeException::new);

        // then
        assertThat(persistMember.getId()).isEqualTo(member.getId());
        assertThat(persistMember.getEmail()).isEqualTo(member.getEmail());
        assertThat(persistMember.getMemberName()).isEqualTo(member.getMemberName());
        assertThat(persistMember.getPassword()).isEqualTo(member.getPassword());
    }

    @Test
    @DisplayName("Redis 에 데이터를 수정하면 정상적으로 수정되어야 한다")
    void redis_update_test() {
        // given
        memberRepository.save(member);
        Member persistMember = memberRepository.findById(member.getId())
                .orElseThrow(RuntimeException::new);

        // when
        persistMember.changeEmail("asdf1234@naver.com");
        memberRepository.save(persistMember);

        // then
        assertThat(persistMember.getEmail()).isEqualTo("asdf1234@naver.com");
    }

    @Test
    @DisplayName("Redis 에 데이터를 삭제하면 정상적으로 삭제되어야 한다")
    void redis_delete_test() {
        // given
        memberRepository.save(member);

        // when
        memberRepository.delete(member);
        Optional<Member> deletedProduct = memberRepository.findById(member.getId());

        // then
        assertTrue(deletedProduct.isEmpty());
    }
}
