package TockTiMan.service;

import TockTiMan.dto.report.BoardReportRequest;
import TockTiMan.dto.report.BoardReportResponse;
import TockTiMan.dto.report.UserReportRequest;
import TockTiMan.dto.report.UserReportResponse;
import TockTiMan.entity.board.Board;
import TockTiMan.entity.user.Authority;
import TockTiMan.entity.user.User;
import TockTiMan.repository.board.BoardRepository;
import TockTiMan.repository.report.BoardReportRepository;
import TockTiMan.repository.report.UserReportRepository;
import TockTiMan.repository.user.UserRepository;
import TockTiMan.service.report.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static TockTiMan.factory.UserFactory.createUser;
import static TockTiMan.factory.UserFactory.createUser2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    @InjectMocks
    ReportService reportService;

    @Mock
    BoardReportRepository boardReportRepository;
    @Mock
    UserReportRepository userReportRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    BoardRepository boardRepository;

    @BeforeEach

    @Test
    @DisplayName("reportUser 서비스 테스트")
    void reportUserTest() {
        // given

        // 매개변수 만들고
        User reporter = createUser();
        User reported = new User("reported", "email","password", "name","nickname",Authority.ROLE_ADMIN);
        UserReportRequest req = new UserReportRequest(1, "content");

        // 로직 내부를 given 으로 처리
        given(userRepository.findById(anyInt())).willReturn(Optional.of(reported));

        // when
        UserReportResponse result = reportService.reportUser(reporter, req);

        // then
        assertThat(result.getReportedUser().getUsername()).isEqualTo("reported");
    }


    @Test
    @DisplayName("reportBoard 서비스 테스트")
    void reportBoardTest() {
        // given
        User reporter = createUser();
        User boardWriter = createUser2();
        reporter.setId(1);
        boardWriter.setId(2);
        Board reported = new Board("title", "content", boardWriter,null, List.of());
        BoardReportRequest req = new BoardReportRequest(1, "content");
        given(boardRepository.findById(anyInt())).willReturn(Optional.of(reported));

        // when
        BoardReportResponse result = reportService.reportBoard(reporter, req);

        // then
        assertThat(result.getContent()).isEqualTo("content");
    }
}
