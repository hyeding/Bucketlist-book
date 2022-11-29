package TockTiMan.service;

import TockTiMan.dto.board.BoardSimpleDto;
import TockTiMan.dto.user.UserDto;
import TockTiMan.entity.board.Board;
import TockTiMan.entity.user.User;
import TockTiMan.repository.board.BoardRepository;
import TockTiMan.repository.report.BoardReportRepository;
import TockTiMan.repository.report.UserReportRepository;
import TockTiMan.repository.user.UserRepository;
import TockTiMan.service.admin.AdminService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static TockTiMan.factory.BoardFactory.createBoard;
import static TockTiMan.factory.UserFactory.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {
    @InjectMocks
    AdminService adminService;

    @Mock
    UserRepository userRepository;
    @Mock
    BoardRepository boardRepository;
    @Mock
    UserReportRepository userReportRepository;
    @Mock
    BoardReportRepository boardReportRepository;


    @Test
    @DisplayName("manageReportedUser 서비스 테스트")
    void manageReportedUserTest() {
        // given
        List<User> users = new ArrayList<>();
        users.add(createUser());
        given(userRepository.findByReportedIsTrue()).willReturn(users);

        // when
        List<UserDto> result = adminService.manageReportedUser();

        // then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("unlockUser 서비스 테스트")
    void unlockUserTest() {
        // given
        User user = createUser();
        user.setReported(true);
        given(userRepository.findById(anyInt())).willReturn(Optional.of(user));

        // when
        UserDto result = adminService.unlockUser(anyInt());

        // then
        verify(userReportRepository).deleteAllByReportedUserId(anyInt());
    }

    @Test
    @DisplayName("manageReportedBoards 서비스 테스트")
    void manageReportedBoardsTest() {
        // given
        List<Board> boards = new ArrayList<>();
        boards.add(createBoard());
        given(boardRepository.findByReportedIsTrue()).willReturn(boards);

        // when
        List<BoardSimpleDto> result = adminService.manageReportedBoards();

        // then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("unlockBoard 서비스 테스트")
    void unlockBoardTest() {
        // given
        Board board = createBoard();
        board.setReported(true);
        given(boardRepository.findById(anyInt())).willReturn(Optional.of(board));

        // when
        BoardSimpleDto result = adminService.unlockBoard(anyInt());

        // then
        assertThat(result.getTitle()).isEqualTo("title");
    }
}
