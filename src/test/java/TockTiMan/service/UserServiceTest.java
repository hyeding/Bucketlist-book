package TockTiMan.service;

import TockTiMan.dto.user.UserDto;
import TockTiMan.entity.user.Authority;
import TockTiMan.entity.user.User;
import TockTiMan.exception.MemberNotFoundException;
import TockTiMan.repository.user.UserRepository;
import TockTiMan.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static TockTiMan.factory.UserFactory.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Test
    @DisplayName("findUser() 서비스 테스트")
    void findUserTest() {
        // given
        User user = new User("hyeding", "honghildong@naver.com","1234", "hyeding","hyeding",Authority.ROLE_USER);
        user.setName("hyeding");
        user.setNickname("hyeding");

        given(userRepository.findById(anyInt())).willReturn(Optional.of(user));

        // when
        UserDto result = userService.findUser(1);

        // then
        assertThat(result.getName()).isEqualTo(user.getName());
    }

    @Test
    @DisplayName("MemberNotFoundException 테스트")
    void memberNotFoundExceptionTest() {
        // given
        given(userRepository.findById(any())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> userService.findUser(1)).isInstanceOf(MemberNotFoundException.class);
    }


    @Test
    @DisplayName("deleteUserInfo() 서비스 테스트")
    void deleteUserInfoTest() {
        // given
        given(userRepository.findById(anyInt())).willReturn(Optional.of(createUser()));

        // when
        userService.deleteUserInfo(createUser(), anyInt());

        // then
        verify(userRepository).deleteById(anyInt());

    }
}
