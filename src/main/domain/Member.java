package TockTiMan.domain;

import TockTiMan.dto.MemberUpdateDto;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String email;

    private String memberName;

    private String phone;

    private String password;

    @ManyToMany
    @JoinTable(
            name = "member_authority",
            joinColumns = {@JoinColumn(name="member_id",referencedColumnName = "member_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_status",referencedColumnName = "authority_status")})
    private Set<Authority> authorities = new HashSet<>();

    @Builder
    public Member(String email, String memberName, String password, String phone, Set<Authority> authorities) {
        this.email = email;
        this.memberName = memberName;
        this.password = password;
        this.phone = phone;
        this.authorities = authorities;
    }

    // 회원 정보 수정
    public void updateMember(MemberUpdateDto dto, PasswordEncoder passwordEncoder) {
        if(dto.getPassword() != null) this.password = passwordEncoder.encode(dto.getPassword());
        if(dto.getMemberName() != null) this.memberName = dto.getMemberName();
        if (dto.getPhone() != null) this.phone = dto.getPhone();
    }
}